/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.scope;

import dev.langchain4j.agentic.scope.AgenticScopeStore;
import java.util.Iterator;
import java.util.ServiceLoader;

public enum AgenticScopePersister {
    INSTANCE;

    static AgenticScopeStore store;

    private AgenticScopePersister() {
        AgenticScopePersister.setStore(AgenticScopePersister.loadStore());
    }

    private static AgenticScopeStore loadStore() {
        ServiceLoader<AgenticScopeStore> loader = ServiceLoader.load(AgenticScopeStore.class);
        Iterator<AgenticScopeStore> iterator = loader.iterator();
        if (iterator.hasNext()) {
            AgenticScopeStore provider = iterator.next();
            return provider;
        }
        return null;
    }

    public static void setStore(AgenticScopeStore store) {
        AgenticScopePersister.store = store;
    }
}

