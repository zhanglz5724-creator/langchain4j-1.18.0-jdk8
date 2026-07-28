/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.scope.AgentInvocation;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Objects;

public class PlanningContext {
    private final AgenticScope agenticScope;
    private final AgentInvocation previousAgentInvocation;

    public PlanningContext(AgenticScope agenticScope, AgentInvocation previousAgentInvocation) {
        this.agenticScope = agenticScope;
        this.previousAgentInvocation = previousAgentInvocation;
    }

    public AgenticScope agenticScope() {
        return this.agenticScope;
    }

    public AgentInvocation previousAgentInvocation() {
        return this.previousAgentInvocation;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanningContext)) {
            return false;
        }
        PlanningContext other = (PlanningContext)o;
        if (!Objects.equals(this.agenticScope, other.agenticScope)) {
            return false;
        }
        return Objects.equals(this.previousAgentInvocation, other.previousAgentInvocation);
    }

    public int hashCode() {
        return Objects.hash(this.agenticScope, this.previousAgentInvocation);
    }

    public String toString() {
        return "PlanningContext{agenticScope=" + this.agenticScope + ", previousAgentInvocation=" + this.previousAgentInvocation + "}";
    }
}

