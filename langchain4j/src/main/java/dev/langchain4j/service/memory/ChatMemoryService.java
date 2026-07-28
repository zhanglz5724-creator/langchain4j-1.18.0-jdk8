/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.memory.ChatMemory
 */
package dev.langchain4j.service.memory;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Internal
public class ChatMemoryService {
    public static final String DEFAULT = "default";
    private ChatMemory defaultChatMemory;
    private Map<Object, ChatMemory> chatMemories;
    private ChatMemoryProvider chatMemoryProvider;

    public ChatMemoryService(ChatMemoryProvider chatMemoryProvider) {
        this.chatMemories = new ConcurrentHashMap<Object, ChatMemory>();
        this.chatMemoryProvider = (ChatMemoryProvider)ValidationUtils.ensureNotNull((Object)chatMemoryProvider, (String)"chatMemoryProvider");
    }

    public ChatMemoryService(ChatMemory chatMemory) {
        this.defaultChatMemory = (ChatMemory)ValidationUtils.ensureNotNull((Object)chatMemory, (String)"chatMemory");
    }

    public ChatMemory getOrCreateChatMemory(Object memoryId) {
        if (this.chatMemoryProvider != null) {
            return this.chatMemories.computeIfAbsent(memoryId, this.chatMemoryProvider::get);
        }
        return this.defaultChatMemory;
    }

    public ChatMemory getChatMemory(Object memoryId) {
        return this.chatMemoryProvider != null ? this.chatMemories.get(memoryId) : (memoryId == DEFAULT ? this.defaultChatMemory : null);
    }

    public ChatMemory evictChatMemory(Object memoryId) {
        return this.chatMemories.remove(memoryId);
    }

    public void clearAll() {
        this.chatMemories.values().forEach(ChatMemory::clear);
        this.chatMemories.clear();
    }

    public Collection<Object> getChatMemoryIDs() {
        return this.chatMemories.keySet();
    }

    public Collection<ChatMemory> getChatMemories() {
        return this.chatMemories.values();
    }
}

