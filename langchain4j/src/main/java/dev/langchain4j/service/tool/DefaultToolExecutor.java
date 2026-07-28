/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.P
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolMemoryId
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.exception.ToolArgumentsException
 *  dev.langchain4j.exception.ToolExecutionException
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.invocation.InvocationParameters
 *  dev.langchain4j.invocation.LangChain4jManaged
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolMemoryId;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.exception.ToolArgumentsException;
import dev.langchain4j.exception.ToolExecutionException;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.invocation.LangChain4jManaged;
import dev.langchain4j.service.tool.ToolExecutionRequestUtil;
import dev.langchain4j.service.tool.ToolExecutionResult;
import dev.langchain4j.service.tool.ToolExecutor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultToolExecutor
implements ToolExecutor {
    private final Object object;
    private final Method originalMethod;
    private final Method methodToInvoke;
    private final boolean wrapToolArgumentsExceptions;
    private final boolean propagateToolExecutionExceptions;

    public DefaultToolExecutor(Builder builder) {
        this.object = ValidationUtils.ensureNotNull((Object)builder.object, (String)"object");
        this.originalMethod = (Method)ValidationUtils.ensureNotNull((Object)builder.originalMethod, (String)"originalMethod");
        this.methodToInvoke = (Method)ValidationUtils.ensureNotNull((Object)builder.methodToInvoke, (String)"methodToInvoke");
        this.wrapToolArgumentsExceptions = (Boolean)Utils.getOrDefault((Object)builder.wrapToolArgumentsExceptions, (Object)false);
        this.propagateToolExecutionExceptions = (Boolean)Utils.getOrDefault((Object)builder.propagateToolExecutionExceptions, (Object)false);
    }

    public DefaultToolExecutor(Object object, Method method) {
        this.object = ValidationUtils.ensureNotNull((Object)object, (String)"object");
        this.methodToInvoke = this.originalMethod = (Method)ValidationUtils.ensureNotNull((Object)method, (String)"method");
        this.wrapToolArgumentsExceptions = false;
        this.propagateToolExecutionExceptions = false;
    }

    public DefaultToolExecutor(Object object, ToolExecutionRequest toolExecutionRequest) {
        this.object = ValidationUtils.ensureNotNull((Object)object, (String)"object");
        ValidationUtils.ensureNotNull((Object)toolExecutionRequest, (String)"toolExecutionRequest");
        this.methodToInvoke = this.originalMethod = this.findMethod(object, toolExecutionRequest);
        this.wrapToolArgumentsExceptions = false;
        this.propagateToolExecutionExceptions = false;
    }

    public Method originalMethod() {
        return this.originalMethod;
    }

    private Method findMethod(Object object, ToolExecutionRequest toolExecutionRequest) {
        String requestedMethodName = toolExecutionRequest.name();
        for (Method method : Utils.allConcreteMethods(object.getClass())) {
            if (!method.getName().equals(requestedMethodName)) continue;
            return method;
        }
        throw new IllegalArgumentException(String.format("Method '%s' is not found in object '%s'", requestedMethodName, object.getClass().getName()));
    }

    public DefaultToolExecutor(Object object, Method originalMethod, Method methodToInvoke) {
        this.object = ValidationUtils.ensureNotNull((Object)object, (String)"object");
        this.originalMethod = (Method)ValidationUtils.ensureNotNull((Object)originalMethod, (String)"originalMethod");
        this.methodToInvoke = (Method)ValidationUtils.ensureNotNull((Object)methodToInvoke, (String)"methodToInvoke");
        this.wrapToolArgumentsExceptions = false;
        this.propagateToolExecutionExceptions = false;
    }

    @Override
    public ToolExecutionResult executeWithContext(ToolExecutionRequest request, InvocationContext context) {
        Object[] arguments = this.prepareArguments(request, context);
        try {
            return this.execute(arguments);
        }
        catch (IllegalAccessException e) {
            try {
                this.methodToInvoke.setAccessible(true);
                return this.execute(arguments);
            }
            catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
            catch (InvocationTargetException e2) {
                if (this.propagateToolExecutionExceptions) {
                    throw new ToolExecutionException(e2.getCause());
                }
                return ToolExecutionResult.builder().isError(true).resultText(DefaultToolExecutor.errorMessage(e2.getCause())).build();
            }
        }
        catch (InvocationTargetException e) {
            if (this.propagateToolExecutionExceptions) {
                throw new ToolExecutionException(e.getCause());
            }
            return ToolExecutionResult.builder().isError(true).resultText(DefaultToolExecutor.errorMessage(e.getCause())).build();
        }
    }

    @Override
    public String execute(ToolExecutionRequest request, Object memoryId) {
        InvocationContext invocationContext = InvocationContext.builder().chatMemoryId(memoryId).build();
        ToolExecutionResult result = this.executeWithContext(request, invocationContext);
        return result.resultText();
    }

    private Object[] prepareArguments(ToolExecutionRequest toolExecutionRequest, InvocationContext context) {
        try {
            Map<String, Object> argumentsMap = ToolExecutionRequestUtil.argumentsAsMap(toolExecutionRequest.arguments());
            return DefaultToolExecutor.prepareArguments(this.originalMethod, toolExecutionRequest.name(), argumentsMap, context);
        }
        catch (Exception e) {
            if (this.wrapToolArgumentsExceptions) {
                throw new ToolArgumentsException(Exceptions.unwrapRuntimeException((Exception)e));
            }
            throw e;
        }
    }

    private ToolExecutionResult execute(Object[] arguments) throws IllegalAccessException, InvocationTargetException {
        Object result = this.methodToInvoke.invoke(this.object, arguments);
        List<Content> resultContents = this.toContents(result);
        if (resultContents != null) {
            return ToolExecutionResult.builder().result(result).resultContents(resultContents).build();
        }
        return ToolExecutionResult.builder().result(result).resultTextSupplier(() -> this.toText(result)).build();
    }

    private List<Content> toContents(Object result) {
        if (result == null) {
            return null;
        }
        if (result instanceof Image) {
            Image image = (Image)result;
            return Collections.singletonList(ImageContent.from((Image)image));
        }
        if (result instanceof Content) {
            Content content = (Content)result;
            return Collections.singletonList(content);
        }
        if (result instanceof Collection) {
            Collection collection = (Collection)result;
            if (!collection.isEmpty() && collection.iterator().next() instanceof Content) {
                return collection.stream().map(Content.class::cast).collect(Collectors.toList());
            }
        } else if (result instanceof Content[]) {
            Content[] array = (Content[])result;
            return Arrays.asList(array);
        }
        return null;
    }

    private String toText(Object result) {
        Class<?> returnType = this.methodToInvoke.getReturnType();
        if (returnType == Void.TYPE) {
            return "Success";
        }
        if (returnType == String.class) {
            if (result == null) {
                return "null";
            }
            return (String)result;
        }
        return Json.toJson((Object)result);
    }

    static Object[] prepareArguments(Method method, String toolName, Map<String, Object> argumentsMap, InvocationContext context) {
        Parameter[] parameters = method.getParameters();
        Object[] arguments = new Object[parameters.length];
        for (int i = 0; i < parameters.length; ++i) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(ToolMemoryId.class)) {
                arguments[i] = context.chatMemoryId();
                continue;
            }
            if (InvocationParameters.class.isAssignableFrom(parameter.getType())) {
                arguments[i] = context.invocationParameters();
                continue;
            }
            if (parameter.getType() == InvocationContext.class) {
                arguments[i] = context;
                continue;
            }
            if (LangChain4jManaged.class.isAssignableFrom(parameter.getType())) {
                arguments[i] = context.managedParameters().get(parameter.getType());
                continue;
            }
            String parameterName = DefaultToolExecutor.getName(parameter);
            Object argument = argumentsMap.get(parameterName);
            Class<?> parameterClass = parameter.getType();
            Type parameterType = parameter.getParameterizedType();
            if (parameterClass == Optional.class) {
                arguments[i] = DefaultToolExecutor.createOptional(argument, parameterName, parameterType);
                continue;
            }
            if (argument != null) {
                arguments[i] = DefaultToolExecutor.coerceArgument(argument, parameterName, parameterClass, parameterType);
                continue;
            }
            P pAnnotation = parameter.getAnnotation(P.class);
            if (pAnnotation != null && !"\u0000__LANGCHAIN4J_NO_DEFAULT__\u0000".equals(pAnnotation.defaultValue())) {
                arguments[i] = DefaultToolExecutor.parseDefaultValue(pAnnotation.defaultValue(), parameterName, parameterClass, parameterType);
                continue;
            }
            if (!parameterClass.isPrimitive()) continue;
            throw new IllegalArgumentException(String.format("Required parameter \"%s\" of tool \"%s\" is missing", parameterName, toolName));
        }
        return arguments;
    }

    private static String errorMessage(Throwable cause) {
        String message = cause.getMessage();
        return message != null ? message : cause.getClass().getName();
    }

    private static String getName(Parameter parameter) {
        P pAnnotation = parameter.getAnnotation(P.class);
        if (pAnnotation != null && Utils.isNotNullOrBlank((String)pAnnotation.name())) {
            return pAnnotation.name();
        }
        return parameter.getName();
    }

    private static Type extractActualType(Type parameterType) {
        return ((ParameterizedType)parameterType).getActualTypeArguments()[0];
    }

    private static Class<?> extractActualClass(Type actualType) {
        return actualType instanceof Class ? (Class)actualType : (Class)((ParameterizedType)actualType).getRawType();
    }

    private static Optional<?> createOptional(Object argument, String parameterName, Type parameterType) {
        if (argument == null) {
            return Optional.empty();
        }
        Type actualType = DefaultToolExecutor.extractActualType(parameterType);
        Class<?> actualClass = DefaultToolExecutor.extractActualClass(actualType);
        Object coercedValue = DefaultToolExecutor.coerceArgument(argument, parameterName, actualClass, actualType);
        return Optional.of(coercedValue);
    }

    static Object parseDefaultValue(String defaultValue, String parameterName, Class<?> parameterClass, Type parameterType) {
        Object jsonParsed;
        if (parameterClass == String.class || parameterClass.isEnum() || parameterClass == UUID.class) {
            return DefaultToolExecutor.coerceArgument(defaultValue, parameterName, parameterClass, parameterType);
        }
        try {
            jsonParsed = Json.fromJson((String)defaultValue, Object.class);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(String.format("Cannot parse @P(defaultValue = \"%s\") for parameter \"%s\" of type %s: %s", defaultValue, parameterName, parameterClass.getName(), e.getMessage()), e);
        }
        if (jsonParsed == null) {
            throw new IllegalArgumentException(String.format("@P(defaultValue = \"%s\") parses to null for parameter \"%s\" of type %s", defaultValue, parameterName, parameterClass.getName()));
        }
        return DefaultToolExecutor.coerceArgument(jsonParsed, parameterName, parameterClass, parameterType);
    }

    static Object coerceArgument(Object argument, String parameterName, Class<?> parameterClass, Type parameterType) {
        if (parameterClass == String.class) {
            return argument.toString();
        }
        if (parameterClass.isEnum()) {
            try {
                Class<?> enumClass = parameterClass;
                try {
                    return Enum.valueOf(enumClass, Objects.requireNonNull(argument).toString());
                }
                catch (IllegalArgumentException e) {
                    return Enum.valueOf(enumClass, Objects.requireNonNull(argument).toString().toUpperCase(Locale.ROOT));
                }
            }
            catch (Error | Exception e) {
                throw new IllegalArgumentException(String.format("Argument \"%s\" is not a valid enum value for %s: <%s>", parameterName, parameterClass.getName(), argument), e);
            }
        }
        if (parameterClass == Boolean.class || parameterClass == Boolean.TYPE) {
            if (argument instanceof Boolean) {
                return argument;
            }
            throw new IllegalArgumentException(String.format("Argument \"%s\" is not convertable to %s, got %s: <%s>", parameterName, parameterClass.getName(), argument.getClass().getName(), argument));
        }
        if (parameterClass == Double.class || parameterClass == Double.TYPE) {
            return DefaultToolExecutor.getDoubleValue(argument, parameterName, parameterClass);
        }
        if (parameterClass == Float.class || parameterClass == Float.TYPE) {
            double doubleValue = DefaultToolExecutor.getDoubleValue(argument, parameterName, parameterClass);
            DefaultToolExecutor.checkBounds(doubleValue, parameterName, parameterClass, -3.4028234663852886E38, 3.4028234663852886E38);
            return Float.valueOf((float)doubleValue);
        }
        if (parameterClass == BigDecimal.class) {
            return DefaultToolExecutor.getBigDecimalValue(argument, parameterName, parameterClass);
        }
        if (parameterClass == Integer.class || parameterClass == Integer.TYPE) {
            return (int)DefaultToolExecutor.getBoundedLongValue(argument, parameterName, parameterClass, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        if (parameterClass == Long.class || parameterClass == Long.TYPE) {
            return DefaultToolExecutor.getBoundedLongValue(argument, parameterName, parameterClass, Long.MIN_VALUE, Long.MAX_VALUE);
        }
        if (parameterClass == Short.class || parameterClass == Short.TYPE) {
            return (short)DefaultToolExecutor.getBoundedLongValue(argument, parameterName, parameterClass, -32768L, 32767L);
        }
        if (parameterClass == Byte.class || parameterClass == Byte.TYPE) {
            return (byte)DefaultToolExecutor.getBoundedLongValue(argument, parameterName, parameterClass, -128L, 127L);
        }
        if (parameterClass == BigInteger.class) {
            return DefaultToolExecutor.getBigIntegerValue(argument, parameterName, parameterClass);
        }
        if (Collection.class.isAssignableFrom(parameterClass) || Map.class.isAssignableFrom(parameterClass)) {
            return Json.fromJson((String)Json.toJson((Object)argument), (Type)parameterType);
        }
        if (parameterClass == UUID.class) {
            return UUID.fromString(argument.toString());
        }
        if (argument instanceof String) {
            return Json.fromJson((String)argument.toString(), parameterClass);
        }
        return Json.fromJson((String)Json.toJson((Object)argument), parameterClass);
    }

    private static double getDoubleValue(Object argument, String parameterName, Class<?> parameterType) {
        if (argument instanceof String) {
            try {
                return Double.parseDouble(argument.toString());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (!(argument instanceof Number)) {
            throw new IllegalArgumentException(String.format("Argument \"%s\" is not convertable to %s, got %s: <%s>", parameterName, parameterType.getName(), argument.getClass().getName(), argument));
        }
        return ((Number)argument).doubleValue();
    }

    private static void checkBounds(double doubleValue, String parameterName, Class<?> parameterType, double minValue, double maxValue) {
        if (doubleValue < minValue || doubleValue > maxValue) {
            throw new IllegalArgumentException(String.format("Argument \"%s\" is out of range for %s: <%s>", parameterName, parameterType.getName(), doubleValue));
        }
    }

    public static long getBoundedLongValue(Object argument, String parameterName, Class<?> parameterType, long minValue, long maxValue) {
        BigInteger bigIntegerValue = DefaultToolExecutor.getBigIntegerValue(argument, parameterName, parameterType);
        if (bigIntegerValue.compareTo(BigInteger.valueOf(minValue)) < 0 || bigIntegerValue.compareTo(BigInteger.valueOf(maxValue)) > 0) {
            throw new IllegalArgumentException(String.format("Argument \"%s\" is out of range for %s: <%s>", parameterName, parameterType.getName(), argument));
        }
        return bigIntegerValue.longValue();
    }

    private static BigInteger getBigIntegerValue(Object argument, String parameterName, Class<?> parameterType) {
        BigDecimal bigDecimalValue = DefaultToolExecutor.getBigDecimalValue(argument, parameterName, parameterType);
        try {
            return bigDecimalValue.toBigIntegerExact();
        }
        catch (ArithmeticException e) {
            throw new IllegalArgumentException(String.format("Argument \"%s\" has non-integer value for %s: <%s>", parameterName, parameterType.getName(), argument));
        }
    }

    private static BigDecimal getBigDecimalValue(Object argument, String parameterName, Class<?> parameterType) {
        if (argument instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal)argument;
            return bigDecimal;
        }
        if (argument instanceof BigInteger) {
            BigInteger bigInteger = (BigInteger)argument;
            return new BigDecimal(bigInteger);
        }
        if (argument instanceof Number) {
            Number number = (Number)argument;
            return new BigDecimal(number.toString());
        }
        if (argument instanceof String) {
            try {
                return new BigDecimal(argument.toString().trim());
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        throw new IllegalArgumentException(String.format("Argument \"%s\" is not convertable to %s, got %s: <%s>", parameterName, parameterType.getName(), argument == null ? "null" : argument.getClass().getName(), argument));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object object;
        private Method originalMethod;
        private Method methodToInvoke;
        private Boolean wrapToolArgumentsExceptions;
        private Boolean propagateToolExecutionExceptions;

        public Builder object(Object object) {
            this.object = object;
            return this;
        }

        public Builder originalMethod(Method originalMethod) {
            this.originalMethod = originalMethod;
            return this;
        }

        public Builder methodToInvoke(Method methodToInvoke) {
            this.methodToInvoke = methodToInvoke;
            return this;
        }

        public Builder wrapToolArgumentsExceptions(Boolean wrapToolArgumentsExceptions) {
            this.wrapToolArgumentsExceptions = wrapToolArgumentsExceptions;
            return this;
        }

        public Builder propagateToolExecutionExceptions(Boolean propagateToolExecutionExceptions) {
            this.propagateToolExecutionExceptions = propagateToolExecutionExceptions;
            return this;
        }

        public DefaultToolExecutor build() {
            return new DefaultToolExecutor(this);
        }
    }
}

