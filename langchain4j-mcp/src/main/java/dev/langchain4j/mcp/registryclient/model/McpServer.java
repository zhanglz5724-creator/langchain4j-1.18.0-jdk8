/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAlias
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.mcp.registryclient.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.mcp.registryclient.model.McpMeta;
import dev.langchain4j.mcp.registryclient.model.McpPackage;
import dev.langchain4j.mcp.registryclient.model.McpRemote;
import dev.langchain4j.mcp.registryclient.model.McpRepository;
import java.util.List;

public class McpServer {
    private String name;
    private String description;
    @JsonProperty(value="$schema")
    private String schema;
    private String status;
    private McpRepository repository;
    private String version;
    @JsonAlias(value={"website_url"})
    private String websiteUrl;
    private List<McpRemote> remotes;
    @JsonProperty(value="_meta")
    private McpMeta meta;
    private List<McpPackage> packages;

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getSchema() {
        return this.schema;
    }

    @Deprecated
    public String getStatus() {
        return this.status;
    }

    public McpRepository getRepository() {
        return this.repository;
    }

    public String getVersion() {
        return this.version;
    }

    public String getWebsiteUrl() {
        return this.websiteUrl;
    }

    public List<McpRemote> getRemotes() {
        return this.remotes;
    }

    public McpMeta getMeta() {
        return this.meta;
    }

    public List<McpPackage> getPackages() {
        return this.packages;
    }

    public String toString() {
        return "McpServer{name='" + this.name + '\'' + ", description='" + this.description + '\'' + ", schema='" + this.schema + '\'' + ", status='" + this.status + '\'' + ", repository=" + this.repository + ", version='" + this.version + '\'' + ", websiteUrl='" + this.websiteUrl + '\'' + ", remotes=" + this.remotes + ", meta=" + this.meta + ", packages=" + this.packages + '}';
    }
}

