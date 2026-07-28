/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import java.util.Objects;

@Experimental
public class BeforeToolExecution {
    private final ToolExecutionRequest request;
    private final InvocationContext invocationContext;

    private BeforeToolExecution(Builder builder) {
        this.request = (ToolExecutionRequest)ValidationUtils.ensureNotNull((Object)builder.request, (String)"request");
        this.invocationContext = (InvocationContext)ValidationUtils.ensureNotNull((Object)builder.invocationContext, (String)"invocationContext");
    }

    public ToolExecutionRequest request() {
        return this.request;
    }

    public InvocationContext invocationContext() {
        return this.invocationContext;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        BeforeToolExecution that = (BeforeToolExecution)obj;
        return Objects.equals(this.request, that.request);
    }

    public String toString() {
        return "BeforeToolExecution { request = " + this.request + " }";
    }

    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.request);
        return h;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ToolExecutionRequest request;
        private InvocationContext invocationContext;

        private Builder() {
        }

        public Builder request(ToolExecutionRequest request) {
            this.request = request;
            return this;
        }

        public Builder invocationContext(InvocationContext invocationContext) {
            this.invocationContext = invocationContext;
            return this;
        }

        public BeforeToolExecution build() {
            return new BeforeToolExecution(this);
        }
    }
}

