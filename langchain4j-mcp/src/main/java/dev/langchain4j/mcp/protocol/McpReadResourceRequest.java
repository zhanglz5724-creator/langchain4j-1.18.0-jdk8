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
import dev.langchain4j.mcp.protocol.McpReadResourceParams;

@Internal
public class McpReadResourceRequest
extends McpClientRequest {
    public McpReadResourceRequest(Long id, String uri) {
        super(id, McpClientMethod.RESOURCES_READ);
        this.setParams(new McpReadResourceParams(uri));
    }
}

