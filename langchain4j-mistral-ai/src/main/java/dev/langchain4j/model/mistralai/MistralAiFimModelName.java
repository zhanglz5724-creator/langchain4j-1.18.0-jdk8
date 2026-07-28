/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.mistralai;

public enum MistralAiFimModelName {
    CODESTRAL_LATEST("codestral-latest"),
    OPEN_CODESTRAL_MAMBA("open-codestral-mamba");

    private final String value;

    private MistralAiFimModelName(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}

