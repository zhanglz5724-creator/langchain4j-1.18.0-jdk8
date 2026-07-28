/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ChatMessageType
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.TokenCountEstimator
 */
package dev.langchain4j.model.anthropic;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.anthropic.AnthropicChatModelName;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCacheType;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCountTokensRequest;
import dev.langchain4j.model.anthropic.internal.api.AnthropicMessage;
import dev.langchain4j.model.anthropic.internal.api.AnthropicRole;
import dev.langchain4j.model.anthropic.internal.api.AnthropicTextContent;
import dev.langchain4j.model.anthropic.internal.client.AnthropicClient;
import dev.langchain4j.model.anthropic.internal.mapper.AnthropicMapper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

@Experimental
public class AnthropicTokenCountEstimator
implements TokenCountEstimator {
    private final AnthropicClient client;
    private final String modelName;
    private final boolean addDummyUserMessageIfNoUserMessages;
    private final String dummyUserMessageText;

    public AnthropicTokenCountEstimator(Builder builder) {
        this.client = ((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)AnthropicClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.anthropic.com/v1/"))).apiKey(builder.apiKey)).version((String)Utils.getOrDefault((Object)builder.version, (Object)"2023-06-01"))).beta(builder.beta)).timeout(builder.timeout)).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.addDummyUserMessageIfNoUserMessages = Boolean.TRUE.equals(builder.addDummyUserMessageIfNoUserMessages);
        this.dummyUserMessageText = (String)Utils.getOrDefault((Object)builder.dummyUserMessageText, (Object)"ping");
    }

    public int estimateTokenCountInText(String text) {
        AnthropicCountTokensRequest request = AnthropicCountTokensRequest.builder().model(this.modelName).messages(Arrays.asList(new AnthropicMessage(AnthropicRole.USER, Arrays.asList(new AnthropicTextContent(text))))).build();
        return this.client.countTokens(request).getInputTokens();
    }

    public int estimateTokenCountInMessage(ChatMessage message) {
        return this.estimateTokenCountInMessages(Arrays.asList(message));
    }

    public int estimateTokenCountInMessages(Iterable<ChatMessage> messages) {
        ArrayList<ChatMessage> systemMessages = new ArrayList<ChatMessage>();
        ArrayList<ChatMessage> otherMessages = new ArrayList<ChatMessage>();
        for (ChatMessage message : messages) {
            if (message.type() == ChatMessageType.SYSTEM) {
                systemMessages.add(message);
                continue;
            }
            otherMessages.add(message);
        }
        AnthropicCountTokensRequest.Builder requestBuilder = AnthropicCountTokensRequest.builder().model(this.modelName);
        if (!systemMessages.isEmpty()) {
            requestBuilder.system(AnthropicMapper.toAnthropicSystemPrompt(systemMessages, AnthropicCacheType.NO_CACHE));
        }
        if (!otherMessages.isEmpty()) {
            requestBuilder.messages(AnthropicMapper.toAnthropicMessages(otherMessages));
        } else if (this.addDummyUserMessageIfNoUserMessages) {
            requestBuilder.messages(Arrays.asList(new AnthropicMessage(AnthropicRole.USER, Arrays.asList(new AnthropicTextContent(this.dummyUserMessageText)))));
        } else {
            throw new IllegalArgumentException("Anthropic countTokens requires at least one non-system message. Provided messages contained only system messages or were empty. To fix: add a UserMessage to your conversation, or configure AnthropicTokenCountEstimator.builder().addDummyUserMessageIfNoUserMessages() to auto-insert a minimal dummy user message for token estimation.");
        }
        return this.client.countTokens(requestBuilder.build()).getInputTokens();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String version;
        private String beta;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private String modelName;
        private Boolean addDummyUserMessageIfNoUserMessages;
        private String dummyUserMessageText;

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

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder beta(String beta) {
            this.beta = beta;
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

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder modelName(AnthropicChatModelName modelName) {
            return this.modelName(modelName.toString());
        }

        public Builder addDummyUserMessageIfNoUserMessages() {
            this.addDummyUserMessageIfNoUserMessages = true;
            return this;
        }

        public Builder addDummyUserMessageIfNoUserMessages(String dummyUserMessage) {
            this.addDummyUserMessageIfNoUserMessages = true;
            this.dummyUserMessageText = dummyUserMessage;
            return this;
        }

        public AnthropicTokenCountEstimator build() {
            return new AnthropicTokenCountEstimator(this);
        }
    }
}

