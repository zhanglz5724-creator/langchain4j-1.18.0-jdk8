/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.memory.ChatMemory
 *  dev.langchain4j.memory.chat.ChatMemoryProvider
 *  dev.langchain4j.model.chat.ChatModel
 */
package dev.langchain4j.agentic.supervisor;

import dev.langchain4j.agentic.declarative.ChatMemoryProviderSupplier;
import dev.langchain4j.agentic.declarative.ChatModelSupplier;
import dev.langchain4j.agentic.declarative.DeclarativeUtil;
import dev.langchain4j.agentic.declarative.Output;
import dev.langchain4j.agentic.declarative.SupervisorRequest;
import dev.langchain4j.agentic.internal.AbstractServiceBuilder;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.planner.AgenticService;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.supervisor.SupervisorAgent;
import dev.langchain4j.agentic.supervisor.SupervisorAgentService;
import dev.langchain4j.agentic.supervisor.SupervisorContextStrategy;
import dev.langchain4j.agentic.supervisor.SupervisorPlanner;
import dev.langchain4j.agentic.supervisor.SupervisorResponseStrategy;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;

public class SupervisorAgentServiceImpl<T>
extends AbstractServiceBuilder<T, SupervisorAgentServiceImpl<T>>
implements SupervisorAgentService<T>,
AgenticService<SupervisorAgentService<T>, T> {
    private ChatModel chatModel;
    private ChatMemoryProvider chatMemoryProvider;
    private int maxAgentsInvocations = 10;
    private SupervisorContextStrategy contextStrategy = SupervisorContextStrategy.CHAT_MEMORY;
    private SupervisorResponseStrategy responseStrategy = SupervisorResponseStrategy.LAST;
    private Function<AgenticScope, String> requestGenerator;
    private String supervisorContext;

    public SupervisorAgentServiceImpl(Class<T> agentServiceClass, Method agenticMethod) {
        this(agentServiceClass, agenticMethod, null);
    }

    public SupervisorAgentServiceImpl(Class<T> agentServiceClass, Method agenticMethod, ChatModel chatModel) {
        super(agentServiceClass, agenticMethod);
        this.configureSupervisor(agentServiceClass, chatModel);
    }

    @Override
    public T build() {
        if (this.supervisorContext != null) {
            this.beforeCall(this.beforeCall.andThen(agenticScope -> agenticScope.writeStateIfAbsent("supervisorContext", this.supervisorContext)));
        }
        return this.build(() -> new SupervisorPlanner(this.chatModel, this.chatMemoryProvider, this.maxAgentsInvocations, this.contextStrategy, this.responseStrategy, this.requestGenerator, this.outputKey, this.output));
    }

    public static SupervisorAgentService<SupervisorAgent> builder() {
        try {
            Method supervisorMethod = SupervisorAgent.class.getMethod("invoke", String.class);
            return new SupervisorAgentServiceImpl<SupervisorAgent>(SupervisorAgent.class, supervisorMethod);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> SupervisorAgentService<T> builder(Class<T> agentServiceClass) {
        return new SupervisorAgentServiceImpl<T>(agentServiceClass, AgentUtil.validateAgentClass(agentServiceClass, false));
    }

    @Override
    public SupervisorAgentServiceImpl<T> chatModel(ChatModel chatModel) {
        this.chatModel = chatModel;
        return this;
    }

    @Override
    public SupervisorAgentServiceImpl<T> chatMemoryProvider(ChatMemoryProvider chatMemoryProvider) {
        this.chatMemoryProvider = chatMemoryProvider;
        return this;
    }

    @Override
    public SupervisorAgentServiceImpl<T> requestGenerator(Function<AgenticScope, String> requestGenerator) {
        this.requestGenerator = requestGenerator;
        return this;
    }

    @Override
    public SupervisorAgentServiceImpl<T> contextGenerationStrategy(SupervisorContextStrategy contextStrategy) {
        this.contextStrategy = contextStrategy;
        return this;
    }

    @Override
    public SupervisorAgentServiceImpl<T> responseStrategy(SupervisorResponseStrategy responseStrategy) {
        this.responseStrategy = responseStrategy;
        return this;
    }

    @Override
    public SupervisorAgentServiceImpl<T> supervisorContext(String supervisorContext) {
        this.supervisorContext = supervisorContext;
        return this;
    }

    @Override
    public SupervisorAgentServiceImpl<T> maxAgentsInvocations(int maxAgentsInvocations) {
        this.maxAgentsInvocations = maxAgentsInvocations;
        return this;
    }

    @Override
    public String serviceType() {
        return "Supervisor";
    }

    private void configureSupervisor(Class<T> agentServiceClass, ChatModel chatModel) {
        DeclarativeUtil.selectMethod(agentServiceClass, method -> method.isAnnotationPresent(SupervisorRequest.class) && method.getReturnType() == String.class).map(m -> DeclarativeUtil.agenticScopeFunction(m, String.class)).ifPresent(function -> this.requestGenerator((Function)function));
        Optional<ChatModel> suppliedChatModel = DeclarativeUtil.selectMethod(agentServiceClass, method -> method.isAnnotationPresent(ChatModelSupplier.class) && method.getReturnType() == ChatModel.class && method.getParameterCount() == 0).map(method -> (ChatModel)DeclarativeUtil.invokeStatic(method, new Object[0]));
        if (suppliedChatModel.isPresent()) {
            this.chatModel(suppliedChatModel.get());
        } else {
            this.chatModel(chatModel);
        }
        DeclarativeUtil.selectMethod(agentServiceClass, method -> method.isAnnotationPresent(ChatMemoryProviderSupplier.class) && method.getReturnType() == ChatMemory.class && method.getParameterCount() == 1).map(method -> memoryId -> (ChatMemory)DeclarativeUtil.invokeStatic(method, memoryId)).ifPresent(chatMemoryProvider -> this.chatMemoryProvider((ChatMemoryProvider)chatMemoryProvider));
        DeclarativeUtil.selectMethod(agentServiceClass, method -> method.isAnnotationPresent(Output.class)).map(m -> DeclarativeUtil.agenticScopeFunction(m, Object.class)).ifPresent(this::output);
        DeclarativeUtil.buildAgentFeatures(agentServiceClass, this);
    }
}

