/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.guardrail;

import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailExecutor;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.guardrail.config.InputGuardrailsConfig;
import dev.langchain4j.observability.api.event.InputGuardrailExecutedEvent;
import dev.langchain4j.spi.guardrail.GuardrailExecutorBuilderFactory;

public interface InputGuardrailExecutorBuilderFactory
extends GuardrailExecutorBuilderFactory<InputGuardrailsConfig, InputGuardrailResult, InputGuardrailRequest, InputGuardrail, InputGuardrailExecutedEvent, InputGuardrailExecutor.InputGuardrailExecutorBuilder> {
}

