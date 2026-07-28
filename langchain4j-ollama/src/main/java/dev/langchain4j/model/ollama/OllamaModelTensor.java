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
public class OllamaModelTensor {
    private String name;
    private String type;
    private List<Long> shape;

    OllamaModelTensor() {
    }

    public OllamaModelTensor(String name, String type, List<Long> shape) {
        this.name = name;
        this.type = type;
        this.shape = shape;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Long> getShape() {
        return this.shape;
    }

    public void setShape(List<Long> shape) {
        this.shape = shape;
    }

    public static class Builder {
        private String name;
        private String type;
        private List<Long> shape;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder shape(List<Long> shape) {
            this.shape = shape;
            return this;
        }

        public OllamaModelTensor build() {
            return new OllamaModelTensor(this.name, this.type, this.shape);
        }
    }
}

