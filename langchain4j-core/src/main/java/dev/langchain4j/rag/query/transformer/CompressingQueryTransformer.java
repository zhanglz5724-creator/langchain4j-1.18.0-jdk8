/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.query.transformer;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CompressingQueryTransformer
implements QueryTransformer {
    public static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = PromptTemplate.from("Read and understand the conversation between the User and the AI. Then, analyze the new query from the User. Identify all relevant details, terms, and context from both the conversation and the new query. Reformulate this query into a clear, concise, and self-contained format suitable for information retrieval.\n\nConversation:\n{{chatMemory}}\n\nUser query: {{query}}\n\nIt is very important that you provide only reformulated query and nothing else! Do not prepend a query with anything!");
    protected final PromptTemplate promptTemplate;
    protected final ChatModel chatModel;

    public CompressingQueryTransformer(ChatModel chatModel) {
        this(chatModel, DEFAULT_PROMPT_TEMPLATE);
    }

    public CompressingQueryTransformer(ChatModel chatModel, PromptTemplate promptTemplate) {
        this.chatModel = ValidationUtils.ensureNotNull(chatModel, "chatModel");
        this.promptTemplate = Utils.getOrDefault(promptTemplate, DEFAULT_PROMPT_TEMPLATE);
    }

    public static CompressingQueryTransformerBuilder builder() {
        return new CompressingQueryTransformerBuilder();
    }

    @Override
    public Collection<Query> transform(Query query) {
        List<ChatMessage> chatMemory = query.metadata().chatMemory();
        if (chatMemory == null || chatMemory.isEmpty()) {
            return Collections.singletonList(query);
        }
        Prompt prompt = this.createPrompt(query, this.format(chatMemory));
        String compressedQueryText = this.chatModel.chat(prompt.text());
        Query compressedQuery = query.metadata() == null ? Query.from(compressedQueryText) : Query.from(compressedQueryText, query.metadata());
        return Collections.singletonList(compressedQuery);
    }

    protected String format(List<ChatMessage> chatMemory) {
        return chatMemory.stream().map(this::format).filter(Objects::nonNull).collect(Collectors.joining("\n"));
    }

    protected String format(ChatMessage message) {
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)message;
            return "User: " + userMessage.singleText();
        }
        if (message instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage)message;
            if (aiMessage.hasToolExecutionRequests()) {
                return null;
            }
            return "AI: " + aiMessage.text();
        }
        return null;
    }

    protected Prompt createPrompt(Query query, String chatMemory) {
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put("query", query.text());
        variables.put("chatMemory", chatMemory);
        return this.promptTemplate.apply(variables);
    }

    public static class CompressingQueryTransformerBuilder {
        private ChatModel chatModel;
        private PromptTemplate promptTemplate;

        CompressingQueryTransformerBuilder() {
        }

        public CompressingQueryTransformerBuilder chatModel(ChatModel chatModel) {
            this.chatModel = chatModel;
            return this;
        }

        public CompressingQueryTransformerBuilder promptTemplate(PromptTemplate promptTemplate) {
            this.promptTemplate = promptTemplate;
            return this;
        }

        public CompressingQueryTransformer build() {
            return new CompressingQueryTransformer(this.chatModel, this.promptTemplate);
        }
    }
}

