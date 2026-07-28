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
import dev.langchain4j.model.ollama.OllamaModelToolCallFunction;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OllamaModelToolCall {
    private OllamaModelToolCallFunction function;

    OllamaModelToolCall() {
    }

    public OllamaModelToolCall(OllamaModelToolCallFunction function) {
        this.function = function;
    }

    public static Builder builder() {
        return new Builder();
    }

    public OllamaModelToolCallFunction getFunction() {
        return this.function;
    }

    public void setFunction(OllamaModelToolCallFunction function) {
        this.function = function;
    }

    public static class Builder {
        private OllamaModelToolCallFunction function;

        public Builder function(OllamaModelToolCallFunction function) {
            this.function = function;
            return this;
        }

        public OllamaModelToolCall build() {
            return new OllamaModelToolCall(this.function);
        }
    }
}

