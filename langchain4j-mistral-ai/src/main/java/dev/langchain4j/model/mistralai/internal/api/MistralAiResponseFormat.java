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
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.mistralai.internal.api.MistralAiJsonSchema;
import dev.langchain4j.model.mistralai.internal.api.MistralAiResponseFormatType;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=MistralAiResponseFormatBuilder.class)
public class MistralAiResponseFormat {
    private Object type;
    private MistralAiJsonSchema jsonSchema;

    private MistralAiResponseFormat(MistralAiResponseFormatBuilder builder) {
        this.type = builder.type;
        this.jsonSchema = builder.jsonSchema;
    }

    public Object getType() {
        return this.type;
    }

    public MistralAiJsonSchema getJsonSchema() {
        return this.jsonSchema;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        MistralAiResponseFormat that = (MistralAiResponseFormat)o;
        return Objects.equals(this.type, that.type) && Objects.equals(this.jsonSchema, that.jsonSchema);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.jsonSchema);
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiResponseFormat [", "]").add("type=" + this.getType()).add("jsonSchema=" + this.jsonSchema).toString();
    }

    public static MistralAiResponseFormat fromType(MistralAiResponseFormatType type) {
        return MistralAiResponseFormat.builder().type(type.toString()).build();
    }

    public static MistralAiResponseFormat fromSchema(JsonSchema schema) {
        return MistralAiResponseFormat.fromSchema(schema, false);
    }

    public static MistralAiResponseFormat fromSchema(JsonSchema schema, boolean strict) {
        MistralAiJsonSchema mistralAiJsonSchema = MistralAiJsonSchema.fromJsonSchema(schema, strict);
        return MistralAiResponseFormat.builder().type("json_schema").jsonSchema(mistralAiJsonSchema).build();
    }

    public static MistralAiResponseFormatBuilder builder() {
        return new MistralAiResponseFormatBuilder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MistralAiResponseFormatBuilder {
        private Object type;
        private MistralAiJsonSchema jsonSchema;

        private MistralAiResponseFormatBuilder() {
        }

        public MistralAiResponseFormatBuilder type(Object type) {
            this.type = type;
            return this;
        }

        public MistralAiResponseFormatBuilder jsonSchema(MistralAiJsonSchema jsonSchema) {
            this.jsonSchema = jsonSchema;
            return this;
        }

        public MistralAiResponseFormat build() {
            return new MistralAiResponseFormat(this);
        }
    }
}

