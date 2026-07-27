/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.ValidationUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Internal
public class JsonSchemaElementJsonUtils {
    private static final Set<String> STRING_KEYS = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList("type", "description")));
    private static final Set<String> INTEGER_KEYS = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList("type", "description")));
    private static final Set<String> NUMBER_KEYS = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList("type", "description")));
    private static final Set<String> BOOLEAN_KEYS = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList("type", "description")));
    private static final Set<String> NULL_KEYS = Collections.singleton("type");
    private static final Set<String> OBJECT_KEYS = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList("type", "description", "properties", "required", "additionalProperties", "$defs")));
    private static final Set<String> ARRAY_KEYS = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList("type", "description", "items")));
    private static final Set<String> ENUM_KEYS = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList("type", "description", "enum")));
    private static final Set<String> ANYOF_KEYS = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList("anyOf", "description")));
    private static final Set<String> REF_KEYS = Collections.singleton("$ref");

    private JsonSchemaElementJsonUtils() {
    }

    public static Map<String, Object> toMap(JsonSchemaElement element) {
        ValidationUtils.ensureNotNull(element, "element");
        if (element instanceof JsonObjectSchema) {
            return JsonSchemaElementJsonUtils.objectSchemaToMap((JsonObjectSchema)element);
        }
        if (element instanceof JsonArraySchema) {
            return JsonSchemaElementJsonUtils.arraySchemaToMap((JsonArraySchema)element);
        }
        if (element instanceof JsonAnyOfSchema) {
            return JsonSchemaElementJsonUtils.anyOfSchemaToMap((JsonAnyOfSchema)element);
        }
        if (element instanceof JsonEnumSchema) {
            JsonEnumSchema en = (JsonEnumSchema)element;
            Map<String, Object> map = JsonSchemaElementJsonUtils.typedSchema("string", en.description());
            map.put("enum", new ArrayList<String>(en.enumValues()));
            return map;
        }
        if (element instanceof JsonStringSchema) {
            return JsonSchemaElementJsonUtils.typedSchema("string", ((JsonStringSchema)element).description());
        }
        if (element instanceof JsonIntegerSchema) {
            return JsonSchemaElementJsonUtils.typedSchema("integer", ((JsonIntegerSchema)element).description());
        }
        if (element instanceof JsonNumberSchema) {
            return JsonSchemaElementJsonUtils.typedSchema("number", ((JsonNumberSchema)element).description());
        }
        if (element instanceof JsonBooleanSchema) {
            return JsonSchemaElementJsonUtils.typedSchema("boolean", ((JsonBooleanSchema)element).description());
        }
        if (element instanceof JsonNullSchema) {
            return JsonSchemaElementJsonUtils.typedSchema("null", null);
        }
        if (element instanceof JsonReferenceSchema) {
            JsonReferenceSchema ref = (JsonReferenceSchema)element;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            if (ref.reference() != null) {
                map.put("$ref", "#/$defs/" + ref.reference());
            }
            return map;
        }
        if (element instanceof JsonRawSchema) {
            JsonRawSchema raw = (JsonRawSchema)element;
            Map map = Json.fromJson(raw.schema(), Map.class);
            return map;
        }
        throw new IllegalArgumentException("Unknown JsonSchemaElement type: " + element.getClass());
    }

    private static Map<String, Object> typedSchema(String type, String description) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("type", type);
        if (description != null) {
            map.put("description", description);
        }
        return map;
    }

    private static Map<String, Object> objectSchemaToMap(JsonObjectSchema obj) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("type", "object");
        if (obj.description() != null) {
            map.put("description", obj.description());
        }
        if (obj.properties() != null && !obj.properties().isEmpty()) {
            LinkedHashMap properties = new LinkedHashMap();
            obj.properties().forEach((name, schema) -> properties.put(name, JsonSchemaElementJsonUtils.toMap(schema)));
            map.put("properties", properties);
        }
        if (obj.required() != null && !obj.required().isEmpty()) {
            map.put("required", new ArrayList<String>(obj.required()));
        }
        if (obj.additionalProperties() != null) {
            map.put("additionalProperties", obj.additionalProperties());
        }
        if (obj.definitions() != null && !obj.definitions().isEmpty()) {
            LinkedHashMap defs = new LinkedHashMap();
            obj.definitions().forEach((name, schema) -> defs.put(name, JsonSchemaElementJsonUtils.toMap(schema)));
            map.put("$defs", defs);
        }
        return map;
    }

    private static Map<String, Object> arraySchemaToMap(JsonArraySchema arr) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("type", "array");
        if (arr.description() != null) {
            map.put("description", arr.description());
        }
        if (arr.items() != null) {
            map.put("items", JsonSchemaElementJsonUtils.toMap(arr.items()));
        }
        return map;
    }

    private static Map<String, Object> anyOfSchemaToMap(JsonAnyOfSchema anyOf) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        if (anyOf.description() != null) {
            map.put("description", anyOf.description());
        }
        ArrayList schemas = new ArrayList(anyOf.anyOf().size());
        anyOf.anyOf().forEach(s -> schemas.add(JsonSchemaElementJsonUtils.toMap(s)));
        map.put("anyOf", schemas);
        return map;
    }

    public static JsonSchemaElement fromMap(Map<String, Object> map) {
        String type;
        ValidationUtils.ensureNotNull(map, "map");
        if (map.containsKey("$ref")) {
            Object refObj = map.get("$ref");
            if (!(refObj instanceof String)) {
                throw new IllegalArgumentException("\"$ref\" must be a string, but was: " + JsonSchemaElementJsonUtils.className(refObj));
            }
            String ref = (String)refObj;
            if (!ref.startsWith("#/$defs/") || !JsonSchemaElementJsonUtils.isRepresentable(map, REF_KEYS)) {
                return JsonSchemaElementJsonUtils.rawFallback(map);
            }
            String reference = ref.substring("#/$defs/".length());
            return JsonReferenceSchema.builder().reference(reference).build();
        }
        if (map.containsKey("anyOf")) {
            Object anyOfObj = map.get("anyOf");
            if (!(anyOfObj instanceof List)) {
                throw new IllegalArgumentException("\"anyOf\" must be a list, but was: " + JsonSchemaElementJsonUtils.className(anyOfObj));
            }
            if (!JsonSchemaElementJsonUtils.isRepresentable(map, ANYOF_KEYS)) {
                return JsonSchemaElementJsonUtils.rawFallback(map);
            }
            List anyOfList = (List)anyOfObj;
            ArrayList<JsonSchemaElement> anyOf = new ArrayList<JsonSchemaElement>(anyOfList.size());
            for (Object element : anyOfList) {
                if (!(element instanceof Map)) {
                    throw new IllegalArgumentException("\"anyOf\" elements must be JSON objects, but found: " + JsonSchemaElementJsonUtils.className(element));
                }
                anyOf.add(JsonSchemaElementJsonUtils.fromMap((Map)element));
            }
            return JsonAnyOfSchema.builder().description(JsonSchemaElementJsonUtils.optionalString(map, "description")).anyOf(anyOf).build();
        }
        if (map.containsKey("enum")) {
            Object enumObj = map.get("enum");
            if (!(enumObj instanceof List)) {
                throw new IllegalArgumentException("\"enum\" must be a list, but was: " + JsonSchemaElementJsonUtils.className(enumObj));
            }
            List enumList = (List)enumObj;
            Object enumTypeObj = map.get("type");
            if (!JsonSchemaElementJsonUtils.isRepresentable(map, ENUM_KEYS) || !JsonSchemaElementJsonUtils.allStrings(enumList) || enumTypeObj != null && !"string".equals(enumTypeObj)) {
                return JsonSchemaElementJsonUtils.rawFallback(map);
            }
            List<String> enumValues = JsonSchemaElementJsonUtils.requireStringList("enum", enumList);
            return JsonEnumSchema.builder().description(JsonSchemaElementJsonUtils.optionalString(map, "description")).enumValues(enumValues).build();
        }
        Object typeObj = map.get("type");
        if (!(typeObj instanceof String)) {
            return JsonSchemaElementJsonUtils.rawFallback(map);
        }
        switch (type = (String)typeObj) {
            case "string": {
                return JsonSchemaElementJsonUtils.isRepresentable(map, STRING_KEYS) ? JsonStringSchema.builder().description(JsonSchemaElementJsonUtils.optionalString(map, "description")).build() : JsonSchemaElementJsonUtils.rawFallback(map);
            }
            case "integer": {
                return JsonSchemaElementJsonUtils.isRepresentable(map, INTEGER_KEYS) ? JsonIntegerSchema.builder().description(JsonSchemaElementJsonUtils.optionalString(map, "description")).build() : JsonSchemaElementJsonUtils.rawFallback(map);
            }
            case "number": {
                return JsonSchemaElementJsonUtils.isRepresentable(map, NUMBER_KEYS) ? JsonNumberSchema.builder().description(JsonSchemaElementJsonUtils.optionalString(map, "description")).build() : JsonSchemaElementJsonUtils.rawFallback(map);
            }
            case "boolean": {
                return JsonSchemaElementJsonUtils.isRepresentable(map, BOOLEAN_KEYS) ? JsonBooleanSchema.builder().description(JsonSchemaElementJsonUtils.optionalString(map, "description")).build() : JsonSchemaElementJsonUtils.rawFallback(map);
            }
            case "null": {
                return JsonSchemaElementJsonUtils.isRepresentable(map, NULL_KEYS) ? new JsonNullSchema() : JsonSchemaElementJsonUtils.rawFallback(map);
            }
            case "object": {
                Object defsObj;
                if (!JsonSchemaElementJsonUtils.isRepresentable(map, OBJECT_KEYS)) {
                    return JsonSchemaElementJsonUtils.rawFallback(map);
                }
                Object additionalProps = map.get("additionalProperties");
                if (additionalProps != null && !(additionalProps instanceof Boolean)) {
                    return JsonSchemaElementJsonUtils.rawFallback(map);
                }
                JsonObjectSchema.Builder builder = JsonObjectSchema.builder().description(JsonSchemaElementJsonUtils.optionalString(map, "description"));
                Object propertiesObj = map.get("properties");
                if (propertiesObj instanceof Map) {
                    Map properties = (Map)propertiesObj;
                    LinkedHashMap<String, JsonSchemaElement> schemaProperties = new LinkedHashMap<String, JsonSchemaElement>();
                    properties.forEach((name, propValue) -> {
                        if (!(propValue instanceof Map)) {
                            throw new IllegalArgumentException("Property \"" + name + "\" must be a JSON object, but was: " + JsonSchemaElementJsonUtils.className(propValue));
                        }
                        schemaProperties.put((String)name, JsonSchemaElementJsonUtils.fromMap((Map)propValue));
                    });
                    builder.addProperties(schemaProperties);
                } else if (propertiesObj != null) {
                    throw new IllegalArgumentException("\"properties\" must be a JSON object, but was: " + JsonSchemaElementJsonUtils.className(propertiesObj));
                }
                Object requiredObj = map.get("required");
                if (requiredObj instanceof List) {
                    builder.required(JsonSchemaElementJsonUtils.requireStringList("required", (List)requiredObj));
                } else if (requiredObj != null) {
                    throw new IllegalArgumentException("\"required\" must be a list, but was: " + JsonSchemaElementJsonUtils.className(requiredObj));
                }
                if (additionalProps instanceof Boolean) {
                    builder.additionalProperties((Boolean)additionalProps);
                }
                if ((defsObj = map.get("$defs")) instanceof Map) {
                    Map defs = (Map)defsObj;
                    LinkedHashMap<String, JsonSchemaElement> definitions = new LinkedHashMap<String, JsonSchemaElement>();
                    defs.forEach((name, defValue) -> {
                        if (!(defValue instanceof Map)) {
                            throw new IllegalArgumentException("Definition \"" + name + "\" must be a JSON object, but was: " + JsonSchemaElementJsonUtils.className(defValue));
                        }
                        definitions.put((String)name, JsonSchemaElementJsonUtils.fromMap((Map)defValue));
                    });
                    builder.definitions(definitions);
                } else if (defsObj != null) {
                    throw new IllegalArgumentException("\"$defs\" must be a JSON object, but was: " + JsonSchemaElementJsonUtils.className(defsObj));
                }
                return builder.build();
            }
            case "array": {
                if (!JsonSchemaElementJsonUtils.isRepresentable(map, ARRAY_KEYS)) {
                    return JsonSchemaElementJsonUtils.rawFallback(map);
                }
                JsonArraySchema.Builder builder = JsonArraySchema.builder().description(JsonSchemaElementJsonUtils.optionalString(map, "description"));
                Object itemsObj = map.get("items");
                if (itemsObj instanceof Map) {
                    builder.items(JsonSchemaElementJsonUtils.fromMap((Map)itemsObj));
                } else if (itemsObj != null) {
                    throw new IllegalArgumentException("\"items\" must be a JSON object, but was: " + JsonSchemaElementJsonUtils.className(itemsObj));
                }
                return builder.build();
            }
        }
        return JsonSchemaElementJsonUtils.rawFallback(map);
    }

    private static boolean isRepresentable(Map<String, Object> map, Set<String> allowedKeys) {
        return allowedKeys.containsAll(map.keySet()) && !JsonSchemaElementJsonUtils.hasNullValue(map);
    }

    private static boolean hasNullValue(Map<String, Object> map) {
        for (Object value : map.values()) {
            if (value != null) continue;
            return true;
        }
        return false;
    }

    private static JsonRawSchema rawFallback(Map<String, Object> map) {
        return JsonRawSchema.from(Json.toJson(map));
    }

    private static boolean allStrings(List<?> list) {
        return list.stream().allMatch(String.class::isInstance);
    }

    private static String optionalString(Map<String, Object> map, String field) {
        Object value = map.get(field);
        if (value == null) {
            return null;
        }
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("\"" + field + "\" must be a string, but was: " + JsonSchemaElementJsonUtils.className(value));
        }
        return (String)value;
    }

    private static String className(Object obj) {
        return obj == null ? "null" : obj.getClass().getSimpleName();
    }

    private static List<String> requireStringList(String fieldName, List<?> list) {
        ArrayList<String> result = new ArrayList<String>(list.size());
        for (Object element : list) {
            if (!(element instanceof String)) {
                throw new IllegalArgumentException("\"" + fieldName + "\" elements must be strings, but found: " + JsonSchemaElementJsonUtils.className(element));
            }
            result.add((String)element);
        }
        return result;
    }
}

