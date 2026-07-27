/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.observability.api.AiServiceListenerRegistrar;
import dev.langchain4j.rag.AugmentationResult;
import java.util.Map;
import java.util.Optional;

public final class GuardrailRequestParams {
    private final ChatMemory chatMemory;
    private final AugmentationResult augmentationResult;
    private final String userMessageTemplate;
    private final Map<String, Object> variables;
    private final InvocationContext invocationContext;
    private final AiServiceListenerRegistrar aiServiceListenerRegistrar;

    private GuardrailRequestParams(Builder builder) {
        this.chatMemory = builder.chatMemory;
        this.augmentationResult = builder.augmentationResult;
        this.userMessageTemplate = ValidationUtils.ensureNotNull(builder.userMessageTemplate, "userMessageTemplate");
        this.variables = ValidationUtils.ensureNotNull(builder.variables, "variables");
        this.invocationContext = builder.invocationContext;
        this.aiServiceListenerRegistrar = Optional.ofNullable(builder.aiServiceListenerRegistrar).orElseGet(AiServiceListenerRegistrar::newInstance);
    }

    public ChatMemory chatMemory() {
        return this.chatMemory;
    }

    public AugmentationResult augmentationResult() {
        return this.augmentationResult;
    }

    public String userMessageTemplate() {
        return this.userMessageTemplate;
    }

    public Map<String, Object> variables() {
        return this.variables;
    }

    public InvocationContext invocationContext() {
        return this.invocationContext;
    }

    public AiServiceListenerRegistrar aiservicelistenerregistrar() {
        return this.aiServiceListenerRegistrar;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ChatMemory chatMemory;
        private AugmentationResult augmentationResult;
        private String userMessageTemplate;
        private Map<String, Object> variables;
        private InvocationContext invocationContext;
        private AiServiceListenerRegistrar aiServiceListenerRegistrar;

        public Builder() {
        }

        public Builder(GuardrailRequestParams src) {
            this.chatMemory = src.chatMemory;
            this.augmentationResult = src.augmentationResult;
            this.userMessageTemplate = src.userMessageTemplate;
            this.variables = src.variables;
            this.invocationContext = src.invocationContext;
            this.aiServiceListenerRegistrar = src.aiServiceListenerRegistrar;
        }

        public Builder chatMemory(ChatMemory chatMemory) {
            this.chatMemory = chatMemory;
            return this;
        }

        public Builder augmentationResult(AugmentationResult augmentationResult) {
            this.augmentationResult = augmentationResult;
            return this;
        }

        public Builder userMessageTemplate(String userMessageTemplate) {
            this.userMessageTemplate = userMessageTemplate;
            return this;
        }

        public Builder variables(Map<String, Object> variables) {
            this.variables = variables;
            return this;
        }

        public Builder invocationContext(InvocationContext invocationContext) {
            this.invocationContext = invocationContext;
            return this;
        }

        public Builder aiServiceListenerRegistrar(AiServiceListenerRegistrar aiServiceListenerRegistrar) {
            this.aiServiceListenerRegistrar = aiServiceListenerRegistrar;
            return this;
        }

        public GuardrailRequestParams build() {
            return new GuardrailRequestParams(this);
        }
    }
}

