/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.PartialResponse
 *  dev.langchain4j.model.chat.response.PartialResponseContext
 *  dev.langchain4j.model.chat.response.PartialThinking
 *  dev.langchain4j.model.chat.response.PartialThinkingContext
 *  dev.langchain4j.model.chat.response.PartialToolCall
 *  dev.langchain4j.model.chat.response.PartialToolCallContext
 *  dev.langchain4j.rag.content.Content
 */
package dev.langchain4j.service;

import dev.langchain4j.Experimental;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialResponse;
import dev.langchain4j.model.chat.response.PartialResponseContext;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.PartialThinkingContext;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.PartialToolCallContext;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.tool.BeforeToolExecution;
import dev.langchain4j.service.tool.ToolExecution;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface TokenStream {
    public TokenStream onPartialResponse(Consumer<String> var1);

    @Experimental
    default public TokenStream onPartialResponseWithContext(BiConsumer<PartialResponse, PartialResponseContext> handler) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Experimental
    default public TokenStream onPartialThinking(Consumer<PartialThinking> partialThinkingHandler) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Experimental
    default public TokenStream onPartialThinkingWithContext(BiConsumer<PartialThinking, PartialThinkingContext> handler) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Experimental
    default public TokenStream onPartialToolCall(Consumer<PartialToolCall> partialToolCallHandler) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Experimental
    default public TokenStream onPartialToolCallWithContext(BiConsumer<PartialToolCall, PartialToolCallContext> handler) {
        throw new UnsupportedOperationException("not implemented");
    }

    public TokenStream onRetrieved(Consumer<List<Content>> var1);

    default public TokenStream onIntermediateResponse(Consumer<ChatResponse> intermediateResponseHandler) {
        throw new UnsupportedOperationException("Consuming intermediate responses is not supported by this implementation of TokenStream: " + this.getClass().getName());
    }

    default public TokenStream beforeToolExecution(Consumer<BeforeToolExecution> beforeToolExecutionHandler) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Experimental
    default public TokenStream onUnmappedRawEvent(Consumer<Object> rawEventHandler) {
        throw new UnsupportedOperationException("not implemented");
    }

    public TokenStream onToolExecuted(Consumer<ToolExecution> var1);

    public TokenStream onCompleteResponse(Consumer<ChatResponse> var1);

    public TokenStream onError(Consumer<Throwable> var1);

    public TokenStream ignoreErrors();

    public void start();
}

