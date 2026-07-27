/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.response;

import dev.langchain4j.Experimental;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.PartialResponse;
import dev.langchain4j.model.chat.response.PartialResponseContext;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.PartialThinkingContext;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.PartialToolCallContext;

public interface StreamingChatResponseHandler {
    default public void onPartialResponse(String partialResponse) {
    }

    @Experimental
    default public void onPartialResponse(PartialResponse partialResponse, PartialResponseContext context) {
        this.onPartialResponse(partialResponse.text());
    }

    @Experimental
    default public void onPartialThinking(PartialThinking partialThinking) {
    }

    @Experimental
    default public void onPartialThinking(PartialThinking partialThinking, PartialThinkingContext context) {
        this.onPartialThinking(partialThinking);
    }

    @Experimental
    default public void onPartialToolCall(PartialToolCall partialToolCall) {
    }

    @Experimental
    default public void onPartialToolCall(PartialToolCall partialToolCall, PartialToolCallContext context) {
        this.onPartialToolCall(partialToolCall);
    }

    @Experimental
    default public void onCompleteToolCall(CompleteToolCall completeToolCall) {
    }

    @Experimental
    default public void onUnmappedRawEvent(Object rawEvent) {
    }

    public void onCompleteResponse(ChatResponse var1);

    public void onError(Throwable var1);
}

