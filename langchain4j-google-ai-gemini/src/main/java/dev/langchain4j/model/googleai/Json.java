/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.DeserializationFeature
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.langchain4j.Internal;

@Internal
class Json {
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).setSerializationInclusion(JsonInclude.Include.NON_NULL);
    static final ObjectMapper OBJECT_MAPPER_WITHOUT_INDENT = new ObjectMapper().disable(SerializationFeature.INDENT_OUTPUT);

    Json() {
    }

    static String toJson(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static String toJsonWithoutIndent(Object o) {
        try {
            return OBJECT_MAPPER_WITHOUT_INDENT.writeValueAsString(o);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> T fromJson(String json, Class<T> type) {
        try {
            return (T)OBJECT_MAPPER.readValue(json, type);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> T convertValue(Object fromValue, TypeReference<T> toValue) {
        return (T)OBJECT_MAPPER.convertValue(fromValue, toValue);
    }
}

