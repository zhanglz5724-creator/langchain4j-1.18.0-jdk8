/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow;

import dev.langchain4j.agentic.internal.AgentExecutor;
import dev.langchain4j.agentic.planner.AgenticService;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.List;
import java.util.function.Predicate;

public interface ConditionalAgentService<T>
extends AgenticService<ConditionalAgentService<T>, T> {
    public ConditionalAgentService<T> subAgents(Predicate<AgenticScope> var1, Object ... var2);

    public ConditionalAgentService<T> subAgents(String var1, Predicate<AgenticScope> var2, Object ... var3);

    public ConditionalAgentService<T> subAgents(Predicate<AgenticScope> var1, List<AgentExecutor> var2);

    public ConditionalAgentService<T> subAgents(String var1, Predicate<AgenticScope> var2, List<AgentExecutor> var3);

    public ConditionalAgentService<T> subAgent(Predicate<AgenticScope> var1, AgentExecutor var2);

    public ConditionalAgentService<T> subAgent(String var1, Predicate<AgenticScope> var2, AgentExecutor var3);
}

