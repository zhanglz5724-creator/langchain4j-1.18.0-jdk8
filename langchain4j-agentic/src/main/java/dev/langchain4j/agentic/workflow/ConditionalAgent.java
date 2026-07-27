package dev.langchain4j.agentic.workflow;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.List;
import java.util.function.Predicate;
public class ConditionalAgent {
    private final String condition;
    private final Predicate<AgenticScope> predicate;
    private final List<AgentInstance> agentInstances;

    public ConditionalAgent(String condition, Predicate<AgenticScope> predicate, List<AgentInstance> agentInstances) {
        this.condition = condition;
        this.predicate = predicate;
        this.agentInstances = agentInstances;
    }

    public String getCondition() {
        return condition;
    }

    public Predicate<AgenticScope> getPredicate() {
        return predicate;
    }

    public List<AgentInstance> getAgentInstances() {
        return agentInstances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionalAgent that = (ConditionalAgent) o;
        return java.util.Objects.equals(this.condition, that.condition) && java.util.Objects.equals(this.predicate, that.predicate) && java.util.Objects.equals(this.agentInstances, that.agentInstances);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(condition, predicate, agentInstances);
    }

    @Override
    public String toString() {
        return "ConditionalAgent{"condition=" + condition + , "predicate=" + predicate + , "agentInstances=" + agentInstances + "}"";
    }

}
