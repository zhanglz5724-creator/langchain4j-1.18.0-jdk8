/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.scope;

import dev.langchain4j.agentic.internal.DelayedResponse;
import java.util.Map;
import java.util.Objects;

public class AgentInvocation {
    private final Class<?> agentType;
    private final String agentName;
    private final String agentId;
    private final Map<String, Object> input;
    private final Object output;

    public AgentInvocation(Class<?> agentType, String agentName, String agentId, Map<String, Object> input, Object output) {
        this.agentType = agentType;
        this.agentName = agentName;
        this.agentId = agentId;
        this.input = input;
        this.output = output;
    }

    public Class<?> agentType() {
        return this.agentType;
    }

    public String agentName() {
        return this.agentName;
    }

    public String agentId() {
        return this.agentId;
    }

    public Map<String, Object> input() {
        return this.input;
    }

    public Object output() {
        if (this.output instanceof DelayedResponse) {
            return ((DelayedResponse)this.output).result();
        }
        return this.output;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentInvocation)) {
            return false;
        }
        AgentInvocation other = (AgentInvocation)o;
        if (!Objects.equals(this.agentType, other.agentType)) {
            return false;
        }
        if (!Objects.equals(this.agentName, other.agentName)) {
            return false;
        }
        if (!Objects.equals(this.agentId, other.agentId)) {
            return false;
        }
        if (!Objects.equals(this.input, other.input)) {
            return false;
        }
        return Objects.equals(this.output, other.output);
    }

    public int hashCode() {
        return Objects.hash(this.agentType, this.agentName, this.agentId, this.input, this.output);
    }

    public String toString() {
        return "AgentInvocation{agentName=" + this.agentName + ", input=" + this.input + ", output=" + this.output + '}';
    }
}

