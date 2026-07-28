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
import dev.langchain4j.model.mistralai.internal.api.MistralAiParameters;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=MistralAiFunctionBuilder.class)
public class MistralAiFunction {
    private String name;
    private String description;
    private MistralAiParameters parameters;

    private MistralAiFunction(MistralAiFunctionBuilder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.parameters = builder.parameters;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public MistralAiParameters getParameters() {
        return this.parameters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + Objects.hashCode(this.description);
        hash = 79 * hash + Objects.hashCode(this.parameters);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MistralAiFunction other = (MistralAiFunction)obj;
        return Objects.equals(this.name, other.name) && Objects.equals(this.description, other.description) && Objects.equals(this.parameters, other.parameters);
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiFunction [", "]").add("name=" + this.getName()).add("description=" + this.getDescription()).add("parameters=" + this.getParameters()).toString();
    }

    public static MistralAiFunctionBuilder builder() {
        return new MistralAiFunctionBuilder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MistralAiFunctionBuilder {
        private String name;
        private String description;
        private MistralAiParameters parameters;

        private MistralAiFunctionBuilder() {
        }

        public MistralAiFunctionBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MistralAiFunctionBuilder description(String description) {
            this.description = description;
            return this;
        }

        public MistralAiFunctionBuilder parameters(MistralAiParameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public MistralAiFunction build() {
            return new MistralAiFunction(this);
        }
    }
}

