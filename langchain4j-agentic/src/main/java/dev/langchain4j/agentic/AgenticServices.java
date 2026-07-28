/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.ChatModel
 */
package dev.langchain4j.agentic;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.agent.AgentBuilder;
import dev.langchain4j.agentic.agent.UntypedAgentBuilder;
import dev.langchain4j.agentic.declarative.A2AClientAgent;
import dev.langchain4j.agentic.declarative.A2AClientCustomizer;
import dev.langchain4j.agentic.declarative.A2AServerUrlSupplier;
import dev.langchain4j.agentic.declarative.ActivationCondition;
import dev.langchain4j.agentic.declarative.AgentListenerSupplier;
import dev.langchain4j.agentic.declarative.ChatModelSupplier;
import dev.langchain4j.agentic.declarative.ConditionalAgent;
import dev.langchain4j.agentic.declarative.DeclarativeUtil;
import dev.langchain4j.agentic.declarative.HumanInTheLoop;
import dev.langchain4j.agentic.declarative.LoopAgent;
import dev.langchain4j.agentic.declarative.McpClientAgent;
import dev.langchain4j.agentic.declarative.McpClientSupplier;
import dev.langchain4j.agentic.declarative.ParallelAgent;
import dev.langchain4j.agentic.declarative.ParallelMapperAgent;
import dev.langchain4j.agentic.declarative.PlannerAgent;
import dev.langchain4j.agentic.declarative.RegistryAgent;
import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.agentic.declarative.SupervisorAgent;
import dev.langchain4j.agentic.internal.A2AClientBuilder;
import dev.langchain4j.agentic.internal.A2AService;
import dev.langchain4j.agentic.internal.AbstractServiceBuilder;
import dev.langchain4j.agentic.internal.AgentExecutor;
import dev.langchain4j.agentic.internal.AgentInvoker;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.internal.McpClientBuilder;
import dev.langchain4j.agentic.internal.McpService;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticService;
import dev.langchain4j.agentic.planner.AgentsRegistry;
import dev.langchain4j.agentic.planner.PlannerBasedService;
import dev.langchain4j.agentic.planner.PlannerBasedServiceImpl;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.supervisor.SupervisorAgentService;
import dev.langchain4j.agentic.supervisor.SupervisorAgentServiceImpl;
import dev.langchain4j.agentic.workflow.ConditionalAgentService;
import dev.langchain4j.agentic.workflow.HumanInTheLoop;
import dev.langchain4j.agentic.workflow.LoopAgentService;
import dev.langchain4j.agentic.workflow.ParallelAgentService;
import dev.langchain4j.agentic.workflow.ParallelMapperService;
import dev.langchain4j.agentic.workflow.SequentialAgentService;
import dev.langchain4j.agentic.workflow.WorkflowAgentsBuilder;
import dev.langchain4j.agentic.workflow.impl.WorkflowAgentsBuilderImpl;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.ChatModel;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AgenticServices {
    private AgenticServices() {
    }

    public static void setWorkflowAgentsBuilder(WorkflowAgentsBuilder workflowAgentsBuilder) {
        WorkflowBuilderProvider.INSTANCE.internalSetWorkflowAgentsBuilder(workflowAgentsBuilder);
    }

    private static WorkflowAgentsBuilder workflowAgentsBuilder() {
        return WorkflowBuilderProvider.INSTANCE.workflowAgentsBuilder;
    }

    public static UntypedAgentBuilder agentBuilder() {
        return new UntypedAgentBuilder();
    }

    public static <T> AgentBuilder<T, AgentBuilder<T, ?>> agentBuilder(Class<T> agentServiceClass) {
        return new AgentBuilder(agentServiceClass);
    }

    public static HumanInTheLoop.HumanInTheLoopBuilder humanInTheLoopBuilder() {
        return new HumanInTheLoop.HumanInTheLoopBuilder();
    }

    public static SequentialAgentService<UntypedAgent> sequenceBuilder() {
        return AgenticServices.workflowAgentsBuilder().sequenceBuilder();
    }

    public static <T> SequentialAgentService<T> sequenceBuilder(Class<T> agentServiceClass) {
        return AgenticServices.workflowAgentsBuilder().sequenceBuilder(agentServiceClass);
    }

    public static ParallelAgentService<UntypedAgent> parallelBuilder() {
        return AgenticServices.workflowAgentsBuilder().parallelBuilder();
    }

    public static <T> ParallelAgentService<T> parallelBuilder(Class<T> agentServiceClass) {
        return AgenticServices.workflowAgentsBuilder().parallelBuilder(agentServiceClass);
    }

    public static ParallelMapperService<UntypedAgent> parallelMapperBuilder() {
        return AgenticServices.workflowAgentsBuilder().parallelMapperBuilder();
    }

    public static <T> ParallelMapperService<T> parallelMapperBuilder(Class<T> agentServiceClass) {
        return AgenticServices.workflowAgentsBuilder().parallelMapperBuilder(agentServiceClass);
    }

    public static LoopAgentService<UntypedAgent> loopBuilder() {
        return AgenticServices.workflowAgentsBuilder().loopBuilder();
    }

    public static <T> LoopAgentService<T> loopBuilder(Class<T> agentServiceClass) {
        return AgenticServices.workflowAgentsBuilder().loopBuilder(agentServiceClass);
    }

    public static ConditionalAgentService<UntypedAgent> conditionalBuilder() {
        return AgenticServices.workflowAgentsBuilder().conditionalBuilder();
    }

    public static <T> ConditionalAgentService<T> conditionalBuilder(Class<T> agentServiceClass) {
        return AgenticServices.workflowAgentsBuilder().conditionalBuilder(agentServiceClass);
    }

    public static SupervisorAgentService<dev.langchain4j.agentic.supervisor.SupervisorAgent> supervisorBuilder() {
        return SupervisorAgentServiceImpl.builder();
    }

    public static <T> SupervisorAgentService<T> supervisorBuilder(Class<T> agentServiceClass) {
        return SupervisorAgentServiceImpl.builder(agentServiceClass);
    }

    public static PlannerBasedService<UntypedAgent> plannerBuilder() {
        return PlannerBasedServiceImpl.builder(UntypedAgent.class);
    }

    public static <T> PlannerBasedService<T> plannerBuilder(Class<T> agentServiceClass) {
        return PlannerBasedServiceImpl.builder(agentServiceClass);
    }

    public static A2AClientBuilder<UntypedAgent> a2aBuilder(String a2aServerUrl) {
        return AgenticServices.a2aBuilder(a2aServerUrl, UntypedAgent.class);
    }

    public static <T> A2AClientBuilder<T> a2aBuilder(String a2aServerUrl, Class<T> agentServiceClass) {
        return A2AService.get().a2aBuilder(a2aServerUrl, agentServiceClass);
    }

    public static <T> T createAgenticSystem(Class<T> agentServiceClass) {
        return AgenticServices.createAgenticSystem(agentServiceClass, AgenticServices.declarativeChatModel(agentServiceClass));
    }

    public static <T> T createAgenticSystem(Class<T> agentServiceClass, ChatModel chatModel) {
        return AgenticServices.createAgenticSystem(agentServiceClass, chatModel, AgentConfigurator.empty());
    }

    public static <T> T createAgenticSystem(Class<T> agentServiceClass, AgentConfigurator agentConfigurator) {
        return AgenticServices.createAgenticSystem(agentServiceClass, AgenticServices.declarativeChatModel(agentServiceClass), agentConfigurator);
    }

    public static <T> T createAgenticSystem(Class<T> agentServiceClass, ChatModel chatModel, AgentConfigurator agentConfigurator) {
        T agent = AgenticServices.createComposedAgent(agentServiceClass, chatModel, agentConfigurator);
        if (agent == null) {
            Optional<Method> a2aClientMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, A2AClientAgent.class);
            if (a2aClientMethod.isPresent()) {
                return AgenticServices.createA2AClient(agentServiceClass, a2aClientMethod.get());
            }
            AgentBuilder<T, AgentBuilder<T, ?>> agentBuilder = AgentBuilder.withoutDeclarativeConfiguration(agentServiceClass);
            DeclarativeUtil.configureAgent(agentServiceClass, chatModel, agentBuilder, agentConfigurator);
            agent = agentBuilder.build();
        }
        if (agent == null) {
            throw new IllegalArgumentException("Provided class " + agentServiceClass.getName() + " is not an agent.");
        }
        return agent;
    }

    private static <T> ChatModel declarativeChatModel(Class<T> agentServiceClass) {
        return DeclarativeUtil.selectMethod(agentServiceClass, method -> method.isAnnotationPresent(ChatModelSupplier.class) && method.getReturnType() == ChatModel.class && method.getParameterCount() == 0).map(method -> (ChatModel)DeclarativeUtil.invokeStatic(method, new Object[0])).orElse(null);
    }

    private static <T> T createComposedAgent(Class<T> agentServiceClass, ChatModel chatModel, AgentConfigurator agentConfigurator) {
        Optional<Method> sequenceMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, SequenceAgent.class);
        if (sequenceMethod.isPresent()) {
            return AgenticServices.buildSequentialAgent(agentServiceClass, sequenceMethod.get(), chatModel, agentConfigurator);
        }
        Optional<Method> loopMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, LoopAgent.class);
        if (loopMethod.isPresent()) {
            return AgenticServices.buildLoopAgent(agentServiceClass, loopMethod.get(), chatModel, agentConfigurator);
        }
        Optional<Method> conditionalMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, ConditionalAgent.class);
        if (conditionalMethod.isPresent()) {
            return AgenticServices.buildConditionalAgent(agentServiceClass, conditionalMethod.get(), chatModel, agentConfigurator);
        }
        Optional<Method> parallelMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, ParallelAgent.class);
        if (parallelMethod.isPresent()) {
            return AgenticServices.buildParallelAgent(agentServiceClass, parallelMethod.get(), chatModel, agentConfigurator);
        }
        Optional<Method> parallelMapperMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, ParallelMapperAgent.class);
        if (parallelMapperMethod.isPresent()) {
            return AgenticServices.buildParallelMapperAgent(agentServiceClass, parallelMapperMethod.get(), chatModel, agentConfigurator);
        }
        Optional<Method> supervisorMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, SupervisorAgent.class);
        if (supervisorMethod.isPresent()) {
            return AgenticServices.buildSupervisorAgent(agentServiceClass, supervisorMethod.get(), chatModel, agentConfigurator);
        }
        Optional<Method> plannerMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, PlannerAgent.class);
        if (plannerMethod.isPresent()) {
            return AgenticServices.buildPlannerAgent(agentServiceClass, plannerMethod.get(), chatModel, agentConfigurator);
        }
        return null;
    }

    private static void setAgentInstanceFactory(Object builder, AgentConfigurator agentConfigurator) {
        if (agentConfigurator.agentInstanceFactory() != null) {
            ((AbstractServiceBuilder)builder).agentInstanceFactory(agentConfigurator.agentInstanceFactory());
        }
    }

    private static void buildAgentSpecs(Method agentMethod, String name, String description, String outputKey, AgenticService<?, ?> builder) {
        if (!Utils.isNullOrBlank((String)name)) {
            builder.name(name);
        } else {
            builder.name(agentMethod.getName());
        }
        if (!Utils.isNullOrBlank((String)description)) {
            builder.description(description);
        }
        if (!Utils.isNullOrBlank((String)outputKey)) {
            builder.outputKey(outputKey);
        }
    }

    private static <T> T buildSequentialAgent(Class<T> agentServiceClass, Method agentMethod, ChatModel chatModel, AgentConfigurator agentConfigurator) {
        SequenceAgent annotation = agentMethod.getAnnotation(SequenceAgent.class);
        SequentialAgentService builder = (SequentialAgentService)AgenticServices.sequenceBuilder(agentServiceClass).subAgents(AgenticServices.createSubagents(annotation.subAgents(), chatModel, agentConfigurator));
        AgenticServices.setAgentInstanceFactory(builder, agentConfigurator);
        AgenticServices.buildAgentSpecs(agentMethod, annotation.name(), annotation.description(), AgentUtil.outputKey(annotation.outputKey(), annotation.typedOutputKey()), builder);
        return (T)builder.build();
    }

    private static <T> T buildLoopAgent(Class<T> agentServiceClass, Method agentMethod, ChatModel chatModel, AgentConfigurator agentConfigurator) {
        LoopAgent annotation = agentMethod.getAnnotation(LoopAgent.class);
        LoopAgentService builder = ((LoopAgentService)AgenticServices.loopBuilder(agentServiceClass).subAgents(AgenticServices.createSubagents(annotation.subAgents(), chatModel, agentConfigurator))).maxIterations(annotation.maxIterations());
        AgenticServices.setAgentInstanceFactory(builder, agentConfigurator);
        AgenticServices.buildAgentSpecs(agentMethod, annotation.name(), annotation.description(), AgentUtil.outputKey(annotation.outputKey(), annotation.typedOutputKey()), builder);
        return (T)builder.build();
    }

    private static <T> T buildConditionalAgent(Class<T> agentServiceClass, Method agentMethod, ChatModel chatModel, AgentConfigurator agentConfigurator) {
        ConditionalAgent annotation = agentMethod.getAnnotation(ConditionalAgent.class);
        ConditionalAgentService builder = AgenticServices.conditionalBuilder(agentServiceClass);
        AgenticServices.setAgentInstanceFactory(builder, agentConfigurator);
        AgenticServices.buildAgentSpecs(agentMethod, annotation.name(), annotation.description(), AgentUtil.outputKey(annotation.outputKey(), annotation.typedOutputKey()), builder);
        for (Class<?> subagent : annotation.subAgents()) {
            DeclarativeUtil.predicateMethod(agentServiceClass, method -> {
                ActivationCondition activationCondition = method.getAnnotation(ActivationCondition.class);
                return activationCondition != null && Arrays.asList(activationCondition.value()).contains(subagent);
            }).ifPresent(method -> builder.subAgent(method.getAnnotation(ActivationCondition.class).description(), DeclarativeUtil.agenticScopePredicate(method), AgenticServices.createSubagent(subagent, chatModel, agentConfigurator)));
        }
        return (T)builder.build();
    }

    private static <T> T buildParallelAgent(Class<T> agentServiceClass, Method agentMethod, ChatModel chatModel, AgentConfigurator agentConfigurator) {
        ParallelAgent annotation = agentMethod.getAnnotation(ParallelAgent.class);
        ParallelAgentService builder = (ParallelAgentService)AgenticServices.parallelBuilder(agentServiceClass).subAgents(AgenticServices.createSubagents(annotation.subAgents(), chatModel, agentConfigurator));
        AgenticServices.setAgentInstanceFactory(builder, agentConfigurator);
        AgenticServices.buildAgentSpecs(agentMethod, annotation.name(), annotation.description(), AgentUtil.outputKey(annotation.outputKey(), annotation.typedOutputKey()), builder);
        return (T)builder.build();
    }

    private static <T> T buildParallelMapperAgent(Class<T> agentServiceClass, Method agentMethod, ChatModel chatModel, AgentConfigurator agentConfigurator) {
        ParallelMapperAgent annotation = agentMethod.getAnnotation(ParallelMapperAgent.class);
        ParallelMapperService builder = ((ParallelMapperService)AgenticServices.parallelMapperBuilder(agentServiceClass).subAgents(Arrays.asList(AgenticServices.createSubagent(annotation.subAgent(), chatModel, agentConfigurator)))).itemsProvider(annotation.itemsProvider());
        AgenticServices.setAgentInstanceFactory(builder, agentConfigurator);
        AgenticServices.buildAgentSpecs(agentMethod, annotation.name(), annotation.description(), AgentUtil.outputKey(annotation.outputKey(), annotation.typedOutputKey()), builder);
        return (T)builder.build();
    }

    private static <T> T buildPlannerAgent(Class<T> agentServiceClass, Method agentMethod, ChatModel chatModel, AgentConfigurator agentConfigurator) {
        PlannerAgent annotation = agentMethod.getAnnotation(PlannerAgent.class);
        PlannerBasedService builder = (PlannerBasedService)new PlannerBasedServiceImpl<T>(agentServiceClass, agentMethod).subAgents(AgenticServices.createSubagents(annotation.subAgents(), chatModel, agentConfigurator));
        AgenticServices.setAgentInstanceFactory(builder, agentConfigurator);
        AgenticServices.buildAgentSpecs(agentMethod, annotation.name(), annotation.description(), AgentUtil.outputKey(annotation.outputKey(), annotation.typedOutputKey()), builder);
        return (T)builder.build();
    }

    private static <T> T buildSupervisorAgent(Class<T> agentServiceClass, Method agentMethod, ChatModel chatModel, AgentConfigurator agentConfigurator) {
        SupervisorAgent supervisorAgent = agentMethod.getAnnotation(SupervisorAgent.class);
        SupervisorAgentServiceImpl builder = (SupervisorAgentServiceImpl)((AbstractServiceBuilder)((Object)((SupervisorAgentServiceImpl)((SupervisorAgentServiceImpl)new SupervisorAgentServiceImpl<T>(agentServiceClass, agentMethod, chatModel).maxAgentsInvocations(supervisorAgent.maxAgentsInvocations())).contextGenerationStrategy(supervisorAgent.contextStrategy())).responseStrategy(supervisorAgent.responseStrategy()))).subAgents(AgenticServices.createSubagents(supervisorAgent.subAgents(), chatModel, agentConfigurator));
        AgenticServices.setAgentInstanceFactory(builder, agentConfigurator);
        if (!Utils.isNullOrBlank((String)supervisorAgent.name())) {
            builder.name(supervisorAgent.name());
        } else {
            builder.name(agentMethod.getName());
        }
        if (!Utils.isNullOrBlank((String)supervisorAgent.description())) {
            builder.description(supervisorAgent.description());
        }
        builder.outputKey(AgentUtil.outputKey(supervisorAgent.outputKey(), supervisorAgent.typedOutputKey()));
        return builder.build();
    }

    private static List<AgentExecutor> createSubagents(Class<?>[] subAgents, ChatModel chatModel, AgentConfigurator agentConfigurator) {
        return Stream.of(subAgents).map(subagent -> AgenticServices.createSubagent(subagent, chatModel, agentConfigurator)).collect(Collectors.toList());
    }

    private static AgentExecutor createSubagent(Class<?> subgentClass, ChatModel chatModel, AgentConfigurator agentConfigurator) {
        Object subagent;
        if (agentConfigurator.subAgentResolver() != null && (subagent = agentConfigurator.subAgentResolver().apply(subgentClass)) != null) {
            return AgentUtil.agentToExecutor(subagent);
        }
        AgentExecutor agentExecutor = AgenticServices.createBuiltInAgentExecutor(subgentClass, chatModel, agentConfigurator);
        if (agentExecutor != null) {
            return agentExecutor;
        }
        AgentBuilder<?, AgentBuilder<?, ?>> agentBuilder = AgentBuilder.withoutDeclarativeConfiguration(subgentClass);
        DeclarativeUtil.configureAgent(subgentClass, chatModel, agentBuilder, agentConfigurator);
        return AgentUtil.agentToExecutor(agentBuilder.build());
    }

    public static AgentExecutor createBuiltInAgentExecutor(Class<?> agentServiceClass) {
        return AgenticServices.createBuiltInAgentExecutor(agentServiceClass, AgenticServices.declarativeChatModel(agentServiceClass), AgentConfigurator.empty());
    }

    private static AgentExecutor createBuiltInAgentExecutor(Class<?> agentServiceClass, ChatModel chatModel, AgentConfigurator agentConfigurator) {
        Method agenticMethod;
        Optional<Method> sequenceMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, SequenceAgent.class);
        if (sequenceMethod.isPresent()) {
            Method method = sequenceMethod.get();
            InternalAgent agent = (InternalAgent)AgenticServices.buildSequentialAgent(agentServiceClass, method, chatModel, agentConfigurator);
            return new AgentExecutor(AgentInvoker.fromMethod(agent, method), agent);
        }
        Optional<Method> loopMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, LoopAgent.class);
        if (loopMethod.isPresent()) {
            Method method = loopMethod.get();
            InternalAgent agent = (InternalAgent)AgenticServices.buildLoopAgent(agentServiceClass, method, chatModel, agentConfigurator);
            return new AgentExecutor(AgentInvoker.fromMethod(agent, method), agent);
        }
        Optional<Method> conditionalMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, ConditionalAgent.class);
        if (conditionalMethod.isPresent()) {
            Method method = conditionalMethod.get();
            InternalAgent agent = (InternalAgent)AgenticServices.buildConditionalAgent(agentServiceClass, method, chatModel, agentConfigurator);
            return new AgentExecutor(AgentInvoker.fromMethod(agent, method), agent);
        }
        Optional<Method> parallelMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, ParallelAgent.class);
        if (parallelMethod.isPresent()) {
            Method method = parallelMethod.get();
            InternalAgent agent = (InternalAgent)AgenticServices.buildParallelAgent(agentServiceClass, method, chatModel, agentConfigurator);
            return new AgentExecutor(AgentInvoker.fromMethod(agent, method), agent);
        }
        Optional<Method> parallelMapperMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, ParallelMapperAgent.class);
        if (parallelMapperMethod.isPresent()) {
            Method method = parallelMapperMethod.get();
            InternalAgent agent = (InternalAgent)AgenticServices.buildParallelMapperAgent(agentServiceClass, method, chatModel, agentConfigurator);
            return new AgentExecutor(AgentInvoker.fromMethod(agent, method), agent);
        }
        Optional<Method> supervisorMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, SupervisorAgent.class);
        if (supervisorMethod.isPresent()) {
            Method method = supervisorMethod.get();
            InternalAgent agent = (InternalAgent)AgenticServices.buildSupervisorAgent(agentServiceClass, method, chatModel, agentConfigurator);
            return new AgentExecutor(AgentInvoker.fromMethod(agent, method), agent);
        }
        Optional<Method> plannerMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, PlannerAgent.class);
        if (plannerMethod.isPresent()) {
            Method method = plannerMethod.get();
            InternalAgent agent = (InternalAgent)AgenticServices.buildPlannerAgent(agentServiceClass, method, chatModel, agentConfigurator);
            return new AgentExecutor(AgentInvoker.fromMethod(agent, method), agent);
        }
        Optional<Method> humanInTheLoopMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, HumanInTheLoop.class);
        if (humanInTheLoopMethod.isPresent()) {
            return AgenticServices.createHumanInTheLoopAgent(agentServiceClass, humanInTheLoopMethod.get());
        }
        Optional<Method> a2aClientMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, A2AClientAgent.class);
        if (a2aClientMethod.isPresent()) {
            return AgenticServices.createA2AClientAgent(agentServiceClass, a2aClientMethod.get());
        }
        Optional<Method> registryAgentMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, RegistryAgent.class);
        if (registryAgentMethod.isPresent()) {
            return AgenticServices.createRegistryAgent(registryAgentMethod.get());
        }
        Optional<Method> mcpClientMethod = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, McpClientAgent.class);
        if (mcpClientMethod.isPresent()) {
            return AgenticServices.createMcpClientAgent(agentServiceClass, mcpClientMethod.get());
        }
        if (!agentServiceClass.isInterface() && (agenticMethod = AgenticServices.nonAiAgentMethod(agentServiceClass)) != null) {
            return AgentUtil.nonAiAgentToExecutor(new AgenticScopeFunction(scope -> DeclarativeUtil.invokeStatic(agenticMethod, AgentUtil.agentInvocationArguments(scope, agenticMethod).positionalArgs())), agenticMethod);
        }
        return null;
    }

    private static AgentExecutor createA2AClientAgent(Class<?> agentServiceClass, Method a2aMethod) {
        return AgentUtil.agentToExecutor(AgenticServices.createA2AClient(agentServiceClass, a2aMethod));
    }

    private static AgentExecutor createRegistryAgent(Method registryMethod) {
        String registryName = registryMethod.getAnnotation(RegistryAgent.class).value();
        AgentInstance agent = AgentsRegistry.get().getAgent(registryName);
        return AgentUtil.agentToExecutor(agent);
    }

    private static <T> T createA2AClient(Class<T> agentServiceClass, Method a2aMethod) {
        A2AClientAgent a2aClient = a2aMethod.getAnnotation(A2AClientAgent.class);
        String a2aServerUrl = AgenticServices.resolveA2AServerUrl(agentServiceClass, a2aClient);
        A2AClientBuilder a2aClientBuilder = AgenticServices.a2aBuilder(a2aServerUrl, agentServiceClass).inputKeys((String[])Stream.of(a2aMethod.getParameters()).map(AgentInvoker::parameterName).toArray(String[]::new)).outputKey(AgentUtil.outputKey(a2aClient.outputKey(), a2aClient.typedOutputKey())).async(a2aClient.async());
        DeclarativeUtil.selectMethod(agentServiceClass, method -> method.isAnnotationPresent(A2AClientCustomizer.class) && method.getParameterCount() == 1).ifPresent(method -> a2aClientBuilder.clientCustomizer(cb -> DeclarativeUtil.invokeStatic(method, cb)));
        AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, AgentListenerSupplier.class).ifPresent(method -> {
            DeclarativeUtil.checkReturnType(method, AgentListener.class);
            a2aClientBuilder.listener((AgentListener)DeclarativeUtil.invokeStatic(method, new Object[0]));
        });
        return a2aClientBuilder.build();
    }

    private static String resolveA2AServerUrl(Class<?> agentServiceClass, A2AClientAgent a2aClient) {
        String annotationUrl = a2aClient.a2aServerUrl();
        Optional<Method> supplierMethod = DeclarativeUtil.selectMethod(agentServiceClass, method -> method.isAnnotationPresent(A2AServerUrlSupplier.class) && method.getParameterCount() == 0);
        if (!Utils.isNullOrBlank((String)annotationUrl) && supplierMethod.isPresent()) {
            throw new IllegalArgumentException("Provide either a2aServerUrl in the @A2AClientAgent annotation or an @A2AServerUrlSupplier method, not both.");
        }
        if (supplierMethod.isPresent()) {
            DeclarativeUtil.checkReturnType(supplierMethod.get(), String.class);
            return (String)DeclarativeUtil.invokeStatic(supplierMethod.get(), new Object[0]);
        }
        if (!Utils.isNullOrBlank((String)annotationUrl)) {
            return annotationUrl;
        }
        throw new IllegalArgumentException("An A2A client agent requires either a2aServerUrl in the @A2AClientAgent annotation or a method annotated with @A2AServerUrlSupplier.");
    }

    private static AgentExecutor createMcpClientAgent(Class<?> agentServiceClass, Method mcpMethod) {
        McpClientAgent mcpAgent = mcpMethod.getAnnotation(McpClientAgent.class);
        Object mcpClient = DeclarativeUtil.selectMethod(agentServiceClass, method -> method.isAnnotationPresent(McpClientSupplier.class) && method.getParameterCount() == 0).map(method -> DeclarativeUtil.invokeStatic(method, new Object[0])).orElseThrow(() -> new IllegalArgumentException("An MCP client agent requires a method annotated with @McpClientSupplier that returns the McpClient instance."));
        McpClientBuilder<?> mcpClientBuilder = McpService.get().mcpBuilder(mcpClient, agentServiceClass).toolName(mcpAgent.toolName()).inputKeys((String[])Stream.of(mcpMethod.getParameters()).map(AgentInvoker::parameterName).toArray(String[]::new)).outputKey(AgentUtil.outputKey(mcpAgent.outputKey(), mcpAgent.typedOutputKey())).async(mcpAgent.async());
        AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, AgentListenerSupplier.class).ifPresent(method -> {
            DeclarativeUtil.checkReturnType(method, AgentListener.class);
            mcpClientBuilder.listener((AgentListener)DeclarativeUtil.invokeStatic(method, new Object[0]));
        });
        return AgentUtil.agentToExecutor(mcpClientBuilder.build());
    }

    private static AgentExecutor createHumanInTheLoopAgent(Class<?> agentServiceClass, Method method) {
        HumanInTheLoop humanInTheLoop = method.getAnnotation(HumanInTheLoop.class);
        List<AgentArgument> methodArguments = AgentUtil.argumentsFromMethod(method).stream().filter(arg -> !arg.name().startsWith("@")).collect(Collectors.toList());
        HumanInTheLoop.HumanInTheLoopBuilder humanInTheLoopBuilder = AgenticServices.humanInTheLoopBuilder().description(humanInTheLoop.description()).outputKey(humanInTheLoop.outputKey()).async(humanInTheLoop.async()).inputs(methodArguments.isEmpty() ? null : methodArguments).responseProvider(scope -> DeclarativeUtil.invokeStatic(method, AgentUtil.agentInvocationArguments(scope, method).positionalArgs()));
        AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, AgentListenerSupplier.class).ifPresent(listenerMethod -> {
            DeclarativeUtil.checkReturnType(listenerMethod, AgentListener.class);
            humanInTheLoopBuilder.listener((AgentListener)DeclarativeUtil.invokeStatic(listenerMethod, new Object[0]));
        });
        String name = Utils.isNullOrBlank((String)humanInTheLoop.name()) ? method.getName() : humanInTheLoop.name();
        AgentInvoker agentInvoker = AgentUtil.nonAiAgentInvoker(method, name, humanInTheLoop.description(), humanInTheLoop.outputKey(), humanInTheLoop.async());
        return new AgentExecutor(agentInvoker, humanInTheLoopBuilder.build());
    }

    private static Method nonAiAgentMethod(Class<?> agentServiceClass) {
        Method agenticMethod = null;
        for (Method method : agentServiceClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Agent.class) || !Modifier.isStatic(method.getModifiers())) continue;
            if (agenticMethod != null) {
                throw new IllegalArgumentException("Multiple agent methods found in class: " + agentServiceClass.getName());
            }
            agenticMethod = method;
        }
        return agenticMethod;
    }

    public static AgentAction agentAction(AgentAction.NonThrowingRunnable runnable) {
        return new AgentAction(runnable);
    }

    public static AgenticScopeAction agentAction(AgenticScopeAction.NonThrowingConsumer<AgenticScope> consumer) {
        return new AgenticScopeAction(consumer);
    }

    public static class AgenticScopeFunction<T> {
        private final NonThrowingFunction<AgenticScope, T> function;

        private AgenticScopeFunction(NonThrowingFunction<AgenticScope, T> function) {
            this.function = function;
        }

        @Agent
        public T accept(AgenticScope agenticScope) {
            try {
                return this.function.apply(agenticScope);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @FunctionalInterface
        public static interface NonThrowingFunction<A, B> {
            public B apply(A var1) throws Exception;
        }
    }

    public static class AgenticScopeAction {
        private final NonThrowingConsumer<AgenticScope> consumer;

        private AgenticScopeAction(NonThrowingConsumer<AgenticScope> consumer) {
            this.consumer = consumer;
        }

        @Agent
        public void accept(AgenticScope agenticScope) {
            try {
                this.consumer.accept(agenticScope);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @FunctionalInterface
        public static interface NonThrowingConsumer<T> {
            public void accept(T var1) throws Exception;
        }
    }

    public static class AgentAction {
        private final NonThrowingRunnable runnable;

        private AgentAction(NonThrowingRunnable runnable) {
            this.runnable = runnable;
        }

        @Agent
        public void run() {
            try {
                this.runnable.run();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @FunctionalInterface
        public static interface NonThrowingRunnable {
            public void run() throws Exception;
        }
    }

    public static class AgentConfigurator {
        private final Consumer<DeclarativeAgentCreationContext<?>> configurator;
        private final Function<Class<?>, Object> subAgentResolver;
        private final Function<InternalAgent, Object> agentInstanceFactory;
        private static final AgentConfigurator EMPTY = new AgentConfigurator(ctx -> {}, null, null);

        public AgentConfigurator(Consumer<DeclarativeAgentCreationContext<?>> configurator, Function<Class<?>, Object> subAgentResolver, Function<InternalAgent, Object> agentInstanceFactory) {
            this.configurator = configurator;
            this.subAgentResolver = subAgentResolver;
            this.agentInstanceFactory = agentInstanceFactory;
        }

        public Consumer<DeclarativeAgentCreationContext<?>> configurator() {
            return this.configurator;
        }

        public Function<Class<?>, Object> subAgentResolver() {
            return this.subAgentResolver;
        }

        public Function<InternalAgent, Object> agentInstanceFactory() {
            return this.agentInstanceFactory;
        }

        public static AgentConfigurator empty() {
            return EMPTY;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AgentConfigurator)) {
                return false;
            }
            AgentConfigurator other = (AgentConfigurator)o;
            if (!Objects.equals(this.configurator, other.configurator)) {
                return false;
            }
            if (!Objects.equals(this.subAgentResolver, other.subAgentResolver)) {
                return false;
            }
            return Objects.equals(this.agentInstanceFactory, other.agentInstanceFactory);
        }

        public int hashCode() {
            return Objects.hash(this.configurator, this.subAgentResolver, this.agentInstanceFactory);
        }

        public String toString() {
            return "AgentConfigurator{configurator=" + this.configurator + ", subAgentResolver=" + this.subAgentResolver + ", agentInstanceFactory=" + this.agentInstanceFactory + "}";
        }
    }

    public static class DefaultDeclarativeAgentCreationContext<T>
    implements DeclarativeAgentCreationContext<T> {
        private final Class<T> agentServiceClass;
        private final AgentBuilder<T, ?> agentBuilder;

        public DefaultDeclarativeAgentCreationContext(Class<T> agentServiceClass, AgentBuilder<T, ?> agentBuilder) {
            this.agentServiceClass = agentServiceClass;
            this.agentBuilder = agentBuilder;
        }

        @Override
        public Class<T> agentServiceClass() {
            return this.agentServiceClass;
        }

        @Override
        public AgentBuilder<T, ?> agentBuilder() {
            return this.agentBuilder;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof DefaultDeclarativeAgentCreationContext)) {
                return false;
            }
            DefaultDeclarativeAgentCreationContext other = (DefaultDeclarativeAgentCreationContext)o;
            if (!Objects.equals(this.agentServiceClass, other.agentServiceClass)) {
                return false;
            }
            return Objects.equals(this.agentBuilder, other.agentBuilder);
        }

        public int hashCode() {
            return Objects.hash(this.agentServiceClass, this.agentBuilder);
        }

        public String toString() {
            return "DefaultDeclarativeAgentCreationContext{agentServiceClass=" + this.agentServiceClass + ", agentBuilder=" + this.agentBuilder + "}";
        }
    }

    public static interface DeclarativeAgentCreationContext<T> {
        public Class<T> agentServiceClass();

        public AgentBuilder<T, ?> agentBuilder();
    }

    private static enum WorkflowBuilderProvider {
        INSTANCE;

        private WorkflowAgentsBuilder workflowAgentsBuilder;

        private WorkflowBuilderProvider() {
            this.internalSetWorkflowAgentsBuilder(WorkflowBuilderProvider.loadWorkflowAgentsBuilder());
        }

        private static WorkflowAgentsBuilder loadWorkflowAgentsBuilder() {
            ServiceLoader<WorkflowAgentsBuilder> loader = ServiceLoader.load(WorkflowAgentsBuilder.class);
            Iterator<WorkflowAgentsBuilder> iterator = loader.iterator();
            if (iterator.hasNext()) {
                WorkflowAgentsBuilder builder = iterator.next();
                return builder;
            }
            return WorkflowAgentsBuilderImpl.INSTANCE;
        }

        private void internalSetWorkflowAgentsBuilder(WorkflowAgentsBuilder workflowAgentsBuilder) {
            this.workflowAgentsBuilder = workflowAgentsBuilder;
        }
    }
}

