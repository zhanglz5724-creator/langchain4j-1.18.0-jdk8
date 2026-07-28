/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.memory.ChatMemory
 *  dev.langchain4j.store.memory.chat.ChatMemoryStore
 */
package dev.langchain4j.memory.chat;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.SingleSlotChatMemoryStore;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MessageWindowChatMemory
implements ChatMemory {
    private final Object id;
    private final Function<Object, Integer> maxMessagesProvider;
    private final ChatMemoryStore store;
    private final boolean alwaysKeepSystemMessageFirst;

    private MessageWindowChatMemory(Builder builder) {
        this.id = ValidationUtils.ensureNotNull((Object)builder.id, (String)"id");
        this.maxMessagesProvider = (Function)ValidationUtils.ensureNotNull((Object)builder.maxMessagesProvider, (String)"maxMessagesProvider");
        ValidationUtils.ensureGreaterThanZero((Integer)this.maxMessagesProvider.apply(this.id), (String)"maxMessages");
        this.store = (ChatMemoryStore)ValidationUtils.ensureNotNull((Object)builder.store(), (String)"store");
        this.alwaysKeepSystemMessageFirst = (Boolean)Utils.getOrDefault((Object)builder.alwaysKeepSystemMessageFirst, (Object)false);
    }

    public Object id() {
        return this.id;
    }

    public void add(ChatMessage message) {
        Optional systemMessage;
        List<ChatMessage> messages = this.messages();
        if (message instanceof SystemMessage && (systemMessage = SystemMessage.findFirst(messages)).isPresent()) {
            if (((SystemMessage)systemMessage.get()).equals((Object)message)) {
                return;
            }
            messages.remove(systemMessage.get());
        }
        if (message instanceof SystemMessage && this.alwaysKeepSystemMessageFirst) {
            messages.add(0, message);
        } else {
            messages.add(message);
        }
        Integer maxMessages = this.maxMessagesProvider.apply(this.id);
        ValidationUtils.ensureGreaterThanZero((Integer)maxMessages, (String)"maxMessages");
        MessageWindowChatMemory.ensureCapacity(messages, maxMessages);
        this.store.updateMessages(this.id, messages);
    }

    public void set(Iterable<ChatMessage> iter) {
        if (iter instanceof List) {
            this.set((List)iter);
        } else {
            ArrayList<ChatMessage> list = new ArrayList<ChatMessage>();
            iter.forEach(list::add);
            this.set((List<ChatMessage>)list);
        }
    }

    private void set(List<ChatMessage> messages) {
        Integer maxMessages = this.maxMessagesProvider.apply(this.id);
        ValidationUtils.ensureGreaterThanZero((Integer)maxMessages, (String)"maxMessages");
        messages = new ArrayList<ChatMessage>(messages);
        MessageWindowChatMemory.ensureCapacity(messages, maxMessages);
        this.store.updateMessages(this.id, messages);
    }

    public List<ChatMessage> messages() {
        Integer maxMessages = this.maxMessagesProvider.apply(this.id);
        ValidationUtils.ensureGreaterThanZero((Integer)maxMessages, (String)"maxMessages");
        LinkedList<ChatMessage> messages = new LinkedList<ChatMessage>(this.store.getMessages(this.id));
        MessageWindowChatMemory.ensureCapacity(messages, maxMessages);
        return messages;
    }

    private static void ensureCapacity(List<ChatMessage> messages, int maxMessages) {
        while (messages.size() > maxMessages) {
            AiMessage aiMessage;
            ChatMessage evictedMessage;
            int messageToEvictIndex = 0;
            if (messages.get(0) instanceof SystemMessage) {
                messageToEvictIndex = 1;
            }
            if (!((evictedMessage = messages.remove(messageToEvictIndex)) instanceof AiMessage) || !(aiMessage = (AiMessage)evictedMessage).hasToolExecutionRequests()) continue;
            while (messages.size() > messageToEvictIndex && messages.get(messageToEvictIndex) instanceof ToolExecutionResultMessage) {
                messages.remove(messageToEvictIndex);
            }
        }
    }

    public void clear() {
        this.store.deleteMessages(this.id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static MessageWindowChatMemory withMaxMessages(int maxMessages) {
        return MessageWindowChatMemory.builder().maxMessages(maxMessages).build();
    }

    public static class Builder {
        private Object id = "default";
        private Function<Object, Integer> maxMessagesProvider;
        private ChatMemoryStore store;
        private Boolean alwaysKeepSystemMessageFirst;

        public Builder id(Object id) {
            this.id = id;
            return this;
        }

        public Builder maxMessages(Integer maxMessages) {
            this.maxMessagesProvider = id -> maxMessages;
            return this;
        }

        public Builder dynamicMaxMessages(Function<Object, Integer> maxMessagesProvider) {
            this.maxMessagesProvider = maxMessagesProvider;
            return this;
        }

        public Builder chatMemoryStore(ChatMemoryStore store) {
            this.store = store;
            return this;
        }

        private ChatMemoryStore store() {
            return this.store != null ? this.store : new SingleSlotChatMemoryStore(this.id);
        }

        public Builder alwaysKeepSystemMessageFirst(Boolean alwaysKeepSystemMessageFirst) {
            this.alwaysKeepSystemMessageFirst = alwaysKeepSystemMessageFirst;
            return this;
        }

        public MessageWindowChatMemory build() {
            return new MessageWindowChatMemory(this);
        }
    }
}

