/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.agent.MissingArgumentException;
import dev.langchain4j.agentic.internal.AbstractAgentInvoker;
import dev.langchain4j.agentic.internal.AgentInvocationArguments;
import dev.langchain4j.agentic.internal.AgentInvoker;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Collections;

public class MapperAgentInvoker
extends AbstractAgentInvoker {
    private final Object item;
    private final String injectionKey;
    private final String instanceName;
    private final String instanceAgentId;
    private final String instanceOutputKey;

    public MapperAgentInvoker(AgentInvoker delegate, Object item, int instanceIndex) {
        super(delegate.method(), delegate);
        this.item = item;
        this.injectionKey = delegate.arguments().isEmpty() ? null : delegate.arguments().get(0).name();
        this.instanceName = delegate.name() + "_" + instanceIndex;
        this.instanceAgentId = delegate.agentId() + "_" + instanceIndex;
        this.instanceOutputKey = delegate.outputKey() != null && !delegate.outputKey().trim().isEmpty() ? delegate.outputKey() + "_" + instanceIndex : null;
    }

    @Override
    public String name() {
        return this.instanceName;
    }

    @Override
    public String agentId() {
        return this.instanceAgentId;
    }

    @Override
    public String outputKey() {
        return this.instanceOutputKey;
    }

    @Override
    public AgentInvocationArguments toInvocationArguments(AgenticScope agenticScope) throws MissingArgumentException {
        if (this.injectionKey == null) {
            return AgentUtil.agentInvocationArguments(agenticScope, this.arguments());
        }
        return AgentUtil.agentInvocationArguments(agenticScope, this.arguments(), Collections.singletonMap(this.injectionKey, this.item));
    }
}

