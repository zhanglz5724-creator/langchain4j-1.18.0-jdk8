/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 */
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.model.anthropic.internal.api.AnthropicOutputFormatType;
import dev.langchain4j.model.anthropic.internal.mapper.AnthropicMapper;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import java.util.Map;
import java.util.Objects;

@JsonDeserialize(builder=AnthropicFormat.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicFormat {
    @JsonProperty
    private final AnthropicOutputFormatType type;
    @JsonProperty
    private final Map<String, Object> schema;

    private AnthropicFormat(Builder builder) {
        this.type = builder.type;
        this.schema = builder.schema;
    }

    public AnthropicOutputFormatType getType() {
        return this.type;
    }

    public Map<String, Object> getSchema() {
        return this.schema;
    }

    public static AnthropicFormat fromJsonSchema(JsonSchema schema) {
        return AnthropicFormat.builder().type(AnthropicOutputFormatType.JSON_SCHEMA).schema(AnthropicMapper.toAnthropicSchema(schema.rootElement())).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toString() {
        return "AnthropicFormat[type" + (Object)((Object)this.type) + ", jsonSchema" + this.schema + "]";
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.type, this.schema});
    }

    public boolean equals(Object other) {
        if (!(other instanceof AnthropicFormat)) {
            return false;
        }
        AnthropicFormat responseFormat = (AnthropicFormat)other;
        return this.equalsTo(responseFormat);
    }

    public boolean equalsTo(AnthropicFormat other) {
        return Objects.equals((Object)this.type, (Object)other.type) && Objects.equals(this.schema, other.schema);
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {
        private AnthropicOutputFormatType type;
        private Map<String, Object> schema;

        public Builder type(AnthropicOutputFormatType type) {
            this.type = type;
            return this;
        }

        public Builder schema(Map<String, Object> schema) {
            this.schema = schema;
            return this;
        }

        public AnthropicFormat build() {
            return new AnthropicFormat(this);
        }
    }
}

