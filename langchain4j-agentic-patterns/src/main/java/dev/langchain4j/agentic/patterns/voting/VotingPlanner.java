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
package dev.langchain4j.agentic.patterns.voting;

import dev.langchain4j.agentic.patterns.voting.VotingStrategy;
import dev.langchain4j.agentic.planner.Action;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.InitPlanningContext;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.planner.PlanningContext;
import java.util.ArrayList;
import java.util.List;

public class VotingPlanner
implements Planner {
    private final VotingStrategy strategy;
    private List<AgentInstance> subagents;
    private int completedCount;
    private final List<Object> votes = new ArrayList<Object>();

    public VotingPlanner() {
        this(VotingStrategy.majority());
    }

    public VotingPlanner(VotingStrategy strategy) {
        this.strategy = strategy;
    }

    public void init(InitPlanningContext initPlanningContext) {
        this.subagents = initPlanningContext.subagents();
    }

    public Action firstAction(PlanningContext planningContext) {
        if (this.subagents.isEmpty()) {
            return this.done();
        }
        return this.call(this.subagents);
    }

    public Action nextAction(PlanningContext planningContext) {
        this.votes.add(planningContext.previousAgentInvocation().output());
        ++this.completedCount;
        if (this.completedCount < this.subagents.size()) {
            return this.noOp();
        }
        return this.done(this.strategy.aggregate(this.votes));
    }

    public AgenticSystemTopology topology() {
        return AgenticSystemTopology.PARALLEL;
    }
}

