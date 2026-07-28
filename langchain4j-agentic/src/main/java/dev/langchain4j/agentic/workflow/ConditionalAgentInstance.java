/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.workflow.ConditionalAgent;
import java.util.List;

public interface ConditionalAgentInstance
extends AgentInstance {
    public List<ConditionalAgent> conditionalSubagents();
}

