/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.filter;

import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.store.embedding.filter.logical.And;
import dev.langchain4j.store.embedding.filter.logical.Not;
import dev.langchain4j.store.embedding.filter.logical.Or;

@JacocoIgnoreCoverageGenerated
public interface Filter {
    public boolean test(Object var1);

    default public Filter and(Filter filter) {
        return Filter.and(this, filter);
    }

    public static Filter and(Filter left, Filter right) {
        return new And(left, right);
    }

    default public Filter or(Filter filter) {
        return Filter.or(this, filter);
    }

    public static Filter or(Filter left, Filter right) {
        return new Or(left, right);
    }

    public static Filter not(Filter expression) {
        return new Not(expression);
    }
}

