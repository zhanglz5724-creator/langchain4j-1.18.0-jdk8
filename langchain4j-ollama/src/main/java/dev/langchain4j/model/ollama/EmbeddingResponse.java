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
class EmbeddingResponse {
    private String model;
    private List<float[]> embeddings;
    private Integer promptEvalCount;

    EmbeddingResponse() {
    }

    EmbeddingResponse(String model, List<float[]> embeddings) {
        this.model = model;
        this.embeddings = embeddings;
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

    public List<float[]> getEmbeddings() {
        return this.embeddings;
    }

    public void setEmbeddings(List<float[]> embeddings) {
        this.embeddings = embeddings;
    }

    public Integer getPromptEvalCount() {
        return this.promptEvalCount;
    }

    public void setPromptEvalCount(Integer promptEvalCount) {
        this.promptEvalCount = promptEvalCount;
    }

    static class Builder {
        private String model;
        private List<float[]> embeddings;

        Builder() {
        }

        Builder model(String model) {
            this.model = model;
            return this;
        }

        Builder embeddings(List<float[]> embeddings) {
            this.embeddings = embeddings;
            return this;
        }

        EmbeddingResponse build() {
            return new EmbeddingResponse(this.model, this.embeddings);
        }
    }
}

