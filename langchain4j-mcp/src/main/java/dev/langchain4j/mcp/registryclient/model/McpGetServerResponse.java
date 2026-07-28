/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.mcp.registryclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.mcp.registryclient.model.McpMeta;
import dev.langchain4j.mcp.registryclient.model.McpServer;

public class McpGetServerResponse {
    @JsonProperty(value="_meta")
    private McpMeta meta;
    private McpServer server;

    public McpMeta getMeta() {
        return this.meta;
    }

    public McpServer getServer() {
        return this.server;
    }

    public String toString() {
        return "McpGetServerResponse{meta=" + this.meta + ", server=" + this.server + '}';
    }
}

