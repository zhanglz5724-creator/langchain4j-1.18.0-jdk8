/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.http.client.HttpClientBuilderLoader
 *  dev.langchain4j.http.client.HttpMethod
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.HttpRequest$Builder
 *  dev.langchain4j.http.client.log.LoggingHttpClient
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.model.openai.internal;

import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.log.LoggingHttpClient;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.openai.internal.Json;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.RequestExecutor;
import dev.langchain4j.model.openai.internal.SyncOrAsync;
import dev.langchain4j.model.openai.internal.SyncOrAsyncOrStreaming;
import dev.langchain4j.model.openai.internal.audio.texttospeech.OpenAiTextToSpeechRequest;
import dev.langchain4j.model.openai.internal.audio.texttospeech.OpenAiTextToSpeechResponse;
import dev.langchain4j.model.openai.internal.audio.transcription.AudioFile;
import dev.langchain4j.model.openai.internal.audio.transcription.OpenAiAudioTranscriptionRequest;
import dev.langchain4j.model.openai.internal.audio.transcription.OpenAiAudioTranscriptionResponse;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse;
import dev.langchain4j.model.openai.internal.completion.CompletionRequest;
import dev.langchain4j.model.openai.internal.completion.CompletionResponse;
import dev.langchain4j.model.openai.internal.embedding.EmbeddingRequest;
import dev.langchain4j.model.openai.internal.embedding.EmbeddingResponse;
import dev.langchain4j.model.openai.internal.image.EditImageRequest;
import dev.langchain4j.model.openai.internal.image.GenerateImagesRequest;
import dev.langchain4j.model.openai.internal.image.GenerateImagesResponse;
import dev.langchain4j.model.openai.internal.image.ImageFile;
import dev.langchain4j.model.openai.internal.models.ModelsListResponse;
import dev.langchain4j.model.openai.internal.moderation.ModerationRequest;
import dev.langchain4j.model.openai.internal.moderation.ModerationResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultOpenAiClient
extends OpenAiClient {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final Map<String, String> defaultHeaders;
    private final Supplier<Map<String, String>> customHeadersSupplier;
    private final Map<String, String> customQueryParams;

    public DefaultOpenAiClient(Builder builder) {
        HttpClientBuilder httpClientBuilder = (HttpClientBuilder)Utils.getOrDefault((Object)builder.httpClientBuilder, HttpClientBuilderLoader::loadHttpClientBuilder);
        HttpClient httpClient = httpClientBuilder.connectTimeout((Duration)Utils.getOrDefault((Object)Utils.getOrDefault((Object)builder.connectTimeout, (Object)httpClientBuilder.connectTimeout()), (Object)Duration.ofSeconds(15L))).readTimeout((Duration)Utils.getOrDefault((Object)Utils.getOrDefault((Object)builder.readTimeout, (Object)httpClientBuilder.readTimeout()), (Object)Duration.ofSeconds(60L))).build();
        this.httpClient = builder.logRequests || builder.logResponses ? new LoggingHttpClient(httpClient, Boolean.valueOf(builder.logRequests), Boolean.valueOf(builder.logResponses), builder.logger) : httpClient;
        this.baseUrl = ValidationUtils.ensureNotBlank((String)builder.baseUrl, (String)"baseUrl");
        HashMap<String, String> defaultHeaders = new HashMap<String, String>();
        if (builder.apiKey != null) {
            defaultHeaders.put("Authorization", "Bearer " + builder.apiKey);
        }
        if (builder.organizationId != null) {
            defaultHeaders.put("OpenAI-Organization", builder.organizationId);
        }
        if (builder.projectId != null) {
            defaultHeaders.put("OpenAI-Project", builder.projectId);
        }
        if (builder.userAgent != null) {
            defaultHeaders.put("User-Agent", builder.userAgent);
        }
        this.defaultHeaders = defaultHeaders;
        this.customHeadersSupplier = builder.customHeadersSupplier != null ? builder.customHeadersSupplier : Collections::emptyMap;
        this.customQueryParams = builder.customQueryParams;
    }

    public static Builder builder() {
        return new Builder();
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

    @Override
    public SyncOrAsyncOrStreaming<CompletionResponse> completion(CompletionRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "completions").addQueryParams(this.customQueryParams).addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(Json.toJson(CompletionRequest.builder().from(request).stream(false).build())).build();
        HttpRequest streamingHttpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "completions").addQueryParams(this.customQueryParams).addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(Json.toJson(CompletionRequest.builder().from(request).stream(true).build())).build();
        return new RequestExecutor<CompletionResponse>(this.httpClient, httpRequest, streamingHttpRequest, CompletionResponse.class);
    }

    @Override
    public SyncOrAsyncOrStreaming<ChatCompletionResponse> chatCompletion(ChatCompletionRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "chat/completions").addQueryParams(this.customQueryParams).addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(Json.toJson(ChatCompletionRequest.builder().from(request).stream(false).build())).build();
        HttpRequest streamingHttpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "chat/completions").addQueryParams(this.customQueryParams).addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(Json.toJson(ChatCompletionRequest.builder().from(request).stream(true).build())).build();
        return new RequestExecutor<ChatCompletionResponse>(this.httpClient, httpRequest, streamingHttpRequest, ChatCompletionResponse.class);
    }

    @Override
    public SyncOrAsync<EmbeddingResponse> embedding(EmbeddingRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "embeddings").addQueryParams(this.customQueryParams).addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(Json.toJson(request)).build();
        return new RequestExecutor<EmbeddingResponse>(this.httpClient, httpRequest, EmbeddingResponse.class);
    }

    @Override
    public SyncOrAsync<ModerationResponse> moderation(ModerationRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "moderations").addQueryParams(this.customQueryParams).addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(Json.toJson(request)).build();
        return new RequestExecutor<ModerationResponse>(this.httpClient, httpRequest, ModerationResponse.class);
    }

    @Override
    public SyncOrAsync<GenerateImagesResponse> imagesGeneration(GenerateImagesRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "images/generations").addQueryParams(this.customQueryParams).addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(Json.toJson(request)).build();
        return new RequestExecutor<GenerateImagesResponse>(this.httpClient, httpRequest, GenerateImagesResponse.class);
    }

    @Override
    public SyncOrAsync<GenerateImagesResponse> imagesEdit(EditImageRequest request) {
        HttpRequest.Builder httpRequestBuilder = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "images/edits").addQueryParams(this.customQueryParams).addHeader("Content-Type", new String[]{"multipart/form-data; boundary=----LangChain4j"}).addHeaders(this.buildRequestHeaders());
        ImageFile image = request.image();
        httpRequestBuilder.addFormDataFile("image", image.fileName(), image.mimeType(), image.content());
        httpRequestBuilder.addFormDataField("prompt", request.prompt());
        if (request.mask() != null) {
            ImageFile mask = request.mask();
            httpRequestBuilder.addFormDataFile("mask", mask.fileName(), mask.mimeType(), mask.content());
        }
        if (request.model() != null) {
            httpRequestBuilder.addFormDataField("model", request.model());
        }
        httpRequestBuilder.addFormDataField("n", Integer.toString(request.n()));
        if (request.size() != null) {
            httpRequestBuilder.addFormDataField("size", request.size());
        }
        if (request.quality() != null) {
            httpRequestBuilder.addFormDataField("quality", request.quality());
        }
        if (request.user() != null) {
            httpRequestBuilder.addFormDataField("user", request.user());
        }
        if (request.background() != null) {
            httpRequestBuilder.addFormDataField("background", request.background());
        }
        if (request.outputFormat() != null) {
            httpRequestBuilder.addFormDataField("output_format", request.outputFormat());
        }
        if (request.outputCompression() != null) {
            httpRequestBuilder.addFormDataField("output_compression", Integer.toString(request.outputCompression()));
        }
        return new RequestExecutor<GenerateImagesResponse>(this.httpClient, httpRequestBuilder.build(), GenerateImagesResponse.class);
    }

    @Override
    public SyncOrAsync<OpenAiAudioTranscriptionResponse> audioTranscription(OpenAiAudioTranscriptionRequest request) {
        HttpRequest.Builder httpRequestBuilder = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "audio/transcriptions").addHeader("Content-Type", new String[]{"multipart/form-data; boundary=----LangChain4j"}).addHeaders(this.buildRequestHeaders());
        httpRequestBuilder.addFormDataField("model", request.model());
        AudioFile file = request.file();
        httpRequestBuilder.addFormDataFile("file", file.fileName(), file.mimeType(), file.content());
        if (request.language() != null) {
            httpRequestBuilder.addFormDataField("language", request.language());
        }
        if (request.prompt() != null) {
            httpRequestBuilder.addFormDataField("prompt", request.prompt());
        }
        if (request.temperature() != null) {
            httpRequestBuilder.addFormDataField("temperature", Double.toString(request.temperature()));
        }
        return new RequestExecutor<OpenAiAudioTranscriptionResponse>(this.httpClient, httpRequestBuilder.build(), OpenAiAudioTranscriptionResponse.class);
    }

    @Override
    public SyncOrAsync<OpenAiTextToSpeechResponse> textToSpeech(OpenAiTextToSpeechRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "audio/speech").addQueryParams(this.customQueryParams).addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(Json.toJson(request)).build();
        return new RequestExecutor<OpenAiTextToSpeechResponse>(this.httpClient, httpRequest, response -> OpenAiTextToSpeechResponse.from(response.bodyBytes(), response.contentType()));
    }

    @Override
    public SyncOrAsync<ModelsListResponse> listModels() {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, "models").addQueryParams(this.customQueryParams).addHeaders(this.buildRequestHeaders()).build();
        return new RequestExecutor<ModelsListResponse>(this.httpClient, httpRequest, ModelsListResponse.class);
    }

    public static class Builder
    extends OpenAiClient.Builder<DefaultOpenAiClient, Builder> {
        @Override
        public DefaultOpenAiClient build() {
            return new DefaultOpenAiClient(this);
        }
    }
}

