/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.qdrant.client.grpc.JsonWithInt$ListValue
 *  io.qdrant.client.grpc.JsonWithInt$Value
 */
package dev.langchain4j.store.embedding.qdrant;

import io.qdrant.client.grpc.JsonWithInt;
import java.util.Map;
import java.util.stream.Collectors;

class ObjectFactory {
    private ObjectFactory() {
    }

    public static Object object(JsonWithInt.Value value) {
        switch (value.getKindCase()) {
            case INTEGER_VALUE: {
                return value.getIntegerValue();
            }
            case STRING_VALUE: {
                return value.getStringValue();
            }
            case DOUBLE_VALUE: {
                return value.getDoubleValue();
            }
            case BOOL_VALUE: {
                return value.getBoolValue();
            }
            case LIST_VALUE: {
                return ObjectFactory.object(value.getListValue());
            }
            case STRUCT_VALUE: {
                return ObjectFactory.objectMap(value.getStructValue().getFieldsMap());
            }
            case NULL_VALUE: {
                return null;
            }
        }
        throw new IllegalArgumentException("Unknown value type: " + value.getKindCase());
    }

    private static Map<String, Object> objectMap(Map<String, JsonWithInt.Value> payload) {
        return payload.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> ObjectFactory.object((JsonWithInt.Value)e.getValue())));
    }

    private static Object object(JsonWithInt.ListValue listValue) {
        return listValue.getValuesList().stream().map(ObjectFactory::object).collect(Collectors.toList());
    }
}

