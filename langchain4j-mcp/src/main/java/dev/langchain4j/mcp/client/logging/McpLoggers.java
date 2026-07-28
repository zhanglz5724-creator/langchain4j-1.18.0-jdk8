/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.client.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class McpLoggers {
    public static final String DEFAULT_TRAFFIC_LOGGER_NAME = "MCP";
    private static final Logger DEFAULT_TRAFFIC_LOGGER = LoggerFactory.getLogger((String)"MCP");

    private McpLoggers() {
    }

    public static Logger traffic() {
        return DEFAULT_TRAFFIC_LOGGER;
    }
}

