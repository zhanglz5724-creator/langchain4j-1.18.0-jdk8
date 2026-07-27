/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.listener;

import dev.langchain4j.Experimental;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreErrorContext;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreRequestContext;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreResponseContext;

@Experimental
public interface EmbeddingStoreListener {
    default public void onRequest(EmbeddingStoreRequestContext<?> requestContext) {
    }

    default public void onResponse(EmbeddingStoreResponseContext<?> responseContext) {
    }

    default public void onError(EmbeddingStoreErrorContext<?> errorContext) {
    }
}

