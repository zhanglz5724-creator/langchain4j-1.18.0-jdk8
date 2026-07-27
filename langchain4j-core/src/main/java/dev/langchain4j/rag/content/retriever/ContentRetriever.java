/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content.retriever;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ListeningContentRetriever;
import dev.langchain4j.rag.content.retriever.listener.ContentRetrieverListener;
import dev.langchain4j.rag.query.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface ContentRetriever {
    public List<Content> retrieve(Query var1);

    @Experimental
    default public ContentRetriever addListener(ContentRetrieverListener listener) {
        return this.addListeners(listener == null ? null : Collections.singletonList(listener));
    }

    @Experimental
    default public ContentRetriever addListeners(Collection<ContentRetrieverListener> listeners) {
        if (Utils.isNullOrEmpty(listeners)) {
            return this;
        }
        if (this instanceof ListeningContentRetriever) {
            ListeningContentRetriever listeningContentRetriever = (ListeningContentRetriever)this;
            return listeningContentRetriever.withAdditionalListeners(listeners);
        }
        if (listeners instanceof List) {
            List listenersList = (List)listeners;
            return new ListeningContentRetriever(this, listenersList);
        }
        return new ListeningContentRetriever(this, new ArrayList<ContentRetrieverListener>(listeners));
    }
}

