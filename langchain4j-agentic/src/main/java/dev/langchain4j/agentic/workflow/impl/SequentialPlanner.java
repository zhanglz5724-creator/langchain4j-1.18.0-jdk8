/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow.impl;

import dev.langchain4j.agentic.planner.Action;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.InitPlanningContext;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.planner.PlanningContext;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SequentialPlanner
implements Planner {
    private List<AgentInstance> agents;
    private int agentCursor = 0;

    @Override
    public void init(InitPlanningContext initPlanningContext) {
        this.agents = initPlanningContext.subagents();
    }

    @Override
    public Action nextAction(PlanningContext planningContext) {
        return this.terminated() ? this.done() : this.call(this.agents.get(this.agentCursor++));
    }

    @Override
    public AgenticSystemTopology topology() {
        return AgenticSystemTopology.SEQUENCE;
    }

    @Override
    public boolean terminated() {
        return this.agentCursor >= this.agents.size();
    }

    @Override
    public Map<String, Object> executionState() {
        return this.agentCursor > 0 ? Collections.singletonMap("cursor", this.agentCursor - 1) : Collections.emptyMap();
    }

    @Override
    public void restoreExecutionState(Map<String, Object> state) {
        Object savedCursor = state.get("cursor");
        if (savedCursor instanceof Number) {
            Number n = (Number)savedCursor;
            this.agentCursor = n.intValue();
        }
    }
}

