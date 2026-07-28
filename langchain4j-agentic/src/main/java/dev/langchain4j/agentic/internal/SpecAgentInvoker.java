/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.internal.AbstractAgentInvoker;
import dev.langchain4j.agentic.internal.AgentInvocationArguments;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.lang.reflect.Method;
import java.util.Collections;

final class SpecAgentInvoker
extends AbstractAgentInvoker {
    SpecAgentInvoker(Method method, InternalAgent agent) {
        super(method, agent);
    }

    @Override
    public AgentInvocationArguments toInvocationArguments(AgenticScope agenticScope) {
        return new AgentInvocationArguments(Collections.emptyMap(), new Object[]{agenticScope});
    }
}

