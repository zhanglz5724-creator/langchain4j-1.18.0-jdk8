/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.model.output.TokenUsage;
import java.util.Objects;

public class BedrockTokenUsage
extends TokenUsage {
    private final Integer cacheWriteInputTokens;
    private final Integer cacheReadInputTokens;

    public BedrockTokenUsage(Builder builder) {
        super(builder.inputTokenCount, builder.outputTokenCount);
        this.cacheWriteInputTokens = builder.cacheWriteInputTokens;
        this.cacheReadInputTokens = builder.cacheReadInputTokens;
    }

    public Integer cacheWriteInputTokens() {
        return this.cacheWriteInputTokens;
    }

    public Integer cacheReadInputTokens() {
        return this.cacheReadInputTokens;
    }

    public BedrockTokenUsage add(TokenUsage that) {
        if (that == null) {
            return this;
        }
        return BedrockTokenUsage.builder().inputTokenCount(BedrockTokenUsage.sum((Integer)this.inputTokenCount(), (Integer)that.inputTokenCount())).outputTokenCount(BedrockTokenUsage.sum((Integer)this.outputTokenCount(), (Integer)that.outputTokenCount())).cacheWriteInputTokens(this.addCacheWriteInputTokens(that)).cacheReadInputTokens(this.addCacheReadInputTokens(that)).build();
    }

    private Integer addCacheWriteInputTokens(TokenUsage that) {
        if (that instanceof BedrockTokenUsage) {
            BedrockTokenUsage thatBedrockTokenUsage = (BedrockTokenUsage)that;
            return BedrockTokenUsage.sum((Integer)this.cacheWriteInputTokens, (Integer)thatBedrockTokenUsage.cacheWriteInputTokens);
        }
        return this.cacheWriteInputTokens;
    }

    private Integer addCacheReadInputTokens(TokenUsage that) {
        if (that instanceof BedrockTokenUsage) {
            BedrockTokenUsage thatBedrockTokenUsage = (BedrockTokenUsage)that;
            return BedrockTokenUsage.sum((Integer)this.cacheReadInputTokens, (Integer)thatBedrockTokenUsage.cacheReadInputTokens);
        }
        return this.cacheReadInputTokens;
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
        BedrockTokenUsage that = (BedrockTokenUsage)((Object)o);
        return Objects.equals(this.cacheWriteInputTokens, that.cacheWriteInputTokens) && Objects.equals(this.cacheReadInputTokens, that.cacheReadInputTokens);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.cacheWriteInputTokens, this.cacheReadInputTokens);
    }

    public String toString() {
        return "BedrockTokenUsage { inputTokenCount = " + this.inputTokenCount() + ", outputTokenCount = " + this.outputTokenCount() + ", totalTokenCount = " + this.totalTokenCount() + ", cacheWriteInputTokens = " + this.cacheWriteInputTokens + ", cacheReadInputTokens = " + this.cacheReadInputTokens + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer inputTokenCount;
        private Integer outputTokenCount;
        private Integer cacheWriteInputTokens;
        private Integer cacheReadInputTokens;

        public Builder inputTokenCount(Integer inputTokenCount) {
            this.inputTokenCount = inputTokenCount;
            return this;
        }

        public Builder outputTokenCount(Integer outputTokenCount) {
            this.outputTokenCount = outputTokenCount;
            return this;
        }

        public Builder cacheWriteInputTokens(Integer cacheWriteInputTokens) {
            this.cacheWriteInputTokens = cacheWriteInputTokens;
            return this;
        }

        public Builder cacheReadInputTokens(Integer cacheReadInputTokens) {
            this.cacheReadInputTokens = cacheReadInputTokens;
            return this;
        }

        public BedrockTokenUsage build() {
            return new BedrockTokenUsage(this);
        }
    }
}

