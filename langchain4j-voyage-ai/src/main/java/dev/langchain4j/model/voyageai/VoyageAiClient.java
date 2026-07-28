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
package dev.langchain4j.model.voyageai;

import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.log.LoggingHttpClient;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.voyageai.EmbeddingRequest;
import dev.langchain4j.model.voyageai.EmbeddingResponse;
import dev.langchain4j.model.voyageai.MultimodalEmbeddingRequest;
import dev.langchain4j.model.voyageai.RerankRequest;
import dev.langchain4j.model.voyageai.RerankResponse;
import dev.langchain4j.model.voyageai.VoyageAiJsonUtils;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;

class VoyageAiClient {
    public static final String DEFAULT_BASE_URL = "https://api.voyageai.com/v1/";
    private final HttpClient httpClient;
    private final String baseUrl;
    private final Map<String, String> defaultHeaders;
    private final Supplier<Map<String, String>> customHeadersSupplier;

    VoyageAiClient(Builder builder) {
        HttpClientBuilder httpClientBuilder = (HttpClientBuilder)Utils.getOrDefault((Object)builder.httpClientBuilder, HttpClientBuilderLoader::loadHttpClientBuilder);
        HttpClient httpClient = httpClientBuilder.connectTimeout((Duration)Utils.getOrDefault((Object)Utils.getOrDefault((Object)builder.timeout, (Object)httpClientBuilder.connectTimeout()), (Object)Duration.ofSeconds(15L))).readTimeout((Duration)Utils.getOrDefault((Object)Utils.getOrDefault((Object)builder.timeout, (Object)httpClientBuilder.readTimeout()), (Object)Duration.ofSeconds(60L))).build();
        this.httpClient = builder.logRequests != null && builder.logRequests != false || builder.logResponses != null && builder.logResponses != false ? new LoggingHttpClient(httpClient, builder.logRequests, builder.logResponses, builder.logger) : httpClient;
        this.baseUrl = Utils.ensureTrailingForwardSlash((String)ValidationUtils.ensureNotBlank((String)builder.baseUrl, (String)"baseUrl"));
        HashMap<String, String> defaultHeaders = new HashMap<String, String>();
        if (builder.apiKey != null) {
            defaultHeaders.put("Authorization", "Bearer " + builder.apiKey);
        }
        this.defaultHeaders = defaultHeaders;
        this.customHeadersSupplier = (Supplier)Utils.getOrDefault((Object)builder.customHeadersSupplier, () -> Collections::emptyMap);
    }

    private Map<String, String> buildRequestHeaders() {
        Map<String, String> dynamicHeaders = this.customHeadersSupplier.get();
        if (Utils.isNullOrEmpty(dynamicHeaders)) {
            return this.defaultHeaders;
        }
        HashMap<String, String> headers = new HashMap<String, String>(this.defaultHeaders);
        headers.putAll(dynamicHeaders);
        return headers;
    }

    EmbeddingResponse embed(EmbeddingRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl + "embeddings").body(VoyageAiJsonUtils.toJson(request)).addHeader("Content-Type", new String[]{"application/json"}).addHeader("Accept", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).build();
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return VoyageAiJsonUtils.fromJson(successfulHttpResponse.body(), EmbeddingResponse.class);
    }

    EmbeddingResponse multimodalEmbed(MultimodalEmbeddingRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl + "multimodalembeddings").body(VoyageAiJsonUtils.toJson(request)).addHeader("Content-Type", new String[]{"application/json"}).addHeader("Accept", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).build();
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return VoyageAiJsonUtils.fromJson(successfulHttpResponse.body(), EmbeddingResponse.class);
    }

    RerankResponse rerank(RerankRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl + "rerank").body(VoyageAiJsonUtils.toJson(request)).addHeader("Content-Type", new String[]{"application/json"}).addHeader("Accept", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).build();
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return VoyageAiJsonUtils.fromJson(successfulHttpResponse.body(), RerankResponse.class);
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private Duration timeout;
        private String apiKey;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Supplier<Map<String, String>> customHeadersSupplier;

        Builder() {
        }

        Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        Builder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        Builder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        Builder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        VoyageAiClient build() {
            return new VoyageAiClient(this);
        }
    }
}

