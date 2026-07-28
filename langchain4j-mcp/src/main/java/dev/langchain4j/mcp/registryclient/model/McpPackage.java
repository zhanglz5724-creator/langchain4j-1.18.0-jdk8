/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAlias
 */
package dev.langchain4j.mcp.registryclient.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import dev.langchain4j.mcp.registryclient.model.McpEnvironmentVariable;
import dev.langchain4j.mcp.registryclient.model.McpPackageArgument;
import dev.langchain4j.mcp.registryclient.model.McpRuntimeArgument;
import dev.langchain4j.mcp.registryclient.model.McpTransport;
import java.util.List;

public class McpPackage {
    @JsonAlias(value={"file_sha256"})
    private String fileSha256;
    private String identifier;
    private String registryBaseUrl;
    @JsonAlias(value={"registry_type"})
    private String registryType;
    private String runtimeHint;
    private String version;
    private McpTransport transport;
    private List<McpRuntimeArgument> runtimeArguments;
    private List<McpPackageArgument> packageArguments;
    private List<McpEnvironmentVariable> environmentVariables;

    public String getFileSha256() {
        return this.fileSha256;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getRegistryBaseUrl() {
        return this.registryBaseUrl;
    }

    public String getRegistryType() {
        return this.registryType;
    }

    public String getRuntimeHint() {
        return this.runtimeHint;
    }

    public String getVersion() {
        return this.version;
    }

    public McpTransport getTransport() {
        return this.transport;
    }

    public List<McpRuntimeArgument> getRuntimeArguments() {
        return this.runtimeArguments;
    }

    public List<McpPackageArgument> getPackageArguments() {
        return this.packageArguments;
    }

    public List<McpEnvironmentVariable> getEnvironmentVariables() {
        return this.environmentVariables;
    }

    public String toString() {
        return "McpPackage{fileSha256='" + this.fileSha256 + '\'' + ", identifier='" + this.identifier + '\'' + ", registryBaseUrl='" + this.registryBaseUrl + '\'' + ", registryType='" + this.registryType + '\'' + ", runtimeHint='" + this.runtimeHint + '\'' + ", version='" + this.version + '\'' + ", transport=" + this.transport + ", runtimeArguments=" + this.runtimeArguments + ", packageArguments=" + this.packageArguments + ", environmentVariables=" + this.environmentVariables + '}';
    }
}

