/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 */
package dev.langchain4j.model.huggingface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

class HuggingFaceJsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private HuggingFaceJsonUtils() throws InstantiationException {
        throw new InstantiationException("Can't instantiate this utility class.");
    }

    static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> T fromJson(String jsonStr, TypeReference<T> typeReference) {
        try {
            return (T)OBJECT_MAPPER.readValue(jsonStr, typeReference);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

