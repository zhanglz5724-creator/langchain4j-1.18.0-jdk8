/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.exception.LangChain4jException
 *  dev.langchain4j.model.moderation.Moderation
 */
package dev.langchain4j.service;

import dev.langchain4j.exception.LangChain4jException;
import dev.langchain4j.model.moderation.Moderation;

public class ModerationException
extends LangChain4jException {
    private final Moderation moderation;

    public ModerationException(String message, Moderation moderation) {
        super(message);
        this.moderation = moderation;
    }

    public Moderation moderation() {
        return this.moderation;
    }
}

