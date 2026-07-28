/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonValue
 */
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;

public enum AnthropicRole {
    USER,
    ASSISTANT,
    SYSTEM;


    @JsonValue
    public String serialize() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}

