/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.scope;

import dev.langchain4j.agentic.scope.AgenticScope;

public interface AgenticScopeAccess {
    public AgenticScope getAgenticScope(Object var1);

    public boolean evictAgenticScope(Object var1);
}

