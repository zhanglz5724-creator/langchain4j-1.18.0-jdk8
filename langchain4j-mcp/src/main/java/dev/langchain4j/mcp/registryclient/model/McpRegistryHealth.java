/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.mcp.registryclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class McpRegistryHealth {
    @JsonProperty(value="github_client_id")
    private String githubClientId;
    private String status;

    public String getGithubClientId() {
        return this.githubClientId;
    }

    public String getStatus() {
        return this.status;
    }

    public String toString() {
        return "McpRegistryHealth{githubClientId='" + this.githubClientId + '\'' + ", status='" + this.status + '\'' + '}';
    }
}

