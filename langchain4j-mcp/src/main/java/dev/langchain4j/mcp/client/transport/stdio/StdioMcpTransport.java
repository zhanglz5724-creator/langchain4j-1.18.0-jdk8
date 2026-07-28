/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  dev.langchain4j.internal.DefaultExecutorProvider
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.client.transport.stdio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.internal.DefaultExecutorProvider;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.mcp.client.McpCallContext;
import dev.langchain4j.mcp.client.transport.McpOperationHandler;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.ProcessStderrHandler;
import dev.langchain4j.mcp.protocol.McpClientMessage;
import dev.langchain4j.mcp.protocol.McpInitializationNotification;
import dev.langchain4j.mcp.protocol.McpInitializeRequest;
import dev.langchain4j.mcp.transport.stdio.JsonRpcIoHandler;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StdioMcpTransport
implements McpTransport {
    private final List<String> command;
    private final Map<String, String> environment;
    private Process process;
    private JsonRpcIoHandler jsonRpcIoHandler;
    private final boolean logEvents;
    private final Logger logger;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(StdioMcpTransport.class);
    private volatile McpOperationHandler messageHandler;
    private ProcessStderrHandler stderrHandler;
    private ExecutorService executorService;
    private boolean shouldShutdownExecutorService;

    public StdioMcpTransport(Builder builder) {
        this.command = builder.command;
        this.environment = builder.environment;
        this.logEvents = builder.logEvents;
        this.logger = builder.logger;
        this.executorService = (ExecutorService)Utils.getOrDefault((Object)builder.executorService, DefaultExecutorProvider::getDefaultExecutorService);
        this.shouldShutdownExecutorService = false;
    }

    @Override
    public void start(McpOperationHandler messageHandler) {
        this.messageHandler = messageHandler;
        log.debug("Starting process: {}", this.command);
        ProcessBuilder processBuilder = new ProcessBuilder(this.command);
        processBuilder.environment().putAll(this.environment);
        try {
            this.process = processBuilder.start();
            log.debug("PID of the started process: N/A (Java 8)");
            log.debug("Process started successfully");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.jsonRpcIoHandler = new JsonRpcIoHandler(this.process.getInputStream(), this.process.getOutputStream(), messageHandler::handle, this.logEvents, this.logger);
        this.stderrHandler = new ProcessStderrHandler(this.process);
        this.executorService.submit(this.jsonRpcIoHandler);
        this.executorService.submit(this.stderrHandler);
    }

    @Override
    public CompletableFuture<JsonNode> initialize(McpInitializeRequest operation) {
        try {
            String requestString = OBJECT_MAPPER.writeValueAsString((Object)operation);
            String initializationNotification = OBJECT_MAPPER.writeValueAsString((Object)new McpInitializationNotification());
            return this.execute(requestString, operation.getId()).thenCompose(originalResponse -> this.execute(initializationNotification, null).thenCompose(nullNode -> CompletableFuture.completedFuture(originalResponse)));
        }
        catch (JsonProcessingException e) {
            CompletableFuture<JsonNode> future = new CompletableFuture<JsonNode>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public CompletableFuture<JsonNode> executeOperationWithResponse(McpClientMessage operation) {
        return this.executeOperationWithResponse(new McpCallContext(null, operation));
    }

    @Override
    public CompletableFuture<JsonNode> executeOperationWithResponse(McpCallContext context) {
        try {
            String requestString = OBJECT_MAPPER.writeValueAsString((Object)context.message());
            return this.execute(requestString, context.message().getId());
        }
        catch (JsonProcessingException e) {
            CompletableFuture<JsonNode> future = new CompletableFuture<JsonNode>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public void executeOperationWithoutResponse(McpClientMessage operation) {
        this.executeOperationWithoutResponse(new McpCallContext(null, operation));
    }

    @Override
    public void executeOperationWithoutResponse(McpCallContext context) {
        try {
            String requestString = OBJECT_MAPPER.writeValueAsString((Object)context.message());
            this.execute(requestString, null);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void checkHealth() {
        if (!this.process.isAlive()) {
            throw new IllegalStateException("Process is not alive");
        }
    }

    @Override
    public void onFailure(Runnable actionOnFailure) {
    }

    @Override
    public void close() throws IOException {
        try {
            this.stderrHandler.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            this.jsonRpcIoHandler.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (this.executorService != null && this.shouldShutdownExecutorService) {
            this.executorService.shutdown();
            try {
                if (!this.executorService.awaitTermination(5L, TimeUnit.SECONDS)) {
                    this.executorService.shutdownNow();
                }
            }
            catch (InterruptedException e) {
                this.executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        this.process.destroy();
    }

    public static Builder builder() {
        return new Builder();
    }

    private CompletableFuture<JsonNode> execute(String request, Long id) {
        CompletableFuture<JsonNode> future = new CompletableFuture<JsonNode>();
        if (id != null) {
            this.messageHandler.startOperation(id, future);
        }
        try {
            this.jsonRpcIoHandler.submit(request);
            if (id == null) {
                future.complete(null);
            }
        }
        catch (IOException e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public Process getProcess() {
        return this.process;
    }

    public static class Builder {
        private List<String> command;
        private Map<String, String> environment;
        private boolean logEvents;
        private Logger logger;
        private ExecutorService executorService;

        public Builder command(List<String> command) {
            this.command = command;
            return this;
        }

        public Builder environment(Map<String, String> environment) {
            this.environment = environment;
            return this;
        }

        public Builder logEvents(boolean logEvents) {
            this.logEvents = logEvents;
            return this;
        }

        public Builder executorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public StdioMcpTransport build() {
            ValidationUtils.ensureNotEmpty(this.command, (String)"command");
            if (this.environment == null) {
                this.environment = Collections.emptyMap();
            }
            return new StdioMcpTransport(this);
        }
    }
}

