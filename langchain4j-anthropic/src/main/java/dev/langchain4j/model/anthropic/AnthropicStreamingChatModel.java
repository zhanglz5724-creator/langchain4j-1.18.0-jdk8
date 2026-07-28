/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.http.client.HttpClientBuilder
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
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.anthropic;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.anthropic.AnthropicChatModelName;
import dev.langchain4j.model.anthropic.AnthropicChatRequestParameters;
import dev.langchain4j.model.anthropic.AnthropicServerTool;
import dev.langchain4j.model.anthropic.AnthropicSkill;
import dev.langchain4j.model.anthropic.InternalAnthropicHelper;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCacheType;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCreateMessageRequest;
import dev.langchain4j.model.anthropic.internal.client.AnthropicClient;
import dev.langchain4j.model.anthropic.internal.client.AnthropicCreateMessageOptions;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class AnthropicStreamingChatModel
implements StreamingChatModel {
    private final AnthropicClient client;
    private final String thinkingDisplay;
    private final List<ChatModelListener> listeners;
    private final AnthropicChatRequestParameters defaultRequestParameters;
    private final List<AnthropicServerTool> serverTools;
    private final boolean returnServerToolResults;
    private final Set<String> toolMetadataKeysToSend;
    private final List<AnthropicSkill> skills;
    private final Map<String, Object> customParameters;
    private final Boolean strictTools;
    private final Set<Capability> supportedCapabilities;

    public AnthropicStreamingChatModel(AnthropicStreamingChatModelBuilder builder) {
        ChatRequestParameters commonParameters;
        this.client = ((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)((AnthropicClient.Builder)AnthropicClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.anthropic.com/v1/"))).apiKey(builder.apiKey)).version((String)Utils.getOrDefault((Object)builder.version, (Object)"2023-06-01"))).beta(builder.beta)).timeout(builder.timeout)).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).customHeaders(builder.customHeadersSupplier)).build();
        this.listeners = Utils.copy((List)builder.listeners);
        this.returnServerToolResults = (Boolean)Utils.getOrDefault((Object)builder.returnServerToolResults, (Object)false);
        this.supportedCapabilities = Utils.copy((Set)builder.supportedCapabilities);
        if (builder.defaultRequestParameters != null) {
            InternalAnthropicHelper.validate(builder.defaultRequestParameters);
            commonParameters = builder.defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        AnthropicChatRequestParameters anthropicDefaults = commonParameters instanceof AnthropicChatRequestParameters ? (AnthropicChatRequestParameters)commonParameters : AnthropicChatRequestParameters.EMPTY;
        this.thinkingDisplay = builder.thinkingDisplay;
        this.serverTools = Utils.copy((List)builder.serverTools);
        this.toolMetadataKeysToSend = Utils.copy((Set)builder.toolMetadataKeysToSend);
        this.skills = Utils.copy((List)builder.skills);
        this.customParameters = Utils.copy((Map)builder.customParameters);
        this.strictTools = builder.strictTools;
        this.defaultRequestParameters = ((AnthropicChatRequestParameters.Builder)((AnthropicChatRequestParameters.Builder)((AnthropicChatRequestParameters.Builder)((AnthropicChatRequestParameters.Builder)((AnthropicChatRequestParameters.Builder)((AnthropicChatRequestParameters.Builder)((AnthropicChatRequestParameters.Builder)((AnthropicChatRequestParameters.Builder)((AnthropicChatRequestParameters.Builder)AnthropicChatRequestParameters.builder().modelName((String)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName()))).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature()))).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP()))).topK((Integer)Utils.getOrDefault((Object)builder.topK, (Object)commonParameters.topK()))).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxTokens, (Object)Utils.getOrDefault((Object)commonParameters.maxOutputTokens(), (Object)1024)))).stopSequences(Utils.getOrDefault((List)builder.stopSequences, (List)commonParameters.stopSequences()))).toolSpecifications(Utils.getOrDefault((List)builder.toolSpecifications, (List)commonParameters.toolSpecifications()))).toolChoice((ToolChoice)Utils.getOrDefault((Object)builder.toolChoice, (Object)commonParameters.toolChoice()))).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat()))).cacheSystemMessages((Boolean)Utils.getOrDefault((Object)builder.cacheSystemMessages, (Object)anthropicDefaults.cacheSystemMessages())).cacheTools((Boolean)Utils.getOrDefault((Object)builder.cacheTools, (Object)anthropicDefaults.cacheTools())).thinkingType((String)Utils.getOrDefault((Object)builder.thinkingType, (Object)anthropicDefaults.thinkingType())).thinkingBudgetTokens((Integer)Utils.getOrDefault((Object)builder.thinkingBudgetTokens, (Object)anthropicDefaults.thinkingBudgetTokens())).sendThinking((Boolean)Utils.getOrDefault((Object)builder.sendThinking, (Object)anthropicDefaults.sendThinking())).midConversationSystemMessages((Boolean)Utils.getOrDefault((Object)builder.midConversationSystemMessages, (Object)anthropicDefaults.midConversationSystemMessages())).returnThinking((Boolean)Utils.getOrDefault((Object)builder.returnThinking, (Object)anthropicDefaults.returnThinking())).toolChoiceName((String)Utils.getOrDefault((Object)builder.toolChoiceName, (Object)anthropicDefaults.toolChoiceName())).disableParallelToolUse((Boolean)Utils.getOrDefault((Object)builder.disableParallelToolUse, (Object)anthropicDefaults.disableParallelToolUse())).userId((String)Utils.getOrDefault((Object)builder.userId, (Object)anthropicDefaults.userId())).returnCacheDiagnostics((Boolean)Utils.getOrDefault((Object)builder.returnCacheDiagnostics, (Object)anthropicDefaults.returnCacheDiagnostics())).build();
    }

    public static AnthropicStreamingChatModelBuilder builder() {
        return new AnthropicStreamingChatModelBuilder();
    }

    public void doChat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
        ValidationUtils.ensureNotNull((Object)handler, (String)"handler");
        AnthropicChatRequestParameters parameters = (AnthropicChatRequestParameters)chatRequest.parameters();
        InternalAnthropicHelper.validate((ChatRequestParameters)parameters);
        AnthropicCreateMessageRequest anthropicRequest = InternalAnthropicHelper.createAnthropicRequest(chatRequest, AnthropicChatModel.toThinking(parameters.thinkingType(), parameters.thinkingBudgetTokens(), this.thinkingDisplay), (Boolean)Utils.getOrDefault((Object)parameters.sendThinking(), (Object)true), (Boolean)Utils.getOrDefault((Object)parameters.midConversationSystemMessages(), (Object)false), (Boolean)Utils.getOrDefault((Object)parameters.cacheSystemMessages(), (Object)false) != false ? AnthropicCacheType.EPHEMERAL : AnthropicCacheType.NO_CACHE, (Boolean)Utils.getOrDefault((Object)parameters.cacheTools(), (Object)false) != false ? AnthropicCacheType.EPHEMERAL : AnthropicCacheType.NO_CACHE, true, parameters.toolChoiceName(), parameters.disableParallelToolUse(), this.serverTools, this.toolMetadataKeysToSend, parameters.userId(), this.skills, this.customParameters, this.strictTools, (Boolean)Utils.getOrDefault((Object)parameters.returnCacheDiagnostics(), (Object)false), parameters.previousMessageId());
        boolean returnThinking = (Boolean)Utils.getOrDefault((Object)parameters.returnThinking(), (Object)false);
        this.client.createMessage(anthropicRequest, new AnthropicCreateMessageOptions(returnThinking, this.returnServerToolResults), handler);
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.ANTHROPIC;
    }

    public AnthropicChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public Set<Capability> supportedCapabilities() {
        return this.supportedCapabilities;
    }

    public static class AnthropicStreamingChatModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String version;
        private String beta;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Integer topK;
        private Integer maxTokens;
        private List<String> stopSequences;
        private ResponseFormat responseFormat;
        private List<ToolSpecification> toolSpecifications;
        private Boolean cacheSystemMessages;
        private Boolean cacheTools;
        private String thinkingType;
        private Integer thinkingBudgetTokens;
        private String thinkingDisplay;
        private Boolean returnThinking;
        private Boolean sendThinking;
        private Boolean midConversationSystemMessages;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private List<ChatModelListener> listeners;
        private ChatRequestParameters defaultRequestParameters;
        private ToolChoice toolChoice;
        private String toolChoiceName;
        private Boolean disableParallelToolUse;
        private List<AnthropicServerTool> serverTools;
        private Boolean returnServerToolResults;
        private Set<String> toolMetadataKeysToSend;
        private List<AnthropicSkill> skills;
        private String userId;
        private Map<String, Object> customParameters;
        private Boolean strictTools;
        private Set<Capability> supportedCapabilities;
        private Supplier<Map<String, String>> customHeadersSupplier;
        private Boolean returnCacheDiagnostics;

        public AnthropicStreamingChatModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public AnthropicStreamingChatModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public AnthropicStreamingChatModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public AnthropicStreamingChatModelBuilder version(String version) {
            this.version = version;
            return this;
        }

        public AnthropicStreamingChatModelBuilder beta(String beta) {
            this.beta = beta;
            return this;
        }

        public AnthropicStreamingChatModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public AnthropicStreamingChatModelBuilder modelName(AnthropicChatModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public AnthropicStreamingChatModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public AnthropicStreamingChatModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public AnthropicStreamingChatModelBuilder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public AnthropicStreamingChatModelBuilder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public AnthropicStreamingChatModelBuilder stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return this;
        }

        public AnthropicStreamingChatModelBuilder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public AnthropicStreamingChatModelBuilder toolSpecifications(List<ToolSpecification> toolSpecifications) {
            this.toolSpecifications = toolSpecifications;
            return this;
        }

        public AnthropicStreamingChatModelBuilder toolSpecifications(ToolSpecification ... toolSpecifications) {
            return this.toolSpecifications(Arrays.asList(toolSpecifications));
        }

        public AnthropicStreamingChatModelBuilder cacheSystemMessages(Boolean cacheSystemMessages) {
            this.cacheSystemMessages = cacheSystemMessages;
            return this;
        }

        public AnthropicStreamingChatModelBuilder cacheTools(Boolean cacheTools) {
            this.cacheTools = cacheTools;
            return this;
        }

        public AnthropicStreamingChatModelBuilder thinkingType(String thinkingType) {
            this.thinkingType = thinkingType;
            return this;
        }

        public AnthropicStreamingChatModelBuilder thinkingBudgetTokens(Integer thinkingBudgetTokens) {
            this.thinkingBudgetTokens = thinkingBudgetTokens;
            return this;
        }

        public AnthropicStreamingChatModelBuilder thinkingDisplay(String thinkingDisplay) {
            this.thinkingDisplay = thinkingDisplay;
            return this;
        }

        public AnthropicStreamingChatModelBuilder returnThinking(Boolean returnThinking) {
            this.returnThinking = returnThinking;
            return this;
        }

        public AnthropicStreamingChatModelBuilder sendThinking(Boolean sendThinking) {
            this.sendThinking = sendThinking;
            return this;
        }

        public AnthropicStreamingChatModelBuilder midConversationSystemMessages(Boolean midConversationSystemMessages) {
            this.midConversationSystemMessages = midConversationSystemMessages;
            return this;
        }

        public AnthropicStreamingChatModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public AnthropicStreamingChatModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public AnthropicStreamingChatModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public AnthropicStreamingChatModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public AnthropicStreamingChatModelBuilder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public AnthropicStreamingChatModelBuilder listeners(ChatModelListener ... listeners) {
            return this.listeners(Arrays.asList(listeners));
        }

        public AnthropicStreamingChatModelBuilder defaultRequestParameters(ChatRequestParameters parameters) {
            this.defaultRequestParameters = parameters;
            return this;
        }

        public AnthropicStreamingChatModelBuilder toolChoice(ToolChoice toolChoice) {
            this.toolChoice = toolChoice;
            return this;
        }

        public AnthropicStreamingChatModelBuilder toolChoiceName(String toolChoiceName) {
            this.toolChoiceName = toolChoiceName;
            return this;
        }

        public AnthropicStreamingChatModelBuilder disableParallelToolUse(Boolean disableParallelToolUse) {
            this.disableParallelToolUse = disableParallelToolUse;
            return this;
        }

        public AnthropicStreamingChatModelBuilder serverTools(List<AnthropicServerTool> serverTools) {
            this.serverTools = serverTools;
            return this;
        }

        public AnthropicStreamingChatModelBuilder serverTools(AnthropicServerTool ... serverTools) {
            return this.serverTools(Arrays.asList(serverTools));
        }

        public AnthropicStreamingChatModelBuilder skills(List<AnthropicSkill> skills) {
            this.skills = skills;
            return this;
        }

        public AnthropicStreamingChatModelBuilder skills(AnthropicSkill ... skills) {
            return this.skills(Arrays.asList(skills));
        }

        public AnthropicStreamingChatModelBuilder returnServerToolResults(Boolean returnServerToolResults) {
            this.returnServerToolResults = returnServerToolResults;
            return this;
        }

        public AnthropicStreamingChatModelBuilder toolMetadataKeysToSend(Set<String> toolMetadataKeysToSend) {
            this.toolMetadataKeysToSend = toolMetadataKeysToSend;
            return this;
        }

        public AnthropicStreamingChatModelBuilder toolMetadataKeysToSend(String ... toolMetadataKeysToSend) {
            return this.toolMetadataKeysToSend(new HashSet<String>(Arrays.asList(toolMetadataKeysToSend)));
        }

        public AnthropicStreamingChatModelBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public AnthropicStreamingChatModelBuilder returnCacheDiagnostics(Boolean returnCacheDiagnostics) {
            this.returnCacheDiagnostics = returnCacheDiagnostics;
            return this;
        }

        public AnthropicStreamingChatModelBuilder customParameters(Map<String, Object> customParameters) {
            this.customParameters = customParameters;
            return this;
        }

        public AnthropicStreamingChatModelBuilder strictTools(Boolean strictTools) {
            this.strictTools = strictTools;
            return this;
        }

        public AnthropicStreamingChatModelBuilder supportedCapabilities(Capability ... supportedCapabilities) {
            this.supportedCapabilities = Arrays.stream(supportedCapabilities).collect(Collectors.toSet());
            return this;
        }

        public AnthropicStreamingChatModelBuilder supportedCapabilities(Set<Capability> supportedCapabilities) {
            this.supportedCapabilities = supportedCapabilities;
            return this;
        }

        public AnthropicStreamingChatModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public AnthropicStreamingChatModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public AnthropicStreamingChatModel build() {
            return new AnthropicStreamingChatModel(this);
        }
    }
}

