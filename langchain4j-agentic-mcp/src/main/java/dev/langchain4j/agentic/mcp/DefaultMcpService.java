/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.Agent
 *  dev.langchain4j.agentic.declarative.McpClientAgent
 *  dev.langchain4j.agentic.internal.AgentExecutor
 *  dev.langchain4j.agentic.internal.AgentInvoker
 *  dev.langchain4j.agentic.internal.InternalAgent
 *  dev.langchain4j.agentic.internal.McpClientBuilder
 *  dev.langchain4j.agentic.internal.McpService
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.mcp.client.McpClient
 */
package dev.langchain4j.agentic.mcp;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.declarative.McpClientAgent;
import dev.langchain4j.agentic.internal.AgentExecutor;
import dev.langchain4j.agentic.internal.AgentInvoker;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.internal.McpClientBuilder;
import dev.langchain4j.agentic.internal.McpService;
import dev.langchain4j.agentic.mcp.DefaultMcpClientBuilder;
import dev.langchain4j.agentic.mcp.McpClientAgentInvoker;
import dev.langchain4j.agentic.mcp.McpClientInstance;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.mcp.client.McpClient;
import java.lang.reflect.Method;
import java.util.Optional;

public class DefaultMcpService
implements McpService {
    public <T> McpClientBuilder<T> mcpBuilder(Object mcpClient, Class<T> agentServiceClass) {
        return new DefaultMcpClientBuilder<T>((McpClient)mcpClient, agentServiceClass);
    }

    public Optional<AgentExecutor> methodToAgentExecutor(InternalAgent agent, Method method) {
        if (agent instanceof McpClientInstance) {
            McpClientInstance mcpAgent = (McpClientInstance)agent;
            Optional<AgentExecutor> mcpAgentExecutor = Utils.getAnnotatedMethod((Method)method, Agent.class).map(agentMethod -> new AgentExecutor((AgentInvoker)new McpClientAgentInvoker(mcpAgent, (Method)agentMethod), (Object)mcpAgent));
            if (mcpAgentExecutor.isEmpty()) {
                mcpAgentExecutor = Utils.getAnnotatedMethod((Method)method, McpClientAgent.class).map(agentMethod -> new AgentExecutor((AgentInvoker)new McpClientAgentInvoker(mcpAgent, (Method)agentMethod), (Object)mcpAgent));
            }
            return mcpAgentExecutor;
        }
        return Optional.empty();
    }
}

