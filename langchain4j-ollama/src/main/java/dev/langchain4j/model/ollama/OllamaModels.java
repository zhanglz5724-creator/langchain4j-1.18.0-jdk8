/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.output.Response
 */
package dev.langchain4j.model.ollama;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ollama.DeleteModelRequest;
import dev.langchain4j.model.ollama.ModelsListResponse;
import dev.langchain4j.model.ollama.OllamaClient;
import dev.langchain4j.model.ollama.OllamaModel;
import dev.langchain4j.model.ollama.OllamaModelCard;
import dev.langchain4j.model.ollama.RunningModelsListResponse;
import dev.langchain4j.model.ollama.RunningOllamaModel;
import dev.langchain4j.model.ollama.ShowModelInformationRequest;
import dev.langchain4j.model.output.Response;
import java.time.Duration;
import java.util.List;

public class OllamaModels {
    private final OllamaClient client;
    private final Integer maxRetries;

    public OllamaModels(OllamaModelsBuilder builder) {
        this.client = OllamaClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl(builder.baseUrl).timeout(builder.timeout).logRequests(builder.logRequests).logResponses(builder.logResponses).build();
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    public static OllamaModelsBuilder builder() {
        return new OllamaModelsBuilder();
    }

    public Response<List<OllamaModel>> availableModels() {
        ModelsListResponse response = (ModelsListResponse)RetryUtils.withRetryMappingExceptions(this.client::listModels, (int)this.maxRetries);
        return Response.from(response.getModels());
    }

    public Response<OllamaModelCard> modelCard(OllamaModel ollamaModel) {
        return this.modelCard(ollamaModel.getName());
    }

    public Response<OllamaModelCard> modelCard(String modelName) {
        OllamaModelCard response = (OllamaModelCard)RetryUtils.withRetryMappingExceptions(() -> this.client.showInformation(ShowModelInformationRequest.builder().name(modelName).build()), (int)this.maxRetries);
        return Response.from(response);
    }

    public void deleteModel(OllamaModel ollamaModel) {
        this.deleteModel(ollamaModel.getName());
    }

    public void deleteModel(String ollamaModelName) {
        RetryUtils.withRetryMappingExceptions(() -> this.client.deleteModel(DeleteModelRequest.builder().name(ollamaModelName).build()), (int)this.maxRetries);
    }

    public Response<List<RunningOllamaModel>> runningModels() {
        RunningModelsListResponse response = (RunningModelsListResponse)RetryUtils.withRetryMappingExceptions(this.client::listRunningModels, (int)this.maxRetries);
        return Response.from(response.getModels());
    }

    public static class OllamaModelsBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;

        public OllamaModelsBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public OllamaModelsBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public OllamaModelsBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public OllamaModelsBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public OllamaModelsBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public OllamaModelsBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OllamaModels build() {
            return new OllamaModels(this);
        }
    }
}

