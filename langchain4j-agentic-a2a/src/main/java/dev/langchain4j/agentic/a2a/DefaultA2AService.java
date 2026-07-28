/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.Agent
 *  dev.langchain4j.agentic.declarative.A2AClientAgent
 *  dev.langchain4j.agentic.internal.A2AClientBuilder
 *  dev.langchain4j.agentic.internal.A2AService
 *  dev.langchain4j.agentic.internal.AgentExecutor
 *  dev.langchain4j.agentic.internal.AgentInvoker
 *  dev.langchain4j.agentic.internal.InternalAgent
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.agentic.a2a;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.a2a.A2AClientAgentInvoker;
import dev.langchain4j.agentic.a2a.A2AClientInstance;
import dev.langchain4j.agentic.a2a.DefaultA2AClientBuilder;
import dev.langchain4j.agentic.declarative.A2AClientAgent;
import dev.langchain4j.agentic.internal.A2AClientBuilder;
import dev.langchain4j.agentic.internal.A2AService;
import dev.langchain4j.agentic.internal.AgentExecutor;
import dev.langchain4j.agentic.internal.AgentInvoker;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.internal.Utils;
import java.lang.reflect.Method;
import java.util.Optional;

public class DefaultA2AService
implements A2AService {
    public <T> A2AClientBuilder<T> a2aBuilder(String a2aServerUrl, Class<T> agentServiceClass) {
        return new DefaultA2AClientBuilder<T>(a2aServerUrl, agentServiceClass);
    }

    public Optional<AgentExecutor> methodToAgentExecutor(InternalAgent agent, Method method) {
        if (agent instanceof A2AClientInstance) {
            A2AClientInstance a2aAgent = (A2AClientInstance)agent;
            Optional<AgentExecutor> a2aAgentExecutor = Utils.getAnnotatedMethod((Method)method, Agent.class).map(agentMethod -> new AgentExecutor((AgentInvoker)new A2AClientAgentInvoker(a2aAgent, (Method)agentMethod), (Object)a2aAgent));
            if (a2aAgentExecutor.isEmpty()) {
                a2aAgentExecutor = Utils.getAnnotatedMethod((Method)method, A2AClientAgent.class).map(agentMethod -> new AgentExecutor((AgentInvoker)new A2AClientAgentInvoker(a2aAgent, (Method)agentMethod), (Object)a2aAgent));
            }
            return a2aAgentExecutor;
        }
        return Optional.empty();
    }
}

