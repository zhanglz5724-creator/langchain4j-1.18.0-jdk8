/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model;

import dev.langchain4j.data.message.ChatMessage;

public interface TokenCountEstimator {
    public int estimateTokenCountInText(String var1);

    public int estimateTokenCountInMessage(ChatMessage var1);

    public int estimateTokenCountInMessages(Iterable<ChatMessage> var1);
}

