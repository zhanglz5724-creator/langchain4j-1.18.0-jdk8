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
import dev.langchain4j.mcp.protocol.McpListResourcesParams;

@Internal
public class McpListResourcesRequest
extends McpClientRequest {
    public McpListResourcesRequest(Long id, String cursor) {
        super(id, McpClientMethod.RESOURCES_LIST);
        if (cursor != null) {
            McpListResourcesParams p = new McpListResourcesParams();
            p.setCursor(cursor);
            this.setParams(p);
        }
    }
}

