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
import dev.langchain4j.model.openai.internal.chat.FunctionCall;
import dev.langchain4j.model.openai.internal.chat.ToolCall;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class Delta {
    @JsonProperty
    private final String role;
    @JsonProperty
    private final String content;
    @JsonProperty
    private final String reasoningContent;
    @JsonProperty
    private final List<ToolCall> toolCalls;
    @JsonProperty
    @Deprecated
    private final FunctionCall functionCall;

    public Delta(Builder builder) {
        this.role = builder.role;
        this.content = builder.content;
        this.reasoningContent = builder.reasoningContent;
        this.toolCalls = builder.toolCalls;
        this.functionCall = builder.functionCall;
    }

    public String role() {
        return this.role;
    }

    public String content() {
        return this.content;
    }

    public String reasoningContent() {
        return this.reasoningContent;
    }

    public List<ToolCall> toolCalls() {
        return this.toolCalls;
    }

    @Deprecated
    public FunctionCall functionCall() {
        return this.functionCall;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof Delta && this.equalTo((Delta)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(Delta another) {
        return Objects.equals(this.role, another.role) && Objects.equals(this.content, another.content) && Objects.equals(this.reasoningContent, another.reasoningContent) && Objects.equals(this.toolCalls, another.toolCalls) && Objects.equals(this.functionCall, another.functionCall);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.role);
        h += (h << 5) + Objects.hashCode(this.content);
        h += (h << 5) + Objects.hashCode(this.reasoningContent);
        h += (h << 5) + Objects.hashCode(this.toolCalls);
        h += (h << 5) + Objects.hashCode(this.functionCall);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "Delta{role=" + this.role + ", content=" + this.content + ", reasoningContent=" + this.reasoningContent + ", toolCalls=" + this.toolCalls + ", functionCall=" + this.functionCall + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String role;
        private String content;
        private String reasoningContent;
        private List<ToolCall> toolCalls;
        @Deprecated
        private FunctionCall functionCall;

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder reasoningContent(String reasoningContent) {
            this.reasoningContent = reasoningContent;
            return this;
        }

        public Builder toolCalls(List<ToolCall> toolCalls) {
            if (toolCalls != null) {
                this.toolCalls = Collections.unmodifiableList(toolCalls);
            }
            return this;
        }

        @Deprecated
        public Builder functionCall(FunctionCall functionCall) {
            this.functionCall = functionCall;
            return this;
        }

        public Delta build() {
            return new Delta(this);
        }
    }
}

