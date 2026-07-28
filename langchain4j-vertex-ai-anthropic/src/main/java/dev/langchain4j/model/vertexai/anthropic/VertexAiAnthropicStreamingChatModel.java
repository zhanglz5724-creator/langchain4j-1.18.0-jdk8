/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  com.google.auth.oauth2.GoogleCredentials
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.AiMessage$Builder
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.vertexai.anthropic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.auth.oauth2.GoogleCredentials;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.vertexai.anthropic.VertexAiAnthropicExceptionMapper;
import dev.langchain4j.model.vertexai.anthropic.internal.ValidationUtils;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicContent;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicRequest;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicResponse;
import dev.langchain4j.model.vertexai.anthropic.internal.client.StreamingResponseHandler;
import dev.langchain4j.model.vertexai.anthropic.internal.client.VertexAiAnthropicClient;
import dev.langchain4j.model.vertexai.anthropic.internal.mapper.AnthropicRequestMapper;
import dev.langchain4j.model.vertexai.anthropic.internal.mapper.AnthropicResponseMapper;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertexAiAnthropicStreamingChatModel
implements StreamingChatModel,
Closeable {
    private static final Logger logger = LoggerFactory.getLogger(VertexAiAnthropicStreamingChatModel.class);
    private final VertexAiAnthropicClient client;
    private final ChatRequestParameters defaultRequestParameters;
    private final Boolean logRequests;
    private final Boolean logResponses;
    private final Boolean enablePromptCaching;
    private final List<ChatModelListener> listeners;

    public VertexAiAnthropicStreamingChatModel(VertexAiAnthropicStreamingChatModelBuilder builder) {
        ChatRequestParameters commonParameters = builder.defaultRequestParameters != null ? builder.defaultRequestParameters : DefaultChatRequestParameters.EMPTY;
        String modelName = dev.langchain4j.internal.ValidationUtils.ensureNotBlank((String)((String)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName())), (String)"modelName");
        this.client = new VertexAiAnthropicClient(dev.langchain4j.internal.ValidationUtils.ensureNotBlank((String)builder.project, (String)"project"), dev.langchain4j.internal.ValidationUtils.ensureNotBlank((String)builder.location, (String)"location"), modelName, builder.credentials);
        this.defaultRequestParameters = DefaultChatRequestParameters.builder().modelName(modelName).maxOutputTokens(ValidationUtils.validateMaxTokens((Integer)Utils.getOrDefault((Object)builder.maxTokens, (Object)commonParameters.maxOutputTokens()))).temperature(ValidationUtils.validateTemperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature()))).topP(ValidationUtils.validateTopP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP()))).topK(ValidationUtils.validateTopK((Integer)Utils.getOrDefault((Object)builder.topK, (Object)commonParameters.topK()))).stopSequences(Utils.getOrDefault((List)builder.stopSequences, (List)commonParameters.stopSequences())).toolSpecifications(commonParameters.toolSpecifications()).toolChoice(commonParameters.toolChoice()).responseFormat(commonParameters.responseFormat()).build();
        this.logRequests = (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false);
        this.logResponses = (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false);
        this.enablePromptCaching = (Boolean)Utils.getOrDefault((Object)builder.enablePromptCaching, (Object)false);
        this.listeners = builder.listeners != null ? new ArrayList(builder.listeners) : new ArrayList();
    }

    public ChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public void doChat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
        ChatRequestParameters parameters = chatRequest.parameters();
        if (parameters.responseFormat() != null) {
            try {
                handler.onError((Throwable)new UnsupportedFeatureException("JSON response format is not supported by Vertex AI Anthropic"));
            }
            catch (Exception userException) {
                logger.warn("User's onError handler threw an exception, ignoring", (Throwable)userException);
            }
            return;
        }
        try {
            String requestModelName = this.determineRequestModelName(chatRequest.parameters());
            AnthropicRequest anthropicRequest = this.buildAnthropicRequest(chatRequest, requestModelName);
            this.logRequestIfEnabled(requestModelName, chatRequest.parameters().modelName(), anthropicRequest);
            this.client.generateContentStreaming(anthropicRequest, requestModelName, this.createStreamingResponseHandler(handler));
        }
        catch (Exception e) {
            try {
                handler.onError((Throwable)VertexAiAnthropicExceptionMapper.INSTANCE.mapException(e));
            }
            catch (Exception userException) {
                logger.warn("User's onError handler threw an exception, ignoring", (Throwable)userException);
            }
        }
    }

    private String determineRequestModelName(ChatRequestParameters parameters) {
        return parameters.modelName();
    }

    private AnthropicRequest buildAnthropicRequest(ChatRequest chatRequest, String requestModelName) {
        ChatRequestParameters parameters = chatRequest.parameters();
        List messages = chatRequest.messages();
        List toolSpecifications = parameters.toolSpecifications();
        return AnthropicRequestMapper.toRequest(requestModelName, messages, toolSpecifications, parameters.toolChoice(), parameters.maxOutputTokens(), parameters.temperature(), parameters.topP(), parameters.topK(), parameters.stopSequences(), this.enablePromptCaching);
    }

    private void logRequestIfEnabled(String requestModelName, String parameterModelName, AnthropicRequest anthropicRequest) {
        if (this.logRequests.booleanValue()) {
            logger.debug("Using model name: {}", (Object)requestModelName);
            logger.debug("Anthropic streaming request: {}", (Object)anthropicRequest);
        }
    }

    private StreamingResponseHandler createStreamingResponseHandler(final StreamingChatResponseHandler handler) {
        return new StreamingResponseHandler(){
            private final StringBuilder currentText = new StringBuilder();
            private final List<ToolExecutionRequest> toolCalls = new ArrayList<ToolExecutionRequest>();
            private AnthropicResponse fullResponse;

            @Override
            public void onResponse(AnthropicResponse response) {
                this.fullResponse = response;
                this.extractToolCallsFromResponse(response);
            }

            @Override
            public void onChunk(String jsonChunk) {
                try {
                    if (VertexAiAnthropicStreamingChatModel.this.logResponses.booleanValue()) {
                        logger.debug("Anthropic streaming chunk: {}", (Object)jsonChunk);
                    }
                    this.processStreamingChunk(jsonChunk, handler);
                }
                catch (Exception e) {
                    logger.error("Error processing streaming chunk", (Throwable)e);
                    try {
                        handler.onError((Throwable)e);
                    }
                    catch (Exception userException) {
                        logger.warn("User's onError handler threw an exception, ignoring", (Throwable)userException);
                    }
                }
            }

            @Override
            public void onComplete() {
                try {
                    this.sendCompleteToolCalls(handler);
                    this.sendCompleteResponse(handler);
                }
                catch (Exception e) {
                    try {
                        handler.onError((Throwable)e);
                    }
                    catch (Exception userException) {
                        logger.warn("User's onError handler threw an exception, ignoring", (Throwable)userException);
                    }
                }
            }

            @Override
            public void onError(Throwable error) {
                try {
                    handler.onError((Throwable)VertexAiAnthropicExceptionMapper.INSTANCE.mapException(error));
                }
                catch (Exception userException) {
                    logger.warn("User's onError handler threw an exception, ignoring", (Throwable)userException);
                }
            }

            private void extractToolCallsFromResponse(AnthropicResponse response) {
                if (response.content != null) {
                    logger.debug("Processing {} content blocks from response", (Object)response.content.size());
                    for (AnthropicContent content : response.content) {
                        logger.debug("Content block: type={}, name={}, id={}", new Object[]{content.type, content.name, content.id});
                        if (!this.isToolUseContent(content)) continue;
                        this.processToolContent(content);
                    }
                    logger.debug("Total tool calls extracted: {}", (Object)this.toolCalls.size());
                }
            }

            private boolean isToolUseContent(AnthropicContent content) {
                return "tool_use".equals(content.type) && content.name != null;
            }

            private void processToolContent(AnthropicContent content) {
                try {
                    String arguments = this.serializeToolArguments(content.input);
                    ToolExecutionRequest toolRequest = ToolExecutionRequest.builder().id(content.id).name(content.name).arguments(arguments).build();
                    this.toolCalls.add(toolRequest);
                    logger.debug("Added tool call: {}", (Object)toolRequest);
                }
                catch (Exception e) {
                    logger.warn("Failed to serialize tool arguments for {}: {}", (Object)content.name, (Object)e.getMessage());
                }
            }

            private String serializeToolArguments(Object input) throws JsonProcessingException {
                if (input == null) {
                    return "{}";
                }
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                return mapper.writeValueAsString(input);
            }

            private void processStreamingChunk(String jsonChunk, StreamingChatResponseHandler handler2) {
                if (jsonChunk.contains("\"type\":\"content_block_delta\"")) {
                    this.handleTextDelta(jsonChunk, handler2);
                } else if (jsonChunk.contains("\"type\":\"content_block_start\"")) {
                    this.handleToolCallStart(jsonChunk);
                } else if (jsonChunk.contains("\"type\":\"content_block_stop\"")) {
                    this.handleContentBlockStop(jsonChunk);
                }
            }

            private void handleTextDelta(String jsonChunk, StreamingChatResponseHandler handler2) {
                String textDelta = this.extractTextDelta(jsonChunk);
                if (textDelta != null && !textDelta.isEmpty()) {
                    this.currentText.append(textDelta);
                    try {
                        handler2.onPartialResponse(textDelta);
                    }
                    catch (Exception userException) {
                        handler2.onError((Throwable)userException);
                    }
                }
            }

            private String extractTextDelta(String jsonChunk) {
                try {
                    JsonNode textNode;
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = mapper.readTree(jsonChunk);
                    JsonNode deltaNode = rootNode.get("delta");
                    if (deltaNode != null && !deltaNode.isNull() && (textNode = deltaNode.get("text")) != null && !textNode.isNull() && textNode.isTextual()) {
                        return textNode.asText();
                    }
                }
                catch (Exception e) {
                    logger.warn("Failed to extract text delta from chunk: {}", (Object)jsonChunk, (Object)e);
                }
                return null;
            }

            private void handleToolCallStart(String jsonChunk) {
                if (Boolean.TRUE.equals(VertexAiAnthropicStreamingChatModel.this.logResponses)) {
                    logger.debug("Tool call started in chunk: {}", (Object)jsonChunk);
                }
            }

            private void handleContentBlockStop(String jsonChunk) {
                if (Boolean.TRUE.equals(VertexAiAnthropicStreamingChatModel.this.logResponses)) {
                    logger.debug("Content block stopped in chunk: {}", (Object)jsonChunk);
                }
            }

            private void sendCompleteToolCalls(StreamingChatResponseHandler handler2) {
                if (!this.toolCalls.isEmpty()) {
                    for (int i = 0; i < this.toolCalls.size(); ++i) {
                        ToolExecutionRequest toolRequest = this.toolCalls.get(i);
                        CompleteToolCall completeToolCall = new CompleteToolCall(i, toolRequest);
                        logger.debug("Calling onCompleteToolCall for index {}: {}", (Object)i, (Object)toolRequest);
                        handler2.onCompleteToolCall(completeToolCall);
                    }
                }
            }

            private void sendCompleteResponse(StreamingChatResponseHandler handler2) {
                if (this.fullResponse != null) {
                    this.sendMappedResponse(handler2);
                } else {
                    this.sendFallbackResponse(handler2);
                }
            }

            private void sendMappedResponse(StreamingChatResponseHandler handler2) {
                ChatResponse chatResponse = AnthropicResponseMapper.toChatResponse(this.fullResponse);
                logger.debug("ChatResponse from mapper: toolExecutionRequests.size()={}", (Object)chatResponse.aiMessage().toolExecutionRequests().size());
                logger.debug("About to call onCompleteResponse with: {}", (Object)chatResponse.aiMessage().toolExecutionRequests());
                handler2.onCompleteResponse(chatResponse);
            }

            private void sendFallbackResponse(StreamingChatResponseHandler handler2) {
                AiMessage.Builder aiMessageBuilder = AiMessage.builder().text(this.currentText.toString());
                if (!this.toolCalls.isEmpty()) {
                    aiMessageBuilder.toolExecutionRequests(this.toolCalls);
                }
                ChatResponse fallbackResponse = ChatResponse.builder().aiMessage(aiMessageBuilder.build()).tokenUsage(new TokenUsage(Integer.valueOf(this.currentText.length() / 4), Integer.valueOf(this.currentText.length() / 4))).finishReason(FinishReason.STOP).build();
                handler2.onCompleteResponse(fallbackResponse);
            }
        };
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

    public static VertexAiAnthropicStreamingChatModelBuilder builder() {
        return new VertexAiAnthropicStreamingChatModelBuilder();
    }

    public static class VertexAiAnthropicStreamingChatModelBuilder {
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

        public VertexAiAnthropicStreamingChatModelBuilder project(String project) {
            this.project = project;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder location(String location) {
            this.location = location;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder defaultRequestParameters(ChatRequestParameters parameters) {
            this.defaultRequestParameters = parameters;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder enablePromptCaching(Boolean enablePromptCaching) {
            this.enablePromptCaching = enablePromptCaching;
            return this;
        }

        public VertexAiAnthropicStreamingChatModelBuilder credentials(GoogleCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public VertexAiAnthropicStreamingChatModel build() {
            return new VertexAiAnthropicStreamingChatModel(this);
        }
    }
}

