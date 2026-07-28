/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.StreamingResponseHandler
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.language.StreamingLanguageModel
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.ollama;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.language.StreamingLanguageModel;
import dev.langchain4j.model.ollama.CompletionRequest;
import dev.langchain4j.model.ollama.InternalOllamaHelper;
import dev.langchain4j.model.ollama.OllamaClient;
import dev.langchain4j.model.ollama.Options;
import dev.langchain4j.model.ollama.spi.OllamaStreamingLanguageModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class OllamaStreamingLanguageModel
implements StreamingLanguageModel {
    private final OllamaClient client;
    private final String modelName;
    private final Options options;
    private final ResponseFormat responseFormat;

    public OllamaStreamingLanguageModel(OllamaStreamingLanguageModelBuilder builder) {
        this.client = OllamaClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl(builder.baseUrl).timeout(builder.timeout).logRequests(builder.logRequests).logResponses(builder.logResponses).customHeaders(builder.customHeadersSupplier).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.options = Options.builder().temperature(builder.temperature).topK(builder.topK).topP(builder.topP).repeatPenalty(builder.repeatPenalty).seed(builder.seed).numPredict(builder.numPredict).numCtx(builder.numCtx).stop(builder.stop).build();
        this.responseFormat = builder.responseFormat;
    }

    public static OllamaStreamingLanguageModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OllamaStreamingLanguageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OllamaStreamingLanguageModelBuilderFactory factory = (OllamaStreamingLanguageModelBuilderFactory)iterator.next();
            return (OllamaStreamingLanguageModelBuilder)factory.get();
        }
        return new OllamaStreamingLanguageModelBuilder();
    }

    public void generate(String prompt, StreamingResponseHandler<String> handler) {
        CompletionRequest request = CompletionRequest.builder().model(this.modelName).prompt(prompt).options(this.options).format(InternalOllamaHelper.toOllamaResponseFormat(this.responseFormat)).stream(true).build();
        this.client.streamingCompletion(request, handler);
    }

    public static class OllamaStreamingLanguageModelBuilder {
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
        private Supplier<Map<String, String>> customHeadersSupplier;
        private Boolean logRequests;
        private Boolean logResponses;

        public OllamaStreamingLanguageModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder repeatPenalty(Double repeatPenalty) {
            this.repeatPenalty = repeatPenalty;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder numPredict(Integer numPredict) {
            this.numPredict = numPredict;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder numCtx(Integer numCtx) {
            this.numCtx = numCtx;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder stop(List<String> stop) {
            this.stop = stop;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public OllamaStreamingLanguageModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OllamaStreamingLanguageModel build() {
            return new OllamaStreamingLanguageModel(this);
        }
    }
}

