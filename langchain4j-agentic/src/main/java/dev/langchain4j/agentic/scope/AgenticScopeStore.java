/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.scope;

import dev.langchain4j.agentic.scope.AgenticScopeKey;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import java.util.Optional;
import java.util.Set;

public interface AgenticScopeStore {
    public boolean save(AgenticScopeKey var1, DefaultAgenticScope var2);

    public Optional<DefaultAgenticScope> load(AgenticScopeKey var1);

    public boolean delete(AgenticScopeKey var1);

    public Set<AgenticScopeKey> getAllKeys();
}

