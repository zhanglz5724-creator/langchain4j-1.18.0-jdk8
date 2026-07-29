/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.openai.azure.AzureOpenAIServiceVersion
 *  com.openai.client.OpenAIClient
 *  com.openai.core.JsonField
 *  com.openai.core.JsonMissing
 *  com.openai.core.JsonValue
 *  com.openai.core.http.StreamResponse
 *  com.openai.credential.Credential
 *  com.openai.models.ChatModel
 *  com.openai.models.Reasoning
 *  com.openai.models.Reasoning$Builder
 *  com.openai.models.Reasoning$Summary
 *  com.openai.models.ReasoningEffort
 *  com.openai.models.ResponseFormatJsonObject
 *  com.openai.models.ResponsesModel
 *  com.openai.models.responses.EasyInputMessage
 *  com.openai.models.responses.EasyInputMessage$Content
 *  com.openai.models.responses.EasyInputMessage$Role
 *  com.openai.models.responses.FunctionTool
 *  com.openai.models.responses.FunctionTool$Parameters
 *  com.openai.models.responses.FunctionTool$Parameters$Builder
 *  com.openai.models.responses.Response
 *  com.openai.models.responses.ResponseCompletedEvent
 *  com.openai.models.responses.ResponseCreateParams
 *  com.openai.models.responses.ResponseCreateParams$Builder
 *  com.openai.models.responses.ResponseCreateParams$ServiceTier
 *  com.openai.models.responses.ResponseCreateParams$StreamOptions
 *  com.openai.models.responses.ResponseCreateParams$Truncation
 *  com.openai.models.responses.ResponseCreatedEvent
 *  com.openai.models.responses.ResponseError
 *  com.openai.models.responses.ResponseErrorEvent
 *  com.openai.models.responses.ResponseFailedEvent
 *  com.openai.models.responses.ResponseFormatTextConfig
 *  com.openai.models.responses.ResponseFormatTextJsonSchemaConfig
 *  com.openai.models.responses.ResponseFormatTextJsonSchemaConfig$Schema
 *  com.openai.models.responses.ResponseFormatTextJsonSchemaConfig$Schema$Builder
 *  com.openai.models.responses.ResponseFunctionCallArgumentsDeltaEvent
 *  com.openai.models.responses.ResponseFunctionCallArgumentsDoneEvent
 *  com.openai.models.responses.ResponseFunctionCallOutputItem
 *  com.openai.models.responses.ResponseFunctionToolCall
 *  com.openai.models.responses.ResponseIncludable
 *  com.openai.models.responses.ResponseIncompleteEvent
 *  com.openai.models.responses.ResponseInputContent
 *  com.openai.models.responses.ResponseInputFile
 *  com.openai.models.responses.ResponseInputFile$Builder
 *  com.openai.models.responses.ResponseInputImage
 *  com.openai.models.responses.ResponseInputImage$Detail
 *  com.openai.models.responses.ResponseInputImageContent
 *  com.openai.models.responses.ResponseInputImageContent$Detail
 *  com.openai.models.responses.ResponseInputItem
 *  com.openai.models.responses.ResponseInputItem$FunctionCallOutput
 *  com.openai.models.responses.ResponseInputItem$FunctionCallOutput$Builder
 *  com.openai.models.responses.ResponseInputItem$FunctionCallOutput$Output
 *  com.openai.models.responses.ResponseInputText
 *  com.openai.models.responses.ResponseInputTextContent
 *  com.openai.models.responses.ResponseOutputItem
 *  com.openai.models.responses.ResponseOutputItemAddedEvent
 *  com.openai.models.responses.ResponseOutputItemDoneEvent
 *  com.openai.models.responses.ResponseReasoningItem
 *  com.openai.models.responses.ResponseReasoningItem$Summary
 *  com.openai.models.responses.ResponseReasoningSummaryTextDeltaEvent
 *  com.openai.models.responses.ResponseReasoningTextDeltaEvent
 *  com.openai.models.responses.ResponseStreamEvent
 *  com.openai.models.responses.ResponseTextConfig
 *  com.openai.models.responses.ResponseTextConfig$Builder
 *  com.openai.models.responses.ResponseTextConfig$Verbosity
 *  com.openai.models.responses.ResponseTextDeltaEvent
 *  com.openai.models.responses.Tool
 *  com.openai.models.responses.ToolChoiceOptions
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolExecutionRequest$Builder
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.AiMessage$Builder
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
 *  dev.langchain4j.internal.DefaultExecutorProvider
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler
 *  dev.langchain4j.internal.ToolSpecificationUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonRawSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.PartialToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  dev.langchain4j.model.output.FinishReason
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.openaiofficial;

import com.openai.azure.AzureOpenAIServiceVersion;
import com.openai.client.OpenAIClient;
import com.openai.core.JsonField;
import com.openai.core.JsonMissing;
import com.openai.core.JsonValue;
import com.openai.core.http.StreamResponse;
import com.openai.credential.Credential;
import com.openai.models.ChatModel;
import com.openai.models.Reasoning;
import com.openai.models.ReasoningEffort;
import com.openai.models.ResponseFormatJsonObject;
import com.openai.models.ResponsesModel;
import com.openai.models.responses.EasyInputMessage;
import com.openai.models.responses.FunctionTool;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCompletedEvent;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseCreatedEvent;
import com.openai.models.responses.ResponseError;
import com.openai.models.responses.ResponseErrorEvent;
import com.openai.models.responses.ResponseFailedEvent;
import com.openai.models.responses.ResponseFormatTextConfig;
import com.openai.models.responses.ResponseFormatTextJsonSchemaConfig;
import com.openai.models.responses.ResponseFunctionCallArgumentsDeltaEvent;
import com.openai.models.responses.ResponseFunctionCallArgumentsDoneEvent;
import com.openai.models.responses.ResponseFunctionCallOutputItem;
import com.openai.models.responses.ResponseFunctionToolCall;
import com.openai.models.responses.ResponseIncludable;
import com.openai.models.responses.ResponseIncompleteEvent;
import com.openai.models.responses.ResponseInputContent;
import com.openai.models.responses.ResponseInputFile;
import com.openai.models.responses.ResponseInputImage;
import com.openai.models.responses.ResponseInputImageContent;
import com.openai.models.responses.ResponseInputItem;
import com.openai.models.responses.ResponseInputText;
import com.openai.models.responses.ResponseInputTextContent;
import com.openai.models.responses.ResponseOutputItem;
import com.openai.models.responses.ResponseOutputItemAddedEvent;
import com.openai.models.responses.ResponseOutputItemDoneEvent;
import com.openai.models.responses.ResponseReasoningItem;
import com.openai.models.responses.ResponseReasoningSummaryTextDeltaEvent;
import com.openai.models.responses.ResponseReasoningTextDeltaEvent;
import com.openai.models.responses.ResponseStreamEvent;
import com.openai.models.responses.ResponseTextConfig;
import com.openai.models.responses.ResponseTextDeltaEvent;
import com.openai.models.responses.Tool;
import com.openai.models.responses.ToolChoiceOptions;
import dev.langchain4j.Experimental;
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
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.DefaultExecutorProvider;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler;
import dev.langchain4j.internal.ToolSpecificationUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonRawSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.StreamingHandle;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialResponsesChatRequestParameters;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialResponsesChatResponseMetadata;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialTokenUsage;
import dev.langchain4j.model.openaiofficial.setup.OpenAiOfficialSetup;
import dev.langchain4j.model.output.FinishReason;
import java.net.Proxy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Experimental
public class OpenAiOfficialResponsesStreamingChatModel
implements StreamingChatModel {
    private static final Logger logger = LoggerFactory.getLogger(OpenAiOfficialResponsesStreamingChatModel.class);
    private static final String PROMPT_CACHE_RETENTION_FIELD = "prompt_cache_retention";
    static final String ENCRYPTED_REASONING_KEY = "encrypted_reasoning";
    private final OpenAIClient client;
    private final ExecutorService executorService;
    private final OpenAiOfficialResponsesChatRequestParameters defaultRequestParameters;
    private final List<ChatModelListener> listeners;

    private OpenAiOfficialResponsesStreamingChatModel(Builder builder) {
        ChatRequestParameters commonParameters;
        this.client = builder.client != null ? builder.client : OpenAiOfficialSetup.setupSyncClient(builder.baseUrl, builder.apiKey, builder.credential, builder.microsoftFoundryDeploymentName, builder.azureOpenAIServiceVersion, builder.organizationId, builder.isMicrosoftFoundry, builder.isGitHubModels, builder.modelName, builder.timeout, builder.maxRetries, builder.proxy, builder.customHeaders);
        this.executorService = (ExecutorService)Utils.getOrDefault((Object)builder.executorService, DefaultExecutorProvider::getDefaultExecutorService);
        if (builder.defaultRequestParameters != null) {
            OpenAiOfficialResponsesStreamingChatModel.validate(builder.defaultRequestParameters);
            commonParameters = builder.defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        OpenAiOfficialResponsesChatRequestParameters responsesParameters = commonParameters instanceof OpenAiOfficialResponsesChatRequestParameters ? (OpenAiOfficialResponsesChatRequestParameters)commonParameters : OpenAiOfficialResponsesChatRequestParameters.EMPTY;
        this.defaultRequestParameters = ((OpenAiOfficialResponsesChatRequestParameters.Builder)((OpenAiOfficialResponsesChatRequestParameters.Builder)((OpenAiOfficialResponsesChatRequestParameters.Builder)((OpenAiOfficialResponsesChatRequestParameters.Builder)((OpenAiOfficialResponsesChatRequestParameters.Builder)((OpenAiOfficialResponsesChatRequestParameters.Builder)((OpenAiOfficialResponsesChatRequestParameters.Builder)OpenAiOfficialResponsesChatRequestParameters.builder().modelName((String)ValidationUtils.ensureNotNull((Object)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName()), (String)"modelName"))).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature()))).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP()))).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxOutputTokens, (Object)commonParameters.maxOutputTokens()))).toolSpecifications(Utils.getOrDefault((List)builder.toolSpecifications, (List)commonParameters.toolSpecifications()))).toolChoice((ToolChoice)Utils.getOrDefault((Object)builder.toolChoice, (Object)commonParameters.toolChoice()))).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat()))).previousResponseId((String)Utils.getOrDefault((Object)builder.previousResponseId, (Object)responsesParameters.previousResponseId())).maxToolCalls((Integer)Utils.getOrDefault((Object)builder.maxToolCalls, (Object)responsesParameters.maxToolCalls())).parallelToolCalls((Boolean)Utils.getOrDefault((Object)builder.parallelToolCalls, (Object)responsesParameters.parallelToolCalls())).topLogprobs((Integer)Utils.getOrDefault((Object)builder.topLogprobs, (Object)responsesParameters.topLogprobs())).truncation((String)Utils.getOrDefault((Object)builder.truncation, (Object)responsesParameters.truncation())).include(Utils.getOrDefault((List)builder.include, responsesParameters.include())).serviceTier((String)Utils.getOrDefault((Object)builder.serviceTier, (Object)responsesParameters.serviceTier())).safetyIdentifier((String)Utils.getOrDefault((Object)builder.safetyIdentifier, (Object)responsesParameters.safetyIdentifier())).promptCacheKey((String)Utils.getOrDefault((Object)builder.promptCacheKey, (Object)responsesParameters.promptCacheKey())).promptCacheRetention((String)Utils.getOrDefault((Object)builder.promptCacheRetention, (Object)responsesParameters.promptCacheRetention())).reasoningEffort((ReasoningEffort)Utils.getOrDefault((Object)builder.reasoningEffort, (Object)responsesParameters.reasoningEffort())).reasoningSummary((Reasoning.Summary)Utils.getOrDefault((Object)builder.reasoningSummary, (Object)responsesParameters.reasoningSummary())).textVerbosity((String)Utils.getOrDefault((Object)builder.textVerbosity, (Object)responsesParameters.textVerbosity())).streamIncludeObfuscation((Boolean)Utils.getOrDefault((Object)builder.streamIncludeObfuscation, (Object)responsesParameters.streamIncludeObfuscation())).store((Boolean)Utils.getOrDefault((Object)builder.store, (Object)Utils.getOrDefault((Object)responsesParameters.store(), (Object)false))).strictTools((Boolean)Utils.getOrDefault((Object)builder.strictTools, (Object)responsesParameters.strictTools())).strictJsonSchema((Boolean)Utils.getOrDefault((Object)builder.strictJsonSchema, (Object)responsesParameters.strictJsonSchema())).serverTools(Utils.getOrDefault((List)builder.serverTools, responsesParameters.serverTools())).build();
        this.listeners = Utils.copy((List)builder.listeners);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void doChat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
        block2: {
            OpenAiOfficialResponsesStreamingChatModel.validate(chatRequest.parameters());
            OpenAiOfficialResponsesChatRequestParameters parameters = (OpenAiOfficialResponsesChatRequestParameters)chatRequest.parameters();
            AtomicReference<String> responseIdRef = new AtomicReference<String>();
            Future<?> streamingFuture = null;
            try {
                ResponseCreateParams params = OpenAiOfficialResponsesStreamingChatModel.buildRequestParams(chatRequest, parameters);
                StreamResponse streamResponse = this.client.responses().createStreaming(params);
                ResponsesStreamingHandle streamingHandle = new ResponsesStreamingHandle(() -> {
                    try {
                        streamResponse.close();
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                });
                ResponsesEventHandler eventHandler = new ResponsesEventHandler(handler, responseIdRef, parameters.modelName(), streamingHandle);
                streamingFuture = this.executorService.submit(() -> {
                    try (StreamResponse sr = streamResponse;){
                        sr.stream().forEach(e -> eventHandler.handleEvent((ResponseStreamEvent) e));
                    }
                    catch (CancellationException e) {
                        InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> handler.onError((Throwable)e));
                    }
                    catch (Exception e) {
                        RuntimeException mappedException = ExceptionMapper.DEFAULT.mapException((Throwable)e);
                        InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> handler.onError((Throwable)mappedException));
                    }
                    finally {
                        streamingHandle.markCompleted();
                    }
                });
                streamingHandle.setStreamingFuture(streamingFuture);
            }
            catch (Exception e) {
                RuntimeException mappedException = ExceptionMapper.DEFAULT.mapException((Throwable)e);
                InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> handler.onError((Throwable)mappedException));
                if (streamingFuture == null) break block2;
                streamingFuture.cancel(true);
            }
        }
    }

    public ChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.OPEN_AI;
    }

    public Set<Capability> supportedCapabilities() {
        return Collections.singleton(Capability.RESPONSE_FORMAT_JSON_SCHEMA);
    }

    static String extractReasoningSummary(Response response) {
        StringBuilder summaryBuilder = new StringBuilder();
        for (ResponseOutputItem item : response.output()) {
            if (!item.isReasoning()) continue;
            for (ResponseReasoningItem.Summary summary : item.asReasoning().summary()) {
                summaryBuilder.append(summary.text());
            }
        }
        return summaryBuilder.length() == 0 ? null : summaryBuilder.toString();
    }

    static String extractEncryptedReasoning(Response response) {
        for (ResponseOutputItem item : response.output()) {
            Optional encrypted;
            if (!item.isReasoning() || !(encrypted = item.asReasoning().encryptedContent()).isPresent() || ((String)encrypted.get()).isEmpty()) continue;
            return (String)encrypted.get();
        }
        return null;
    }

    static List<ToolExecutionRequest> extractToolExecutionRequests(Response response) {
        ArrayList<ToolExecutionRequest> requests = new ArrayList<ToolExecutionRequest>();
        for (ResponseOutputItem item : response.output()) {
            if (!item.isFunctionCall()) continue;
            ResponseFunctionToolCall fn = item.asFunctionCall();
            requests.add(ToolExecutionRequest.builder().id(fn.callId()).name(fn.name()).arguments(fn.arguments()).build());
        }
        return requests;
    }

    static String extractText(Response response) {
        StringBuilder textBuilder = new StringBuilder();
        for (ResponseOutputItem item : response.output()) {
            if (!item.isMessage()) continue;
            item.asMessage().content().forEach(content -> {
                if (content.isOutputText()) {
                    textBuilder.append(content.asOutputText().text());
                }
            });
        }
        return textBuilder.length() == 0 ? null : textBuilder.toString();
    }

    static AiMessage buildAiMessage(String text, String thinking, List<ToolExecutionRequest> toolExecutionRequests, String encryptedReasoning) {
        AiMessage.Builder builder = AiMessage.builder().text(text).thinking(thinking).toolExecutionRequests(toolExecutionRequests);
        if (encryptedReasoning != null) {
            builder.attributes(Collections.singletonMap(ENCRYPTED_REASONING_KEY, encryptedReasoning));
        }
        return builder.build();
    }

    static OpenAiOfficialResponsesChatResponseMetadata buildResponseMetadata(String responseId, String modelName, Response response, String finishReason, OpenAiOfficialTokenUsage tokenUsage) {
        OpenAiOfficialResponsesChatResponseMetadata.Builder builder = ((OpenAiOfficialResponsesChatResponseMetadata.Builder)((OpenAiOfficialResponsesChatResponseMetadata.Builder)OpenAiOfficialResponsesChatResponseMetadata.builder().id(responseId)).modelName(modelName)).createdAt((long)response.createdAt()).rawResponse(response);
        response.completedAt().ifPresent(ts -> builder.completedAt(ts.longValue()));
        response.serviceTier().ifPresent(tier -> builder.serviceTier(tier.asString()));
        if (finishReason != null) {
            builder.finishReason(FinishReason.valueOf((String)finishReason));
        }
        if (tokenUsage != null) {
            builder.tokenUsage(tokenUsage);
        }
        return builder.build();
    }

    static String mapStatusToFinishReason(String status, boolean hasToolCalls) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case "completed": {
                return hasToolCalls ? "TOOL_EXECUTION" : "STOP";
            }
            case "incomplete": {
                return "LENGTH";
            }
            case "failed": {
                return "OTHER";
            }
        }
        return "OTHER";
    }

    static OpenAiOfficialTokenUsage extractTokenUsage(Response response) {
        return response.usage().map(usage -> OpenAiOfficialTokenUsage.builder().inputTokenCount(usage.inputTokens()).outputTokenCount(usage.outputTokens()).totalTokenCount(usage.totalTokens()).inputTokensDetails(OpenAiOfficialTokenUsage.InputTokensDetails.builder().cachedTokens(usage.inputTokensDetails().cachedTokens()).build()).outputTokensDetails(OpenAiOfficialTokenUsage.OutputTokensDetails.builder().reasoningTokens(usage.outputTokensDetails().reasoningTokens()).build()).build()).orElse(null);
    }

    static ResponseCreateParams buildRequestParams(ChatRequest chatRequest, OpenAiOfficialResponsesChatRequestParameters parameters) {
        ResponseCreateParams.Builder paramsBuilder = ResponseCreateParams.builder().model(ResponsesModel.ofChat((ChatModel)ChatModel.of((String)parameters.modelName()))).store(parameters.store());
        ArrayList<ResponseInputItem> inputItems = new ArrayList<ResponseInputItem>();
        for (Object msg : chatRequest.messages()) {
            inputItems.addAll(OpenAiOfficialResponsesStreamingChatModel.toResponseInputItems((ChatMessage)msg));
        }
        paramsBuilder.inputOfResponse(inputItems);
        if (parameters.temperature() != null) {
            paramsBuilder.temperature(parameters.temperature());
        }
        if (parameters.topP() != null) {
            paramsBuilder.topP(parameters.topP());
        }
        if (parameters.maxOutputTokens() != null) {
            paramsBuilder.maxOutputTokens(parameters.maxOutputTokens().longValue());
        }
        if (parameters.maxToolCalls() != null) {
            paramsBuilder.maxToolCalls((long)parameters.maxToolCalls().intValue());
        }
        if (parameters.parallelToolCalls() != null) {
            paramsBuilder.parallelToolCalls(parameters.parallelToolCalls());
        }
        if (parameters.previousResponseId() != null) {
            paramsBuilder.previousResponseId(parameters.previousResponseId());
        }
        if (parameters.topLogprobs() != null) {
            paramsBuilder.topLogprobs((long)parameters.topLogprobs().intValue());
        }
        if (parameters.truncation() != null) {
            paramsBuilder.truncation(ResponseCreateParams.Truncation.of((String)parameters.truncation()));
        }
        if (parameters.include() != null && !parameters.include().isEmpty()) {
            ArrayList<ResponseIncludable> includables = new ArrayList<ResponseIncludable>();
            for (String string : parameters.include()) {
                includables.add(ResponseIncludable.of((String)string));
            }
            paramsBuilder.include(includables);
        }
        if (parameters.serviceTier() != null) {
            paramsBuilder.serviceTier(ResponseCreateParams.ServiceTier.of((String)parameters.serviceTier()));
        }
        if (parameters.safetyIdentifier() != null) {
            paramsBuilder.safetyIdentifier(parameters.safetyIdentifier());
        }
        if (parameters.promptCacheKey() != null) {
            paramsBuilder.promptCacheKey(parameters.promptCacheKey());
        }
        if (parameters.promptCacheRetention() != null) {
            paramsBuilder.putAdditionalBodyProperty(PROMPT_CACHE_RETENTION_FIELD, JsonValue.from((Object)parameters.promptCacheRetention()));
        }
        if (parameters.reasoningEffort() != null || parameters.reasoningSummary() != null) {
            Reasoning.Builder reasoningBuilder = Reasoning.builder();
            if (parameters.reasoningEffort() != null) {
                reasoningBuilder.effort(parameters.reasoningEffort());
            }
            if (parameters.reasoningSummary() != null) {
                reasoningBuilder.summary(parameters.reasoningSummary());
            }
            paramsBuilder.reasoning(reasoningBuilder.build());
        }
        if (parameters.streamIncludeObfuscation() != null) {
            paramsBuilder.streamOptions(ResponseCreateParams.StreamOptions.builder().includeObfuscation(parameters.streamIncludeObfuscation().booleanValue()).build());
        }
        boolean strictTools = Boolean.TRUE.equals(parameters.strictTools());
        List<Tool> tools = OpenAiOfficialResponsesStreamingChatModel.toResponsesTools(parameters.toolSpecifications(), strictTools, parameters.serverTools());
        if (!tools.isEmpty()) {
            for (Tool tool : tools) {
                paramsBuilder.addTool(tool);
            }
            if (parameters.toolChoice() != null) {
                paramsBuilder.toolChoice(OpenAiOfficialResponsesStreamingChatModel.toResponsesToolChoice(parameters.toolChoice()));
            }
        }
        boolean bl = Boolean.TRUE.equals(parameters.strictJsonSchema());
        ResponseTextConfig textConfig = OpenAiOfficialResponsesStreamingChatModel.toResponseTextConfig(parameters.responseFormat(), bl, parameters.textVerbosity());
        if (textConfig != null) {
            paramsBuilder.text(textConfig);
        }
        return paramsBuilder.build();
    }

    static void validate(ChatRequestParameters parameters) {
        if (parameters.topK() != null) {
            throw new UnsupportedFeatureException("'topK' parameter is not supported by OpenAI Responses API");
        }
        if (parameters.frequencyPenalty() != null) {
            throw new UnsupportedFeatureException("'frequencyPenalty' parameter is not supported by OpenAI Responses API");
        }
        if (parameters.presencePenalty() != null) {
            throw new UnsupportedFeatureException("'presencePenalty' parameter is not supported by OpenAI Responses API");
        }
        if (parameters.stopSequences() != null && !parameters.stopSequences().isEmpty()) {
            throw new UnsupportedFeatureException("'stopSequences' parameter is not supported by OpenAI Responses API");
        }
    }

    private static List<ResponseInputItem> toResponseInputItems(ChatMessage msg) {
        if (msg instanceof SystemMessage) {
            SystemMessage systemMessage = (SystemMessage)msg;
            return Collections.singletonList(OpenAiOfficialResponsesStreamingChatModel.createTextMessage(EasyInputMessage.Role.SYSTEM, systemMessage.text()));
        }
        if (msg instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)msg;
            return Collections.singletonList(OpenAiOfficialResponsesStreamingChatModel.createUserMessage(userMessage));
        }
        if (msg instanceof AiMessage) {
            String text;
            AiMessage aiMessage = (AiMessage)msg;
            ArrayList<ResponseInputItem> items = new ArrayList<ResponseInputItem>();
            String encryptedReasoning = (String)aiMessage.attribute(ENCRYPTED_REASONING_KEY, String.class);
            if (encryptedReasoning != null && !encryptedReasoning.isEmpty()) {
                items.add(OpenAiOfficialResponsesStreamingChatModel.toReasoningInputItem(encryptedReasoning, aiMessage.thinking()));
            }
            if ((text = aiMessage.text()) != null && !text.isEmpty()) {
                items.add(OpenAiOfficialResponsesStreamingChatModel.createTextMessage(EasyInputMessage.Role.ASSISTANT, text));
            }
            if (aiMessage.hasToolExecutionRequests()) {
                aiMessage.toolExecutionRequests().stream().map(toolRequest -> ResponseInputItem.ofFunctionCall((ResponseFunctionToolCall)ResponseFunctionToolCall.builder().callId(toolRequest.id()).name(toolRequest.name()).arguments(toolRequest.arguments()).build())).forEach(items::add);
            }
            if (items.isEmpty()) {
                items.add(OpenAiOfficialResponsesStreamingChatModel.createTextMessage(EasyInputMessage.Role.ASSISTANT, ""));
            }
            return items;
        }
        if (msg instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage toolResultMessage = (ToolExecutionResultMessage)msg;
            ResponseInputItem.FunctionCallOutput.Builder outputBuilder = ResponseInputItem.FunctionCallOutput.builder().callId(toolResultMessage.id());
            if (toolResultMessage.hasSingleText()) {
                outputBuilder.output(toolResultMessage.text());
            } else {
                ArrayList<ResponseFunctionCallOutputItem> outputItems = new ArrayList<ResponseFunctionCallOutputItem>();
                for (Content content : toolResultMessage.contents()) {
                    if (content instanceof TextContent) {
                        TextContent textContent = (TextContent)content;
                        outputItems.add(ResponseFunctionCallOutputItem.ofInputText((ResponseInputTextContent)ResponseInputTextContent.builder().text(textContent.text()).build()));
                        continue;
                    }
                    if (content instanceof ImageContent) {
                        ImageContent imageContent = (ImageContent)content;
                        outputItems.add(ResponseFunctionCallOutputItem.ofInputImage((ResponseInputImageContent)ResponseInputImageContent.builder().imageUrl(OpenAiOfficialResponsesStreamingChatModel.buildImageUrl(imageContent.image())).detail(OpenAiOfficialResponsesStreamingChatModel.toResponsesImageDetail(imageContent.detailLevel())).build()));
                        continue;
                    }
                    throw new UnsupportedFeatureException("Unsupported content type in tool result: " + content.getClass().getName() + ". Only TextContent and ImageContent are supported.");
                }
                outputBuilder.output(ResponseInputItem.FunctionCallOutput.Output.ofResponseFunctionCallOutputItemList(outputItems));
            }
            return Collections.singletonList(ResponseInputItem.ofFunctionCallOutput((ResponseInputItem.FunctionCallOutput)outputBuilder.build()));
        }
        return Collections.singletonList(OpenAiOfficialResponsesStreamingChatModel.createTextMessage(EasyInputMessage.Role.USER, msg.toString()));
    }

    private static ResponseInputItem toReasoningInputItem(String encryptedContent, String thinking) {
        ArrayList<ResponseReasoningItem.Summary> summaries = new ArrayList<ResponseReasoningItem.Summary>();
        if (thinking != null && !thinking.isEmpty()) {
            summaries.add(ResponseReasoningItem.Summary.builder().text(thinking).build());
        }
        ResponseReasoningItem reasoningItem = ResponseReasoningItem.builder().id((JsonField)JsonMissing.of()).summary(summaries).encryptedContent(encryptedContent).build();
        return ResponseInputItem.ofReasoning((ResponseReasoningItem)reasoningItem);
    }

    private static ResponseInputItem createTextMessage(EasyInputMessage.Role role, String text) {
        return ResponseInputItem.ofEasyInputMessage((EasyInputMessage)EasyInputMessage.builder().role(role).content(EasyInputMessage.Content.ofTextInput((String)text)).build());
    }

    private static ResponseInputItem createUserMessage(UserMessage userMessage) {
        List<Content> contents = userMessage.contents();
        ArrayList<ResponseInputContent> contentList = new ArrayList<ResponseInputContent>();
        for (Content content : contents) {
            if (content instanceof TextContent) {
                TextContent textContent = (TextContent)content;
                contentList.add(ResponseInputContent.ofInputText((ResponseInputText)ResponseInputText.builder().text(textContent.text()).build()));
                continue;
            }
            if (content instanceof ImageContent) {
                ImageContent imageContent = (ImageContent)content;
                Image image = imageContent.image();
                String imageUrl = OpenAiOfficialResponsesStreamingChatModel.buildImageUrl(image);
                contentList.add(ResponseInputContent.ofInputImage((ResponseInputImage)ResponseInputImage.builder().imageUrl(imageUrl).detail(OpenAiOfficialResponsesStreamingChatModel.toResponsesUserImageDetail(imageContent.detailLevel())).build()));
                continue;
            }
            if (!(content instanceof PdfFileContent)) continue;
            PdfFileContent pdfFileContent = (PdfFileContent)content;
            ResponseInputFile.Builder pdfInput = ResponseInputFile.builder();
            if (pdfFileContent.pdfFile().url() != null) {
                pdfInput.fileUrl(pdfFileContent.pdfFile().url().toString());
            } else if (pdfFileContent.pdfFile().base64Data() != null) {
                pdfInput.filename("document.pdf");
                pdfInput.fileData("data:" + pdfFileContent.pdfFile().mimeType() + ";base64," + pdfFileContent.pdfFile().base64Data());
            } else {
                throw new IllegalArgumentException("PDF must have either url or base64Data");
            }
            contentList.add(ResponseInputContent.ofInputFile((ResponseInputFile)pdfInput.build()));
        }
        return ResponseInputItem.ofEasyInputMessage((EasyInputMessage)EasyInputMessage.builder().role(EasyInputMessage.Role.USER).content(EasyInputMessage.Content.ofResponseInputMessageContentList(contentList)).build());
    }

    private static String buildImageUrl(Image image) {
        if (image.url() != null) {
            return image.url().toString();
        }
        if (image.base64Data() != null) {
            String mimeType = image.mimeType() != null ? image.mimeType() : "image/jpeg";
            return "data:" + mimeType + ";base64," + image.base64Data();
        }
        throw new IllegalArgumentException("Image must have either url or base64Data");
    }

    private static ResponseInputImage.Detail toResponsesUserImageDetail(ImageContent.DetailLevel detailLevel) {
        switch (detailLevel) {
            case LOW: {
                return ResponseInputImage.Detail.LOW;
            }
            case HIGH: {
                return ResponseInputImage.Detail.HIGH;
            }
            case AUTO: {
                return ResponseInputImage.Detail.AUTO;
            }
        }
        throw new UnsupportedFeatureException("DetailLevel " + detailLevel + " is not supported by OpenAI Responses API. Supported values: LOW, HIGH, AUTO");
    }

    private static ResponseInputImageContent.Detail toResponsesImageDetail(ImageContent.DetailLevel detailLevel) {
        switch (detailLevel) {
            case LOW: {
                return ResponseInputImageContent.Detail.LOW;
            }
            case HIGH: {
                return ResponseInputImageContent.Detail.HIGH;
            }
            case AUTO: {
                return ResponseInputImageContent.Detail.AUTO;
            }
        }
        throw new UnsupportedFeatureException("DetailLevel " + detailLevel + " is not supported by OpenAI Responses API. Supported values: LOW, HIGH, AUTO");
    }

    private static FunctionTool toResponsesTool(ToolSpecification toolSpec, boolean strict) {
        boolean effectiveStrict = ToolSpecificationUtils.isEffectivelyStrict((ToolSpecification)toolSpec, (boolean)strict);
        try {
            FunctionTool.Parameters.Builder parametersBuilder = FunctionTool.Parameters.builder();
            if (toolSpec.parameters() != null) {
                JsonSchemaElementUtils.toMap((JsonSchemaElement)toolSpec.parameters(), (boolean)effectiveStrict).forEach((key, value) -> parametersBuilder.putAdditionalProperty(key, JsonValue.from((Object)value)));
            } else if (effectiveStrict) {
                parametersBuilder.putAdditionalProperty("type", JsonValue.from((Object)"object")).putAdditionalProperty("properties", JsonValue.from(Collections.emptyMap())).putAdditionalProperty("additionalProperties", JsonValue.from((Object)false));
            }
            return FunctionTool.builder().name(toolSpec.name()).description(toolSpec.description()).parameters(parametersBuilder.build()).strict(effectiveStrict).build();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to convert tool specification for tool: " + toolSpec.name(), e);
        }
    }

    private static List<Tool> toResponsesTools(List<ToolSpecification> toolSpecifications, boolean strict, List<Tool> serverTools) {
        ArrayList<Tool> tools = new ArrayList<Tool>();
        if (toolSpecifications != null) {
            for (ToolSpecification toolSpecification : toolSpecifications) {
                tools.add(Tool.ofFunction((FunctionTool)OpenAiOfficialResponsesStreamingChatModel.toResponsesTool(toolSpecification, strict)));
            }
        }
        if (serverTools != null) {
            tools.addAll(serverTools);
        }
        return tools;
    }

    private static ToolChoiceOptions toResponsesToolChoice(ToolChoice toolChoice) {
        if (toolChoice == null) {
            return null;
        }
        switch (toolChoice) {
            case AUTO: {
                return ToolChoiceOptions.AUTO;
            }
            case REQUIRED: {
                return ToolChoiceOptions.REQUIRED;
            }
            case NONE: {
                return ToolChoiceOptions.NONE;
            }
        }
        return null;
    }

    private static ResponseTextConfig toResponseTextConfig(ResponseFormat responseFormat, Boolean strict, String textVerbosity) {
        ResponseTextConfig.Builder builder = null;
        if (responseFormat != null && responseFormat.type() != ResponseFormatType.TEXT) {
            builder = ResponseTextConfig.builder();
            JsonSchema jsonSchema = responseFormat.jsonSchema();
            if (jsonSchema == null) {
                builder.format(ResponseFormatTextConfig.ofJsonObject((ResponseFormatJsonObject)ResponseFormatJsonObject.builder().build()));
            } else {
                if (!(jsonSchema.rootElement() instanceof JsonObjectSchema) && !(jsonSchema.rootElement() instanceof JsonRawSchema)) {
                    throw new IllegalArgumentException("For OpenAI, the root element of the JSON Schema must be either a JsonObjectSchema or a JsonRawSchema, but it was: " + jsonSchema.rootElement().getClass());
                }
                Map<String, Object> schemaMap = JsonSchemaElementUtils.toMap((JsonSchemaElement)jsonSchema.rootElement(), (boolean)strict);
                ResponseFormatTextJsonSchemaConfig.Schema.Builder schemaBuilder = ResponseFormatTextJsonSchemaConfig.Schema.builder();
                for (Map.Entry<String, Object> entry : schemaMap.entrySet()) {
                    schemaBuilder.putAdditionalProperty((String)entry.getKey(), JsonValue.from(entry.getValue()));
                }
                ResponseFormatTextJsonSchemaConfig schemaConfig = ResponseFormatTextJsonSchemaConfig.builder().name(jsonSchema.name()).schema(schemaBuilder.build()).strict(strict).build();
                builder.format(ResponseFormatTextConfig.ofJsonSchema((ResponseFormatTextJsonSchemaConfig)schemaConfig));
            }
        }
        if (textVerbosity != null && !textVerbosity.isEmpty()) {
            if (builder == null) {
                builder = ResponseTextConfig.builder();
            }
            builder.verbosity(ResponseTextConfig.Verbosity.Companion.of(textVerbosity));
        }
        return builder != null ? builder.build() : null;
    }

    private static class ResponsesStreamingHandle
    implements StreamingHandle {
        private final Runnable cancelCallback;
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private volatile Future<?> streamingFuture;
        private volatile boolean completed;

        ResponsesStreamingHandle(Runnable cancelCallback) {
            this.cancelCallback = cancelCallback;
        }

        void setStreamingFuture(Future<?> streamingFuture) {
            this.streamingFuture = streamingFuture;
            if (this.cancelled.get() && streamingFuture != null) {
                streamingFuture.cancel(true);
            }
        }

        void markCompleted() {
            this.completed = true;
        }

        public void cancel() {
            if (this.cancelled.compareAndSet(false, true)) {
                if (!this.completed && this.cancelCallback != null) {
                    try {
                        this.cancelCallback.run();
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                if (this.streamingFuture != null) {
                    this.streamingFuture.cancel(true);
                }
            }
        }

        public boolean isCancelled() {
            return this.cancelled.get();
        }
    }

    static class ResponsesEventHandler {
        private final MappingTrackingStreamingChatResponseHandler handler;
        private final AtomicReference<String> responseIdRef;
        private final String modelName;
        private final StreamingHandle streamingHandle;
        private final Map<String, ToolExecutionRequest.Builder> toolCallBuilders = new HashMap<String, ToolExecutionRequest.Builder>();
        private final Map<String, Integer> toolCallIndices = new HashMap<String, Integer>();
        private final List<ToolExecutionRequest> completedToolCalls = new ArrayList<ToolExecutionRequest>();
        private final StringBuilder textBuilder = new StringBuilder();
        private OpenAiOfficialTokenUsage tokenUsage;
        private String responseId;
        private String finishReason;
        private int nextToolCallIndex = 0;

        ResponsesEventHandler(StreamingChatResponseHandler handler, AtomicReference<String> responseIdRef, String modelName, StreamingHandle streamingHandle) {
            this.handler = new MappingTrackingStreamingChatResponseHandler(handler);
            this.responseIdRef = responseIdRef;
            this.modelName = modelName;
            this.streamingHandle = streamingHandle;
        }

        void handleEvent(ResponseStreamEvent event) {
            if (this.streamingHandle != null && this.streamingHandle.isCancelled()) {
                throw new CancellationException("Request cancelled by user");
            }
            try {
                this.handler.resetMappingTracking();
                if (event.isCreated()) {
                    this.handleCreated(event.asCreated());
                } else if (event.isOutputTextDelta()) {
                    this.handleOutputTextDelta(event.asOutputTextDelta());
                } else if (event.isOutputItemAdded()) {
                    this.handleOutputItemAdded(event.asOutputItemAdded());
                } else if (event.isReasoningTextDelta()) {
                    this.handleReasoningTextDelta(event.asReasoningTextDelta());
                } else if (event.isReasoningSummaryTextDelta()) {
                    this.handleReasoningSummaryTextDelta(event.asReasoningSummaryTextDelta());
                } else if (event.isFunctionCallArgumentsDelta()) {
                    this.handleFunctionCallArgumentsDelta(event.asFunctionCallArgumentsDelta());
                } else if (event.isFunctionCallArgumentsDone()) {
                    this.handleFunctionCallArgumentsDone(event.asFunctionCallArgumentsDone());
                } else if (event.isOutputItemDone()) {
                    this.handleOutputItemDone(event.asOutputItemDone());
                } else if (event.isCompleted()) {
                    this.handleCompleted(event.asCompleted());
                } else if (event.isError()) {
                    this.handleError(event.asError());
                } else if (event.isFailed()) {
                    this.handleFailed(event.asFailed());
                } else if (event.isIncomplete()) {
                    this.handleIncomplete(event.asIncomplete());
                }
                if (!this.handler.wasMapped()) {
                    InternalStreamingChatResponseHandlerUtils.onUnmappedRawEvent((StreamingChatResponseHandler)this.handler, (Object)event);
                }
            }
            catch (RuntimeException e) {
                throw e;
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void handleCreated(ResponseCreatedEvent event) {
            this.responseId = event.response().id();
            this.responseIdRef.set(this.responseId);
        }

        private void handleOutputTextDelta(ResponseTextDeltaEvent event) {
            String delta = event.delta();
            if (!delta.isEmpty()) {
                this.textBuilder.append(delta);
                InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)this.handler, (String)delta, (StreamingHandle)this.streamingHandle);
            }
        }

        private void handleOutputItemAdded(ResponseOutputItemAddedEvent event) {
            ResponseOutputItem item = event.item();
            if (item.isFunctionCall()) {
                ResponseFunctionToolCall functionCall = item.asFunctionCall();
                String itemId = functionCall.id().orElse(null);
                if (itemId != null) {
                    this.toolCallBuilders.put(itemId, ToolExecutionRequest.builder().id(functionCall.callId()).name(functionCall.name()).arguments(""));
                    this.toolCallIndices.put(itemId, this.nextToolCallIndex++);
                } else {
                    logger.warn("Function call missing item ID: {}", (Object)functionCall.callId());
                }
            }
        }

        private void handleFunctionCallArgumentsDelta(ResponseFunctionCallArgumentsDeltaEvent event) {
            String itemId = event.itemId();
            ToolExecutionRequest.Builder builder = this.toolCallBuilders.get(itemId);
            Integer index = this.toolCallIndices.get(itemId);
            if (builder == null || index == null) {
                return;
            }
            String delta = event.delta();
            if (delta.isEmpty()) {
                return;
            }
            String currentArgs = builder.build().arguments();
            builder.arguments(currentArgs + delta);
            PartialToolCall partialToolCall = PartialToolCall.builder().index(index.intValue()).id(builder.build().id()).name(builder.build().name()).partialArguments(delta).build();
            InternalStreamingChatResponseHandlerUtils.onPartialToolCall((StreamingChatResponseHandler)this.handler, (PartialToolCall)partialToolCall, (StreamingHandle)this.streamingHandle);
        }

        private void handleReasoningTextDelta(ResponseReasoningTextDeltaEvent event) {
            String delta = event.delta();
            if (!delta.isEmpty()) {
                InternalStreamingChatResponseHandlerUtils.onPartialThinking((StreamingChatResponseHandler)this.handler, (String)delta, (StreamingHandle)this.streamingHandle);
            }
        }

        private void handleReasoningSummaryTextDelta(ResponseReasoningSummaryTextDeltaEvent event) {
            String delta = event.delta();
            if (!delta.isEmpty()) {
                InternalStreamingChatResponseHandlerUtils.onPartialThinking((StreamingChatResponseHandler)this.handler, (String)delta, (StreamingHandle)this.streamingHandle);
            }
        }

        private void handleFunctionCallArgumentsDone(ResponseFunctionCallArgumentsDoneEvent event) {
            String itemId = event.itemId();
            ToolExecutionRequest.Builder builder = this.toolCallBuilders.remove(itemId);
            Integer index = this.toolCallIndices.remove(itemId);
            if (builder != null && index != null) {
                builder.arguments(event.arguments());
                ToolExecutionRequest toolExecutionRequest = builder.build();
                this.completedToolCalls.add(toolExecutionRequest);
                if (!this.streamingHandle.isCancelled()) {
                    try {
                        this.handler.onCompleteToolCall(new CompleteToolCall(index.intValue(), toolExecutionRequest));
                    }
                    catch (Exception e) {
                        InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)e));
                    }
                }
            } else {
                logger.warn("No builder for itemId in argumentsDone: {}", (Object)itemId);
            }
        }

        private void handleOutputItemDone(ResponseOutputItemDoneEvent event) {
        }

        private void handleCompleted(ResponseCompletedEvent event) {
            Response response = event.response();
            response.status().ifPresent(status -> {
                this.finishReason = OpenAiOfficialResponsesStreamingChatModel.mapStatusToFinishReason(status.asString(), !this.completedToolCalls.isEmpty());
            });
            this.extractTokenUsageAndComplete(response);
        }

        private void handleError(ResponseErrorEvent event) {
            String message = event.message();
            InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)new RuntimeException("Response error: " + message)));
        }

        private void handleFailed(ResponseFailedEvent event) {
            Response response = event.response();
            String message = response.error().map(this::extractErrorMessage).orElse("Response failed");
            InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)new RuntimeException("Response failed: " + message)));
        }

        private String extractErrorMessage(ResponseError error) {
            String message = error.message();
            if (message.trim().isEmpty()) {
                return error.toString();
            }
            return message;
        }

        private void handleIncomplete(ResponseIncompleteEvent event) {
            this.finishReason = "LENGTH";
            this.extractTokenUsageAndComplete(event.response());
        }

        private void extractTokenUsageAndComplete(Response response) {
            String text = this.textBuilder.length() > 0 ? this.textBuilder.toString() : null;
            AiMessage aiMessage = OpenAiOfficialResponsesStreamingChatModel.buildAiMessage(text, OpenAiOfficialResponsesStreamingChatModel.extractReasoningSummary(response), this.completedToolCalls, OpenAiOfficialResponsesStreamingChatModel.extractEncryptedReasoning(response));
            this.tokenUsage = OpenAiOfficialResponsesStreamingChatModel.extractTokenUsage(response);
            OpenAiOfficialResponsesChatResponseMetadata metadata = OpenAiOfficialResponsesStreamingChatModel.buildResponseMetadata(this.responseId, this.modelName, response, this.finishReason, this.tokenUsage);
            ChatResponse chatResponse = ChatResponse.builder().aiMessage(aiMessage).metadata((ChatResponseMetadata)metadata).build();
            if (!this.streamingHandle.isCancelled()) {
                try {
                    this.handler.onCompleteResponse(chatResponse);
                }
                catch (Exception e) {
                    InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)e));
                }
            }
        }
    }

    public static class Builder {
        private String baseUrl;
        private String apiKey;
        private Credential credential;
        private String microsoftFoundryDeploymentName;
        private AzureOpenAIServiceVersion azureOpenAIServiceVersion;
        private String organizationId;
        private boolean isMicrosoftFoundry;
        private boolean isGitHubModels;
        private Map<String, String> customHeaders;
        private Duration timeout;
        private Integer maxRetries;
        private Proxy proxy;
        private OpenAIClient client;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Integer maxOutputTokens;
        private Integer maxToolCalls;
        private Boolean parallelToolCalls;
        private String previousResponseId;
        private Integer topLogprobs;
        private String truncation;
        private List<String> include;
        private String serviceTier;
        private String safetyIdentifier;
        private String promptCacheKey;
        private String promptCacheRetention;
        private ReasoningEffort reasoningEffort;
        private Reasoning.Summary reasoningSummary;
        private String textVerbosity;
        private Boolean streamIncludeObfuscation;
        private Boolean store;
        private List<ChatModelListener> listeners;
        private ExecutorService executorService;
        private Boolean strictTools;
        private Boolean strictJsonSchema;
        private List<ToolSpecification> toolSpecifications;
        private ToolChoice toolChoice;
        private ResponseFormat responseFormat;
        private ChatRequestParameters defaultRequestParameters;
        private List<Tool> serverTools;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder credential(Credential credential) {
            this.credential = credential;
            return this;
        }

        @Deprecated
        public Builder azureDeploymentName(String azureDeploymentName) {
            this.microsoftFoundryDeploymentName = azureDeploymentName;
            return this;
        }

        public Builder microsoftFoundryDeploymentName(String microsoftFoundryDeploymentName) {
            this.microsoftFoundryDeploymentName = microsoftFoundryDeploymentName;
            return this;
        }

        public Builder azureOpenAIServiceVersion(AzureOpenAIServiceVersion azureOpenAIServiceVersion) {
            this.azureOpenAIServiceVersion = azureOpenAIServiceVersion;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        @Deprecated
        public Builder isAzure(boolean isAzure) {
            this.isMicrosoftFoundry = isAzure;
            return this;
        }

        public Builder isMicrosoftFoundry(boolean isMicrosoftFoundry) {
            this.isMicrosoftFoundry = isMicrosoftFoundry;
            return this;
        }

        public Builder isGitHubModels(boolean isGitHubModels) {
            this.isGitHubModels = isGitHubModels;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public Builder client(OpenAIClient client) {
            this.client = client;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Builder maxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
            return this;
        }

        public Builder maxToolCalls(Integer maxToolCalls) {
            this.maxToolCalls = maxToolCalls;
            return this;
        }

        public Builder parallelToolCalls(Boolean parallelToolCalls) {
            this.parallelToolCalls = parallelToolCalls;
            return this;
        }

        public Builder previousResponseId(String previousResponseId) {
            this.previousResponseId = previousResponseId;
            return this;
        }

        public Builder topLogprobs(Integer topLogprobs) {
            this.topLogprobs = topLogprobs;
            return this;
        }

        public Builder truncation(String truncation) {
            this.truncation = truncation;
            return this;
        }

        public Builder include(List<String> include) {
            this.include = include;
            return this;
        }

        public Builder serviceTier(String serviceTier) {
            this.serviceTier = serviceTier;
            return this;
        }

        public Builder safetyIdentifier(String safetyIdentifier) {
            this.safetyIdentifier = safetyIdentifier;
            return this;
        }

        public Builder promptCacheKey(String promptCacheKey) {
            this.promptCacheKey = promptCacheKey;
            return this;
        }

        public Builder promptCacheRetention(String promptCacheRetention) {
            this.promptCacheRetention = promptCacheRetention;
            return this;
        }

        public Builder reasoningEffort(ReasoningEffort reasoningEffort) {
            this.reasoningEffort = reasoningEffort;
            return this;
        }

        @Deprecated
        public Builder reasoningEffort(String reasoningEffort) {
            return this.reasoningEffort(reasoningEffort != null ? ReasoningEffort.of((String)reasoningEffort) : null);
        }

        public Builder reasoningSummary(Reasoning.Summary reasoningSummary) {
            this.reasoningSummary = reasoningSummary;
            return this;
        }

        public Builder textVerbosity(String textVerbosity) {
            this.textVerbosity = textVerbosity;
            return this;
        }

        public Builder streamIncludeObfuscation(Boolean streamIncludeObfuscation) {
            this.streamIncludeObfuscation = streamIncludeObfuscation;
            return this;
        }

        public Builder store(Boolean store) {
            this.store = store;
            return this;
        }

        public Builder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public Builder listeners(ChatModelListener ... listeners) {
            return this.listeners(Arrays.asList(listeners));
        }

        public Builder executorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        @Deprecated
        public Builder strict(Boolean strict) {
            this.strictTools = strict;
            this.strictJsonSchema = strict;
            return this;
        }

        public Builder strictTools(Boolean strictTools) {
            this.strictTools = strictTools;
            return this;
        }

        public Builder strictJsonSchema(Boolean strictJsonSchema) {
            this.strictJsonSchema = strictJsonSchema;
            return this;
        }

        public Builder toolSpecifications(List<ToolSpecification> toolSpecifications) {
            this.toolSpecifications = toolSpecifications;
            return this;
        }

        public Builder toolSpecifications(ToolSpecification ... toolSpecifications) {
            return this.toolSpecifications(Arrays.asList(toolSpecifications));
        }

        public Builder toolChoice(ToolChoice toolChoice) {
            this.toolChoice = toolChoice;
            return this;
        }

        public Builder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public Builder defaultRequestParameters(ChatRequestParameters defaultRequestParameters) {
            this.defaultRequestParameters = defaultRequestParameters;
            return this;
        }

        public Builder serverTools(List<Tool> serverTools) {
            this.serverTools = serverTools;
            return this;
        }

        public Builder serverTools(Tool ... serverTools) {
            return this.serverTools(Arrays.asList(serverTools));
        }

        public OpenAiOfficialResponsesStreamingChatModel build() {
            return new OpenAiOfficialResponsesStreamingChatModel(this);
        }
    }
}

