/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail.config;

import dev.langchain4j.guardrail.config.DefaultInputGuardrailsConfig;
import dev.langchain4j.guardrail.config.GuardrailsConfig;
import dev.langchain4j.guardrail.config.GuardrailsConfigBuilder;
import dev.langchain4j.spi.guardrail.config.InputGuardrailsConfigBuilderFactory;
import java.util.Iterator;
import java.util.ServiceLoader;

public interface InputGuardrailsConfig
extends GuardrailsConfig {
    public static InputGuardrailsConfigBuilder builder() {
        Iterator<InputGuardrailsConfigBuilderFactory> iterator = ServiceLoader.load(InputGuardrailsConfigBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            InputGuardrailsConfigBuilderFactory factory = iterator.next();
            return (InputGuardrailsConfigBuilder)factory.get();
        }
        return DefaultInputGuardrailsConfig.builder();
    }

    public static interface InputGuardrailsConfigBuilder
    extends GuardrailsConfigBuilder<InputGuardrailsConfig> {
    }
}

