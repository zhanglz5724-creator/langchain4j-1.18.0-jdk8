/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModelListenerUtils;
import dev.langchain4j.model.chat.ChatRequestOptions;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface ChatModel {
    default public ChatResponse chat(ChatRequest chatRequest) {
        return this.chat(chatRequest, ChatRequestOptions.EMPTY);
    }

    default public ChatResponse chat(ChatRequest chatRequest, ChatRequestOptions options) {
        ChatRequestOptions effectiveOptions = Utils.getOrDefault(options, ChatRequestOptions.EMPTY);
        ChatRequest finalChatRequest = ChatRequest.builder().messages(chatRequest.messages()).parameters(this.defaultRequestParameters().overrideWith(chatRequest.parameters())).build();
        List<ChatModelListener> listeners = this.listeners();
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>(effectiveOptions.listenerAttributes());
        ChatModelListenerUtils.onRequest(finalChatRequest, this.provider(), attributes, listeners);
        try {
            ChatResponse chatResponse = this.doChat(finalChatRequest);
            ChatModelListenerUtils.onResponse(chatResponse, finalChatRequest, this.provider(), attributes, listeners);
            return chatResponse;
        }
        catch (Exception error) {
            ChatModelListenerUtils.onError(error, finalChatRequest, this.provider(), attributes, listeners);
            throw error;
        }
    }

    default public ChatResponse doChat(ChatRequest chatRequest) {
        throw new RuntimeException("Not implemented");
    }

    default public ChatRequestParameters defaultRequestParameters() {
        return DefaultChatRequestParameters.EMPTY;
    }

    default public List<ChatModelListener> listeners() {
        return Collections.emptyList();
    }

    default public ModelProvider provider() {
        return ModelProvider.OTHER;
    }

    default public String chat(String userMessage) {
        ChatRequest chatRequest = ChatRequest.builder().messages(UserMessage.from(userMessage)).build();
        ChatResponse chatResponse = this.chat(chatRequest);
        return chatResponse.aiMessage().text();
    }

    default public ChatResponse chat(ChatMessage ... messages) {
        ChatRequest chatRequest = ChatRequest.builder().messages(messages).build();
        return this.chat(chatRequest);
    }

    default public ChatResponse chat(List<ChatMessage> messages) {
        ChatRequest chatRequest = ChatRequest.builder().messages(messages).build();
        return this.chat(chatRequest);
    }

    default public Set<Capability> supportedCapabilities() {
        return Collections.emptySet();
    }
}

