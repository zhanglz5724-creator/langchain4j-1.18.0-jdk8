/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.workflow.ConditionalAgentService;
import dev.langchain4j.agentic.workflow.LoopAgentService;
import dev.langchain4j.agentic.workflow.ParallelAgentService;
import dev.langchain4j.agentic.workflow.ParallelMapperService;
import dev.langchain4j.agentic.workflow.SequentialAgentService;

public interface WorkflowAgentsBuilder {
    public SequentialAgentService<UntypedAgent> sequenceBuilder();

    public <T> SequentialAgentService<T> sequenceBuilder(Class<T> var1);

    public ParallelAgentService<UntypedAgent> parallelBuilder();

    public <T> ParallelAgentService<T> parallelBuilder(Class<T> var1);

    public LoopAgentService<UntypedAgent> loopBuilder();

    public <T> LoopAgentService<T> loopBuilder(Class<T> var1);

    public ConditionalAgentService<UntypedAgent> conditionalBuilder();

    public <T> ConditionalAgentService<T> conditionalBuilder(Class<T> var1);

    default public ParallelMapperService<UntypedAgent> parallelMapperBuilder() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    default public <T> ParallelMapperService<T> parallelMapperBuilder(Class<T> agentServiceClass) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

