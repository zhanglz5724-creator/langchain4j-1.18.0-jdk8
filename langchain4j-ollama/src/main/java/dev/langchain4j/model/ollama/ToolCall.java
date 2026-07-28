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
import dev.langchain4j.model.ollama.FunctionCall;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
class ToolCall {
    private String id;
    private FunctionCall function;

    ToolCall() {
    }

    ToolCall(String id, FunctionCall function) {
        this.id = id;
        this.function = function;
    }

    static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FunctionCall getFunction() {
        return this.function;
    }

    public void setFunction(FunctionCall function) {
        this.function = function;
    }

    static class Builder {
        private String id;
        private FunctionCall function;

        Builder() {
        }

        Builder id(String id) {
            this.id = id;
            return this;
        }

        Builder function(FunctionCall function) {
            this.function = function;
            return this;
        }

        ToolCall build() {
            return new ToolCall(this.id, this.function);
        }
    }
}

