/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.agent.ErrorContext;
import dev.langchain4j.agentic.agent.ErrorRecoveryResult;
import dev.langchain4j.agentic.declarative.TypedKey;
import dev.langchain4j.agentic.internal.AgentExecutor;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.internal.PlannerBasedInvocationHandler;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.AgentMonitor;
import dev.langchain4j.agentic.observability.ComposedAgentListener;
import dev.langchain4j.agentic.observability.MonitoredAgent;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.internal.Utils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractServiceBuilder<T, S> {
    private static final Consumer<AgenticScope> DEFAULT_INIT_FUNCTION = agenticScope -> {};
    protected final Class<T> agentServiceClass;
    protected final Method agenticMethod;
    protected Consumer<AgenticScope> beforeCall = DEFAULT_INIT_FUNCTION;
    protected String name;
    protected String description;
    protected String outputKey;
    protected Function<AgenticScope, Object> output;
    protected AgentListener agentListener;
    protected final List<AgentExecutor> subagents = new ArrayList<AgentExecutor>();
    protected Function<ErrorContext, ErrorRecoveryResult> errorHandler;
    protected Function<InternalAgent, Object> agentInstanceFactory;
    protected Executor executor;

    protected AbstractServiceBuilder(Class<T> agentServiceClass, Method agenticMethod) {
        this.agentServiceClass = agentServiceClass;
        this.agenticMethod = agenticMethod;
        this.initService(agenticMethod);
    }

    private void initService(Method agenticMethod) {
        if (agenticMethod == null) {
            this.name = this.serviceType();
            return;
        }
        Agent agent = agenticMethod.getAnnotation(Agent.class);
        if (agent == null) {
            return;
        }
        this.name = !Utils.isNullOrBlank((String)agent.name()) ? agent.name() : agenticMethod.getName();
        if (!Utils.isNullOrBlank((String)agent.description())) {
            this.description = agent.description();
        } else if (!Utils.isNullOrBlank((String)agent.value())) {
            this.description = agent.value();
        }
        this.outputKey = AgentUtil.outputKey(agent.outputKey(), agent.typedOutputKey());
    }

    Type agentReturnType() {
        return this.agenticMethod == null ? Object.class : this.agenticMethod.getGenericReturnType();
    }

    public S beforeCall(Consumer<AgenticScope> beforeCall) {
        this.beforeCall = beforeCall;
        return (S)this;
    }

    public S name(String name) {
        this.name = name;
        return (S)this;
    }

    public S description(String description) {
        this.description = description;
        return (S)this;
    }

    public S outputKey(String outputKey) {
        this.outputKey = outputKey;
        return (S)this;
    }

    public S outputKey(Class<? extends TypedKey<?>> outputKey) {
        return this.outputKey(AgentUtil.keyName(outputKey));
    }

    public S output(Function<AgenticScope, Object> output) {
        this.output = output;
        return (S)this;
    }

    public S subAgents(Object ... agents) {
        return this.subAgents(Arrays.asList(agents));
    }

    public S subAgents(Collection<?> agents) {
        this.addSubagents(AgentUtil.agentsToExecutors(agents));
        return (S)this;
    }

    public S errorHandler(Function<ErrorContext, ErrorRecoveryResult> errorHandler) {
        this.errorHandler = errorHandler;
        return (S)this;
    }

    public S listener(AgentListener agentListener) {
        if (this.agentListener == null) {
            this.agentListener = agentListener;
        } else if (this.agentListener instanceof ComposedAgentListener) {
            ComposedAgentListener composed = (ComposedAgentListener)this.agentListener;
            composed.addListener(agentListener);
        } else {
            this.agentListener = new ComposedAgentListener(this.agentListener, agentListener);
        }
        return (S)this;
    }

    public S agentInstanceFactory(Function<InternalAgent, Object> factory) {
        this.agentInstanceFactory = factory;
        return (S)this;
    }

    public S executor(Executor executor) {
        this.executor = executor;
        return (S)this;
    }

    private void addSubagents(List<AgentExecutor> agents) {
        this.subagents.addAll(agents);
    }

    public T build(Supplier<Planner> plannerSupplier) {
        AgentMonitor monitor = ComposedAgentListener.listenerOfType(this.agentListener, AgentMonitor.class);
        if (MonitoredAgent.class.isAssignableFrom(this.agentServiceClass) && monitor == null) {
            monitor = new AgentMonitor();
            this.listener(monitor);
        }
        AgentInstance agent = (AgentInstance)this.build(new PlannerBasedInvocationHandler(this, plannerSupplier));
        if (monitor != null) {
            monitor.setRootAgent(agent);
        }
        return (T)agent;
    }

    public T build(InvocationHandler invocationHandler) {
        if (this.agentInstanceFactory != null) {
            return (T)this.agentInstanceFactory.apply((InternalAgent)((Object)invocationHandler));
        }
        return AgentUtil.buildAgent(this.agentServiceClass, invocationHandler);
    }

    public abstract String serviceType();
}

