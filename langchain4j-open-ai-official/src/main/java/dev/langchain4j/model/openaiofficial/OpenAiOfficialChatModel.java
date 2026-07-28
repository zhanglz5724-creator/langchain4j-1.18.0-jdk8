/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.openai.azure.AzureOpenAIServiceVersion
 *  com.openai.client.OpenAIClient
 *  com.openai.credential.Credential
 *  com.openai.models.ChatModel
 *  com.openai.models.chat.completions.ChatCompletion
 *  com.openai.models.chat.completions.ChatCompletion$Choice
 *  com.openai.models.chat.completions.ChatCompletion$Choice$FinishReason
 *  com.openai.models.chat.completions.ChatCompletion$ServiceTier
 *  com.openai.models.chat.completions.ChatCompletionCreateParams
 *  com.openai.models.completions.CompletionUsage
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.TokenCountEstimator
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.FinishReason
 */
package dev.langchain4j.model.openaiofficial;

import com.openai.azure.AzureOpenAIServiceVersion;
import com.openai.client.OpenAIClient;
import com.openai.credential.Credential;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.completions.CompletionUsage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.openaiofficial.InternalOpenAiOfficialHelper;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialBaseChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatRequestParameters;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatResponseMetadata;
import dev.langchain4j.model.output.FinishReason;
import java.net.Proxy;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OpenAiOfficialChatModel
extends OpenAiOfficialBaseChatModel
implements dev.langchain4j.model.chat.ChatModel {
    public OpenAiOfficialChatModel(Builder builder) {
        this.client = builder.openAIClient;
        this.init(builder.baseUrl, builder.apiKey, builder.credential, builder.microsoftFoundryDeploymentName, builder.azureOpenAIServiceVersion, builder.organizationId, builder.isMicrosoftFoundry, builder.isGitHubModels, builder.defaultRequestParameters, builder.modelName, builder.temperature, builder.topP, builder.stop, builder.maxCompletionTokens, builder.presencePenalty, builder.frequencyPenalty, builder.logitBias, builder.responseFormat, builder.strictJsonSchema, builder.seed, builder.user, builder.strictTools, builder.parallelToolCalls, builder.store, builder.metadata, builder.serviceTier, builder.timeout, builder.maxRetries, builder.proxy, builder.tokenCountEstimator, builder.customHeaders, builder.listeners, builder.capabilities, false);
        this.modelName = builder.modelName;
    }

    public ChatResponse doChat(ChatRequest chatRequest) {
        OpenAiOfficialChatRequestParameters parameters = (OpenAiOfficialChatRequestParameters)chatRequest.parameters();
        InternalOpenAiOfficialHelper.validate((ChatRequestParameters)parameters);
        ChatCompletionCreateParams chatCompletionCreateParams = InternalOpenAiOfficialHelper.toOpenAiChatCompletionCreateParams(chatRequest, parameters, this.strictTools, this.strictJsonSchema).build();
        if ((this.modelProvider.equals((Object)ModelProvider.MICROSOFT_FOUNDRY) || this.modelProvider.equals((Object)ModelProvider.GITHUB_MODELS)) && !parameters.modelName().equals(this.modelName)) {
            throw new UnsupportedFeatureException("Modifying the modelName is not supported");
        }
        ChatCompletion chatCompletion = this.client.chat().completions().create(chatCompletionCreateParams);
        OpenAiOfficialChatResponseMetadata.Builder responseMetadataBuilder = ((OpenAiOfficialChatResponseMetadata.Builder)((OpenAiOfficialChatResponseMetadata.Builder)OpenAiOfficialChatResponseMetadata.builder().id(chatCompletion.id())).modelName(chatCompletion.model())).created(chatCompletion.created());
        if (!chatCompletion.choices().isEmpty()) {
            ChatCompletion.Choice choice = (ChatCompletion.Choice)chatCompletion.choices().get(0);
            responseMetadataBuilder.finishReason(InternalOpenAiOfficialHelper.finishReasonFrom(choice.finishReason()));
            if (choice.message().toolCalls().isPresent() && choice.finishReason().equals((Object)ChatCompletion.Choice.FinishReason.STOP)) {
                responseMetadataBuilder.finishReason(FinishReason.TOOL_EXECUTION);
            }
        }
        if (chatCompletion.usage().isPresent()) {
            responseMetadataBuilder.tokenUsage(InternalOpenAiOfficialHelper.tokenUsageFrom((CompletionUsage)chatCompletion.usage().get()));
        }
        if (chatCompletion.serviceTier().isPresent()) {
            responseMetadataBuilder.serviceTier(((ChatCompletion.ServiceTier)chatCompletion.serviceTier().get()).toString());
        }
        if (chatCompletion.systemFingerprint().isPresent()) {
            responseMetadataBuilder.systemFingerprint((String)chatCompletion.systemFingerprint().get());
        }
        return ChatResponse.builder().aiMessage(InternalOpenAiOfficialHelper.aiMessageFrom(chatCompletion)).metadata((ChatResponseMetadata)responseMetadataBuilder.build()).build();
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
        private OpenAIClient openAIClient;
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

        public Builder isGitHubModels(boolean isGitHubModels) {
            this.isGitHubModels = isGitHubModels;
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

        public Builder openAIClient(OpenAIClient openAIClient) {
            this.openAIClient = openAIClient;
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

        public OpenAiOfficialChatModel build() {
            return new OpenAiOfficialChatModel(this);
        }
    }
}

