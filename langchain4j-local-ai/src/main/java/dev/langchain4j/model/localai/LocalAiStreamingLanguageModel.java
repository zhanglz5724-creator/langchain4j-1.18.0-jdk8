/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.StreamingResponseHandler
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.language.StreamingLanguageModel
 *  dev.langchain4j.model.openai.OpenAiStreamingResponseBuilder
 *  dev.langchain4j.model.openai.internal.OpenAiClient
 *  dev.langchain4j.model.openai.internal.completion.CompletionRequest
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.localai;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.language.StreamingLanguageModel;
import dev.langchain4j.model.localai.spi.LocalAiStreamingLanguageModelBuilderFactory;
import dev.langchain4j.model.openai.OpenAiStreamingResponseBuilder;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.completion.CompletionRequest;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import org.slf4j.Logger;

public class LocalAiStreamingLanguageModel
implements StreamingLanguageModel {
    private final OpenAiClient client;
    private final String modelName;
    private final Double temperature;
    private final Double topP;
    private final Integer maxTokens;

    @Deprecated
    public LocalAiStreamingLanguageModel(String baseUrl, String modelName, Double temperature, Double topP, Integer maxTokens, Duration timeout, Boolean logRequests, Boolean logResponses) {
        temperature = temperature == null ? 0.7 : temperature;
        timeout = timeout == null ? Duration.ofSeconds(60L) : timeout;
        this.client = OpenAiClient.builder().baseUrl(ValidationUtils.ensureNotBlank((String)baseUrl, (String)"baseUrl")).connectTimeout(timeout).readTimeout(timeout).logRequests(logRequests).logResponses(logResponses).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.temperature = temperature;
        this.topP = topP;
        this.maxTokens = maxTokens;
    }

    public LocalAiStreamingLanguageModel(LocalAiStreamingLanguageModelBuilder builder) {
        this.client = OpenAiClient.builder().baseUrl(ValidationUtils.ensureNotBlank((String)builder.baseUrl, (String)"baseUrl")).connectTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).readTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).logRequests(builder.logRequests).logResponses(builder.logResponses).logger(builder.logger).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.temperature = (Double)Utils.getOrDefault((Object)builder.temperature, (Object)0.7);
        this.topP = builder.topP;
        this.maxTokens = builder.maxTokens;
    }

    public void generate(String prompt, StreamingResponseHandler<String> handler) {
        CompletionRequest request = CompletionRequest.builder().model(this.modelName).prompt(prompt).temperature(this.temperature).topP(this.topP).maxTokens(this.maxTokens).build();
        OpenAiStreamingResponseBuilder responseBuilder = new OpenAiStreamingResponseBuilder();
        this.client.completion(request).onPartialResponse(partialResponse -> {
            responseBuilder.append(partialResponse);
            String token = partialResponse.text();
            if (token != null) {
                handler.onNext(token);
            }
        }).onComplete(() -> {
            ChatResponse chatResponse = responseBuilder.build();
            handler.onComplete(Response.from((Object)chatResponse.aiMessage().text(), (TokenUsage)chatResponse.metadata().tokenUsage(), (FinishReason)chatResponse.metadata().finishReason()));
        }).onError(arg_0 -> handler.onError(arg_0)).execute();
    }

    public static LocalAiStreamingLanguageModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(LocalAiStreamingLanguageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            LocalAiStreamingLanguageModelBuilderFactory factory = (LocalAiStreamingLanguageModelBuilderFactory)iterator.next();
            return (LocalAiStreamingLanguageModelBuilder)factory.get();
        }
        return new LocalAiStreamingLanguageModelBuilder();
    }

    public static class LocalAiStreamingLanguageModelBuilder {
        private String baseUrl;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Integer maxTokens;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;

        public LocalAiStreamingLanguageModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public LocalAiStreamingLanguageModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public LocalAiStreamingLanguageModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public LocalAiStreamingLanguageModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public LocalAiStreamingLanguageModelBuilder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public LocalAiStreamingLanguageModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public LocalAiStreamingLanguageModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public LocalAiStreamingLanguageModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public LocalAiStreamingLanguageModel build() {
            return new LocalAiStreamingLanguageModel(this);
        }

        public String toString() {
            return "LocalAiStreamingLanguageModel.LocalAiStreamingLanguageModelBuilder(baseUrl=" + this.baseUrl + ", modelName=" + this.modelName + ", temperature=" + this.temperature + ", topP=" + this.topP + ", maxTokens=" + this.maxTokens + ", timeout=" + this.timeout + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

