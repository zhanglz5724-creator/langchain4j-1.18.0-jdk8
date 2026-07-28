/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.genai.Client
 *  com.google.genai.types.Content
 *  com.google.genai.types.CountTokensConfig
 *  com.google.genai.types.CountTokensResponse
 *  com.google.genai.types.FunctionDeclaration
 *  com.google.genai.types.Tool
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.TokenCountEstimator
 */
package dev.langchain4j.model.google.genai;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.CountTokensConfig;
import com.google.genai.types.CountTokensResponse;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.google.genai.GoogleGenAiClientFactory;
import dev.langchain4j.model.google.genai.GoogleGenAiContentMapper;
import dev.langchain4j.model.google.genai.GoogleGenAiExceptionMapper;
import dev.langchain4j.model.google.genai.GoogleGenAiToolMapper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GoogleGenAiTokenCountEstimator
implements TokenCountEstimator {
    private final Client client;
    private final String modelName;
    private final Integer maxRetries;

    private GoogleGenAiTokenCountEstimator(Builder builder) {
        this.client = builder.client != null ? builder.client : GoogleGenAiClientFactory.createClient(builder.apiKey, builder.googleCredentials, builder.projectId, builder.location, builder.timeout, builder.customHeaders, builder.apiEndpoint);
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)3);
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

    public int estimateTokenCountInMessages(Iterable<ChatMessage> messages) {
        LinkedList<ChatMessage> allMessages = new LinkedList<ChatMessage>();
        messages.forEach(allMessages::add);
        List<Content> contents = GoogleGenAiContentMapper.toContents(allMessages);
        Content systemInstruction = GoogleGenAiContentMapper.toSystemInstruction(allMessages);
        if (systemInstruction != null) {
            ArrayList<Content> merged = new ArrayList<Content>();
            merged.add(systemInstruction);
            merged.addAll(contents);
            contents = merged;
        }
        if (contents.isEmpty()) {
            return 0;
        }
        return this.estimateTokenCount(contents, null);
    }

    public int estimateTokenCountInToolExecutionRequests(Iterable<ToolExecutionRequest> toolExecutionRequests) {
        LinkedList allToolRequests = new LinkedList();
        toolExecutionRequests.forEach(allToolRequests::add);
        return this.estimateTokenCountInMessage((ChatMessage)AiMessage.from(allToolRequests));
    }

    public int estimateTokenCountInToolSpecifications(Iterable<ToolSpecification> toolSpecifications) {
        ArrayList<FunctionDeclaration> functionDeclarations = new ArrayList<FunctionDeclaration>();
        for (ToolSpecification toolSpec : toolSpecifications) {
            functionDeclarations.add(GoogleGenAiToolMapper.convertToGoogleFunction(toolSpec));
        }
        Tool tool = Tool.builder().functionDeclarations(functionDeclarations).build();
        String toolJson = tool.toJson();
        return this.estimateTokenCountInText(toolJson);
    }

    private int estimateTokenCount(List<Content> contents, CountTokensConfig config) {
        CountTokensResponse response = (CountTokensResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.models.countTokens(this.modelName, contents, config), (int)this.maxRetries, (ExceptionMapper)GoogleGenAiExceptionMapper.INSTANCE);
        return response.totalTokens().orElse(0);
    }

    public static class Builder {
        private Client client;
        private String apiKey;
        private GoogleCredentials googleCredentials;
        private String projectId;
        private String location;
        private Duration timeout;
        private String modelName;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private String apiEndpoint;
        private Map<String, String> customHeaders;

        public Builder client(Client client) {
            this.client = client;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder googleCredentials(GoogleCredentials googleCredentials) {
            this.googleCredentials = googleCredentials;
            return this;
        }

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
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

        public Builder apiEndpoint(String apiEndpoint) {
            this.apiEndpoint = apiEndpoint;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public GoogleGenAiTokenCountEstimator build() {
            return new GoogleGenAiTokenCountEstimator(this);
        }
    }
}

