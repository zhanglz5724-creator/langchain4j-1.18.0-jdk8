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
import dev.langchain4j.model.mistralai.internal.api.MistralAiFunction;
import dev.langchain4j.model.mistralai.internal.api.MistralAiToolType;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=MistralAiToolBuilder.class)
public class MistralAiTool {
    private MistralAiToolType type;
    private MistralAiFunction function;

    private MistralAiTool(MistralAiToolBuilder builder) {
        this.type = builder.type;
        this.function = builder.function;
    }

    public MistralAiToolType getType() {
        return this.type;
    }

    public MistralAiFunction getFunction() {
        return this.function;
    }

    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode((Object)this.type);
        hash = 37 * hash + Objects.hashCode(this.function);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MistralAiTool other = (MistralAiTool)obj;
        return this.type == other.type && Objects.equals(this.function, other.function);
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiTool [", "]").add("type=" + (Object)((Object)this.getType())).add("function=" + this.getFunction()).toString();
    }

    public static MistralAiTool from(MistralAiFunction function) {
        return MistralAiTool.builder().type(MistralAiToolType.FUNCTION).function(function).build();
    }

    public static MistralAiToolBuilder builder() {
        return new MistralAiToolBuilder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MistralAiToolBuilder {
        private MistralAiToolType type;
        private MistralAiFunction function;

        private MistralAiToolBuilder() {
        }

        public MistralAiToolBuilder type(MistralAiToolType type) {
            this.type = type;
            return this;
        }

        public MistralAiToolBuilder function(MistralAiFunction function) {
            this.function = function;
            return this;
        }

        public MistralAiTool build() {
            return new MistralAiTool(this);
        }
    }
}

