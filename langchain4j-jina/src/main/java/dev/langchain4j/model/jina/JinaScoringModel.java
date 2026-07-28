/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.model.scoring.ScoringModel
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.jina;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.jina.internal.api.JinaRerankingRequest;
import dev.langchain4j.model.jina.internal.api.JinaRerankingResponse;
import dev.langchain4j.model.jina.internal.client.JinaClient;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.scoring.ScoringModel;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class JinaScoringModel
implements ScoringModel {
    private static final String DEFAULT_BASE_URL = "https://api.jina.ai/v1/";
    private final JinaClient client;
    private final String modelName;
    private final Integer maxRetries;

    @Deprecated
    public JinaScoringModel(String baseUrl, String apiKey, String modelName, Duration timeout, Integer maxRetries, Boolean logRequests, Boolean logResponses) {
        this.client = JinaClient.builder().baseUrl((String)Utils.getOrDefault((Object)baseUrl, (Object)DEFAULT_BASE_URL)).apiKey(ValidationUtils.ensureNotBlank((String)apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)logResponses, (Object)false)).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)maxRetries, (Object)2);
    }

    public JinaScoringModel(JinaScoringModelBuilder builder) {
        this.client = JinaClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)DEFAULT_BASE_URL)).apiKey(ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false)).logger(builder.logger).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    public static JinaScoringModelBuilder builder() {
        return new JinaScoringModelBuilder();
    }

    public Response<List<Double>> scoreAll(List<TextSegment> segments, String query) {
        JinaRerankingRequest request = JinaRerankingRequest.builder().model(this.modelName).query(query).documents(segments.stream().map(TextSegment::text).collect(Collectors.toList())).returnDocuments(false).build();
        JinaRerankingResponse response = (JinaRerankingResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.rerank(request), (int)this.maxRetries);
        List scores = response.results.stream().sorted(Comparator.comparingInt(result -> result.index)).map(result -> result.relevanceScore).collect(Collectors.toList());
        TokenUsage tokenUsage = new TokenUsage(response.usage.promptTokens, Integer.valueOf(0), response.usage.totalTokens);
        return Response.from(scores, (TokenUsage)tokenUsage);
    }

    public static class JinaScoringModelBuilder {
        private String baseUrl;
        private String apiKey;
        private String modelName;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private HttpClientBuilder httpClientBuilder;

        JinaScoringModelBuilder() {
        }

        public JinaScoringModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public JinaScoringModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public JinaScoringModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public JinaScoringModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public JinaScoringModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public JinaScoringModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public JinaScoringModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public JinaScoringModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public JinaScoringModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public JinaScoringModel build() {
            return new JinaScoringModel(this);
        }

        public String toString() {
            return "JinaScoringModel.JinaScoringModelBuilder(baseUrl=" + this.baseUrl + ", apiKey=" + (this.apiKey == null ? null : "********") + ", modelName=" + this.modelName + ", timeout=" + this.timeout + ", maxRetries=" + this.maxRetries + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

