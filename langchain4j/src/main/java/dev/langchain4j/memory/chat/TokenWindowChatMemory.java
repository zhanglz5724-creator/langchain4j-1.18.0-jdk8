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
 *  dev.langchain4j.model.TokenCountEstimator
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
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TokenWindowChatMemory
implements ChatMemory {
    private final Object id;
    private final Function<Object, Integer> maxTokensProvider;
    private final TokenCountEstimator tokenCountEstimator;
    private final ChatMemoryStore store;
    private final boolean alwaysKeepSystemMessageFirst;

    private TokenWindowChatMemory(Builder builder) {
        this.id = ValidationUtils.ensureNotNull((Object)builder.id, (String)"id");
        this.maxTokensProvider = (Function)ValidationUtils.ensureNotNull((Object)builder.maxTokensProvider, (String)"maxTokensProvider");
        ValidationUtils.ensureGreaterThanZero((Integer)this.maxTokensProvider.apply(this.id), (String)"maxTokens");
        this.tokenCountEstimator = (TokenCountEstimator)ValidationUtils.ensureNotNull((Object)builder.tokenCountEstimator, (String)"tokenCountEstimator");
        this.store = (ChatMemoryStore)ValidationUtils.ensureNotNull((Object)builder.store(), (String)"store");
        this.alwaysKeepSystemMessageFirst = (Boolean)Utils.getOrDefault((Object)builder.alwaysKeepSystemMessageFirst, (Object)false);
    }

    public Object id() {
        return this.id;
    }

    public void add(ChatMessage message) {
        Optional maybeSystemMessage;
        List<ChatMessage> messages = this.messages();
        if (message instanceof SystemMessage && (maybeSystemMessage = SystemMessage.findFirst(messages)).isPresent()) {
            if (((SystemMessage)maybeSystemMessage.get()).equals((Object)message)) {
                return;
            }
            messages.remove(maybeSystemMessage.get());
        }
        if (message instanceof SystemMessage && this.alwaysKeepSystemMessageFirst) {
            messages.add(0, message);
        } else {
            messages.add(message);
        }
        Integer maxTokens = this.maxTokensProvider.apply(this.id);
        ValidationUtils.ensureGreaterThanZero((Integer)maxTokens, (String)"maxTokens");
        TokenWindowChatMemory.ensureCapacity(messages, maxTokens, this.tokenCountEstimator);
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
        Integer maxTokens = this.maxTokensProvider.apply(this.id);
        ValidationUtils.ensureGreaterThanZero((Integer)maxTokens, (String)"maxTokens");
        messages = new ArrayList<ChatMessage>(messages);
        TokenWindowChatMemory.ensureCapacity(messages, maxTokens, this.tokenCountEstimator);
        this.store.updateMessages(this.id, messages);
    }

    public List<ChatMessage> messages() {
        Integer maxTokens = this.maxTokensProvider.apply(this.id);
        ValidationUtils.ensureGreaterThanZero((Integer)maxTokens, (String)"maxTokens");
        LinkedList<ChatMessage> messages = new LinkedList<ChatMessage>(this.store.getMessages(this.id));
        TokenWindowChatMemory.ensureCapacity(messages, maxTokens, this.tokenCountEstimator);
        return messages;
    }

    private static void ensureCapacity(List<ChatMessage> messages, int maxTokens, TokenCountEstimator estimator) {
        if (messages.isEmpty()) {
            return;
        }
        int currentTokenCount = estimator.estimateTokenCountInMessages(messages);
        while (currentTokenCount > maxTokens && !messages.isEmpty()) {
            AiMessage aiMessage;
            int messageToEvictIndex = 0;
            if (messages.get(0) instanceof SystemMessage) {
                if (messages.size() == 1) {
                    return;
                }
                messageToEvictIndex = 1;
            }
            ChatMessage evictedMessage = messages.remove(messageToEvictIndex);
            int tokenCountOfEvictedMessage = estimator.estimateTokenCountInMessage(evictedMessage);
            currentTokenCount -= tokenCountOfEvictedMessage;
            if (!(evictedMessage instanceof AiMessage) || !(aiMessage = (AiMessage)evictedMessage).hasToolExecutionRequests()) continue;
            while (messages.size() > messageToEvictIndex && messages.get(messageToEvictIndex) instanceof ToolExecutionResultMessage) {
                ChatMessage orphanToolExecutionResultMessage = messages.remove(messageToEvictIndex);
                currentTokenCount -= estimator.estimateTokenCountInMessage(orphanToolExecutionResultMessage);
            }
        }
    }

    public void clear() {
        this.store.deleteMessages(this.id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static TokenWindowChatMemory withMaxTokens(int maxTokens, TokenCountEstimator tokenCountEstimator) {
        return TokenWindowChatMemory.builder().maxTokens(maxTokens, tokenCountEstimator).build();
    }

    public static class Builder {
        private Object id = "default";
        private Function<Object, Integer> maxTokensProvider;
        private TokenCountEstimator tokenCountEstimator;
        private ChatMemoryStore store;
        private Boolean alwaysKeepSystemMessageFirst;

        public Builder id(Object id) {
            this.id = id;
            return this;
        }

        public Builder maxTokens(Integer maxTokens, TokenCountEstimator tokenCountEstimator) {
            this.maxTokensProvider = id -> maxTokens;
            this.tokenCountEstimator = tokenCountEstimator;
            return this;
        }

        public Builder dynamicMaxTokens(Function<Object, Integer> maxTokensProvider, TokenCountEstimator tokenCountEstimator) {
            this.maxTokensProvider = maxTokensProvider;
            this.tokenCountEstimator = tokenCountEstimator;
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

        public TokenWindowChatMemory build() {
            return new TokenWindowChatMemory(this);
        }
    }
}

