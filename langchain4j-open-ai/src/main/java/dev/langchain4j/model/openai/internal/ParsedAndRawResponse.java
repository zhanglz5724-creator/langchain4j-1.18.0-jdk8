/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.http.client.sse.ServerSentEvent
 *  dev.langchain4j.model.chat.response.StreamingHandle
 */
package dev.langchain4j.model.openai.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.model.chat.response.StreamingHandle;

@Internal
public class ParsedAndRawResponse<R> {
    private final R parsedResponse;
    private final SuccessfulHttpResponse rawHttpResponse;
    private final ServerSentEvent rawServerSentEvent;
    private final StreamingHandle streamingHandle;

    protected ParsedAndRawResponse(Builder<R> builder) {
        this.parsedResponse = ((Builder)builder).parsedResponse;
        this.rawHttpResponse = ((Builder)builder).rawHttpResponse;
        this.rawServerSentEvent = ((Builder)builder).rawServerSentEvent;
        this.streamingHandle = ((Builder)builder).streamingHandle;
    }

    protected ParsedAndRawResponse(R parsedResponse, SuccessfulHttpResponse rawHttpResponse) {
        this.parsedResponse = parsedResponse;
        this.rawHttpResponse = rawHttpResponse;
        this.rawServerSentEvent = null;
        this.streamingHandle = null;
    }

    protected ParsedAndRawResponse(R parsedResponse, ServerSentEvent rawServerSentEvent) {
        this.parsedResponse = parsedResponse;
        this.rawHttpResponse = null;
        this.rawServerSentEvent = rawServerSentEvent;
        this.streamingHandle = null;
    }

    protected ParsedAndRawResponse(R parsedResponse, SuccessfulHttpResponse rawHttpResponse, ServerSentEvent rawServerSentEvent) {
        this.parsedResponse = parsedResponse;
        this.rawHttpResponse = rawHttpResponse;
        this.rawServerSentEvent = rawServerSentEvent;
        this.streamingHandle = null;
    }

    public R parsedResponse() {
        return this.parsedResponse;
    }

    public SuccessfulHttpResponse rawHttpResponse() {
        return this.rawHttpResponse;
    }

    public ServerSentEvent rawServerSentEvent() {
        return this.rawServerSentEvent;
    }

    public StreamingHandle streamingHandle() {
        return this.streamingHandle;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder<R> {
        private R parsedResponse;
        private SuccessfulHttpResponse rawHttpResponse;
        private ServerSentEvent rawServerSentEvent;
        private StreamingHandle streamingHandle;

        public Builder<R> parsedResponse(R parsedResponse) {
            this.parsedResponse = parsedResponse;
            return this;
        }

        public Builder<R> rawHttpResponse(SuccessfulHttpResponse rawHttpResponse) {
            this.rawHttpResponse = rawHttpResponse;
            return this;
        }

        public Builder<R> rawServerSentEvent(ServerSentEvent rawServerSentEvent) {
            this.rawServerSentEvent = rawServerSentEvent;
            return this;
        }

        public Builder<R> streamingHandle(StreamingHandle streamingHandle) {
            this.streamingHandle = streamingHandle;
            return this;
        }

        public ParsedAndRawResponse<R> build() {
            return new ParsedAndRawResponse(this);
        }
    }
}

