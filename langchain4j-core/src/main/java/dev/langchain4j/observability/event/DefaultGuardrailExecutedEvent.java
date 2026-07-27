/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.event;

import dev.langchain4j.guardrail.Guardrail;
import dev.langchain4j.guardrail.GuardrailRequest;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.observability.api.event.GuardrailExecutedEvent;
import dev.langchain4j.observability.event.AbstractAiServiceEvent;
import java.time.Duration;

public abstract class DefaultGuardrailExecutedEvent<P extends GuardrailRequest<P>, R extends GuardrailResult<R>, G extends Guardrail<P, R>, E extends GuardrailExecutedEvent<P, R, G>>
extends AbstractAiServiceEvent
implements GuardrailExecutedEvent<P, R, G> {
    private final P request;
    private final R result;
    private final Class<G> guardrailClass;
    private final String guardrailName;
    private final Duration duration;

    protected DefaultGuardrailExecutedEvent(GuardrailExecutedEvent.GuardrailExecutedEventBuilder<P, R, G, E> builder) {
        super(builder);
        this.request = (P) ValidationUtils.ensureNotNull(builder.request(), "request");
        this.result = (R) ValidationUtils.ensureNotNull(builder.result(), "result");
        this.guardrailClass = ValidationUtils.ensureNotNull(builder.guardrailClass(), "guardrailClass");
        this.guardrailName = builder.guardrailName() != null ? builder.guardrailName() : this.guardrailClass.getSimpleName();
        this.duration = ValidationUtils.ensureNotNull(builder.duration(), "duration");
    }

    @Override
    public P request() {
        return this.request;
    }

    @Override
    public R result() {
        return this.result;
    }

    @Override
    public Class<G> guardrailClass() {
        return this.guardrailClass;
    }

    @Override
    public String guardrailName() {
        return this.guardrailName;
    }

    @Override
    public Duration duration() {
        return this.duration;
    }
}

