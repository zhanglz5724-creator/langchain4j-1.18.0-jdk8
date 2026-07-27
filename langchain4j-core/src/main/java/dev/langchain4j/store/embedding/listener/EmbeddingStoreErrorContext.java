/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.listener;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreRequestContext;
import java.util.Map;

@Experimental
public class EmbeddingStoreErrorContext<Embedded> {
    private final Throwable error;
    private final EmbeddingStoreRequestContext<Embedded> requestContext;
    private final Map<Object, Object> attributes;

    public EmbeddingStoreErrorContext(Throwable error, EmbeddingStoreRequestContext<Embedded> requestContext, Map<Object, Object> attributes) {
        this.error = ValidationUtils.ensureNotNull(error, "error");
        this.requestContext = ValidationUtils.ensureNotNull(requestContext, "requestContext");
        this.attributes = ValidationUtils.ensureNotNull(attributes, "attributes");
    }

    public Throwable error() {
        return this.error;
    }

    public EmbeddingStore<Embedded> embeddingStore() {
        return this.requestContext.embeddingStore();
    }

    public EmbeddingStoreRequestContext<Embedded> requestContext() {
        return this.requestContext;
    }

    public Map<Object, Object> attributes() {
        return this.attributes;
    }
}

