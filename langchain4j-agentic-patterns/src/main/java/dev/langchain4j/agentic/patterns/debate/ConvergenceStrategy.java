/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.patterns.debate;

import java.util.Collection;
import java.util.Objects;

@FunctionalInterface
public interface ConvergenceStrategy {
    public boolean hasConverged(Collection<Object> var1);

    public static ConvergenceStrategy unanimous() {
        return positions -> {
            if (positions.isEmpty()) {
                return true;
            }
            Object first = positions.iterator().next();
            return positions.stream().allMatch(p -> Objects.equals(first, p));
        };
    }

    public static ConvergenceStrategy unanimousLastWord() {
        return positions -> {
            String firstVerdict = null;
            for (Object p : positions) {
                String text = p.toString().trim();
                String[] tokens = text.split("\\s+");
                String lastWord = tokens[tokens.length - 1].replaceAll("^[^\\p{Alnum}]+|[^\\p{Alnum}]+$", "").toUpperCase();
                if (firstVerdict == null) {
                    firstVerdict = lastWord;
                    continue;
                }
                if (firstVerdict.equals(lastWord)) continue;
                return false;
            }
            return firstVerdict != null;
        };
    }
}

