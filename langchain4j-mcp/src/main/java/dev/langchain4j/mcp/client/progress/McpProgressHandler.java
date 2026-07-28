/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.mcp.client.progress;

import dev.langchain4j.mcp.client.progress.McpProgressNotification;

public interface McpProgressHandler {
    public void onProgress(McpProgressNotification var1);
}

