/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agent.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.SearchBehavior;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.invocation.LangChain4jManaged;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ToolSpecifications {
    private static final Type MAP_TYPE = new ParameterizedType(){

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{String.class, Object.class};
        }

        @Override
        public Type getRawType() {
            return Map.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    };

    private ToolSpecifications() {
    }

    public static List<ToolSpecification> toolSpecificationsFrom(Class<?> classWithTools) {
        List<ToolSpecification> toolSpecifications = Utils.allConcreteMethods(classWithTools).stream().filter(method -> method.isAnnotationPresent(Tool.class)).map(ToolSpecifications::toolSpecificationFrom).collect(Collectors.toList());
        ToolSpecifications.validateSpecifications(toolSpecifications);
        return toolSpecifications;
    }

    public static List<ToolSpecification> toolSpecificationsFrom(Object objectWithTools) {
        return ToolSpecifications.toolSpecificationsFrom(objectWithTools.getClass());
    }

    public static void validateSpecifications(List<ToolSpecification> toolSpecifications) throws IllegalArgumentException {
        HashSet<String> names = new HashSet<String>();
        for (ToolSpecification toolSpecification : toolSpecifications) {
            if (names.add(toolSpecification.name())) continue;
            throw new IllegalArgumentException(String.format("Tool names must be unique. The tool '%s' appears several times", toolSpecification.name()));
        }
    }

    public static ToolSpecification toolSpecificationFrom(Method method) {
        Tool tool = method.getAnnotation(Tool.class);
        return ToolSpecification.builder().name(ToolSpecifications.getName(tool, method)).description(ToolSpecifications.getDescription(tool)).parameters(ToolSpecifications.parametersFrom(method.getParameters())).metadata(ToolSpecifications.getMetadata(tool)).build();
    }

    private static String getName(Tool tool, Method method) {
        return Utils.isNullOrBlank(tool.name()) ? method.getName() : tool.name();
    }

    private static String getDescription(Tool tool) {
        String description = String.join((CharSequence)"\n", tool.value());
        return description.isEmpty() ? null : description;
    }

    private static Map<String, Object> getMetadata(Tool annotation) {
        Map metadata = (Map)Json.fromJson(annotation.metadata(), MAP_TYPE);
        if (annotation.searchBehavior() != SearchBehavior.SEARCHABLE) {
            metadata.put("searchBehavior", annotation.searchBehavior());
        }
        return metadata;
    }

    private static JsonObjectSchema parametersFrom(Parameter[] parameters) {
        LinkedHashMap<String, JsonSchemaElement> properties = new LinkedHashMap<String, JsonSchemaElement>();
        ArrayList<String> required = new ArrayList<String>();
        LinkedHashMap visited = new LinkedHashMap();
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(ToolMemoryId.class) || InvocationParameters.class.isAssignableFrom(parameter.getType()) || LangChain4jManaged.class.isAssignableFrom(parameter.getType()) || parameter.getType() == InvocationContext.class) continue;
            boolean isOptional = Optional.class.equals(parameter.getType());
            P pAnnotation = parameter.getAnnotation(P.class);
            boolean hasDefaultValue = pAnnotation != null && !"\u0000__LANGCHAIN4J_NO_DEFAULT__\u0000".equals(pAnnotation.defaultValue());
            boolean isRequired = !isOptional && !hasDefaultValue && Optional.ofNullable(pAnnotation).map(P::required).orElse(true) != false;
            String parameterName = Optional.ofNullable(pAnnotation).map(P::name).filter(name -> Utils.isNotNullOrBlank(name)).orElse(parameter.getName());
            properties.put(parameterName, ToolSpecifications.jsonSchemaElementFrom(parameter, visited));
            if (!isRequired) continue;
            required.add(parameterName);
        }
        LinkedHashMap<String, JsonSchemaElement> definitions = new LinkedHashMap<String, JsonSchemaElement>();
        visited.forEach((clazz, visitedClassMetadata) -> {
            JsonSchemaElementUtils.VisitedClassMetadata metadata = (JsonSchemaElementUtils.VisitedClassMetadata) visitedClassMetadata;
            if (metadata.recursionDetected) {
                definitions.put(metadata.reference, metadata.jsonSchemaElement);
            }
        });
        if (properties.isEmpty()) {
            return null;
        }
        return JsonObjectSchema.builder().addProperties(properties).required(required).definitions(definitions.isEmpty() ? null : definitions).build();
    }

    private static JsonSchemaElement jsonSchemaElementFrom(Parameter parameter, Map<Class<?>, JsonSchemaElementUtils.VisitedClassMetadata> visited) {
        P annotation = parameter.getAnnotation(P.class);
        String description = null;
        if (annotation != null) {
            if (Utils.isNotNullOrBlank(annotation.value()) && Utils.isNotNullOrBlank(annotation.description())) {
                throw new IllegalArgumentException(String.format("Parameter '%s' has both 'value' and 'description' set in @P. Use one or the other, but not both.", parameter.getName()));
            }
            if (Utils.isNotNullOrBlank(annotation.description())) {
                description = annotation.description();
            } else if (Utils.isNotNullOrBlank(annotation.value())) {
                description = annotation.value();
            }
        }
        Type type = parameter.getParameterizedType();
        Class clazz = parameter.getType();
        if (clazz == Optional.class && type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            if ((type = parameterizedType.getActualTypeArguments()[0]) instanceof Class) {
                clazz = (Class)type;
            } else if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType1 = (ParameterizedType)type;
                clazz = (Class)parameterizedType1.getRawType();
            }
        }
        return JsonSchemaElementUtils.jsonSchemaElementFrom(clazz, type, description, true, visited);
    }
}

