/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.event;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.observability.api.event.GuardrailExecutedEvent;
import dev.langchain4j.observability.event.DefaultInputGuardrailExecutedEvent;

public interface InputGuardrailExecutedEvent
extends GuardrailExecutedEvent<InputGuardrailRequest, InputGuardrailResult, InputGuardrail> {
    public UserMessage rewrittenUserMessage();

    default public Class<InputGuardrailExecutedEvent> eventClass() {
        return InputGuardrailExecutedEvent.class;
    }

    default public InputGuardrailExecutedEventBuilder toBuilder() {
        return new InputGuardrailExecutedEventBuilder(this);
    }

    public static InputGuardrailExecutedEventBuilder builder() {
        return new InputGuardrailExecutedEventBuilder();
    }

    public static class InputGuardrailExecutedEventBuilder
    extends GuardrailExecutedEvent.GuardrailExecutedEventBuilder<InputGuardrailRequest, InputGuardrailResult, InputGuardrail, InputGuardrailExecutedEvent> {
        protected InputGuardrailExecutedEventBuilder() {
        }

        protected InputGuardrailExecutedEventBuilder(InputGuardrailExecutedEvent src) {
            super(src);
        }

        @Override
        public InputGuardrailExecutedEvent build() {
            return new DefaultInputGuardrailExecutedEvent(this);
        }
    }
}

