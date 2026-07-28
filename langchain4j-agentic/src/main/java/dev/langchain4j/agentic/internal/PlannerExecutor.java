/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.scope.AgentInvocation;

public interface PlannerExecutor {
    public void onSubagentInvoked(AgentInvocation var1);

    default public void onSubagentSuspended() {
    }

    public boolean propagateStreaming();
}

