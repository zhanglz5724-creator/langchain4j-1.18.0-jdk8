/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.invocation.LangChain4jManaged
 *  dev.langchain4j.service.ParameterNameResolver
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.agent.AgentInvocationException;
import dev.langchain4j.agentic.agent.ChatMessagesAccess;
import dev.langchain4j.agentic.agent.MissingArgumentException;
import dev.langchain4j.agentic.internal.AgentInvocationArguments;
import dev.langchain4j.agentic.internal.AgentSpecsProvider;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.internal.MethodAgentInvoker;
import dev.langchain4j.agentic.internal.NonAiAgentInstance;
import dev.langchain4j.agentic.internal.SpecAgentInvoker;
import dev.langchain4j.agentic.internal.UntypedAgentInvoker;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.ListenerNotifierUtil;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.scope.AgenticSystemSuspendedException;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import dev.langchain4j.invocation.LangChain4jManaged;
import dev.langchain4j.service.ParameterNameResolver;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface AgentInvoker
extends AgentInstance,
InternalAgent {
    public Method method();

    public AgentInvocationArguments toInvocationArguments(AgenticScope var1) throws MissingArgumentException;

    default public Object invoke(DefaultAgenticScope agenticScope, Object agent, AgentInvocationArguments args) throws AgentInvocationException {
        Object result;
        AgentListener listener = this.listener();
        ListenerNotifierUtil.beforeAgentInvocation(listener, agenticScope, this, args.namedArgs());
        HashMap<Class<AgenticScope>, DefaultAgenticScope> m = new HashMap<Class<AgenticScope>, DefaultAgenticScope>();
        m.put(AgenticScope.class, agenticScope);
        LangChain4jManaged.setCurrent(m);
        try {
            result = this.method().invoke(agent, args.positionalArgs());
        }
        catch (Exception e) {
            Throwable cause;
            Throwable throwable = cause = e instanceof InvocationTargetException ? e.getCause() : e;
            if (cause instanceof AgenticSystemSuspendedException) {
                throw (AgenticSystemSuspendedException)cause;
            }
            AgentInvocationException invocationException = new AgentInvocationException("Failed to invoke agent method: " + this.method(), e);
            ListenerNotifierUtil.agentError(listener, agenticScope, this, args.namedArgs(), (Throwable)((Object)invocationException));
            throw invocationException;
        }
        finally {
            LangChain4jManaged.removeCurrent();
        }
        if (agent instanceof ChatMessagesAccess) {
            ChatMessagesAccess chatMessagesAccess = (ChatMessagesAccess)agent;
            ListenerNotifierUtil.afterAgentInvocation(listener, agenticScope, this, args.namedArgs(), result, chatMessagesAccess.lastChatRequest(agenticScope.memoryId()), chatMessagesAccess.lastChatResponse(agenticScope.memoryId()));
        } else {
            ListenerNotifierUtil.afterAgentInvocation(listener, agenticScope, this, args.namedArgs(), result);
        }
        return result;
    }

    public static AgentInvoker fromSpec(AgentSpecsProvider spec, Method agenticMethod, String name) {
        List<AgentArgument> arguments = spec.arguments() != null ? spec.arguments() : Arrays.asList(new AgentArgument((Type)((Object)AgenticScope.class), "@AgenticScope"));
        NonAiAgentInstance agentInstance = new NonAiAgentInstance(agenticMethod.getDeclaringClass(), name, spec.description(), agenticMethod.getGenericReturnType(), spec.outputKey(), spec.async(), arguments, spec.listener());
        return new SpecAgentInvoker(agenticMethod, agentInstance);
    }

    public static AgentInvoker fromMethod(InternalAgent agent, Method method) {
        if (method.getDeclaringClass() == UntypedAgent.class) {
            return new UntypedAgentInvoker(method, agent);
        }
        return new MethodAgentInvoker(method, agent);
    }

    public static String parameterName(Parameter parameter) {
        return AgentInvoker.optionalParameterName(parameter).orElseThrow(() -> new IllegalArgumentException("Parameter name not specified and no @V or @K annotation present: " + parameter));
    }

    public static Optional<String> optionalParameterName(Parameter parameter) {
        return Optional.ofNullable(ParameterNameResolver.name((Parameter)parameter));
    }
}

