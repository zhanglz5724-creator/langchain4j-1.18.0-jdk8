/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ChatMessageType
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ContentType
 *  dev.langchain4j.data.message.CustomMessage
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.ollama;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.CustomMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.ollama.Function;
import dev.langchain4j.model.ollama.FunctionCall;
import dev.langchain4j.model.ollama.ImageUtils;
import dev.langchain4j.model.ollama.Message;
import dev.langchain4j.model.ollama.OllamaChatRequest;
import dev.langchain4j.model.ollama.OllamaChatRequestParameters;
import dev.langchain4j.model.ollama.OllamaChatResponse;
import dev.langchain4j.model.ollama.OllamaJsonUtils;
import dev.langchain4j.model.ollama.Options;
import dev.langchain4j.model.ollama.Parameters;
import dev.langchain4j.model.ollama.Role;
import dev.langchain4j.model.ollama.Tool;
import dev.langchain4j.model.ollama.ToolCall;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Internal
class InternalOllamaHelper {
    private static final Predicate<ChatMessage> isUserMessage = UserMessage.class::isInstance;
    private static final Predicate<UserMessage> hasImages = userMessage -> userMessage.contents().stream().anyMatch(content -> ContentType.IMAGE.equals((Object)content.type()));

    InternalOllamaHelper() {
    }

    static List<Message> toOllamaMessages(List<ChatMessage> messages) {
        if (!InternalOllamaHelper.messagesContainOnlyTextAndOrImageContentType(messages)) {
            throw new UnsupportedFeatureException("Ollama only supports message types Text and Image.");
        }
        return messages.stream().map(message -> isUserMessage.test((ChatMessage)message) && hasImages.test((UserMessage)message) ? InternalOllamaHelper.messagesWithImageSupport((UserMessage)message) : InternalOllamaHelper.otherMessages(message)).collect(Collectors.toList());
    }

    static List<Tool> toOllamaTools(List<ToolSpecification> toolSpecifications) {
        if (toolSpecifications == null) {
            return null;
        }
        return toolSpecifications.stream().map(toolSpecification -> Tool.builder().function(Function.builder().name(toolSpecification.name()).description(toolSpecification.description()).parameters(InternalOllamaHelper.toOllamaParameters(toolSpecification)).build()).build()).collect(Collectors.toList());
    }

    private static Parameters toOllamaParameters(ToolSpecification toolSpecification) {
        if (toolSpecification.parameters() != null) {
            JsonObjectSchema parameters = toolSpecification.parameters();
            return Parameters.builder().properties(JsonSchemaElementUtils.toMap((Map)parameters.properties())).required(parameters.required()).build();
        }
        return null;
    }

    static List<ToolExecutionRequest> toToolExecutionRequests(List<ToolCall> toolCalls) {
        return toolCalls.stream().map(toolCall -> ToolExecutionRequest.builder().id(toolCall.getId()).name(toolCall.getFunction().getName()).arguments(OllamaJsonUtils.toJsonWithoutIdent(toolCall.getFunction().getArguments())).build()).collect(Collectors.toList());
    }

    static String toOllamaResponseFormat(ResponseFormat responseFormat) {
        if (responseFormat == null || responseFormat.type() == ResponseFormatType.TEXT) {
            return null;
        }
        if (responseFormat.type() == ResponseFormatType.JSON && responseFormat.jsonSchema() == null) {
            return "json";
        }
        return OllamaJsonUtils.toJson(JsonSchemaElementUtils.toMap((JsonSchemaElement)responseFormat.jsonSchema().rootElement()));
    }

    static FinishReason toFinishReason(OllamaChatResponse ollamaChatResponse) {
        if (ollamaChatResponse.getMessage() != null && !Utils.isNullOrEmpty(ollamaChatResponse.getMessage().getToolCalls())) {
            return FinishReason.TOOL_EXECUTION;
        }
        String doneReason = ollamaChatResponse.getDoneReason();
        if (doneReason == null) {
            return null;
        }
        switch (doneReason) {
            case "stop": {
                return FinishReason.STOP;
            }
            case "length": {
                return FinishReason.LENGTH;
            }
        }
        return FinishReason.OTHER;
    }

    static void validate(ChatRequestParameters chatRequestParameters) {
        if (chatRequestParameters.frequencyPenalty() != null) {
            throw new UnsupportedFeatureException("'frequencyPenalty' parameter is not supported by Ollama");
        }
        if (chatRequestParameters.presencePenalty() != null) {
            throw new UnsupportedFeatureException("'presencePenalty' parameter is not supported by Ollama");
        }
    }

    static AiMessage aiMessageFrom(Message message, boolean returnThinking) {
        String content = message.getContent();
        String thinking = null;
        if (returnThinking) {
            thinking = message.getThinking();
        }
        List toolCalls = Utils.getOrDefault(message.getToolCalls(), Collections.emptyList());
        return AiMessage.builder().text(Utils.isNullOrEmpty((String)content) ? null : content).thinking(Utils.isNullOrEmpty((String)thinking) ? null : thinking).toolExecutionRequests(InternalOllamaHelper.toToolExecutionRequests(toolCalls)).build();
    }

    static ChatResponseMetadata chatResponseMetadataFrom(OllamaChatResponse ollamaChatResponse) {
        return InternalOllamaHelper.chatResponseMetadataFrom(ollamaChatResponse.getModel(), InternalOllamaHelper.toFinishReason(ollamaChatResponse), new TokenUsage(ollamaChatResponse.getPromptEvalCount(), ollamaChatResponse.getEvalCount()));
    }

    static OllamaChatRequest toOllamaChatRequest(ChatRequest chatRequest, boolean stream) {
        OllamaChatRequestParameters requestParameters = (OllamaChatRequestParameters)chatRequest.parameters();
        return OllamaChatRequest.builder().model(requestParameters.modelName()).messages(InternalOllamaHelper.toOllamaMessages(chatRequest.messages())).options(Options.builder().mirostat(requestParameters.mirostat()).mirostatEta(requestParameters.mirostatEta()).mirostatTau(requestParameters.mirostatTau()).repeatLastN(requestParameters.repeatLastN()).temperature(requestParameters.temperature()).topK(requestParameters.topK()).topP(requestParameters.topP()).repeatPenalty(requestParameters.repeatPenalty()).seed(requestParameters.seed()).numPredict(requestParameters.maxOutputTokens()).numCtx(requestParameters.numCtx()).numThread(requestParameters.numThread()).numKeep(requestParameters.numKeep()).typicalP(requestParameters.typicalP()).numBatch(requestParameters.numBatch()).numGPU(requestParameters.numGPU()).mainGPU(requestParameters.mainGPU()).useMmap(requestParameters.useMmap()).stop(requestParameters.stopSequences()).minP(requestParameters.minP()).build()).format(InternalOllamaHelper.toOllamaResponseFormat(requestParameters.responseFormat())).stream(stream).tools(InternalOllamaHelper.toOllamaTools(chatRequest.toolSpecifications())).keepAlive(requestParameters.keepAlive()).think(requestParameters.think()).build();
    }

    static ChatResponseMetadata chatResponseMetadataFrom(String modelName, FinishReason finishReason, TokenUsage tokenUsage) {
        return ChatResponseMetadata.builder().modelName(modelName).finishReason(finishReason).tokenUsage(tokenUsage).build();
    }

    private static Message messagesWithImageSupport(UserMessage userMessage) {
        Map<ContentType, List<Content>> groupedContents = userMessage.contents().stream().collect(Collectors.groupingBy(Content::type));
        String text = InternalOllamaHelper.concatenateTextContents(userMessage);
        List<ImageContent> imageContents = groupedContents.getOrDefault(ContentType.IMAGE, Collections.emptyList()).stream().map(content -> (ImageContent)content).collect(Collectors.toList());
        return Message.builder().role(InternalOllamaHelper.toOllamaRole(userMessage.type())).content(text).images(ImageUtils.base64EncodeImageList(imageContents)).build();
    }

    private static String concatenateTextContents(UserMessage userMessage) {
        return userMessage.contents().stream().filter(content -> ContentType.TEXT.equals((Object)content.type())).map(content -> ((TextContent)content).text()).collect(Collectors.joining("\n"));
    }

    private static Message otherMessages(ChatMessage chatMessage) {
        if (chatMessage instanceof CustomMessage) {
            CustomMessage customMessage = (CustomMessage)chatMessage;
            return Message.builder().additionalFields(customMessage.attributes()).build();
        }
        List toolCalls = null;
        if (chatMessage instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage)chatMessage;
            List toolExecutionRequests = aiMessage.toolExecutionRequests();
            toolCalls = Optional.ofNullable(toolExecutionRequests).map(reqs -> reqs.stream().map(toolExecutionRequest -> {
                TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>(){};
                FunctionCall functionCall = FunctionCall.builder().name(toolExecutionRequest.name()).arguments((Map<String, Object>)OllamaJsonUtils.fromJson(toolExecutionRequest.arguments(), typeReference)).build();
                return ToolCall.builder().id(toolExecutionRequest.id()).function(functionCall).build();
            }).collect(Collectors.toList())).orElse(null);
        }
        return Message.builder().role(InternalOllamaHelper.toOllamaRole(chatMessage.type())).content(InternalOllamaHelper.toText(chatMessage)).toolCalls(toolCalls).build();
    }

    private static String toText(ChatMessage chatMessage) {
        if (chatMessage instanceof SystemMessage) {
            SystemMessage systemMessage = (SystemMessage)chatMessage;
            return systemMessage.text();
        }
        if (chatMessage instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)chatMessage;
            return InternalOllamaHelper.concatenateTextContents(userMessage);
        }
        if (chatMessage instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage)chatMessage;
            return aiMessage.text();
        }
        if (chatMessage instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage toolExecutionResultMessage = (ToolExecutionResultMessage)chatMessage;
            if (!toolExecutionResultMessage.hasSingleText()) {
                throw new UnsupportedFeatureException("Ollama does not support non-text content in tool results. Only text content is supported.");
            }
            return toolExecutionResultMessage.text();
        }
        throw new IllegalArgumentException("Unsupported message type: " + chatMessage.type());
    }

    private static Role toOllamaRole(ChatMessageType chatMessageType) {
        switch (chatMessageType) {
            case SYSTEM: {
                return Role.SYSTEM;
            }
            case USER: {
                return Role.USER;
            }
            case AI: {
                return Role.ASSISTANT;
            }
            case TOOL_EXECUTION_RESULT: {
                return Role.TOOL;
            }
        }
        throw new IllegalArgumentException("Unknown ChatMessageType: " + chatMessageType);
    }

    private static boolean messagesContainOnlyTextAndOrImageContentType(List<ChatMessage> messages) {
        Set contentTypes = messages.stream().filter(isUserMessage).map(UserMessage.class::cast).map(UserMessage::contents).filter(Objects::nonNull).flatMap(Collection::stream).map(Content::type).collect(Collectors.toSet());
        if (contentTypes.size() > 2) {
            return false;
        }
        contentTypes.remove(ContentType.TEXT);
        contentTypes.remove(ContentType.IMAGE);
        return contentTypes.isEmpty();
    }
}

