/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.service.tool.ToolExecutor
 */
package dev.langchain4j.mcp.resourcesastools;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.service.tool.ToolExecutor;
import java.util.List;

public interface McpResourcesAsToolsPresenter {
    public ToolSpecification createListResourcesSpecification();

    public ToolExecutor createListResourcesExecutor(List<McpClient> var1);

    public ToolSpecification createGetResourceSpecification();

    public ToolExecutor createGetResourceExecutor(List<McpClient> var1);
}

