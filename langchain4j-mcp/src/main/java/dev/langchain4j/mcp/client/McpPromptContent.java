/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.annotation.JsonSubTypes
 *  com.fasterxml.jackson.annotation.JsonSubTypes$Type
 *  com.fasterxml.jackson.annotation.JsonTypeInfo
 *  com.fasterxml.jackson.annotation.JsonTypeInfo$Id
 *  dev.langchain4j.data.message.Content
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.mcp.client.McpEmbeddedResource;
import dev.langchain4j.mcp.client.McpImageContent;
import dev.langchain4j.mcp.client.McpTextContent;
import java.util.Locale;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="type")
@JsonSubTypes(value={@JsonSubTypes.Type(value=McpTextContent.class, name="text"), @JsonSubTypes.Type(value=McpEmbeddedResource.class, name="resource"), @JsonSubTypes.Type(value=McpImageContent.class, name="image")})
public interface McpPromptContent {
    @JsonProperty(value="type")
    default public String getType() {
        return this.type().toString().toLowerCase(Locale.ROOT);
    }

    public Type type();

    public Content toContent();

    public static enum Type {
        TEXT,
        RESOURCE,
        IMAGE;

    }
}

