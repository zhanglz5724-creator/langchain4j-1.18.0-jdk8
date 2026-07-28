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

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
class EmbeddingRequest {
    private String model;
    private List<String> input;
    private Integer dimensions;

    EmbeddingRequest() {
    }

    EmbeddingRequest(String model, List<String> input) {
        this(model, input, null);
    }

    EmbeddingRequest(String model, List<String> input, Integer dimensions) {
        this.model = model;
        this.input = input;
        this.dimensions = dimensions;
    }

    static Builder builder() {
        return new Builder();
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<String> getInput() {
        return this.input;
    }

    public void setInput(List<String> input) {
        this.input = input;
    }

    public Integer getDimensions() {
        return this.dimensions;
    }

    public void setDimension(Integer dimensions) {
        this.dimensions = dimensions;
    }

    static class Builder {
        private String model;
        private List<String> input;
        private Integer dimensions;

        Builder() {
        }

        Builder model(String model) {
            this.model = model;
            return this;
        }

        Builder input(List<String> input) {
            this.input = input;
            return this;
        }

        Builder dimensions(Integer dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        EmbeddingRequest build() {
            return new EmbeddingRequest(this.model, this.input, this.dimensions);
        }
    }
}

