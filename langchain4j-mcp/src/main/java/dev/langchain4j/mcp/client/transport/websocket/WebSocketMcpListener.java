package dev.langchain4j.mcp.client.transport.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.mcp.client.transport.McpOperationHandler;
import java.util.concurrent.CompletableFuture;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.langchain4j.mcp.client.transport.websocket.WebSocketMcpTransport.OBJECT_MAPPER;

public class WebSocketMcpListener extends WebSocketListener {

    private final McpOperationHandler operationHandler;
    private final Logger trafficLogger;
    private final boolean logResponses;
    private final Runnable onCloseCallback;
    private final Logger logger = LoggerFactory.getLogger(WebSocketMcpListener.class);
    private final Runnable onFailureCallback;
    private final CompletableFuture<WebSocket> connectionFuture;

    public WebSocketMcpListener(McpOperationHandler operationHandler,
                                Logger trafficLogger,
                                boolean logResponses,
                                Runnable onCloseCallback,
                                Runnable onFailureCallback,
                                CompletableFuture<WebSocket> connectionFuture) {
        this.operationHandler = operationHandler;
        this.trafficLogger = trafficLogger;
        this.logResponses = logResponses;
        this.onCloseCallback = onCloseCallback;
        this.onFailureCallback = onFailureCallback;
        this.connectionFuture = connectionFuture;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        logger.debug("Websocket connection opened");
        connectionFuture.complete(webSocket);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        if (logResponses) {
            trafficLogger.info("< " + text);
        }
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(text);
            operationHandler.handle(jsonNode);
        } catch (JsonProcessingException e) {
            logger.warn("Failed to parse JSON message: {}", text, e);
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        logger.debug("Websocket connection closing with status {} and reason: {}", code, reason);
        operationHandler.cancelAllPendingOperations("Status " + code + ", Reason: " + reason);
        onCloseCallback.run();
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        logger.debug("Websocket connection closed with status {} and reason: {}", code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        logger.warn("WebSocket error", t);
        connectionFuture.completeExceptionally(t);
        onFailureCallback.run();
    }
}
