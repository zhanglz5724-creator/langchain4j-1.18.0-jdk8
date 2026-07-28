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
import dev.langchain4j.mcp.protocol.McpGetPromptParams;
import java.util.Map;

@Internal
public class McpGetPromptRequest
extends McpClientRequest {
    public McpGetPromptRequest(Long id, String promptName, Map<String, Object> arguments) {
        super(id, McpClientMethod.PROMPTS_GET);
        this.setParams(new McpGetPromptParams(promptName, arguments));
    }
}

