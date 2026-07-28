/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.watsonx.ai.chat.model.AssistantMessage
 *  com.ibm.watsonx.ai.chat.model.ChatMessage
 *  com.ibm.watsonx.ai.chat.model.ChatParameters
 *  com.ibm.watsonx.ai.chat.model.ChatParameters$Builder
 *  com.ibm.watsonx.ai.chat.model.ChatParameters$ToolChoiceOption
 *  com.ibm.watsonx.ai.chat.model.Image$Detail
 *  com.ibm.watsonx.ai.chat.model.ImageContent
 *  com.ibm.watsonx.ai.chat.model.PartialToolCall
 *  com.ibm.watsonx.ai.chat.model.SystemMessage
 *  com.ibm.watsonx.ai.chat.model.TextContent
 *  com.ibm.watsonx.ai.chat.model.Tool
 *  com.ibm.watsonx.ai.chat.model.ToolCall
 *  com.ibm.watsonx.ai.chat.model.ToolMessage
 *  com.ibm.watsonx.ai.chat.model.UserContent
 *  com.ibm.watsonx.ai.chat.model.UserMessage
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ChatMessageType
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ContentType
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.ImageContent$DetailLevel
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.PartialToolCall
 *  dev.langchain4j.model.output.FinishReason
 */
package dev.langchain4j.model.watsonx;

import com.ibm.watsonx.ai.chat.model.AssistantMessage;
import com.ibm.watsonx.ai.chat.model.ChatMessage;
import com.ibm.watsonx.ai.chat.model.ChatParameters;
import com.ibm.watsonx.ai.chat.model.Image;
import com.ibm.watsonx.ai.chat.model.ImageContent;
import com.ibm.watsonx.ai.chat.model.PartialToolCall;
import com.ibm.watsonx.ai.chat.model.SystemMessage;
import com.ibm.watsonx.ai.chat.model.Tool;
import com.ibm.watsonx.ai.chat.model.ToolCall;
import com.ibm.watsonx.ai.chat.model.ToolMessage;
import com.ibm.watsonx.ai.chat.model.UserContent;
import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.watsonx.WatsonxChatRequestParameters;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Internal
class Converter {
    Converter() {
    }

    public static ChatMessage toChatMessage(dev.langchain4j.data.message.ChatMessage chatMessage) {
        return switch (chatMessage.type()) {
            default -> throw new IncompatibleClassChangeError();
            case ChatMessageType.SYSTEM -> Converter.toSystemMessage((dev.langchain4j.data.message.SystemMessage)dev.langchain4j.data.message.SystemMessage.class.cast(chatMessage));
            case ChatMessageType.AI -> Converter.toAssistantMessage((AiMessage)AiMessage.class.cast(chatMessage));
            case ChatMessageType.USER -> Converter.toUserMessage((UserMessage)UserMessage.class.cast(chatMessage));
            case ChatMessageType.CUSTOM -> throw new UnsupportedOperationException("The custom message type is not supported");
            case ChatMessageType.TOOL_EXECUTION_RESULT -> Converter.toToolMessage((ToolExecutionResultMessage)ToolExecutionResultMessage.class.cast(chatMessage));
        };
    }

    public static Tool toTool(ToolSpecification toolSpecification) {
        Map parameters = Objects.nonNull(toolSpecification.parameters()) ? JsonSchemaElementUtils.toMap((JsonSchemaElement)toolSpecification.parameters()) : null;
        return Tool.of((String)toolSpecification.name(), (String)toolSpecification.description(), (Map)parameters);
    }

    public static ToolExecutionRequest toToolExecutionRequest(ToolCall toolCall) {
        return ToolExecutionRequest.builder().arguments(toolCall.function().arguments()).id(toolCall.id()).name(toolCall.function().name()).build();
    }

    public static FinishReason toFinishReason(String finishReason) {
        if (finishReason == null) {
            return FinishReason.OTHER;
        }
        return switch (finishReason) {
            case "length" -> FinishReason.LENGTH;
            case "stop" -> FinishReason.STOP;
            case "tool_calls" -> FinishReason.TOOL_EXECUTION;
            case "time_limit", "cancelled", "error" -> FinishReason.OTHER;
            default -> throw new IllegalArgumentException("%s not supported".formatted(new Object[]{finishReason}));
        };
    }

    public static CompleteToolCall toCompleteToolCall(ToolCall toolCall) {
        return new CompleteToolCall(toolCall.index().intValue(), Converter.toToolExecutionRequest(toolCall));
    }

    public static dev.langchain4j.model.chat.response.PartialToolCall toPartialToolCall(PartialToolCall partialToolCall) {
        return dev.langchain4j.model.chat.response.PartialToolCall.builder().id(partialToolCall.id()).index(partialToolCall.toolIndex()).name(partialToolCall.name()).partialArguments(partialToolCall.arguments()).build();
    }

    public static ChatParameters toChatParameters(ChatRequestParameters parameters) {
        ChatParameters.Builder builder = ((ChatParameters.Builder)ChatParameters.builder().modelId(parameters.modelName())).frequencyPenalty(parameters.frequencyPenalty()).maxCompletionTokens(parameters.maxOutputTokens()).presencePenalty(parameters.presencePenalty()).stop(parameters.stopSequences()).temperature(parameters.temperature()).topP(parameters.topP());
        ResponseFormat responseFormat = parameters.responseFormat();
        if (Objects.nonNull(responseFormat)) {
            switch (responseFormat.type()) {
                case JSON: {
                    if (Objects.nonNull(responseFormat.jsonSchema())) {
                        String name = responseFormat.jsonSchema().name();
                        Map jsonSchema = JsonSchemaElementUtils.toMap((JsonSchemaElement)responseFormat.jsonSchema().rootElement());
                        builder.responseAsJsonSchema(name, jsonSchema, true);
                        break;
                    }
                    builder.responseAsJson();
                    break;
                }
            }
        }
        if (parameters instanceof WatsonxChatRequestParameters) {
            WatsonxChatRequestParameters watsonxParameters = (WatsonxChatRequestParameters)parameters;
            builder.projectId(watsonxParameters.projectId());
            builder.spaceId(watsonxParameters.spaceId());
            builder.logitBias(watsonxParameters.logitBias());
            builder.logprobs(watsonxParameters.logprobs());
            builder.seed(watsonxParameters.seed());
            builder.timeLimit(watsonxParameters.timeout());
            builder.topLogprobs(watsonxParameters.topLogprobs());
            builder.guidedChoice(watsonxParameters.guidedChoice());
            builder.guidedGrammar(watsonxParameters.guidedGrammar());
            builder.guidedRegex(watsonxParameters.guidedRegex());
            builder.repetitionPenalty(watsonxParameters.repetitionPenalty());
            builder.lengthPenalty(watsonxParameters.lengthPenalty());
            List toolSpecifications = parameters.toolSpecifications();
            ToolChoice toolChoice = parameters.toolChoice();
            if ((Objects.isNull(toolChoice) || toolChoice.equals((Object)ToolChoice.REQUIRED)) && Objects.nonNull(watsonxParameters.toolChoiceName())) {
                if (toolSpecifications.isEmpty()) {
                    throw new IllegalArgumentException("If tool-choice-name is set, at least one tool must be specified.");
                }
                builder.toolChoiceOption(null);
                builder.toolChoice(toolSpecifications.stream().filter(toolSpecification -> toolSpecification.name().equalsIgnoreCase(watsonxParameters.toolChoiceName())).findFirst().map(ToolSpecification::name).orElseThrow(() -> new IllegalArgumentException("The tool with name '%s' is not available in the list of tools sent to the model.".formatted(new Object[]{watsonxParameters.toolChoiceName()}))));
            } else if (Objects.nonNull(toolChoice)) {
                switch (toolChoice) {
                    case AUTO: {
                        builder.toolChoiceOption(ChatParameters.ToolChoiceOption.AUTO);
                        break;
                    }
                    case REQUIRED: {
                        if (toolSpecifications.isEmpty()) {
                            throw new IllegalArgumentException("If tool-choice is 'REQUIRED', at least one tool must be specified.");
                        }
                        builder.toolChoiceOption(ChatParameters.ToolChoiceOption.REQUIRED);
                        break;
                    }
                    case NONE: {
                        builder.toolChoiceOption(ChatParameters.ToolChoiceOption.NONE);
                    }
                }
            }
        }
        return builder.build();
    }

    private static ToolCall toToolCall(ToolExecutionRequest toolExecutionRequest) {
        return ToolCall.of((String)toolExecutionRequest.id(), (String)toolExecutionRequest.name(), (String)toolExecutionRequest.arguments());
    }

    private static SystemMessage toSystemMessage(dev.langchain4j.data.message.SystemMessage systemMessage) {
        return SystemMessage.of((String)systemMessage.text());
    }

    private static AssistantMessage toAssistantMessage(AiMessage aiMessage) {
        List toolCalls = null;
        if (aiMessage.hasToolExecutionRequests()) {
            toolCalls = aiMessage.toolExecutionRequests().stream().map(Converter::toToolCall).toList();
        }
        return new AssistantMessage("assistant", aiMessage.text(), null, null, null, toolCalls);
    }

    private static com.ibm.watsonx.ai.chat.model.UserMessage toUserMessage(UserMessage userMessage) {
        return com.ibm.watsonx.ai.chat.model.UserMessage.of((String)userMessage.name(), (List)userMessage.contents().stream().map(Converter::toUserContent).toList());
    }

    private static UserContent toUserContent(Content content) {
        return switch (content.type()) {
            default -> throw new IncompatibleClassChangeError();
            case ContentType.AUDIO, ContentType.VIDEO, ContentType.PDF -> throw new RuntimeException("Not implemented");
            case ContentType.IMAGE -> {
                dev.langchain4j.data.message.ImageContent imageContent = (dev.langchain4j.data.message.ImageContent)content;
                if (Objects.nonNull(imageContent.image().url())) {
                    throw new UnsupportedFeatureException("image URL is not supported");
                }
                String mimeType = imageContent.image().mimeType();
                String base64Data = Objects.requireNonNull(imageContent.image().base64Data(), "The base64Data can not be null");
                Image.Detail v0 = switch (imageContent.detailLevel()) {
                    case ImageContent.DetailLevel.AUTO -> Image.Detail.AUTO;
                    case ImageContent.DetailLevel.HIGH -> Image.Detail.HIGH;
                    case ImageContent.DetailLevel.LOW -> Image.Detail.LOW;
                    default -> throw new UnsupportedFeatureException("Unsupported detail level: " + imageContent.detailLevel());
                };
                Image.Detail detailLevel = v0;
                yield ImageContent.of((String)mimeType, (String)base64Data, (Image.Detail)detailLevel);
            }
            case ContentType.TEXT -> {
                TextContent textContent = (TextContent)content;
                yield com.ibm.watsonx.ai.chat.model.TextContent.of((String)textContent.text());
            }
        };
    }

    private static ToolMessage toToolMessage(ToolExecutionResultMessage toolExecutionResultMessage) {
        if (!toolExecutionResultMessage.hasSingleText()) {
            throw new UnsupportedFeatureException("watsonx does not support non-text content in tool results. Only text content is supported.");
        }
        return ToolMessage.of((String)toolExecutionResultMessage.text(), (String)toolExecutionResultMessage.id());
    }
}

