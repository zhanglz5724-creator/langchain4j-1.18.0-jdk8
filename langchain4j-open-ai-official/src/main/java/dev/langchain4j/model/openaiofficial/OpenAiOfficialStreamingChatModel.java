/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.openai.azure.AzureOpenAIServiceVersion
 *  com.openai.client.OpenAIClientAsync
 *  com.openai.core.http.AsyncStreamResponse
 *  com.openai.core.http.AsyncStreamResponse$Handler
 *  com.openai.credential.Credential
 *  com.openai.models.ChatModel
 *  com.openai.models.chat.completions.ChatCompletionChunk
 *  com.openai.models.chat.completions.ChatCompletionChunk$Choice$Delta$ToolCall
 *  com.openai.models.chat.completions.ChatCompletionChunk$Choice$Delta$ToolCall$Function
 *  com.openai.models.chat.completions.ChatCompletionChunk$Choice$FinishReason
 *  com.openai.models.chat.completions.ChatCompletionChunk$ServiceTier
 *  com.openai.models.chat.completions.ChatCompletionCreateParams
 *  com.openai.models.chat.completions.ChatCompletionStreamOptions
 *  com.openai.models.completions.CompletionUsage
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils
 *  dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler
 *  dev.langchain4j.internal.ToolCallBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.TokenCountEstimator
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.PartialToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingHandle
 */
package dev.langchain4j.model.openaiofficial;

import com.openai.azure.AzureOpenAIServiceVersion;
import com.openai.client.OpenAIClientAsync;
import com.openai.core.http.AsyncStreamResponse;
import com.openai.credential.Credential;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionChunk;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionStreamOptions;
import com.openai.models.completions.CompletionUsage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils;
import dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler;
import dev.langchain4j.internal.ToolCallBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.StreamingHandle;
import dev.langchain4j.model.openaiofficial.InternalOpenAiOfficialHelper;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialBaseChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatRequestParameters;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatResponseMetadata;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialStreamingHandle;
import java.net.Proxy;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class OpenAiOfficialStreamingChatModel
extends OpenAiOfficialBaseChatModel
implements StreamingChatModel {
    public OpenAiOfficialStreamingChatModel(Builder builder) {
        this.asyncClient = builder.openAIClientAsync;
        this.init(builder.baseUrl, builder.apiKey, builder.credential, builder.microsoftFoundryDeploymentName, builder.azureOpenAIServiceVersion, builder.organizationId, builder.isMicrosoftFoundry, builder.isGitHubModels, builder.defaultRequestParameters, builder.modelName, builder.temperature, builder.topP, builder.stop, builder.maxCompletionTokens, builder.presencePenalty, builder.frequencyPenalty, builder.logitBias, builder.responseFormat, builder.strictJsonSchema, builder.seed, builder.user, builder.strictTools, builder.parallelToolCalls, builder.store, builder.metadata, builder.serviceTier, builder.timeout, builder.maxRetries, builder.proxy, builder.tokenCountEstimator, builder.customHeaders, builder.listeners, builder.capabilities, true);
        this.modelName = builder.modelName;
    }

    public void doChat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
        OpenAiOfficialChatRequestParameters parameters = (OpenAiOfficialChatRequestParameters)chatRequest.parameters();
        InternalOpenAiOfficialHelper.validate((ChatRequestParameters)parameters);
        ChatCompletionCreateParams chatCompletionCreateParams = InternalOpenAiOfficialHelper.toOpenAiChatCompletionCreateParams(chatRequest, parameters, this.strictTools, this.strictJsonSchema).streamOptions(ChatCompletionStreamOptions.builder().includeUsage(true).build()).build();
        if ((this.modelProvider.equals((Object)ModelProvider.MICROSOFT_FOUNDRY) || this.modelProvider.equals((Object)ModelProvider.GITHUB_MODELS)) && !parameters.modelName().equals(this.modelName)) {
            throw new UnsupportedFeatureException("Modifying the modelName is not supported");
        }
        try {
            final OpenAiOfficialChatResponseMetadata.Builder responseMetadataBuilder = OpenAiOfficialChatResponseMetadata.builder();
            final StringBuffer textBuilder = new StringBuffer();
            final ToolCallBuilder toolCallBuilder = new ToolCallBuilder();
            final AtomicReference<OpenAiOfficialStreamingHandle> streamingHandle = new AtomicReference<OpenAiOfficialStreamingHandle>();
            final MappingTrackingStreamingChatResponseHandler trackingHandler = new MappingTrackingStreamingChatResponseHandler(handler);
            AsyncStreamResponse asyncStreamResponse = this.asyncClient.chat().completions().createStreaming(chatCompletionCreateParams).subscribe((AsyncStreamResponse.Handler)new AsyncStreamResponse.Handler<ChatCompletionChunk>(){

                public void onNext(ChatCompletionChunk completion) {
                    trackingHandler.resetMappingTracking();
                    OpenAiOfficialStreamingChatModel.this.manageChatCompletionChunks(completion, (StreamingChatResponseHandler)trackingHandler, (StreamingHandle)streamingHandle.get(), responseMetadataBuilder, textBuilder, toolCallBuilder);
                    if (!trackingHandler.wasMapped()) {
                        InternalStreamingChatResponseHandlerUtils.onUnmappedRawEvent((StreamingChatResponseHandler)trackingHandler, (Object)completion);
                    }
                }

                public void onComplete(Optional<Throwable> error) {
                    if (((StreamingHandle)streamingHandle.get()).isCancelled()) {
                        return;
                    }
                    if (error.isPresent()) {
                        InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> trackingHandler.onError((Throwable)error.get()));
                    } else {
                        if (toolCallBuilder.hasRequests()) {
                            InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)trackingHandler, (CompleteToolCall)toolCallBuilder.buildAndReset());
                        }
                        String text = textBuilder.toString();
                        AiMessage aiMessage = AiMessage.builder().text(text.isEmpty() ? null : text).toolExecutionRequests(toolCallBuilder.allRequests()).build();
                        ChatResponse chatResponse = ChatResponse.builder().aiMessage(aiMessage).metadata((ChatResponseMetadata)responseMetadataBuilder.build()).build();
                        InternalStreamingChatResponseHandlerUtils.onCompleteResponse((StreamingChatResponseHandler)trackingHandler, (ChatResponse)chatResponse);
                    }
                }
            });
            streamingHandle.set(new OpenAiOfficialStreamingHandle(asyncStreamResponse));
        }
        catch (Exception e) {
            InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> handler.onError((Throwable)e));
        }
    }

    private void manageChatCompletionChunks(ChatCompletionChunk chatCompletionChunk, StreamingChatResponseHandler handler, StreamingHandle streamingHandle, OpenAiOfficialChatResponseMetadata.Builder responseMetadataBuilder, StringBuffer text, ToolCallBuilder toolCallBuilder) {
        responseMetadataBuilder.id(chatCompletionChunk.id());
        responseMetadataBuilder.modelName(chatCompletionChunk.model());
        if (chatCompletionChunk.usage().isPresent()) {
            responseMetadataBuilder.tokenUsage(InternalOpenAiOfficialHelper.tokenUsageFrom((CompletionUsage)chatCompletionChunk.usage().get()));
        }
        responseMetadataBuilder.created(chatCompletionChunk.created());
        responseMetadataBuilder.serviceTier(chatCompletionChunk.serviceTier().isPresent() ? ((ChatCompletionChunk.ServiceTier)chatCompletionChunk.serviceTier().get()).toString() : null);
        responseMetadataBuilder.systemFingerprint(chatCompletionChunk.systemFingerprint().isPresent() ? (String)chatCompletionChunk.systemFingerprint().get() : null);
        chatCompletionChunk.choices().forEach(choice -> {
            if (choice.delta().content().isPresent() && !((String)choice.delta().content().get()).isEmpty()) {
                String partialResponse = (String)choice.delta().content().get();
                text.append(partialResponse);
                InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)handler, (String)partialResponse, (StreamingHandle)streamingHandle);
            }
            if (choice.delta().toolCalls().isPresent()) {
                for (ChatCompletionChunk.Choice.Delta.ToolCall toolCall : (List)choice.delta().toolCalls().get()) {
                    if (!toolCall.function().isPresent()) continue;
                    ChatCompletionChunk.Choice.Delta.ToolCall.Function function = (ChatCompletionChunk.Choice.Delta.ToolCall.Function)toolCall.function().get();
                    int index = (int)toolCall.index();
                    if (toolCallBuilder.index() != index) {
                        InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)handler, (CompleteToolCall)toolCallBuilder.buildAndReset());
                        toolCallBuilder.updateIndex(Integer.valueOf(index));
                    }
                    String id = toolCallBuilder.updateId((String)toolCall.id().orElse(null));
                    String name = toolCallBuilder.updateName((String)function.name().orElse(null));
                    String partialArguments = function.arguments().orElse(null);
                    if (!Utils.isNotNullOrEmpty((String)partialArguments)) continue;
                    toolCallBuilder.appendArguments(partialArguments);
                    PartialToolCall partialToolRequest = PartialToolCall.builder().index(index).id(id).name(name).partialArguments(partialArguments).build();
                    InternalStreamingChatResponseHandlerUtils.onPartialToolCall((StreamingChatResponseHandler)handler, (PartialToolCall)partialToolRequest, (StreamingHandle)streamingHandle);
                }
            }
            if (choice.finishReason().isPresent()) {
                responseMetadataBuilder.finishReason(InternalOpenAiOfficialHelper.finishReasonFrom((ChatCompletionChunk.Choice.FinishReason)choice.finishReason().get()));
            }
        });
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String baseUrl;
        private String apiKey;
        private Credential credential;
        private String microsoftFoundryDeploymentName;
        private AzureOpenAIServiceVersion azureOpenAIServiceVersion;
        private String organizationId;
        private boolean isMicrosoftFoundry;
        private boolean isGitHubModels;
        private OpenAIClientAsync openAIClientAsync;
        private ChatRequestParameters defaultRequestParameters;
        private String modelName;
        private Double temperature;
        private Double topP;
        private List<String> stop;
        private Integer maxCompletionTokens;
        private Double presencePenalty;
        private Double frequencyPenalty;
        private Map<String, Integer> logitBias;
        private String responseFormat;
        private Boolean strictJsonSchema;
        private Integer seed;
        private String user;
        private Boolean strictTools;
        private Boolean parallelToolCalls;
        private Boolean store;
        private Map<String, String> metadata;
        private String serviceTier;
        private Duration timeout;
        private Integer maxRetries;
        private Proxy proxy;
        private TokenCountEstimator tokenCountEstimator;
        private Map<String, String> customHeaders;
        private List<ChatModelListener> listeners;
        private Set<Capability> capabilities;

        public Builder defaultRequestParameters(ChatRequestParameters parameters) {
            this.defaultRequestParameters = parameters;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder modelName(ChatModel modelName) {
            this.modelName = modelName.toString();
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

        public Builder credential(Credential credential) {
            this.credential = credential;
            return this;
        }

        @Deprecated
        public Builder azureDeploymentName(String azureDeploymentName) {
            this.microsoftFoundryDeploymentName = azureDeploymentName;
            return this;
        }

        public Builder microsoftFoundryDeploymentName(String microsoftFoundryDeploymentName) {
            this.microsoftFoundryDeploymentName = microsoftFoundryDeploymentName;
            return this;
        }

        public Builder azureOpenAIServiceVersion(AzureOpenAIServiceVersion azureOpenAIServiceVersion) {
            this.azureOpenAIServiceVersion = azureOpenAIServiceVersion;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        @Deprecated
        public Builder isAzure(boolean isAzure) {
            this.isMicrosoftFoundry = isAzure;
            return this;
        }

        public Builder isMicrosoftFoundry(boolean isMicrosoftFoundry) {
            this.isMicrosoftFoundry = isMicrosoftFoundry;
            return this;
        }

        public Builder isGitHubModels(boolean isGitHubModels) {
            this.isGitHubModels = isGitHubModels;
            return this;
        }

        public Builder openAIClientAsync(OpenAIClientAsync openAIClientAsync) {
            this.openAIClientAsync = openAIClientAsync;
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

        public Builder maxCompletionTokens(Integer maxCompletionTokens) {
            this.maxCompletionTokens = maxCompletionTokens;
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

        public Builder logitBias(Map<String, Integer> logitBias) {
            this.logitBias = logitBias;
            return this;
        }

        public Builder responseFormat(String responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public Builder strictJsonSchema(Boolean strictJsonSchema) {
            this.strictJsonSchema = strictJsonSchema;
            return this;
        }

        public Builder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder strictTools(Boolean strictTools) {
            this.strictTools = strictTools;
            return this;
        }

        public Builder parallelToolCalls(Boolean parallelToolCalls) {
            this.parallelToolCalls = parallelToolCalls;
            return this;
        }

        public Builder store(Boolean store) {
            this.store = store;
            return this;
        }

        public Builder metadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder serviceTier(String serviceTier) {
            this.serviceTier = serviceTier;
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

        public Builder proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public Builder tokenCountEstimator(TokenCountEstimator tokenCountEstimator) {
            this.tokenCountEstimator = tokenCountEstimator;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public Builder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public Builder supportedCapabilities(Set<Capability> capabilities) {
            this.capabilities = capabilities;
            return this;
        }

        public OpenAiOfficialStreamingChatModel build() {
            return new OpenAiOfficialStreamingChatModel(this);
        }
    }
}

