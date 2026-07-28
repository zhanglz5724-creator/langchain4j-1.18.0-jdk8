/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.internal.ToolCallBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.ollama;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.internal.ToolCallBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.InternalOllamaHelper;
import dev.langchain4j.model.ollama.Message;
import dev.langchain4j.model.ollama.OllamaChatResponse;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;

class OllamaStreamingResponseBuilder {
    private final StringBuffer contentBuilder = new StringBuffer();
    private final boolean returnThinking;
    private final StringBuffer thinkingBuilder;
    private final ToolCallBuilder toolCallBuilder;
    private volatile String modelName;
    private volatile TokenUsage tokenUsage;

    OllamaStreamingResponseBuilder(ToolCallBuilder toolCallBuilder, boolean returnThinking) {
        this.toolCallBuilder = toolCallBuilder;
        this.returnThinking = returnThinking;
        this.thinkingBuilder = returnThinking ? new StringBuffer() : null;
    }

    void append(OllamaChatResponse partialResponse) {
        Message message;
        if (partialResponse == null) {
            return;
        }
        if (this.modelName == null && partialResponse.getModel() != null) {
            this.modelName = partialResponse.getModel();
        }
        if (partialResponse.getEvalCount() != null && partialResponse.getPromptEvalCount() != null) {
            this.tokenUsage = new TokenUsage(partialResponse.getPromptEvalCount(), partialResponse.getEvalCount());
        }
        if ((message = partialResponse.getMessage()) == null) {
            return;
        }
        String content = message.getContent();
        if (content != null) {
            this.contentBuilder.append(content);
        }
        String thinking = message.getThinking();
        if (this.returnThinking && thinking != null) {
            this.thinkingBuilder.append(thinking);
        }
    }

    ChatResponse build(OllamaChatResponse ollamaChatResponse) {
        String text = this.contentBuilder.toString();
        String thinking = null;
        if (this.returnThinking) {
            thinking = this.thinkingBuilder.toString();
        }
        if (this.toolCallBuilder.hasRequests()) {
            return ChatResponse.builder().aiMessage(AiMessage.builder().text(Utils.isNullOrEmpty((String)text) ? null : text).thinking(Utils.isNullOrEmpty((String)thinking) ? null : thinking).toolExecutionRequests(this.toolCallBuilder.allRequests()).build()).metadata(InternalOllamaHelper.chatResponseMetadataFrom(this.modelName, FinishReason.TOOL_EXECUTION, this.tokenUsage)).build();
        }
        return ChatResponse.builder().aiMessage(AiMessage.builder().text(Utils.isNullOrEmpty((String)text) ? null : text).thinking(Utils.isNullOrEmpty((String)thinking) ? null : thinking).build()).metadata(InternalOllamaHelper.chatResponseMetadataFrom(this.modelName, InternalOllamaHelper.toFinishReason(ollamaChatResponse), this.tokenUsage)).build();
    }
}

