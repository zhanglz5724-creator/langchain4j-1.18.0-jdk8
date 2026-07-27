/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail.config;

import dev.langchain4j.guardrail.config.DefaultOutputGuardrailsConfig;
import dev.langchain4j.guardrail.config.GuardrailsConfig;
import dev.langchain4j.guardrail.config.GuardrailsConfigBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.spi.guardrail.config.OutputGuardrailsConfigBuilderFactory;
import java.util.Iterator;
import java.util.ServiceLoader;

@JacocoIgnoreCoverageGenerated
public interface OutputGuardrailsConfig
extends GuardrailsConfig {
    public static final int MAX_RETRIES_DEFAULT = 2;

    public int maxRetries();

    public static OutputGuardrailsConfigBuilder builder() {
        Iterator<OutputGuardrailsConfigBuilderFactory> iterator = ServiceLoader.load(OutputGuardrailsConfigBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OutputGuardrailsConfigBuilderFactory factory = iterator.next();
            return (OutputGuardrailsConfigBuilder)factory.get();
        }
        return DefaultOutputGuardrailsConfig.builder();
    }

    public static interface OutputGuardrailsConfigBuilder
    extends GuardrailsConfigBuilder<OutputGuardrailsConfig> {
        public OutputGuardrailsConfigBuilder maxRetries(int var1);
    }
}

