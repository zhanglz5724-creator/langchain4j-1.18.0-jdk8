/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.embedding.EmbeddingModelListenerUtils;
import dev.langchain4j.model.embedding.ListeningEmbeddingModel;
import dev.langchain4j.model.embedding.listener.EmbeddingModelErrorContext;
import dev.langchain4j.model.embedding.listener.EmbeddingModelListener;
import dev.langchain4j.model.embedding.listener.EmbeddingModelRequestContext;
import dev.langchain4j.model.embedding.listener.EmbeddingModelResponseContext;
import dev.langchain4j.model.embedding.request.EmbeddingInput;
import dev.langchain4j.model.embedding.request.EmbeddingParameter;
import dev.langchain4j.model.embedding.request.EmbeddingRequest;
import dev.langchain4j.model.embedding.request.EmbeddingRequestParameters;
import dev.langchain4j.model.embedding.response.EmbeddingResponse;
import dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata;
import dev.langchain4j.model.output.Response;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public interface EmbeddingModel {
    @Experimental
    default public EmbeddingResponse embed(EmbeddingRequest request) {
        EmbeddingRequestParameters finalParameters = this.defaultRequestParameters().overrideWith(request.parameters());
        LinkedHashSet<Object> unsupported = new LinkedHashSet<Object>(finalParameters.presentParameters());
        unsupported.removeAll(this.supportedParameters());
        if (!unsupported.isEmpty()) {
            String names = unsupported.stream().map(p -> (String) ((EmbeddingParameter) p).name()).collect(Collectors.joining(", "));
            throw new UnsupportedFeatureException("EmbeddingModel '" + this.getClass().getName() + "' does not support the following per-call parameter(s): " + names + ". Only the following are supported: " + this.supportedParameters().stream().map(p -> (String) ((EmbeddingParameter) p).name()).collect(Collectors.joining(", ")));
        }
        LinkedHashSet<ContentType> unsupportedContentTypes = new LinkedHashSet<ContentType>();
        for (EmbeddingInput input2 : request.inputs()) {
            unsupportedContentTypes.addAll(input2.contentTypes());
        }
        unsupportedContentTypes.removeAll(this.supportedContentTypes());
        if (!unsupportedContentTypes.isEmpty()) {
            throw new UnsupportedFeatureException("EmbeddingModel '" + this.getClass().getName() + "' does not support the following content type(s): " + unsupportedContentTypes + ". Only the following are supported: " + this.supportedContentTypes());
        }
        EmbeddingRequest finalRequest = EmbeddingRequest.builder().inputs(request.inputs()).parameters(finalParameters).build();
        List<EmbeddingModelListener> listeners = this.listeners();
        if (Utils.isNullOrEmpty(listeners)) {
            return this.doEmbed(finalRequest);
        }
        List<TextSegment> textSegments = finalRequest.inputs().stream().map(input -> TextSegment.from(input.text())).collect(Collectors.toList());
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        EmbeddingModelListenerUtils.onRequest(EmbeddingModelRequestContext.builder().textSegments(textSegments).embeddingRequest(finalRequest).embeddingModel(this).attributes(attributes).build(), listeners);
        try {
            EmbeddingResponse response = this.doEmbed(finalRequest);
            Response<List<Embedding>> legacyResponse = Response.from(response.embeddings(), response.metadata().tokenUsage());
            EmbeddingModelListenerUtils.onResponse(EmbeddingModelResponseContext.builder().embeddingRequest(finalRequest).embeddingResponse(response).embeddingModel(this).attributes(attributes).response(legacyResponse).textSegments(textSegments).build(), listeners);
            return response;
        }
        catch (Exception error) {
            EmbeddingModelListenerUtils.onError(EmbeddingModelErrorContext.builder().error(error).textSegments(textSegments).embeddingRequest(finalRequest).embeddingModel(this).attributes(attributes).build(), listeners);
            throw error;
        }
    }

    @Experimental
    default public List<EmbeddingModelListener> listeners() {
        return Collections.emptyList();
    }

    @Experimental
    default public ModelProvider provider() {
        return ModelProvider.OTHER;
    }

    @Experimental
    default public EmbeddingResponse doEmbed(EmbeddingRequest request) {
        Response<List<Embedding>> legacy = this.embedAll(request.inputs().stream().map(input -> TextSegment.from(input.text())).collect(Collectors.toList()));
        return EmbeddingResponse.builder().embeddings(legacy.content()).metadata(((EmbeddingResponseMetadata.Builder)((EmbeddingResponseMetadata.Builder)EmbeddingResponseMetadata.builder().modelName(this.modelName())).tokenUsage(legacy.tokenUsage())).build()).build();
    }

    @Experimental
    default public EmbeddingRequestParameters defaultRequestParameters() {
        return EmbeddingRequestParameters.EMPTY;
    }

    @Experimental
    default public Set<EmbeddingParameter<?>> supportedParameters() {
        return Collections.emptySet();
    }

    @Experimental
    default public Set<ContentType> supportedContentTypes() {
        return Collections.singleton(ContentType.TEXT);
    }

    default public Response<Embedding> embed(String text) {
        return this.embed(TextSegment.from(text));
    }

    default public Response<Embedding> embed(TextSegment textSegment) {
        EmbeddingResponse response = this.embed(EmbeddingRequest.builder().textSegment(textSegment).build());
        ValidationUtils.ensureEq(response.embeddings().size(), 1, "Expected a single embedding, but got %d", response.embeddings().size());
        return Response.from(response.embeddings().get(0), response.metadata().tokenUsage());
    }

    default public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        if (Utils.isNullOrEmpty(textSegments)) {
            return Response.from(Collections.emptyList());
        }
        EmbeddingResponse response = this.embed(EmbeddingRequest.builder().textSegments(textSegments).build());
        return Response.from(response.embeddings(), response.metadata().tokenUsage());
    }

    default public int dimension() {
        return this.embed("test").content().dimension();
    }

    default public String modelName() {
        return "unknown";
    }

    @Experimental
    default public EmbeddingModel addListener(EmbeddingModelListener listener) {
        return this.addListeners(listener == null ? null : Collections.singletonList(listener));
    }

    @Experimental
    default public EmbeddingModel addListeners(List<EmbeddingModelListener> listeners) {
        if (Utils.isNullOrEmpty(listeners)) {
            return this;
        }
        if (this instanceof ListeningEmbeddingModel) {
            ListeningEmbeddingModel listeningEmbeddingModel = (ListeningEmbeddingModel)this;
            return listeningEmbeddingModel.withAdditionalListeners(listeners);
        }
        return new ListeningEmbeddingModel(this, listeners);
    }
}

