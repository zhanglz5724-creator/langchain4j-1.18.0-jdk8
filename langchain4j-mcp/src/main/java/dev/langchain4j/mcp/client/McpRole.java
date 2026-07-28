/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum McpRole {
    ASSISTANT,
    USER;


    @JsonCreator
    public static McpRole fromString(String key) {
        for (McpRole role : McpRole.values()) {
            if (!role.name().equalsIgnoreCase(key)) continue;
            return role;
        }
        return null;
    }
}

