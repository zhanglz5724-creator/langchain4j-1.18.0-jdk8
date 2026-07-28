/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.service.AiServices
 *  dev.langchain4j.service.UserMessage
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.internal.UserMessageTransformer;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import java.util.function.Function;

public class Context {
    private static ContextSummarizer SUMMARIZER_INSTANCE;

    private static ContextSummarizer initSummarizer(ChatModel chatModel) {
        if (SUMMARIZER_INSTANCE == null) {
            SUMMARIZER_INSTANCE = (ContextSummarizer)AiServices.builder(ContextSummarizer.class).chatModel(chatModel).build();
        }
        return SUMMARIZER_INSTANCE;
    }

    public static class Summarizer
    extends AgenticScopeContextGenerator {
        public Summarizer(AgenticScope agenticScope, ChatModel chatModel, String ... agentNames) {
            super(agenticScope, c -> {
                String context = c.contextAsConversation(agentNames);
                return context.trim().isEmpty() ? context : Context.initSummarizer(chatModel).summarize(context).getSummary();
            });
        }
    }

    public static class AgenticScopeContextGenerator
    implements UserMessageTransformer {
        private final AgenticScope agenticScope;
        private final Function<AgenticScope, String> contextProvider;

        public AgenticScopeContextGenerator(AgenticScope agenticScope, Function<AgenticScope, String> contextProvider) {
            this.agenticScope = agenticScope;
            this.contextProvider = contextProvider;
        }

        @Override
        public String transformUserMessage(String userMessage, Object memoryId) {
            if (this.agenticScope == null) {
                return userMessage;
            }
            String agenticScopeContext = this.contextProvider.apply(this.agenticScope);
            if (Utils.isNullOrBlank((String)agenticScopeContext)) {
                return userMessage;
            }
            return "Considering this context \"" + agenticScopeContext + "\"\n" + userMessage;
        }
    }

    public static class Summary {
        private String summary;

        public String getSummary() {
            return this.summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }

    public static interface ContextSummarizer {
        @UserMessage(value={"Create a short summary of the following conversation between one or more AI agents and a user.\nMention all the agents involved in the conversation.\nDo not provide any additional information, just the summary.\nThe user conversation is: '{{it}}'.\n"})
        public Summary summarize(String var1);
    }
}

