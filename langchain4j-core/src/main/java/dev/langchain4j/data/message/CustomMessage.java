/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.internal.Utils;
import java.util.Map;
import java.util.Objects;

public class CustomMessage
implements ChatMessage {
    private final Map<String, Object> attributes;

    public CustomMessage(Map<String, Object> attributes) {
        this.attributes = Utils.copy(attributes);
    }

    public Map<String, Object> attributes() {
        return this.attributes;
    }

    @Override
    public ChatMessageType type() {
        return ChatMessageType.CUSTOM;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        CustomMessage that = (CustomMessage)o;
        return Objects.equals(this.attributes, that.attributes);
    }

    public int hashCode() {
        return Objects.hash(this.attributes);
    }

    public String toString() {
        return "CustomMessage { attributes = " + this.attributes + " }";
    }

    public static CustomMessage from(Map<String, Object> attributes) {
        return new CustomMessage(attributes);
    }

    public static CustomMessage customMessage(Map<String, Object> attributes) {
        return CustomMessage.from(attributes);
    }
}

