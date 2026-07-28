/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.catalog.ModelCatalog
 *  dev.langchain4j.model.catalog.ModelDescription
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.catalog.ModelCatalog;
import dev.langchain4j.model.catalog.ModelDescription;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.models.ModelsListResponse;
import dev.langchain4j.model.openai.internal.models.OpenAiModelInfo;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class OpenAiModelCatalog
implements ModelCatalog {
    private final OpenAiClient client;

    private OpenAiModelCatalog(Builder builder) {
        this.client = ((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)OpenAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.openai.com/v1/"))).apiKey(builder.apiKey)).organizationId(builder.organizationId)).projectId(builder.projectId)).connectTimeout(builder.connectTimeout)).readTimeout(builder.readTimeout)).userAgent(builder.userAgent)).logRequests(builder.logRequests)).logResponses(builder.logResponses)).logger(builder.logger)).customHeaders(builder.customHeaders)).customQueryParams(builder.customQueryParams)).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<ModelDescription> listModels() {
        ModelsListResponse response = this.client.listModels().execute();
        List<ModelDescription> models = response.getData().stream().map(this::mapToModelDescription).collect(Collectors.toList());
        return models;
    }

    public ModelProvider provider() {
        return ModelProvider.OPEN_AI;
    }

    private ModelDescription mapToModelDescription(OpenAiModelInfo modelInfo) {
        return ModelDescription.builder().name(modelInfo.id()).provider(ModelProvider.OPEN_AI).owner(modelInfo.ownedBy()).createdAt(modelInfo.created() != null ? Instant.ofEpochSecond(modelInfo.created()) : null).build();
    }

    public static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String organizationId;
        private String projectId;
        private Duration connectTimeout;
        private Duration readTimeout;
        private String userAgent;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Map<String, String> customHeaders;
        private Map<String, String> customQueryParams;

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
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

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public Builder customQueryParams(Map<String, String> customQueryParams) {
            this.customQueryParams = customQueryParams;
            return this;
        }

        public OpenAiModelCatalog build() {
            return new OpenAiModelCatalog(this);
        }
    }
}

