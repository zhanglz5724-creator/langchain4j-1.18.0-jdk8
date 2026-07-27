/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding;

import dev.langchain4j.Internal;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreListenerUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreErrorContext;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreListener;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreRequestContext;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreResponseContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Internal
final class ListeningEmbeddingStore<Embedded>
implements EmbeddingStore<Embedded> {
    private final EmbeddingStore<Embedded> delegate;
    private final List<EmbeddingStoreListener> listeners;

    ListeningEmbeddingStore(EmbeddingStore<Embedded> delegate, List<EmbeddingStoreListener> listeners) {
        this.delegate = ValidationUtils.ensureNotNull(delegate, "delegate");
        this.listeners = Utils.copy(listeners);
    }

    EmbeddingStore<Embedded> withAdditionalListeners(List<EmbeddingStoreListener> additionalListeners) {
        if (additionalListeners == null || additionalListeners.isEmpty()) {
            return this;
        }
        ArrayList<EmbeddingStoreListener> merged = new ArrayList<EmbeddingStoreListener>(this.listeners);
        merged.addAll(additionalListeners);
        return new ListeningEmbeddingStore<Embedded>(this.delegate, merged);
    }

    @Override
    public String add(Embedding embedding) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingStoreRequestContext.Add requestContext = new EmbeddingStoreRequestContext.Add(this, attributes, embedding);
        EmbeddingStoreListenerUtils.onRequest(requestContext, this.listeners);
        try {
            String id = this.delegate.add(embedding);
            EmbeddingStoreResponseContext.Add responseContext = new EmbeddingStoreResponseContext.Add(requestContext, attributes, id);
            EmbeddingStoreListenerUtils.onResponse(responseContext, this.listeners);
            return id;
        }
        catch (Exception error) {
            EmbeddingStoreListenerUtils.onError(new EmbeddingStoreErrorContext(error, requestContext, attributes), this.listeners);
            throw error;
        }
    }

    @Override
    public void add(String id, Embedding embedding) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingStoreRequestContext.Add requestContext = new EmbeddingStoreRequestContext.Add(this, attributes, id, embedding);
        EmbeddingStoreListenerUtils.onRequest(requestContext, this.listeners);
        try {
            this.delegate.add(id, embedding);
            EmbeddingStoreResponseContext.Add responseContext = new EmbeddingStoreResponseContext.Add(requestContext, attributes, id);
            EmbeddingStoreListenerUtils.onResponse(responseContext, this.listeners);
        }
        catch (Exception error) {
            EmbeddingStoreListenerUtils.onError(new EmbeddingStoreErrorContext(error, requestContext, attributes), this.listeners);
            throw error;
        }
    }

    @Override
    public String add(Embedding embedding, Embedded embedded) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingStoreRequestContext.Add<Embedded> requestContext = new EmbeddingStoreRequestContext.Add<Embedded>(this, attributes, null, embedding, embedded);
        EmbeddingStoreListenerUtils.onRequest(requestContext, this.listeners);
        try {
            String id = this.delegate.add(embedding, embedded);
            EmbeddingStoreResponseContext.Add<Embedded> responseContext = new EmbeddingStoreResponseContext.Add<Embedded>(requestContext, attributes, id);
            EmbeddingStoreListenerUtils.onResponse(responseContext, this.listeners);
            return id;
        }
        catch (Exception error) {
            EmbeddingStoreListenerUtils.onError(new EmbeddingStoreErrorContext<Embedded>(error, requestContext, attributes), this.listeners);
            throw error;
        }
    }

    @Override
    public List<String> addAll(List<Embedding> embeddings) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingStoreRequestContext.AddAll requestContext = new EmbeddingStoreRequestContext.AddAll(this, attributes, null, embeddings, null);
        EmbeddingStoreListenerUtils.onRequest(requestContext, this.listeners);
        try {
            List<String> ids = this.delegate.addAll(embeddings);
            EmbeddingStoreResponseContext.AddAll responseContext = new EmbeddingStoreResponseContext.AddAll(requestContext, attributes, ids);
            EmbeddingStoreListenerUtils.onResponse(responseContext, this.listeners);
            return ids;
        }
        catch (Exception error) {
            EmbeddingStoreListenerUtils.onError(new EmbeddingStoreErrorContext(error, requestContext, attributes), this.listeners);
            throw error;
        }
    }

    @Override
    public List<String> addAll(List<Embedding> embeddings, List<Embedded> embedded) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingStoreRequestContext.AddAll<Embedded> requestContext = new EmbeddingStoreRequestContext.AddAll<Embedded>(this, attributes, null, embeddings, embedded);
        EmbeddingStoreListenerUtils.onRequest(requestContext, this.listeners);
        try {
            List<String> ids = this.delegate.addAll(embeddings, embedded);
            EmbeddingStoreResponseContext.AddAll<Embedded> responseContext = new EmbeddingStoreResponseContext.AddAll<Embedded>(requestContext, attributes, ids);
            EmbeddingStoreListenerUtils.onResponse(responseContext, this.listeners);
            return ids;
        }
        catch (Exception error) {
            EmbeddingStoreListenerUtils.onError(new EmbeddingStoreErrorContext<Embedded>(error, requestContext, attributes), this.listeners);
            throw error;
        }
    }

    @Override
    public void addAll(List<String> ids, List<Embedding> embeddings, List<Embedded> embedded) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingStoreRequestContext.AddAll<Embedded> requestContext = new EmbeddingStoreRequestContext.AddAll<Embedded>(this, attributes, ids, embeddings, embedded);
        EmbeddingStoreListenerUtils.onRequest(requestContext, this.listeners);
        try {
            this.delegate.addAll(ids, embeddings, embedded);
            EmbeddingStoreResponseContext.AddAll<Embedded> responseContext = new EmbeddingStoreResponseContext.AddAll<Embedded>(requestContext, attributes, ids);
            EmbeddingStoreListenerUtils.onResponse(responseContext, this.listeners);
        }
        catch (Exception error) {
            EmbeddingStoreListenerUtils.onError(new EmbeddingStoreErrorContext<Embedded>(error, requestContext, attributes), this.listeners);
            throw error;
        }
    }

    @Override
    public void remove(String id) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingStoreRequestContext.Remove requestContext = new EmbeddingStoreRequestContext.Remove(this, attributes, id);
        EmbeddingStoreListenerUtils.onRequest(requestContext, this.listeners);
        try {
            this.delegate.remove(id);
            EmbeddingStoreResponseContext.Remove responseContext = new EmbeddingStoreResponseContext.Remove(requestContext, (Map<Object, Object>)attributes);
            EmbeddingStoreListenerUtils.onResponse(responseContext, this.listeners);
        }
        catch (Exception error) {
            EmbeddingStoreListenerUtils.onError(new EmbeddingStoreErrorContext(error, requestContext, attributes), this.listeners);
            throw error;
        }
    }

    @Override
    public void removeAll(Collection<String> ids) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        ArrayList<String> idsAsList = ids == null ? null : new ArrayList<String>(ids);
        EmbeddingStoreRequestContext.RemoveAllIds requestContext = new EmbeddingStoreRequestContext.RemoveAllIds(this, attributes, idsAsList);
        EmbeddingStoreListenerUtils.onRequest(requestContext, this.listeners);
        try {
            this.delegate.removeAll(ids);
            EmbeddingStoreResponseContext.RemoveAllIds responseContext = new EmbeddingStoreResponseContext.RemoveAllIds(requestContext, (Map<Object, Object>)attributes);
            EmbeddingStoreListenerUtils.onResponse(responseContext, this.listeners);
        }
        catch (Exception error) {
            EmbeddingStoreListenerUtils.onError(new EmbeddingStoreErrorContext(error, requestContext, attributes), this.listeners);
            throw error;
        }
    }

    @Override
    public void removeAll(Filter filter) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingStoreRequestContext.RemoveAllFilter requestContext = new EmbeddingStoreRequestContext.RemoveAllFilter(this, attributes, filter);
        EmbeddingStoreListenerUtils.onRequest(requestContext, this.listeners);
        try {
            this.delegate.removeAll(filter);
            EmbeddingStoreResponseContext.RemoveAllFilter responseContext = new EmbeddingStoreResponseContext.RemoveAllFilter(requestContext, (Map<Object, Object>)attributes);
            EmbeddingStoreListenerUtils.onResponse(responseContext, this.listeners);
        }
        catch (Exception error) {
            EmbeddingStoreListenerUtils.onError(new EmbeddingStoreErrorContext(error, requestContext, attributes), this.listeners);
            throw error;
        }
    }

    @Override
    public void removeAll() {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingStoreRequestContext.RemoveAll requestContext = new EmbeddingStoreRequestContext.RemoveAll(this, attributes);
        EmbeddingStoreListenerUtils.onRequest(requestContext, this.listeners);
        try {
            this.delegate.removeAll();
            EmbeddingStoreResponseContext.RemoveAll responseContext = new EmbeddingStoreResponseContext.RemoveAll(requestContext, (Map<Object, Object>)attributes);
            EmbeddingStoreListenerUtils.onResponse(responseContext, this.listeners);
        }
        catch (Exception error) {
            EmbeddingStoreListenerUtils.onError(new EmbeddingStoreErrorContext(error, requestContext, attributes), this.listeners);
            throw error;
        }
    }

    @Override
    public EmbeddingSearchResult<Embedded> search(EmbeddingSearchRequest request) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingStoreRequestContext.Search requestContext = new EmbeddingStoreRequestContext.Search(this, attributes, request);
        EmbeddingStoreListenerUtils.onRequest(requestContext, this.listeners);
        try {
            EmbeddingSearchResult<Embedded> result = this.delegate.search(request);
            EmbeddingStoreResponseContext.Search responseContext = new EmbeddingStoreResponseContext.Search(requestContext, attributes, result);
            EmbeddingStoreListenerUtils.onResponse(responseContext, this.listeners);
            return result;
        }
        catch (Exception error) {
            EmbeddingStoreListenerUtils.onError(new EmbeddingStoreErrorContext(error, requestContext, attributes), this.listeners);
            throw error;
        }
    }
}

