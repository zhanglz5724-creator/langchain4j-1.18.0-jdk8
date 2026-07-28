/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.Planner;
import java.lang.reflect.Type;
import java.util.List;

public interface AgentInstance {
    public Class<?> type();

    public Class<? extends Planner> plannerType();

    public String name();

    public String agentId();

    public String description();

    public Type outputType();

    public String outputKey();

    public boolean async();

    default public boolean optional() {
        return false;
    }

    public List<AgentArgument> arguments();

    public AgentInstance parent();

    public List<AgentInstance> subagents();

    default public boolean leaf() {
        return this.subagents().isEmpty();
    }

    public AgenticSystemTopology topology();

    default public <T extends AgentInstance> T as(Class<T> agentInstanceClass) {
        throw new ClassCastException("Cannot cast to " + agentInstanceClass.getName() + ": incompatible type");
    }
}

