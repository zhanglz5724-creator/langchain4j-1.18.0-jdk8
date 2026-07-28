/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.agent;

import dev.langchain4j.agentic.agent.AgentInvocationException;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Objects;

public class ErrorContext {
    private final String agentName;
    private final AgenticScope agenticScope;
    private final AgentInvocationException exception;

    public ErrorContext(String agentName, AgenticScope agenticScope, AgentInvocationException exception) {
        this.agentName = agentName;
        this.agenticScope = agenticScope;
        this.exception = exception;
    }

    public String agentName() {
        return this.agentName;
    }

    public AgenticScope agenticScope() {
        return this.agenticScope;
    }

    public AgentInvocationException exception() {
        return this.exception;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ErrorContext)) {
            return false;
        }
        ErrorContext other = (ErrorContext)o;
        if (!Objects.equals(this.agentName, other.agentName)) {
            return false;
        }
        if (!Objects.equals(this.agenticScope, other.agenticScope)) {
            return false;
        }
        return Objects.equals((Object)this.exception, (Object)other.exception);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.agentName, this.agenticScope, this.exception});
    }

    public String toString() {
        return "ErrorContext{agentName=" + this.agentName + ", agenticScope=" + this.agenticScope + ", exception=" + (Object)((Object)this.exception) + "}";
    }
}

