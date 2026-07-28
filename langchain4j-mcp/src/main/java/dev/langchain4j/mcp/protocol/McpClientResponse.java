/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.mcp.protocol;

import dev.langchain4j.Internal;
import dev.langchain4j.mcp.protocol.McpClientMessage;

@Internal
public class McpClientResponse
extends McpClientMessage {
    public McpClientResponse(Long id) {
        super(id, null);
    }
}

