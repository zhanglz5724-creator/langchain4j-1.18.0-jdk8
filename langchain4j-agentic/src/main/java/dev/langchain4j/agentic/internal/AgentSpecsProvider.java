/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.planner.AgentArgument;
import java.util.List;

public interface AgentSpecsProvider {
    public String outputKey();

    public String description();

    public boolean async();

    public AgentListener listener();

    default public List<AgentArgument> arguments() {
        return null;
    }
}

