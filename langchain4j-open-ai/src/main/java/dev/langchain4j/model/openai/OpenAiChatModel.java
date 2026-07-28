/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.exception.InternalServerException
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
package dev.langchain4j.model.openai;

import dev.langchain4j.exception.InternalServerException;
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
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiChatResponseMetadata;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.OpenAiUtils;
import dev.langchain4j.model.openai.internal.ParsedAndRawResponse;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse;
import dev.langchain4j.model.openai.spi.OpenAiChatModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.slf4j.Logger;

public class OpenAiChatModel
implements ChatModel {
    private final OpenAiClient client;
    private final Integer maxRetries;
    private final OpenAiChatRequestParameters defaultRequestParameters;
    private final String responseFormatString;
    private final Set<Capability> supportedCapabilities;
    private final boolean strictJsonSchema;
    private final boolean strictTools;
    private final boolean returnThinking;
    private final boolean sendThinking;
    private final String thinkingFieldName;
    private final boolean useInputImageFormat;
    private final List<ChatModelListener> listeners;

    public OpenAiChatModel(OpenAiChatModelBuilder builder) {
        ChatRequestParameters commonParameters;
        this.client = ((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)OpenAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.openai.com/v1"))).apiKey(builder.apiKey)).organizationId(builder.organizationId)).projectId(builder.projectId)).connectTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(15L)))).readTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L)))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).userAgent("langchain4j-openai")).customHeaders(builder.customHeadersSupplier)).customQueryParams(builder.customQueryParams)).build();
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        if (builder.defaultRequestParameters != null) {
            OpenAiUtils.validate(builder.defaultRequestParameters);
            commonParameters = builder.defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        OpenAiChatRequestParameters openAiParameters = builder.defaultRequestParameters instanceof OpenAiChatRequestParameters ? (OpenAiChatRequestParameters)builder.defaultRequestParameters : OpenAiChatRequestParameters.EMPTY;
        this.defaultRequestParameters = ((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)((OpenAiChatRequestParameters.Builder)OpenAiChatRequestParameters.builder().modelName((String)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName()))).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature()))).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP()))).frequencyPenalty((Double)Utils.getOrDefault((Object)builder.frequencyPenalty, (Object)commonParameters.frequencyPenalty()))).presencePenalty((Double)Utils.getOrDefault((Object)builder.presencePenalty, (Object)commonParameters.presencePenalty()))).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxTokens, (Object)commonParameters.maxOutputTokens()))).stopSequences(Utils.getOrDefault((List)builder.stop, (List)commonParameters.stopSequences()))).toolSpecifications(commonParameters.toolSpecifications())).toolChoice(commonParameters.toolChoice())).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat()))).maxCompletionTokens((Integer)Utils.getOrDefault((Object)builder.maxCompletionTokens, (Object)openAiParameters.maxCompletionTokens())).logitBias(Utils.getOrDefault((Map)builder.logitBias, openAiParameters.logitBias())).parallelToolCalls((Boolean)Utils.getOrDefault((Object)builder.parallelToolCalls, (Object)openAiParameters.parallelToolCalls())).seed((Integer)Utils.getOrDefault((Object)builder.seed, (Object)openAiParameters.seed())).user((String)Utils.getOrDefault((Object)builder.user, (Object)openAiParameters.user())).store((Boolean)Utils.getOrDefault((Object)builder.store, (Object)openAiParameters.store())).metadata(Utils.getOrDefault((Map)builder.metadata, openAiParameters.metadata())).serviceTier((String)Utils.getOrDefault((Object)builder.serviceTier, (Object)openAiParameters.serviceTier())).reasoningEffort((String)Utils.getOrDefault((Object)builder.reasoningEffort, (Object)openAiParameters.reasoningEffort())).logprobs((Boolean)Utils.getOrDefault((Object)builder.logprobs, (Object)openAiParameters.logprobs())).topLogprobs((Integer)Utils.getOrDefault((Object)builder.topLogprobs, (Object)openAiParameters.topLogprobs())).customParameters(Utils.getOrDefault((Map)builder.customParameters, openAiParameters.customParameters())).build();
        this.responseFormatString = builder.responseFormatString;
        this.supportedCapabilities = Utils.copy((Set)builder.supportedCapabilities);
        this.strictJsonSchema = (Boolean)Utils.getOrDefault((Object)builder.strictJsonSchema, (Object)false);
        this.strictTools = (Boolean)Utils.getOrDefault((Object)builder.strictTools, (Object)false);
        this.returnThinking = (Boolean)Utils.getOrDefault((Object)builder.returnThinking, (Object)false);
        this.sendThinking = (Boolean)Utils.getOrDefault((Object)builder.sendThinking, (Object)false);
        this.thinkingFieldName = (String)Utils.getOrDefault((Object)builder.thinkingFieldName, (Object)"reasoning_content");
        this.useInputImageFormat = (Boolean)Utils.getOrDefault((Object)builder.useInputImageFormat, (Object)false);
        this.listeners = Utils.copy((List)builder.listeners);
    }

    public OpenAiChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public Set<Capability> supportedCapabilities() {
        HashSet<Capability> capabilities = new HashSet<Capability>(this.supportedCapabilities);
        if ("json_schema".equals(this.responseFormatString)) {
            capabilities.add(Capability.RESPONSE_FORMAT_JSON_SCHEMA);
        }
        return capabilities;
    }

    public ChatResponse doChat(ChatRequest chatRequest) {
        OpenAiChatRequestParameters parameters = (OpenAiChatRequestParameters)chatRequest.parameters();
        OpenAiUtils.validate((ChatRequestParameters)parameters);
        ChatCompletionRequest openAiRequest = OpenAiUtils.toOpenAiChatRequest(chatRequest, parameters, this.sendThinking, this.thinkingFieldName, this.strictTools, this.strictJsonSchema, this.useInputImageFormat).build();
        ParsedAndRawResponse parsedAndRawResponse = (ParsedAndRawResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.chatCompletion(openAiRequest).executeRaw(), (int)this.maxRetries);
        ChatCompletionResponse openAiResponse = (ChatCompletionResponse)parsedAndRawResponse.parsedResponse();
        if (Utils.isNullOrEmpty(openAiResponse.choices())) {
            throw new InternalServerException("Chat completion failed: no choices returned in response");
        }
        OpenAiChatResponseMetadata responseMetadata = ((OpenAiChatResponseMetadata.Builder)((OpenAiChatResponseMetadata.Builder)((OpenAiChatResponseMetadata.Builder)((OpenAiChatResponseMetadata.Builder)OpenAiChatResponseMetadata.builder().id(openAiResponse.id())).modelName(openAiResponse.model())).tokenUsage(OpenAiUtils.tokenUsageFrom(openAiResponse.usage()))).finishReason(OpenAiUtils.finishReasonFrom(openAiResponse.choices().get(0).finishReason()))).created(openAiResponse.created()).serviceTier(openAiResponse.serviceTier()).systemFingerprint(openAiResponse.systemFingerprint()).rawHttpResponse(parsedAndRawResponse.rawHttpResponse()).logProbs(OpenAiUtils.logProbsFrom(openAiResponse.choices().get(0).logprobs())).build();
        return ChatResponse.builder().aiMessage(OpenAiUtils.aiMessageFrom(openAiResponse, this.returnThinking)).metadata((ChatResponseMetadata)responseMetadata).build();
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.OPEN_AI;
    }

    public static OpenAiChatModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OpenAiChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OpenAiChatModelBuilderFactory factory = (OpenAiChatModelBuilderFactory)iterator.next();
            return (OpenAiChatModelBuilder)factory.get();
        }
        return new OpenAiChatModelBuilder();
    }

    public static class OpenAiChatModelBuilder {
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
        private Set<Capability> supportedCapabilities;
        private ResponseFormat responseFormat;
        private String responseFormatString;
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
        private Boolean useInputImageFormat;
        private Boolean logprobs;
        private Integer topLogprobs;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Supplier<Map<String, String>> customHeadersSupplier;
        private Map<String, String> customQueryParams;
        private Map<String, Object> customParameters;
        private List<ChatModelListener> listeners;

        public OpenAiChatModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public OpenAiChatModelBuilder defaultRequestParameters(ChatRequestParameters parameters) {
            this.defaultRequestParameters = parameters;
            return this;
        }

        public OpenAiChatModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public OpenAiChatModelBuilder modelName(OpenAiChatModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public OpenAiChatModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public OpenAiChatModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public OpenAiChatModelBuilder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public OpenAiChatModelBuilder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public OpenAiChatModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public OpenAiChatModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public OpenAiChatModelBuilder stop(List<String> stop) {
            this.stop = stop;
            return this;
        }

        public OpenAiChatModelBuilder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public OpenAiChatModelBuilder maxCompletionTokens(Integer maxCompletionTokens) {
            this.maxCompletionTokens = maxCompletionTokens;
            return this;
        }

        public OpenAiChatModelBuilder presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public OpenAiChatModelBuilder frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public OpenAiChatModelBuilder logitBias(Map<String, Integer> logitBias) {
            this.logitBias = logitBias;
            return this;
        }

        public OpenAiChatModelBuilder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public OpenAiChatModelBuilder responseFormat(String responseFormat) {
            this.responseFormat = OpenAiUtils.fromOpenAiResponseFormat(responseFormat);
            this.responseFormatString = responseFormat;
            return this;
        }

        public OpenAiChatModelBuilder supportedCapabilities(Set<Capability> supportedCapabilities) {
            this.supportedCapabilities = supportedCapabilities;
            return this;
        }

        public OpenAiChatModelBuilder supportedCapabilities(Capability ... supportedCapabilities) {
            return this.supportedCapabilities(new HashSet<Capability>(Arrays.asList(supportedCapabilities)));
        }

        public OpenAiChatModelBuilder strictJsonSchema(Boolean strictJsonSchema) {
            this.strictJsonSchema = strictJsonSchema;
            return this;
        }

        public OpenAiChatModelBuilder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public OpenAiChatModelBuilder user(String user) {
            this.user = user;
            return this;
        }

        public OpenAiChatModelBuilder strictTools(Boolean strictTools) {
            this.strictTools = strictTools;
            return this;
        }

        public OpenAiChatModelBuilder parallelToolCalls(Boolean parallelToolCalls) {
            this.parallelToolCalls = parallelToolCalls;
            return this;
        }

        public OpenAiChatModelBuilder store(Boolean store) {
            this.store = store;
            return this;
        }

        public OpenAiChatModelBuilder metadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        public OpenAiChatModelBuilder serviceTier(String serviceTier) {
            this.serviceTier = serviceTier;
            return this;
        }

        public OpenAiChatModelBuilder reasoningEffort(String reasoningEffort) {
            this.reasoningEffort = reasoningEffort;
            return this;
        }

        public OpenAiChatModelBuilder returnThinking(Boolean returnThinking) {
            this.returnThinking = returnThinking;
            return this;
        }

        public OpenAiChatModelBuilder sendThinking(Boolean sendThinking, String fieldName) {
            this.sendThinking = sendThinking;
            this.thinkingFieldName = fieldName;
            return this;
        }

        public OpenAiChatModelBuilder sendThinking(Boolean sendThinking) {
            this.sendThinking = sendThinking;
            this.thinkingFieldName = "reasoning_content";
            return this;
        }

        public OpenAiChatModelBuilder useInputImageFormat(Boolean useInputImageFormat) {
            this.useInputImageFormat = useInputImageFormat;
            return this;
        }

        public OpenAiChatModelBuilder logprobs(Boolean logprobs) {
            this.logprobs = logprobs;
            return this;
        }

        public OpenAiChatModelBuilder topLogprobs(Integer topLogprobs) {
            this.topLogprobs = topLogprobs;
            return this;
        }

        public OpenAiChatModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public OpenAiChatModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public OpenAiChatModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public OpenAiChatModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OpenAiChatModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public OpenAiChatModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public OpenAiChatModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public OpenAiChatModelBuilder customQueryParams(Map<String, String> customQueryParams) {
            this.customQueryParams = customQueryParams;
            return this;
        }

        public OpenAiChatModelBuilder customParameters(Map<String, Object> customParameters) {
            this.customParameters = customParameters;
            return this;
        }

        public OpenAiChatModelBuilder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public OpenAiChatModelBuilder listeners(ChatModelListener ... listeners) {
            return this.listeners(Arrays.asList(listeners));
        }

        public OpenAiChatModel build() {
            return new OpenAiChatModel(this);
        }
    }
}

