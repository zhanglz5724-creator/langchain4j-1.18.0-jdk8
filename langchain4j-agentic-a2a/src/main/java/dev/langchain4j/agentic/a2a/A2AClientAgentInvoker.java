/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.UntypedAgent
 *  dev.langchain4j.agentic.internal.AgentInvocationArguments
 *  dev.langchain4j.agentic.internal.AgentInvoker
 *  dev.langchain4j.agentic.internal.AgentUtil
 *  dev.langchain4j.agentic.internal.InternalAgent
 *  dev.langchain4j.agentic.observability.AgentListener
 *  dev.langchain4j.agentic.planner.AgentArgument
 *  dev.langchain4j.agentic.planner.AgentInstance
 *  dev.langchain4j.agentic.planner.AgenticSystemTopology
 *  dev.langchain4j.agentic.planner.Planner
 *  dev.langchain4j.agentic.scope.AgenticScope
 *  dev.langchain4j.service.ParameterNameResolver
 *  org.a2aproject.sdk.spec.AgentCard
 */
package dev.langchain4j.agentic.a2a;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.a2a.A2AClientInstance;
import dev.langchain4j.agentic.a2a.A2AContextId;
import dev.langchain4j.agentic.a2a.A2ATaskId;
import dev.langchain4j.agentic.internal.AgentInvocationArguments;
import dev.langchain4j.agentic.internal.AgentInvoker;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.service.ParameterNameResolver;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.a2aproject.sdk.spec.AgentCard;

public class A2AClientAgentInvoker
implements AgentInvoker {
    private String agentId;
    private final List<AgentArgument> arguments;
    private final A2AClientInstance a2AClientInstance;
    private final AgentCard agentCard;
    private final Method method;
    private InternalAgent parent;

    public A2AClientAgentInvoker(A2AClientInstance a2AClientInstance, Method method) {
        this.method = method;
        this.a2AClientInstance = a2AClientInstance;
        this.agentCard = a2AClientInstance.agentCard();
        this.agentId = this.name();
        this.arguments = this.arguments(a2AClientInstance);
    }

    private List<AgentArgument> arguments(A2AClientInstance a2AClientInstance) {
        Set a2aArgs = Stream.of(this.method.getParameters()).filter(p -> p.isAnnotationPresent(A2AContextId.class) || p.isAnnotationPresent(A2ATaskId.class)).map(ParameterNameResolver::name).collect(Collectors.toSet());
        return this.isUntyped() ? Stream.of(a2AClientInstance.inputKeys()).map(input -> new AgentArgument(Object.class, input)).toList() : AgentUtil.argumentsFromMethod((Method)this.method, a2aArgs);
    }

    public String name() {
        return this.agentCard.name();
    }

    public String agentId() {
        return this.agentId;
    }

    public String description() {
        return this.agentCard.description();
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
        return this.a2AClientInstance.outputKey();
    }

    public boolean async() {
        return this.a2AClientInstance.async();
    }

    public Method method() {
        return this.method;
    }

    public List<AgentArgument> arguments() {
        return this.arguments;
    }

    public List<AgentInstance> subagents() {
        return List.of();
    }

    public AgentInvocationArguments toInvocationArguments(AgenticScope agenticScope) {
        return this.isUntyped() ? new AgentInvocationArguments(agenticScope.state(), new Object[]{agenticScope.state()}) : AgentUtil.agentInvocationArguments((AgenticScope)agenticScope, this.arguments);
    }

    private boolean isUntyped() {
        return this.method.getDeclaringClass() == UntypedAgent.class;
    }

    public AgentListener listener() {
        return this.a2AClientInstance.listener();
    }

    public AgenticSystemTopology topology() {
        return this.a2AClientInstance.topology();
    }

    public AgentInstance parent() {
        return this.parent;
    }

    public void setParent(InternalAgent parent) {
        this.parent = parent;
    }

    public void registerInheritedParentListener(AgentListener parentListener) {
        this.a2AClientInstance.registerInheritedParentListener(parentListener);
    }

    public void appendId(String idSuffix) {
        this.agentId = this.agentId + idSuffix;
    }
}

