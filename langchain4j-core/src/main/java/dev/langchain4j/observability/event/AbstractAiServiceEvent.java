/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.event;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.observability.api.event.AiServiceEvent;

public abstract class AbstractAiServiceEvent
implements AiServiceEvent {
    private final InvocationContext invocationContext;

    protected AbstractAiServiceEvent(AiServiceEvent.Builder<?> builder) {
        ValidationUtils.ensureNotNull(builder, "builder");
        this.invocationContext = ValidationUtils.ensureNotNull(builder.invocationContext(), "invocationContext");
    }

    @Override
    public InvocationContext invocationContext() {
        return this.invocationContext;
    }
}

