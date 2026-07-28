/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.audio.Audio
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.audio.TextToSpeechModel
 *  dev.langchain4j.model.audio.TextToSpeechRequest
 *  dev.langchain4j.model.audio.TextToSpeechResponse
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.audio.TextToSpeechModel;
import dev.langchain4j.model.audio.TextToSpeechRequest;
import dev.langchain4j.model.audio.TextToSpeechResponse;
import dev.langchain4j.model.openai.OpenAiTextToSpeechModelName;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.audio.texttospeech.OpenAiTextToSpeechRequest;
import dev.langchain4j.model.openai.internal.audio.texttospeech.OpenAiTextToSpeechResponse;
import dev.langchain4j.model.openai.spi.OpenAiTextToSpeechModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import org.slf4j.Logger;

@Experimental
public class OpenAiTextToSpeechModel
implements TextToSpeechModel {
    private static final int MAX_INPUT_TEXT_LENGTH = 4096;
    private final OpenAiClient client;
    private final int maxRetries;
    private final String modelName;
    private final String voice;

    public OpenAiTextToSpeechModel(Builder builder) {
        this.client = ((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)OpenAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.openai.com/v1"))).apiKey(builder.apiKey)).organizationId(builder.organizationId)).projectId(builder.projectId)).connectTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(15L)))).readTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L)))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).userAgent("langchain4j-openai")).build();
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.voice = (String)Utils.getOrDefault((Object)builder.voice, (Object)"alloy");
    }

    public TextToSpeechResponse synthesize(TextToSpeechRequest audioRequest) {
        if (audioRequest == null) {
            throw new IllegalArgumentException("Request is required");
        }
        if (audioRequest.text().length() > 4096) {
            throw new IllegalArgumentException("Input text exceeds the maximum length of 4096 characters");
        }
        OpenAiTextToSpeechRequest openAiRequest = this.requestBuilder(audioRequest).build();
        OpenAiTextToSpeechResponse openAiResponse = (OpenAiTextToSpeechResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.textToSpeech(openAiRequest).execute(), (int)this.maxRetries);
        Audio audio = Audio.builder().binaryData(openAiResponse.audio()).mimeType((String)Utils.getOrDefault((Object)openAiResponse.contentType(), (Object)"audio/mpeg")).build();
        return TextToSpeechResponse.from((Audio)audio);
    }

    private OpenAiTextToSpeechRequest.Builder requestBuilder(TextToSpeechRequest request) {
        return OpenAiTextToSpeechRequest.builder().model(this.modelName).text(request.text()).voice((String)Utils.getOrDefault((Object)request.voice(), (Object)this.voice));
    }

    public ModelProvider provider() {
        return ModelProvider.OPEN_AI;
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OpenAiTextToSpeechModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OpenAiTextToSpeechModelBuilderFactory factory = (OpenAiTextToSpeechModelBuilderFactory)iterator.next();
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
        private String voice;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
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

        public Builder modelName(OpenAiTextToSpeechModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder voice(String voice) {
            this.voice = voice;
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

        public OpenAiTextToSpeechModel build() {
            return new OpenAiTextToSpeechModel(this);
        }
    }
}

