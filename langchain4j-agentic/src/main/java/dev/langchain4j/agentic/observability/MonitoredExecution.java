/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.observability;

import dev.langchain4j.agentic.observability.AfterAgentToolExecution;
import dev.langchain4j.agentic.observability.AgentInvocation;
import dev.langchain4j.agentic.observability.AgentInvocationError;
import dev.langchain4j.agentic.observability.AgentRequest;
import dev.langchain4j.agentic.observability.AgentResponse;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonitoredExecution {
    private final AgentInvocation topLevelInvocations;
    private final Map<Object, AgentInvocation> ongoingInvocations = new ConcurrentHashMap<Object, AgentInvocation>();
    private AgentInvocationError agentInvocationError;

    MonitoredExecution(AgentRequest firstAgentRequest) {
        this.topLevelInvocations = new AgentInvocation(firstAgentRequest);
        this.ongoingInvocations.put(firstAgentRequest.agentId(), this.topLevelInvocations);
    }

    void beforeAgentInvocation(AgentRequest agentRequest) {
        AgentInvocation parentInvocation = this.ongoingInvocations.get(agentRequest.agent().parent().agentId());
        if (parentInvocation == null) {
            throw new IllegalStateException("No ongoing parent invocation found for agent ID: " + agentRequest.agent().parent().agentId());
        }
        AgentInvocation newInvocation = new AgentInvocation(agentRequest);
        if (parentInvocation.agent().topology() == AgenticSystemTopology.LOOP) {
            String agentId = agentRequest.agentId();
            int count = 0;
            for (AgentInvocation existing : parentInvocation.nestedInvocations()) {
                if (!agentId.equals(existing.agent().agentId())) continue;
                ++count;
            }
            newInvocation.setIterationIndex(count);
        }
        parentInvocation.addNestedInvocation(newInvocation);
        this.ongoingInvocations.put(agentRequest.agentId(), newInvocation);
    }

    void afterAgentInvocation(AgentResponse agentResponse) {
        AgentInvocation finishedInvocation = this.ongoingInvocations.remove(agentResponse.agentId());
        if (finishedInvocation == null) {
            throw new IllegalStateException("No ongoing invocation found for agent ID: " + agentResponse.agentId());
        }
        finishedInvocation.finished(agentResponse);
    }

    void onAgentInvocationError(AgentInvocationError agentInvocationError) {
        if (this.agentInvocationError == null) {
            this.agentInvocationError = agentInvocationError;
        }
        this.ongoingInvocations.remove(agentInvocationError.agentId());
    }

    void afterToolExecution(AfterAgentToolExecution afterToolExecution) {
        String agentId = afterToolExecution.agentInstance().agentId();
        AgentInvocation invocation = this.ongoingInvocations.get(agentId);
        if (invocation != null) {
            invocation.addToolExecution(afterToolExecution.toolExecution());
        }
    }

    public Collection<AgentInvocation> ongoingInvocations() {
        return this.ongoingInvocations.values();
    }

    public boolean done() {
        return this.topLevelInvocations.done();
    }

    public boolean hasError() {
        return this.agentInvocationError != null;
    }

    public AgentInvocationError error() {
        return this.agentInvocationError;
    }

    public AgentInvocation topLevelInvocations() {
        return this.topLevelInvocations;
    }

    public Object memoryId() {
        return this.agenticScope().memoryId();
    }

    public AgenticScope agenticScope() {
        return this.topLevelInvocations.agenticScope();
    }

    public String toString() {
        return this.topLevelInvocations.toString();
    }
}

