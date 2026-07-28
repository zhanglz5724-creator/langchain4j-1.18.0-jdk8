/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.invocation.LangChain4jManaged
 *  dev.langchain4j.memory.chat.ChatMemoryProvider
 *  dev.langchain4j.memory.chat.MessageWindowChatMemory
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.service.AiServices
 *  dev.langchain4j.service.memory.ChatMemoryAccess
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.agentic.supervisor;

import dev.langchain4j.agentic.internal.Context;
import dev.langchain4j.agentic.planner.Action;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.ChatMemoryAccessProvider;
import dev.langchain4j.agentic.planner.InitPlanningContext;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.planner.PlanningContext;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import dev.langchain4j.agentic.supervisor.AgentInvocation;
import dev.langchain4j.agentic.supervisor.PlannerAgent;
import dev.langchain4j.agentic.supervisor.ResponseAgent;
import dev.langchain4j.agentic.supervisor.ResponseScore;
import dev.langchain4j.agentic.supervisor.SupervisorContextStrategy;
import dev.langchain4j.agentic.supervisor.SupervisorResponseStrategy;
import dev.langchain4j.invocation.LangChain4jManaged;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.memory.ChatMemoryAccess;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SupervisorPlanner
implements Planner,
ChatMemoryAccessProvider {
    private static final Logger LOG = LoggerFactory.getLogger(SupervisorPlanner.class);
    public static final String SUPERVISOR_CONTEXT_KEY = "supervisorContext";
    public static final String SUPERVISOR_CONTEXT_PREFIX = "Use the following supervisor context to better understand constraints, policies or preferences when creating the plan ";
    private final ChatModel chatModel;
    private final ChatMemoryProvider chatMemoryProvider;
    private final int maxAgentsInvocations;
    private int loopCount = 0;
    private ResponseAgent responseAgent;
    private final SupervisorContextStrategy contextStrategy;
    private final SupervisorResponseStrategy responseStrategy;
    private final Function<AgenticScope, String> requestGenerator;
    private final String outputKey;
    private final Function<AgenticScope, Object> output;
    private Map<String, AgentInstance> agents;
    private String agentsList;
    private String request;

    public SupervisorPlanner(ChatModel chatModel, ChatMemoryProvider chatMemoryProvider, int maxAgentsInvocations, SupervisorContextStrategy contextStrategy, SupervisorResponseStrategy responseStrategy, Function<AgenticScope, String> requestGenerator, String outputKey, Function<AgenticScope, Object> output) {
        this.chatModel = chatModel;
        this.chatMemoryProvider = chatMemoryProvider;
        this.maxAgentsInvocations = maxAgentsInvocations;
        this.contextStrategy = contextStrategy;
        this.responseStrategy = responseStrategy;
        this.requestGenerator = requestGenerator;
        this.outputKey = outputKey;
        this.output = output;
    }

    @Override
    public void init(InitPlanningContext initPlanningContext) {
        this.agents = initPlanningContext.subagents().stream().collect(Collectors.toMap(AgentInstance::agentId, Function.identity()));
        this.agentsList = initPlanningContext.subagents().stream().map(SupervisorPlanner::toCard).collect(Collectors.joining(", "));
        String string = this.request = this.requestGenerator != null ? this.requestGenerator.apply(initPlanningContext.agenticScope()) : initPlanningContext.agenticScope().readState("request", "");
        if (this.responseStrategy == SupervisorResponseStrategy.SCORED) {
            this.responseAgent = (ResponseAgent)AiServices.builder(ResponseAgent.class).chatModel(this.chatModel).build();
        }
    }

    @Override
    public Action nextAction(PlanningContext planningContext) {
        String lastResponse;
        String string = lastResponse = planningContext.previousAgentInvocation() == null || planningContext.previousAgentInvocation().output() == null ? "" : planningContext.previousAgentInvocation().output().toString();
        if (this.loopCount++ >= this.maxAgentsInvocations) {
            return this.doneAction(planningContext.agenticScope(), lastResponse, null);
        }
        return this.nextSubagent(planningContext.agenticScope(), lastResponse);
    }

    private static String toCard(AgentInstance agent) {
        List agentArguments = agent.arguments().stream().filter(a -> !a.name().equals("@MemoryId")).map(SupervisorPlanner::argumentDescription).collect(Collectors.toList());
        return "{'" + agent.agentId() + "', '" + agent.description() + "', " + agentArguments + "}";
    }

    private static String argumentDescription(AgentArgument arg) {
        return SupervisorPlanner.argumentDescription(arg.rawType(), arg.name());
    }

    private static String argumentDescription(Class<?> type, String name) {
        if (name == null) {
            return "";
        }
        if (type.isPrimitive() || type.isEnum() || type == String.class || type == Boolean.class || Number.class.isAssignableFrom(type)) {
            return name + ": " + type.getSimpleName();
        }
        String fieldsDescription = Stream.of(type.getDeclaredFields()).map(f -> SupervisorPlanner.argumentDescription(f.getType(), f.getName())).collect(Collectors.joining(", "));
        return name + ": {" + fieldsDescription + "}";
    }

    private Action nextSubagent(AgenticScope agenticScope, String lastResponse) {
        String supervisorContext = agenticScope.hasState(SUPERVISOR_CONTEXT_KEY) ? "Use the following supervisor context to better understand constraints, policies or preferences when creating the plan '" + agenticScope.readState(SUPERVISOR_CONTEXT_KEY, "") + "'." : "";
        AgentInvocation agentInvocation = SupervisorPlanner.withAgenticScope(agenticScope, () -> this.planner(agenticScope).plan(agenticScope.memoryId(), this.agentsList, this.request, lastResponse, supervisorContext));
        LOG.info("Agent Invocation: {}", (Object)agentInvocation);
        if (agentInvocation.getAgentName().equalsIgnoreCase("done")) {
            return this.doneAction(agenticScope, lastResponse, agentInvocation);
        }
        AgentInstance agent = this.findAgentByName(agentInvocation.getAgentName());
        agentInvocation.getArguments().entrySet().stream().filter(entry -> this.writeArgumentToScope(agenticScope, agent, (String)entry.getKey(), entry.getValue())).forEach(entry -> agenticScope.writeState((String)entry.getKey(), entry.getValue()));
        return this.call(agent);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static <T> T withAgenticScope(AgenticScope agenticScope, Supplier<T> supplier) {
        HashMap<Class<AgenticScope>, AgenticScope> m = new HashMap<Class<AgenticScope>, AgenticScope>();
        m.put(AgenticScope.class, agenticScope);
        LangChain4jManaged.setCurrent(m);
        try {
            T t = supplier.get();
            return t;
        }
        finally {
            LangChain4jManaged.removeCurrent();
        }
    }

    private AgentInstance findAgentByName(String agentName) {
        List candidateAgents;
        AgentInstance agent = this.agents.get(agentName);
        if (agent == null && (candidateAgents = this.agents.values().stream().filter(a -> a.name().equals(agentName)).collect(Collectors.toList())).size() == 1) {
            agent = (AgentInstance)candidateAgents.get(0);
        }
        if (agent == null) {
            throw new IllegalStateException("No agent found with name: " + agentName);
        }
        return agent;
    }

    private boolean writeArgumentToScope(AgenticScope agenticScope, AgentInstance agent, String key, Object value) {
        Class argType;
        if (agenticScope.hasState(key) && (argType = (Class)agent.arguments().stream().filter(arg -> arg.name().equals(key)).findFirst().map(AgentArgument::rawType).orElse(null)) != null) {
            Object existingValue = agenticScope.readState(key);
            return !argType.isAssignableFrom(existingValue.getClass()) || argType.isAssignableFrom(value.getClass());
        }
        return true;
    }

    private Action doneAction(AgenticScope agenticScope, String lastResponse, AgentInvocation done) {
        Object result = this.result(agenticScope, this.request, lastResponse, done);
        if (this.outputKey != null) {
            agenticScope.writeState(this.outputKey, result);
        }
        return this.done(this.output != null ? this.output.apply(agenticScope) : result);
    }

    private PlannerAgent planner(AgenticScope agenticScope) {
        return ((DefaultAgenticScope)agenticScope).getOrCreateAgent(this.agentId(), this::buildPlannerAgent);
    }

    private Object result(AgenticScope agenticScope, String request, String lastResponse, AgentInvocation done) {
        if (done == null || done.getArguments() == null || done.getArguments().get("response") == null) {
            return lastResponse;
        }
        String doneResponse = done.getArguments().get("response").toString();
        switch (this.responseStrategy) {
            case LAST: {
                return lastResponse;
            }
            case SUMMARY: {
                return doneResponse;
            }
            case SCORED: {
                ResponseScore score = SupervisorPlanner.withAgenticScope(agenticScope, () -> this.responseAgent.scoreResponses(request, lastResponse, doneResponse));
                LOG.info("Response scores: {}", (Object)score);
                return score.getScore2() > score.getScore1() ? doneResponse : lastResponse;
            }
        }
        throw new IllegalStateException("Unexpected response strategy: " + (Object)((Object)this.responseStrategy));
    }

    private PlannerAgent buildPlannerAgent(AgenticScope agenticScope) {
        AiServices builder = AiServices.builder(PlannerAgent.class).chatModel(this.chatModel);
        this.configureMemoryAndContext(agenticScope, (AiServices<PlannerAgent>)builder);
        return (PlannerAgent)builder.build();
    }

    private void configureMemoryAndContext(AgenticScope agenticScope, AiServices<PlannerAgent> builder) {
        if (this.chatMemoryProvider != null) {
            builder.chatMemoryProvider(this.chatMemoryProvider);
            if (this.contextStrategy != SupervisorContextStrategy.CHAT_MEMORY) {
                builder.chatRequestTransformer((BiFunction)new Context.Summarizer(agenticScope, this.chatModel, new String[0]));
            }
        } else {
            switch (this.contextStrategy) {
                case CHAT_MEMORY: {
                    builder.chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages((int)20));
                    break;
                }
                case SUMMARIZATION: {
                    builder.chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages((int)2)).chatRequestTransformer((BiFunction)new Context.Summarizer(agenticScope, this.chatModel, new String[0]));
                    break;
                }
                case CHAT_MEMORY_AND_SUMMARIZATION: {
                    builder.chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages((int)20)).chatRequestTransformer((BiFunction)new Context.Summarizer(agenticScope, this.chatModel, new String[0]));
                }
            }
        }
    }

    private String agentId() {
        return this.outputKey + "@Supervisor";
    }

    @Override
    public Map<String, Object> executionState() {
        return Collections.singletonMap("loopCount", this.loopCount);
    }

    @Override
    public void restoreExecutionState(Map<String, Object> state) {
        Object savedLoopCount = state.get("loopCount");
        if (savedLoopCount instanceof Number) {
            Number n = (Number)savedLoopCount;
            this.loopCount = n.intValue();
        }
    }

    @Override
    public ChatMemoryAccess chatMemoryAccess(AgenticScope agenticScope) {
        return this.planner(agenticScope);
    }
}

