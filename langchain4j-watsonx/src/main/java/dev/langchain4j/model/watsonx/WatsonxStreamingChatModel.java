/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.watsonx.ai.chat.ChatHandler
 *  com.ibm.watsonx.ai.chat.ChatRequest
 *  com.ibm.watsonx.ai.chat.ChatRequest$Builder
 *  com.ibm.watsonx.ai.chat.ChatResponse
 *  com.ibm.watsonx.ai.chat.ChatResponse$ResultChoice
 *  com.ibm.watsonx.ai.chat.model.AssistantMessage
 *  com.ibm.watsonx.ai.chat.model.ChatParameters
 *  com.ibm.watsonx.ai.chat.model.ChatUsage
 *  com.ibm.watsonx.ai.chat.model.CompletedToolCall
 *  com.ibm.watsonx.ai.chat.model.PartialChatResponse
 *  com.ibm.watsonx.ai.chat.model.PartialToolCall
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.AiMessage$Builder
 *  dev.langchain4j.exception.ContentFilteredException
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.PartialThinking
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.watsonx;

import com.ibm.watsonx.ai.chat.ChatHandler;
import com.ibm.watsonx.ai.chat.ChatRequest;
import com.ibm.watsonx.ai.chat.ChatResponse;
import com.ibm.watsonx.ai.chat.model.AssistantMessage;
import com.ibm.watsonx.ai.chat.model.ChatParameters;
import com.ibm.watsonx.ai.chat.model.ChatUsage;
import com.ibm.watsonx.ai.chat.model.CompletedToolCall;
import com.ibm.watsonx.ai.chat.model.PartialChatResponse;
import com.ibm.watsonx.ai.chat.model.PartialToolCall;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.exception.ContentFilteredException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.watsonx.Converter;
import dev.langchain4j.model.watsonx.WatsonxChat;
import dev.langchain4j.model.watsonx.WatsonxChatRequestParameters;
import dev.langchain4j.model.watsonx.WatsonxChatResponseMetadata;
import dev.langchain4j.model.watsonx.WatsonxExceptionMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WatsonxStreamingChatModel
extends WatsonxChat
implements StreamingChatModel {
    private WatsonxStreamingChatModel(Builder builder) {
        super(builder);
    }

    public void doChat(dev.langchain4j.model.chat.request.ChatRequest chatRequest, final StreamingChatResponseHandler handler) {
        this.validate(chatRequest.parameters());
        List toolSpecifications = Utils.getOrDefault((List)chatRequest.parameters().toolSpecifications(), (List)this.defaultRequestParameters.toolSpecifications());
        List messages = chatRequest.messages().stream().map(Converter::toChatMessage).collect(Collectors.toCollection(ArrayList::new));
        List tools = Objects.nonNull(toolSpecifications) && toolSpecifications.size() > 0 ? toolSpecifications.stream().map(Converter::toTool).toList() : null;
        String deploymentId = null;
        ChatRequest.Builder watsonxChatRequestBuilder = ChatRequest.builder();
        ChatRequestParameters chatRequestParameters = chatRequest.parameters();
        if (chatRequestParameters instanceof WatsonxChatRequestParameters) {
            WatsonxChatRequestParameters wcrp = (WatsonxChatRequestParameters)chatRequestParameters;
            deploymentId = wcrp.deploymentId();
            if (Objects.nonNull(wcrp.thinking())) {
                watsonxChatRequestBuilder.thinking(wcrp.thinking());
            }
        }
        ChatParameters parameters = Converter.toChatParameters(chatRequest.parameters());
        ChatRequest watsonxChatRequest = watsonxChatRequestBuilder.messages(messages).tools(tools).parameters(parameters).deploymentId(deploymentId).build();
        this.chatProvider.chatStreaming(watsonxChatRequest, new ChatHandler(){

            public void onCompleteResponse(com.ibm.watsonx.ai.chat.ChatResponse completeResponse) {
                ChatResponse.ResultChoice choice = (ChatResponse.ResultChoice)completeResponse.choices().get(0);
                FinishReason finishReason = Converter.toFinishReason(choice.finishReason());
                ChatUsage completeUsage = completeResponse.usage();
                TokenUsage tokenUsage = completeUsage != null ? new TokenUsage(completeUsage.promptTokens(), completeUsage.completionTokens(), completeUsage.totalTokens()) : null;
                AssistantMessage assistantMessage = completeResponse.toAssistantMessage();
                AiMessage.Builder aiMessage = AiMessage.builder();
                if (Utils.isNotNullOrBlank((String)assistantMessage.refusal())) {
                    handler.onError((Throwable)new ContentFilteredException(assistantMessage.refusal()));
                }
                if (Objects.nonNull(assistantMessage.toolCalls())) {
                    aiMessage.toolExecutionRequests(assistantMessage.toolCalls().stream().map(Converter::toToolExecutionRequest).toList());
                }
                aiMessage.thinking(assistantMessage.thinking());
                aiMessage.text(assistantMessage.content());
                ChatResponse chatResponse = ChatResponse.builder().aiMessage(aiMessage.build()).metadata(((WatsonxChatResponseMetadata.Builder)((WatsonxChatResponseMetadata.Builder)((WatsonxChatResponseMetadata.Builder)((WatsonxChatResponseMetadata.Builder)WatsonxChatResponseMetadata.builder().created(completeResponse.created()).modelVersion(completeResponse.modelVersion()).finishReason(finishReason)).id(completeResponse.id())).modelName(completeResponse.modelId())).tokenUsage(tokenUsage)).build()).build();
                handler.onCompleteResponse(chatResponse);
            }

            public void onError(Throwable error) {
                handler.onError((Throwable)WatsonxExceptionMapper.INSTANCE.mapException(error));
            }

            public void onPartialResponse(String partialResponse, PartialChatResponse partialChatResponse) {
                handler.onPartialResponse(partialResponse);
            }

            public void onCompleteToolCall(CompletedToolCall completedToolCall) {
                handler.onCompleteToolCall(Converter.toCompleteToolCall(completedToolCall.toolCall()));
            }

            public void onPartialThinking(String partialThinking, PartialChatResponse partialChatResponse) {
                handler.onPartialThinking(new PartialThinking(partialThinking));
            }

            public void onPartialToolCall(PartialToolCall partialToolCall) {
                handler.onPartialToolCall(Converter.toPartialToolCall(partialToolCall));
            }
        });
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public Set<Capability> supportedCapabilities() {
        return this.supportedCapabilities;
    }

    public ModelProvider provider() {
        return ModelProvider.WATSONX;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends WatsonxChat.Builder<Builder> {
        private Builder() {
        }

        public WatsonxStreamingChatModel build() {
            return new WatsonxStreamingChatModel(this);
        }
    }
}

