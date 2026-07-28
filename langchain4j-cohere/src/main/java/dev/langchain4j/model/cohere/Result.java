/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.cohere;

class Result {
    private Integer index;
    private Double relevanceScore;

    Result() {
    }

    public Integer getIndex() {
        return this.index;
    }

    public Double getRelevanceScore() {
        return this.relevanceScore;
    }
}

