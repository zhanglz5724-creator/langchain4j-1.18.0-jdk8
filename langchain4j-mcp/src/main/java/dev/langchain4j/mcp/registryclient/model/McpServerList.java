/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.mcp.registryclient.model;

import dev.langchain4j.mcp.registryclient.model.McpGetServerResponse;
import dev.langchain4j.mcp.registryclient.model.McpMetadata;
import java.util.List;

public class McpServerList {
    private List<McpGetServerResponse> servers;
    private McpMetadata metadata;

    public List<McpGetServerResponse> getServers() {
        return this.servers;
    }

    public McpMetadata getMetadata() {
        return this.metadata;
    }

    public String toString() {
        return "McpServerList{servers=" + this.servers + ", metadata=" + this.metadata + '}';
    }
}

