/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.query.router;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.QueryRouter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LanguageModelQueryRouter
implements QueryRouter {
    public static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = PromptTemplate.from("Based on the user query, determine the most suitable data source(s) to retrieve relevant information from the following options:\n{{options}}\nIt is very important that your answer consists of either a single number or multiple numbers separated by commas and nothing else!\nUser query: {{query}}");
    protected final ChatModel chatModel;
    protected final PromptTemplate promptTemplate;
    protected final String options;
    protected final Map<Integer, ContentRetriever> idToRetriever;
    protected final FallbackStrategy fallbackStrategy;

    public LanguageModelQueryRouter(ChatModel chatModel, Map<ContentRetriever, String> retrieverToDescription) {
        this(chatModel, retrieverToDescription, DEFAULT_PROMPT_TEMPLATE, FallbackStrategy.DO_NOT_ROUTE);
    }

    public LanguageModelQueryRouter(ChatModel chatModel, Map<ContentRetriever, String> retrieverToDescription, PromptTemplate promptTemplate, FallbackStrategy fallbackStrategy) {
        this.chatModel = ValidationUtils.ensureNotNull(chatModel, "chatModel");
        ValidationUtils.ensureNotEmpty(retrieverToDescription, "retrieverToDescription");
        this.promptTemplate = Utils.getOrDefault(promptTemplate, DEFAULT_PROMPT_TEMPLATE);
        HashMap<Integer, ContentRetriever> idToRetriever = new HashMap<Integer, ContentRetriever>();
        StringBuilder optionsBuilder = new StringBuilder();
        int id = 1;
        for (Map.Entry<ContentRetriever, String> entry : retrieverToDescription.entrySet()) {
            idToRetriever.put(id, ValidationUtils.ensureNotNull(entry.getKey(), "ContentRetriever"));
            if (id > 1) {
                optionsBuilder.append("\n");
            }
            optionsBuilder.append(id);
            optionsBuilder.append(": ");
            optionsBuilder.append(ValidationUtils.ensureNotBlank(entry.getValue(), "ContentRetriever description"));
            ++id;
        }
        this.idToRetriever = idToRetriever;
        this.options = optionsBuilder.toString();
        this.fallbackStrategy = Utils.getOrDefault(fallbackStrategy, FallbackStrategy.DO_NOT_ROUTE);
    }

    public static LanguageModelQueryRouterBuilder builder() {
        return new LanguageModelQueryRouterBuilder();
    }

    @Override
    public Collection<ContentRetriever> route(Query query) {
        Prompt prompt = this.createPrompt(query);
        try {
            String response = this.chatModel.chat(prompt.text());
            return this.parse(response);
        }
        catch (Exception e) {
            return this.fallback(query, e);
        }
    }

    protected Collection<ContentRetriever> fallback(Query query, Exception e) {
        switch (this.fallbackStrategy) {
            case DO_NOT_ROUTE: {
                return Collections.emptyList();
            }
            case ROUTE_TO_ALL: {
                return new ArrayList<ContentRetriever>(this.idToRetriever.values());
            }
        }
        throw new RuntimeException(e);
    }

    protected Prompt createPrompt(Query query) {
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put("query", query.text());
        variables.put("options", this.options);
        return this.promptTemplate.apply(variables);
    }

    protected Collection<ContentRetriever> parse(String choices) {
        return Arrays.stream(choices.split(",")).map(String::trim).map(Integer::parseInt).map(this.idToRetriever::get).collect(Collectors.toList());
    }

    public static class LanguageModelQueryRouterBuilder {
        private ChatModel chatModel;
        private Map<ContentRetriever, String> retrieverToDescription;
        private PromptTemplate promptTemplate;
        private FallbackStrategy fallbackStrategy;

        LanguageModelQueryRouterBuilder() {
        }

        public LanguageModelQueryRouterBuilder chatModel(ChatModel chatModel) {
            this.chatModel = chatModel;
            return this;
        }

        public LanguageModelQueryRouterBuilder retrieverToDescription(Map<ContentRetriever, String> retrieverToDescription) {
            this.retrieverToDescription = retrieverToDescription;
            return this;
        }

        public LanguageModelQueryRouterBuilder promptTemplate(PromptTemplate promptTemplate) {
            this.promptTemplate = promptTemplate;
            return this;
        }

        public LanguageModelQueryRouterBuilder fallbackStrategy(FallbackStrategy fallbackStrategy) {
            this.fallbackStrategy = fallbackStrategy;
            return this;
        }

        public LanguageModelQueryRouter build() {
            return new LanguageModelQueryRouter(this.chatModel, this.retrieverToDescription, this.promptTemplate, this.fallbackStrategy);
        }
    }

    public static enum FallbackStrategy {
        DO_NOT_ROUTE,
        ROUTE_TO_ALL,
        FAIL;

    }
}

