/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.exception.LangChain4jException
 */
package dev.langchain4j.agentic.planner;

import dev.langchain4j.exception.LangChain4jException;

public class AgenticSystemConfigurationException
extends LangChain4jException {
    public AgenticSystemConfigurationException(Exception cause) {
        super((Throwable)cause);
    }

    public AgenticSystemConfigurationException(String message) {
        super(message);
    }

    public AgenticSystemConfigurationException(String message, Exception cause) {
        super(message, (Throwable)cause);
    }
}

