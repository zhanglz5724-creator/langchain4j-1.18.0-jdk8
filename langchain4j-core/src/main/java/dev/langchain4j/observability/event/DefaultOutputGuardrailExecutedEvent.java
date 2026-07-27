/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.event;

import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.observability.api.event.OutputGuardrailExecutedEvent;
import dev.langchain4j.observability.event.DefaultGuardrailExecutedEvent;

public class DefaultOutputGuardrailExecutedEvent
extends DefaultGuardrailExecutedEvent<OutputGuardrailRequest, OutputGuardrailResult, OutputGuardrail, OutputGuardrailExecutedEvent>
implements OutputGuardrailExecutedEvent {
    public DefaultOutputGuardrailExecutedEvent(OutputGuardrailExecutedEvent.OutputGuardrailExecutedEventBuilder builder) {
        super(builder);
    }
}

