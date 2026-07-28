/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.node.ObjectNode
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.exception.ToolArgumentsException
 *  dev.langchain4j.exception.ToolExecutionException
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.service.tool.ToolExecutionResult
 *  dev.langchain4j.service.tool.ToolExecutor
 */
package dev.langchain4j.mcp.resourcesastools;

import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.exception.ToolArgumentsException;
import dev.langchain4j.exception.ToolExecutionException;
import dev.langchain4j.internal.Json;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.McpResourceContents;
import dev.langchain4j.mcp.client.McpTextResourceContents;
import dev.langchain4j.service.tool.ToolExecutionResult;
import dev.langchain4j.service.tool.ToolExecutor;
import java.util.List;
import java.util.Optional;

class GetResourceToolExecutor
implements ToolExecutor {
    private final List<McpClient> mcpClients;

    GetResourceToolExecutor(List<McpClient> mcpClients) {
        this.mcpClients = mcpClients;
    }

    public ToolExecutionResult executeWithContext(ToolExecutionRequest toolExecutionRequest, InvocationContext context) {
        ObjectNode arguments = GetResourceToolExecutor.parseArguments(toolExecutionRequest);
        if (!arguments.has("mcpServer")) {
            throw new ToolArgumentsException("ERROR: missing argument 'mcpServer'");
        }
        String mcpServerKey = arguments.get("mcpServer").asText();
        if (!arguments.has("uri")) {
            throw new ToolArgumentsException("ERROR: missing argument 'uri'");
        }
        String uri = arguments.get("uri").asText();
        Optional<McpClient> client = this.mcpClients.stream().filter(mcpClient -> mcpClient.key().equals(mcpServerKey)).findFirst();
        if (!client.isPresent()) {
            throw new ToolArgumentsException("ERROR: unknown MCP server: " + mcpServerKey);
        }
        StringBuilder result = new StringBuilder();
        List<McpResourceContents> contents = client.get().readResource(uri, context).contents();
        for (McpResourceContents content : contents) {
            if (content instanceof McpTextResourceContents) {
                result.append(((McpTextResourceContents)content).text());
                continue;
            }
            throw new ToolExecutionException("ERROR: binary content was requested, this is not supported yet");
        }
        return ToolExecutionResult.builder().resultText(result.toString()).build();
    }

    public String execute(ToolExecutionRequest toolExecutionRequest, Object memoryId) {
        return this.executeWithContext(toolExecutionRequest, null).resultText();
    }

    private static ObjectNode parseArguments(ToolExecutionRequest toolExecutionRequest) {
        try {
            return (ObjectNode)Json.fromJson((String)toolExecutionRequest.arguments(), ObjectNode.class);
        }
        catch (Exception e) {
            throw new ToolArgumentsException(e.getCause());
        }
    }
}

