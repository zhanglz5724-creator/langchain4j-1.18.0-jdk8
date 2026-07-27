/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.memory.chat;

import dev.langchain4j.data.message.ChatMessage;
import java.util.List;

public interface ChatMemoryStore {
    public List<ChatMessage> getMessages(Object var1);

    public void updateMessages(Object var1, List<ChatMessage> var2);

    public void deleteMessages(Object var1);
}

