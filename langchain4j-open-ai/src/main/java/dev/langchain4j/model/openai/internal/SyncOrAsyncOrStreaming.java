/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.sse.ServerSentEvent
 */
package dev.langchain4j.model.openai.internal;

import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.model.openai.internal.ParsedAndRawResponse;
import dev.langchain4j.model.openai.internal.StreamingResponseHandling;
import dev.langchain4j.model.openai.internal.SyncOrAsync;
import java.util.function.Consumer;

public interface SyncOrAsyncOrStreaming<ResponseContent>
extends SyncOrAsync<ResponseContent> {
    public StreamingResponseHandling onPartialResponse(Consumer<ResponseContent> var1);

    default public StreamingResponseHandling onRawPartialResponse(Consumer<ParsedAndRawResponse<ResponseContent>> handler) {
        ServerSentEvent rawEvent = null;
        return this.onPartialResponse(parsedResponse -> handler.accept(new ParsedAndRawResponse<ResponseContent>(parsedResponse, rawEvent)));
    }
}

