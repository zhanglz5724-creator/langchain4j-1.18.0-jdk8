package dev.langchain4j.agentic.scope;

import dev.langchain4j.agentic.internal.DelayedResponse;

import java.util.Map;
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

    public Class<?> getAgentType() {
        return agentType;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAgentId() {
        return agentId;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public Object getOutput() {
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentInvocation that = (AgentInvocation) o;
        return java.util.Objects.equals(this.agentType, that.agentType) && java.util.Objects.equals(this.agentName, that.agentName) && java.util.Objects.equals(this.agentId, that.agentId) && java.util.Objects.equals(this.input, that.input) && java.util.Objects.equals(this.output, that.output);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(agentType, agentName, agentId, input, output);
    }

    @Override
    public String toString() {
        return "AgentInvocation{"agentType=" + agentType + , "agentName=" + agentName + , "agentId=" + agentId + , "input=" + input + , "output=" + output + "}"";
    }


    @Override
    public Object output() {
        return output instanceof DelayedResponse<?> delayedResponse ? delayedResponse.result() : output;
    }

    @Override
    public String toString() {
        return "AgentInvocation{" +
                "agentName=" + agentName +
                ", input=" + input +
                ", output=" + output +
                '}';
    }
}
