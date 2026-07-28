/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonSerialize
 */
package dev.langchain4j.model.ollama;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.langchain4j.model.ollama.FormatSerializer;
import dev.langchain4j.model.ollama.Options;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
class CompletionRequest {
    private String model;
    private String system;
    private String prompt;
    private Options options;
    private Integer width;
    private Integer height;
    private Integer steps;
    @JsonSerialize(using=FormatSerializer.class)
    private String format;
    private Boolean stream;

    CompletionRequest() {
    }

    CompletionRequest(String model, String system, String prompt, Options options, Integer width, Integer height, Integer steps, String format, Boolean stream) {
        this.model = model;
        this.system = system;
        this.prompt = prompt;
        this.options = options;
        this.width = width;
        this.height = height;
        this.steps = steps;
        this.format = format;
        this.stream = stream;
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

    public String getSystem() {
        return this.system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Options getOptions() {
        return this.options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getSteps() {
        return this.steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Boolean getStream() {
        return this.stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    static class Builder {
        private String model;
        private String system;
        private String prompt;
        private Options options;
        private Integer width;
        private Integer height;
        private Integer steps;
        private String format;
        private Boolean stream;

        Builder() {
        }

        Builder model(String model) {
            this.model = model;
            return this;
        }

        Builder system(String system) {
            this.system = system;
            return this;
        }

        Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        Builder options(Options options) {
            this.options = options;
            return this;
        }

        Builder width(Integer width) {
            this.width = width;
            return this;
        }

        Builder height(Integer height) {
            this.height = height;
            return this;
        }

        Builder steps(Integer steps) {
            this.steps = steps;
            return this;
        }

        Builder format(String format) {
            this.format = format;
            return this;
        }

        Builder stream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        CompletionRequest build() {
            return new CompletionRequest(this.model, this.system, this.prompt, this.options, this.width, this.height, this.steps, this.format, this.stream);
        }
    }
}

