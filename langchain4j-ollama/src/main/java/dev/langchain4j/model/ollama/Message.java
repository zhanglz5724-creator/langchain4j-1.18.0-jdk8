/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAnyGetter
 *  com.fasterxml.jackson.annotation.JsonAnySetter
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.ollama;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.model.ollama.Role;
import dev.langchain4j.model.ollama.ToolCall;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
class Message {
    private Role role;
    private String content;
    private String thinking;
    private List<String> images;
    private List<ToolCall> toolCalls;
    private Map<String, Object> additionalFields;

    Message() {
    }

    Message(Builder builder) {
        this.role = builder.role;
        this.content = builder.content;
        this.thinking = builder.thinking;
        this.images = builder.images;
        this.toolCalls = builder.toolCalls;
        this.additionalFields = builder.additionalFields;
    }

    static Builder builder() {
        return new Builder();
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<ToolCall> getToolCalls() {
        return this.toolCalls;
    }

    public void setToolCalls(List<ToolCall> toolCalls) {
        this.toolCalls = toolCalls;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
        return this.additionalFields;
    }

    @JsonAnySetter
    public void setAdditionalFields(Map<String, Object> additionalFields) {
        this.additionalFields = additionalFields;
    }

    public String getThinking() {
        return this.thinking;
    }

    public void setThinking(String thinking) {
        this.thinking = thinking;
    }

    static class Builder {
        private Role role;
        private String content;
        private String thinking;
        private List<String> images;
        private List<ToolCall> toolCalls;
        private Map<String, Object> additionalFields;

        Builder() {
        }

        Builder role(Role role) {
            this.role = role;
            return this;
        }

        Builder content(String content) {
            this.content = content;
            return this;
        }

        Builder thinking(String thinking) {
            this.thinking = thinking;
            return this;
        }

        Builder images(List<String> images) {
            this.images = images;
            return this;
        }

        Builder toolCalls(List<ToolCall> toolCalls) {
            this.toolCalls = toolCalls;
            return this;
        }

        Builder additionalFields(Map<String, Object> additionalFields) {
            this.additionalFields = additionalFields;
            return this;
        }

        Message build() {
            return new Message(this);
        }
    }
}

