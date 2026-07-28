/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.openai.internal;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.model.openai.internal.DefaultOpenAiClient;
import dev.langchain4j.model.openai.internal.SyncOrAsync;
import dev.langchain4j.model.openai.internal.SyncOrAsyncOrStreaming;
import dev.langchain4j.model.openai.internal.audio.texttospeech.OpenAiTextToSpeechRequest;
import dev.langchain4j.model.openai.internal.audio.texttospeech.OpenAiTextToSpeechResponse;
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
import dev.langchain4j.model.openai.internal.models.ModelsListResponse;
import dev.langchain4j.model.openai.internal.moderation.ModerationRequest;
import dev.langchain4j.model.openai.internal.moderation.ModerationResponse;
import dev.langchain4j.model.openai.internal.spi.OpenAiClientBuilderFactory;
import dev.langchain4j.model.openai.internal.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;

public abstract class OpenAiClient {
    public abstract SyncOrAsyncOrStreaming<CompletionResponse> completion(CompletionRequest var1);

    public abstract SyncOrAsyncOrStreaming<ChatCompletionResponse> chatCompletion(ChatCompletionRequest var1);

    public abstract SyncOrAsync<EmbeddingResponse> embedding(EmbeddingRequest var1);

    public abstract SyncOrAsync<ModerationResponse> moderation(ModerationRequest var1);

    public abstract SyncOrAsync<GenerateImagesResponse> imagesGeneration(GenerateImagesRequest var1);

    public SyncOrAsync<GenerateImagesResponse> imagesEdit(EditImageRequest request) {
        throw new UnsupportedOperationException("Image editing is not supported by this client implementation");
    }

    public SyncOrAsync<OpenAiAudioTranscriptionResponse> audioTranscription(OpenAiAudioTranscriptionRequest request) {
        throw new UnsupportedOperationException("Audio transcription is not supported by this client implementation");
    }

    public SyncOrAsync<OpenAiTextToSpeechResponse> textToSpeech(OpenAiTextToSpeechRequest request) {
        throw new UnsupportedOperationException("Text-to-speech is not supported by this client implementation");
    }

    public SyncOrAsync<ModelsListResponse> listModels() {
        throw new UnsupportedOperationException("Model listing is not supported by this client implementation");
    }

    public static Builder builder() {
        Iterator<OpenAiClientBuilderFactory> iterator = ServiceHelper.loadFactories(OpenAiClientBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OpenAiClientBuilderFactory factory = iterator.next();
            return (Builder)factory.get();
        }
        return DefaultOpenAiClient.builder();
    }

    public static abstract class Builder<T extends OpenAiClient, B extends Builder<T, B>> {
        public HttpClientBuilder httpClientBuilder;
        public String baseUrl;
        public String organizationId;
        public String projectId;
        public String apiKey;
        public Duration connectTimeout;
        public Duration readTimeout;
        public String userAgent;
        public boolean logRequests;
        public boolean logResponses;
        public Logger logger;
        public Supplier<Map<String, String>> customHeadersSupplier;
        public Map<String, String> customQueryParams;

        public abstract T build();

        public B httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return (B)this;
        }

        public B baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return (B)this;
        }

        public B organizationId(String organizationId) {
            this.organizationId = organizationId;
            return (B)this;
        }

        public B projectId(String projectId) {
            this.projectId = projectId;
            return (B)this;
        }

        public B apiKey(String apiKey) {
            this.apiKey = apiKey;
            return (B)this;
        }

        public B connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return (B)this;
        }

        public B readTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
            return (B)this;
        }

        public B userAgent(String userAgent) {
            this.userAgent = userAgent;
            return (B)this;
        }

        public B logRequests(Boolean logRequests) {
            if (logRequests == null) {
                logRequests = false;
            }
            this.logRequests = logRequests;
            return (B)this;
        }

        public B logger(Logger logger) {
            this.logger = logger;
            return (B)this;
        }

        public B logResponses(Boolean logResponses) {
            if (logResponses == null) {
                logResponses = false;
            }
            this.logResponses = logResponses;
            return (B)this;
        }

        public B customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return (B)this;
        }

        public B customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return (B)this;
        }

        public B customQueryParams(Map<String, String> customQueryParams) {
            this.customQueryParams = customQueryParams;
            return (B)this;
        }
    }
}

