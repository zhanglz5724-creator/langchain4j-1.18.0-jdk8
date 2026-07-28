/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.language.LanguageModel
 *  dev.langchain4j.model.openai.internal.OpenAiClient
 *  dev.langchain4j.model.openai.internal.OpenAiUtils
 *  dev.langchain4j.model.openai.internal.completion.CompletionChoice
 *  dev.langchain4j.model.openai.internal.completion.CompletionRequest
 *  dev.langchain4j.model.openai.internal.completion.CompletionResponse
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.localai;

import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.language.LanguageModel;
import dev.langchain4j.model.localai.spi.LocalAiLanguageModelBuilderFactory;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.OpenAiUtils;
import dev.langchain4j.model.openai.internal.completion.CompletionChoice;
import dev.langchain4j.model.openai.internal.completion.CompletionRequest;
import dev.langchain4j.model.openai.internal.completion.CompletionResponse;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import org.slf4j.Logger;

public class LocalAiLanguageModel
implements LanguageModel {
    private final OpenAiClient client;
    private final String modelName;
    private final Double temperature;
    private final Double topP;
    private final Integer maxTokens;
    private final Integer maxRetries;

    @Deprecated
    public LocalAiLanguageModel(String baseUrl, String modelName, Double temperature, Double topP, Integer maxTokens, Duration timeout, Integer maxRetries, Boolean logRequests, Boolean logResponses) {
        temperature = temperature == null ? 0.7 : temperature;
        timeout = timeout == null ? Duration.ofSeconds(60L) : timeout;
        maxRetries = maxRetries == null ? 3 : maxRetries;
        this.client = OpenAiClient.builder().baseUrl(ValidationUtils.ensureNotBlank((String)baseUrl, (String)"baseUrl")).connectTimeout(timeout).readTimeout(timeout).logRequests(logRequests).logResponses(logResponses).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.temperature = temperature;
        this.topP = topP;
        this.maxTokens = maxTokens;
        this.maxRetries = maxRetries;
    }

    public LocalAiLanguageModel(LocalAiLanguageModelBuilder builder) {
        this.client = OpenAiClient.builder().baseUrl(ValidationUtils.ensureNotBlank((String)builder.baseUrl, (String)"baseUrl")).connectTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).readTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).logRequests(builder.logRequests).logResponses(builder.logResponses).logger(builder.logger).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.temperature = (Double)Utils.getOrDefault((Object)builder.temperature, (Object)0.7);
        this.topP = builder.topP;
        this.maxTokens = builder.maxTokens;
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)3);
    }

    public Response<String> generate(String prompt) {
        CompletionRequest request = CompletionRequest.builder().model(this.modelName).prompt(prompt).temperature(this.temperature).topP(this.topP).maxTokens(this.maxTokens).build();
        CompletionResponse response = (CompletionResponse)RetryUtils.withRetryMappingExceptions(() -> (CompletionResponse)this.client.completion(request).execute(), (int)this.maxRetries);
        return Response.from((Object)response.text(), null, (FinishReason)OpenAiUtils.finishReasonFrom((String)((CompletionChoice)response.choices().get(0)).finishReason()));
    }

    public static LocalAiLanguageModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(LocalAiLanguageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            LocalAiLanguageModelBuilderFactory factory = (LocalAiLanguageModelBuilderFactory)iterator.next();
            return (LocalAiLanguageModelBuilder)factory.get();
        }
        return new LocalAiLanguageModelBuilder();
    }

    public static class LocalAiLanguageModelBuilder {
        private String baseUrl;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Integer maxTokens;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;

        public LocalAiLanguageModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public LocalAiLanguageModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public LocalAiLanguageModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public LocalAiLanguageModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public LocalAiLanguageModelBuilder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public LocalAiLanguageModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public LocalAiLanguageModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public LocalAiLanguageModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public LocalAiLanguageModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public LocalAiLanguageModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public LocalAiLanguageModel build() {
            return new LocalAiLanguageModel(this);
        }

        public String toString() {
            return "LocalAiLanguageModel.LocalAiLanguageModelBuilder(baseUrl=" + this.baseUrl + ", modelName=" + this.modelName + ", temperature=" + this.temperature + ", topP=" + this.topP + ", maxTokens=" + this.maxTokens + ", timeout=" + this.timeout + ", maxRetries=" + this.maxRetries + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

