/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.catalog.ModelCatalog
 *  dev.langchain4j.model.catalog.ModelDescription
 *  dev.langchain4j.model.catalog.ModelDescription$Builder
 *  dev.langchain4j.model.catalog.ModelType
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.catalog.ModelCatalog;
import dev.langchain4j.model.catalog.ModelDescription;
import dev.langchain4j.model.catalog.ModelType;
import dev.langchain4j.model.googleai.GeminiModelInfo;
import dev.langchain4j.model.googleai.GeminiModelsListResponse;
import dev.langchain4j.model.googleai.GeminiService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class GoogleAiGeminiModelCatalog
implements ModelCatalog {
    private final GeminiService geminiService;

    private GoogleAiGeminiModelCatalog(Builder builder) {
        this.geminiService = new GeminiService(builder.httpClientBuilder, builder.apiKey, builder.baseUrl, builder.logRequestsAndResponses, builder.logRequests, builder.logResponses, builder.logger, builder.timeout, null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<ModelDescription> listModels() {
        GeminiModelsListResponse response;
        ArrayList<ModelDescription> allModels = new ArrayList<ModelDescription>();
        String pageToken = null;
        do {
            if ((response = this.geminiService.listModels(null, pageToken)).models() == null) continue;
            List pageModels = response.models().stream().map(this::mapToModelDescription).collect(Collectors.toList());
            allModels.addAll(pageModels);
        } while ((pageToken = response.nextPageToken()) != null);
        return allModels;
    }

    public ModelProvider provider() {
        return ModelProvider.GOOGLE_AI_GEMINI;
    }

    private ModelDescription mapToModelDescription(GeminiModelInfo modelInfo) {
        ModelDescription.Builder builder = ModelDescription.builder().provider(ModelProvider.GOOGLE_AI_GEMINI);
        if (modelInfo.name() != null) {
            String id = modelInfo.name().startsWith("models/") ? modelInfo.name().substring(7) : modelInfo.name();
            builder.name(id);
        }
        if (modelInfo.displayName() != null && !modelInfo.displayName().isEmpty()) {
            builder.displayName(modelInfo.displayName());
        }
        if (modelInfo.description() != null) {
            builder.description(modelInfo.description());
        }
        if (modelInfo.inputTokenLimit() != null) {
            builder.maxInputTokens(modelInfo.inputTokenLimit());
        }
        if (modelInfo.outputTokenLimit() != null) {
            builder.maxOutputTokens(modelInfo.outputTokenLimit());
        }
        if (modelInfo.supportedGenerationMethods() != null) {
            if (modelInfo.supportedGenerationMethods().contains("generateContent")) {
                builder.type(ModelType.CHAT);
            } else if (modelInfo.supportedGenerationMethods().contains("embedContent")) {
                builder.type(ModelType.EMBEDDING);
            }
        }
        return builder.build();
    }

    public static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private boolean logRequestsAndResponses;
        private boolean logRequests;
        private boolean logResponses;
        private Logger logger;
        private Duration timeout;

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

        public Builder logRequestsAndResponses(boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
            return this;
        }

        public Builder logRequests(boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public GoogleAiGeminiModelCatalog build() {
            return new GoogleAiGeminiModelCatalog(this);
        }
    }
}

