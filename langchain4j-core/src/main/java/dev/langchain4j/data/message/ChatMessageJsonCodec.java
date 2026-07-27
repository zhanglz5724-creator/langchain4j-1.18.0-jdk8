/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.Internal;
import dev.langchain4j.data.message.ChatMessage;
import java.util.List;

@Internal
public interface ChatMessageJsonCodec {
    public ChatMessage messageFromJson(String var1);

    public List<ChatMessage> messagesFromJson(String var1);

    public String messageToJson(ChatMessage var1);

    public String messagesToJson(List<ChatMessage> var1);
}

