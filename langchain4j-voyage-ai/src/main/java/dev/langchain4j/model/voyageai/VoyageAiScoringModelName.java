/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.voyageai;

public enum VoyageAiScoringModelName {
    RERANK_1("rerank-1"),
    RERANK_LITE_1("rerank-lite-1"),
    RERANK_2("rerank-2"),
    RERANK_2_LITE("rerank-2-lite");

    private final String stringValue;

    private VoyageAiScoringModelName(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return this.stringValue;
    }
}

