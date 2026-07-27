package dev.langchain4j.agentic.scope;
public class AgenticScopeKey {
    private final String agentId;
    private final Object memoryId;

    public AgenticScopeKey(String agentId, Object memoryId) {
        this.agentId = agentId;
        this.memoryId = memoryId;
    }

    public String getAgentId() {
        return agentId;
    }

    public Object getMemoryId() {
        return memoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgenticScopeKey that = (AgenticScopeKey) o;
        return java.util.Objects.equals(this.agentId, that.agentId) && java.util.Objects.equals(this.memoryId, that.memoryId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(agentId, memoryId);
    }

    @Override
    public String toString() {
        return "AgenticScopeKey{"agentId=" + agentId + , "memoryId=" + memoryId + "}"";
    }

}
