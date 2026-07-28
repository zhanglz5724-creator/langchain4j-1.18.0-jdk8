/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.openai.OpenAIAsyncClient
 *  com.azure.ai.openai.implementation.accesshelpers.ChatCompletionsOptionsAccessHelper
 *  com.azure.ai.openai.models.AzureChatEnhancementConfiguration
 *  com.azure.ai.openai.models.AzureChatExtensionConfiguration
 *  com.azure.ai.openai.models.ChatChoice
 *  com.azure.ai.openai.models.ChatCompletionStreamOptions
 *  com.azure.ai.openai.models.ChatCompletions
 *  com.azure.ai.openai.models.ChatCompletionsFunctionToolCall
 *  com.azure.ai.openai.models.ChatCompletionsOptions
 *  com.azure.ai.openai.models.ChatCompletionsToolCall
 *  com.azure.ai.openai.models.ChatResponseMessage
 *  com.azure.ai.openai.models.ReasoningEffortValue
 *  com.azure.core.credential.KeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.core.http.HttpClientProvider
 *  com.azure.core.http.ProxyOptions
 *  com.azure.core.http.policy.RetryOptions
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils
 *  dev.langchain4j.internal.ToolCallBuilder
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
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.PartialToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 *  reactor.core.Disposable
 *  reactor.core.publisher.Flux
 */
package dev.langchain4j.model.azure;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.implementation.accesshelpers.ChatCompletionsOptionsAccessHelper;
import com.azure.ai.openai.models.AzureChatEnhancementConfiguration;
import com.azure.ai.openai.models.AzureChatExtensionConfiguration;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletionStreamOptions;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsFunctionToolCall;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatCompletionsToolCall;
import com.azure.ai.openai.models.ChatResponseMessage;
import com.azure.ai.openai.models.ReasoningEffortValue;
import com.azure.core.credential.KeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.core.http.HttpClientProvider;
import com.azure.core.http.ProxyOptions;
import com.azure.core.http.policy.RetryOptions;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils;
import dev.langchain4j.internal.ToolCallBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.azure.AzureOpenAiExceptionMapper;
import dev.langchain4j.model.azure.AzureOpenAiStreamingHandle;
import dev.langchain4j.model.azure.AzureOpenAiStreamingResponseBuilder;
import dev.langchain4j.model.azure.InternalAzureOpenAiHelper;
import dev.langchain4j.model.azure.spi.AzureOpenAiStreamingChatModelBuilderFactory;
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
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.StreamingHandle;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

public class AzureOpenAiStreamingChatModel
implements StreamingChatModel {
    private final OpenAIAsyncClient client;
    private final ChatRequestParameters defaultRequestParameters;
    private final Map<String, Integer> logitBias;
    private final String user;
    private final List<AzureChatExtensionConfiguration> dataSources;
    private final AzureChatEnhancementConfiguration enhancements;
    private final Long seed;
    private final boolean strictJsonSchema;
    private final Integer maxCompletionTokens;
    private final ReasoningEffortValue reasoningEffort;
    private final List<ChatModelListener> listeners;
    private final Set<Capability> supportedCapabilities;

    public AzureOpenAiStreamingChatModel(Builder builder) {
        ChatRequestParameters parameters;
        this.client = builder.openAIAsyncClient == null ? (builder.tokenCredential != null ? InternalAzureOpenAiHelper.setupAsyncClient(builder.endpoint, builder.serviceVersion, builder.tokenCredential, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders) : (builder.keyCredential != null ? InternalAzureOpenAiHelper.setupAsyncClient(builder.endpoint, builder.serviceVersion, builder.keyCredential, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders) : InternalAzureOpenAiHelper.setupAsyncClient(builder.endpoint, builder.serviceVersion, builder.apiKey, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders))) : (OpenAIAsyncClient)ValidationUtils.ensureNotNull((Object)builder.openAIAsyncClient, (String)"openAIAsyncClient");
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

    public void doChat(ChatRequest request, StreamingChatResponseHandler handler) {
        ChatRequestParameters parameters = request.parameters();
        InternalAzureOpenAiHelper.validate(parameters);
        ChatCompletionsOptions options = new ChatCompletionsOptions(InternalAzureOpenAiHelper.toOpenAiMessages(request.messages())).setModel(parameters.modelName()).setTemperature(parameters.temperature()).setTopP(parameters.topP()).setFrequencyPenalty(parameters.frequencyPenalty()).setPresencePenalty(parameters.presencePenalty()).setMaxTokens(parameters.maxOutputTokens()).setMaxCompletionTokens(this.maxCompletionTokens).setStop(parameters.stopSequences().isEmpty() ? null : parameters.stopSequences()).setResponseFormat(InternalAzureOpenAiHelper.toAzureOpenAiResponseFormat(parameters.responseFormat(), this.strictJsonSchema)).setLogitBias(this.logitBias.isEmpty() ? null : this.logitBias).setUser(this.user).setDataSources(this.dataSources).setEnhancements(this.enhancements).setSeed(this.seed).setReasoningEffort(this.reasoningEffort);
        ChatCompletionStreamOptions streamOptions = new ChatCompletionStreamOptions().setIncludeUsage(Boolean.valueOf(true));
        ChatCompletionsOptionsAccessHelper.setStreamOptions((ChatCompletionsOptions)options, (ChatCompletionStreamOptions)streamOptions);
        if (!parameters.toolSpecifications().isEmpty()) {
            options.setTools(InternalAzureOpenAiHelper.toToolDefinitions(parameters.toolSpecifications()));
        }
        if (parameters.toolChoice() != null) {
            options.setToolChoice(InternalAzureOpenAiHelper.toToolChoice(parameters.toolChoice()));
        }
        ToolCallBuilder toolCallBuilder = new ToolCallBuilder(-1);
        AzureOpenAiStreamingResponseBuilder responseBuilder = new AzureOpenAiStreamingResponseBuilder(toolCallBuilder);
        Flux chatCompletionsStream = this.client.getChatCompletionsStream(parameters.modelName(), options);
        AtomicReference responseId = new AtomicReference();
        AtomicReference responseModelName = new AtomicReference();
        AtomicReference<AzureOpenAiStreamingHandle> streamingHandle = new AtomicReference<AzureOpenAiStreamingHandle>();
        Disposable disposable = chatCompletionsStream.subscribe(chatCompletion -> {
            responseBuilder.append((ChatCompletions)chatCompletion);
            AzureOpenAiStreamingChatModel.handle(chatCompletion, toolCallBuilder, handler, (StreamingHandle)streamingHandle.get());
            if (Utils.isNotNullOrBlank((String)chatCompletion.getId())) {
                responseId.set(chatCompletion.getId());
            }
            if (Utils.isNotNullOrBlank((String)chatCompletion.getModel())) {
                responseModelName.set(chatCompletion.getModel());
            }
        }, error -> {
            RuntimeException mappedError = AzureOpenAiExceptionMapper.INSTANCE.mapException((Throwable)error);
            InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> handler.onError((Throwable)mappedError));
        }, () -> {
            if (toolCallBuilder.hasRequests()) {
                InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)handler, (CompleteToolCall)toolCallBuilder.buildAndReset());
            }
            Response<AiMessage> response = responseBuilder.build();
            ChatResponse chatResponse = ChatResponse.builder().aiMessage((AiMessage)response.content()).metadata(ChatResponseMetadata.builder().id((String)responseId.get()).modelName((String)responseModelName.get()).tokenUsage(response.tokenUsage()).finishReason(response.finishReason()).build()).build();
            InternalStreamingChatResponseHandlerUtils.onCompleteResponse((StreamingChatResponseHandler)handler, (ChatResponse)chatResponse);
        });
        streamingHandle.set(new AzureOpenAiStreamingHandle(disposable));
    }

    private static void handle(ChatCompletions chatCompletions, ToolCallBuilder toolCallBuilder, StreamingChatResponseHandler handler, StreamingHandle streamingHandle) {
        List toolCalls;
        List choices = chatCompletions.getChoices();
        if (Utils.isNullOrEmpty((Collection)choices)) {
            return;
        }
        ChatResponseMessage delta = ((ChatChoice)choices.get(0)).getDelta();
        if (delta == null) {
            return;
        }
        String content = delta.getContent();
        if (!Utils.isNullOrEmpty((String)content)) {
            InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)handler, (String)content, (StreamingHandle)streamingHandle);
        }
        if ((toolCalls = delta.getToolCalls()) != null) {
            for (ChatCompletionsToolCall toolCall : toolCalls) {
                if (!(toolCall instanceof ChatCompletionsFunctionToolCall)) continue;
                ChatCompletionsFunctionToolCall functionToolCall = (ChatCompletionsFunctionToolCall)toolCall;
                int index = toolCallBuilder.index();
                if (AzureOpenAiStreamingChatModel.startOfNewToolCall(toolCall)) {
                    if (index > -1) {
                        InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)handler, (CompleteToolCall)toolCallBuilder.buildAndReset());
                    }
                    toolCallBuilder.updateIndex(Integer.valueOf(++index));
                }
                String id = toolCallBuilder.updateId(toolCall.getId());
                String name = toolCallBuilder.updateName(functionToolCall.getFunction().getName());
                String partialArguments = functionToolCall.getFunction().getArguments();
                if (!Utils.isNotNullOrEmpty((String)partialArguments)) continue;
                toolCallBuilder.appendArguments(partialArguments);
                PartialToolCall partialToolRequest = PartialToolCall.builder().index(index).id(id).name(name).partialArguments(partialArguments).build();
                InternalStreamingChatResponseHandlerUtils.onPartialToolCall((StreamingChatResponseHandler)handler, (PartialToolCall)partialToolRequest, (StreamingHandle)streamingHandle);
            }
        }
    }

    private static boolean startOfNewToolCall(ChatCompletionsToolCall toolCall) {
        return toolCall.getId() != null;
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.AZURE_OPEN_AI;
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(AzureOpenAiStreamingChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            AzureOpenAiStreamingChatModelBuilderFactory factory = (AzureOpenAiStreamingChatModelBuilderFactory)iterator.next();
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
        private Duration timeout;
        private List<AzureChatExtensionConfiguration> dataSources;
        private AzureChatEnhancementConfiguration enhancements;
        private Long seed;
        private ResponseFormat responseFormat;
        private Boolean strictJsonSchema;
        private Integer maxRetries;
        private RetryOptions retryOptions;
        private ProxyOptions proxyOptions;
        private boolean logRequestsAndResponses;
        private OpenAIAsyncClient openAIAsyncClient;
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

        public Builder logRequestsAndResponses(boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
            return this;
        }

        public Builder openAIAsyncClient(OpenAIAsyncClient openAIAsyncClient) {
            this.openAIAsyncClient = openAIAsyncClient;
            return this;
        }

        public Builder userAgentSuffix(String userAgentSuffix) {
            this.userAgentSuffix = userAgentSuffix;
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

        public AzureOpenAiStreamingChatModel build() {
            return new AzureOpenAiStreamingChatModel(this);
        }
    }
}

