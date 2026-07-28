/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.UntypedAgent
 *  dev.langchain4j.agentic.internal.A2AClientBuilder
 *  dev.langchain4j.agentic.internal.InternalAgent
 *  dev.langchain4j.agentic.observability.AgentListener
 *  dev.langchain4j.agentic.observability.ComposedAgentListener
 *  dev.langchain4j.agentic.planner.AgentArgument
 *  dev.langchain4j.agentic.planner.AgentInstance
 *  dev.langchain4j.agentic.planner.AgenticSystemTopology
 *  dev.langchain4j.agentic.planner.Planner
 *  dev.langchain4j.agentic.scope.AgenticScope
 *  dev.langchain4j.agentic.scope.DefaultAgenticScope
 *  dev.langchain4j.agentic.scope.ResultWithAgenticScope
 *  dev.langchain4j.invocation.LangChain4jManaged
 *  dev.langchain4j.service.ParameterNameResolver
 *  dev.langchain4j.service.output.ServiceOutputParser
 *  org.a2aproject.sdk.A2A
 *  org.a2aproject.sdk.client.Client
 *  org.a2aproject.sdk.client.ClientBuilder
 *  org.a2aproject.sdk.client.MessageEvent
 *  org.a2aproject.sdk.client.TaskEvent
 *  org.a2aproject.sdk.client.TaskUpdateEvent
 *  org.a2aproject.sdk.client.transport.jsonrpc.JSONRPCTransport
 *  org.a2aproject.sdk.client.transport.jsonrpc.JSONRPCTransportConfigBuilder
 *  org.a2aproject.sdk.client.transport.spi.ClientTransportConfigBuilder
 *  org.a2aproject.sdk.spec.A2AClientError
 *  org.a2aproject.sdk.spec.A2AClientException
 *  org.a2aproject.sdk.spec.AgentCard
 *  org.a2aproject.sdk.spec.Message
 *  org.a2aproject.sdk.spec.Message$Builder
 *  org.a2aproject.sdk.spec.Message$Role
 *  org.a2aproject.sdk.spec.Part
 *  org.a2aproject.sdk.spec.Task
 *  org.a2aproject.sdk.spec.TaskState
 *  org.a2aproject.sdk.spec.TextPart
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.agentic.a2a;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.a2a.A2AClientInstance;
import dev.langchain4j.agentic.a2a.A2AContextId;
import dev.langchain4j.agentic.a2a.A2ATaskId;
import dev.langchain4j.agentic.internal.A2AClientBuilder;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.ComposedAgentListener;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.invocation.LangChain4jManaged;
import dev.langchain4j.service.ParameterNameResolver;
import dev.langchain4j.service.output.ServiceOutputParser;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.a2aproject.sdk.A2A;
import org.a2aproject.sdk.client.Client;
import org.a2aproject.sdk.client.ClientBuilder;
import org.a2aproject.sdk.client.MessageEvent;
import org.a2aproject.sdk.client.TaskEvent;
import org.a2aproject.sdk.client.TaskUpdateEvent;
import org.a2aproject.sdk.client.transport.jsonrpc.JSONRPCTransport;
import org.a2aproject.sdk.client.transport.jsonrpc.JSONRPCTransportConfigBuilder;
import org.a2aproject.sdk.client.transport.spi.ClientTransportConfigBuilder;
import org.a2aproject.sdk.spec.A2AClientError;
import org.a2aproject.sdk.spec.A2AClientException;
import org.a2aproject.sdk.spec.AgentCard;
import org.a2aproject.sdk.spec.Message;
import org.a2aproject.sdk.spec.Part;
import org.a2aproject.sdk.spec.Task;
import org.a2aproject.sdk.spec.TaskState;
import org.a2aproject.sdk.spec.TextPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultA2AClientBuilder<T>
implements A2AClientBuilder<T>,
InternalAgent,
InvocationHandler {
    private final ServiceOutputParser serviceOutputParser = new ServiceOutputParser();
    private final Class<T> agentServiceClass;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultA2AClientBuilder.class);
    private final AgentCard agentCard;
    private Client a2aClient;
    private Consumer<ClientBuilder> clientCustomizer;
    private final String name;
    private String agentId;
    private InternalAgent parent;
    private String[] inputKeys;
    private String outputKey;
    private boolean async;
    private AgentListener agentListener;

    DefaultA2AClientBuilder(String a2aServerUrl, Class<T> agentServiceClass) {
        this.agentCard = DefaultA2AClientBuilder.agentCard(a2aServerUrl);
        this.agentId = this.name = this.agentCard.name();
        this.agentServiceClass = agentServiceClass;
    }

    private Client buildClient() {
        try {
            ClientBuilder cb = Client.builder((AgentCard)this.agentCard);
            if (this.clientCustomizer != null) {
                this.clientCustomizer.accept(cb);
            } else {
                cb.withTransport(JSONRPCTransport.class, (ClientTransportConfigBuilder)new JSONRPCTransportConfigBuilder());
            }
            return cb.build();
        }
        catch (A2AClientException e) {
            throw new RuntimeException(e);
        }
    }

    private static AgentCard agentCard(String a2aServerUrl) {
        try {
            return A2A.getAgentCard((String)a2aServerUrl);
        }
        catch (A2AClientError e) {
            throw new RuntimeException(e);
        }
    }

    public T build() {
        if (this.agentServiceClass == UntypedAgent.class && this.inputKeys == null) {
            throw new IllegalArgumentException("Input names must be provided for UntypedAgent.");
        }
        this.a2aClient = this.buildClient();
        Object agent = Proxy.newProxyInstance(this.agentServiceClass.getClassLoader(), new Class[]{this.agentServiceClass, A2AClientInstance.class}, this);
        return (T)agent;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        if (method.getDeclaringClass() == AgentInstance.class || method.getDeclaringClass() == InternalAgent.class) {
            return method.invoke(Proxy.getInvocationHandler(proxy), args);
        }
        if (method.getDeclaringClass() == A2AClientInstance.class) {
            return switch (method.getName()) {
                case "agentCard" -> this.agentCard;
                case "inputKeys" -> this.inputKeys;
                default -> throw new UnsupportedOperationException("Unknown method on A2AClientInstance class : " + method.getName());
            };
        }
        boolean wrapWithScope = method.getReturnType() == ResultWithAgenticScope.class;
        Type returnType = wrapWithScope ? DefaultA2AClientBuilder.unwrapResultType(method.getGenericReturnType()) : DefaultA2AClientBuilder.getReturnType(method);
        A2AInvocationResult result = this.invokeAgent(method, returnType, args);
        AgenticScope scope = (AgenticScope)LangChain4jManaged.current(AgenticScope.class);
        if (scope == null) {
            scope = DefaultAgenticScope.ephemeralAgenticScope();
            if (this.outputKey != null && result.parsedResult != null) {
                scope.writeState(this.outputKey, result.parsedResult);
            }
        }
        if (result.contextIdKey != null && result.contextId != null) {
            scope.writeState(result.contextIdKey, (Object)result.contextId);
        }
        if (result.taskIdKey != null && result.taskId != null) {
            scope.writeState(result.taskIdKey, (Object)result.taskId);
        }
        return method.getReturnType() == ResultWithAgenticScope.class ? new ResultWithAgenticScope(scope, result.parsedResult) : result.parsedResult;
    }

    private static Type getReturnType(Method method) {
        Type type = method.getGenericReturnType();
        return type == Object.class ? String.class : type;
    }

    private static Type unwrapResultType(Type type) {
        ParameterizedType pt;
        if (type instanceof ParameterizedType && (pt = (ParameterizedType)type).getRawType() == ResultWithAgenticScope.class) {
            Type inner = pt.getActualTypeArguments()[0];
            return inner == Object.class ? String.class : inner;
        }
        return String.class;
    }

    private A2AInvocationResult invokeAgent(Method method, Type returnType, Object[] args) throws A2AClientException {
        ArrayList<TextPart> parts = new ArrayList<TextPart>();
        String contextId = null;
        String taskId = null;
        String contextIdKey = null;
        String taskIdKey = null;
        if (this.agentServiceClass == UntypedAgent.class) {
            Map params = (Map)args[0];
            for (String inputKey : this.inputKeys) {
                parts.add(new TextPart(params.get(inputKey).toString()));
            }
        } else {
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < args.length; ++i) {
                if (parameters[i].getAnnotation(A2AContextId.class) != null) {
                    String string = contextId = args[i] != null ? args[i].toString() : null;
                    if (!ParameterNameResolver.hasName((Parameter)parameters[i])) continue;
                    contextIdKey = ParameterNameResolver.name((Parameter)parameters[i]);
                    continue;
                }
                if (parameters[i].getAnnotation(A2ATaskId.class) != null) {
                    String string = taskId = args[i] != null ? args[i].toString() : null;
                    if (!ParameterNameResolver.hasName((Parameter)parameters[i])) continue;
                    taskIdKey = ParameterNameResolver.name((Parameter)parameters[i]);
                    continue;
                }
                parts.add(new TextPart(args[i].toString()));
            }
        }
        Message.Builder messageBuilder = Message.builder().role(Message.Role.ROLE_USER).parts(parts);
        if (contextId != null) {
            messageBuilder.contextId(contextId);
        }
        if (taskId != null) {
            messageBuilder.taskId(taskId);
        }
        Message message = messageBuilder.build();
        CompletableFuture messageResponse = new CompletableFuture();
        AtomicReference responseContextId = new AtomicReference();
        AtomicReference responseTaskId = new AtomicReference();
        List consumers = List.of((event, card) -> {
            if (event instanceof MessageEvent) {
                MessageEvent messageEvent = (MessageEvent)event;
                Message msg = messageEvent.getMessage();
                responseContextId.set(msg.contextId());
                responseTaskId.set(msg.taskId());
                messageResponse.complete(DefaultA2AClientBuilder.extractTextFromParts(msg.parts()));
            } else if (event instanceof TaskEvent) {
                TaskEvent taskEvent = (TaskEvent)event;
                DefaultA2AClientBuilder.captureTaskIds(taskEvent.getTask(), responseContextId, responseTaskId);
                DefaultA2AClientBuilder.completeFromTask(taskEvent.getTask(), messageResponse);
            } else if (event instanceof TaskUpdateEvent) {
                TaskUpdateEvent updateEvent = (TaskUpdateEvent)event;
                DefaultA2AClientBuilder.captureTaskIds(updateEvent.getTask(), responseContextId, responseTaskId);
                DefaultA2AClientBuilder.completeFromTask(updateEvent.getTask(), messageResponse);
            } else {
                messageResponse.completeExceptionally(new IllegalArgumentException("The event expected should be of type " + event.getClass()));
            }
        });
        Consumer<Throwable> streamingErrorHandler = error -> {
            if (messageResponse.isDone()) {
                LOG.debug("SSE stream closed after response received: {}", (Object)error.getMessage());
            } else {
                LOG.error("Streaming error occurred: {}", (Object)error.getMessage(), error);
                messageResponse.completeExceptionally((Throwable)error);
            }
        };
        this.a2aClient.sendMessage(message, consumers, streamingErrorHandler, null);
        String finalContextIdKey = contextIdKey;
        String finalTaskIdKey = taskIdKey;
        try {
            String responseText = (String)messageResponse.get();
            LOG.debug("Response: {}", (Object)responseText);
            Object parsedResult = this.serviceOutputParser.parseText(returnType, responseText);
            return new A2AInvocationResult(parsedResult, finalContextIdKey, (String)responseContextId.get(), finalTaskIdKey, (String)responseTaskId.get());
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.error("Failed to get response: {}", (Object)e.getMessage(), (Object)e);
            throw new RuntimeException("Failed to get response: " + e.getMessage(), e);
        }
        catch (ExecutionException e) {
            LOG.error("Failed to get response: {}", (Object)e.getMessage(), (Object)e);
            throw new RuntimeException("Failed to get response: " + e.getMessage(), e);
        }
    }

    private static void captureTaskIds(Task task, AtomicReference<String> contextId, AtomicReference<String> taskId) {
        contextId.set(task.contextId());
        taskId.set(task.id());
    }

    static void completeFromTask(Task task, CompletableFuture<String> messageResponse) {
        TaskState state = task.status().state();
        if (!DefaultA2AClientBuilder.isTerminalState(state) && task.artifacts().isEmpty()) {
            return;
        }
        if (DefaultA2AClientBuilder.isFailureState(state)) {
            Message statusMessage = task.status().message();
            String reason = statusMessage != null ? DefaultA2AClientBuilder.extractTextFromParts(statusMessage.parts()) : "";
            messageResponse.completeExceptionally(new RuntimeException("A2A task " + task.id() + " ended in terminal state " + state + (String)(reason.isEmpty() ? "" : ": " + reason)));
            return;
        }
        messageResponse.complete(DefaultA2AClientBuilder.extractTextFromParts(task.artifacts().stream().flatMap(a -> a.parts().stream()).toList()));
    }

    private static boolean isFailureState(TaskState state) {
        return state == TaskState.TASK_STATE_FAILED || state == TaskState.TASK_STATE_CANCELED || state == TaskState.TASK_STATE_REJECTED;
    }

    private static boolean isTerminalState(TaskState state) {
        return state == TaskState.TASK_STATE_COMPLETED || state == TaskState.TASK_STATE_FAILED || state == TaskState.TASK_STATE_CANCELED || state == TaskState.TASK_STATE_REJECTED;
    }

    private static String extractTextFromParts(List<Part<?>> parts) {
        return parts.stream().filter(TextPart.class::isInstance).map(TextPart.class::cast).map(TextPart::text).collect(Collectors.joining("\n"));
    }

    public DefaultA2AClientBuilder<T> inputKeys(String ... inputKeys) {
        this.inputKeys = inputKeys;
        return this;
    }

    public DefaultA2AClientBuilder<T> outputKey(String outputKey) {
        this.outputKey = outputKey;
        return this;
    }

    public DefaultA2AClientBuilder<T> async(boolean async) {
        this.async = async;
        return this;
    }

    public DefaultA2AClientBuilder<T> listener(AgentListener agentListener) {
        this.agentListener = agentListener;
        return this;
    }

    public DefaultA2AClientBuilder<T> clientCustomizer(Consumer<?> clientCustomizer) {
        if (clientCustomizer != null) {
            this.clientCustomizer = clientCustomizer;
        }
        return this;
    }

    public void setParent(InternalAgent parent) {
        this.parent = parent;
    }

    public void registerInheritedParentListener(AgentListener parentListener) {
        if (parentListener != null && parentListener.inheritedBySubagents()) {
            this.agentListener = ComposedAgentListener.composeWithInherited((AgentListener)this.listener(), (AgentListener)parentListener);
        }
    }

    public void appendId(String idSuffix) {
        this.agentId = this.agentId + idSuffix;
    }

    public AgentListener listener() {
        return this.agentListener;
    }

    public Class<?> type() {
        return this.agentServiceClass;
    }

    public Class<? extends Planner> plannerType() {
        return null;
    }

    public String name() {
        return this.name;
    }

    public String agentId() {
        return this.agentId;
    }

    public String description() {
        return this.agentCard.description();
    }

    public Type outputType() {
        return Object.class;
    }

    public String outputKey() {
        return this.outputKey;
    }

    public boolean async() {
        return this.async;
    }

    public List<AgentArgument> arguments() {
        return List.of();
    }

    public AgentInstance parent() {
        return this.parent;
    }

    public List<AgentInstance> subagents() {
        return List.of();
    }

    public AgenticSystemTopology topology() {
        return AgenticSystemTopology.AI_AGENT;
    }

    private record A2AInvocationResult(Object parsedResult, String contextIdKey, String contextId, String taskIdKey, String taskId) {
    }
}

