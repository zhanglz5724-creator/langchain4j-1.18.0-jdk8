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

@Internal
interface BedrockEmbeddingResponse {
    public Embedding toEmbedding();

    public int getInputTextTokenCount();
}

