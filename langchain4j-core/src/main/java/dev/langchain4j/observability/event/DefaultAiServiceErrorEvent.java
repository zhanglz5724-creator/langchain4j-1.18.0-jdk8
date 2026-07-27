/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.event;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.observability.api.event.AiServiceErrorEvent;
import dev.langchain4j.observability.event.AbstractAiServiceEvent;

public class DefaultAiServiceErrorEvent
extends AbstractAiServiceEvent
implements AiServiceErrorEvent {
    private final Throwable error;

    public DefaultAiServiceErrorEvent(AiServiceErrorEvent.AiServiceErrorEventBuilder builder) {
        super(builder);
        this.error = ValidationUtils.ensureNotNull(builder.getError(), "error");
    }

    @Override
    public Throwable error() {
        return this.error;
    }
}

