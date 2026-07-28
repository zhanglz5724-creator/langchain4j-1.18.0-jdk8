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
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.service.AiServices
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.agentic.patterns.p2p;

import dev.langchain4j.agentic.patterns.p2p.VariablesExtractorAgent;
import dev.langchain4j.agentic.planner.Action;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.InitPlanningContext;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.planner.PlanningContext;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P2PPlanner
implements Planner {
    private static final Logger LOG = LoggerFactory.getLogger(P2PPlanner.class);
    private final ChatModel chatModel;
    private final int maxAgentsInvocations;
    private final BiPredicate<AgenticScope, Integer> exitCondition;
    private int invocationCounter = 0;
    private Map<String, AgentActivator> agentActivators;

    public P2PPlanner() {
        this(10);
    }

    public P2PPlanner(int maxAgentsInvocations) {
        this(maxAgentsInvocations, (AgenticScope scope, Integer invocationsCounter) -> false);
    }

    public P2PPlanner(Predicate<AgenticScope> exitCondition) {
        this(10, exitCondition);
    }

    public P2PPlanner(int maxAgentsInvocations, Predicate<AgenticScope> exitCondition) {
        this(null, maxAgentsInvocations, exitCondition);
    }

    public P2PPlanner(int maxAgentsInvocations, BiPredicate<AgenticScope, Integer> exitCondition) {
        this(null, maxAgentsInvocations, exitCondition);
    }

    public P2PPlanner(ChatModel chatModel, int maxAgentsInvocations, Predicate<AgenticScope> exitCondition) {
        this(chatModel, maxAgentsInvocations, (AgenticScope scope, Integer invocationsCounter) -> exitCondition.test((AgenticScope)scope));
    }

    public P2PPlanner(ChatModel chatModel, int maxAgentsInvocations, BiPredicate<AgenticScope, Integer> exitCondition) {
        this.chatModel = chatModel;
        this.exitCondition = exitCondition;
        this.maxAgentsInvocations = maxAgentsInvocations;
    }

    public void init(InitPlanningContext initPlanningContext) {
        this.agentActivators = initPlanningContext.subagents().stream().collect(Collectors.toMap(AgentInstance::agentId, AgentActivator::new));
    }

    public Action firstAction(PlanningContext planningContext) {
        if (planningContext.agenticScope().hasState("p2pRequest")) {
            String request = (String)planningContext.agenticScope().readState("p2pRequest", (Object)"");
            Collection variableNames = this.agentActivators.values().stream().flatMap(agentActivator -> ((AgentActivator)agentActivator).argumentNames().stream()).distinct().collect(Collectors.toList());
            Map<String, String> vars = P2PPlanner.createVariablesExtractorAgent(this.chatModel).extractVariables(request, variableNames);
            LOG.info("Variables extracted from user's prompt: {}", vars);
            vars.forEach((arg_0, arg_1) -> ((AgenticScope)planningContext.agenticScope()).writeState(arg_0, arg_1));
        }
        return this.nextCallAction(planningContext.agenticScope());
    }

    public Action nextAction(PlanningContext planningContext) {
        if (this.terminated(planningContext.agenticScope())) {
            return this.done();
        }
        AgentActivator lastExecutedAgent = this.agentActivators.get(planningContext.previousAgentInvocation().agentId());
        if (lastExecutedAgent != null) {
            lastExecutedAgent.finishExecution();
            this.agentActivators.values().forEach(a -> ((AgentActivator)a).onStateChanged(lastExecutedAgent.agent.outputKey()));
        }
        return this.nextCallAction(planningContext.agenticScope());
    }

    private Action nextCallAction(AgenticScope agenticScope) {
        AgentInstance[] agentsToCall = (AgentInstance[])this.agentActivators.values().stream().filter(agentActivator -> ((AgentActivator)agentActivator).canActivate(agenticScope)).peek(rec$ -> ((AgentActivator)rec$).startExecution()).map(rec$ -> ((AgentActivator)rec$).agent()).toArray(AgentInstance[]::new);
        this.invocationCounter += agentsToCall.length;
        return this.call(agentsToCall);
    }

    private boolean terminated(AgenticScope agenticScope) {
        return this.invocationCounter > this.maxAgentsInvocations || this.exitCondition.test(agenticScope, this.invocationCounter);
    }

    private static VariablesExtractorAgent createVariablesExtractorAgent(ChatModel chatModel) {
        if (chatModel == null) {
            throw new IllegalArgumentException("ChatModel must be provided for P2PAgent to extract variables from user's prompt.");
        }
        return (VariablesExtractorAgent)AiServices.builder(VariablesExtractorAgent.class).chatModel(chatModel).build();
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
        private volatile boolean executing = false;
        private volatile boolean shouldExecute = true;

        AgentActivator(AgentInstance agent) {
            this.agent = agent;
            this.argumentNames = agent.arguments().stream().map(AgentArgument::name).collect(Collectors.toList());
        }

        private AgentInstance agent() {
            return this.agent;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private boolean canActivate(AgenticScope agenticScope) {
            if (this.executing) return false;
            if (!this.shouldExecute) return false;
            if (!this.argumentNames.stream().allMatch(arg_0 -> ((AgenticScope)agenticScope).hasState(arg_0))) return false;
            return true;
        }

        private void startExecution() {
            LOG.info("Starting agent: {}", (Object)this.agent.agentId());
            this.shouldExecute = false;
            this.executing = true;
        }

        private void finishExecution() {
            LOG.info("Stopping agent: {}", (Object)this.agent.agentId());
            this.executing = false;
        }

        private void onStateChanged(String state) {
            boolean inputChanged = this.argumentNames.contains(state);
            this.shouldExecute = this.shouldExecute || inputChanged;
        }

        private List<String> argumentNames() {
            return this.argumentNames;
        }
    }
}

