/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.openai.models.ChatChoice
 *  com.azure.ai.openai.models.ChatCompletions
 *  com.azure.ai.openai.models.ChatResponseMessage
 *  com.azure.ai.openai.models.Choice
 *  com.azure.ai.openai.models.Completions
 *  com.azure.ai.openai.models.CompletionsFinishReason
 *  com.azure.ai.openai.models.CompletionsUsage
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.internal.ToolCallBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.azure;

import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatResponseMessage;
import com.azure.ai.openai.models.Choice;
import com.azure.ai.openai.models.Completions;
import com.azure.ai.openai.models.CompletionsFinishReason;
import com.azure.ai.openai.models.CompletionsUsage;
import dev.langchain4j.Internal;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.internal.ToolCallBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.azure.InternalAzureOpenAiHelper;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Internal
class AzureOpenAiStreamingResponseBuilder {
    private final StringBuffer contentBuilder = new StringBuffer();
    private final ToolCallBuilder toolCallBuilder;
    private volatile TokenUsage tokenUsage;
    private volatile CompletionsFinishReason finishReason;

    AzureOpenAiStreamingResponseBuilder() {
        this(null);
    }

    AzureOpenAiStreamingResponseBuilder(ToolCallBuilder toolCallBuilder) {
        this.toolCallBuilder = toolCallBuilder;
    }

    void append(ChatCompletions completions) {
        ChatResponseMessage delta;
        List choices;
        if (completions == null) {
            return;
        }
        CompletionsUsage usage = completions.getUsage();
        if (usage != null) {
            this.tokenUsage = new TokenUsage(Integer.valueOf(usage.getPromptTokens()), Integer.valueOf(usage.getCompletionTokens()));
        }
        if (Utils.isNullOrEmpty((Collection)(choices = completions.getChoices()))) {
            return;
        }
        ChatChoice chatCompletionChoice = (ChatChoice)choices.get(0);
        if (chatCompletionChoice == null) {
            return;
        }
        CompletionsFinishReason finishReason = chatCompletionChoice.getFinishReason();
        if (finishReason != null) {
            this.finishReason = finishReason;
        }
        if ((delta = chatCompletionChoice.getDelta()) == null) {
            return;
        }
        String content = delta.getContent();
        if (content != null) {
            this.contentBuilder.append(content);
        }
    }

    void append(Completions completions) {
        String token;
        List choices;
        if (completions == null) {
            return;
        }
        CompletionsUsage usage = completions.getUsage();
        if (usage != null) {
            this.tokenUsage = new TokenUsage(Integer.valueOf(usage.getPromptTokens()), Integer.valueOf(usage.getCompletionTokens()));
        }
        if (Utils.isNullOrEmpty((Collection)(choices = completions.getChoices()))) {
            return;
        }
        Choice completionChoice = (Choice)choices.get(0);
        if (completionChoice == null) {
            return;
        }
        CompletionsFinishReason completionsFinishReason = completionChoice.getFinishReason();
        if (completionsFinishReason != null) {
            this.finishReason = completionsFinishReason;
        }
        if ((token = completionChoice.getText()) != null) {
            this.contentBuilder.append(token);
        }
    }

    Response<AiMessage> build() {
        String content = this.contentBuilder.toString();
        List toolExecutionRequests = Collections.emptyList();
        if (this.toolCallBuilder != null) {
            toolExecutionRequests = this.toolCallBuilder.allRequests();
        }
        AiMessage aiMessage = AiMessage.builder().text(content.isEmpty() ? null : content).toolExecutionRequests(toolExecutionRequests).build();
        return Response.from((Object)aiMessage, (TokenUsage)this.tokenUsage, (FinishReason)InternalAzureOpenAiHelper.finishReasonFrom(this.finishReason));
    }
}

