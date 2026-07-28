/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.planner.AgentInstance;

public interface InternalAgent
extends AgentInstance {
    public void setParent(InternalAgent var1);

    public void registerInheritedParentListener(AgentListener var1);

    public void appendId(String var1);

    default public void setAgentId(String agentId) {
    }

    public AgentListener listener();

    default public boolean allowStreamingOutput() {
        throw new UnsupportedOperationException();
    }

    default public boolean allowChatMemory() {
        return true;
    }
}

