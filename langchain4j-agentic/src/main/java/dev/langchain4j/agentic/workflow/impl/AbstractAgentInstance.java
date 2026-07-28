/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow.impl;

import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.Planner;
import java.lang.reflect.Type;
import java.util.List;

abstract class AbstractAgentInstance
implements AgentInstance {
    private final AgentInstance delegate;

    AbstractAgentInstance(AgentInstance delegate) {
        this.delegate = delegate;
    }

    @Override
    public Class<?> type() {
        return this.delegate.type();
    }

    @Override
    public Class<? extends Planner> plannerType() {
        return this.delegate.plannerType();
    }

    @Override
    public String name() {
        return this.delegate.name();
    }

    @Override
    public String agentId() {
        return this.delegate.agentId();
    }

    @Override
    public String description() {
        return this.delegate.description();
    }

    @Override
    public Type outputType() {
        return this.delegate.outputType();
    }

    @Override
    public String outputKey() {
        return this.delegate.outputKey();
    }

    @Override
    public boolean async() {
        return this.delegate.async();
    }

    @Override
    public List<AgentArgument> arguments() {
        return this.delegate.arguments();
    }

    @Override
    public AgentInstance parent() {
        return this.delegate.parent();
    }

    @Override
    public List<AgentInstance> subagents() {
        return this.delegate.subagents();
    }

    @Override
    public boolean leaf() {
        return this.delegate.leaf();
    }

    @Override
    public AgenticSystemTopology topology() {
        return this.delegate.topology();
    }

    @Override
    public <T extends AgentInstance> T as(Class<T> agentInstanceClass) {
        return this.delegate.as(agentInstanceClass);
    }
}

