/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonRawValue
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCacheControl;
import dev.langchain4j.model.anthropic.internal.api.AnthropicMessageContent;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicToolUseContent
extends AnthropicMessageContent {
    public String id;
    public String name;
    @JsonRawValue
    public String input;

    public AnthropicToolUseContent(String id, String name, String input) {
        super("tool_use");
        this.id = id;
        this.name = name;
        this.input = input;
    }

    public AnthropicToolUseContent(String id, String name, String input, AnthropicCacheControl cacheControl) {
        super("tool_use", cacheControl);
        this.id = id;
        this.name = name;
        this.input = input;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AnthropicToolUseContent that = (AnthropicToolUseContent)o;
        return Objects.equals(this.id, that.id) && Objects.equals(this.name, that.name) && Objects.equals(this.input, that.input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.id, this.name, this.input);
    }

    public String toString() {
        return "AnthropicToolUseContent{input=" + this.input + ", type='" + this.type + '\'' + ", cacheControl=" + this.cacheControl + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String input;
        private AnthropicCacheControl cacheControl;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder input(String input) {
            this.input = input;
            return this;
        }

        public Builder cacheControl(AnthropicCacheControl cacheControl) {
            this.cacheControl = cacheControl;
            return this;
        }

        public AnthropicToolUseContent build() {
            return new AnthropicToolUseContent(this.id, this.name, this.input, this.cacheControl);
        }
    }
}

