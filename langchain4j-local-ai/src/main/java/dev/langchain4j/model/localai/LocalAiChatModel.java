/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.ChatRequestValidationUtils
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.openai.internal.OpenAiClient
 *  dev.langchain4j.model.openai.internal.OpenAiUtils
 *  dev.langchain4j.model.openai.internal.chat.ChatCompletionChoice
 *  dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest
 *  dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest$Builder
 *  dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.localai;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.ChatRequestValidationUtils;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.localai.spi.LocalAiChatModelBuilderFactory;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.OpenAiUtils;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionChoice;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;

public class LocalAiChatModel
implements ChatModel {
    private final OpenAiClient client;
    private final String modelName;
    private final Double temperature;
    private final Double topP;
    private final Integer maxTokens;
    private final Integer maxRetries;

    @Deprecated
    public LocalAiChatModel(String baseUrl, String modelName, Double temperature, Double topP, Integer maxTokens, Duration timeout, Integer maxRetries, Boolean logRequests, Boolean logResponses) {
        temperature = temperature == null ? 0.7 : temperature;
        timeout = timeout == null ? Duration.ofSeconds(60L) : timeout;
        maxRetries = maxRetries == null ? 3 : maxRetries;
        this.client = OpenAiClient.builder().baseUrl(ValidationUtils.ensureNotBlank((String)baseUrl, (String)"baseUrl")).connectTimeout(timeout).readTimeout(timeout).logRequests(logRequests).logResponses(logResponses).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.temperature = temperature;
        this.topP = topP;
        this.maxTokens = maxTokens;
        this.maxRetries = maxRetries;
    }

    public LocalAiChatModel(LocalAiChatModelBuilder builder) {
        this.client = OpenAiClient.builder().baseUrl(ValidationUtils.ensureNotBlank((String)builder.baseUrl, (String)"baseUrl")).connectTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).readTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).logRequests(builder.logRequests).logResponses(builder.logResponses).logger(builder.logger).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.temperature = (Double)Utils.getOrDefault((Object)builder.temperature, (Object)0.7);
        this.topP = builder.topP;
        this.maxTokens = builder.maxTokens;
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)3);
    }

    public ChatResponse chat(ChatRequest chatRequest) {
        Response<AiMessage> response;
        ChatRequestParameters parameters = chatRequest.parameters();
        ChatRequestValidationUtils.validateParameters((ChatRequestParameters)parameters);
        ChatRequestValidationUtils.validate((ResponseFormat)parameters.responseFormat());
        List toolSpecifications = parameters.toolSpecifications();
        if (Utils.isNullOrEmpty((Collection)toolSpecifications)) {
            response = this.generate(chatRequest.messages());
        } else if (parameters.toolChoice() == ToolChoice.REQUIRED) {
            if (toolSpecifications.size() != 1) {
                throw new UnsupportedFeatureException(String.format("%s.%s is currently supported only when there is a single tool", ToolChoice.class.getSimpleName(), ToolChoice.REQUIRED.name()));
            }
            response = this.generate((List<ChatMessage>)chatRequest.messages(), (ToolSpecification)toolSpecifications.get(0));
        } else {
            response = this.generate((List<ChatMessage>)chatRequest.messages(), toolSpecifications);
        }
        return ChatResponse.builder().aiMessage((AiMessage)response.content()).metadata(ChatResponseMetadata.builder().tokenUsage(response.tokenUsage()).finishReason(response.finishReason()).build()).build();
    }

    private Response<AiMessage> generate(List<ChatMessage> messages) {
        return this.generate(messages, null, null);
    }

    private Response<AiMessage> generate(List<ChatMessage> messages, List<ToolSpecification> toolSpecifications) {
        return this.generate(messages, toolSpecifications, null);
    }

    private Response<AiMessage> generate(List<ChatMessage> messages, ToolSpecification toolSpecification) {
        return this.generate(messages, Collections.singletonList(toolSpecification), toolSpecification);
    }

    private Response<AiMessage> generate(List<ChatMessage> messages, List<ToolSpecification> toolSpecifications, ToolSpecification toolThatMustBeExecuted) {
        ChatCompletionRequest.Builder requestBuilder = ChatCompletionRequest.builder().model(this.modelName).messages(OpenAiUtils.toOpenAiMessages(messages)).temperature(this.temperature).topP(this.topP).maxTokens(this.maxTokens);
        if (toolSpecifications != null && !toolSpecifications.isEmpty()) {
            requestBuilder.functions(OpenAiUtils.toFunctions(toolSpecifications));
        }
        if (toolThatMustBeExecuted != null) {
            requestBuilder.functionCall(toolThatMustBeExecuted.name());
        }
        ChatCompletionRequest request = requestBuilder.build();
        ChatCompletionResponse response = (ChatCompletionResponse)RetryUtils.withRetryMappingExceptions(() -> (ChatCompletionResponse)this.client.chatCompletion(request).execute(), (int)this.maxRetries);
        return Response.from(OpenAiUtils.aiMessageFrom((ChatCompletionResponse)response), null, (FinishReason)OpenAiUtils.finishReasonFrom((String)((ChatCompletionChoice)response.choices().get(0)).finishReason()));
    }

    public static LocalAiChatModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(LocalAiChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            LocalAiChatModelBuilderFactory factory = (LocalAiChatModelBuilderFactory)iterator.next();
            return (LocalAiChatModelBuilder)factory.get();
        }
        return new LocalAiChatModelBuilder();
    }

    public static class LocalAiChatModelBuilder {
        private String baseUrl;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Integer maxTokens;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;

        public LocalAiChatModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public LocalAiChatModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public LocalAiChatModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public LocalAiChatModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public LocalAiChatModelBuilder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public LocalAiChatModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public LocalAiChatModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public LocalAiChatModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public LocalAiChatModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public LocalAiChatModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public LocalAiChatModel build() {
            return new LocalAiChatModel(this);
        }

        public String toString() {
            return "LocalAiChatModel.LocalAiChatModelBuilder(baseUrl=" + this.baseUrl + ", modelName=" + this.modelName + ", temperature=" + this.temperature + ", topP=" + this.topP + ", maxTokens=" + this.maxTokens + ", timeout=" + this.timeout + ", maxRetries=" + this.maxRetries + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

