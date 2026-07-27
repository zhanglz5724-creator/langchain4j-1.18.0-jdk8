package dev.langchain4j.agentic.observability;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Map;
public class AgentRequest {
    private final AgenticScope agenticScope;
    private final AgentInstance agent;
    private final Map<String, Object> inputs;

    public AgentRequest(AgenticScope agenticScope, AgentInstance agent, Map<String, Object> inputs) {
        this.agenticScope = agenticScope;
        this.agent = agent;
        this.inputs = inputs;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentRequest that = (AgentRequest) o;
        return java.util.Objects.equals(this.agenticScope, that.agenticScope) && java.util.Objects.equals(this.agent, that.agent) && java.util.Objects.equals(this.inputs, that.inputs);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(agenticScope, agent, inputs);
    }

    @Override
    public String toString() {
        return "AgentRequest{"agenticScope=" + agenticScope + , "agent=" + agent + , "inputs=" + inputs + "}"";
    }


    public String agentName() {
        return agent.name();
    }

    public String agentId() {
        return agent.agentId();
    }
}
