/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow.impl;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.declarative.DeclarativeUtil;
import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.agentic.internal.AbstractServiceBuilder;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.workflow.SequentialAgentService;
import dev.langchain4j.agentic.workflow.impl.SequentialPlanner;
import java.lang.reflect.Method;

public class SequentialAgentServiceImpl<T>
extends AbstractServiceBuilder<T, SequentialAgentService<T>>
implements SequentialAgentService<T> {
    public SequentialAgentServiceImpl(Class<T> agentServiceClass, Method agenticMethod) {
        super(agentServiceClass, agenticMethod);
        this.configureSequential(agentServiceClass);
    }

    @Override
    public T build() {
        return this.build(SequentialPlanner::new);
    }

    public static SequentialAgentServiceImpl<UntypedAgent> builder() {
        return new SequentialAgentServiceImpl<UntypedAgent>(UntypedAgent.class, null);
    }

    public static <T> SequentialAgentServiceImpl<T> builder(Class<T> agentServiceClass) {
        return new SequentialAgentServiceImpl<T>(agentServiceClass, AgentUtil.validateAgentClass(agentServiceClass, false, SequenceAgent.class));
    }

    @Override
    public String serviceType() {
        return "Sequential";
    }

    private void configureSequential(Class<T> agentServiceClass) {
        DeclarativeUtil.configureOutput(agentServiceClass, this);
        DeclarativeUtil.buildAgentFeatures(agentServiceClass, this);
    }
}

