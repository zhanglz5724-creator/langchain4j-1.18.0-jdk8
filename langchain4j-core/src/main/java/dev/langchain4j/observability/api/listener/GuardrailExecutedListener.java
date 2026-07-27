/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.listener;

import dev.langchain4j.guardrail.Guardrail;
import dev.langchain4j.guardrail.GuardrailRequest;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.observability.api.event.GuardrailExecutedEvent;
import dev.langchain4j.observability.api.listener.AiServiceListener;

public interface GuardrailExecutedListener<E extends GuardrailExecutedEvent<P, R, G>, P extends GuardrailRequest<P>, R extends GuardrailResult<R>, G extends Guardrail<P, R>>
extends AiServiceListener<E> {
}

