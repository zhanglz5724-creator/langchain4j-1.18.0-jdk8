/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.output;

import dev.langchain4j.internal.Utils;
import java.util.Objects;

public class TokenUsage {
    private final Integer inputTokenCount;
    private final Integer outputTokenCount;
    private final Integer totalTokenCount;

    public TokenUsage() {
        this(null);
    }

    public TokenUsage(Integer inputTokenCount) {
        this(inputTokenCount, null);
    }

    public TokenUsage(Integer inputTokenCount, Integer outputTokenCount) {
        this(inputTokenCount, outputTokenCount, TokenUsage.sum(inputTokenCount, outputTokenCount));
    }

    public TokenUsage(Integer inputTokenCount, Integer outputTokenCount, Integer totalTokenCount) {
        this.inputTokenCount = inputTokenCount;
        this.outputTokenCount = outputTokenCount;
        this.totalTokenCount = totalTokenCount;
    }

    public Integer inputTokenCount() {
        return this.inputTokenCount;
    }

    public Integer outputTokenCount() {
        return this.outputTokenCount;
    }

    public Integer totalTokenCount() {
        return this.totalTokenCount;
    }

    public static TokenUsage sum(TokenUsage first, TokenUsage second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        return first.add(second);
    }

    public TokenUsage add(TokenUsage that) {
        if (that == null) {
            return this;
        }
        if (that.getClass() != TokenUsage.class) {
            return that.add(this);
        }
        return new TokenUsage(TokenUsage.sum(this.inputTokenCount, that.inputTokenCount), TokenUsage.sum(this.outputTokenCount, that.outputTokenCount), TokenUsage.sum(this.totalTokenCount, that.totalTokenCount));
    }

    protected static Integer sum(Integer first, Integer second) {
        if (first == null && second == null) {
            return null;
        }
        return Utils.getOrDefault(first, 0) + Utils.getOrDefault(second, 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        TokenUsage that = (TokenUsage)o;
        return Objects.equals(this.inputTokenCount, that.inputTokenCount) && Objects.equals(this.outputTokenCount, that.outputTokenCount) && Objects.equals(this.totalTokenCount, that.totalTokenCount);
    }

    public int hashCode() {
        return Objects.hash(this.inputTokenCount, this.outputTokenCount, this.totalTokenCount);
    }

    public String toString() {
        return "TokenUsage { inputTokenCount = " + this.inputTokenCount + ", outputTokenCount = " + this.outputTokenCount + ", totalTokenCount = " + this.totalTokenCount + " }";
    }
}

