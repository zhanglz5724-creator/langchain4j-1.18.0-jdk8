/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.model.StreamingResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.mistralai.internal.client;

import dev.langchain4j.Internal;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.mistralai.internal.api.MistralAiBatchJob;
import dev.langchain4j.model.mistralai.internal.api.MistralAiBatchJobRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiBatchJobsResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiBatchResultEntry;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiEmbeddingRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiEmbeddingResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiFimCompletionRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModelResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModerationRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModerationResponse;
import dev.langchain4j.model.mistralai.internal.client.DefaultMistralAiClient;
import dev.langchain4j.model.mistralai.internal.client.MistralAiClientBuilderFactory;
import dev.langchain4j.model.mistralai.internal.client.ParsedAndRawResponse;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;

@Internal
public abstract class MistralAiClient {
    public abstract MistralAiChatCompletionResponse chatCompletion(MistralAiChatCompletionRequest var1);

    public ParsedAndRawResponse<MistralAiChatCompletionResponse> chatCompletionWithRawResponse(MistralAiChatCompletionRequest request) {
        MistralAiChatCompletionResponse parsedResponse = this.chatCompletion(request);
        return new ParsedAndRawResponse<MistralAiChatCompletionResponse>(parsedResponse, null);
    }

    public abstract void streamingChatCompletion(MistralAiChatCompletionRequest var1, StreamingChatResponseHandler var2);

    public void streamingChatCompletion(MistralAiChatCompletionRequest request, StreamingChatResponseHandler handler, boolean returnThinking) {
        if (returnThinking) {
            throw new UnsupportedFeatureException("Returning thinking/reasoning content is not supported with this client implementation: " + this.getClass().getName());
        }
        this.streamingChatCompletion(request, handler);
    }

    public abstract MistralAiEmbeddingResponse embedding(MistralAiEmbeddingRequest var1);

    public ParsedAndRawResponse<MistralAiEmbeddingResponse> embeddingWithRawResponse(MistralAiEmbeddingRequest request) {
        MistralAiEmbeddingResponse parsedResponse = this.embedding(request);
        return new ParsedAndRawResponse<MistralAiEmbeddingResponse>(parsedResponse, null);
    }

    public abstract MistralAiModerationResponse moderation(MistralAiModerationRequest var1);

    public abstract MistralAiModelResponse listModels();

    public abstract MistralAiChatCompletionResponse fimCompletion(MistralAiFimCompletionRequest var1);

    public ParsedAndRawResponse<MistralAiChatCompletionResponse> fimCompletionWithRawResponse(MistralAiFimCompletionRequest request) {
        MistralAiChatCompletionResponse parsedResponse = this.fimCompletion(request);
        return new ParsedAndRawResponse<MistralAiChatCompletionResponse>(parsedResponse, null);
    }

    public abstract void streamingFimCompletion(MistralAiFimCompletionRequest var1, StreamingResponseHandler<String> var2);

    public MistralAiBatchJob createBatchJob(MistralAiBatchJobRequest request) {
        throw this.batchNotSupported();
    }

    public MistralAiBatchJob retrieveBatchJob(String jobId) {
        throw this.batchNotSupported();
    }

    public MistralAiBatchJob cancelBatchJob(String jobId) {
        throw this.batchNotSupported();
    }

    public MistralAiBatchJobsResponse listBatchJobs(Integer page, Integer pageSize) {
        throw this.batchNotSupported();
    }

    public List<MistralAiBatchResultEntry> downloadBatchResults(String fileId) {
        throw this.batchNotSupported();
    }

    private UnsupportedFeatureException batchNotSupported() {
        return new UnsupportedFeatureException("Batch operations are not supported by this client implementation: " + this.getClass().getName());
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(MistralAiClientBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            MistralAiClientBuilderFactory factory = (MistralAiClientBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return DefaultMistralAiClient.builder();
    }

    public static abstract class Builder<T extends MistralAiClient, B extends Builder<T, B>> {
        public String baseUrl;
        public String apiKey;
        public Duration timeout;
        public Boolean logRequests;
        public Boolean logResponses;
        public Logger logger;
        public HttpClientBuilder httpClientBuilder;
        public Supplier<Map<String, String>> customHeadersSupplier;

        public abstract T build();

        public B baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return (B)this;
        }

        public B apiKey(String apiKey) {
            this.apiKey = apiKey;
            return (B)this;
        }

        public B timeout(Duration timeout) {
            this.timeout = timeout;
            return (B)this;
        }

        public B logRequests() {
            return this.logRequests(true);
        }

        public B logRequests(Boolean logRequests) {
            if (logRequests == null) {
                logRequests = false;
            }
            this.logRequests = logRequests;
            return (B)this;
        }

        public B logResponses() {
            return this.logResponses(true);
        }

        public B logResponses(Boolean logResponses) {
            if (logResponses == null) {
                logResponses = false;
            }
            this.logResponses = logResponses;
            return (B)this;
        }

        public B logger(Logger logger) {
            this.logger = logger;
            return (B)this;
        }

        public B httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return (B)this;
        }

        public B customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return (B)this;
        }

        public B customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return (B)this;
        }
    }
}

