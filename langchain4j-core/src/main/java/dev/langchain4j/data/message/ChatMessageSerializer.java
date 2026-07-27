/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageJsonCodec;
import dev.langchain4j.data.message.JacksonChatMessageJsonCodec;
import dev.langchain4j.spi.ServiceHelper;
import dev.langchain4j.spi.data.message.ChatMessageJsonCodecFactory;
import java.util.Iterator;
import java.util.List;

public class ChatMessageSerializer {
    static final ChatMessageJsonCodec CODEC = ChatMessageSerializer.loadCodec();

    private static ChatMessageJsonCodec loadCodec() {
        Iterator<ChatMessageJsonCodecFactory> iterator = ServiceHelper.loadFactories(ChatMessageJsonCodecFactory.class).iterator();
        if (iterator.hasNext()) {
            ChatMessageJsonCodecFactory factory = iterator.next();
            return factory.create();
        }
        return new JacksonChatMessageJsonCodec();
    }

    public static String messageToJson(ChatMessage message) {
        return CODEC.messageToJson(message);
    }

    public static String messagesToJson(List<ChatMessage> messages) {
        return CODEC.messagesToJson(messages);
    }
}

