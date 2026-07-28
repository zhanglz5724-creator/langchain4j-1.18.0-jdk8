/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.openai.OpenAIClient
 *  com.azure.ai.openai.models.AudioTranscription
 *  com.azure.ai.openai.models.AudioTranscriptionFormat
 *  com.azure.ai.openai.models.AudioTranscriptionOptions
 *  com.azure.core.credential.KeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.core.http.HttpClientProvider
 *  com.azure.core.http.ProxyOptions
 *  com.azure.core.http.policy.RetryOptions
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.audio.Audio
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.audio.AudioTranscriptionModel
 *  dev.langchain4j.model.audio.AudioTranscriptionRequest
 *  dev.langchain4j.model.audio.AudioTranscriptionResponse
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.azure;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.AudioTranscription;
import com.azure.ai.openai.models.AudioTranscriptionFormat;
import com.azure.ai.openai.models.AudioTranscriptionOptions;
import com.azure.core.credential.KeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.core.http.HttpClientProvider;
import com.azure.core.http.ProxyOptions;
import com.azure.core.http.policy.RetryOptions;
import dev.langchain4j.Experimental;
import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.audio.AudioTranscriptionModel;
import dev.langchain4j.model.audio.AudioTranscriptionRequest;
import dev.langchain4j.model.audio.AudioTranscriptionResponse;
import dev.langchain4j.model.azure.InternalAzureOpenAiHelper;
import dev.langchain4j.model.azure.spi.AzureOpenAiAudioTranscriptionModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;

@Experimental
public class AzureOpenAiAudioTranscriptionModel
implements AudioTranscriptionModel {
    private final OpenAIClient client;
    private final String deploymentName;
    private final AudioTranscriptionFormat responseFormat;

    public AzureOpenAiAudioTranscriptionModel(Builder builder) {
        this.client = builder.openAIClient != null ? builder.openAIClient : this.createClient(builder);
        if (builder.deploymentName == null || builder.deploymentName.trim().isEmpty()) {
            throw new IllegalArgumentException("deploymentName is required");
        }
        this.deploymentName = builder.deploymentName;
        this.responseFormat = builder.responseFormat != null ? builder.responseFormat : AudioTranscriptionFormat.JSON;
    }

    public AzureOpenAiAudioTranscriptionModel(OpenAIClient client, String deploymentName, AudioTranscriptionFormat responseFormat) {
        if (client == null) {
            throw new IllegalArgumentException("client is required");
        }
        if (deploymentName == null || deploymentName.trim().isEmpty()) {
            throw new IllegalArgumentException("deploymentName is required");
        }
        this.client = client;
        this.deploymentName = deploymentName;
        this.responseFormat = responseFormat != null ? responseFormat : AudioTranscriptionFormat.JSON;
    }

    public AudioTranscriptionResponse transcribe(AudioTranscriptionRequest request) {
        if (request == null || request.audio() == null) {
            throw new IllegalArgumentException("Request and audio data are required");
        }
        Audio audio = request.audio();
        byte[] audioData = this.getBinaryDataFromAudio(audio);
        String filename = "audio.mp3";
        AudioTranscriptionOptions options = new AudioTranscriptionOptions(audioData).setPrompt(request.prompt()).setModel(this.deploymentName).setFilename(filename).setResponseFormat(this.responseFormat);
        if (request.language() != null) {
            options.setLanguage(request.language());
        }
        if (request.temperature() != null) {
            options.setTemperature(request.temperature());
        }
        AudioTranscription audioTranscription = this.client.getAudioTranscription(this.deploymentName, options.getFilename(), options);
        return AudioTranscriptionResponse.from((String)audioTranscription.getText());
    }

    private byte[] getBinaryDataFromAudio(Audio audio) {
        if (audio.binaryData() != null) {
            return audio.binaryData();
        }
        if (audio.base64Data() != null) {
            try {
                return Base64.getDecoder().decode(audio.base64Data());
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid base64 audio data provided", e);
            }
        }
        if (audio.url() != null) {
            throw new IllegalArgumentException("URL-based audio is not supported by Azure OpenAI transcription. Please provide audio as binary data or base64 encoded data.");
        }
        throw new IllegalArgumentException("No audio data found. Audio must contain either binary data, base64 data");
    }

    private OpenAIClient createClient(Builder builder) {
        if (builder.tokenCredential != null) {
            return InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.tokenCredential, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders);
        }
        if (builder.keyCredential != null) {
            return InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.keyCredential, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders);
        }
        if (builder.apiKey != null && !builder.apiKey.trim().isEmpty()) {
            return InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.apiKey, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders);
        }
        throw new IllegalArgumentException("Authentication is required: provide either apiKey, tokenCredential, keyCredential, or openAIClient");
    }

    public ModelProvider provider() {
        return ModelProvider.AZURE_OPEN_AI;
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(AzureOpenAiAudioTranscriptionModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            AzureOpenAiAudioTranscriptionModelBuilderFactory factory = (AzureOpenAiAudioTranscriptionModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public static class Builder {
        private String endpoint;
        private String serviceVersion;
        private String apiKey;
        private KeyCredential keyCredential;
        private TokenCredential tokenCredential;
        private HttpClientProvider httpClientProvider;
        private String deploymentName;
        private AudioTranscriptionFormat responseFormat = AudioTranscriptionFormat.JSON;
        private Duration timeout;
        private Integer maxRetries;
        private RetryOptions retryOptions;
        private ProxyOptions proxyOptions;
        private boolean logRequestsAndResponses;
        private OpenAIClient openAIClient;
        private String userAgentSuffix;
        private Map<String, String> customHeaders;

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder httpClientProvider(HttpClientProvider httpClientProvider) {
            this.httpClientProvider = httpClientProvider;
            return this;
        }

        public Builder serviceVersion(String serviceVersion) {
            this.serviceVersion = serviceVersion;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder nonAzureApiKey(String nonAzureApiKey) {
            this.keyCredential = new KeyCredential(nonAzureApiKey);
            this.endpoint = "https://api.openai.com/v1";
            return this;
        }

        public Builder tokenCredential(TokenCredential tokenCredential) {
            this.tokenCredential = tokenCredential;
            return this;
        }

        public Builder deploymentName(String deploymentName) {
            this.deploymentName = deploymentName;
            return this;
        }

        public Builder responseFormat(AudioTranscriptionFormat format) {
            this.responseFormat = format;
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

        public Builder retryOptions(RetryOptions retryOptions) {
            this.retryOptions = retryOptions;
            return this;
        }

        public Builder proxyOptions(ProxyOptions proxyOptions) {
            this.proxyOptions = proxyOptions;
            return this;
        }

        public Builder logRequestsAndResponses(Boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
            return this;
        }

        public Builder openAIClient(OpenAIClient openAIClient) {
            this.openAIClient = openAIClient;
            return this;
        }

        public Builder userAgentSuffix(String userAgentSuffix) {
            this.userAgentSuffix = userAgentSuffix;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public AzureOpenAiAudioTranscriptionModel build() {
            return new AzureOpenAiAudioTranscriptionModel(this);
        }
    }
}

