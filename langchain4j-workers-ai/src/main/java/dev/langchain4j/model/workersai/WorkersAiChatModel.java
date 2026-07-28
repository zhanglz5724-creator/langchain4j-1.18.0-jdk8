/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.ChatRequestValidationUtils
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.workersai;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.ChatRequestValidationUtils;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.workersai.client.AbstractWorkersAIModel;
import dev.langchain4j.model.workersai.client.WorkersAiChatCompletionRequest;
import dev.langchain4j.model.workersai.client.WorkersAiChatCompletionResponse;
import dev.langchain4j.model.workersai.client.WorkersAiTextCompletionResponse;
import dev.langchain4j.model.workersai.spi.WorkersAiChatModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkersAiChatModel
extends AbstractWorkersAIModel
implements ChatModel {
    private static final Logger log = LoggerFactory.getLogger(WorkersAiChatModel.class);

    public WorkersAiChatModel(Builder builder) {
        super(builder.accountId, builder.modelName, builder.apiToken, builder.httpClientBuilder);
    }

    public WorkersAiChatModel(String accountId, String modelName, String apiToken) {
        super(accountId, modelName, apiToken);
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(WorkersAiChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            WorkersAiChatModelBuilderFactory factory = (WorkersAiChatModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public ChatResponse chat(ChatRequest chatRequest) {
        ChatRequestValidationUtils.validateMessages((List)chatRequest.messages());
        ChatRequestParameters parameters = chatRequest.parameters();
        ChatRequestValidationUtils.validateParameters((ChatRequestParameters)parameters);
        ChatRequestValidationUtils.validate((List)parameters.toolSpecifications());
        ChatRequestValidationUtils.validate((ToolChoice)parameters.toolChoice());
        ChatRequestValidationUtils.validate((ResponseFormat)parameters.responseFormat());
        Response<AiMessage> response = this.generate(chatRequest.messages());
        return ChatResponse.builder().aiMessage((AiMessage)response.content()).metadata(ChatResponseMetadata.builder().tokenUsage(response.tokenUsage()).finishReason(response.finishReason()).build()).build();
    }

    private Response<AiMessage> generate(List<ChatMessage> messages) {
        WorkersAiChatCompletionRequest req = new WorkersAiChatCompletionRequest();
        req.setMessages(messages.stream().map(this::toMessage).collect(Collectors.toList()));
        return new Response((Object)new AiMessage(this.generate(req)), null, FinishReason.STOP);
    }

    private WorkersAiChatCompletionRequest.Message toMessage(ChatMessage message) {
        return new WorkersAiChatCompletionRequest.Message(WorkersAiChatCompletionRequest.MessageRole.valueOf(message.type().name().toLowerCase()), WorkersAiChatModel.toText(message));
    }

    private static String toText(ChatMessage chatMessage) {
        if (chatMessage instanceof SystemMessage) {
            SystemMessage systemMessage = (SystemMessage)chatMessage;
            return systemMessage.text();
        }
        if (chatMessage instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)chatMessage;
            return userMessage.singleText();
        }
        if (chatMessage instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage)chatMessage;
            return aiMessage.text();
        }
        if (chatMessage instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage toolExecutionResultMessage = (ToolExecutionResultMessage)chatMessage;
            if (!toolExecutionResultMessage.hasSingleText()) {
                throw new UnsupportedFeatureException("Workers AI does not support non-text content in tool results. Only text content is supported.");
            }
            return toolExecutionResultMessage.text();
        }
        throw new IllegalArgumentException("Unsupported message type: " + chatMessage.type());
    }

    private String generate(WorkersAiChatCompletionRequest req) {
        WorkersAiChatCompletionResponse response = this.client.generateChat(req, this.accountId, this.modelName);
        if (response == null || response.getResult() == null) {
            throw new IllegalStateException("Response is empty");
        }
        return ((WorkersAiTextCompletionResponse.TextResponse)response.getResult()).getResponse();
    }

    public static class Builder {
        public String accountId;
        public String apiToken;
        public String modelName;
        public HttpClientBuilder httpClientBuilder;

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder apiToken(String apiToken) {
            this.apiToken = apiToken;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public WorkersAiChatModel build() {
            return new WorkersAiChatModel(this);
        }
    }
}

