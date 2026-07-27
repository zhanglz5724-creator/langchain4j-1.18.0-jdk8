/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding.response;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata;
import dev.langchain4j.model.output.TokenUsage;
import java.util.List;
import java.util.Objects;

@Experimental
public class EmbeddingResponse {
    private final List<Embedding> embeddings;
    private final EmbeddingResponseMetadata metadata;

    protected EmbeddingResponse(Builder builder) {
        this.embeddings = Utils.copy(builder.embeddings);
        EmbeddingResponseMetadata.Builder<?> metadataBuilder = EmbeddingResponseMetadata.builder();
        if (builder.modelName != null) {
            EmbeddingResponse.validate(builder, "modelName");
            metadataBuilder.modelName(builder.modelName);
        }
        if (builder.tokenUsage != null) {
            EmbeddingResponse.validate(builder, "tokenUsage");
            metadataBuilder.tokenUsage(builder.tokenUsage);
        }
        this.metadata = builder.metadata != null ? builder.metadata : metadataBuilder.build();
    }

    public List<Embedding> embeddings() {
        return this.embeddings;
    }

    public EmbeddingResponseMetadata metadata() {
        return this.metadata;
    }

    public String modelName() {
        return this.metadata.modelName();
    }

    public TokenUsage tokenUsage() {
        return this.metadata.tokenUsage();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        EmbeddingResponse that = (EmbeddingResponse)o;
        return Objects.equals(this.embeddings, that.embeddings) && Objects.equals(this.metadata, that.metadata);
    }

    public int hashCode() {
        return Objects.hash(this.embeddings, this.metadata);
    }

    public String toString() {
        return "EmbeddingResponse{embeddings=" + this.embeddings + ", metadata=" + this.metadata + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    private static void validate(Builder builder, String name) {
        if (builder.metadata != null) {
            throw new IllegalArgumentException(String.format("Cannot set both 'metadata' and '%s' on EmbeddingResponse", name));
        }
    }

    public static class Builder {
        private List<Embedding> embeddings;
        private EmbeddingResponseMetadata metadata;
        private String modelName;
        private TokenUsage tokenUsage;

        public Builder embeddings(List<Embedding> embeddings) {
            this.embeddings = embeddings;
            return this;
        }

        public Builder metadata(EmbeddingResponseMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder tokenUsage(TokenUsage tokenUsage) {
            this.tokenUsage = tokenUsage;
            return this;
        }

        public EmbeddingResponse build() {
            return new EmbeddingResponse(this);
        }
    }
}

