/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.observability.event;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.observability.api.event.AiServiceStartedEvent;
import dev.langchain4j.observability.event.AbstractAiServiceEvent;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public class DefaultAiServiceStartedEvent
extends AbstractAiServiceEvent
implements AiServiceStartedEvent {
    private final @Nullable SystemMessage systemMessage;
    private final UserMessage userMessage;

    public DefaultAiServiceStartedEvent(AiServiceStartedEvent.AiServiceStartedEventBuilder builder) {
        super(builder);
        this.systemMessage = builder.systemMessage();
        this.userMessage = ValidationUtils.ensureNotNull(builder.userMessage(), "userMessage");
    }

    @Override
    public Optional<SystemMessage> systemMessage() {
        return Optional.ofNullable(this.systemMessage);
    }

    @Override
    public UserMessage userMessage() {
        return this.userMessage;
    }
}

