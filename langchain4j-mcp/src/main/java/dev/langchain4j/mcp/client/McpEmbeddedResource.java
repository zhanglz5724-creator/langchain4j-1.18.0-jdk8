/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.TextContent
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.mcp.client.McpPromptContent;
import dev.langchain4j.mcp.client.McpResourceContents;
import dev.langchain4j.mcp.client.McpTextResourceContents;
import java.util.Objects;

public final class McpEmbeddedResource
implements McpPromptContent {
    private final McpResourceContents resource;

    @JsonCreator
    public McpEmbeddedResource(@JsonProperty(value="resource") McpResourceContents resource) {
        this.resource = resource;
    }

    @Override
    public McpPromptContent.Type type() {
        return McpPromptContent.Type.RESOURCE;
    }

    @Override
    public Content toContent() {
        if (this.resource.type().equals((Object)McpResourceContents.Type.TEXT)) {
            return TextContent.from((String)((McpTextResourceContents)this.resource).text());
        }
        throw new UnsupportedOperationException("Representing blob embedded resources as Content is currently not supported");
    }

    public McpResourceContents resource() {
        return this.resource;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        McpEmbeddedResource that = (McpEmbeddedResource)obj;
        return Objects.equals(this.resource, that.resource);
    }

    public int hashCode() {
        return Objects.hash(this.resource);
    }

    public String toString() {
        return "McpEmbeddedResource[resource=" + this.resource + ']';
    }
}

