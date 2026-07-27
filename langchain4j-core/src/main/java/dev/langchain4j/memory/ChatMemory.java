/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.memory;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Arrays;
import java.util.List;

public interface ChatMemory {
    public Object id();

    public void add(ChatMessage var1);

    default public void add(ChatMessage ... messages) {
        if (messages != null && messages.length > 0) {
            this.add(Arrays.asList(messages));
        }
    }

    default public void add(Iterable<ChatMessage> messages) {
        if (messages != null) {
            messages.forEach(this::add);
        }
    }

    default public void set(ChatMessage ... messages) {
        ValidationUtils.ensureNotEmpty(messages, "messages");
        this.set(Arrays.asList(messages));
    }

    default public void set(Iterable<ChatMessage> messages) {
        ValidationUtils.ensureNotNull(messages, "messages");
        if (!messages.iterator().hasNext()) {
            throw new IllegalArgumentException("messages must not be empty");
        }
        this.clear();
        this.add(messages);
    }

    public List<ChatMessage> messages();

    public void clear();
}

