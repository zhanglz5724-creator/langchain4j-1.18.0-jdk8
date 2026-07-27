/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding.response;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.output.TokenUsage;
import java.util.Objects;

@Experimental
@JacocoIgnoreCoverageGenerated
public class EmbeddingResponseMetadata {
    private final String modelName;
    private final TokenUsage tokenUsage;

    protected EmbeddingResponseMetadata(Builder<?> builder) {
        this.modelName = ((Builder)builder).modelName;
        this.tokenUsage = ((Builder)builder).tokenUsage;
    }

    public String modelName() {
        return this.modelName;
    }

    public TokenUsage tokenUsage() {
        return this.tokenUsage;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        EmbeddingResponseMetadata that = (EmbeddingResponseMetadata)o;
        return Objects.equals(this.modelName, that.modelName) && Objects.equals(this.tokenUsage, that.tokenUsage);
    }

    public int hashCode() {
        return Objects.hash(this.modelName, this.tokenUsage);
    }

    public String toString() {
        return "EmbeddingResponseMetadata{modelName='" + this.modelName + '\'' + ", tokenUsage=" + this.tokenUsage + '}';
    }

    public static Builder<?> builder() {
        return new Builder();
    }

    public static class Builder<T extends Builder<T>> {
        private String modelName;
        private TokenUsage tokenUsage;

        public T modelName(String modelName) {
            this.modelName = modelName;
            return this.self();
        }

        public T tokenUsage(TokenUsage tokenUsage) {
            this.tokenUsage = tokenUsage;
            return this.self();
        }

        protected T self() {
            return (T)this;
        }

        public EmbeddingResponseMetadata build() {
            return new EmbeddingResponseMetadata(this);
        }
    }
}

