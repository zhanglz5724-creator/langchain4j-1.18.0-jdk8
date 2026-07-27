/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class SystemMessage
implements ChatMessage {
    private final String text;

    public SystemMessage(String text) {
        this.text = ValidationUtils.ensureNotBlank(text, "text");
    }

    public String text() {
        return this.text;
    }

    @Override
    public ChatMessageType type() {
        return ChatMessageType.SYSTEM;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        SystemMessage that = (SystemMessage)o;
        return Objects.equals(this.text, that.text);
    }

    public int hashCode() {
        return Objects.hash(this.text);
    }

    public String toString() {
        return "SystemMessage { text = " + Utils.quoted(this.text) + " }";
    }

    public static SystemMessage from(String text) {
        return new SystemMessage(text);
    }

    public static SystemMessage systemMessage(String text) {
        return SystemMessage.from(text);
    }

    public static Optional<SystemMessage> findFirst(List<ChatMessage> messages) {
        return messages.stream().filter(message -> message instanceof SystemMessage).map(message -> (SystemMessage)message).findFirst();
    }

    public static Optional<SystemMessage> findLast(List<ChatMessage> messages) {
        return messages.stream().filter(message -> message instanceof SystemMessage).map(message -> (SystemMessage)message).reduce((first, second) -> second);
    }

    public static List<SystemMessage> findAll(List<ChatMessage> messages) {
        return messages.stream().filter(message -> message instanceof SystemMessage).map(message -> (SystemMessage)message).collect(Collectors.toList());
    }
}

