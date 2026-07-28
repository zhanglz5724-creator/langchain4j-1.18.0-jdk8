/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.DeserializationFeature
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.node.ArrayNode
 *  com.fasterxml.jackson.databind.node.JsonNodeFactory
 *  com.fasterxml.jackson.databind.node.ObjectNode
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.exception.ToolArgumentsException
 *  dev.langchain4j.exception.ToolExecutionException
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.service.tool.ToolExecutionResult
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.exception.ToolArgumentsException;
import dev.langchain4j.exception.ToolExecutionException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.mcp.client.DefaultMcpToolResultExtractor;
import dev.langchain4j.mcp.client.McpCallContext;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.McpClientListener;
import dev.langchain4j.mcp.client.McpException;
import dev.langchain4j.mcp.client.McpGetPromptResult;
import dev.langchain4j.mcp.client.McpMetaSupplier;
import dev.langchain4j.mcp.client.McpPrompt;
import dev.langchain4j.mcp.client.McpReadResourceResult;
import dev.langchain4j.mcp.client.McpResource;
import dev.langchain4j.mcp.client.McpResourceTemplate;
import dev.langchain4j.mcp.client.McpRoot;
import dev.langchain4j.mcp.client.McpToolResultExtractor;
import dev.langchain4j.mcp.client.PromptsHelper;
import dev.langchain4j.mcp.client.ResourcesHelper;
import dev.langchain4j.mcp.client.ToolExecutionHelper;
import dev.langchain4j.mcp.client.ToolSpecificationHelper;
import dev.langchain4j.mcp.client.logging.DefaultMcpLogMessageHandler;
import dev.langchain4j.mcp.client.logging.McpLogMessage;
import dev.langchain4j.mcp.client.logging.McpLogMessageHandler;
import dev.langchain4j.mcp.client.progress.McpProgressHandler;
import dev.langchain4j.mcp.client.transport.McpOperationHandler;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.protocol.McpCallToolRequest;
import dev.langchain4j.mcp.protocol.McpCancellationNotification;
import dev.langchain4j.mcp.protocol.McpClientMessage;
import dev.langchain4j.mcp.protocol.McpClientNotification;
import dev.langchain4j.mcp.protocol.McpClientParams;
import dev.langchain4j.mcp.protocol.McpClientRequest;
import dev.langchain4j.mcp.protocol.McpGetPromptRequest;
import dev.langchain4j.mcp.protocol.McpImplementation;
import dev.langchain4j.mcp.protocol.McpInitializeParams;
import dev.langchain4j.mcp.protocol.McpInitializeRequest;
import dev.langchain4j.mcp.protocol.McpInitializeResult;
import dev.langchain4j.mcp.protocol.McpListPromptsRequest;
import dev.langchain4j.mcp.protocol.McpListResourceTemplatesRequest;
import dev.langchain4j.mcp.protocol.McpListResourcesRequest;
import dev.langchain4j.mcp.protocol.McpListToolsRequest;
import dev.langchain4j.mcp.protocol.McpPingRequest;
import dev.langchain4j.mcp.protocol.McpReadResourceRequest;
import dev.langchain4j.mcp.protocol.McpRootsListChangedNotification;
import dev.langchain4j.mcp.protocol.McpSubscribeResourceRequest;
import dev.langchain4j.mcp.protocol.McpUnsubscribeResourceRequest;
import dev.langchain4j.service.tool.ToolExecutionResult;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMcpClient
implements McpClient {
    private static final Logger log = LoggerFactory.getLogger(DefaultMcpClient.class);
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    private final AtomicLong idGenerator = new AtomicLong(0L);
    private final McpTransport transport;
    private final String key;
    private final String clientName;
    private final String clientVersion;
    private final String protocolVersion;
    private final Duration initializationTimeout;
    private final Duration toolExecutionTimeout;
    private final Duration resourcesTimeout;
    private final Duration promptsTimeout;
    private final Duration pingTimeout;
    private final JsonNode RESULT_TIMEOUT;
    private final String toolExecutionTimeoutErrorMessage;
    private final Map<Long, CompletableFuture<JsonNode>> pendingOperations = new ConcurrentHashMap<Long, CompletableFuture<JsonNode>>();
    private final McpOperationHandler messageHandler;
    private final McpLogMessageHandler logHandler;
    private final McpProgressHandler progressHandler;
    private final AtomicReference<List<McpResource>> resourceRefs = new AtomicReference();
    private final AtomicReference<List<McpResourceTemplate>> resourceTemplateRefs = new AtomicReference();
    private final AtomicReference<List<McpPrompt>> promptRefs = new AtomicReference();
    private final AtomicReference<List<ToolSpecification>> toolListRefs = new AtomicReference();
    private final AtomicReference<CompletableFuture<List<ToolSpecification>>> toolListUpdateInProgress = new AtomicReference<Object>(null);
    private final AtomicReference<CompletableFuture<List<McpResource>>> resourceListUpdateInProgress = new AtomicReference<Object>(null);
    private final AtomicReference<CompletableFuture<List<McpResourceTemplate>>> resourceTemplateListUpdateInProgress = new AtomicReference<Object>(null);
    private final AtomicReference<CompletableFuture<List<McpPrompt>>> promptListUpdateInProgress = new AtomicReference<Object>(null);
    private final BiConsumer<McpClient, String> onResourceUpdated;
    private final Duration reconnectInterval;
    private volatile boolean closed = false;
    private final Boolean autoHealthCheck;
    private final Duration autoHealthCheckInterval;
    private final ScheduledExecutorService healthCheckScheduler;
    private final ReentrantLock initializationLock = new ReentrantLock();
    private final AtomicReference<List<McpRoot>> mcpRoots;
    private final Boolean cacheToolList;
    private final Boolean cacheResourceList;
    private final Boolean cachePromptList;
    private final List<McpClientListener> listeners;
    private final McpMetaSupplier metaSupplier;
    private final McpToolResultExtractor toolResultExtractor;
    private volatile @Nullable McpInitializeResult initializeResult;

    public DefaultMcpClient(Builder builder) {
        try {
            this.transport = (McpTransport)ValidationUtils.ensureNotNull((Object)builder.transport, (String)"transport");
            this.key = (String)Utils.getOrDefault((Object)builder.key, () -> UUID.randomUUID().toString());
            this.clientName = (String)Utils.getOrDefault((Object)builder.clientName, (Object)"langchain4j");
            this.clientVersion = (String)Utils.getOrDefault((Object)builder.clientVersion, (Object)"1.0");
            this.protocolVersion = (String)Utils.getOrDefault((Object)builder.protocolVersion, (Object)"2025-11-25");
            this.initializationTimeout = (Duration)Utils.getOrDefault((Object)builder.initializationTimeout, (Object)Duration.ofSeconds(30L));
            this.toolExecutionTimeout = (Duration)Utils.getOrDefault((Object)builder.toolExecutionTimeout, (Object)Duration.ofSeconds(60L));
            this.resourcesTimeout = (Duration)Utils.getOrDefault((Object)builder.resourcesTimeout, (Object)Duration.ofSeconds(60L));
            this.promptsTimeout = (Duration)Utils.getOrDefault((Object)builder.promptsTimeout, (Object)Duration.ofSeconds(60L));
            this.logHandler = (McpLogMessageHandler)Utils.getOrDefault((Object)builder.logHandler, (Object)new DefaultMcpLogMessageHandler());
            this.progressHandler = builder.progressHandler;
            this.pingTimeout = (Duration)Utils.getOrDefault((Object)builder.pingTimeout, (Object)Duration.ofSeconds(10L));
            this.reconnectInterval = (Duration)Utils.getOrDefault((Object)builder.reconnectInterval, (Object)Duration.ofSeconds(5L));
            this.autoHealthCheck = (Boolean)Utils.getOrDefault((Object)builder.autoHealthCheck, (Object)Boolean.TRUE);
            this.autoHealthCheckInterval = (Duration)Utils.getOrDefault((Object)builder.autoHealthCheckInterval, (Object)Duration.ofSeconds(30L));
            this.listeners = Collections.unmodifiableList(new ArrayList(builder.listeners));
            this.metaSupplier = builder.metaSupplier;
            this.healthCheckScheduler = this.autoHealthCheck != false ? Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "mcp-server-health-checker");
                t.setDaemon(true);
                return t;
            }) : null;
            this.toolExecutionTimeoutErrorMessage = (String)Utils.getOrDefault((Object)builder.toolExecutionTimeoutErrorMessage, (Object)"There was a timeout executing the tool");
            this.mcpRoots = new AtomicReference<List>(Utils.getOrDefault((List)builder.roots, new ArrayList()));
            this.cacheToolList = (Boolean)Utils.getOrDefault((Object)builder.cacheToolList, (Object)Boolean.TRUE);
            this.cacheResourceList = (Boolean)Utils.getOrDefault((Object)builder.cacheResourceList, (Object)Boolean.TRUE);
            this.cachePromptList = (Boolean)Utils.getOrDefault((Object)builder.cachePromptList, (Object)Boolean.TRUE);
            this.onResourceUpdated = builder.onResourceUpdated;
            this.toolResultExtractor = (McpToolResultExtractor)Utils.getOrDefault((Object)builder.toolResultExtractor, (Object)new DefaultMcpToolResultExtractor());
            this.RESULT_TIMEOUT = JsonNodeFactory.instance.objectNode();
            this.messageHandler = new McpOperationHandler(this.pendingOperations, this.mcpRoots::get, this.transport, message -> {
                this.logHandler.handleLogMessage((McpLogMessage)message);
                this.notifyListeners(l -> l.onNotificationMessage((McpLogMessage)message));
            }, () -> {
                this.toolListRefs.set(null);
                this.notifyListeners(l -> l.onNotificationToolsListChanged());
            }, () -> {
                this.resourceRefs.set(null);
                this.resourceTemplateRefs.set(null);
                this.notifyListeners(l -> l.onNotificationResourcesListChanged());
            }, () -> {
                this.promptRefs.set(null);
                this.notifyListeners(l -> l.onNotificationPromptsListChanged());
            }, uri -> {
                if (this.onResourceUpdated != null) {
                    this.onResourceUpdated.accept(this, (String)uri);
                }
                this.notifyListeners(l -> l.onNotificationResourceUpdated((String)uri));
            }, notification -> {
                if (this.progressHandler != null) {
                    this.progressHandler.onProgress(notification);
                }
                this.notifyListeners(l -> l.onNotificationProgress(notification));
            }, () -> this.notifyListeners(l -> l.onServerPing()), () -> this.notifyListeners(l -> l.onServerRootsList()), (requestId, reason) -> this.notifyListeners(l -> l.onNotificationCancelled((long)requestId, (String)reason)));
            ((ObjectNode)this.RESULT_TIMEOUT).putObject("result").putArray("content").addObject().put("type", "text").put("text", this.toolExecutionTimeoutErrorMessage);
            this.transport.onFailure(() -> {
                if (!this.closed) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(this.reconnectInterval.toMillis());
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    log.info("Trying to reconnect...");
                    this.triggerReconnection();
                }
            });
            this.initialize();
            this.startAutoHealthCheck();
        }
        catch (RuntimeException e) {
            this.closed = true;
            throw e;
        }
    }

    private void initialize() {
        this.transport.start(this.messageHandler);
        long operationId = this.idGenerator.getAndIncrement();
        McpInitializeRequest request = new McpInitializeRequest(operationId);
        McpInitializeParams params = this.createInitializeParams();
        request.setParams(params);
        McpCallContext context = new McpCallContext(null, request);
        this.notifyListeners(l -> l.beforeInitialize(context));
        this.applyMeta(request, context);
        try {
            JsonNode capabilities = this.transport.initialize(request).get(this.initializationTimeout.toMillis(), TimeUnit.MILLISECONDS);
            log.debug("MCP server capabilities: {}", (Object)capabilities.get("result"));
            this.initializeResult = DefaultMcpClient.toInitializeResult(capabilities);
            this.notifyListeners(l -> l.afterInitialize(context));
        }
        catch (Exception e) {
            this.notifyListeners(l -> l.onInitializeError(context, e));
            throw new RuntimeException(e);
        }
        finally {
            this.pendingOperations.remove(operationId);
        }
    }

    private McpInitializeParams createInitializeParams() {
        McpInitializeParams params = new McpInitializeParams();
        params.setProtocolVersion(this.protocolVersion);
        McpImplementation clientInfo = new McpImplementation();
        clientInfo.setName(this.clientName);
        clientInfo.setVersion(this.clientVersion);
        params.setClientInfo(clientInfo);
        McpInitializeParams.Capabilities capabilities = new McpInitializeParams.Capabilities();
        McpInitializeParams.Capabilities.Roots roots = new McpInitializeParams.Capabilities.Roots();
        roots.setListChanged(true);
        capabilities.setRoots(roots);
        params.setCapabilities(capabilities);
        return params;
    }

    private static McpInitializeResult toInitializeResult(JsonNode response) {
        JsonNode result = response.path("result");
        JsonNode serverInfo = result.path("serverInfo");
        JsonNode tools = result.path("capabilities").path("tools");
        McpImplementation implementation = null;
        if (!serverInfo.isMissingNode() && !serverInfo.isNull()) {
            implementation = (McpImplementation)OBJECT_MAPPER.convertValue((Object)serverInfo, McpImplementation.class);
        }
        McpInitializeResult.Capabilities capabilities = new McpInitializeResult.Capabilities(new McpInitializeResult.Capabilities.Tools(DefaultMcpClient.toNullableBoolean(tools.get("listChanged"))));
        return new McpInitializeResult(DefaultMcpClient.toNullableLong(response.get("id")), new McpInitializeResult.Result(result.path("protocolVersion").asText(null), capabilities, implementation, result.path("instructions").asText(null)));
    }

    private static @Nullable Long toNullableLong(JsonNode node) {
        return node == null || node.isNull() || !node.canConvertToLong() ? null : Long.valueOf(node.asLong());
    }

    private static @Nullable Boolean toNullableBoolean(JsonNode node) {
        return node == null || node.isNull() ? null : Boolean.valueOf(node.asBoolean());
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public @Nullable String instructions() {
        McpInitializeResult currentInitializeResult = this.initializeResult;
        if (currentInitializeResult == null || currentInitializeResult.getResult() == null) {
            return null;
        }
        return currentInitializeResult.getResult().getInstructions();
    }

    @Override
    public List<ToolSpecification> listTools() {
        return this.listTools(null);
    }

    @Override
    public List<ToolSpecification> listTools(InvocationContext invocationContext) {
        this.assertNotClosed();
        return this.retrieveWithPossibleCaching(this.cacheToolList, this::obtainToolList, this.toolListUpdateInProgress, () -> this.toolListRefs.get(), invocationContext);
    }

    public void evictToolListCache() {
        this.toolListRefs.set(null);
    }

    @Override
    public ToolExecutionResult executeTool(ToolExecutionRequest executionRequest) {
        return this.executeTool(executionRequest, null);
    }

    @Override
    public ToolExecutionResult executeTool(ToolExecutionRequest executionRequest, InvocationContext invocationContext) {
        this.assertNotClosed();
        ObjectNode arguments = null;
        try {
            String args = executionRequest.arguments();
            if (Utils.isNullOrBlank((String)args)) {
                args = "{}";
            }
            arguments = (ObjectNode)OBJECT_MAPPER.readValue(args, ObjectNode.class);
        }
        catch (JsonProcessingException e) {
            throw new ToolArgumentsException((Throwable)e);
        }
        long operationId = this.idGenerator.getAndIncrement();
        String progressToken = this.progressHandler != null ? String.valueOf(operationId) : null;
        McpCallToolRequest operation = new McpCallToolRequest(operationId, executionRequest.name(), arguments, progressToken);
        long timeoutMillis = this.toolExecutionTimeout.toMillis() == 0L ? Integer.MAX_VALUE : this.toolExecutionTimeout.toMillis();
        CompletableFuture<JsonNode> resultFuture = null;
        JsonNode result = null;
        McpCallContext context = new McpCallContext(invocationContext, operation);
        try {
            this.notifyListeners(l -> l.beforeExecuteTool(context));
            this.applyMeta(operation, context);
            resultFuture = this.transport.executeOperationWithResponse(context);
            result = resultFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
        }
        catch (TimeoutException timeout) {
            this.notifyListeners(l -> l.onExecuteToolError(context, timeout));
            McpCancellationNotification cancellation = new McpCancellationNotification((Long)operationId, "Timeout");
            this.applyMeta(cancellation, null);
            this.transport.executeOperationWithoutResponse(cancellation);
            ToolExecutionResult toolExecutionResult = ToolExecutionHelper.extractResult(this.RESULT_TIMEOUT, false, this.toolResultExtractor);
            return toolExecutionResult;
        }
        catch (ExecutionException e) {
            this.notifyListeners(l -> l.onExecuteToolError(context, e));
            throw new ToolExecutionException(e.getCause());
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        finally {
            this.pendingOperations.remove(operationId);
        }
        JsonNode finalResult = result;
        try {
            ToolExecutionResult toolResult = ToolExecutionHelper.extractResult(finalResult, false, this.toolResultExtractor);
            this.notifyListeners(l -> l.afterExecuteTool(context, toolResult, (Map)ToolExecutionHelper.toObject(finalResult)));
            return toolResult;
        }
        catch (ToolExecutionException e) {
            if (e.errorCode() != null) {
                this.notifyListeners(l -> l.onExecuteToolError(context, e));
            } else {
                this.notifyListeners(l -> l.afterExecuteTool(context, ToolExecutionHelper.extractResult(finalResult, true, this.toolResultExtractor), (Map)ToolExecutionHelper.toObject(finalResult)));
            }
            throw e;
        }
    }

    @Override
    public List<McpResource> listResources() {
        return this.listResources(null);
    }

    @Override
    public List<McpResource> listResources(InvocationContext invocationContext) {
        this.assertNotClosed();
        return this.retrieveWithPossibleCaching(this.cacheResourceList, this::obtainResourceList, this.resourceListUpdateInProgress, () -> this.resourceRefs.get(), invocationContext);
    }

    @Override
    public McpReadResourceResult readResource(String uri) {
        return this.readResource(uri, null);
    }

    @Override
    public McpReadResourceResult readResource(String uri, InvocationContext invocationContext) {
        this.assertNotClosed();
        long operationId = this.idGenerator.getAndIncrement();
        McpReadResourceRequest operation = new McpReadResourceRequest((Long)operationId, uri);
        McpCallContext context = new McpCallContext(invocationContext, operation);
        long timeoutMillis = this.resourcesTimeout.toMillis() == 0L ? Integer.MAX_VALUE : this.resourcesTimeout.toMillis();
        JsonNode result = null;
        CompletableFuture<JsonNode> resultFuture = null;
        this.notifyListeners(l -> l.beforeResourceGet(context));
        this.applyMeta(operation, context);
        try {
            resultFuture = this.transport.executeOperationWithResponse(context);
            result = resultFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
            McpReadResourceResult resourceResult = ResourcesHelper.parseResourceContents(result);
            JsonNode finalResult = result;
            this.notifyListeners(l -> l.afterResourceGet(context, resourceResult, (Map)ToolExecutionHelper.toObject(finalResult)));
            McpReadResourceResult mcpReadResourceResult = resourceResult;
            return mcpReadResourceResult;
        }
        catch (ExecutionException | TimeoutException e) {
            this.notifyListeners(l -> l.onResourceGetError(context, e));
            throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            Thread.interrupted();
            throw new RuntimeException(e);
        }
        catch (McpException e) {
            this.notifyListeners(l -> l.onResourceGetError(context, (Throwable)((Object)e)));
            throw e;
        }
        finally {
            this.pendingOperations.remove(operationId);
        }
    }

    @Override
    public List<McpPrompt> listPrompts() {
        this.assertNotClosed();
        return this.retrieveWithPossibleCaching(this.cachePromptList, this::obtainPromptList, this.promptListUpdateInProgress, () -> this.promptRefs.get(), null);
    }

    @Override
    public McpGetPromptResult getPrompt(String name, Map<String, Object> arguments) {
        this.assertNotClosed();
        long operationId = this.idGenerator.getAndIncrement();
        McpGetPromptRequest operation = new McpGetPromptRequest(operationId, name, arguments == null ? Collections.emptyMap() : arguments);
        McpCallContext context = new McpCallContext(null, operation);
        long timeoutMillis = this.promptsTimeout.toMillis() == 0L ? Integer.MAX_VALUE : this.promptsTimeout.toMillis();
        JsonNode result = null;
        CompletableFuture<JsonNode> resultFuture = null;
        this.notifyListeners(l -> l.beforePromptGet(context));
        this.applyMeta(operation, context);
        try {
            resultFuture = this.transport.executeOperationWithResponse(context);
            result = resultFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
            McpGetPromptResult promptResult = PromptsHelper.parsePromptContents(result);
            JsonNode finalResult = result;
            this.notifyListeners(l -> l.afterPromptGet(context, promptResult, (Map)ToolExecutionHelper.toObject(finalResult)));
            McpGetPromptResult mcpGetPromptResult = promptResult;
            return mcpGetPromptResult;
        }
        catch (ExecutionException | TimeoutException e) {
            this.notifyListeners(l -> l.onPromptGetError(context, e));
            throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            Thread.interrupted();
            throw new RuntimeException(e);
        }
        catch (McpException e) {
            this.notifyListeners(l -> l.onPromptGetError(context, (Throwable)((Object)e)));
            throw e;
        }
        finally {
            this.pendingOperations.remove(operationId);
        }
    }

    @Override
    public void checkHealth() {
        this.assertNotClosed();
        this.transport.checkHealth();
        long operationId = this.idGenerator.getAndIncrement();
        McpPingRequest ping = new McpPingRequest(operationId);
        McpCallContext context = new McpCallContext(null, ping);
        this.notifyListeners(l -> l.beforePing(context));
        this.applyMeta(ping, context);
        try {
            CompletableFuture<JsonNode> resultFuture = this.transport.executeOperationWithResponse(context);
            resultFuture.get(this.pingTimeout.toMillis(), TimeUnit.MILLISECONDS);
            this.notifyListeners(l -> l.afterPing(context));
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            RuntimeException re = new RuntimeException(e);
            this.notifyListeners(l -> l.onPingError(context, re));
            throw re;
        }
        finally {
            this.pendingOperations.remove(operationId);
        }
    }

    @Override
    public void setRoots(List<McpRoot> roots) {
        this.mcpRoots.set(roots);
        McpRootsListChangedNotification notification = new McpRootsListChangedNotification();
        McpCallContext context = new McpCallContext(null, notification);
        this.applyMeta(notification, context);
        this.transport.executeOperationWithoutResponse(context);
        this.notifyListeners(l -> l.onRootsListChanged(context));
    }

    @Override
    public void subscribeToResource(String uri) {
        this.assertNotClosed();
        if (this.onResourceUpdated == null) {
            log.warn("Subscribing to MCP resource '{}' but no onResourceUpdated callback was registered. The client willnot react to resource update notifications in any way.", (Object)uri);
        }
        long operationId = this.idGenerator.getAndIncrement();
        McpSubscribeResourceRequest operation = new McpSubscribeResourceRequest((Long)operationId, uri);
        McpCallContext context = new McpCallContext(null, operation);
        this.notifyListeners(l -> l.beforeResourceSubscribe(context));
        this.applyMeta(operation, context);
        long timeoutMillis = this.resourcesTimeout.toMillis() == 0L ? Integer.MAX_VALUE : this.resourcesTimeout.toMillis();
        try {
            CompletableFuture<JsonNode> resultFuture = this.transport.executeOperationWithResponse(context);
            resultFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
            this.notifyListeners(l -> l.afterResourceSubscribe(context));
        }
        catch (ExecutionException | TimeoutException e) {
            RuntimeException re = new RuntimeException(e);
            this.notifyListeners(l -> l.onResourceSubscribeError(context, re));
            throw re;
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        finally {
            this.pendingOperations.remove(operationId);
        }
    }

    @Override
    public void unsubscribeFromResource(String uri) {
        this.assertNotClosed();
        long operationId = this.idGenerator.getAndIncrement();
        McpUnsubscribeResourceRequest operation = new McpUnsubscribeResourceRequest((Long)operationId, uri);
        McpCallContext context = new McpCallContext(null, operation);
        this.notifyListeners(l -> l.beforeResourceUnsubscribe(context));
        this.applyMeta(operation, context);
        long timeoutMillis = this.resourcesTimeout.toMillis() == 0L ? Integer.MAX_VALUE : this.resourcesTimeout.toMillis();
        try {
            CompletableFuture<JsonNode> resultFuture = this.transport.executeOperationWithResponse(context);
            resultFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
            this.notifyListeners(l -> l.afterResourceUnsubscribe(context));
        }
        catch (ExecutionException | TimeoutException e) {
            RuntimeException re = new RuntimeException(e);
            this.notifyListeners(l -> l.onResourceUnsubscribeError(context, re));
            throw re;
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        finally {
            this.pendingOperations.remove(operationId);
        }
    }

    @Override
    public List<McpResourceTemplate> listResourceTemplates() {
        return this.listResourceTemplates(null);
    }

    @Override
    public List<McpResourceTemplate> listResourceTemplates(InvocationContext invocationContext) {
        this.assertNotClosed();
        return this.retrieveWithPossibleCaching(this.cacheResourceList, this::obtainResourceTemplateList, this.resourceTemplateListUpdateInProgress, () -> this.resourceTemplateRefs.get(), invocationContext);
    }

    private <T> T retrieveWithPossibleCaching(boolean useCache, Function<InvocationContext, T> retriever, AtomicReference<CompletableFuture<T>> updateInProgressReference, Supplier<T> cachedReferenceSupplier, InvocationContext invocationContext) {
        if (useCache) {
            T cachedValue = cachedReferenceSupplier.get();
            if (cachedValue != null) {
                return cachedValue;
            }
            CompletableFuture<T> newUpdate = new CompletableFuture<T>();
            boolean set = updateInProgressReference.compareAndSet(null, newUpdate);
            if (set) {
                Object updateInProgress = null;
                try {
                    T result = retriever.apply(invocationContext);
                    newUpdate.complete(result);
                    T t = result;
                    return t;
                }
                catch (RuntimeException e) {
                    newUpdate.completeExceptionally(e);
                    throw e;
                }
                finally {
                    updateInProgressReference.set(null);
                }
            }
            CompletableFuture<T> updateInProgress = updateInProgressReference.get();
            return updateInProgress.join();
        }
        return retriever.apply(invocationContext);
    }

    private List<ToolSpecification> obtainToolList(InvocationContext invocationContext) {
        McpCallContext listenerContext = new McpCallContext(invocationContext, new McpListToolsRequest((Long)this.idGenerator.getAndIncrement(), null));
        this.notifyListeners(l -> l.beforeToolsList(listenerContext));
        try {
            List<ToolSpecification> list = this.fetchPaginatedList((id, cursor) -> new McpListToolsRequest((Long)id, (String)cursor), this.toolExecutionTimeout, invocationContext, result -> ToolSpecificationHelper.toolSpecificationListFromMcpResponse((ArrayNode)result.get("result").get("tools")));
            this.toolListRefs.set(list);
            this.notifyListeners(l -> l.afterToolsList(listenerContext, list));
            return list;
        }
        catch (RuntimeException e) {
            this.notifyListeners(l -> l.onToolsListError(listenerContext, e));
            throw e;
        }
    }

    private List<McpResource> obtainResourceList(InvocationContext invocationContext) {
        McpCallContext listenerContext = new McpCallContext(invocationContext, new McpListResourcesRequest((Long)this.idGenerator.getAndIncrement(), null));
        this.notifyListeners(l -> l.beforeResourcesList(listenerContext));
        try {
            List<McpResource> list = this.fetchPaginatedList((id, cursor) -> new McpListResourcesRequest((Long)id, (String)cursor), this.resourcesTimeout, invocationContext, ResourcesHelper::parseResourceRefs);
            this.resourceRefs.set(list);
            this.notifyListeners(l -> l.afterResourcesList(listenerContext, list));
            return list;
        }
        catch (RuntimeException e) {
            this.notifyListeners(l -> l.onResourcesListError(listenerContext, e));
            throw e;
        }
    }

    private List<McpResourceTemplate> obtainResourceTemplateList(InvocationContext invocationContext) {
        McpCallContext listenerContext = new McpCallContext(invocationContext, new McpListResourceTemplatesRequest((Long)this.idGenerator.getAndIncrement(), null));
        this.notifyListeners(l -> l.beforeResourceTemplatesList(listenerContext));
        try {
            List<McpResourceTemplate> list = this.fetchPaginatedList((id, cursor) -> new McpListResourceTemplatesRequest((Long)id, (String)cursor), this.resourcesTimeout, invocationContext, ResourcesHelper::parseResourceTemplateRefs);
            this.resourceTemplateRefs.set(list);
            this.notifyListeners(l -> l.afterResourceTemplatesList(listenerContext, list));
            return list;
        }
        catch (RuntimeException e) {
            this.notifyListeners(l -> l.onResourceTemplatesListError(listenerContext, e));
            throw e;
        }
    }

    private List<McpPrompt> obtainPromptList(InvocationContext invocationContext) {
        McpCallContext listenerContext = new McpCallContext(invocationContext, new McpListPromptsRequest((Long)this.idGenerator.getAndIncrement(), null));
        this.notifyListeners(l -> l.beforePromptsList(listenerContext));
        try {
            List<McpPrompt> list = this.fetchPaginatedList((id, cursor) -> new McpListPromptsRequest((Long)id, (String)cursor), this.promptsTimeout, invocationContext, PromptsHelper::parsePromptRefs);
            this.promptRefs.set(list);
            this.notifyListeners(l -> l.afterPromptsList(listenerContext, list));
            return list;
        }
        catch (RuntimeException e) {
            this.notifyListeners(l -> l.onPromptsListError(listenerContext, e));
            throw e;
        }
    }

    private void startAutoHealthCheck() {
        if (Boolean.FALSE.equals(this.autoHealthCheck)) {
            return;
        }
        Runnable healthCheckTask = () -> {
            try {
                this.checkHealth();
            }
            catch (Exception e) {
                log.warn("MCP server health check (client key: " + this.key + ") failed. Attempting to reconnect...", (Throwable)e);
                this.triggerReconnection();
            }
        };
        this.healthCheckScheduler.scheduleAtFixedRate(healthCheckTask, this.autoHealthCheckInterval.toMillis(), this.autoHealthCheckInterval.toMillis(), TimeUnit.MILLISECONDS);
    }

    private void triggerReconnection() {
        if (this.initializationLock.tryLock()) {
            try {
                this.initialize();
            }
            catch (Exception e) {
                log.warn("mcp server reconnection failed", (Throwable)e);
            }
            finally {
                this.initializationLock.unlock();
            }
        }
    }

    private <T> List<T> fetchPaginatedList(BiFunction<Long, String, McpClientRequest> requestFactory, Duration timeout, InvocationContext invocationContext, Function<JsonNode, List<T>> resultParser) {
        JsonNode result;
        long timeoutMillis = timeout.toMillis() == 0L ? Integer.MAX_VALUE : timeout.toMillis();
        ArrayList allItems = new ArrayList();
        String cursor = null;
        do {
            McpClientRequest operation = requestFactory.apply(this.idGenerator.getAndIncrement(), cursor);
            McpCallContext context = new McpCallContext(invocationContext, operation);
            this.applyMeta(operation, context);
            try {
                CompletableFuture<JsonNode> resultFuture = this.transport.executeOperationWithResponse(context);
                result = resultFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
            finally {
                this.pendingOperations.remove(operation.getId());
            }
            allItems.addAll(resultParser.apply(result));
        } while ((cursor = DefaultMcpClient.getNextCursor(result)) != null);
        return allItems;
    }

    private static String getNextCursor(JsonNode response) {
        String nextCursor;
        JsonNode resultNode = response.get("result");
        if (resultNode != null && resultNode.has("nextCursor") && !(nextCursor = resultNode.get("nextCursor").asText()).isEmpty()) {
            return nextCursor;
        }
        return null;
    }

    @Override
    public void close() {
        this.closed = true;
        if (this.healthCheckScheduler != null) {
            this.healthCheckScheduler.shutdownNow();
        }
        try {
            this.transport.close();
        }
        catch (Exception e) {
            log.warn("Cannot close MCP transport", (Throwable)e);
        }
    }

    private void applyMeta(McpClientMessage message, McpCallContext context) {
        if (this.metaSupplier == null) {
            return;
        }
        Map meta = (Map)this.metaSupplier.apply(context);
        if (meta == null || meta.isEmpty()) {
            return;
        }
        if (message instanceof McpClientRequest) {
            McpClientRequest request = (McpClientRequest)message;
            if (request.getParams() == null) {
                request.setParams(new McpClientParams());
            }
            request.getParams().setMeta(meta);
        } else if (message instanceof McpClientNotification) {
            McpClientNotification notification = (McpClientNotification)message;
            if (notification.getParams() == null) {
                notification.setParams(new McpClientParams());
            }
            notification.getParams().setMeta(meta);
        }
    }

    private void notifyListeners(Consumer<McpClientListener> action) {
        for (McpClientListener listener : this.listeners) {
            try {
                action.accept(listener);
            }
            catch (Exception e) {
                log.warn("MCP client listener threw an exception", (Throwable)e);
            }
        }
    }

    private void assertNotClosed() {
        if (this.closed) {
            throw new IllegalStateException("The client is closed");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String toolExecutionTimeoutErrorMessage;
        private McpTransport transport;
        private String key;
        private String clientName;
        private String clientVersion;
        private String protocolVersion;
        private Duration initializationTimeout;
        private Duration toolExecutionTimeout;
        private Duration resourcesTimeout;
        private Duration pingTimeout;
        private Duration promptsTimeout;
        private McpLogMessageHandler logHandler;
        private Duration reconnectInterval;
        private Boolean autoHealthCheck;
        private Duration autoHealthCheckInterval;
        private List<McpRoot> roots;
        private Boolean cacheToolList;
        private Boolean cacheResourceList;
        private Boolean cachePromptList;
        private final List<McpClientListener> listeners = new ArrayList<McpClientListener>();
        private McpProgressHandler progressHandler;
        private McpMetaSupplier metaSupplier;
        private BiConsumer<McpClient, String> onResourceUpdated;
        private McpToolResultExtractor toolResultExtractor;

        public Builder transport(McpTransport transport) {
            this.transport = transport;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder clientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder clientVersion(String clientVersion) {
            this.clientVersion = clientVersion;
            return this;
        }

        public Builder protocolVersion(String protocolVersion) {
            this.protocolVersion = protocolVersion;
            return this;
        }

        public Builder initializationTimeout(Duration initializationTimeout) {
            this.initializationTimeout = initializationTimeout;
            return this;
        }

        public Builder toolExecutionTimeout(Duration toolExecutionTimeout) {
            this.toolExecutionTimeout = toolExecutionTimeout;
            return this;
        }

        public Builder resourcesTimeout(Duration resourcesTimeout) {
            this.resourcesTimeout = resourcesTimeout;
            return this;
        }

        public Builder promptsTimeout(Duration promptsTimeout) {
            this.promptsTimeout = promptsTimeout;
            return this;
        }

        public Builder toolExecutionTimeoutErrorMessage(String toolExecutionTimeoutErrorMessage) {
            this.toolExecutionTimeoutErrorMessage = toolExecutionTimeoutErrorMessage;
            return this;
        }

        public Builder logHandler(McpLogMessageHandler logHandler) {
            this.logHandler = logHandler;
            return this;
        }

        public Builder pingTimeout(Duration pingTimeout) {
            this.pingTimeout = pingTimeout;
            return this;
        }

        public Builder reconnectInterval(Duration reconnectInterval) {
            this.reconnectInterval = reconnectInterval;
            return this;
        }

        public Builder autoHealthCheck(boolean autoHealthCheck) {
            this.autoHealthCheck = autoHealthCheck;
            return this;
        }

        public Builder autoHealthCheckInterval(Duration interval) {
            this.autoHealthCheckInterval = interval;
            return this;
        }

        public Builder roots(List<McpRoot> roots) {
            this.roots = new ArrayList<McpRoot>(roots);
            return this;
        }

        public Builder cacheToolList(boolean cacheToolList) {
            this.cacheToolList = cacheToolList;
            return this;
        }

        public Builder cacheResourceList(boolean cacheResourceList) {
            this.cacheResourceList = cacheResourceList;
            return this;
        }

        public Builder cachePromptList(boolean cachePromptList) {
            this.cachePromptList = cachePromptList;
            return this;
        }

        @Deprecated
        public Builder listener(McpClientListener listener) {
            this.listeners.add(listener);
            return this;
        }

        public Builder addListener(McpClientListener listener) {
            this.listeners.add(listener);
            return this;
        }

        public Builder addListeners(List<McpClientListener> listeners) {
            this.listeners.addAll(listeners);
            return this;
        }

        public Builder progressHandler(McpProgressHandler progressHandler) {
            this.progressHandler = progressHandler;
            return this;
        }

        public Builder metaSupplier(McpMetaSupplier metaSupplier) {
            this.metaSupplier = metaSupplier;
            return this;
        }

        public Builder toolResultExtractor(McpToolResultExtractor toolResultExtractor) {
            this.toolResultExtractor = (McpToolResultExtractor)ValidationUtils.ensureNotNull((Object)toolResultExtractor, (String)"toolResultExtractor");
            return this;
        }

        public Builder onResourceUpdated(BiConsumer<McpClient, String> onResourceUpdated) {
            this.onResourceUpdated = onResourceUpdated;
            return this;
        }

        public DefaultMcpClient build() {
            return new DefaultMcpClient(this);
        }
    }
}

