/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.exception.LangChain4jException
 */
package dev.langchain4j.mcp.client;

import dev.langchain4j.exception.LangChain4jException;

public class McpException
extends LangChain4jException {
    private final int errorCode;
    private final String errorMessage;

    public McpException(int errorCode, String errorMessage) {
        super(String.format("Code: %d, message: %s", errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int errorCode() {
        return this.errorCode;
    }

    public String errorMessage() {
        return this.errorMessage;
    }
}

