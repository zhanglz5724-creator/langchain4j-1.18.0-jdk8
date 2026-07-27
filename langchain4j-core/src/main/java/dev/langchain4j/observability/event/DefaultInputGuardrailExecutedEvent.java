/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.event;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.observability.api.event.InputGuardrailExecutedEvent;
import dev.langchain4j.observability.event.DefaultGuardrailExecutedEvent;

public class DefaultInputGuardrailExecutedEvent
extends DefaultGuardrailExecutedEvent<InputGuardrailRequest, InputGuardrailResult, InputGuardrail, InputGuardrailExecutedEvent>
implements InputGuardrailExecutedEvent {
    public DefaultInputGuardrailExecutedEvent(InputGuardrailExecutedEvent.InputGuardrailExecutedEventBuilder builder) {
        super(builder);
    }

    @Override
    public UserMessage rewrittenUserMessage() {
        return ((InputGuardrailResult)this.result()).userMessage((InputGuardrailRequest)this.request());
    }
}

