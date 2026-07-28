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
import dev.langchain4j.model.ollama.OllamaModel;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
class ModelsListResponse {
    private List<OllamaModel> models;

    ModelsListResponse() {
    }

    ModelsListResponse(List<OllamaModel> models) {
        this.models = models;
    }

    static Builder builder() {
        return new Builder();
    }

    public List<OllamaModel> getModels() {
        return this.models;
    }

    public void setModels(List<OllamaModel> models) {
        this.models = models;
    }

    static class Builder {
        private List<OllamaModel> models;

        Builder() {
        }

        Builder models(List<OllamaModel> models) {
            this.models = models;
            return this;
        }

        ModelsListResponse build() {
            return new ModelsListResponse(this.models);
        }
    }
}

