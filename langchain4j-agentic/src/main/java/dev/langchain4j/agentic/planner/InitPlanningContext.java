/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.List;
import java.util.Objects;

public class InitPlanningContext {
    private final AgenticScope agenticScope;
    private final AgentInstance plannerAgent;
    private final List<AgentInstance> subagents;

    public InitPlanningContext(AgenticScope agenticScope, AgentInstance plannerAgent, List<AgentInstance> subagents) {
        this.agenticScope = agenticScope;
        this.plannerAgent = plannerAgent;
        this.subagents = subagents;
    }

    public AgenticScope agenticScope() {
        return this.agenticScope;
    }

    public AgentInstance plannerAgent() {
        return this.plannerAgent;
    }

    public List<AgentInstance> subagents() {
        return this.subagents;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InitPlanningContext)) {
            return false;
        }
        InitPlanningContext other = (InitPlanningContext)o;
        if (!Objects.equals(this.agenticScope, other.agenticScope)) {
            return false;
        }
        if (!Objects.equals(this.plannerAgent, other.plannerAgent)) {
            return false;
        }
        return Objects.equals(this.subagents, other.subagents);
    }

    public int hashCode() {
        return Objects.hash(this.agenticScope, this.plannerAgent, this.subagents);
    }

    public String toString() {
        return "InitPlanningContext{agenticScope=" + this.agenticScope + ", plannerAgent=" + this.plannerAgent + ", subagents=" + this.subagents + "}";
    }
}

