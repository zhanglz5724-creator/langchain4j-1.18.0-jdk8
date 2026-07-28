/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow;

import dev.langchain4j.agentic.planner.AgentInstance;

public interface LoopAgentInstance
extends AgentInstance {
    public int maxIterations();

    public boolean testExitAtLoopEnd();

    public String exitCondition();
}

