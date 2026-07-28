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
package dev.langchain4j.model.openai.internal.completion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.completion.Logprobs;
import java.util.Objects;

@JsonDeserialize(builder=CompletionChoice.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class CompletionChoice {
    @JsonProperty
    private final String text;
    @JsonProperty
    private final Integer index;
    @JsonProperty
    private final Logprobs logprobs;
    @JsonProperty
    private final String finishReason;

    public CompletionChoice(Builder builder) {
        this.text = builder.text;
        this.index = builder.index;
        this.logprobs = builder.logprobs;
        this.finishReason = builder.finishReason;
    }

    public String text() {
        return this.text;
    }

    public Integer index() {
        return this.index;
    }

    public Logprobs logprobs() {
        return this.logprobs;
    }

    public String finishReason() {
        return this.finishReason;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof CompletionChoice && this.equalTo((CompletionChoice)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(CompletionChoice another) {
        return Objects.equals(this.text, another.text) && Objects.equals(this.index, another.index) && Objects.equals(this.logprobs, another.logprobs) && Objects.equals(this.finishReason, another.finishReason);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.text);
        h += (h << 5) + Objects.hashCode(this.index);
        h += (h << 5) + Objects.hashCode(this.logprobs);
        h += (h << 5) + Objects.hashCode(this.finishReason);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "CompletionChoice{text=" + this.text + ", index=" + this.index + ", logprobs=" + this.logprobs + ", finishReason=" + this.finishReason + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        @JsonProperty
        private String text;
        @JsonProperty
        private Integer index;
        @JsonProperty
        private Logprobs logprobs;
        @JsonProperty
        private String finishReason;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder index(Integer index) {
            this.index = index;
            return this;
        }

        public Builder logprobs(Logprobs logprobs) {
            this.logprobs = logprobs;
            return this;
        }

        public Builder finishReason(String finishReason) {
            this.finishReason = finishReason;
            return this;
        }

        public CompletionChoice build() {
            return new CompletionChoice(this);
        }
    }
}

