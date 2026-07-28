/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonValue
 */
package dev.langchain4j.model.ollama;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;

enum Role {
    SYSTEM,
    USER,
    ASSISTANT,
    TOOL;


    @JsonValue
    public String serialize() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}

