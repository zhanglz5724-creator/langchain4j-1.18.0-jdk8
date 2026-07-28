/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.service.tool.ToolExecutionResult
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.mcp.client;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.mcp.client.McpGetPromptResult;
import dev.langchain4j.mcp.client.McpPrompt;
import dev.langchain4j.mcp.client.McpReadResourceResult;
import dev.langchain4j.mcp.client.McpResource;
import dev.langchain4j.mcp.client.McpResourceTemplate;
import dev.langchain4j.mcp.client.McpRoot;
import dev.langchain4j.service.tool.ToolExecutionResult;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface McpClient
extends AutoCloseable {
    public String key();

    default public @Nullable String instructions() {
        return null;
    }

    public List<ToolSpecification> listTools();

    public List<ToolSpecification> listTools(InvocationContext var1);

    public ToolExecutionResult executeTool(ToolExecutionRequest var1);

    public ToolExecutionResult executeTool(ToolExecutionRequest var1, InvocationContext var2);

    public List<McpResource> listResources();

    public List<McpResource> listResources(InvocationContext var1);

    public List<McpResourceTemplate> listResourceTemplates();

    public List<McpResourceTemplate> listResourceTemplates(InvocationContext var1);

    public McpReadResourceResult readResource(String var1);

    public McpReadResourceResult readResource(String var1, InvocationContext var2);

    public void subscribeToResource(String var1);

    public void unsubscribeFromResource(String var1);

    public List<McpPrompt> listPrompts();

    public McpGetPromptResult getPrompt(String var1, Map<String, Object> var2);

    public void checkHealth();

    public void setRoots(List<McpRoot> var1);
}

