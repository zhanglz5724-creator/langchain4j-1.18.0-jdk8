/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Map;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicContent {
    public String type;
    public String text;
    public String id;
    public String name;
    public Map<String, Object> input;
    public String thinking;
    public String signature;
    public String data;
    public String toolUseId;
    public Object content;

    public AnthropicContent() {
    }

    private AnthropicContent(Builder builder) {
        this.type = builder.type;
        this.text = builder.text;
        this.id = builder.id;
        this.name = builder.name;
        this.input = builder.input;
        this.thinking = builder.thinking;
        this.signature = builder.signature;
        this.data = builder.data;
        this.toolUseId = builder.toolUseId;
        this.content = builder.content;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int hashCode() {
        return Objects.hash(this.type, this.text, this.id, this.name, this.input, this.thinking, this.signature, this.data, this.toolUseId, this.content);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AnthropicContent)) {
            return false;
        }
        AnthropicContent that = (AnthropicContent)obj;
        return Objects.equals(this.type, that.type) && Objects.equals(this.text, that.text) && Objects.equals(this.id, that.id) && Objects.equals(this.name, that.name) && Objects.equals(this.input, that.input) && Objects.equals(this.thinking, that.thinking) && Objects.equals(this.signature, that.signature) && Objects.equals(this.data, that.data) && Objects.equals(this.toolUseId, that.toolUseId) && Objects.equals(this.content, that.content);
    }

    public String toString() {
        return "AnthropicContent{type='" + this.type + '\'' + ", text='" + this.text + '\'' + ", id='" + this.id + '\'' + ", name='" + this.name + '\'' + ", input=" + this.input + ", thinking='" + this.thinking + '\'' + ", signature='" + this.signature + '\'' + ", data='" + this.data + '\'' + ", toolUseId='" + this.toolUseId + '\'' + ", content=" + this.content + '}';
    }

    public static class Builder {
        private String type;
        private String text;
        private String id;
        private String name;
        private Map<String, Object> input;
        private String thinking;
        private String signature;
        private String data;
        private String toolUseId;
        private Object content;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder input(Map<String, Object> input) {
            this.input = input;
            return this;
        }

        public Builder thinking(String thinking) {
            this.thinking = thinking;
            return this;
        }

        public Builder signature(String signature) {
            this.signature = signature;
            return this;
        }

        public Builder data(String data) {
            this.data = data;
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

        public AnthropicContent build() {
            return new AnthropicContent(this);
        }
    }
}

