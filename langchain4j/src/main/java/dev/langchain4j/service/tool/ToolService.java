/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.CompensateFor
 *  dev.langchain4j.agent.tool.P
 *  dev.langchain4j.agent.tool.ReturnBehavior
 *  dev.langchain4j.agent.tool.Tool
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolMemoryId
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.agent.tool.ToolSpecifications
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.exception.ToolArgumentsException
 *  dev.langchain4j.internal.DefaultExecutorProvider
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.invocation.InvocationParameters
 *  dev.langchain4j.invocation.LangChain4jManaged
 *  dev.langchain4j.memory.ChatMemory
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.observability.api.AiServiceListenerRegistrar
 *  dev.langchain4j.observability.api.event.AiServiceEvent
 *  dev.langchain4j.observability.api.event.AiServiceRequestIssuedEvent
 *  dev.langchain4j.observability.api.event.AiServiceResponseReceivedEvent
 *  dev.langchain4j.observability.api.event.ToolExecutedEvent
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.CompensateFor;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.ReturnBehavior;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolMemoryId;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.exception.ToolArgumentsException;
import dev.langchain4j.internal.DefaultExecutorProvider;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.invocation.LangChain4jManaged;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.observability.api.AiServiceListenerRegistrar;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.api.event.AiServiceRequestIssuedEvent;
import dev.langchain4j.observability.api.event.AiServiceResponseReceivedEvent;
import dev.langchain4j.observability.api.event.ToolExecutedEvent;
import dev.langchain4j.service.AiServiceContext;
import dev.langchain4j.service.IllegalConfigurationException;
import dev.langchain4j.service.tool.AiServiceTool;
import dev.langchain4j.service.tool.BeforeToolExecution;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import dev.langchain4j.service.tool.HallucinatedToolNameStrategy;
import dev.langchain4j.service.tool.ToolArgumentsErrorHandler;
import dev.langchain4j.service.tool.ToolErrorContext;
import dev.langchain4j.service.tool.ToolErrorHandlerResult;
import dev.langchain4j.service.tool.ToolExecution;
import dev.langchain4j.service.tool.ToolExecutionErrorHandler;
import dev.langchain4j.service.tool.ToolExecutionResult;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.service.tool.ToolProviderRequest;
import dev.langchain4j.service.tool.ToolProviderResult;
import dev.langchain4j.service.tool.ToolServiceContext;
import dev.langchain4j.service.tool.ToolServiceResult;
import dev.langchain4j.service.tool.search.ToolSearchService;
import dev.langchain4j.service.tool.search.ToolSearchStrategy;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Internal
public class ToolService {
    private static final Logger log = LoggerFactory.getLogger(ToolService.class);
    private static final ToolArgumentsErrorHandler DEFAULT_TOOL_ARGUMENTS_ERROR_HANDLER = (error, context) -> {
        if (error instanceof RuntimeException) {
            RuntimeException re = (RuntimeException)error;
            throw re;
        }
        throw new RuntimeException(error);
    };
    private static final ToolExecutionErrorHandler DEFAULT_TOOL_EXECUTION_ERROR_HANDLER = (error, context) -> {
        String errorMessage = ToolService.errorMessage(error);
        log.warn("Tool '{}' execution failed. The error message is being returned to the LLM. To customize this behavior (and silence this log), configure a custom ToolExecutionErrorHandler via AiServices.toolExecutionErrorHandler(...). Error: {}", new Object[]{context.toolExecutionRequest().name(), errorMessage, error});
        return ToolErrorHandlerResult.text(errorMessage);
    };
    private final List<ToolSpecification> toolSpecifications = new ArrayList<ToolSpecification>();
    private final Map<String, ToolExecutor> toolExecutors = new HashMap<String, ToolExecutor>();
    private final Map<String, ReturnBehavior> returnBehaviors = new HashMap<String, ReturnBehavior>();
    private final Map<String, BiConsumer<ToolExecution, InvocationContext>> compensatingExecutors = new HashMap<String, BiConsumer<ToolExecution, InvocationContext>>();
    private IllegalConfigurationException compensatingToolMisconfiguration;
    private final Set<ToolProvider> toolProviders = new LinkedHashSet<ToolProvider>();
    private boolean compensateOnToolErrors;
    private Executor executor;
    private int maxToolCallingRoundTrips = 100;
    private ToolArgumentsErrorHandler argumentsErrorHandler;
    private ToolExecutionErrorHandler executionErrorHandler;
    private Function<ToolExecutionRequest, ToolExecutionResultMessage> toolHallucinationStrategy = HallucinatedToolNameStrategy.THROW_EXCEPTION;
    private ToolSearchService toolSearchService;
    private Consumer<BeforeToolExecution> beforeToolExecution = null;
    private Consumer<ToolExecution> afterToolExecution = null;

    public void hallucinatedToolNameStrategy(Function<ToolExecutionRequest, ToolExecutionResultMessage> toolHallucinationStrategy) {
        this.toolHallucinationStrategy = toolHallucinationStrategy;
    }

    public void toolProvider(ToolProvider toolProvider) {
        if (toolProvider != null) {
            this.toolProviders.add(toolProvider);
        }
    }

    public void toolProviders(Collection<ToolProvider> toolProviders) {
        if (toolProviders != null) {
            this.toolProviders.addAll(toolProviders);
        }
    }

    public void tools(Map<ToolSpecification, ToolExecutor> tools) {
        tools.forEach((toolSpecification, toolExecutor) -> {
            this.toolSpecifications.add((ToolSpecification)toolSpecification);
            this.toolExecutors.put(toolSpecification.name(), (ToolExecutor)toolExecutor);
        });
    }

    public void tools(Map<ToolSpecification, ToolExecutor> tools, Set<String> immediateReturnToolNames) {
        this.tools(tools);
        immediateReturnToolNames.forEach(name -> this.returnBehaviors.put((String)name, ReturnBehavior.IMMEDIATE));
    }

    public void tools(Collection<Object> objectsWithTools) {
        for (Object objectWithTools : objectsWithTools) {
            List<AiServiceTool> tools = ToolService.findTools(objectWithTools);
            ToolService.addTools(tools, this.toolExecutors, this.toolSpecifications, this.returnBehaviors);
            this.compensatingExecutors.putAll(this.findCompensatingActions(objectWithTools));
        }
    }

    public void tools(List<AiServiceTool> tools) {
        ToolService.addTools(tools, this.toolExecutors, this.toolSpecifications, this.returnBehaviors);
    }

    private static void validateToolParameters(Method toolMethod) {
        for (Parameter parameter : toolMethod.getParameters()) {
            boolean hasDefault;
            P pAnnotation = parameter.getAnnotation(P.class);
            if (pAnnotation == null) continue;
            Class<?> type = parameter.getType();
            boolean bl = hasDefault = !"\u0000__LANGCHAIN4J_NO_DEFAULT__\u0000".equals(pAnnotation.defaultValue());
            if (type.isPrimitive() && !pAnnotation.required() && !hasDefault) {
                throw IllegalConfigurationException.illegalConfiguration("Parameter '%s' of tool '%s.%s' is a primitive (%s) and cannot be marked as @P(required = false). Use a boxed type (e.g. Integer instead of int), Optional<T>, or @P(defaultValue = ...).", parameter.getName(), toolMethod.getDeclaringClass().getName(), toolMethod.getName(), type.getName());
            }
            if (!hasDefault) continue;
            if (type == Optional.class) {
                throw IllegalConfigurationException.illegalConfiguration("Parameter '%s' of tool '%s.%s' has @P(defaultValue = ...) and is Optional<T>. Optional<T> already represents \"absent\"; use one mechanism or the other.", parameter.getName(), toolMethod.getDeclaringClass().getName(), toolMethod.getName());
            }
            if (parameter.isAnnotationPresent(ToolMemoryId.class) || InvocationParameters.class.isAssignableFrom(type) || type == InvocationContext.class || LangChain4jManaged.class.isAssignableFrom(type)) {
                throw IllegalConfigurationException.illegalConfiguration("Parameter '%s' of tool '%s.%s' has @P(defaultValue = ...) but is a framework-injected parameter; default values are not supported on framework-injected parameters.", parameter.getName(), toolMethod.getDeclaringClass().getName(), toolMethod.getName());
            }
            try {
                DefaultToolExecutor.parseDefaultValue(pAnnotation.defaultValue(), parameter.getName(), type, parameter.getParameterizedType());
            }
            catch (Exception e) {
                throw IllegalConfigurationException.illegalConfiguration("Cannot parse @P(defaultValue = \"%s\") for parameter '%s' of tool '%s.%s' (type %s): %s", pAnnotation.defaultValue(), parameter.getName(), toolMethod.getDeclaringClass().getName(), toolMethod.getName(), type.getName(), e.getMessage());
            }
        }
    }

    private static ToolExecutor createToolExecutor(Object object, Method method) {
        return DefaultToolExecutor.builder().object(object).originalMethod(method).methodToInvoke(method).wrapToolArgumentsExceptions(true).propagateToolExecutionExceptions(true).build();
    }

    public static List<AiServiceTool> findTools(Object objectWithTools) {
        if (objectWithTools instanceof Class) {
            throw IllegalConfigurationException.illegalConfiguration("Tool '%s' must be an object, not a class", objectWithTools);
        }
        if (objectWithTools instanceof Iterable) {
            throw IllegalConfigurationException.illegalConfiguration("Tool '%s' is an Iterable (likely a nested collection). Please pass tool objects directly, not wrapped in collections.", objectWithTools.getClass().getName());
        }
        ArrayList<AiServiceTool> result = new ArrayList<AiServiceTool>();
        for (Method method : Utils.allConcreteMethods(objectWithTools.getClass())) {
            Optional annotatedMethod = Utils.getAnnotatedMethod((Method)method, Tool.class);
            if (!annotatedMethod.isPresent()) continue;
            Method toolMethod = (Method)annotatedMethod.get();
            ToolService.validateToolParameters(toolMethod);
            result.add(AiServiceTool.builder().toolSpecification(ToolSpecifications.toolSpecificationFrom((Method)toolMethod)).toolExecutor(ToolService.createToolExecutor(objectWithTools, toolMethod)).returnBehavior(toolMethod.getAnnotation(Tool.class).returnBehavior()).build());
        }
        if (result.isEmpty()) {
            throw IllegalConfigurationException.illegalConfiguration("Object '%s' does not have any methods annotated with @Tool", objectWithTools.getClass().getName());
        }
        return result;
    }

    public void compensateOnToolErrors(boolean compensateOnToolErrors) {
        this.compensateOnToolErrors = compensateOnToolErrors;
        if (compensateOnToolErrors && this.compensatingToolMisconfiguration != null) {
            throw this.compensatingToolMisconfiguration;
        }
    }

    private Map<String, BiConsumer<ToolExecution, InvocationContext>> findCompensatingActions(Object objectWithTools) {
        HashMap<String, BiConsumer<ToolExecution, InvocationContext>> compensatingActions = new HashMap<String, BiConsumer<ToolExecution, InvocationContext>>();
        if (this.compensatingToolMisconfiguration != null) {
            return compensatingActions;
        }
        for (Method method : Utils.allConcreteMethods(objectWithTools.getClass())) {
            boolean acceptsToolExecution;
            CompensateFor compensateFor = method.getAnnotation(CompensateFor.class);
            if (compensateFor == null) continue;
            String toolName = compensateFor.value();
            ToolExecutor toolExecutor = this.toolExecutors.get(toolName);
            if (toolExecutor == null) {
                this.compensatingToolMisconfiguration = IllegalConfigurationException.illegalConfiguration("@CompensateFor(\"%s\") on method '%s.%s' references tool '%s' which does not exist", toolName, objectWithTools.getClass().getName(), method.getName(), toolName);
                if (!this.compensateOnToolErrors) break;
                throw this.compensatingToolMisconfiguration;
            }
            if (!(toolExecutor instanceof DefaultToolExecutor)) {
                this.compensatingToolMisconfiguration = IllegalConfigurationException.illegalConfiguration("@CompensateFor(\"%s\") on method '%s.%s' references tool '%s' which is not a @Tool-annotated method. Only @Tool-annotated methods support compensating actions", toolName, objectWithTools.getClass().getName(), method.getName(), toolName);
                if (!this.compensateOnToolErrors) break;
                throw this.compensatingToolMisconfiguration;
            }
            Method toolMethod = ((DefaultToolExecutor)toolExecutor).originalMethod();
            Object[] compensatingParams = method.getParameterTypes();
            boolean bl = acceptsToolExecution = compensatingParams.length == 1 && compensatingParams[0] == ToolExecution.class;
            if (!acceptsToolExecution && !Arrays.equals(toolMethod.getParameterTypes(), compensatingParams)) {
                this.compensatingToolMisconfiguration = IllegalConfigurationException.illegalConfiguration("@CompensateFor(\"%s\") on method '%s.%s' must have the same parameter types as tool '%s' or a single %s parameter", toolName, objectWithTools.getClass().getName(), method.getName(), toolName, ToolExecution.class.getSimpleName());
                if (!this.compensateOnToolErrors) break;
                throw this.compensatingToolMisconfiguration;
            }
            if (acceptsToolExecution) {
                method.setAccessible(true);
                Method compensatingMethod = method;
                compensatingActions.put(toolName, (toolExecution, ctx) -> {
                    try {
                        compensatingMethod.invoke(objectWithTools, toolExecution);
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                continue;
            }
            DefaultToolExecutor executor = DefaultToolExecutor.builder().object(objectWithTools).originalMethod(toolMethod).methodToInvoke(method).propagateToolExecutionExceptions(true).build();
            compensatingActions.put(toolName, (toolExecution, ctx) -> executor.executeWithContext(toolExecution.request(), (InvocationContext)ctx));
        }
        return compensatingActions;
    }

    public void executeToolsConcurrently() {
        this.executor = ToolService.defaultExecutor();
    }

    public void executeToolsConcurrently(Executor executor) {
        this.executor = (Executor)Utils.getOrDefault((Object)executor, ToolService::defaultExecutor);
    }

    private static Executor defaultExecutor() {
        return DefaultExecutorProvider.getDefaultExecutorService();
    }

    public void maxToolCallingRoundTrips(int maxToolCallingRoundTrips) {
        this.maxToolCallingRoundTrips = maxToolCallingRoundTrips;
    }

    public int maxToolCallingRoundTrips() {
        return this.maxToolCallingRoundTrips;
    }

    @Deprecated
    public void maxSequentialToolsInvocations(int maxSequentialToolsInvocations) {
        this.maxToolCallingRoundTrips = maxSequentialToolsInvocations;
    }

    @Deprecated
    public int maxSequentialToolsInvocations() {
        return this.maxToolCallingRoundTrips;
    }

    public void beforeToolExecution(Consumer<BeforeToolExecution> beforeToolExecution) {
        this.beforeToolExecution = beforeToolExecution;
    }

    public Consumer<BeforeToolExecution> beforeToolExecution() {
        return this.beforeToolExecution;
    }

    public void afterToolExecution(Consumer<ToolExecution> afterToolExecution) {
        this.afterToolExecution = afterToolExecution;
    }

    public Consumer<ToolExecution> afterToolExecution() {
        return this.afterToolExecution;
    }

    public void argumentsErrorHandler(ToolArgumentsErrorHandler handler) {
        this.argumentsErrorHandler = handler;
    }

    public ToolArgumentsErrorHandler argumentsErrorHandler() {
        return (ToolArgumentsErrorHandler)Utils.getOrDefault((Object)this.argumentsErrorHandler, (Object)DEFAULT_TOOL_ARGUMENTS_ERROR_HANDLER);
    }

    public void executionErrorHandler(ToolExecutionErrorHandler handler) {
        this.executionErrorHandler = handler;
    }

    public ToolExecutionErrorHandler executionErrorHandler() {
        return (ToolExecutionErrorHandler)Utils.getOrDefault((Object)this.executionErrorHandler, (Object)DEFAULT_TOOL_EXECUTION_ERROR_HANDLER);
    }

    public void toolSearchStrategy(ToolSearchStrategy toolSearchStrategy) {
        this.toolSearchService = new ToolSearchService(toolSearchStrategy);
    }

    public ToolServiceContext createContext(InvocationContext invocationContext, UserMessage userMessage, List<ChatMessage> messages) {
        ToolServiceContext context = this.createContextFromStaticToolsAndProviders(invocationContext, userMessage, messages);
        if (this.toolSearchService != null) {
            context = this.toolSearchService.adjust(context, messages, invocationContext);
        }
        context = ToolService.refreshDynamicProviders(context, messages, invocationContext);
        return context;
    }

    private ToolServiceContext createContextFromStaticToolsAndProviders(InvocationContext invocationContext, UserMessage userMessage, List<ChatMessage> messages) {
        if (this.toolProviders.isEmpty()) {
            if (this.toolSpecifications.isEmpty()) {
                return ToolServiceContext.Empty.INSTANCE;
            }
            return ToolServiceContext.builder().effectiveTools(this.toolSpecifications).availableTools(this.toolSpecifications).toolExecutors(this.toolExecutors).returnBehaviors(this.returnBehaviors).build();
        }
        ArrayList<ToolSpecification> toolSpecifications = new ArrayList<ToolSpecification>(this.toolSpecifications);
        HashMap<String, ToolExecutor> toolExecutors = new HashMap<String, ToolExecutor>(this.toolExecutors);
        HashMap<String, ReturnBehavior> returnBehaviors = new HashMap<String, ReturnBehavior>(this.returnBehaviors);
        ArrayList<ToolProvider> dynamicToolProviders = new ArrayList<ToolProvider>();
        ToolProviderRequest toolProviderRequest = ToolProviderRequest.builder().invocationContext(invocationContext).userMessage(userMessage).messages(messages).build();
        this.toolProviders.forEach(toolProvider -> {
            if (toolProvider.isDynamic()) {
                dynamicToolProviders.add((ToolProvider)toolProvider);
                return;
            }
            ToolProviderResult toolProviderResult = toolProvider.provideTools(toolProviderRequest);
            if (toolProviderResult != null) {
                ToolService.addTools(toolProviderResult.aiServiceTools(), toolExecutors, toolSpecifications, returnBehaviors);
            }
        });
        return ToolServiceContext.builder().effectiveTools(toolSpecifications).availableTools(toolSpecifications).toolExecutors(toolExecutors).returnBehaviors(returnBehaviors).dynamicToolProviders(dynamicToolProviders).build();
    }

    private static void addTools(List<AiServiceTool> tools, Map<String, ToolExecutor> toolExecutors, List<ToolSpecification> toolSpecifications, Map<String, ReturnBehavior> returnBehaviors) {
        for (AiServiceTool tool : tools) {
            if (toolExecutors.putIfAbsent(tool.name(), tool.toolExecutor()) != null) {
                throw new IllegalConfigurationException("Duplicated definition for tool: " + tool.name());
            }
            toolSpecifications.add(tool.toolSpecification());
            returnBehaviors.put(tool.name(), tool.returnBehavior());
        }
    }

    public ToolServiceResult executeInferenceAndToolsLoop(AiServiceContext context, Object memoryId, ChatResponse chatResponse, ChatRequestParameters parameters, List<ChatMessage> messages, ChatMemory chatMemory, InvocationContext invocationContext, ToolServiceContext toolServiceContext, boolean isReturnTypeResult) {
        return this.executeInferenceAndToolsLoop(context, memoryId, chatResponse, parameters, messages, chatMemory, invocationContext, toolServiceContext, arg_0 -> ((ChatModel)context.chatModel).chat(arg_0));
    }

    public ToolServiceResult executeInferenceAndToolsLoop(AiServiceContext context, Object memoryId, ChatResponse chatResponse, ChatRequestParameters parameters, List<ChatMessage> messages, ChatMemory chatMemory, InvocationContext invocationContext, ToolServiceContext toolServiceContext, Function<ChatRequest, ChatResponse> chatModelInvoker) {
        TokenUsage aggregateTokenUsage = chatResponse.metadata().tokenUsage();
        ArrayList<ToolExecution> toolExecutions = new ArrayList<ToolExecution>();
        ArrayList<ChatResponse> intermediateResponses = new ArrayList<ChatResponse>();
        ArrayList<CompensableToolExecution> compensableExecutions = this.compensateOnToolErrors ? new ArrayList<CompensableToolExecution>() : null;
        int roundTripsLeft = this.maxToolCallingRoundTrips;
        while (true) {
            if (roundTripsLeft-- == 0) {
                throw Exceptions.runtime((String)"Something is wrong, exceeded %s tool calling round trips (maxToolCallingRoundTrips)", (Object[])new Object[]{this.maxToolCallingRoundTrips});
            }
            AiMessage aiMessage = chatResponse.aiMessage();
            if (chatMemory != null) {
                chatMemory.add((ChatMessage)aiMessage);
            } else {
                messages = new ArrayList<ChatMessage>(messages);
                messages.add((ChatMessage)aiMessage);
            }
            if (!aiMessage.hasToolExecutionRequests()) break;
            intermediateResponses.add(chatResponse);
            List<ToolExecutionRequest> toolExecutionRequests = aiMessage.toolExecutionRequests();
            Map<ToolExecutionRequest, ToolExecutionResult> toolResults = this.execute(toolExecutionRequests, toolServiceContext.toolExecutors(), invocationContext);
            boolean anyToolErrored = false;
            String failedToolName = null;
            ArrayList<ReturnBehavior> returnBehaviors = new ArrayList<ReturnBehavior>(toolExecutionRequests.size());
            ArrayList<ToolExecutionResultMessage> resultMessages = new ArrayList<ToolExecutionResultMessage>(toolExecutionRequests.size());
            for (ToolExecutionRequest request : toolExecutionRequests) {
                ToolExecutionResult result = toolResults.get(request);
                ToolExecutionResultMessage toolExecMsg = ToolService.toResultMessage(request, result);
                resultMessages.add(toolExecMsg);
                ToolExecution toolExecution = ToolExecution.builder().request(request).result(result).invocationContext(invocationContext).build();
                toolExecutions.add(toolExecution);
                this.fireToolExecutedEvent(invocationContext, request, toolExecution, context.eventListenerRegistrar);
                if (!result.isError() && this.compensateOnToolErrors && this.compensatingExecutors.containsKey(request.name())) {
                    compensableExecutions.add(new CompensableToolExecution(toolExecution, toolExecMsg));
                }
                if (result.isError() && failedToolName == null) {
                    failedToolName = request.name();
                }
                anyToolErrored = anyToolErrored || result.isError();
                returnBehaviors.add(toolServiceContext.returnBehavior(request.name()));
            }
            if (anyToolErrored && compensableExecutions != null && !compensableExecutions.isEmpty()) {
                this.compensateToolsActions(compensableExecutions, invocationContext);
                ToolService.rewriteChatMemoryForCompensatedTools(messages, chatMemory, compensableExecutions, failedToolName);
                compensableExecutions.clear();
                this.rewriteCurrentResults(toolExecutionRequests, toolResults, resultMessages, failedToolName);
            }
            for (ToolExecutionResultMessage resultMessage : resultMessages) {
                if (chatMemory != null) {
                    chatMemory.add((ChatMessage)resultMessage);
                    continue;
                }
                messages.add((ChatMessage)resultMessage);
            }
            if (ToolService.shouldReturnImmediately(anyToolErrored, returnBehaviors)) {
                ChatResponse finalResponse = (ChatResponse)intermediateResponses.remove(intermediateResponses.size() - 1);
                return ToolServiceResult.builder().intermediateResponses(intermediateResponses).finalResponse(finalResponse).toolExecutions(toolExecutions).aggregateTokenUsage(aggregateTokenUsage).immediateToolReturn(true).build();
            }
            if (chatMemory != null) {
                messages = chatMemory.messages();
                if (!context.storeRetrievedContentInChatMemory) {
                    messages = UserMessage.replaceLast((List)chatMemory.messages(), (UserMessage)invocationContext.userMessage());
                }
            }
            toolServiceContext = ToolService.refreshDynamicProviders(toolServiceContext, messages, invocationContext);
            if (this.toolSearchService != null) {
                toolServiceContext = ToolSearchService.addFoundTools(toolServiceContext, toolResults.values());
            }
            parameters = parameters.overrideWith(ChatRequestParameters.builder().toolSpecifications(toolServiceContext.effectiveTools()).build());
            ChatRequest chatRequest = context.chatRequestTransformer.apply(ChatRequest.builder().messages(messages).parameters(parameters).build(), memoryId);
            this.fireRequestIssuedEvent(chatRequest, invocationContext, context.eventListenerRegistrar);
            chatResponse = chatModelInvoker.apply(chatRequest);
            this.fireResponseReceivedEvent(chatRequest, chatResponse, invocationContext, context.eventListenerRegistrar);
            aggregateTokenUsage = TokenUsage.sum((TokenUsage)aggregateTokenUsage, (TokenUsage)chatResponse.metadata().tokenUsage());
        }
        return ToolServiceResult.builder().intermediateResponses(intermediateResponses).finalResponse(chatResponse).toolExecutions(toolExecutions).aggregateTokenUsage(aggregateTokenUsage).build();
    }

    private void rewriteCurrentResults(List<ToolExecutionRequest> toolExecutionRequests, Map<ToolExecutionRequest, ToolExecutionResult> toolResults, List<ToolExecutionResultMessage> resultMessages, String failedToolName) {
        for (int i = 0; i < toolExecutionRequests.size(); ++i) {
            ToolExecutionRequest request = toolExecutionRequests.get(i);
            if (toolResults.get(request).isError() || !this.compensatingExecutors.containsKey(request.name())) continue;
            resultMessages.set(i, ToolService.rolledBackResultMessage(resultMessages.get(i), failedToolName));
        }
    }

    private static void rewriteChatMemoryForCompensatedTools(List<ChatMessage> messages, ChatMemory chatMemory, List<CompensableToolExecution> compensableExecutions, String failedToolName) {
        List<ChatMessage> memoryMessages = chatMemory != null ? new ArrayList<ChatMessage>(chatMemory.messages()) : messages;
        block0: for (CompensableToolExecution entry : compensableExecutions) {
            ToolExecutionResultMessage originalMsg = entry.resultMessage();
            ToolExecutionResultMessage replacementMsg = ToolService.rolledBackResultMessage(originalMsg, failedToolName);
            for (int j = 0; j < memoryMessages.size(); ++j) {
                ToolExecutionResultMessage msg;
                if (!(memoryMessages.get(j) instanceof ToolExecutionResultMessage) || !(msg = (ToolExecutionResultMessage)memoryMessages.get(j)).id().equals(originalMsg.id())) continue;
                memoryMessages.set(j, replacementMsg);
                continue block0;
            }
        }
        if (chatMemory != null) {
            chatMemory.set(new ArrayList<ChatMessage>(memoryMessages));
        }
    }

    private static ToolExecutionResultMessage rolledBackResultMessage(ToolExecutionResultMessage original, String failedToolName) {
        String rolledBackText = "Tool '" + original.toolName() + "' was executed successfully but was rolled back due to failure of tool '" + failedToolName + "'";
        return original.toBuilder().contents(Collections.singletonList(TextContent.from((String)rolledBackText))).isError(Boolean.valueOf(true)).build();
    }

    private void compensateToolsActions(List<CompensableToolExecution> compensableExecutions, InvocationContext invocationContext) {
        for (int i = compensableExecutions.size() - 1; i >= 0; --i) {
            ToolExecution toolExecution = compensableExecutions.get(i).toolExecution();
            String toolName = toolExecution.request().name();
            BiConsumer<ToolExecution, InvocationContext> compensatingAction = this.compensatingExecutors.get(toolName);
            try {
                compensatingAction.accept(toolExecution, invocationContext);
                continue;
            }
            catch (Exception e) {
                log.warn("Compensating action failed for tool '{}': {}", new Object[]{toolName, e.getMessage(), e});
            }
        }
    }

    public static boolean shouldReturnImmediately(boolean anyToolErrored, List<ReturnBehavior> returnBehaviors) {
        if (anyToolErrored) {
            return false;
        }
        if (returnBehaviors.isEmpty()) {
            return false;
        }
        if (returnBehaviors.get(returnBehaviors.size() - 1) == ReturnBehavior.IMMEDIATE_IF_LAST) {
            return true;
        }
        return returnBehaviors.stream().allMatch(rb -> rb == ReturnBehavior.IMMEDIATE || rb == ReturnBehavior.IMMEDIATE_IF_LAST);
    }

    public static ToolServiceContext refreshDynamicProviders(ToolServiceContext toolServiceContext, List<ChatMessage> messages, InvocationContext invocationContext) {
        if (toolServiceContext == null) {
            return null;
        }
        List<ToolProvider> dynamicProviders = toolServiceContext.dynamicToolProviders();
        if (dynamicProviders.isEmpty()) {
            return toolServiceContext;
        }
        UserMessage userMessage = (UserMessage)UserMessage.findLast(messages).get();
        ToolProviderRequest request = ToolProviderRequest.builder().invocationContext(invocationContext).userMessage(userMessage).messages(messages).build();
        ArrayList<ToolSpecification> newEffectiveTools = new ArrayList<ToolSpecification>(toolServiceContext.effectiveTools());
        ArrayList<ToolSpecification> newAvailableTools = new ArrayList<ToolSpecification>(toolServiceContext.availableTools());
        HashMap<String, ToolExecutor> newToolExecutors = new HashMap<String, ToolExecutor>(toolServiceContext.toolExecutors());
        HashMap<String, ReturnBehavior> newReturnBehaviors = new HashMap<String, ReturnBehavior>(toolServiceContext.returnBehaviors());
        boolean changed = false;
        for (ToolProvider dynamicProvider : dynamicProviders) {
            ToolProviderResult result = dynamicProvider.provideTools(request);
            if (result == null) continue;
            for (AiServiceTool tool : result.aiServiceTools()) {
                if (newToolExecutors.containsKey(tool.name())) continue;
                newEffectiveTools.add(tool.toolSpecification());
                newAvailableTools.add(tool.toolSpecification());
                newToolExecutors.put(tool.name(), tool.toolExecutor());
                newReturnBehaviors.put(tool.name(), tool.returnBehavior());
                changed = true;
            }
        }
        if (!changed) {
            return toolServiceContext;
        }
        return toolServiceContext.toBuilder().effectiveTools(newEffectiveTools).availableTools(newAvailableTools).toolExecutors(newToolExecutors).returnBehaviors(newReturnBehaviors).build();
    }

    private void fireToolExecutedEvent(InvocationContext invocationContext, ToolExecutionRequest request, ToolExecution toolExecution, AiServiceListenerRegistrar listenerRegistrar) {
        listenerRegistrar.fireEvent((AiServiceEvent)ToolExecutedEvent.builder().invocationContext(invocationContext).request(request).resultContents(toolExecution.resultContents()).build());
    }

    private void fireRequestIssuedEvent(ChatRequest chatRequest, InvocationContext invocationContext, AiServiceListenerRegistrar listenerRegistrar) {
        listenerRegistrar.fireEvent((AiServiceEvent)AiServiceRequestIssuedEvent.builder().invocationContext(invocationContext).request(chatRequest).build());
    }

    private void fireResponseReceivedEvent(ChatRequest chatRequest, ChatResponse chatResponse, InvocationContext invocationContext, AiServiceListenerRegistrar listenerRegistrar) {
        listenerRegistrar.fireEvent((AiServiceEvent)AiServiceResponseReceivedEvent.builder().invocationContext(invocationContext).request(chatRequest).response(chatResponse).build());
    }

    private Map<ToolExecutionRequest, ToolExecutionResult> execute(List<ToolExecutionRequest> toolRequests, Map<String, ToolExecutor> toolExecutors, InvocationContext invocationContext) {
        if (this.executor != null && toolRequests.size() > 1) {
            return this.executeConcurrently(toolRequests, toolExecutors, invocationContext);
        }
        return this.executeSequentially(toolRequests, toolExecutors, invocationContext);
    }

    private Map<ToolExecutionRequest, ToolExecutionResult> executeConcurrently(List<ToolExecutionRequest> toolRequests, Map<String, ToolExecutor> toolExecutors, InvocationContext invocationContext) {
        LinkedHashMap<ToolExecutionRequest, CompletableFuture<ToolExecutionResult>> futures = new LinkedHashMap<ToolExecutionRequest, CompletableFuture<ToolExecutionResult>>();
        for (ToolExecutionRequest toolRequest : toolRequests) {
            CompletableFuture<ToolExecutionResult> future = CompletableFuture.supplyAsync(() -> this.executeTool(invocationContext, toolExecutors, toolRequest), this.executor);
            futures.put(toolRequest, future);
        }
        LinkedHashMap<ToolExecutionRequest, ToolExecutionResult> results = new LinkedHashMap<ToolExecutionRequest, ToolExecutionResult>();
        for (Map.Entry entry : futures.entrySet()) {
            try {
                results.put((ToolExecutionRequest)entry.getKey(), (ToolExecutionResult)((CompletableFuture)entry.getValue()).get());
            }
            catch (ExecutionException e) {
                if (e.getCause() instanceof RuntimeException) {
                    RuntimeException re = (RuntimeException)e.getCause();
                    throw re;
                }
                throw new RuntimeException(e.getCause());
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        return results;
    }

    private Map<ToolExecutionRequest, ToolExecutionResult> executeSequentially(List<ToolExecutionRequest> toolRequests, Map<String, ToolExecutor> toolExecutors, InvocationContext invocationContext) {
        LinkedHashMap<ToolExecutionRequest, ToolExecutionResult> toolResults = new LinkedHashMap<ToolExecutionRequest, ToolExecutionResult>();
        for (ToolExecutionRequest toolRequest : toolRequests) {
            toolResults.put(toolRequest, this.executeTool(invocationContext, toolExecutors, toolRequest));
        }
        return toolResults;
    }

    private ToolExecutionResult executeTool(InvocationContext invocationContext, Map<String, ToolExecutor> toolExecutors, ToolExecutionRequest toolRequest) {
        return this.internalExecuteTool(invocationContext, toolExecutors, toolRequest, this.beforeToolExecution, this.afterToolExecution);
    }

    public ToolExecutionResult executeTool(InvocationContext invocationContext, Map<String, ToolExecutor> toolExecutors, ToolExecutionRequest toolRequest, Consumer<BeforeToolExecution> externalBeforeToolExecution, Consumer<ToolExecution> externalAfterToolExecution) {
        return this.internalExecuteTool(invocationContext, toolExecutors, toolRequest, ToolService.nullSafeCombineConsumers(this.beforeToolExecution, externalBeforeToolExecution), ToolService.nullSafeCombineConsumers(this.afterToolExecution, externalAfterToolExecution));
    }

    private static <T> Consumer<T> nullSafeCombineConsumers(Consumer<T> first, Consumer<T> second) {
        if (first != null && second != null) {
            return first.andThen(second);
        }
        return first != null ? first : second;
    }

    private ToolExecutionResult internalExecuteTool(InvocationContext invocationContext, Map<String, ToolExecutor> toolExecutors, ToolExecutionRequest toolRequest, Consumer<BeforeToolExecution> beforeToolExecution, Consumer<ToolExecution> afterToolExecution) {
        ToolExecutionResult toolResult;
        if (beforeToolExecution != null) {
            beforeToolExecution.accept(BeforeToolExecution.builder().request(toolRequest).invocationContext(invocationContext).build());
        }
        LocalDateTime startTime = LocalDateTime.now();
        ToolExecutor executor = toolExecutors.get(toolRequest.name());
        ToolExecutionResult toolExecutionResult = toolResult = executor == null ? this.applyToolHallucinationStrategy(toolRequest) : ToolService.executeWithErrorHandling(toolRequest, executor, invocationContext, this.argumentsErrorHandler(), this.executionErrorHandler());
        if (afterToolExecution != null) {
            afterToolExecution.accept(ToolExecution.builder().request(toolRequest).result(toolResult).startTime(startTime).finishTime(LocalDateTime.now()).invocationContext(invocationContext).build());
        }
        return toolResult;
    }

    public static ToolExecutionResult executeWithErrorHandling(ToolExecutionRequest toolRequest, ToolExecutor toolExecutor, InvocationContext invocationContext, ToolArgumentsErrorHandler argumentsErrorHandler, ToolExecutionErrorHandler executionErrorHandler) {
        try {
            return toolExecutor.executeWithContext(toolRequest, invocationContext);
        }
        catch (Exception e) {
            ToolErrorContext errorContext = ToolErrorContext.builder().toolExecutionRequest(toolRequest).invocationContext(invocationContext).rawError(e).build();
            ToolErrorHandlerResult errorHandlerResult = e instanceof ToolArgumentsException ? argumentsErrorHandler.handle(ToolService.getCause(e), errorContext) : executionErrorHandler.handle(ToolService.getCause(e), errorContext);
            return ToolExecutionResult.builder().isError(true).resultText(errorHandlerResult.text()).build();
        }
    }

    static ToolExecutionResultMessage toResultMessage(ToolExecutionRequest request, ToolExecutionResult result) {
        return ToolExecutionResultMessage.builder().id(request.id()).toolName(request.name()).contents(result.resultContents()).isError(Boolean.valueOf(result.isError())).attributes(result.attributes()).build();
    }

    private static Throwable getCause(Exception e) {
        Throwable cause = e.getCause();
        return cause != null ? cause : e;
    }

    private static String errorMessage(Throwable throwable) {
        if (throwable == null) {
            return "unknown error";
        }
        String message = throwable.getMessage();
        return Utils.isNullOrBlank((String)message) ? throwable.getClass().getName() : message;
    }

    public ToolExecutionResult applyToolHallucinationStrategy(ToolExecutionRequest toolRequest) {
        ToolExecutionResultMessage toolResultMessage = this.toolHallucinationStrategy.apply(toolRequest);
        return ToolExecutionResult.builder().resultText(toolResultMessage.text()).build();
    }

    public List<ToolSpecification> toolSpecifications() {
        return this.toolSpecifications;
    }

    public Map<String, ToolExecutor> toolExecutors() {
        return this.toolExecutors;
    }

    public Executor executor() {
        return this.executor;
    }

    public Set<ToolProvider> toolProviders() {
        return Utils.copy(this.toolProviders);
    }

    @Deprecated
    public ToolProvider toolProvider() {
        if (this.toolProviders.size() == 1) {
            return this.toolProviders.iterator().next();
        }
        if (this.toolProviders.isEmpty()) {
            return null;
        }
        throw new IllegalStateException("There are multiple ToolProvider configured, use toolProviders() instead");
    }

    public ReturnBehavior returnBehavior(String toolName) {
        return this.returnBehaviors.getOrDefault(toolName, ReturnBehavior.TO_LLM);
    }

    @Deprecated
    public boolean isImmediateTool(String toolName) {
        return this.returnBehaviors.get(toolName) == ReturnBehavior.IMMEDIATE;
    }

    private static final class CompensableToolExecution {
        final ToolExecution toolExecution;
        final ToolExecutionResultMessage resultMessage;

        CompensableToolExecution(ToolExecution toolExecution, ToolExecutionResultMessage resultMessage) {
            this.toolExecution = toolExecution;
            this.resultMessage = resultMessage;
        }

        ToolExecution toolExecution() {
            return this.toolExecution;
        }

        ToolExecutionResultMessage resultMessage() {
            return this.resultMessage;
        }
    }
}

