/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.auth.Credentials
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.cloud.vertexai.VertexAI
 *  com.google.cloud.vertexai.VertexAI$Builder
 *  com.google.cloud.vertexai.api.Content
 *  com.google.cloud.vertexai.api.FunctionCall
 *  com.google.cloud.vertexai.api.FunctionCallingConfig
 *  com.google.cloud.vertexai.api.FunctionCallingConfig$Mode
 *  com.google.cloud.vertexai.api.GenerateContentRequest
 *  com.google.cloud.vertexai.api.GenerateContentRequest$Builder
 *  com.google.cloud.vertexai.api.GenerateContentResponse
 *  com.google.cloud.vertexai.api.GenerationConfig
 *  com.google.cloud.vertexai.api.GenerationConfig$Builder
 *  com.google.cloud.vertexai.api.Part
 *  com.google.cloud.vertexai.api.Schema
 *  com.google.cloud.vertexai.api.Tool
 *  com.google.cloud.vertexai.api.ToolConfig
 *  com.google.cloud.vertexai.generativeai.GenerativeModel
 *  com.google.cloud.vertexai.generativeai.ResponseHandler
 *  com.google.common.annotations.VisibleForTesting
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.internal.ChatRequestValidationUtils
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelErrorContext
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.listener.ChatModelRequestContext
 *  dev.langchain4j.model.chat.listener.ChatModelResponseContext
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.vertexai.gemini;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.FunctionCall;
import com.google.cloud.vertexai.api.FunctionCallingConfig;
import com.google.cloud.vertexai.api.GenerateContentRequest;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.api.Schema;
import com.google.cloud.vertexai.api.Tool;
import com.google.cloud.vertexai.api.ToolConfig;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.google.common.annotations.VisibleForTesting;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.internal.ChatRequestValidationUtils;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.vertexai.gemini.ContentsMapper;
import dev.langchain4j.model.vertexai.gemini.FinishReasonMapper;
import dev.langchain4j.model.vertexai.gemini.FunctionCallHelper;
import dev.langchain4j.model.vertexai.gemini.HarmCategory;
import dev.langchain4j.model.vertexai.gemini.ResponseGrounding;
import dev.langchain4j.model.vertexai.gemini.SafetySettingsMapper;
import dev.langchain4j.model.vertexai.gemini.SafetyThreshold;
import dev.langchain4j.model.vertexai.gemini.SchemaHelper;
import dev.langchain4j.model.vertexai.gemini.TokenUsageMapper;
import dev.langchain4j.model.vertexai.gemini.ToolCallingMode;
import dev.langchain4j.model.vertexai.gemini.VertexAiGeminiExceptionMapper;
import dev.langchain4j.model.vertexai.gemini.spi.VertexAiGeminiChatModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertexAiGeminiChatModel
implements ChatModel,
Closeable {
    private static final Logger logger = LoggerFactory.getLogger(VertexAiGeminiChatModel.class);
    private final GenerativeModel generativeModel;
    private final GenerationConfig generationConfig;
    private final Integer maxRetries;
    private final VertexAI vertexAI;
    private final Map<HarmCategory, SafetyThreshold> safetySettings;
    private final Tool googleSearch;
    private final Tool vertexSearch;
    private final ToolConfig toolConfig;
    private final List<String> allowedFunctionNames;
    private final Boolean logRequests;
    private final Boolean logResponses;
    private final List<ChatModelListener> listeners;
    private final Set<Capability> supportedCapabilities;
    private final Map<String, String> labels;

    public VertexAiGeminiChatModel(VertexAiGeminiChatModelBuilder builder) {
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
        if (builder.seed != null) {
            generationConfigBuilder.setSeed(builder.seed.intValue());
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
        if (builder.toolCallingMode != null) {
            ToolConfig toolConfig;
            switch (builder.toolCallingMode) {
                case NONE: {
                    toolConfig = ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.NONE).build()).build();
                    break;
                }
                case AUTO: {
                    toolConfig = ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.AUTO).build()).build();
                    break;
                }
                case ANY: {
                    toolConfig = ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.ANY).addAllAllowedFunctionNames(this.allowedFunctionNames).build()).build();
                    break;
                }
                default: {
                    throw new IllegalStateException("Unexpected value: " + (Object)((Object)builder.toolCallingMode));
                }
            }
            this.toolConfig = toolConfig;
        } else {
            this.toolConfig = ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.AUTO).build()).build();
        }
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
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.logRequests = (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false);
        this.logResponses = (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false);
        this.listeners = Utils.copy((List)builder.listeners);
        this.supportedCapabilities = Utils.copy((Set)builder.supportedCapabilities);
        this.labels = Utils.copy((Map)builder.labels);
    }

    @Deprecated
    public VertexAiGeminiChatModel(String project, String location, String modelName, Float temperature, Integer maxOutputTokens, Integer topK, Float topP, Integer seed, Integer maxRetries, String responseMimeType, Schema responseSchema, Map<HarmCategory, SafetyThreshold> safetySettings, Boolean useGoogleSearch, String vertexSearchDatastore, ToolCallingMode toolCallingMode, List<String> allowedFunctionNames, Boolean logRequests, Boolean logResponses, List<ChatModelListener> listeners, Set<Capability> supportedCapabilities) {
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
        if (seed != null) {
            generationConfigBuilder.setSeed(seed.intValue());
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
        if (toolCallingMode != null) {
            ToolConfig tc;
            switch (toolCallingMode) {
                case NONE: {
                    tc = ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.NONE).build()).build();
                    break;
                }
                case AUTO: {
                    tc = ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.AUTO).build()).build();
                    break;
                }
                case ANY: {
                    tc = ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.ANY).addAllAllowedFunctionNames(this.allowedFunctionNames).build()).build();
                    break;
                }
                default: {
                    throw new IllegalStateException("Unexpected value: " + (Object)((Object)toolCallingMode));
                }
            }
            this.toolConfig = tc;
        } else {
            this.toolConfig = ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.AUTO).build()).build();
        }
        this.vertexAI = new VertexAI.Builder().setProjectId(ValidationUtils.ensureNotBlank((String)project, (String)"project")).setLocation(ValidationUtils.ensureNotBlank((String)location, (String)"location")).setCustomHeaders(Collections.singletonMap("user-agent", "LangChain4j")).build();
        this.generativeModel = new GenerativeModel(modelName, this.vertexAI).withGenerationConfig(this.generationConfig);
        this.maxRetries = (Integer)Utils.getOrDefault((Object)maxRetries, (Object)2);
        this.logRequests = (Boolean)Utils.getOrDefault((Object)logRequests, (Object)false);
        this.logResponses = (Boolean)Utils.getOrDefault((Object)logResponses, (Object)false);
        this.listeners = listeners == null ? Collections.emptyList() : new ArrayList<ChatModelListener>(listeners);
        this.supportedCapabilities = Utils.copy(supportedCapabilities);
        this.labels = Collections.emptyMap();
    }

    public VertexAiGeminiChatModel(GenerativeModel generativeModel, GenerationConfig generationConfig) {
        this(generativeModel, generationConfig, 2);
    }

    public VertexAiGeminiChatModel(GenerativeModel generativeModel, GenerationConfig generationConfig, Integer maxRetries) {
        this.generationConfig = (GenerationConfig)ValidationUtils.ensureNotNull((Object)generationConfig, (String)"generationConfig");
        this.generativeModel = ((GenerativeModel)ValidationUtils.ensureNotNull((Object)generativeModel, (String)"generativeModel")).withGenerationConfig(generationConfig);
        this.maxRetries = (Integer)Utils.getOrDefault((Object)maxRetries, (Object)2);
        this.vertexAI = null;
        this.safetySettings = Collections.emptyMap();
        this.googleSearch = null;
        this.vertexSearch = null;
        this.toolConfig = ToolConfig.newBuilder().setFunctionCallingConfig(FunctionCallingConfig.newBuilder().setMode(FunctionCallingConfig.Mode.AUTO).build()).build();
        this.allowedFunctionNames = Collections.emptyList();
        this.logRequests = false;
        this.logResponses = false;
        this.listeners = Collections.emptyList();
        this.supportedCapabilities = Collections.emptySet();
        this.labels = Collections.emptyMap();
    }

    public ChatResponse chat(ChatRequest chatRequest) {
        ChatRequestParameters parameters = chatRequest.parameters();
        ChatRequestValidationUtils.validateParameters((ChatRequestParameters)parameters);
        ChatRequestValidationUtils.validate((ToolChoice)parameters.toolChoice());
        List toolSpecifications = parameters.toolSpecifications();
        Response<AiMessage> response = Utils.isNullOrEmpty((Collection)toolSpecifications) ? this.generate(chatRequest.messages(), parameters.responseFormat()) : this.generate(chatRequest.messages(), toolSpecifications, parameters.responseFormat());
        return ChatResponse.builder().aiMessage((AiMessage)response.content()).metadata(ChatResponseMetadata.builder().tokenUsage(response.tokenUsage()).finishReason(response.finishReason()).build()).build();
    }

    private Response<AiMessage> generate(List<ChatMessage> messages, ResponseFormat responseFormat) {
        return this.generate(messages, new ArrayList<ToolSpecification>(), responseFormat);
    }

    private Response<AiMessage> generate(List<ChatMessage> messages, List<ToolSpecification> toolSpecifications, ResponseFormat responseFormat) {
        AiMessage aiMessage;
        Content content;
        List<FunctionCall> functionCalls;
        GenerateContentResponse response;
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
        GenerationConfig generationConfig = this.generationConfig;
        if (responseFormat != null && responseFormat.type() == ResponseFormatType.JSON) {
            GenerationConfig.Builder configBuilder = GenerationConfig.newBuilder((GenerationConfig)this.generationConfig);
            configBuilder.setResponseMimeType("application/json");
            if (responseFormat.jsonSchema() != null) {
                Schema schema = SchemaHelper.from(responseFormat.jsonSchema().rootElement());
                configBuilder.setResponseSchema(schema);
            }
            generationConfig = configBuilder.build();
        }
        GenerativeModel model = this.generativeModel.withGenerationConfig(generationConfig).withTools(tools).withToolConfig(this.toolConfig);
        ContentsMapper.InstructionAndContent instructionAndContent = ContentsMapper.splitInstructionAndContent(messages);
        if (instructionAndContent.systemInstruction != null) {
            model = model.withSystemInstruction(instructionAndContent.systemInstruction);
        }
        if (!this.safetySettings.isEmpty()) {
            model = model.withSafetySettings(SafetySettingsMapper.mapSafetySettings(this.safetySettings));
        }
        if (this.logRequests.booleanValue() && logger.isDebugEnabled()) {
            logger.debug("GEMINI ({}) request: {} tools: {} responseFormat: {}", new Object[]{modelName, instructionAndContent, tools, responseFormat});
        }
        GenerativeModel finalModel = model;
        ChatRequest listenerRequest = ChatRequest.builder().messages(messages).parameters(ChatRequestParameters.builder().modelName(modelName).temperature(Double.valueOf(generationConfig.getTemperature())).topP(Double.valueOf(generationConfig.getTopP())).maxOutputTokens(Integer.valueOf(generationConfig.getMaxOutputTokens())).toolSpecifications(toolSpecifications).responseFormat(responseFormat).build()).build();
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
        try {
            response = (GenerateContentResponse)RetryUtils.withRetryMappingExceptions(() -> {
                if (this.labels.isEmpty()) {
                    return finalModel.generateContent(instructionAndContent.contents);
                }
                GenerateContentRequest request = VertexAiGeminiChatModel.buildGenerateContentRequest(finalModel, this.vertexAI, instructionAndContent.contents, this.labels);
                return (GenerateContentResponse)this.vertexAI.getPredictionServiceClient().generateContentCallable().call((Object)request);
            }, (int)this.maxRetries, (ExceptionMapper)VertexAiGeminiExceptionMapper.INSTANCE);
        }
        catch (RuntimeException e) {
            this.listeners.forEach(listener -> {
                try {
                    ChatModelErrorContext chatModelErrorContext = new ChatModelErrorContext((Throwable)e, listenerRequest, this.provider(), (Map)listenerAttributes);
                    listener.onError(chatModelErrorContext);
                }
                catch (Exception t) {
                    logger.warn("Exception while calling model listener (onError)", (Throwable)t);
                }
            });
            throw e;
        }
        if (this.logResponses.booleanValue() && logger.isDebugEnabled()) {
            logger.debug("GEMINI ({}) response: {}", (Object)modelName, (Object)response);
        }
        if (!(functionCalls = (content = ResponseHandler.getContent((GenerateContentResponse)response)).getPartsList().stream().filter(Part::hasFunctionCall).map(Part::getFunctionCall).collect(Collectors.toList())).isEmpty()) {
            List<ToolExecutionRequest> toolExecutionRequests = FunctionCallHelper.fromFunctionCalls(functionCalls);
            aiMessage = AiMessage.from(toolExecutionRequests);
        } else {
            aiMessage = AiMessage.from((String)ResponseHandler.getText((GenerateContentResponse)response));
        }
        Response finalResponse = Response.from((Object)aiMessage, (TokenUsage)TokenUsageMapper.map(response.getUsageMetadata()), (FinishReason)FinishReasonMapper.map(ResponseHandler.getFinishReason((GenerateContentResponse)response)));
        ChatResponse listenerResponse = ChatResponse.builder().aiMessage(aiMessage).metadata(ChatResponseMetadata.builder().modelName(modelName).tokenUsage(finalResponse.tokenUsage()).finishReason(finalResponse.finishReason()).build()).build();
        ChatModelResponseContext chatModelResponseContext = new ChatModelResponseContext(listenerResponse, listenerRequest, this.provider(), listenerAttributes);
        this.listeners.forEach(listener -> {
            try {
                listener.onResponse(chatModelResponseContext);
            }
            catch (Exception e) {
                logger.warn("Exception while calling model listener (onResponse)", (Throwable)e);
            }
        });
        return finalResponse;
    }

    @Override
    public void close() {
        if (this.vertexAI != null) {
            this.vertexAI.close();
        }
    }

    @VisibleForTesting
    VertexAI vertexAI() {
        return this.vertexAI;
    }

    @VisibleForTesting
    Map<String, String> labels() {
        return this.labels;
    }

    static GenerateContentRequest buildGenerateContentRequest(GenerativeModel model, VertexAI vertexAI, List<Content> contents, Map<String, String> labels) {
        GenerateContentRequest.Builder requestBuilder = GenerateContentRequest.newBuilder().setModel(VertexAiGeminiChatModel.buildResourceName(model.getModelName(), vertexAI)).addAllContents(contents).setGenerationConfig(model.getGenerationConfig()).addAllSafetySettings((Iterable)model.getSafetySettings()).addAllTools((Iterable)model.getTools());
        model.getToolConfig().ifPresent(arg_0 -> ((GenerateContentRequest.Builder)requestBuilder).setToolConfig(arg_0));
        model.getSystemInstruction().ifPresent(arg_0 -> ((GenerateContentRequest.Builder)requestBuilder).setSystemInstruction(arg_0));
        if (labels != null && !labels.isEmpty()) {
            requestBuilder.putAllLabels(labels);
        }
        return requestBuilder.build();
    }

    static String buildResourceName(String modelName, VertexAI vertexAI) {
        if (modelName.startsWith("projects/")) {
            return modelName;
        }
        String projectId = vertexAI.getProjectId();
        String location = vertexAI.getLocation();
        if (modelName.startsWith("publishers/")) {
            return String.format("projects/%s/locations/%s/%s", projectId, location, modelName);
        }
        if (modelName.startsWith("models/")) {
            return String.format("projects/%s/locations/%s/publishers/google/%s", projectId, location, modelName);
        }
        return String.format("projects/%s/locations/%s/publishers/google/models/%s", projectId, location, modelName);
    }

    public Set<Capability> supportedCapabilities() {
        return this.supportedCapabilities;
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.GOOGLE_VERTEX_AI_GEMINI;
    }

    public static VertexAiGeminiChatModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(VertexAiGeminiChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            VertexAiGeminiChatModelBuilderFactory factory = (VertexAiGeminiChatModelBuilderFactory)iterator.next();
            return (VertexAiGeminiChatModelBuilder)factory.get();
        }
        return new VertexAiGeminiChatModelBuilder();
    }

    public static class VertexAiGeminiChatModelBuilder {
        private String project;
        private String location;
        private String modelName;
        private Float temperature;
        private Integer maxOutputTokens;
        private Integer topK;
        private Float topP;
        private Integer seed;
        private Integer maxRetries;
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
        private Set<Capability> supportedCapabilities;
        private Map<String, String> customHeaders;
        private GoogleCredentials credentials;
        private String apiEndpoint;
        private Map<String, String> labels;

        public VertexAiGeminiChatModelBuilder project(String project) {
            this.project = project;
            return this;
        }

        public VertexAiGeminiChatModelBuilder location(String location) {
            this.location = location;
            return this;
        }

        public VertexAiGeminiChatModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public VertexAiGeminiChatModelBuilder temperature(Float temperature) {
            this.temperature = temperature;
            return this;
        }

        public VertexAiGeminiChatModelBuilder maxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
            return this;
        }

        public VertexAiGeminiChatModelBuilder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public VertexAiGeminiChatModelBuilder topP(Float topP) {
            this.topP = topP;
            return this;
        }

        public VertexAiGeminiChatModelBuilder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public VertexAiGeminiChatModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public VertexAiGeminiChatModelBuilder responseMimeType(String responseMimeType) {
            this.responseMimeType = responseMimeType;
            return this;
        }

        public VertexAiGeminiChatModelBuilder responseSchema(Schema responseSchema) {
            this.responseSchema = responseSchema;
            return this;
        }

        public VertexAiGeminiChatModelBuilder safetySettings(Map<HarmCategory, SafetyThreshold> safetySettings) {
            this.safetySettings = safetySettings;
            return this;
        }

        public VertexAiGeminiChatModelBuilder useGoogleSearch(Boolean useGoogleSearch) {
            this.useGoogleSearch = useGoogleSearch;
            return this;
        }

        public VertexAiGeminiChatModelBuilder vertexSearchDatastore(String vertexSearchDatastore) {
            this.vertexSearchDatastore = vertexSearchDatastore;
            return this;
        }

        public VertexAiGeminiChatModelBuilder toolCallingMode(ToolCallingMode toolCallingMode) {
            this.toolCallingMode = toolCallingMode;
            return this;
        }

        public VertexAiGeminiChatModelBuilder allowedFunctionNames(List<String> allowedFunctionNames) {
            this.allowedFunctionNames = allowedFunctionNames;
            return this;
        }

        public VertexAiGeminiChatModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public VertexAiGeminiChatModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public VertexAiGeminiChatModelBuilder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public VertexAiGeminiChatModelBuilder supportedCapabilities(Set<Capability> supportedCapabilities) {
            this.supportedCapabilities = supportedCapabilities;
            return this;
        }

        public VertexAiGeminiChatModelBuilder supportedCapabilities(Capability ... supportedCapabilities) {
            return this.supportedCapabilities(new HashSet<Capability>(Arrays.asList(supportedCapabilities)));
        }

        public VertexAiGeminiChatModelBuilder credentials(GoogleCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public VertexAiGeminiChatModelBuilder apiEndpoint(String apiEndpoint) {
            this.apiEndpoint = apiEndpoint;
            return this;
        }

        public VertexAiGeminiChatModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public VertexAiGeminiChatModelBuilder labels(Map<String, String> labels) {
            this.labels = labels;
            return this;
        }

        public VertexAiGeminiChatModel build() {
            return new VertexAiGeminiChatModel(this);
        }
    }
}

