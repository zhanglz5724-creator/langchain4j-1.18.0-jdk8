/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAnyGetter
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCacheControl;
import dev.langchain4j.model.anthropic.internal.api.AnthropicToolSchema;
import java.util.Map;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicTool {
    public String name;
    public String description;
    public AnthropicToolSchema inputSchema;
    public AnthropicCacheControl cacheControl;
    @JsonIgnore
    public Map<String, Object> customParameters;
    public Boolean strict;

    public AnthropicTool() {
    }

    public AnthropicTool(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.inputSchema = builder.inputSchema;
        this.cacheControl = builder.cacheControl;
        this.customParameters = builder.customParameters;
        this.strict = builder.strict;
    }

    @Deprecated
    public AnthropicTool(String name, String description, AnthropicToolSchema inputSchema, AnthropicCacheControl cacheControl) {
        this.name = name;
        this.description = description;
        this.inputSchema = inputSchema;
        this.cacheControl = cacheControl;
    }

    @JsonAnyGetter
    public Map<String, Object> customParameters() {
        return this.customParameters;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AnthropicTool that = (AnthropicTool)o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.description, that.description) && Objects.equals(this.inputSchema, that.inputSchema) && Objects.equals(this.cacheControl, that.cacheControl) && Objects.equals(this.customParameters, that.customParameters) && Objects.equals(this.strict, that.strict);
    }

    public int hashCode() {
        return Objects.hash(this.name, this.description, this.inputSchema, this.cacheControl, this.customParameters, this.strict);
    }

    public String toString() {
        return "AnthropicTool{name='" + this.name + '\'' + ", description='" + this.description + '\'' + ", inputSchema=" + this.inputSchema + ", cacheControl=" + this.cacheControl + ", customParameters=" + this.customParameters + ", strict=" + this.strict + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private AnthropicToolSchema inputSchema;
        private AnthropicCacheControl cacheControl;
        private Map<String, Object> customParameters;
        private Boolean strict;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder inputSchema(AnthropicToolSchema inputSchema) {
            this.inputSchema = inputSchema;
            return this;
        }

        public Builder cacheControl(AnthropicCacheControl cacheControl) {
            this.cacheControl = cacheControl;
            return this;
        }

        public Builder customParameters(Map<String, Object> customParameters) {
            this.customParameters = customParameters;
            return this;
        }

        public Builder strict(Boolean strict) {
            this.strict = strict;
            return this;
        }

        public AnthropicTool build() {
            return new AnthropicTool(this);
        }
    }
}

