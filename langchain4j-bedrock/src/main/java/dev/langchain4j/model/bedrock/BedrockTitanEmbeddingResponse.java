/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.embedding.Embedding
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.Internal;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.bedrock.BedrockEmbeddingResponse;

@Internal
class BedrockTitanEmbeddingResponse
implements BedrockEmbeddingResponse {
    private float[] embedding;
    private int inputTextTokenCount;

    BedrockTitanEmbeddingResponse() {
    }

    @Override
    public Embedding toEmbedding() {
        return new Embedding(this.embedding);
    }

    public float[] getEmbedding() {
        return this.embedding;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    @Override
    public int getInputTextTokenCount() {
        return this.inputTextTokenCount;
    }

    public void setInputTextTokenCount(int inputTextTokenCount) {
        this.inputTextTokenCount = inputTextTokenCount;
    }
}

