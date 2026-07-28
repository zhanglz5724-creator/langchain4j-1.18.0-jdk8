/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.inference.models.CompletionsFinishReason
 *  com.azure.ai.inference.models.StreamingChatChoiceUpdate
 *  com.azure.ai.inference.models.StreamingChatCompletionsUpdate
 *  com.azure.ai.inference.models.StreamingChatResponseMessageUpdate
 *  com.azure.ai.inference.models.StreamingChatResponseToolCallUpdate
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.github;

import com.azure.ai.inference.models.CompletionsFinishReason;
import com.azure.ai.inference.models.StreamingChatChoiceUpdate;
import com.azure.ai.inference.models.StreamingChatCompletionsUpdate;
import com.azure.ai.inference.models.StreamingChatResponseMessageUpdate;
import com.azure.ai.inference.models.StreamingChatResponseToolCallUpdate;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.github.InternalGitHubModelHelper;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class GitHubModelsStreamingResponseBuilder {
    private final StringBuffer contentBuilder = new StringBuffer();
    private int inputTokenCount = 0;
    private int outputTokenCount = 0;
    private String toolExecutionsIndex = "call_undefined";
    private final Map<String, ToolExecutionRequestBuilder> toolExecutionRequestBuilderHashMap = new HashMap<String, ToolExecutionRequestBuilder>();
    private volatile CompletionsFinishReason azureFinishReason;

    public void append(StreamingChatCompletionsUpdate streamingChatCompletionsUpdate) {
        StreamingChatResponseMessageUpdate delta;
        List choices;
        if (streamingChatCompletionsUpdate == null) {
            return;
        }
        if (streamingChatCompletionsUpdate.getUsage() != null) {
            this.inputTokenCount = streamingChatCompletionsUpdate.getUsage().getPromptTokens();
            this.outputTokenCount = streamingChatCompletionsUpdate.getUsage().getCompletionTokens();
        }
        if (Utils.isNullOrEmpty((Collection)(choices = streamingChatCompletionsUpdate.getChoices()))) {
            return;
        }
        StreamingChatChoiceUpdate chatCompletionChoice = (StreamingChatChoiceUpdate)choices.get(0);
        if (chatCompletionChoice == null) {
            return;
        }
        CompletionsFinishReason finishReason = chatCompletionChoice.getFinishReason();
        if (finishReason != null) {
            this.azureFinishReason = finishReason;
        }
        if ((delta = chatCompletionChoice.getDelta()) == null) {
            return;
        }
        String content = delta.getContent();
        if (content != null) {
            this.contentBuilder.append(content);
            return;
        }
        if (delta.getToolCalls() != null && !delta.getToolCalls().isEmpty()) {
            for (StreamingChatResponseToolCallUpdate toolCall : delta.getToolCalls()) {
                ToolExecutionRequestBuilder toolExecutionRequestBuilder;
                if (toolCall.getId() != null) {
                    this.toolExecutionsIndex = toolCall.getId();
                    toolExecutionRequestBuilder = new ToolExecutionRequestBuilder();
                    toolExecutionRequestBuilder.idBuilder.append(this.toolExecutionsIndex);
                    this.toolExecutionRequestBuilderHashMap.put(this.toolExecutionsIndex, toolExecutionRequestBuilder);
                } else {
                    toolExecutionRequestBuilder = this.toolExecutionRequestBuilderHashMap.get(this.toolExecutionsIndex);
                    if (toolExecutionRequestBuilder == null) {
                        throw new IllegalStateException("Function without an id defined in the tool call");
                    }
                }
                if (toolCall.getFunction().getName() != null) {
                    toolExecutionRequestBuilder.nameBuilder.append(toolCall.getFunction().getName());
                }
                if (toolCall.getFunction().getArguments() == null) continue;
                toolExecutionRequestBuilder.argumentsBuilder.append(toolCall.getFunction().getArguments());
            }
        }
    }

    public Response<AiMessage> build() {
        String content = this.contentBuilder.toString();
        TokenUsage tokenUsage = new TokenUsage(Integer.valueOf(this.inputTokenCount), Integer.valueOf(this.outputTokenCount));
        FinishReason finishReason = InternalGitHubModelHelper.finishReasonFrom(this.azureFinishReason);
        if (this.toolExecutionRequestBuilderHashMap.isEmpty()) {
            if (Utils.isNullOrBlank((String)content)) {
                return null;
            }
            return Response.from((Object)AiMessage.from((String)content), (TokenUsage)tokenUsage, (FinishReason)finishReason);
        }
        List toolExecutionRequests = this.toolExecutionRequestBuilderHashMap.values().stream().map(it -> ToolExecutionRequest.builder().id(((ToolExecutionRequestBuilder)it).idBuilder.toString()).name(((ToolExecutionRequestBuilder)it).nameBuilder.toString()).arguments(((ToolExecutionRequestBuilder)it).argumentsBuilder.toString()).build()).collect(Collectors.toList());
        AiMessage aiMessage = Utils.isNullOrBlank((String)content) ? AiMessage.from(toolExecutionRequests) : AiMessage.from((String)content, toolExecutionRequests);
        return Response.from((Object)aiMessage, (TokenUsage)tokenUsage, (FinishReason)finishReason);
    }

    private static class ToolExecutionRequestBuilder {
        private final StringBuffer idBuilder = new StringBuffer();
        private final StringBuffer nameBuilder = new StringBuffer();
        private final StringBuffer argumentsBuilder = new StringBuffer();

        private ToolExecutionRequestBuilder() {
        }
    }
}

