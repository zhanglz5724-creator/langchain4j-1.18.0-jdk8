/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.watsonx.ai.chat.ChatRequest
 *  com.ibm.watsonx.ai.chat.ChatRequest$Builder
 *  com.ibm.watsonx.ai.chat.ChatResponse
 *  com.ibm.watsonx.ai.chat.ChatResponse$ResultChoice
 *  com.ibm.watsonx.ai.chat.model.AssistantMessage
 *  com.ibm.watsonx.ai.chat.model.ChatParameters
 *  com.ibm.watsonx.ai.chat.model.ChatUsage
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.AiMessage$Builder
 *  dev.langchain4j.exception.ContentFilteredException
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.watsonx;

import com.ibm.watsonx.ai.chat.ChatRequest;
import com.ibm.watsonx.ai.chat.ChatResponse;
import com.ibm.watsonx.ai.chat.model.AssistantMessage;
import com.ibm.watsonx.ai.chat.model.ChatParameters;
import com.ibm.watsonx.ai.chat.model.ChatUsage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.exception.ContentFilteredException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
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

public class WatsonxChatModel
extends WatsonxChat
implements ChatModel {
    private WatsonxChatModel(Builder builder) {
        super(builder);
    }

    public ChatResponse doChat(dev.langchain4j.model.chat.request.ChatRequest chatRequest) {
        this.validate(chatRequest.parameters());
        List toolSpecifications = Utils.getOrDefault((List)chatRequest.parameters().toolSpecifications(), (List)this.defaultRequestParameters.toolSpecifications());
        List messages = chatRequest.messages().stream().map(Converter::toChatMessage).collect(Collectors.toCollection(ArrayList::new));
        List tools = Objects.nonNull(toolSpecifications) && !toolSpecifications.isEmpty() ? toolSpecifications.stream().map(Converter::toTool).toList() : null;
        ChatRequest.Builder watsonxChatRequestBuilder = ChatRequest.builder();
        String deploymentId = null;
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
        com.ibm.watsonx.ai.chat.ChatResponse chatResponse = (com.ibm.watsonx.ai.chat.ChatResponse)WatsonxExceptionMapper.INSTANCE.withExceptionMapper(() -> this.chatProvider.chat(watsonxChatRequest));
        AssistantMessage assistantMessage = chatResponse.toAssistantMessage();
        ChatResponse.ResultChoice choice = (ChatResponse.ResultChoice)chatResponse.choices().get(0);
        ChatUsage usage = chatResponse.usage();
        if (Utils.isNotNullOrBlank((String)assistantMessage.refusal())) {
            throw new ContentFilteredException(assistantMessage.refusal());
        }
        AiMessage.Builder aiMessage = AiMessage.builder();
        if (Objects.nonNull(assistantMessage.toolCalls()) && !assistantMessage.toolCalls().isEmpty()) {
            aiMessage.toolExecutionRequests(assistantMessage.toolCalls().stream().map(Converter::toToolExecutionRequest).toList());
        }
        aiMessage.thinking(assistantMessage.thinking());
        aiMessage.text(assistantMessage.content());
        FinishReason finishReason = Converter.toFinishReason(choice.finishReason());
        TokenUsage tokenUsage = usage != null ? new TokenUsage(usage.promptTokens(), usage.completionTokens(), usage.totalTokens()) : null;
        return ChatResponse.builder().aiMessage(aiMessage.build()).metadata(((WatsonxChatResponseMetadata.Builder)((WatsonxChatResponseMetadata.Builder)((WatsonxChatResponseMetadata.Builder)((WatsonxChatResponseMetadata.Builder)WatsonxChatResponseMetadata.builder().created(chatResponse.created()).modelVersion(chatResponse.modelVersion()).finishReason(finishReason)).id(chatResponse.id())).modelName(chatResponse.modelId())).tokenUsage(tokenUsage)).build()).build();
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public ModelProvider provider() {
        return ModelProvider.WATSONX;
    }

    public Set<Capability> supportedCapabilities() {
        return this.supportedCapabilities;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends WatsonxChat.Builder<Builder> {
        private Builder() {
        }

        public WatsonxChatModel build() {
            return new WatsonxChatModel(this);
        }
    }
}

