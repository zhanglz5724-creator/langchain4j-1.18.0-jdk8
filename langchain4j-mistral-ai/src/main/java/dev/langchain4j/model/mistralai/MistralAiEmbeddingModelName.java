/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.mistralai;

public enum MistralAiEmbeddingModelName {
    MISTRAL_EMBED("mistral-embed");

    private final String value;

    private MistralAiEmbeddingModelName(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}

