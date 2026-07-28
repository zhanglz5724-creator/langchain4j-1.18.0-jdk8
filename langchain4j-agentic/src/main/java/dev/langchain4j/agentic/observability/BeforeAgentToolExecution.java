/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.service.tool.BeforeToolExecution
 */
package dev.langchain4j.agentic.observability;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.service.tool.BeforeToolExecution;
import java.util.Objects;

public class BeforeAgentToolExecution {
    private final AgentInstance agentInstance;
    private final BeforeToolExecution toolExecution;

    public BeforeAgentToolExecution(AgentInstance agentInstance, BeforeToolExecution toolExecution) {
        this.agentInstance = agentInstance;
        this.toolExecution = toolExecution;
    }

    public AgentInstance agentInstance() {
        return this.agentInstance;
    }

    public BeforeToolExecution toolExecution() {
        return this.toolExecution;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeforeAgentToolExecution)) {
            return false;
        }
        BeforeAgentToolExecution other = (BeforeAgentToolExecution)o;
        if (!Objects.equals(this.agentInstance, other.agentInstance)) {
            return false;
        }
        return Objects.equals(this.toolExecution, other.toolExecution);
    }

    public int hashCode() {
        return Objects.hash(this.agentInstance, this.toolExecution);
    }

    public String toString() {
        return "BeforeAgentToolExecution{agentInstance=" + this.agentInstance + ", toolExecution=" + this.toolExecution + "}";
    }

    public AgenticScope agenticScope() {
        return (AgenticScope)this.toolExecution.invocationContext().managedParameters().get(AgenticScope.class);
    }
}

