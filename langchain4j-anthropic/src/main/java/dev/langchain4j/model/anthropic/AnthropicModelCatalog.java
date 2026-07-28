/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.catalog.ModelCatalog
 *  dev.langchain4j.model.catalog.ModelDescription
 *  dev.langchain4j.model.catalog.ModelDescription$Builder
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.anthropic;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.anthropic.internal.api.AnthropicModelInfo;
import dev.langchain4j.model.anthropic.internal.api.AnthropicModelsListResponse;
import dev.langchain4j.model.anthropic.internal.client.AnthropicClient;
import dev.langchain4j.model.catalog.ModelCatalog;
import dev.langchain4j.model.catalog.ModelDescription;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class AnthropicModelCatalog
implements ModelCatalog {
    private final AnthropicClient client;

    private AnthropicModelCatalog(Builder builder) {
        this.client = ((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)AnthropicClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.anthropic.com/v1/"))).apiKey(builder.apiKey)).version((String)Utils.getOrDefault((Object)builder.version, (Object)"2023-06-01"))).timeout(builder.timeout)).logRequests(builder.logRequests)).logResponses(builder.logResponses)).logger(builder.logger)).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<ModelDescription> listModels() {
        AnthropicModelsListResponse response = this.client.listModels();
        List<ModelDescription> models = response.data.stream().map(this::mapToModelDescription).collect(Collectors.toList());
        return models;
    }

    private ModelDescription mapToModelDescription(AnthropicModelInfo modelInfo) {
        ModelDescription.Builder builder = ModelDescription.builder().name(modelInfo.id).provider(ModelProvider.ANTHROPIC).displayName(Utils.isNullOrBlank((String)modelInfo.displayName) ? null : modelInfo.displayName).createdAt(modelInfo.createdAt != null ? AnthropicModelCatalog.parse(modelInfo.createdAt) : null);
        if (modelInfo.maxInputTokens != null) {
            builder.maxInputTokens(modelInfo.maxInputTokens);
        }
        if (modelInfo.maxOutputTokens != null) {
            builder.maxOutputTokens(modelInfo.maxOutputTokens);
        }
        return builder.build();
    }

    private static Instant parse(String createdAt) {
        try {
            return Instant.from(DateTimeFormatter.ISO_INSTANT.parse(createdAt));
        }
        catch (DateTimeParseException e) {
            return null;
        }
    }

    public ModelProvider provider() {
        return ModelProvider.ANTHROPIC;
    }

    public static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String version;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;

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

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
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

        public AnthropicModelCatalog build() {
            return new AnthropicModelCatalog(this);
        }
    }
}

