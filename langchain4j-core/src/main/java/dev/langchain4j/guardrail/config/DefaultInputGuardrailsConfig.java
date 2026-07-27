/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail.config;

import dev.langchain4j.guardrail.config.InputGuardrailsConfig;
import dev.langchain4j.internal.ValidationUtils;

final class DefaultInputGuardrailsConfig
implements InputGuardrailsConfig {
    DefaultInputGuardrailsConfig(Builder builder) {
        ValidationUtils.ensureNotNull(builder, "builder");
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder
    implements InputGuardrailsConfig.InputGuardrailsConfigBuilder {
        Builder() {
        }

        @Override
        public InputGuardrailsConfig build() {
            return new DefaultInputGuardrailsConfig(this);
        }
    }
}

