/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.spi.store.embedding.inmemory;

import dev.langchain4j.Internal;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStoreJsonCodec;

@Internal
public interface InMemoryEmbeddingStoreJsonCodecFactory {
    public InMemoryEmbeddingStoreJsonCodec create();
}

