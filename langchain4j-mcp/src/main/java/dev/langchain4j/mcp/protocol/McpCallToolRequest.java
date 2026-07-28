/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.node.ObjectNode
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.mcp.protocol;

import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.langchain4j.Internal;
import dev.langchain4j.mcp.protocol.McpCallToolParams;
import dev.langchain4j.mcp.protocol.McpClientMethod;
import dev.langchain4j.mcp.protocol.McpClientRequest;
import java.util.Collections;

@Internal
public class McpCallToolRequest
extends McpClientRequest {
    public McpCallToolRequest(Long id, String toolName, ObjectNode arguments) {
        this(id, toolName, arguments, null);
    }

    public McpCallToolRequest(Long id, String toolName, ObjectNode arguments, String progressToken) {
        super(id, McpClientMethod.TOOLS_CALL);
        McpCallToolParams params = new McpCallToolParams(toolName, arguments);
        if (progressToken != null) {
            params.setMeta(Collections.singletonMap("progressToken", progressToken));
        }
        this.setParams(params);
    }
}

