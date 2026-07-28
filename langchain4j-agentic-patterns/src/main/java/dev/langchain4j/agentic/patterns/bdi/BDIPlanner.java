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
package dev.langchain4j.agentic.patterns.bdi;

import dev.langchain4j.agentic.patterns.bdi.Desire;
import dev.langchain4j.agentic.planner.Action;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.InitPlanningContext;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.planner.PlanningContext;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BDIPlanner
implements Planner {
    private static final Logger LOG = LoggerFactory.getLogger(BDIPlanner.class);
    private static final int DEFAULT_MAX_INVOCATIONS = 50;
    private final List<Desire> desires;
    private final int maxInvocations;
    private Map<Class<?>, AgentInstance> agentsByType;
    private final Map<String, Integer> desireProgress = new HashMap<String, Integer>();
    private Desire currentDesire;
    private List<AgentInstance> currentIntention;
    private int intentionCursor;
    private int invocationCounter;

    public BDIPlanner(List<Desire> desires) {
        this(desires, 50);
    }

    public BDIPlanner(List<Desire> desires, int maxInvocations) {
        if (desires == null || desires.isEmpty()) {
            throw new IllegalArgumentException("BDIPlanner requires at least one desire");
        }
        if (maxInvocations <= 0) {
            throw new IllegalArgumentException("maxInvocations must be positive, got " + maxInvocations);
        }
        this.desires = desires;
        this.maxInvocations = maxInvocations;
    }

    public AgenticSystemTopology topology() {
        return AgenticSystemTopology.SEQUENCE;
    }

    public void init(InitPlanningContext initPlanningContext) {
        this.agentsByType = initPlanningContext.subagents().stream().collect(Collectors.toMap(AgentInstance::type, a -> a, (a, b) -> {
            throw new IllegalArgumentException("BDI desires reference agents by type, so each agent type must be unique. Duplicate agent type: " + a.type().getName());
        }));
        for (Desire desire : this.desires) {
            for (Class<?> agentType : desire.agentTypes()) {
                if (this.agentsByType.containsKey(agentType)) continue;
                throw new IllegalArgumentException("Desire '" + desire.name() + "' references unknown agent type: " + agentType.getName());
            }
        }
    }

    public Action firstAction(PlanningContext planningContext) {
        return this.deliberate(planningContext.agenticScope());
    }

    public Action nextAction(PlanningContext planningContext) {
        AgenticScope scope = planningContext.agenticScope();
        if (this.currentDesire != null && !this.currentDesire.satisfied().test(scope)) {
            boolean preempted = this.desires.stream().filter(d -> d.priority() > this.currentDesire.priority()).filter(d -> d.achievable().test(scope)).anyMatch(d -> !d.satisfied().test(scope));
            if (preempted) {
                this.desireProgress.put(this.currentDesire.name(), this.intentionCursor + 1);
                LOG.info("Preempting desire '{}' for a higher-priority desire", (Object)this.currentDesire.name());
                return this.deliberate(scope);
            }
            if (this.currentDesire.achievable().test(scope)) {
                ++this.intentionCursor;
                if (this.intentionCursor < this.currentIntention.size()) {
                    return this.dispatch(this.currentIntention.get(this.intentionCursor));
                }
                throw new IllegalStateException("Desire '" + this.currentDesire.name() + "' is still unsatisfied after its entire intention completed (" + this.currentIntention.size() + " agents). Check that the intention's agents write the state keys required by the desire's satisfied predicate.");
            }
        }
        return this.deliberate(scope);
    }

    private Action dispatch(AgentInstance agent) {
        if (this.invocationCounter >= this.maxInvocations) {
            throw new IllegalStateException("Maximum invocations (" + this.maxInvocations + ") reached with unsatisfied desires. Increase maxInvocations or check desire predicates.");
        }
        ++this.invocationCounter;
        return this.call(new AgentInstance[]{agent});
    }

    private Action deliberate(AgenticScope scope) {
        return this.desires.stream().filter(d -> d.achievable().test(scope)).filter(d -> !d.satisfied().test(scope)).max(Comparator.comparingInt(Desire::priority)).map(desire -> {
            this.currentDesire = desire;
            this.currentIntention = desire.agentTypes().stream().map(this.agentsByType::get).collect(Collectors.toList());
            this.intentionCursor = this.desireProgress.getOrDefault(desire.name(), 0);
            if (this.intentionCursor >= this.currentIntention.size()) {
                throw new IllegalStateException("Desire '" + desire.name() + "' is still unsatisfied after its entire intention completed (" + this.currentIntention.size() + " agents). Check that the intention's agents write the state keys required by the desire's satisfied predicate.");
            }
            LOG.info("Committing to desire '{}' (priority {}) at step {}/{}", new Object[]{desire.name(), desire.priority(), this.intentionCursor + 1, this.currentIntention.size()});
            return this.dispatch(this.currentIntention.get(this.intentionCursor));
        }).orElseGet(() -> {
            LOG.info("All desires satisfied or none achievable after {} invocations", (Object)this.invocationCounter);
            return this.done();
        });
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
}

