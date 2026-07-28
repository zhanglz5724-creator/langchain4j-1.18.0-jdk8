/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.mistralai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.mistralai.MistralAiEmbeddingModelName;
import dev.langchain4j.model.mistralai.internal.api.MistralAiEmbeddingRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiEmbeddingResponse;
import dev.langchain4j.model.mistralai.internal.client.MistralAiClient;
import dev.langchain4j.model.mistralai.internal.mapper.MistralAiMapper;
import dev.langchain4j.model.mistralai.spi.MistralAiEmbeddingModelBuilderFactory;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class MistralAiEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private static final String EMBEDDINGS_ENCODING_FORMAT = "float";
    private final MistralAiClient client;
    private final String modelName;
    private final Integer maxRetries;

    public MistralAiEmbeddingModel(MistralAiEmbeddingModelBuilder builder) {
        this.client = ((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)MistralAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.mistral.ai/v1"))).apiKey(builder.apiKey)).timeout(builder.timeout)).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).customHeaders(builder.customHeadersSupplier)).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        MistralAiEmbeddingRequest request = MistralAiEmbeddingRequest.builder().model(this.modelName).input(textSegments.stream().map(TextSegment::text).collect(Collectors.toList())).encodingFormat(EMBEDDINGS_ENCODING_FORMAT).build();
        MistralAiEmbeddingResponse response = (MistralAiEmbeddingResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.embedding(request), (int)this.maxRetries);
        List embeddings = response.getData().stream().map(mistralAiEmbedding -> Embedding.from(mistralAiEmbedding.getEmbedding())).collect(Collectors.toList());
        return Response.from(embeddings, (TokenUsage)MistralAiMapper.tokenUsageFrom(response.getUsage()));
    }

    public String modelName() {
        return this.modelName;
    }

    public static MistralAiEmbeddingModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(MistralAiEmbeddingModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            MistralAiEmbeddingModelBuilderFactory factory = (MistralAiEmbeddingModelBuilderFactory)iterator.next();
            return (MistralAiEmbeddingModelBuilder)factory.get();
        }
        return new MistralAiEmbeddingModelBuilder();
    }

    public static class MistralAiEmbeddingModelBuilder {
        private String baseUrl;
        private String apiKey;
        private String modelName;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Integer maxRetries;
        private HttpClientBuilder httpClientBuilder;
        private Supplier<Map<String, String>> customHeadersSupplier;

        public MistralAiEmbeddingModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public MistralAiEmbeddingModelBuilder modelName(MistralAiEmbeddingModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public MistralAiEmbeddingModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public MistralAiEmbeddingModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public MistralAiEmbeddingModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public MistralAiEmbeddingModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public MistralAiEmbeddingModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public MistralAiEmbeddingModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public MistralAiEmbeddingModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public MistralAiEmbeddingModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public MistralAiEmbeddingModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public MistralAiEmbeddingModel build() {
            return new MistralAiEmbeddingModel(this);
        }
    }
}

