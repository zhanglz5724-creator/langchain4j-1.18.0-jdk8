/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.IllegalResponseException;
import dev.langchain4j.mcp.client.McpErrorHelper;
import dev.langchain4j.mcp.client.McpGetPromptResult;
import dev.langchain4j.mcp.client.McpPrompt;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PromptsHelper {
    private static final Logger log = LoggerFactory.getLogger(PromptsHelper.class);

    PromptsHelper() {
    }

    static List<McpPrompt> parsePromptRefs(JsonNode mcpMessage) {
        McpErrorHelper.checkForErrors(mcpMessage);
        if (mcpMessage.has("result")) {
            JsonNode resultNode = mcpMessage.get("result");
            if (resultNode.has("prompts")) {
                ArrayList<McpPrompt> promptRefs = new ArrayList<McpPrompt>();
                for (JsonNode promptNode : resultNode.get("prompts")) {
                    promptRefs.add((McpPrompt)DefaultMcpClient.OBJECT_MAPPER.convertValue((Object)promptNode, McpPrompt.class));
                }
                return promptRefs;
            }
            log.warn("Result does not contain 'prompts' element: {}", (Object)resultNode);
            throw new IllegalResponseException("Result does not contain 'prompts' element");
        }
        log.warn("Result does not contain 'result' element: {}", (Object)mcpMessage);
        throw new IllegalResponseException("Result does not contain 'result' element");
    }

    static McpGetPromptResult parsePromptContents(JsonNode mcpMessage) {
        McpErrorHelper.checkForErrors(mcpMessage);
        return (McpGetPromptResult)DefaultMcpClient.OBJECT_MAPPER.convertValue((Object)mcpMessage.get("result"), McpGetPromptResult.class);
    }
}

