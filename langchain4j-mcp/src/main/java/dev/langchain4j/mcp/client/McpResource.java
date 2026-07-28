/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.mcp.client.McpIcon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class McpResource {
    private final String uri;
    private final String name;
    private final String description;
    private final String mimeType;
    private final Map<String, Object> metadata;
    private final List<McpIcon> icons;

    public McpResource(@JsonProperty(value="uri") String uri, @JsonProperty(value="name") String name, @JsonProperty(value="description") String description, @JsonProperty(value="mimeType") String mimeType) {
        this(uri, name, description, mimeType, null, null);
    }

    @JsonCreator
    public McpResource(@JsonProperty(value="uri") String uri, @JsonProperty(value="name") String name, @JsonProperty(value="description") String description, @JsonProperty(value="mimeType") String mimeType, @JsonProperty(value="_meta") Map<String, Object> metadata, @JsonProperty(value="icons") List<McpIcon> icons) {
        this.uri = Utils.warnIfNullOrBlank((String)uri, (String)"uri", McpResource.class);
        this.name = Utils.warnIfNullOrBlank((String)name, (String)"name", McpResource.class);
        this.description = description;
        this.mimeType = mimeType;
        this.metadata = Utils.copy(metadata);
        this.icons = icons == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<McpIcon>(icons));
    }

    public String uri() {
        return this.uri;
    }

    public String name() {
        return this.name;
    }

    public String description() {
        return this.description;
    }

    public String mimeType() {
        return this.mimeType;
    }

    public Map<String, Object> metadata() {
        return this.metadata;
    }

    public List<McpIcon> icons() {
        return this.icons;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        McpResource that = (McpResource)obj;
        return Objects.equals(this.uri, that.uri) && Objects.equals(this.name, that.name) && Objects.equals(this.description, that.description) && Objects.equals(this.mimeType, that.mimeType) && Objects.equals(this.metadata, that.metadata) && Objects.equals(this.icons, that.icons);
    }

    public int hashCode() {
        return Objects.hash(this.uri, this.name, this.description, this.mimeType, this.metadata, this.icons);
    }

    public String toString() {
        return "McpResource[uri=" + this.uri + ", name=" + this.name + ", description=" + this.description + ", mimeType=" + this.mimeType + ", metadata=" + this.metadata + ", icons=" + this.icons + ']';
    }
}

