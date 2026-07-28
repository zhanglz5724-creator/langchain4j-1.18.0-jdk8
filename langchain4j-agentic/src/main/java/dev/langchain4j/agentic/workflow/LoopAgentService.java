/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow;

import dev.langchain4j.agentic.planner.AgenticService;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface LoopAgentService<T>
extends AgenticService<LoopAgentService<T>, T> {
    public LoopAgentService<T> maxIterations(int var1);

    public LoopAgentService<T> exitCondition(Predicate<AgenticScope> var1);

    public LoopAgentService<T> exitCondition(BiPredicate<AgenticScope, Integer> var1);

    public LoopAgentService<T> exitCondition(String var1, Predicate<AgenticScope> var2);

    public LoopAgentService<T> exitCondition(String var1, BiPredicate<AgenticScope, Integer> var2);

    public LoopAgentService<T> testExitAtLoopEnd(boolean var1);
}

