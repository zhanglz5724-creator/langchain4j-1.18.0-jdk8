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
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=MistralAiFunctionCallBuilder.class)
public class MistralAiFunctionCall {
    private String name;
    private String arguments;

    private MistralAiFunctionCall(MistralAiFunctionCallBuilder builder) {
        this.name = builder.name;
        this.arguments = builder.arguments;
    }

    public String getName() {
        return this.name;
    }

    public String getArguments() {
        return this.arguments;
    }

    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + Objects.hashCode(this.arguments);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MistralAiFunctionCall other = (MistralAiFunctionCall)obj;
        return Objects.equals(this.name, other.name) && Objects.equals(this.arguments, other.arguments);
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiFunctionCall [", "]").add("name=" + this.getName()).add("arguments=" + this.getArguments()).toString();
    }

    public static MistralAiFunctionCallBuilder builder() {
        return new MistralAiFunctionCallBuilder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MistralAiFunctionCallBuilder {
        private String name;
        private String arguments;

        private MistralAiFunctionCallBuilder() {
        }

        public MistralAiFunctionCallBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MistralAiFunctionCallBuilder arguments(String arguments) {
            this.arguments = arguments;
            return this;
        }

        public MistralAiFunctionCall build() {
            return new MistralAiFunctionCall(this);
        }
    }
}

