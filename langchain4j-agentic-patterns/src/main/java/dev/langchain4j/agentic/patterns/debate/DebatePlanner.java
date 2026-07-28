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
 *  dev.langchain4j.agentic.scope.AgenticScope
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.agentic.patterns.debate;

import dev.langchain4j.agentic.patterns.debate.ConvergenceStrategy;
import dev.langchain4j.agentic.planner.Action;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.InitPlanningContext;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.planner.PlanningContext;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebatePlanner
implements Planner {
    private static final Logger LOG = LoggerFactory.getLogger(DebatePlanner.class);
    public static final String DEBATE_CONTEXT_KEY = "debateContext";
    private static final int DEFAULT_MAX_ROUNDS = 3;
    private final int maxRounds;
    private final ConvergenceStrategy convergenceStrategy;
    private List<AgentInstance> debaters;
    private AgentInstance judge;
    private int currentRound = 1;
    private int completedInRound = 0;
    private final Map<String, Object> lastDebatersMessages = new HashMap<String, Object>();
    private boolean judgePhase = false;

    public DebatePlanner() {
        this(3, ConvergenceStrategy.unanimous());
    }

    public DebatePlanner(int maxRounds) {
        this(maxRounds, ConvergenceStrategy.unanimous());
    }

    public DebatePlanner(ConvergenceStrategy convergenceStrategy) {
        this(3, convergenceStrategy);
    }

    public DebatePlanner(int maxRounds, ConvergenceStrategy convergenceStrategy) {
        this.maxRounds = maxRounds;
        this.convergenceStrategy = convergenceStrategy;
    }

    public void init(InitPlanningContext initPlanningContext) {
        List subagents = initPlanningContext.subagents();
        if (subagents.size() < 3) {
            throw new IllegalArgumentException("DebatePlanner requires at least 3 subagents (2 debaters + 1 judge), got " + subagents.size());
        }
        this.debaters = new ArrayList<AgentInstance>(subagents.subList(0, subagents.size() - 1));
        this.judge = (AgentInstance)subagents.get(subagents.size() - 1);
    }

    public Action firstAction(PlanningContext planningContext) {
        planningContext.agenticScope().writeState(DEBATE_CONTEXT_KEY, (Object)"");
        LOG.info("Starting debate round 1 with {} debaters", (Object)this.debaters.size());
        return this.call(this.debaters);
    }

    public Action nextAction(PlanningContext planningContext) {
        if (this.judgePhase) {
            return this.done(planningContext.previousAgentInvocation().output());
        }
        this.lastDebatersMessages.put(planningContext.previousAgentInvocation().agentName(), planningContext.previousAgentInvocation().output());
        ++this.completedInRound;
        if (this.completedInRound < this.debaters.size()) {
            return this.noOp();
        }
        AgenticScope scope = planningContext.agenticScope();
        if (this.convergenceStrategy.hasConverged(this.lastDebatersMessages.values())) {
            LOG.info("Convergence reached after {} rounds", (Object)this.currentRound);
            this.judgePhase = true;
        }
        if (this.currentRound >= this.maxRounds) {
            LOG.info("Max rounds ({}) reached without convergence, invoking judge", (Object)this.maxRounds);
            this.judgePhase = true;
        }
        if (this.judgePhase) {
            this.writeDebateContext(scope);
            return this.call(new AgentInstance[]{this.judge});
        }
        ++this.currentRound;
        this.completedInRound = 0;
        this.writeDebateContext(scope);
        LOG.info("Starting debate round {}", (Object)this.currentRound);
        return this.call(this.debaters);
    }

    private void writeDebateContext(AgenticScope scope) {
        String debatersContext = this.lastDebatersMessages.entrySet().stream().map(e -> (String)e.getKey() + ": \"" + e.getValue() + "\"").collect(Collectors.joining("\n"));
        scope.writeState(DEBATE_CONTEXT_KEY, (Object)debatersContext);
    }

    public Map<String, Object> executionState() {
        HashMap<String, Object> m = new HashMap<String, Object>();
        m.put("currentRound", this.currentRound);
        m.put("lastDebatersMessages", this.lastDebatersMessages);
        return m;
    }

    public void restoreExecutionState(Map<String, Object> state) {
        Object savedRound = state.get("currentRound");
        if (savedRound instanceof Number) {
            Number n = (Number)savedRound;
            this.currentRound = n.intValue();
        }
    }

    public AgenticSystemTopology topology() {
        return AgenticSystemTopology.STAR;
    }
}

