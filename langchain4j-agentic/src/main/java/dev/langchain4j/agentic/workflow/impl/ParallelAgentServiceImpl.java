/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow.impl;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.declarative.DeclarativeUtil;
import dev.langchain4j.agentic.declarative.ParallelAgent;
import dev.langchain4j.agentic.internal.AbstractServiceBuilder;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.workflow.ParallelAgentService;
import dev.langchain4j.agentic.workflow.impl.ParallelPlanner;
import java.lang.reflect.Method;

public class ParallelAgentServiceImpl<T>
extends AbstractServiceBuilder<T, ParallelAgentService<T>>
implements ParallelAgentService<T> {
    public ParallelAgentServiceImpl(Class<T> agentServiceClass, Method agenticMethod) {
        super(agentServiceClass, agenticMethod);
        this.configureParallel(agentServiceClass);
    }

    @Override
    public T build() {
        return this.build(ParallelPlanner::new);
    }

    public static ParallelAgentServiceImpl<UntypedAgent> builder() {
        return new ParallelAgentServiceImpl<UntypedAgent>(UntypedAgent.class, null);
    }

    public static <T> ParallelAgentServiceImpl<T> builder(Class<T> agentServiceClass) {
        return new ParallelAgentServiceImpl<T>(agentServiceClass, AgentUtil.validateAgentClass(agentServiceClass, false, ParallelAgent.class));
    }

    @Override
    public String serviceType() {
        return "Parallel";
    }

    private void configureParallel(Class<T> agentServiceClass) {
        DeclarativeUtil.configureOutput(agentServiceClass, this);
        DeclarativeUtil.buildAgentFeatures(agentServiceClass, this);
        DeclarativeUtil.parallelExecutor(agentServiceClass).ifPresent(this::executor);
    }
}

