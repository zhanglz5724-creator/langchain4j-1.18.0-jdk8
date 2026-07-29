/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.genai.Client
 *  com.google.genai.ResponseStream
 *  com.google.genai.types.Content
 *  com.google.genai.types.GenerateContentConfig
 *  com.google.genai.types.GenerateContentConfig$Builder
 *  com.google.genai.types.GenerateContentResponse
 *  com.google.genai.types.SafetySetting
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.internal.DefaultExecutorProvider
 *  dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils
 *  dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.google.genai;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.genai.Client;
import com.google.genai.ResponseStream;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.SafetySetting;
import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.internal.DefaultExecutorProvider;
import dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils;
import dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.google.genai.GoogleGenAiChatRequestParameters;
import dev.langchain4j.model.google.genai.GoogleGenAiChatResponseMetadata;
import dev.langchain4j.model.google.genai.GoogleGenAiClientFactory;
import dev.langchain4j.model.google.genai.GoogleGenAiConfigBuilder;
import dev.langchain4j.model.google.genai.GoogleGenAiContentMapper;
import dev.langchain4j.model.google.genai.GoogleGenAiExceptionMapper;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Experimental
public class GoogleGenAiStreamingChatModel
implements StreamingChatModel {
    private static final Logger log = LoggerFactory.getLogger(GoogleGenAiStreamingChatModel.class);
    private final Client client;
    private final List<ChatModelListener> listeners;
    private final ChatRequestParameters defaultRequestParameters;
    private final boolean logRequests;
    private final boolean logResponses;
    private final List<SafetySetting> safetySettings;
    private final Integer thinkingBudget;
    private final String thinkingLevel;
    private final Integer seed;
    private final boolean googleSearchEnabled;
    private final boolean googleMapsEnabled;
    private final boolean urlContextEnabled;
    private final List<String> allowedFunctionNames;
    private final String vertexSearchDatastore;
    private final Map<String, String> labels;
    private final Consumer<GenerateContentConfig.Builder> generateContentConfigCustomizer;
    private final ExecutorService executor;

    private GoogleGenAiStreamingChatModel(Builder builder) {
        this.listeners = Utils.copy((List)builder.listeners);
        this.logRequests = (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false);
        this.logResponses = (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false);
        this.googleSearchEnabled = (Boolean)Utils.getOrDefault((Object)builder.googleSearch, (Object)false);
        this.googleMapsEnabled = (Boolean)Utils.getOrDefault((Object)builder.googleMaps, (Object)false);
        this.urlContextEnabled = (Boolean)Utils.getOrDefault((Object)builder.urlContext, (Object)false);
        this.allowedFunctionNames = Utils.copy((List)builder.allowedFunctionNames);
        this.thinkingBudget = builder.thinkingBudget;
        this.thinkingLevel = builder.thinkingLevel;
        this.seed = builder.seed;
        this.safetySettings = Utils.copy((List)builder.safetySettings);
        this.vertexSearchDatastore = builder.vertexSearchDatastore;
        this.labels = builder.labels != null ? new HashMap(builder.labels) : null;
        this.generateContentConfigCustomizer = builder.generateContentConfigCustomizer;
        this.client = builder.client != null ? builder.client : GoogleGenAiClientFactory.createClient(builder.apiKey, builder.googleCredentials, builder.projectId, builder.location, builder.timeout, builder.customHeaders, builder.apiEndpoint);
        ChatRequestParameters commonParameters = (ChatRequestParameters)Utils.getOrDefault((Object)builder.defaultRequestParameters, (Object)DefaultChatRequestParameters.EMPTY);
        GoogleGenAiChatRequestParameters genAiParameters = builder.defaultRequestParameters instanceof GoogleGenAiChatRequestParameters ? (GoogleGenAiChatRequestParameters)builder.defaultRequestParameters : GoogleGenAiChatRequestParameters.EMPTY;
        this.defaultRequestParameters = ((GoogleGenAiChatRequestParameters.Builder)((GoogleGenAiChatRequestParameters.Builder)((GoogleGenAiChatRequestParameters.Builder)((GoogleGenAiChatRequestParameters.Builder)((GoogleGenAiChatRequestParameters.Builder)((GoogleGenAiChatRequestParameters.Builder)((GoogleGenAiChatRequestParameters.Builder)((GoogleGenAiChatRequestParameters.Builder)((GoogleGenAiChatRequestParameters.Builder)((GoogleGenAiChatRequestParameters.Builder)((GoogleGenAiChatRequestParameters.Builder)GoogleGenAiChatRequestParameters.builder().modelName((String)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName()))).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature()))).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP()))).topK((Integer)Utils.getOrDefault((Object)builder.topK, (Object)commonParameters.topK()))).frequencyPenalty((Double)Utils.getOrDefault((Object)builder.frequencyPenalty, (Object)commonParameters.frequencyPenalty()))).presencePenalty((Double)Utils.getOrDefault((Object)builder.presencePenalty, (Object)commonParameters.presencePenalty()))).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxOutputTokens, (Object)commonParameters.maxOutputTokens()))).stopSequences(Utils.getOrDefault((List)builder.stopSequences, (List)commonParameters.stopSequences()))).toolSpecifications(commonParameters.toolSpecifications())).toolChoice(commonParameters.toolChoice())).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat()))).cachedContent((String)Utils.getOrDefault((Object)builder.cachedContent, (Object)genAiParameters.cachedContent())).build();
        this.executor = (ExecutorService)Utils.getOrDefault((Object)builder.executor, DefaultExecutorProvider::getDefaultExecutorService);
    }

    public void doChat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
        String modelName = chatRequest.modelName();
        Content systemInstruction = GoogleGenAiContentMapper.toSystemInstruction(chatRequest.messages());
        List<Content> contents = GoogleGenAiContentMapper.toContents(chatRequest.messages());
        GoogleGenAiChatRequestParameters parameters = (GoogleGenAiChatRequestParameters)chatRequest.parameters();
        GenerateContentConfig config = GoogleGenAiConfigBuilder.buildConfig((ChatRequestParameters)parameters, systemInstruction, this.safetySettings, this.thinkingBudget, this.thinkingLevel, this.seed, this.googleSearchEnabled, this.googleMapsEnabled, this.urlContextEnabled, this.allowedFunctionNames, this.vertexSearchDatastore, this.labels, parameters.cachedContent(), this.generateContentConfigCustomizer);
        if (this.logRequests) {
            log.info("Request:\n- model: {}\n- messages: {}\n- config: {}", new Object[]{chatRequest.modelName(), chatRequest.messages(), config});
        }
        MappingTrackingStreamingChatResponseHandler trackingHandler = new MappingTrackingStreamingChatResponseHandler(handler);
        this.executor.execute(() -> {
            try {
                ResponseStream<GenerateContentResponse> stream = this.client.models.generateContentStream(modelName, contents, config);
                StringBuilder textBuilder = new StringBuilder();
                ArrayList<ToolExecutionRequest> toolRequests = new ArrayList<ToolExecutionRequest>();
                HashMap attributes = new HashMap();
                TokenUsage tokenUsage = new TokenUsage();
                FinishReason finishReason = null;
                GenerateContentResponse lastChunk = null;
                int toolIndex = 0;
                for (GenerateContentResponse chunk : stream) {
                    FinishReason partialReason;
                    trackingHandler.resetMappingTracking();
                    lastChunk = chunk;
                    ChatResponse partialResponse = GoogleGenAiContentMapper.toChatResponse(chunk, modelName);
                    AiMessage aiMessage = partialResponse.aiMessage();
                    if (aiMessage.attributes() != null && !aiMessage.attributes().isEmpty()) {
                        attributes.putAll(aiMessage.attributes());
                    }
                    if (aiMessage.text() != null && !aiMessage.text().isEmpty()) {
                        textBuilder.append(aiMessage.text());
                        try {
                            trackingHandler.onPartialResponse(aiMessage.text());
                        }
                        catch (Exception userException) {
                            trackingHandler.onError((Throwable)userException);
                        }
                    }
                    if (aiMessage.toolExecutionRequests() != null) {
                        for (ToolExecutionRequest req : aiMessage.toolExecutionRequests()) {
                            toolRequests.add(req);
                            try {
                                trackingHandler.onCompleteToolCall(new CompleteToolCall(toolIndex++, req));
                            }
                            catch (Exception userException) {
                                trackingHandler.onError((Throwable)userException);
                            }
                        }
                    }
                    if (partialResponse.tokenUsage() != null) {
                        tokenUsage = partialResponse.tokenUsage();
                    }
                    if ((partialReason = partialResponse.finishReason()) != null && partialReason != FinishReason.OTHER && finishReason != FinishReason.LENGTH && finishReason != FinishReason.CONTENT_FILTER) {
                        finishReason = partialReason;
                    }
                    if (trackingHandler.wasMapped()) continue;
                    InternalStreamingChatResponseHandlerUtils.onUnmappedRawEvent((StreamingChatResponseHandler)trackingHandler, (Object)chunk);
                }
                AiMessage finalAiMessage = !toolRequests.isEmpty() && textBuilder.length() > 0 ? new AiMessage(textBuilder.toString(), toolRequests) : (!toolRequests.isEmpty() ? AiMessage.from(toolRequests) : AiMessage.from((String)textBuilder.toString()));
                if (!attributes.isEmpty()) {
                    finalAiMessage = finalAiMessage.toBuilder().attributes(attributes).build();
                }
                GoogleGenAiChatResponseMetadata metadata = ((GoogleGenAiChatResponseMetadata.Builder)((GoogleGenAiChatResponseMetadata.Builder)((GoogleGenAiChatResponseMetadata.Builder)GoogleGenAiChatResponseMetadata.builder().modelName(modelName)).tokenUsage(tokenUsage)).finishReason(!toolRequests.isEmpty() ? FinishReason.TOOL_EXECUTION : (finishReason != null ? finishReason : FinishReason.STOP))).rawResponse(lastChunk).build();
                ChatResponse finalChatResponse = ChatResponse.builder().aiMessage(finalAiMessage).metadata((ChatResponseMetadata)metadata).build();
                if (this.logResponses) {
                    log.info("Response:\n- model: {}\n- response: {}", (Object)modelName, (Object)finalChatResponse);
                }
                trackingHandler.onCompleteResponse(finalChatResponse);
            }
            catch (Exception e) {
                trackingHandler.onError((Throwable)GoogleGenAiExceptionMapper.INSTANCE.mapException(e));
            }
        });
    }

    public ChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.GOOGLE_GENAI;
    }

    public Set<Capability> supportedCapabilities() {
        return Collections.singleton(Capability.RESPONSE_FORMAT_JSON_SCHEMA);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Client client;
        private GoogleCredentials googleCredentials;
        private String apiKey;
        private String projectId;
        private String location;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Double frequencyPenalty;
        private Double presencePenalty;
        private Integer topK;
        private Integer maxOutputTokens;
        private Integer thinkingBudget;
        private Integer seed;
        private String thinkingLevel;
        private List<String> stopSequences;
        private Duration timeout;
        private Boolean googleSearch;
        private Boolean googleMaps;
        private Boolean urlContext;
        private List<SafetySetting> safetySettings;
        private ResponseFormat responseFormat;
        private List<String> allowedFunctionNames;
        private List<ChatModelListener> listeners;
        private ExecutorService executor;
        private ChatRequestParameters defaultRequestParameters;
        private String vertexSearchDatastore;
        private Map<String, String> labels;
        private String apiEndpoint;
        private Map<String, String> customHeaders;
        private String cachedContent;
        private Boolean logRequests;
        private Boolean logResponses;
        private Consumer<GenerateContentConfig.Builder> generateContentConfigCustomizer;

        public Builder client(Client client) {
            this.client = client;
            return this;
        }

        public Builder googleCredentials(GoogleCredentials credentials) {
            this.googleCredentials = credentials;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
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

        public Builder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public Builder frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public Builder presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public Builder maxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
            return this;
        }

        public Builder thinkingBudget(Integer thinkingBudget) {
            this.thinkingBudget = thinkingBudget;
            return this;
        }

        public Builder thinkingLevel(String thinkingLevel) {
            this.thinkingLevel = thinkingLevel;
            return this;
        }

        public Builder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public Builder stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return this;
        }

        public Builder safetySettings(List<SafetySetting> safetySettings) {
            this.safetySettings = safetySettings;
            return this;
        }

        public Builder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public Builder enableGoogleSearch(boolean googleSearch) {
            this.googleSearch = googleSearch;
            return this;
        }

        public Builder enableGoogleMaps(boolean googleMaps) {
            this.googleMaps = googleMaps;
            return this;
        }

        public Builder enableUrlContext(boolean urlContext) {
            this.urlContext = urlContext;
            return this;
        }

        public Builder allowedFunctionNames(List<String> allowedFunctionNames) {
            this.allowedFunctionNames = allowedFunctionNames;
            return this;
        }

        public Builder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public Builder executor(ExecutorService executor) {
            this.executor = executor;
            return this;
        }

        public Builder defaultRequestParameters(ChatRequestParameters defaultRequestParameters) {
            this.defaultRequestParameters = defaultRequestParameters;
            return this;
        }

        public Builder cachedContent(String cachedContent) {
            this.cachedContent = cachedContent;
            return this;
        }

        public Builder vertexSearchDatastore(String vertexSearchDatastore) {
            this.vertexSearchDatastore = vertexSearchDatastore;
            return this;
        }

        public Builder labels(Map<String, String> labels) {
            this.labels = labels;
            return this;
        }

        public Builder apiEndpoint(String apiEndpoint) {
            this.apiEndpoint = apiEndpoint;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public Builder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public Builder logRequestsAndResponses(Boolean logRequestsAndResponses) {
            this.logRequests = logRequestsAndResponses;
            this.logResponses = logRequestsAndResponses;
            return this;
        }

        public Builder generateContentConfigCustomizer(Consumer<GenerateContentConfig.Builder> generateContentConfigCustomizer) {
            this.generateContentConfigCustomizer = generateContentConfigCustomizer;
            return this;
        }

        public GoogleGenAiStreamingChatModel build() {
            return new GoogleGenAiStreamingChatModel(this);
        }
    }
}

