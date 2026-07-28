/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.model.bedrock;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.Internal;

@Internal
class BedrockCohereEmbeddingResponse {
    private Embeddings embeddings;
    private Integer inputTextTokenCount;

    BedrockCohereEmbeddingResponse() {
    }

    public Embeddings getEmbeddings() {
        return this.embeddings;
    }

    public void setEmbeddings(Embeddings embeddings) {
        this.embeddings = embeddings;
    }

    public Integer getInputTextTokenCount() {
        return this.inputTextTokenCount;
    }

    public void setInputTextTokenCount(Integer inputTextTokenCount) {
        this.inputTextTokenCount = inputTextTokenCount;
    }

    static class Embeddings {
        @JsonProperty(value="float")
        private float[][] floatEmbeddings;

        Embeddings() {
        }

        public float[][] getFloatEmbeddings() {
            return this.floatEmbeddings;
        }

        public void setFloatEmbeddings(float[][] floatEmbeddings) {
            this.floatEmbeddings = floatEmbeddings;
        }
    }
}

