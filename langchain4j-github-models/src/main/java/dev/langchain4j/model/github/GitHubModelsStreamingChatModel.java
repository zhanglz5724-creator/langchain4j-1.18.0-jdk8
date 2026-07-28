/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.inference.ChatCompletionsAsyncClient
 *  com.azure.ai.inference.ModelServiceVersion
 *  com.azure.ai.inference.models.ChatCompletionsOptions
 *  com.azure.ai.inference.models.ChatCompletionsResponseFormat
 *  com.azure.ai.inference.models.StreamingChatChoiceUpdate
 *  com.azure.ai.inference.models.StreamingChatCompletionsUpdate
 *  com.azure.ai.inference.models.StreamingChatResponseMessageUpdate
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
 *  dev.langchain4j.model.StreamingResponseHandler
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
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  reactor.core.publisher.Flux
 */
package dev.langchain4j.model.github;

import com.azure.ai.inference.ChatCompletionsAsyncClient;
import com.azure.ai.inference.ModelServiceVersion;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.ChatCompletionsResponseFormat;
import com.azure.ai.inference.models.StreamingChatChoiceUpdate;
import com.azure.ai.inference.models.StreamingChatCompletionsUpdate;
import com.azure.ai.inference.models.StreamingChatResponseMessageUpdate;
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
import dev.langchain4j.model.StreamingResponseHandler;
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
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.github.GitHubModelsChatModelName;
import dev.langchain4j.model.github.GitHubModelsStreamingResponseBuilder;
import dev.langchain4j.model.github.InternalGitHubModelHelper;
import dev.langchain4j.model.github.spi.GitHubModelsStreamingChatModelBuilderFactory;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

@Deprecated
public class GitHubModelsStreamingChatModel
implements StreamingChatModel {
    private static final Logger logger = LoggerFactory.getLogger(GitHubModelsStreamingChatModel.class);
    private ChatCompletionsAsyncClient client;
    private final String modelName;
    private final Integer maxTokens;
    private final Double temperature;
    private final Double topP;
    private final List<String> stop;
    private final Double presencePenalty;
    private final Double frequencyPenalty;
    private final Long seed;
    private final ChatCompletionsResponseFormat responseFormat;
    private final List<ChatModelListener> listeners;

    private GitHubModelsStreamingChatModel(ChatCompletionsAsyncClient client, String modelName, Integer maxTokens, Double temperature, Double topP, List<String> stop, Double presencePenalty, Double frequencyPenalty, Long seed, ChatCompletionsResponseFormat responseFormat, List<ChatModelListener> listeners) {
        this(modelName, maxTokens, temperature, topP, stop, presencePenalty, frequencyPenalty, seed, responseFormat, listeners);
        this.client = client;
    }

    private GitHubModelsStreamingChatModel(String endpoint, ModelServiceVersion serviceVersion, String gitHubToken, String modelName, Integer maxTokens, Double temperature, Double topP, List<String> stop, Double presencePenalty, Double frequencyPenalty, Long seed, ChatCompletionsResponseFormat responseFormat, Duration timeout, Integer maxRetries, ProxyOptions proxyOptions, boolean logRequestsAndResponses, List<ChatModelListener> listeners, String userAgentSuffix, Map<String, String> customHeaders) {
        this(modelName, maxTokens, temperature, topP, stop, presencePenalty, frequencyPenalty, seed, responseFormat, listeners);
        this.client = InternalGitHubModelHelper.setupChatCompletionsBuilder(endpoint, serviceVersion, gitHubToken, timeout, maxRetries, proxyOptions, logRequestsAndResponses, userAgentSuffix, customHeaders).buildAsyncClient();
    }

    private GitHubModelsStreamingChatModel(String modelName, Integer maxTokens, Double temperature, Double topP, List<String> stop, Double presencePenalty, Double frequencyPenalty, Long seed, ChatCompletionsResponseFormat responseFormat, List<ChatModelListener> listeners) {
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.topP = topP;
        this.stop = Utils.copyIfNotNull(stop);
        this.presencePenalty = presencePenalty;
        this.frequencyPenalty = frequencyPenalty;
        this.seed = seed;
        this.responseFormat = responseFormat;
        this.listeners = listeners == null ? Collections.emptyList() : new ArrayList<ChatModelListener>(listeners);
    }

    public void chat(ChatRequest request, final StreamingChatResponseHandler handler) {
        ChatRequestParameters parameters = request.parameters();
        ChatRequestValidationUtils.validateParameters((ChatRequestParameters)parameters);
        ChatRequestValidationUtils.validate((ResponseFormat)parameters.responseFormat());
        StreamingResponseHandler<AiMessage> legacyHandler = new StreamingResponseHandler<AiMessage>(){

            public void onNext(String token) {
                handler.onPartialResponse(token);
            }

            public void onComplete(Response<AiMessage> response) {
                ChatResponse chatResponse = ChatResponse.builder().aiMessage((AiMessage)response.content()).metadata(ChatResponseMetadata.builder().tokenUsage(response.tokenUsage()).finishReason(response.finishReason()).build()).build();
                handler.onCompleteResponse(chatResponse);
            }

            public void onError(Throwable error) {
                handler.onError(error);
            }
        };
        List toolSpecifications = parameters.toolSpecifications();
        if (Utils.isNullOrEmpty((Collection)toolSpecifications)) {
            this.generate(request.messages(), legacyHandler);
        } else if (parameters.toolChoice() == ToolChoice.REQUIRED) {
            if (toolSpecifications.size() != 1) {
                throw new UnsupportedFeatureException(String.format("%s.%s is currently supported only when there is a single tool", ToolChoice.class.getSimpleName(), ToolChoice.REQUIRED.name()));
            }
            this.generate((List<ChatMessage>)request.messages(), (ToolSpecification)toolSpecifications.get(0), legacyHandler);
        } else {
            this.generate((List<ChatMessage>)request.messages(), toolSpecifications, legacyHandler);
        }
    }

    private void generate(List<ChatMessage> messages, StreamingResponseHandler<AiMessage> handler) {
        this.generate(messages, null, null, handler);
    }

    private void generate(List<ChatMessage> messages, List<ToolSpecification> toolSpecifications, StreamingResponseHandler<AiMessage> handler) {
        this.generate(messages, toolSpecifications, null, handler);
    }

    private void generate(List<ChatMessage> messages, ToolSpecification toolSpecification, StreamingResponseHandler<AiMessage> handler) {
        this.generate(messages, null, toolSpecification, handler);
    }

    private void generate(List<ChatMessage> messages, List<ToolSpecification> toolSpecifications, ToolSpecification toolThatMustBeExecuted, StreamingResponseHandler<AiMessage> handler) {
        ChatCompletionsOptions options = new ChatCompletionsOptions(InternalGitHubModelHelper.toAzureAiMessages(messages)).setModel(this.modelName).setMaxTokens(this.maxTokens).setTemperature(this.temperature).setTopP(this.topP).setStop(this.stop).setPresencePenalty(this.presencePenalty).setFrequencyPenalty(this.frequencyPenalty).setSeed(this.seed).setResponseFormat(this.responseFormat);
        if (toolThatMustBeExecuted != null) {
            options.setTools(InternalGitHubModelHelper.toToolDefinitions(Collections.singletonList(toolThatMustBeExecuted)));
            options.setToolChoice(InternalGitHubModelHelper.toToolChoice(toolThatMustBeExecuted));
        }
        if (!Utils.isNullOrEmpty(toolSpecifications)) {
            options.setTools(InternalGitHubModelHelper.toToolDefinitions(toolSpecifications));
        }
        GitHubModelsStreamingResponseBuilder responseBuilder = new GitHubModelsStreamingResponseBuilder();
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
        this.asyncCall(handler, options, responseBuilder, requestContext);
    }

    private void handleResponseException(Throwable throwable, StreamingResponseHandler<AiMessage> handler) {
        if (throwable instanceof HttpResponseException) {
            HttpResponseException httpResponseException = (HttpResponseException)throwable;
            logger.info("Error generating response, {}", httpResponseException.getValue());
            FinishReason exceptionFinishReason = InternalGitHubModelHelper.contentFilterManagement(httpResponseException, "content_filter");
            if (exceptionFinishReason == FinishReason.CONTENT_FILTER) {
                Response response = Response.from((Object)AiMessage.aiMessage((String)httpResponseException.getMessage()), null, (FinishReason)exceptionFinishReason);
                handler.onComplete(response);
            } else {
                handler.onError(throwable);
            }
        } else {
            handler.onError(throwable);
        }
    }

    private void asyncCall(StreamingResponseHandler<AiMessage> handler, ChatCompletionsOptions options, GitHubModelsStreamingResponseBuilder responseBuilder, ChatModelRequestContext requestContext) {
        Flux chatCompletionsStream = this.client.completeStream(options);
        AtomicReference responseId = new AtomicReference();
        AtomicReference responseModel = new AtomicReference();
        chatCompletionsStream.subscribe(chatCompletion -> {
            responseBuilder.append((StreamingChatCompletionsUpdate)chatCompletion);
            GitHubModelsStreamingChatModel.handle(chatCompletion, handler);
            if (Utils.isNotNullOrBlank((String)chatCompletion.getId())) {
                responseId.set(chatCompletion.getId());
            }
            if (!Utils.isNullOrBlank((String)chatCompletion.getModel())) {
                responseModel.set(chatCompletion.getModel());
            }
        }, throwable -> {
            ChatModelErrorContext errorContext = new ChatModelErrorContext(throwable, requestContext.chatRequest(), this.provider(), requestContext.attributes());
            this.listeners.forEach(listener -> {
                try {
                    listener.onError(errorContext);
                }
                catch (Exception e2) {
                    logger.warn("Exception while calling model listener", (Throwable)e2);
                }
            });
            this.handleResponseException((Throwable)throwable, handler);
        }, () -> {
            Response<AiMessage> response = responseBuilder.build();
            ChatResponse listenerResponse = InternalGitHubModelHelper.createListenerResponse((String)responseId.get(), options.getModel(), response);
            ChatModelResponseContext responseContext = new ChatModelResponseContext(listenerResponse, requestContext.chatRequest(), this.provider(), requestContext.attributes());
            this.listeners.forEach(listener -> {
                try {
                    listener.onResponse(responseContext);
                }
                catch (Exception e) {
                    logger.warn("Exception while calling model listener", (Throwable)e);
                }
            });
            handler.onComplete(response);
        });
    }

    private static void handle(StreamingChatCompletionsUpdate chatCompletions, StreamingResponseHandler<AiMessage> handler) {
        List choices = chatCompletions.getChoices();
        if (Utils.isNullOrEmpty((Collection)choices)) {
            return;
        }
        StreamingChatResponseMessageUpdate message = ((StreamingChatChoiceUpdate)choices.get(0)).getDelta();
        if (message != null && message.getContent() != null) {
            handler.onNext(message.getContent());
        }
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.GITHUB_MODELS;
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(GitHubModelsStreamingChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            GitHubModelsStreamingChatModelBuilderFactory factory = (GitHubModelsStreamingChatModelBuilderFactory)iterator.next();
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
        private Duration timeout;
        private Long seed;
        private ChatCompletionsResponseFormat responseFormat;
        private Integer maxRetries;
        private ProxyOptions proxyOptions;
        private boolean logRequestsAndResponses;
        private ChatCompletionsAsyncClient client;
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

        public Builder logRequestsAndResponses(boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
            return this;
        }

        public Builder chatCompletionsAsyncClient(ChatCompletionsAsyncClient client) {
            this.client = client;
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

        public GitHubModelsStreamingChatModel build() {
            if (this.client != null) {
                return new GitHubModelsStreamingChatModel(this.client, this.modelName, this.maxTokens, this.temperature, this.topP, this.stop, this.presencePenalty, this.frequencyPenalty, this.seed, this.responseFormat, this.listeners);
            }
            return new GitHubModelsStreamingChatModel(this.endpoint, this.serviceVersion, this.gitHubToken, this.modelName, this.maxTokens, this.temperature, this.topP, this.stop, this.presencePenalty, this.frequencyPenalty, this.seed, this.responseFormat, this.timeout, this.maxRetries, this.proxyOptions, this.logRequestsAndResponses, this.listeners, this.userAgentSuffix, this.customHeaders);
        }
    }
}

