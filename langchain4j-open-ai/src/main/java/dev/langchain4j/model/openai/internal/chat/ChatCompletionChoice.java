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
import dev.langchain4j.model.openai.internal.chat.AssistantMessage;
import dev.langchain4j.model.openai.internal.chat.Delta;
import dev.langchain4j.model.openai.internal.chat.LogProbs;
import java.util.Objects;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class ChatCompletionChoice {
    @JsonProperty
    private final Integer index;
    @JsonProperty
    private final AssistantMessage message;
    @JsonProperty
    private final Delta delta;
    @JsonProperty
    private final String finishReason;
    @JsonProperty
    private final LogProbs logprobs;

    public ChatCompletionChoice(Builder builder) {
        this.index = builder.index;
        this.message = builder.message;
        this.delta = builder.delta;
        this.finishReason = builder.finishReason;
        this.logprobs = builder.logprobs;
    }

    public Integer index() {
        return this.index;
    }

    public AssistantMessage message() {
        return this.message;
    }

    public Delta delta() {
        return this.delta;
    }

    public String finishReason() {
        return this.finishReason;
    }

    public LogProbs logprobs() {
        return this.logprobs;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof ChatCompletionChoice && this.equalTo((ChatCompletionChoice)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(ChatCompletionChoice another) {
        return Objects.equals(this.index, another.index) && Objects.equals(this.message, another.message) && Objects.equals(this.delta, another.delta) && Objects.equals(this.finishReason, another.finishReason) && Objects.equals(this.logprobs, another.logprobs);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.index);
        h += (h << 5) + Objects.hashCode(this.message);
        h += (h << 5) + Objects.hashCode(this.delta);
        h += (h << 5) + Objects.hashCode(this.finishReason);
        h += (h << 5) + Objects.hashCode(this.logprobs);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ChatCompletionChoice{index=" + this.index + ", message=" + this.message + ", delta=" + this.delta + ", finishReason=" + this.finishReason + ", logprobs=" + this.logprobs + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private Integer index;
        private AssistantMessage message;
        private Delta delta;
        private String finishReason;
        private LogProbs logprobs;

        public Builder index(Integer index) {
            this.index = index;
            return this;
        }

        public Builder message(AssistantMessage message) {
            this.message = message;
            return this;
        }

        public Builder delta(Delta delta) {
            this.delta = delta;
            return this;
        }

        public Builder finishReason(String finishReason) {
            this.finishReason = finishReason;
            return this;
        }

        public Builder logprobs(LogProbs logprobs) {
            this.logprobs = logprobs;
            return this;
        }

        public ChatCompletionChoice build() {
            return new ChatCompletionChoice(this);
        }
    }
}

