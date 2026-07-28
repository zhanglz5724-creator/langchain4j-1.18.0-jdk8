/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.http.client.HttpClientBuilderLoader
 *  dev.langchain4j.http.client.HttpMethod
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.model.workersai.client;

import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.workersai.client.ApiResponse;
import dev.langchain4j.model.workersai.client.WorkersAiChatCompletionRequest;
import dev.langchain4j.model.workersai.client.WorkersAiChatCompletionResponse;
import dev.langchain4j.model.workersai.client.WorkersAiEmbeddingRequest;
import dev.langchain4j.model.workersai.client.WorkersAiEmbeddingResponse;
import dev.langchain4j.model.workersai.client.WorkersAiImageGenerationRequest;
import dev.langchain4j.model.workersai.client.WorkersAiJsonUtils;
import dev.langchain4j.model.workersai.client.WorkersAiTextCompletionRequest;
import dev.langchain4j.model.workersai.client.WorkersAiTextCompletionResponse;
import java.time.Duration;

public class WorkersAiClient {
    private static final String BASE_URL = "https://api.cloudflare.com/";
    private final HttpClient httpClient;
    private final String authorizationHeader;

    WorkersAiClient(Builder builder) {
        HttpClientBuilder httpClientBuilder = (HttpClientBuilder)Utils.getOrDefault((Object)builder.httpClientBuilder, HttpClientBuilderLoader::loadHttpClientBuilder);
        this.httpClient = httpClientBuilder.connectTimeout((Duration)Utils.getOrDefault((Object)Utils.getOrDefault((Object)builder.timeout, (Object)httpClientBuilder.connectTimeout()), (Object)Duration.ofSeconds(30L))).readTimeout((Duration)Utils.getOrDefault((Object)Utils.getOrDefault((Object)builder.timeout, (Object)httpClientBuilder.readTimeout()), (Object)Duration.ofSeconds(30L))).build();
        this.authorizationHeader = "Bearer " + builder.apiToken;
    }

    public WorkersAiChatCompletionResponse generateChat(WorkersAiChatCompletionRequest apiRequest, String accountIdentifier, String modelName) {
        return (WorkersAiChatCompletionResponse)WorkersAiClient.checkSuccess((ApiResponse)WorkersAiJsonUtils.fromJson(this.execute(apiRequest, accountIdentifier, modelName).body(), WorkersAiChatCompletionResponse.class));
    }

    public WorkersAiTextCompletionResponse generateText(WorkersAiTextCompletionRequest apiRequest, String accountIdentifier, String modelName) {
        return (WorkersAiTextCompletionResponse)WorkersAiClient.checkSuccess((ApiResponse)WorkersAiJsonUtils.fromJson(this.execute(apiRequest, accountIdentifier, modelName).body(), WorkersAiTextCompletionResponse.class));
    }

    public byte[] generateImage(WorkersAiImageGenerationRequest apiRequest, String accountIdentifier, String modelName) {
        return this.execute(apiRequest, accountIdentifier, modelName).bodyBytes();
    }

    public WorkersAiEmbeddingResponse embed(WorkersAiEmbeddingRequest apiRequest, String accountIdentifier, String modelName) {
        return (WorkersAiEmbeddingResponse)WorkersAiClient.checkSuccess((ApiResponse)WorkersAiJsonUtils.fromJson(this.execute(apiRequest, accountIdentifier, modelName).body(), WorkersAiEmbeddingResponse.class));
    }

    private static <T extends ApiResponse<?>> T checkSuccess(T response) {
        if (response == null || !response.isSuccess()) {
            StringBuilder errorMessage = new StringBuilder("Failed to generate chat message:");
            if (response != null && response.getErrors() != null) {
                errorMessage.append(response.getErrors().stream().map(ApiResponse.Error::getMessage).reduce((a, b) -> a + "\n" + b).orElse(""));
            }
            throw new RuntimeException(errorMessage.toString());
        }
        return response;
    }

    private SuccessfulHttpResponse execute(Object apiRequest, String accountIdentifier, String modelName) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url("https://api.cloudflare.com/client/v4/accounts/" + accountIdentifier + "/ai/run/" + modelName).addHeader("Content-Type", new String[]{"application/json"}).addHeader("Authorization", new String[]{this.authorizationHeader}).body(WorkersAiJsonUtils.toJson(apiRequest)).build();
        return (SuccessfulHttpResponse)ExceptionMapper.mappingException(() -> this.httpClient.execute(httpRequest));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private Duration timeout;
        private String apiToken;

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder apiToken(String apiToken) {
            this.apiToken = apiToken;
            return this;
        }

        public WorkersAiClient build() {
            return new WorkersAiClient(this);
        }
    }
}

