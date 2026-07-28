/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicToolSchema {
    public String type = "object";
    @JsonProperty(value="additionalProperties")
    public Boolean additionalProperties;
    public Map<String, Map<String, Object>> properties;
    public List<String> required;
    @JsonProperty(value="$defs")
    public Map<String, Map<String, Object>> defs;

    public AnthropicToolSchema() {
    }

    @Deprecated
    public AnthropicToolSchema(String type, Map<String, Map<String, Object>> properties, List<String> required) {
        this.type = type;
        this.properties = properties;
        this.required = required;
    }

    public AnthropicToolSchema(Builder builder) {
        this.type = builder.type;
        this.additionalProperties = builder.additionalProperties;
        this.properties = builder.properties;
        this.required = builder.required;
        this.defs = builder.defs;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AnthropicToolSchema that = (AnthropicToolSchema)o;
        return Objects.equals(this.type, that.type) && Objects.equals(this.additionalProperties, that.additionalProperties) && Objects.equals(this.properties, that.properties) && Objects.equals(this.required, that.required) && Objects.equals(this.defs, that.defs);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.additionalProperties, this.properties, this.required, this.defs);
    }

    public String toString() {
        return "AnthropicToolSchema{type='" + this.type + '\'' + ", additionalProperties=" + this.additionalProperties + ", properties=" + this.properties + ", required=" + this.required + ", defs=" + this.defs + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String type = "object";
        private Boolean additionalProperties;
        private Map<String, Map<String, Object>> properties;
        private List<String> required;
        private Map<String, Map<String, Object>> defs;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder additionalProperties(Boolean additionalProperties) {
            this.additionalProperties = additionalProperties;
            return this;
        }

        public Builder properties(Map<String, Map<String, Object>> properties) {
            this.properties = properties;
            return this;
        }

        public Builder required(List<String> required) {
            this.required = required;
            return this;
        }

        public Builder defs(Map<String, Map<String, Object>> defs) {
            this.defs = defs;
            return this;
        }

        public AnthropicToolSchema build() {
            return new AnthropicToolSchema(this);
        }
    }
}

