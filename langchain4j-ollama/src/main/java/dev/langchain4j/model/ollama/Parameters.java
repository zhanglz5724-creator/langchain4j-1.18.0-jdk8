/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.ollama;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
class Parameters {
    private String type;
    private Map<String, Map<String, Object>> properties;
    private List<String> required;

    Parameters() {
    }

    Parameters(String type, Map<String, Map<String, Object>> properties, List<String> required) {
        this.type = type;
        this.properties = properties;
        this.required = required;
    }

    static Builder builder() {
        return new Builder();
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Map<String, Object>> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, Map<String, Object>> properties) {
        this.properties = properties;
    }

    public List<String> getRequired() {
        return this.required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }

    static class Builder {
        private String type = "object";
        private Map<String, Map<String, Object>> properties;
        private List<String> required;

        Builder() {
        }

        Builder type(String type) {
            this.type = type;
            return this;
        }

        Builder properties(Map<String, Map<String, Object>> properties) {
            this.properties = properties;
            return this;
        }

        Builder required(List<String> required) {
            this.required = required;
            return this;
        }

        Parameters build() {
            return new Parameters(this.type, this.properties, this.required);
        }
    }
}

