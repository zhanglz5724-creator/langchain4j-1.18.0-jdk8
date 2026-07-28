/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.ChatRequestValidationUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.StreamingResponseHandler
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.openai.OpenAiStreamingResponseBuilder
 *  dev.langchain4j.model.openai.internal.OpenAiClient
 *  dev.langchain4j.model.openai.internal.OpenAiUtils
 *  dev.langchain4j.model.openai.internal.chat.ChatCompletionChoice
 *  dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest
 *  dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest$Builder
 *  dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse
 *  dev.langchain4j.model.openai.internal.chat.Delta
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
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.localai.spi.LocalAiStreamingChatModelBuilderFactory;
import dev.langchain4j.model.openai.OpenAiStreamingResponseBuilder;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.OpenAiUtils;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionChoice;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse;
import dev.langchain4j.model.openai.internal.chat.Delta;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;

public class LocalAiStreamingChatModel
implements StreamingChatModel {
    private final OpenAiClient client;
    private final String modelName;
    private final Double temperature;
    private final Double topP;
    private final Integer maxTokens;

    @Deprecated
    public LocalAiStreamingChatModel(String baseUrl, String modelName, Double temperature, Double topP, Integer maxTokens, Duration timeout, Boolean logRequests, Boolean logResponses) {
        temperature = temperature == null ? 0.7 : temperature;
        timeout = timeout == null ? Duration.ofSeconds(60L) : timeout;
        this.client = OpenAiClient.builder().baseUrl(ValidationUtils.ensureNotBlank((String)baseUrl, (String)"baseUrl")).connectTimeout(timeout).readTimeout(timeout).logRequests(logRequests).logResponses(logResponses).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.temperature = temperature;
        this.topP = topP;
        this.maxTokens = maxTokens;
    }

    public LocalAiStreamingChatModel(LocalAiStreamingChatModelBuilder builder) {
        this.client = OpenAiClient.builder().baseUrl(ValidationUtils.ensureNotBlank((String)builder.baseUrl, (String)"baseUrl")).connectTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).readTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).logRequests(builder.logRequests).logResponses(builder.logResponses).logger(builder.logger).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.temperature = (Double)Utils.getOrDefault((Object)builder.temperature, (Object)0.7);
        this.topP = builder.topP;
        this.maxTokens = builder.maxTokens;
    }

    public void chat(ChatRequest chatRequest, final StreamingChatResponseHandler handler) {
        ChatRequestParameters parameters = chatRequest.parameters();
        ChatRequestValidationUtils.validateParameters((ChatRequestParameters)parameters);
        ChatRequestValidationUtils.validate((ResponseFormat)parameters.responseFormat());
        StreamingResponseHandler<AiMessage> legacyHandler = new StreamingResponseHandler<AiMessage>(){

            public void onNext(String token) {
                handler.onPartialResponse(token);
            }

            public void onComplete(Response<AiMessage> response) {
                ChatResponse chatResponse = ChatResponse.builder().aiMessage((AiMessage)response.content()).metadata(ChatResponseMetadata.builder().tokenUsage(response.tokenUsage()).finishReason(response.finishReason()).build()).build();
                handler.onCompleteResponse(chatResponse);
            }

            public void onError(Throwable error) {
                handler.onError(error);
            }
        };
        List toolSpecifications = parameters.toolSpecifications();
        if (Utils.isNullOrEmpty((Collection)toolSpecifications)) {
            this.generate(chatRequest.messages(), legacyHandler);
        } else if (parameters.toolChoice() == ToolChoice.REQUIRED) {
            if (toolSpecifications.size() != 1) {
                throw new UnsupportedFeatureException(String.format("%s.%s is currently supported only when there is a single tool", ToolChoice.class.getSimpleName(), ToolChoice.REQUIRED.name()));
            }
            this.generate((List<ChatMessage>)chatRequest.messages(), (ToolSpecification)toolSpecifications.get(0), legacyHandler);
        } else {
            this.generate((List<ChatMessage>)chatRequest.messages(), toolSpecifications, legacyHandler);
        }
    }

    private void generate(List<ChatMessage> messages, StreamingResponseHandler<AiMessage> handler) {
        this.generate(messages, null, null, handler);
    }

    private void generate(List<ChatMessage> messages, List<ToolSpecification> toolSpecifications, StreamingResponseHandler<AiMessage> handler) {
        this.generate(messages, toolSpecifications, null, handler);
    }

    private void generate(List<ChatMessage> messages, ToolSpecification toolSpecification, StreamingResponseHandler<AiMessage> handler) {
        this.generate(messages, Collections.singletonList(toolSpecification), toolSpecification, handler);
    }

    private void generate(List<ChatMessage> messages, List<ToolSpecification> toolSpecifications, ToolSpecification toolThatMustBeExecuted, StreamingResponseHandler<AiMessage> handler) {
        ChatCompletionRequest.Builder requestBuilder = ChatCompletionRequest.builder().stream(Boolean.valueOf(true)).model(this.modelName).messages(OpenAiUtils.toOpenAiMessages(messages)).temperature(this.temperature).topP(this.topP).maxTokens(this.maxTokens);
        if (toolSpecifications != null && !toolSpecifications.isEmpty()) {
            requestBuilder.functions(OpenAiUtils.toFunctions(toolSpecifications));
        }
        if (toolThatMustBeExecuted != null) {
            requestBuilder.functionCall(toolThatMustBeExecuted.name());
        }
        ChatCompletionRequest request = requestBuilder.build();
        OpenAiStreamingResponseBuilder responseBuilder = new OpenAiStreamingResponseBuilder();
        this.client.chatCompletion(request).onPartialResponse(partialResponse -> {
            responseBuilder.append(partialResponse);
            LocalAiStreamingChatModel.handle(partialResponse, handler);
        }).onComplete(() -> {
            ChatResponse chatResponse = responseBuilder.build();
            handler.onComplete(OpenAiUtils.convertResponse((ChatResponse)chatResponse));
        }).onError(arg_0 -> handler.onError(arg_0)).execute();
    }

    private static void handle(ChatCompletionResponse partialResponse, StreamingResponseHandler<AiMessage> handler) {
        List choices = partialResponse.choices();
        if (Utils.isNullOrEmpty((Collection)choices)) {
            return;
        }
        Delta delta = ((ChatCompletionChoice)choices.get(0)).delta();
        String content = delta.content();
        if (content != null) {
            handler.onNext(content);
        }
    }

    public static LocalAiStreamingChatModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(LocalAiStreamingChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            LocalAiStreamingChatModelBuilderFactory factory = (LocalAiStreamingChatModelBuilderFactory)iterator.next();
            return (LocalAiStreamingChatModelBuilder)factory.get();
        }
        return new LocalAiStreamingChatModelBuilder();
    }

    public static class LocalAiStreamingChatModelBuilder {
        private String baseUrl;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Integer maxTokens;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;

        public LocalAiStreamingChatModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public LocalAiStreamingChatModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public LocalAiStreamingChatModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public LocalAiStreamingChatModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public LocalAiStreamingChatModelBuilder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public LocalAiStreamingChatModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public LocalAiStreamingChatModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public LocalAiStreamingChatModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public LocalAiStreamingChatModel build() {
            return new LocalAiStreamingChatModel(this);
        }

        public String toString() {
            return "LocalAiStreamingChatModel.LocalAiStreamingChatModelBuilder(baseUrl=" + this.baseUrl + ", modelName=" + this.modelName + ", temperature=" + this.temperature + ", topP=" + this.topP + ", maxTokens=" + this.maxTokens + ", timeout=" + this.timeout + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

