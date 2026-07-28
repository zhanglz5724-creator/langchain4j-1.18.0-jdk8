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
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.nomic;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.nomic.EmbeddingRequest;
import dev.langchain4j.model.nomic.EmbeddingResponse;
import dev.langchain4j.model.nomic.NomicClient;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class NomicEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private static final String DEFAULT_BASE_URL = "https://api-atlas.nomic.ai/v1/";
    private final NomicClient client;
    private final String modelName;
    private final String taskType;
    private final Integer maxSegmentsPerBatch;
    private final Integer maxRetries;

    @Deprecated
    public NomicEmbeddingModel(String baseUrl, String apiKey, String modelName, String taskType, Integer maxSegmentsPerBatch, Duration timeout, Integer maxRetries, Boolean logRequests, Boolean logResponses) {
        this.client = NomicClient.builder().baseUrl((String)Utils.getOrDefault((Object)baseUrl, (Object)DEFAULT_BASE_URL)).apiKey(ValidationUtils.ensureNotBlank((String)apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)logResponses, (Object)false)).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.taskType = taskType;
        this.maxSegmentsPerBatch = (Integer)Utils.getOrDefault((Object)maxSegmentsPerBatch, (Object)500);
        this.maxRetries = (Integer)Utils.getOrDefault((Object)maxRetries, (Object)2);
    }

    public NomicEmbeddingModel(NomicEmbeddingModelBuilder builder) {
        this.client = NomicClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)DEFAULT_BASE_URL)).apiKey(ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false)).logger(builder.logger).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.taskType = builder.taskType;
        this.maxSegmentsPerBatch = (Integer)Utils.getOrDefault((Object)builder.maxSegmentsPerBatch, (Object)500);
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    public static NomicEmbeddingModelBuilder builder() {
        return new NomicEmbeddingModelBuilder();
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        List<String> texts = textSegments.stream().map(TextSegment::text).collect(Collectors.toList());
        return this.embedTexts(texts);
    }

    public String modelName() {
        return this.modelName;
    }

    private Response<List<Embedding>> embedTexts(List<String> texts) {
        ArrayList<Embedding> embeddings = new ArrayList<Embedding>();
        int inputTokenCount = 0;
        for (int i = 0; i < texts.size(); i += this.maxSegmentsPerBatch.intValue()) {
            List<String> batch = texts.subList(i, Math.min(i + this.maxSegmentsPerBatch, texts.size()));
            EmbeddingRequest request = EmbeddingRequest.builder().model(this.modelName).texts(batch).taskType(this.taskType).build();
            EmbeddingResponse response = (EmbeddingResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.embed(request), (int)this.maxRetries);
            embeddings.addAll(this.getEmbeddings(response));
            inputTokenCount += this.getTokenUsage(response).intValue();
        }
        return Response.from(embeddings, (TokenUsage)new TokenUsage(Integer.valueOf(inputTokenCount), Integer.valueOf(0)));
    }

    private List<Embedding> getEmbeddings(EmbeddingResponse response) {
        return response.getEmbeddings().stream().map(Embedding::from).collect(Collectors.toList());
    }

    private Integer getTokenUsage(EmbeddingResponse response) {
        if (response.getUsage() != null) {
            return response.getUsage().getTotalTokens();
        }
        return 0;
    }

    public static class NomicEmbeddingModelBuilder {
        private String baseUrl;
        private String apiKey;
        private String modelName;
        private String taskType;
        private Integer maxSegmentsPerBatch;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private HttpClientBuilder httpClientBuilder;

        NomicEmbeddingModelBuilder() {
        }

        public NomicEmbeddingModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public NomicEmbeddingModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public NomicEmbeddingModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public NomicEmbeddingModelBuilder taskType(String taskType) {
            this.taskType = taskType;
            return this;
        }

        public NomicEmbeddingModelBuilder maxSegmentsPerBatch(Integer maxSegmentsPerBatch) {
            this.maxSegmentsPerBatch = maxSegmentsPerBatch;
            return this;
        }

        public NomicEmbeddingModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public NomicEmbeddingModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public NomicEmbeddingModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public NomicEmbeddingModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public NomicEmbeddingModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public NomicEmbeddingModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public NomicEmbeddingModel build() {
            return new NomicEmbeddingModel(this);
        }

        public String toString() {
            return "NomicEmbeddingModel.NomicEmbeddingModelBuilder(baseUrl=" + this.baseUrl + ", apiKey=" + (this.apiKey == null ? null : "********") + ", modelName=" + this.modelName + ", taskType=" + this.taskType + ", maxSegmentsPerBatch=" + this.maxSegmentsPerBatch + ", timeout=" + this.timeout + ", maxRetries=" + this.maxRetries + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

