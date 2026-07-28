/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 */
package dev.langchain4j.model.anthropic;

import dev.langchain4j.Internal;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.anthropic.AnthropicServerTool;
import dev.langchain4j.model.anthropic.AnthropicSkill;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCacheType;
import dev.langchain4j.model.anthropic.internal.api.AnthropicContainer;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCreateMessageRequest;
import dev.langchain4j.model.anthropic.internal.api.AnthropicDiagnosticsParameters;
import dev.langchain4j.model.anthropic.internal.api.AnthropicFormat;
import dev.langchain4j.model.anthropic.internal.api.AnthropicMetadata;
import dev.langchain4j.model.anthropic.internal.api.AnthropicOutputConfig;
import dev.langchain4j.model.anthropic.internal.api.AnthropicThinking;
import dev.langchain4j.model.anthropic.internal.api.AnthropicTool;
import dev.langchain4j.model.anthropic.internal.mapper.AnthropicMapper;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Internal
class InternalAnthropicHelper {
    private static final String ANTHROPIC_SKILL_TYPE = "anthropic";
    private static final String SKILL_VERSION_LATEST = "latest";
    private static final String CODE_EXECUTION_TOOL_TYPE = "code_execution_20250825";
    private static final String CODE_EXECUTION_TOOL_NAME = "code_execution";

    private InternalAnthropicHelper() {
    }

    private static AnthropicContainer toAnthropicContainer(List<AnthropicSkill> skills) {
        List<AnthropicContainer.AnthropicContainerSkill> containerSkills = skills.stream().filter(Objects::nonNull).distinct().map(skill -> new AnthropicContainer.AnthropicContainerSkill(ANTHROPIC_SKILL_TYPE, skill.skillId(), SKILL_VERSION_LATEST)).collect(Collectors.toList());
        return new AnthropicContainer(containerSkills);
    }

    private static AnthropicTool codeExecutionTool() {
        LinkedHashMap<String, Object> customParameters = new LinkedHashMap<String, Object>();
        customParameters.put("type", CODE_EXECUTION_TOOL_TYPE);
        return AnthropicTool.builder().name(CODE_EXECUTION_TOOL_NAME).customParameters(customParameters).build();
    }

    private static boolean hasCodeExecutionTool(List<AnthropicTool> tools) {
        return tools.stream().anyMatch(tool -> tool.customParameters() != null && tool.customParameters().get("type") instanceof String && ((String)tool.customParameters().get("type")).startsWith(CODE_EXECUTION_TOOL_NAME));
    }

    static void validate(ChatRequestParameters parameters) {
        ArrayList<String> unsupportedFeatures = new ArrayList<String>();
        if (parameters.frequencyPenalty() != null) {
            unsupportedFeatures.add("Frequency Penalty");
        }
        if (parameters.presencePenalty() != null) {
            unsupportedFeatures.add("Presence Penalty");
        }
        if (parameters.responseFormat() != null && parameters.responseFormat().type() == ResponseFormatType.JSON && parameters.responseFormat().jsonSchema() == null) {
            unsupportedFeatures.add("Schemaless JSON response format");
        }
        if (!unsupportedFeatures.isEmpty()) {
            if (unsupportedFeatures.size() == 1) {
                throw new UnsupportedFeatureException((String)unsupportedFeatures.get(0) + " is not supported by Anthropic");
            }
            throw new UnsupportedFeatureException(String.join((CharSequence)", ", unsupportedFeatures) + " are not supported by Anthropic");
        }
    }

    static AnthropicCreateMessageRequest createAnthropicRequest(ChatRequest chatRequest, AnthropicThinking thinking, boolean sendThinking, boolean midConversationSystemMessages, AnthropicCacheType cacheType, AnthropicCacheType toolsCacheType, boolean stream, String toolChoiceName, Boolean disableParallelToolUse, List<AnthropicServerTool> serverTools, Set<String> toolMetadataKeysToSend, String userId, List<AnthropicSkill> skills, Map<String, Object> customParameters, Boolean strictTools, boolean returnCacheDiagnostics, String previousMessageId) {
        AnthropicCreateMessageRequest.Builder requestBuilder = AnthropicCreateMessageRequest.builder().stream(stream).model(chatRequest.modelName()).messages(AnthropicMapper.toAnthropicMessages(chatRequest.messages(), sendThinking, midConversationSystemMessages)).system(AnthropicMapper.toAnthropicSystemPrompt(chatRequest.messages(), cacheType, midConversationSystemMessages)).maxTokens(chatRequest.maxOutputTokens()).stopSequences(chatRequest.stopSequences()).temperature(chatRequest.temperature()).topP(chatRequest.topP()).topK(chatRequest.topK()).thinking(thinking).outputConfig(InternalAnthropicHelper.toAnthropicOutputConfig(chatRequest.responseFormat())).customParameters(customParameters);
        ArrayList<AnthropicTool> tools = new ArrayList<AnthropicTool>();
        if (!Utils.isNullOrEmpty(serverTools)) {
            tools.addAll(AnthropicMapper.toAnthropicTools(serverTools));
        }
        if (!Utils.isNullOrEmpty((Collection)chatRequest.toolSpecifications())) {
            tools.addAll(AnthropicMapper.toAnthropicTools(chatRequest.toolSpecifications(), toolsCacheType, toolMetadataKeysToSend, strictTools));
        }
        if (!Utils.isNullOrEmpty(skills)) {
            AnthropicContainer container = InternalAnthropicHelper.toAnthropicContainer(skills);
            if (!Utils.isNullOrEmpty(container.skills)) {
                requestBuilder.container(container);
                if (!InternalAnthropicHelper.hasCodeExecutionTool(tools)) {
                    tools.add(InternalAnthropicHelper.codeExecutionTool());
                }
            }
        }
        if (!tools.isEmpty()) {
            requestBuilder.tools(tools);
        }
        if (chatRequest.toolChoice() != null) {
            requestBuilder.toolChoice(AnthropicMapper.toAnthropicToolChoice(chatRequest.toolChoice(), toolChoiceName, disableParallelToolUse));
        }
        if (!Utils.isNullOrEmpty((String)userId)) {
            requestBuilder.metadata(AnthropicMetadata.builder().userId(userId).build());
        }
        if (returnCacheDiagnostics) {
            requestBuilder.diagnostics(new AnthropicDiagnosticsParameters(previousMessageId));
        }
        return requestBuilder.build();
    }

    public static AnthropicOutputConfig toAnthropicOutputConfig(ResponseFormat responseFormat) {
        if (responseFormat == null || responseFormat.type() == ResponseFormatType.TEXT || responseFormat.jsonSchema() == null) {
            return null;
        }
        return AnthropicOutputConfig.builder().format(AnthropicFormat.fromJsonSchema(responseFormat.jsonSchema())).build();
    }
}

