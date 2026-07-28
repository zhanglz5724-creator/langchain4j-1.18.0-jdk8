/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.audio.AudioTranscriptionModel
 *  dev.langchain4j.model.audio.AudioTranscriptionRequest
 *  dev.langchain4j.model.audio.AudioTranscriptionResponse
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.Experimental;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.audio.AudioTranscriptionModel;
import dev.langchain4j.model.audio.AudioTranscriptionRequest;
import dev.langchain4j.model.audio.AudioTranscriptionResponse;
import dev.langchain4j.model.openai.OpenAiAudioTranscriptionModelName;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.ParsedAndRawResponse;
import dev.langchain4j.model.openai.internal.audio.transcription.AudioFile;
import dev.langchain4j.model.openai.internal.audio.transcription.OpenAiAudioTranscriptionRequest;
import dev.langchain4j.model.openai.internal.audio.transcription.OpenAiAudioTranscriptionResponse;
import dev.langchain4j.model.openai.spi.OpenAiAudioTranscriptionModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import org.slf4j.Logger;

@Experimental
public class OpenAiAudioTranscriptionModel
implements AudioTranscriptionModel {
    private final OpenAiClient client;
    private final int maxRetries;
    private final String modelName;

    public OpenAiAudioTranscriptionModel(Builder builder) {
        this.client = ((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)OpenAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.openai.com/v1"))).apiKey(builder.apiKey)).organizationId(builder.organizationId)).projectId(builder.projectId)).connectTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(15L)))).readTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L)))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).userAgent("langchain4j-openai")).build();
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.modelName = builder.modelName;
    }

    public AudioTranscriptionResponse transcribe(AudioTranscriptionRequest audioRequest) {
        if (audioRequest == null || audioRequest.audio() == null) {
            throw new IllegalArgumentException("Request and audio are required");
        }
        OpenAiAudioTranscriptionRequest openAiRequest = this.requestBuilder(audioRequest).build();
        ParsedAndRawResponse parsedAndRawResponse = (ParsedAndRawResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.audioTranscription(openAiRequest).executeRaw(), (int)this.maxRetries);
        OpenAiAudioTranscriptionResponse openAiResponse = (OpenAiAudioTranscriptionResponse)parsedAndRawResponse.parsedResponse();
        return AudioTranscriptionResponse.from((String)openAiResponse.text());
    }

    private OpenAiAudioTranscriptionRequest.Builder requestBuilder(AudioTranscriptionRequest request) {
        return OpenAiAudioTranscriptionRequest.builder().model(this.modelName).file(AudioFile.from(request.audio())).language(request.language()).prompt(request.prompt()).temperature(request.temperature());
    }

    public ModelProvider provider() {
        return ModelProvider.OPEN_AI;
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OpenAiAudioTranscriptionModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OpenAiAudioTranscriptionModelBuilderFactory factory = (OpenAiAudioTranscriptionModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String organizationId;
        private String projectId;
        private String modelName;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;

        public Builder httpClientProvider(HttpClientBuilder httpClientProvider) {
            this.httpClientBuilder = httpClientProvider;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder modelName(OpenAiAudioTranscriptionModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public OpenAiAudioTranscriptionModel build() {
            return new OpenAiAudioTranscriptionModel(this);
        }
    }
}

