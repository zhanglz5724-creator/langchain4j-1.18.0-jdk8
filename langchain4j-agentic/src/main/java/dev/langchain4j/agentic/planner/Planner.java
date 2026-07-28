/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.planner.Action;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.InitPlanningContext;
import dev.langchain4j.agentic.planner.PlanningContext;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface Planner {
    default public void init(InitPlanningContext initPlanningContext) {
    }

    default public Map<String, Object> executionState() {
        return Collections.emptyMap();
    }

    default public void restoreExecutionState(Map<String, Object> state) {
    }

    default public Action firstAction(PlanningContext planningContext) {
        return this.nextAction(planningContext);
    }

    default public AgenticSystemTopology topology() {
        return AgenticSystemTopology.SEQUENCE;
    }

    public Action nextAction(PlanningContext var1);

    default public boolean terminated() {
        return false;
    }

    default public Action noOp() {
        return Action.NoOpAction.INSTANCE;
    }

    default public Action call(AgentInstance ... agents) {
        return new Action.AgentCallAction(agents);
    }

    default public Action call(List<AgentInstance> agents) {
        return this.call(agents.toArray(new AgentInstance[0]));
    }

    default public Action done() {
        return Action.DoneAction.INSTANCE;
    }

    default public Action done(Object result) {
        return new Action.DoneWithResultAction(result);
    }

    default public Action suspend() {
        return Action.SuspendAction.INSTANCE;
    }

    default public <T extends AgentInstance> T as(Class<T> agentInstanceClass, AgentInstance agentInstance) {
        throw new ClassCastException("Cannot cast to " + agentInstanceClass.getName() + ": incompatible type");
    }
}

