/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ConditionalAgent {
    private final String condition;
    private final Predicate<AgenticScope> predicate;
    private final List<AgentInstance> agentInstances;

    public ConditionalAgent(String condition, Predicate<AgenticScope> predicate, List<AgentInstance> agentInstances) {
        this.condition = condition;
        this.predicate = predicate;
        this.agentInstances = agentInstances;
    }

    public String condition() {
        return this.condition;
    }

    public Predicate<AgenticScope> predicate() {
        return this.predicate;
    }

    public List<AgentInstance> agentInstances() {
        return this.agentInstances;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConditionalAgent)) {
            return false;
        }
        ConditionalAgent other = (ConditionalAgent)o;
        if (!Objects.equals(this.condition, other.condition)) {
            return false;
        }
        if (!Objects.equals(this.predicate, other.predicate)) {
            return false;
        }
        return Objects.equals(this.agentInstances, other.agentInstances);
    }

    public int hashCode() {
        return Objects.hash(this.condition, this.predicate, this.agentInstances);
    }

    public String toString() {
        return "ConditionalAgent{condition=" + this.condition + ", predicate=" + this.predicate + ", agentInstances=" + this.agentInstances + "}";
    }
}

