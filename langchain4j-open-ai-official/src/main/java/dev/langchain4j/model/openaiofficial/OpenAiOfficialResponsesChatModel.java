/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.openai.azure.AzureOpenAIServiceVersion
 *  com.openai.client.OpenAIClient
 *  com.openai.credential.Credential
 *  com.openai.models.Reasoning$Summary
 *  com.openai.models.ReasoningEffort
 *  com.openai.models.responses.Response
 *  com.openai.models.responses.ResponseCreateParams
 *  com.openai.models.responses.Tool
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.internal.ExceptionMapper
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
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 */
package dev.langchain4j.model.openaiofficial;

import com.openai.azure.AzureOpenAIServiceVersion;
import com.openai.client.OpenAIClient;
import com.openai.credential.Credential;
import com.openai.models.Reasoning;
import com.openai.models.ReasoningEffort;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.Tool;
import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialResponsesChatRequestParameters;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialResponsesChatResponseMetadata;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialResponsesStreamingChatModel;
import dev.langchain4j.model.openaiofficial.setup.OpenAiOfficialSetup;
import java.net.Proxy;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Experimental
public class OpenAiOfficialResponsesChatModel
implements ChatModel {
    private final OpenAIClient client;
    private final OpenAiOfficialResponsesChatRequestParameters defaultRequestParameters;
    private final List<ChatModelListener> listeners;

    private OpenAiOfficialResponsesChatModel(Builder builder) {
        ChatRequestParameters commonParameters;
        OpenAIClient openAIClient = this.client = builder.client != null ? builder.client : OpenAiOfficialSetup.setupSyncClient(builder.baseUrl, builder.apiKey, builder.credential, builder.microsoftFoundryDeploymentName, builder.azureOpenAIServiceVersion, builder.organizationId, builder.isMicrosoftFoundry, builder.isGitHubModels, builder.modelName, builder.timeout, builder.maxRetries, builder.proxy, builder.customHeaders);
        if (builder.defaultRequestParameters != null) {
            OpenAiOfficialResponsesStreamingChatModel.validate(builder.defaultRequestParameters);
            commonParameters = builder.defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        OpenAiOfficialResponsesChatRequestParameters responsesParameters = commonParameters instanceof OpenAiOfficialResponsesChatRequestParameters ? (OpenAiOfficialResponsesChatRequestParameters)commonParameters : OpenAiOfficialResponsesChatRequestParameters.EMPTY;
        this.defaultRequestParameters = ((OpenAiOfficialResponsesChatRequestParameters.Builder)((OpenAiOfficialResponsesChatRequestParameters.Builder)((OpenAiOfficialResponsesChatRequestParameters.Builder)((OpenAiOfficialResponsesChatRequestParameters.Builder)((OpenAiOfficialResponsesChatRequestParameters.Builder)((OpenAiOfficialResponsesChatRequestParameters.Builder)((OpenAiOfficialResponsesChatRequestParameters.Builder)OpenAiOfficialResponsesChatRequestParameters.builder().modelName((String)ValidationUtils.ensureNotNull((Object)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName()), (String)"modelName"))).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature()))).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP()))).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxOutputTokens, (Object)commonParameters.maxOutputTokens()))).toolSpecifications(Utils.getOrDefault((List)builder.toolSpecifications, (List)commonParameters.toolSpecifications()))).toolChoice((ToolChoice)Utils.getOrDefault((Object)builder.toolChoice, (Object)commonParameters.toolChoice()))).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat()))).previousResponseId((String)Utils.getOrDefault((Object)builder.previousResponseId, (Object)responsesParameters.previousResponseId())).maxToolCalls((Integer)Utils.getOrDefault((Object)builder.maxToolCalls, (Object)responsesParameters.maxToolCalls())).parallelToolCalls((Boolean)Utils.getOrDefault((Object)builder.parallelToolCalls, (Object)responsesParameters.parallelToolCalls())).topLogprobs((Integer)Utils.getOrDefault((Object)builder.topLogprobs, (Object)responsesParameters.topLogprobs())).truncation((String)Utils.getOrDefault((Object)builder.truncation, (Object)responsesParameters.truncation())).include(Utils.getOrDefault((List)builder.include, responsesParameters.include())).serviceTier((String)Utils.getOrDefault((Object)builder.serviceTier, (Object)responsesParameters.serviceTier())).safetyIdentifier((String)Utils.getOrDefault((Object)builder.safetyIdentifier, (Object)responsesParameters.safetyIdentifier())).promptCacheKey((String)Utils.getOrDefault((Object)builder.promptCacheKey, (Object)responsesParameters.promptCacheKey())).promptCacheRetention((String)Utils.getOrDefault((Object)builder.promptCacheRetention, (Object)responsesParameters.promptCacheRetention())).reasoningEffort((ReasoningEffort)Utils.getOrDefault((Object)builder.reasoningEffort, (Object)responsesParameters.reasoningEffort())).reasoningSummary((Reasoning.Summary)Utils.getOrDefault((Object)builder.reasoningSummary, (Object)responsesParameters.reasoningSummary())).textVerbosity((String)Utils.getOrDefault((Object)builder.textVerbosity, (Object)responsesParameters.textVerbosity())).store((Boolean)Utils.getOrDefault((Object)builder.store, (Object)Utils.getOrDefault((Object)responsesParameters.store(), (Object)false))).strictTools((Boolean)Utils.getOrDefault((Object)builder.strictTools, (Object)responsesParameters.strictTools())).strictJsonSchema((Boolean)Utils.getOrDefault((Object)builder.strictJsonSchema, (Object)responsesParameters.strictJsonSchema())).serverTools(Utils.getOrDefault((List)builder.serverTools, responsesParameters.serverTools())).build();
        this.listeners = Utils.copy((List)builder.listeners);
    }

    public static Builder builder() {
        return new Builder();
    }

    public ChatResponse doChat(ChatRequest chatRequest) {
        OpenAiOfficialResponsesStreamingChatModel.validate(chatRequest.parameters());
        OpenAiOfficialResponsesChatRequestParameters parameters = (OpenAiOfficialResponsesChatRequestParameters)chatRequest.parameters();
        try {
            ResponseCreateParams params = OpenAiOfficialResponsesStreamingChatModel.buildRequestParams(chatRequest, parameters);
            Response response = this.client.responses().create(params);
            String text = OpenAiOfficialResponsesStreamingChatModel.extractText(response);
            String thinking = OpenAiOfficialResponsesStreamingChatModel.extractReasoningSummary(response);
            String encryptedReasoning = OpenAiOfficialResponsesStreamingChatModel.extractEncryptedReasoning(response);
            List<ToolExecutionRequest> toolExecutionRequests = OpenAiOfficialResponsesStreamingChatModel.extractToolExecutionRequests(response);
            AiMessage aiMessage = OpenAiOfficialResponsesStreamingChatModel.buildAiMessage(text, thinking, toolExecutionRequests, encryptedReasoning);
            String finishReason = response.status().map(status -> OpenAiOfficialResponsesStreamingChatModel.mapStatusToFinishReason(status.asString(), !toolExecutionRequests.isEmpty())).orElse(null);
            OpenAiOfficialResponsesChatResponseMetadata metadata = OpenAiOfficialResponsesStreamingChatModel.buildResponseMetadata(response.id(), parameters.modelName(), response, finishReason, OpenAiOfficialResponsesStreamingChatModel.extractTokenUsage(response));
            return ChatResponse.builder().aiMessage(aiMessage).metadata((ChatResponseMetadata)metadata).build();
        }
        catch (Exception e) {
            throw ExceptionMapper.DEFAULT.mapException((Throwable)e);
        }
    }

    public ChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.OPEN_AI;
    }

    public Set<Capability> supportedCapabilities() {
        return Collections.singleton(Capability.RESPONSE_FORMAT_JSON_SCHEMA);
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
        private Map<String, String> customHeaders;
        private Duration timeout;
        private Integer maxRetries;
        private Proxy proxy;
        private OpenAIClient client;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Integer maxOutputTokens;
        private Integer maxToolCalls;
        private Boolean parallelToolCalls;
        private String previousResponseId;
        private Integer topLogprobs;
        private String truncation;
        private List<String> include;
        private String serviceTier;
        private String safetyIdentifier;
        private String promptCacheKey;
        private String promptCacheRetention;
        private ReasoningEffort reasoningEffort;
        private Reasoning.Summary reasoningSummary;
        private String textVerbosity;
        private Boolean store;
        private List<ChatModelListener> listeners;
        private Boolean strictTools;
        private Boolean strictJsonSchema;
        private List<ToolSpecification> toolSpecifications;
        private ToolChoice toolChoice;
        private ResponseFormat responseFormat;
        private ChatRequestParameters defaultRequestParameters;
        private List<Tool> serverTools;

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

        public Builder isMicrosoftFoundry(boolean isMicrosoftFoundry) {
            this.isMicrosoftFoundry = isMicrosoftFoundry;
            return this;
        }

        public Builder isGitHubModels(boolean isGitHubModels) {
            this.isGitHubModels = isGitHubModels;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
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

        public Builder client(OpenAIClient client) {
            this.client = client;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
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

        public Builder maxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
            return this;
        }

        public Builder maxToolCalls(Integer maxToolCalls) {
            this.maxToolCalls = maxToolCalls;
            return this;
        }

        public Builder parallelToolCalls(Boolean parallelToolCalls) {
            this.parallelToolCalls = parallelToolCalls;
            return this;
        }

        public Builder previousResponseId(String previousResponseId) {
            this.previousResponseId = previousResponseId;
            return this;
        }

        public Builder topLogprobs(Integer topLogprobs) {
            this.topLogprobs = topLogprobs;
            return this;
        }

        public Builder truncation(String truncation) {
            this.truncation = truncation;
            return this;
        }

        public Builder include(List<String> include) {
            this.include = include;
            return this;
        }

        public Builder serviceTier(String serviceTier) {
            this.serviceTier = serviceTier;
            return this;
        }

        public Builder safetyIdentifier(String safetyIdentifier) {
            this.safetyIdentifier = safetyIdentifier;
            return this;
        }

        public Builder promptCacheKey(String promptCacheKey) {
            this.promptCacheKey = promptCacheKey;
            return this;
        }

        public Builder promptCacheRetention(String promptCacheRetention) {
            this.promptCacheRetention = promptCacheRetention;
            return this;
        }

        public Builder reasoningEffort(ReasoningEffort reasoningEffort) {
            this.reasoningEffort = reasoningEffort;
            return this;
        }

        public Builder reasoningSummary(Reasoning.Summary reasoningSummary) {
            this.reasoningSummary = reasoningSummary;
            return this;
        }

        public Builder textVerbosity(String textVerbosity) {
            this.textVerbosity = textVerbosity;
            return this;
        }

        public Builder store(Boolean store) {
            this.store = store;
            return this;
        }

        public Builder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public Builder listeners(ChatModelListener ... listeners) {
            return this.listeners(Arrays.asList(listeners));
        }

        public Builder strictTools(Boolean strictTools) {
            this.strictTools = strictTools;
            return this;
        }

        public Builder strictJsonSchema(Boolean strictJsonSchema) {
            this.strictJsonSchema = strictJsonSchema;
            return this;
        }

        public Builder toolSpecifications(List<ToolSpecification> toolSpecifications) {
            this.toolSpecifications = toolSpecifications;
            return this;
        }

        public Builder toolSpecifications(ToolSpecification ... toolSpecifications) {
            return this.toolSpecifications(Arrays.asList(toolSpecifications));
        }

        public Builder toolChoice(ToolChoice toolChoice) {
            this.toolChoice = toolChoice;
            return this;
        }

        public Builder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public Builder defaultRequestParameters(ChatRequestParameters defaultRequestParameters) {
            this.defaultRequestParameters = defaultRequestParameters;
            return this;
        }

        public Builder serverTools(List<Tool> serverTools) {
            this.serverTools = serverTools;
            return this;
        }

        public Builder serverTools(Tool ... serverTools) {
            return this.serverTools(Arrays.asList(serverTools));
        }

        public OpenAiOfficialResponsesChatModel build() {
            return new OpenAiOfficialResponsesChatModel(this);
        }
    }
}

