/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content.retriever;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.ContentRetrieverListenerUtils;
import dev.langchain4j.rag.content.retriever.listener.ContentRetrieverErrorContext;
import dev.langchain4j.rag.content.retriever.listener.ContentRetrieverListener;
import dev.langchain4j.rag.content.retriever.listener.ContentRetrieverRequestContext;
import dev.langchain4j.rag.content.retriever.listener.ContentRetrieverResponseContext;
import dev.langchain4j.rag.query.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Internal
final class ListeningContentRetriever
implements ContentRetriever {
    private final ContentRetriever delegate;
    private final List<ContentRetrieverListener> listeners;

    ListeningContentRetriever(ContentRetriever delegate, List<ContentRetrieverListener> listeners) {
        this.delegate = ValidationUtils.ensureNotNull(delegate, "delegate");
        this.listeners = Utils.copy(listeners);
    }

    ContentRetriever withAdditionalListeners(Collection<ContentRetrieverListener> additionalListeners) {
        if (additionalListeners == null || additionalListeners.isEmpty()) {
            return this;
        }
        ArrayList<ContentRetrieverListener> merged = new ArrayList<ContentRetrieverListener>(this.listeners);
        merged.addAll(additionalListeners);
        return new ListeningContentRetriever(this.delegate, merged);
    }

    @Override
    public List<Content> retrieve(Query query) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        ContentRetrieverRequestContext requestContext = ContentRetrieverRequestContext.builder().query(query).contentRetriever(this).attributes(attributes).build();
        ContentRetrieverListenerUtils.onRequest(requestContext, this.listeners);
        try {
            List<Content> contents = this.delegate.retrieve(query);
            ContentRetrieverListenerUtils.onResponse(ContentRetrieverResponseContext.builder().contents(contents).query(query).contentRetriever(this).attributes(attributes).build(), this.listeners);
            return contents;
        }
        catch (Exception error) {
            ContentRetrieverListenerUtils.onError(ContentRetrieverErrorContext.builder().error(error).query(query).contentRetriever(this).attributes(attributes).build(), this.listeners);
            throw error;
        }
    }
}

