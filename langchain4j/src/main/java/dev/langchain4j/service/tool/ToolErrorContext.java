/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.invocation.InvocationParameters
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.invocation.InvocationParameters;
import java.util.Objects;

public class ToolErrorContext {
    private final ToolExecutionRequest toolExecutionRequest;
    private final InvocationContext invocationContext;
    private final Exception rawError;

    public ToolErrorContext(Builder builder) {
        this.toolExecutionRequest = (ToolExecutionRequest)ValidationUtils.ensureNotNull((Object)builder.toolExecutionRequest, (String)"toolExecutionRequest");
        this.invocationContext = (InvocationContext)ValidationUtils.ensureNotNull((Object)builder.invocationContext, (String)"invocationContext");
        this.rawError = builder.rawError;
    }

    public ToolExecutionRequest toolExecutionRequest() {
        return this.toolExecutionRequest;
    }

    public InvocationContext invocationContext() {
        return this.invocationContext;
    }

    public InvocationParameters invocationParameters() {
        return this.invocationContext.invocationParameters();
    }

    public Exception rawError() {
        return this.rawError;
    }

    public Object memoryId() {
        return this.invocationContext.chatMemoryId();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        ToolErrorContext that = (ToolErrorContext)object;
        return Objects.equals(this.toolExecutionRequest, that.toolExecutionRequest) && Objects.equals(this.invocationContext, that.invocationContext) && Objects.equals(this.rawError, that.rawError);
    }

    public int hashCode() {
        return Objects.hash(this.toolExecutionRequest, this.invocationContext, this.rawError);
    }

    public String toString() {
        return "ToolErrorContext{toolExecutionRequest=" + this.toolExecutionRequest + ", invocationContext=" + this.invocationContext + ", rawError=" + this.rawError + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ToolExecutionRequest toolExecutionRequest;
        private InvocationContext invocationContext;
        private Exception rawError;

        public Builder toolExecutionRequest(ToolExecutionRequest toolExecutionRequest) {
            this.toolExecutionRequest = toolExecutionRequest;
            return this;
        }

        public Builder invocationContext(InvocationContext invocationContext) {
            this.invocationContext = invocationContext;
            return this;
        }

        public Builder rawError(Exception rawError) {
            this.rawError = rawError;
            return this;
        }

        @Deprecated
        public Builder memoryId(Object memoryId) {
            this.invocationContext = InvocationContext.builder().chatMemoryId(memoryId).build();
            return this;
        }

        public ToolErrorContext build() {
            return new ToolErrorContext(this);
        }
    }
}

