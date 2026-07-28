/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.planner.Action
 *  dev.langchain4j.agentic.planner.AgentArgument
 *  dev.langchain4j.agentic.planner.AgentInstance
 *  dev.langchain4j.agentic.planner.AgenticSystemTopology
 *  dev.langchain4j.agentic.planner.InitPlanningContext
 *  dev.langchain4j.agentic.planner.Planner
 *  dev.langchain4j.agentic.planner.PlanningContext
 *  dev.langchain4j.agentic.scope.AgenticScope
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.agentic.patterns.blackboard;

import dev.langchain4j.agentic.patterns.blackboard.ConflictResolutionStrategy;
import dev.langchain4j.agentic.planner.Action;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.InitPlanningContext;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.planner.PlanningContext;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlackboardPlanner
implements Planner {
    private static final Logger LOG = LoggerFactory.getLogger(BlackboardPlanner.class);
    private static final int DEFAULT_MAX_INVOCATIONS = 50;
    private Predicate<AgenticScope> goalPredicate;
    private final ConflictResolutionStrategy conflictResolutionStrategy;
    private final int maxInvocations;
    private Map<String, AgentActivator> agentActivators;
    private int invocationCounter = 0;

    public BlackboardPlanner() {
        this(null, 50, ConflictResolutionStrategy.DECLARATION_ORDER);
    }

    public BlackboardPlanner(Predicate<AgenticScope> goalPredicate) {
        this(goalPredicate, 50, ConflictResolutionStrategy.DECLARATION_ORDER);
    }

    public BlackboardPlanner(ConflictResolutionStrategy conflictResolutionStrategy) {
        this(null, 50, conflictResolutionStrategy);
    }

    public BlackboardPlanner(Predicate<AgenticScope> goalPredicate, int maxInvocations) {
        this(goalPredicate, maxInvocations, ConflictResolutionStrategy.DECLARATION_ORDER);
    }

    public BlackboardPlanner(Predicate<AgenticScope> goalPredicate, ConflictResolutionStrategy conflictResolutionStrategy) {
        this(goalPredicate, 50, conflictResolutionStrategy);
    }

    public BlackboardPlanner(Predicate<AgenticScope> goalPredicate, int maxInvocations, ConflictResolutionStrategy conflictResolutionStrategy) {
        this.goalPredicate = goalPredicate;
        this.maxInvocations = maxInvocations;
        this.conflictResolutionStrategy = conflictResolutionStrategy;
    }

    public void init(InitPlanningContext initPlanningContext) {
        if (this.goalPredicate == null) {
            String outputKey = initPlanningContext.plannerAgent().outputKey();
            this.goalPredicate = scope -> scope.hasState(outputKey);
        }
        this.agentActivators = initPlanningContext.subagents().stream().collect(Collectors.toMap(AgentInstance::agentId, AgentActivator::new, (a, b) -> a, LinkedHashMap::new));
    }

    public Action firstAction(PlanningContext planningContext) {
        AgenticScope scope = planningContext.agenticScope();
        if (this.goalPredicate.test(scope)) {
            return this.done();
        }
        return this.selectAndCall(scope);
    }

    public Action nextAction(PlanningContext planningContext) {
        AgenticScope scope = planningContext.agenticScope();
        AgentActivator lastExecutedAgent = this.agentActivators.get(planningContext.previousAgentInvocation().agentId());
        if (lastExecutedAgent != null) {
            this.agentActivators.values().forEach(a -> ((AgentActivator)a).onStateChanged(lastExecutedAgent.agent.outputKey()));
        }
        if (this.goalPredicate.test(scope)) {
            LOG.info("Goal predicate satisfied after {} invocations", (Object)this.invocationCounter);
            return this.done();
        }
        if (this.invocationCounter >= this.maxInvocations) {
            throw new IllegalStateException("Maximum invocations (" + this.maxInvocations + ") reached without satisfying goal. Increase maxInvocations or check goal predicate.");
        }
        return this.selectAndCall(scope);
    }

    private Action selectAndCall(AgenticScope scope) {
        List<AgentActivator> ready = this.agentActivators.values().stream().filter(a -> ((AgentActivator)a).canActivate(scope)).collect(Collectors.toList());
        if (ready.isEmpty()) {
            LOG.info("No agents can fire \u2014 blackboard quiescent after {} invocations", (Object)this.invocationCounter);
            return this.done();
        }
        AgentActivator selected = this.selectActivator(ready, scope);
        if (selected == null) {
            LOG.info("No agents can fire \u2014 blackboard quiescent after {} invocations", (Object)this.invocationCounter);
            return this.done();
        }
        selected.markFired();
        ++this.invocationCounter;
        LOG.info("Activating agent '{}' (invocation #{})", (Object)selected.agent.name(), (Object)this.invocationCounter);
        return this.call(new AgentInstance[]{selected.agent});
    }

    private AgentActivator selectActivator(List<AgentActivator> ready, AgenticScope scope) {
        List<AgentInstance> candidates = ready.stream().map(a -> ((AgentActivator)a).agent).collect(Collectors.toList());
        AgentInstance selected = this.conflictResolutionStrategy.resolve(scope, candidates);
        return selected == null ? null : ready.stream().filter(a -> ((AgentActivator)a).agent == selected).findFirst().orElseThrow(() -> new IllegalStateException("Agent not found in ready list"));
    }

    public Map<String, Object> executionState() {
        return Collections.singletonMap("invocationCounter", this.invocationCounter);
    }

    public void restoreExecutionState(Map<String, Object> state) {
        Object savedCounter = state.get("invocationCounter");
        if (savedCounter instanceof Number) {
            Number n = (Number)savedCounter;
            this.invocationCounter = n.intValue();
        }
    }

    public AgenticSystemTopology topology() {
        return AgenticSystemTopology.STAR;
    }

    private static class AgentActivator {
        private final AgentInstance agent;
        private final List<String> argumentNames;
        private boolean shouldExecute = true;

        AgentActivator(AgentInstance agent) {
            this.agent = agent;
            this.argumentNames = agent.arguments().stream().map(AgentArgument::name).collect(Collectors.toList());
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private boolean canActivate(AgenticScope agenticScope) {
            if (!this.shouldExecute) return false;
            if (!this.argumentNames.stream().allMatch(arg_0 -> ((AgenticScope)agenticScope).hasState(arg_0))) return false;
            return true;
        }

        private void markFired() {
            this.shouldExecute = false;
        }

        private void onStateChanged(String state) {
            if (this.argumentNames.contains(state)) {
                this.shouldExecute = true;
            }
        }
    }
}

