/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow.impl;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.workflow.ConditionalAgent;
import dev.langchain4j.agentic.workflow.ConditionalAgentInstance;
import dev.langchain4j.agentic.workflow.impl.AbstractAgentInstance;
import dev.langchain4j.agentic.workflow.impl.ConditionalPlanner;
import java.util.List;

public class DefaultConditionalAgentInstance
extends AbstractAgentInstance
implements ConditionalAgentInstance {
    private final ConditionalPlanner planner;

    public DefaultConditionalAgentInstance(AgentInstance delegate, ConditionalPlanner planner) {
        super(delegate);
        this.planner = planner;
    }

    @Override
    public List<ConditionalAgent> conditionalSubagents() {
        return this.planner.conditionalSubagents();
    }
}

