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

public class McpPackageArgument {
    private List<String> choices;
    @JsonProperty(value="default")
    private String defaultValue;
    private String description;
    private String format;
    @JsonAlias(value={"is_repeated"})
    private boolean isRepeated;
    @JsonAlias(value={"is_required"})
    private boolean isRequired;
    @JsonAlias(value={"is_secret"})
    private boolean isSecret;
    private String name;
    private String type;
    private String value;
    @JsonAlias(value={"value_hint"})
    private String valueHint;
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

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public Map<String, McpVariable> getVariables() {
        return this.variables;
    }

    public boolean isRepeated() {
        return this.isRepeated;
    }

    public boolean isRequired() {
        return this.isRequired;
    }

    public boolean isSecret() {
        return this.isSecret;
    }

    public String getType() {
        return this.type;
    }

    public String getValueHint() {
        return this.valueHint;
    }

    public String toString() {
        String maskedDefaultValue = this.isSecret ? "<redacted>" : this.defaultValue;
        String maskedValue = this.isSecret ? "<redacted>" : this.value;
        return "McpPackageArgument{choices=" + this.choices + ", defaultValue='" + maskedDefaultValue + '\'' + ", description='" + this.description + '\'' + ", format='" + this.format + '\'' + ", isRepeated=" + this.isRepeated + ", isRequired=" + this.isRequired + ", isSecret=" + this.isSecret + ", name='" + this.name + '\'' + ", type='" + this.type + '\'' + ", value='" + maskedValue + '\'' + ", valueHint='" + this.valueHint + '\'' + ", variables=" + this.variables + '}';
    }
}

