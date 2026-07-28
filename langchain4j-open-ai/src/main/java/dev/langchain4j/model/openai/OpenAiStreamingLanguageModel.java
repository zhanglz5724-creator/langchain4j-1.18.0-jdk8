/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.StreamingResponseHandler
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.language.StreamingLanguageModel
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.language.StreamingLanguageModel;
import dev.langchain4j.model.openai.OpenAiLanguageModelName;
import dev.langchain4j.model.openai.OpenAiStreamingResponseBuilder;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.completion.CompletionChoice;
import dev.langchain4j.model.openai.internal.completion.CompletionRequest;
import dev.langchain4j.model.openai.internal.completion.CompletionResponse;
import dev.langchain4j.model.openai.internal.shared.StreamOptions;
import dev.langchain4j.model.openai.spi.OpenAiStreamingLanguageModelBuilderFactory;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;

public class OpenAiStreamingLanguageModel
implements StreamingLanguageModel {
    private final OpenAiClient client;
    private final String modelName;
    private final Double temperature;

    public OpenAiStreamingLanguageModel(OpenAiStreamingLanguageModelBuilder builder) {
        this.client = ((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)OpenAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.openai.com/v1"))).apiKey(builder.apiKey)).organizationId(builder.organizationId)).projectId(builder.projectId)).connectTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(15L)))).readTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L)))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).userAgent("langchain4j-openai")).customHeaders(builder.customHeadersSupplier)).customQueryParams(builder.customQueryParams)).build();
        this.modelName = builder.modelName;
        this.temperature = builder.temperature;
    }

    public String modelName() {
        return this.modelName;
    }

    public void generate(String prompt, StreamingResponseHandler<String> handler) {
        CompletionRequest request = CompletionRequest.builder().stream(true).streamOptions(StreamOptions.builder().includeUsage(true).build()).model(this.modelName).prompt(prompt).temperature(this.temperature).build();
        OpenAiStreamingResponseBuilder responseBuilder = new OpenAiStreamingResponseBuilder();
        this.client.completion(request).onPartialResponse(partialResponse -> {
            responseBuilder.append((CompletionResponse)partialResponse);
            for (CompletionChoice choice : partialResponse.choices()) {
                String token = choice.text();
                if (!Utils.isNotNullOrEmpty((String)token)) continue;
                handler.onNext(token);
            }
        }).onComplete(() -> {
            ChatResponse chatResponse = responseBuilder.build();
            handler.onComplete(Response.from((Object)chatResponse.aiMessage().text(), (TokenUsage)chatResponse.metadata().tokenUsage(), (FinishReason)chatResponse.metadata().finishReason()));
        }).onError(throwable -> handler.onError((Throwable)ExceptionMapper.DEFAULT.mapException(throwable))).execute();
    }

    public static OpenAiStreamingLanguageModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OpenAiStreamingLanguageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OpenAiStreamingLanguageModelBuilderFactory factory = (OpenAiStreamingLanguageModelBuilderFactory)iterator.next();
            return (OpenAiStreamingLanguageModelBuilder)factory.get();
        }
        return new OpenAiStreamingLanguageModelBuilder();
    }

    public static class OpenAiStreamingLanguageModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String organizationId;
        private String projectId;
        private String modelName;
        private Double temperature;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Supplier<Map<String, String>> customHeadersSupplier;
        private Map<String, String> customQueryParams;

        public OpenAiStreamingLanguageModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder modelName(OpenAiLanguageModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public OpenAiStreamingLanguageModelBuilder customQueryParams(Map<String, String> customQueryParams) {
            this.customQueryParams = customQueryParams;
            return this;
        }

        public OpenAiStreamingLanguageModel build() {
            return new OpenAiStreamingLanguageModel(this);
        }
    }
}

