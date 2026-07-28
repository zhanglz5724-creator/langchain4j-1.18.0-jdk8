/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.mcp.client.McpResourceContents;
import java.util.Base64;
import java.util.Objects;

public final class McpBlobResourceContents
implements McpResourceContents {
    private final String uri;
    private final String blob;
    private final String mimeType;

    public static McpBlobResourceContents create(String uri, String blob) {
        return new McpBlobResourceContents(uri, blob, null);
    }

    public static McpBlobResourceContents create(String uri, byte[] blob) {
        return new McpBlobResourceContents(uri, Base64.getMimeEncoder().encodeToString(blob), null);
    }

    @JsonCreator
    public McpBlobResourceContents(@JsonProperty(value="uri") String uri, @JsonProperty(value="blob") String blob, @JsonProperty(value="mimeType") String mimeType) {
        ValidationUtils.ensureNotNull((Object)uri, (String)"uri");
        ValidationUtils.ensureNotNull((Object)blob, (String)"blob");
        this.uri = uri;
        this.blob = blob;
        this.mimeType = mimeType;
    }

    @Override
    public McpResourceContents.Type type() {
        return McpResourceContents.Type.BLOB;
    }

    public String uri() {
        return this.uri;
    }

    public String blob() {
        return this.blob;
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
        McpBlobResourceContents that = (McpBlobResourceContents)obj;
        return Objects.equals(this.uri, that.uri) && Objects.equals(this.blob, that.blob) && Objects.equals(this.mimeType, that.mimeType);
    }

    public int hashCode() {
        return Objects.hash(this.uri, this.blob, this.mimeType);
    }

    public String toString() {
        return "McpBlobResourceContents[uri=" + this.uri + ", blob=" + this.blob + ", mimeType=" + this.mimeType + ']';
    }
}

