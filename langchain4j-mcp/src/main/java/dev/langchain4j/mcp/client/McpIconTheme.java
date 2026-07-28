/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonValue
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum McpIconTheme {
    DARK("dark"),
    LIGHT("light");

    private final String value;

    private McpIconTheme(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static McpIconTheme from(String value) {
        for (McpIconTheme theme : McpIconTheme.values()) {
            if (!theme.value.equals(value)) continue;
            return theme;
        }
        return null;
    }
}

