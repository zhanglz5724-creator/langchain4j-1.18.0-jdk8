/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.openai.OpenAIClient
 *  com.azure.ai.openai.models.AzureChatEnhancementConfiguration
 *  com.azure.ai.openai.models.AzureChatExtensionConfiguration
 *  com.azure.ai.openai.models.ChatChoice
 *  com.azure.ai.openai.models.ChatCompletions
 *  com.azure.ai.openai.models.ChatCompletionsOptions
 *  com.azure.ai.openai.models.CompletionsFinishReason
 *  com.azure.ai.openai.models.ReasoningEffortValue
 *  com.azure.core.credential.KeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.core.http.HttpClientProvider
 *  com.azure.core.http.ProxyOptions
 *  com.azure.core.http.policy.RetryOptions
 *  dev.langchain4j.exception.ContentFilteredException
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
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
 */
package dev.langchain4j.model.azure;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.AzureChatEnhancementConfiguration;
import com.azure.ai.openai.models.AzureChatExtensionConfiguration;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.CompletionsFinishReason;
import com.azure.ai.openai.models.ReasoningEffortValue;
import com.azure.core.credential.KeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.core.http.HttpClientProvider;
import com.azure.core.http.ProxyOptions;
import com.azure.core.http.policy.RetryOptions;
import dev.langchain4j.exception.ContentFilteredException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.azure.AzureOpenAiExceptionMapper;
import dev.langchain4j.model.azure.InternalAzureOpenAiHelper;
import dev.langchain4j.model.azure.spi.AzureOpenAiChatModelBuilderFactory;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AzureOpenAiChatModel
implements ChatModel {
    private final OpenAIClient client;
    private final ChatRequestParameters defaultRequestParameters;
    private final Map<String, Integer> logitBias;
    private final String user;
    private final List<AzureChatExtensionConfiguration> dataSources;
    private final AzureChatEnhancementConfiguration enhancements;
    private final Long seed;
    private final Boolean strictJsonSchema;
    private final Integer maxCompletionTokens;
    private final ReasoningEffortValue reasoningEffort;
    private final List<ChatModelListener> listeners;
    private final Set<Capability> supportedCapabilities;

    public AzureOpenAiChatModel(Builder builder) {
        ChatRequestParameters parameters;
        this.client = builder.openAIClient == null ? (builder.tokenCredential != null ? InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.tokenCredential, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders) : (builder.keyCredential != null ? InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.keyCredential, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders) : InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.apiKey, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders))) : (OpenAIClient)ValidationUtils.ensureNotNull((Object)builder.openAIClient, (String)"openAIClient");
        if (builder.defaultRequestParameters != null) {
            InternalAzureOpenAiHelper.validate(builder.defaultRequestParameters);
            parameters = builder.defaultRequestParameters;
        } else {
            parameters = DefaultChatRequestParameters.EMPTY;
        }
        this.defaultRequestParameters = ChatRequestParameters.builder().modelName((String)Utils.getOrDefault((Object)builder.deploymentName, (Object)parameters.modelName())).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)parameters.temperature())).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)parameters.topP())).frequencyPenalty((Double)Utils.getOrDefault((Object)builder.frequencyPenalty, (Object)parameters.frequencyPenalty())).presencePenalty((Double)Utils.getOrDefault((Object)builder.presencePenalty, (Object)parameters.presencePenalty())).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxTokens, (Object)parameters.maxOutputTokens())).stopSequences(Utils.getOrDefault((List)builder.stop, (List)parameters.stopSequences())).toolSpecifications(parameters.toolSpecifications()).toolChoice(parameters.toolChoice()).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)parameters.responseFormat())).build();
        this.logitBias = Utils.copy((Map)builder.logitBias);
        this.user = builder.user;
        this.dataSources = Utils.copyIfNotNull((List)builder.dataSources);
        this.enhancements = builder.enhancements;
        this.seed = builder.seed;
        this.strictJsonSchema = (Boolean)Utils.getOrDefault((Object)builder.strictJsonSchema, (Object)false);
        this.maxCompletionTokens = builder.maxCompletionTokens;
        this.reasoningEffort = builder.reasoningEffort;
        this.listeners = Utils.copy((List)builder.listeners);
        this.supportedCapabilities = Utils.copy((Set)builder.supportedCapabilities);
    }

    public ChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public Set<Capability> supportedCapabilities() {
        return this.supportedCapabilities;
    }

    public ChatResponse doChat(ChatRequest request) {
        ChatCompletions chatCompletions;
        ChatChoice chatChoice;
        ChatRequestParameters parameters = request.parameters();
        InternalAzureOpenAiHelper.validate(parameters);
        ChatCompletionsOptions options = new ChatCompletionsOptions(InternalAzureOpenAiHelper.toOpenAiMessages(request.messages())).setModel(parameters.modelName()).setTemperature(parameters.temperature()).setTopP(parameters.topP()).setFrequencyPenalty(parameters.frequencyPenalty()).setPresencePenalty(parameters.presencePenalty()).setMaxTokens(parameters.maxOutputTokens()).setMaxCompletionTokens(this.maxCompletionTokens).setStop(parameters.stopSequences().isEmpty() ? null : parameters.stopSequences()).setResponseFormat(InternalAzureOpenAiHelper.toAzureOpenAiResponseFormat(parameters.responseFormat(), this.strictJsonSchema)).setLogitBias(this.logitBias.isEmpty() ? null : this.logitBias).setUser(this.user).setDataSources(this.dataSources).setEnhancements(this.enhancements).setSeed(this.seed).setReasoningEffort(this.reasoningEffort);
        if (!parameters.toolSpecifications().isEmpty()) {
            options.setTools(InternalAzureOpenAiHelper.toToolDefinitions(parameters.toolSpecifications()));
        }
        if (parameters.toolChoice() != null) {
            options.setToolChoice(InternalAzureOpenAiHelper.toToolChoice(parameters.toolChoice()));
        }
        if ((chatChoice = (ChatChoice)(chatCompletions = (ChatCompletions)AzureOpenAiExceptionMapper.INSTANCE.withExceptionMapper(() -> this.client.getChatCompletions(parameters.modelName(), options))).getChoices().get(0)).getFinishReason() == CompletionsFinishReason.CONTENT_FILTERED) {
            String details;
            try {
                details = chatChoice.getContentFilterResults().toJsonString();
            }
            catch (Exception ignored) {
                details = "The content has been filtered, and no additional information is available.";
            }
            throw new ContentFilteredException(details);
        }
        return ChatResponse.builder().aiMessage(InternalAzureOpenAiHelper.aiMessageFrom(chatChoice.getMessage())).metadata(ChatResponseMetadata.builder().id(chatCompletions.getId()).modelName(chatCompletions.getModel()).tokenUsage(InternalAzureOpenAiHelper.tokenUsageFrom(chatCompletions.getUsage())).finishReason(InternalAzureOpenAiHelper.finishReasonFrom(chatChoice.getFinishReason())).build()).build();
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.AZURE_OPEN_AI;
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(AzureOpenAiChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            AzureOpenAiChatModelBuilderFactory factory = (AzureOpenAiChatModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public static class Builder {
        private ChatRequestParameters defaultRequestParameters;
        private String endpoint;
        private String serviceVersion;
        private String apiKey;
        private KeyCredential keyCredential;
        private TokenCredential tokenCredential;
        private HttpClientProvider httpClientProvider;
        private String deploymentName;
        private Integer maxTokens;
        private Integer maxCompletionTokens;
        private Double temperature;
        private Double topP;
        private Map<String, Integer> logitBias;
        private String user;
        private List<String> stop;
        private Double presencePenalty;
        private Double frequencyPenalty;
        private List<AzureChatExtensionConfiguration> dataSources;
        private AzureChatEnhancementConfiguration enhancements;
        private Long seed;
        private ResponseFormat responseFormat;
        private Boolean strictJsonSchema;
        private Duration timeout;
        private Integer maxRetries;
        private RetryOptions retryOptions;
        private ProxyOptions proxyOptions;
        private boolean logRequestsAndResponses;
        private OpenAIClient openAIClient;
        private String userAgentSuffix;
        private List<ChatModelListener> listeners;
        private Map<String, String> customHeaders;
        private Set<Capability> supportedCapabilities;
        private ReasoningEffortValue reasoningEffort;

        public Builder defaultRequestParameters(ChatRequestParameters parameters) {
            this.defaultRequestParameters = parameters;
            return this;
        }

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder serviceVersion(String serviceVersion) {
            this.serviceVersion = serviceVersion;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder nonAzureApiKey(String nonAzureApiKey) {
            this.keyCredential = new KeyCredential(nonAzureApiKey);
            this.endpoint = "https://api.openai.com/v1";
            return this;
        }

        public Builder tokenCredential(TokenCredential tokenCredential) {
            this.tokenCredential = tokenCredential;
            return this;
        }

        public Builder httpClientProvider(HttpClientProvider httpClientProvider) {
            this.httpClientProvider = httpClientProvider;
            return this;
        }

        public Builder deploymentName(String deploymentName) {
            this.deploymentName = deploymentName;
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder maxCompletionTokens(Integer maxCompletionTokens) {
            this.maxCompletionTokens = maxCompletionTokens;
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

        public Builder logitBias(Map<String, Integer> logitBias) {
            this.logitBias = logitBias;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder stop(List<String> stop) {
            this.stop = stop;
            return this;
        }

        public Builder presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public Builder frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public Builder dataSources(List<AzureChatExtensionConfiguration> dataSources) {
            this.dataSources = dataSources;
            return this;
        }

        public Builder enhancements(AzureChatEnhancementConfiguration enhancements) {
            this.enhancements = enhancements;
            return this;
        }

        public Builder seed(Long seed) {
            this.seed = seed;
            return this;
        }

        public Builder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public Builder strictJsonSchema(Boolean strictJsonSchema) {
            this.strictJsonSchema = strictJsonSchema;
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

        public Builder retryOptions(RetryOptions retryOptions) {
            this.retryOptions = retryOptions;
            return this;
        }

        public Builder proxyOptions(ProxyOptions proxyOptions) {
            this.proxyOptions = proxyOptions;
            return this;
        }

        public Builder logRequestsAndResponses(Boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
            return this;
        }

        public Builder userAgentSuffix(String userAgentSuffix) {
            this.userAgentSuffix = userAgentSuffix;
            return this;
        }

        public Builder openAIClient(OpenAIClient openAIClient) {
            this.openAIClient = openAIClient;
            return this;
        }

        public Builder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public Builder supportedCapabilities(Set<Capability> supportedCapabilities) {
            this.supportedCapabilities = supportedCapabilities;
            return this;
        }

        public Builder supportedCapabilities(Capability ... supportedCapabilities) {
            return this.supportedCapabilities(new HashSet<Capability>(Arrays.asList(supportedCapabilities)));
        }

        public Builder reasoningEffort(ReasoningEffortValue reasoningEffort) {
            this.reasoningEffort = reasoningEffort;
            return this;
        }

        public AzureOpenAiChatModel build() {
            return new AzureOpenAiChatModel(this);
        }
    }
}

