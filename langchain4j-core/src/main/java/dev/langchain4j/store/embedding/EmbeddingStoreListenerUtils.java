/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreErrorContext;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreListener;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreRequestContext;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreResponseContext;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Internal
class EmbeddingStoreListenerUtils {
    private static final Logger LOG = LoggerFactory.getLogger(EmbeddingStoreListenerUtils.class);

    private EmbeddingStoreListenerUtils() {
    }

    static void onRequest(EmbeddingStoreRequestContext<?> requestContext, List<EmbeddingStoreListener> listeners) {
        if (Utils.isNullOrEmpty(listeners)) {
            return;
        }
        listeners.forEach(listener -> {
            try {
                listener.onRequest(requestContext);
            }
            catch (Exception e) {
                LOG.warn("An exception occurred during the invocation of the embedding store listener. This exception has been ignored.", (Throwable)e);
            }
        });
    }

    static void onResponse(EmbeddingStoreResponseContext<?> responseContext, List<EmbeddingStoreListener> listeners) {
        if (Utils.isNullOrEmpty(listeners)) {
            return;
        }
        listeners.forEach(listener -> {
            try {
                listener.onResponse(responseContext);
            }
            catch (Exception e) {
                LOG.warn("An exception occurred during the invocation of the embedding store listener. This exception has been ignored.", (Throwable)e);
            }
        });
    }

    static void onError(EmbeddingStoreErrorContext<?> errorContext, List<EmbeddingStoreListener> listeners) {
        if (Utils.isNullOrEmpty(listeners)) {
            return;
        }
        listeners.forEach(listener -> {
            try {
                listener.onError(errorContext);
            }
            catch (Exception e) {
                LOG.warn("An exception occurred during the invocation of the embedding store listener. This exception has been ignored.", (Throwable)e);
            }
        });
    }
}

