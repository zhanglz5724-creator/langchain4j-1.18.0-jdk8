/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.mistralai;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.mistralai.InternalMistralAIHelper;
import dev.langchain4j.model.mistralai.MistralAiChatModelName;
import dev.langchain4j.model.mistralai.MistralAiChatResponseMetadata;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionResponse;
import dev.langchain4j.model.mistralai.internal.client.MistralAiClient;
import dev.langchain4j.model.mistralai.internal.client.ParsedAndRawResponse;
import dev.langchain4j.model.mistralai.internal.mapper.MistralAiMapper;
import dev.langchain4j.model.mistralai.spi.MistralAiChatModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class MistralAiChatModel
implements ChatModel {
    private final MistralAiClient client;
    private final Boolean safePrompt;
    private final Integer randomSeed;
    private final boolean returnThinking;
    private final boolean sendThinking;
    private final Integer maxRetries;
    private final List<ChatModelListener> listeners;
    private final Set<Capability> supportedCapabilities;
    private final boolean strictJsonSchema;
    private final ChatRequestParameters defaultRequestParameters;

    public MistralAiChatModel(MistralAiChatModelBuilder builder) {
        this.client = ((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)MistralAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.mistral.ai/v1"))).apiKey(builder.apiKey)).timeout(builder.timeout)).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).customHeaders(builder.customHeadersSupplier)).build();
        this.safePrompt = builder.safePrompt;
        this.randomSeed = builder.randomSeed;
        this.returnThinking = (Boolean)Utils.getOrDefault((Object)builder.returnThinking, (Object)false);
        this.sendThinking = (Boolean)Utils.getOrDefault((Object)builder.sendThinking, (Object)false);
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.listeners = Utils.copy((List)builder.listeners);
        this.supportedCapabilities = Utils.copy((Set)builder.supportedCapabilities);
        this.strictJsonSchema = (Boolean)Utils.getOrDefault((Object)builder.strictJsonSchema, (Object)false);
        this.defaultRequestParameters = this.initDefaultRequestParameters(builder);
    }

    private ChatRequestParameters initDefaultRequestParameters(MistralAiChatModelBuilder builder) {
        ChatRequestParameters commonParameters;
        if (builder.defaultRequestParameters != null) {
            InternalMistralAIHelper.validate(builder.defaultRequestParameters);
            commonParameters = builder.defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        return DefaultChatRequestParameters.builder().modelName((String)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName())).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature())).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP())).frequencyPenalty((Double)Utils.getOrDefault((Object)builder.frequencyPenalty, (Object)commonParameters.frequencyPenalty())).presencePenalty((Double)Utils.getOrDefault((Object)builder.presencePenalty, (Object)commonParameters.presencePenalty())).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxTokens, (Object)commonParameters.maxOutputTokens())).stopSequences(Utils.getOrDefault((List)builder.stopSequences, (List)commonParameters.stopSequences())).toolSpecifications(commonParameters.toolSpecifications()).toolChoice(commonParameters.toolChoice()).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat())).build();
    }

    public ChatResponse doChat(ChatRequest chatRequest) {
        InternalMistralAIHelper.validate(chatRequest.parameters());
        MistralAiChatCompletionRequest request = InternalMistralAIHelper.createMistralAiRequest(chatRequest, this.safePrompt, this.randomSeed, false, this.sendThinking, this.strictJsonSchema);
        ParsedAndRawResponse response = (ParsedAndRawResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.chatCompletionWithRawResponse(request), (int)this.maxRetries);
        MistralAiChatCompletionResponse mistralAiResponse = (MistralAiChatCompletionResponse)response.parsedResponse();
        return ChatResponse.builder().aiMessage(MistralAiMapper.aiMessageFrom(mistralAiResponse, this.returnThinking)).metadata((ChatResponseMetadata)((MistralAiChatResponseMetadata.Builder)((MistralAiChatResponseMetadata.Builder)((MistralAiChatResponseMetadata.Builder)((MistralAiChatResponseMetadata.Builder)MistralAiChatResponseMetadata.builder().id(mistralAiResponse.getId())).modelName(mistralAiResponse.getModel())).tokenUsage(MistralAiMapper.tokenUsageFrom(mistralAiResponse.getUsage()))).finishReason(MistralAiMapper.finishReasonFrom(mistralAiResponse.getChoices().get(0).getFinishReason()))).rawHttpResponse(response.rawResponse()).build()).build();
    }

    public ChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.MISTRAL_AI;
    }

    public Set<Capability> supportedCapabilities() {
        return this.supportedCapabilities;
    }

    public static MistralAiChatModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(MistralAiChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            MistralAiChatModelBuilderFactory factory = (MistralAiChatModelBuilderFactory)iterator.next();
            return (MistralAiChatModelBuilder)factory.get();
        }
        return new MistralAiChatModelBuilder();
    }

    public static class MistralAiChatModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Integer maxTokens;
        private Boolean safePrompt;
        private Integer randomSeed;
        private Boolean returnThinking;
        private Boolean sendThinking;
        private ResponseFormat responseFormat;
        private List<String> stopSequences;
        private Double frequencyPenalty;
        private Double presencePenalty;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Integer maxRetries;
        private List<ChatModelListener> listeners;
        private Set<Capability> supportedCapabilities;
        private Boolean strictJsonSchema;
        private ChatRequestParameters defaultRequestParameters;
        private Supplier<Map<String, String>> customHeadersSupplier;

        public MistralAiChatModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public MistralAiChatModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public MistralAiChatModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public MistralAiChatModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public MistralAiChatModelBuilder modelName(MistralAiChatModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public MistralAiChatModelBuilder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public MistralAiChatModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public MistralAiChatModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public MistralAiChatModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public MistralAiChatModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public MistralAiChatModelBuilder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public MistralAiChatModelBuilder safePrompt(Boolean safePrompt) {
            this.safePrompt = safePrompt;
            return this;
        }

        public MistralAiChatModelBuilder randomSeed(Integer randomSeed) {
            this.randomSeed = randomSeed;
            return this;
        }

        public MistralAiChatModelBuilder returnThinking(Boolean returnThinking) {
            this.returnThinking = returnThinking;
            return this;
        }

        public MistralAiChatModelBuilder sendThinking(Boolean sendThinking) {
            this.sendThinking = sendThinking;
            return this;
        }

        public MistralAiChatModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public MistralAiChatModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public MistralAiChatModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public MistralAiChatModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public MistralAiChatModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public MistralAiChatModelBuilder supportedCapabilities(Capability ... supportedCapabilities) {
            this.supportedCapabilities = Arrays.stream(supportedCapabilities).collect(Collectors.toSet());
            return this;
        }

        public MistralAiChatModelBuilder supportedCapabilities(Set<Capability> supportedCapabilities) {
            this.supportedCapabilities = new HashSet<Capability>(supportedCapabilities);
            return this;
        }

        public MistralAiChatModelBuilder strictJsonSchema(Boolean strictJsonSchema) {
            this.strictJsonSchema = strictJsonSchema;
            return this;
        }

        public MistralAiChatModelBuilder stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return this;
        }

        public MistralAiChatModelBuilder presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public MistralAiChatModelBuilder frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public MistralAiChatModelBuilder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public MistralAiChatModelBuilder defaultRequestParameters(ChatRequestParameters parameters) {
            this.defaultRequestParameters = parameters;
            return this;
        }

        public MistralAiChatModel build() {
            return new MistralAiChatModel(this);
        }
    }
}

