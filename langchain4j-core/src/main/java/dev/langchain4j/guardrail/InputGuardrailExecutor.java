/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.guardrail.AbstractGuardrailExecutor;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailException;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.guardrail.config.InputGuardrailsConfig;
import dev.langchain4j.observability.api.event.InputGuardrailExecutedEvent;
import dev.langchain4j.spi.guardrail.InputGuardrailExecutorBuilderFactory;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class InputGuardrailExecutor
extends AbstractGuardrailExecutor<InputGuardrailsConfig, InputGuardrailRequest, InputGuardrailResult, InputGuardrail, InputGuardrailExecutedEvent, InputGuardrailResult.Failure> {
    protected InputGuardrailExecutor(InputGuardrailsConfig config, List<InputGuardrail> guardrails) {
        super(config, guardrails);
    }

    @Override
    protected InputGuardrailResult createFailure(List<InputGuardrailResult.Failure> failures) {
        return new InputGuardrailResult(failures, false);
    }

    @Override
    protected InputGuardrailResult createSuccess() {
        return InputGuardrailResult.success();
    }

    @Override
    protected InputGuardrailException createGuardrailException(String message, Throwable cause) {
        return new InputGuardrailException(message, cause);
    }

    protected InputGuardrailExecutedEvent.InputGuardrailExecutedEventBuilder createEmptyObservabilityEventBuilderInstance() {
        return InputGuardrailExecutedEvent.builder();
    }

    @Override
    public InputGuardrailResult execute(InputGuardrailRequest request) {
        InputGuardrailResult result = (InputGuardrailResult)this.executeGuardrails(request);
        if (!result.isSuccess()) {
            throw new InputGuardrailException(result.toString(), result.getFirstFailureException());
        }
        return result;
    }

    public static InputGuardrailExecutorBuilder builder() {
        Iterator<InputGuardrailExecutorBuilderFactory> iterator = ServiceLoader.load(InputGuardrailExecutorBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            InputGuardrailExecutorBuilderFactory factory = iterator.next();
            return (InputGuardrailExecutorBuilder)factory.getBuilder();
        }
        return new InputGuardrailExecutorBuilder();
    }

    public static class InputGuardrailExecutorBuilder
    extends AbstractGuardrailExecutor.GuardrailExecutorBuilder<InputGuardrailsConfig, InputGuardrailResult, InputGuardrailRequest, InputGuardrail, InputGuardrailExecutedEvent, InputGuardrailExecutorBuilder> {
        public InputGuardrailExecutorBuilder() {
            super(InputGuardrailsConfig.builder().build());
        }

        public InputGuardrailExecutor build() {
            return new InputGuardrailExecutor((InputGuardrailsConfig)this.config(), this.guardrails());
        }
    }
}

