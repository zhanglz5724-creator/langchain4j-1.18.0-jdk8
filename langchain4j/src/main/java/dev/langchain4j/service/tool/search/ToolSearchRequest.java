/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 */
package dev.langchain4j.service.tool.search;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import java.util.List;
import java.util.Objects;

@Experimental
public class ToolSearchRequest {
    private final ToolExecutionRequest toolExecutionRequest;
    private final List<ToolSpecification> searchableTools;
    private final InvocationContext invocationContext;

    public ToolSearchRequest(Builder builder) {
        this.toolExecutionRequest = (ToolExecutionRequest)ValidationUtils.ensureNotNull((Object)builder.toolExecutionRequest, (String)"toolExecutionRequest");
        this.searchableTools = Utils.copy((List)builder.searchableTools);
        this.invocationContext = (InvocationContext)ValidationUtils.ensureNotNull((Object)builder.invocationContext, (String)"invocationContext");
    }

    public ToolExecutionRequest toolExecutionRequest() {
        return this.toolExecutionRequest;
    }

    public List<ToolSpecification> searchableTools() {
        return this.searchableTools;
    }

    public InvocationContext invocationContext() {
        return this.invocationContext;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ToolSearchRequest that = (ToolSearchRequest)o;
        return Objects.equals(this.toolExecutionRequest, that.toolExecutionRequest) && Objects.equals(this.searchableTools, that.searchableTools) && Objects.equals(this.invocationContext, that.invocationContext);
    }

    public int hashCode() {
        return Objects.hash(this.toolExecutionRequest, this.searchableTools, this.invocationContext);
    }

    public String toString() {
        return "ToolSearchRequest{toolExecutionRequest=" + this.toolExecutionRequest + ", searchableTools=" + this.searchableTools + ", invocationContext=" + this.invocationContext + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ToolExecutionRequest toolExecutionRequest;
        private List<ToolSpecification> searchableTools;
        private InvocationContext invocationContext;

        public Builder toolExecutionRequest(ToolExecutionRequest toolExecutionRequest) {
            this.toolExecutionRequest = toolExecutionRequest;
            return this;
        }

        public Builder searchableTools(List<ToolSpecification> searchableTools) {
            this.searchableTools = searchableTools;
            return this;
        }

        public Builder invocationContext(InvocationContext invocationContext) {
            this.invocationContext = invocationContext;
            return this;
        }

        public ToolSearchRequest build() {
            return new ToolSearchRequest(this);
        }
    }
}

