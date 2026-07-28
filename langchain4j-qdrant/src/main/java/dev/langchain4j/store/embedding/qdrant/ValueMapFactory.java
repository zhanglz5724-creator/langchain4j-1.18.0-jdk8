/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.qdrant.client.ValueFactory
 *  io.qdrant.client.grpc.JsonWithInt$Struct
 *  io.qdrant.client.grpc.JsonWithInt$Struct$Builder
 *  io.qdrant.client.grpc.JsonWithInt$Value
 */
package dev.langchain4j.store.embedding.qdrant;

import io.qdrant.client.ValueFactory;
import io.qdrant.client.grpc.JsonWithInt;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

class ValueMapFactory {
    private ValueMapFactory() {
    }

    public static Map<String, JsonWithInt.Value> valueMap(Map<String, Object> inputMap) {
        return inputMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> ValueMapFactory.value(e.getValue())));
    }

    private static JsonWithInt.Value value(Object value) {
        if (value == null) {
            return ValueFactory.nullValue();
        }
        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            Object[] objectArray = new Object[length];
            for (int i = 0; i < length; ++i) {
                objectArray[i] = Array.get(value, i);
            }
            return ValueMapFactory.value(objectArray);
        }
        if (value instanceof Map) {
            return ValueMapFactory.value((Map)value);
        }
        switch (value.getClass().getSimpleName()) {
            case "UUID": {
                return ValueFactory.value((String)value.toString());
            }
            case "String": {
                return ValueFactory.value((String)((String)value));
            }
            case "Integer": {
                return ValueFactory.value((long)((Integer)value).intValue());
            }
            case "Long": {
                return ValueFactory.value((long)((Long)value));
            }
            case "Double": {
                return ValueFactory.value((double)((Double)value));
            }
            case "Float": {
                return ValueFactory.value((double)((Float)value).floatValue());
            }
            case "Boolean": {
                return ValueFactory.value((boolean)((Boolean)value));
            }
        }
        throw new IllegalArgumentException("Unsupported Qdrant value type: " + value.getClass());
    }

    private static JsonWithInt.Value value(Object[] elements) {
        ArrayList<JsonWithInt.Value> values = new ArrayList<JsonWithInt.Value>(elements.length);
        for (Object element : elements) {
            values.add(ValueMapFactory.value(element));
        }
        return ValueFactory.list(values);
    }

    private static JsonWithInt.Value value(Map<String, Object> inputMap) {
        JsonWithInt.Struct.Builder structBuilder = JsonWithInt.Struct.newBuilder();
        Map<String, JsonWithInt.Value> map = ValueMapFactory.valueMap(inputMap);
        structBuilder.putAllFields(map);
        return JsonWithInt.Value.newBuilder().setStructValue(structBuilder).build();
    }
}

