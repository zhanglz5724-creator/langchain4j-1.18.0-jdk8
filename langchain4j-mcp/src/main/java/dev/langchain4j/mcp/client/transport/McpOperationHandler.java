/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.client.transport;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.mcp.client.McpRoot;
import dev.langchain4j.mcp.client.logging.McpLogMessage;
import dev.langchain4j.mcp.client.progress.McpProgressHandler;
import dev.langchain4j.mcp.client.progress.McpProgressNotification;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.protocol.McpPingResponse;
import dev.langchain4j.mcp.protocol.McpRootsListResponse;
import dev.langchain4j.mcp.protocol.McpServerMethod;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class McpOperationHandler {
    private final Map<Long, CompletableFuture<JsonNode>> pendingOperations;
    private static final Logger log = LoggerFactory.getLogger(McpOperationHandler.class);
    private final McpTransport transport;
    private final Consumer<McpLogMessage> logMessageConsumer;
    private final Runnable onToolListUpdate;
    private final Runnable onResourceListUpdate;
    private final Runnable onPromptListUpdate;
    private final Consumer<String> onResourceUpdate;
    private final Supplier<List<McpRoot>> roots;
    private final McpProgressHandler progressHandler;
    private final Runnable onServerPing;
    private final Runnable onServerRootsList;
    private final BiConsumer<Long, String> onServerCancelled;

    public McpOperationHandler(Map<Long, CompletableFuture<JsonNode>> pendingOperations, Supplier<List<McpRoot>> roots, McpTransport transport, Consumer<McpLogMessage> logMessageConsumer, Runnable onToolListUpdate, Runnable onResourceListUpdate, Runnable onPromptListUpdate, Consumer<String> onResourceUpdate, McpProgressHandler progressHandler, Runnable onServerPing, Runnable onServerRootsList, BiConsumer<Long, String> onServerCancelled) {
        this.pendingOperations = pendingOperations;
        this.transport = transport;
        this.logMessageConsumer = logMessageConsumer;
        this.onToolListUpdate = onToolListUpdate;
        this.onResourceListUpdate = onResourceListUpdate;
        this.onPromptListUpdate = onPromptListUpdate;
        this.onResourceUpdate = onResourceUpdate;
        this.roots = roots;
        this.progressHandler = progressHandler;
        this.onServerPing = onServerPing;
        this.onServerRootsList = onServerRootsList;
        this.onServerCancelled = onServerCancelled;
    }

    public void handle(JsonNode message) {
        if (message.has("id")) {
            this.handleMessageWithId(message);
        } else if (message.has("method")) {
            this.handleNotification(message);
        }
    }

    private void handleMessageWithId(JsonNode message) {
        block10: {
            long messageId;
            block11: {
                block9: {
                    messageId = message.get("id").asLong();
                    if (!message.has("result") && !message.has("error")) break block9;
                    CompletableFuture<JsonNode> op = this.pendingOperations.remove(messageId);
                    if (op != null) {
                        op.complete(message);
                    } else {
                        log.warn("Received response for unknown message id: {}", (Object)messageId);
                    }
                    break block10;
                }
                if (!message.has("method")) break block11;
                McpServerMethod method = McpServerMethod.from(message.get("method").asText());
                if (method == null) {
                    log.warn("Received response for unknown message id: {}", (Object)messageId);
                    return;
                }
                switch (method) {
                    case PING: {
                        this.transport.executeOperationWithoutResponse(new McpPingResponse(messageId));
                        if (this.onServerPing != null) {
                            this.onServerPing.run();
                            break;
                        }
                        break block10;
                    }
                    case ROOTS_LIST: {
                        this.transport.executeOperationWithoutResponse(new McpRootsListResponse((Long)messageId, this.roots.get()));
                        if (this.onServerRootsList != null) {
                            this.onServerRootsList.run();
                            break;
                        }
                        break block10;
                    }
                    default: {
                        log.warn("Received response for unknown message id: {}", (Object)messageId);
                        break;
                    }
                }
                break block10;
            }
            log.warn("Received response for unknown message id: {}", (Object)messageId);
        }
    }

    private void handleNotification(JsonNode message) {
        McpServerMethod method = McpServerMethod.from(message.get("method").asText());
        if (method == null) {
            log.warn("Received unknown message: {}", (Object)message);
            return;
        }
        switch (method) {
            case NOTIFICATION_MESSAGE: {
                this.handleLogMessage(message);
                break;
            }
            case NOTIFICATION_TOOLS_LIST_CHANGED: {
                this.onToolListUpdate.run();
                break;
            }
            case NOTIFICATION_RESOURCES_LIST_CHANGED: {
                if (this.onResourceListUpdate == null) break;
                this.onResourceListUpdate.run();
                break;
            }
            case NOTIFICATION_PROMPTS_LIST_CHANGED: {
                if (this.onPromptListUpdate == null) break;
                this.onPromptListUpdate.run();
                break;
            }
            case NOTIFICATION_RESOURCES_UPDATED: {
                this.handleResourceUpdatedNotification(message);
                break;
            }
            case NOTIFICATION_PROGRESS: {
                this.handleProgressNotification(message);
                break;
            }
            case NOTIFICATION_CANCELLED: {
                this.handleCancelledNotification(message);
                break;
            }
            default: {
                log.warn("Received unknown message: {}", (Object)message);
            }
        }
    }

    private void handleCancelledNotification(JsonNode message) {
        JsonNode params = message.get("params");
        if (params == null || !params.has("requestId")) {
            log.warn("Received cancelled notification without requestId: {}", (Object)message);
            return;
        }
        long requestId = params.get("requestId").asLong();
        String reason = params.has("reason") ? params.get("reason").asText() : null;
        CompletableFuture<JsonNode> pending = this.pendingOperations.remove(requestId);
        if (pending != null) {
            String message1 = reason != null ? "Request " + requestId + " was cancelled by the server: " + reason : "Request " + requestId + " was cancelled by the server";
            pending.completeExceptionally(new CancellationException(message1));
        } else {
            log.debug("Received cancelled notification for unknown or already completed request id: {} (reason: {})", (Object)requestId, (Object)reason);
        }
        if (this.onServerCancelled != null) {
            this.onServerCancelled.accept(requestId, reason);
        }
    }

    private void handleLogMessage(JsonNode message) {
        if (message.has("params")) {
            if (this.logMessageConsumer != null) {
                this.logMessageConsumer.accept(McpLogMessage.fromJson(message.get("params")));
            }
        } else {
            log.warn("Received log message without params: {}", (Object)message);
        }
    }

    private void handleResourceUpdatedNotification(JsonNode message) {
        if (this.onResourceUpdate != null && message.has("params") && message.get("params").has("uri")) {
            String uri = message.get("params").get("uri").asText();
            this.onResourceUpdate.accept(uri);
        } else if (message.has("params") && !message.get("params").has("uri")) {
            log.warn("Received resource updated notification without uri: {}", (Object)message);
        }
    }

    private void handleProgressNotification(JsonNode message) {
        if (this.progressHandler != null && message.has("params")) {
            this.progressHandler.onProgress(McpProgressNotification.fromJson(message.get("params")));
        }
    }

    public void startOperation(Long id, CompletableFuture<JsonNode> future) {
        this.pendingOperations.put(id, future);
    }

    public synchronized void cancelAllPendingOperations(String reason) {
        for (CompletableFuture<JsonNode> future : this.pendingOperations.values()) {
            future.completeExceptionally(new IllegalStateException("Operation cancelled due to transport failure: " + reason));
        }
        this.pendingOperations.clear();
    }
}

