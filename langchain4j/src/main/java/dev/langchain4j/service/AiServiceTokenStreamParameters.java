/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.guardrail.GuardrailRequestParams
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.rag.content.Content
 */
package dev.langchain4j.service;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.guardrail.GuardrailRequestParams;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.AiServiceContext;
import dev.langchain4j.service.tool.ToolArgumentsErrorHandler;
import dev.langchain4j.service.tool.ToolExecutionErrorHandler;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.tool.ToolServiceContext;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Internal
public class AiServiceTokenStreamParameters {
    private final List<ChatMessage> messages;
    private final ToolServiceContext toolServiceContext;
    private final ToolArgumentsErrorHandler toolArgumentsErrorHandler;
    private final ToolExecutionErrorHandler toolExecutionErrorHandler;
    private final Executor toolExecutor;
    private final List<Content> retrievedContents;
    private final AiServiceContext context;
    private final InvocationContext invocationContext;
    private final GuardrailRequestParams commonGuardrailParams;
    private final Object methodKey;

    protected AiServiceTokenStreamParameters(Builder builder) {
        this.messages = builder.messages;
        this.toolServiceContext = builder.toolServiceContext;
        this.toolArgumentsErrorHandler = builder.toolArgumentsErrorHandler;
        this.toolExecutionErrorHandler = builder.toolExecutionErrorHandler;
        this.toolExecutor = builder.toolExecutor;
        this.retrievedContents = builder.retrievedContents;
        this.context = builder.context;
        this.invocationContext = builder.invocationContext;
        this.commonGuardrailParams = builder.commonGuardrailParams;
        this.methodKey = builder.methodKey;
    }

    public List<ChatMessage> messages() {
        return this.messages;
    }

    public ToolServiceContext toolServiceContext() {
        return this.toolServiceContext;
    }

    @Deprecated
    public List<ToolSpecification> effectiveTools() {
        return this.toolServiceContext != null ? this.toolServiceContext.effectiveTools() : null;
    }

    @Deprecated
    public List<ToolSpecification> toolSpecifications() {
        return this.effectiveTools();
    }

    @Deprecated
    public List<ToolSpecification> availableTools() {
        return this.toolServiceContext != null ? this.toolServiceContext.availableTools() : null;
    }

    @Deprecated
    public Map<String, ToolExecutor> toolExecutors() {
        return this.toolServiceContext != null ? this.toolServiceContext.toolExecutors() : null;
    }

    public ToolArgumentsErrorHandler toolArgumentsErrorHandler() {
        return this.toolArgumentsErrorHandler;
    }

    public ToolExecutionErrorHandler toolExecutionErrorHandler() {
        return this.toolExecutionErrorHandler;
    }

    public Executor toolExecutor() {
        return this.toolExecutor;
    }

    public List<Content> retrievedContents() {
        return this.retrievedContents;
    }

    public AiServiceContext context() {
        return this.context;
    }

    public InvocationContext invocationContext() {
        return this.invocationContext;
    }

    public GuardrailRequestParams commonGuardrailParams() {
        return this.commonGuardrailParams;
    }

    public Object methodKey() {
        return this.methodKey;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<ChatMessage> messages;
        private ToolServiceContext toolServiceContext;
        private ToolArgumentsErrorHandler toolArgumentsErrorHandler;
        private ToolExecutionErrorHandler toolExecutionErrorHandler;
        private Executor toolExecutor;
        private List<Content> retrievedContents;
        private AiServiceContext context;
        private InvocationContext invocationContext;
        private GuardrailRequestParams commonGuardrailParams;
        private Object methodKey;

        protected Builder() {
        }

        public Builder messages(List<ChatMessage> messages) {
            this.messages = messages;
            return this;
        }

        public Builder toolServiceContext(ToolServiceContext toolServiceContext) {
            this.toolServiceContext = toolServiceContext;
            return this;
        }

        @Deprecated
        public Builder effectiveTools(List<ToolSpecification> effectiveTools) {
            this.ensureToolServiceContext();
            this.toolServiceContext = this.toolServiceContext.toBuilder().effectiveTools(effectiveTools).build();
            return this;
        }

        @Deprecated
        public Builder toolSpecifications(List<ToolSpecification> toolSpecifications) {
            this.effectiveTools(toolSpecifications);
            this.availableTools(toolSpecifications);
            return this;
        }

        @Deprecated
        public Builder availableTools(List<ToolSpecification> availableTools) {
            this.ensureToolServiceContext();
            this.toolServiceContext = this.toolServiceContext.toBuilder().availableTools(availableTools).build();
            return this;
        }

        @Deprecated
        public Builder toolExecutors(Map<String, ToolExecutor> toolExecutors) {
            this.ensureToolServiceContext();
            this.toolServiceContext = this.toolServiceContext.toBuilder().toolExecutors(toolExecutors).build();
            return this;
        }

        private void ensureToolServiceContext() {
            if (this.toolServiceContext == null) {
                this.toolServiceContext = ToolServiceContext.builder().build();
            }
        }

        public Builder toolArgumentsErrorHandler(ToolArgumentsErrorHandler handler) {
            this.toolArgumentsErrorHandler = handler;
            return this;
        }

        public Builder toolExecutionErrorHandler(ToolExecutionErrorHandler handler) {
            this.toolExecutionErrorHandler = handler;
            return this;
        }

        public Builder toolExecutor(Executor toolExecutor) {
            this.toolExecutor = toolExecutor;
            return this;
        }

        public Builder retrievedContents(List<Content> retrievedContents) {
            this.retrievedContents = retrievedContents;
            return this;
        }

        public Builder context(AiServiceContext context) {
            this.context = context;
            return this;
        }

        public Builder invocationContext(InvocationContext invocationContext) {
            this.invocationContext = invocationContext;
            return this;
        }

        public Builder commonGuardrailParams(GuardrailRequestParams commonGuardrailParams) {
            this.commonGuardrailParams = commonGuardrailParams;
            return this;
        }

        public Builder methodKey(Object methodKey) {
            this.methodKey = methodKey;
            return this;
        }

        public AiServiceTokenStreamParameters build() {
            return new AiServiceTokenStreamParameters(this);
        }
    }
}

