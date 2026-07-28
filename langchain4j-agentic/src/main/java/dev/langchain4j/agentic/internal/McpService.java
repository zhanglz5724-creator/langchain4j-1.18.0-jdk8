/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.internal.AgentExecutor;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.internal.McpClientBuilder;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;

public interface McpService {
    public <T> McpClientBuilder<T> mcpBuilder(Object var1, Class<T> var2);

    public Optional<AgentExecutor> methodToAgentExecutor(InternalAgent var1, Method var2);

    public static McpService get() {
        return Provider.mcpService;
    }

    public static class DummyMcpService
    implements McpService {
        private DummyMcpService() {
        }

        @Override
        public <T> McpClientBuilder<T> mcpBuilder(Object mcpClient, Class<T> agentServiceClass) {
            throw DummyMcpService.noMcpException();
        }

        @Override
        public Optional<AgentExecutor> methodToAgentExecutor(InternalAgent agent, Method method) {
            return Optional.empty();
        }

        private static UnsupportedOperationException noMcpException() {
            return new UnsupportedOperationException("No MCP service implementation found. Please add 'langchain4j-agentic-mcp' to your dependencies.");
        }
    }

    public static class Provider {
        static McpService mcpService = Provider.loadMcpService();

        private Provider() {
        }

        private static McpService loadMcpService() {
            ServiceLoader<McpService> loader = ServiceLoader.load(McpService.class);
            Iterator<McpService> iterator = loader.iterator();
            if (iterator.hasNext()) {
                McpService service = iterator.next();
                return service;
            }
            return new DummyMcpService();
        }
    }
}

