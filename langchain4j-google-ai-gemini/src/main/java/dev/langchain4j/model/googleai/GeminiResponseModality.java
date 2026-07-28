/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonValue
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GeminiResponseModality {
    TEXT("Text"),
    IMAGE("Image");

    private final String value;

    private GeminiResponseModality(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}

