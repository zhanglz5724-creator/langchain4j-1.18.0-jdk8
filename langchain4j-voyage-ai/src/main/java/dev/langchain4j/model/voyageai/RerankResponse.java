/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.voyageai;

import dev.langchain4j.model.voyageai.TokenUsage;
import java.util.List;

class RerankResponse {
    private String object;
    private List<RerankData> data;
    private String model;
    private TokenUsage usage;

    RerankResponse() {
    }

    public String getObject() {
        return this.object;
    }

    public List<RerankData> getData() {
        return this.data;
    }

    public String getModel() {
        return this.model;
    }

    public TokenUsage getUsage() {
        return this.usage;
    }

    static class RerankData {
        private String object;
        private Double relevanceScore;
        private Integer index;

        RerankData() {
        }

        public String getObject() {
            return this.object;
        }

        public Double getRelevanceScore() {
            return this.relevanceScore;
        }

        public Integer getIndex() {
            return this.index;
        }
    }
}

