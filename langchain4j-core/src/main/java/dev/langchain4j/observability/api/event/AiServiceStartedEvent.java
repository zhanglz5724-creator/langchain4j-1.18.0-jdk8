/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.observability.api.event;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.event.DefaultAiServiceStartedEvent;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public interface AiServiceStartedEvent
extends AiServiceEvent {
    public Optional<SystemMessage> systemMessage();

    public UserMessage userMessage();

    public static AiServiceStartedEventBuilder builder() {
        return new AiServiceStartedEventBuilder();
    }

    default public Class<AiServiceStartedEvent> eventClass() {
        return AiServiceStartedEvent.class;
    }

    default public AiServiceStartedEventBuilder toBuilder() {
        return new AiServiceStartedEventBuilder(this);
    }

    public static class AiServiceStartedEventBuilder
    extends AiServiceEvent.Builder<AiServiceStartedEvent> {
        private @Nullable SystemMessage systemMessage;
        private UserMessage userMessage;

        protected AiServiceStartedEventBuilder() {
        }

        protected AiServiceStartedEventBuilder(AiServiceStartedEvent src) {
            super(src);
            this.systemMessage((SystemMessage)src.systemMessage().orElse(null));
            this.userMessage(src.userMessage());
        }

        public AiServiceStartedEventBuilder invocationContext(InvocationContext invocationContext) {
            return (AiServiceStartedEventBuilder)super.invocationContext(invocationContext);
        }

        public AiServiceStartedEventBuilder systemMessage(@Nullable SystemMessage systemMessage) {
            this.systemMessage = systemMessage;
            return this;
        }

        public AiServiceStartedEventBuilder systemMessage(Optional<SystemMessage> systemMessage) {
            return this.systemMessage((SystemMessage)systemMessage.orElse(null));
        }

        public AiServiceStartedEventBuilder userMessage(UserMessage userMessage) {
            this.userMessage = userMessage;
            return this;
        }

        @Override
        public AiServiceStartedEvent build() {
            return new DefaultAiServiceStartedEvent(this);
        }

        public @Nullable SystemMessage systemMessage() {
            return this.systemMessage;
        }

        public UserMessage userMessage() {
            return this.userMessage;
        }
    }
}

