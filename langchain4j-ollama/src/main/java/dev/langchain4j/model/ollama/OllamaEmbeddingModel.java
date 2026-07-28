/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.embedding.listener.EmbeddingModelListener
 *  dev.langchain4j.model.embedding.request.EmbeddingInput
 *  dev.langchain4j.model.embedding.request.EmbeddingRequest
 *  dev.langchain4j.model.embedding.response.EmbeddingResponse
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.ollama;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.embedding.listener.EmbeddingModelListener;
import dev.langchain4j.model.embedding.request.EmbeddingInput;
import dev.langchain4j.model.ollama.EmbeddingRequest;
import dev.langchain4j.model.ollama.EmbeddingResponse;
import dev.langchain4j.model.ollama.OllamaClient;
import dev.langchain4j.model.ollama.spi.OllamaEmbeddingModelBuilderFactory;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class OllamaEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private final OllamaClient client;
    private final String modelName;
    private final Integer maxRetries;
    private final Integer dimensions;
    private final List<EmbeddingModelListener> listeners;

    public OllamaEmbeddingModel(OllamaEmbeddingModelBuilder builder) {
        this.client = OllamaClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl(builder.baseUrl).timeout(builder.timeout).logRequests(builder.logRequests).logResponses(builder.logResponses).customHeaders(builder.customHeadersSupplier).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.dimensions = ValidationUtils.ensureGreaterThanZeroIfNotNull((Integer)builder.dimensions, (String)"dimensions");
        this.listeners = Utils.copy((List)builder.listeners);
    }

    public List<EmbeddingModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.OLLAMA;
    }

    public static OllamaEmbeddingModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OllamaEmbeddingModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OllamaEmbeddingModelBuilderFactory factory = (OllamaEmbeddingModelBuilderFactory)iterator.next();
            return (OllamaEmbeddingModelBuilder)factory.get();
        }
        return new OllamaEmbeddingModelBuilder();
    }

    public dev.langchain4j.model.embedding.response.EmbeddingResponse doEmbed(dev.langchain4j.model.embedding.request.EmbeddingRequest request) {
        List<String> input = request.inputs().stream().map(EmbeddingInput::text).collect(Collectors.toList());
        EmbeddingRequest ollamaRequest = EmbeddingRequest.builder().model(this.modelName).input(input).dimensions(this.dimensions).build();
        EmbeddingResponse ollamaResponse = (EmbeddingResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.embed(ollamaRequest), (int)this.maxRetries);
        List embeddings = ollamaResponse.getEmbeddings().stream().map(Embedding::from).collect(Collectors.toList());
        TokenUsage tokenUsage = ollamaResponse.getPromptEvalCount() == null ? null : new TokenUsage(ollamaResponse.getPromptEvalCount());
        return dev.langchain4j.model.embedding.response.EmbeddingResponse.builder().embeddings(embeddings).modelName(this.modelName).tokenUsage(tokenUsage).build();
    }

    public String modelName() {
        return this.modelName;
    }

    protected Integer knownDimension() {
        return this.dimensions;
    }

    public static class OllamaEmbeddingModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String modelName;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Supplier<Map<String, String>> customHeadersSupplier;
        private Integer dimensions;
        private List<EmbeddingModelListener> listeners;

        public OllamaEmbeddingModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public OllamaEmbeddingModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public OllamaEmbeddingModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public OllamaEmbeddingModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public OllamaEmbeddingModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public OllamaEmbeddingModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public OllamaEmbeddingModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OllamaEmbeddingModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public OllamaEmbeddingModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public OllamaEmbeddingModelBuilder dimensions(Integer dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public OllamaEmbeddingModelBuilder listeners(List<EmbeddingModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public OllamaEmbeddingModel build() {
            return new OllamaEmbeddingModel(this);
        }
    }
}

