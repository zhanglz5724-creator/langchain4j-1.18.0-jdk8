/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.cohere;

import dev.langchain4j.model.cohere.Meta;

class EmbedResponse {
    private String id;
    private String[] texts;
    private float[][] embeddings;
    private Meta meta;

    EmbedResponse() {
    }

    public String getId() {
        return this.id;
    }

    public String[] getTexts() {
        return this.texts;
    }

    public float[][] getEmbeddings() {
        return this.embeddings;
    }

    public Meta getMeta() {
        return this.meta;
    }
}

