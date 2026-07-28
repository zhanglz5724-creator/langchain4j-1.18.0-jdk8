/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 */
package dev.langchain4j.mcp.client.transport;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.mcp.client.McpCallContext;
import dev.langchain4j.mcp.client.transport.McpOperationHandler;
import dev.langchain4j.mcp.protocol.McpClientMessage;
import dev.langchain4j.mcp.protocol.McpInitializeRequest;
import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

public interface McpTransport
extends Closeable {
    public void start(McpOperationHandler var1);

    public CompletableFuture<JsonNode> initialize(McpInitializeRequest var1);

    public CompletableFuture<JsonNode> executeOperationWithResponse(McpClientMessage var1);

    public CompletableFuture<JsonNode> executeOperationWithResponse(McpCallContext var1);

    public void executeOperationWithoutResponse(McpClientMessage var1);

    public void executeOperationWithoutResponse(McpCallContext var1);

    public void checkHealth();

    public void onFailure(Runnable var1);
}

