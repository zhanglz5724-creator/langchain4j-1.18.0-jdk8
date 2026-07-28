/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils
 *  dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler
 *  dev.langchain4j.internal.ToolCallBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  org.reactivestreams.Subscriber
 *  org.reactivestreams.Subscription
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
 *  software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
 *  software.amazon.awssdk.core.interceptor.ExecutionInterceptor
 *  software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient
 *  software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClientBuilder
 *  software.amazon.awssdk.services.bedrockruntime.model.CacheTTL
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlockDelta
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlockDelta$Type
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlockDeltaEvent
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlockStart$Type
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlockStartEvent
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlockStopEvent
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamMetadataEvent
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamOutput
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamRequest
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamResponseHandler
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamResponseHandler$Builder
 *  software.amazon.awssdk.services.bedrockruntime.model.MessageStartEvent
 *  software.amazon.awssdk.services.bedrockruntime.model.MessageStopEvent
 *  software.amazon.awssdk.services.bedrockruntime.model.ReasoningContentBlockDelta
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils;
import dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler;
import dev.langchain4j.internal.ToolCallBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.bedrock.AbstractBedrockChatModel;
import dev.langchain4j.model.bedrock.AwsLoggingInterceptor;
import dev.langchain4j.model.bedrock.BedrockCachePointPlacement;
import dev.langchain4j.model.bedrock.BedrockChatRequestParameters;
import dev.langchain4j.model.bedrock.BedrockChatResponseMetadata;
import dev.langchain4j.model.bedrock.BedrockCustomHeadersInterceptor;
import dev.langchain4j.model.bedrock.BedrockExceptionMapper;
import dev.langchain4j.model.bedrock.BedrockGuardrailConfiguration;
import dev.langchain4j.model.bedrock.BedrockServiceTier;
import dev.langchain4j.model.bedrock.BedrockStreamingHandle;
import dev.langchain4j.model.bedrock.ConverseResponseFromStreamBuilder;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.StreamingHandle;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptor;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClientBuilder;
import software.amazon.awssdk.services.bedrockruntime.model.CacheTTL;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlockDelta;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlockDeltaEvent;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlockStart;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlockStartEvent;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlockStopEvent;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamMetadataEvent;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamOutput;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamRequest;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamResponseHandler;
import software.amazon.awssdk.services.bedrockruntime.model.MessageStartEvent;
import software.amazon.awssdk.services.bedrockruntime.model.MessageStopEvent;
import software.amazon.awssdk.services.bedrockruntime.model.ReasoningContentBlockDelta;

public class BedrockStreamingChatModel
extends AbstractBedrockChatModel
implements StreamingChatModel {
    private static final Logger log = LoggerFactory.getLogger(BedrockStreamingChatModel.class);
    private final BedrockRuntimeAsyncClient client;
    private final boolean logResponses;

    public BedrockStreamingChatModel(String modelId) {
        this((Builder)BedrockStreamingChatModel.builder().modelId(modelId));
    }

    public BedrockStreamingChatModel(Builder builder) {
        super(builder);
        this.client = Objects.isNull(builder.client) ? this.createClient((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false), builder.logger) : builder.client;
        this.logResponses = (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false);
    }

    public void doChat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
        BedrockStreamingChatModel.validate(chatRequest.parameters());
        final ConverseStreamRequest converseStreamRequest = this.buildConverseStreamRequest(chatRequest);
        final ConverseResponseFromStreamBuilder responseBuilder = new ConverseResponseFromStreamBuilder(this.returnThinking);
        final ToolCallBuilder toolCallBuilder = new ToolCallBuilder(-1);
        final AtomicReference currentContentType = new AtomicReference();
        final AtomicReference streamingHandle = new AtomicReference();
        final StreamingChatResponseHandler targetHandler = handler;
        ConverseStreamResponseHandler converseStreamResponseHandler = ((ConverseStreamResponseHandler.Builder)ConverseStreamResponseHandler.builder().onEventStream(publisher -> publisher.subscribe((Subscriber)new Subscriber<ConverseStreamOutput>(){
            final MappingTrackingStreamingChatResponseHandler handler;
            volatile Subscription subscription;
            {
                this.handler = new MappingTrackingStreamingChatResponseHandler(targetHandler);
            }

            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                streamingHandle.set(new BedrockStreamingHandle(subscription));
                subscription.request(1L);
            }

            public void onNext(ConverseStreamOutput output) {
                this.handler.resetMappingTracking();
                if (output instanceof MessageStartEvent) {
                    MessageStartEvent event = (MessageStartEvent)output;
                    if (BedrockStreamingChatModel.this.logResponses) {
                        log.debug("onMessageStart: {}", (Object)event);
                    }
                    responseBuilder.append(event);
                } else if (output instanceof ContentBlockStartEvent) {
                    ContentBlockStartEvent event = (ContentBlockStartEvent)output;
                    if (BedrockStreamingChatModel.this.logResponses) {
                        log.debug("onContentBlockStart: {}", (Object)event);
                    }
                    if (event.start().type() == ContentBlockStart.Type.TOOL_USE) {
                        toolCallBuilder.updateIndex(Integer.valueOf(toolCallBuilder.index() + 1));
                        toolCallBuilder.updateId(event.start().toolUse().toolUseId());
                        toolCallBuilder.updateName(event.start().toolUse().name());
                    }
                    responseBuilder.append(event);
                } else if (output instanceof ContentBlockDeltaEvent) {
                    String input;
                    ContentBlockDeltaEvent event = (ContentBlockDeltaEvent)output;
                    if (BedrockStreamingChatModel.this.logResponses) {
                        log.debug("onContentBlockDelta: {}", (Object)event);
                    }
                    ContentBlockDelta delta = event.delta();
                    currentContentType.set(delta.type());
                    if (currentContentType.get() == ContentBlockDelta.Type.TEXT) {
                        InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)this.handler, (String)delta.text(), (StreamingHandle)((StreamingHandle)streamingHandle.get()));
                    } else if (currentContentType.get() == ContentBlockDelta.Type.REASONING_CONTENT) {
                        ReasoningContentBlockDelta reasoningContent = delta.reasoningContent();
                        String thinking = reasoningContent.text();
                        if (Utils.isNotNullOrEmpty((String)thinking)) {
                            InternalStreamingChatResponseHandlerUtils.onPartialThinking((StreamingChatResponseHandler)this.handler, (String)thinking, (StreamingHandle)((StreamingHandle)streamingHandle.get()));
                        }
                    } else if (currentContentType.get() == ContentBlockDelta.Type.TOOL_USE && Utils.isNotNullOrEmpty((String)(input = delta.toolUse().input()))) {
                        toolCallBuilder.appendArguments(input);
                    }
                    responseBuilder.append(delta);
                } else if (output instanceof ContentBlockStopEvent) {
                    ContentBlockStopEvent event = (ContentBlockStopEvent)output;
                    if (BedrockStreamingChatModel.this.logResponses) {
                        log.debug("onContentBlockStop: {}", (Object)event);
                    }
                    if (currentContentType.get() == ContentBlockDelta.Type.TOOL_USE) {
                        InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)this.handler, (CompleteToolCall)toolCallBuilder.buildAndReset());
                    }
                    responseBuilder.append(event);
                } else if (output instanceof MessageStopEvent) {
                    MessageStopEvent event = (MessageStopEvent)output;
                    if (BedrockStreamingChatModel.this.logResponses) {
                        log.debug("onMessageStop: {}", (Object)event);
                    }
                    responseBuilder.append(event);
                } else if (output instanceof ConverseStreamMetadataEvent) {
                    ConverseStreamMetadataEvent event = (ConverseStreamMetadataEvent)output;
                    if (BedrockStreamingChatModel.this.logResponses) {
                        log.debug("onMetadata: {}", (Object)event);
                    }
                    responseBuilder.append(event);
                    ChatResponse response = BedrockStreamingChatModel.this.responseFrom(responseBuilder.build(), converseStreamRequest.modelId());
                    InternalStreamingChatResponseHandlerUtils.onCompleteResponse((StreamingChatResponseHandler)this.handler, (ChatResponse)response);
                }
                if (!this.handler.wasMapped()) {
                    InternalStreamingChatResponseHandlerUtils.onUnmappedRawEvent((StreamingChatResponseHandler)this.handler, (Object)output);
                }
                this.subscription.request(1L);
            }

            public void onError(Throwable error) {
                RuntimeException mappedError = BedrockExceptionMapper.INSTANCE.mapException(error);
                InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)mappedError));
            }

            public void onComplete() {
            }
        }))).build();
        this.client.converseStream(converseStreamRequest, converseStreamResponseHandler).exceptionally(ex -> {
            RuntimeException mappedError = BedrockExceptionMapper.INSTANCE.mapException((Throwable)ex);
            InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> handler.onError((Throwable)mappedError));
            return null;
        });
    }

    public BedrockChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    private ConverseStreamRequest buildConverseStreamRequest(ChatRequest chatRequest) {
        BedrockChatRequestParameters parameters = (BedrockChatRequestParameters)chatRequest.parameters();
        BedrockCachePointPlacement cachePointPlacement = parameters.cachePointPlacement();
        CacheTTL cacheTtl = parameters.cacheTtl();
        BedrockGuardrailConfiguration bedrockGuardrailConfiguration = parameters.bedrockGuardrailConfiguration();
        BedrockServiceTier bedrockServiceTier = parameters.serviceTier();
        boolean hasTools = chatRequest.toolSpecifications() != null && !chatRequest.toolSpecifications().isEmpty();
        this.validateTotalCachePoints(chatRequest.messages(), cachePointPlacement, hasTools);
        return (ConverseStreamRequest)ConverseStreamRequest.builder().modelId(chatRequest.modelName()).inferenceConfig(this.inferenceConfigFrom(chatRequest.parameters())).system(this.extractSystemMessages(chatRequest.messages(), cachePointPlacement, cacheTtl)).messages(this.extractRegularMessages(chatRequest.messages(), cachePointPlacement, cacheTtl)).toolConfig(this.extractToolConfigurationFrom(chatRequest, cachePointPlacement, cacheTtl)).additionalModelRequestFields(this.additionalRequestModelFieldsFrom(chatRequest.parameters())).guardrailConfig(this.guardrailStreamConfigFrom(bedrockGuardrailConfiguration)).outputConfig(BedrockStreamingChatModel.outputConfigFrom(chatRequest.responseFormat())).serviceTier(this.serviceTierFor(bedrockServiceTier)).build();
    }

    private ChatResponse responseFrom(ConverseResponse converseResponse, String modelId) {
        return ChatResponse.builder().aiMessage(this.aiMessageFrom(converseResponse)).metadata((ChatResponseMetadata)((BedrockChatResponseMetadata.Builder)((BedrockChatResponseMetadata.Builder)((BedrockChatResponseMetadata.Builder)((BedrockChatResponseMetadata.Builder)BedrockChatResponseMetadata.builder().id(UUID.randomUUID().toString())).finishReason(this.finishReasonFrom(converseResponse.stopReason()))).tokenUsage(this.tokenUsageFrom(converseResponse.usage()))).modelName(modelId)).guardrailAssessmentSummary(this.guardrailAssessmentSummaryFrom(converseResponse.trace())).build()).build();
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public Set<Capability> supportedCapabilities() {
        return this.supportedCapabilities;
    }

    public ModelProvider provider() {
        return ModelProvider.AMAZON_BEDROCK;
    }

    public static Builder builder() {
        return new Builder();
    }

    private BedrockRuntimeAsyncClient createClient(boolean logRequests, boolean logResponses, Logger logger) {
        return (BedrockRuntimeAsyncClient)((BedrockRuntimeAsyncClientBuilder)((BedrockRuntimeAsyncClientBuilder)((BedrockRuntimeAsyncClientBuilder)BedrockRuntimeAsyncClient.builder().region(this.region)).credentialsProvider((AwsCredentialsProvider)DefaultCredentialsProvider.create())).overrideConfiguration(config -> {
            config.apiCallTimeout(this.timeout);
            if (logRequests || logResponses) {
                config.addExecutionInterceptor((ExecutionInterceptor)new AwsLoggingInterceptor(logRequests, logResponses, logger));
            }
            if (this.customHeadersSupplier != null) {
                config.addExecutionInterceptor((ExecutionInterceptor)new BedrockCustomHeadersInterceptor(this.customHeadersSupplier));
            }
        })).build();
    }

    public static class Builder
    extends AbstractBedrockChatModel.AbstractBuilder<Builder> {
        private BedrockRuntimeAsyncClient client;

        public Builder client(BedrockRuntimeAsyncClient client) {
            this.client = client;
            return this;
        }

        public BedrockStreamingChatModel build() {
            return new BedrockStreamingChatModel(this);
        }
    }
}

