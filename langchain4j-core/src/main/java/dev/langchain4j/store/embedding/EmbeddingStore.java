/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.ListeningEmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface EmbeddingStore<Embedded> {
    public String add(Embedding var1);

    public void add(String var1, Embedding var2);

    public String add(Embedding var1, Embedded var2);

    public List<String> addAll(List<Embedding> var1);

    default public List<String> addAll(List<Embedding> embeddings, List<Embedded> embedded) {
        List<String> ids = this.generateIds(embeddings.size());
        this.addAll(ids, embeddings, embedded);
        return ids;
    }

    default public List<String> generateIds(int n) {
        ArrayList<String> ids = new ArrayList<String>();
        for (int i = 0; i < n; ++i) {
            ids.add(Utils.randomUUID());
        }
        return ids;
    }

    default public void addAll(List<String> ids, List<Embedding> embeddings, List<Embedded> embedded) {
        throw new UnsupportedFeatureException("Not supported yet.");
    }

    default public void remove(String id) {
        ValidationUtils.ensureNotBlank(id, "id");
        this.removeAll(Collections.singletonList(id));
    }

    default public void removeAll(Collection<String> ids) {
        throw new UnsupportedFeatureException("Not supported yet.");
    }

    default public void removeAll(Filter filter) {
        throw new UnsupportedFeatureException("Not supported yet.");
    }

    default public void removeAll() {
        throw new UnsupportedFeatureException("Not supported yet.");
    }

    public EmbeddingSearchResult<Embedded> search(EmbeddingSearchRequest var1);

    @Experimental
    default public EmbeddingStore<Embedded> addListener(EmbeddingStoreListener listener) {
        return this.addListeners(listener == null ? null : Collections.singletonList(listener));
    }

    @Experimental
    default public EmbeddingStore<Embedded> addListeners(List<EmbeddingStoreListener> listeners) {
        if (Utils.isNullOrEmpty(listeners)) {
            return this;
        }
        if (this instanceof ListeningEmbeddingStore) {
            ListeningEmbeddingStore listeningEmbeddingStore = (ListeningEmbeddingStore)this;
            return listeningEmbeddingStore.withAdditionalListeners(listeners);
        }
        return new ListeningEmbeddingStore(this, listeners);
    }
}

