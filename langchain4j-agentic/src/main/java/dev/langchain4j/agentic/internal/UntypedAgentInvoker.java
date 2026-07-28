/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.agent.MissingArgumentException;
import dev.langchain4j.agentic.internal.AbstractAgentInvoker;
import dev.langchain4j.agentic.internal.AgentInvocationArguments;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class UntypedAgentInvoker
extends AbstractAgentInvoker {
    public UntypedAgentInvoker(Method method, InternalAgent agent) {
        super(method, agent);
    }

    @Override
    public AgentInvocationArguments toInvocationArguments(AgenticScope agenticScope) {
        for (AgentArgument arg : this.arguments()) {
            if (agenticScope.readState(arg.name()) != null) continue;
            throw new MissingArgumentException(arg.name());
        }
        HashMap<String, Object> args = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : agenticScope.state().entrySet()) {
            if (!DefaultAgenticScope.isSerializable(entry.getValue())) continue;
            args.put(entry.getKey(), entry.getValue());
        }
        return new AgentInvocationArguments(args, new Object[]{args});
    }

    @Override
    public String toString() {
        return "UntypedAgentInvoker[method=" + this.method + ", agent=" + this.agent + ']';
    }
}

