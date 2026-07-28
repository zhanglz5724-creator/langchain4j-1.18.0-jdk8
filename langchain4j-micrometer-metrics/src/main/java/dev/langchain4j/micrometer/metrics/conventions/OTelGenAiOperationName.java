/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.micrometer.metrics.conventions;

public enum OTelGenAiOperationName {
    CHAT("chat"),
    TEXT_COMPLETION("text_completion"),
    EMBEDDINGS("embeddings");

    private final String value;

    private OTelGenAiOperationName(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}

