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
package dev.langchain4j.model.cohere;

import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.log.LoggingHttpClient;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.cohere.CohereJsonUtils;
import dev.langchain4j.model.cohere.EmbedRequest;
import dev.langchain4j.model.cohere.EmbedResponse;
import dev.langchain4j.model.cohere.EmbedV2Request;
import dev.langchain4j.model.cohere.EmbedV2Response;
import dev.langchain4j.model.cohere.RerankRequest;
import dev.langchain4j.model.cohere.RerankResponse;
import java.net.Proxy;
import java.time.Duration;
import org.slf4j.Logger;

class CohereClient {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String authorizationHeader;

    CohereClient(CohereClientBuilder builder) {
        HttpClientBuilder httpClientBuilder = (HttpClientBuilder)Utils.getOrDefault((Object)builder.httpClientBuilder, HttpClientBuilderLoader::loadHttpClientBuilder);
        Duration timeout = (Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L));
        HttpClient httpClient = httpClientBuilder.connectTimeout(timeout).readTimeout(timeout).build();
        this.httpClient = builder.logRequests != null && builder.logRequests != false || builder.logResponses != null && builder.logResponses != false ? new LoggingHttpClient(httpClient, builder.logRequests, builder.logResponses, builder.logger) : httpClient;
        this.baseUrl = Utils.ensureTrailingForwardSlash((String)builder.baseUrl);
        this.authorizationHeader = "Bearer " + ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey");
    }

    public static CohereClientBuilder builder() {
        return new CohereClientBuilder();
    }

    EmbedResponse embed(EmbedRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl + "embed").addHeader("Content-Type", new String[]{"application/json"}).addHeader("Accept", new String[]{"application/json"}).addHeader("Authorization", new String[]{this.authorizationHeader}).body(CohereJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse response = this.httpClient.execute(httpRequest);
        return CohereJsonUtils.fromJson(response.body(), EmbedResponse.class);
    }

    EmbedV2Response embedV2(EmbedV2Request request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl + "embed").addHeader("Content-Type", new String[]{"application/json"}).addHeader("Accept", new String[]{"application/json"}).addHeader("Authorization", new String[]{this.authorizationHeader}).body(CohereJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse response = this.httpClient.execute(httpRequest);
        return CohereJsonUtils.fromJson(response.body(), EmbedV2Response.class);
    }

    RerankResponse rerank(RerankRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl + "rerank").addHeader("Content-Type", new String[]{"application/json"}).addHeader("Accept", new String[]{"application/json"}).addHeader("Authorization", new String[]{this.authorizationHeader}).body(CohereJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse response = this.httpClient.execute(httpRequest);
        return CohereJsonUtils.fromJson(response.body(), RerankResponse.class);
    }

    public static class CohereClientBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private Duration timeout;
        private Proxy proxy;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;

        CohereClientBuilder() {
        }

        public CohereClientBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public CohereClientBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public CohereClientBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public CohereClientBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public CohereClientBuilder proxy(Proxy proxy) {
            if (proxy != null) {
                throw new UnsupportedOperationException("Proxy configuration via proxy(...) is no longer supported. Supply a custom HttpClientBuilder via httpClientBuilder(...) to configure a proxy.");
            }
            this.proxy = proxy;
            return this;
        }

        public CohereClientBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public CohereClientBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public CohereClientBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public CohereClient build() {
            return new CohereClient(this);
        }

        public String toString() {
            return "CohereClient.CohereClientBuilder(baseUrl=" + this.baseUrl + ", apiKey=" + this.apiKey + ", timeout=" + this.timeout + ", proxy=" + this.proxy + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

