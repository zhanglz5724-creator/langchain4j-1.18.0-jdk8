/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.rag.content.Content
 */
package dev.langchain4j.service;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.tool.ToolExecution;
import java.util.Collections;
import java.util.List;

public class Result<T> {
    private final T content;
    private final TokenUsage tokenUsage;
    private final List<Content> sources;
    private final FinishReason finishReason;
    private final List<ToolExecution> toolExecutions;
    private final List<ChatResponse> intermediateResponses;
    private final ChatResponse finalResponse;

    public Result(ResultBuilder<T> builder) {
        this.content = ((ResultBuilder<T>) builder).content;
        this.tokenUsage = ((ResultBuilder<T>) builder).tokenUsage;
        this.sources = Utils.copy((List)((ResultBuilder<T>) builder).sources);
        this.finishReason = ((ResultBuilder<T>) builder).finishReason;
        this.toolExecutions = Utils.copy((List)((ResultBuilder<T>) builder).toolExecutions);
        this.intermediateResponses = Utils.copy((List)((ResultBuilder<T>) builder).intermediateResponses);
        this.finalResponse = ((ResultBuilder<T>) builder).finalResponse;
    }

    public Result(T content, TokenUsage tokenUsage, List<Content> sources, FinishReason finishReason, List<ToolExecution> toolExecutions) {
        this.content = content;
        this.tokenUsage = tokenUsage;
        this.sources = Utils.copy(sources);
        this.finishReason = finishReason;
        this.toolExecutions = Utils.copy(toolExecutions);
        this.intermediateResponses = Collections.emptyList();
        this.finalResponse = null;
    }

    public static <T> ResultBuilder<T> builder() {
        return new ResultBuilder();
    }

    public T content() {
        return this.content;
    }

    public TokenUsage tokenUsage() {
        return this.tokenUsage;
    }

    public List<Content> sources() {
        return this.sources;
    }

    public FinishReason finishReason() {
        return this.finishReason;
    }

    public List<ToolExecution> toolExecutions() {
        return this.toolExecutions;
    }

    public List<ChatResponse> intermediateResponses() {
        return this.intermediateResponses;
    }

    public ChatResponse finalResponse() {
        return this.finalResponse;
    }

    public static class ResultBuilder<T> {
        private T content;
        private TokenUsage tokenUsage;
        private List<Content> sources;
        private FinishReason finishReason;
        private List<ToolExecution> toolExecutions;
        private List<ChatResponse> intermediateResponses;
        private ChatResponse finalResponse;

        ResultBuilder() {
        }

        public ResultBuilder<T> content(T content) {
            this.content = content;
            return this;
        }

        public ResultBuilder<T> tokenUsage(TokenUsage tokenUsage) {
            this.tokenUsage = tokenUsage;
            return this;
        }

        public ResultBuilder<T> sources(List<Content> sources) {
            this.sources = sources;
            return this;
        }

        public ResultBuilder<T> finishReason(FinishReason finishReason) {
            this.finishReason = finishReason;
            return this;
        }

        public ResultBuilder<T> toolExecutions(List<ToolExecution> toolExecutions) {
            this.toolExecutions = toolExecutions;
            return this;
        }

        public ResultBuilder<T> intermediateResponses(List<ChatResponse> intermediateResponses) {
            this.intermediateResponses = intermediateResponses;
            return this;
        }

        public ResultBuilder<T> finalResponse(ChatResponse finalResponse) {
            this.finalResponse = finalResponse;
            return this;
        }

        public Result<T> build() {
            return new Result(this);
        }
    }
}

