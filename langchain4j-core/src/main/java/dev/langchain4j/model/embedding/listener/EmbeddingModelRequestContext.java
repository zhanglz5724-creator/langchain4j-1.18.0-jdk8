/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding.listener;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.request.EmbeddingRequest;
import java.util.List;
import java.util.Map;

@Experimental
public class EmbeddingModelRequestContext {
    private final EmbeddingRequest embeddingRequest;
    private final EmbeddingModel embeddingModel;
    private final Map<Object, Object> attributes;
    private final List<TextSegment> textSegments;

    public EmbeddingModelRequestContext(Builder builder) {
        this.embeddingRequest = builder.embeddingRequest;
        this.embeddingModel = ValidationUtils.ensureNotNull(builder.embeddingModel, "embeddingModel");
        this.attributes = ValidationUtils.ensureNotNull(builder.attributes, "attributes");
        this.textSegments = Utils.copy(ValidationUtils.ensureNotNull(builder.textSegments, "textSegments"));
    }

    public EmbeddingRequest embeddingRequest() {
        return this.embeddingRequest;
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

    public List<TextSegment> textSegments() {
        return this.textSegments;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Experimental
    public static class Builder {
        private EmbeddingRequest embeddingRequest;
        private EmbeddingModel embeddingModel;
        private Map<Object, Object> attributes;
        private List<TextSegment> textSegments;

        Builder() {
        }

        public Builder embeddingRequest(EmbeddingRequest embeddingRequest) {
            this.embeddingRequest = embeddingRequest;
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

        public Builder textSegments(List<TextSegment> textSegments) {
            this.textSegments = textSegments;
            return this;
        }

        public EmbeddingModelRequestContext build() {
            return new EmbeddingModelRequestContext(this);
        }
    }
}

