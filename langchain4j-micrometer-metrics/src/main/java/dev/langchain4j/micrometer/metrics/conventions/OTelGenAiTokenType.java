/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.micrometer.metrics.conventions;

public enum OTelGenAiTokenType {
    INPUT("input"),
    OUTPUT("output");

    private final String value;

    private OTelGenAiTokenType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}

