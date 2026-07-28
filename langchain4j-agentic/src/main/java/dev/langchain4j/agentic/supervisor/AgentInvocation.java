/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.supervisor;

import java.util.Map;

public class AgentInvocation {
    private String agentName;
    private Map<String, Object> arguments;

    public String getAgentName() {
        return this.agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Map<String, Object> getArguments() {
        return this.arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    public String toString() {
        return "AgentInvocation{agentName='" + this.agentName + '\'' + ", arguments=" + this.arguments + '}';
    }
}

