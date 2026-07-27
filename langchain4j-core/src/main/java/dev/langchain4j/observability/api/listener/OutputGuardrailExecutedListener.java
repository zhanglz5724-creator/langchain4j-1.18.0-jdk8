/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.listener;

import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.observability.api.event.OutputGuardrailExecutedEvent;
import dev.langchain4j.observability.api.listener.GuardrailExecutedListener;

@FunctionalInterface
public interface OutputGuardrailExecutedListener
extends GuardrailExecutedListener<OutputGuardrailExecutedEvent, OutputGuardrailRequest, OutputGuardrailResult, OutputGuardrail> {
    @Override
    default public Class<OutputGuardrailExecutedEvent> getEventClass() {
        return OutputGuardrailExecutedEvent.class;
    }
}

