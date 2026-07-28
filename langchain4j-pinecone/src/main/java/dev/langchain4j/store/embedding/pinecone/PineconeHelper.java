/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.protobuf.Struct
 *  com.google.protobuf.Struct$Builder
 *  com.google.protobuf.Value
 *  dev.langchain4j.data.segment.TextSegment
 */
package dev.langchain4j.store.embedding.pinecone;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import dev.langchain4j.data.segment.TextSegment;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class PineconeHelper {
    private PineconeHelper() throws InstantiationException {
        throw new InstantiationException("can't instantiate this class");
    }

    public static Map<String, Object> structToMetadata(Map<String, Value> filedsMap, String metadataTextKey) {
        if (filedsMap.size() == 1 && filedsMap.containsKey(metadataTextKey) || filedsMap.isEmpty()) {
            return new HashMap<String, Object>();
        }
        HashMap<String, Object> metadataMap = new HashMap<String, Object>(filedsMap.size() - 1);
        for (Map.Entry<String, Value> entry : filedsMap.entrySet()) {
            String key = entry.getKey();
            Value value = entry.getValue();
            if (value.hasNumberValue()) {
                metadataMap.put(key, value.getNumberValue());
                continue;
            }
            if (!value.hasStringValue()) continue;
            metadataMap.put(key, value.getStringValue());
        }
        return metadataMap;
    }

    public static Struct metadataToStruct(TextSegment textSegment, String metadataTextKey) {
        Map metadata = textSegment.metadata().toMap();
        Struct.Builder metadataBuilder = Struct.newBuilder().putFields(metadataTextKey, Value.newBuilder().setStringValue(textSegment.text()).build());
        if (!metadata.isEmpty()) {
            for (Map.Entry entry : metadata.entrySet()) {
                String key = (String)entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String || value instanceof UUID) {
                    metadataBuilder.putFields(key, Value.newBuilder().setStringValue(value.toString()).build());
                    continue;
                }
                if (!(value instanceof Integer) && !(value instanceof Long) && !(value instanceof Float) && !(value instanceof Double)) continue;
                metadataBuilder.putFields(key, Value.newBuilder().setNumberValue(((Number)value).doubleValue()).build());
            }
        }
        return metadataBuilder.build();
    }
}

