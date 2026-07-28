/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.mcp.registryclient;

import dev.langchain4j.mcp.registryclient.model.McpGetServerResponse;
import dev.langchain4j.mcp.registryclient.model.McpRegistryHealth;
import dev.langchain4j.mcp.registryclient.model.McpRegistryPong;
import dev.langchain4j.mcp.registryclient.model.McpServerList;
import dev.langchain4j.mcp.registryclient.model.McpServerListRequest;

public interface McpRegistryClient {
    public McpServerList listServers(McpServerListRequest var1);

    @Deprecated
    public McpGetServerResponse getServerDetails(String var1);

    public McpGetServerResponse getSpecificServerVersion(String var1, String var2);

    public McpServerList getAllVersionsOfServer(String var1);

    public McpRegistryHealth healthCheck();

    public McpRegistryPong ping();
}

