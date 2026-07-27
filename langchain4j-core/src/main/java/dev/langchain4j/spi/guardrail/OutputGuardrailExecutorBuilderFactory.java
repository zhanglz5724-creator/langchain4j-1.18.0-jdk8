/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.guardrail;

import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailExecutor;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.guardrail.config.OutputGuardrailsConfig;
import dev.langchain4j.observability.api.event.OutputGuardrailExecutedEvent;
import dev.langchain4j.spi.guardrail.GuardrailExecutorBuilderFactory;

public interface OutputGuardrailExecutorBuilderFactory
extends GuardrailExecutorBuilderFactory<OutputGuardrailsConfig, OutputGuardrailResult, OutputGuardrailRequest, OutputGuardrail, OutputGuardrailExecutedEvent, OutputGuardrailExecutor.OutputGuardrailExecutorBuilder> {
}

