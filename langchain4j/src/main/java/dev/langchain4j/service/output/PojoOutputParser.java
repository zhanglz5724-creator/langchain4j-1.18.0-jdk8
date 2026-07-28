/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.internal.JsonParsingUtils
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.internal.PolymorphicTypes
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.output.structured.Description
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.JsonParsingUtils;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.internal.PolymorphicTypes;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.IllegalConfigurationException;
import dev.langchain4j.service.output.OutputParser;
import dev.langchain4j.service.output.ParsingUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Internal
class PojoOutputParser<T>
implements OutputParser<T> {
    private final Class<T> type;

    PojoOutputParser(Class<T> type) {
        this.type = type;
    }

    @Override
    public T parse(String text) {
        if (Utils.isNullOrBlank((String)text)) {
            throw ParsingUtils.outputParsingException(text, this.type);
        }
        try {
            if (PolymorphicTypes.isPolymorphic(this.type)) {
                return (T)JsonParsingUtils.extractAndParseJson((String)text, json -> {
                    Map map = (Map)Json.fromJson((String)json, Map.class);
                    if (map != null && map.size() == 1 && map.containsKey("value")) {
                        return Json.fromJson((String)Json.toJson(map.get("value")), this.type);
                    }
                    return Json.fromJson((String)json, this.type);
                }).value();
            }
            return (T)JsonParsingUtils.extractAndParseJson((String)text, this.type).value();
        }
        catch (Exception e) {
            throw ParsingUtils.outputParsingException(text, this.type.getTypeName(), e);
        }
    }

    @Override
    public Optional<JsonSchema> jsonSchema() {
        JsonObjectSchema obj;
        JsonSchemaElement root;
        if (PolymorphicTypes.isPolymorphic(this.type)) {
            LinkedHashMap visited = new LinkedHashMap();
            JsonSchemaElement anyOf = JsonSchemaElementUtils.polymorphicSchemaFrom(this.type, null, (boolean)false, visited);
            root = JsonSchemaElementUtils.wrapPolymorphic((String)"value", (JsonSchemaElement)JsonSchemaElementUtils.referenceIfRecursive((JsonSchemaElement)anyOf, this.type, visited), visited);
        } else {
            root = JsonSchemaElementUtils.jsonObjectOrReferenceSchemaFrom(this.type, null, (boolean)false, new LinkedHashMap(), (boolean)true);
        }
        if (root instanceof JsonObjectSchema && (obj = (JsonObjectSchema)root).properties().isEmpty() && Modifier.isAbstract(this.type.getModifiers())) {
            throw new UnsupportedFeatureException(String.format("Type %s is an interface or abstract class with no permitted subtypes discoverable by langchain4j, which produced an empty JSON schema. To use it as a polymorphic return type, either make it sealed or annotate it with @JsonSubTypes.", this.type.getName()));
        }
        return Optional.of(JsonSchema.builder().name(this.type.getSimpleName()).rootElement(root).build());
    }

    @Override
    public String formatInstructions() {
        String jsonStructure = PojoOutputParser.jsonStructure(this.type, new HashSet());
        this.validateJsonStructure(jsonStructure, this.type);
        return "\nYou must answer strictly in the following JSON format: " + jsonStructure;
    }

    private static String jsonStructure(Class<?> type, Set<Class<?>> visited) {
        StringBuilder jsonSchema = new StringBuilder();
        jsonSchema.append("{\n");
        for (Field field : type.getDeclaredFields()) {
            String name = field.getName();
            if (name.equals("__$hits$__") || Modifier.isStatic(field.getModifiers())) continue;
            jsonSchema.append(String.format("\"%s\": (%s),\n", name, PojoOutputParser.descriptionFor(field, visited)));
        }
        int trailingCommaIndex = jsonSchema.lastIndexOf(",");
        if (trailingCommaIndex > 0) {
            jsonSchema.delete(trailingCommaIndex, trailingCommaIndex + 1);
        }
        jsonSchema.append("}");
        return jsonSchema.toString();
    }

    private static String descriptionFor(Field field, Set<Class<?>> visited) {
        Description fieldDescription = field.getAnnotation(Description.class);
        if (fieldDescription == null) {
            return "type: " + PojoOutputParser.typeOf(field, visited);
        }
        return String.join((CharSequence)" ", fieldDescription.value()) + "; type: " + PojoOutputParser.typeOf(field, visited);
    }

    private static String typeOf(Field field, Set<Class<?>> visited) {
        return PojoOutputParser.typeOf(field.getGenericType(), visited);
    }

    private static String typeOf(Type type, Set<Class<?>> visited) {
        if (type instanceof ParameterizedType) {
            Class rawClass;
            ParameterizedType parameterizedType = (ParameterizedType)type;
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class && ((rawClass = (Class)rawType) == List.class || rawClass == Set.class)) {
                return String.format("array of %s", PojoOutputParser.typeOf(parameterizedType.getActualTypeArguments()[0], visited));
            }
        } else if (type instanceof Class) {
            Class clazz = (Class)type;
            if (clazz.isArray()) {
                return String.format("array of %s", PojoOutputParser.typeOf(clazz.getComponentType(), visited));
            }
            if (clazz.isEnum()) {
                return "enum, must be one of " + Arrays.stream(clazz.getEnumConstants()).map(e -> ((Enum)e).name()).collect(Collectors.toList());
            }
            return PojoOutputParser.simpleNameOrJsonStructure(clazz, visited);
        }
        return PojoOutputParser.simpleTypeName(type);
    }

    private static String simpleNameOrJsonStructure(Class<?> structured, Set<Class<?>> visited) {
        String simpleTypeName = PojoOutputParser.simpleTypeName(structured);
        if (structured.getPackage() == null || structured.getPackage().getName().startsWith("java.") || visited.contains(structured)) {
            return simpleTypeName;
        }
        visited.add(structured);
        return simpleTypeName + ": " + PojoOutputParser.jsonStructure(structured, visited);
    }

    private static String simpleTypeName(Type type) {
        String typeName = type.getTypeName();
        if ("java.lang.String".equals(typeName)) {
            return "string";
        }
        if ("java.lang.Integer".equals(typeName) || "int".equals(typeName)) {
            return "integer";
        }
        if ("java.lang.Boolean".equals(typeName) || "boolean".equals(typeName)) {
            return "boolean";
        }
        if ("java.lang.Float".equals(typeName) || "float".equals(typeName)) {
            return "float";
        }
        if ("java.lang.Double".equals(typeName) || "double".equals(typeName)) {
            return "double";
        }
        if ("java.util.Date".equals(typeName) || "java.time.LocalDate".equals(typeName)) {
            return "date string (2023-12-31)";
        }
        if ("java.time.LocalTime".equals(typeName)) {
            return "time string (23:59:59)";
        }
        if ("java.time.LocalDateTime".equals(typeName)) {
            return "date-time string (2023-12-31T23:59:59)";
        }
        return type.getTypeName();
    }

    private void validateJsonStructure(String jsonStructure, Type returnType) {
        if (jsonStructure.replaceAll("\\s", "").equals("{}")) {
            if (returnType.toString().contains("reactor.core.publisher.Flux")) {
                throw IllegalConfigurationException.illegalConfiguration("Please import langchain4j-reactor module if you wish to use Flux<String> as a method return type");
            }
            throw IllegalConfigurationException.illegalConfiguration("Illegal method return type: " + returnType);
        }
    }
}

