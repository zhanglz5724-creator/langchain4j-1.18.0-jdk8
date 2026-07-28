/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.memory.ChatMemory
 *  dev.langchain4j.service.TokenStream
 *  dev.langchain4j.service.memory.ChatMemoryAccess
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.agentic.scope;

import dev.langchain4j.Internal;
import dev.langchain4j.agentic.agent.AgentInvocationException;
import dev.langchain4j.agentic.agent.ChatMessagesAccess;
import dev.langchain4j.agentic.agent.ErrorContext;
import dev.langchain4j.agentic.agent.ErrorRecoveryResult;
import dev.langchain4j.agentic.declarative.TypedKey;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.internal.DeferredResponse;
import dev.langchain4j.agentic.internal.DelayedResponse;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgentInvocation;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.scope.AgenticScopeRegistry;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.memory.ChatMemoryAccess;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Internal
public class DefaultAgenticScope
implements AgenticScope {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAgenticScope.class);
    private final Object memoryId;
    private final Map<String, Object> state = new ConcurrentHashMap<String, Object>();
    private final List<AgentInvocation> agentInvocations = Collections.synchronizedList(new ArrayList());
    private final List<AgentMessage> context = Collections.synchronizedList(new ArrayList());
    private final transient Map<String, Object> agents = new ConcurrentHashMap<String, Object>();
    private final transient Map<String, Object> executionContexts = new ConcurrentHashMap<String, Object>();
    private static final Function<ErrorContext, ErrorRecoveryResult> DEFAULT_ERROR_RECOVERY = errorContext -> ErrorRecoveryResult.throwException();
    private transient Function<ErrorContext, ErrorRecoveryResult> errorHandler = DEFAULT_ERROR_RECOVERY;
    private static Predicate<Object> serializableStateFilter = o -> !DefaultAgenticScope.isProxy(o) && !DefaultAgenticScope.isTokenStream(o) && !DefaultAgenticScope.isFuture(o);
    private final Kind kind;
    private final transient ReadWriteLock lock;

    private static boolean isProxy(Object obj) {
        return Proxy.isProxyClass(obj.getClass());
    }

    private static boolean isTokenStream(Object obj) {
        return obj instanceof TokenStream;
    }

    private static boolean isFuture(Object obj) {
        return obj instanceof Future;
    }

    DefaultAgenticScope serializableCopy() {
        DefaultAgenticScope copy = new DefaultAgenticScope(this.memoryId, this.kind);
        this.state.forEach((key, value) -> {
            if (DefaultAgenticScope.isSerializable(value)) {
                copy.state.put((String)key, value);
            }
        });
        copy.agentInvocations.addAll(this.agentInvocations);
        copy.context.addAll(this.context);
        return copy;
    }

    public static boolean isSerializable(Object value) {
        return value == null || serializableStateFilter.test(value);
    }

    public static void addSerializableStateFilter(Predicate<Object> filter) {
        serializableStateFilter = serializableStateFilter.and(filter);
    }

    DefaultAgenticScope(Kind kind) {
        this(Utils.randomUUID(), kind);
    }

    DefaultAgenticScope(Object memoryId, Kind kind) {
        this.memoryId = memoryId;
        this.kind = kind;
        this.lock = kind == Kind.PERSISTENT ? new ReentrantReadWriteLock() : null;
    }

    public static DefaultAgenticScope ephemeralAgenticScope() {
        return new DefaultAgenticScope(Kind.EPHEMERAL);
    }

    @Override
    public Object memoryId() {
        return this.memoryId;
    }

    @Override
    public void writeState(String key, Object value) {
        this.withReadLock(() -> {
            Object old = value == null ? this.state.remove(key) : this.state.put(key, value);
            if (old instanceof DeferredResponse && !((DeferredResponse)old).isDone()) {
                ((DeferredResponse)old).complete(value);
            }
        });
    }

    @Override
    public <T> void writeState(Class<? extends TypedKey<T>> key, T value) {
        this.writeState(AgentUtil.keyName(key), value);
    }

    @Override
    public void writeStateIfAbsent(String key, Object value) {
        if (value != null) {
            this.withReadLock(() -> this.state.compute(key, (k, v) -> this.hasState((String)k) ? v : value));
        }
    }

    @Override
    public <T> void writeStateIfAbsent(Class<? extends TypedKey<T>> key, T value) {
        this.writeStateIfAbsent(AgentUtil.keyName(key), value);
    }

    @Override
    public void writeStates(Map<String, Object> newState) {
        this.withReadLock(() -> this.state.putAll(newState));
    }

    @Override
    public boolean hasState(String key) {
        Object value = this.state.get(key);
        if (value == null) {
            return false;
        }
        if (value instanceof String) {
            String s = (String)value;
            return !s.trim().isEmpty();
        }
        return true;
    }

    @Override
    public boolean hasState(Class<? extends TypedKey<?>> key) {
        return this.hasState(AgentUtil.keyName(key));
    }

    @Override
    public Object readState(String key) {
        return this.readStateBlocking(key, this.state.get(key));
    }

    @Override
    public <T> T readState(String key, T defaultValue) {
        return (T)this.readStateBlocking(key, this.state.getOrDefault(key, defaultValue));
    }

    @Override
    public <T> T readState(Class<? extends TypedKey<T>> key) {
        return this.readState(AgentUtil.keyName(key), AgentUtil.keyDefaultValue(key));
    }

    private Object readStateBlocking(String key, Object state) {
        if (state instanceof DelayedResponse) {
            DelayedResponse asyncResponse = (DelayedResponse)state;
            state = asyncResponse.blockingGet();
            this.writeState(key, state);
        }
        return state;
    }

    @Override
    public Map<String, Object> state() {
        return this.state;
    }

    public <T> T getOrCreateAgent(String agentId, Function<DefaultAgenticScope, T> agentFactory) {
        return (T)this.agents.computeIfAbsent(agentId, id -> agentFactory.apply(this));
    }

    public void registerAgentInvocation(AgentInvocation agentInvocation, Object agent) {
        this.withReadLock(() -> {
            this.agentInvocations.add(agentInvocation);
            this.registerContext(agentInvocation, agent);
        });
    }

    public void rootCallStarted(AgenticScopeRegistry registry) {
    }

    public void rootCallEnded(AgenticScopeRegistry registry, AgentListener agentListener) {
        this.state.replaceAll(this::readStateBlocking);
        if (this.kind == Kind.EPHEMERAL) {
            registry.evict(this.memoryId, agentListener);
        } else if (this.kind == Kind.PERSISTENT) {
            this.flush(registry);
        }
    }

    private void flush(AgenticScopeRegistry registry) {
        this.lock.writeLock().lock();
        try {
            registry.update(this);
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }

    private void registerContext(AgentInvocation agentInvocation, Object agent) {
        ChatMemory chatMemory;
        ChatMemory chatMemory2 = chatMemory = agent instanceof ChatMemoryAccess ? ((ChatMemoryAccess)agent).getChatMemory(this.memoryId) : null;
        if (chatMemory != null) {
            this.registerContextFromChatMemory(agentInvocation, chatMemory);
        } else if (agentInvocation.output() != null && agent instanceof ChatMessagesAccess) {
            ChatMessagesAccess chatMessagesAccess = (ChatMessagesAccess)agent;
            this.context.add(new AgentMessage(agentInvocation.agentName(), agentInvocation.agentId(), (ChatMessage)chatMessagesAccess.lastUserMessage(this.memoryId())));
            this.context.add(new AgentMessage(agentInvocation.agentName(), agentInvocation.agentId(), (ChatMessage)AiMessage.aiMessage((String)agentInvocation.output().toString())));
            chatMessagesAccess.removeLastResponseEvent(this.memoryId());
        }
    }

    private void registerContextFromChatMemory(AgentInvocation agentInvocation, ChatMemory chatMemory) {
        List agentMessages = chatMemory.messages();
        if (Utils.isNullOrEmpty((Collection)agentMessages)) {
            return;
        }
        ChatMessage lastMessage = (ChatMessage)agentMessages.get(agentMessages.size() - 1);
        if (!(lastMessage instanceof AiMessage)) {
            return;
        }
        AiMessage aiMessage = (AiMessage)lastMessage;
        for (int i = agentMessages.size() - 1; i >= 0; --i) {
            if (!(agentMessages.get(i) instanceof UserMessage)) continue;
            UserMessage userMessage = (UserMessage)agentMessages.get(i);
            this.context.add(new AgentMessage(agentInvocation.agentName(), agentInvocation.agentId(), (ChatMessage)userMessage));
            this.context.add(new AgentMessage(agentInvocation.agentName(), agentInvocation.agentId(), (ChatMessage)aiMessage));
            return;
        }
    }

    public List<AgentMessage> context() {
        return this.context;
    }

    @Override
    public String contextAsConversation(Object ... agents) {
        Predicate<String> agentFilter = agents != null && agents.length > 0 ? Arrays.stream(agents).filter(AgentInstance.class::isInstance).map(AgentInstance.class::cast).map(AgentInstance::name).collect(Collectors.toList())::contains : agent -> true;
        return this.contextAsConversation(agentFilter);
    }

    @Override
    public String contextAsConversation(String ... agentNames) {
        Predicate<String> agentFilter = agentNames != null && agentNames.length > 0 ? Arrays.asList(agentNames)::contains : agent -> true;
        return this.contextAsConversation(agentFilter);
    }

    private String contextAsConversation(Predicate<String> agentFilter) {
        StringBuilder sb = new StringBuilder();
        for (AgentMessage agentMessage : this.context) {
            if (!agentFilter.test(agentMessage.agentName())) continue;
            ChatMessage message = agentMessage.message();
            if (message instanceof UserMessage) {
                UserMessage userMessage = (UserMessage)message;
                sb.append("User: \"").append(userMessage.singleText()).append("\"\n");
                continue;
            }
            if (!(message instanceof AiMessage)) continue;
            AiMessage aiMessage = (AiMessage)message;
            sb.append(agentMessage.agentName()).append(" agent: \"").append(aiMessage.text()).append("\"\n");
        }
        String contextAsConversation = sb.toString();
        LOG.trace("AgenticScope context as conversation: '{}'", (Object)contextAsConversation);
        return contextAsConversation;
    }

    @Override
    public List<AgentInvocation> agentInvocations() {
        return this.agentInvocations;
    }

    @Override
    public List<AgentInvocation> agentInvocations(String agentName) {
        return this.agentInvocations.stream().filter(inv -> inv.agentName().equals(agentName)).collect(Collectors.toList());
    }

    @Override
    public List<AgentInvocation> agentInvocations(Class<?> agentType) {
        return this.agentInvocations.stream().filter(inv -> inv.agentType().equals(agentType)).collect(Collectors.toList());
    }

    public String toString() {
        return "AgenticScope{memoryId='" + this.memoryId + '\'' + ", state=" + this.state + '}';
    }

    private void withReadLock(Runnable action) {
        if (this.kind == Kind.PERSISTENT) {
            this.lock.readLock().lock();
            try {
                action.run();
            }
            finally {
                this.lock.readLock().unlock();
            }
        } else {
            action.run();
        }
    }

    public DefaultAgenticScope withErrorHandler(Function<ErrorContext, ErrorRecoveryResult> errorHandler) {
        if (errorHandler != null) {
            this.errorHandler = errorHandler;
        }
        return this;
    }

    public ErrorRecoveryResult handleError(String agentName, AgentInvocationException exception) {
        return this.errorHandler.apply(new ErrorContext(agentName, this, exception));
    }

    public void checkpoint(AgenticScopeRegistry registry) {
        if (this.kind == Kind.PERSISTENT) {
            this.flush(registry);
        }
    }

    @Override
    public boolean completePendingResponse(String responseId, Object value) {
        for (Map.Entry<String, Object> entry : this.state.entrySet()) {
            if (!(entry.getValue() instanceof DeferredResponse) || !((DeferredResponse)entry.getValue()).responseId().equals(responseId)) continue;
            DeferredResponse deferred = (DeferredResponse)entry.getValue();
            boolean completed = deferred.complete(value);
            if (completed) {
                this.withReadLock(() -> this.state.put((String)entry.getKey(), value));
            }
            return completed;
        }
        return false;
    }

    @Override
    public Set<String> pendingResponseIds() {
        return this.state.values().stream().filter(DeferredResponse.class::isInstance).map(DeferredResponse.class::cast).filter(p -> !p.isDone()).map(DeferredResponse::responseId).collect(Collectors.toSet());
    }

    @Override
    public void writeExecutionContext(String key, Object context) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        this.executionContexts.put(key, context);
    }

    @Override
    public Object executionContext(String key) {
        return this.executionContexts.get(key);
    }

    @Override
    public <T> T executionContextAs(String key, Class<T> type) {
        return (T)this.executionContexts.get(key);
    }

    public static enum Kind {
        EPHEMERAL,
        REGISTERED,
        PERSISTENT;

    }

    public class AgentMessage {
        private final String agentName;
        private final String agentId;
        private final ChatMessage message;

        public AgentMessage(String agentName, String agentId, ChatMessage message) {
            this.agentName = agentName;
            this.agentId = agentId;
            this.message = message;
        }

        public String agentName() {
            return this.agentName;
        }

        public String agentId() {
            return this.agentId;
        }

        public ChatMessage message() {
            return this.message;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AgentMessage)) {
                return false;
            }
            AgentMessage other = (AgentMessage)o;
            if (!Objects.equals(this.agentName, other.agentName)) {
                return false;
            }
            if (!Objects.equals(this.agentId, other.agentId)) {
                return false;
            }
            return Objects.equals(this.message, other.message);
        }

        public int hashCode() {
            return Objects.hash(this.agentName, this.agentId, this.message);
        }

        public String toString() {
            return "AgentMessage{agentName=" + this.agentName + ", agentId=" + this.agentId + ", message=" + this.message + "}";
        }
    }
}

