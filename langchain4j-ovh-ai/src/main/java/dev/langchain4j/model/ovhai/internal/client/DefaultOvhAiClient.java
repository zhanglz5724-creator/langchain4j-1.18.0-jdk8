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
 */
package dev.langchain4j.model.ovhai.internal.client;

import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.log.LoggingHttpClient;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ovhai.internal.api.EmbeddingRequest;
import dev.langchain4j.model.ovhai.internal.api.EmbeddingResponse;
import dev.langchain4j.model.ovhai.internal.client.OvhAiClient;
import dev.langchain4j.model.ovhai.internal.client.OvhAiJsonUtils;
import java.util.Arrays;

@Deprecated
public class DefaultOvhAiClient
extends OvhAiClient {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String authorizationHeader;

    public static Builder builder() {
        return new Builder();
    }

    DefaultOvhAiClient(Builder builder) {
        ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"%s", (Object[])new Object[]{"OVHcloud API key must be defined. It can be generated here: https://endpoints.ai.cloud.ovh.net/"});
        HttpClientBuilder httpClientBuilder = HttpClientBuilderLoader.loadHttpClientBuilder();
        HttpClient httpClient = httpClientBuilder.connectTimeout(builder.timeout).readTimeout(builder.timeout).build();
        this.httpClient = builder.logRequests != null && builder.logRequests != false || builder.logResponses != null && builder.logResponses != false ? new LoggingHttpClient(httpClient, builder.logRequests, builder.logResponses, builder.logger) : httpClient;
        this.baseUrl = Utils.ensureTrailingForwardSlash((String)ValidationUtils.ensureNotBlank((String)builder.baseUrl, (String)"baseUrl"));
        this.authorizationHeader = "Bearer " + ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey");
    }

    public EmbeddingResponse embed(EmbeddingRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl + "api/batch_text2vec").addHeader("Content-Type", new String[]{"application/json"}).addHeader("Authorization", new String[]{this.authorizationHeader}).body(OvhAiJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse response = this.httpClient.execute(httpRequest);
        float[][] embeddings = OvhAiJsonUtils.fromJson(response.body(), float[][].class);
        return new EmbeddingResponse(Arrays.asList(embeddings));
    }

    public static class Builder
    extends OvhAiClient.Builder<DefaultOvhAiClient, Builder> {
        @Override
        public DefaultOvhAiClient build() {
            return new DefaultOvhAiClient(this);
        }
    }
}

