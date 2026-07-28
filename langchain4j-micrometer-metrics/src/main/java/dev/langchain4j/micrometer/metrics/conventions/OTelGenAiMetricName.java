/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.micrometer.metrics.conventions;

public enum OTelGenAiMetricName {
    OPERATION_DURATION("gen_ai.client.operation.duration"),
    TOKEN_USAGE("gen_ai.client.token.usage");

    private final String value;

    private OTelGenAiMetricName(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}

