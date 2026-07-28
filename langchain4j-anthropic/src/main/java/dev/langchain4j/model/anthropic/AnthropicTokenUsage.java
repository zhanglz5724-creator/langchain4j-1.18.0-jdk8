/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.anthropic;

import dev.langchain4j.model.output.TokenUsage;

public class AnthropicTokenUsage
extends TokenUsage {
    private final Integer cacheCreationInputTokens;
    private final Integer cacheReadInputTokens;

    public AnthropicTokenUsage(Builder builder) {
        super(builder.inputTokenCount, builder.outputTokenCount);
        this.cacheCreationInputTokens = builder.cacheCreationInputTokens;
        this.cacheReadInputTokens = builder.cacheReadInputTokens;
    }

    public Integer cacheCreationInputTokens() {
        return this.cacheCreationInputTokens;
    }

    public Integer cacheReadInputTokens() {
        return this.cacheReadInputTokens;
    }

    public AnthropicTokenUsage add(TokenUsage that) {
        if (that == null) {
            return this;
        }
        return AnthropicTokenUsage.builder().inputTokenCount(AnthropicTokenUsage.sum((Integer)this.inputTokenCount(), (Integer)that.inputTokenCount())).outputTokenCount(AnthropicTokenUsage.sum((Integer)this.outputTokenCount(), (Integer)that.outputTokenCount())).cacheCreationInputTokens(this.addCacheCreationInputTokens(that)).cacheReadInputTokens(this.addCacheReadInputTokens(that)).build();
    }

    private Integer addCacheCreationInputTokens(TokenUsage that) {
        if (that instanceof AnthropicTokenUsage) {
            AnthropicTokenUsage thatAnthropicTokenUsage = (AnthropicTokenUsage)that;
            return AnthropicTokenUsage.sum((Integer)this.cacheCreationInputTokens, (Integer)thatAnthropicTokenUsage.cacheCreationInputTokens);
        }
        return this.cacheCreationInputTokens;
    }

    private Integer addCacheReadInputTokens(TokenUsage that) {
        if (that instanceof AnthropicTokenUsage) {
            AnthropicTokenUsage thatAnthropicTokenUsage = (AnthropicTokenUsage)that;
            return AnthropicTokenUsage.sum((Integer)this.cacheReadInputTokens, (Integer)thatAnthropicTokenUsage.cacheReadInputTokens);
        }
        return this.cacheReadInputTokens;
    }

    public String toString() {
        return "AnthropicTokenUsage { inputTokenCount = " + this.inputTokenCount() + ", outputTokenCount = " + this.outputTokenCount() + ", totalTokenCount = " + this.totalTokenCount() + ", cacheCreationInputTokens = " + this.cacheCreationInputTokens + ", cacheReadInputTokens = " + this.cacheReadInputTokens + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer inputTokenCount;
        private Integer outputTokenCount;
        private Integer cacheCreationInputTokens;
        private Integer cacheReadInputTokens;

        public Builder inputTokenCount(Integer inputTokenCount) {
            this.inputTokenCount = inputTokenCount;
            return this;
        }

        public Builder outputTokenCount(Integer outputTokenCount) {
            this.outputTokenCount = outputTokenCount;
            return this;
        }

        public Builder cacheCreationInputTokens(Integer cacheCreationInputTokens) {
            this.cacheCreationInputTokens = cacheCreationInputTokens;
            return this;
        }

        public Builder cacheReadInputTokens(Integer cacheReadInputTokens) {
            this.cacheReadInputTokens = cacheReadInputTokens;
            return this;
        }

        public AnthropicTokenUsage build() {
            return new AnthropicTokenUsage(this);
        }
    }
}

