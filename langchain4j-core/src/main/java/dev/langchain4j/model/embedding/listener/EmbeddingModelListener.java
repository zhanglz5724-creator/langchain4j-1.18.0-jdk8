/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding.listener;

import dev.langchain4j.Experimental;
import dev.langchain4j.model.embedding.listener.EmbeddingModelErrorContext;
import dev.langchain4j.model.embedding.listener.EmbeddingModelRequestContext;
import dev.langchain4j.model.embedding.listener.EmbeddingModelResponseContext;

@Experimental
public interface EmbeddingModelListener {
    default public void onRequest(EmbeddingModelRequestContext requestContext) {
    }

    default public void onResponse(EmbeddingModelResponseContext responseContext) {
    }

    default public void onError(EmbeddingModelErrorContext errorContext) {
    }
}

