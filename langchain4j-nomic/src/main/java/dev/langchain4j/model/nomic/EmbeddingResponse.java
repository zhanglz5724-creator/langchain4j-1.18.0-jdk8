/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.nomic;

import dev.langchain4j.model.nomic.Usage;
import java.util.List;

class EmbeddingResponse {
    private List<float[]> embeddings;
    private Usage usage;

    EmbeddingResponse() {
    }

    public List<float[]> getEmbeddings() {
        return this.embeddings;
    }

    public Usage getUsage() {
        return this.usage;
    }
}

