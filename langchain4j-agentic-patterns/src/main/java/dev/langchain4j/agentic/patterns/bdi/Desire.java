package dev.langchain4j.agentic.patterns.bdi;

import dev.langchain4j.agentic.scope.AgenticScope;

import java.util.List;
import java.util.function.Predicate;
import java.util.Collections;

/**
 * A prioritized goal for the {@link BDIPlanner}. Each desire declares when it is achievable, when
 * it is satisfied, and the ordered sequence of agent types that form its intention. Higher priority
 * values take precedence; among equal priorities, declaration order wins (stable ordering).
 *
 * @param name       human-readable label, used in log messages and error diagnostics
 * @param priority   higher value = more important; strictly higher priority triggers preemption
 * @param achievable predicate on {@link dev.langchain4j.agentic.scope.AgenticScope} — can this desire be pursued now?
 * @param satisfied  predicate on {@link dev.langchain4j.agentic.scope.AgenticScope} — has this desire been achieved?
 * @param agentTypes ordered agent classes forming the intention; resolved to instances at init time
 */
public class Desire {
    private final String name;
    private final int priority;
    private final Predicate<AgenticScope> achievable;
    private final Predicate<AgenticScope> satisfied;
    private final List<Class<?>> agentTypes;

    public Desire(String name, int priority, Predicate<AgenticScope> achievable, Predicate<AgenticScope> satisfied, List<Class<?>> agentTypes) {
        this.name = name;
        this.priority = priority;
        this.achievable = achievable;
        this.satisfied = satisfied;
        this.agentTypes = agentTypes;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public Predicate<AgenticScope> getAchievable() {
        return achievable;
    }

    public Predicate<AgenticScope> getSatisfied() {
        return satisfied;
    }

    public List<Class<?>> getAgentTypes() {
        return agentTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Desire that = (Desire) o;
        return java.util.Objects.equals(this.name, that.name) && java.util.Objects.equals(this.priority, that.priority) && java.util.Objects.equals(this.achievable, that.achievable) && java.util.Objects.equals(this.satisfied, that.satisfied) && java.util.Objects.equals(this.agentTypes, that.agentTypes);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, priority, achievable, satisfied, agentTypes);
    }

    @Override
    public String toString() {
        return "Desire{"name=" + name + , "priority=" + priority + , "achievable=" + achievable + , "satisfied=" + satisfied + , "agentTypes=" + agentTypes + "}"";
    }


    public Desire {
        if (agentTypes == null || agentTypes.isEmpty()) {
            throw new IllegalArgumentException("Desire '" + name + "' must have at least one agent type");
        }
    }

    public static Desire of(String name, int priority,
                            Predicate<AgenticScope> achievable,
                            Predicate<AgenticScope> satisfied,
                            Class<?>... agentTypes) {
        return new Desire(name, priority, achievable, satisfied, Collections.singletonList(agentTypes));
    }

    public static Desire of(String name, int priority,
                            String achievableStateKey,
                            String satisfiedStateKey,
                            Class<?>... agentTypes) {
        return new Desire(name, priority,
                scope -> scope.hasState(achievableStateKey),
                scope -> scope.hasState(satisfiedStateKey),
                Collections.singletonList(agentTypes));
    }
}
