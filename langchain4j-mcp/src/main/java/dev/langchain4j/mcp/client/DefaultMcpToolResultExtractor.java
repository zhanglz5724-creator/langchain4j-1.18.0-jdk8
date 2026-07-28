/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 *  dev.langchain4j.service.tool.ToolExecutionResult
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.mcp.client.McpToolResultExtractor;
import dev.langchain4j.service.tool.ToolExecutionResult;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DefaultMcpToolResultExtractor
implements McpToolResultExtractor {
    @Override
    public ToolExecutionResult extract(JsonNode content, boolean isError) {
        String resultText = StreamSupport.stream(content.spliterator(), false).map(this::extractText).collect(Collectors.joining("\n"));
        return ToolExecutionResult.builder().isError(isError).resultText(resultText).build();
    }

    private String extractText(JsonNode contentItem) {
        if (!contentItem.get("type").asText().equals("text")) {
            throw new RuntimeException("Unsupported content type: " + contentItem.get("type"));
        }
        return contentItem.get("text").asText();
    }
}

