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
package dev.langchain4j.model.ollama;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.model.ollama.OllamaModelToolCall;
import java.util.List;
import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OllamaModelMessage {
    private String role;
    private String content;
    private String thinking;
    private List<String> images;
    private List<OllamaModelToolCall> toolCalls;
    private String toolName;

    OllamaModelMessage() {
    }

    public OllamaModelMessage(String role, String content, String thinking, List<String> images, List<OllamaModelToolCall> toolCalls, String toolName) {
        this.role = OllamaModelMessage.normalizeRole(role);
        this.content = content;
        this.thinking = thinking;
        this.images = images;
        this.toolCalls = toolCalls;
        this.toolName = toolName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = OllamaModelMessage.normalizeRole(role);
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThinking() {
        return this.thinking;
    }

    public void setThinking(String thinking) {
        this.thinking = thinking;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<OllamaModelToolCall> getToolCalls() {
        return this.toolCalls;
    }

    public void setToolCalls(List<OllamaModelToolCall> toolCalls) {
        this.toolCalls = toolCalls;
    }

    public String getToolName() {
        return this.toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    private static String normalizeRole(String role) {
        return role == null ? null : role.toLowerCase(Locale.ROOT);
    }

    public static class Builder {
        private String role;
        private String content;
        private String thinking;
        private List<String> images;
        private List<OllamaModelToolCall> toolCalls;
        private String toolName;

        public Builder role(String role) {
            this.role = OllamaModelMessage.normalizeRole(role);
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder thinking(String thinking) {
            this.thinking = thinking;
            return this;
        }

        public Builder images(List<String> images) {
            this.images = images;
            return this;
        }

        public Builder toolCalls(List<OllamaModelToolCall> toolCalls) {
            this.toolCalls = toolCalls;
            return this;
        }

        public Builder toolName(String toolName) {
            this.toolName = toolName;
            return this;
        }

        public OllamaModelMessage build() {
            return new OllamaModelMessage(this.role, this.content, this.thinking, this.images, this.toolCalls, this.toolName);
        }
    }
}

