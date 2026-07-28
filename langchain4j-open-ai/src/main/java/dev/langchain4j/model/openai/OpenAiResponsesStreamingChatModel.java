/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.http.client.HttpClientBuilder
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
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.http.client.HttpClientBuilder;
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
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiResponsesChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiResponsesClient;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Experimental
public class OpenAiResponsesStreamingChatModel
implements StreamingChatModel {
    private final OpenAiResponsesClient client;
    private final OpenAiResponsesChatRequestParameters defaultRequestParameters;
    private final List<ChatModelListener> listeners;

    private OpenAiResponsesStreamingChatModel(Builder builder) {
        ChatRequestParameters commonParameters;
        this.client = OpenAiResponsesClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl(builder.baseUrl).apiKey(builder.apiKey).organizationId(builder.organizationId).logRequests(builder.logRequests).logResponses(builder.logResponses).build();
        if (builder.defaultRequestParameters != null) {
            OpenAiResponsesStreamingChatModel.validate(builder.defaultRequestParameters);
            commonParameters = builder.defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        OpenAiResponsesChatRequestParameters responsesParameters = commonParameters instanceof OpenAiResponsesChatRequestParameters ? (OpenAiResponsesChatRequestParameters)commonParameters : OpenAiResponsesChatRequestParameters.EMPTY;
        this.defaultRequestParameters = ((OpenAiResponsesChatRequestParameters.Builder)((OpenAiResponsesChatRequestParameters.Builder)((OpenAiResponsesChatRequestParameters.Builder)((OpenAiResponsesChatRequestParameters.Builder)((OpenAiResponsesChatRequestParameters.Builder)((OpenAiResponsesChatRequestParameters.Builder)((OpenAiResponsesChatRequestParameters.Builder)OpenAiResponsesChatRequestParameters.builder().modelName((String)ValidationUtils.ensureNotNull((Object)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName()), (String)"modelName"))).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature()))).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP()))).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxOutputTokens, (Object)commonParameters.maxOutputTokens()))).toolSpecifications(Utils.getOrDefault((List)builder.toolSpecifications, (List)commonParameters.toolSpecifications()))).toolChoice((ToolChoice)Utils.getOrDefault((Object)builder.toolChoice, (Object)commonParameters.toolChoice()))).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat()))).previousResponseId((String)Utils.getOrDefault((Object)builder.previousResponseId, (Object)responsesParameters.previousResponseId())).maxToolCalls((Integer)Utils.getOrDefault((Object)builder.maxToolCalls, (Object)responsesParameters.maxToolCalls())).parallelToolCalls((Boolean)Utils.getOrDefault((Object)builder.parallelToolCalls, (Object)responsesParameters.parallelToolCalls())).topLogprobs((Integer)Utils.getOrDefault((Object)builder.topLogprobs, (Object)responsesParameters.topLogprobs())).truncation((String)Utils.getOrDefault((Object)builder.truncation, (Object)responsesParameters.truncation())).include(Utils.getOrDefault((List)builder.include, responsesParameters.include())).serviceTier((String)Utils.getOrDefault((Object)builder.serviceTier, (Object)responsesParameters.serviceTier())).safetyIdentifier((String)Utils.getOrDefault((Object)builder.safetyIdentifier, (Object)responsesParameters.safetyIdentifier())).promptCacheKey((String)Utils.getOrDefault((Object)builder.promptCacheKey, (Object)responsesParameters.promptCacheKey())).promptCacheRetention((String)Utils.getOrDefault((Object)builder.promptCacheRetention, (Object)responsesParameters.promptCacheRetention())).reasoningEffort((String)Utils.getOrDefault((Object)builder.reasoningEffort, (Object)responsesParameters.reasoningEffort())).reasoningSummary((String)Utils.getOrDefault((Object)builder.reasoningSummary, (Object)responsesParameters.reasoningSummary())).textVerbosity((String)Utils.getOrDefault((Object)builder.textVerbosity, (Object)responsesParameters.textVerbosity())).streamIncludeObfuscation((Boolean)Utils.getOrDefault((Object)builder.streamIncludeObfuscation, (Object)responsesParameters.streamIncludeObfuscation())).store((Boolean)Utils.getOrDefault((Object)builder.store, (Object)Utils.getOrDefault((Object)responsesParameters.store(), (Object)false))).strictTools((Boolean)Utils.getOrDefault((Object)builder.strictTools, (Object)responsesParameters.strictTools())).strictJsonSchema((Boolean)Utils.getOrDefault((Object)builder.strictJsonSchema, (Object)responsesParameters.strictJsonSchema())).serverTools(Utils.getOrDefault((List)builder.serverTools, responsesParameters.serverTools())).build();
        this.listeners = Utils.copy((List)builder.listeners);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void doChat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
        OpenAiResponsesStreamingChatModel.validate(chatRequest.parameters());
        OpenAiResponsesChatRequestParameters parameters = (OpenAiResponsesChatRequestParameters)chatRequest.parameters();
        this.client.streamingChat(chatRequest, parameters, handler);
    }

    private static void validate(ChatRequestParameters parameters) {
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

    public static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String organizationId;
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
        private String reasoningEffort;
        private String reasoningSummary;
        private String textVerbosity;
        private Boolean streamIncludeObfuscation;
        private Boolean store;
        private Boolean strictTools;
        private Boolean strictJsonSchema;
        private ResponseFormat responseFormat;
        private List<ToolSpecification> toolSpecifications;
        private List<Map<String, Object>> serverTools;
        private ToolChoice toolChoice;
        private Boolean logRequests;
        private Boolean logResponses;
        private List<ChatModelListener> listeners;
        private ChatRequestParameters defaultRequestParameters;

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
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

        public Builder reasoningEffort(String reasoningEffort) {
            this.reasoningEffort = reasoningEffort;
            return this;
        }

        public Builder reasoningSummary(String reasoningSummary) {
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

        public Builder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public Builder toolSpecifications(List<ToolSpecification> toolSpecifications) {
            this.toolSpecifications = toolSpecifications;
            return this;
        }

        public Builder toolSpecifications(ToolSpecification ... toolSpecifications) {
            return this.toolSpecifications(Arrays.asList(toolSpecifications));
        }

        public Builder serverTools(List<Map<String, Object>> serverTools) {
            this.serverTools = serverTools;
            return this;
        }

        public Builder toolChoice(ToolChoice toolChoice) {
            this.toolChoice = toolChoice;
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

        public Builder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public Builder listeners(ChatModelListener ... listeners) {
            return this.listeners(Arrays.asList(listeners));
        }

        public Builder defaultRequestParameters(ChatRequestParameters parameters) {
            this.defaultRequestParameters = parameters;
            return this;
        }

        public OpenAiResponsesStreamingChatModel build() {
            return new OpenAiResponsesStreamingChatModel(this);
        }
    }
}

