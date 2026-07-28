/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.mcp.client.logging;

public enum McpLogLevel {
    DEBUG,
    INFO,
    NOTICE,
    WARNING,
    ERROR,
    CRITICAL,
    ALERT,
    EMERGENCY;


    public static McpLogLevel from(String val) {
        if (val == null || val.trim().isEmpty()) {
            return null;
        }
        try {
            return McpLogLevel.valueOf(val.toUpperCase());
        }
        catch (Exception e) {
            return null;
        }
    }
}

