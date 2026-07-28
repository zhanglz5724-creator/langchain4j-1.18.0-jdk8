/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.mistralai;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.mistralai.InternalMistralAIHelper;
import dev.langchain4j.model.mistralai.MistralAiChatModelName;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionRequest;
import dev.langchain4j.model.mistralai.internal.client.MistralAiClient;
import dev.langchain4j.model.mistralai.spi.MistralAiStreamingChatModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class MistralAiStreamingChatModel
implements StreamingChatModel {
    private final MistralAiClient client;
    private final Boolean safePrompt;
    private final Integer randomSeed;
    private final boolean returnThinking;
    private final boolean sendThinking;
    private final List<ChatModelListener> listeners;
    private final Set<Capability> supportedCapabilities;
    private final boolean strictJsonSchema;
    private final ChatRequestParameters defaultRequestParameters;

    public MistralAiStreamingChatModel(MistralAiStreamingChatModelBuilder builder) {
        this.client = ((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)MistralAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.mistral.ai/v1"))).apiKey(builder.apiKey)).timeout(builder.timeout)).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).customHeaders(builder.customHeadersSupplier)).build();
        this.safePrompt = builder.safePrompt;
        this.randomSeed = builder.randomSeed;
        this.returnThinking = (Boolean)Utils.getOrDefault((Object)builder.returnThinking, (Object)false);
        this.sendThinking = (Boolean)Utils.getOrDefault((Object)builder.sendThinking, (Object)false);
        this.listeners = Utils.copy((List)builder.listeners);
        this.supportedCapabilities = Utils.copy((Set)builder.supportedCapabilities);
        this.strictJsonSchema = (Boolean)Utils.getOrDefault((Object)builder.strictJsonSchema, (Object)false);
        this.defaultRequestParameters = this.initDefaultRequestParameters(builder);
    }

    private ChatRequestParameters initDefaultRequestParameters(MistralAiStreamingChatModelBuilder builder) {
        ChatRequestParameters commonParameters;
        if (builder.defaultRequestParameters != null) {
            InternalMistralAIHelper.validate(builder.defaultRequestParameters);
            commonParameters = builder.defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        return DefaultChatRequestParameters.builder().modelName((String)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName())).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature())).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP())).frequencyPenalty((Double)Utils.getOrDefault((Object)builder.frequencyPenalty, (Object)commonParameters.frequencyPenalty())).presencePenalty((Double)Utils.getOrDefault((Object)builder.presencePenalty, (Object)commonParameters.presencePenalty())).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxTokens, (Object)commonParameters.maxOutputTokens())).stopSequences(Utils.getOrDefault((List)builder.stopSequences, (List)commonParameters.stopSequences())).toolSpecifications(commonParameters.toolSpecifications()).toolChoice(commonParameters.toolChoice()).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat())).build();
    }

    public void doChat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
        ValidationUtils.ensureNotNull((Object)handler, (String)"handler");
        InternalMistralAIHelper.validate(chatRequest.parameters());
        MistralAiChatCompletionRequest request = InternalMistralAIHelper.createMistralAiRequest(chatRequest, this.safePrompt, this.randomSeed, true, this.sendThinking, this.strictJsonSchema);
        this.client.streamingChatCompletion(request, handler, this.returnThinking);
    }

    public ChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.MISTRAL_AI;
    }

    public Set<Capability> supportedCapabilities() {
        return this.supportedCapabilities;
    }

    public static MistralAiStreamingChatModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(MistralAiStreamingChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            MistralAiStreamingChatModelBuilderFactory factory = (MistralAiStreamingChatModelBuilderFactory)iterator.next();
            return (MistralAiStreamingChatModelBuilder)factory.get();
        }
        return new MistralAiStreamingChatModelBuilder();
    }

    public static class MistralAiStreamingChatModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Integer maxTokens;
        private Boolean safePrompt;
        private Integer randomSeed;
        private Boolean returnThinking;
        private Boolean sendThinking;
        private ResponseFormat responseFormat;
        private List<String> stopSequences;
        private Double frequencyPenalty;
        private Double presencePenalty;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private List<ChatModelListener> listeners;
        private Set<Capability> supportedCapabilities;
        private Boolean strictJsonSchema;
        private ChatRequestParameters defaultRequestParameters;
        private Supplier<Map<String, String>> customHeadersSupplier;

        public MistralAiStreamingChatModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public MistralAiStreamingChatModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public MistralAiStreamingChatModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public MistralAiStreamingChatModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public MistralAiStreamingChatModelBuilder modelName(MistralAiChatModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public MistralAiStreamingChatModelBuilder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public MistralAiStreamingChatModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public MistralAiStreamingChatModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public MistralAiStreamingChatModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public MistralAiStreamingChatModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public MistralAiStreamingChatModelBuilder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public MistralAiStreamingChatModelBuilder safePrompt(Boolean safePrompt) {
            this.safePrompt = safePrompt;
            return this;
        }

        public MistralAiStreamingChatModelBuilder randomSeed(Integer randomSeed) {
            this.randomSeed = randomSeed;
            return this;
        }

        public MistralAiStreamingChatModelBuilder returnThinking(Boolean returnThinking) {
            this.returnThinking = returnThinking;
            return this;
        }

        public MistralAiStreamingChatModelBuilder sendThinking(Boolean sendThinking) {
            this.sendThinking = sendThinking;
            return this;
        }

        public MistralAiStreamingChatModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public MistralAiStreamingChatModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public MistralAiStreamingChatModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public MistralAiStreamingChatModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public MistralAiStreamingChatModelBuilder supportedCapabilities(Capability ... supportedCapabilities) {
            this.supportedCapabilities = Arrays.stream(supportedCapabilities).collect(Collectors.toSet());
            return this;
        }

        public MistralAiStreamingChatModelBuilder supportedCapabilities(Set<Capability> supportedCapabilities) {
            this.supportedCapabilities = new HashSet<Capability>(supportedCapabilities);
            return this;
        }

        public MistralAiStreamingChatModelBuilder strictJsonSchema(Boolean strictJsonSchema) {
            this.strictJsonSchema = strictJsonSchema;
            return this;
        }

        public MistralAiStreamingChatModelBuilder stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return this;
        }

        public MistralAiStreamingChatModelBuilder presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public MistralAiStreamingChatModelBuilder frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public MistralAiStreamingChatModelBuilder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public MistralAiStreamingChatModelBuilder defaultRequestParameters(ChatRequestParameters parameters) {
            this.defaultRequestParameters = parameters;
            return this;
        }

        public MistralAiStreamingChatModel build() {
            return new MistralAiStreamingChatModel(this);
        }
    }
}

