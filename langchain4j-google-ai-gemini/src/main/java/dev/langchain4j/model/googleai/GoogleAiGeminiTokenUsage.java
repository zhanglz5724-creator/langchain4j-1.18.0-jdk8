/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.model.output.TokenUsage;
import java.util.Objects;

public class GoogleAiGeminiTokenUsage
extends TokenUsage {
    private final Integer cachedContentTokenCount;
    private final Integer thoughtsTokenCount;

    private GoogleAiGeminiTokenUsage(Builder builder) {
        super(builder.inputTokenCount, builder.outputTokenCount, builder.totalTokenCount);
        this.cachedContentTokenCount = builder.cachedContentTokenCount;
        this.thoughtsTokenCount = builder.thoughtsTokenCount;
    }

    public Integer cachedContentTokenCount() {
        return this.cachedContentTokenCount;
    }

    public Integer thoughtsTokenCount() {
        return this.thoughtsTokenCount;
    }

    public GoogleAiGeminiTokenUsage add(TokenUsage that) {
        if (that == null) {
            return this;
        }
        return GoogleAiGeminiTokenUsage.builder().inputTokenCount(GoogleAiGeminiTokenUsage.sum((Integer)this.inputTokenCount(), (Integer)that.inputTokenCount())).outputTokenCount(GoogleAiGeminiTokenUsage.sum((Integer)this.outputTokenCount(), (Integer)that.outputTokenCount())).totalTokenCount(GoogleAiGeminiTokenUsage.sum((Integer)this.totalTokenCount(), (Integer)that.totalTokenCount())).cachedContentTokenCount(this.addCachedContentTokenCount(that)).thoughtsTokenCount(this.addThoughtsTokenCount(that)).build();
    }

    private Integer addCachedContentTokenCount(TokenUsage that) {
        if (that instanceof GoogleAiGeminiTokenUsage) {
            GoogleAiGeminiTokenUsage thatGeminiTokenUsage = (GoogleAiGeminiTokenUsage)that;
            return GoogleAiGeminiTokenUsage.sum((Integer)this.cachedContentTokenCount, (Integer)thatGeminiTokenUsage.cachedContentTokenCount);
        }
        return this.cachedContentTokenCount;
    }

    private Integer addThoughtsTokenCount(TokenUsage that) {
        if (that instanceof GoogleAiGeminiTokenUsage) {
            GoogleAiGeminiTokenUsage thatGeminiTokenUsage = (GoogleAiGeminiTokenUsage)that;
            return GoogleAiGeminiTokenUsage.sum((Integer)this.thoughtsTokenCount, (Integer)thatGeminiTokenUsage.thoughtsTokenCount);
        }
        return this.thoughtsTokenCount;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || ((Object)((Object)this)).getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        GoogleAiGeminiTokenUsage that = (GoogleAiGeminiTokenUsage)((Object)o);
        return Objects.equals(this.cachedContentTokenCount, that.cachedContentTokenCount) && Objects.equals(this.thoughtsTokenCount, that.thoughtsTokenCount);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.cachedContentTokenCount, this.thoughtsTokenCount);
    }

    public String toString() {
        return "GoogleAiGeminiTokenUsage { inputTokenCount = " + this.inputTokenCount() + ", outputTokenCount = " + this.outputTokenCount() + ", totalTokenCount = " + this.totalTokenCount() + ", cachedContentTokenCount = " + this.cachedContentTokenCount + ", thoughtsTokenCount = " + this.thoughtsTokenCount + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer inputTokenCount;
        private Integer outputTokenCount;
        private Integer totalTokenCount;
        private Integer cachedContentTokenCount;
        private Integer thoughtsTokenCount;

        public Builder inputTokenCount(Integer inputTokenCount) {
            this.inputTokenCount = inputTokenCount;
            return this;
        }

        public Builder outputTokenCount(Integer outputTokenCount) {
            this.outputTokenCount = outputTokenCount;
            return this;
        }

        public Builder totalTokenCount(Integer totalTokenCount) {
            this.totalTokenCount = totalTokenCount;
            return this;
        }

        public Builder cachedContentTokenCount(Integer cachedContentTokenCount) {
            this.cachedContentTokenCount = cachedContentTokenCount;
            return this;
        }

        public Builder thoughtsTokenCount(Integer thoughtsTokenCount) {
            this.thoughtsTokenCount = thoughtsTokenCount;
            return this;
        }

        public GoogleAiGeminiTokenUsage build() {
            return new GoogleAiGeminiTokenUsage(this);
        }
    }
}

