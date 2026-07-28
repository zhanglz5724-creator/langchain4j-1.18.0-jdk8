/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.ovhai.internal.api;

import java.util.List;

@Deprecated
public class EmbeddingResponse {
    private List<float[]> embeddings;

    public EmbeddingResponse(List<float[]> embeddings) {
        this.embeddings = embeddings;
    }

    public EmbeddingResponse() {
    }

    public static EmbeddingResponseBuilder builder() {
        return new EmbeddingResponseBuilder();
    }

    public List<float[]> getEmbeddings() {
        return this.embeddings;
    }

    public void setEmbeddings(List<float[]> embeddings) {
        this.embeddings = embeddings;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EmbeddingResponse)) {
            return false;
        }
        EmbeddingResponse other = (EmbeddingResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<float[]> this$embeddings = this.getEmbeddings();
        List<float[]> other$embeddings = other.getEmbeddings();
        return !(this$embeddings == null ? other$embeddings != null : !((Object)this$embeddings).equals(other$embeddings));
    }

    protected boolean canEqual(Object other) {
        return other instanceof EmbeddingResponse;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<float[]> $embeddings = this.getEmbeddings();
        result = result * 59 + ($embeddings == null ? 43 : ((Object)$embeddings).hashCode());
        return result;
    }

    public String toString() {
        return "EmbeddingResponse(embeddings=" + this.getEmbeddings() + ")";
    }

    public static class EmbeddingResponseBuilder {
        private List<float[]> embeddings;

        EmbeddingResponseBuilder() {
        }

        public EmbeddingResponseBuilder embeddings(List<float[]> embeddings) {
            this.embeddings = embeddings;
            return this;
        }

        public EmbeddingResponse build() {
            return new EmbeddingResponse(this.embeddings);
        }

        public String toString() {
            return "EmbeddingResponse.EmbeddingResponseBuilder(embeddings=" + this.embeddings + ")";
        }
    }
}

