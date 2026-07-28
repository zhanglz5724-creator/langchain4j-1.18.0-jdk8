/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.memory.chat.ChatMemoryProvider
 *  dev.langchain4j.model.chat.ChatModel
 */
package dev.langchain4j.agentic.supervisor;

import dev.langchain4j.agentic.agent.ErrorContext;
import dev.langchain4j.agentic.agent.ErrorRecoveryResult;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.supervisor.SupervisorContextStrategy;
import dev.langchain4j.agentic.supervisor.SupervisorResponseStrategy;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public interface SupervisorAgentService<T> {
    public T build();

    public SupervisorAgentService<T> chatModel(ChatModel var1);

    public SupervisorAgentService<T> chatMemoryProvider(ChatMemoryProvider var1);

    public SupervisorAgentService<T> name(String var1);

    public SupervisorAgentService<T> description(String var1);

    public SupervisorAgentService<T> outputKey(String var1);

    public SupervisorAgentService<T> requestGenerator(Function<AgenticScope, String> var1);

    public SupervisorAgentService<T> contextGenerationStrategy(SupervisorContextStrategy var1);

    public SupervisorAgentService<T> responseStrategy(SupervisorResponseStrategy var1);

    public SupervisorAgentService<T> supervisorContext(String var1);

    public SupervisorAgentService<T> subAgents(Object ... var1);

    public SupervisorAgentService<T> subAgents(Collection<?> var1);

    public SupervisorAgentService<T> maxAgentsInvocations(int var1);

    public SupervisorAgentService<T> output(Function<AgenticScope, Object> var1);

    public SupervisorAgentService<T> errorHandler(Function<ErrorContext, ErrorRecoveryResult> var1);

    public SupervisorAgentService<T> listener(AgentListener var1);

    public SupervisorAgentService<T> beforeCall(Consumer<AgenticScope> var1);
}

