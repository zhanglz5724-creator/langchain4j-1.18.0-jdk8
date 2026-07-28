/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 */
package dev.langchain4j.model.anthropic;

import dev.langchain4j.Experimental;
import java.util.Objects;

@Experimental
public class AnthropicCacheDiagnostics {
    private final String cacheMissReasonType;
    private final Integer cacheMissedInputTokens;

    private AnthropicCacheDiagnostics(Builder builder) {
        this.cacheMissReasonType = builder.cacheMissReasonType;
        this.cacheMissedInputTokens = builder.cacheMissedInputTokens;
    }

    public String cacheMissReasonType() {
        return this.cacheMissReasonType;
    }

    public Integer cacheMissedInputTokens() {
        return this.cacheMissedInputTokens;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnthropicCacheDiagnostics)) {
            return false;
        }
        AnthropicCacheDiagnostics that = (AnthropicCacheDiagnostics)o;
        return Objects.equals(this.cacheMissReasonType, that.cacheMissReasonType) && Objects.equals(this.cacheMissedInputTokens, that.cacheMissedInputTokens);
    }

    public int hashCode() {
        return Objects.hash(this.cacheMissReasonType, this.cacheMissedInputTokens);
    }

    public String toString() {
        return "AnthropicCacheDiagnostics{cacheMissReasonType='" + this.cacheMissReasonType + '\'' + ", cacheMissedInputTokens=" + this.cacheMissedInputTokens + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String cacheMissReasonType;
        private Integer cacheMissedInputTokens;

        public Builder cacheMissReasonType(String cacheMissReasonType) {
            this.cacheMissReasonType = cacheMissReasonType;
            return this;
        }

        public Builder cacheMissedInputTokens(Integer cacheMissedInputTokens) {
            this.cacheMissedInputTokens = cacheMissedInputTokens;
            return this;
        }

        public AnthropicCacheDiagnostics build() {
            return new AnthropicCacheDiagnostics(this);
        }
    }
}

