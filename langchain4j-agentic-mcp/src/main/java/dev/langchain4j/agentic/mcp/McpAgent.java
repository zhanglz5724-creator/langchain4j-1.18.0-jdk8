/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.UntypedAgent
 *  dev.langchain4j.agentic.internal.McpClientBuilder
 *  dev.langchain4j.mcp.client.McpClient
 */
package dev.langchain4j.agentic.mcp;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.internal.McpClientBuilder;
import dev.langchain4j.agentic.mcp.DefaultMcpClientBuilder;
import dev.langchain4j.mcp.client.McpClient;

public class McpAgent {
    private McpAgent() {
    }

    public static McpClientBuilder<UntypedAgent> builder(McpClient mcpClient) {
        return McpAgent.builder(mcpClient, UntypedAgent.class);
    }

    public static <T> McpClientBuilder<T> builder(McpClient mcpClient, Class<T> agentServiceClass) {
        return new DefaultMcpClientBuilder<T>(mcpClient, agentServiceClass);
    }
}

