/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.annotation.JsonTypeInfo
 *  com.fasterxml.jackson.annotation.JsonTypeInfo$As
 */
package dev.langchain4j.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.langchain4j.Internal;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.PolymorphicTypes;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.json.JsonAnyOfSchema;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonBooleanSchema;
import dev.langchain4j.model.chat.request.json.JsonEnumSchema;
import dev.langchain4j.model.chat.request.json.JsonIntegerSchema;
import dev.langchain4j.model.chat.request.json.JsonNullSchema;
import dev.langchain4j.model.chat.request.json.JsonNumberSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonRawSchema;
import dev.langchain4j.model.chat.request.json.JsonReferenceSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import dev.langchain4j.model.output.structured.Description;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Internal
public class JsonSchemaElementUtils {
    private static final String DEFAULT_UUID_DESCRIPTION = "String in a UUID format";

    public static JsonSchemaElement jsonSchemaElementFrom(Class<?> clazz) {
        return JsonSchemaElementUtils.jsonSchemaElementFrom(clazz, clazz, null, false, new LinkedHashMap());
    }

    public static JsonSchemaElement jsonSchemaElementFrom(Class<?> clazz, Type type, String fieldDescription, boolean areSubFieldsRequiredByDefault, Map<Class<?>, VisitedClassMetadata> visited) {
        if (JsonSchemaElementUtils.isJsonString(clazz)) {
            return JsonStringSchema.builder().description(Optional.ofNullable(fieldDescription).orElse(JsonSchemaElementUtils.descriptionFrom(clazz))).build();
        }
        if (JsonSchemaElementUtils.isJsonInteger(clazz)) {
            return JsonIntegerSchema.builder().description(fieldDescription).build();
        }
        if (JsonSchemaElementUtils.isJsonNumber(clazz)) {
            return JsonNumberSchema.builder().description(fieldDescription).build();
        }
        if (JsonSchemaElementUtils.isJsonBoolean(clazz)) {
            return JsonBooleanSchema.builder().description(fieldDescription).build();
        }
        if (clazz.isEnum()) {
            return JsonEnumSchema.builder().enumValues(Arrays.stream(clazz.getEnumConstants()).map(e -> ((Enum)e).name()).collect(Collectors.toList())).description(Optional.ofNullable(fieldDescription).orElse(JsonSchemaElementUtils.descriptionFrom(clazz))).build();
        }
        if (clazz.isArray()) {
            return JsonArraySchema.builder().items(JsonSchemaElementUtils.jsonSchemaElementFrom(clazz.getComponentType(), null, null, areSubFieldsRequiredByDefault, visited)).description(fieldDescription).build();
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            Type elementType = JsonSchemaElementUtils.collectionElementType(type);
            return JsonArraySchema.builder().items(JsonSchemaElementUtils.jsonSchemaElementFrom(JsonSchemaElementUtils.rawClassOf(elementType), elementType, null, areSubFieldsRequiredByDefault, visited)).description(fieldDescription).build();
        }
        if (PolymorphicTypes.isPolymorphic(clazz)) {
            return JsonSchemaElementUtils.polymorphicSchemaFrom(clazz, fieldDescription, areSubFieldsRequiredByDefault, visited);
        }
        return JsonSchemaElementUtils.jsonObjectOrReferenceSchemaFrom(clazz, fieldDescription, areSubFieldsRequiredByDefault, visited, false);
    }

    public static JsonSchemaElement polymorphicSchemaFrom(Class<?> baseType, String description, boolean areSubFieldsRequiredByDefault, Map<Class<?>, VisitedClassMetadata> visited) {
        PolymorphicTypes.verifyJsonTypeInfoIsSupported(baseType);
        if (visited.containsKey(baseType)) {
            VisitedClassMetadata metadata = visited.get(baseType);
            metadata.recursionDetected = true;
            return JsonReferenceSchema.builder().reference(metadata.reference).build();
        }
        String reference = Utils.generateUUIDFrom(baseType.getName());
        VisitedClassMetadata metadata = new VisitedClassMetadata(JsonReferenceSchema.builder().reference(reference).build(), reference, false);
        visited.put(baseType, metadata);
        String discriminatorProperty = PolymorphicTypes.discriminatorPropertyName(baseType);
        ArrayList<JsonSchemaElement> options = new ArrayList<JsonSchemaElement>();
        for (Class<?> subtype : PolymorphicTypes.findConcreteSubtypes(baseType)) {
            JsonSchemaElement subtypeSchema = JsonSchemaElementUtils.jsonObjectOrReferenceSchemaFrom(subtype, null, areSubFieldsRequiredByDefault, visited, false);
            JsonSchemaElement withDiscriminator = JsonSchemaElementUtils.addDiscriminator(subtypeSchema, baseType, subtype, discriminatorProperty);
            options.add(withDiscriminator);
            VisitedClassMetadata subtypeMetadata = visited.get(subtype);
            if (subtypeMetadata == null) continue;
            subtypeMetadata.jsonSchemaElement = withDiscriminator;
        }
        String desc = description != null ? description : Optional.ofNullable(JsonSchemaElementUtils.descriptionFrom(baseType)).orElse(baseType.getSimpleName());
        JsonAnyOfSchema anyOf = JsonAnyOfSchema.builder().description(desc).anyOf(options).build();
        metadata.jsonSchemaElement = anyOf;
        return anyOf;
    }

    private static JsonSchemaElement addDiscriminator(JsonSchemaElement subtypeSchema, Class<?> baseType, Class<?> subtype, String discriminatorProperty) {
        JsonEnumSchema existing;
        if (!(subtypeSchema instanceof JsonObjectSchema)) {
            return subtypeSchema;
        }
        JsonObjectSchema obj = (JsonObjectSchema)subtypeSchema;
        String discriminatorValue = PolymorphicTypes.discriminatorValue(baseType, subtype);
        JsonSchemaElement discPropValue = obj.properties().get(discriminatorProperty);
        if (discPropValue instanceof JsonEnumSchema && (existing = (JsonEnumSchema)discPropValue).enumValues() != null && existing.enumValues().size() == 1 && discriminatorValue.equals(existing.enumValues().get(0))) {
            return obj;
        }
        if (obj.properties().containsKey(discriminatorProperty)) {
            boolean allowed;
            JsonTypeInfo info = baseType.getAnnotation(JsonTypeInfo.class);
            boolean bl = allowed = info != null && (info.visible() || info.include() == JsonTypeInfo.As.EXISTING_PROPERTY);
            if (!allowed) {
                throw new IllegalArgumentException(String.format("Polymorphic subtype %s declares a field named '%s', which collides with the discriminator property used for %s. Either rename the field, specify a different discriminator name with @JsonTypeInfo(property = \"...\") on %s, set @JsonTypeInfo(visible = true), or use @JsonTypeInfo(include = As.EXISTING_PROPERTY) if the field is intentionally part of the subtype.", subtype.getName(), discriminatorProperty, baseType.getName(), baseType.getName()));
            }
        }
        LinkedHashMap<String, JsonSchemaElement> properties = new LinkedHashMap<String, JsonSchemaElement>();
        properties.put(discriminatorProperty, JsonEnumSchema.builder().enumValues(discriminatorValue).build());
        obj.properties().forEach(properties::putIfAbsent);
        ArrayList<String> required = new ArrayList<String>();
        required.add(discriminatorProperty);
        obj.required().forEach(r -> {
            if (!required.contains(r)) {
                required.add((String)r);
            }
        });
        return JsonObjectSchema.builder().description(Optional.ofNullable(obj.description()).orElse(subtype.getSimpleName())).addProperties(properties).required(required).additionalProperties(obj.additionalProperties()).build();
    }

    public static JsonSchemaElement referenceIfRecursive(JsonSchemaElement element, Class<?> baseType, Map<Class<?>, VisitedClassMetadata> visited) {
        VisitedClassMetadata metadata = visited.get(baseType);
        if (metadata != null && metadata.recursionDetected && element instanceof JsonAnyOfSchema) {
            return JsonReferenceSchema.builder().reference(metadata.reference).build();
        }
        return element;
    }

    public static JsonObjectSchema wrapPolymorphic(String propertyName, JsonSchemaElement element, Map<Class<?>, VisitedClassMetadata> visited) {
        JsonObjectSchema.Builder builder = JsonObjectSchema.builder().addProperty(propertyName, element).required(propertyName);
        LinkedHashMap<String, JsonSchemaElement> definitions = new LinkedHashMap<String, JsonSchemaElement>();
        visited.forEach((clazz, meta) -> {
            if (meta.recursionDetected) {
                definitions.put(meta.reference, meta.jsonSchemaElement);
            }
        });
        if (!definitions.isEmpty()) {
            builder.definitions(definitions);
        }
        return builder.build();
    }

    public static JsonSchemaElement jsonObjectOrReferenceSchemaFrom(Class<?> type, String description, boolean areSubFieldsRequiredByDefault, Map<Class<?>, VisitedClassMetadata> visited, boolean setDefinitions) {
        if (visited.containsKey(type) && JsonSchemaElementUtils.isCustomClass(type)) {
            VisitedClassMetadata visitedClassMetadata2 = visited.get(type);
            JsonSchemaElement jsonSchemaElement = visitedClassMetadata2.jsonSchemaElement;
            if (jsonSchemaElement instanceof JsonReferenceSchema) {
                visitedClassMetadata2.recursionDetected = true;
            }
            if (jsonSchemaElement instanceof JsonObjectSchema) {
                JsonObjectSchema obj = (JsonObjectSchema)jsonSchemaElement;
                if (Objects.equals(description, obj.description())) {
                    return obj;
                }
                return obj.toBuilder().description(description).build();
            }
            return jsonSchemaElement;
        }
        String reference = Utils.generateUUIDFrom(type.getName());
        JsonReferenceSchema jsonReferenceSchema = JsonReferenceSchema.builder().reference(reference).build();
        visited.put(type, new VisitedClassMetadata(jsonReferenceSchema, reference, false));
        LinkedHashMap<String, JsonSchemaElement> properties = new LinkedHashMap<String, JsonSchemaElement>();
        ArrayList<String> required = new ArrayList<String>();
        for (Field field : type.getDeclaredFields()) {
            String fieldName = field.getName();
            if (Modifier.isStatic(field.getModifiers()) || fieldName.equals("__$hits$__") || fieldName.startsWith("this$")) continue;
            if (JsonSchemaElementUtils.isRequired(field, areSubFieldsRequiredByDefault)) {
                required.add(fieldName);
            }
            String fieldDescription = JsonSchemaElementUtils.descriptionFrom(field);
            JsonSchemaElement jsonSchemaElement = JsonSchemaElementUtils.jsonSchemaElementFrom(field.getType(), field.getGenericType(), fieldDescription, areSubFieldsRequiredByDefault, visited);
            properties.put(fieldName, jsonSchemaElement);
        }
        JsonObjectSchema.Builder builder = JsonObjectSchema.builder().description(Optional.ofNullable(description).orElse(JsonSchemaElementUtils.descriptionFrom(type))).addProperties(properties).required(required);
        visited.get(type).jsonSchemaElement = builder.build();
        if (setDefinitions) {
            LinkedHashMap<String, JsonSchemaElement> definitions = new LinkedHashMap<String, JsonSchemaElement>();
            visited.forEach((clazz, visitedClassMetadata) -> {
                if (visitedClassMetadata.recursionDetected) {
                    definitions.put(visitedClassMetadata.reference, visitedClassMetadata.jsonSchemaElement);
                }
            });
            if (!definitions.isEmpty()) {
                builder.definitions(definitions);
            }
        }
        return builder.build();
    }

    private static boolean isRequired(Field field, boolean defaultValue) {
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.required();
        }
        return defaultValue;
    }

    private static String descriptionFrom(Field field) {
        return JsonSchemaElementUtils.descriptionFrom(field.getAnnotation(Description.class));
    }

    private static String descriptionFrom(Class<?> type) {
        if (type == UUID.class) {
            return DEFAULT_UUID_DESCRIPTION;
        }
        return JsonSchemaElementUtils.descriptionFrom(type.getAnnotation(Description.class));
    }

    private static String descriptionFrom(Description description) {
        if (description == null) {
            return null;
        }
        return String.join((CharSequence)" ", description.value());
    }

    private static Type collectionElementType(Type type) {
        ParameterizedType parameterizedType;
        Type[] actualTypeArguments;
        if (type instanceof ParameterizedType && (actualTypeArguments = (parameterizedType = (ParameterizedType)type).getActualTypeArguments()).length == 1) {
            return actualTypeArguments[0];
        }
        return null;
    }

    private static Class<?> rawClassOf(Type type) {
        TypeVariable typeVariable;
        Type[] bounds;
        WildcardType wildcardType;
        Type[] upperBounds;
        ParameterizedType parameterizedType;
        if (type == null) {
            return Object.class;
        }
        if (type instanceof Class) {
            Class clazz = (Class)type;
            return clazz;
        }
        if (type instanceof ParameterizedType && (parameterizedType = (ParameterizedType)type).getRawType() instanceof Class) {
            Class raw = (Class)parameterizedType.getRawType();
            return raw;
        }
        if (type instanceof WildcardType && (upperBounds = (wildcardType = (WildcardType)type).getUpperBounds()).length > 0) {
            return JsonSchemaElementUtils.rawClassOf(upperBounds[0]);
        }
        if (type instanceof TypeVariable && (bounds = (typeVariable = (TypeVariable)type).getBounds()).length > 0) {
            return JsonSchemaElementUtils.rawClassOf(bounds[0]);
        }
        if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType)type;
            Class<?> componentClass = JsonSchemaElementUtils.rawClassOf(genericArrayType.getGenericComponentType());
            return Array.newInstance(componentClass, 0).getClass();
        }
        return Object.class;
    }

    static boolean isCustomClass(Class<?> clazz) {
        String packageName;
        return clazz.getPackage() == null || !(packageName = clazz.getPackage().getName()).startsWith("java.") && !packageName.startsWith("javax.") && !packageName.startsWith("jdk.") && !packageName.startsWith("sun.") && !packageName.startsWith("com.sun.");
    }

    public static Map<String, Map<String, Object>> toMap(Map<String, JsonSchemaElement> properties) {
        return JsonSchemaElementUtils.toMap(properties, false);
    }

    public static Map<String, Map<String, Object>> toMap(Map<String, JsonSchemaElement> properties, boolean strict) {
        LinkedHashMap<String, Map<String, Object>> map = new LinkedHashMap<String, Map<String, Object>>();
        properties.forEach((property, value) -> map.put((String)property, JsonSchemaElementUtils.toMap(value, strict)));
        return map;
    }

    public static Map<String, Object> toMap(JsonSchemaElement jsonSchemaElement) {
        return JsonSchemaElementUtils.toMap(jsonSchemaElement, false);
    }

    public static Map<String, Object> toMap(JsonSchemaElement jsonSchemaElement, boolean strict) {
        return JsonSchemaElementUtils.toMap(jsonSchemaElement, strict, true);
    }

    public static Map<String, Object> toMap(JsonSchemaElement jsonSchemaElement, boolean strict, boolean required) {
        return JsonSchemaElementUtils.toMap(jsonSchemaElement, strict, required, null);
    }

    public static Map<String, Object> toMap(JsonSchemaElement jsonSchemaElement, boolean strict, boolean required, String enumType) {
        if (jsonSchemaElement instanceof JsonObjectSchema) {
            JsonObjectSchema jsonObjectSchema = (JsonObjectSchema)jsonSchemaElement;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("type", JsonSchemaElementUtils.type("object", strict, required));
            if (jsonObjectSchema.description() != null) {
                map.put("description", jsonObjectSchema.description());
            }
            LinkedHashMap properties = new LinkedHashMap();
            jsonObjectSchema.properties().forEach((property, value) -> properties.put(property, JsonSchemaElementUtils.toMap(value, strict, jsonObjectSchema.required().contains(property), enumType)));
            map.put("properties", properties);
            if (strict) {
                map.put("required", jsonObjectSchema.properties().keySet().stream().collect(Collectors.toList()));
            } else if (jsonObjectSchema.required() != null) {
                map.put("required", jsonObjectSchema.required());
            }
            if (strict) {
                map.put("additionalProperties", false);
            }
            if (!jsonObjectSchema.definitions().isEmpty()) {
                map.put("$defs", JsonSchemaElementUtils.toMap(jsonObjectSchema.definitions(), strict));
            }
            return map;
        }
        if (jsonSchemaElement instanceof JsonArraySchema) {
            JsonArraySchema jsonArraySchema = (JsonArraySchema)jsonSchemaElement;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("type", JsonSchemaElementUtils.type("array", strict, required));
            if (jsonArraySchema.description() != null) {
                map.put("description", jsonArraySchema.description());
            }
            if (jsonArraySchema.items() != null) {
                map.put("items", JsonSchemaElementUtils.toMap(jsonArraySchema.items(), strict));
            } else {
                map.put("items", Collections.emptyMap());
            }
            return map;
        }
        if (jsonSchemaElement instanceof JsonEnumSchema) {
            JsonEnumSchema jsonEnumSchema = (JsonEnumSchema)jsonSchemaElement;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            if (enumType != null) {
                map.put("type", enumType);
            } else {
                map.put("type", JsonSchemaElementUtils.type("string", strict, required));
            }
            if (jsonEnumSchema.description() != null) {
                map.put("description", jsonEnumSchema.description());
            }
            map.put("enum", jsonEnumSchema.enumValues());
            return map;
        }
        if (jsonSchemaElement instanceof JsonStringSchema) {
            JsonStringSchema jsonStringSchema = (JsonStringSchema)jsonSchemaElement;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("type", JsonSchemaElementUtils.type("string", strict, required));
            if (jsonStringSchema.description() != null) {
                map.put("description", jsonStringSchema.description());
            }
            return map;
        }
        if (jsonSchemaElement instanceof JsonIntegerSchema) {
            JsonIntegerSchema jsonIntegerSchema = (JsonIntegerSchema)jsonSchemaElement;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("type", JsonSchemaElementUtils.type("integer", strict, required));
            if (jsonIntegerSchema.description() != null) {
                map.put("description", jsonIntegerSchema.description());
            }
            return map;
        }
        if (jsonSchemaElement instanceof JsonNumberSchema) {
            JsonNumberSchema jsonNumberSchema = (JsonNumberSchema)jsonSchemaElement;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("type", JsonSchemaElementUtils.type("number", strict, required));
            if (jsonNumberSchema.description() != null) {
                map.put("description", jsonNumberSchema.description());
            }
            return map;
        }
        if (jsonSchemaElement instanceof JsonBooleanSchema) {
            JsonBooleanSchema jsonBooleanSchema = (JsonBooleanSchema)jsonSchemaElement;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("type", JsonSchemaElementUtils.type("boolean", strict, required));
            if (jsonBooleanSchema.description() != null) {
                map.put("description", jsonBooleanSchema.description());
            }
            return map;
        }
        if (jsonSchemaElement instanceof JsonReferenceSchema) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            String reference = ((JsonReferenceSchema)jsonSchemaElement).reference();
            if (reference != null) {
                map.put("$ref", "#/$defs/" + reference);
            }
            return map;
        }
        if (jsonSchemaElement instanceof JsonAnyOfSchema) {
            JsonAnyOfSchema jsonAnyOfSchema = (JsonAnyOfSchema)jsonSchemaElement;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            if (jsonAnyOfSchema.description() != null) {
                map.put("description", jsonAnyOfSchema.description());
            }
            List anyOf = jsonAnyOfSchema.anyOf().stream().map(element -> JsonSchemaElementUtils.toMap(element, strict)).collect(Collectors.toList());
            map.put("anyOf", anyOf);
            return map;
        }
        if (jsonSchemaElement instanceof JsonNullSchema) {
            return Collections.singletonMap("type", "null");
        }
        if (jsonSchemaElement instanceof JsonRawSchema) {
            JsonRawSchema jsonNative = (JsonRawSchema)jsonSchemaElement;
            Map map = Json.fromJson(jsonNative.schema(), Map.class);
            return map;
        }
        throw new IllegalArgumentException("Unknown type: " + jsonSchemaElement.getClass());
    }

    private static Object type(String type, boolean strict, boolean required) {
        if (strict && !required) {
            return new String[]{type, "null"};
        }
        return type;
    }

    static boolean isJsonInteger(Class<?> type) {
        return type == Byte.TYPE || type == Byte.class || type == Short.TYPE || type == Short.class || type == Integer.TYPE || type == Integer.class || type == Long.TYPE || type == Long.class || type == BigInteger.class;
    }

    static boolean isJsonNumber(Class<?> type) {
        return type == Float.TYPE || type == Float.class || type == Double.TYPE || type == Double.class || type == BigDecimal.class;
    }

    static boolean isJsonBoolean(Class<?> type) {
        return type == Boolean.TYPE || type == Boolean.class;
    }

    static boolean isJsonString(Class<?> type) {
        return type == String.class || type == Character.TYPE || type == Character.class || CharSequence.class.isAssignableFrom(type) || type == UUID.class;
    }

    static boolean isJsonArray(Class<?> type) {
        return type.isArray() || Iterable.class.isAssignableFrom(type);
    }

    public static class VisitedClassMetadata {
        public JsonSchemaElement jsonSchemaElement;
        public String reference;
        public boolean recursionDetected;

        public VisitedClassMetadata(JsonSchemaElement jsonSchemaElement, String reference, boolean recursionDetected) {
            this.jsonSchemaElement = jsonSchemaElement;
            this.reference = reference;
            this.recursionDetected = recursionDetected;
        }
    }
}

