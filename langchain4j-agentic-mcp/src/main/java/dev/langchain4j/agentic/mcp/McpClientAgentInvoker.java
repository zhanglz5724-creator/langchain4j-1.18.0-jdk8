/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.UntypedAgent
 *  dev.langchain4j.agentic.agent.MissingArgumentException
 *  dev.langchain4j.agentic.internal.AgentInvocationArguments
 *  dev.langchain4j.agentic.internal.AgentInvoker
 *  dev.langchain4j.agentic.internal.InternalAgent
 *  dev.langchain4j.agentic.observability.AgentListener
 *  dev.langchain4j.agentic.planner.AgentArgument
 *  dev.langchain4j.agentic.planner.AgentInstance
 *  dev.langchain4j.agentic.planner.AgenticSystemTopology
 *  dev.langchain4j.agentic.planner.Planner
 *  dev.langchain4j.agentic.scope.AgenticScope
 */
package dev.langchain4j.agentic.mcp;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.agent.MissingArgumentException;
import dev.langchain4j.agentic.internal.AgentInvocationArguments;
import dev.langchain4j.agentic.internal.AgentInvoker;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.mcp.McpClientInstance;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class McpClientAgentInvoker
implements AgentInvoker {
    private String agentId;
    private final String[] inputKeys;
    private final McpClientInstance mcpClientInstance;
    private final String toolName;
    private final String toolDescription;
    private final Method method;
    private InternalAgent parent;

    public McpClientAgentInvoker(McpClientInstance mcpClientInstance, Method method) {
        this.method = method;
        this.mcpClientInstance = mcpClientInstance;
        this.toolName = mcpClientInstance.toolName();
        this.toolDescription = mcpClientInstance.toolDescription();
        this.agentId = this.name();
        this.inputKeys = this.inputKeys(mcpClientInstance);
    }

    private String[] inputKeys(McpClientInstance mcpClientInstance) {
        return this.isUntyped() ? mcpClientInstance.inputKeys() : (String[])Stream.of(this.method.getParameters()).map(AgentInvoker::parameterName).toArray(String[]::new);
    }

    public String name() {
        return this.toolName;
    }

    public String agentId() {
        return this.agentId;
    }

    public String description() {
        return this.toolDescription;
    }

    public Class<?> type() {
        return Object.class;
    }

    public Class<? extends Planner> plannerType() {
        return null;
    }

    public Type outputType() {
        return Object.class;
    }

    public String outputKey() {
        return this.mcpClientInstance.outputKey();
    }

    public boolean async() {
        return this.mcpClientInstance.async();
    }

    public Method method() {
        return this.method;
    }

    public List<AgentArgument> arguments() {
        return Stream.of(this.inputKeys).map(input -> new AgentArgument(Object.class, input)).toList();
    }

    public List<AgentInstance> subagents() {
        return List.of();
    }

    public AgentInvocationArguments toInvocationArguments(AgenticScope agenticScope) {
        return this.isUntyped() ? new AgentInvocationArguments(agenticScope.state(), new Object[]{agenticScope.state()}) : this.agentInvocationArguments(agenticScope);
    }

    private AgentInvocationArguments agentInvocationArguments(AgenticScope agenticScope) {
        HashMap<String, Object> namedArgs = new HashMap<String, Object>();
        Object[] positionalArgs = new Object[this.inputKeys.length];
        int i = 0;
        for (String argName : this.inputKeys) {
            Object argValue = agenticScope.readState(argName);
            if (argValue == null) {
                throw new MissingArgumentException(argName);
            }
            positionalArgs[i++] = argValue;
            namedArgs.put(argName, argValue);
        }
        return new AgentInvocationArguments(namedArgs, positionalArgs);
    }

    private boolean isUntyped() {
        return this.method.getDeclaringClass() == UntypedAgent.class;
    }

    public AgentListener listener() {
        return this.mcpClientInstance.listener();
    }

    public AgenticSystemTopology topology() {
        return this.mcpClientInstance.topology();
    }

    public AgentInstance parent() {
        return this.parent;
    }

    public void setParent(InternalAgent parent) {
        this.parent = parent;
    }

    public void registerInheritedParentListener(AgentListener parentListener) {
        this.mcpClientInstance.registerInheritedParentListener(parentListener);
    }

    public void appendId(String idSuffix) {
        this.agentId = this.agentId + idSuffix;
    }
}

