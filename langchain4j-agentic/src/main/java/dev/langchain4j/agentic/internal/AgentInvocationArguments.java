/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class AgentInvocationArguments {
    private final Map<String, Object> namedArgs;
    private final Object[] positionalArgs;

    public AgentInvocationArguments(Map<String, Object> namedArgs, Object[] positionalArgs) {
        this.namedArgs = namedArgs;
        this.positionalArgs = positionalArgs;
    }

    public Map<String, Object> namedArgs() {
        return this.namedArgs;
    }

    public Object[] positionalArgs() {
        return this.positionalArgs;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentInvocationArguments)) {
            return false;
        }
        AgentInvocationArguments other = (AgentInvocationArguments)o;
        if (!Objects.equals(this.namedArgs, other.namedArgs)) {
            return false;
        }
        return Objects.equals(this.positionalArgs, other.positionalArgs);
    }

    public int hashCode() {
        return Objects.hash(this.namedArgs, this.positionalArgs);
    }

    public String toString() {
        return "AgentInvocationArguments{namedArgs=" + this.namedArgs + ", positionalArgs=" + Arrays.toString(this.positionalArgs) + "}";
    }
}

