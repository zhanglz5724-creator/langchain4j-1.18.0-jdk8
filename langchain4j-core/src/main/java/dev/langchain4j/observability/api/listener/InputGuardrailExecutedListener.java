/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.listener;

import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.observability.api.event.InputGuardrailExecutedEvent;
import dev.langchain4j.observability.api.listener.GuardrailExecutedListener;

@FunctionalInterface
public interface InputGuardrailExecutedListener
extends GuardrailExecutedListener<InputGuardrailExecutedEvent, InputGuardrailRequest, InputGuardrailResult, InputGuardrail> {
    @Override
    default public Class<InputGuardrailExecutedEvent> getEventClass() {
        return InputGuardrailExecutedEvent.class;
    }
}

