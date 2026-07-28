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
 */
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.model.anthropic.internal.api.AnthropicFormat;
import java.util.Objects;

@JsonDeserialize(builder=AnthropicOutputConfig.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicOutputConfig {
    @JsonProperty
    private final AnthropicFormat format;

    private AnthropicOutputConfig(Builder builder) {
        this.format = builder.format;
    }

    public AnthropicFormat getFormat() {
        return this.format;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toString() {
        return "AnthropicOutputConfig[format" + this.format + "]";
    }

    public int hashCode() {
        return Objects.hash(this.format);
    }

    public boolean equals(Object other) {
        if (!(other instanceof AnthropicOutputConfig)) {
            return false;
        }
        AnthropicOutputConfig outputConfig = (AnthropicOutputConfig)other;
        return this.equalsTo(outputConfig);
    }

    public boolean equalsTo(AnthropicOutputConfig other) {
        return Objects.equals(this.format, other.format);
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {
        private AnthropicFormat format;

        public Builder format(AnthropicFormat format) {
            this.format = format;
            return this;
        }

        public AnthropicOutputConfig build() {
            return new AnthropicOutputConfig(this);
        }
    }
}

