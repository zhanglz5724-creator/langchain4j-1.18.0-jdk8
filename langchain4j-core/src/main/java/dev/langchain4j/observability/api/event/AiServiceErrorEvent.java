/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.event;

import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.event.DefaultAiServiceErrorEvent;

public interface AiServiceErrorEvent
extends AiServiceEvent {
    public Throwable error();

    default public Class<AiServiceErrorEvent> eventClass() {
        return AiServiceErrorEvent.class;
    }

    default public AiServiceErrorEventBuilder toBuilder() {
        return new AiServiceErrorEventBuilder(this);
    }

    public static AiServiceErrorEventBuilder builder() {
        return new AiServiceErrorEventBuilder();
    }

    public static class AiServiceErrorEventBuilder
    extends AiServiceEvent.Builder<AiServiceErrorEvent> {
        private Throwable error;

        protected AiServiceErrorEventBuilder() {
        }

        protected AiServiceErrorEventBuilder(AiServiceErrorEvent src) {
            super(src);
            this.error(src.error());
        }

        public AiServiceErrorEventBuilder invocationContext(InvocationContext invocationContext) {
            return (AiServiceErrorEventBuilder)super.invocationContext(invocationContext);
        }

        public AiServiceErrorEventBuilder error(Throwable error) {
            this.error = error;
            return this;
        }

        @Override
        public AiServiceErrorEvent build() {
            return new DefaultAiServiceErrorEvent(this);
        }

        public Throwable getError() {
            return this.error;
        }
    }
}

