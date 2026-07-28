/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.ComposedAgentListener;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.workflow.HumanInTheLoop;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NonAiAgentInstance
implements AgentInstance,
InternalAgent {
    private final Class<?> type;
    private final String name;
    private final String description;
    private final Type outputType;
    private final String outputKey;
    private final boolean async;
    private final List<AgentArgument> arguments;
    private AgentListener listener;
    private InternalAgent parent;
    private String agentId;

    public NonAiAgentInstance(Class<?> type, String name, String description, Type outputType, String outputKey, boolean async, List<AgentArgument> arguments, AgentListener listener) {
        this.type = type;
        this.name = name;
        this.agentId = name;
        this.description = description;
        this.outputType = outputType;
        this.outputKey = outputKey;
        this.async = async;
        this.arguments = arguments;
        this.listener = listener;
    }

    @Override
    public String agentId() {
        return this.agentId;
    }

    @Override
    public AgentInstance parent() {
        return this.parent;
    }

    @Override
    public List<AgentInstance> subagents() {
        return Collections.emptyList();
    }

    @Override
    public AgenticSystemTopology topology() {
        return this.type == HumanInTheLoop.class ? AgenticSystemTopology.HUMAN_IN_THE_LOOP : AgenticSystemTopology.NON_AI_AGENT;
    }

    @Override
    public Class<?> type() {
        return this.type;
    }

    @Override
    public Class<? extends Planner> plannerType() {
        return null;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public Type outputType() {
        return this.outputType;
    }

    @Override
    public String outputKey() {
        return this.outputKey;
    }

    @Override
    public boolean async() {
        return this.async;
    }

    @Override
    public List<AgentArgument> arguments() {
        return this.arguments;
    }

    @Override
    public AgentListener listener() {
        return this.listener;
    }

    @Override
    public void registerInheritedParentListener(AgentListener parentListener) {
        if (parentListener != null && parentListener.inheritedBySubagents()) {
            this.listener = ComposedAgentListener.composeWithInherited(this.listener(), parentListener);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        NonAiAgentInstance that = (NonAiAgentInstance)obj;
        return Objects.equals(this.type, that.type) && Objects.equals(this.name, that.name) && Objects.equals(this.description, that.description) && Objects.equals(this.outputType, that.outputType) && Objects.equals(this.outputKey, that.outputKey) && this.async == that.async && Objects.equals(this.arguments, that.arguments) && Objects.equals(this.listener, that.listener);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.name, this.description, this.outputType, this.outputKey, this.async, this.arguments, this.listener);
    }

    public String toString() {
        return "NonAiAgentInstance[type=" + this.type + ", name=" + this.name + ", description=" + this.description + ", outputType=" + this.outputType + ", outputKey=" + this.outputKey + ", async=" + this.async + ", arguments=" + this.arguments + ", listener=" + this.listener + ']';
    }

    @Override
    public void setParent(InternalAgent parent) {
        this.parent = parent;
    }

    @Override
    public void appendId(String idSuffix) {
        this.agentId = this.agentId + idSuffix;
    }
}

