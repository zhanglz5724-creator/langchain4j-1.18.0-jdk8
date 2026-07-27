/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.event;

import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.observability.api.event.GuardrailExecutedEvent;
import dev.langchain4j.observability.event.DefaultOutputGuardrailExecutedEvent;

public interface OutputGuardrailExecutedEvent
extends GuardrailExecutedEvent<OutputGuardrailRequest, OutputGuardrailResult, OutputGuardrail> {
    default public Class<OutputGuardrailExecutedEvent> eventClass() {
        return OutputGuardrailExecutedEvent.class;
    }

    default public OutputGuardrailExecutedEventBuilder toBuilder() {
        return new OutputGuardrailExecutedEventBuilder(this);
    }

    public static OutputGuardrailExecutedEventBuilder builder() {
        return new OutputGuardrailExecutedEventBuilder();
    }

    public static class OutputGuardrailExecutedEventBuilder
    extends GuardrailExecutedEvent.GuardrailExecutedEventBuilder<OutputGuardrailRequest, OutputGuardrailResult, OutputGuardrail, OutputGuardrailExecutedEvent> {
        protected OutputGuardrailExecutedEventBuilder() {
        }

        protected OutputGuardrailExecutedEventBuilder(OutputGuardrailExecutedEvent src) {
            super(src);
        }

        @Override
        public OutputGuardrailExecutedEvent build() {
            return new DefaultOutputGuardrailExecutedEvent(this);
        }
    }
}

