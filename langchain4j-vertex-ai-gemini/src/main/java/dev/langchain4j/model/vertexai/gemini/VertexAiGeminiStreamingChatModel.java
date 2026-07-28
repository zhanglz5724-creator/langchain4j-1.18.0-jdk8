/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.auth.Credentials
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.cloud.vertexai.VertexAI
 *  com.google.cloud.vertexai.VertexAI$Builder
 *  com.google.cloud.vertexai.api.FunctionCall
 *  com.google.cloud.vertexai.api.FunctionCallingConfig
 *  com.google.cloud.vertexai.api.FunctionCallingConfig$Mode
 *  com.google.cloud.vertexai.api.GenerateContentRequest
 *  com.google.cloud.vertexai.api.GenerateContentResponse
 *  com.google.cloud.vertexai.api.GenerationConfig
 *  com.google.cloud.vertexai.api.GenerationConfig$Builder
 *  com.google.cloud.vertexai.api.Schema
 *  com.google.cloud.vertexai.api.Tool
 *  com.google.cloud.vertexai.api.ToolConfig
 *  com.google.cloud.vertexai.generativeai.GenerativeModel
 *  com.google.cloud.vertexai.generativeai.ResponseStream
 *  com.google.common.annotations.VisibleForTesting
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.internal.ChatRequestValidationUtils
 *  dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelErrorContext
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.listener.ChatModelRequestContext
 *  dev.langchain4j.model.chat.listener.ChatModelResponseContext
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.vertexai.gemini;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.FunctionCall;
import com.google.cloud.vertexai.api.FunctionCallingConfig;
import com.google.cloud.vertexai.api.GenerateContentRequest;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.Schema;
import com.google.cloud.vertexai.api.Tool;
import com.google.cloud.vertexai.api.ToolConfig;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import com.google.common.annotations.VisibleForTesting;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.internal.ChatRequestValidationUtils;
import dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.StreamingHandle;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.gemini.ContentsMapper;
import dev.langchain4j.model.vertexai.gemini.FunctionCallHelper;
import dev.langchain4j.model.vertexai.gemini.HarmCategory;
import dev.langchain4j.model.vertexai.gemini.ResponseGrounding;
import dev.langchain4j.model.vertexai.gemini.SafetySettingsMapper;
import dev.langchain4j.model.vertexai.gemini.SafetyThreshold;
import dev.langchain4j.model.vertexai.gemini.StreamingChatResponseBuilder;
import dev.langchain4j.model.vertexai.gemini.ToolCallingMode;
import dev.langchain4j.model.vertexai.gemini.VertexAiGeminiChatModel;
import dev.langchain4j.model.vertexai.gemini.VertexAiGeminiExceptionMapper;
import dev.langchain4j.model.vertexai.gemini.VertexAiGeminiStreamingHandle;
import dev.langchain4j.model.vertexai.gemini.spi.VertexAiGeminiStreamingChatModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertexAiGeminiStreamingChatModel
implements StreamingChatModel,
Closeable {
    private static final Logger logger = LoggerFactory.getLogger(VertexAiGeminiStreamingChatModel.class);
    private final GenerativeModel generativeModel;
    private final GenerationConfig generationConfig;
    private final VertexAI vertexAI;
    private final Map<HarmCategory, SafetyThreshold> safetySettings;
    private final Tool googleSearch;
    private final Tool vertexSearch;
    private final ToolConfig toolConfig;
    private final List<String> allowedFunctionNames;
    private final Boolean logRequests;
    private final Boolean logResponses;
    private final List<ChatModelListener> listeners;
    private final Executor executor;
    private final Map<String, String> labels;

    public VertexAiGeminiStreamingChatModel(VertexAiGeminiStreamingChatModelBuilder builder) {
        Map<String, String> headers;
        ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        GenerationConfig.Builder generationConfigBuilder = GenerationConfig.newBuilder();
        if (builder.temperature != null) {
            generationConfigBuilder.setTemperature(builder.temperature.floatValue());
        }
        if (builder.maxOutputTokens != null) {
            generationConfigBuilder.setMaxOutputTokens(builder.maxOutputTokens.intValue());
        }
        if (builder.topK != null) {
            generationConfigBuilder.setTopK((float)builder.topK.intValue());
        }
        if (builder.topP != null) {
            generationConfigBuilder.setTopP(builder.topP.floatValue());
        }
        if (builder.responseMimeType != null) {
            generationConfigBuilder.setResponseMimeType(builder.responseMimeType);
        }
        if (builder.responseSchema != null) {
            if (builder.responseSchema.getEnumCount() > 0) {
                generationConfigBuilder.setResponseMimeType("text/x.enum");
            } else {
                generationConfigBuilder.setResponseMimeType("application/json");
            }
            generationConfigBuilder.setResponseSchema(builder.responseSchema);
        }
        this.generationConfig = generationConfigBuilder.build();
        this.safetySettings = Utils.copy((Map)builder.safetySettings);
        this.googleSearch = builder.useGoogleSearch != null && builder.useGoogleSearch != false ? ResponseGrounding.googleSearchTool(builder.modelName) : null;
        this.vertexSearch = builder.vertexSearchDatastore != null ? ResponseGrounding.vertexAiSearch(builder.vertexSearchDatastore) : null;
        this.allowedFunctionNames = builder.allowedFunctionNames != null ? Collections.unmodifiableList(builder.allowedFunctionNames) : Collections.emptyList();
        this.toolConfig = builder.toolCallingMode != null ? (builder.toolCallingMode == ToolCallingMode.ANY && !this.allowedFunctionNames.isEmpty() ? ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.ANY).addAllAllowedFunctionNames(this.allowedFunctionNames).build()).build() : (builder.toolCallingMode == ToolCallingMode.NONE ? ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.NONE).build()).build() : ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.AUTO).build()).build())) : ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.AUTO).build()).build();
        if (builder.customHeaders != null) {
            headers = new HashMap<String, String>(builder.customHeaders);
            headers.putIfAbsent("user-agent", "LangChain4j");
        } else {
            headers = Collections.singletonMap("user-agent", "LangChain4j");
        }
        VertexAI.Builder vertexAiBuilder = new VertexAI.Builder().setProjectId(ValidationUtils.ensureNotBlank((String)builder.project, (String)"project")).setLocation(ValidationUtils.ensureNotBlank((String)builder.location, (String)"location")).setCustomHeaders(headers);
        if (builder.credentials != null) {
            GoogleCredentials scopedCredentials = builder.credentials.createScoped(new String[]{"https://www.googleapis.com/auth/cloud-platform"});
            vertexAiBuilder.setCredentials((Credentials)scopedCredentials);
        }
        if (builder.apiEndpoint != null) {
            vertexAiBuilder.setApiEndpoint(builder.apiEndpoint);
        }
        this.vertexAI = vertexAiBuilder.build();
        this.generativeModel = new GenerativeModel(builder.modelName, this.vertexAI).withGenerationConfig(this.generationConfig);
        this.logRequests = (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false);
        this.logResponses = (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false);
        this.listeners = Utils.copy((List)builder.listeners);
        this.executor = (Executor)Utils.getOrDefault((Object)builder.executor, VertexAiGeminiStreamingChatModel::createDefaultExecutor);
        this.labels = Utils.copy((Map)builder.labels);
    }

    @Deprecated
    public VertexAiGeminiStreamingChatModel(String project, String location, String modelName, Float temperature, Integer maxOutputTokens, Integer topK, Float topP, String responseMimeType, Schema responseSchema, Map<HarmCategory, SafetyThreshold> safetySettings, Boolean useGoogleSearch, String vertexSearchDatastore, ToolCallingMode toolCallingMode, List<String> allowedFunctionNames, Boolean logRequests, Boolean logResponses, List<ChatModelListener> listeners, Map<String, String> customHeaders, Executor executor) {
        Map<String, String> headers;
        ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        GenerationConfig.Builder generationConfigBuilder = GenerationConfig.newBuilder();
        if (temperature != null) {
            generationConfigBuilder.setTemperature(temperature.floatValue());
        }
        if (maxOutputTokens != null) {
            generationConfigBuilder.setMaxOutputTokens(maxOutputTokens.intValue());
        }
        if (topK != null) {
            generationConfigBuilder.setTopK((float)topK.intValue());
        }
        if (topP != null) {
            generationConfigBuilder.setTopP(topP.floatValue());
        }
        if (responseMimeType != null) {
            generationConfigBuilder.setResponseMimeType(responseMimeType);
        }
        if (responseSchema != null) {
            if (responseSchema.getEnumCount() > 0) {
                generationConfigBuilder.setResponseMimeType("text/x.enum");
            } else {
                generationConfigBuilder.setResponseMimeType("application/json");
            }
            generationConfigBuilder.setResponseSchema(responseSchema);
        }
        this.generationConfig = generationConfigBuilder.build();
        this.safetySettings = safetySettings != null ? new HashMap<HarmCategory, SafetyThreshold>(safetySettings) : Collections.emptyMap();
        this.googleSearch = useGoogleSearch != null && useGoogleSearch != false ? ResponseGrounding.googleSearchTool(modelName) : null;
        this.vertexSearch = vertexSearchDatastore != null ? ResponseGrounding.vertexAiSearch(vertexSearchDatastore) : null;
        this.allowedFunctionNames = allowedFunctionNames != null ? Collections.unmodifiableList(allowedFunctionNames) : Collections.emptyList();
        this.toolConfig = toolCallingMode != null ? (toolCallingMode == ToolCallingMode.ANY && allowedFunctionNames != null && !allowedFunctionNames.isEmpty() ? ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.ANY).addAllAllowedFunctionNames(this.allowedFunctionNames).build()).build() : (toolCallingMode == ToolCallingMode.NONE ? ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.NONE).build()).build() : ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.AUTO).build()).build())) : ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.AUTO).build()).build();
        if (customHeaders != null) {
            headers = new HashMap<String, String>(customHeaders);
            headers.putIfAbsent("user-agent", "LangChain4j");
        } else {
            headers = Collections.singletonMap("user-agent", "LangChain4j");
        }
        this.vertexAI = new VertexAI.Builder().setProjectId(ValidationUtils.ensureNotBlank((String)project, (String)"project")).setLocation(ValidationUtils.ensureNotBlank((String)location, (String)"location")).setCustomHeaders(headers).build();
        this.generativeModel = new GenerativeModel(modelName, this.vertexAI).withGenerationConfig(this.generationConfig);
        this.logRequests = logRequests != null ? logRequests : false;
        this.logResponses = logResponses != null ? logResponses : false;
        this.listeners = listeners == null ? Collections.emptyList() : new ArrayList<ChatModelListener>(listeners);
        this.executor = (Executor)Utils.getOrDefault((Object)executor, VertexAiGeminiStreamingChatModel::createDefaultExecutor);
        this.labels = Collections.emptyMap();
    }

    public VertexAiGeminiStreamingChatModel(GenerativeModel generativeModel, GenerationConfig generationConfig) {
        this.generativeModel = (GenerativeModel)ValidationUtils.ensureNotNull((Object)generativeModel, (String)"generativeModel");
        this.generationConfig = (GenerationConfig)ValidationUtils.ensureNotNull((Object)generationConfig, (String)"generationConfig");
        this.vertexAI = null;
        this.safetySettings = Collections.emptyMap();
        this.googleSearch = null;
        this.vertexSearch = null;
        this.toolConfig = ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.AUTO).build()).build();
        this.allowedFunctionNames = Collections.emptyList();
        this.logRequests = false;
        this.logResponses = false;
        this.listeners = Collections.emptyList();
        this.executor = VertexAiGeminiStreamingChatModel.createDefaultExecutor();
        this.labels = Collections.emptyMap();
    }

    public VertexAiGeminiStreamingChatModel(GenerativeModel generativeModel, GenerationConfig generationConfig, Executor executor) {
        this.generativeModel = (GenerativeModel)ValidationUtils.ensureNotNull((Object)generativeModel, (String)"generativeModel");
        this.generationConfig = (GenerationConfig)ValidationUtils.ensureNotNull((Object)generationConfig, (String)"generationConfig");
        this.vertexAI = null;
        this.safetySettings = Collections.emptyMap();
        this.googleSearch = null;
        this.vertexSearch = null;
        this.toolConfig = ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.AUTO).build()).build();
        this.allowedFunctionNames = Collections.emptyList();
        this.logRequests = false;
        this.logResponses = false;
        this.listeners = Collections.emptyList();
        this.executor = (Executor)Utils.getOrDefault((Object)executor, VertexAiGeminiStreamingChatModel::createDefaultExecutor);
        this.labels = Collections.emptyMap();
    }

    private static ExecutorService createDefaultExecutor() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 1L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    public void chat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
        ChatRequestParameters parameters = chatRequest.parameters();
        ChatRequestValidationUtils.validateParameters((ChatRequestParameters)parameters);
        ChatRequestValidationUtils.validate((ToolChoice)parameters.toolChoice());
        ChatRequestValidationUtils.validate((ResponseFormat)parameters.responseFormat());
        List toolSpecifications = parameters.toolSpecifications();
        if (Utils.isNullOrEmpty((Collection)toolSpecifications)) {
            this.generate(chatRequest.messages(), handler);
        } else {
            this.generate(chatRequest.messages(), toolSpecifications, handler);
        }
    }

    private void generate(List<ChatMessage> messages, StreamingChatResponseHandler handler) {
        this.generate(messages, Collections.emptyList(), handler);
    }

    private void generate(List<ChatMessage> messages, List<ToolSpecification> toolSpecifications, StreamingChatResponseHandler handler) {
        String modelName = this.generativeModel.getModelName();
        ArrayList<Tool> tools = new ArrayList<Tool>();
        if (toolSpecifications != null && !toolSpecifications.isEmpty()) {
            Tool tool = FunctionCallHelper.convertToolSpecifications(toolSpecifications);
            tools.add(tool);
        }
        if (this.googleSearch != null) {
            tools.add(this.googleSearch);
        }
        if (this.vertexSearch != null) {
            tools.add(this.vertexSearch);
        }
        GenerativeModel model = this.generativeModel.withTools(tools).withToolConfig(this.toolConfig);
        ContentsMapper.InstructionAndContent instructionAndContent = ContentsMapper.splitInstructionAndContent(messages);
        if (instructionAndContent.systemInstruction != null) {
            model = model.withSystemInstruction(instructionAndContent.systemInstruction);
        }
        if (!this.safetySettings.isEmpty()) {
            model = model.withSafetySettings(SafetySettingsMapper.mapSafetySettings(this.safetySettings));
        }
        if (this.logRequests.booleanValue() && logger.isDebugEnabled()) {
            logger.debug("GEMINI ({}) request: {} tools: {}", new Object[]{modelName, instructionAndContent, tools});
        }
        ChatRequest listenerRequest = ChatRequest.builder().messages(messages).parameters(ChatRequestParameters.builder().modelName(modelName).temperature(Double.valueOf(this.generationConfig.getTemperature())).topP(Double.valueOf(this.generationConfig.getTopP())).maxOutputTokens(Integer.valueOf(this.generationConfig.getMaxOutputTokens())).toolSpecifications(toolSpecifications).build()).build();
        ConcurrentHashMap listenerAttributes = new ConcurrentHashMap();
        ChatModelRequestContext chatModelRequestContext = new ChatModelRequestContext(listenerRequest, this.provider(), listenerAttributes);
        this.listeners.forEach(listener -> {
            try {
                listener.onRequest(chatModelRequestContext);
            }
            catch (Exception e) {
                logger.warn("Exception while calling model listener (onRequest)", (Throwable)e);
            }
        });
        StreamingChatResponseBuilder responseBuilder = new StreamingChatResponseBuilder();
        GenerativeModel finalModel = model;
        AtomicInteger toolIndex = new AtomicInteger(0);
        VertexAiGeminiStreamingHandle streamingHandle = new VertexAiGeminiStreamingHandle();
        this.executor.execute(() -> {
            try {
                ResponseStream responseStream;
                if (this.labels.isEmpty()) {
                    responseStream = finalModel.generateContentStream(instructionAndContent.contents);
                } else {
                    GenerateContentRequest request = VertexAiGeminiChatModel.buildGenerateContentRequest(finalModel, this.vertexAI, instructionAndContent.contents, this.labels);
                    responseStream = this.vertexAI.getPredictionServiceClient().streamGenerateContentCallable().call((Object)request);
                }
                responseStream.forEach(partialResponse -> {
                    if (streamingHandle.isCancelled()) {
                        return;
                    }
                    if (partialResponse.getCandidatesCount() > 0) {
                        StreamingChatResponseBuilder.TextAndFunctions textAndFunctions = responseBuilder.append((GenerateContentResponse)partialResponse);
                        String text = textAndFunctions.text;
                        if (Utils.isNotNullOrEmpty((String)text)) {
                            InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)handler, (String)text, (StreamingHandle)streamingHandle);
                        }
                        for (FunctionCall functionCall : textAndFunctions.functionCalls) {
                            int index = toolIndex.get();
                            ToolExecutionRequest toolExecutionRequest = FunctionCallHelper.fromFunctionCall(index, functionCall);
                            CompleteToolCall completeToolCall = new CompleteToolCall(index, toolExecutionRequest);
                            InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)handler, (CompleteToolCall)completeToolCall);
                            toolIndex.incrementAndGet();
                        }
                    }
                });
                if (streamingHandle.isCancelled()) {
                    return;
                }
                Response<AiMessage> fullResponse = responseBuilder.build();
                ChatResponse chatResponse = ChatResponse.builder().aiMessage((AiMessage)fullResponse.content()).metadata(ChatResponseMetadata.builder().tokenUsage(fullResponse.tokenUsage()).finishReason(fullResponse.finishReason()).build()).build();
                InternalStreamingChatResponseHandlerUtils.onCompleteResponse((StreamingChatResponseHandler)handler, (ChatResponse)chatResponse);
                ChatResponse listenerResponse = ChatResponse.builder().aiMessage((AiMessage)fullResponse.content()).metadata(ChatResponseMetadata.builder().modelName(modelName).tokenUsage(fullResponse.tokenUsage()).finishReason(fullResponse.finishReason()).build()).build();
                ChatModelResponseContext chatModelResponseContext = new ChatModelResponseContext(listenerResponse, listenerRequest, this.provider(), (Map)listenerAttributes);
                this.listeners.forEach(listener -> {
                    try {
                        listener.onResponse(chatModelResponseContext);
                    }
                    catch (Exception e) {
                        logger.warn("Exception while calling model listener (onResponse)", (Throwable)e);
                    }
                });
                if (this.logResponses.booleanValue() && logger.isDebugEnabled()) {
                    logger.debug("GEMINI ({}) response: {}", (Object)modelName, fullResponse);
                }
            }
            catch (Exception exception) {
                RuntimeException mappedException = VertexAiGeminiExceptionMapper.INSTANCE.mapException(exception);
                this.listeners.forEach(listener -> {
                    try {
                        ChatModelErrorContext chatModelErrorContext = new ChatModelErrorContext((Throwable)mappedException, listenerRequest, this.provider(), (Map)listenerAttributes);
                        listener.onError(chatModelErrorContext);
                    }
                    catch (Exception t) {
                        logger.warn("Exception while calling model listener (onError)", (Throwable)t);
                    }
                });
                handler.onError((Throwable)mappedException);
            }
        });
    }

    @VisibleForTesting
    VertexAI vertexAI() {
        return this.vertexAI;
    }

    @VisibleForTesting
    Map<String, String> labels() {
        return this.labels;
    }

    @Override
    public void close() {
        if (this.vertexAI != null) {
            this.vertexAI.close();
        }
        if (this.executor instanceof ExecutorService) {
            ((ExecutorService)this.executor).shutdown();
        }
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.GOOGLE_VERTEX_AI_GEMINI;
    }

    public static VertexAiGeminiStreamingChatModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(VertexAiGeminiStreamingChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            VertexAiGeminiStreamingChatModelBuilderFactory factory = (VertexAiGeminiStreamingChatModelBuilderFactory)iterator.next();
            return (VertexAiGeminiStreamingChatModelBuilder)factory.get();
        }
        return new VertexAiGeminiStreamingChatModelBuilder();
    }

    public static class VertexAiGeminiStreamingChatModelBuilder {
        private Executor executor;
        private String project;
        private String location;
        private String modelName;
        private Float temperature;
        private Integer maxOutputTokens;
        private Integer topK;
        private Float topP;
        private String responseMimeType;
        private Schema responseSchema;
        private Map<HarmCategory, SafetyThreshold> safetySettings;
        private Boolean useGoogleSearch;
        private String vertexSearchDatastore;
        private ToolCallingMode toolCallingMode;
        private List<String> allowedFunctionNames;
        private Boolean logRequests;
        private Boolean logResponses;
        private List<ChatModelListener> listeners;
        private Map<String, String> customHeaders;
        private GoogleCredentials credentials;
        private String apiEndpoint;
        private Map<String, String> labels;

        public VertexAiGeminiStreamingChatModelBuilder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder project(String project) {
            this.project = project;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder location(String location) {
            this.location = location;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder temperature(Float temperature) {
            this.temperature = temperature;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder maxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder topP(Float topP) {
            this.topP = topP;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder responseMimeType(String responseMimeType) {
            this.responseMimeType = responseMimeType;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder responseSchema(Schema responseSchema) {
            this.responseSchema = responseSchema;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder safetySettings(Map<HarmCategory, SafetyThreshold> safetySettings) {
            this.safetySettings = safetySettings;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder useGoogleSearch(Boolean useGoogleSearch) {
            this.useGoogleSearch = useGoogleSearch;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder vertexSearchDatastore(String vertexSearchDatastore) {
            this.vertexSearchDatastore = vertexSearchDatastore;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder toolCallingMode(ToolCallingMode toolCallingMode) {
            this.toolCallingMode = toolCallingMode;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder allowedFunctionNames(List<String> allowedFunctionNames) {
            this.allowedFunctionNames = allowedFunctionNames;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder apiEndpoint(String apiEndpoint) {
            this.apiEndpoint = apiEndpoint;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder credentials(GoogleCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public VertexAiGeminiStreamingChatModelBuilder labels(Map<String, String> labels) {
            this.labels = labels;
            return this;
        }

        public VertexAiGeminiStreamingChatModel build() {
            return new VertexAiGeminiStreamingChatModel(this);
        }
    }
}

