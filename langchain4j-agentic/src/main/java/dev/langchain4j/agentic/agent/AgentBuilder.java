/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.guardrail.InputGuardrail
 *  dev.langchain4j.guardrail.OutputGuardrail
 *  dev.langchain4j.guardrail.config.InputGuardrailsConfig
 *  dev.langchain4j.guardrail.config.OutputGuardrailsConfig
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.memory.ChatMemory
 *  dev.langchain4j.memory.chat.ChatMemoryProvider
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.observability.api.listener.AiServiceListener
 *  dev.langchain4j.observability.api.listener.AiServiceResponseReceivedListener
 *  dev.langchain4j.rag.RetrievalAugmentor
 *  dev.langchain4j.rag.content.retriever.ContentRetriever
 *  dev.langchain4j.service.AiServiceContext
 *  dev.langchain4j.service.AiServices
 *  dev.langchain4j.service.memory.ChatMemoryAccess
 *  dev.langchain4j.service.tool.BeforeToolExecution
 *  dev.langchain4j.service.tool.ToolArgumentsErrorHandler
 *  dev.langchain4j.service.tool.ToolExecution
 *  dev.langchain4j.service.tool.ToolExecutionErrorHandler
 *  dev.langchain4j.service.tool.ToolExecutor
 *  dev.langchain4j.service.tool.ToolProvider
 */
package dev.langchain4j.agentic.agent;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.agent.AgentInvocationHandler;
import dev.langchain4j.agentic.agent.ChatMessagesAccess;
import dev.langchain4j.agentic.declarative.DeclarativeUtil;
import dev.langchain4j.agentic.declarative.TypedKey;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.internal.AgenticScopeOwner;
import dev.langchain4j.agentic.internal.Context;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.observability.AfterAgentToolExecution;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.AgentMonitor;
import dev.langchain4j.agentic.observability.BeforeAgentToolExecution;
import dev.langchain4j.agentic.observability.ComposedAgentListener;
import dev.langchain4j.agentic.observability.MonitoredAgent;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemConfigurationException;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.config.InputGuardrailsConfig;
import dev.langchain4j.guardrail.config.OutputGuardrailsConfig;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.observability.api.listener.AiServiceListener;
import dev.langchain4j.observability.api.listener.AiServiceResponseReceivedListener;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServiceContext;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.memory.ChatMemoryAccess;
import dev.langchain4j.service.tool.BeforeToolExecution;
import dev.langchain4j.service.tool.ToolArgumentsErrorHandler;
import dev.langchain4j.service.tool.ToolExecution;
import dev.langchain4j.service.tool.ToolExecutionErrorHandler;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.tool.ToolProvider;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class AgentBuilder<T, B extends AgentBuilder<T, ?>> {
    private static final ChatModel PLACEHOLDER_CHAT_MODEL = new ChatModel(){

        public ChatResponse doChat(ChatRequest chatRequest) {
            throw new IllegalStateException("Placeholder ChatModel should never be invoked. The actual model is provided dynamically via the chatModel(Function) provider.");
        }
    };
    private static final StreamingChatModel PLACEHOLDER_STREAMING_CHAT_MODEL = new StreamingChatModel(){

        public void doChat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
            throw new IllegalStateException("Placeholder StreamingChatModel should never be invoked. The actual model is provided dynamically via the streamingChatModel(Function) provider.");
        }
    };
    final Class<T> agentServiceClass;
    final Method agenticMethod;
    final Class<?> agentReturnType;
    List<AgentArgument> arguments;
    String name;
    String description;
    String outputKey;
    boolean async;
    boolean optional;
    private final Map<String, Object> defaultValues = new HashMap<String, Object>();
    private ChatModel model;
    private StreamingChatModel streamingChatModel;
    Function<AgenticScope, ChatModel> chatModelProvider;
    Function<AgenticScope, StreamingChatModel> streamingChatModelProvider;
    private ChatMemory chatMemory;
    private ChatMemoryProvider chatMemoryProvider;
    private Function<AgenticScope, String> contextProvider;
    private String[] contextProvidingAgents;
    private ContentRetriever contentRetriever;
    private RetrievalAugmentor retrievalAugmentor;
    private Function<Object, String> systemMessageProvider;
    private BiFunction<String, InvocationContext, String> systemMessageTransformer;
    private Function<Object, String> userMessageProvider;
    private InputGuardrailsConfig inputGuardrailsConfig;
    private OutputGuardrailsConfig outputGuardrailsConfig;
    private Class<? extends InputGuardrail>[] inputGuardrailClasses;
    private Class<? extends OutputGuardrail>[] outputGuardrailClasses;
    private InputGuardrail[] inputGuardrails;
    private OutputGuardrail[] outputGuardrails;
    private Object[] objectsWithTools;
    private Map<ToolSpecification, ToolExecutor> toolsMap;
    private Set<String> immediateReturnToolNames;
    private final List<ToolProvider> toolProviders = new ArrayList<ToolProvider>();
    private Integer maxToolCallingRoundTrips;
    private Function<ToolExecutionRequest, ToolExecutionResultMessage> hallucinatedToolNameStrategy;
    private boolean executeToolsConcurrently;
    private Executor concurrentToolsExecutor;
    private ToolArgumentsErrorHandler toolArgumentsErrorHandler;
    private ToolExecutionErrorHandler toolExecutionErrorHandler;
    Function<InternalAgent, Object> agentInstanceFactory;
    AgentListener agentListener;

    public AgentBuilder(Class<T> agentServiceClass) {
        this(agentServiceClass, true);
    }

    @Internal
    public static <T> AgentBuilder<T, AgentBuilder<T, ?>> withoutDeclarativeConfiguration(Class<T> agentServiceClass) {
        return new AgentBuilder(agentServiceClass, false);
    }

    private AgentBuilder(Class<T> agentServiceClass, boolean configureDeclarativeAgent) {
        this.agentServiceClass = agentServiceClass;
        this.agenticMethod = AgentUtil.validateAgentClass(agentServiceClass);
        this.agentReturnType = this.agenticMethod.getReturnType();
        Agent agent = this.agenticMethod.getAnnotation(Agent.class);
        if (agent == null) {
            throw new IllegalArgumentException("Method " + this.agenticMethod + " is not annotated with @Agent");
        }
        if (configureDeclarativeAgent) {
            DeclarativeUtil.configureAgent(agentServiceClass, this);
        }
        String string = this.name = !Utils.isNullOrBlank((String)agent.name()) ? agent.name() : this.agenticMethod.getName();
        if (!Utils.isNullOrBlank((String)agent.description())) {
            this.description = agent.description();
        } else if (!Utils.isNullOrBlank((String)agent.value())) {
            this.description = agent.value();
        }
        this.outputKey = AgentUtil.outputKey(agent.outputKey(), agent.typedOutputKey());
        this.async = agent.async();
        this.optional = agent.optional();
        if (agent.summarizedContext() != null && agent.summarizedContext().length > 0) {
            this.contextProvidingAgents = agent.summarizedContext();
        }
    }

    public T build() {
        return this.build(null);
    }

    T build(DefaultAgenticScope agenticScope) {
        boolean agenticScopeDependent;
        if (this.arguments == null) {
            this.arguments = AgentUtil.argumentsFromMethod(this.agenticMethod, this.defaultValues);
        }
        AiServiceContext context = AiServiceContext.create(this.agentServiceClass);
        AiServices aiServices = AiServices.builder((AiServiceContext)context);
        this.configureChatModel(aiServices);
        if (this.chatMemory != null) {
            aiServices.chatMemory(this.chatMemory);
        }
        if (this.chatMemoryProvider != null) {
            aiServices.chatMemoryProvider(this.chatMemoryProvider);
        }
        if (this.systemMessageProvider != null) {
            aiServices.systemMessageProvider(this.systemMessageProvider);
        }
        if (this.userMessageProvider != null) {
            aiServices.userMessageProvider(this.userMessageProvider);
        }
        if (this.contentRetriever != null) {
            aiServices.contentRetriever(this.contentRetriever);
        }
        if (this.retrievalAugmentor != null) {
            aiServices.retrievalAugmentor(this.retrievalAugmentor);
        }
        if (this.systemMessageTransformer != null) {
            aiServices.systemMessageTransformer(this.systemMessageTransformer);
        }
        this.setupGuardrails(aiServices);
        this.setupTools(aiServices);
        boolean bl = agenticScopeDependent = this.contextProvider != null || this.contextProvidingAgents != null && this.contextProvidingAgents.length > 0;
        if (agenticScope != null && agenticScopeDependent) {
            if (this.contextProvider != null) {
                aiServices.chatRequestTransformer((BiFunction)new Context.AgenticScopeContextGenerator(agenticScope, this.contextProvider));
            } else {
                aiServices.chatRequestTransformer((BiFunction)new Context.Summarizer(agenticScope, this.model, this.contextProvidingAgents));
            }
        }
        AgentMonitor monitor = ComposedAgentListener.listenerOfType(this.agentListener, AgentMonitor.class);
        if (MonitoredAgent.class.isAssignableFrom(this.agentServiceClass) && monitor == null) {
            monitor = new AgentMonitor();
            this.listener(monitor);
        }
        this.build(agenticScope, context, aiServices);
        AgentInvocationHandler handler = new AgentInvocationHandler(context, aiServices.build(), this, agenticScopeDependent);
        AgentInstance agent = this.agentInstanceFactory != null ? (AgentInstance)this.agentInstanceFactory.apply(handler) : (AgentInstance)Proxy.newProxyInstance(this.agentServiceClass.getClassLoader(), AgentBuilder.interfacesToImplement(this.agentServiceClass), handler);
        aiServices.registerListener((AiServiceListener)((AiServiceResponseReceivedListener)agent));
        if (monitor != null) {
            monitor.setRootAgent(agent);
        }
        if (this.agentListener != null) {
            aiServices.beforeToolExecution(beforeToolExecution -> this.agentListener.beforeAgentToolExecution(new BeforeAgentToolExecution(agent, (BeforeToolExecution)beforeToolExecution)));
            aiServices.afterToolExecution(afterToolExecution -> this.agentListener.afterAgentToolExecution(new AfterAgentToolExecution(agent, (ToolExecution)afterToolExecution)));
        }
        return (T)agent;
    }

    public static Class[] interfacesToImplement(Class clazz) {
        return new Class[]{clazz, InternalAgent.class, AgenticScopeOwner.class, ChatMemoryAccess.class, ChatMessagesAccess.class, AiServiceResponseReceivedListener.class};
    }

    private void configureChatModel(AiServices<T> aiServices) {
        this.validateChatModel();
        if (this.model != null) {
            aiServices.chatModel(this.model);
        } else if (this.streamingChatModel != null) {
            aiServices.streamingChatModel(this.streamingChatModel);
        } else if (this.chatModelProvider != null) {
            aiServices.chatModel(PLACEHOLDER_CHAT_MODEL);
        } else if (this.streamingChatModelProvider != null) {
            aiServices.streamingChatModel(PLACEHOLDER_STREAMING_CHAT_MODEL);
        } else {
            throw new AgenticSystemConfigurationException("No chat model is configured for agent '" + this.name + "'.");
        }
    }

    private void validateChatModel() {
        int modelConfigCount = (this.model != null ? 1 : 0) + (this.streamingChatModel != null ? 1 : 0) + (this.chatModelProvider != null ? 1 : 0) + (this.streamingChatModelProvider != null ? 1 : 0);
        if (modelConfigCount != 1) {
            throw new AgenticSystemConfigurationException("One and only one of chatModel, streamingChatModel, or their Function variants can be set for agent '" + this.name + "'.");
        }
    }

    protected void build(DefaultAgenticScope agenticScope, AiServiceContext context, AiServices<T> aiServices) {
    }

    private void setupGuardrails(AiServices<T> aiServices) {
        if (this.inputGuardrailsConfig != null) {
            aiServices.inputGuardrailsConfig(this.inputGuardrailsConfig);
        }
        if (this.outputGuardrailsConfig != null) {
            aiServices.outputGuardrailsConfig(this.outputGuardrailsConfig);
        }
        if (this.inputGuardrailClasses != null) {
            aiServices.inputGuardrailClasses((Class[])this.inputGuardrailClasses);
        }
        if (this.outputGuardrailClasses != null) {
            aiServices.outputGuardrailClasses((Class[])this.outputGuardrailClasses);
        }
        if (this.inputGuardrails != null) {
            aiServices.inputGuardrails(this.inputGuardrails);
        }
        if (this.outputGuardrails != null) {
            aiServices.outputGuardrails(this.outputGuardrails);
        }
    }

    private void setupTools(AiServices<T> aiServices) {
        if (this.objectsWithTools != null) {
            aiServices.tools(this.objectsWithTools);
        }
        if (this.toolsMap != null) {
            if (this.immediateReturnToolNames != null) {
                aiServices.tools(this.toolsMap, this.immediateReturnToolNames);
            } else {
                aiServices.tools(this.toolsMap);
            }
        }
        if (!this.toolProviders.isEmpty()) {
            aiServices.toolProviders(this.toolProviders);
        }
        if (this.maxToolCallingRoundTrips != null) {
            aiServices.maxToolCallingRoundTrips(this.maxToolCallingRoundTrips.intValue());
        }
        if (this.hallucinatedToolNameStrategy != null) {
            aiServices.hallucinatedToolNameStrategy(this.hallucinatedToolNameStrategy);
        }
        if (this.executeToolsConcurrently) {
            if (this.concurrentToolsExecutor != null) {
                aiServices.executeToolsConcurrently(this.concurrentToolsExecutor);
            } else {
                aiServices.executeToolsConcurrently();
            }
        }
        if (this.toolArgumentsErrorHandler != null) {
            aiServices.toolArgumentsErrorHandler(this.toolArgumentsErrorHandler);
        }
        if (this.toolExecutionErrorHandler != null) {
            aiServices.toolExecutionErrorHandler(this.toolExecutionErrorHandler);
        }
    }

    public B chatModel(ChatModel model) {
        this.model = model;
        return (B)this;
    }

    public B streamingChatModel(StreamingChatModel streamingChatModel) {
        this.streamingChatModel = streamingChatModel;
        return (B)this;
    }

    public B chatModel(Function<AgenticScope, ChatModel> chatModelProvider) {
        this.chatModelProvider = chatModelProvider;
        return (B)this;
    }

    public B streamingChatModel(Function<AgenticScope, StreamingChatModel> streamingChatModelProvider) {
        this.streamingChatModelProvider = streamingChatModelProvider;
        return (B)this;
    }

    public B chatMemory(ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
        return (B)this;
    }

    public B chatMemoryProvider(ChatMemoryProvider chatMemoryProvider) {
        this.chatMemoryProvider = chatMemoryProvider;
        return (B)this;
    }

    boolean hasChatMemory() {
        return this.chatMemory != null || this.chatMemoryProvider != null;
    }

    boolean hasNonDefaultChatMemory() {
        return this.chatMemoryProvider != null;
    }

    public B tools(Object ... objectsWithTools) {
        this.objectsWithTools = objectsWithTools;
        return (B)this;
    }

    public B tools(Map<ToolSpecification, ToolExecutor> toolsMap) {
        this.toolsMap = toolsMap;
        return (B)this;
    }

    public B tools(Map<ToolSpecification, ToolExecutor> toolsMap, Set<String> immediateReturnToolNames) {
        this.toolsMap = toolsMap;
        this.immediateReturnToolNames = immediateReturnToolNames;
        return (B)this;
    }

    public B toolProvider(ToolProvider toolProvider) {
        this.toolProviders.add(toolProvider);
        return (B)this;
    }

    public B toolProviders(Collection<ToolProvider> toolProviders) {
        this.toolProviders.addAll(toolProviders);
        return (B)this;
    }

    public B toolProviders(ToolProvider ... toolProviders) {
        return this.toolProviders(Arrays.asList(toolProviders));
    }

    public B maxToolCallingRoundTrips(int maxToolCallingRoundTrips) {
        this.maxToolCallingRoundTrips = maxToolCallingRoundTrips;
        return (B)this;
    }

    @Deprecated
    public B maxSequentialToolsInvocations(int maxSequentialToolsInvocations) {
        return this.maxToolCallingRoundTrips(maxSequentialToolsInvocations);
    }

    public B hallucinatedToolNameStrategy(Function<ToolExecutionRequest, ToolExecutionResultMessage> hallucinatedToolNameStrategy) {
        this.hallucinatedToolNameStrategy = hallucinatedToolNameStrategy;
        return (B)this;
    }

    public B contentRetriever(ContentRetriever contentRetriever) {
        this.contentRetriever = contentRetriever;
        return (B)this;
    }

    public B retrievalAugmentor(RetrievalAugmentor retrievalAugmentor) {
        this.retrievalAugmentor = retrievalAugmentor;
        return (B)this;
    }

    public B inputGuardrailsConfig(InputGuardrailsConfig inputGuardrailsConfig) {
        this.inputGuardrailsConfig = inputGuardrailsConfig;
        return (B)this;
    }

    public B outputGuardrailsConfig(OutputGuardrailsConfig outputGuardrailsConfig) {
        this.outputGuardrailsConfig = outputGuardrailsConfig;
        return (B)this;
    }

    public <I extends InputGuardrail> B inputGuardrailClasses(Class<? extends I> ... inputGuardrailClasses) {
        this.inputGuardrailClasses = inputGuardrailClasses;
        return (B)this;
    }

    public <O extends OutputGuardrail> B outputGuardrailClasses(Class<? extends O> ... outputGuardrailClasses) {
        this.outputGuardrailClasses = outputGuardrailClasses;
        return (B)this;
    }

    public <I extends InputGuardrail> B inputGuardrails(I ... inputGuardrails) {
        this.inputGuardrails = inputGuardrails;
        return (B)this;
    }

    public <O extends OutputGuardrail> B outputGuardrails(O ... outputGuardrails) {
        this.outputGuardrails = outputGuardrails;
        return (B)this;
    }

    public B name(String name) {
        this.name = name;
        return (B)this;
    }

    public B description(String description) {
        this.description = description;
        return (B)this;
    }

    public B outputKey(String outputKey) {
        this.outputKey = outputKey;
        return (B)this;
    }

    public B outputKey(Class<? extends TypedKey<?>> outputKey) {
        return this.outputKey(AgentUtil.keyName(outputKey));
    }

    public B async(boolean async) {
        this.async = async;
        return (B)this;
    }

    public B optional(boolean optional) {
        this.optional = optional;
        return (B)this;
    }

    public B context(Function<AgenticScope, String> contextProvider) {
        this.contextProvider = contextProvider;
        return (B)this;
    }

    public B summarizedContext(String ... contextProvidingAgents) {
        this.contextProvidingAgents = contextProvidingAgents;
        return (B)this;
    }

    public B systemMessage(String systemMessage) {
        return this.systemMessageProvider(ignore -> systemMessage);
    }

    public B systemMessageProvider(Function<Object, String> systemMessageProvider) {
        this.systemMessageProvider = systemMessageProvider;
        return (B)this;
    }

    public B userMessage(String userMessage) {
        return this.userMessageProvider(ignore -> userMessage);
    }

    public B userMessageProvider(Function<Object, String> userMessageProvider) {
        this.userMessageProvider = userMessageProvider;
        return (B)this;
    }

    public B systemMessageTransformer(UnaryOperator<String> systemMessageTransformer) {
        return this.systemMessageTransformer((String msg, InvocationContext ctx) -> (String)systemMessageTransformer.apply((String)msg));
    }

    public B systemMessageTransformer(BiFunction<String, InvocationContext, String> systemMessageTransformer) {
        this.systemMessageTransformer = systemMessageTransformer;
        return (B)this;
    }

    public B executeToolsConcurrently() {
        this.executeToolsConcurrently = true;
        return (B)this;
    }

    public B executeToolsConcurrently(Executor executor) {
        this.executeToolsConcurrently = true;
        this.concurrentToolsExecutor = executor;
        return (B)this;
    }

    public B toolArgumentsErrorHandler(ToolArgumentsErrorHandler toolArgumentsErrorHandler) {
        this.toolArgumentsErrorHandler = toolArgumentsErrorHandler;
        return (B)this;
    }

    public B toolExecutionErrorHandler(ToolExecutionErrorHandler toolExecutionErrorHandler) {
        this.toolExecutionErrorHandler = toolExecutionErrorHandler;
        return (B)this;
    }

    public B defaultKeyValue(String key, Object value) {
        this.defaultValues.put(key, value);
        return (B)this;
    }

    public <K> B defaultKeyValue(Class<? extends TypedKey<K>> key, K value) {
        return this.defaultKeyValue(AgentUtil.keyName(key), value);
    }

    public B agentInstanceFactory(Function<InternalAgent, Object> factory) {
        this.agentInstanceFactory = factory;
        return (B)this;
    }

    public B listener(AgentListener agentListener) {
        if (this.agentListener == null) {
            this.agentListener = agentListener;
        } else if (this.agentListener instanceof ComposedAgentListener) {
            ComposedAgentListener composed = (ComposedAgentListener)this.agentListener;
            composed.addListener(agentListener);
        } else {
            this.agentListener = new ComposedAgentListener(this.agentListener, agentListener);
        }
        return (B)this;
    }
}

