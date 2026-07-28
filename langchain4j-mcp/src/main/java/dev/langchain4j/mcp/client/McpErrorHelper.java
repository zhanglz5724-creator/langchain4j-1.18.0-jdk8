/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.mcp.client.McpException;

class McpErrorHelper {
    McpErrorHelper() {
    }

    static void checkForErrors(JsonNode mcpMessage) {
        if (mcpMessage.has("error")) {
            JsonNode errorNode = mcpMessage.get("error");
            throw new McpException(errorNode.get("code").asInt(), errorNode.get("message").asText());
        }
    }
}

