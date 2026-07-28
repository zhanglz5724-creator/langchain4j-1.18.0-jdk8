/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.mcp.registryclient.model;

import dev.langchain4j.mcp.registryclient.model.McpHeader;
import java.util.List;

public class McpRemote {
    private List<McpHeader> headers;
    private String type;
    private String url;

    public List<McpHeader> getHeaders() {
        return this.headers;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public String toString() {
        return "McpRemote{headers=" + this.headers + ", type='" + this.type + '\'' + ", url='" + this.url + '\'' + '}';
    }
}

