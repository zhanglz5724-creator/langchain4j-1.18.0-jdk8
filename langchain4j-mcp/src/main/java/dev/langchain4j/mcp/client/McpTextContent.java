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
import java.util.Objects;

public final class McpTextContent
implements McpPromptContent {
    private final String text;

    @JsonCreator
    public McpTextContent(@JsonProperty(value="text") String text) {
        this.text = text;
    }

    @Override
    public McpPromptContent.Type type() {
        return McpPromptContent.Type.TEXT;
    }

    @Override
    public Content toContent() {
        return TextContent.from((String)this.text);
    }

    public String text() {
        return this.text;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        McpTextContent that = (McpTextContent)obj;
        return Objects.equals(this.text, that.text);
    }

    public int hashCode() {
        return Objects.hash(this.text);
    }

    public String toString() {
        return "McpTextContent[text=" + this.text + ']';
    }
}

