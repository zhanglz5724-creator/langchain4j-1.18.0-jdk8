/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  okhttp3.Response
 *  okhttp3.sse.EventSource
 *  okhttp3.sse.EventSourceListener
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.client.transport.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.mcp.client.logging.McpLoggers;
import dev.langchain4j.mcp.client.transport.McpOperationHandler;
import java.util.concurrent.CompletableFuture;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SseEventListener
extends EventSourceListener {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(SseEventListener.class);
    private static final Logger trafficLog = McpLoggers.traffic();
    private final boolean logEvents;
    private final CompletableFuture<String> initializationFinished;
    private final McpOperationHandler messageHandler;
    private final Runnable onFailure;

    public SseEventListener(McpOperationHandler messageHandler, boolean logEvents, CompletableFuture initializationFinished, Runnable onFailure) {
        this.messageHandler = messageHandler;
        this.logEvents = logEvents;
        this.initializationFinished = initializationFinished;
        this.onFailure = onFailure;
    }

    public void onClosed(EventSource eventSource) {
        log.debug("SSE channel closed");
    }

    public void onEvent(EventSource eventSource, String id, String type, String data) {
        if (type.equals("message")) {
            if (this.logEvents) {
                trafficLog.info("< {}", (Object)data);
            }
            try {
                JsonNode jsonNode = OBJECT_MAPPER.readTree(data);
                this.messageHandler.handle(jsonNode);
            }
            catch (JsonProcessingException e) {
                log.warn("Failed to parse JSON message: {}", (Object)data, (Object)e);
            }
        } else if (type.equals("endpoint")) {
            if (this.initializationFinished.isDone()) {
                log.warn("Received endpoint event after initialization");
                return;
            }
            this.initializationFinished.complete(data);
        }
    }

    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        if (!this.initializationFinished.isDone()) {
            if (t != null) {
                this.initializationFinished.completeExceptionally(t);
            } else if (response != null) {
                this.initializationFinished.completeExceptionally(new RuntimeException("The server returned: " + response.message()));
            }
        }
        if (!(t == null || t.getMessage() != null && t.getMessage().contains("Socket closed"))) {
            log.warn("SSE channel failure", t);
            if (this.onFailure != null) {
                this.onFailure.run();
            }
        }
    }

    public void onOpen(EventSource eventSource, Response response) {
        log.debug("Connected to SSE channel at {}", (Object)response.request().url());
    }
}

