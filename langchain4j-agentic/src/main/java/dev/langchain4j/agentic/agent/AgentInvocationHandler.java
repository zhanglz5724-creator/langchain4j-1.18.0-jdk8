/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.invocation.LangChain4jManaged
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.observability.api.event.AiServiceResponseReceivedEvent
 *  dev.langchain4j.observability.api.listener.AiServiceListener
 *  dev.langchain4j.observability.api.listener.AiServiceResponseReceivedListener
 *  dev.langchain4j.service.AiServiceContext
 *  dev.langchain4j.service.ParameterNameResolver
 *  dev.langchain4j.service.memory.ChatMemoryAccess
 *  dev.langchain4j.service.tool.BeforeToolExecution
 *  dev.langchain4j.service.tool.ToolExecution
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.agentic.agent;

import dev.langchain4j.agentic.agent.AgentBuilder;
import dev.langchain4j.agentic.agent.AgentInvocationException;
import dev.langchain4j.agentic.agent.ChatMessagesAccess;
import dev.langchain4j.agentic.internal.AgenticScopeOwner;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.observability.AfterAgentToolExecution;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.AgentMonitor;
import dev.langchain4j.agentic.observability.BeforeAgentToolExecution;
import dev.langchain4j.agentic.observability.ComposedAgentListener;
import dev.langchain4j.agentic.observability.ListenerNotifierUtil;
import dev.langchain4j.agentic.observability.MonitoredAgent;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemConfigurationException;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.invocation.LangChain4jManaged;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.observability.api.event.AiServiceResponseReceivedEvent;
import dev.langchain4j.observability.api.listener.AiServiceListener;
import dev.langchain4j.observability.api.listener.AiServiceResponseReceivedListener;
import dev.langchain4j.service.AiServiceContext;
import dev.langchain4j.service.ParameterNameResolver;
import dev.langchain4j.service.memory.ChatMemoryAccess;
import dev.langchain4j.service.tool.BeforeToolExecution;
import dev.langchain4j.service.tool.ToolExecution;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentInvocationHandler
implements InvocationHandler,
InternalAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentInvocationHandler.class);
    private final AiServiceContext context;
    private final AgentBuilder<?, ?> builder;
    private final Object agent;
    private final boolean agenticScopeDependent;
    private final Function<AgenticScope, ChatModel> chatModelProvider;
    private final Function<AgenticScope, StreamingChatModel> streamingChatModelProvider;
    private String agentId;
    private InternalAgent parent;
    private AgentListener agentListener;
    private final Map<Object, AiServiceResponseReceivedEvent> lastResponseEvents = new ConcurrentHashMap<Object, AiServiceResponseReceivedEvent>();

    public AgentInvocationHandler(AiServiceContext context, Object agent, AgentBuilder<?, ?> builder, boolean agenticScopeDependent) {
        this.context = context;
        this.agent = agent;
        this.builder = builder;
        this.agentId = builder.name;
        this.agenticScopeDependent = agenticScopeDependent;
        this.agentListener = builder.agentListener;
        this.chatModelProvider = builder.chatModelProvider;
        this.streamingChatModelProvider = builder.streamingChatModelProvider;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        if (method.getDeclaringClass() == AiServiceListener.class || method.getDeclaringClass() == AiServiceResponseReceivedListener.class) {
            switch (method.getName()) {
                case "getEventClass": {
                    return AiServiceResponseReceivedEvent.class;
                }
                case "onEvent": {
                    AiServiceResponseReceivedEvent event = (AiServiceResponseReceivedEvent)args[0];
                    AgenticScope agenticScope = (AgenticScope)event.invocationContext().managedParameters().get(AgenticScope.class);
                    this.lastResponseEvents.put(agenticScope.memoryId(), event);
                    return null;
                }
            }
            throw new UnsupportedOperationException("Unknown method on AiServiceResponseReceivedListener class : " + method.getName());
        }
        if (method.getDeclaringClass() == ChatMessagesAccess.class) {
            if ("removeLastResponseEvent".equals(method.getName())) {
                this.lastResponseEvents.remove(args[0]);
                return null;
            }
            AiServiceResponseReceivedEvent lastResponseEvent = this.lastResponseEvents.get(args[0]);
            if (lastResponseEvent == null) {
                return null;
            }
            switch (method.getName()) {
                case "lastUserMessage": {
                    return AgentInvocationHandler.lastUserMessage(lastResponseEvent.request().messages()).orElse(null);
                }
                case "lastChatRequest": {
                    return lastResponseEvent.request();
                }
                case "lastChatResponse": {
                    return lastResponseEvent.response();
                }
            }
            throw new UnsupportedOperationException("Unknown method on AgenticScopeOwner class : " + method.getName());
        }
        if (method.getDeclaringClass() == AgenticScopeOwner.class) {
            switch (method.getName()) {
                case "withAgenticScope": {
                    if (!this.agenticScopeDependent) {
                        return proxy;
                    }
                    Object agentProxy = ((DefaultAgenticScope)args[0]).getOrCreateAgent(this.agentId, this.builder::build);
                    ((InternalAgent)agentProxy).setParent(this.parent);
                    ((InternalAgent)agentProxy).setAgentId(this.agentId);
                    return agentProxy;
                }
                case "registry": {
                    throw new UnsupportedOperationException("AgenticScopeOwner's registry method can be used only on the root agent of an agentic system.");
                }
            }
            throw new UnsupportedOperationException("Unknown method on AgenticScopeOwner class : " + method.getName());
        }
        if (method.getDeclaringClass() == ChatMemoryAccess.class) {
            switch (method.getName()) {
                case "getChatMemory": {
                    return this.context.hasChatMemory() && ("default".equals(args[0]) || this.builder.hasNonDefaultChatMemory()) ? this.context.chatMemoryService.getChatMemory(args[0]) : null;
                }
                case "evictChatMemory": {
                    return this.context.hasChatMemory() && this.context.chatMemoryService.evictChatMemory(args[0]) != null;
                }
            }
            throw new UnsupportedOperationException("Unknown method on ChatMemoryAccess class : " + method.getName());
        }
        if (method.getDeclaringClass() == AgentInstance.class || method.getDeclaringClass() == InternalAgent.class) {
            return method.invoke(this, args);
        }
        if (method.getDeclaringClass() == MonitoredAgent.class) {
            return ComposedAgentListener.listenerOfType(this.agentListener, AgentMonitor.class);
        }
        if (method.getDeclaringClass() == Object.class) {
            switch (method.getName()) {
                case "toString": {
                    return "Agent<" + this.builder.agentServiceClass.getSimpleName() + ">";
                }
                case "hashCode": {
                    return System.identityHashCode(this.agent);
                }
            }
            throw new UnsupportedOperationException("Unknown method on Object class : " + method.getName());
        }
        AgenticScope agenticScope = (AgenticScope)LangChain4jManaged.current(AgenticScope.class);
        if (agenticScope != null) {
            if (this.chatModelProvider != null) {
                this.context.chatModel = this.chatModelProvider.apply(agenticScope);
            } else if (this.streamingChatModelProvider != null) {
                this.context.streamingChatModel = this.streamingChatModelProvider.apply(agenticScope);
            }
            return method.invoke(this.agent, args);
        }
        return this.invokeStandaloneAgent(method, args);
    }

    private Object invokeStandaloneAgent(Method method, Object[] args) {
        LOGGER.warn("Improper invocation of a standalone agent outside of an agentic system, consider using AiServices instead.");
        DefaultAgenticScope standaloneAgenticScope = DefaultAgenticScope.ephemeralAgenticScope();
        HashMap<Class<AgenticScope>, DefaultAgenticScope> m = new HashMap<Class<AgenticScope>, DefaultAgenticScope>();
        m.put(AgenticScope.class, standaloneAgenticScope);
        LangChain4jManaged.setCurrent(m);
        Map<String, Object> namedArgs = AgentInvocationHandler.argToMap(method, args);
        ListenerNotifierUtil.beforeAgentInvocation(this.agentListener, standaloneAgenticScope, this, namedArgs);
        Object result = null;
        try {
            result = method.invoke(this.agent, args);
        }
        catch (Exception e) {
            AgentInvocationException invocationException = new AgentInvocationException("Failed to invoke agent method: " + method, e);
            ListenerNotifierUtil.agentError(this.agentListener, standaloneAgenticScope, this, namedArgs, (Throwable)((Object)invocationException));
            throw invocationException;
        }
        finally {
            LangChain4jManaged.removeCurrent();
            if (result != null) {
                ListenerNotifierUtil.afterAgentInvocation(this.agentListener, standaloneAgenticScope, this, namedArgs, result);
            }
        }
        return result;
    }

    private static Map<String, Object> argToMap(Method method, Object[] args) {
        if (method.getParameterCount() == 1 && Map.class.isAssignableFrom(method.getParameters()[0].getType())) {
            return (Map)args[0];
        }
        if (args == null || args.length == 0) {
            return Collections.emptyMap();
        }
        HashMap<String, Object> namedArgs = new HashMap<String, Object>();
        for (int i = 0; i < args.length; ++i) {
            namedArgs.put(ParameterNameResolver.name((Parameter)method.getParameters()[i]), args[i]);
        }
        return namedArgs;
    }

    private static Optional<UserMessage> lastUserMessage(Collection<ChatMessage> messages) {
        return messages.stream().filter(UserMessage.class::isInstance).map(UserMessage.class::cast).reduce((first, second) -> second);
    }

    public String toString() {
        return "Agent<" + this.builder.agentServiceClass.getSimpleName() + ">";
    }

    @Override
    public void setParent(InternalAgent parent) {
        if (this.builder.hasChatMemory() && parent != null && !parent.allowChatMemory()) {
            throw new AgenticSystemConfigurationException("Agents with chat memory can't be a subagent of " + parent.type());
        }
        this.parent = parent;
        this.registerInheritedParentListener(parent.listener());
    }

    @Override
    public void registerInheritedParentListener(AgentListener parentListener) {
        if (parentListener != null && parentListener.inheritedBySubagents() && AgentInvocationHandler.isNewListener(this.agentListener, parentListener)) {
            this.agentListener = ComposedAgentListener.composeWithInherited(this.agentListener, parentListener);
            this.context.toolService.beforeToolExecution(beforeToolExecution -> this.agentListener.beforeAgentToolExecution(new BeforeAgentToolExecution(this, (BeforeToolExecution)beforeToolExecution)));
            this.context.toolService.afterToolExecution(toolExecution -> this.agentListener.afterAgentToolExecution(new AfterAgentToolExecution(this, (ToolExecution)toolExecution)));
        }
    }

    private static boolean isNewListener(AgentListener currentListener, AgentListener newListener) {
        if (newListener == currentListener) {
            return false;
        }
        if (currentListener instanceof ComposedAgentListener) {
            ComposedAgentListener composed = (ComposedAgentListener)currentListener;
            return !composed.contains(newListener);
        }
        return true;
    }

    @Override
    public void appendId(String idSuffix) {
        this.agentId = this.agentId + idSuffix;
    }

    @Override
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Override
    public AgentListener listener() {
        return this.agentListener;
    }

    @Override
    public Class<?> type() {
        return this.builder.agentServiceClass;
    }

    @Override
    public Class<? extends Planner> plannerType() {
        return null;
    }

    @Override
    public String name() {
        return this.builder.name;
    }

    @Override
    public String agentId() {
        return this.agentId;
    }

    @Override
    public String description() {
        return this.builder.description;
    }

    @Override
    public Type outputType() {
        return this.builder.agentReturnType;
    }

    @Override
    public String outputKey() {
        return this.builder.outputKey;
    }

    @Override
    public boolean async() {
        return this.builder.async;
    }

    @Override
    public boolean optional() {
        return this.builder.optional;
    }

    @Override
    public List<AgentArgument> arguments() {
        return this.builder.arguments;
    }

    @Override
    public AgentInstance parent() {
        return this.parent;
    }

    @Override
    public List<AgentInstance> subagents() {
        return Collections.emptyList();
    }

    @Override
    public AgenticSystemTopology topology() {
        return AgenticSystemTopology.AI_AGENT;
    }
}

