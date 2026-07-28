/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.mcp.protocol;

import dev.langchain4j.Internal;
import dev.langchain4j.mcp.protocol.McpClientMethod;
import dev.langchain4j.mcp.protocol.McpClientNotification;

@Internal
public class McpRootsListChangedNotification
extends McpClientNotification {
    public McpRootsListChangedNotification() {
        super(McpClientMethod.NOTIFICATION_ROOTS_LIST_CHANGED);
    }
}

