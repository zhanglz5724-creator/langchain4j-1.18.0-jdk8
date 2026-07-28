/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 */
package dev.langchain4j.model.anthropic;

import dev.langchain4j.Experimental;
import java.util.Objects;

@Experimental
public class AnthropicServerToolResult {
    private final String type;
    private final String toolUseId;
    private final Object content;

    public AnthropicServerToolResult(Builder builder) {
        this.type = builder.type;
        this.toolUseId = builder.toolUseId;
        this.content = builder.content;
    }

    public String type() {
        return this.type;
    }

    public String toolUseId() {
        return this.toolUseId;
    }

    public Object content() {
        return this.content;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AnthropicServerToolResult that = (AnthropicServerToolResult)o;
        return Objects.equals(this.type, that.type) && Objects.equals(this.toolUseId, that.toolUseId) && Objects.equals(this.content, that.content);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.toolUseId, this.content);
    }

    public String toString() {
        return "AnthropicServerToolResult{type='" + this.type + '\'' + ", toolUseId='" + this.toolUseId + '\'' + ", content=" + this.content + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String type;
        private String toolUseId;
        private Object content;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder toolUseId(String toolUseId) {
            this.toolUseId = toolUseId;
            return this;
        }

        public Builder content(Object content) {
            this.content = content;
            return this;
        }

        public AnthropicServerToolResult build() {
            return new AnthropicServerToolResult(this);
        }
    }
}

