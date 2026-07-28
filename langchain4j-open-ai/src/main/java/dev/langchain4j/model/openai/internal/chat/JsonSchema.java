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
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 */
package dev.langchain4j.model.openai.internal.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import java.util.Map;
import java.util.Objects;

@JsonDeserialize(builder=JsonSchema.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JsonSchema {
    @JsonProperty
    private final String name;
    @JsonProperty
    private final Boolean strict;
    @JsonProperty
    private final Map<String, Object> schema;

    public JsonSchema(Builder builder) {
        this.name = builder.name;
        this.strict = builder.strict;
        this.schema = builder.schema;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof JsonSchema && this.equalTo((JsonSchema)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(JsonSchema another) {
        return Objects.equals(this.name, another.name) && Objects.equals(this.strict, another.strict) && Objects.equals(this.schema, another.schema);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.name);
        h += (h << 5) + Objects.hashCode(this.strict);
        h += (h << 5) + Objects.hashCode(this.schema);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "JsonSchema{name=" + this.name + ", strict=" + this.strict + ", schema=" + this.schema + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {
        private String name;
        private Boolean strict;
        private Map<String, Object> schema;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder strict(Boolean strict) {
            this.strict = strict;
            return this;
        }

        public Builder schema(Map<String, Object> schema) {
            this.schema = schema;
            return this;
        }

        public JsonSchema build() {
            return new JsonSchema(this);
        }
    }
}

