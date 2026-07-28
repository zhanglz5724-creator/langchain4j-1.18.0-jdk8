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
package dev.langchain4j.model.nomic;

import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.log.LoggingHttpClient;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.nomic.EmbeddingRequest;
import dev.langchain4j.model.nomic.EmbeddingResponse;
import dev.langchain4j.model.nomic.NomicJsonUtils;
import java.time.Duration;
import org.slf4j.Logger;

class NomicClient {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String authorizationHeader;

    NomicClient(NomicClientBuilder builder) {
        HttpClientBuilder httpClientBuilder = (HttpClientBuilder)Utils.getOrDefault((Object)builder.httpClientBuilder, HttpClientBuilderLoader::loadHttpClientBuilder);
        HttpClient httpClient = httpClientBuilder.connectTimeout(builder.timeout).readTimeout(builder.timeout).build();
        this.httpClient = builder.logRequests != null && builder.logRequests != false || builder.logResponses != null && builder.logResponses != false ? new LoggingHttpClient(httpClient, builder.logRequests, builder.logResponses, builder.logger) : httpClient;
        this.baseUrl = Utils.ensureTrailingForwardSlash((String)builder.baseUrl);
        this.authorizationHeader = "Bearer " + ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey");
    }

    public static NomicClientBuilder builder() {
        return new NomicClientBuilder();
    }

    public EmbeddingResponse embed(EmbeddingRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl + "embedding/text").addHeader("Content-Type", new String[]{"application/json"}).addHeader("Authorization", new String[]{this.authorizationHeader}).body(NomicJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse response = this.httpClient.execute(httpRequest);
        return NomicJsonUtils.fromJson(response.body(), EmbeddingResponse.class);
    }

    public static class NomicClientBuilder {
        private String baseUrl;
        private String apiKey;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private HttpClientBuilder httpClientBuilder;

        NomicClientBuilder() {
        }

        public NomicClientBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public NomicClientBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public NomicClientBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public NomicClientBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public NomicClientBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public NomicClientBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public NomicClientBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public NomicClient build() {
            return new NomicClient(this);
        }

        public String toString() {
            return "NomicClient.NomicClientBuilder(baseUrl=" + this.baseUrl + ", apiKey=" + (this.apiKey == null ? null : "********") + ", timeout=" + this.timeout + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

