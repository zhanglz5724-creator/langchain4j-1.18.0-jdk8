/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.exception.ToolExecutionException
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.service.tool.ToolExecutionResult
 *  dev.langchain4j.service.tool.ToolExecutor
 */
package dev.langchain4j.mcp.resourcesastools;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.exception.ToolExecutionException;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Json;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.McpResource;
import dev.langchain4j.mcp.client.McpResourceTemplate;
import dev.langchain4j.service.tool.ToolExecutionResult;
import dev.langchain4j.service.tool.ToolExecutor;
import java.util.ArrayList;
import java.util.List;

class ListResourcesToolExecutor
implements ToolExecutor {
    private final List<McpClient> mcpClients;

    ListResourcesToolExecutor(List<McpClient> mcpClients) {
        this.mcpClients = mcpClients;
    }

    public ToolExecutionResult executeWithContext(ToolExecutionRequest request, InvocationContext invocationContext) {
        try {
            return ToolExecutionResult.builder().resultText(this.doExecute(invocationContext)).build();
        }
        catch (Exception e) {
            throw new ToolExecutionException(Exceptions.unwrapRuntimeException((Exception)e));
        }
    }

    public String execute(ToolExecutionRequest toolExecutionRequest, Object memoryId) {
        return this.executeWithContext(toolExecutionRequest, null).resultText();
    }

    private String doExecute(InvocationContext invocationContext) {
        ArrayList<ResourceDescription> descriptions = new ArrayList<ResourceDescription>();
        for (McpClient client : this.mcpClients) {
            for (McpResource resource : client.listResources(invocationContext)) {
                descriptions.add(new ResourceDescription(client.key(), resource.uri(), null, resource.name(), resource.description(), resource.mimeType()));
            }
            for (McpResourceTemplate template : client.listResourceTemplates()) {
                descriptions.add(new ResourceDescription(client.key(), null, template.uriTemplate(), template.name(), template.description(), template.mimeType()));
            }
        }
        return Json.toJson(descriptions);
    }

    private static class ResourceDescription {
        String mcpServer;
        String uri;
        String uriTemplate;
        String name;
        String description;
        String mimeType;

        ResourceDescription(String mcpServer, String uri, String uriTemplate, String name, String description, String mimeType) {
            this.mcpServer = mcpServer;
            this.uri = uri;
            this.uriTemplate = uriTemplate;
            this.name = name;
            this.description = description;
            this.mimeType = mimeType;
        }
    }
}

