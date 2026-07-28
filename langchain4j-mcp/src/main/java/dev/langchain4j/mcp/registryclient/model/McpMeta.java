/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.JsonNode
 */
package dev.langchain4j.mcp.registryclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.mcp.registryclient.model.McpOfficialMeta;
import java.util.Map;

public class McpMeta {
    @JsonProperty(value="io.modelcontextprotocol.registry/official")
    private McpOfficialMeta official;
    @JsonProperty(value="io.modelcontextprotocol.registry/publisher-provided")
    private Map<String, JsonNode> publisherProvided;

    public McpOfficialMeta getOfficial() {
        return this.official;
    }

    public Map<String, JsonNode> getPublisherProvided() {
        return this.publisherProvided;
    }

    public String toString() {
        return "McpMeta{official=" + this.official + ", publisherProvided=" + this.publisherProvided + '}';
    }
}

