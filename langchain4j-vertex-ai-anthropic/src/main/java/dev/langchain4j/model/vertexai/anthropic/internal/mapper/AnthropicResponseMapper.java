/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.vertexai.anthropic.internal.mapper;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicResponse;
import java.util.List;
import java.util.stream.Collectors;

public class AnthropicResponseMapper {
    private AnthropicResponseMapper() {
    }

    public static ChatResponse toChatResponse(AnthropicResponse anthropicResponse) {
        AiMessage aiMessage = AnthropicResponseMapper.toAiMessage(anthropicResponse);
        TokenUsage tokenUsage = AnthropicResponseMapper.toTokenUsage(anthropicResponse);
        FinishReason finishReason = AnthropicResponseMapper.toFinishReason(anthropicResponse.stopReason);
        String normalizedModelName = AnthropicResponseMapper.normalizeModelName(anthropicResponse.model);
        ChatResponseMetadata metadata = ChatResponseMetadata.builder().id(anthropicResponse.id).modelName(normalizedModelName).tokenUsage(tokenUsage).finishReason(finishReason).build();
        return ChatResponse.builder().aiMessage(aiMessage).metadata(metadata).build();
    }

    public static AiMessage toAiMessage(AnthropicResponse anthropicResponse) {
        if (anthropicResponse.content == null || anthropicResponse.content.isEmpty()) {
            return AiMessage.from((String)"");
        }
        String text = anthropicResponse.content.stream().filter(content -> content != null && "text".equals(content.type)).map(content -> content.text).filter(t -> t != null).collect(Collectors.joining("\n"));
        List toolExecutionRequests = anthropicResponse.content.stream().filter(content -> content != null && "tool_use".equals(content.type)).filter(content -> Utils.isNotNullOrBlank((String)content.name)).map(content -> ToolExecutionRequest.builder().id(content.id).name(content.name).arguments(content.input != null ? Json.toJson((Object)content.input) : "{}").build()).collect(Collectors.toList());
        if (Utils.isNotNullOrBlank((String)text) && !Utils.isNullOrEmpty(toolExecutionRequests)) {
            return AiMessage.from((String)text, toolExecutionRequests);
        }
        if (!Utils.isNullOrEmpty(toolExecutionRequests)) {
            return AiMessage.from(toolExecutionRequests);
        }
        return AiMessage.from((String)text);
    }

    private static TokenUsage toTokenUsage(AnthropicResponse anthropicResponse) {
        int totalInputTokens;
        if (anthropicResponse.usage == null) {
            return null;
        }
        int n = totalInputTokens = anthropicResponse.usage.inputTokens != null ? anthropicResponse.usage.inputTokens : 0;
        if (anthropicResponse.usage.cacheCreationInputTokens != null) {
            totalInputTokens += anthropicResponse.usage.cacheCreationInputTokens.intValue();
        }
        if (anthropicResponse.usage.cacheReadInputTokens != null) {
            totalInputTokens += anthropicResponse.usage.cacheReadInputTokens.intValue();
        }
        return new TokenUsage(Integer.valueOf(totalInputTokens), anthropicResponse.usage.outputTokens);
    }

    private static FinishReason toFinishReason(String stopReason) {
        if (stopReason == null) {
            return FinishReason.OTHER;
        }
        if ("end_turn".equals(stopReason) || "stop_sequence".equals(stopReason)) {
            return FinishReason.STOP;
        }
        if ("max_tokens".equals(stopReason)) {
            return FinishReason.LENGTH;
        }
        if ("tool_use".equals(stopReason)) {
            return FinishReason.TOOL_EXECUTION;
        }
        return FinishReason.OTHER;
    }

    private static String normalizeModelName(String vertexAiModelName) {
        int lastDashIndex;
        if (vertexAiModelName == null) {
            return null;
        }
        if (vertexAiModelName.matches(".*-\\d{8}$") && (lastDashIndex = vertexAiModelName.lastIndexOf(45)) > 0 && vertexAiModelName.substring(lastDashIndex + 1).matches("\\d{8}")) {
            return vertexAiModelName.substring(0, lastDashIndex) + "@" + vertexAiModelName.substring(lastDashIndex + 1);
        }
        return vertexAiModelName;
    }
}

