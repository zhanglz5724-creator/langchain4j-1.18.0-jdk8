/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.language.LanguageModel
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.mistralai;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.language.LanguageModel;
import dev.langchain4j.model.mistralai.MistralAiFimModelName;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionChoice;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiFimCompletionRequest;
import dev.langchain4j.model.mistralai.internal.client.MistralAiClient;
import dev.langchain4j.model.mistralai.internal.mapper.MistralAiMapper;
import dev.langchain4j.model.mistralai.spi.MistralAiFimModelBuilderFactory;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;

public class MistralAiFimModel
implements LanguageModel {
    private final MistralAiClient client;
    private final String modelName;
    private final Double temperature;
    private final Integer maxTokens;
    private final Integer minTokens;
    private final Double topP;
    private final Integer randomSeed;
    private final List<String> stop;
    private final Integer maxRetries;

    public MistralAiFimModel(Builder builder) {
        this.client = ((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)MistralAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.mistral.ai/v1"))).apiKey(builder.apiKey)).timeout(builder.timeout)).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.temperature = builder.temperature;
        this.maxTokens = builder.maxTokens;
        this.minTokens = builder.minTokens;
        this.topP = builder.topP;
        this.randomSeed = builder.randomSeed;
        this.stop = Utils.copy((List)builder.stop);
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    public Response<String> generate(String prompt, String suffix) {
        return this.completion(prompt, suffix);
    }

    public Response<String> generate(String prompt) {
        return this.generate(prompt, null);
    }

    private Response<String> completion(String prompt, String suffix) {
        ValidationUtils.ensureNotBlank((String)prompt, (String)"Prompt");
        MistralAiFimCompletionRequest request = MistralAiFimCompletionRequest.builder().model(this.modelName).prompt(prompt).suffix(suffix).temperature(this.temperature).maxTokens(this.maxTokens).minTokens(this.minTokens).topP(this.topP).randomSeed(this.randomSeed).stop(this.stop).stream(false).build();
        MistralAiChatCompletionResponse response = (MistralAiChatCompletionResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.fimCompletion(request), (int)this.maxRetries);
        MistralAiChatCompletionChoice responseChoice = response.getChoices().get(0);
        return Response.from((Object)responseChoice.getMessage().asText(), (TokenUsage)MistralAiMapper.tokenUsageFrom(response.getUsage()), (FinishReason)MistralAiMapper.finishReasonFrom(responseChoice.getFinishReason()));
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(MistralAiFimModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            MistralAiFimModelBuilderFactory factory = (MistralAiFimModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String modelName;
        private Double temperature;
        private Integer maxTokens;
        private Integer minTokens;
        private Double topP;
        private Integer randomSeed;
        private List<String> stop;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Integer maxRetries;

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder modelName(MistralAiFimModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder minTokens(Integer minTokens) {
            this.minTokens = minTokens;
            return this;
        }

        public Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Builder randomSeed(Integer randomSeed) {
            this.randomSeed = randomSeed;
            return this;
        }

        public Builder stop(List<String> stop) {
            this.stop = stop;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
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

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public MistralAiFimModel build() {
            return new MistralAiFimModel(this);
        }
    }
}

