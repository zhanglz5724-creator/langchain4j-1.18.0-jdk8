/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.agent.MissingArgumentException;
import dev.langchain4j.agentic.internal.AbstractAgentInvoker;
import dev.langchain4j.agentic.internal.AgentInvocationArguments;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.lang.reflect.Method;

public final class MethodAgentInvoker
extends AbstractAgentInvoker {
    public MethodAgentInvoker(Method method, InternalAgent agent) {
        super(method, agent);
    }

    @Override
    public AgentInvocationArguments toInvocationArguments(AgenticScope agenticScope) throws MissingArgumentException {
        return AgentUtil.agentInvocationArguments(agenticScope, this.arguments());
    }

    @Override
    public String toString() {
        return "MethodAgentInvoker[method=" + this.method + ", agent=" + this.agent + ']';
    }
}

