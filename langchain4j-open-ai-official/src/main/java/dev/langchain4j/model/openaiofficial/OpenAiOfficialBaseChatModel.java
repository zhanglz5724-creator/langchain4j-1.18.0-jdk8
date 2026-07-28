/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.openai.azure.AzureOpenAIServiceVersion
 *  com.openai.client.OpenAIClient
 *  com.openai.client.OpenAIClientAsync
 *  com.openai.credential.Credential
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.TokenCountEstimator
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 */
package dev.langchain4j.model.openaiofficial;

import com.openai.azure.AzureOpenAIServiceVersion;
import com.openai.client.OpenAIClient;
import com.openai.client.OpenAIClientAsync;
import com.openai.credential.Credential;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.openaiofficial.InternalOpenAiOfficialHelper;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatRequestParameters;
import dev.langchain4j.model.openaiofficial.setup.OpenAiOfficialSetup;
import java.net.Proxy;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract class OpenAiOfficialBaseChatModel {
    protected OpenAIClient client;
    protected OpenAIClientAsync asyncClient;
    protected String modelName;
    protected OpenAiOfficialChatRequestParameters defaultRequestParameters;
    protected String responseFormat;
    protected Boolean strictJsonSchema;
    protected Boolean strictTools;
    protected TokenCountEstimator tokenCountEstimator;
    protected List<ChatModelListener> listeners;
    protected Set<Capability> supportedCapabilities;
    protected ModelProvider modelProvider;

    OpenAiOfficialBaseChatModel() {
    }

    public void init(String baseUrl, String apiKey, Credential credential, String microsoftFoundryDeploymentName, AzureOpenAIServiceVersion azureOpenAIServiceVersion, String organizationId, boolean isAzure, boolean isGitHubModels, ChatRequestParameters defaultRequestParameters, String modelName, Double temperature, Double topP, List<String> stop, Integer maxCompletionTokens, Double presencePenalty, Double frequencyPenalty, Map<String, Integer> logitBias, String responseFormat, Boolean strictJsonSchema, Integer seed, String user, Boolean strictTools, Boolean parallelToolCalls, Boolean store, Map<String, String> metadata, String serviceTier, Duration timeout, Integer maxRetries, Proxy proxy, TokenCountEstimator tokenCountEstimator, Map<String, String> customHeaders, List<ChatModelListener> listeners, Set<Capability> capabilities, boolean isAsync) {
        ChatRequestParameters commonParameters;
        if (isAsync) {
            if (this.asyncClient == null) {
                this.asyncClient = OpenAiOfficialSetup.setupAsyncClient(baseUrl, apiKey, credential, microsoftFoundryDeploymentName, azureOpenAIServiceVersion, organizationId, isAzure, isGitHubModels, modelName, timeout, maxRetries, proxy, customHeaders);
            }
        } else if (this.client == null) {
            this.client = OpenAiOfficialSetup.setupSyncClient(baseUrl, apiKey, credential, microsoftFoundryDeploymentName, azureOpenAIServiceVersion, organizationId, isAzure, isGitHubModels, modelName, timeout, maxRetries, proxy, customHeaders);
        }
        if (defaultRequestParameters != null) {
            InternalOpenAiOfficialHelper.validate(defaultRequestParameters);
            commonParameters = defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        OpenAiOfficialChatRequestParameters openAiParameters = defaultRequestParameters instanceof OpenAiOfficialChatRequestParameters ? (OpenAiOfficialChatRequestParameters)defaultRequestParameters : OpenAiOfficialChatRequestParameters.EMPTY;
        this.defaultRequestParameters = ((OpenAiOfficialChatRequestParameters.Builder)((OpenAiOfficialChatRequestParameters.Builder)((OpenAiOfficialChatRequestParameters.Builder)((OpenAiOfficialChatRequestParameters.Builder)((OpenAiOfficialChatRequestParameters.Builder)((OpenAiOfficialChatRequestParameters.Builder)((OpenAiOfficialChatRequestParameters.Builder)((OpenAiOfficialChatRequestParameters.Builder)((OpenAiOfficialChatRequestParameters.Builder)((OpenAiOfficialChatRequestParameters.Builder)OpenAiOfficialChatRequestParameters.builder().modelName((String)Utils.getOrDefault((Object)modelName, (Object)commonParameters.modelName()))).temperature((Double)Utils.getOrDefault((Object)temperature, (Object)commonParameters.temperature()))).topP((Double)Utils.getOrDefault((Object)topP, (Object)commonParameters.topP()))).frequencyPenalty((Double)Utils.getOrDefault((Object)frequencyPenalty, (Object)commonParameters.frequencyPenalty()))).presencePenalty((Double)Utils.getOrDefault((Object)presencePenalty, (Object)commonParameters.presencePenalty()))).maxOutputTokens((Integer)Utils.getOrDefault((Object)maxCompletionTokens, (Object)commonParameters.maxOutputTokens()))).stopSequences(Utils.getOrDefault(stop, (List)commonParameters.stopSequences()))).toolSpecifications(commonParameters.toolSpecifications())).toolChoice(commonParameters.toolChoice())).responseFormat((ResponseFormat)Utils.getOrDefault((Object)InternalOpenAiOfficialHelper.fromOpenAiResponseFormat(responseFormat), (Object)commonParameters.responseFormat()))).maxCompletionTokens((Integer)Utils.getOrDefault((Object)maxCompletionTokens, (Object)openAiParameters.maxCompletionTokens())).logitBias(Utils.getOrDefault(logitBias, openAiParameters.logitBias())).parallelToolCalls((Boolean)Utils.getOrDefault((Object)parallelToolCalls, (Object)openAiParameters.parallelToolCalls())).seed((Integer)Utils.getOrDefault((Object)seed, (Object)openAiParameters.seed())).user((String)Utils.getOrDefault((Object)user, (Object)openAiParameters.user())).store((Boolean)Utils.getOrDefault((Object)store, (Object)openAiParameters.store())).metadata(Utils.getOrDefault(metadata, openAiParameters.metadata())).serviceTier((String)Utils.getOrDefault((Object)serviceTier, (Object)openAiParameters.serviceTier())).reasoningEffort(openAiParameters.reasoningEffort()).build();
        this.modelProvider = OpenAiOfficialSetup.detectModelProvider(isAzure, isGitHubModels, baseUrl, microsoftFoundryDeploymentName, azureOpenAIServiceVersion);
        if ((this.modelProvider.equals((Object)ModelProvider.MICROSOFT_FOUNDRY) || this.modelProvider.equals((Object)ModelProvider.GITHUB_MODELS)) && this.defaultRequestParameters.modelName() != null && !this.defaultRequestParameters.modelName().equals(modelName)) {
            throw new UnsupportedFeatureException("Modifying the modelName is not supported");
        }
        this.responseFormat = responseFormat;
        this.strictJsonSchema = (Boolean)Utils.getOrDefault((Object)strictJsonSchema, (Object)false);
        this.strictTools = (Boolean)Utils.getOrDefault((Object)strictTools, (Object)false);
        this.tokenCountEstimator = tokenCountEstimator;
        this.listeners = Utils.copy(listeners);
        this.supportedCapabilities = Utils.copy(capabilities);
    }

    public OpenAiOfficialChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public Set<Capability> supportedCapabilities() {
        return this.supportedCapabilities;
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return this.modelProvider;
    }
}

