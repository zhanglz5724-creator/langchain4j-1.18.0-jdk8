/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.client.logging;

import dev.langchain4j.mcp.client.logging.McpLogMessage;
import dev.langchain4j.mcp.client.logging.McpLogMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMcpLogMessageHandler
implements McpLogMessageHandler {
    private static final Logger log = LoggerFactory.getLogger(DefaultMcpLogMessageHandler.class);

    @Override
    public void handleLogMessage(McpLogMessage message) {
        if (message.level() == null) {
            log.warn("Received MCP log message with unknown level: {}", (Object)message.data());
            return;
        }
        switch (message.level()) {
            case DEBUG: {
                log.debug("MCP logger: {}: {}", (Object)message.logger(), (Object)message.data());
                break;
            }
            case INFO: 
            case NOTICE: {
                log.info("MCP logger: {}: {}", (Object)message.logger(), (Object)message.data());
                break;
            }
            case WARNING: {
                log.warn("MCP logger: {}: {}", (Object)message.logger(), (Object)message.data());
                break;
            }
            case ERROR: 
            case CRITICAL: 
            case ALERT: 
            case EMERGENCY: {
                log.error("MCP logger: {}: {}", (Object)message.logger(), (Object)message.data());
            }
        }
    }
}

