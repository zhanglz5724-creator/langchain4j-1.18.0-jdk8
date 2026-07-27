/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.model.embedding;

import dev.langchain4j.Internal;
import dev.langchain4j.model.embedding.EmbeddingModel;

@Internal
public interface EmbeddingModelFactory {
    public EmbeddingModel create();
}

