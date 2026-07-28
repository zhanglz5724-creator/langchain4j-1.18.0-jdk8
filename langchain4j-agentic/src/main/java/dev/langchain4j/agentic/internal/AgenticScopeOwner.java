/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.agentic.scope.AgenticScopeRegistry;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;

@Internal
public interface AgenticScopeOwner {
    public AgenticScopeOwner withAgenticScope(DefaultAgenticScope var1);

    public AgenticScopeRegistry registry();
}

