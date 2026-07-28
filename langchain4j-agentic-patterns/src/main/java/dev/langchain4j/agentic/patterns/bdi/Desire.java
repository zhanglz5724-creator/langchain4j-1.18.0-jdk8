/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.scope.AgenticScope
 */
package dev.langchain4j.agentic.patterns.bdi;

import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Desire {
    private final String name;
    private final int priority;
    private final Predicate<AgenticScope> achievable;
    private final Predicate<AgenticScope> satisfied;
    private final List<Class<?>> agentTypes;

    public Desire(String name, int priority, Predicate<AgenticScope> achievable, Predicate<AgenticScope> satisfied, List<Class<?>> agentTypes) {
        if (agentTypes == null || agentTypes.isEmpty()) {
            throw new IllegalArgumentException("Desire '" + name + "' must have at least one agent type");
        }
        this.name = name;
        this.priority = priority;
        this.achievable = achievable;
        this.satisfied = satisfied;
        this.agentTypes = agentTypes;
    }

    public String name() {
        return this.name;
    }

    public int priority() {
        return this.priority;
    }

    public Predicate<AgenticScope> achievable() {
        return this.achievable;
    }

    public Predicate<AgenticScope> satisfied() {
        return this.satisfied;
    }

    public List<Class<?>> agentTypes() {
        return this.agentTypes;
    }

    public static Desire of(String name, int priority, Predicate<AgenticScope> achievable, Predicate<AgenticScope> satisfied, Class<?> ... agentTypes) {
        return new Desire(name, priority, achievable, satisfied, Arrays.asList(agentTypes));
    }

    public static Desire of(String name, int priority, String achievableStateKey, String satisfiedStateKey, Class<?> ... agentTypes) {
        return new Desire(name, priority, scope -> scope.hasState(achievableStateKey), scope -> scope.hasState(satisfiedStateKey), Arrays.asList(agentTypes));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Desire)) {
            return false;
        }
        Desire other = (Desire)o;
        if (this.priority != other.priority) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.achievable, other.achievable)) {
            return false;
        }
        if (!Objects.equals(this.satisfied, other.satisfied)) {
            return false;
        }
        return Objects.equals(this.agentTypes, other.agentTypes);
    }

    public int hashCode() {
        return Objects.hash(this.name, this.priority, this.achievable, this.satisfied, this.agentTypes);
    }

    public String toString() {
        return "Desire{name=" + this.name + ", priority=" + this.priority + ", achievable=" + this.achievable + ", satisfied=" + this.satisfied + ", agentTypes=" + this.agentTypes + "}";
    }
}

