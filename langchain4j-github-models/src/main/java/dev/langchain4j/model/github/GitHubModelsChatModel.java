/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.inference.ChatCompletionsClient
 *  com.azure.ai.inference.ModelServiceVersion
 *  com.azure.ai.inference.models.ChatChoice
 *  com.azure.ai.inference.models.ChatCompletions
 *  com.azure.ai.inference.models.ChatCompletionsOptions
 *  com.azure.ai.inference.models.ChatCompletionsResponseFormat
 *  com.azure.ai.inference.models.ChatCompletionsResponseFormatJsonSchema
 *  com.azure.core.exception.HttpResponseException
 *  com.azure.core.http.ProxyOptions
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.ChatRequestValidationUtils
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
package dev.langchain4j.model.github;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ModelServiceVersion;
import com.azure.ai.inference.models.ChatChoice;
import com.azure.ai.inference.models.ChatCompletions;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.ChatCompletionsResponseFormat;
import com.azure.ai.inference.models.ChatCompletionsResponseFormatJsonSchema;
import com.azure.core.exception.HttpResponseException;
import com.azure.core.http.ProxyOptions;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.ChatRequestValidationUtils;
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
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.github.GitHubModelsChatModelName;
import dev.langchain4j.model.github.InternalGitHubModelHelper;
import dev.langchain4j.model.github.spi.GitHubModelsChatModelBuilderFactory;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class GitHubModelsChatModel
implements ChatModel {
    private static final Logger logger = LoggerFactory.getLogger(GitHubModelsChatModel.class);
    private ChatCompletionsClient client;
    private final String modelName;
    private final Integer maxTokens;
    private final Double temperature;
    private final Double topP;
    private final List<String> stop;
    private final Double presencePenalty;
    private final Double frequencyPenalty;
    private final Long seed;
    private final ChatCompletionsResponseFormat responseFormat;
    private final boolean strictJsonSchema;
    private Set<Capability> supportedCapabilities;
    private final List<ChatModelListener> listeners;

    private GitHubModelsChatModel(ChatCompletionsClient client, String modelName, Integer maxTokens, Double temperature, Double topP, List<String> stop, Double presencePenalty, Double frequencyPenalty, Long seed, ChatCompletionsResponseFormat responseFormat, boolean strictJsonSchema, Set<Capability> supportedCapabilities, List<ChatModelListener> listeners) {
        this(modelName, maxTokens, temperature, topP, stop, presencePenalty, frequencyPenalty, seed, responseFormat, strictJsonSchema, supportedCapabilities, listeners);
        this.client = client;
    }

    private GitHubModelsChatModel(String endpoint, ModelServiceVersion serviceVersion, String gitHubToken, String modelName, Integer maxTokens, Double temperature, Double topP, List<String> stop, Double presencePenalty, Double frequencyPenalty, Long seed, ChatCompletionsResponseFormat responseFormat, boolean strictJsonSchema, Set<Capability> capabilities, Duration timeout, Integer maxRetries, ProxyOptions proxyOptions, boolean logRequestsAndResponses, List<ChatModelListener> listeners, String userAgentSuffix, Map<String, String> customHeaders) {
        this(modelName, maxTokens, temperature, topP, stop, presencePenalty, frequencyPenalty, seed, responseFormat, strictJsonSchema, capabilities, listeners);
        this.client = InternalGitHubModelHelper.setupChatCompletionsBuilder(endpoint, serviceVersion, gitHubToken, timeout, maxRetries, proxyOptions, logRequestsAndResponses, userAgentSuffix, customHeaders).buildClient();
    }

    private GitHubModelsChatModel(String modelName, Integer maxTokens, Double temperature, Double topP, List<String> stop, Double presencePenalty, Double frequencyPenalty, Long seed, ChatCompletionsResponseFormat responseFormat, boolean strictJsonSchema, Set<Capability> supportedCapabilities, List<ChatModelListener> listeners) {
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.topP = topP;
        this.stop = Utils.copyIfNotNull(stop);
        this.presencePenalty = presencePenalty;
        this.frequencyPenalty = frequencyPenalty;
        this.seed = seed;
        this.responseFormat = responseFormat;
        this.strictJsonSchema = strictJsonSchema;
        this.supportedCapabilities = Utils.copy(supportedCapabilities);
        this.listeners = listeners == null ? Collections.emptyList() : new ArrayList<ChatModelListener>(listeners);
    }

    public Set<Capability> supportedCapabilities() {
        HashSet<Capability> capabilities = new HashSet<Capability>(this.supportedCapabilities);
        if (this.responseFormat instanceof ChatCompletionsResponseFormatJsonSchema) {
            capabilities.add(Capability.RESPONSE_FORMAT_JSON_SCHEMA);
        }
        return capabilities;
    }

    public ChatResponse chat(ChatRequest chatRequest) {
        Response<AiMessage> response;
        ChatRequestParameters parameters = chatRequest.parameters();
        ChatRequestValidationUtils.validateParameters((ChatRequestParameters)parameters);
        List<ToolSpecification> toolSpecifications = parameters.toolSpecifications();
        ChatCompletionsResponseFormat responseFormat = (ChatCompletionsResponseFormat)Utils.getOrDefault((Object)InternalGitHubModelHelper.toChatCompletionsResponseFormat(chatRequest.responseFormat(), this.strictJsonSchema), (Object)this.responseFormat);
        if (Utils.isNullOrEmpty(toolSpecifications)) {
            response = this.generate(chatRequest.messages(), null, null, responseFormat);
        } else if (parameters.toolChoice() == ToolChoice.REQUIRED) {
            if (toolSpecifications.size() != 1) {
                throw new UnsupportedFeatureException(String.format("%s.%s is currently supported only when there is a single tool", ToolChoice.class.getSimpleName(), ToolChoice.REQUIRED.name()));
            }
            response = this.generate(chatRequest.messages(), Collections.singletonList(toolSpecifications.get(0)), toolSpecifications.get(0), responseFormat);
        } else {
            response = this.generate(chatRequest.messages(), toolSpecifications, null, responseFormat);
        }
        return ChatResponse.builder().aiMessage((AiMessage)response.content()).metadata(ChatResponseMetadata.builder().tokenUsage(response.tokenUsage()).finishReason(response.finishReason()).build()).build();
    }

    private Response<AiMessage> generate(List<ChatMessage> messages) {
        return this.generate(messages, null, null, this.responseFormat);
    }

    private Response<AiMessage> generate(List<ChatMessage> messages, List<ToolSpecification> toolSpecifications) {
        return this.generate(messages, toolSpecifications, null, this.responseFormat);
    }

    private Response<AiMessage> generate(List<ChatMessage> messages, ToolSpecification toolSpecification) {
        return this.generate(messages, Collections.singletonList(toolSpecification), toolSpecification, this.responseFormat);
    }

    private Response<AiMessage> generate(List<ChatMessage> messages, List<ToolSpecification> toolSpecifications, ToolSpecification toolThatMustBeExecuted, ChatCompletionsResponseFormat responseFormat) {
        ChatCompletionsOptions options = new ChatCompletionsOptions(InternalGitHubModelHelper.toAzureAiMessages(messages)).setModel(this.modelName).setMaxTokens(this.maxTokens).setTemperature(this.temperature).setTopP(this.topP).setStop(this.stop).setPresencePenalty(this.presencePenalty).setFrequencyPenalty(this.frequencyPenalty).setSeed(this.seed).setResponseFormat(responseFormat);
        if (toolThatMustBeExecuted != null) {
            options.setTools(InternalGitHubModelHelper.toToolDefinitions(Collections.singletonList(toolThatMustBeExecuted)));
            options.setToolChoice(InternalGitHubModelHelper.toToolChoice(toolThatMustBeExecuted));
        }
        if (!Utils.isNullOrEmpty(toolSpecifications)) {
            options.setTools(InternalGitHubModelHelper.toToolDefinitions(toolSpecifications));
        }
        ChatRequest listenerRequest = InternalGitHubModelHelper.createListenerRequest(options, messages, toolSpecifications);
        ConcurrentHashMap attributes = new ConcurrentHashMap();
        ChatModelRequestContext requestContext = new ChatModelRequestContext(listenerRequest, this.provider(), attributes);
        this.listeners.forEach(listener -> {
            try {
                listener.onRequest(requestContext);
            }
            catch (Exception e) {
                logger.warn("Exception while calling model listener", (Throwable)e);
            }
        });
        try {
            ChatCompletions chatCompletions = this.client.complete(options);
            Response response = Response.from(InternalGitHubModelHelper.aiMessageFrom(((ChatChoice)chatCompletions.getChoices().get(0)).getMessage()), (TokenUsage)InternalGitHubModelHelper.tokenUsageFrom(chatCompletions.getUsage()), (FinishReason)InternalGitHubModelHelper.finishReasonFrom(((ChatChoice)chatCompletions.getChoices().get(0)).getFinishReason()));
            ChatResponse listenerResponse = InternalGitHubModelHelper.createListenerResponse(chatCompletions.getId(), options.getModel(), (Response<AiMessage>)response);
            ChatModelResponseContext responseContext = new ChatModelResponseContext(listenerResponse, listenerRequest, this.provider(), attributes);
            this.listeners.forEach(listener -> {
                try {
                    listener.onResponse(responseContext);
                }
                catch (Exception e) {
                    logger.warn("Exception while calling model listener", (Throwable)e);
                }
            });
            return response;
        }
        catch (HttpResponseException httpResponseException) {
            logger.info("Error generating response, {}", httpResponseException.getValue());
            ChatModelErrorContext errorContext = new ChatModelErrorContext((Throwable)httpResponseException, listenerRequest, this.provider(), attributes);
            this.listeners.forEach(listener -> {
                try {
                    listener.onError(errorContext);
                }
                catch (Exception e2) {
                    logger.warn("Exception while calling model listener", (Throwable)e2);
                }
            });
            FinishReason exceptionFinishReason = InternalGitHubModelHelper.contentFilterManagement(httpResponseException, "content_filter");
            if (exceptionFinishReason != FinishReason.CONTENT_FILTER) {
                throw httpResponseException;
            }
            return Response.from(AiMessage.aiMessage((String)httpResponseException.getMessage()), null, (FinishReason)exceptionFinishReason);
        }
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.GITHUB_MODELS;
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(GitHubModelsChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            GitHubModelsChatModelBuilderFactory factory = (GitHubModelsChatModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public static class Builder {
        private String endpoint;
        private ModelServiceVersion serviceVersion;
        private String gitHubToken;
        private String modelName;
        private Integer maxTokens;
        private Double temperature;
        private Double topP;
        private List<String> stop;
        private Double presencePenalty;
        private Double frequencyPenalty;
        private Long seed;
        private ChatCompletionsResponseFormat responseFormat;
        private boolean strictJsonSchema;
        private Set<Capability> supportedCapabilities;
        private Duration timeout;
        private Integer maxRetries;
        private ProxyOptions proxyOptions;
        private boolean logRequestsAndResponses;
        private ChatCompletionsClient chatCompletionsClient;
        private String userAgentSuffix;
        private List<ChatModelListener> listeners;
        private Map<String, String> customHeaders;

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder serviceVersion(ModelServiceVersion serviceVersion) {
            this.serviceVersion = serviceVersion;
            return this;
        }

        public Builder gitHubToken(String gitHubToken) {
            this.gitHubToken = gitHubToken;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder modelName(GitHubModelsChatModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
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

        public Builder seed(Long seed) {
            this.seed = seed;
            return this;
        }

        public Builder responseFormat(ChatCompletionsResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public Builder strictJsonSchema(boolean strictJsonSchema) {
            this.strictJsonSchema = strictJsonSchema;
            return this;
        }

        public Builder supportedCapabilities(Set<Capability> supportedCapabilities) {
            this.supportedCapabilities = supportedCapabilities;
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

        public Builder chatCompletionsClient(ChatCompletionsClient chatCompletionsClient) {
            this.chatCompletionsClient = chatCompletionsClient;
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

        public GitHubModelsChatModel build() {
            if (this.chatCompletionsClient == null) {
                return new GitHubModelsChatModel(this.endpoint, this.serviceVersion, this.gitHubToken, this.modelName, this.maxTokens, this.temperature, this.topP, this.stop, this.presencePenalty, this.frequencyPenalty, this.seed, this.responseFormat, this.strictJsonSchema, this.supportedCapabilities, this.timeout, this.maxRetries, this.proxyOptions, this.logRequestsAndResponses, this.listeners, this.userAgentSuffix, this.customHeaders);
            }
            return new GitHubModelsChatModel(this.chatCompletionsClient, this.modelName, this.maxTokens, this.temperature, this.topP, this.stop, this.presencePenalty, this.frequencyPenalty, this.seed, this.responseFormat, this.strictJsonSchema, this.supportedCapabilities, this.listeners);
        }
    }
}

