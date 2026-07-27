/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding.listener;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.request.EmbeddingRequest;
import dev.langchain4j.model.embedding.response.EmbeddingResponse;
import dev.langchain4j.model.output.Response;
import java.util.List;
import java.util.Map;

@Experimental
public class EmbeddingModelResponseContext {
    private final EmbeddingRequest embeddingRequest;
    private final EmbeddingResponse embeddingResponse;
    private final EmbeddingModel embeddingModel;
    private final Map<Object, Object> attributes;
    private final Response<List<Embedding>> response;
    private final List<TextSegment> textSegments;

    public EmbeddingModelResponseContext(Builder builder) {
        this.embeddingRequest = builder.embeddingRequest;
        this.embeddingResponse = builder.embeddingResponse;
        this.embeddingModel = ValidationUtils.ensureNotNull(builder.embeddingModel, "embeddingModel");
        this.attributes = ValidationUtils.ensureNotNull(builder.attributes, "attributes");
        this.response = ValidationUtils.ensureNotNull(builder.response, "response");
        this.textSegments = Utils.copy(ValidationUtils.ensureNotNull(builder.textSegments, "textSegments"));
    }

    public EmbeddingRequest embeddingRequest() {
        return this.embeddingRequest;
    }

    public EmbeddingResponse embeddingResponse() {
        return this.embeddingResponse;
    }

    public EmbeddingModel embeddingModel() {
        return this.embeddingModel;
    }

    public ModelProvider modelProvider() {
        return this.embeddingModel.provider();
    }

    public Map<Object, Object> attributes() {
        return this.attributes;
    }

    public Response<List<Embedding>> response() {
        return this.response;
    }

    public List<TextSegment> textSegments() {
        return this.textSegments;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Experimental
    public static class Builder {
        private EmbeddingRequest embeddingRequest;
        private EmbeddingResponse embeddingResponse;
        private EmbeddingModel embeddingModel;
        private Map<Object, Object> attributes;
        private Response<List<Embedding>> response;
        private List<TextSegment> textSegments;

        Builder() {
        }

        public Builder embeddingRequest(EmbeddingRequest embeddingRequest) {
            this.embeddingRequest = embeddingRequest;
            return this;
        }

        public Builder embeddingResponse(EmbeddingResponse embeddingResponse) {
            this.embeddingResponse = embeddingResponse;
            return this;
        }

        public Builder embeddingModel(EmbeddingModel embeddingModel) {
            this.embeddingModel = embeddingModel;
            return this;
        }

        public Builder attributes(Map<Object, Object> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder response(Response<List<Embedding>> response) {
            this.response = response;
            return this;
        }

        public Builder textSegments(List<TextSegment> textSegments) {
            this.textSegments = textSegments;
            return this;
        }

        public EmbeddingModelResponseContext build() {
            return new EmbeddingModelResponseContext(this);
        }
    }
}

