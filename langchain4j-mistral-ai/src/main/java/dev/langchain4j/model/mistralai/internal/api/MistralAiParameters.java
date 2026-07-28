/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=MistralAiParametersBuilder.class)
public class MistralAiParameters {
    private String type;
    private Map<String, Map<String, Object>> properties;
    private List<String> required;

    private MistralAiParameters(MistralAiParametersBuilder builder) {
        this.type = builder.type$value;
        this.properties = builder.properties;
        this.required = builder.required;
    }

    private static String $default$type() {
        return "object";
    }

    public String getType() {
        return this.type;
    }

    public Map<String, Map<String, Object>> getProperties() {
        return this.properties;
    }

    public List<String> getRequired() {
        return this.required;
    }

    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.type);
        hash = 31 * hash + Objects.hashCode(this.properties);
        hash = 31 * hash + Objects.hashCode(this.required);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MistralAiParameters other = (MistralAiParameters)obj;
        return Objects.equals(this.type, other.type) && Objects.equals(this.properties, other.properties) && Objects.equals(this.required, other.required);
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiParameters [", "]").add("type=" + this.getType()).add("properties=" + this.getProperties()).add("required=" + this.getRequired()).toString();
    }

    public static MistralAiParametersBuilder builder() {
        return new MistralAiParametersBuilder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MistralAiParametersBuilder {
        private boolean type$set;
        private String type$value;
        private Map<String, Map<String, Object>> properties;
        private List<String> required;

        private MistralAiParametersBuilder() {
        }

        public MistralAiParametersBuilder type(String type) {
            this.type$value = type;
            this.type$set = true;
            return this;
        }

        public MistralAiParametersBuilder properties(Map<String, Map<String, Object>> properties) {
            this.properties = properties;
            return this;
        }

        public MistralAiParametersBuilder required(List<String> required) {
            this.required = required;
            return this;
        }

        public MistralAiParameters build() {
            String type$value = this.type$value;
            if (!this.type$set) {
                this.type$value = MistralAiParameters.$default$type();
            }
            return new MistralAiParameters(this);
        }
    }
}

