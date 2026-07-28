/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.service.guardrail.spi;

import dev.langchain4j.service.guardrail.GuardrailService;

public interface GuardrailServiceBuilderFactory {
    public GuardrailService.Builder getBuilder(Class<?> var1);
}

