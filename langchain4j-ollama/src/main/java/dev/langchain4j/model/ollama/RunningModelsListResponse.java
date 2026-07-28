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
import dev.langchain4j.model.ollama.RunningOllamaModel;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
class RunningModelsListResponse {
    private List<RunningOllamaModel> models;

    RunningModelsListResponse() {
    }

    RunningModelsListResponse(List<RunningOllamaModel> models) {
        this.models = models;
    }

    public List<RunningOllamaModel> getModels() {
        return this.models;
    }

    public void setModels(List<RunningOllamaModel> models) {
        this.models = models;
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private List<RunningOllamaModel> models;

        Builder() {
        }

        Builder models(List<RunningOllamaModel> models) {
            this.models = models;
            return this;
        }

        RunningModelsListResponse build() {
            return new RunningModelsListResponse(this.models);
        }
    }
}

