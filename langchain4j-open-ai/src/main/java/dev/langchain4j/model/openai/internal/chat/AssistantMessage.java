/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAnyGetter
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.annotation.JsonSetter
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 */
package dev.langchain4j.model.openai.internal.chat;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.chat.FunctionCall;
import dev.langchain4j.model.openai.internal.chat.Message;
import dev.langchain4j.model.openai.internal.chat.Role;
import dev.langchain4j.model.openai.internal.chat.ToolCall;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonDeserialize(builder=AssistantMessage.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class AssistantMessage
implements Message {
    @JsonProperty
    private final Role role = Role.ASSISTANT;
    @JsonProperty
    private final String content;
    @JsonProperty
    private final String reasoningContent;
    @JsonProperty
    private final String name;
    @JsonProperty
    private final List<ToolCall> toolCalls;
    @JsonProperty
    private final String refusal;
    @JsonProperty
    @Deprecated
    private final FunctionCall functionCall;
    @JsonIgnore
    private final Map<String, Object> customParameters;

    public AssistantMessage(Builder builder) {
        this.content = builder.content;
        this.reasoningContent = builder.reasoningContent;
        this.name = builder.name;
        this.toolCalls = builder.toolCalls;
        this.refusal = builder.refusal;
        this.functionCall = builder.functionCall;
        this.customParameters = builder.customParameters;
    }

    @Override
    public Role role() {
        return this.role;
    }

    public String content() {
        return this.content;
    }

    public String reasoningContent() {
        return this.reasoningContent;
    }

    public String name() {
        return this.name;
    }

    public List<ToolCall> toolCalls() {
        return this.toolCalls;
    }

    public String refusal() {
        return this.refusal;
    }

    @Deprecated
    public FunctionCall functionCall() {
        return this.functionCall;
    }

    @JsonAnyGetter
    @JsonInclude(value=JsonInclude.Include.NON_EMPTY)
    public Map<String, Object> customParameters() {
        return this.customParameters;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof AssistantMessage && this.equalTo((AssistantMessage)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(AssistantMessage another) {
        return Objects.equals((Object)this.role, (Object)another.role) && Objects.equals(this.content, another.content) && Objects.equals(this.reasoningContent, another.reasoningContent) && Objects.equals(this.name, another.name) && Objects.equals(this.toolCalls, another.toolCalls) && Objects.equals(this.refusal, another.refusal) && Objects.equals(this.functionCall, another.functionCall) && Objects.equals(this.customParameters, another.customParameters);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode((Object)this.role);
        h += (h << 5) + Objects.hashCode(this.content);
        h += (h << 5) + Objects.hashCode(this.reasoningContent);
        h += (h << 5) + Objects.hashCode(this.name);
        h += (h << 5) + Objects.hashCode(this.toolCalls);
        h += (h << 5) + Objects.hashCode(this.refusal);
        h += (h << 5) + Objects.hashCode(this.functionCall);
        h += (h << 5) + Objects.hashCode(this.customParameters);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "AssistantMessage{role=" + (Object)((Object)this.role) + ", content=" + this.content + ", reasoningContent=" + this.reasoningContent + ", name=" + this.name + ", toolCalls=" + this.toolCalls + ", refusal=" + this.refusal + ", functionCall=" + this.functionCall + ", customParameters=" + this.customParameters + "}";
    }

    public static AssistantMessage from(String content) {
        return AssistantMessage.builder().content(content).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String content;
        private String reasoningContent;
        private String name;
        private List<ToolCall> toolCalls;
        private String refusal;
        @Deprecated
        private FunctionCall functionCall;
        private Map<String, Object> customParameters;

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder reasoningContent(String reasoningContent) {
            this.reasoningContent = reasoningContent;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @JsonSetter
        public Builder toolCalls(List<ToolCall> toolCalls) {
            if (toolCalls != null) {
                this.toolCalls = Collections.unmodifiableList(toolCalls);
            }
            return this;
        }

        @JsonIgnore
        public Builder toolCalls(ToolCall ... toolCalls) {
            return this.toolCalls(Arrays.asList(toolCalls));
        }

        public Builder refusal(String refusal) {
            this.refusal = refusal;
            return this;
        }

        @Deprecated
        public Builder functionCall(FunctionCall functionCall) {
            this.functionCall = functionCall;
            return this;
        }

        public Builder customParameters(Map<String, Object> customParameters) {
            this.customParameters = customParameters;
            return this;
        }

        public Builder customParameter(String key, Object value) {
            if (this.customParameters == null) {
                this.customParameters = new LinkedHashMap<String, Object>();
            }
            this.customParameters.put(key, value);
            return this;
        }

        public AssistantMessage build() {
            return new AssistantMessage(this);
        }
    }
}

