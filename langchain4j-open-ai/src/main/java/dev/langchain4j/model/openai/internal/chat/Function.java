/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 */
package dev.langchain4j.model.openai.internal.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonDeserialize(builder= Function.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Function {
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String description;
    @JsonProperty
    private final Boolean strict;
    @JsonProperty
    private final Map<String, Object> parameters;

    public Function(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.strict = builder.strict;
        this.parameters = builder.parameters;
    }

    public String name() {
        return this.name;
    }

    public String description() {
        return this.description;
    }

    public Boolean strict() {
        return this.strict;
    }

    public Map<String, Object> parameters() {
        return this.parameters;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof Function && this.equalTo((Function)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(Function another) {
        return Objects.equals(this.name, another.name) && Objects.equals(this.description, another.description) && Objects.equals(this.strict, another.strict) && Objects.equals(this.parameters, another.parameters);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.name);
        h += (h << 5) + Objects.hashCode(this.description);
        h += (h << 5) + Objects.hashCode(this.strict);
        h += (h << 5) + Objects.hashCode(this.parameters);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "Function{name=" + this.name + ", description=" + this.description + ", strict=" + this.strict + ", parameters=" + this.parameters + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String name;
        private String description;
        private Boolean strict;
        private Map<String, Object> parameters = new HashMap<String, Object>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder strict(Boolean strict) {
            this.strict = strict;
            return this;
        }

        public Builder parameters(Map<String, Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Function build() {
            return new Function(this);
        }
    }
}

