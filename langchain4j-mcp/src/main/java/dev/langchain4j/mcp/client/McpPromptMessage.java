/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.UserMessage
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.mcp.client.McpPromptContent;
import dev.langchain4j.mcp.client.McpRole;
import java.util.Objects;

public class McpPromptMessage {
    private final McpRole role;
    private final McpPromptContent content;

    @JsonCreator
    public McpPromptMessage(@JsonProperty(value="role") McpRole role, @JsonProperty(value="content") McpPromptContent content) {
        this.role = role;
        this.content = content;
    }

    public ChatMessage toChatMessage() {
        if (this.role.equals((Object)McpRole.USER)) {
            return UserMessage.userMessage((Content[])new Content[]{this.content.toContent()});
        }
        if (this.role.equals((Object)McpRole.ASSISTANT)) {
            Content convertedContent = this.content.toContent();
            if (convertedContent instanceof TextContent) {
                TextContent convertedTextContent = (TextContent)convertedContent;
                return AiMessage.aiMessage((String)convertedTextContent.text());
            }
            throw new UnsupportedOperationException("Cannot create an AiMessage with content of type " + convertedContent.getClass().getName());
        }
        throw new UnsupportedOperationException("Unknown role: " + (Object)((Object)this.role));
    }

    public McpRole role() {
        return this.role;
    }

    public McpPromptContent content() {
        return this.content;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        McpPromptMessage that = (McpPromptMessage)obj;
        return Objects.equals((Object)this.role, (Object)that.role) && Objects.equals(this.content, that.content);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.role, this.content});
    }

    public String toString() {
        return "McpPromptMessage[role=" + (Object)((Object)this.role) + ", content=" + this.content + ']';
    }
}

