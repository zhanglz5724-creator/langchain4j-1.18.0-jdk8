/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.guardrail.InputGuardrail
 *  dev.langchain4j.guardrail.InputGuardrailExecutor
 *  dev.langchain4j.guardrail.OutputGuardrail
 *  dev.langchain4j.guardrail.OutputGuardrailExecutor
 *  dev.langchain4j.guardrail.config.InputGuardrailsConfig
 *  dev.langchain4j.guardrail.config.OutputGuardrailsConfig
 */
package dev.langchain4j.service.guardrail;

import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailExecutor;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailExecutor;
import dev.langchain4j.guardrail.config.InputGuardrailsConfig;
import dev.langchain4j.guardrail.config.OutputGuardrailsConfig;
import dev.langchain4j.service.guardrail.AbstractGuardrailService;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

final class DefaultGuardrailService
extends AbstractGuardrailService {
    DefaultGuardrailService(Class<?> aiServiceClass, Map<Object, InputGuardrailExecutor> inputGuardrails, Map<Object, OutputGuardrailExecutor> outputGuardrails) {
        super(aiServiceClass, inputGuardrails, outputGuardrails);
    }

    Optional<InputGuardrailsConfig> getInputConfig(String methodName) {
        return this.findMethod(methodName).flatMap(x$0 -> super.getInputConfig(x$0));
    }

    Optional<OutputGuardrailsConfig> getOutputConfig(String methodName) {
        return this.findMethod(methodName).flatMap(x$0 -> super.getOutputConfig(x$0));
    }

    List<InputGuardrail> getInputGuardrails(String methodName) {
        return this.findMethod(methodName).map(x$0 -> super.getInputGuardrails(x$0)).orElseGet(ArrayList::new);
    }

    List<OutputGuardrail> getOutputGuardrails(String methodName) {
        return this.findMethod(methodName).map(x$0 -> super.getOutputGuardrails(x$0)).orElseGet(ArrayList::new);
    }

    private Optional<Method> findMethod(String methodName) {
        return Stream.of(this.aiServiceClass().getMethods()).filter(method -> methodName.equals(method.getName())).findFirst();
    }
}

