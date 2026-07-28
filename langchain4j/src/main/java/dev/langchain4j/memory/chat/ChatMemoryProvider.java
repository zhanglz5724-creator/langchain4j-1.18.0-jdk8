/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.memory.ChatMemory
 */
package dev.langchain4j.memory.chat;

import dev.langchain4j.memory.ChatMemory;

@FunctionalInterface
public interface ChatMemoryProvider {
    public ChatMemory get(Object var1);
}

