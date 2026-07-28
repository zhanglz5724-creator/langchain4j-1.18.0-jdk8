/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.observability;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Map;
import java.util.Objects;

public class AgentInvocationError {
    private final AgenticScope agenticScope;
    private final AgentInstance agent;
    private final Map<String, Object> inputs;
    private final Throwable error;

    public AgentInvocationError(AgenticScope agenticScope, AgentInstance agent, Map<String, Object> inputs, Throwable error) {
        this.agenticScope = agenticScope;
        this.agent = agent;
        this.inputs = inputs;
        this.error = error;
    }

    public AgenticScope agenticScope() {
        return this.agenticScope;
    }

    public AgentInstance agent() {
        return this.agent;
    }

    public Map<String, Object> inputs() {
        return this.inputs;
    }

    public Throwable error() {
        return this.error;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentInvocationError)) {
            return false;
        }
        AgentInvocationError other = (AgentInvocationError)o;
        if (!Objects.equals(this.agenticScope, other.agenticScope)) {
            return false;
        }
        if (!Objects.equals(this.agent, other.agent)) {
            return false;
        }
        if (!Objects.equals(this.inputs, other.inputs)) {
            return false;
        }
        return Objects.equals(this.error, other.error);
    }

    public int hashCode() {
        return Objects.hash(this.agenticScope, this.agent, this.inputs, this.error);
    }

    public String toString() {
        return "AgentInvocationError{agenticScope=" + this.agenticScope + ", agent=" + this.agent + ", inputs=" + this.inputs + ", error=" + this.error + "}";
    }

    public String agentName() {
        return this.agent.name();
    }

    public String agentId() {
        return this.agent.agentId();
    }
}

