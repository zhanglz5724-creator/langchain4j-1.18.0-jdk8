/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.patterns.voting;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@FunctionalInterface
public interface VotingStrategy {
    public Object aggregate(Collection<Object> var1);

    public static VotingStrategy majority() {
        return votes -> votes.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
    }

    public static VotingStrategy average() {
        return votes -> votes.stream().mapToDouble(v -> ((Number)v).doubleValue()).average().orElse(0.0);
    }

    public static VotingStrategy highest() {
        return votes -> votes.stream().map(v -> (Comparable)v).max(Comparator.naturalOrder()).orElse(null);
    }
}

