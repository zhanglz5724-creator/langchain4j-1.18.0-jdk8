package dev.langchain4j.agentic.observability;

import java.util.Map;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
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

    public AgenticScope getAgenticScope() {
        return agenticScope;
    }

    public AgentInstance getAgent() {
        return agent;
    }

    public Map<String, Object> getInputs() {
        return inputs;
    }

    public Throwable getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentInvocationError that = (AgentInvocationError) o;
        return java.util.Objects.equals(this.agenticScope, that.agenticScope) && java.util.Objects.equals(this.agent, that.agent) && java.util.Objects.equals(this.inputs, that.inputs) && java.util.Objects.equals(this.error, that.error);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(agenticScope, agent, inputs, error);
    }

    @Override
    public String toString() {
        return "AgentInvocationError{"agenticScope=" + agenticScope + , "agent=" + agent + , "inputs=" + inputs + , "error=" + error + "}"";
    }


    public String agentName() {
        return agent.name();
    }

    public String agentId() {
        return agent.agentId();
    }
}
