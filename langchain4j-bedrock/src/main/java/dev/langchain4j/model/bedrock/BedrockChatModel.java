/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  org.slf4j.Logger
 *  software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
 *  software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
 *  software.amazon.awssdk.core.interceptor.ExecutionInterceptor
 *  software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient
 *  software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClientBuilder
 *  software.amazon.awssdk.services.bedrockruntime.model.CacheTTL
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.RetryUtils;
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
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptor;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClientBuilder;
import software.amazon.awssdk.services.bedrockruntime.model.CacheTTL;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;

public class BedrockChatModel
extends AbstractBedrockChatModel
implements ChatModel {
    private final BedrockRuntimeClient client;
    private final Integer maxRetries;

    public BedrockChatModel(String modelId) {
        this((Builder)BedrockChatModel.builder().modelId(modelId));
    }

    public BedrockChatModel(Builder builder) {
        super(builder);
        this.client = Objects.isNull(builder.client) ? this.createClient((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false), builder.logger) : builder.client;
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    public ChatResponse doChat(ChatRequest request) {
        BedrockChatModel.validate(request.parameters());
        ConverseRequest converseRequest = this.buildConverseRequest(request);
        ConverseResponse converseResponse = (ConverseResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.converse(converseRequest), (int)this.maxRetries, (ExceptionMapper)BedrockExceptionMapper.INSTANCE);
        return ChatResponse.builder().aiMessage(this.aiMessageFrom(converseResponse)).metadata((ChatResponseMetadata)((BedrockChatResponseMetadata.Builder)((BedrockChatResponseMetadata.Builder)((BedrockChatResponseMetadata.Builder)((BedrockChatResponseMetadata.Builder)BedrockChatResponseMetadata.builder().id(converseResponse.responseMetadata().requestId())).finishReason(this.finishReasonFrom(converseResponse.stopReason()))).tokenUsage(this.tokenUsageFrom(converseResponse.usage()))).modelName(converseRequest.modelId())).guardrailAssessmentSummary(this.guardrailAssessmentSummaryFrom(converseResponse.trace())).build()).build();
    }

    public BedrockChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    private ConverseRequest buildConverseRequest(ChatRequest chatRequest) {
        BedrockChatRequestParameters parameters = (BedrockChatRequestParameters)chatRequest.parameters();
        BedrockCachePointPlacement cachePointPlacement = parameters.cachePointPlacement();
        CacheTTL cacheTtl = parameters.cacheTtl();
        BedrockGuardrailConfiguration bedrockGuardrailConfiguration = parameters.bedrockGuardrailConfiguration();
        BedrockServiceTier bedrockServiceTier = parameters.serviceTier();
        boolean hasTools = chatRequest.toolSpecifications() != null && !chatRequest.toolSpecifications().isEmpty();
        this.validateTotalCachePoints(chatRequest.messages(), cachePointPlacement, hasTools);
        return (ConverseRequest)ConverseRequest.builder().modelId(chatRequest.modelName()).inferenceConfig(this.inferenceConfigFrom(chatRequest.parameters())).system(this.extractSystemMessages(chatRequest.messages(), cachePointPlacement, cacheTtl)).messages(this.extractRegularMessages(chatRequest.messages(), cachePointPlacement, cacheTtl)).toolConfig(this.extractToolConfigurationFrom(chatRequest, cachePointPlacement, cacheTtl)).additionalModelRequestFields(this.additionalRequestModelFieldsFrom(chatRequest.parameters())).guardrailConfig(this.guardrailConfigFrom(bedrockGuardrailConfiguration)).outputConfig(BedrockChatModel.outputConfigFrom(chatRequest.responseFormat())).serviceTier(this.serviceTierFor(bedrockServiceTier)).build();
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

    private BedrockRuntimeClient createClient(boolean logRequests, boolean logResponses, Logger logger) {
        return (BedrockRuntimeClient)((BedrockRuntimeClientBuilder)((BedrockRuntimeClientBuilder)((BedrockRuntimeClientBuilder)BedrockRuntimeClient.builder().region(this.region)).credentialsProvider((AwsCredentialsProvider)DefaultCredentialsProvider.create())).overrideConfiguration(config -> {
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
        private BedrockRuntimeClient client;
        private Integer maxRetries;

        public Builder client(BedrockRuntimeClient client) {
            this.client = client;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public BedrockChatModel build() {
            return new BedrockChatModel(this);
        }
    }
}

