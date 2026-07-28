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
package dev.langchain4j.model.cohere;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.cohere.CohereClient;
import dev.langchain4j.model.cohere.RerankRequest;
import dev.langchain4j.model.cohere.RerankResponse;
import dev.langchain4j.model.cohere.Result;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.scoring.ScoringModel;
import java.net.Proxy;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class CohereScoringModel
implements ScoringModel {
    private static final String DEFAULT_BASE_URL = "https://api.cohere.ai/v1/";
    private final CohereClient client;
    private final String modelName;
    private final Integer maxRetries;

    @Deprecated
    public CohereScoringModel(String baseUrl, String apiKey, String modelName, Duration timeout, Integer maxRetries, Proxy proxy, Boolean logRequests, Boolean logResponses) {
        this.client = CohereClient.builder().baseUrl((String)Utils.getOrDefault((Object)baseUrl, (Object)DEFAULT_BASE_URL)).apiKey(ValidationUtils.ensureNotBlank((String)apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)timeout, (Object)Duration.ofSeconds(60L))).proxy(proxy).logRequests((Boolean)Utils.getOrDefault((Object)logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)logResponses, (Object)false)).build();
        this.modelName = modelName;
        this.maxRetries = (Integer)Utils.getOrDefault((Object)maxRetries, (Object)2);
    }

    public CohereScoringModel(CohereScoringModelBuilder builder) {
        this.client = CohereClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)DEFAULT_BASE_URL)).apiKey(ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).proxy(builder.proxy).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false)).logger(builder.logger).build();
        this.modelName = builder.modelName;
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    @Deprecated
    public static CohereScoringModel withApiKey(String apiKey) {
        return CohereScoringModel.builder().apiKey(apiKey).build();
    }

    public static CohereScoringModelBuilder builder() {
        return new CohereScoringModelBuilder();
    }

    public Response<List<Double>> scoreAll(List<TextSegment> segments, String query) {
        RerankRequest request = RerankRequest.builder().model(this.modelName).query(query).documents(segments.stream().map(TextSegment::text).collect(Collectors.toList())).build();
        RerankResponse response = (RerankResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.rerank(request), (int)this.maxRetries);
        List scores = response.getResults().stream().sorted(Comparator.comparingInt(Result::getIndex)).map(Result::getRelevanceScore).collect(Collectors.toList());
        return Response.from(scores, (TokenUsage)new TokenUsage(response.getMeta().getBilledUnits().getSearchUnits()));
    }

    public static class CohereScoringModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String modelName;
        private Duration timeout;
        private Integer maxRetries;
        private Proxy proxy;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;

        CohereScoringModelBuilder() {
        }

        public CohereScoringModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public CohereScoringModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public CohereScoringModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public CohereScoringModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public CohereScoringModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public CohereScoringModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        @Deprecated
        public CohereScoringModelBuilder proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public CohereScoringModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public CohereScoringModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public CohereScoringModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public CohereScoringModel build() {
            return new CohereScoringModel(this);
        }

        public String toString() {
            return "CohereScoringModel.CohereScoringModelBuilder(baseUrl=" + this.baseUrl + ", apiKey=" + (this.apiKey == null ? null : "********") + ", modelName=" + this.modelName + ", timeout=" + this.timeout + ", maxRetries=" + this.maxRetries + ", proxy=" + this.proxy + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

