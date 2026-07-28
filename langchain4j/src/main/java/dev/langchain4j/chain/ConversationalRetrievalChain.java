/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.memory.ChatMemory
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.rag.AugmentationRequest
 *  dev.langchain4j.rag.AugmentationResult
 *  dev.langchain4j.rag.DefaultRetrievalAugmentor
 *  dev.langchain4j.rag.RetrievalAugmentor
 *  dev.langchain4j.rag.content.retriever.ContentRetriever
 *  dev.langchain4j.rag.query.Metadata
 */
package dev.langchain4j.chain;

import dev.langchain4j.chain.Chain;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.AugmentationRequest;
import dev.langchain4j.rag.AugmentationResult;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Metadata;
import java.util.List;

public class ConversationalRetrievalChain
implements Chain<String, String> {
    private final ChatModel chatModel;
    private final ChatMemory chatMemory;
    private final RetrievalAugmentor retrievalAugmentor;

    public ConversationalRetrievalChain(ChatModel chatModel, ChatMemory chatMemory, ContentRetriever contentRetriever) {
        this(chatModel, chatMemory, (RetrievalAugmentor)DefaultRetrievalAugmentor.builder().contentRetriever(contentRetriever).build());
    }

    public ConversationalRetrievalChain(ChatModel chatModel, ChatMemory chatMemory, RetrievalAugmentor retrievalAugmentor) {
        this.chatModel = (ChatModel)ValidationUtils.ensureNotNull((Object)chatModel, (String)"chatModel");
        this.chatMemory = (ChatMemory)Utils.getOrDefault((Object)chatMemory, () -> MessageWindowChatMemory.withMaxMessages(10));
        this.retrievalAugmentor = (RetrievalAugmentor)ValidationUtils.ensureNotNull((Object)retrievalAugmentor, (String)"retrievalAugmentor");
    }

    @Override
    public String execute(String query) {
        UserMessage userMessage = UserMessage.from((String)query);
        userMessage = this.augment(userMessage);
        this.chatMemory.add((ChatMessage)userMessage);
        AiMessage aiMessage = this.chatModel.chat(this.chatMemory.messages()).aiMessage();
        this.chatMemory.add((ChatMessage)aiMessage);
        return aiMessage.text();
    }

    private UserMessage augment(UserMessage userMessage) {
        Metadata metadata = Metadata.from((ChatMessage)userMessage, (Object)this.chatMemory.id(), (List)this.chatMemory.messages());
        AugmentationRequest augmentationRequest = new AugmentationRequest((ChatMessage)userMessage, metadata);
        AugmentationResult augmentationResult = this.retrievalAugmentor.augment(augmentationRequest);
        return (UserMessage)augmentationResult.chatMessage();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ChatModel chatModel;
        private ChatMemory chatMemory;
        private RetrievalAugmentor retrievalAugmentor;

        public Builder chatModel(ChatModel chatModel) {
            this.chatModel = chatModel;
            return this;
        }

        public Builder chatMemory(ChatMemory chatMemory) {
            this.chatMemory = chatMemory;
            return this;
        }

        public Builder contentRetriever(ContentRetriever contentRetriever) {
            if (contentRetriever != null) {
                this.retrievalAugmentor = DefaultRetrievalAugmentor.builder().contentRetriever(contentRetriever).build();
            }
            return this;
        }

        public Builder retrievalAugmentor(RetrievalAugmentor retrievalAugmentor) {
            this.retrievalAugmentor = retrievalAugmentor;
            return this;
        }

        public ConversationalRetrievalChain build() {
            return new ConversationalRetrievalChain(this.chatModel, this.chatMemory, this.retrievalAugmentor);
        }
    }
}

