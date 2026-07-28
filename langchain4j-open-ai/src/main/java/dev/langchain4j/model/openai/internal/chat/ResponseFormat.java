/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 */
package dev.langchain4j.model.openai.internal.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.chat.JsonSchema;
import dev.langchain4j.model.openai.internal.chat.ResponseFormatType;
import java.util.Objects;

@JsonDeserialize(builder=ResponseFormat.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFormat {
    @JsonProperty
    private final ResponseFormatType type;
    @JsonProperty
    private final JsonSchema jsonSchema;

    @JsonCreator
    public ResponseFormat(Builder builder) {
        this.type = builder.type;
        this.jsonSchema = builder.jsonSchema;
    }

    public ResponseFormatType type() {
        return this.type;
    }

    public JsonSchema jsonSchema() {
        return this.jsonSchema;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof ResponseFormat && this.equalTo((ResponseFormat)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(ResponseFormat another) {
        return Objects.equals((Object)this.type, (Object)another.type) && Objects.equals(this.jsonSchema, another.jsonSchema);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode((Object)this.type);
        h += (h << 5) + Objects.hashCode(this.jsonSchema);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ResponseFormat{type=" + (Object)((Object)this.type) + ", jsonSchema=" + this.jsonSchema + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {
        private ResponseFormatType type;
        private JsonSchema jsonSchema;

        public Builder type(ResponseFormatType type) {
            this.type = type;
            return this;
        }

        public Builder jsonSchema(JsonSchema jsonSchema) {
            this.jsonSchema = jsonSchema;
            return this;
        }

        public ResponseFormat build() {
            return new ResponseFormat(this);
        }
    }
}

