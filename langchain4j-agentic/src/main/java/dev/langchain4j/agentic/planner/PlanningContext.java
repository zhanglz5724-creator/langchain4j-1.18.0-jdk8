package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.scope.AgentInvocation;
import dev.langchain4j.agentic.scope.AgenticScope;
public class PlanningContext {
    private final AgenticScope agenticScope;
    private final AgentInvocation previousAgentInvocation;

    public PlanningContext(AgenticScope agenticScope, AgentInvocation previousAgentInvocation) {
        this.agenticScope = agenticScope;
        this.previousAgentInvocation = previousAgentInvocation;
    }

    public AgenticScope getAgenticScope() {
        return agenticScope;
    }

    public AgentInvocation getPreviousAgentInvocation() {
        return previousAgentInvocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanningContext that = (PlanningContext) o;
        return java.util.Objects.equals(this.agenticScope, that.agenticScope) && java.util.Objects.equals(this.previousAgentInvocation, that.previousAgentInvocation);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(agenticScope, previousAgentInvocation);
    }

    @Override
    public String toString() {
        return "PlanningContext{"agenticScope=" + agenticScope + , "previousAgentInvocation=" + previousAgentInvocation + "}"";
    }

}
