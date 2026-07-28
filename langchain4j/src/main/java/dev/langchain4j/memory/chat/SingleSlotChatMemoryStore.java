/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.store.memory.chat.ChatMemoryStore
 */
package dev.langchain4j.memory.chat;

import dev.langchain4j.Internal;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.util.ArrayList;
import java.util.List;

@Internal
class SingleSlotChatMemoryStore
implements ChatMemoryStore {
    private List<ChatMessage> messages = new ArrayList<ChatMessage>();
    private final Object memoryId;

    public SingleSlotChatMemoryStore(Object memoryId) {
        this.memoryId = memoryId;
    }

    public List<ChatMessage> getMessages(Object memoryId) {
        this.checkMemoryId(memoryId);
        return this.messages;
    }

    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        this.checkMemoryId(memoryId);
        this.messages = messages;
    }

    public void deleteMessages(Object memoryId) {
        this.checkMemoryId(memoryId);
        this.messages = new ArrayList<ChatMessage>();
    }

    private void checkMemoryId(Object memoryId) {
        if (!this.memoryId.equals(memoryId)) {
            throw new IllegalStateException("This chat memory has id: " + this.memoryId + " but an operation has been requested on a memory with id: " + memoryId);
        }
    }
}

