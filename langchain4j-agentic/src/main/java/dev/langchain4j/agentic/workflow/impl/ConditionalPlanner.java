/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow.impl;

import dev.langchain4j.agentic.planner.Action;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.planner.PlanningContext;
import dev.langchain4j.agentic.workflow.ConditionalAgent;
import dev.langchain4j.agentic.workflow.ConditionalAgentInstance;
import dev.langchain4j.agentic.workflow.impl.DefaultConditionalAgentInstance;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConditionalPlanner
implements Planner {
    private final List<ConditionalAgent> conditionalSubagents;

    public ConditionalPlanner(List<ConditionalAgent> conditionalSubagents) {
        this.conditionalSubagents = conditionalSubagents;
    }

    public List<ConditionalAgent> conditionalSubagents() {
        return this.conditionalSubagents;
    }

    @Override
    public Action firstAction(PlanningContext planningContext) {
        List<AgentInstance> agentsToCall = this.conditionalSubagents.stream().filter(conditionalAgent -> conditionalAgent.predicate().test(planningContext.agenticScope())).flatMap(conditionalAgent -> conditionalAgent.agentInstances().stream()).collect(Collectors.toList());
        return agentsToCall.isEmpty() ? this.done() : this.call(agentsToCall);
    }

    @Override
    public Action nextAction(PlanningContext planningContext) {
        return this.done();
    }

    @Override
    public AgenticSystemTopology topology() {
        return AgenticSystemTopology.ROUTER;
    }

    @Override
    public boolean terminated() {
        return true;
    }

    @Override
    public <T extends AgentInstance> T as(Class<T> agentInstanceClass, AgentInstance agentInstance) {
        if (agentInstanceClass != ConditionalAgentInstance.class) {
            throw new ClassCastException("Cannot cast to " + agentInstanceClass.getName() + ": incompatible type");
        }
        return (T)new DefaultConditionalAgentInstance(agentInstance, this);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConditionalPlanner)) {
            return false;
        }
        ConditionalPlanner other = (ConditionalPlanner)o;
        return Objects.equals(this.conditionalSubagents, other.conditionalSubagents);
    }

    public int hashCode() {
        return Objects.hash(this.conditionalSubagents);
    }

    public String toString() {
        return "ConditionalPlanner{conditionalSubagents=" + this.conditionalSubagents + "}";
    }
}

