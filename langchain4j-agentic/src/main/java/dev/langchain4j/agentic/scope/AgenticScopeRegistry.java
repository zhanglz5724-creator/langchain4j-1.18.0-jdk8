/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.agentic.scope;

import dev.langchain4j.Internal;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.ListenerNotifierUtil;
import dev.langchain4j.agentic.scope.AgenticScopeKey;
import dev.langchain4j.agentic.scope.AgenticScopePersister;
import dev.langchain4j.agentic.scope.AgenticScopeStore;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Internal
public class AgenticScopeRegistry {
    private final String agentId;
    private final AgenticScopeStore store;
    private final Map<AgenticScopeKey, DefaultAgenticScope> inMemoryAgenticScope = new ConcurrentHashMap<AgenticScopeKey, DefaultAgenticScope>();

    public AgenticScopeRegistry(String agentId) {
        this.agentId = agentId;
        this.store = AgenticScopePersister.store;
    }

    private boolean hasStore() {
        return this.store != null;
    }

    public void update(DefaultAgenticScope agenticScope) {
        if (this.hasStore()) {
            this.store.save(new AgenticScopeKey(this.agentId, agenticScope.memoryId()), agenticScope);
        }
    }

    public DefaultAgenticScope get(Object memoryId) {
        AgenticScopeKey key = new AgenticScopeKey(this.agentId, memoryId);
        DefaultAgenticScope agenticScope = this.inMemoryAgenticScope.get(key);
        if (agenticScope == null && this.hasStore()) {
            agenticScope = this.store.load(key).map(loaded -> {
                this.inMemoryAgenticScope.put(key, (DefaultAgenticScope)loaded);
                return loaded;
            }).orElse(null);
        }
        return agenticScope;
    }

    public DefaultAgenticScope create(Object memoryId) {
        DefaultAgenticScope agenticScope = new DefaultAgenticScope(memoryId, this.hasStore() ? DefaultAgenticScope.Kind.PERSISTENT : DefaultAgenticScope.Kind.REGISTERED);
        this.register(agenticScope);
        return agenticScope;
    }

    public DefaultAgenticScope createEphemeralAgenticScope() {
        DefaultAgenticScope agenticScope = DefaultAgenticScope.ephemeralAgenticScope();
        this.register(agenticScope);
        return agenticScope;
    }

    private void register(DefaultAgenticScope agenticScope) {
        this.inMemoryAgenticScope.put(new AgenticScopeKey(this.agentId, agenticScope.memoryId()), agenticScope);
        this.update(agenticScope);
    }

    public boolean evict(Object memoryId, AgentListener listener) {
        boolean removed;
        AgenticScopeKey key = new AgenticScopeKey(this.agentId, memoryId);
        DefaultAgenticScope agenticScope = this.inMemoryAgenticScope.remove(key);
        boolean bl = removed = agenticScope != null;
        if (removed) {
            ListenerNotifierUtil.beforeAgenticScopeDestroyed(listener, agenticScope);
        }
        if (this.hasStore()) {
            return this.store.delete(key) || removed;
        }
        return removed;
    }

    public Set<AgenticScopeKey> getAllAgenticScopeKeysInMemory() {
        return this.inMemoryAgenticScope.keySet();
    }

    public void clearInMemory() {
        this.inMemoryAgenticScope.clear();
    }
}

