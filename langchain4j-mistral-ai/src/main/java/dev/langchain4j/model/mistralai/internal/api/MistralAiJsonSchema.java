/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import java.util.Map;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=Builder.class)
public class MistralAiJsonSchema {
    private final String name;
    private final String description;
    private final Map<String, Object> schema;
    private final boolean strict;

    public MistralAiJsonSchema(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.schema = builder.schema;
        this.strict = builder.strict;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Map<String, Object> getSchema() {
        return this.schema;
    }

    public boolean isStrict() {
        return this.strict;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static MistralAiJsonSchema fromJsonSchema(JsonSchema schema, boolean strict) {
        return MistralAiJsonSchema.builder().name(schema.name()).schema(JsonSchemaElementUtils.toMap((JsonSchemaElement)schema.rootElement(), (boolean)strict)).strict(strict).build();
    }

    public static MistralAiJsonSchema fromJsonSchema(JsonSchema schema) {
        return MistralAiJsonSchema.fromJsonSchema(schema, false);
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {
        private String name;
        private String description;
        private Map<String, Object> schema;
        private boolean strict;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder schema(Map<String, Object> schema) {
            this.schema = schema;
            return this;
        }

        public Builder strict(boolean strict) {
            this.strict = strict;
            return this;
        }

        public MistralAiJsonSchema build() {
            return new MistralAiJsonSchema(this);
        }
    }
}

