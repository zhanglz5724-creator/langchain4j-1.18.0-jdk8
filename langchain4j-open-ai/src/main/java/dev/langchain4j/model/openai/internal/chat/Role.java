/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.openai.internal.chat;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    SYSTEM,
    USER,
    ASSISTANT,
    TOOL,
    FUNCTION;

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }
}

