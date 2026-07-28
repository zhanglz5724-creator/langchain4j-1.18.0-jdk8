/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.mcp.client;

import java.util.Objects;

public class McpRoot {
    private final String name;
    private final String uri;

    public McpRoot(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String name() {
        return this.name;
    }

    public String uri() {
        return this.uri;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof McpRoot)) {
            return false;
        }
        McpRoot other = (McpRoot)o;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.uri, other.uri);
    }

    public int hashCode() {
        return Objects.hash(this.name, this.uri);
    }

    public String toString() {
        return "McpRoot{name=" + Objects.toString(this.name) + ", uri=" + Objects.toString(this.uri) + "}";
    }
}

