/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.anthropic.internal.client;

import dev.langchain4j.Internal;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCountTokensRequest;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCreateMessageRequest;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCreateMessageResponse;
import dev.langchain4j.model.anthropic.internal.api.AnthropicModelsListResponse;
import dev.langchain4j.model.anthropic.internal.api.MessageTokenCountResponse;
import dev.langchain4j.model.anthropic.internal.client.AnthropicClientBuilderFactory;
import dev.langchain4j.model.anthropic.internal.client.AnthropicCreateMessageOptions;
import dev.langchain4j.model.anthropic.internal.client.DefaultAnthropicClient;
import dev.langchain4j.model.anthropic.internal.client.ParsedAndRawResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;

@Internal
public abstract class AnthropicClient {
    public abstract AnthropicCreateMessageResponse createMessage(AnthropicCreateMessageRequest var1);

    public ParsedAndRawResponse createMessageWithRawResponse(AnthropicCreateMessageRequest request) {
        AnthropicCreateMessageResponse parsedResponse = this.createMessage(request);
        return new ParsedAndRawResponse(parsedResponse, null);
    }

    public void createMessage(AnthropicCreateMessageRequest request, AnthropicCreateMessageOptions options, StreamingChatResponseHandler handler) {
        this.createMessage(request, handler);
    }

    public abstract void createMessage(AnthropicCreateMessageRequest var1, StreamingChatResponseHandler var2);

    public MessageTokenCountResponse countTokens(AnthropicCountTokensRequest request) {
        throw new UnsupportedOperationException("Token counting is not implemented");
    }

    public AnthropicModelsListResponse listModels() {
        throw new UnsupportedOperationException("Model listing is not supported by this client implementation");
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(AnthropicClientBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            AnthropicClientBuilderFactory factory = (AnthropicClientBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return DefaultAnthropicClient.builder();
    }

    public static abstract class Builder<T extends AnthropicClient, B extends Builder<T, B>> {
        public HttpClientBuilder httpClientBuilder;
        public String baseUrl;
        public String apiKey;
        public String version;
        public String beta;
        public Duration timeout;
        public Logger logger;
        public Boolean logRequests;
        public Boolean logResponses;
        public Supplier<Map<String, String>> customHeadersSupplier;

        public abstract T build();

        public B httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this.self();
        }

        public B baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this.self();
        }

        public B apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this.self();
        }

        public B version(String version) {
            this.version = version;
            return this.self();
        }

        public B beta(String beta) {
            this.beta = beta;
            return this.self();
        }

        public B timeout(Duration timeout) {
            this.timeout = timeout;
            return this.self();
        }

        public B logRequests() {
            return this.logRequests(true);
        }

        public B logRequests(Boolean logRequests) {
            if (logRequests == null) {
                logRequests = false;
            }
            this.logRequests = logRequests;
            return this.self();
        }

        public B logResponses() {
            return this.logResponses(true);
        }

        public B logResponses(Boolean logResponses) {
            if (logResponses == null) {
                logResponses = false;
            }
            this.logResponses = logResponses;
            return this.self();
        }

        public B logger(Logger logger) {
            this.logger = logger;
            return this.self();
        }

        public B customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this.self();
        }

        public B customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this.self();
        }

        private B self() {
            return (B)this;
        }
    }
}

