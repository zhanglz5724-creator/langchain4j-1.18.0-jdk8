package dev.langchain4j.agentic.agent;

import dev.langchain4j.agentic.scope.AgenticScope;
public class ErrorContext {
    private final String agentName;
    private final AgenticScope agenticScope;
    private final AgentInvocationException exception;

    public ErrorContext(String agentName, AgenticScope agenticScope, AgentInvocationException exception) {
        this.agentName = agentName;
        this.agenticScope = agenticScope;
        this.exception = exception;
    }

    public String getAgentName() {
        return agentName;
    }

    public AgenticScope getAgenticScope() {
        return agenticScope;
    }

    public AgentInvocationException getException() {
        return exception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorContext that = (ErrorContext) o;
        return java.util.Objects.equals(this.agentName, that.agentName) && java.util.Objects.equals(this.agenticScope, that.agenticScope) && java.util.Objects.equals(this.exception, that.exception);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(agentName, agenticScope, exception);
    }

    @Override
    public String toString() {
        return "ErrorContext{"agentName=" + agentName + , "agenticScope=" + agenticScope + , "exception=" + exception + "}"";
    }



}
