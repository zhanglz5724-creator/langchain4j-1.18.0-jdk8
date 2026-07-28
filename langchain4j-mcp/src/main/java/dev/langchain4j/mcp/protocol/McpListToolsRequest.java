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
import dev.langchain4j.mcp.protocol.McpListToolsParams;

@Internal
public class McpListToolsRequest
extends McpClientRequest {
    public McpListToolsRequest(Long id, String cursor) {
        super(id, McpClientMethod.TOOLS_LIST);
        if (cursor != null) {
            McpListToolsParams p = new McpListToolsParams();
            p.setCursor(cursor);
            this.setParams(p);
        }
    }
}

