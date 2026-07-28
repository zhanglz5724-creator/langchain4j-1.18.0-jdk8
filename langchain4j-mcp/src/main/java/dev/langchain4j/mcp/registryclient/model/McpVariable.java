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
import java.util.List;

public class McpVariable {
    private List<String> choices;
    @JsonProperty(value="default")
    private String defaultValue;
    private String description;
    private String format;
    @JsonAlias(value={"is_required"})
    private boolean isRequired;
    @JsonAlias(value={"is_secret"})
    private boolean isSecret;
    private String placeholder;
    private String value;

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

    public String getPlaceholder() {
        return this.placeholder;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        String maskedDefaultValue = this.isSecret ? "<redacted>" : this.defaultValue;
        String maskedValue = this.isSecret ? "<redacted>" : this.value;
        return "McpVariable{choices=" + this.choices + ", defaultValue='" + maskedDefaultValue + '\'' + ", description='" + this.description + '\'' + ", format='" + this.format + '\'' + ", isRequired=" + this.isRequired + ", isSecret=" + this.isSecret + ", value='" + maskedValue + '\'' + '}';
    }
}

