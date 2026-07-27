/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding;

import dev.langchain4j.model.output.TokenUsage;

public class IngestionResult {
    private final TokenUsage tokenUsage;

    public IngestionResult(TokenUsage tokenUsage) {
        this.tokenUsage = tokenUsage;
    }

    public TokenUsage tokenUsage() {
        return this.tokenUsage;
    }
}

