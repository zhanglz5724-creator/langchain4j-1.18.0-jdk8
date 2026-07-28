/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.service.TokenStream
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.internal.DelayedResponse;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.TokenStream;
import java.util.concurrent.CompletableFuture;

public class StreamingResponse
implements DelayedResponse<String> {
    private final CompletableFuture<ChatResponse> futureResponse = new CompletableFuture();

    public StreamingResponse(TokenStream tokenStream) {
        tokenStream.onCompleteResponse(this.futureResponse::complete).onError(this.futureResponse::completeExceptionally).start();
    }

    @Override
    public boolean isDone() {
        return this.futureResponse.isDone();
    }

    @Override
    public String blockingGet() {
        return DelayedResponse.join(this.futureResponse).aiMessage().text();
    }

    public String toString() {
        return this.result().toString();
    }
}

