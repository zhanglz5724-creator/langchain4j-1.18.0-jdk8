/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.embedding;

import dev.langchain4j.Internal;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.listener.EmbeddingModelErrorContext;
import dev.langchain4j.model.embedding.listener.EmbeddingModelListener;
import dev.langchain4j.model.embedding.listener.EmbeddingModelRequestContext;
import dev.langchain4j.model.embedding.listener.EmbeddingModelResponseContext;
import dev.langchain4j.model.embedding.request.EmbeddingRequest;
import dev.langchain4j.model.embedding.response.EmbeddingResponse;
import dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata;
import dev.langchain4j.model.output.Response;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Internal
public class EmbeddingModelListenerUtils {
    private static final Logger LOG = LoggerFactory.getLogger(EmbeddingModelListenerUtils.class);

    private EmbeddingModelListenerUtils() {
    }

    public static Response<List<Embedding>> withListeners(EmbeddingModel model, List<TextSegment> textSegments, Supplier<Response<List<Embedding>>> operation) {
        List<EmbeddingModelListener> listeners = model.listeners();
        if (Utils.isNullOrEmpty(listeners) || Utils.isNullOrEmpty(textSegments)) {
            return operation.get();
        }
        EmbeddingRequest request = EmbeddingRequest.builder().textSegments(textSegments).build();
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingModelListenerUtils.onRequest(EmbeddingModelRequestContext.builder().textSegments(textSegments).embeddingRequest(request).embeddingModel(model).attributes(attributes).build(), listeners);
        try {
            Response<List<Embedding>> response = operation.get();
            EmbeddingResponse embeddingResponse = EmbeddingResponse.builder().embeddings(response.content()).metadata(((EmbeddingResponseMetadata.Builder)((EmbeddingResponseMetadata.Builder)EmbeddingResponseMetadata.builder().modelName(model.modelName())).tokenUsage(response.tokenUsage())).build()).build();
            EmbeddingModelListenerUtils.onResponse(EmbeddingModelResponseContext.builder().embeddingRequest(request).embeddingResponse(embeddingResponse).embeddingModel(model).attributes(attributes).response(response).textSegments(textSegments).build(), listeners);
            return response;
        }
        catch (Exception error) {
            EmbeddingModelListenerUtils.onError(EmbeddingModelErrorContext.builder().error(error).textSegments(textSegments).embeddingRequest(request).embeddingModel(model).attributes(attributes).build(), listeners);
            throw error;
        }
    }

    public static Response<Embedding> withListeners(EmbeddingModel model, TextSegment textSegment, Supplier<Response<Embedding>> operation) {
        Response<List<Embedding>> response = EmbeddingModelListenerUtils.withListeners(model, Collections.singletonList(textSegment), () -> {
            Response<Embedding> single = (Response<Embedding>) operation.get();
            return Response.from(Collections.singletonList((Embedding) single.content()), single.tokenUsage());
        });
        return Response.from(response.content().get(0), response.tokenUsage());
    }

    static void onRequest(EmbeddingModelRequestContext requestContext, List<EmbeddingModelListener> listeners) {
        if (Utils.isNullOrEmpty(listeners)) {
            return;
        }
        listeners.forEach(listener -> {
            try {
                listener.onRequest(requestContext);
            }
            catch (Exception e) {
                LOG.warn("An exception occurred during the invocation of the embedding model listener. This exception has been ignored.", (Throwable)e);
            }
        });
    }

    static void onResponse(EmbeddingModelResponseContext responseContext, List<EmbeddingModelListener> listeners) {
        if (Utils.isNullOrEmpty(listeners)) {
            return;
        }
        listeners.forEach(listener -> {
            try {
                listener.onResponse(responseContext);
            }
            catch (Exception e) {
                LOG.warn("An exception occurred during the invocation of the embedding model listener. This exception has been ignored.", (Throwable)e);
            }
        });
    }

    static void onError(EmbeddingModelErrorContext errorContext, List<EmbeddingModelListener> listeners) {
        if (Utils.isNullOrEmpty(listeners)) {
            return;
        }
        listeners.forEach(listener -> {
            try {
                listener.onError(errorContext);
            }
            catch (Exception e) {
                LOG.warn("An exception occurred during the invocation of the embedding model listener. This exception has been ignored.", (Throwable)e);
            }
        });
    }
}

