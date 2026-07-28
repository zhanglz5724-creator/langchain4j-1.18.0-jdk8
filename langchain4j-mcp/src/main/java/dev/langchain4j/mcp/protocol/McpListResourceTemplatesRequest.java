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
import dev.langchain4j.mcp.protocol.McpListResourceTemplatesParams;

@Internal
public class McpListResourceTemplatesRequest
extends McpClientRequest {
    public McpListResourceTemplatesRequest(Long id, String cursor) {
        super(id, McpClientMethod.RESOURCES_TEMPLATES_LIST);
        if (cursor != null) {
            McpListResourceTemplatesParams p = new McpListResourceTemplatesParams();
            p.setCursor(cursor);
            this.setParams(p);
        }
    }
}

