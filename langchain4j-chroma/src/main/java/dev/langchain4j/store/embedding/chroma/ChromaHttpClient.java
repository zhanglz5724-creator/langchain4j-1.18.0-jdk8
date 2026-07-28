/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.DeserializationFeature
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.HttpException
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.http.client.HttpClientBuilderLoader
 *  dev.langchain4j.http.client.HttpMethod
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.http.client.log.LoggingHttpClient
 *  dev.langchain4j.internal.Utils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.chroma;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.langchain4j.Internal;
import dev.langchain4j.exception.HttpException;
import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.log.LoggingHttpClient;
import dev.langchain4j.internal.Utils;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Internal
class ChromaHttpClient {
    private static final Logger log = LoggerFactory.getLogger(ChromaHttpClient.class);
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final Supplier<Map<String, String>> customHeadersSupplier;

    public ChromaHttpClient(String baseUrl, Duration timeout, boolean logRequests, boolean logResponses) {
        this(baseUrl, timeout, logRequests, logResponses, null, null);
    }

    public ChromaHttpClient(String baseUrl, Duration timeout, boolean logRequests, boolean logResponses, HttpClientBuilder httpClientBuilder, Supplier<Map<String, String>> customHeadersSupplier) {
        this.baseUrl = Utils.ensureTrailingForwardSlash((String)baseUrl);
        HttpClientBuilder clientBuilder = httpClientBuilder == null ? HttpClientBuilderLoader.loadHttpClientBuilder() : httpClientBuilder;
        this.httpClient = new LoggingHttpClient(clientBuilder.connectTimeout(timeout).readTimeout(timeout).build(), Boolean.valueOf(logRequests), Boolean.valueOf(logResponses));
        Supplier<Map<String, String>> defaultHeadersSupplier = new Supplier<Map<String, String>>(){

            @Override
            public Map<String, String> get() {
                return Collections.emptyMap();
            }
        };
        this.customHeadersSupplier = (Supplier)Utils.getOrDefault(customHeadersSupplier, (Object)defaultHeadersSupplier);
        this.objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).enable(SerializationFeature.INDENT_OUTPUT).setSerializationInclusion(JsonInclude.Include.NON_NULL).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> T get(String path, Class<T> responseType) throws IOException {
        return this.get(path, responseType, null);
    }

    public <T> T get(String path, Class<T> responseType, Map<String, String> pathParams) throws IOException {
        String url = this.buildUrl(path, pathParams);
        HttpRequest request = HttpRequest.builder().method(HttpMethod.GET).url(url).addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.customHeadersSupplier.get()).build();
        return this.executeRequest(request, responseType);
    }

    public <T> T post(String path, Object requestBody, Class<T> responseType) throws IOException {
        return this.post(path, requestBody, responseType, null);
    }

    public <T> T post(String path, Object requestBody, Class<T> responseType, Map<String, String> pathParams) throws IOException {
        String url = this.buildUrl(path, pathParams);
        String jsonBody = this.objectMapper.writeValueAsString(requestBody);
        HttpRequest request = HttpRequest.builder().method(HttpMethod.POST).url(url).addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.customHeadersSupplier.get()).body(jsonBody).build();
        return this.executeRequest(request, responseType);
    }

    public void delete(String path) throws IOException {
        this.delete(path, null);
    }

    public void delete(String path, Map<String, String> pathParams) throws IOException {
        String url = this.buildUrl(path, pathParams);
        HttpRequest request = HttpRequest.builder().method(HttpMethod.DELETE).url(url).addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.customHeadersSupplier.get()).build();
        this.executeRequest(request, Void.class);
    }

    private <T> T executeRequest(HttpRequest request, Class<T> responseType) throws IOException {
        try {
            SuccessfulHttpResponse response = this.httpClient.execute(request);
            if (responseType == Void.class || response.body().isEmpty()) {
                return null;
            }
            try {
                return (T)this.objectMapper.readValue(response.body(), responseType);
            }
            catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse response: " + response.body(), e);
            }
        }
        catch (HttpException e) {
            throw new RuntimeException("HTTP error: " + e.getMessage(), e);
        }
    }

    private String buildUrl(String path, Map<String, String> pathParams) {
        String url = this.baseUrl + path;
        if (pathParams != null) {
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                url = url.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return url;
    }
}

