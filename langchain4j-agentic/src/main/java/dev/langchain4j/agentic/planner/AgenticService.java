/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.agent.ErrorContext;
import dev.langchain4j.agentic.agent.ErrorRecoveryResult;
import dev.langchain4j.agentic.declarative.TypedKey;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public interface AgenticService<T, A> {
    public A build();

    public T subAgents(Object ... var1);

    public T subAgents(Collection<?> var1);

    public T beforeCall(Consumer<AgenticScope> var1);

    public T name(String var1);

    public T description(String var1);

    public T outputKey(String var1);

    public T outputKey(Class<? extends TypedKey<?>> var1);

    public T output(Function<AgenticScope, Object> var1);

    public T errorHandler(Function<ErrorContext, ErrorRecoveryResult> var1);

    public T listener(AgentListener var1);
}

