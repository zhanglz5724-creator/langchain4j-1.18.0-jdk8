/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow.impl;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.declarative.DeclarativeUtil;
import dev.langchain4j.agentic.internal.AbstractServiceBuilder;
import dev.langchain4j.agentic.internal.AgentExecutor;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.workflow.ConditionalAgent;
import dev.langchain4j.agentic.workflow.ConditionalAgentService;
import dev.langchain4j.agentic.workflow.impl.ConditionalPlanner;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConditionalAgentServiceImpl<T>
extends AbstractServiceBuilder<T, ConditionalAgentService<T>>
implements ConditionalAgentService<T> {
    protected final List<ConditionalAgent> conditionalAgents = new ArrayList<ConditionalAgent>();

    public ConditionalAgentServiceImpl(Class<T> agentServiceClass, Method agenticMethod) {
        super(agentServiceClass, agenticMethod);
        this.configureConditional(agentServiceClass);
    }

    @Override
    public T build() {
        return this.build(() -> new ConditionalPlanner(this.conditionalAgents));
    }

    public static ConditionalAgentServiceImpl<UntypedAgent> builder() {
        return new ConditionalAgentServiceImpl<UntypedAgent>(UntypedAgent.class, null);
    }

    public static <T> ConditionalAgentServiceImpl<T> builder(Class<T> agentServiceClass) {
        return new ConditionalAgentServiceImpl<T>(agentServiceClass, AgentUtil.validateAgentClass(agentServiceClass, false, dev.langchain4j.agentic.declarative.ConditionalAgent.class));
    }

    @Override
    public ConditionalAgentServiceImpl<T> subAgents(Object ... agents) {
        return this.subAgents(agenticScope -> true, agents);
    }

    @Override
    public ConditionalAgentServiceImpl<T> subAgents(Predicate<AgenticScope> condition, Object ... agents) {
        return this.subAgents("<unknown>", (Predicate)condition, (List)AgentUtil.agentsToExecutors(Arrays.asList(agents)));
    }

    @Override
    public ConditionalAgentServiceImpl<T> subAgents(String conditionDescription, Predicate<AgenticScope> condition, Object ... agents) {
        return this.subAgents(conditionDescription, (Predicate)condition, (List)AgentUtil.agentsToExecutors(Arrays.asList(agents)));
    }

    @Override
    public ConditionalAgentServiceImpl<T> subAgents(Collection<?> agents) {
        return this.subAgents(agenticScope -> true, (List)AgentUtil.agentsToExecutors(agents));
    }

    @Override
    public ConditionalAgentServiceImpl<T> subAgents(Predicate<AgenticScope> condition, List<AgentExecutor> agentExecutors) {
        return this.subAgents("<unknown>", (Predicate)condition, (List)agentExecutors);
    }

    @Override
    public ConditionalAgentServiceImpl<T> subAgents(String conditionDescription, Predicate<AgenticScope> condition, List<AgentExecutor> agentExecutors) {
        super.subAgents(agentExecutors);
        this.conditionalAgents.add(new ConditionalAgent(conditionDescription, condition, agentExecutors.stream().map(AgentInstance.class::cast).collect(Collectors.toList())));
        return this;
    }

    @Override
    public ConditionalAgentServiceImpl<T> subAgent(Predicate<AgenticScope> condition, AgentExecutor agentExecutor) {
        return this.subAgents((Predicate)condition, (List)Arrays.asList(agentExecutor));
    }

    @Override
    public ConditionalAgentServiceImpl<T> subAgent(String conditionDescription, Predicate<AgenticScope> condition, AgentExecutor agentExecutor) {
        return this.subAgents(conditionDescription, (Predicate)condition, (List)Arrays.asList(agentExecutor));
    }

    @Override
    public String serviceType() {
        return "Conditional";
    }

    private void configureConditional(Class<T> agentServiceClass) {
        DeclarativeUtil.configureOutput(agentServiceClass, this);
        DeclarativeUtil.buildAgentFeatures(agentServiceClass, this);
    }
}

