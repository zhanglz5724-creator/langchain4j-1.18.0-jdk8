/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.mcp.protocol;

import dev.langchain4j.Internal;
import dev.langchain4j.mcp.protocol.McpClientMethod;
import dev.langchain4j.mcp.protocol.McpClientRequest;

@Internal
public class McpInitializeRequest
extends McpClientRequest {
    public McpInitializeRequest(Long id) {
        super(id, McpClientMethod.INITIALIZE);
    }
}

