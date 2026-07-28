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
import dev.langchain4j.mcp.registryclient.model.McpVariable;
import java.util.List;
import java.util.Map;

public class McpEnvironmentVariable {
    private List<String> choices;
    @JsonProperty(value="default")
    private String defaultValue;
    private String description;
    private String format;
    @JsonAlias(value={"is_required"})
    private boolean isRequired;
    @JsonAlias(value={"is_secret"})
    private boolean isSecret;
    private String name;
    private String value;
    private Map<String, McpVariable> variables;

    public List<String> getChoices() {
        return this.choices;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFormat() {
        return this.format;
    }

    public boolean isRequired() {
        return this.isRequired;
    }

    public boolean isSecret() {
        return this.isSecret;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public Map<String, McpVariable> getVariables() {
        return this.variables;
    }

    public String toString() {
        return "McpEnvironmentVariable{choices=" + this.choices + ", defaultValue='" + this.defaultValue + '\'' + ", description='" + this.description + '\'' + ", format='" + this.format + '\'' + ", isRequired=" + this.isRequired + ", isSecret=" + this.isSecret + ", name='" + this.name + '\'' + ", value='" + this.value + '\'' + ", variables=" + this.variables + '}';
    }
}

