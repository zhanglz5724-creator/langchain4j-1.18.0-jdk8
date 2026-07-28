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
import dev.langchain4j.mcp.client.McpBlobResourceContents;
import dev.langchain4j.mcp.client.McpErrorHelper;
import dev.langchain4j.mcp.client.McpReadResourceResult;
import dev.langchain4j.mcp.client.McpResource;
import dev.langchain4j.mcp.client.McpResourceContents;
import dev.langchain4j.mcp.client.McpResourceTemplate;
import dev.langchain4j.mcp.client.McpTextResourceContents;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ResourcesHelper {
    private static final Logger log = LoggerFactory.getLogger(ResourcesHelper.class);

    ResourcesHelper() {
    }

    static List<McpResource> parseResourceRefs(JsonNode mcpMessage) {
        McpErrorHelper.checkForErrors(mcpMessage);
        if (mcpMessage.has("result")) {
            JsonNode resultNode = mcpMessage.get("result");
            if (resultNode.has("resources")) {
                ArrayList<McpResource> resourceRefs = new ArrayList<McpResource>();
                for (JsonNode resourceNode : resultNode.get("resources")) {
                    resourceRefs.add((McpResource)DefaultMcpClient.OBJECT_MAPPER.convertValue((Object)resourceNode, McpResource.class));
                }
                return resourceRefs;
            }
            log.warn("Result does not contain 'resources' element: {}", (Object)resultNode);
            throw new IllegalResponseException("Result does not contain 'resources' element");
        }
        log.warn("Result does not contain 'result' element: {}", (Object)mcpMessage);
        throw new IllegalResponseException("Result does not contain 'result' element");
    }

    static McpReadResourceResult parseResourceContents(JsonNode mcpMessage) {
        McpErrorHelper.checkForErrors(mcpMessage);
        if (mcpMessage.has("result")) {
            JsonNode resultNode = mcpMessage.get("result");
            if (resultNode.has("contents")) {
                ArrayList<McpResourceContents> resourceContentsList = new ArrayList<McpResourceContents>();
                for (JsonNode resourceNode : resultNode.get("contents")) {
                    String mimeType;
                    String uri = resourceNode.get("uri").asText();
                    String string = mimeType = resourceNode.get("mimeType") != null ? resourceNode.get("mimeType").asText() : null;
                    if (resourceNode.has("text")) {
                        resourceContentsList.add(new McpTextResourceContents(uri, resourceNode.get("text").asText(), mimeType));
                        continue;
                    }
                    if (!resourceNode.has("blob")) continue;
                    resourceContentsList.add(new McpBlobResourceContents(uri, resourceNode.get("blob").asText(), mimeType));
                }
                return new McpReadResourceResult(resourceContentsList);
            }
            log.warn("Result does not contain 'contents' element: {}", (Object)resultNode);
            throw new IllegalResponseException("Result does not contain 'resources' element");
        }
        log.warn("Result does not contain 'result' element: {}", (Object)mcpMessage);
        throw new IllegalResponseException("Result does not contain 'result' element");
    }

    static List<McpResourceTemplate> parseResourceTemplateRefs(JsonNode mcpMessage) {
        McpErrorHelper.checkForErrors(mcpMessage);
        if (mcpMessage.has("result")) {
            JsonNode resultNode = mcpMessage.get("result");
            if (resultNode.has("resourceTemplates")) {
                ArrayList<McpResourceTemplate> resourceTemplateRefs = new ArrayList<McpResourceTemplate>();
                for (JsonNode resourceTemplateNode : resultNode.get("resourceTemplates")) {
                    resourceTemplateRefs.add((McpResourceTemplate)DefaultMcpClient.OBJECT_MAPPER.convertValue((Object)resourceTemplateNode, McpResourceTemplate.class));
                }
                return resourceTemplateRefs;
            }
            log.warn("Result does not contain 'resourceTemplates' element: {}", (Object)resultNode);
            throw new IllegalResponseException("Result does not contain 'resourceTemplates' element");
        }
        log.warn("Result does not contain 'result' element: {}", (Object)mcpMessage);
        throw new IllegalResponseException("Result does not contain 'result' element");
    }
}

