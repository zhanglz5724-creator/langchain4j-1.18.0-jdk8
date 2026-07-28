/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  org.jspecify.annotations.NonNull
 */
package dev.langchain4j.mcp.protocol;

import dev.langchain4j.Internal;
import dev.langchain4j.mcp.protocol.McpCancellationParams;
import dev.langchain4j.mcp.protocol.McpClientMethod;
import dev.langchain4j.mcp.protocol.McpClientNotification;
import org.jspecify.annotations.NonNull;

@Internal
public class McpCancellationNotification
extends McpClientNotification {
    public McpCancellationNotification(@NonNull Long requestId, String reason) {
        super(McpClientMethod.NOTIFICATION_CANCELLED);
        this.setParams(new McpCancellationParams(requestId, reason));
    }
}

