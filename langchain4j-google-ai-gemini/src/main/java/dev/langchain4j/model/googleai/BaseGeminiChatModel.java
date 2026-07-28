/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.request.json.JsonEnumSchema
 *  dev.langchain4j.model.chat.request.json.JsonRawSchema
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.request.json.JsonEnumSchema;
import dev.langchain4j.model.chat.request.json.JsonRawSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.googleai.FinishReasonMapper;
import dev.langchain4j.model.googleai.FunctionMapper;
import dev.langchain4j.model.googleai.GeminiContent;
import dev.langchain4j.model.googleai.GeminiFunctionCallingConfig;
import dev.langchain4j.model.googleai.GeminiGenerateContentRequest;
import dev.langchain4j.model.googleai.GeminiGenerateContentResponse;
import dev.langchain4j.model.googleai.GeminiGenerationConfig;
import dev.langchain4j.model.googleai.GeminiHarmBlockThreshold;
import dev.langchain4j.model.googleai.GeminiHarmCategory;
import dev.langchain4j.model.googleai.GeminiMediaResolutionLevel;
import dev.langchain4j.model.googleai.GeminiMode;
import dev.langchain4j.model.googleai.GeminiRole;
import dev.langchain4j.model.googleai.GeminiSafetySetting;
import dev.langchain4j.model.googleai.GeminiSchema;
import dev.langchain4j.model.googleai.GeminiService;
import dev.langchain4j.model.googleai.GeminiThinkingConfig;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatRequestParameters;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatResponseMetadata;
import dev.langchain4j.model.googleai.GoogleAiGeminiTokenUsage;
import dev.langchain4j.model.googleai.Json;
import dev.langchain4j.model.googleai.PartsAndContentsMapper;
import dev.langchain4j.model.googleai.SchemaMapper;
import dev.langchain4j.model.googleai.UrlContextMetadata;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;

class BaseGeminiChatModel {
    protected final GeminiService geminiService;
    protected final GeminiFunctionCallingConfig functionCallingConfig;
    protected final boolean allowCodeExecution;
    protected final boolean allowGoogleSearch;
    protected final boolean allowGoogleMaps;
    protected final boolean retrieveGoogleMapsWidgetToken;
    protected final boolean allowUrlContext;
    protected final boolean includeCodeExecutionOutput;
    protected final List<GeminiSafetySetting> safetySettings;
    protected final List<ChatModelListener> listeners;
    protected final GeminiThinkingConfig thinkingConfig;
    protected final Boolean returnThinking;
    protected final boolean sendThinking;
    protected final Integer seed;
    protected final Integer logprobs;
    protected final Boolean responseLogprobs;
    protected final Boolean enableEnhancedCivicAnswers;
    protected final GeminiMediaResolutionLevel mediaResolution;
    protected final boolean mediaResolutionPerPartEnabled;
    protected final GoogleAiGeminiChatRequestParameters defaultRequestParameters;

    protected BaseGeminiChatModel(GoogleAiGeminiChatModelBaseBuilder<?> builder, GeminiService geminiService) {
        this.geminiService = geminiService;
        this.functionCallingConfig = builder.functionCallingConfig;
        this.allowCodeExecution = (Boolean)Utils.getOrDefault((Object)builder.allowCodeExecution, (Object)false);
        this.allowGoogleSearch = (Boolean)Utils.getOrDefault((Object)builder.allowGoogleSearch, (Object)false);
        this.allowGoogleMaps = (Boolean)Utils.getOrDefault((Object)builder.allowGoogleMaps, (Object)false);
        this.retrieveGoogleMapsWidgetToken = (Boolean)Utils.getOrDefault((Object)builder.retrieveGoogleMapsWidgetToken, (Object)false);
        this.allowUrlContext = (Boolean)Utils.getOrDefault((Object)builder.allowUrlContext, (Object)false);
        this.includeCodeExecutionOutput = (Boolean)Utils.getOrDefault((Object)builder.includeCodeExecutionOutput, (Object)false);
        this.safetySettings = Utils.copyIfNotNull(builder.safetySettings);
        this.listeners = Utils.copy(builder.listeners);
        this.thinkingConfig = builder.thinkingConfig;
        this.returnThinking = builder.returnThinking;
        this.sendThinking = (Boolean)Utils.getOrDefault((Object)builder.sendThinking, (Object)false);
        this.seed = builder.seed;
        this.responseLogprobs = (Boolean)Utils.getOrDefault((Object)builder.responseLogprobs, (Object)false);
        this.enableEnhancedCivicAnswers = (Boolean)Utils.getOrDefault((Object)builder.enableEnhancedCivicAnswers, (Object)false);
        this.logprobs = builder.logprobs;
        this.mediaResolution = builder.mediaResolution;
        this.mediaResolutionPerPartEnabled = (Boolean)Utils.getOrDefault((Object)builder.mediaResolutionPerPartEnabled, (Object)false);
        ChatRequestParameters commonParameters = builder.defaultRequestParameters != null ? builder.defaultRequestParameters : DefaultChatRequestParameters.EMPTY;
        GoogleAiGeminiChatRequestParameters geminiParameters = builder.defaultRequestParameters instanceof GoogleAiGeminiChatRequestParameters ? (GoogleAiGeminiChatRequestParameters)builder.defaultRequestParameters : GoogleAiGeminiChatRequestParameters.EMPTY;
        this.defaultRequestParameters = ((GoogleAiGeminiChatRequestParameters.Builder)((GoogleAiGeminiChatRequestParameters.Builder)((GoogleAiGeminiChatRequestParameters.Builder)((GoogleAiGeminiChatRequestParameters.Builder)((GoogleAiGeminiChatRequestParameters.Builder)((GoogleAiGeminiChatRequestParameters.Builder)((GoogleAiGeminiChatRequestParameters.Builder)((GoogleAiGeminiChatRequestParameters.Builder)((GoogleAiGeminiChatRequestParameters.Builder)((GoogleAiGeminiChatRequestParameters.Builder)((GoogleAiGeminiChatRequestParameters.Builder)GoogleAiGeminiChatRequestParameters.builder().modelName((String)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName()))).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature()))).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP()))).topK((Integer)Utils.getOrDefault((Object)builder.topK, (Object)commonParameters.topK()))).frequencyPenalty((Double)Utils.getOrDefault((Object)builder.frequencyPenalty, (Object)commonParameters.frequencyPenalty()))).presencePenalty((Double)Utils.getOrDefault((Object)builder.presencePenalty, (Object)commonParameters.presencePenalty()))).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxOutputTokens, (Object)commonParameters.maxOutputTokens()))).stopSequences(Utils.getOrDefault(builder.stopSequences, (List)commonParameters.stopSequences()))).toolSpecifications(commonParameters.toolSpecifications())).toolChoice((ToolChoice)Utils.getOrDefault((Object)BaseGeminiChatModel.toToolChoice(this.functionCallingConfig), (Object)commonParameters.toolChoice()))).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat()))).aspectRatio((String)Utils.getOrDefault((Object)builder.aspectRatio, (Object)geminiParameters.aspectRatio())).imageSize((String)Utils.getOrDefault((Object)builder.imageSize, (Object)geminiParameters.imageSize())).cachedContentName((String)Utils.getOrDefault((Object)builder.cachedContentName, (Object)geminiParameters.cachedContentName())).build();
    }

    protected static GeminiService buildGeminiService(GoogleAiGeminiChatModelBaseBuilder<?> builder) {
        return new GeminiService(builder.httpClientBuilder, builder.apiKey, builder.baseUrl, (Boolean)Utils.getOrDefault((Object)builder.logRequestsAndResponses, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false), builder.logger, builder.timeout, builder.customHeadersSupplier);
    }

    protected GeminiGenerateContentRequest createGenerateContentRequest(ChatRequest chatRequest) {
        GoogleAiGeminiChatRequestParameters parameters = (GoogleAiGeminiChatRequestParameters)chatRequest.parameters();
        GeminiGenerationConfig.GeminiImageConfig effectiveImageConfig = BaseGeminiChatModel.buildImageConfig(parameters.aspectRatio(), parameters.imageSize());
        GeminiContent systemInstruction = new GeminiContent(Collections.emptyList(), GeminiRole.MODEL.toString());
        List<GeminiContent> geminiContentList = PartsAndContentsMapper.fromMessageToGContent(chatRequest.messages(), systemInstruction, this.sendThinking, this.mediaResolutionPerPartEnabled);
        ResponseFormat responseFormat = chatRequest.responseFormat();
        GeminiSchema schema = null;
        Map rawSchema = null;
        if (responseFormat != null && responseFormat.jsonSchema() != null) {
            if (responseFormat.jsonSchema().rootElement() instanceof JsonRawSchema) {
                JsonRawSchema jsonRawSchema = (JsonRawSchema)responseFormat.jsonSchema().rootElement();
                rawSchema = Json.fromJson(jsonRawSchema.schema(), Map.class);
            } else {
                schema = SchemaMapper.fromJsonSchemaToGSchema(responseFormat.jsonSchema());
            }
        }
        return GeminiGenerateContentRequest.builder().contents(geminiContentList).systemInstruction(!systemInstruction.parts().isEmpty() ? systemInstruction : null).generationConfig(GeminiGenerationConfig.builder().candidateCount(1).maxOutputTokens(parameters.maxOutputTokens()).responseMimeType(BaseGeminiChatModel.computeMimeType(responseFormat)).responseSchema(schema).responseJsonSchema(rawSchema).stopSequences(parameters.stopSequences()).temperature(parameters.temperature()).topK(parameters.topK()).seed(this.seed).topP(parameters.topP()).presencePenalty(parameters.presencePenalty()).frequencyPenalty(parameters.frequencyPenalty()).responseLogprobs(this.responseLogprobs).enableEnhancedCivicAnswers(this.enableEnhancedCivicAnswers).logprobs(this.logprobs).thinkingConfig(this.thinkingConfig).mediaResolution(this.mediaResolution).imageConfig(effectiveImageConfig).build()).safetySettings(this.safetySettings).tools(FunctionMapper.fromToolSpecsToGTools(chatRequest.toolSpecifications(), this.allowCodeExecution, this.allowGoogleSearch, this.allowUrlContext, this.allowGoogleMaps, this.retrieveGoogleMapsWidgetToken)).toolConfig(this.toToolConfig(parameters.toolChoice(), this.functionCallingConfig)).cachedContent(parameters.cachedContentName()).build();
    }

    private GeminiGenerateContentRequest.GeminiToolConfig toToolConfig(ToolChoice toolChoice, GeminiFunctionCallingConfig functionCallingConfig) {
        if (toolChoice == null && functionCallingConfig == null) {
            return null;
        }
        GeminiMode geminiMode = Optional.ofNullable(functionCallingConfig).map(GeminiFunctionCallingConfig::getMode).orElse(null);
        List allowedFunctionNames = Optional.ofNullable(functionCallingConfig).map(GeminiFunctionCallingConfig::getAllowedFunctionNames).orElse(null);
        if (toolChoice != null) {
            geminiMode = BaseGeminiChatModel.toGeminiMode(toolChoice);
        }
        return new GeminiGenerateContentRequest.GeminiToolConfig(new GeminiFunctionCallingConfig(geminiMode, allowedFunctionNames));
    }

    protected static String computeMimeType(ResponseFormat responseFormat) {
        if (responseFormat == null || ResponseFormatType.TEXT.equals((Object)responseFormat.type())) {
            return "text/plain";
        }
        if (ResponseFormatType.JSON.equals((Object)responseFormat.type()) && responseFormat.jsonSchema() != null && responseFormat.jsonSchema().rootElement() != null && responseFormat.jsonSchema().rootElement() instanceof JsonEnumSchema) {
            return "text/x.enum";
        }
        return "application/json";
    }

    private static GeminiMode toGeminiMode(ToolChoice toolChoice) {
        switch (toolChoice) {
            case AUTO: {
                return GeminiMode.AUTO;
            }
            case REQUIRED: {
                return GeminiMode.ANY;
            }
            case NONE: {
                return GeminiMode.NONE;
            }
        }
        throw new IllegalArgumentException("Unknown tool choice: " + toolChoice);
    }

    private static ToolChoice toToolChoice(GeminiFunctionCallingConfig config) {
        if (config == null || config.getMode() == null) {
            return null;
        }
        switch (config.getMode()) {
            case AUTO: {
                return ToolChoice.AUTO;
            }
            case ANY: {
                return ToolChoice.REQUIRED;
            }
            case NONE: 
            case VALIDATED: {
                return null;
            }
        }
        throw new IllegalArgumentException("Unknown mode: " + (Object)((Object)config.getMode()));
    }

    private static GeminiGenerationConfig.GeminiImageConfig buildImageConfig(String aspectRatio, String imageSize) {
        if (aspectRatio == null && imageSize == null) {
            return null;
        }
        return GeminiGenerationConfig.GeminiImageConfig.builder().aspectRatio(aspectRatio).imageSize(imageSize).build();
    }

    protected ChatResponse processResponse(GeminiGenerateContentResponse geminiResponse) {
        GeminiGenerateContentResponse.GeminiCandidate firstCandidate = geminiResponse.candidates().get(0);
        AiMessage aiMessage = this.createAiMessage(firstCandidate);
        FinishReason finishReason = FinishReasonMapper.fromGFinishReasonToFinishReason(firstCandidate.finishReason());
        if (aiMessage != null && aiMessage.hasToolExecutionRequests()) {
            finishReason = FinishReason.TOOL_EXECUTION;
        }
        return ChatResponse.builder().aiMessage(aiMessage).metadata((ChatResponseMetadata)((GoogleAiGeminiChatResponseMetadata.Builder)((GoogleAiGeminiChatResponseMetadata.Builder)((GoogleAiGeminiChatResponseMetadata.Builder)((GoogleAiGeminiChatResponseMetadata.Builder)GoogleAiGeminiChatResponseMetadata.builder().id(geminiResponse.responseId())).modelName(geminiResponse.modelVersion())).tokenUsage(this.createTokenUsage(geminiResponse.usageMetadata()))).finishReason(finishReason)).groundingMetadata(geminiResponse.groundingMetadata() != null ? geminiResponse.groundingMetadata() : firstCandidate.groundingMetadata()).urlContextMetadata(firstCandidate.urlContextMetadata() != null ? this.toUrlContextMetadata(firstCandidate.urlContextMetadata()) : null).build()).build();
    }

    protected AiMessage createAiMessage(GeminiGenerateContentResponse.GeminiCandidate candidate) {
        if (candidate == null || candidate.content() == null) {
            return PartsAndContentsMapper.fromGPartsToAiMessage(Collections.emptyList(), this.includeCodeExecutionOutput, this.returnThinking);
        }
        return PartsAndContentsMapper.fromGPartsToAiMessage(candidate.content().parts(), this.includeCodeExecutionOutput, this.returnThinking);
    }

    protected TokenUsage createTokenUsage(GeminiGenerateContentResponse.GeminiUsageMetadata tokenCounts) {
        return GoogleAiGeminiTokenUsage.builder().inputTokenCount(tokenCounts.promptTokenCount()).outputTokenCount(tokenCounts.candidatesTokenCount()).totalTokenCount(tokenCounts.totalTokenCount()).cachedContentTokenCount(tokenCounts.cachedContentTokenCount()).thoughtsTokenCount(tokenCounts.thoughtsTokenCount()).build();
    }

    private UrlContextMetadata toUrlContextMetadata(GeminiGenerateContentResponse.GeminiUrlContextMetadata geminiUrlContextMetadata) {
        if (geminiUrlContextMetadata == null || geminiUrlContextMetadata.urlMetadata() == null) {
            return null;
        }
        return new UrlContextMetadata(geminiUrlContextMetadata.urlMetadata().stream().map(this::toUrlMetadata).collect(Collectors.toList()));
    }

    private UrlContextMetadata.UrlMetadata toUrlMetadata(GeminiGenerateContentResponse.GeminiUrlMetadata geminiUrlMetadata) {
        if (geminiUrlMetadata == null) {
            return null;
        }
        return new UrlContextMetadata.UrlMetadata(geminiUrlMetadata.retrievedUrl(), geminiUrlMetadata.urlRetrievalStatus() != null ? geminiUrlMetadata.urlRetrievalStatus().toString() : null);
    }

    public static abstract class GoogleAiGeminiChatModelBaseBuilder<B extends GoogleAiGeminiChatModelBaseBuilder<B>> {
        protected HttpClientBuilder httpClientBuilder;
        protected ChatRequestParameters defaultRequestParameters;
        protected String apiKey;
        protected String baseUrl;
        protected String modelName;
        protected Double temperature;
        protected Integer topK;
        protected Integer seed;
        protected Double topP;
        protected Double frequencyPenalty;
        protected Double presencePenalty;
        protected Integer maxOutputTokens;
        protected Duration timeout;
        protected ResponseFormat responseFormat;
        protected List<String> stopSequences;
        protected GeminiFunctionCallingConfig functionCallingConfig;
        protected Boolean allowCodeExecution;
        protected Boolean allowGoogleSearch;
        protected Boolean allowGoogleMaps;
        protected Boolean retrieveGoogleMapsWidgetToken;
        protected Boolean allowUrlContext;
        protected Boolean includeCodeExecutionOutput;
        protected Boolean logRequestsAndResponses;
        protected Boolean logRequests;
        protected Boolean logResponses;
        protected Logger logger;
        protected Boolean responseLogprobs;
        protected Boolean enableEnhancedCivicAnswers;
        protected List<GeminiSafetySetting> safetySettings;
        protected GeminiThinkingConfig thinkingConfig;
        protected Boolean returnThinking;
        protected Boolean sendThinking;
        protected Integer logprobs;
        protected List<ChatModelListener> listeners;
        protected GeminiMediaResolutionLevel mediaResolution;
        protected Boolean mediaResolutionPerPartEnabled;
        protected String aspectRatio;
        protected String imageSize;
        protected String cachedContentName;
        protected Supplier<Map<String, String>> customHeadersSupplier;

        protected B builder() {
            return (B)this;
        }

        public B httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this.builder();
        }

        public B defaultRequestParameters(ChatRequestParameters defaultRequestParameters) {
            this.defaultRequestParameters = defaultRequestParameters;
            return this.builder();
        }

        public B apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this.builder();
        }

        public B baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this.builder();
        }

        public B modelName(String modelName) {
            this.modelName = modelName;
            return this.builder();
        }

        public B timeout(Duration timeout) {
            this.timeout = timeout;
            return this.builder();
        }

        public B listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this.builder();
        }

        public B includeCodeExecutionOutput(Boolean includeCodeExecutionOutput) {
            this.includeCodeExecutionOutput = includeCodeExecutionOutput;
            return this.builder();
        }

        public B logRequestsAndResponses(Boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
            return this.builder();
        }

        public B logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this.builder();
        }

        public B logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this.builder();
        }

        public B logger(Logger logger) {
            this.logger = logger;
            return this.builder();
        }

        public B toolConfig(GeminiFunctionCallingConfig toolConfig) {
            this.functionCallingConfig = toolConfig;
            return this.builder();
        }

        public B toolConfig(GeminiMode mode, String ... allowedFunctionNames) {
            this.functionCallingConfig = new GeminiFunctionCallingConfig(mode, Arrays.asList(allowedFunctionNames));
            return this.builder();
        }

        public B safetySettings(Map<GeminiHarmCategory, GeminiHarmBlockThreshold> safetySettingMap) {
            this.safetySettings = safetySettingMap.entrySet().stream().map(entry -> new GeminiSafetySetting((GeminiHarmCategory)((Object)((Object)entry.getKey())), (GeminiHarmBlockThreshold)((Object)((Object)entry.getValue())))).collect(Collectors.toList());
            return this.builder();
        }

        public B temperature(Double temperature) {
            this.temperature = temperature;
            return this.builder();
        }

        public B topK(Integer topK) {
            this.topK = topK;
            return this.builder();
        }

        public B seed(Integer seed) {
            this.seed = seed;
            return this.builder();
        }

        public B topP(Double topP) {
            this.topP = topP;
            return this.builder();
        }

        public B frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this.builder();
        }

        public B presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this.builder();
        }

        public B maxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
            return this.builder();
        }

        public B responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this.builder();
        }

        public B stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return this.builder();
        }

        public B allowCodeExecution(Boolean allowCodeExecution) {
            this.allowCodeExecution = allowCodeExecution;
            return this.builder();
        }

        public B allowGoogleSearch(Boolean allowGoogleSearch) {
            this.allowGoogleSearch = allowGoogleSearch;
            return this.builder();
        }

        public B allowGoogleMaps(Boolean allowGoogleMaps) {
            this.allowGoogleMaps = allowGoogleMaps;
            return this.builder();
        }

        public B retrieveGoogleMapsWidgetToken(Boolean retrieveGoogleMapsWidgetToken) {
            this.retrieveGoogleMapsWidgetToken = retrieveGoogleMapsWidgetToken;
            return this.builder();
        }

        public B allowUrlContext(Boolean allowUrlContext) {
            this.allowUrlContext = allowUrlContext;
            return this.builder();
        }

        public B safetySettings(List<GeminiSafetySetting> safetySettings) {
            this.safetySettings = safetySettings;
            return this.builder();
        }

        public B thinkingConfig(GeminiThinkingConfig thinkingConfig) {
            this.thinkingConfig = thinkingConfig;
            return this.builder();
        }

        public B returnThinking(Boolean returnThinking) {
            this.returnThinking = returnThinking;
            return this.builder();
        }

        public B sendThinking(Boolean sendThinking) {
            this.sendThinking = sendThinking;
            return this.builder();
        }

        public B responseLogprobs(Boolean responseLogprobs) {
            this.responseLogprobs = responseLogprobs;
            return this.builder();
        }

        public B logprobs(Integer logprobs) {
            this.logprobs = logprobs;
            return this.builder();
        }

        public B enableEnhancedCivicAnswers(Boolean enableEnhancedCivicAnswers) {
            this.enableEnhancedCivicAnswers = enableEnhancedCivicAnswers;
            return this.builder();
        }

        public B mediaResolution(GeminiMediaResolutionLevel mediaResolution) {
            this.mediaResolution = mediaResolution;
            return this.builder();
        }

        public B mediaResolutionPerPartEnabled(Boolean mediaResolutionPerPartEnabled) {
            this.mediaResolutionPerPartEnabled = mediaResolutionPerPartEnabled;
            return this.builder();
        }

        public B aspectRatio(String aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this.builder();
        }

        public B imageAspectRatio(String imageAspectRatio) {
            return this.aspectRatio(imageAspectRatio);
        }

        public B imageSize(String imageSize) {
            this.imageSize = imageSize;
            return this.builder();
        }

        public B cachedContentName(String cachedContentName) {
            this.cachedContentName = cachedContentName;
            return this.builder();
        }

        public B customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this.builder();
        }

        public B customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this.builder();
        }
    }
}

