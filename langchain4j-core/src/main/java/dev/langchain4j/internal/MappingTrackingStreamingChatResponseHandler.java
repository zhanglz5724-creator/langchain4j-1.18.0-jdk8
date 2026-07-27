/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.PartialResponse;
import dev.langchain4j.model.chat.response.PartialResponseContext;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.PartialThinkingContext;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.PartialToolCallContext;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;

@Internal
public class MappingTrackingStreamingChatResponseHandler
implements StreamingChatResponseHandler {
    private final StreamingChatResponseHandler delegate;
    private boolean mapped;

    public MappingTrackingStreamingChatResponseHandler(StreamingChatResponseHandler delegate) {
        this.delegate = delegate;
    }

    public void resetMappingTracking() {
        this.mapped = false;
    }

    public boolean wasMapped() {
        return this.mapped;
    }

    @Override
    public void onPartialResponse(String partialResponse) {
        this.mapped = true;
        this.delegate.onPartialResponse(partialResponse);
    }

    @Override
    public void onPartialResponse(PartialResponse partialResponse, PartialResponseContext context) {
        this.mapped = true;
        this.delegate.onPartialResponse(partialResponse, context);
    }

    @Override
    public void onPartialThinking(PartialThinking partialThinking) {
        this.mapped = true;
        this.delegate.onPartialThinking(partialThinking);
    }

    @Override
    public void onPartialThinking(PartialThinking partialThinking, PartialThinkingContext context) {
        this.mapped = true;
        this.delegate.onPartialThinking(partialThinking, context);
    }

    @Override
    public void onPartialToolCall(PartialToolCall partialToolCall) {
        this.mapped = true;
        this.delegate.onPartialToolCall(partialToolCall);
    }

    @Override
    public void onPartialToolCall(PartialToolCall partialToolCall, PartialToolCallContext context) {
        this.mapped = true;
        this.delegate.onPartialToolCall(partialToolCall, context);
    }

    @Override
    public void onCompleteToolCall(CompleteToolCall completeToolCall) {
        this.mapped = true;
        this.delegate.onCompleteToolCall(completeToolCall);
    }

    @Override
    public void onUnmappedRawEvent(Object rawEvent) {
        this.delegate.onUnmappedRawEvent(rawEvent);
    }

    @Override
    public void onCompleteResponse(ChatResponse completeResponse) {
        this.mapped = true;
        this.delegate.onCompleteResponse(completeResponse);
    }

    @Override
    public void onError(Throwable error) {
        this.mapped = true;
        this.delegate.onError(error);
    }
}

