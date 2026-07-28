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
import dev.langchain4j.model.ollama.Message;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
class OllamaChatResponse {
    private String model;
    private String createdAt;
    private Message message;
    private String doneReason;
    private Boolean done;
    private Integer promptEvalCount;
    private Integer evalCount;
    private String error;

    OllamaChatResponse() {
    }

    OllamaChatResponse(String model, String createdAt, Message message, String doneReason, Boolean done, Integer promptEvalCount, Integer evalCount, String error) {
        this.model = model;
        this.createdAt = createdAt;
        this.message = message;
        this.doneReason = doneReason;
        this.done = done;
        this.promptEvalCount = promptEvalCount;
        this.evalCount = evalCount;
        this.error = error;
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

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getDoneReason() {
        return this.doneReason;
    }

    public void setDoneReason(String doneReason) {
        this.doneReason = doneReason;
    }

    public Boolean getDone() {
        return this.done;
    }

    public void setDone(Boolean done) {
        this.done = done;
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
        private Message message;
        private String doneReason;
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

        Builder message(Message message) {
            this.message = message;
            return this;
        }

        Builder doneReason(String doneReason) {
            this.doneReason = doneReason;
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

        OllamaChatResponse build() {
            return new OllamaChatResponse(this.model, this.createdAt, this.message, this.doneReason, this.done, this.promptEvalCount, this.evalCount, this.error);
        }
    }
}

