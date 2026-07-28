/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.AudioContent
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.ImageContent$DetailLevel
 *  dev.langchain4j.data.message.PdfFileContent
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.data.message.VideoContent
 *  dev.langchain4j.data.video.Video
 *  dev.langchain4j.exception.ContentFilteredException
 *  dev.langchain4j.exception.InternalServerException
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.internal.ToolSpecificationUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonRawSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.openai.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.AudioContent;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.VideoContent;
import dev.langchain4j.data.video.Video;
import dev.langchain4j.exception.ContentFilteredException;
import dev.langchain4j.exception.InternalServerException;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.internal.ToolSpecificationUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonRawSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.LogProb;
import dev.langchain4j.model.openai.OpenAiChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiTokenUsage;
import dev.langchain4j.model.openai.internal.chat.AssistantMessage;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse;
import dev.langchain4j.model.openai.internal.chat.Content;
import dev.langchain4j.model.openai.internal.chat.ContentType;
import dev.langchain4j.model.openai.internal.chat.Function;
import dev.langchain4j.model.openai.internal.chat.FunctionCall;
import dev.langchain4j.model.openai.internal.chat.FunctionMessage;
import dev.langchain4j.model.openai.internal.chat.ImageDetail;
import dev.langchain4j.model.openai.internal.chat.ImageUrl;
import dev.langchain4j.model.openai.internal.chat.InputAudio;
import dev.langchain4j.model.openai.internal.chat.JsonSchema;
import dev.langchain4j.model.openai.internal.chat.LogProbs;
import dev.langchain4j.model.openai.internal.chat.Message;
import dev.langchain4j.model.openai.internal.chat.PdfFile;
import dev.langchain4j.model.openai.internal.chat.ResponseFormat;
import dev.langchain4j.model.openai.internal.chat.ResponseFormatType;
import dev.langchain4j.model.openai.internal.chat.SystemMessage;
import dev.langchain4j.model.openai.internal.chat.Tool;
import dev.langchain4j.model.openai.internal.chat.ToolCall;
import dev.langchain4j.model.openai.internal.chat.ToolChoiceMode;
import dev.langchain4j.model.openai.internal.chat.ToolMessage;
import dev.langchain4j.model.openai.internal.chat.ToolType;
import dev.langchain4j.model.openai.internal.chat.UserMessage;
import dev.langchain4j.model.openai.internal.chat.VideoUrl;
import dev.langchain4j.model.openai.internal.shared.CompletionTokensDetails;
import dev.langchain4j.model.openai.internal.shared.PromptTokensDetails;
import dev.langchain4j.model.openai.internal.shared.Usage;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Internal
public class OpenAiUtils {
    public static final String DEFAULT_OPENAI_URL = "https://api.openai.com/v1";
    public static final String DEFAULT_USER_AGENT = "langchain4j-openai";

    public static List<Message> toOpenAiMessages(List<ChatMessage> messages) {
        return OpenAiUtils.toOpenAiMessages(messages, false, null, false);
    }

    public static List<Message> toOpenAiMessages(List<ChatMessage> messages, boolean sendThinking, String thinkingFieldName) {
        return OpenAiUtils.toOpenAiMessages(messages, sendThinking, thinkingFieldName, false);
    }

    public static List<Message> toOpenAiMessages(List<ChatMessage> messages, boolean sendThinking, String thinkingFieldName, boolean useInputImageFormat) {
        return messages.stream().map(message -> OpenAiUtils.toOpenAiMessage(message, sendThinking, thinkingFieldName, useInputImageFormat)).collect(Collectors.toList());
    }

    public static Message toOpenAiMessage(ChatMessage message) {
        return OpenAiUtils.toOpenAiMessage(message, false, null, false);
    }

    public static Message toOpenAiMessage(ChatMessage message, boolean sendThinking, String thinkingFieldName) {
        return OpenAiUtils.toOpenAiMessage(message, sendThinking, thinkingFieldName, false);
    }

    public static Message toOpenAiMessage(ChatMessage message, boolean sendThinking, String thinkingFieldName, boolean useInputImageFormat) {
        if (message instanceof dev.langchain4j.data.message.SystemMessage) {
            return SystemMessage.from(((dev.langchain4j.data.message.SystemMessage)message).text());
        }
        if (message instanceof dev.langchain4j.data.message.UserMessage) {
            dev.langchain4j.data.message.UserMessage userMessage = (dev.langchain4j.data.message.UserMessage)message;
            if (userMessage.hasSingleText()) {
                return UserMessage.builder().content(userMessage.singleText()).name(userMessage.name()).build();
            }
            return UserMessage.builder().content(userMessage.contents().stream().map(content -> OpenAiUtils.toOpenAiContent(content, useInputImageFormat)).collect(Collectors.toList())).name(userMessage.name()).build();
        }
        if (message instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage)message;
            String thinking = null;
            if (sendThinking && !Utils.isNullOrEmpty((String)aiMessage.thinking())) {
                thinking = aiMessage.thinking();
            }
            if (!aiMessage.hasToolExecutionRequests()) {
                AssistantMessage.Builder builder = AssistantMessage.builder().content(aiMessage.text());
                if (thinking != null) {
                    builder.customParameter(thinkingFieldName, thinking);
                }
                return builder.build();
            }
            ToolExecutionRequest toolExecutionRequest = (ToolExecutionRequest)aiMessage.toolExecutionRequests().get(0);
            if (toolExecutionRequest.id() == null) {
                FunctionCall functionCall = FunctionCall.builder().name(toolExecutionRequest.name()).arguments(toolExecutionRequest.arguments()).build();
                AssistantMessage.Builder builder = AssistantMessage.builder().functionCall(functionCall);
                if (thinking != null) {
                    builder.customParameter(thinkingFieldName, thinking);
                }
                return builder.build();
            }
            List<ToolCall> toolCalls = aiMessage.toolExecutionRequests().stream().map(it -> ToolCall.builder().id(it.id()).type(ToolType.FUNCTION).function(FunctionCall.builder().name(it.name()).arguments(Utils.isNullOrBlank((String)it.arguments()) ? "{}" : it.arguments()).build()).build()).collect(Collectors.toList());
            AssistantMessage.Builder builder = AssistantMessage.builder().content(aiMessage.text()).toolCalls(toolCalls);
            if (thinking != null) {
                builder.customParameter(thinkingFieldName, thinking);
            }
            return builder.build();
        }
        if (message instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage toolExecutionResultMessage = (ToolExecutionResultMessage)message;
            if (!toolExecutionResultMessage.hasSingleText()) {
                throw new UnsupportedFeatureException("OpenAI Chat Completions API does not support non-text content in tool results. Only text content is supported.");
            }
            if (toolExecutionResultMessage.id() == null) {
                return FunctionMessage.from(toolExecutionResultMessage.toolName(), toolExecutionResultMessage.text());
            }
            return ToolMessage.from(toolExecutionResultMessage.id(), toolExecutionResultMessage.text());
        }
        throw Exceptions.illegalArgument((String)("Unknown message type: " + message.type()), (Object[])new Object[0]);
    }

    private static Content toOpenAiContent(dev.langchain4j.data.message.Content content, boolean useInputImageFormat) {
        if (content instanceof TextContent) {
            return OpenAiUtils.toOpenAiContent((TextContent)content);
        }
        if (content instanceof ImageContent) {
            return OpenAiUtils.toOpenAiContent((ImageContent)content, useInputImageFormat);
        }
        if (content instanceof VideoContent) {
            VideoContent videoContent = (VideoContent)content;
            return OpenAiUtils.toOpenAiContent(videoContent);
        }
        if (content instanceof AudioContent) {
            AudioContent audioContent = (AudioContent)content;
            return OpenAiUtils.toOpenAiContent(audioContent);
        }
        if (content instanceof PdfFileContent) {
            PdfFileContent pdfFileContent = (PdfFileContent)content;
            return OpenAiUtils.toOpenAiContent(pdfFileContent);
        }
        throw Exceptions.illegalArgument((String)("Unknown content type: " + content), (Object[])new Object[0]);
    }

    private static Content toOpenAiContent(TextContent content) {
        return Content.builder().type(ContentType.TEXT).text(content.text()).build();
    }

    private static Content toOpenAiContent(ImageContent content, boolean useInputImageFormat) {
        if (useInputImageFormat) {
            return Content.builder().type(ContentType.INPUT_IMAGE).inputImageUrl(OpenAiUtils.toUrl(content.image())).build();
        }
        return Content.builder().type(ContentType.IMAGE_URL).imageUrl(ImageUrl.builder().url(OpenAiUtils.toUrl(content.image())).detail(OpenAiUtils.toDetail(content.detailLevel())).build()).build();
    }

    private static Content toOpenAiContent(VideoContent content) {
        return Content.builder().type(ContentType.VIDEO_URL).videoUrl(VideoUrl.builder().url(OpenAiUtils.toVideoUrl(content.video())).build()).build();
    }

    private static Content toOpenAiContent(AudioContent audioContent) {
        return Content.builder().type(ContentType.AUDIO).inputAudio(InputAudio.builder().data(ValidationUtils.ensureNotBlank((String)audioContent.audio().base64Data(), (String)"audio.base64Data")).format(OpenAiUtils.extractSubtype(ValidationUtils.ensureNotBlank((String)audioContent.audio().mimeType(), (String)"audio.mimeType"))).build()).build();
    }

    private static Content toOpenAiContent(PdfFileContent pdfFileContent) {
        String fileData = pdfFileContent.pdfFile().url() != null ? pdfFileContent.pdfFile().url().toString() : String.format("data:%s;base64,%s", pdfFileContent.pdfFile().mimeType(), pdfFileContent.pdfFile().base64Data());
        return Content.builder().type(ContentType.FILE).file(PdfFile.builder().fileData(fileData).filename("pdf_file").build()).build();
    }

    private static String extractSubtype(String mimetype) {
        return mimetype.split("/")[1];
    }

    private static String toUrl(Image image) {
        if (image.url() != null) {
            return image.url().toString();
        }
        return String.format("data:%s;base64,%s", image.mimeType(), image.base64Data());
    }

    private static String toVideoUrl(Video video) {
        if (video.url() != null) {
            return video.url().toString();
        }
        return String.format("data:%s;base64,%s", video.mimeType(), video.base64Data());
    }

    private static ImageDetail toDetail(ImageContent.DetailLevel detailLevel) {
        if (detailLevel == null) {
            return null;
        }
        switch (detailLevel) {
            case LOW: {
                return ImageDetail.LOW;
            }
            case HIGH: {
                return ImageDetail.HIGH;
            }
            case AUTO: {
                return ImageDetail.AUTO;
            }
        }
        throw new UnsupportedFeatureException("Unsupported detail level: " + detailLevel);
    }

    public static List<Tool> toTools(Collection<ToolSpecification> toolSpecifications, boolean strict) {
        return toolSpecifications.stream().map(toolSpecification -> OpenAiUtils.toTool(toolSpecification, strict)).collect(Collectors.toList());
    }

    private static Tool toTool(ToolSpecification toolSpecification, boolean strict) {
        boolean effectiveStrict = ToolSpecificationUtils.isEffectivelyStrict((ToolSpecification)toolSpecification, (boolean)strict);
        Function.Builder functionBuilder = Function.builder().name(toolSpecification.name()).description(toolSpecification.description()).parameters(OpenAiUtils.toOpenAiParameters(toolSpecification.parameters(), effectiveStrict));
        if (effectiveStrict) {
            functionBuilder.strict(true);
        }
        Function function = functionBuilder.build();
        return Tool.from(function);
    }

    @Deprecated
    public static List<Function> toFunctions(Collection<ToolSpecification> toolSpecifications) {
        return toolSpecifications.stream().map(OpenAiUtils::toFunction).collect(Collectors.toList());
    }

    @Deprecated
    private static Function toFunction(ToolSpecification toolSpecification) {
        return Function.builder().name(toolSpecification.name()).description(toolSpecification.description()).parameters(OpenAiUtils.toOpenAiParameters(toolSpecification.parameters(), false)).build();
    }

    private static Map<String, Object> toOpenAiParameters(JsonObjectSchema parameters, boolean strict) {
        if (parameters != null) {
            return JsonSchemaElementUtils.toMap((JsonSchemaElement)parameters, (boolean)strict);
        }
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("type", "object");
        map.put("properties", new HashMap());
        map.put("required", new ArrayList());
        if (strict) {
            map.put("additionalProperties", false);
        }
        return map;
    }

    public static AiMessage aiMessageFrom(ChatCompletionResponse response) {
        return OpenAiUtils.aiMessageFrom(response, false);
    }

    public static AiMessage aiMessageFrom(ChatCompletionResponse response, boolean returnThinking) {
        if (Utils.isNullOrEmpty(response.choices())) {
            throw new InternalServerException("Chat completion failed: no choices returned in response");
        }
        if (response.choices().size() > 1) {
            throw new InternalServerException(String.format("Chat completion failed: expected exactly one choice, but got %s choices", response.choices().size()));
        }
        AssistantMessage assistantMessage = response.choices().get(0).message();
        String refusal = assistantMessage.refusal();
        if (Utils.isNotNullOrBlank((String)refusal)) {
            throw new ContentFilteredException(refusal);
        }
        String content = assistantMessage.content();
        String reasoningContent = null;
        if (returnThinking) {
            reasoningContent = assistantMessage.reasoningContent();
        }
        List toolExecutionRequests = Utils.getOrDefault(assistantMessage.toolCalls(), Collections.emptyList()).stream().filter(toolCall -> toolCall.type() == ToolType.FUNCTION).map(OpenAiUtils::toToolExecutionRequest).collect(Collectors.toList());
        FunctionCall functionCall = assistantMessage.functionCall();
        if (functionCall != null) {
            ToolExecutionRequest toolExecutionRequest = ToolExecutionRequest.builder().name(functionCall.name()).arguments(functionCall.arguments()).build();
            toolExecutionRequests.add(toolExecutionRequest);
        }
        return AiMessage.builder().text(Utils.isNullOrEmpty((String)content) ? null : content).thinking(Utils.isNullOrEmpty((String)reasoningContent) ? null : reasoningContent).toolExecutionRequests(toolExecutionRequests).build();
    }

    private static ToolExecutionRequest toToolExecutionRequest(ToolCall toolCall) {
        FunctionCall functionCall = toolCall.function();
        return ToolExecutionRequest.builder().id(toolCall.id()).name(functionCall.name()).arguments(functionCall.arguments()).build();
    }

    public static OpenAiTokenUsage tokenUsageFrom(Usage openAiUsage) {
        if (openAiUsage == null) {
            return null;
        }
        PromptTokensDetails promptTokensDetails = openAiUsage.promptTokensDetails();
        OpenAiTokenUsage.InputTokensDetails inputTokensDetails = null;
        if (promptTokensDetails != null) {
            inputTokensDetails = OpenAiTokenUsage.InputTokensDetails.builder().cachedTokens(promptTokensDetails.cachedTokens()).build();
        }
        CompletionTokensDetails completionTokensDetails = openAiUsage.completionTokensDetails();
        OpenAiTokenUsage.OutputTokensDetails outputTokensDetails = null;
        if (completionTokensDetails != null) {
            outputTokensDetails = OpenAiTokenUsage.OutputTokensDetails.builder().reasoningTokens(completionTokensDetails.reasoningTokens()).build();
        }
        return OpenAiTokenUsage.builder().inputTokenCount(openAiUsage.promptTokens()).inputTokensDetails(inputTokensDetails).outputTokenCount(openAiUsage.completionTokens()).outputTokensDetails(outputTokensDetails).totalTokenCount(openAiUsage.totalTokens()).build();
    }

    public static List<LogProb> logProbsFrom(LogProbs logProbs) {
        if (logProbs == null || logProbs.content() == null) {
            return null;
        }
        return logProbs.content().stream().map(OpenAiUtils::toLogProb).collect(Collectors.toList());
    }

    private static LogProb toLogProb(dev.langchain4j.model.openai.internal.chat.LogProb internal) {
        return LogProb.builder().token(internal.token()).logprob(internal.logprob()).bytes(internal.bytes()).topLogprobs(internal.topLogprobs() == null ? null : internal.topLogprobs().stream().map(OpenAiUtils::toLogProb).collect(Collectors.toList())).build();
    }

    public static FinishReason finishReasonFrom(String openAiFinishReason) {
        if (openAiFinishReason == null) {
            return null;
        }
        switch (openAiFinishReason) {
            case "stop": {
                return FinishReason.STOP;
            }
            case "length": {
                return FinishReason.LENGTH;
            }
            case "tool_calls": 
            case "function_call": {
                return FinishReason.TOOL_EXECUTION;
            }
            case "content_filter": {
                return FinishReason.CONTENT_FILTER;
            }
        }
        return null;
    }

    static ResponseFormat toOpenAiResponseFormat(dev.langchain4j.model.chat.request.ResponseFormat responseFormat, Boolean strict) {
        if (responseFormat == null || responseFormat.type() == dev.langchain4j.model.chat.request.ResponseFormatType.TEXT) {
            return null;
        }
        dev.langchain4j.model.chat.request.json.JsonSchema jsonSchema = responseFormat.jsonSchema();
        if (jsonSchema == null) {
            return ResponseFormat.builder().type(ResponseFormatType.JSON_OBJECT).build();
        }
        if (!(jsonSchema.rootElement() instanceof JsonObjectSchema) && !(jsonSchema.rootElement() instanceof JsonRawSchema)) {
            throw new IllegalArgumentException("For OpenAI, the root element of the JSON Schema must be either a JsonObjectSchema or a JsonRawSchema, but it was: " + jsonSchema.rootElement().getClass());
        }
        JsonSchema openAiJsonSchema = JsonSchema.builder().name(jsonSchema.name()).strict(strict).schema(JsonSchemaElementUtils.toMap((JsonSchemaElement)jsonSchema.rootElement(), (boolean)strict)).build();
        return ResponseFormat.builder().type(ResponseFormatType.JSON_SCHEMA).jsonSchema(openAiJsonSchema).build();
    }

    public static ToolChoiceMode toOpenAiToolChoice(ToolChoice toolChoice) {
        if (toolChoice == null) {
            return null;
        }
        switch (toolChoice) {
            case AUTO: {
                return ToolChoiceMode.AUTO;
            }
            case REQUIRED: {
                return ToolChoiceMode.REQUIRED;
            }
            case NONE: {
                return ToolChoiceMode.NONE;
            }
        }
        return null;
    }

    public static Response<AiMessage> convertResponse(ChatResponse chatResponse) {
        return Response.from(chatResponse.aiMessage(), (TokenUsage)chatResponse.metadata().tokenUsage(), (FinishReason)chatResponse.metadata().finishReason());
    }

    public static void validate(ChatRequestParameters parameters) {
        if (parameters.topK() != null) {
            throw new UnsupportedFeatureException("'topK' parameter is not supported by OpenAI");
        }
    }

    public static dev.langchain4j.model.chat.request.ResponseFormat fromOpenAiResponseFormat(String responseFormat) {
        if ("json_object".equals(responseFormat)) {
            return dev.langchain4j.model.chat.request.ResponseFormat.JSON;
        }
        return null;
    }

    public static ChatCompletionRequest.Builder toOpenAiChatRequest(ChatRequest chatRequest, OpenAiChatRequestParameters parameters, Boolean strictTools, Boolean strictJsonSchema) {
        return OpenAiUtils.toOpenAiChatRequest(chatRequest, parameters, false, null, strictTools, strictJsonSchema);
    }

    public static ChatCompletionRequest.Builder toOpenAiChatRequest(ChatRequest chatRequest, OpenAiChatRequestParameters parameters, boolean sendThinking, String thinkingFieldName, Boolean strictTools, Boolean strictJsonSchema) {
        return OpenAiUtils.toOpenAiChatRequest(chatRequest, parameters, sendThinking, thinkingFieldName, strictTools, strictJsonSchema, false);
    }

    public static ChatCompletionRequest.Builder toOpenAiChatRequest(ChatRequest chatRequest, OpenAiChatRequestParameters parameters, boolean sendThinking, String thinkingFieldName, Boolean strictTools, Boolean strictJsonSchema, boolean useInputImageFormat) {
        return ChatCompletionRequest.builder().messages(OpenAiUtils.toOpenAiMessages(chatRequest.messages(), sendThinking, thinkingFieldName, useInputImageFormat)).model(parameters.modelName()).temperature(parameters.temperature()).topP(parameters.topP()).frequencyPenalty(parameters.frequencyPenalty()).presencePenalty(parameters.presencePenalty()).maxTokens(parameters.maxOutputTokens()).stop(parameters.stopSequences()).tools(OpenAiUtils.toTools(parameters.toolSpecifications(), strictTools)).toolChoice(OpenAiUtils.toOpenAiToolChoice(parameters.toolChoice())).responseFormat(OpenAiUtils.toOpenAiResponseFormat(parameters.responseFormat(), strictJsonSchema)).maxCompletionTokens(parameters.maxCompletionTokens()).logitBias(parameters.logitBias()).parallelToolCalls(parameters.parallelToolCalls()).seed(parameters.seed()).user(parameters.user()).store(parameters.store()).metadata(parameters.metadata()).serviceTier(parameters.serviceTier()).reasoningEffort(parameters.reasoningEffort()).logprobs(parameters.logprobs()).topLogprobs(parameters.topLogprobs()).customParameters(parameters.customParameters());
    }
}

