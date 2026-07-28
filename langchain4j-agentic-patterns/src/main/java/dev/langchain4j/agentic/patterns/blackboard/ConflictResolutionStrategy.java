/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.planner.AgentInstance
 *  dev.langchain4j.agentic.scope.AgenticScope
 */
package dev.langchain4j.agentic.patterns.blackboard;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.List;
import java.util.function.Predicate;

@FunctionalInterface
public interface ConflictResolutionStrategy {
    public static final ConflictResolutionStrategy DECLARATION_ORDER = (scope, candidates) -> (AgentInstance)candidates.get(0);

    public AgentInstance resolve(AgenticScope var1, List<AgentInstance> var2);

    public static ConflictResolutionStrategy declarationOrder() {
        return DECLARATION_ORDER;
    }

    public static ConflictResolutionStrategy agentOfType(Class<?> agentType, Predicate<AgenticScope> condition) {
        return ConflictResolutionStrategy.selectAgent(a -> a.type() == agentType, condition);
    }

    public static ConflictResolutionStrategy agentOfType(Class<?> agentType) {
        return ConflictResolutionStrategy.selectAgent(a -> a.type() == agentType);
    }

    public static ConflictResolutionStrategy agentWithName(String agentName, Predicate<AgenticScope> condition) {
        return ConflictResolutionStrategy.selectAgent(a -> agentName.equals(a.name()), condition);
    }

    public static ConflictResolutionStrategy agentWithName(String agentName) {
        return ConflictResolutionStrategy.selectAgent(a -> agentName.equals(a.name()));
    }

    public static ConflictResolutionStrategy selectAgent(Predicate<AgentInstance> agentFilter, Predicate<AgenticScope> condition) {
        return (scope, candidates) -> {
            if (condition.test(scope)) {
                return ConflictResolutionStrategy.selectAgent(agentFilter).resolve(scope, candidates);
            }
            return null;
        };
    }

    public static ConflictResolutionStrategy selectAgent(Predicate<AgentInstance> agentFilter) {
        return (scope, candidates) -> candidates.stream().filter(agentFilter).findFirst().orElse(null);
    }

    default public ConflictResolutionStrategy or(ConflictResolutionStrategy other) {
        return (scope, candidates) -> {
            AgentInstance result = this.resolve(scope, candidates);
            if (result != null) {
                return result;
            }
            return other.resolve(scope, candidates);
        };
    }
}

