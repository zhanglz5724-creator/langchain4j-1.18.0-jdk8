/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.service.TokenStream
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.agent.AgentInvocationException;
import dev.langchain4j.agentic.agent.ErrorRecoveryResult;
import dev.langchain4j.agentic.agent.MissingArgumentException;
import dev.langchain4j.agentic.internal.AgentInvocationArguments;
import dev.langchain4j.agentic.internal.AgentInvoker;
import dev.langchain4j.agentic.internal.AgenticScopeOwner;
import dev.langchain4j.agentic.internal.AsyncResponse;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.internal.PlannerExecutor;
import dev.langchain4j.agentic.internal.StreamingResponse;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.scope.AgentInvocation;
import dev.langchain4j.agentic.scope.AgenticSystemSuspendedException;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import dev.langchain4j.service.TokenStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentExecutor
implements AgentInstance,
InternalAgent {
    private final AgentInvoker agentInvoker;
    private final Object agent;
    private static final Logger LOG = LoggerFactory.getLogger(AgentExecutor.class);

    public AgentExecutor(AgentInvoker agentInvoker, Object agent) {
        this.agentInvoker = agentInvoker;
        this.agent = agent;
    }

    public AgentInvoker agentInvoker() {
        return this.agentInvoker;
    }

    public Object agent() {
        return this.agent;
    }

    public Object execute(DefaultAgenticScope agenticScope, PlannerExecutor planner) {
        return this.execute(agenticScope, planner, this.agentInvoker.async());
    }

    public Object syncExecute(DefaultAgenticScope agenticScope, PlannerExecutor planner) {
        if (this.agentInvoker.async()) {
            LOG.info("Executing '{}' agent in a sync way even if declared as async", (Object)this.agentInvoker.name());
        }
        return this.execute(agenticScope, planner, false);
    }

    private Object execute(DefaultAgenticScope agenticScope, PlannerExecutor planner, boolean async) {
        Object invokedAgent = this.agent instanceof AgenticScopeOwner ? ((AgenticScopeOwner)this.agent).withAgenticScope(agenticScope) : this.agent;
        return this.internalExecute(agenticScope, invokedAgent, planner, async);
    }

    private Object handleAgentFailure(AgentInvocationException e, DefaultAgenticScope agenticScope, Object invokedAgent, PlannerExecutor planner, AgentInvocationArguments args, boolean plannerAlreadyNotified) {
        ErrorRecoveryResult recoveryResult = agenticScope.handleError(this.agentInvoker.name(), e);
        switch (recoveryResult.type()) {
            case THROW_EXCEPTION: {
                throw e;
            }
            case RETRY: {
                return this.internalExecute(agenticScope, invokedAgent, planner, false);
            }
            case RETURN_RESULT: {
                return plannerAlreadyNotified ? recoveryResult.result() : this.completeAgentInvocation(recoveryResult.result(), agenticScope, invokedAgent, planner, args);
            }
        }
        throw new IllegalStateException("Unexpected recovery type: " + (Object)((Object)recoveryResult.type()));
    }

    private Object internalExecute(DefaultAgenticScope agenticScope, Object invokedAgent, PlannerExecutor planner, boolean async) {
        AgentInvocationArguments args = null;
        try {
            try {
                args = this.agentInvoker.toInvocationArguments(agenticScope);
            }
            catch (MissingArgumentException e) {
                if (this.optional()) {
                    LOG.info("Skipping optional agent '{}' because of missing argument '{}'", (Object)this.agentInvoker.name(), (Object)e.argumentName());
                    Object response = agenticScope.readState(this.agentInvoker.outputKey());
                    if (planner != null) {
                        planner.onSubagentInvoked(new AgentInvocation(this.type(), this.name(), this.agentId(), Collections.emptyMap(), response));
                    }
                    return response;
                }
                throw e;
            }
            Object response = this.agentResponse(agenticScope, invokedAgent, planner, args, async);
            return this.completeAgentInvocation(response, agenticScope, invokedAgent, planner, args);
        }
        catch (AgenticSystemSuspendedException e) {
            if (planner != null) {
                planner.onSubagentSuspended();
            }
            return null;
        }
        catch (AgentInvocationException e) {
            return this.handleAgentFailure(e, agenticScope, invokedAgent, planner, args, false);
        }
    }

    private Object completeAgentInvocation(Object response, DefaultAgenticScope agenticScope, Object invokedAgent, PlannerExecutor planner, AgentInvocationArguments args) {
        String outputKey = this.agentInvoker.outputKey();
        if (outputKey != null && !outputKey.trim().isEmpty()) {
            agenticScope.writeState(outputKey, response);
        }
        Map<String, Object> namedArgs = args != null ? args.namedArgs() : Collections.emptyMap();
        AgentInvocation agentInvocation = new AgentInvocation(this.type(), this.name(), this.agentId(), namedArgs, DefaultAgenticScope.isSerializable(response) ? response : "<unknown>");
        agenticScope.registerAgentInvocation(agentInvocation, invokedAgent);
        if (planner != null) {
            planner.onSubagentInvoked(agentInvocation);
        }
        return response;
    }

    private Object agentResponse(DefaultAgenticScope agenticScope, Object invokedAgent, PlannerExecutor planner, AgentInvocationArguments args, boolean async) {
        if (async) {
            return new AsyncResponse<Object>(() -> {
                try {
                    return this.agentInvoker.invoke(agenticScope, invokedAgent, args);
                }
                catch (AgentInvocationException e) {
                    return this.handleAgentFailure(e, agenticScope, invokedAgent, planner, args, true);
                }
            });
        }
        Object response = this.agentInvoker.invoke(agenticScope, invokedAgent, args);
        if (planner != null && response instanceof TokenStream) {
            TokenStream tokenStream = (TokenStream)response;
            return planner.propagateStreaming() ? tokenStream : new StreamingResponse(tokenStream);
        }
        return response;
    }

    @Override
    public Class<?> type() {
        return this.agentInvoker.type();
    }

    @Override
    public Class<? extends Planner> plannerType() {
        return this.agentInvoker.plannerType();
    }

    @Override
    public String name() {
        return this.agentInvoker.name();
    }

    @Override
    public String agentId() {
        return this.agentInvoker.agentId();
    }

    @Override
    public String description() {
        return this.agentInvoker.description();
    }

    @Override
    public Type outputType() {
        return this.agentInvoker.outputType();
    }

    @Override
    public String outputKey() {
        return this.agentInvoker.outputKey();
    }

    @Override
    public List<AgentArgument> arguments() {
        return this.agentInvoker.arguments();
    }

    @Override
    public List<AgentInstance> subagents() {
        return this.agentInvoker.subagents();
    }

    @Override
    public boolean async() {
        return this.agentInvoker.async();
    }

    @Override
    public boolean optional() {
        return this.agentInvoker.optional();
    }

    @Override
    public AgenticSystemTopology topology() {
        return this.agentInvoker.topology();
    }

    @Override
    public AgentInstance parent() {
        return this.agentInvoker.parent();
    }

    @Override
    public void setParent(InternalAgent parent) {
        this.agentInvoker.setParent(parent);
    }

    @Override
    public void registerInheritedParentListener(AgentListener parentListener) {
        this.agentInvoker.registerInheritedParentListener(parentListener);
    }

    @Override
    public void appendId(String idSuffix) {
        this.agentInvoker.appendId(idSuffix);
    }

    @Override
    public AgentListener listener() {
        return this.agentInvoker.listener();
    }

    @Override
    public <T extends AgentInstance> T as(Class<T> agentInstanceClass) {
        return this.agentInvoker.as(agentInstanceClass);
    }

    void setParent(InternalAgent parent, int index) {
        this.setParent(parent);
        this.propagateParentIndex(this.agentInvoker, index);
    }

    private void propagateParentIndex(InternalAgent agent, int index) {
        agent.appendId("$" + index);
        for (AgentInstance subagent : agent.subagents()) {
            if (!(subagent instanceof InternalAgent)) continue;
            InternalAgent internalAgent = (InternalAgent)subagent;
            this.propagateParentIndex(internalAgent, index);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentExecutor)) {
            return false;
        }
        AgentExecutor other = (AgentExecutor)o;
        if (!Objects.equals(this.agentInvoker, other.agentInvoker)) {
            return false;
        }
        return Objects.equals(this.agent, other.agent);
    }

    public int hashCode() {
        return Objects.hash(this.agentInvoker, this.agent);
    }

    public String toString() {
        return "AgentExecutor{agentInvoker=" + this.agentInvoker + ", agent=" + this.agent + "}";
    }
}

