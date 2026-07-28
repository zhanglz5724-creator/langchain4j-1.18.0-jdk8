/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.invocation.InvocationContext
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.service.tool.ToolExecutionResult;

@FunctionalInterface
public interface ToolExecutor {
    public String execute(ToolExecutionRequest var1, Object var2);

    default public ToolExecutionResult executeWithContext(ToolExecutionRequest request, InvocationContext context) {
        Object memoryId = context == null ? null : context.chatMemoryId();
        String result = this.execute(request, memoryId);
        return ToolExecutionResult.builder().resultText(result).build();
    }
}

