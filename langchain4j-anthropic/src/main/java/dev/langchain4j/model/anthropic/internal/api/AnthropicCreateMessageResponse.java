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
import dev.langchain4j.model.anthropic.internal.api.AnthropicContent;
import dev.langchain4j.model.anthropic.internal.api.AnthropicDiagnostics;
import dev.langchain4j.model.anthropic.internal.api.AnthropicUsage;
import java.util.List;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicCreateMessageResponse {
    public String id;
    public String type;
    public String role;
    public List<AnthropicContent> content;
    public String model;
    public String stopReason;
    public String stopSequence;
    public AnthropicUsage usage;
    public AnthropicDiagnostics diagnostics;

    public AnthropicCreateMessageResponse() {
    }

    private AnthropicCreateMessageResponse(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.role = builder.role;
        this.content = builder.content;
        this.model = builder.model;
        this.stopReason = builder.stopReason;
        this.stopSequence = builder.stopSequence;
        this.usage = builder.usage;
        this.diagnostics = builder.diagnostics;
    }

    public int hashCode() {
        return Objects.hash(this.id, this.type, this.role, this.content, this.model, this.stopReason, this.stopSequence, this.usage, this.diagnostics);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AnthropicCreateMessageResponse)) {
            return false;
        }
        AnthropicCreateMessageResponse that = (AnthropicCreateMessageResponse)obj;
        return Objects.equals(this.id, that.id) && Objects.equals(this.type, that.type) && Objects.equals(this.role, that.role) && Objects.equals(this.content, that.content) && Objects.equals(this.model, that.model) && Objects.equals(this.stopReason, that.stopReason) && Objects.equals(this.stopSequence, that.stopSequence) && Objects.equals(this.usage, that.usage) && Objects.equals(this.diagnostics, that.diagnostics);
    }

    public String toString() {
        return "AnthropicCreateMessageResponse{id='" + this.id + '\'' + ", type='" + this.type + '\'' + ", role='" + this.role + '\'' + ", content=" + this.content + ", model='" + this.model + '\'' + ", stopReason='" + this.stopReason + '\'' + ", stopSequence='" + this.stopSequence + '\'' + ", usage=" + this.usage + ", diagnostics=" + this.diagnostics + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String type;
        private String role;
        private List<AnthropicContent> content;
        private String model;
        private String stopReason;
        private String stopSequence;
        private AnthropicUsage usage;
        private AnthropicDiagnostics diagnostics;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder content(List<AnthropicContent> content) {
            this.content = content;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder stopReason(String stopReason) {
            this.stopReason = stopReason;
            return this;
        }

        public Builder stopSequence(String stopSequence) {
            this.stopSequence = stopSequence;
            return this;
        }

        public Builder usage(AnthropicUsage usage) {
            this.usage = usage;
            return this;
        }

        public Builder diagnostics(AnthropicDiagnostics diagnostics) {
            this.diagnostics = diagnostics;
            return this;
        }

        public AnthropicCreateMessageResponse build() {
            return new AnthropicCreateMessageResponse(this);
        }
    }
}

