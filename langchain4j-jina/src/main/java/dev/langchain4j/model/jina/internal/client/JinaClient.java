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
 *  dev.langchain4j.http.client.log.LoggingHttpClient
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.jina.internal.client;

import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.log.LoggingHttpClient;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.jina.internal.api.JinaEmbeddingRequest;
import dev.langchain4j.model.jina.internal.api.JinaEmbeddingResponse;
import dev.langchain4j.model.jina.internal.api.JinaMultimodalEmbeddingRequest;
import dev.langchain4j.model.jina.internal.api.JinaRerankingRequest;
import dev.langchain4j.model.jina.internal.api.JinaRerankingResponse;
import dev.langchain4j.model.jina.internal.client.JinaJsonUtils;
import java.time.Duration;
import org.slf4j.Logger;

public class JinaClient {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String authorizationHeader;

    JinaClient(JinaClientBuilder builder) {
        HttpClientBuilder httpClientBuilder = (HttpClientBuilder)Utils.getOrDefault((Object)builder.httpClientBuilder, HttpClientBuilderLoader::loadHttpClientBuilder);
        HttpClient httpClient = httpClientBuilder.connectTimeout(builder.timeout).readTimeout(builder.timeout).build();
        this.httpClient = builder.logRequests || builder.logResponses ? new LoggingHttpClient(httpClient, Boolean.valueOf(builder.logRequests), Boolean.valueOf(builder.logResponses), builder.logger) : httpClient;
        this.baseUrl = Utils.ensureTrailingForwardSlash((String)builder.baseUrl);
        this.authorizationHeader = "Bearer " + ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey");
    }

    public static JinaClientBuilder builder() {
        return new JinaClientBuilder();
    }

    public JinaEmbeddingResponse embed(JinaEmbeddingRequest request) {
        return this.post("v1/embeddings", request, JinaEmbeddingResponse.class);
    }

    public JinaEmbeddingResponse embedMultimodal(JinaMultimodalEmbeddingRequest request) {
        return this.post("v1/embeddings", request, JinaEmbeddingResponse.class);
    }

    public JinaRerankingResponse rerank(JinaRerankingRequest request) {
        return this.post("rerank", request, JinaRerankingResponse.class);
    }

    private <T> T post(String path, Object request, Class<T> responseType) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl + path).addHeader("Content-Type", new String[]{"application/json"}).addHeader("Authorization", new String[]{this.authorizationHeader}).body(JinaJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse response = this.httpClient.execute(httpRequest);
        return JinaJsonUtils.fromJson(response.body(), responseType);
    }

    public static class JinaClientBuilder {
        private String baseUrl;
        private String apiKey;
        private Duration timeout;
        private boolean logRequests;
        private boolean logResponses;
        private Logger logger;
        private HttpClientBuilder httpClientBuilder;

        JinaClientBuilder() {
        }

        public JinaClientBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public JinaClientBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public JinaClientBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public JinaClientBuilder logRequests(boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public JinaClientBuilder logResponses(boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public JinaClientBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public JinaClientBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public JinaClient build() {
            return new JinaClient(this);
        }

        public String toString() {
            return "JinaClient.JinaClientBuilder(baseUrl=" + this.baseUrl + ", apiKey=" + (this.apiKey == null ? null : "********") + ", timeout=" + this.timeout + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

