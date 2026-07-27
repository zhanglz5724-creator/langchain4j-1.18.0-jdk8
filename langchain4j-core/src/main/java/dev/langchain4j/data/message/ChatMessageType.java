/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.CustomMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;

public enum ChatMessageType {
    SYSTEM(SystemMessage.class),
    USER(UserMessage.class),
    AI(AiMessage.class),
    TOOL_EXECUTION_RESULT(ToolExecutionResultMessage.class),
    CUSTOM(CustomMessage.class);

    private final Class<? extends ChatMessage> messageClass;

    private ChatMessageType(Class<? extends ChatMessage> messageClass) {
        this.messageClass = messageClass;
    }

    public Class<? extends ChatMessage> messageClass() {
        return this.messageClass;
    }
}

