/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.event;

import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.event.DefaultAiServiceResponseReceivedEvent;

public interface AiServiceResponseReceivedEvent
extends AiServiceEvent {
    public ChatRequest request();

    public ChatResponse response();

    default public Class<AiServiceResponseReceivedEvent> eventClass() {
        return AiServiceResponseReceivedEvent.class;
    }

    default public AiServiceResponseReceivedEventBuilder toBuilder() {
        return new AiServiceResponseReceivedEventBuilder(this);
    }

    public static AiServiceResponseReceivedEventBuilder builder() {
        return new AiServiceResponseReceivedEventBuilder();
    }

    public static class AiServiceResponseReceivedEventBuilder
    extends AiServiceEvent.Builder<AiServiceResponseReceivedEvent> {
        private ChatResponse response;
        private ChatRequest request;

        protected AiServiceResponseReceivedEventBuilder() {
        }

        protected AiServiceResponseReceivedEventBuilder(AiServiceResponseReceivedEvent src) {
            super(src);
            this.response(src.response());
            this.request(src.request());
        }

        public ChatResponse response() {
            return this.response;
        }

        public ChatRequest request() {
            return this.request;
        }

        public AiServiceResponseReceivedEventBuilder invocationContext(InvocationContext invocationContext) {
            return (AiServiceResponseReceivedEventBuilder)super.invocationContext(invocationContext);
        }

        public AiServiceResponseReceivedEventBuilder request(ChatRequest request) {
            this.request = request;
            return this;
        }

        public AiServiceResponseReceivedEventBuilder response(ChatResponse response) {
            this.response = response;
            return this;
        }

        @Override
        public AiServiceResponseReceivedEvent build() {
            return new DefaultAiServiceResponseReceivedEvent(this);
        }
    }
}

