/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 */
package dev.langchain4j.model.voyageai;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.langchain4j.model.voyageai.TokenUsage;
import dev.langchain4j.model.voyageai.VoyageAiEmbeddingDeserializer;
import java.util.List;

class EmbeddingResponse {
    private String object;
    @JsonDeserialize(using=VoyageAiEmbeddingDeserializer.class)
    private List<EmbeddingData> data;
    private String model;
    private TokenUsage usage;

    EmbeddingResponse() {
    }

    public String getObject() {
        return this.object;
    }

    public List<EmbeddingData> getData() {
        return this.data;
    }

    public String getModel() {
        return this.model;
    }

    public TokenUsage getUsage() {
        return this.usage;
    }

    static class EmbeddingData {
        private String object;
        private List<Float> embedding;
        private Integer index;

        EmbeddingData() {
        }

        EmbeddingData(String object, List<Float> embedding, Integer index) {
            this.object = object;
            this.embedding = embedding;
            this.index = index;
        }

        public String getObject() {
            return this.object;
        }

        public List<Float> getEmbedding() {
            return this.embedding;
        }

        public Integer getIndex() {
            return this.index;
        }
    }
}

