/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.exception.LangChain4jException
 */
package dev.langchain4j.agentic.agent;

import dev.langchain4j.exception.LangChain4jException;

public class AgentInvocationException
extends LangChain4jException {
    public AgentInvocationException(Exception cause) {
        super((Throwable)cause);
    }

    public AgentInvocationException(String message) {
        super(message);
    }

    public AgentInvocationException(String message, Exception cause) {
        super(message, (Throwable)cause);
    }
}

