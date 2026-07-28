/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai;

public enum WorkersAiEmbeddingModelName {
    BAAI_EMBEDDING_SMALL("@cf/baai/bge-small-en-v1.5"),
    BAAI_EMBEDDING_BASE("@cf/baai/bge-base-en-v1.5"),
    BAAI_EMBEDDING_LARGE("@cf/baai/bge-large-en-v1.5");

    private final String stringValue;

    private WorkersAiEmbeddingModelName(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return this.stringValue;
    }
}

