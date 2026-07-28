/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.exception.LangChain4jException
 */
package dev.langchain4j.service;

import dev.langchain4j.exception.LangChain4jException;

public class IllegalConfigurationException
extends LangChain4jException {
    public IllegalConfigurationException(String message) {
        super(message);
    }

    public static IllegalConfigurationException illegalConfiguration(String message) {
        return new IllegalConfigurationException(message);
    }

    public static IllegalConfigurationException illegalConfiguration(String format, Object ... args) {
        return new IllegalConfigurationException(String.format(format, args));
    }
}

