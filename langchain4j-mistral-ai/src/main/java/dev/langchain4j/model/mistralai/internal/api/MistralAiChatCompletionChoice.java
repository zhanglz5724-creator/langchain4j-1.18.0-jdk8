/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatMessage;
import dev.langchain4j.model.mistralai.internal.api.MistralAiDeltaMessage;
import dev.langchain4j.model.mistralai.internal.api.MistralAiUsage;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=MistralAiChatCompletionChoiceBuilder.class)
public class MistralAiChatCompletionChoice {
    private Integer index;
    private MistralAiChatMessage message;
    private MistralAiDeltaMessage delta;
    private String finishReason;
    private MistralAiUsage usage;

    private MistralAiChatCompletionChoice(MistralAiChatCompletionChoiceBuilder builder) {
        this.index = builder.index;
        this.message = builder.message;
        this.delta = builder.delta;
        this.finishReason = builder.finishReason;
        this.usage = builder.usage;
    }

    public Integer getIndex() {
        return this.index;
    }

    public MistralAiChatMessage getMessage() {
        return this.message;
    }

    public MistralAiDeltaMessage getDelta() {
        return this.delta;
    }

    public String getFinishReason() {
        return this.finishReason;
    }

    public MistralAiUsage getUsage() {
        return this.usage;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MistralAiChatCompletionChoice other = (MistralAiChatCompletionChoice)obj;
        return Objects.equals(this.finishReason, other.finishReason) && Objects.equals(this.index, other.index) && Objects.equals(this.message, other.message) && Objects.equals(this.delta, other.delta) && Objects.equals(this.usage, other.usage);
    }

    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.index);
        hash = 59 * hash + Objects.hashCode(this.message);
        hash = 59 * hash + Objects.hashCode(this.delta);
        hash = 59 * hash + Objects.hashCode(this.finishReason);
        hash = 59 * hash + Objects.hashCode(this.usage);
        return hash;
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiChatCompletionChoice [", "]").add("index=" + this.getIndex()).add("message=" + this.getMessage() == null ? "" : "**********").add("delta=" + this.getDelta()).add("finishReason=" + this.getFinishReason()).add("usage=" + this.getUsage()).toString();
    }

    public static MistralAiChatCompletionChoiceBuilder builder() {
        return new MistralAiChatCompletionChoiceBuilder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MistralAiChatCompletionChoiceBuilder {
        private Integer index;
        private MistralAiChatMessage message;
        private MistralAiDeltaMessage delta;
        private String finishReason;
        private MistralAiUsage usage;

        private MistralAiChatCompletionChoiceBuilder() {
        }

        public MistralAiChatCompletionChoiceBuilder index(Integer index) {
            this.index = index;
            return this;
        }

        public MistralAiChatCompletionChoiceBuilder message(MistralAiChatMessage message) {
            this.message = message;
            return this;
        }

        public MistralAiChatCompletionChoiceBuilder delta(MistralAiDeltaMessage delta) {
            this.delta = delta;
            return this;
        }

        public MistralAiChatCompletionChoiceBuilder finishReason(String finishReason) {
            this.finishReason = finishReason;
            return this;
        }

        public MistralAiChatCompletionChoiceBuilder usage(MistralAiUsage usage) {
            this.usage = usage;
            return this;
        }

        public MistralAiChatCompletionChoice build() {
            return new MistralAiChatCompletionChoice(this);
        }
    }
}

