/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.observability.event;

import dev.langchain4j.observability.api.event.AiServiceCompletedEvent;
import dev.langchain4j.observability.event.AbstractAiServiceEvent;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public class DefaultAiServiceCompletedEvent
extends AbstractAiServiceEvent
implements AiServiceCompletedEvent {
    private final @Nullable Object result;

    public DefaultAiServiceCompletedEvent(AiServiceCompletedEvent.AiServiceCompletedEventBuilder builder) {
        super(builder);
        this.result = builder.getResult();
    }

    @Override
    public Optional<Object> result() {
        return Optional.ofNullable(this.result);
    }
}

