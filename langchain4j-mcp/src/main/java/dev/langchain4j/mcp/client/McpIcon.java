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
import dev.langchain4j.mcp.client.McpIconTheme;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class McpIcon {
    private final String mimeType;
    private final List<String> sizes;
    private final String src;
    private final McpIconTheme theme;

    @JsonCreator
    public McpIcon(@JsonProperty(value="mimeType") String mimeType, @JsonProperty(value="sizes") List<String> sizes, @JsonProperty(value="src") String src, @JsonProperty(value="theme") McpIconTheme theme) {
        this.mimeType = mimeType;
        this.sizes = sizes == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<String>(sizes));
        this.src = src;
        this.theme = theme;
    }

    @JsonProperty(value="mimeType")
    public String mimeType() {
        return this.mimeType;
    }

    @JsonProperty(value="sizes")
    public List<String> sizes() {
        return this.sizes;
    }

    @JsonProperty(value="src")
    public String src() {
        return this.src;
    }

    @JsonProperty(value="theme")
    public McpIconTheme theme() {
        return this.theme;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof McpIcon)) {
            return false;
        }
        McpIcon other = (McpIcon)o;
        if (!Objects.equals(this.mimeType, other.mimeType)) {
            return false;
        }
        if (!Objects.equals(this.sizes, other.sizes)) {
            return false;
        }
        if (!Objects.equals(this.src, other.src)) {
            return false;
        }
        return Objects.equals((Object)this.theme, (Object)other.theme);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mimeType, this.sizes, this.src, this.theme});
    }

    public String toString() {
        return "McpIcon{mimeType=" + Objects.toString(this.mimeType) + ", sizes=" + Objects.toString(this.sizes) + ", src=" + Objects.toString(this.src) + ", theme=" + Objects.toString((Object)this.theme) + "}";
    }
}

