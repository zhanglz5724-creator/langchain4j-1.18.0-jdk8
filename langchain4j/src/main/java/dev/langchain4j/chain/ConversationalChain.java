/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.memory.ChatMemory
 *  dev.langchain4j.model.chat.ChatModel
 */
package dev.langchain4j.chain;

import dev.langchain4j.chain.Chain;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;

public class ConversationalChain
implements Chain<String, String> {
    private final ChatModel chatModel;
    private final ChatMemory chatMemory;

    private ConversationalChain(ChatModel chatModel, ChatMemory chatMemory) {
        this.chatModel = (ChatModel)ValidationUtils.ensureNotNull((Object)chatModel, (String)"chatModel");
        this.chatMemory = chatMemory == null ? MessageWindowChatMemory.withMaxMessages(10) : chatMemory;
    }

    public static ConversationalChainBuilder builder() {
        return new ConversationalChainBuilder();
    }

    @Override
    public String execute(String userMessage) {
        this.chatMemory.add((ChatMessage)UserMessage.userMessage((String)ValidationUtils.ensureNotBlank((String)userMessage, (String)"userMessage")));
        AiMessage aiMessage = this.chatModel.chat(this.chatMemory.messages()).aiMessage();
        this.chatMemory.add((ChatMessage)aiMessage);
        return aiMessage.text();
    }

    public static class ConversationalChainBuilder {
        private ChatModel chatModel;
        private ChatMemory chatMemory;

        ConversationalChainBuilder() {
        }

        public ConversationalChainBuilder chatModel(ChatModel chatModel) {
            this.chatModel = chatModel;
            return this;
        }

        public ConversationalChainBuilder chatMemory(ChatMemory chatMemory) {
            this.chatMemory = chatMemory;
            return this;
        }

        public ConversationalChain build() {
            return new ConversationalChain(this.chatModel, this.chatMemory);
        }
    }
}

