/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.PdfFileContent
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.data.pdf.PdfFile
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 */
package dev.langchain4j.model.vertexai.anthropic.internal.mapper;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.pdf.PdfFile;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicCacheControl;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicContent;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicMessage;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicRequest;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicSource;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicSystemMessage;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicTool;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicToolChoice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnthropicRequestMapper {
    private AnthropicRequestMapper() {
    }

    public static AnthropicRequest toRequest(String model, List<ChatMessage> messages, List<ToolSpecification> toolSpecs, ToolChoice toolChoice, Integer maxTokens, Double temperature, Double topP, Integer topK, List<String> stopSequences, Boolean enablePromptCaching) {
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("model cannot be null or empty");
        }
        if (messages == null || messages.isEmpty()) {
            throw new IllegalArgumentException("messages cannot be null or empty");
        }
        AnthropicRequest request = new AnthropicRequest();
        request.maxTokens = maxTokens != null ? maxTokens : 4096;
        request.temperature = temperature;
        request.topP = topP;
        request.topK = topK;
        request.stopSequences = stopSequences;
        request.stream = false;
        List<AnthropicMessage> anthropicMessages = AnthropicRequestMapper.toAnthropicMessages(messages);
        List<AnthropicSystemMessage> systemMessages = AnthropicRequestMapper.toAnthropicSystemMessages(messages);
        if (enablePromptCaching != null && enablePromptCaching.booleanValue()) {
            AnthropicRequestMapper.applyCacheControl(anthropicMessages, systemMessages);
        }
        request.messages = anthropicMessages;
        List<AnthropicSystemMessage> list = request.system = systemMessages.isEmpty() ? null : systemMessages;
        if (toolSpecs != null && !toolSpecs.isEmpty()) {
            request.tools = toolSpecs.stream().map(AnthropicRequestMapper::toTool).collect(Collectors.toList());
            request.toolChoice = AnthropicRequestMapper.toAnthropicToolChoice(toolChoice);
        }
        request.anthropicVersion = "vertex-2023-10-16";
        return request;
    }

    public static List<AnthropicMessage> toAnthropicMessages(List<ChatMessage> messages) {
        ArrayList<AnthropicMessage> anthropicMessages = new ArrayList<AnthropicMessage>();
        ArrayList<AnthropicContent> toolContents = new ArrayList<AnthropicContent>();
        for (ChatMessage message : messages) {
            List<AnthropicContent> contents;
            if (message instanceof ToolExecutionResultMessage) {
                toolContents.add(AnthropicRequestMapper.toAnthropicToolResultContent((ToolExecutionResultMessage)message));
                continue;
            }
            if (message instanceof SystemMessage) continue;
            if (!toolContents.isEmpty()) {
                anthropicMessages.add(new AnthropicMessage("user", toolContents));
                toolContents = new ArrayList();
            }
            if (message instanceof UserMessage) {
                contents = AnthropicRequestMapper.toAnthropicMessageContents((UserMessage)message);
                anthropicMessages.add(new AnthropicMessage("user", contents));
                continue;
            }
            if (!(message instanceof AiMessage)) continue;
            contents = AnthropicRequestMapper.toAnthropicMessageContents((AiMessage)message);
            anthropicMessages.add(new AnthropicMessage("assistant", contents));
        }
        if (!toolContents.isEmpty()) {
            anthropicMessages.add(new AnthropicMessage("user", toolContents));
        }
        return anthropicMessages;
    }

    private static AnthropicContent toAnthropicToolResultContent(ToolExecutionResultMessage message) {
        if (!message.hasSingleText()) {
            throw new UnsupportedFeatureException("Vertex AI Anthropic does not support non-text content in tool results. Only text content is supported.");
        }
        return AnthropicContent.toolResult(message.id(), message.text());
    }

    private static List<AnthropicContent> toAnthropicMessageContents(UserMessage message) {
        return message.contents().stream().map(content -> {
            if (content instanceof TextContent) {
                TextContent textContent = (TextContent)content;
                return AnthropicContent.textContent(textContent.text());
            }
            if (content instanceof ImageContent) {
                ImageContent imageContent = (ImageContent)content;
                Image image = imageContent.image();
                if (image.url() != null) {
                    throw new UnsupportedFeatureException("Anthropic does not support images as URLs, only as Base64-encoded strings");
                }
                String base64Data = ValidationUtils.ensureNotBlank((String)image.base64Data(), (String)"base64Data");
                String mimeType = ValidationUtils.ensureNotBlank((String)image.mimeType(), (String)"mimeType");
                AnthropicSource source = AnthropicSource.base64(mimeType, base64Data);
                return AnthropicContent.imageContent(source);
            }
            if (content instanceof PdfFileContent) {
                PdfFileContent pdfFileContent = (PdfFileContent)content;
                PdfFile pdfFile = pdfFileContent.pdfFile();
                String base64Data = ValidationUtils.ensureNotBlank((String)pdfFile.base64Data(), (String)"base64Data");
                throw new UnsupportedFeatureException("PDF files are not yet supported in Vertex AI Anthropic");
            }
            throw Exceptions.illegalArgument((String)("Unknown content type: " + content), (Object[])new Object[0]);
        }).collect(Collectors.toList());
    }

    private static List<AnthropicContent> toAnthropicMessageContents(AiMessage message) {
        ArrayList<AnthropicContent> contents = new ArrayList<AnthropicContent>();
        if (Utils.isNotNullOrBlank((String)message.text())) {
            contents.add(AnthropicContent.textContent(message.text()));
        }
        if (message.hasToolExecutionRequests()) {
            List toolUseContents = message.toolExecutionRequests().stream().map(toolExecutionRequest -> AnthropicContent.toolUse(toolExecutionRequest.id(), toolExecutionRequest.name(), AnthropicRequestMapper.toAnthropicInput(toolExecutionRequest))).collect(Collectors.toList());
            contents.addAll(toolUseContents);
        }
        return contents;
    }

    private static Object toAnthropicInput(ToolExecutionRequest toolExecutionRequest) {
        String arguments = toolExecutionRequest.arguments();
        if (Utils.isNullOrBlank((String)arguments)) {
            return Collections.emptyMap();
        }
        try {
            return Json.fromJson((String)arguments, Map.class);
        }
        catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public static List<AnthropicSystemMessage> toAnthropicSystemMessages(List<ChatMessage> messages) {
        return messages.stream().filter(message -> message instanceof SystemMessage).map(message -> AnthropicSystemMessage.textSystemMessage(((SystemMessage)message).text())).collect(Collectors.toList());
    }

    public static AnthropicTool toAnthropicTool(ToolSpecification toolSpecification) {
        String description;
        JsonObjectSchema parameters = toolSpecification.parameters();
        Map properties = parameters != null && parameters.properties() != null ? JsonSchemaElementUtils.toMap((Map)parameters.properties()) : Collections.emptyMap();
        List required = parameters != null ? parameters.required() : Collections.emptyList();
        LinkedHashMap<String, Object> inputSchema = new LinkedHashMap<String, Object>();
        inputSchema.put("type", "object");
        inputSchema.put("properties", properties);
        inputSchema.put("required", required);
        if (parameters != null && parameters.definitions() != null && !parameters.definitions().isEmpty()) {
            inputSchema.put("$defs", JsonSchemaElementUtils.toMap((Map)parameters.definitions()));
        }
        if (Utils.isNotNullOrBlank((String)toolSpecification.description())) {
            description = toolSpecification.description();
        } else {
            String lowerName;
            switch (lowerName = toolSpecification.name().toLowerCase()) {
                case "get_current_time": 
                case "current_time": 
                case "time": {
                    description = "Gets the current time";
                    break;
                }
                case "get_weather": 
                case "weather": {
                    description = "Gets weather information";
                    break;
                }
                case "calculator": 
                case "calculate": {
                    description = "Performs mathematical calculations";
                    break;
                }
                default: {
                    description = "Tool: " + toolSpecification.name();
                }
            }
        }
        return new AnthropicTool(toolSpecification.name(), description, inputSchema);
    }

    private static AnthropicTool toTool(ToolSpecification toolSpec) {
        return AnthropicRequestMapper.toAnthropicTool(toolSpec);
    }

    public static AnthropicToolChoice toAnthropicToolChoice(ToolChoice toolChoice) {
        if (toolChoice == null) {
            return null;
        }
        switch (toolChoice) {
            case AUTO: {
                return AnthropicToolChoice.auto();
            }
            case REQUIRED: {
                return AnthropicToolChoice.any();
            }
            case NONE: {
                return null;
            }
        }
        throw new IllegalArgumentException("Unknown tool choice: " + toolChoice);
    }

    private static void applyCacheControl(List<AnthropicMessage> anthropicMessages, List<AnthropicSystemMessage> systemMessages) {
        if (!systemMessages.isEmpty()) {
            AnthropicSystemMessage lastSystemMessage = systemMessages.get(systemMessages.size() - 1);
            lastSystemMessage.cacheControl = AnthropicCacheControl.ephemeral();
        }
        for (int i = anthropicMessages.size() - 1; i >= 0; --i) {
            boolean hasSubstantialContent;
            AnthropicMessage message = anthropicMessages.get(i);
            if (!"user".equals(message.role) || message.content == null || message.content.isEmpty() || !(hasSubstantialContent = message.content.stream().anyMatch(content -> "text".equals(content.type) && content.text != null && content.text.length() > 100))) continue;
            message.cacheControl = AnthropicCacheControl.ephemeral();
            break;
        }
    }
}

