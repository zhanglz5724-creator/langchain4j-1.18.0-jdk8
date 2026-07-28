/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.invocation.LangChain4jManaged
 */
package dev.langchain4j.agentic.scope;

import dev.langchain4j.agentic.declarative.TypedKey;
import dev.langchain4j.agentic.scope.AgentInvocation;
import dev.langchain4j.invocation.LangChain4jManaged;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AgenticScope
extends LangChain4jManaged {
    public Object memoryId();

    public void writeState(String var1, Object var2);

    public <T> void writeState(Class<? extends TypedKey<T>> var1, T var2);

    public void writeStateIfAbsent(String var1, Object var2);

    public <T> void writeStateIfAbsent(Class<? extends TypedKey<T>> var1, T var2);

    public void writeStates(Map<String, Object> var1);

    public boolean hasState(String var1);

    public boolean hasState(Class<? extends TypedKey<?>> var1);

    public Object readState(String var1);

    public <T> T readState(String var1, T var2);

    public <T> T readState(Class<? extends TypedKey<T>> var1);

    public Map<String, Object> state();

    public String contextAsConversation(String ... var1);

    public String contextAsConversation(Object ... var1);

    public List<AgentInvocation> agentInvocations();

    public List<AgentInvocation> agentInvocations(String var1);

    public List<AgentInvocation> agentInvocations(Class<?> var1);

    default public boolean completePendingResponse(String responseId, Object value) {
        return false;
    }

    default public boolean completePendingResponse(Object value) {
        Set<String> ids = this.pendingResponseIds();
        if (ids.size() != 1) {
            throw new IllegalStateException("Expected exactly 1 pending response, but found " + ids.size() + ": " + ids);
        }
        return this.completePendingResponse(ids.iterator().next(), value);
    }

    default public Set<String> pendingResponseIds() {
        return Collections.emptySet();
    }

    public void writeExecutionContext(String var1, Object var2);

    default public void writeExecutionContext(Class<?> type, Object context) {
        this.writeExecutionContext(type.getName(), context);
    }

    public Object executionContext(String var1);

    public <T> T executionContextAs(String var1, Class<T> var2);

    default public <T> T executionContextAs(Class<T> type) {
        return this.executionContextAs(type.getName(), type);
    }
}

