/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content.injector;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.rag.content.Content;
import java.util.List;

public interface ContentInjector {
    public ChatMessage inject(List<Content> var1, ChatMessage var2);
}

