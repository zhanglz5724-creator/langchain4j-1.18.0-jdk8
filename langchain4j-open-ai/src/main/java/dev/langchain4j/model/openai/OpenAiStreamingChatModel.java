/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils
 *  dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler
 *  dev.langchain4j.internal.ToolCallBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.PartialToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils;
import dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler;
import dev.langchain4j.internal.ToolCallBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.StreamingHandle;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiStreamingResponseBuilder;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.OpenAiUtils;
import dev.langchain4j.model.openai.internal.ParsedAndRawResponse;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionChoice;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse;
import dev.langchain4j.model.openai.internal.chat.Delta;
import dev.langchain4j.model.openai.internal.chat.FunctionCall;
import dev.langchain4j.model.openai.internal.chat.ToolCall;
import dev.langchain4j.model.openai.internal.shared.StreamOptions;
import dev.langchain4j.model.openai.spi.OpenAiStreamingChatModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;

public class OpenAiStreamingChatModel
implements StreamingChatModel {
    private final OpenAiClient client;
    private final OpenAiChatRequestParameters defaultRequestParameters;
    private final boolean strictJsonSchema;
    private final boolean strictTools;
    private final boolean returnThinking;
    private final boolean sendThinking;
    private final String thinkingFieldName;
    private final boolean accumulateToolCallId;
    private final boolean useInputImageFormat;
    private final List<ChatModelListener> listeners;

    public OpenAiStreamingChatModel(OpenAiStreamingChatModelBuilder builder) {
        ChatRequestParameters commonParameters;
        this.client = ((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)OpenAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.openai.com/v1"))).apiKey(builder.apiKey)).organizationId(builder.organizationId)).projectId(builder.projectId)).connectTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(15L)))).readTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L)))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).userAgent("langchain4j-openai")).customHeaders(builder.customHeadersSupplier)).customQueryParams(builder.customQueryParams)).build();
        if (builder.defaultRequestParameters != null) {
            OpenAiUtils.validate(builder.defaultRequestParameters);
            commonParameters = builder.defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        OpenAiChatRequestParameters openAiParameters = builder.defaultRequestParameters instanceof OpenAiChatRequestParameters ? (OpenAiChatRequestParameters)builder.defaultRequestParameters : OpenAiChatRequestParameters.EMPTY;
        this.defaultRequestParameters = ((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)OpenAiChatRequestParameters.builder().modelName((String)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName()))).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature()))).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP()))).frequencyPenalty((Double)Utils.getOrDefault((Object)builder.frequencyPenalty, (Object)commonParameters.frequencyPenalty()))).presencePenalty((Double)Utils.getOrDefault((Object)builder.presencePenalty, (Object)commonParameters.presencePenalty()))).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxTokens, (Object)commonParameters.maxOutputTokens()))).stopSequences(Utils.getOrDefault((List)builder.stop, (List)commonParameters.stopSequences()))).toolSpecifications(commonParameters.toolSpecifications())).toolChoice(commonParameters.toolChoice())).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat()))).maxCompletionTokens((Integer)Utils.getOrDefault((Object)builder.maxCompletionTokens, (Object)openAiParameters.maxCompletionTokens())).logitBias(Utils.getOrDefault((Map)builder.logitBias, openAiParameters.logitBias())).parallelToolCalls((Boolean)Utils.getOrDefault((Object)builder.parallelToolCalls, (Object)openAiParameters.parallelToolCalls())).seed((Integer)Utils.getOrDefault((Object)builder.seed, (Object)openAiParameters.seed())).user((String)Utils.getOrDefault((Object)builder.user, (Object)openAiParameters.user())).store((Boolean)Utils.getOrDefault((Object)builder.store, (Object)openAiParameters.store())).metadata(Utils.getOrDefault((Map)builder.metadata, openAiParameters.metadata())).serviceTier((String)Utils.getOrDefault((Object)builder.serviceTier, (Object)openAiParameters.serviceTier())).reasoningEffort((String)Utils.getOrDefault((Object)builder.reasoningEffort, (Object)openAiParameters.reasoningEffort())).customParameters(Utils.getOrDefault((Map)builder.customParameters, openAiParameters.customParameters())).build();
        this.strictJsonSchema = (Boolean)Utils.getOrDefault((Object)builder.strictJsonSchema, (Object)false);
        this.strictTools = (Boolean)Utils.getOrDefault((Object)builder.strictTools, (Object)false);
        this.returnThinking = (Boolean)Utils.getOrDefault((Object)builder.returnThinking, (Object)false);
        this.sendThinking = (Boolean)Utils.getOrDefault((Object)builder.sendThinking, (Object)false);
        this.thinkingFieldName = (String)Utils.getOrDefault((Object)builder.thinkingFieldName, (Object)"reasoning_content");
        this.accumulateToolCallId = (Boolean)Utils.getOrDefault((Object)builder.accumulateToolCallId, (Object)true);
        this.useInputImageFormat = (Boolean)Utils.getOrDefault((Object)builder.useInputImageFormat, (Object)false);
        this.listeners = Utils.copy((List)builder.listeners);
    }

    public OpenAiChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public void doChat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
        OpenAiChatRequestParameters parameters = (OpenAiChatRequestParameters)chatRequest.parameters();
        OpenAiUtils.validate((ChatRequestParameters)parameters);
        ChatCompletionRequest openAiRequest = OpenAiUtils.toOpenAiChatRequest(chatRequest, parameters, this.sendThinking, this.thinkingFieldName, this.strictTools, this.strictJsonSchema, this.useInputImageFormat).stream(true).streamOptions(StreamOptions.builder().includeUsage(true).build()).build();
        OpenAiStreamingResponseBuilder openAiResponseBuilder = new OpenAiStreamingResponseBuilder(this.returnThinking, this.accumulateToolCallId);
        ToolCallBuilder toolCallBuilder = new ToolCallBuilder();
        MappingTrackingStreamingChatResponseHandler trackingHandler = new MappingTrackingStreamingChatResponseHandler(handler);
        this.client.chatCompletion(openAiRequest).onRawPartialResponse(parsedAndRawResponse -> {
            trackingHandler.resetMappingTracking();
            openAiResponseBuilder.append((ParsedAndRawResponse<ChatCompletionResponse>)parsedAndRawResponse);
            this.handle((ParsedAndRawResponse<ChatCompletionResponse>)parsedAndRawResponse, toolCallBuilder, (StreamingChatResponseHandler)trackingHandler);
            if (!trackingHandler.wasMapped()) {
                InternalStreamingChatResponseHandlerUtils.onUnmappedRawEvent((StreamingChatResponseHandler)trackingHandler, (Object)parsedAndRawResponse.rawServerSentEvent());
            }
        }).onComplete(() -> {
            if (toolCallBuilder.hasRequests()) {
                InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)trackingHandler, (CompleteToolCall)toolCallBuilder.buildAndReset());
            }
            ChatResponse completeResponse = openAiResponseBuilder.build();
            InternalStreamingChatResponseHandlerUtils.onCompleteResponse((StreamingChatResponseHandler)trackingHandler, (ChatResponse)completeResponse);
        }).onError(throwable -> {
            RuntimeException mappedException = ExceptionMapper.DEFAULT.mapException(throwable);
            InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> handler.onError((Throwable)mappedException));
        }).execute();
    }

    private void handle(ParsedAndRawResponse<ChatCompletionResponse> parsedAndRawResponse, ToolCallBuilder toolCallBuilder, StreamingChatResponseHandler handler) {
        List<ToolCall> toolCalls;
        ChatCompletionResponse partialResponse = parsedAndRawResponse.parsedResponse();
        if (partialResponse == null) {
            return;
        }
        List<ChatCompletionChoice> choices = partialResponse.choices();
        if (Utils.isNullOrEmpty(choices)) {
            return;
        }
        ChatCompletionChoice chatCompletionChoice = choices.get(0);
        if (chatCompletionChoice == null) {
            return;
        }
        Delta delta = chatCompletionChoice.delta();
        if (delta == null) {
            return;
        }
        String content = delta.content();
        if (!Utils.isNullOrEmpty((String)content)) {
            InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)handler, (String)content, (StreamingHandle)parsedAndRawResponse.streamingHandle());
        }
        String reasoningContent = delta.reasoningContent();
        if (this.returnThinking && !Utils.isNullOrEmpty((String)reasoningContent)) {
            InternalStreamingChatResponseHandlerUtils.onPartialThinking((StreamingChatResponseHandler)handler, (String)reasoningContent, (StreamingHandle)parsedAndRawResponse.streamingHandle());
        }
        if ((toolCalls = delta.toolCalls()) != null) {
            for (ToolCall toolCall : toolCalls) {
                int index;
                if (toolCall.index() != null) {
                    index = toolCall.index();
                } else {
                    index = toolCallBuilder.index();
                    if (toolCall.id() != null && toolCallBuilder.id() != null && !toolCallBuilder.id().equals(toolCall.id())) {
                        index = toolCallBuilder.index() + 1;
                    }
                }
                if (toolCallBuilder.index() != index) {
                    InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)handler, (CompleteToolCall)toolCallBuilder.buildAndReset());
                    toolCallBuilder.updateIndex(Integer.valueOf(index));
                }
                String id = toolCallBuilder.updateId(toolCall.id());
                FunctionCall functionCall = toolCall.function();
                String name = toolCallBuilder.updateName(functionCall == null ? null : functionCall.name());
                String partialArguments = functionCall == null ? null : functionCall.arguments();
                if (!Utils.isNotNullOrEmpty((String)partialArguments)) continue;
                toolCallBuilder.appendArguments(partialArguments);
                PartialToolCall partialToolRequest = PartialToolCall.builder().index(index).id(id).name(name).partialArguments(partialArguments).build();
                InternalStreamingChatResponseHandlerUtils.onPartialToolCall((StreamingChatResponseHandler)handler, (PartialToolCall)partialToolRequest, (StreamingHandle)parsedAndRawResponse.streamingHandle());
            }
        }
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.OPEN_AI;
    }

    public static OpenAiStreamingChatModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OpenAiStreamingChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OpenAiStreamingChatModelBuilderFactory factory = (OpenAiStreamingChatModelBuilderFactory)iterator.next();
            return (OpenAiStreamingChatModelBuilder)factory.get();
        }
        return new OpenAiStreamingChatModelBuilder();
    }

    public static class OpenAiStreamingChatModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String organizationId;
        private String projectId;
        private ChatRequestParameters defaultRequestParameters;
        private String modelName;
        private Double temperature;
        private Double topP;
        private List<String> stop;
        private Integer maxTokens;
        private Integer maxCompletionTokens;
        private Double presencePenalty;
        private Double frequencyPenalty;
        private Map<String, Integer> logitBias;
        private ResponseFormat responseFormat;
        private Boolean strictJsonSchema;
        private Integer seed;
        private String user;
        private Boolean strictTools;
        private Boolean parallelToolCalls;
        private Boolean store;
        private Map<String, String> metadata;
        private String serviceTier;
        private String reasoningEffort;
        private Boolean returnThinking;
        private Boolean sendThinking;
        private String thinkingFieldName;
        private Boolean accumulateToolCallId;
        private Boolean useInputImageFormat;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Supplier<Map<String, String>> customHeadersSupplier;
        private Map<String, String> customQueryParams;
        private Map<String, Object> customParameters;
        private List<ChatModelListener> listeners;

        public OpenAiStreamingChatModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public OpenAiStreamingChatModelBuilder defaultRequestParameters(ChatRequestParameters parameters) {
            this.defaultRequestParameters = parameters;
            return this;
        }

        public OpenAiStreamingChatModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public OpenAiStreamingChatModelBuilder modelName(OpenAiChatModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public OpenAiStreamingChatModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public OpenAiStreamingChatModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public OpenAiStreamingChatModelBuilder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public OpenAiStreamingChatModelBuilder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public OpenAiStreamingChatModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public OpenAiStreamingChatModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public OpenAiStreamingChatModelBuilder stop(List<String> stop) {
            this.stop = stop;
            return this;
        }

        public OpenAiStreamingChatModelBuilder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public OpenAiStreamingChatModelBuilder maxCompletionTokens(Integer maxCompletionTokens) {
            this.maxCompletionTokens = maxCompletionTokens;
            return this;
        }

        public OpenAiStreamingChatModelBuilder presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public OpenAiStreamingChatModelBuilder frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public OpenAiStreamingChatModelBuilder logitBias(Map<String, Integer> logitBias) {
            this.logitBias = logitBias;
            return this;
        }

        public OpenAiStreamingChatModelBuilder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public OpenAiStreamingChatModelBuilder responseFormat(String responseFormat) {
            this.responseFormat = OpenAiUtils.fromOpenAiResponseFormat(responseFormat);
            return this;
        }

        public OpenAiStreamingChatModelBuilder strictJsonSchema(Boolean strictJsonSchema) {
            this.strictJsonSchema = strictJsonSchema;
            return this;
        }

        public OpenAiStreamingChatModelBuilder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public OpenAiStreamingChatModelBuilder user(String user) {
            this.user = user;
            return this;
        }

        public OpenAiStreamingChatModelBuilder strictTools(Boolean strictTools) {
            this.strictTools = strictTools;
            return this;
        }

        public OpenAiStreamingChatModelBuilder parallelToolCalls(Boolean parallelToolCalls) {
            this.parallelToolCalls = parallelToolCalls;
            return this;
        }

        public OpenAiStreamingChatModelBuilder store(Boolean store) {
            this.store = store;
            return this;
        }

        public OpenAiStreamingChatModelBuilder metadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        public OpenAiStreamingChatModelBuilder serviceTier(String serviceTier) {
            this.serviceTier = serviceTier;
            return this;
        }

        public OpenAiStreamingChatModelBuilder reasoningEffort(String reasoningEffort) {
            this.reasoningEffort = reasoningEffort;
            return this;
        }

        public OpenAiStreamingChatModelBuilder returnThinking(Boolean returnThinking) {
            this.returnThinking = returnThinking;
            return this;
        }

        public OpenAiStreamingChatModelBuilder sendThinking(Boolean sendThinking, String fieldName) {
            this.sendThinking = sendThinking;
            this.thinkingFieldName = fieldName;
            return this;
        }

        public OpenAiStreamingChatModelBuilder sendThinking(Boolean sendThinking) {
            this.sendThinking = sendThinking;
            this.thinkingFieldName = "reasoning_content";
            return this;
        }

        public OpenAiStreamingChatModelBuilder accumulateToolCallId(Boolean accumulateToolCallId) {
            this.accumulateToolCallId = accumulateToolCallId;
            return this;
        }

        public OpenAiStreamingChatModelBuilder useInputImageFormat(Boolean useInputImageFormat) {
            this.useInputImageFormat = useInputImageFormat;
            return this;
        }

        public OpenAiStreamingChatModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public OpenAiStreamingChatModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public OpenAiStreamingChatModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OpenAiStreamingChatModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public OpenAiStreamingChatModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public OpenAiStreamingChatModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public OpenAiStreamingChatModelBuilder customQueryParams(Map<String, String> customQueryParams) {
            this.customQueryParams = customQueryParams;
            return this;
        }

        public OpenAiStreamingChatModelBuilder customParameters(Map<String, Object> customParameters) {
            this.customParameters = customParameters;
            return this;
        }

        public OpenAiStreamingChatModelBuilder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public OpenAiStreamingChatModelBuilder listeners(ChatModelListener ... listeners) {
            return this.listeners(Arrays.asList(listeners));
        }

        public OpenAiStreamingChatModel build() {
            return new OpenAiStreamingChatModel(this);
        }
    }
}

