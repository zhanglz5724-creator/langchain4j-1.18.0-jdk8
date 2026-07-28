/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.http.client.HttpClientBuilderLoader
 *  dev.langchain4j.http.client.HttpMethod
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.http.client.log.LoggingHttpClient
 *  dev.langchain4j.http.client.sse.ServerSentEventListener
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.StreamingResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 */
package dev.langchain4j.model.mistralai.internal.client;

import dev.langchain4j.Internal;
import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.log.LoggingHttpClient;
import dev.langchain4j.http.client.sse.ServerSentEventListener;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.mistralai.internal.api.MistralAiBatchJob;
import dev.langchain4j.model.mistralai.internal.api.MistralAiBatchJobRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiBatchJobsResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiBatchResultEntry;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiEmbeddingRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiEmbeddingResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiFimCompletionRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModelResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModerationRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModerationResponse;
import dev.langchain4j.model.mistralai.internal.client.MistralAiClient;
import dev.langchain4j.model.mistralai.internal.client.MistralAiFimServerSentEventListener;
import dev.langchain4j.model.mistralai.internal.client.MistralAiJsonUtils;
import dev.langchain4j.model.mistralai.internal.client.MistralAiServerSentEventListener;
import dev.langchain4j.model.mistralai.internal.client.ParsedAndRawResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Internal
public class DefaultMistralAiClient
extends MistralAiClient {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String apiKey;
    private final Supplier<Map<String, String>> customHeadersSupplier;

    public static Builder builder() {
        return new Builder();
    }

    DefaultMistralAiClient(Builder builder) {
        HttpClientBuilder httpClientBuilder = (HttpClientBuilder)Utils.getOrDefault((Object)builder.httpClientBuilder, HttpClientBuilderLoader::loadHttpClientBuilder);
        HttpClient httpClient = httpClientBuilder.connectTimeout((Duration)Utils.getOrDefault((Object)Utils.getOrDefault((Object)builder.timeout, (Object)httpClientBuilder.connectTimeout()), (Object)Duration.ofSeconds(15L))).readTimeout((Duration)Utils.getOrDefault((Object)Utils.getOrDefault((Object)builder.timeout, (Object)httpClientBuilder.readTimeout()), (Object)Duration.ofSeconds(60L))).build();
        this.httpClient = builder.logRequests != null && builder.logRequests != false || builder.logResponses != null && builder.logResponses != false ? new LoggingHttpClient(httpClient, builder.logRequests, builder.logResponses, builder.logger) : httpClient;
        this.baseUrl = ValidationUtils.ensureNotBlank((String)builder.baseUrl, (String)"baseUrl");
        this.apiKey = ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey");
        this.customHeadersSupplier = (Supplier)Utils.getOrDefault((Object)builder.customHeadersSupplier, (Object)new Supplier<Map<String, String>>(){

            @Override
            public Map<String, String> get() {
                return Collections.emptyMap();
            }
        });
    }

    private Map<String, String> buildRequestHeaders() {
        Map<String, String> dynamicHeaders = this.customHeadersSupplier.get();
        if (Utils.isNullOrEmpty(dynamicHeaders)) {
            return Collections.emptyMap();
        }
        return dynamicHeaders;
    }

    @Override
    public MistralAiChatCompletionResponse chatCompletion(MistralAiChatCompletionRequest request) {
        return this.chatCompletionWithRawResponse(request).parsedResponse();
    }

    @Override
    public ParsedAndRawResponse<MistralAiChatCompletionResponse> chatCompletionWithRawResponse(MistralAiChatCompletionRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "chat/completions").addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"langchain4j-mistral-ai"}).addHeaders(this.buildRequestHeaders()).body(MistralAiJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse rawResponse = this.httpClient.execute(httpRequest);
        MistralAiChatCompletionResponse parsedResponse = MistralAiJsonUtils.fromJson(rawResponse.body(), MistralAiChatCompletionResponse.class);
        return new ParsedAndRawResponse<MistralAiChatCompletionResponse>(parsedResponse, rawResponse);
    }

    @Override
    public void streamingChatCompletion(MistralAiChatCompletionRequest request, StreamingChatResponseHandler handler) {
        this.streamingChatCompletion(request, handler, false);
    }

    @Override
    public void streamingChatCompletion(MistralAiChatCompletionRequest request, StreamingChatResponseHandler handler, boolean returnThinking) {
        ValidationUtils.ensureNotEmpty(request.getMessages(), (String)"messages");
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "chat/completions").addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"langchain4j-mistral-ai"}).addHeaders(this.buildRequestHeaders()).body(MistralAiJsonUtils.toJson(request)).build();
        this.httpClient.execute(httpRequest, (ServerSentEventListener)new MistralAiServerSentEventListener(handler, returnThinking));
    }

    @Override
    public MistralAiChatCompletionResponse fimCompletion(MistralAiFimCompletionRequest request) {
        return this.fimCompletionWithRawResponse(request).parsedResponse();
    }

    @Override
    public ParsedAndRawResponse<MistralAiChatCompletionResponse> fimCompletionWithRawResponse(MistralAiFimCompletionRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "fim/completions").addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"langchain4j-mistral-ai"}).addHeaders(this.buildRequestHeaders()).body(MistralAiJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse rawResponse = this.httpClient.execute(httpRequest);
        MistralAiChatCompletionResponse parsedResponse = MistralAiJsonUtils.fromJson(rawResponse.body(), MistralAiChatCompletionResponse.class);
        return new ParsedAndRawResponse<MistralAiChatCompletionResponse>(parsedResponse, rawResponse);
    }

    @Override
    public void streamingFimCompletion(MistralAiFimCompletionRequest request, StreamingResponseHandler<String> handler) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "fim/completions").addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"langchain4j-mistral-ai"}).addHeaders(this.buildRequestHeaders()).body(MistralAiJsonUtils.toJson(request)).build();
        MistralAiFimServerSentEventListener listener = new MistralAiFimServerSentEventListener(handler, (content, toolExecutionRequests) -> content);
        this.httpClient.execute(httpRequest, (ServerSentEventListener)listener);
    }

    @Override
    public MistralAiEmbeddingResponse embedding(MistralAiEmbeddingRequest request) {
        return this.embeddingWithRawResponse(request).parsedResponse();
    }

    @Override
    public ParsedAndRawResponse<MistralAiEmbeddingResponse> embeddingWithRawResponse(MistralAiEmbeddingRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "embeddings").addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"langchain4j-mistral-ai"}).addHeaders(this.buildRequestHeaders()).body(MistralAiJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse rawResponse = this.httpClient.execute(httpRequest);
        MistralAiEmbeddingResponse parsedResponse = MistralAiJsonUtils.fromJson(rawResponse.body(), MistralAiEmbeddingResponse.class);
        return new ParsedAndRawResponse<MistralAiEmbeddingResponse>(parsedResponse, rawResponse);
    }

    @Override
    public MistralAiModerationResponse moderation(MistralAiModerationRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "moderations").addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"langchain4j-mistral-ai"}).addHeaders(this.buildRequestHeaders()).body(MistralAiJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return MistralAiJsonUtils.fromJson(successfulHttpResponse.body(), MistralAiModerationResponse.class);
    }

    @Override
    public MistralAiModelResponse listModels() {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, "models").addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"langchain4j-mistral-ai"}).addHeaders(this.buildRequestHeaders()).build();
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return MistralAiJsonUtils.fromJson(successfulHttpResponse.body(), MistralAiModelResponse.class);
    }

    @Override
    public MistralAiBatchJob createBatchJob(MistralAiBatchJobRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "batch/jobs").addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"langchain4j-mistral-ai"}).addHeaders(this.buildRequestHeaders()).body(MistralAiJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse rawResponse = this.httpClient.execute(httpRequest);
        return MistralAiJsonUtils.fromJson(rawResponse.body(), MistralAiBatchJob.class);
    }

    @Override
    public MistralAiBatchJob retrieveBatchJob(String jobId) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, "batch/jobs/" + jobId).addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"langchain4j-mistral-ai"}).addHeaders(this.buildRequestHeaders()).build();
        SuccessfulHttpResponse rawResponse = this.httpClient.execute(httpRequest);
        return MistralAiJsonUtils.fromJson(rawResponse.body(), MistralAiBatchJob.class);
    }

    @Override
    public MistralAiBatchJob cancelBatchJob(String jobId) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "batch/jobs/" + jobId + "/cancel").addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"langchain4j-mistral-ai"}).addHeaders(this.buildRequestHeaders()).build();
        SuccessfulHttpResponse rawResponse = this.httpClient.execute(httpRequest);
        return MistralAiJsonUtils.fromJson(rawResponse.body(), MistralAiBatchJob.class);
    }

    @Override
    public MistralAiBatchJobsResponse listBatchJobs(Integer page, Integer pageSize) {
        StringBuilder path = new StringBuilder("batch/jobs");
        ArrayList<String> queryParams = new ArrayList<String>();
        if (page != null) {
            queryParams.add("page=" + page);
        }
        if (pageSize != null) {
            queryParams.add("page_size=" + pageSize);
        }
        if (!queryParams.isEmpty()) {
            path.append('?').append(String.join((CharSequence)"&", queryParams));
        }
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, path.toString()).addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"langchain4j-mistral-ai"}).addHeaders(this.buildRequestHeaders()).build();
        SuccessfulHttpResponse rawResponse = this.httpClient.execute(httpRequest);
        return MistralAiJsonUtils.fromJson(rawResponse.body(), MistralAiBatchJobsResponse.class);
    }

    @Override
    public List<MistralAiBatchResultEntry> downloadBatchResults(String fileId) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, "files/" + fileId + "/content").addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).addHeader("User-Agent", new String[]{"langchain4j-mistral-ai"}).addHeaders(this.buildRequestHeaders()).build();
        SuccessfulHttpResponse rawResponse = this.httpClient.execute(httpRequest);
        String body = rawResponse.body();
        if (body == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(body.split("\\R")).stream().map(s -> s.trim()).filter(line -> !line.isEmpty()).map(line -> MistralAiJsonUtils.fromJson(line, MistralAiBatchResultEntry.class)).collect(Collectors.toList());
    }

    public static class Builder
    extends MistralAiClient.Builder<DefaultMistralAiClient, Builder> {
        @Override
        public DefaultMistralAiClient build() {
            return new DefaultMistralAiClient(this);
        }
    }
}

