/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail.config;

import dev.langchain4j.guardrail.config.OutputGuardrailsConfig;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.ValidationUtils;

@JacocoIgnoreCoverageGenerated
final class DefaultOutputGuardrailsConfig
implements OutputGuardrailsConfig {
    private final int maxRetries;

    DefaultOutputGuardrailsConfig(Builder builder) {
        ValidationUtils.ensureNotNull(builder, "builder");
        this.maxRetries = builder.maxRetries;
    }

    static Builder builder() {
        return new Builder();
    }

    @Override
    public int maxRetries() {
        return this.maxRetries;
    }

    static class Builder
    implements OutputGuardrailsConfig.OutputGuardrailsConfigBuilder {
        private int maxRetries = 2;

        Builder() {
        }

        @Override
        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        @Override
        public OutputGuardrailsConfig build() {
            return new DefaultOutputGuardrailsConfig(this);
        }
    }
}

