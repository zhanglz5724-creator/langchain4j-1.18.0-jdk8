/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.mcp.client.McpResourceContents;
import java.util.Objects;

public final class McpTextResourceContents
implements McpResourceContents {
    private final String uri;
    private final String text;
    private final String mimeType;

    @JsonCreator
    public McpTextResourceContents(@JsonProperty(value="uri") String uri, @JsonProperty(value="text") String text, @JsonProperty(value="mimeType") String mimeType) {
        this.uri = uri;
        this.text = text;
        this.mimeType = mimeType;
    }

    @Override
    public McpResourceContents.Type type() {
        return McpResourceContents.Type.TEXT;
    }

    public String uri() {
        return this.uri;
    }

    public String text() {
        return this.text;
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
        McpTextResourceContents that = (McpTextResourceContents)obj;
        return Objects.equals(this.uri, that.uri) && Objects.equals(this.text, that.text) && Objects.equals(this.mimeType, that.mimeType);
    }

    public int hashCode() {
        return Objects.hash(this.uri, this.text, this.mimeType);
    }

    public String toString() {
        return "McpTextResourceContents[uri=" + this.uri + ", text=" + this.text + ", mimeType=" + this.mimeType + ']';
    }
}

