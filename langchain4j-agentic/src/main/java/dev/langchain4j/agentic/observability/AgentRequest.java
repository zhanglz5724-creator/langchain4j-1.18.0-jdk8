/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.observability;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Map;
import java.util.Objects;

public class AgentRequest {
    private final AgenticScope agenticScope;
    private final AgentInstance agent;
    private final Map<String, Object> inputs;

    public AgentRequest(AgenticScope agenticScope, AgentInstance agent, Map<String, Object> inputs) {
        this.agenticScope = agenticScope;
        this.agent = agent;
        this.inputs = inputs;
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

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentRequest)) {
            return false;
        }
        AgentRequest other = (AgentRequest)o;
        if (!Objects.equals(this.agenticScope, other.agenticScope)) {
            return false;
        }
        if (!Objects.equals(this.agent, other.agent)) {
            return false;
        }
        return Objects.equals(this.inputs, other.inputs);
    }

    public int hashCode() {
        return Objects.hash(this.agenticScope, this.agent, this.inputs);
    }

    public String toString() {
        return "AgentRequest{agenticScope=" + this.agenticScope + ", agent=" + this.agent + ", inputs=" + this.inputs + "}";
    }

    public String agentName() {
        return this.agent.name();
    }

    public String agentId() {
        return this.agent.agentId();
    }
}

