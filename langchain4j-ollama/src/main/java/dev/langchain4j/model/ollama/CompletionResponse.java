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

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
class CompletionResponse {
    private String model;
    private String createdAt;
    private String response;
    private String image;
    private Boolean done;
    private Integer promptEvalCount;
    private Integer evalCount;
    private String error;

    CompletionResponse() {
    }

    CompletionResponse(String model, String createdAt, String response, String image, Boolean done, Integer promptEvalCount, Integer evalCount, String error) {
        this.model = model;
        this.createdAt = createdAt;
        this.response = response;
        this.image = image;
        this.done = done;
        this.promptEvalCount = promptEvalCount;
        this.evalCount = evalCount;
        this.error = error;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDone() {
        return this.done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPromptEvalCount() {
        return this.promptEvalCount;
    }

    public void setPromptEvalCount(Integer promptEvalCount) {
        this.promptEvalCount = promptEvalCount;
    }

    public Integer getEvalCount() {
        return this.evalCount;
    }

    public void setEvalCount(Integer evalCount) {
        this.evalCount = evalCount;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    static class Builder {
        private String model;
        private String createdAt;
        private String response;
        private String image;
        private Boolean done;
        private Integer promptEvalCount;
        private Integer evalCount;
        private String error;

        Builder() {
        }

        Builder model(String model) {
            this.model = model;
            return this;
        }

        Builder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        Builder response(String response) {
            this.response = response;
            return this;
        }

        Builder image(String image) {
            this.image = image;
            return this;
        }

        Builder done(Boolean done) {
            this.done = done;
            return this;
        }

        Builder promptEvalCount(Integer promptEvalCount) {
            this.promptEvalCount = promptEvalCount;
            return this;
        }

        Builder evalCount(Integer evalCount) {
            this.evalCount = evalCount;
            return this;
        }

        Builder error(String error) {
            this.error = error;
            return this;
        }

        CompletionResponse build() {
            return new CompletionResponse(this.model, this.createdAt, this.response, this.image, this.done, this.promptEvalCount, this.evalCount, this.error);
        }
    }
}

