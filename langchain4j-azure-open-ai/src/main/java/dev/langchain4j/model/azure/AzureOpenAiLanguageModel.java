/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.openai.OpenAIClient
 *  com.azure.ai.openai.models.Choice
 *  com.azure.ai.openai.models.Completions
 *  com.azure.ai.openai.models.CompletionsOptions
 *  com.azure.core.credential.KeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.core.http.HttpClientProvider
 *  com.azure.core.http.ProxyOptions
 *  com.azure.core.http.policy.RetryOptions
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.language.LanguageModel
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.azure;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.Choice;
import com.azure.ai.openai.models.Completions;
import com.azure.ai.openai.models.CompletionsOptions;
import com.azure.core.credential.KeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.core.http.HttpClientProvider;
import com.azure.core.http.ProxyOptions;
import com.azure.core.http.policy.RetryOptions;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.azure.AzureOpenAiExceptionMapper;
import dev.langchain4j.model.azure.InternalAzureOpenAiHelper;
import dev.langchain4j.model.azure.spi.AzureOpenAiLanguageModelBuilderFactory;
import dev.langchain4j.model.language.LanguageModel;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AzureOpenAiLanguageModel
implements LanguageModel {
    private final OpenAIClient client;
    private final String deploymentName;
    private final Integer maxTokens;
    private final Double temperature;
    private final Double topP;
    private final Map<String, Integer> logitBias;
    private final String user;
    private final Integer logprobs;
    private final Boolean echo;
    private final List<String> stop;
    private final Double presencePenalty;
    private final Double frequencyPenalty;
    private final Integer bestOf;

    public AzureOpenAiLanguageModel(Builder builder) {
        this.client = builder.openAIClient == null ? (builder.tokenCredential != null ? InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.tokenCredential, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders) : (builder.keyCredential != null ? InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.keyCredential, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders) : InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.apiKey, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders))) : (OpenAIClient)ValidationUtils.ensureNotNull((Object)builder.openAIClient, (String)"openAIClient");
        this.deploymentName = ValidationUtils.ensureNotBlank((String)builder.deploymentName, (String)"deploymentName");
        this.maxTokens = builder.maxTokens;
        this.temperature = builder.temperature;
        this.topP = builder.topP;
        this.logitBias = Utils.copyIfNotNull((Map)builder.logitBias);
        this.user = builder.user;
        this.logprobs = builder.logprobs;
        this.echo = builder.echo;
        this.stop = Utils.copyIfNotNull((List)builder.stop);
        this.presencePenalty = builder.presencePenalty;
        this.frequencyPenalty = builder.frequencyPenalty;
        this.bestOf = builder.bestOf;
    }

    public Response<String> generate(String prompt) {
        CompletionsOptions options = new CompletionsOptions(Collections.singletonList(prompt)).setModel(this.deploymentName).setMaxTokens(this.maxTokens).setTemperature(this.temperature).setTopP(this.topP).setLogitBias(this.logitBias).setUser(this.user).setLogprobs(this.logprobs).setEcho(this.echo).setStop(this.stop).setPresencePenalty(this.presencePenalty).setFrequencyPenalty(this.frequencyPenalty).setBestOf(this.bestOf);
        Completions completions = (Completions)AzureOpenAiExceptionMapper.INSTANCE.withExceptionMapper(() -> this.client.getCompletions(this.deploymentName, options));
        return Response.from((Object)((Choice)completions.getChoices().get(0)).getText(), (TokenUsage)InternalAzureOpenAiHelper.tokenUsageFrom(completions.getUsage()), (FinishReason)InternalAzureOpenAiHelper.finishReasonFrom(((Choice)completions.getChoices().get(0)).getFinishReason()));
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(AzureOpenAiLanguageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            AzureOpenAiLanguageModelBuilderFactory factory = (AzureOpenAiLanguageModelBuilderFactory)iterator.next();
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
        private Integer maxTokens;
        private Double temperature;
        private Double topP;
        private Map<String, Integer> logitBias;
        private String user;
        private Integer logprobs;
        private Boolean echo;
        private List<String> stop;
        private Double presencePenalty;
        private Double frequencyPenalty;
        private Integer bestOf;
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

        public Builder httpClientProvider(HttpClientProvider httpClientProvider) {
            this.httpClientProvider = httpClientProvider;
            return this;
        }

        public Builder deploymentName(String deploymentName) {
            this.deploymentName = deploymentName;
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Builder logitBias(Map<String, Integer> logitBias) {
            this.logitBias = logitBias;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder logprobs(Integer logprobs) {
            this.logprobs = logprobs;
            return this;
        }

        public Builder echo(Boolean echo) {
            this.echo = echo;
            return this;
        }

        public Builder stop(List<String> stop) {
            this.stop = stop;
            return this;
        }

        public Builder presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public Builder frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public Builder bestOf(Integer bestOf) {
            this.bestOf = bestOf;
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

        public Builder logRequestsAndResponses(boolean logRequestsAndResponses) {
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

        public AzureOpenAiLanguageModel build() {
            return new AzureOpenAiLanguageModel(this);
        }
    }
}

