/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.model.ollama;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.langchain4j.Internal;

@Internal
class OllamaJsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final ObjectMapper OBJECT_MAPPER_WITHOUT_IDENT = new ObjectMapper().disable(SerializationFeature.INDENT_OUTPUT);

    private OllamaJsonUtils() throws InstantiationException {
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

    static String toJsonWithoutIdent(Object object) {
        try {
            return OBJECT_MAPPER_WITHOUT_IDENT.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> T fromJson(String jsonStr, Class<T> clazz) {
        try {
            return (T)OBJECT_MAPPER.readValue(jsonStr, clazz);
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

