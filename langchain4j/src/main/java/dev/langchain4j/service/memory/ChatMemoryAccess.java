/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.memory.ChatMemory
 */
package dev.langchain4j.service.memory;

import dev.langchain4j.memory.ChatMemory;

public interface ChatMemoryAccess {
    public ChatMemory getChatMemory(Object var1);

    public boolean evictChatMemory(Object var1);
}

