/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.event;

import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.event.DefaultAiServiceRequestIssuedEvent;

public interface AiServiceRequestIssuedEvent
extends AiServiceEvent {
    public ChatRequest request();

    default public Class<AiServiceRequestIssuedEvent> eventClass() {
        return AiServiceRequestIssuedEvent.class;
    }

    default public AiServiceRequestIssuedEventBuilder toBuilder() {
        return new AiServiceRequestIssuedEventBuilder(this);
    }

    public static AiServiceRequestIssuedEventBuilder builder() {
        return new AiServiceRequestIssuedEventBuilder();
    }

    public static class AiServiceRequestIssuedEventBuilder
    extends AiServiceEvent.Builder<AiServiceRequestIssuedEvent> {
        private ChatRequest request;

        protected AiServiceRequestIssuedEventBuilder() {
        }

        protected AiServiceRequestIssuedEventBuilder(AiServiceRequestIssuedEvent src) {
            super(src);
            this.request(src.request());
        }

        public ChatRequest request() {
            return this.request;
        }

        public AiServiceRequestIssuedEventBuilder invocationContext(InvocationContext invocationContext) {
            return (AiServiceRequestIssuedEventBuilder)super.invocationContext(invocationContext);
        }

        public AiServiceRequestIssuedEventBuilder request(ChatRequest request) {
            this.request = request;
            return this;
        }

        @Override
        public AiServiceRequestIssuedEvent build() {
            return new DefaultAiServiceRequestIssuedEvent(this);
        }
    }
}

