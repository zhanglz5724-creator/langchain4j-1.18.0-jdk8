/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.query.Metadata;

public class AugmentationRequest {
    private final ChatMessage chatMessage;
    private final Metadata metadata;

    public AugmentationRequest(ChatMessage chatMessage, Metadata metadata) {
        this.chatMessage = ValidationUtils.ensureNotNull(chatMessage, "chatMessage");
        this.metadata = ValidationUtils.ensureNotNull(metadata, "metadata");
    }

    public ChatMessage chatMessage() {
        return this.chatMessage;
    }

    public Metadata metadata() {
        return this.metadata;
    }
}

