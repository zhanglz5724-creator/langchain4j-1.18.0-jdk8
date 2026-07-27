/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.query.transformer;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ExpandingQueryTransformer
implements QueryTransformer {
    public static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = PromptTemplate.from("Generate {{n}} different versions of a provided user query. Each version should be worded differently, using synonyms or alternative sentence structures, but they should all retain the original meaning. These versions will be used to retrieve relevant documents. It is very important to provide each query version on a separate line, without enumerations, hyphens, or any additional formatting!\nUser query: {{query}}");
    public static final int DEFAULT_N = 3;
    protected final ChatModel chatModel;
    protected final PromptTemplate promptTemplate;
    protected final int n;

    public ExpandingQueryTransformer(ChatModel chatModel) {
        this(chatModel, DEFAULT_PROMPT_TEMPLATE, 3);
    }

    public ExpandingQueryTransformer(ChatModel chatModel, int n) {
        this(chatModel, DEFAULT_PROMPT_TEMPLATE, n);
    }

    public ExpandingQueryTransformer(ChatModel chatModel, PromptTemplate promptTemplate) {
        this(chatModel, ValidationUtils.ensureNotNull(promptTemplate, "promptTemplate"), 3);
    }

    public ExpandingQueryTransformer(ChatModel chatModel, PromptTemplate promptTemplate, Integer n) {
        this.chatModel = ValidationUtils.ensureNotNull(chatModel, "chatModel");
        this.promptTemplate = Utils.getOrDefault(promptTemplate, DEFAULT_PROMPT_TEMPLATE);
        this.n = ValidationUtils.ensureGreaterThanZero(Utils.getOrDefault(n, 3), "n");
    }

    public static ExpandingQueryTransformerBuilder builder() {
        return new ExpandingQueryTransformerBuilder();
    }

    @Override
    public Collection<Query> transform(Query query) {
        Prompt prompt = this.createPrompt(query);
        String response = this.chatModel.chat(prompt.text());
        List<String> queries = this.parse(response);
        return queries.stream().map(queryText -> query.metadata() == null ? Query.from(queryText) : Query.from(queryText, query.metadata())).collect(Collectors.toList());
    }

    protected Prompt createPrompt(Query query) {
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put("query", query.text());
        variables.put("n", this.n);
        return this.promptTemplate.apply(variables);
    }

    protected List<String> parse(String queries) {
        return Arrays.stream(queries.split("\n")).filter(Utils::isNotNullOrBlank).collect(Collectors.toList());
    }

    public static class ExpandingQueryTransformerBuilder {
        private ChatModel chatModel;
        private PromptTemplate promptTemplate;
        private Integer n;

        ExpandingQueryTransformerBuilder() {
        }

        public ExpandingQueryTransformerBuilder chatModel(ChatModel chatModel) {
            this.chatModel = chatModel;
            return this;
        }

        public ExpandingQueryTransformerBuilder promptTemplate(PromptTemplate promptTemplate) {
            this.promptTemplate = promptTemplate;
            return this;
        }

        public ExpandingQueryTransformerBuilder n(Integer n) {
            this.n = n;
            return this;
        }

        public ExpandingQueryTransformer build() {
            return new ExpandingQueryTransformer(this.chatModel, this.promptTemplate, this.n);
        }
    }
}

