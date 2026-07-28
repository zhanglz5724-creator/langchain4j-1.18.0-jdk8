/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.auth.oauth2.GoogleCredentials
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.vertexai.anthropic;

import com.google.auth.oauth2.GoogleCredentials;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.vertexai.anthropic.VertexAiAnthropicExceptionMapper;
import dev.langchain4j.model.vertexai.anthropic.internal.ValidationUtils;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicRequest;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicResponse;
import dev.langchain4j.model.vertexai.anthropic.internal.client.VertexAiAnthropicClient;
import dev.langchain4j.model.vertexai.anthropic.internal.mapper.AnthropicRequestMapper;
import dev.langchain4j.model.vertexai.anthropic.internal.mapper.AnthropicResponseMapper;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertexAiAnthropicChatModel
implements ChatModel,
Closeable {
    private static final Logger logger = LoggerFactory.getLogger(VertexAiAnthropicChatModel.class);
    private final VertexAiAnthropicClient client;
    private final String modelName;
    private final Integer maxTokens;
    private final Double temperature;
    private final Double topP;
    private final Integer topK;
    private final List<String> stopSequences;
    private final Boolean logRequests;
    private final Boolean logResponses;
    private final Boolean enablePromptCaching;
    private final List<ChatModelListener> listeners;
    private final String location;

    public VertexAiAnthropicChatModel(VertexAiAnthropicChatModelBuilder builder) {
        this.client = new VertexAiAnthropicClient(dev.langchain4j.internal.ValidationUtils.ensureNotBlank((String)builder.project, (String)"project"), dev.langchain4j.internal.ValidationUtils.ensureNotBlank((String)builder.location, (String)"location"), dev.langchain4j.internal.ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName"), builder.credentials);
        this.modelName = builder.modelName;
        this.maxTokens = ValidationUtils.validateMaxTokens(builder.maxTokens);
        this.temperature = ValidationUtils.validateTemperature(builder.temperature);
        this.topP = ValidationUtils.validateTopP(builder.topP);
        this.topK = ValidationUtils.validateTopK(builder.topK);
        this.stopSequences = builder.stopSequences;
        this.logRequests = (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false);
        this.logResponses = (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false);
        this.enablePromptCaching = (Boolean)Utils.getOrDefault((Object)builder.enablePromptCaching, (Object)false);
        this.listeners = builder.listeners != null ? new ArrayList(builder.listeners) : new ArrayList();
        this.location = dev.langchain4j.internal.ValidationUtils.ensureNotBlank((String)builder.location, (String)"location");
    }

    public ChatResponse doChat(ChatRequest chatRequest) {
        ChatRequestParameters parameters = chatRequest.parameters();
        List messages = chatRequest.messages();
        List toolSpecifications = parameters.toolSpecifications();
        if (parameters.responseFormat() != null && parameters.responseFormat().jsonSchema() != null) {
            throw new UnsupportedFeatureException("JSON response format is not supported yet");
        }
        try {
            String requestModelName = (String)Utils.getOrDefault((Object)parameters.modelName(), (Object)this.modelName);
            if (this.logRequests.booleanValue()) {
                logger.debug("Base URL: {}-aiplatform.googleapis.com:443", (Object)this.location);
                logger.debug("Using model name: {} (from parameters: {}, default: {})", new Object[]{requestModelName, parameters.modelName(), this.modelName});
            }
            AnthropicRequest anthropicRequest = AnthropicRequestMapper.toRequest(requestModelName, messages, toolSpecifications, parameters.toolChoice(), parameters.maxOutputTokens() != null ? parameters.maxOutputTokens() : this.maxTokens, this.temperature, this.topP, this.topK, parameters.stopSequences() != null && !parameters.stopSequences().isEmpty() ? parameters.stopSequences() : this.stopSequences, this.enablePromptCaching);
            if (this.logRequests.booleanValue()) {
                logger.debug("Anthropic request: {}", (Object)anthropicRequest);
            }
            AnthropicResponse anthropicResponse = this.client.generateContent(anthropicRequest, requestModelName);
            if (this.logResponses.booleanValue()) {
                logger.debug("Anthropic response: {}", (Object)anthropicResponse);
            }
            return AnthropicResponseMapper.toChatResponse(anthropicResponse);
        }
        catch (IOException e) {
            throw VertexAiAnthropicExceptionMapper.INSTANCE.mapException(e);
        }
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.GOOGLE_VERTEX_AI_ANTHROPIC;
    }

    @Override
    public void close() throws IOException {
        if (this.client != null) {
            this.client.close();
        }
    }

    public static VertexAiAnthropicChatModelBuilder builder() {
        return new VertexAiAnthropicChatModelBuilder();
    }

    public static class VertexAiAnthropicChatModelBuilder {
        private String project;
        private String location;
        private ChatRequestParameters defaultRequestParameters;
        private String modelName;
        private Integer maxTokens;
        private Double temperature;
        private Double topP;
        private Integer topK;
        private List<String> stopSequences;
        private Boolean logRequests;
        private Boolean logResponses;
        private Boolean enablePromptCaching;
        private List<ChatModelListener> listeners;
        private GoogleCredentials credentials;

        public VertexAiAnthropicChatModelBuilder project(String project) {
            this.project = project;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder location(String location) {
            this.location = location;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder defaultRequestParameters(ChatRequestParameters parameters) {
            this.defaultRequestParameters = parameters;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder enablePromptCaching(Boolean enablePromptCaching) {
            this.enablePromptCaching = enablePromptCaching;
            return this;
        }

        public VertexAiAnthropicChatModelBuilder credentials(GoogleCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public VertexAiAnthropicChatModel build() {
            return new VertexAiAnthropicChatModel(this);
        }
    }
}

