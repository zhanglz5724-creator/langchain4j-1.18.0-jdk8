/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.observability.api.event;

import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.event.DefaultAiServiceCompletedEvent;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public interface AiServiceCompletedEvent
extends AiServiceEvent {
    public Optional<Object> result();

    default public Class<AiServiceCompletedEvent> eventClass() {
        return AiServiceCompletedEvent.class;
    }

    public static AiServiceCompletedEventBuilder builder() {
        return new AiServiceCompletedEventBuilder();
    }

    default public AiServiceCompletedEventBuilder toBuilder() {
        return new AiServiceCompletedEventBuilder(this);
    }

    public static class AiServiceCompletedEventBuilder
    extends AiServiceEvent.Builder<AiServiceCompletedEvent> {
        private @Nullable Object result;

        protected AiServiceCompletedEventBuilder() {
        }

        protected AiServiceCompletedEventBuilder(AiServiceCompletedEvent src) {
            super(src);
            this.result(src.result());
        }

        public AiServiceCompletedEventBuilder invocationContext(InvocationContext invocationContext) {
            return (AiServiceCompletedEventBuilder)super.invocationContext(invocationContext);
        }

        public AiServiceCompletedEventBuilder result(@Nullable Object result) {
            this.result = result;
            return this;
        }

        @Override
        public AiServiceCompletedEvent build() {
            return new DefaultAiServiceCompletedEvent(this);
        }

        public @Nullable Object getResult() {
            return this.result;
        }
    }
}

