/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonSubTypes
 *  com.fasterxml.jackson.annotation.JsonSubTypes$Type
 *  com.fasterxml.jackson.annotation.JsonTypeInfo
 *  com.fasterxml.jackson.annotation.JsonTypeInfo$As
 *  com.fasterxml.jackson.annotation.JsonTypeInfo$Id
 *  com.fasterxml.jackson.annotation.JsonTypeName
 */
package dev.langchain4j.internal;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.langchain4j.Internal;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Utils;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Internal
public final class PolymorphicTypes {
    public static final String DEFAULT_DISCRIMINATOR_PROPERTY = "type";

    private PolymorphicTypes() {
    }

    public static boolean isPolymorphic(Class<?> type) {
        if (type == null || type.isPrimitive() || type.isEnum() || type.isArray()) {
            return false;
        }
        if (type.getAnnotation(JsonSubTypes.class) != null) {
            return !PolymorphicTypes.findConcreteSubtypes(type).isEmpty();
        }
        return false;
    }

    public static List<Class<?>> findConcreteSubtypes(Class<?> type) {
        LinkedHashSet result = new LinkedHashSet();
        PolymorphicTypes.flatten(type, result);
        return new ArrayList(result);
    }

    private static void flatten(Class<?> type, Set<Class<?>> result) {
        JsonSubTypes ann = type.getAnnotation(JsonSubTypes.class);
        if (ann != null) {
            for (JsonSubTypes.Type t : ann.value()) {
                PolymorphicTypes.flatten(t.value(), result);
            }
            return;
        }
        if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers())) {
            result.add(type);
        }
    }

    public static String discriminatorPropertyName(Class<?> baseType) {
        JsonTypeInfo ann = baseType.getAnnotation(JsonTypeInfo.class);
        if (ann == null) {
            return DEFAULT_DISCRIMINATOR_PROPERTY;
        }
        return Utils.isNullOrBlank(ann.property()) ? "@type" : ann.property();
    }

    public static String discriminatorValue(Class<?> baseType, Class<?> subtype) {
        JsonTypeName typeName;
        JsonSubTypes ann = baseType.getAnnotation(JsonSubTypes.class);
        if (ann != null) {
            for (JsonSubTypes.Type t : ann.value()) {
                if (t.value() != subtype || Utils.isNullOrBlank(t.name())) continue;
                return t.name();
            }
        }
        if ((typeName = subtype.getAnnotation(JsonTypeName.class)) != null && !Utils.isNullOrBlank(typeName.value())) {
            return typeName.value();
        }
        return subtype.getSimpleName();
    }

    public static void verifyJsonTypeInfoIsSupported(Class<?> baseType) {
        JsonTypeInfo ann = baseType.getAnnotation(JsonTypeInfo.class);
        if (ann == null) {
            return;
        }
        if (ann.use() != JsonTypeInfo.Id.NAME && ann.use() != JsonTypeInfo.Id.SIMPLE_NAME) {
            throw new UnsupportedFeatureException(String.format("@JsonTypeInfo(use = Id.%s) on %s is not supported for AI Service return types. Supported values: Id.NAME, Id.SIMPLE_NAME.", ann.use().name(), baseType.getName()));
        }
        if (ann.include() != JsonTypeInfo.As.PROPERTY && ann.include() != JsonTypeInfo.As.EXISTING_PROPERTY) {
            throw new UnsupportedFeatureException(String.format("@JsonTypeInfo(include = As.%s) on %s is not supported for AI Service return types. Supported values: As.PROPERTY, As.EXISTING_PROPERTY.", ann.include().name(), baseType.getName()));
        }
    }
}

