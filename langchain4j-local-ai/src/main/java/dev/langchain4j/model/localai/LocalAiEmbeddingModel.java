/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.openai.internal.OpenAiClient
 *  dev.langchain4j.model.openai.internal.embedding.EmbeddingRequest
 *  dev.langchain4j.model.openai.internal.embedding.EmbeddingResponse
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.localai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.localai.spi.LocalAiEmbeddingModelBuilderFactory;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.embedding.EmbeddingRequest;
import dev.langchain4j.model.openai.internal.embedding.EmbeddingResponse;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class LocalAiEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private final OpenAiClient client;
    private final String modelName;
    private final Integer maxRetries;

    @Deprecated
    public LocalAiEmbeddingModel(String baseUrl, String modelName, Duration timeout, Integer maxRetries, Boolean logRequests, Boolean logResponses) {
        timeout = timeout == null ? Duration.ofSeconds(60L) : timeout;
        maxRetries = maxRetries == null ? 3 : maxRetries;
        this.client = OpenAiClient.builder().baseUrl(ValidationUtils.ensureNotBlank((String)baseUrl, (String)"baseUrl")).connectTimeout(timeout).readTimeout(timeout).logRequests(logRequests).logResponses(logResponses).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.maxRetries = maxRetries;
    }

    public LocalAiEmbeddingModel(LocalAiEmbeddingModelBuilder builder) {
        this.client = OpenAiClient.builder().baseUrl(ValidationUtils.ensureNotBlank((String)builder.baseUrl, (String)"baseUrl")).connectTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).readTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).logRequests(builder.logRequests).logResponses(builder.logResponses).logger(builder.logger).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)3);
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        List texts = textSegments.stream().map(TextSegment::text).collect(Collectors.toList());
        EmbeddingRequest request = EmbeddingRequest.builder().input(texts).model(this.modelName).build();
        EmbeddingResponse response = (EmbeddingResponse)RetryUtils.withRetryMappingExceptions(() -> (EmbeddingResponse)this.client.embedding(request).execute(), (int)this.maxRetries);
        List<Embedding> embeddings = (List<Embedding>)(List) response.data().stream().map(openAiEmbedding -> Embedding.from((List)openAiEmbedding.embedding())).collect(Collectors.toList());
        return Response.from(embeddings);
    }

    public String modelName() {
        return this.modelName;
    }

    public static LocalAiEmbeddingModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(LocalAiEmbeddingModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            LocalAiEmbeddingModelBuilderFactory factory = (LocalAiEmbeddingModelBuilderFactory)iterator.next();
            return (LocalAiEmbeddingModelBuilder)factory.get();
        }
        return new LocalAiEmbeddingModelBuilder();
    }

    public static class LocalAiEmbeddingModelBuilder {
        private String baseUrl;
        private String modelName;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;

        public LocalAiEmbeddingModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public LocalAiEmbeddingModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public LocalAiEmbeddingModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public LocalAiEmbeddingModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public LocalAiEmbeddingModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public LocalAiEmbeddingModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public LocalAiEmbeddingModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public LocalAiEmbeddingModel build() {
            return new LocalAiEmbeddingModel(this);
        }

        public String toString() {
            return "LocalAiEmbeddingModel.LocalAiEmbeddingModelBuilder(baseUrl=" + this.baseUrl + ", modelName=" + this.modelName + ", timeout=" + this.timeout + ", maxRetries=" + this.maxRetries + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

