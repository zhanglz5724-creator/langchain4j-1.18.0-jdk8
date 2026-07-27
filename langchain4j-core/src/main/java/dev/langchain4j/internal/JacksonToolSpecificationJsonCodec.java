/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 */
package dev.langchain4j.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.ToolSpecificationJsonCodec;
import dev.langchain4j.internal.ValidationUtils;

class JacksonToolSpecificationJsonCodec
implements ToolSpecificationJsonCodec {
    private final ObjectMapper objectMapper;

    public JacksonToolSpecificationJsonCodec() {
        this(new ObjectMapper());
    }

    public JacksonToolSpecificationJsonCodec(ObjectMapper objectMapper) {
        this.objectMapper = ValidationUtils.ensureNotNull(objectMapper, "objectMapper");
    }

    @Override
    public String toJson(Object object) {
        try {
            return this.objectMapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> type) {
        try {
            return (T)this.objectMapper.readValue(json, type);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

