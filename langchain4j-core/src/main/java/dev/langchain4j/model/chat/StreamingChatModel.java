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
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.PartialResponse;
import dev.langchain4j.model.chat.response.PartialResponseContext;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.PartialThinkingContext;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.PartialToolCallContext;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface StreamingChatModel {
    default public void chat(ChatRequest request, StreamingChatResponseHandler handler) {
        this.chat(request, ChatRequestOptions.EMPTY, handler);
    }

    default public void chat(ChatRequest request, ChatRequestOptions options, final StreamingChatResponseHandler handler) {
        final ChatRequest finalChatRequest = ChatRequest.builder().messages(request.messages()).parameters(this.defaultRequestParameters().overrideWith(request.parameters())).build();
        ChatRequestOptions effectiveOptions = Utils.getOrDefault(options, ChatRequestOptions.EMPTY);
        final List<ChatModelListener> listeners = this.listeners();
        final ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>(effectiveOptions.listenerAttributes());
        StreamingChatResponseHandler observingHandler = new StreamingChatResponseHandler(){

            @Override
            public void onPartialResponse(String partialResponse) {
                handler.onPartialResponse(partialResponse);
            }

            @Override
            public void onPartialResponse(PartialResponse partialResponse, PartialResponseContext context) {
                handler.onPartialResponse(partialResponse, context);
            }

            @Override
            public void onPartialThinking(PartialThinking partialThinking) {
                handler.onPartialThinking(partialThinking);
            }

            @Override
            public void onPartialThinking(PartialThinking partialThinking, PartialThinkingContext context) {
                handler.onPartialThinking(partialThinking, context);
            }

            @Override
            public void onPartialToolCall(PartialToolCall partialToolCall) {
                handler.onPartialToolCall(partialToolCall);
            }

            @Override
            public void onPartialToolCall(PartialToolCall partialToolCall, PartialToolCallContext context) {
                handler.onPartialToolCall(partialToolCall, context);
            }

            @Override
            public void onCompleteToolCall(CompleteToolCall completeToolCall) {
                handler.onCompleteToolCall(completeToolCall);
            }

            @Override
            public void onUnmappedRawEvent(Object rawEvent) {
                handler.onUnmappedRawEvent(rawEvent);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                ChatModelListenerUtils.onResponse(completeResponse, finalChatRequest, StreamingChatModel.this.provider(), attributes, listeners);
                handler.onCompleteResponse(completeResponse);
            }

            @Override
            public void onError(Throwable error) {
                ChatModelListenerUtils.onError(error, finalChatRequest, StreamingChatModel.this.provider(), attributes, listeners);
                handler.onError(error);
            }
        };
        ChatModelListenerUtils.onRequest(finalChatRequest, this.provider(), attributes, listeners);
        this.doChat(finalChatRequest, observingHandler);
    }

    default public void doChat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
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

    default public void chat(String userMessage, StreamingChatResponseHandler handler) {
        ChatRequest chatRequest = ChatRequest.builder().messages(UserMessage.from(userMessage)).build();
        this.chat(chatRequest, handler);
    }

    default public void chat(List<ChatMessage> messages, StreamingChatResponseHandler handler) {
        ChatRequest chatRequest = ChatRequest.builder().messages(messages).build();
        this.chat(chatRequest, handler);
    }

    default public Set<Capability> supportedCapabilities() {
        return Collections.emptySet();
    }
}

