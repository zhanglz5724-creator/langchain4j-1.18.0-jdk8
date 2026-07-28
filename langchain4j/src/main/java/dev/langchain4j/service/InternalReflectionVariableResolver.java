/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.invocation.InvocationParameters
 *  dev.langchain4j.model.input.structured.StructuredPrompt
 *  dev.langchain4j.model.input.structured.StructuredPromptProcessor
 */
package dev.langchain4j.service;

import dev.langchain4j.Internal;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.service.IllegalConfigurationException;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.ParameterNameResolver;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.UserName;
import dev.langchain4j.service.V;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Internal
public class InternalReflectionVariableResolver {
    private InternalReflectionVariableResolver() {
    }

    public static Map<String, Object> findTemplateVariables(String template, Method method, Object[] args) {
        if (args == null) {
            return Collections.emptyMap();
        }
        Parameter[] parameters = method.getParameters();
        HashMap<String, Object> variables = new HashMap<String, Object>();
        for (int i = 0; i < args.length; ++i) {
            Parameter parameter = parameters[i];
            if (InvocationParameters.class.isAssignableFrom(parameter.getType())) continue;
            Object variableValue = args[i];
            variables.put(ParameterNameResolver.name(parameter), variableValue);
            if (!(variableValue instanceof Map)) continue;
            Map<?, ?> variablesMap = (Map<?, ?>) variableValue;
            variablesMap.entrySet().stream().filter(e -> e.getKey() instanceof String).forEach(e -> variables.put((String) e.getKey(), e.getValue()));
        }
        if (template.contains("{{it}}") && !variables.containsKey("it")) {
            String itValue = InternalReflectionVariableResolver.getValueOfVariableIt(parameters, args);
            variables.put("it", itValue);
        }
        return variables;
    }

    private static String getValueOfVariableIt(Parameter[] parameters, Object[] args) {
        if (args != null) {
            Parameter parameter;
            if (!(args.length != 1 || (parameter = parameters[0]).isAnnotationPresent(MemoryId.class) || parameter.isAnnotationPresent(UserMessage.class) || parameter.isAnnotationPresent(UserName.class) || parameter.isAnnotationPresent(V.class) && !InternalReflectionVariableResolver.isAnnotatedWithIt(parameter))) {
                return InternalReflectionVariableResolver.asString(args[0]);
            }
            for (int i = 0; i < args.length; ++i) {
                if (!InternalReflectionVariableResolver.isAnnotatedWithIt(parameters[i])) continue;
                return InternalReflectionVariableResolver.asString(args[i]);
            }
        }
        throw IllegalConfigurationException.illegalConfiguration("Error: cannot find the value of the prompt template variable \"{{it}}\".");
    }

    private static boolean isAnnotatedWithIt(Parameter parameter) {
        V annotation = parameter.getAnnotation(V.class);
        return annotation != null && "it".equals(annotation.value());
    }

    static String asString(Object arg) {
        if (arg == null) {
            return "null";
        }
        if (arg.getClass().isArray()) {
            return InternalReflectionVariableResolver.arrayAsString(arg);
        }
        if (arg.getClass().isAnnotationPresent(StructuredPrompt.class)) {
            return StructuredPromptProcessor.toPrompt((Object)arg).text();
        }
        return arg.toString();
    }

    private static String arrayAsString(Object arg) {
        StringBuilder sb = new StringBuilder("[");
        int length = Array.getLength(arg);
        for (int i = 0; i < length; ++i) {
            sb.append(InternalReflectionVariableResolver.asString(Array.get(arg, i)));
            if (i >= length - 1) continue;
            sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}

