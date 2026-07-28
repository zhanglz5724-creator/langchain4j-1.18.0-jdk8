/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ImageContent
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.mcp.client.McpPromptContent;
import java.util.Objects;

public final class McpImageContent
implements McpPromptContent {
    private final String data;
    private final String mimeType;

    @JsonCreator
    public McpImageContent(@JsonProperty(value="data") String data, @JsonProperty(value="mimeType") String mimeType) {
        this.data = data;
        this.mimeType = mimeType;
    }

    @Override
    public McpPromptContent.Type type() {
        return McpPromptContent.Type.IMAGE;
    }

    @Override
    public Content toContent() {
        return ImageContent.from((String)this.data, (String)this.mimeType);
    }

    public String data() {
        return this.data;
    }

    public String mimeType() {
        return this.mimeType;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        McpImageContent that = (McpImageContent)obj;
        return Objects.equals(this.data, that.data) && Objects.equals(this.mimeType, that.mimeType);
    }

    public int hashCode() {
        return Objects.hash(this.data, this.mimeType);
    }

    public String toString() {
        return "McpImageContent[data=" + this.data + ", mimeType=" + this.mimeType + ']';
    }
}

