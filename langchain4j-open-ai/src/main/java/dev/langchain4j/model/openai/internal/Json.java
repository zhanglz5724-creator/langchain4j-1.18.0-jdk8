/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.DeserializationFeature
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 */
package dev.langchain4j.model.openai.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

class Json {
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

    Json() {
    }

    static String toJson(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        }
        catch (JsonProcessingException jpe) {
            throw new RuntimeException(jpe);
        }
    }

    static <T> T fromJson(String json, Class<T> type) {
        try {
            return (T)OBJECT_MAPPER.readValue(json, type);
        }
        catch (JsonProcessingException jpe) {
            throw new RuntimeException(jpe);
        }
    }
}

