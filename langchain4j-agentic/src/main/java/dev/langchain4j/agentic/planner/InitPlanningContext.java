package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.scope.AgenticScope;

import java.util.List;
public class InitPlanningContext {
    private final AgenticScope agenticScope;
    private final AgentInstance plannerAgent;
    private final List<AgentInstance> subagents;

    public InitPlanningContext(AgenticScope agenticScope, AgentInstance plannerAgent, List<AgentInstance> subagents) {
        this.agenticScope = agenticScope;
        this.plannerAgent = plannerAgent;
        this.subagents = subagents;
    }

    public AgenticScope getAgenticScope() {
        return agenticScope;
    }

    public AgentInstance getPlannerAgent() {
        return plannerAgent;
    }

    public List<AgentInstance> getSubagents() {
        return subagents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InitPlanningContext that = (InitPlanningContext) o;
        return java.util.Objects.equals(this.agenticScope, that.agenticScope) && java.util.Objects.equals(this.plannerAgent, that.plannerAgent) && java.util.Objects.equals(this.subagents, that.subagents);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(agenticScope, plannerAgent, subagents);
    }

    @Override
    public String toString() {
        return "InitPlanningContext{"agenticScope=" + agenticScope + , "plannerAgent=" + plannerAgent + , "subagents=" + subagents + "}"";
    }

}
