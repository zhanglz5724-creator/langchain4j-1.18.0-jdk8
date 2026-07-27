/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Objects;

public class EmbeddingMatch<Embedded> {
    private final Double score;
    private final String embeddingId;
    private final Embedding embedding;
    private final Embedded embedded;

    public EmbeddingMatch(Double score, String embeddingId, Embedding embedding, Embedded embedded) {
        this.score = ValidationUtils.ensureNotNull(score, "score");
        this.embeddingId = ValidationUtils.ensureNotBlank(embeddingId, "embeddingId");
        this.embedding = embedding;
        this.embedded = embedded;
    }

    public Double score() {
        return this.score;
    }

    public String embeddingId() {
        return this.embeddingId;
    }

    public Embedding embedding() {
        return this.embedding;
    }

    public Embedded embedded() {
        return this.embedded;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        EmbeddingMatch that = (EmbeddingMatch)o;
        return Objects.equals(this.score, that.score) && Objects.equals(this.embeddingId, that.embeddingId) && Objects.equals(this.embedding, that.embedding) && Objects.equals(this.embedded, that.embedded);
    }

    public int hashCode() {
        return Objects.hash(this.score, this.embeddingId, this.embedding, this.embedded);
    }

    public String toString() {
        return "EmbeddingMatch { score = " + this.score + ", embedded = " + this.embedded + ", embeddingId = " + this.embeddingId + ", embedding = " + this.embedding + " }";
    }
}

