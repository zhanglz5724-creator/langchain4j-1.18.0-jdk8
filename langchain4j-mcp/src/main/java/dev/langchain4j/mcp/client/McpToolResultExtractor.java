/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 *  dev.langchain4j.service.tool.ToolExecutionResult
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.service.tool.ToolExecutionResult;

public interface McpToolResultExtractor {
    public ToolExecutionResult extract(JsonNode var1, boolean var2);
}

