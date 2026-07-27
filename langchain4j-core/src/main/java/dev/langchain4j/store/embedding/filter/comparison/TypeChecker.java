/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.filter.comparison;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Exceptions;
import java.util.UUID;

@Internal
class TypeChecker {
    TypeChecker() {
    }

    static void ensureTypesAreCompatible(Object actualValue, Object comparisonValue, String key) {
        if (actualValue instanceof Number && comparisonValue instanceof Number) {
            return;
        }
        if (actualValue instanceof String && comparisonValue instanceof UUID) {
            return;
        }
        if (actualValue.getClass() != comparisonValue.getClass()) {
            throw Exceptions.illegalArgument("Type mismatch: actual value of metadata key \"%s\" (%s) has type %s, while comparison value (%s) has type %s", key, actualValue, actualValue.getClass().getName(), comparisonValue, comparisonValue.getClass().getName());
        }
    }
}

