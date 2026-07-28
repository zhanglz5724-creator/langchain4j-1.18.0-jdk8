/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.audio.Audio
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.AudioContent
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
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.mistralai.internal.mapper;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.AudioContent;
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
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.mistralai.internal.api.MistralAiAudioBase64Content;
import dev.langchain4j.model.mistralai.internal.api.MistralAiAudioUrlContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatMessage;
import dev.langchain4j.model.mistralai.internal.api.MistralAiDocumentBase64Content;
import dev.langchain4j.model.mistralai.internal.api.MistralAiDocumentUrlContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiFunction;
import dev.langchain4j.model.mistralai.internal.api.MistralAiFunctionCall;
import dev.langchain4j.model.mistralai.internal.api.MistralAiImageBase64Content;
import dev.langchain4j.model.mistralai.internal.api.MistralAiImageUrlContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiMessageContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiParameters;
import dev.langchain4j.model.mistralai.internal.api.MistralAiResponseFormat;
import dev.langchain4j.model.mistralai.internal.api.MistralAiResponseFormatType;
import dev.langchain4j.model.mistralai.internal.api.MistralAiRole;
import dev.langchain4j.model.mistralai.internal.api.MistralAiTextContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiThinkingContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiTool;
import dev.langchain4j.model.mistralai.internal.api.MistralAiToolCall;
import dev.langchain4j.model.mistralai.internal.api.MistralAiToolChoiceName;
import dev.langchain4j.model.mistralai.internal.api.MistralAiToolType;
import dev.langchain4j.model.mistralai.internal.api.MistralAiUsage;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Internal
public class MistralAiMapper {
    public static List<MistralAiChatMessage> toMistralAiMessages(List<ChatMessage> messages, boolean sendThinking) {
        return messages.stream().map(message -> MistralAiMapper.toMistralAiMessage(message, sendThinking)).collect(Collectors.toList());
    }

    private static MistralAiChatMessage toMistralAiMessage(ChatMessage message, boolean sendThinking) {
        if (message instanceof SystemMessage) {
            SystemMessage systemMessage = (SystemMessage)message;
            return MistralAiChatMessage.builder().role(MistralAiRole.SYSTEM).content(systemMessage.text()).build();
        }
        if (message instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage)message;
            ArrayList<MistralAiMessageContent> contents = new ArrayList<MistralAiMessageContent>(2);
            if (sendThinking && aiMessage.thinking() != null) {
                MistralAiTextContent thinkingText = new MistralAiTextContent(aiMessage.thinking());
                contents.add(new MistralAiThinkingContent(Collections.singletonList(thinkingText)));
            }
            if (Utils.isNotNullOrBlank((String)aiMessage.text())) {
                contents.add(new MistralAiTextContent(aiMessage.text()));
            }
            List<MistralAiToolCall> toolCalls = null;
            if (aiMessage.hasToolExecutionRequests()) {
                toolCalls = aiMessage.toolExecutionRequests().stream().map(MistralAiMapper::toMistralAiToolCall).collect(Collectors.toList());
            }
            return MistralAiChatMessage.builder().role(MistralAiRole.ASSISTANT).content(contents.isEmpty() ? null : contents).toolCalls(toolCalls).build();
        }
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)message;
            return MistralAiChatMessage.builder().role(MistralAiRole.USER).content(MistralAiMapper.toMistralAiMessageContents(userMessage)).build();
        }
        if (message instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage toolExecutionResultMessage = (ToolExecutionResultMessage)message;
            if (!toolExecutionResultMessage.hasSingleText()) {
                throw new UnsupportedFeatureException("Mistral AI does not support non-text content in tool results. Only text content is supported.");
            }
            return MistralAiChatMessage.builder().role(MistralAiRole.TOOL).toolCallId(toolExecutionResultMessage.id()).name(toolExecutionResultMessage.toolName()).content(toolExecutionResultMessage.text()).build();
        }
        throw new IllegalArgumentException("Unknown message type: " + message.type());
    }

    static MistralAiToolCall toMistralAiToolCall(ToolExecutionRequest toolExecutionRequest) {
        return MistralAiToolCall.builder().id(toolExecutionRequest.id()).function(MistralAiFunctionCall.builder().name(toolExecutionRequest.name()).arguments(toolExecutionRequest.arguments()).build()).build();
    }

    public static TokenUsage tokenUsageFrom(MistralAiUsage mistralAiUsage) {
        if (mistralAiUsage == null) {
            return null;
        }
        return new TokenUsage(mistralAiUsage.getPromptTokens(), mistralAiUsage.getCompletionTokens(), mistralAiUsage.getTotalTokens());
    }

    public static FinishReason finishReasonFrom(String mistralAiFinishReason) {
        if (mistralAiFinishReason == null) {
            return null;
        }
        switch (mistralAiFinishReason) {
            case "stop": {
                return FinishReason.STOP;
            }
            case "length": {
                return FinishReason.LENGTH;
            }
            case "tool_calls": {
                return FinishReason.TOOL_EXECUTION;
            }
            case "content_filter": {
                return FinishReason.CONTENT_FILTER;
            }
        }
        return FinishReason.OTHER;
    }

    public static AiMessage aiMessageFrom(MistralAiChatCompletionResponse response, boolean returnThinking) {
        List<MistralAiMessageContent> contents;
        MistralAiChatMessage aiMistralMessage = response.getChoices().get(0).getMessage();
        List<MistralAiToolCall> toolCalls = aiMistralMessage.getToolCalls();
        List<ToolExecutionRequest> toolExecutionRequests = null;
        if (Utils.isNotNullOrEmpty(toolCalls)) {
            toolExecutionRequests = MistralAiMapper.toToolExecutionRequests(toolCalls);
        }
        if ((contents = aiMistralMessage.getContent()) == null) {
            contents = Collections.emptyList();
        }
        String text = contents.stream().filter(content -> "text".equals(content.getType())).map(MistralAiTextContent.class::cast).map(MistralAiTextContent::getText).collect(Collectors.joining("\n"));
        String thinking = null;
        if (returnThinking) {
            List thinkingTexts = contents.stream().filter(content -> "thinking".equals(content.getType())).map(MistralAiThinkingContent.class::cast).map(MistralAiThinkingContent::getThinking).flatMap(Collection::stream).map(MistralAiTextContent::getText).collect(Collectors.toList());
            if (!thinkingTexts.isEmpty()) {
                thinking = String.join((CharSequence)"\n", thinkingTexts);
            }
        }
        return AiMessage.builder().text(text).thinking(thinking).toolExecutionRequests(toolExecutionRequests).build();
    }

    public static List<ToolExecutionRequest> toToolExecutionRequests(List<MistralAiToolCall> mistralAiToolCalls) {
        return mistralAiToolCalls.stream().filter(toolCall -> toolCall.getType() == MistralAiToolType.FUNCTION).map(MistralAiMapper::toToolExecutionRequest).collect(Collectors.toList());
    }

    public static ToolExecutionRequest toToolExecutionRequest(MistralAiToolCall mistralAiToolCall) {
        return ToolExecutionRequest.builder().id(mistralAiToolCall.getId()).name(mistralAiToolCall.getFunction().getName()).arguments(mistralAiToolCall.getFunction().getArguments()).build();
    }

    public static List<MistralAiTool> toMistralAiTools(List<ToolSpecification> toolSpecifications) {
        return toolSpecifications.stream().map(MistralAiMapper::toMistralAiTool).collect(Collectors.toList());
    }

    static MistralAiTool toMistralAiTool(ToolSpecification toolSpecification) {
        MistralAiFunction function = MistralAiFunction.builder().name(toolSpecification.name()).description(toolSpecification.description()).parameters(MistralAiMapper.toMistralAiParameters(toolSpecification)).build();
        return MistralAiTool.from(function);
    }

    public static MistralAiToolChoiceName toMistralAiToolChoiceName(ToolChoice toolChoice) {
        if (toolChoice == null) {
            return null;
        }
        switch (toolChoice) {
            case AUTO: {
                return MistralAiToolChoiceName.AUTO;
            }
            case REQUIRED: {
                return MistralAiToolChoiceName.ANY;
            }
            case NONE: {
                return MistralAiToolChoiceName.NONE;
            }
        }
        throw new IllegalStateException("Unexpected tool choice: " + toolChoice);
    }

    static MistralAiParameters toMistralAiParameters(ToolSpecification toolSpecification) {
        if (toolSpecification.parameters() != null) {
            JsonObjectSchema parameters = toolSpecification.parameters();
            return MistralAiParameters.builder().properties(JsonSchemaElementUtils.toMap((Map)parameters.properties())).required(parameters.required()).build();
        }
        return MistralAiParameters.builder().build();
    }

    public static MistralAiResponseFormat toMistralAiResponseFormat(ResponseFormat responseFormat, boolean strictJsonSchema) {
        if (responseFormat == null) {
            return null;
        }
        switch (responseFormat.type()) {
            case TEXT: {
                return MistralAiResponseFormat.fromType(MistralAiResponseFormatType.TEXT);
            }
            case JSON: {
                return responseFormat.jsonSchema() != null ? MistralAiResponseFormat.fromSchema(responseFormat.jsonSchema(), strictJsonSchema) : MistralAiResponseFormat.fromType(MistralAiResponseFormatType.JSON_OBJECT);
            }
        }
        throw new IllegalStateException("Unexpected response format type: " + responseFormat.type());
    }

    private static List<MistralAiMessageContent> toMistralAiMessageContents(UserMessage message) {
        return message.contents().stream().map(content -> {
            if (content instanceof TextContent) {
                TextContent textContent = (TextContent)content;
                return new MistralAiTextContent(textContent.text());
            }
            if (content instanceof ImageContent) {
                ImageContent imageContent = (ImageContent)content;
                Image image = imageContent.image();
                return image.url() != null ? new MistralAiImageUrlContent(image.url().toString()) : new MistralAiImageBase64Content(image.base64Data());
            }
            if (content instanceof AudioContent) {
                AudioContent audioContent = (AudioContent)content;
                Audio audio = audioContent.audio();
                return audio.url() != null ? new MistralAiAudioUrlContent(audio.url().toString()) : new MistralAiAudioBase64Content(audio.base64Data(), audio.mimeType());
            }
            if (content instanceof PdfFileContent) {
                PdfFileContent pdfFileContent = (PdfFileContent)content;
                PdfFile pdfFile = pdfFileContent.pdfFile();
                return pdfFile.url() != null ? new MistralAiDocumentUrlContent(pdfFile.url().toString()) : new MistralAiDocumentBase64Content(pdfFile.base64Data(), pdfFile.mimeType());
            }
            throw Exceptions.illegalArgument((String)("Unknown content type: " + content), (Object[])new Object[0]);
        }).collect(Collectors.toList());
    }
}

