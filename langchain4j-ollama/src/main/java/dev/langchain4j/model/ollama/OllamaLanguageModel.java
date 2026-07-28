/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.language.LanguageModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.ollama;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.language.LanguageModel;
import dev.langchain4j.model.ollama.CompletionRequest;
import dev.langchain4j.model.ollama.CompletionResponse;
import dev.langchain4j.model.ollama.InternalOllamaHelper;
import dev.langchain4j.model.ollama.OllamaClient;
import dev.langchain4j.model.ollama.Options;
import dev.langchain4j.model.ollama.spi.OllamaLanguageModelBuilderFactory;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class OllamaLanguageModel
implements LanguageModel {
    private final OllamaClient client;
    private final String modelName;
    private final Options options;
    private final ResponseFormat responseFormat;
    private final Integer maxRetries;

    public OllamaLanguageModel(OllamaLanguageModelBuilder builder) {
        this.client = OllamaClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl(builder.baseUrl).timeout(builder.timeout).logRequests(builder.logRequests).logResponses(builder.logResponses).customHeaders(builder.customHeadersSupplier).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.options = Options.builder().temperature(builder.temperature).topK(builder.topK).topP(builder.topP).repeatPenalty(builder.repeatPenalty).seed(builder.seed).numPredict(builder.numPredict).numCtx(builder.numCtx).stop(builder.stop).build();
        this.responseFormat = builder.responseFormat;
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    public static OllamaLanguageModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OllamaLanguageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OllamaLanguageModelBuilderFactory factory = (OllamaLanguageModelBuilderFactory)iterator.next();
            return (OllamaLanguageModelBuilder)factory.get();
        }
        return new OllamaLanguageModelBuilder();
    }

    public Response<String> generate(String prompt) {
        CompletionRequest request = CompletionRequest.builder().model(this.modelName).prompt(prompt).options(this.options).format(InternalOllamaHelper.toOllamaResponseFormat(this.responseFormat)).stream(false).build();
        CompletionResponse response = (CompletionResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.completion(request), (int)this.maxRetries);
        return Response.from((Object)response.getResponse(), (TokenUsage)new TokenUsage(response.getPromptEvalCount(), response.getEvalCount()));
    }

    public static class OllamaLanguageModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String modelName;
        private Double temperature;
        private Integer topK;
        private Double topP;
        private Double repeatPenalty;
        private Integer seed;
        private Integer numPredict;
        private Integer numCtx;
        private List<String> stop;
        private ResponseFormat responseFormat;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Supplier<Map<String, String>> customHeadersSupplier;

        public OllamaLanguageModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public OllamaLanguageModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public OllamaLanguageModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public OllamaLanguageModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public OllamaLanguageModelBuilder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public OllamaLanguageModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public OllamaLanguageModelBuilder repeatPenalty(Double repeatPenalty) {
            this.repeatPenalty = repeatPenalty;
            return this;
        }

        public OllamaLanguageModelBuilder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public OllamaLanguageModelBuilder numPredict(Integer numPredict) {
            this.numPredict = numPredict;
            return this;
        }

        public OllamaLanguageModelBuilder numCtx(Integer numCtx) {
            this.numCtx = numCtx;
            return this;
        }

        public OllamaLanguageModelBuilder stop(List<String> stop) {
            this.stop = stop;
            return this;
        }

        public OllamaLanguageModelBuilder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public OllamaLanguageModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public OllamaLanguageModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public OllamaLanguageModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public OllamaLanguageModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OllamaLanguageModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public OllamaLanguageModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public OllamaLanguageModel build() {
            return new OllamaLanguageModel(this);
        }
    }
}

