/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.guardrail.AbstractGuardrailExecutor
 *  dev.langchain4j.guardrail.InputGuardrail
 *  dev.langchain4j.guardrail.InputGuardrailExecutor
 *  dev.langchain4j.guardrail.InputGuardrailRequest
 *  dev.langchain4j.guardrail.InputGuardrailResult
 *  dev.langchain4j.guardrail.OutputGuardrail
 *  dev.langchain4j.guardrail.OutputGuardrailExecutor
 *  dev.langchain4j.guardrail.OutputGuardrailRequest
 *  dev.langchain4j.guardrail.OutputGuardrailResult
 *  dev.langchain4j.guardrail.config.InputGuardrailsConfig
 *  dev.langchain4j.guardrail.config.OutputGuardrailsConfig
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.service.guardrail;

import dev.langchain4j.Internal;
import dev.langchain4j.guardrail.AbstractGuardrailExecutor;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailExecutor;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailExecutor;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.guardrail.config.InputGuardrailsConfig;
import dev.langchain4j.guardrail.config.OutputGuardrailsConfig;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.service.guardrail.GuardrailService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Internal
public abstract class AbstractGuardrailService
implements GuardrailService {
    private static final Object NULL_KEY = new Object();
    private final Class<?> aiServiceClass;
    private final Map<Object, InputGuardrailExecutor> inputGuardrails = new ConcurrentHashMap<Object, InputGuardrailExecutor>();
    private final Map<Object, OutputGuardrailExecutor> outputGuardrails = new ConcurrentHashMap<Object, OutputGuardrailExecutor>();
    private final Map<Object, Boolean> inputGuardrailMethods = new ConcurrentHashMap<Object, Boolean>();
    private final Map<Object, Boolean> outputGuardrailMethods = new ConcurrentHashMap<Object, Boolean>();

    protected AbstractGuardrailService(Class<?> aiServiceClass, Map<Object, InputGuardrailExecutor> inputGuardrails, Map<Object, OutputGuardrailExecutor> outputGuardrails) {
        this.aiServiceClass = (Class)ValidationUtils.ensureNotNull(aiServiceClass, (String)"aiServiceClass");
        Optional.ofNullable(inputGuardrails).ifPresent(this.inputGuardrails::putAll);
        Optional.ofNullable(outputGuardrails).ifPresent(this.outputGuardrails::putAll);
    }

    @Override
    public Class<?> aiServiceClass() {
        return this.aiServiceClass;
    }

    @Override
    public <MethodKey> InputGuardrailResult executeInputGuardrails(MethodKey method, InputGuardrailRequest request) {
        return Optional.ofNullable(method).map(this.inputGuardrails::get).map(executor -> executor.execute(request)).orElseGet(InputGuardrailResult::success);
    }

    @Override
    public <MethodKey> OutputGuardrailResult executeOutputGuardrails(MethodKey method, OutputGuardrailRequest request) {
        return Optional.ofNullable(method).map(this.outputGuardrails::get).map(executor -> executor.execute(request)).orElseGet(OutputGuardrailResult::success);
    }

    @Override
    public <MethodKey> boolean hasInputGuardrails(MethodKey method) {
        return this.inputGuardrailMethods.computeIfAbsent(AbstractGuardrailService.checkMethodKey(method), m -> !this.getInputGuardrails(m).isEmpty());
    }

    @Override
    public <MethodKey> boolean hasOutputGuardrails(MethodKey method) {
        return this.outputGuardrailMethods.computeIfAbsent(AbstractGuardrailService.checkMethodKey(method), m -> !this.getOutputGuardrails(m).isEmpty());
    }

    private static <MethodKey> MethodKey checkMethodKey(MethodKey method) {
        return (MethodKey)(method != null ? method : NULL_KEY);
    }

    int getInputGuardrailMethodCount() {
        return this.inputGuardrails.size();
    }

    int getOutputGuardrailMethodCount() {
        return this.outputGuardrails.size();
    }

    <MethodKey> Optional<InputGuardrailsConfig> getInputConfig(MethodKey method) {
        return Optional.ofNullable(this.inputGuardrails.get(method)).map(AbstractGuardrailExecutor::config);
    }

    <MethodKey> Optional<OutputGuardrailsConfig> getOutputConfig(MethodKey method) {
        return Optional.ofNullable(this.outputGuardrails.get(method)).map(AbstractGuardrailExecutor::config);
    }

    <MethodKey> List<InputGuardrail> getInputGuardrails(MethodKey method) {
        return Optional.ofNullable(method).map(this.inputGuardrails::get).map(AbstractGuardrailExecutor::guardrails).orElseGet(ArrayList::new);
    }

    <MethodKey> List<OutputGuardrail> getOutputGuardrails(MethodKey method) {
        return Optional.ofNullable(method).map(this.outputGuardrails::get).map(AbstractGuardrailExecutor::guardrails).orElseGet(ArrayList::new);
    }
}

