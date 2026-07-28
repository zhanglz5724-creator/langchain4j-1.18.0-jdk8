/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.openai.core.JsonValue
 *  com.openai.models.FunctionDefinition
 *  com.openai.models.FunctionDefinition$Builder
 *  com.openai.models.FunctionParameters
 *  com.openai.models.FunctionParameters$Builder
 *  com.openai.models.ReasoningEffort
 *  com.openai.models.ResponseFormatJsonObject
 *  com.openai.models.chat.completions.ChatCompletion
 *  com.openai.models.chat.completions.ChatCompletion$Choice
 *  com.openai.models.chat.completions.ChatCompletion$Choice$FinishReason
 *  com.openai.models.chat.completions.ChatCompletionAssistantMessageParam
 *  com.openai.models.chat.completions.ChatCompletionChunk$Choice$FinishReason
 *  com.openai.models.chat.completions.ChatCompletionContentPart
 *  com.openai.models.chat.completions.ChatCompletionContentPart$File
 *  com.openai.models.chat.completions.ChatCompletionContentPart$File$FileObject
 *  com.openai.models.chat.completions.ChatCompletionContentPartImage
 *  com.openai.models.chat.completions.ChatCompletionContentPartImage$ImageUrl
 *  com.openai.models.chat.completions.ChatCompletionContentPartImage$ImageUrl$Builder
 *  com.openai.models.chat.completions.ChatCompletionContentPartImage$ImageUrl$Detail
 *  com.openai.models.chat.completions.ChatCompletionContentPartInputAudio
 *  com.openai.models.chat.completions.ChatCompletionContentPartInputAudio$InputAudio
 *  com.openai.models.chat.completions.ChatCompletionContentPartInputAudio$InputAudio$Format
 *  com.openai.models.chat.completions.ChatCompletionContentPartText
 *  com.openai.models.chat.completions.ChatCompletionCreateParams
 *  com.openai.models.chat.completions.ChatCompletionCreateParams$Builder
 *  com.openai.models.chat.completions.ChatCompletionCreateParams$LogitBias
 *  com.openai.models.chat.completions.ChatCompletionCreateParams$Metadata
 *  com.openai.models.chat.completions.ChatCompletionCreateParams$ServiceTier
 *  com.openai.models.chat.completions.ChatCompletionCreateParams$Stop
 *  com.openai.models.chat.completions.ChatCompletionFunctionTool
 *  com.openai.models.chat.completions.ChatCompletionMessage
 *  com.openai.models.chat.completions.ChatCompletionMessageFunctionToolCall
 *  com.openai.models.chat.completions.ChatCompletionMessageFunctionToolCall$Function
 *  com.openai.models.chat.completions.ChatCompletionMessageParam
 *  com.openai.models.chat.completions.ChatCompletionMessageToolCall
 *  com.openai.models.chat.completions.ChatCompletionSystemMessageParam
 *  com.openai.models.chat.completions.ChatCompletionTool
 *  com.openai.models.chat.completions.ChatCompletionToolChoiceOption
 *  com.openai.models.chat.completions.ChatCompletionToolChoiceOption$Auto
 *  com.openai.models.chat.completions.ChatCompletionToolMessageParam
 *  com.openai.models.chat.completions.ChatCompletionUserMessageParam
 *  com.openai.models.chat.completions.ChatCompletionUserMessageParam$Builder
 *  com.openai.models.completions.CompletionUsage
 *  com.openai.models.completions.CompletionUsage$CompletionTokensDetails
 *  com.openai.models.completions.CompletionUsage$PromptTokensDetails
 *  com.openai.models.embeddings.CreateEmbeddingResponse$Usage
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
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
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.internal.ToolSpecificationUtils
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
 *  dev.langchain4j.model.output.FinishReason
 */
package dev.langchain4j.model.openaiofficial;

import com.openai.core.JsonValue;
import com.openai.models.FunctionDefinition;
import com.openai.models.FunctionParameters;
import com.openai.models.ReasoningEffort;
import com.openai.models.ResponseFormatJsonObject;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionAssistantMessageParam;
import com.openai.models.chat.completions.ChatCompletionChunk;
import com.openai.models.chat.completions.ChatCompletionContentPart;
import com.openai.models.chat.completions.ChatCompletionContentPartImage;
import com.openai.models.chat.completions.ChatCompletionContentPartInputAudio;
import com.openai.models.chat.completions.ChatCompletionContentPartText;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionFunctionTool;
import com.openai.models.chat.completions.ChatCompletionMessage;
import com.openai.models.chat.completions.ChatCompletionMessageFunctionToolCall;
import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.chat.completions.ChatCompletionMessageToolCall;
import com.openai.models.chat.completions.ChatCompletionSystemMessageParam;
import com.openai.models.chat.completions.ChatCompletionTool;
import com.openai.models.chat.completions.ChatCompletionToolChoiceOption;
import com.openai.models.chat.completions.ChatCompletionToolMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;
import com.openai.models.completions.CompletionUsage;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.AudioContent;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.internal.ToolSpecificationUtils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonRawSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatRequestParameters;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialTokenUsage;
import dev.langchain4j.model.output.FinishReason;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

class InternalOpenAiOfficialHelper {
    InternalOpenAiOfficialHelper() {
    }

    static List<ChatCompletionMessageParam> toOpenAiMessages(List<ChatMessage> messages) {
        return messages.stream().map(InternalOpenAiOfficialHelper::toOpenAiMessage).collect(Collectors.toList());
    }

    static ChatCompletionMessageParam toOpenAiMessage(ChatMessage message) {
        if (message instanceof SystemMessage) {
            SystemMessage systemMessage = (SystemMessage)message;
            return ChatCompletionMessageParam.ofSystem((ChatCompletionSystemMessageParam)ChatCompletionSystemMessageParam.builder().content(systemMessage.text()).build());
        }
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)message;
            ChatCompletionUserMessageParam.Builder builder = ChatCompletionUserMessageParam.builder();
            if (userMessage.hasSingleText()) {
                builder.content(userMessage.singleText());
            } else {
                builder.contentOfArrayOfContentParts(InternalOpenAiOfficialHelper.toOpenAiContent(userMessage.contents()));
            }
            if (userMessage.name() != null) {
                builder.name(userMessage.name());
            }
            return ChatCompletionMessageParam.ofUser((ChatCompletionUserMessageParam)builder.build());
        }
        if (message instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage)message;
            if (!aiMessage.hasToolExecutionRequests()) {
                return ChatCompletionMessageParam.ofAssistant((ChatCompletionAssistantMessageParam)ChatCompletionAssistantMessageParam.builder().content(aiMessage.text()).build());
            }
            List toolCalls = aiMessage.toolExecutionRequests().stream().map(it -> ChatCompletionMessageToolCall.ofFunction((ChatCompletionMessageFunctionToolCall)ChatCompletionMessageFunctionToolCall.builder().id(it.id()).function(ChatCompletionMessageFunctionToolCall.Function.builder().name(it.name()).arguments(it.arguments()).build()).build())).collect(Collectors.toList());
            return ChatCompletionMessageParam.ofAssistant((ChatCompletionAssistantMessageParam)ChatCompletionAssistantMessageParam.builder().content(aiMessage.text() != null ? aiMessage.text() : "").toolCalls(toolCalls).build());
        }
        if (message instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage toolExecutionResultMessage = (ToolExecutionResultMessage)message;
            if (!toolExecutionResultMessage.hasSingleText()) {
                throw new UnsupportedFeatureException("OpenAI Chat Completions API does not support non-text content in tool results. Only text content is supported.");
            }
            return ChatCompletionMessageParam.ofTool((ChatCompletionToolMessageParam)ChatCompletionToolMessageParam.builder().toolCallId(toolExecutionResultMessage.id()).content(toolExecutionResultMessage.text()).build());
        }
        throw Exceptions.illegalArgument((String)("Unknown message type: " + message.type()), (Object[])new Object[0]);
    }

    private static List<ChatCompletionContentPart> toOpenAiContent(List<Content> contents) {
        ArrayList<ChatCompletionContentPart> parts = new ArrayList<ChatCompletionContentPart>();
        for (Content content : contents) {
            if (content instanceof TextContent) {
                TextContent textContent = (TextContent)content;
                parts.add(ChatCompletionContentPart.ofText((ChatCompletionContentPartText)ChatCompletionContentPartText.builder().text(textContent.text()).build()));
                continue;
            }
            if (content instanceof ImageContent) {
                ImageContent imageContent = (ImageContent)content;
                ChatCompletionContentPartImage.ImageUrl.Builder imageUrlBuilder = ChatCompletionContentPartImage.ImageUrl.builder();
                if (imageContent.image().url() != null) {
                    imageUrlBuilder.url(imageContent.image().url().toString());
                    imageUrlBuilder.detail(InternalOpenAiOfficialHelper.toImageDetail(imageContent.detailLevel()));
                    parts.add(ChatCompletionContentPart.ofImageUrl((ChatCompletionContentPartImage)ChatCompletionContentPartImage.builder().imageUrl(imageUrlBuilder.build()).build()));
                    continue;
                }
                if (imageContent.image().base64Data() != null) {
                    imageUrlBuilder.url("data:" + imageContent.image().mimeType() + ";base64," + imageContent.image().base64Data());
                    imageUrlBuilder.detail(InternalOpenAiOfficialHelper.toImageDetail(imageContent.detailLevel()));
                    parts.add(ChatCompletionContentPart.ofImageUrl((ChatCompletionContentPartImage)ChatCompletionContentPartImage.builder().imageUrl(imageUrlBuilder.build()).build()));
                    continue;
                }
                throw new UnsupportedFeatureException("Image URL is not present.");
            }
            if (content instanceof AudioContent) {
                AudioContent audioContent = (AudioContent)content;
                parts.add(ChatCompletionContentPart.ofInputAudio((ChatCompletionContentPartInputAudio)ChatCompletionContentPartInputAudio.builder().inputAudio(ChatCompletionContentPartInputAudio.builder().inputAudio(ChatCompletionContentPartInputAudio.InputAudio.builder().data(ValidationUtils.ensureNotBlank((String)audioContent.audio().base64Data(), (String)"audio.base64Data")).format(ChatCompletionContentPartInputAudio.InputAudio.Format.of((String)ValidationUtils.ensureNotBlank((String)audioContent.audio().mimeType(), (String)"audio.mimeType").split("/")[1])).build()).build().inputAudio()).build()));
                continue;
            }
            if (content instanceof PdfFileContent) {
                PdfFileContent pdfFileContent = (PdfFileContent)content;
                if (pdfFileContent.pdfFile().url() != null) {
                    throw new UnsupportedFeatureException("OpenAI Official Chat Completions API does not support URL-based PDF inputs. Provide PDF content as base64 data instead.");
                }
                String fileData = String.format("data:%s;base64,%s", pdfFileContent.pdfFile().mimeType(), pdfFileContent.pdfFile().base64Data());
                parts.add(ChatCompletionContentPart.ofFile((ChatCompletionContentPart.File)ChatCompletionContentPart.File.builder().file(ChatCompletionContentPart.File.FileObject.builder().fileData(fileData).filename("document.pdf").build()).build()));
                continue;
            }
            throw Exceptions.illegalArgument((String)("Unknown content type: " + content), (Object[])new Object[0]);
        }
        return parts;
    }

    private static ChatCompletionContentPartImage.ImageUrl.Detail toImageDetail(ImageContent.DetailLevel detailLevel) {
        switch (detailLevel) {
            case LOW: {
                return ChatCompletionContentPartImage.ImageUrl.Detail.LOW;
            }
            case HIGH: {
                return ChatCompletionContentPartImage.ImageUrl.Detail.HIGH;
            }
            case AUTO: {
                return ChatCompletionContentPartImage.ImageUrl.Detail.AUTO;
            }
            case MEDIUM: 
            case ULTRA_HIGH: {
                throw new UnsupportedFeatureException("DetailLevel " + detailLevel + " is not supported by OpenAI Chat Completions API. Supported values: LOW, HIGH, AUTO");
            }
        }
        throw new UnsupportedFeatureException("DetailLevel " + detailLevel + " is not supported by OpenAI Chat Completions API. Supported values: LOW, HIGH, AUTO");
    }

    static List<ChatCompletionTool> toTools(Collection<ToolSpecification> toolSpecifications, boolean strict) {
        return toolSpecifications.stream().map(toolSpecification -> InternalOpenAiOfficialHelper.toTool(toolSpecification, strict)).collect(Collectors.toList());
    }

    private static ChatCompletionTool toTool(ToolSpecification toolSpecification, boolean strict) {
        boolean effectiveStrict = ToolSpecificationUtils.isEffectivelyStrict((ToolSpecification)toolSpecification, (boolean)strict);
        FunctionDefinition.Builder functionDefinitionBuilder = FunctionDefinition.builder().name(toolSpecification.name()).description(toolSpecification.description() != null ? toolSpecification.description() : "").parameters(InternalOpenAiOfficialHelper.toOpenAiParameters(toolSpecification, effectiveStrict));
        if (effectiveStrict) {
            functionDefinitionBuilder.strict(true);
        }
        return ChatCompletionTool.ofFunction((ChatCompletionFunctionTool)ChatCompletionFunctionTool.builder().function(functionDefinitionBuilder.build()).build());
    }

    private static FunctionParameters toOpenAiParameters(ToolSpecification toolSpecification, boolean strict) {
        FunctionParameters.Builder parametersBuilder = FunctionParameters.builder();
        JsonObjectSchema parameters = toolSpecification.parameters();
        parametersBuilder.putAdditionalProperty("type", JsonValue.from((Object)"object"));
        if (parameters != null) {
            parametersBuilder.putAdditionalProperty("properties", JsonValue.from((Object)JsonSchemaElementUtils.toMap((Map)parameters.properties(), (boolean)strict)));
            if (strict) {
                parametersBuilder.putAdditionalProperty("required", JsonValue.from(new ArrayList(parameters.properties().keySet())));
                parametersBuilder.putAdditionalProperty("additionalProperties", JsonValue.from((Object)false));
            } else {
                parametersBuilder.putAdditionalProperty("required", JsonValue.from((Object)parameters.required()));
            }
            if (!parameters.definitions().isEmpty()) {
                parametersBuilder.putAdditionalProperty("$defs", JsonValue.from((Object)JsonSchemaElementUtils.toMap((Map)parameters.definitions(), (boolean)strict)));
            }
            return parametersBuilder.build();
        }
        parametersBuilder.putAdditionalProperty("properties", JsonValue.from((Object)JsonSchemaElementUtils.toMap(new HashMap(), (boolean)strict)));
        if (strict) {
            parametersBuilder.putAdditionalProperty("additionalProperties", JsonValue.from((Object)false));
        }
        return parametersBuilder.build();
    }

    static AiMessage aiMessageFrom(ChatCompletion chatCompletion) {
        ChatCompletionMessage assistantMessage = ((ChatCompletion.Choice)chatCompletion.choices().get(0)).message();
        Optional text = assistantMessage.content();
        Optional toolCalls = assistantMessage.toolCalls();
        if (toolCalls.isPresent()) {
            List toolExecutionRequests = ((List)toolCalls.get()).stream().map(InternalOpenAiOfficialHelper::toToolExecutionRequest).filter(Objects::nonNull).collect(Collectors.toList());
            if (!text.isPresent()) {
                return AiMessage.from(toolExecutionRequests);
            }
            if (toolExecutionRequests.isEmpty()) {
                return AiMessage.from((String)((String)text.get()));
            }
            return AiMessage.from((String)((String)text.get()), toolExecutionRequests);
        }
        return AiMessage.from((String)text.orElse(""));
    }

    private static ToolExecutionRequest toToolExecutionRequest(ChatCompletionMessageToolCall toolCall) {
        if (!toolCall.isFunction() || !toolCall.function().isPresent()) {
            return null;
        }
        ChatCompletionMessageFunctionToolCall functionToolCall = (ChatCompletionMessageFunctionToolCall)toolCall.function().get();
        return ToolExecutionRequest.builder().id(functionToolCall.id()).name(functionToolCall.function().name()).arguments(functionToolCall.function().arguments()).build();
    }

    static OpenAiOfficialTokenUsage tokenUsageFrom(CreateEmbeddingResponse.Usage openAiUsage) {
        return OpenAiOfficialTokenUsage.builder().inputTokenCount(openAiUsage.promptTokens()).totalTokenCount(openAiUsage.totalTokens()).build();
    }

    static OpenAiOfficialTokenUsage tokenUsageFrom(CompletionUsage openAiUsage) {
        Optional promptTokensDetails = openAiUsage.promptTokensDetails();
        OpenAiOfficialTokenUsage.InputTokensDetails inputTokensDetails = null;
        if (promptTokensDetails.isPresent() && ((CompletionUsage.PromptTokensDetails)promptTokensDetails.get()).cachedTokens().isPresent()) {
            inputTokensDetails = OpenAiOfficialTokenUsage.InputTokensDetails.builder().cachedTokens((Long)((CompletionUsage.PromptTokensDetails)promptTokensDetails.get()).cachedTokens().get()).build();
        }
        Optional completionTokensDetails = openAiUsage.completionTokensDetails();
        OpenAiOfficialTokenUsage.OutputTokensDetails outputTokensDetails = null;
        if (completionTokensDetails.isPresent() && ((CompletionUsage.CompletionTokensDetails)completionTokensDetails.get()).reasoningTokens().isPresent()) {
            outputTokensDetails = OpenAiOfficialTokenUsage.OutputTokensDetails.builder().reasoningTokens((Long)((CompletionUsage.CompletionTokensDetails)completionTokensDetails.get()).reasoningTokens().get()).build();
        }
        return OpenAiOfficialTokenUsage.builder().inputTokenCount(openAiUsage.promptTokens()).inputTokensDetails(inputTokensDetails).outputTokenCount(openAiUsage.completionTokens()).outputTokensDetails(outputTokensDetails).totalTokenCount(openAiUsage.totalTokens()).build();
    }

    static FinishReason finishReasonFrom(ChatCompletion.Choice.FinishReason openAiFinishReason) {
        if (openAiFinishReason == null) {
            return null;
        }
        if (openAiFinishReason.equals((Object)ChatCompletion.Choice.FinishReason.STOP)) {
            return FinishReason.STOP;
        }
        if (openAiFinishReason.equals((Object)ChatCompletion.Choice.FinishReason.LENGTH)) {
            return FinishReason.LENGTH;
        }
        if (openAiFinishReason.equals((Object)ChatCompletion.Choice.FinishReason.TOOL_CALLS)) {
            return FinishReason.TOOL_EXECUTION;
        }
        if (openAiFinishReason.equals((Object)ChatCompletion.Choice.FinishReason.FUNCTION_CALL)) {
            return FinishReason.TOOL_EXECUTION;
        }
        if (openAiFinishReason.equals((Object)ChatCompletion.Choice.FinishReason.CONTENT_FILTER)) {
            return FinishReason.CONTENT_FILTER;
        }
        return null;
    }

    static FinishReason finishReasonFrom(ChatCompletionChunk.Choice.FinishReason openAiFinishReason) {
        if (openAiFinishReason == null) {
            return null;
        }
        if (openAiFinishReason.equals((Object)ChatCompletionChunk.Choice.FinishReason.STOP)) {
            return FinishReason.STOP;
        }
        if (openAiFinishReason.equals((Object)ChatCompletionChunk.Choice.FinishReason.LENGTH)) {
            return FinishReason.LENGTH;
        }
        if (openAiFinishReason.equals((Object)ChatCompletionChunk.Choice.FinishReason.TOOL_CALLS)) {
            return FinishReason.TOOL_EXECUTION;
        }
        if (openAiFinishReason.equals((Object)ChatCompletionChunk.Choice.FinishReason.FUNCTION_CALL)) {
            return FinishReason.TOOL_EXECUTION;
        }
        if (openAiFinishReason.equals((Object)ChatCompletionChunk.Choice.FinishReason.CONTENT_FILTER)) {
            return FinishReason.CONTENT_FILTER;
        }
        return null;
    }

    static ResponseFormatJsonObject toOpenAiResponseFormat(ResponseFormat responseFormat, Boolean strict) {
        if (responseFormat == null || responseFormat.type() == ResponseFormatType.TEXT) {
            return null;
        }
        JsonSchema jsonSchema = responseFormat.jsonSchema();
        if (jsonSchema == null) {
            return ResponseFormatJsonObject.builder().type(JsonValue.from((Object)"json_object")).build();
        }
        if (!(jsonSchema.rootElement() instanceof JsonObjectSchema) && !(jsonSchema.rootElement() instanceof JsonRawSchema)) {
            throw new IllegalArgumentException("For OpenAI, the root element of the JSON Schema must be either a JsonObjectSchema or a JsonRawSchema, but it was: " + jsonSchema.rootElement().getClass());
        }
        HashMap<String, JsonValue> properties = new HashMap<String, JsonValue>();
        properties.put("name", JsonValue.from((Object)jsonSchema.name()));
        properties.put("strict", strict != false ? JsonValue.from((Object)true) : JsonValue.from((Object)false));
        properties.put("schema", JsonValue.from((Object)JsonSchemaElementUtils.toMap((JsonSchemaElement)jsonSchema.rootElement(), (boolean)strict)));
        return ResponseFormatJsonObject.builder().type(JsonValue.from((Object)"json_schema")).putAllAdditionalProperties(Collections.singletonMap("json_schema", JsonValue.from(properties))).build();
    }

    static ChatCompletionToolChoiceOption toOpenAiToolChoice(ToolChoice toolChoice) {
        if (toolChoice == null) {
            return null;
        }
        switch (toolChoice) {
            case AUTO: {
                return ChatCompletionToolChoiceOption.ofAuto((ChatCompletionToolChoiceOption.Auto)ChatCompletionToolChoiceOption.Auto.AUTO);
            }
            case REQUIRED: {
                return ChatCompletionToolChoiceOption.ofAuto((ChatCompletionToolChoiceOption.Auto)ChatCompletionToolChoiceOption.Auto.REQUIRED);
            }
            case NONE: {
                return ChatCompletionToolChoiceOption.ofAuto((ChatCompletionToolChoiceOption.Auto)ChatCompletionToolChoiceOption.Auto.NONE);
            }
        }
        return null;
    }

    static void validate(ChatRequestParameters parameters) {
        if (parameters.topK() != null) {
            throw new UnsupportedFeatureException("'topK' parameter is not supported by OpenAI");
        }
    }

    static ResponseFormat fromOpenAiResponseFormat(String responseFormat) {
        if ("json_object".equals(responseFormat)) {
            return ResponseFormat.JSON;
        }
        return null;
    }

    static ChatCompletionCreateParams.Builder toOpenAiChatCompletionCreateParams(ChatRequest chatRequest, OpenAiOfficialChatRequestParameters parameters, Boolean strictTools, Boolean strictJsonSchema) {
        ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder().model(parameters.modelName());
        if (parameters.maxOutputTokens() != null && parameters.maxCompletionTokens() == null) {
            builder.maxTokens((long)parameters.maxOutputTokens().intValue());
        }
        if (parameters.maxCompletionTokens() != null) {
            builder.maxCompletionTokens((long)parameters.maxCompletionTokens().intValue());
        }
        if (!parameters.logitBias().isEmpty()) {
            builder.logitBias(ChatCompletionCreateParams.LogitBias.builder().putAllAdditionalProperties(parameters.logitBias().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> JsonValue.from(entry.getValue())))).build());
        }
        if (parameters.parallelToolCalls() != null) {
            builder.parallelToolCalls(parameters.parallelToolCalls().booleanValue());
        }
        if (parameters.seed() != null) {
            builder.seed((long)parameters.seed().intValue());
        }
        if (parameters.user() != null) {
            builder.user(parameters.user());
        }
        if (parameters.store() != null) {
            builder.store(parameters.store());
        }
        if (!parameters.metadata().isEmpty()) {
            builder.metadata(ChatCompletionCreateParams.Metadata.builder().putAllAdditionalProperties(parameters.metadata().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> JsonValue.from(entry.getValue())))).build());
        }
        if (parameters.serviceTier() != null) {
            builder.serviceTier(ChatCompletionCreateParams.ServiceTier.of((String)parameters.serviceTier()));
        }
        if (parameters.reasoningEffort() != null) {
            builder.reasoningEffort(ReasoningEffort.of((String)parameters.reasoningEffort()));
        }
        builder.messages(InternalOpenAiOfficialHelper.toOpenAiMessages(chatRequest.messages()));
        if (parameters.temperature() != null) {
            builder.temperature(parameters.temperature());
        }
        if (parameters.topP() != null) {
            builder.topP(parameters.topP());
        }
        if (parameters.frequencyPenalty() != null) {
            builder.frequencyPenalty(parameters.frequencyPenalty());
        }
        if (parameters.presencePenalty() != null) {
            builder.presencePenalty(parameters.presencePenalty());
        }
        if (!parameters.stopSequences().isEmpty()) {
            builder.stop(ChatCompletionCreateParams.Stop.ofStrings((List)parameters.stopSequences()));
        }
        if (!parameters.toolSpecifications().isEmpty()) {
            builder.tools(InternalOpenAiOfficialHelper.toTools(parameters.toolSpecifications(), strictTools));
        }
        if (parameters.toolChoice() != null) {
            builder.toolChoice(InternalOpenAiOfficialHelper.toOpenAiToolChoice(parameters.toolChoice()));
        }
        if (parameters.responseFormat() != null) {
            builder.responseFormat(InternalOpenAiOfficialHelper.toOpenAiResponseFormat(parameters.responseFormat(), strictJsonSchema));
        }
        return builder;
    }
}

