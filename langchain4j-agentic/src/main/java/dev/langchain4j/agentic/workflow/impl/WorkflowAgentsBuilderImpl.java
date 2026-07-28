/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow.impl;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.workflow.ConditionalAgentService;
import dev.langchain4j.agentic.workflow.LoopAgentService;
import dev.langchain4j.agentic.workflow.ParallelAgentService;
import dev.langchain4j.agentic.workflow.ParallelMapperService;
import dev.langchain4j.agentic.workflow.SequentialAgentService;
import dev.langchain4j.agentic.workflow.WorkflowAgentsBuilder;
import dev.langchain4j.agentic.workflow.impl.ConditionalAgentServiceImpl;
import dev.langchain4j.agentic.workflow.impl.LoopAgentServiceImpl;
import dev.langchain4j.agentic.workflow.impl.ParallelAgentServiceImpl;
import dev.langchain4j.agentic.workflow.impl.ParallelMapperServiceImpl;
import dev.langchain4j.agentic.workflow.impl.SequentialAgentServiceImpl;

public enum WorkflowAgentsBuilderImpl implements WorkflowAgentsBuilder
{
    INSTANCE;


    @Override
    public SequentialAgentService<UntypedAgent> sequenceBuilder() {
        return SequentialAgentServiceImpl.builder();
    }

    @Override
    public <T> SequentialAgentService<T> sequenceBuilder(Class<T> agentServiceClass) {
        return SequentialAgentServiceImpl.builder(agentServiceClass);
    }

    @Override
    public ParallelAgentService<UntypedAgent> parallelBuilder() {
        return ParallelAgentServiceImpl.builder();
    }

    @Override
    public <T> ParallelAgentService<T> parallelBuilder(Class<T> agentServiceClass) {
        return ParallelAgentServiceImpl.builder(agentServiceClass);
    }

    @Override
    public LoopAgentService<UntypedAgent> loopBuilder() {
        return LoopAgentServiceImpl.builder();
    }

    @Override
    public <T> LoopAgentService<T> loopBuilder(Class<T> agentServiceClass) {
        return LoopAgentServiceImpl.builder(agentServiceClass);
    }

    @Override
    public ConditionalAgentService<UntypedAgent> conditionalBuilder() {
        return ConditionalAgentServiceImpl.builder();
    }

    @Override
    public <T> ConditionalAgentService<T> conditionalBuilder(Class<T> agentServiceClass) {
        return ConditionalAgentServiceImpl.builder(agentServiceClass);
    }

    @Override
    public ParallelMapperService<UntypedAgent> parallelMapperBuilder() {
        return ParallelMapperServiceImpl.builder();
    }

    @Override
    public <T> ParallelMapperService<T> parallelMapperBuilder(Class<T> agentServiceClass) {
        return ParallelMapperServiceImpl.builder(agentServiceClass);
    }
}

