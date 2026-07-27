/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.Content;
import java.util.List;

public class AugmentationResult {
    private final ChatMessage chatMessage;
    private final List<Content> contents;

    public AugmentationResult(ChatMessage chatMessage, List<Content> contents) {
        this.chatMessage = ValidationUtils.ensureNotNull(chatMessage, "chatMessage");
        this.contents = Utils.copy(contents);
    }

    public static AugmentationResultBuilder builder() {
        return new AugmentationResultBuilder();
    }

    public ChatMessage chatMessage() {
        return this.chatMessage;
    }

    public List<Content> contents() {
        return this.contents;
    }

    public static class AugmentationResultBuilder {
        private ChatMessage chatMessage;
        private List<Content> contents;

        AugmentationResultBuilder() {
        }

        public AugmentationResultBuilder chatMessage(ChatMessage chatMessage) {
            this.chatMessage = chatMessage;
            return this;
        }

        public AugmentationResultBuilder contents(List<Content> contents) {
            this.contents = contents;
            return this;
        }

        public AugmentationResult build() {
            return new AugmentationResult(this.chatMessage, this.contents);
        }
    }
}

