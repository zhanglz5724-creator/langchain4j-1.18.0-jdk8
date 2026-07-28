/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.scope;

import java.util.Objects;

public class AgenticScopeKey {
    private final String agentId;
    private final Object memoryId;

    public AgenticScopeKey(String agentId, Object memoryId) {
        this.agentId = agentId;
        this.memoryId = memoryId;
    }

    public String agentId() {
        return this.agentId;
    }

    public Object memoryId() {
        return this.memoryId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgenticScopeKey)) {
            return false;
        }
        AgenticScopeKey other = (AgenticScopeKey)o;
        if (!Objects.equals(this.agentId, other.agentId)) {
            return false;
        }
        return Objects.equals(this.memoryId, other.memoryId);
    }

    public int hashCode() {
        return Objects.hash(this.agentId, this.memoryId);
    }

    public String toString() {
        return "AgenticScopeKey{agentId=" + this.agentId + ", memoryId=" + this.memoryId + "}";
    }
}

