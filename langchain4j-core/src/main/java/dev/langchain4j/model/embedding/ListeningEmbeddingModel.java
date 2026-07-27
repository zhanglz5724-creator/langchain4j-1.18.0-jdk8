/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding;

import dev.langchain4j.Internal;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModelListenerUtils;
import dev.langchain4j.model.embedding.listener.EmbeddingModelErrorContext;
import dev.langchain4j.model.embedding.listener.EmbeddingModelListener;
import dev.langchain4j.model.embedding.listener.EmbeddingModelRequestContext;
import dev.langchain4j.model.embedding.listener.EmbeddingModelResponseContext;
import dev.langchain4j.model.embedding.request.EmbeddingParameter;
import dev.langchain4j.model.embedding.request.EmbeddingRequest;
import dev.langchain4j.model.embedding.request.EmbeddingRequestParameters;
import dev.langchain4j.model.embedding.response.EmbeddingResponse;
import dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Internal
final class ListeningEmbeddingModel
implements EmbeddingModel {
    private final EmbeddingModel delegate;
    private final List<EmbeddingModelListener> listeners;

    ListeningEmbeddingModel(EmbeddingModel delegate, List<EmbeddingModelListener> listeners) {
        this.delegate = ValidationUtils.ensureNotNull(delegate, "delegate");
        this.listeners = Utils.copy(listeners);
    }

    EmbeddingModel withAdditionalListeners(List<EmbeddingModelListener> additionalListeners) {
        if (additionalListeners == null || additionalListeners.isEmpty()) {
            return this;
        }
        ArrayList<EmbeddingModelListener> merged = new ArrayList<EmbeddingModelListener>(this.listeners);
        merged.addAll(additionalListeners);
        return new ListeningEmbeddingModel(this.delegate, merged);
    }

    @Override
    public Response<Embedding> embed(String text) {
        List<TextSegment> textSegmentsForContext;
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        try {
            textSegmentsForContext = Collections.singletonList(TextSegment.from(text));
        }
        catch (Exception ignored) {
            textSegmentsForContext = Collections.emptyList();
        }
        EmbeddingRequest reconstructedRequest = ListeningEmbeddingModel.requestFrom(textSegmentsForContext);
        EmbeddingModelRequestContext requestContext = EmbeddingModelRequestContext.builder().textSegments(textSegmentsForContext).embeddingRequest(reconstructedRequest).embeddingModel(this).attributes(attributes).build();
        EmbeddingModelListenerUtils.onRequest(requestContext, this.listeners);
        try {
            Response<Embedding> response = this.delegate.embed(text);
            Response<List<Embedding>> responseForListeners = Response.from(Collections.singletonList(response.content()), response.tokenUsage(), response.finishReason());
            EmbeddingModelListenerUtils.onResponse(EmbeddingModelResponseContext.builder().embeddingRequest(reconstructedRequest).embeddingResponse(ListeningEmbeddingModel.responseFrom(responseForListeners.content(), response.tokenUsage())).response(responseForListeners).textSegments(textSegmentsForContext).embeddingModel(this).attributes(attributes).build(), this.listeners);
            return response;
        }
        catch (Exception error) {
            EmbeddingModelListenerUtils.onError(EmbeddingModelErrorContext.builder().error(error).textSegments(textSegmentsForContext).embeddingRequest(reconstructedRequest).embeddingModel(this).attributes(attributes).build(), this.listeners);
            throw error;
        }
    }

    @Override
    public Response<Embedding> embed(TextSegment textSegment) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        List<TextSegment> textSegmentsForContext = textSegment == null ? Collections.emptyList() : Collections.singletonList(textSegment);
        EmbeddingRequest reconstructedRequest = ListeningEmbeddingModel.requestFrom(textSegmentsForContext);
        EmbeddingModelRequestContext requestContext = EmbeddingModelRequestContext.builder().textSegments(textSegmentsForContext).embeddingRequest(reconstructedRequest).embeddingModel(this).attributes(attributes).build();
        EmbeddingModelListenerUtils.onRequest(requestContext, this.listeners);
        try {
            Response<Embedding> response = this.delegate.embed(textSegment);
            Response<List<Embedding>> responseForListeners = Response.from(Collections.singletonList(response.content()), response.tokenUsage(), response.finishReason());
            EmbeddingModelListenerUtils.onResponse(EmbeddingModelResponseContext.builder().embeddingRequest(reconstructedRequest).embeddingResponse(ListeningEmbeddingModel.responseFrom(responseForListeners.content(), response.tokenUsage())).response(responseForListeners).textSegments(textSegmentsForContext).embeddingModel(this).attributes(attributes).build(), this.listeners);
            return response;
        }
        catch (Exception error) {
            EmbeddingModelListenerUtils.onError(EmbeddingModelErrorContext.builder().error(error).textSegments(textSegmentsForContext).embeddingRequest(reconstructedRequest).embeddingModel(this).attributes(attributes).build(), this.listeners);
            throw error;
        }
    }

    @Override
    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingRequest reconstructedRequest = ListeningEmbeddingModel.requestFrom(textSegments);
        EmbeddingModelRequestContext requestContext = EmbeddingModelRequestContext.builder().textSegments(textSegments).embeddingRequest(reconstructedRequest).embeddingModel(this).attributes(attributes).build();
        EmbeddingModelListenerUtils.onRequest(requestContext, this.listeners);
        try {
            Response<List<Embedding>> response = this.delegate.embedAll(textSegments);
            EmbeddingModelListenerUtils.onResponse(EmbeddingModelResponseContext.builder().embeddingRequest(reconstructedRequest).embeddingResponse(ListeningEmbeddingModel.responseFrom(response.content(), response.tokenUsage())).response(response).textSegments(textSegments).embeddingModel(this).attributes(attributes).build(), this.listeners);
            return response;
        }
        catch (Exception error) {
            EmbeddingModelListenerUtils.onError(EmbeddingModelErrorContext.builder().error(error).textSegments(textSegments).embeddingRequest(reconstructedRequest).embeddingModel(this).attributes(attributes).build(), this.listeners);
            throw error;
        }
    }

    @Override
    public EmbeddingResponse embed(EmbeddingRequest request) {
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        List<TextSegment> textSegmentsForContext = request.inputs().stream().map(input -> TextSegment.from(input.text())).collect(Collectors.toList());
        EmbeddingModelRequestContext requestContext = EmbeddingModelRequestContext.builder().textSegments(textSegmentsForContext).embeddingRequest(request).embeddingModel(this).attributes(attributes).build();
        EmbeddingModelListenerUtils.onRequest(requestContext, this.listeners);
        try {
            EmbeddingResponse response = this.delegate.embed(request);
            Response<List<Embedding>> responseForListeners = Response.from(response.embeddings(), response.metadata().tokenUsage());
            EmbeddingModelListenerUtils.onResponse(EmbeddingModelResponseContext.builder().embeddingRequest(request).embeddingResponse(response).embeddingModel(this).attributes(attributes).response(responseForListeners).textSegments(textSegmentsForContext).build(), this.listeners);
            return response;
        }
        catch (Exception error) {
            EmbeddingModelListenerUtils.onError(EmbeddingModelErrorContext.builder().error(error).textSegments(textSegmentsForContext).embeddingRequest(request).embeddingModel(this).attributes(attributes).build(), this.listeners);
            throw error;
        }
    }

    @Override
    public ModelProvider provider() {
        return this.delegate.provider();
    }

    @Override
    public Set<EmbeddingParameter<?>> supportedParameters() {
        return this.delegate.supportedParameters();
    }

    @Override
    public Set<ContentType> supportedContentTypes() {
        return this.delegate.supportedContentTypes();
    }

    @Override
    public EmbeddingRequestParameters defaultRequestParameters() {
        return this.delegate.defaultRequestParameters();
    }

    @Override
    public String modelName() {
        return this.delegate.modelName();
    }

    @Override
    public int dimension() {
        return this.delegate.dimension();
    }

    private static EmbeddingRequest requestFrom(List<TextSegment> textSegments) {
        return textSegments == null || textSegments.isEmpty() ? null : EmbeddingRequest.builder().textSegments(textSegments).build();
    }

    private static EmbeddingResponse responseFrom(List<Embedding> embeddings, TokenUsage tokenUsage) {
        return EmbeddingResponse.builder().embeddings(embeddings).metadata(((EmbeddingResponseMetadata.Builder)EmbeddingResponseMetadata.builder().tokenUsage(tokenUsage)).build()).build();
    }
}

