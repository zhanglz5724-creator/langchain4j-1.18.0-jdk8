/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.planner.Action
 *  dev.langchain4j.agentic.planner.AgentInstance
 *  dev.langchain4j.agentic.planner.AgenticSystemTopology
 *  dev.langchain4j.agentic.planner.InitPlanningContext
 *  dev.langchain4j.agentic.planner.Planner
 *  dev.langchain4j.agentic.planner.PlanningContext
 */
package dev.langchain4j.agentic.patterns.goap;

import dev.langchain4j.agentic.patterns.goap.GoalOrientedSearchGraph;
import dev.langchain4j.agentic.planner.Action;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.InitPlanningContext;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.planner.PlanningContext;
import java.util.List;
import java.util.Map;

public class GoalOrientedPlanner
implements Planner {
    private String goal;
    private GoalOrientedSearchGraph graph;
    private List<AgentInstance> path;
    private int agentCursor = 0;

    public void init(InitPlanningContext initPlanningContext) {
        this.goal = initPlanningContext.plannerAgent().outputKey();
        this.graph = new GoalOrientedSearchGraph(initPlanningContext.subagents());
    }

    public Action firstAction(PlanningContext planningContext) {
        this.path = this.graph.search(planningContext.agenticScope().state().keySet(), this.goal);
        if (this.path.isEmpty()) {
            throw new IllegalStateException("No path found for goal: " + this.goal);
        }
        return this.call(new AgentInstance[]{this.path.get(this.agentCursor++)});
    }

    public Action nextAction(PlanningContext planningContext) {
        return this.agentCursor >= this.path.size() ? this.done() : this.call(new AgentInstance[]{this.path.get(this.agentCursor++)});
    }

    public void restoreExecutionState(Map<String, Object> state) {
    }

    public AgenticSystemTopology topology() {
        return AgenticSystemTopology.SEQUENCE;
    }
}

