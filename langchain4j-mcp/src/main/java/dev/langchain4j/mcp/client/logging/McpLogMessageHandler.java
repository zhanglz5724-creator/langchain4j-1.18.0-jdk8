/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.mcp.client.logging;

import dev.langchain4j.mcp.client.logging.McpLogMessage;

public interface McpLogMessageHandler {
    public void handleLogMessage(McpLogMessage var1);
}

