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
package dev.langchain4j.model.voyageai;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.model.voyageai.RerankRequest;
import dev.langchain4j.model.voyageai.RerankResponse;
import dev.langchain4j.model.voyageai.VoyageAiClient;
import dev.langchain4j.model.voyageai.VoyageAiScoringModelName;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class VoyageAiScoringModel
implements ScoringModel {
    private final VoyageAiClient client;
    private final Integer maxRetries;
    private final String modelName;
    private final Integer topK;
    private final Boolean truncation;

    @Deprecated
    public VoyageAiScoringModel(HttpClientBuilder httpClientBuilder, Map<String, String> customHeaders, String baseUrl, Duration timeout, Integer maxRetries, String apiKey, String modelName, Integer topK, Boolean truncation, Boolean logRequests, Boolean logResponses) {
        this.maxRetries = (Integer)Utils.getOrDefault((Object)maxRetries, (Object)2);
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.truncation = truncation;
        this.topK = topK;
        this.client = VoyageAiClient.builder().httpClientBuilder(httpClientBuilder).baseUrl((String)Utils.getOrDefault((Object)baseUrl, (Object)"https://api.voyageai.com/v1/")).apiKey(ValidationUtils.ensureNotBlank((String)apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)logResponses, (Object)false)).customHeaders(() -> customHeaders).build();
    }

    public VoyageAiScoringModel(Builder builder) {
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.truncation = builder.truncation;
        this.topK = builder.topK;
        this.client = VoyageAiClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.voyageai.com/v1/")).apiKey(ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false)).logger(builder.logger).customHeaders(builder.customHeadersSupplier).build();
    }

    public Response<List<Double>> scoreAll(List<TextSegment> segments, String query) {
        RerankRequest request = RerankRequest.builder().model(this.modelName).query(query).documents(segments.stream().map(TextSegment::text).collect(Collectors.toList())).topK(this.topK).truncation(this.truncation).build();
        RerankResponse response = (RerankResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.rerank(request), (int)this.maxRetries);
        List scores = response.getData().stream().sorted(Comparator.comparingInt(RerankResponse.RerankData::getIndex)).map(RerankResponse.RerankData::getRelevanceScore).collect(Collectors.toList());
        return Response.from(scores, (TokenUsage)new TokenUsage(response.getUsage().getTotalTokens()));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private Supplier<Map<String, String>> customHeadersSupplier;
        private String baseUrl;
        private Duration timeout;
        private Integer maxRetries;
        private String apiKey;
        private String modelName;
        private Integer topK;
        private Boolean truncation;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public Builder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder modelName(VoyageAiScoringModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public Builder truncation(Boolean truncation) {
            this.truncation = truncation;
            return this;
        }

        public Builder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public VoyageAiScoringModel build() {
            return new VoyageAiScoringModel(this);
        }
    }
}

