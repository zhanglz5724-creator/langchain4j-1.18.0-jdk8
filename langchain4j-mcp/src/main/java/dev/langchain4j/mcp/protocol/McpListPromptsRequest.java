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
import dev.langchain4j.mcp.protocol.McpListPromptsParams;

@Internal
public class McpListPromptsRequest
extends McpClientRequest {
    public McpListPromptsRequest(Long id, String cursor) {
        super(id, McpClientMethod.PROMPTS_LIST);
        if (cursor != null) {
            McpListPromptsParams p = new McpListPromptsParams();
            p.setCursor(cursor);
            this.setParams(p);
        }
    }
}

