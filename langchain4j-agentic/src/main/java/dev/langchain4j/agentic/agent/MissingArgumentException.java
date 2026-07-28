/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.agent;

import dev.langchain4j.agentic.agent.AgentInvocationException;

public class MissingArgumentException
extends AgentInvocationException {
    private final String argumentName;

    public MissingArgumentException(String argumentName) {
        super("Missing argument: " + argumentName);
        this.argumentName = argumentName;
    }

    public String argumentName() {
        return this.argumentName;
    }
}

