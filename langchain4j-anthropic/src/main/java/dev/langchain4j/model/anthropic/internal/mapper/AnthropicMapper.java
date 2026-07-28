/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.PdfFileContent
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.data.pdf.PdfFile
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.internal.ToolSpecificationUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.request.json.JsonAnyOfSchema
 *  dev.langchain4j.model.chat.request.json.JsonArraySchema
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.anthropic.internal.mapper;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.pdf.PdfFile;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.internal.ToolSpecificationUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.anthropic.AnthropicCacheDiagnostics;
import dev.langchain4j.model.anthropic.AnthropicServerTool;
import dev.langchain4j.model.anthropic.AnthropicServerToolResult;
import dev.langchain4j.model.anthropic.AnthropicTokenUsage;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCacheControl;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCacheMissReason;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCacheType;
import dev.langchain4j.model.anthropic.internal.api.AnthropicContent;
import dev.langchain4j.model.anthropic.internal.api.AnthropicDiagnostics;
import dev.langchain4j.model.anthropic.internal.api.AnthropicImageContent;
import dev.langchain4j.model.anthropic.internal.api.AnthropicMessage;
import dev.langchain4j.model.anthropic.internal.api.AnthropicMessageContent;
import dev.langchain4j.model.anthropic.internal.api.AnthropicPdfContent;
import dev.langchain4j.model.anthropic.internal.api.AnthropicRedactedThinkingContent;
import dev.langchain4j.model.anthropic.internal.api.AnthropicRole;
import dev.langchain4j.model.anthropic.internal.api.AnthropicTextContent;
import dev.langchain4j.model.anthropic.internal.api.AnthropicThinkingContent;
import dev.langchain4j.model.anthropic.internal.api.AnthropicTool;
import dev.langchain4j.model.anthropic.internal.api.AnthropicToolChoice;
import dev.langchain4j.model.anthropic.internal.api.AnthropicToolChoiceType;
import dev.langchain4j.model.anthropic.internal.api.AnthropicToolResultContent;
import dev.langchain4j.model.anthropic.internal.api.AnthropicToolSchema;
import dev.langchain4j.model.anthropic.internal.api.AnthropicToolUseContent;
import dev.langchain4j.model.anthropic.internal.api.AnthropicUsage;
import dev.langchain4j.model.anthropic.internal.client.Json;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.request.json.JsonAnyOfSchema;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Internal
public class AnthropicMapper {
    public static final String THINKING_SIGNATURE_KEY = "thinking_signature";
    public static final String REDACTED_THINKING_KEY = "redacted_thinking";
    public static final String SERVER_TOOL_RESULTS_KEY = "server_tool_results";
    public static final String CACHE_CONTROL = "cache_control";

    private static boolean isMarkedForCaching(Map<String, Object> attributes) {
        return attributes != null && "ephemeral".equals(attributes.get(CACHE_CONTROL));
    }

    public static List<AnthropicMessage> toAnthropicMessages(List<ChatMessage> messages) {
        return AnthropicMapper.toAnthropicMessages(messages, false, false);
    }

    public static List<AnthropicMessage> toAnthropicMessages(List<ChatMessage> messages, boolean sendThinking) {
        return AnthropicMapper.toAnthropicMessages(messages, sendThinking, false);
    }

    public static List<AnthropicMessage> toAnthropicMessages(List<ChatMessage> messages, boolean sendThinking, boolean midConversationSystemMessages) {
        ArrayList<AnthropicMessage> anthropicMessages = new ArrayList<AnthropicMessage>();
        ArrayList<AnthropicMessageContent> toolContents = new ArrayList<AnthropicMessageContent>();
        boolean conversationStarted = false;
        for (ChatMessage message : messages) {
            List<AnthropicMessageContent> contents;
            if (message instanceof ToolExecutionResultMessage) {
                conversationStarted = true;
                ToolExecutionResultMessage toolExecutionResultMessage = (ToolExecutionResultMessage)message;
                toolContents.add(AnthropicMapper.toAnthropicToolResultContent(toolExecutionResultMessage));
                continue;
            }
            if (message instanceof SystemMessage) {
                SystemMessage systemMessage = (SystemMessage)message;
                if (!midConversationSystemMessages || !conversationStarted) continue;
                if (!toolContents.isEmpty()) {
                    anthropicMessages.add(new AnthropicMessage(AnthropicRole.USER, toolContents));
                    toolContents = new ArrayList();
                }
                anthropicMessages.add(new AnthropicMessage(AnthropicRole.SYSTEM, Arrays.asList(new AnthropicTextContent(systemMessage.text()))));
                continue;
            }
            conversationStarted = true;
            if (!toolContents.isEmpty()) {
                anthropicMessages.add(new AnthropicMessage(AnthropicRole.USER, toolContents));
                toolContents = new ArrayList();
            }
            if (message instanceof UserMessage) {
                UserMessage userMessage = (UserMessage)message;
                contents = AnthropicMapper.toAnthropicMessageContents(userMessage);
                anthropicMessages.add(new AnthropicMessage(AnthropicRole.USER, contents));
                continue;
            }
            if (!(message instanceof AiMessage)) continue;
            AiMessage aiMessage = (AiMessage)message;
            contents = AnthropicMapper.toAnthropicMessageContents(aiMessage, sendThinking);
            anthropicMessages.add(new AnthropicMessage(AnthropicRole.ASSISTANT, contents));
        }
        if (!toolContents.isEmpty()) {
            anthropicMessages.add(new AnthropicMessage(AnthropicRole.USER, toolContents));
        }
        return anthropicMessages;
    }

    private static AnthropicToolResultContent toAnthropicToolResultContent(ToolExecutionResultMessage message) {
        Boolean isError;
        AnthropicCacheControl cacheControl = AnthropicMapper.isMarkedForCaching(message.attributes()) ? AnthropicCacheType.EPHEMERAL.cacheControl() : null;
        Boolean bl = isError = Boolean.TRUE.equals(message.isError()) ? Boolean.valueOf(true) : null;
        if (message.hasSingleText()) {
            return new AnthropicToolResultContent(message.id(), message.text(), isError, cacheControl);
        }
        ArrayList<AnthropicMessageContent> contentBlocks = new ArrayList<AnthropicMessageContent>();
        for (Content content : message.contents()) {
            if (content instanceof TextContent) {
                TextContent textContent = (TextContent)content;
                contentBlocks.add(new AnthropicTextContent(textContent.text()));
                continue;
            }
            if (content instanceof ImageContent) {
                ImageContent imageContent = (ImageContent)content;
                Image image = imageContent.image();
                if (image.url() != null) {
                    contentBlocks.add(AnthropicImageContent.fromUrl(image.url().toString()));
                    continue;
                }
                contentBlocks.add(AnthropicImageContent.fromBase64(ValidationUtils.ensureNotBlank((String)image.mimeType(), (String)"mimeType"), ValidationUtils.ensureNotBlank((String)image.base64Data(), (String)"base64Data")));
                continue;
            }
            throw Exceptions.illegalArgument((String)("Unsupported content type in tool result: " + content.type()), (Object[])new Object[0]);
        }
        return new AnthropicToolResultContent(message.id(), contentBlocks, isError, cacheControl);
    }

    private static List<AnthropicMessageContent> toAnthropicMessageContents(UserMessage message) {
        boolean shouldCache = AnthropicMapper.isMarkedForCaching(message.attributes());
        List contents = message.contents();
        ArrayList<AnthropicMessageContent> anthropicContents = new ArrayList<AnthropicMessageContent>();
        for (int i = 0; i < contents.size(); ++i) {
            boolean applyCache;
            Content content = (Content)contents.get(i);
            boolean isLastItem = i == contents.size() - 1;
            boolean bl = applyCache = shouldCache && isLastItem;
            if (content instanceof TextContent) {
                TextContent textContent = (TextContent)content;
                if (applyCache) {
                    anthropicContents.add(new AnthropicTextContent(textContent.text(), AnthropicCacheType.EPHEMERAL.cacheControl()));
                    continue;
                }
                anthropicContents.add(new AnthropicTextContent(textContent.text()));
                continue;
            }
            if (content instanceof ImageContent) {
                ImageContent imageContent = (ImageContent)content;
                Image image = imageContent.image();
                if (image.url() != null) {
                    anthropicContents.add(AnthropicImageContent.fromUrl(image.url().toString()));
                    continue;
                }
                anthropicContents.add(AnthropicImageContent.fromBase64(ValidationUtils.ensureNotBlank((String)image.mimeType(), (String)"mimeType"), ValidationUtils.ensureNotBlank((String)image.base64Data(), (String)"base64Data")));
                continue;
            }
            if (content instanceof PdfFileContent) {
                PdfFileContent pdfFileContent = (PdfFileContent)content;
                PdfFile pdfFile = pdfFileContent.pdfFile();
                if (pdfFile.url() != null) {
                    anthropicContents.add(AnthropicPdfContent.fromUrl(pdfFile.url().toString()));
                    continue;
                }
                anthropicContents.add(AnthropicPdfContent.fromBase64(pdfFile.mimeType(), ValidationUtils.ensureNotBlank((String)pdfFile.base64Data(), (String)"base64Data")));
                continue;
            }
            throw Exceptions.illegalArgument((String)("Unknown content type: " + content), (Object[])new Object[0]);
        }
        return anthropicContents;
    }

    private static List<AnthropicMessageContent> toAnthropicMessageContents(AiMessage message, boolean sendThinking) {
        ArrayList<AnthropicMessageContent> contents = new ArrayList<AnthropicMessageContent>();
        if (sendThinking && Utils.isNotNullOrBlank((String)message.thinking())) {
            String signature = (String)message.attribute(THINKING_SIGNATURE_KEY, String.class);
            contents.add(new AnthropicThinkingContent(message.thinking(), signature));
        }
        if (sendThinking && message.attributes().containsKey(REDACTED_THINKING_KEY)) {
            List redactedThinkings = (List)message.attribute(REDACTED_THINKING_KEY, List.class);
            for (String redactedThinking : redactedThinkings) {
                contents.add(new AnthropicRedactedThinkingContent(redactedThinking));
            }
        }
        boolean shouldCache = AnthropicMapper.isMarkedForCaching(message.attributes());
        boolean hasToolExecutionRequests = message.hasToolExecutionRequests();
        if (Utils.isNotNullOrBlank((String)message.text())) {
            boolean applyCache = shouldCache && !hasToolExecutionRequests;
            contents.add(applyCache ? new AnthropicTextContent(message.text(), AnthropicCacheType.EPHEMERAL.cacheControl()) : new AnthropicTextContent(message.text()));
        }
        if (hasToolExecutionRequests) {
            List toolExecutionRequests = message.toolExecutionRequests();
            for (int i = 0; i < toolExecutionRequests.size(); ++i) {
                ToolExecutionRequest toolExecutionRequest = (ToolExecutionRequest)toolExecutionRequests.get(i);
                boolean isLastItem = i == toolExecutionRequests.size() - 1;
                AnthropicToolUseContent.Builder toolUseContentBuilder = AnthropicToolUseContent.builder().id(toolExecutionRequest.id()).name(toolExecutionRequest.name()).input(AnthropicMapper.toAnthropicInput(toolExecutionRequest));
                if (shouldCache && isLastItem) {
                    toolUseContentBuilder.cacheControl(AnthropicCacheType.EPHEMERAL.cacheControl());
                }
                contents.add(toolUseContentBuilder.build());
            }
        }
        return contents;
    }

    private static String toAnthropicInput(ToolExecutionRequest toolExecutionRequest) {
        String arguments = toolExecutionRequest.arguments();
        if (Utils.isNullOrBlank((String)arguments)) {
            return "{}";
        }
        return arguments;
    }

    public static List<AnthropicTextContent> toAnthropicSystemPrompt(List<ChatMessage> messages, AnthropicCacheType cacheType) {
        return AnthropicMapper.toAnthropicSystemPrompt(messages, cacheType, false);
    }

    public static List<AnthropicTextContent> toAnthropicSystemPrompt(List<ChatMessage> messages, AnthropicCacheType cacheType, boolean midConversationSystemMessages) {
        ArrayList<SystemMessage> systemMessages = new ArrayList<SystemMessage>();
        boolean conversationStarted = false;
        for (ChatMessage message2 : messages) {
            if (message2 instanceof SystemMessage) {
                SystemMessage systemMessage = (SystemMessage)message2;
                if (midConversationSystemMessages && conversationStarted) continue;
                systemMessages.add(systemMessage);
                continue;
            }
            conversationStarted = true;
        }
        SystemMessage lastSystemMessage = systemMessages.isEmpty() ? null : (SystemMessage)systemMessages.get(systemMessages.size() - 1);
        return systemMessages.stream().map(message -> {
            boolean isLastItem = message.equals((Object)lastSystemMessage);
            if (isLastItem && cacheType != AnthropicCacheType.NO_CACHE) {
                return new AnthropicTextContent(message.text(), cacheType.cacheControl());
            }
            return new AnthropicTextContent(message.text());
        }).collect(Collectors.toList());
    }

    public static AiMessage toAiMessage(List<AnthropicContent> contents) {
        return AnthropicMapper.toAiMessage(contents, false, false);
    }

    public static AiMessage toAiMessage(List<AnthropicContent> contents, boolean returnThinking) {
        return AnthropicMapper.toAiMessage(contents, returnThinking, false);
    }

    public static AiMessage toAiMessage(List<AnthropicContent> contents, boolean returnThinking, boolean returnServerToolResults) {
        List serverToolResults;
        String text = contents.stream().filter(content -> "text".equals(content.type)).map(content -> content.text).collect(Collectors.joining("\n"));
        String thinking = null;
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        if (returnThinking) {
            List redactedThinkings;
            thinking = contents.stream().filter(content -> "thinking".equals(content.type)).map(content -> content.thinking).collect(Collectors.joining("\n"));
            String signature = contents.stream().filter(content -> "thinking".equals(content.type)).map(content -> content.signature).collect(Collectors.joining("\n"));
            if (Utils.isNotNullOrEmpty((String)signature)) {
                attributes.put(THINKING_SIGNATURE_KEY, signature);
            }
            if (!(redactedThinkings = contents.stream().filter(content -> REDACTED_THINKING_KEY.equals(content.type)).map(content -> content.data).collect(Collectors.toList())).isEmpty()) {
                attributes.put(REDACTED_THINKING_KEY, redactedThinkings);
            }
        }
        if (returnServerToolResults && !(serverToolResults = contents.stream().filter(content -> AnthropicMapper.isServerToolResultType(content.type)).map(content -> AnthropicServerToolResult.builder().type(content.type).toolUseId(content.toolUseId).content(content.content).build()).collect(Collectors.toList())).isEmpty()) {
            attributes.put(SERVER_TOOL_RESULTS_KEY, serverToolResults);
        }
        List toolExecutionRequests = contents.stream().filter(content -> "tool_use".equals(content.type)).map(content -> ToolExecutionRequest.builder().id(content.id).name(content.name).arguments(Json.toJson(content.input)).build()).collect(Collectors.toList());
        return AiMessage.builder().text(Utils.isNullOrEmpty((String)text) ? null : text).thinking(Utils.isNullOrEmpty((String)thinking) ? null : thinking).toolExecutionRequests(toolExecutionRequests).attributes(attributes).build();
    }

    private static boolean isServerToolResultType(String type) {
        return type != null && type.endsWith("_tool_result");
    }

    public static TokenUsage toTokenUsage(AnthropicUsage anthropicUsage) {
        if (anthropicUsage == null) {
            return null;
        }
        return AnthropicTokenUsage.builder().inputTokenCount(anthropicUsage.inputTokens).outputTokenCount(anthropicUsage.outputTokens).cacheCreationInputTokens(anthropicUsage.cacheCreationInputTokens).cacheReadInputTokens(anthropicUsage.cacheReadInputTokens).build();
    }

    public static AnthropicCacheDiagnostics toCacheDiagnostics(AnthropicDiagnostics anthropicDiagnostics) {
        if (anthropicDiagnostics == null) {
            return null;
        }
        AnthropicCacheMissReason cacheMissReason = anthropicDiagnostics.cacheMissReason;
        return AnthropicCacheDiagnostics.builder().cacheMissReasonType(cacheMissReason == null ? null : cacheMissReason.type).cacheMissedInputTokens(cacheMissReason == null ? null : cacheMissReason.cacheMissedInputTokens).build();
    }

    public static FinishReason toFinishReason(String anthropicStopReason) {
        if (anthropicStopReason == null) {
            return null;
        }
        switch (anthropicStopReason) {
            case "end_turn": 
            case "stop_sequence": {
                return FinishReason.STOP;
            }
            case "max_tokens": {
                return FinishReason.LENGTH;
            }
            case "tool_use": {
                return FinishReason.TOOL_EXECUTION;
            }
        }
        return FinishReason.OTHER;
    }

    public static AnthropicToolChoice toAnthropicToolChoice(ToolChoice toolChoice, String toolChoiceName, Boolean disableParallelToolUse) {
        AnthropicToolChoiceType toolChoiceType;
        if (toolChoice == null) {
            return null;
        }
        switch (toolChoice) {
            case AUTO: {
                toolChoiceType = AnthropicToolChoiceType.AUTO;
                break;
            }
            case REQUIRED: {
                toolChoiceType = AnthropicToolChoiceType.ANY;
                break;
            }
            case NONE: {
                toolChoiceType = AnthropicToolChoiceType.NONE;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unexpected tool choice: " + toolChoice);
            }
        }
        if (toolChoiceName != null) {
            return AnthropicToolChoice.from(toolChoiceName, disableParallelToolUse);
        }
        return AnthropicToolChoice.from(toolChoiceType, disableParallelToolUse);
    }

    public static List<AnthropicTool> toAnthropicTools(List<ToolSpecification> toolSpecifications, AnthropicCacheType cacheToolsPrompt, Boolean strictTools) {
        return AnthropicMapper.toAnthropicTools(toolSpecifications, cacheToolsPrompt, new HashSet<String>(), strictTools);
    }

    public static List<AnthropicTool> toAnthropicTools(List<ToolSpecification> toolSpecifications, AnthropicCacheType cacheToolsPrompt, Set<String> toolMetadataKeysToSend, Boolean strictTools) {
        ToolSpecification lastToolSpecification = toolSpecifications.isEmpty() ? null : toolSpecifications.get(toolSpecifications.size() - 1);
        return toolSpecifications.stream().map(toolSpecification -> {
            boolean isLastItem = toolSpecification.equals((Object)lastToolSpecification);
            if (isLastItem && cacheToolsPrompt != AnthropicCacheType.NO_CACHE) {
                return AnthropicMapper.toAnthropicTool(toolSpecification, cacheToolsPrompt, toolMetadataKeysToSend, strictTools);
            }
            return AnthropicMapper.toAnthropicTool(toolSpecification, AnthropicCacheType.NO_CACHE, toolMetadataKeysToSend, strictTools);
        }).collect(Collectors.toList());
    }

    public static AnthropicTool toAnthropicTool(ToolSpecification toolSpecification, AnthropicCacheType cacheToolsPrompt) {
        return AnthropicMapper.toAnthropicTool(toolSpecification, cacheToolsPrompt, new HashSet<String>(), null);
    }

    public static AnthropicTool toAnthropicTool(ToolSpecification toolSpecification, AnthropicCacheType cacheToolsPrompt, Set<String> toolMetadataKeysToSend, Boolean strictTools) {
        JsonObjectSchema parameters = toolSpecification.parameters();
        boolean strict = ToolSpecificationUtils.isEffectivelyStrict((ToolSpecification)toolSpecification, (boolean)Boolean.TRUE.equals(strictTools));
        AnthropicToolSchema.Builder inputSchemaBuilder = AnthropicToolSchema.builder().properties(parameters != null ? JsonSchemaElementUtils.toMap((Map)parameters.properties(), (boolean)strict) : Collections.emptyMap()).required(parameters != null ? parameters.required() : Collections.emptyList()).additionalProperties(strict ? Boolean.FALSE : null);
        if (parameters != null && !parameters.definitions().isEmpty()) {
            inputSchemaBuilder.defs(AnthropicMapper.mapDefs(parameters.definitions()));
        }
        AnthropicTool.Builder toolBuilder = AnthropicTool.builder().name(toolSpecification.name()).description(toolSpecification.description()).strict(strict ? Boolean.TRUE : null).inputSchema(inputSchemaBuilder.build());
        if (cacheToolsPrompt != AnthropicCacheType.NO_CACHE) {
            toolBuilder.cacheControl(cacheToolsPrompt.cacheControl());
        }
        if (!toolMetadataKeysToSend.isEmpty()) {
            toolBuilder.customParameters(AnthropicMapper.retainKeys(toolSpecification.metadata(), toolMetadataKeysToSend));
        }
        return toolBuilder.build();
    }

    public static Map<String, Object> retainKeys(Map<String, Object> map, Set<String> keys) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        for (String key : keys) {
            if (!map.containsKey(key)) continue;
            result.put(key, map.get(key));
        }
        return result;
    }

    public static List<AnthropicTool> toAnthropicTools(List<AnthropicServerTool> serverTools) {
        return serverTools.stream().map(AnthropicMapper::toAnthropicTool).collect(Collectors.toList());
    }

    public static AnthropicTool toAnthropicTool(AnthropicServerTool serverTool) {
        LinkedHashMap<String, Object> customParameters = new LinkedHashMap<String, Object>();
        customParameters.put("type", serverTool.type());
        customParameters.putAll(serverTool.attributes());
        return AnthropicTool.builder().name(serverTool.name()).customParameters(customParameters).build();
    }

    public static Map<String, Object> toAnthropicSchema(JsonSchemaElement schemaElement) {
        if (schemaElement instanceof JsonObjectSchema) {
            JsonObjectSchema objectSchema = (JsonObjectSchema)schemaElement;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("type", "object");
            if (objectSchema.description() != null) {
                map.put("description", objectSchema.description());
            }
            LinkedHashMap properties = new LinkedHashMap();
            objectSchema.properties().forEach((property, value) -> properties.put(property, AnthropicMapper.toAnthropicSchema(value)));
            map.put("properties", properties);
            if (objectSchema.required() != null) {
                map.put("required", objectSchema.required());
            }
            map.put("additionalProperties", false);
            if (!objectSchema.definitions().isEmpty()) {
                map.put("$defs", AnthropicMapper.mapDefs(objectSchema.definitions()));
            }
            return map;
        }
        if (schemaElement instanceof JsonArraySchema) {
            JsonArraySchema arraySchema = (JsonArraySchema)schemaElement;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("type", "array");
            if (arraySchema.description() != null) {
                map.put("description", arraySchema.description());
            }
            if (arraySchema.items() != null) {
                map.put("items", AnthropicMapper.toAnthropicSchema(arraySchema.items()));
            } else {
                map.put("items", Collections.emptyMap());
            }
            return map;
        }
        if (schemaElement instanceof JsonAnyOfSchema) {
            JsonAnyOfSchema anyOfSchema = (JsonAnyOfSchema)schemaElement;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            if (anyOfSchema.description() != null) {
                map.put("description", anyOfSchema.description());
            }
            List anyOf = anyOfSchema.anyOf().stream().map(AnthropicMapper::toAnthropicSchema).collect(Collectors.toList());
            map.put("anyOf", anyOf);
            return map;
        }
        return JsonSchemaElementUtils.toMap((JsonSchemaElement)schemaElement, (boolean)false);
    }

    private static Map<String, Map<String, Object>> mapDefs(Map<String, JsonSchemaElement> defs) {
        LinkedHashMap<String, Map<String, Object>> map = new LinkedHashMap<String, Map<String, Object>>();
        defs.forEach((property, schema) -> map.put((String)property, AnthropicMapper.toAnthropicSchema(schema)));
        return map;
    }
}

