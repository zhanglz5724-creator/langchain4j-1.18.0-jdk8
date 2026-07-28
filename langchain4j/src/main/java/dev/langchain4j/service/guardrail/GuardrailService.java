/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.guardrail.InputGuardrail
 *  dev.langchain4j.guardrail.InputGuardrailRequest
 *  dev.langchain4j.guardrail.InputGuardrailResult
 *  dev.langchain4j.guardrail.OutputGuardrail
 *  dev.langchain4j.guardrail.OutputGuardrailRequest
 *  dev.langchain4j.guardrail.OutputGuardrailResult
 *  dev.langchain4j.guardrail.config.InputGuardrailsConfig
 *  dev.langchain4j.guardrail.config.OutputGuardrailsConfig
 */
package dev.langchain4j.service.guardrail;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.guardrail.config.InputGuardrailsConfig;
import dev.langchain4j.guardrail.config.OutputGuardrailsConfig;
import dev.langchain4j.service.guardrail.GuardrailServiceBuilder;
import dev.langchain4j.service.guardrail.spi.GuardrailServiceBuilderFactory;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public interface GuardrailService {
    public Class<?> aiServiceClass();

    public <MethodKey> InputGuardrailResult executeInputGuardrails(MethodKey var1, InputGuardrailRequest var2);

    default public <MethodKey> UserMessage executeGuardrails(MethodKey method, InputGuardrailRequest request) {
        return this.executeInputGuardrails(method, request).userMessage(request);
    }

    public <MethodKey> OutputGuardrailResult executeOutputGuardrails(MethodKey var1, OutputGuardrailRequest var2);

    public <MethodKey> boolean hasInputGuardrails(MethodKey var1);

    public <MethodKey> boolean hasOutputGuardrails(MethodKey var1);

    default public <MethodKey, T> T executeGuardrails(MethodKey method, OutputGuardrailRequest request) {
        return (T)this.executeOutputGuardrails(method, request).response(request);
    }

    public static Builder builder(Class<?> aiServiceClass) {
        Iterator<GuardrailServiceBuilderFactory> it = ServiceLoader.load(GuardrailServiceBuilderFactory.class).iterator();
        if (it.hasNext()) {
            return it.next().getBuilder(aiServiceClass);
        }
        return new GuardrailServiceBuilder(aiServiceClass);
    }

    public static interface Builder {
        public Builder inputGuardrailsConfig(InputGuardrailsConfig var1);

        public Builder outputGuardrailsConfig(OutputGuardrailsConfig var1);

        public <I extends InputGuardrail> Builder inputGuardrailClasses(List<Class<? extends I>> var1);

        default public <I extends InputGuardrail> Builder inputGuardrailClasses(Class<? extends I> ... guardrailClasses) {
            if (guardrailClasses != null) {
                return this.inputGuardrailClasses(Arrays.asList(guardrailClasses));
            }
            return this;
        }

        public <O extends OutputGuardrail> Builder outputGuardrailClasses(List<Class<? extends O>> var1);

        default public <O extends OutputGuardrail> Builder outputGuardrailClasses(Class<? extends O> ... guardrailClasses) {
            if (guardrailClasses != null) {
                return this.outputGuardrailClasses(Arrays.asList(guardrailClasses));
            }
            return this;
        }

        public <I extends InputGuardrail> Builder inputGuardrails(List<I> var1);

        default public <I extends InputGuardrail> Builder inputGuardrails(I ... guardrails) {
            if (guardrails != null) {
                return this.inputGuardrails(Arrays.asList(guardrails));
            }
            return this;
        }

        public <O extends OutputGuardrail> Builder outputGuardrails(List<O> var1);

        default public <O extends OutputGuardrail> Builder outputGuardrails(O ... guardrails) {
            if (guardrails != null) {
                return this.outputGuardrails(Arrays.asList(guardrails));
            }
            return this;
        }

        public GuardrailService build();
    }
}

