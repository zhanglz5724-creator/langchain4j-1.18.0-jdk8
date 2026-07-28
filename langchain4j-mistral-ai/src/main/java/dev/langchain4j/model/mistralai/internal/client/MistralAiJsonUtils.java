/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.model.mistralai.internal.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.langchain4j.Internal;
import java.io.IOException;

@Internal
class MistralAiJsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    MistralAiJsonUtils() {
    }

    static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return (T)OBJECT_MAPPER.readValue(json, clazz);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

