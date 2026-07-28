/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.service;

import dev.langchain4j.Internal;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.ValidationUtils;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Internal
public class TypeUtils {
    private TypeUtils() {
    }

    public static Class<?> getRawClass(Type type) {
        if (type == null) {
            throw new NullPointerException("Type should not be null.");
        }
        if (type instanceof Class) {
            return (Class)type;
        }
        if (type instanceof ParameterizedType) {
            return (Class)((ParameterizedType)type).getRawType();
        }
        throw new IllegalArgumentException("Unable to extract raw class.");
    }

    public static boolean typeHasRawClass(Type type, Class<?> rawClass) {
        if (type == null || rawClass == null) {
            return false;
        }
        return rawClass.equals(TypeUtils.getRawClass(type));
    }

    public static Class<?> resolveFirstGenericParameterClass(Type type) {
        Type[] typeArguments = TypeUtils.getTypeArguments(type);
        if (typeArguments.length == 0) {
            return null;
        }
        Type firstTypeArgument = typeArguments[0];
        if (firstTypeArgument instanceof Class) {
            return (Class)firstTypeArgument;
        }
        if (firstTypeArgument instanceof ParameterizedType) {
            return (Class)((ParameterizedType)firstTypeArgument).getRawType();
        }
        return null;
    }

    public static Type resolveFirstGenericParameterType(Type type) {
        Type[] typeArguments = TypeUtils.getTypeArguments(type);
        return typeArguments.length > 0 ? typeArguments[0] : null;
    }

    private static Type[] getTypeArguments(Type type) {
        ValidationUtils.ensureNotNull((Object)type, (String)"type");
        if (!(type instanceof ParameterizedType)) {
            return new Type[0];
        }
        ParameterizedType parameterizedType = (ParameterizedType)type;
        Object[] typeArguments = parameterizedType.getActualTypeArguments();
        ValidationUtils.ensureNotEmpty((Object[])typeArguments, (String)"%s", (Object[])new Object[]{"Parameterized type has no type arguments."});
        return typeArguments;
    }

    public static void validateReturnTypesAreProperlyParametrized(String methodName, Type type) {
        TypeUtils.validateReturnTypesAreProperlyParametrized(methodName, type, new ArrayList<Type>());
    }

    private static void validateReturnTypesAreProperlyParametrized(String methodName, Type type, List<Type> typeChain) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            for (Type actualTypeArgument : parameterizedType.getActualTypeArguments()) {
                typeChain.add(parameterizedType);
                TypeUtils.validateReturnTypesAreProperlyParametrized(methodName, actualTypeArgument, typeChain);
            }
        } else {
            Class clazz;
            if (type instanceof WildcardType) {
                typeChain.add(type);
                throw TypeUtils.genericNotProperlySpecifiedException(methodName, typeChain);
            }
            if (type instanceof TypeVariable) {
                typeChain.add(type);
                throw TypeUtils.genericNotProperlySpecifiedException(methodName, typeChain);
            }
            if (type instanceof Class && (clazz = (Class)type).getTypeParameters().length > 0) {
                typeChain.add(type);
                throw TypeUtils.genericNotProperlySpecifiedException(methodName, typeChain);
            }
        }
    }

    private static IllegalArgumentException genericNotProperlySpecifiedException(String methodName, List<Type> typeChain) {
        String actualDeclaration = TypeUtils.getActualDeclaration(typeChain);
        String exampleStringDeclaration = TypeUtils.getExemplarDeclaration(typeChain, "String");
        String examplePojoDeclaration = TypeUtils.getExemplarDeclaration(typeChain, "MyCustomPojo");
        return Exceptions.illegalArgument((String)"The return type '%s' of the method '%s' must be parameterized with a concrete type, for example: %s or %s", (Object[])new Object[]{actualDeclaration, methodName, exampleStringDeclaration, examplePojoDeclaration});
    }

    private static String getActualDeclaration(List<Type> typeChain) {
        StringBuilder actualDeclaration = new StringBuilder(typeChain.stream().map(type -> {
            if (type instanceof WildcardType) {
                return "?";
            }
            if (type instanceof TypeVariable) {
                return type.getTypeName();
            }
            return TypeUtils.getRawClass(type).getSimpleName();
        }).collect(Collectors.joining("<")));
        actualDeclaration.append(String.join((CharSequence)"", Collections.nCopies(Math.max(0, typeChain.size() - 1), ">")));
        return actualDeclaration.toString();
    }

    private static String getExemplarDeclaration(List<Type> typeChain, String forType) {
        List rawTypesOnly = typeChain.stream().filter(type -> !(type instanceof WildcardType) && !(type instanceof TypeVariable)).collect(Collectors.toList());
        StringBuilder declarationExample = new StringBuilder(rawTypesOnly.stream().map(type -> TypeUtils.getRawClass(type).getSimpleName()).collect(Collectors.joining("<")));
        declarationExample.append("<").append(forType);
        declarationExample.append(String.join((CharSequence)"", Collections.nCopies(rawTypesOnly.size(), ">")));
        return declarationExample.toString();
    }

    public static boolean isImageType(Class<?> rawReturnType) {
        return Image.class.isAssignableFrom(rawReturnType) || ImageContent.class.isAssignableFrom(rawReturnType);
    }
}

