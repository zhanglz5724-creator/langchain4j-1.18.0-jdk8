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
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.workflow.LoopAgentInstance;
import dev.langchain4j.agentic.workflow.impl.DefaultLoopAgentInstance;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public class LoopPlanner
implements Planner {
    private final int maxIterations;
    private int iterationsCounter = 1;
    private final boolean testExitAtLoopEnd;
    private final BiPredicate<AgenticScope, Integer> exitCondition;
    private final String exitConditionDescription;
    private List<AgentInstance> agents;
    private int agentCursor = 0;

    public LoopPlanner(int maxIterations, boolean testExitAtLoopEnd, BiPredicate<AgenticScope, Integer> exitCondition, String exitConditionDescription) {
        this.maxIterations = maxIterations;
        this.testExitAtLoopEnd = testExitAtLoopEnd;
        this.exitCondition = exitCondition;
        this.exitConditionDescription = exitConditionDescription;
    }

    @Override
    public void init(InitPlanningContext initPlanningContext) {
        this.agents = initPlanningContext.subagents();
    }

    @Override
    public Action firstAction(PlanningContext planningContext) {
        return this.call(this.agents.get(this.agentCursor));
    }

    @Override
    public Action nextAction(PlanningContext planningContext) {
        this.agentCursor = (this.agentCursor + 1) % this.agents.size();
        if (this.agentCursor == 0) {
            if (this.iterationsCounter >= this.maxIterations || this.exitCondition.test(planningContext.agenticScope(), this.iterationsCounter)) {
                return this.done();
            }
            ++this.iterationsCounter;
        } else if (!this.testExitAtLoopEnd && this.exitCondition.test(planningContext.agenticScope(), this.iterationsCounter)) {
            return this.done();
        }
        return this.call(this.agents.get(this.agentCursor));
    }

    @Override
    public AgenticSystemTopology topology() {
        return AgenticSystemTopology.LOOP;
    }

    @Override
    public <T extends AgentInstance> T as(Class<T> agentInstanceClass, AgentInstance agentInstance) {
        if (agentInstanceClass != LoopAgentInstance.class) {
            throw new ClassCastException("Cannot cast to " + agentInstanceClass.getName() + ": incompatible type");
        }
        return (T)new DefaultLoopAgentInstance(agentInstance, this);
    }

    public int maxIterations() {
        return this.maxIterations;
    }

    public boolean testExitAtLoopEnd() {
        return this.testExitAtLoopEnd;
    }

    public String exitCondition() {
        return this.exitConditionDescription;
    }

    @Override
    public Map<String, Object> executionState() {
        HashMap<String, Object> m = new HashMap<String, Object>();
        m.put("cursor", this.agentCursor);
        m.put("iteration", this.iterationsCounter);
        return m;
    }

    @Override
    public void restoreExecutionState(Map<String, Object> state) {
        Object savedIteration;
        Object savedCursor = state.get("cursor");
        if (savedCursor instanceof Number) {
            Number n = (Number)savedCursor;
            this.agentCursor = n.intValue();
        }
        if ((savedIteration = state.get("iteration")) instanceof Number) {
            Number n = (Number)savedIteration;
            this.iterationsCounter = n.intValue();
        }
    }
}

