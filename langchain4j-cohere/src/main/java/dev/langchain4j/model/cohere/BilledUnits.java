/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.cohere;

class BilledUnits {
    private Integer inputTokens;
    private Integer outputTokens;
    private Integer searchUnits;

    BilledUnits() {
    }

    public Integer getInputTokens() {
        return this.inputTokens;
    }

    public Integer getOutputTokens() {
        return this.outputTokens;
    }

    public Integer getSearchUnits() {
        return this.searchUnits;
    }
}

