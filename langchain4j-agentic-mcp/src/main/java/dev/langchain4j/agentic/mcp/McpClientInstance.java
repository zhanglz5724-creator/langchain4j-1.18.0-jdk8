/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.internal.InternalAgent
 */
package dev.langchain4j.agentic.mcp;

import dev.langchain4j.agentic.internal.InternalAgent;

public interface McpClientInstance
extends InternalAgent {
    public String[] inputKeys();

    public String toolName();

    public String toolDescription();
}

