/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.guardrail;

import dev.langchain4j.guardrail.AbstractGuardrailExecutor;
import dev.langchain4j.guardrail.Guardrail;
import dev.langchain4j.guardrail.GuardrailRequest;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.guardrail.config.GuardrailsConfig;
import dev.langchain4j.observability.api.event.GuardrailExecutedEvent;

public interface GuardrailExecutorBuilderFactory<C extends GuardrailsConfig, R extends GuardrailResult<R>, P extends GuardrailRequest<P>, G extends Guardrail<P, R>, E extends GuardrailExecutedEvent<P, R, G>, B extends AbstractGuardrailExecutor.GuardrailExecutorBuilder<C, R, P, G, E, B>> {
    public B getBuilder();
}

