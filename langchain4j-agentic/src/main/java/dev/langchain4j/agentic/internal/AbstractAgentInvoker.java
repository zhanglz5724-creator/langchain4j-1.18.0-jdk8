/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.agent.AgentInvocationException;
import dev.langchain4j.agentic.agent.MissingArgumentException;
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
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public abstract class AbstractAgentInvoker
implements AgentInvoker,
InternalAgent {
    protected final Method method;
    protected final InternalAgent agent;

    protected AbstractAgentInvoker(Method method, InternalAgent agent) {
        this.method = method;
        this.agent = agent;
    }

    @Override
    public Class<?> type() {
        return this.agent.type();
    }

    @Override
    public Class<? extends Planner> plannerType() {
        return this.agent.plannerType();
    }

    @Override
    public String name() {
        return this.agent.name();
    }

    @Override
    public String agentId() {
        return this.agent.agentId();
    }

    @Override
    public String description() {
        return this.agent.description();
    }

    @Override
    public Type outputType() {
        return this.agent.outputType();
    }

    @Override
    public String outputKey() {
        return this.agent.outputKey();
    }

    @Override
    public List<AgentArgument> arguments() {
        return this.agent.arguments();
    }

    @Override
    public List<AgentInstance> subagents() {
        return this.agent.subagents();
    }

    @Override
    public boolean async() {
        return this.agent.async();
    }

    @Override
    public boolean optional() {
        return this.agent.optional();
    }

    @Override
    public AgentInvocationArguments toInvocationArguments(AgenticScope agenticScope) throws MissingArgumentException {
        return AgentUtil.agentInvocationArguments(agenticScope, this.arguments());
    }

    @Override
    public Object invoke(DefaultAgenticScope agenticScope, Object agent, AgentInvocationArguments args) throws AgentInvocationException {
        return AgentInvoker.super.invoke(agenticScope, agent, args);
    }

    @Override
    public AgentListener listener() {
        return this.agent.listener();
    }

    @Override
    public AgenticSystemTopology topology() {
        return this.agent.topology();
    }

    @Override
    public Method method() {
        return this.method;
    }

    @Override
    public AgentInstance parent() {
        return this.agent.parent();
    }

    @Override
    public <T extends AgentInstance> T as(Class<T> agentInstanceClass) {
        return this.agent.as(agentInstanceClass);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        AbstractAgentInvoker that = (AbstractAgentInvoker)obj;
        return Objects.equals(this.method, that.method) && Objects.equals(this.agent, that.agent);
    }

    public int hashCode() {
        return Objects.hash(this.method, this.agent);
    }

    public String toString() {
        return "MethodAgentInvoker[method=" + this.method + ", agentInstance=" + this.agent + ']';
    }

    @Override
    public void setParent(InternalAgent parent) {
        this.agent.setParent(parent);
    }

    @Override
    public void registerInheritedParentListener(AgentListener parentListener) {
        this.agent.registerInheritedParentListener(parentListener);
    }

    @Override
    public void appendId(String idSuffix) {
        this.agent.appendId(idSuffix);
    }
}

