/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow;

import dev.langchain4j.agentic.planner.AgenticService;
import java.util.concurrent.Executor;

public interface ParallelAgentService<T>
extends AgenticService<ParallelAgentService<T>, T> {
    public ParallelAgentService<T> executor(Executor var1);
}

