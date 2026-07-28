/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.TokenCountEstimator
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.googleai.FunctionMapper;
import dev.langchain4j.model.googleai.GeminiContent;
import dev.langchain4j.model.googleai.GeminiCountTokensRequest;
import dev.langchain4j.model.googleai.GeminiCountTokensResponse;
import dev.langchain4j.model.googleai.GeminiGenerateContentRequest;
import dev.langchain4j.model.googleai.GeminiService;
import dev.langchain4j.model.googleai.PartsAndContentsMapper;
import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;

public class GoogleAiGeminiTokenCountEstimator
implements TokenCountEstimator {
    private final GeminiService geminiService;
    private final String modelName;
    private final Integer maxRetries;

    public GoogleAiGeminiTokenCountEstimator(Builder builder) {
        this.geminiService = new GeminiService(builder.httpClientBuilder, builder.apiKey, builder.baseUrl, (Boolean)Utils.getOrDefault((Object)builder.logRequestsAndResponses, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false), builder.logger, builder.timeout, null);
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    public static Builder builder() {
        return new Builder();
    }

    public int estimateTokenCountInText(String text) {
        return this.estimateTokenCountInMessages(Collections.singletonList(UserMessage.from((String)text)));
    }

    public int estimateTokenCountInMessage(ChatMessage message) {
        return this.estimateTokenCountInMessages(Collections.singletonList(message));
    }

    public int estimateTokenCountInToolExecutionRequests(Iterable<ToolExecutionRequest> toolExecutionRequests) {
        LinkedList allToolRequests = new LinkedList();
        toolExecutionRequests.forEach(allToolRequests::add);
        return this.estimateTokenCountInMessage((ChatMessage)AiMessage.from(allToolRequests));
    }

    public int estimateTokenCountInMessages(Iterable<ChatMessage> messages) {
        LinkedList<ChatMessage> allMessages = new LinkedList<ChatMessage>();
        messages.forEach(allMessages::add);
        List<GeminiContent> geminiContentList = PartsAndContentsMapper.fromMessageToGContent(allMessages, null, false);
        GeminiCountTokensRequest countTokensRequest = new GeminiCountTokensRequest(geminiContentList, null);
        return this.estimateTokenCount(countTokensRequest);
    }

    public int estimateTokenCountInToolSpecifications(Iterable<ToolSpecification> toolSpecifications) {
        LinkedList<ToolSpecification> allTools = new LinkedList<ToolSpecification>();
        toolSpecifications.forEach(allTools::add);
        GeminiContent dummyContent = new GeminiContent(Collections.singletonList(GeminiContent.GeminiPart.builder().text("Dummy content").build()), null);
        GeminiCountTokensRequest countTokensRequestWithDummyContent = new GeminiCountTokensRequest(null, GeminiGenerateContentRequest.builder().model("models/" + this.modelName).contents(Collections.singletonList(dummyContent)).tools(FunctionMapper.fromToolSpecsToGTools(allTools, false, false, false, false, false)).build());
        return this.estimateTokenCount(countTokensRequestWithDummyContent) - 2;
    }

    private int estimateTokenCount(GeminiCountTokensRequest countTokensRequest) {
        GeminiCountTokensResponse countTokensResponse = (GeminiCountTokensResponse)RetryUtils.withRetryMappingExceptions(() -> this.geminiService.countTokens(this.modelName, countTokensRequest), (int)this.maxRetries);
        return countTokensResponse.totalTokens();
    }

    public static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String modelName;
        private String apiKey;
        private String baseUrl;
        private Boolean logRequestsAndResponses;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Duration timeout;
        private Integer maxRetries;

        Builder() {
        }

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder logRequestsAndResponses(Boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
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

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public GoogleAiGeminiTokenCountEstimator build() {
            return new GoogleAiGeminiTokenCountEstimator(this);
        }
    }
}

