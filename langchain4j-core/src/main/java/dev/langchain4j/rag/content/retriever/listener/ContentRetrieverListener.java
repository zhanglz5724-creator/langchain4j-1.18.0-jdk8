/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content.retriever.listener;

import dev.langchain4j.Experimental;
import dev.langchain4j.rag.content.retriever.listener.ContentRetrieverErrorContext;
import dev.langchain4j.rag.content.retriever.listener.ContentRetrieverRequestContext;
import dev.langchain4j.rag.content.retriever.listener.ContentRetrieverResponseContext;

@Experimental
public interface ContentRetrieverListener {
    default public void onRequest(ContentRetrieverRequestContext requestContext) {
    }

    default public void onResponse(ContentRetrieverResponseContext responseContext) {
    }

    default public void onError(ContentRetrieverErrorContext errorContext) {
    }
}

