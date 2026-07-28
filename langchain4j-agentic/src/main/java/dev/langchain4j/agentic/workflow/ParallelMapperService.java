/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow;

import dev.langchain4j.agentic.planner.AgenticService;
import java.util.concurrent.Executor;

public interface ParallelMapperService<T>
extends AgenticService<ParallelMapperService<T>, T> {
    public ParallelMapperService<T> executor(Executor var1);

    public ParallelMapperService<T> itemsProvider(String var1);
}

