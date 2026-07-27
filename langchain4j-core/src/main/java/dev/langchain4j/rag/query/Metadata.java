/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.query;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.invocation.InvocationParameters;
import java.util.List;
import java.util.Objects;

public class Metadata {
    private final ChatMessage chatMessage;
    private final SystemMessage systemMessage;
    private final List<ChatMessage> chatMemory;
    private final InvocationContext invocationContext;

    public Metadata(Builder builder) {
        this.chatMessage = ValidationUtils.ensureNotNull(builder.chatMessage, "chatMessage");
        this.systemMessage = builder.systemMessage;
        this.chatMemory = Utils.copy(builder.chatMemory);
        this.invocationContext = ValidationUtils.ensureNotNull(builder.invocationContext, "invocationContext");
    }

    public Metadata(ChatMessage chatMessage, Object chatMemoryId, List<ChatMessage> chatMemory) {
        this.chatMessage = ValidationUtils.ensureNotNull(chatMessage, "chatMessage");
        this.systemMessage = null;
        this.chatMemory = Utils.copy(chatMemory);
        this.invocationContext = InvocationContext.builder().chatMemoryId(chatMemoryId).build();
    }

    public Metadata(ChatMessage chatMessage, SystemMessage systemMessage, Object chatMemoryId, List<ChatMessage> chatMemory) {
        this.chatMessage = ValidationUtils.ensureNotNull(chatMessage, "chatMessage");
        this.systemMessage = ValidationUtils.ensureNotNull(systemMessage, "systemMessage");
        this.chatMemory = Utils.copy(chatMemory);
        this.invocationContext = InvocationContext.builder().chatMemoryId(chatMemoryId).build();
    }

    public ChatMessage chatMessage() {
        return this.chatMessage;
    }

    public SystemMessage systemMessage() {
        return this.systemMessage;
    }

    public Object chatMemoryId() {
        return this.invocationContext.chatMemoryId();
    }

    public List<ChatMessage> chatMemory() {
        return this.chatMemory;
    }

    public InvocationContext invocationContext() {
        return this.invocationContext;
    }

    public InvocationParameters invocationParameters() {
        return this.invocationContext.invocationParameters();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Metadata that = (Metadata)o;
        return Objects.equals(this.chatMessage, that.chatMessage) && Objects.equals(this.systemMessage, that.systemMessage) && Objects.equals(this.chatMemory, that.chatMemory) && Objects.equals(this.invocationContext, that.invocationContext);
    }

    public int hashCode() {
        return Objects.hash(this.chatMessage, this.systemMessage, this.chatMemory, this.invocationContext);
    }

    public String toString() {
        return "Metadata { chatMessage = " + this.chatMessage + ", systemMessage = " + this.systemMessage + ", chatMemory = " + this.chatMemory + ", invocationContext = " + this.invocationContext + " }";
    }

    public static Metadata from(ChatMessage chatMessage, Object chatMemoryId, List<ChatMessage> chatMemory) {
        return new Metadata(chatMessage, chatMemoryId, chatMemory);
    }

    public static Metadata from(ChatMessage chatMessage, SystemMessage systemMessage, Object chatMemoryId, List<ChatMessage> chatMemory) {
        return new Metadata(chatMessage, systemMessage, chatMemoryId, chatMemory);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ChatMessage chatMessage;
        private SystemMessage systemMessage;
        private List<ChatMessage> chatMemory;
        private InvocationContext invocationContext;

        public Builder chatMessage(ChatMessage chatMessage) {
            this.chatMessage = chatMessage;
            return this;
        }

        public Builder systemMessage(SystemMessage systemMessage) {
            this.systemMessage = systemMessage;
            return this;
        }

        public Builder chatMemory(List<ChatMessage> chatMemory) {
            this.chatMemory = chatMemory;
            return this;
        }

        public Builder invocationContext(InvocationContext invocationContext) {
            this.invocationContext = invocationContext;
            return this;
        }

        public Metadata build() {
            return new Metadata(this);
        }
    }
}

