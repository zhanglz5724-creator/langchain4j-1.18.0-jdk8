/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.declarative;

public interface TypedKey<T> {
    default public T defaultValue() {
        return null;
    }

    default public String name() {
        return this.getClass().getSimpleName();
    }
}

