/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageSerializer;
import java.util.List;

public class ChatMessageDeserializer {
    private ChatMessageDeserializer() {
    }

    public static ChatMessage messageFromJson(String json) {
        return ChatMessageSerializer.CODEC.messageFromJson(json);
    }

    public static List<ChatMessage> messagesFromJson(String json) {
        return ChatMessageSerializer.CODEC.messagesFromJson(json);
    }
}

