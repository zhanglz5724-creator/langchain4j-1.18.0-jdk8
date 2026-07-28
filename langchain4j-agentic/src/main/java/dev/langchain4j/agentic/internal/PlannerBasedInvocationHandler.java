/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.DefaultExecutorProvider
 *  dev.langchain4j.invocation.InvocationParameters
 *  dev.langchain4j.service.MemoryId
 *  dev.langchain4j.service.ParameterNameResolver
 *  dev.langchain4j.service.TokenStream
 *  dev.langchain4j.service.memory.ChatMemoryAccess
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.agent.ErrorContext;
import dev.langchain4j.agentic.agent.ErrorRecoveryResult;
import dev.langchain4j.agentic.internal.AbstractServiceBuilder;
import dev.langchain4j.agentic.internal.AgentExecutor;
import dev.langchain4j.agentic.internal.AgentInvoker;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.internal.AgenticScopeOwner;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.internal.PlannerExecutor;
import dev.langchain4j.agentic.internal.SuspendedResponse;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.AgentMonitor;
import dev.langchain4j.agentic.observability.ComposedAgentListener;
import dev.langchain4j.agentic.observability.ListenerNotifierUtil;
import dev.langchain4j.agentic.observability.MonitoredAgent;
import dev.langchain4j.agentic.planner.Action;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.ChatMemoryAccessProvider;
import dev.langchain4j.agentic.planner.InitPlanningContext;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.planner.PlanningContext;
import dev.langchain4j.agentic.scope.AgentInvocation;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.scope.AgenticScopeAccess;
import dev.langchain4j.agentic.scope.AgenticScopeRegistry;
import dev.langchain4j.agentic.scope.AgenticSystemSuspendedException;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.internal.DefaultExecutorProvider;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.ParameterNameResolver;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.memory.ChatMemoryAccess;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PlannerBasedInvocationHandler
implements InvocationHandler,
InternalAgent {
    private final Executor executor;
    private final Function<AgenticScope, Object> output;
    protected AgentListener agentListener;
    private final Consumer<AgenticScope> beforeCall;
    private final DefaultAgenticScope agenticScope;
    private final Function<ErrorContext, ErrorRecoveryResult> errorHandler;
    private final AtomicReference<AgenticScopeRegistry> agenticScopeRegistry = new AtomicReference();
    private final AbstractServiceBuilder<?, ?> service;
    private final Supplier<Planner> plannerSupplier;
    private final Planner defaultPlannerInstance;
    private final Class<?> type;
    private final String name;
    private final String description;
    private final Type outputType;
    private boolean allowStreamingOutput;
    private final String outputKey;
    private final List<AgentArgument> arguments;
    private final List<AgentInstance> subagents;
    private String agentId;
    private InternalAgent parent;

    public PlannerBasedInvocationHandler(AbstractServiceBuilder<?, ?> service, Supplier<Planner> plannerSupplier) {
        this(service, null, service.name, plannerSupplier, null);
        for (int i = 0; i < service.subagents.size(); ++i) {
            service.subagents.get(i).setParent(this, i);
        }
        AgentUtil.agenticSystemDataTypes(this);
    }

    private PlannerBasedInvocationHandler(AbstractServiceBuilder<?, ?> service, InternalAgent parent, String agentId, Supplier<Planner> plannerSupplier, DefaultAgenticScope agenticScope) {
        this.service = service;
        this.agentId = agentId;
        this.output = service.output;
        this.executor = service.executor;
        this.beforeCall = service.beforeCall;
        this.errorHandler = service.errorHandler;
        this.agentListener = service.agentListener;
        this.plannerSupplier = plannerSupplier;
        this.defaultPlannerInstance = plannerSupplier.get();
        this.agenticScope = agenticScope;
        this.type = service.agentServiceClass;
        this.name = service.name;
        this.description = service.description;
        this.outputType = service.agentReturnType();
        this.allowStreamingOutput = UntypedAgent.class.isAssignableFrom(this.type) || TokenStream.class.isAssignableFrom(AgentUtil.rawType(this.outputType));
        this.outputKey = service.outputKey;
        this.arguments = service.agenticMethod != null ? AgentUtil.argumentsFromMethod(service.agenticMethod) : Collections.emptyList();
        this.subagents = service.subagents.stream().map(AgentInstance.class::cast).collect(Collectors.toList());
        this.setParent(parent);
    }

    public AgenticScopeOwner withAgenticScope(DefaultAgenticScope agenticScope) {
        PlannerBasedInvocationHandler newHandler = new PlannerBasedInvocationHandler(this.service, this.parent, this.agentId, this.plannerSupplier, agenticScope);
        if (this.service.agentInstanceFactory != null) {
            return (AgenticScopeOwner)this.service.agentInstanceFactory.apply(newHandler);
        }
        return (AgenticScopeOwner)Proxy.newProxyInstance(this.type.getClassLoader(), new Class[]{this.type, InternalAgent.class, AgenticScopeOwner.class}, newHandler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        AgenticScopeRegistry registry = this.agenticScopeRegistry();
        if (method.getDeclaringClass() == AgenticScopeOwner.class) {
            switch (method.getName()) {
                case "withAgenticScope": {
                    return this.withAgenticScope((DefaultAgenticScope)args[0]);
                }
                case "registry": {
                    return registry;
                }
            }
            throw new UnsupportedOperationException("Unknown method on AgenticScopeOwner class : " + method.getName());
        }
        if (method.getDeclaringClass() == AgenticScopeAccess.class) {
            switch (method.getName()) {
                case "getAgenticScope": {
                    return registry.get(args[0]);
                }
                case "evictAgenticScope": {
                    return registry.evict(args[0], this.agentListener);
                }
            }
            throw new UnsupportedOperationException("Unknown method on AgenticScopeAccess class : " + method.getName());
        }
        if (method.getDeclaringClass() == AgentInstance.class || method.getDeclaringClass() == InternalAgent.class) {
            try {
                return method.invoke(this, args);
            }
            catch (Exception e) {
                throw e.getCause() != null ? (Exception)e.getCause() : e;
            }
        }
        if (method.getDeclaringClass() == MonitoredAgent.class) {
            return ComposedAgentListener.listenerOfType(this.agentListener, AgentMonitor.class);
        }
        if (method.getDeclaringClass() == Object.class) {
            switch (method.getName()) {
                case "toString": {
                    return this.service.serviceType() + "<" + this.type.getSimpleName() + ">";
                }
                case "hashCode": {
                    return System.identityHashCode(this);
                }
            }
            throw new UnsupportedOperationException("Unknown method on Object class : " + method.getName());
        }
        if (method.getDeclaringClass() == ChatMemoryAccess.class) {
            Object memoryId = args[0];
            return this.accessChatMemory(this.getOrCreateAgenticScope(registry, memoryId), method.getName(), memoryId);
        }
        return this.executeAgentMethod(registry, method, args);
    }

    private AgenticScopeRegistry agenticScopeRegistry() {
        if (this.isRootCall()) {
            this.agenticScopeRegistry.compareAndSet(null, new AgenticScopeRegistry(this.type.getName()));
        }
        return this.agenticScopeRegistry.get();
    }

    private Object executeAgentMethod(AgenticScopeRegistry registry, Method method, Object[] args) {
        Object output;
        Object result;
        Map<String, Object> namedArgs;
        DefaultAgenticScope currentScope = this.currentAgenticScope(registry, method, args);
        PlannerBasedInvocationHandler.writeAgenticScope(currentScope, method, args);
        this.beforeCall.accept(currentScope);
        Map<String, Object> map = namedArgs = this.isRootCall() ? PlannerBasedInvocationHandler.argToMap(method, args) : null;
        if (this.isRootCall()) {
            currentScope.rootCallStarted(registry);
            ListenerNotifierUtil.beforeAgentInvocation(this.agentListener, currentScope, this, namedArgs);
        }
        Planner planner = this.plannerSupplier.get();
        planner.init(new InitPlanningContext(currentScope, this, this.subagents));
        try {
            result = new PlannerLoop(planner, currentScope, registry).loop();
        }
        catch (Exception e) {
            if (this.isRootCall()) {
                ListenerNotifierUtil.agentError(this.agentListener, currentScope, this, namedArgs, e);
                currentScope.rootCallEnded(registry, this.agentListener);
            }
            throw e;
        }
        if (result instanceof Action && ((Action)result).isSuspended()) {
            Action action = (Action)result;
            ListenerNotifierUtil.onAgenticSystemSuspended(this.agentListener, currentScope);
            if (this.isRootCall() && method.getReturnType().equals(ResultWithAgenticScope.class)) {
                return new ResultWithAgenticScope<Object>(currentScope, null, true, () -> (ResultWithAgenticScope)this.executeAgentMethod(registry, method, args));
            }
            throw new AgenticSystemSuspendedException(currentScope);
        }
        Object object = output = this.outputKey != null ? currentScope.readState(this.outputKey) : result;
        if (this.isRootCall()) {
            ListenerNotifierUtil.afterAgentInvocation(this.agentListener, currentScope, this, namedArgs, output);
            currentScope.rootCallEnded(registry, this.agentListener);
        }
        return method.getReturnType().equals(ResultWithAgenticScope.class) ? new ResultWithAgenticScope<Object>(currentScope, output) : output;
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

    public String toString() {
        return this.service.serviceType() + "<" + this.type.getSimpleName() + ">";
    }

    @Override
    public Class<?> type() {
        return this.type;
    }

    @Override
    public Class<? extends Planner> plannerType() {
        return this.defaultPlannerInstance.getClass();
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String agentId() {
        return this.agentId;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public Type outputType() {
        return this.outputType;
    }

    @Override
    public String outputKey() {
        return this.outputKey;
    }

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public List<AgentArgument> arguments() {
        return this.arguments;
    }

    @Override
    public AgentInstance parent() {
        return this.parent;
    }

    @Override
    public void setParent(InternalAgent parent) {
        if (parent == null) {
            return;
        }
        this.parent = parent;
        this.registerInheritedParentListener(parent.listener());
        if (!parent.allowStreamingOutput()) {
            this.allowStreamingOutput = false;
        }
    }

    @Override
    public void registerInheritedParentListener(AgentListener parentListener) {
        if (parentListener != null && parentListener.inheritedBySubagents()) {
            this.agentListener = ComposedAgentListener.composeWithInherited(this.agentListener, parentListener);
            this.subagents().stream().map(InternalAgent.class::cast).forEach(agent -> agent.registerInheritedParentListener(parentListener));
        }
    }

    @Override
    public boolean allowStreamingOutput() {
        return this.allowStreamingOutput;
    }

    @Override
    public boolean allowChatMemory() {
        return !"ParallelMapper".equals(this.service.serviceType());
    }

    @Override
    public void appendId(String idSuffix) {
        this.agentId = this.agentId + idSuffix;
    }

    @Override
    public List<AgentInstance> subagents() {
        return this.subagents;
    }

    @Override
    public AgenticSystemTopology topology() {
        return this.defaultPlannerInstance.topology();
    }

    @Override
    public <T extends AgentInstance> T as(Class<T> agentInstanceClass) {
        return this.defaultPlannerInstance.as(agentInstanceClass, this);
    }

    @Override
    public AgentListener listener() {
        return this.agentListener;
    }

    private static boolean hasSuspendedResponses(AgenticScope agenticScope) {
        return agenticScope.state().values().stream().anyMatch(v -> v instanceof SuspendedResponse && !((SuspendedResponse)v).isDone());
    }

    private boolean isRootCall() {
        return this.agenticScope == null;
    }

    private static void writeAgenticScope(DefaultAgenticScope agenticScope, Method method, Object[] args) {
        if (method.getDeclaringClass() == UntypedAgent.class) {
            agenticScope.writeStates((Map)args[0]);
        } else {
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; ++i) {
                int index = i;
                if (InvocationParameters.class.isAssignableFrom(parameters[i].getType())) {
                    if (args[index] == null) continue;
                    agenticScope.writeExecutionContext(InvocationParameters.class, args[index]);
                    continue;
                }
                AgentInvoker.optionalParameterName(parameters[i]).ifPresent(argName -> agenticScope.writeState((String)argName, args[index]));
            }
        }
    }

    private DefaultAgenticScope currentAgenticScope(AgenticScopeRegistry registry, Method method, Object[] args) {
        if (this.agenticScope != null) {
            return this.agenticScope;
        }
        Object memoryId = this.memoryId(method, args);
        DefaultAgenticScope newAgenticScope = memoryId != null ? this.getOrCreateAgenticScope(registry, memoryId) : this.createEphemeralAgenticScope(registry);
        return newAgenticScope.withErrorHandler(this.errorHandler);
    }

    private DefaultAgenticScope createEphemeralAgenticScope(AgenticScopeRegistry registry) {
        DefaultAgenticScope agenticScope = registry.createEphemeralAgenticScope();
        ListenerNotifierUtil.afterAgenticScopeCreated(this.agentListener, agenticScope);
        return agenticScope;
    }

    private DefaultAgenticScope getOrCreateAgenticScope(AgenticScopeRegistry registry, Object memoryId) {
        DefaultAgenticScope agenticScope = registry.get(memoryId);
        if (agenticScope == null) {
            agenticScope = registry.create(memoryId);
            ListenerNotifierUtil.afterAgenticScopeCreated(this.agentListener, agenticScope);
        }
        return agenticScope;
    }

    private Object memoryId(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; ++i) {
            if (parameters[i].getAnnotation(MemoryId.class) == null) continue;
            return args[i];
        }
        return null;
    }

    private Object accessChatMemory(AgenticScope agenticScope, String methodName, Object memoryId) {
        ChatMemoryAccess chatMemoryAccess = ((ChatMemoryAccessProvider)((Object)this.defaultPlannerInstance)).chatMemoryAccess(agenticScope);
        switch (methodName) {
            case "getChatMemory": {
                return chatMemoryAccess.getChatMemory(memoryId);
            }
            case "evictChatMemory": {
                return chatMemoryAccess.evictChatMemory(memoryId);
            }
        }
        throw new UnsupportedOperationException("Unknown method on ChatMemoryAccess class : " + methodName);
    }

    private class PlannerLoop
    implements PlannerExecutor {
        static final String EXECUTION_STATE_PREFIX = "__planner_state_";
        private static final String COMPLETED_AGENTS_KEY = "__completedAgents";
        private final Planner planner;
        private final DefaultAgenticScope agenticScope;
        private final AgenticScopeRegistry registry;
        private final ReentrantLock lock = new ReentrantLock();
        private final Set<String> completedAgentIds = new HashSet<String>();
        private volatile Action nextAction = null;

        private PlannerLoop(Planner planner, DefaultAgenticScope agenticScope, AgenticScopeRegistry registry) {
            this.planner = planner;
            this.agenticScope = agenticScope;
            this.registry = registry;
        }

        public Object loop() {
            Map<String, Object> savedState = this.agenticScope.readState(this.executionStateId(), Collections.emptyMap());
            if (!savedState.isEmpty()) {
                this.restoreCompletedAgents(savedState);
                this.planner.restoreExecutionState(savedState);
            }
            this.nextAction = this.planner.firstAction(new PlanningContext(this.agenticScope, null));
            this.nextAction = this.filterCompletedAgents(this.nextAction);
            block4: while (this.nextAction == null || !this.nextAction.isDone()) {
                if (this.nextAction == null) {
                    Thread.yield();
                    continue;
                }
                if (PlannerBasedInvocationHandler.hasSuspendedResponses(this.agenticScope)) {
                    this.nextAction = this.planner.suspend();
                    break;
                }
                List<AgentExecutor> agents = ((Action.AgentCallAction)this.nextAction).agentsToCall();
                this.nextAction = null;
                switch (agents.size()) {
                    case 0: {
                        Thread.yield();
                        continue block4;
                    }
                    case 1: {
                        agents.get(0).execute(this.agenticScope, this);
                        continue block4;
                    }
                }
                this.parallelExecution(agents);
            }
            if (this.nextAction != null && this.nextAction.isSuspended()) {
                return this.nextAction;
            }
            if (PlannerBasedInvocationHandler.hasSuspendedResponses(this.agenticScope)) {
                return this.planner.suspend();
            }
            this.agenticScope.writeState(this.executionStateId(), null);
            return this.result();
        }

        private Action filterCompletedAgents(Action action) {
            if (this.completedAgentIds.isEmpty() || !(action instanceof Action.AgentCallAction) || ((Action.AgentCallAction)action).agentsToCall().size() <= 1) {
                return action;
            }
            Action.AgentCallAction callAction = (Action.AgentCallAction)action;
            List<AgentExecutor> remaining = callAction.agentsToCall().stream().filter(a -> !this.completedAgentIds.contains(a.agentId())).collect(Collectors.toList());
            return remaining.isEmpty() ? this.planner.done() : new Action.AgentCallAction(remaining);
        }

        private void restoreCompletedAgents(Map<String, Object> savedState) {
            Object completed = savedState.get(COMPLETED_AGENTS_KEY);
            if (completed instanceof List) {
                List list = (List)completed;
                for (Object id : list) {
                    this.completedAgentIds.add(id.toString());
                }
            }
        }

        private String executionStateId() {
            return EXECUTION_STATE_PREFIX + PlannerBasedInvocationHandler.this.agentId();
        }

        private void parallelExecution(List<AgentExecutor> agents) {
            ExecutorService exec = PlannerBasedInvocationHandler.this.executor != null ? PlannerBasedInvocationHandler.this.executor : DefaultExecutorProvider.getDefaultExecutorService();
            CompletableFuture[] tasks = (CompletableFuture[])agents.stream().map(agentExecutor -> CompletableFuture.supplyAsync(() -> agentExecutor.execute(this.agenticScope, this), exec)).toArray(CompletableFuture[]::new);
            try {
                CompletableFuture.allOf(tasks).get();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        private Object result() {
            Object result;
            Object object = result = PlannerBasedInvocationHandler.this.output != null ? PlannerBasedInvocationHandler.this.output.apply(this.agenticScope) : this.nextAction.result();
            if (PlannerBasedInvocationHandler.this.outputKey != null) {
                if (result != null) {
                    this.agenticScope.writeState(PlannerBasedInvocationHandler.this.outputKey, result);
                    return result;
                }
                return this.agenticScope.readState(PlannerBasedInvocationHandler.this.outputKey);
            }
            return result;
        }

        private void saveExecutionState() {
            Map<String, Object> execState = this.planner.executionState();
            if (!this.completedAgentIds.isEmpty()) {
                execState = new HashMap<String, Object>(execState);
                execState.put(COMPLETED_AGENTS_KEY, new ArrayList<String>(this.completedAgentIds));
            }
            if (!execState.isEmpty()) {
                this.agenticScope.writeState(this.executionStateId(), execState);
            }
        }

        @Override
        public void onSubagentSuspended() {
            this.lock.lock();
            try {
                this.nextAction = this.planner.suspend();
                this.saveExecutionState();
                if (this.registry != null) {
                    this.agenticScope.checkpoint(this.registry);
                }
            }
            finally {
                this.lock.unlock();
            }
        }

        @Override
        public void onSubagentInvoked(AgentInvocation agentInvocation) {
            this.lock.lock();
            try {
                this.completedAgentIds.add(agentInvocation.agentId());
                this.nextAction = this.composeActions(this.nextAction, this.planner.nextAction(new PlanningContext(this.agenticScope, agentInvocation)));
                this.saveExecutionState();
                if (this.registry != null) {
                    this.agenticScope.checkpoint(this.registry);
                }
            }
            finally {
                this.lock.unlock();
            }
        }

        @Override
        public boolean propagateStreaming() {
            return PlannerBasedInvocationHandler.this.allowStreamingOutput && this.planner.terminated();
        }

        private Action composeActions(Action first, Action second) {
            if (first == null || first.isDone() || this.isEmptyCall(first)) {
                return second;
            }
            if (second == null || second.isDone() || this.isEmptyCall(second)) {
                return first;
            }
            ArrayList<AgentExecutor> agentsToCall = new ArrayList<AgentExecutor>();
            agentsToCall.addAll(((Action.AgentCallAction)first).agentsToCall());
            agentsToCall.addAll(((Action.AgentCallAction)second).agentsToCall());
            return new Action.AgentCallAction(agentsToCall);
        }

        private boolean isEmptyCall(Action action) {
            return action instanceof Action.AgentCallAction && ((Action.AgentCallAction)action).agentsToCall().isEmpty();
        }
    }
}

