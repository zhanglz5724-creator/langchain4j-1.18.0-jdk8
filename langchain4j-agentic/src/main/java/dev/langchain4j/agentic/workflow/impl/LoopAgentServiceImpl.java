/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow.impl;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.declarative.DeclarativeUtil;
import dev.langchain4j.agentic.declarative.ExitCondition;
import dev.langchain4j.agentic.declarative.LoopAgent;
import dev.langchain4j.agentic.internal.AbstractServiceBuilder;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.workflow.LoopAgentService;
import dev.langchain4j.agentic.workflow.impl.LoopPlanner;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class LoopAgentServiceImpl<T>
extends AbstractServiceBuilder<T, LoopAgentService<T>>
implements LoopAgentService<T> {
    protected int maxIterations = Integer.MAX_VALUE;
    protected BiPredicate<AgenticScope, Integer> exitCondition = (scope, loopCounter) -> false;
    protected String exitConditionDescription;
    protected boolean testExitAtLoopEnd = false;

    public LoopAgentServiceImpl(Class<T> agentServiceClass, Method agenticMethod) {
        super(agentServiceClass, agenticMethod);
        this.configureLoop(agentServiceClass);
    }

    @Override
    public T build() {
        return this.build(() -> new LoopPlanner(this.maxIterations, this.testExitAtLoopEnd, this.exitCondition, this.exitConditionDescription));
    }

    public static LoopAgentServiceImpl<UntypedAgent> builder() {
        return new LoopAgentServiceImpl<UntypedAgent>(UntypedAgent.class, null);
    }

    public static <T> LoopAgentServiceImpl<T> builder(Class<T> agentServiceClass) {
        return new LoopAgentServiceImpl<T>(agentServiceClass, AgentUtil.validateAgentClass(agentServiceClass, false, LoopAgent.class));
    }

    @Override
    public LoopAgentServiceImpl<T> maxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        return this;
    }

    @Override
    public LoopAgentServiceImpl<T> exitCondition(Predicate<AgenticScope> exitCondition) {
        return this.exitCondition((scope, loopCounter) -> exitCondition.test((AgenticScope)scope));
    }

    @Override
    public LoopAgentServiceImpl<T> exitCondition(BiPredicate<AgenticScope, Integer> exitCondition) {
        return this.exitCondition("<unknown>", (BiPredicate)exitCondition);
    }

    @Override
    public LoopAgentServiceImpl<T> exitCondition(String exitConditionDescription, Predicate<AgenticScope> exitCondition) {
        return this.exitCondition(exitConditionDescription, (scope, loopCounter) -> exitCondition.test((AgenticScope)scope));
    }

    @Override
    public LoopAgentServiceImpl<T> exitCondition(String exitConditionDescription, BiPredicate<AgenticScope, Integer> exitCondition) {
        this.exitCondition = exitCondition;
        this.exitConditionDescription = exitConditionDescription;
        return this;
    }

    @Override
    public LoopAgentServiceImpl<T> testExitAtLoopEnd(boolean testExitAtLoopEnd) {
        this.testExitAtLoopEnd = testExitAtLoopEnd;
        return this;
    }

    @Override
    public String serviceType() {
        return "Loop";
    }

    private void configureLoop(Class<T> agentServiceClass) {
        DeclarativeUtil.configureOutput(agentServiceClass, this);
        DeclarativeUtil.buildAgentFeatures(agentServiceClass, this);
        DeclarativeUtil.predicateMethod(agentServiceClass, method -> method.isAnnotationPresent(ExitCondition.class)).map(method -> {
            this.testExitAtLoopEnd(method.getAnnotation(ExitCondition.class).testExitAtLoopEnd());
            return method;
        }).ifPresent(method -> this.exitCondition(method.getAnnotation(ExitCondition.class).description(), (BiPredicate)LoopAgentServiceImpl.loopExitConditionPredicate(method)));
    }

    private static BiPredicate<AgenticScope, Integer> loopExitConditionPredicate(Method predicateMethod) {
        List<AgentArgument> agentArguments = AgentUtil.argumentsFromMethod(predicateMethod);
        return (agenticScope, loopCounter) -> {
            try {
                Object[] args = AgentUtil.agentInvocationArguments(agenticScope, agentArguments, LoopAgentServiceImpl.createScopeMap(agenticScope, loopCounter)).positionalArgs();
                return (Boolean)predicateMethod.invoke(null, args);
            }
            catch (Exception e) {
                throw new RuntimeException("Error invoking exit predicate method: " + predicateMethod.getName(), e);
            }
        };
    }

    private static Map<String, Object> createScopeMap(AgenticScope agenticScope, int loopCounter) {
        HashMap<String, Object> m = new HashMap<String, Object>();
        m.put("@AgenticScope", agenticScope);
        m.put("@LoopCounter", loopCounter);
        return m;
    }
}

