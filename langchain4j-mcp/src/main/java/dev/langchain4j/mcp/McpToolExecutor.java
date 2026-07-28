/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.service.tool.ToolExecutionResult
 *  dev.langchain4j.service.tool.ToolExecutor
 */
package dev.langchain4j.mcp;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.service.tool.ToolExecutionResult;
import dev.langchain4j.service.tool.ToolExecutor;
import java.util.Optional;

public class McpToolExecutor
implements ToolExecutor {
    private final McpClient mcpClient;
    private final Optional<String> fixedToolName;

    public McpToolExecutor(McpClient mcpClient) {
        this(mcpClient, null);
    }

    public McpToolExecutor(McpClient mcpClient, String fixedToolName) {
        this.mcpClient = (McpClient)ValidationUtils.ensureNotNull((Object)mcpClient, (String)"mcpClient");
        this.fixedToolName = Optional.ofNullable(fixedToolName);
    }

    public String execute(ToolExecutionRequest executionRequest, Object memoryId) {
        InvocationContext invocationContext = InvocationContext.builder().chatMemoryId(memoryId).build();
        return this.mcpClient.executeTool(this.sanitizeToolName(executionRequest), invocationContext).resultText();
    }

    public ToolExecutionResult executeWithContext(ToolExecutionRequest executionRequest, InvocationContext invocationContext) {
        return this.mcpClient.executeTool(this.sanitizeToolName(executionRequest), invocationContext);
    }

    private ToolExecutionRequest sanitizeToolName(ToolExecutionRequest executionRequest) {
        if (this.fixedToolName.isPresent()) {
            return executionRequest.toBuilder().name(this.fixedToolName.get()).build();
        }
        return executionRequest;
    }
}

