/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.guardrail.config;

import dev.langchain4j.guardrail.config.OutputGuardrailsConfig;
import java.util.function.Supplier;

public interface OutputGuardrailsConfigBuilderFactory
extends Supplier<OutputGuardrailsConfig.OutputGuardrailsConfigBuilder> {
}

