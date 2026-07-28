/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow.impl;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.workflow.LoopAgentInstance;
import dev.langchain4j.agentic.workflow.impl.AbstractAgentInstance;
import dev.langchain4j.agentic.workflow.impl.LoopPlanner;

public class DefaultLoopAgentInstance
extends AbstractAgentInstance
implements LoopAgentInstance {
    private final LoopPlanner planner;

    public DefaultLoopAgentInstance(AgentInstance delegate, LoopPlanner planner) {
        super(delegate);
        this.planner = planner;
    }

    @Override
    public int maxIterations() {
        return this.planner.maxIterations();
    }

    @Override
    public boolean testExitAtLoopEnd() {
        return this.planner.testExitAtLoopEnd();
    }

    @Override
    public String exitCondition() {
        return this.planner.exitCondition();
    }
}

