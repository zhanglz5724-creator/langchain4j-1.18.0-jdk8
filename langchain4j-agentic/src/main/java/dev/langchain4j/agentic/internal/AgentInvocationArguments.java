package dev.langchain4j.agentic.internal;

import java.util.Map;
public class AgentInvocationArguments {
    private final Map<String, Object> namedArgs;
    private final Object[] positionalArgs;

    public AgentInvocationArguments(Map<String, Object> namedArgs, Object[] positionalArgs) {
        this.namedArgs = namedArgs;
        this.positionalArgs = positionalArgs;
    }

    public Map<String, Object> getNamedArgs() {
        return namedArgs;
    }

    public Object[] getPositionalArgs() {
        return positionalArgs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentInvocationArguments that = (AgentInvocationArguments) o;
        return java.util.Objects.equals(this.namedArgs, that.namedArgs) && java.util.Objects.equals(this.positionalArgs, that.positionalArgs);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(namedArgs, positionalArgs);
    }

    @Override
    public String toString() {
        return "AgentInvocationArguments{"namedArgs=" + namedArgs + , "positionalArgs=" + positionalArgs + "}"";
    }

}
