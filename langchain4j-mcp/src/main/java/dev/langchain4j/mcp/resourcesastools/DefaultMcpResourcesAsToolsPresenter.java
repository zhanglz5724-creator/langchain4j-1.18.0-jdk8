/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.service.tool.ToolExecutor
 */
package dev.langchain4j.mcp.resourcesastools;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.resourcesastools.GetResourceToolExecutor;
import dev.langchain4j.mcp.resourcesastools.ListResourcesToolExecutor;
import dev.langchain4j.mcp.resourcesastools.McpResourcesAsToolsPresenter;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.service.tool.ToolExecutor;
import java.util.List;

public class DefaultMcpResourcesAsToolsPresenter
implements McpResourcesAsToolsPresenter {
    private final String nameOfGetResourceTool;
    private final String descriptionOfGetResourceTool;
    private final String descriptionOfMcpServerParameterOfGetResourceTool;
    private final String descriptionOfUriParameterOfGetResourceTool;
    private final String nameOfListResourcesTool;
    private final String descriptionOfListResourcesTool;
    public static final String DEFAULT_NAME_OF_GET_RESOURCE_TOOL = "get_resource";
    public static final String DEFAULT_DESCRIPTION_OF_GET_RESOURCE_TOOL = "Retrieves a resource identified by the MCP server name and the resource URI.The 'list_resources' tool should be called before this one to obtain the list.";
    public static final String DEFAULT_DESCRIPTION_OF_MCP_SERVER_PARAMETER_OF_GET_RESOURCE_TOOL = "The name of the MCP server to get the resource from.";
    public static final String DEFAULT_DESCRIPTION_OF_URI_PARAMETER_OF_GET_RESOURCE_TOOL = "The URI of the resource to get.";
    public static final String DEFAULT_NAME_OF_LIST_RESOURCES_TOOL = "list_resources";
    public static final String DEFAULT_DESCRIPTION_OF_LIST_RESOURCES_TOOL = "Lists all available resources.";

    private DefaultMcpResourcesAsToolsPresenter(Builder builder) {
        this((String)Utils.getOrDefault((Object)builder.nameOfGetResourceTool, (Object)DEFAULT_NAME_OF_GET_RESOURCE_TOOL), (String)Utils.getOrDefault((Object)builder.descriptionOfGetResourceTool, (Object)DEFAULT_DESCRIPTION_OF_GET_RESOURCE_TOOL), (String)Utils.getOrDefault((Object)builder.descriptionOfMcpServerParameterOfGetResourceTool, (Object)DEFAULT_DESCRIPTION_OF_MCP_SERVER_PARAMETER_OF_GET_RESOURCE_TOOL), (String)Utils.getOrDefault((Object)builder.descriptionOfUriParameterOfGetResourceTool, (Object)DEFAULT_DESCRIPTION_OF_URI_PARAMETER_OF_GET_RESOURCE_TOOL), (String)Utils.getOrDefault((Object)builder.nameOfListResourcesTool, (Object)DEFAULT_NAME_OF_LIST_RESOURCES_TOOL), (String)Utils.getOrDefault((Object)builder.descriptionOfListResourcesTool, (Object)DEFAULT_DESCRIPTION_OF_LIST_RESOURCES_TOOL));
    }

    protected DefaultMcpResourcesAsToolsPresenter(String nameOfGetResourceTool, String descriptionOfGetResourceTool, String descriptionOfMcpServerParameterOfGetResourceTool, String descriptionOfUriParameterOfGetResourceTool, String nameOfListResourcesTool, String descriptionOfListResourcesTool) {
        this.nameOfGetResourceTool = nameOfGetResourceTool;
        this.descriptionOfGetResourceTool = descriptionOfGetResourceTool;
        this.descriptionOfMcpServerParameterOfGetResourceTool = descriptionOfMcpServerParameterOfGetResourceTool;
        this.descriptionOfUriParameterOfGetResourceTool = descriptionOfUriParameterOfGetResourceTool;
        this.nameOfListResourcesTool = nameOfListResourcesTool;
        this.descriptionOfListResourcesTool = descriptionOfListResourcesTool;
    }

    @Override
    public ToolSpecification createListResourcesSpecification() {
        return ToolSpecification.builder().name(this.nameOfListResourcesTool).description(this.descriptionOfListResourcesTool).parameters(JsonObjectSchema.builder().build()).build();
    }

    @Override
    public ToolExecutor createListResourcesExecutor(List<McpClient> mcpClients) {
        return new ListResourcesToolExecutor(mcpClients);
    }

    @Override
    public ToolSpecification createGetResourceSpecification() {
        return ToolSpecification.builder().name(this.nameOfGetResourceTool).description(this.descriptionOfGetResourceTool).parameters(JsonObjectSchema.builder().addStringProperty("mcpServer", this.descriptionOfMcpServerParameterOfGetResourceTool).addStringProperty("uri", this.descriptionOfUriParameterOfGetResourceTool).build()).build();
    }

    @Override
    public ToolExecutor createGetResourceExecutor(List<McpClient> mcpClients) {
        return new GetResourceToolExecutor(mcpClients);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String nameOfGetResourceTool;
        private String descriptionOfGetResourceTool;
        private String descriptionOfMcpServerParameterOfGetResourceTool;
        private String descriptionOfUriParameterOfGetResourceTool;
        private String nameOfListResourcesTool;
        private String descriptionOfListResourcesTool;

        public Builder nameOfGetResourceTool(String nameOfGetResourceTool) {
            this.nameOfGetResourceTool = nameOfGetResourceTool;
            return this;
        }

        public Builder descriptionOfGetResourceTool(String descriptionOfGetResourceTool) {
            this.descriptionOfGetResourceTool = descriptionOfGetResourceTool;
            return this;
        }

        public Builder descriptionOfMcpServerParameterOfGetResourceTool(String descriptionOfMcpServerParameterOfGetResourceTool) {
            this.descriptionOfMcpServerParameterOfGetResourceTool = descriptionOfMcpServerParameterOfGetResourceTool;
            return this;
        }

        public Builder descriptionOfUriParameterOfGetResourceTool(String descriptionOfUriParameterOfGetResourceTool) {
            this.descriptionOfUriParameterOfGetResourceTool = descriptionOfUriParameterOfGetResourceTool;
            return this;
        }

        public Builder nameOfListResourcesTool(String nameOfListResourcesTool) {
            this.nameOfListResourcesTool = nameOfListResourcesTool;
            return this;
        }

        public Builder descriptionOfListResourcesTool(String descriptionOfListResourcesTool) {
            this.descriptionOfListResourcesTool = descriptionOfListResourcesTool;
            return this;
        }

        public DefaultMcpResourcesAsToolsPresenter build() {
            return new DefaultMcpResourcesAsToolsPresenter(this);
        }
    }
}

