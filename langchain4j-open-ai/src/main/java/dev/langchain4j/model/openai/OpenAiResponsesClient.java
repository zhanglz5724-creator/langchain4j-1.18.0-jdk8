/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolExecutionRequest$Builder
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.AiMessage$Builder
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.ImageContent$DetailLevel
 *  dev.langchain4j.data.message.PdfFileContent
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.http.client.HttpClientBuilderLoader
 *  dev.langchain4j.http.client.HttpMethod
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.HttpRequest$Builder
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.http.client.log.LoggingHttpClient
 *  dev.langchain4j.http.client.sse.CancellationUnsupportedHandle
 *  dev.langchain4j.http.client.sse.DefaultServerSentEventParser
 *  dev.langchain4j.http.client.sse.ServerSentEvent
 *  dev.langchain4j.http.client.sse.ServerSentEventContext
 *  dev.langchain4j.http.client.sse.ServerSentEventListener
 *  dev.langchain4j.http.client.sse.ServerSentEventParser
 *  dev.langchain4j.http.client.sse.ServerSentEventParsingHandle
 *  dev.langchain4j.http.client.sse.ServerSentEventParsingHandleUtils
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler
 *  dev.langchain4j.internal.ToolSpecificationUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonRawSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponse$Builder
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.PartialToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  dev.langchain4j.model.output.FinishReason
 */
package dev.langchain4j.model.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.log.LoggingHttpClient;
import dev.langchain4j.http.client.sse.CancellationUnsupportedHandle;
import dev.langchain4j.http.client.sse.DefaultServerSentEventParser;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.http.client.sse.ServerSentEventContext;
import dev.langchain4j.http.client.sse.ServerSentEventListener;
import dev.langchain4j.http.client.sse.ServerSentEventParser;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandle;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandleUtils;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler;
import dev.langchain4j.internal.ToolSpecificationUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonRawSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.StreamingHandle;
import dev.langchain4j.model.openai.OpenAiResponsesChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiResponsesChatResponseMetadata;
import dev.langchain4j.model.openai.OpenAiTokenUsage;
import dev.langchain4j.model.output.FinishReason;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dev.langchain4j.internal.JsonSchemaElementUtils.toMap;
import static dev.langchain4j.internal.ToolSpecificationUtils.isEffectivelyStrict;

class OpenAiResponsesClient {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final String DEFAULT_BASE_URL = "https://api.openai.com/v1";
    private static final String OPENAI_ORGANIZATION_HEADER = "OpenAI-Organization";
    private static final String STREAM_DONE_MARKER = "[DONE]";
    private static final String EVENT_OUTPUT_TEXT_DELTA = "response.output_text.delta";
    private static final String EVENT_OUTPUT_ITEM_ADDED = "response.output_item.added";
    private static final String EVENT_FUNCTION_CALL_ARGUMENTS_DELTA = "response.function_call_arguments.delta";
    private static final String EVENT_FUNCTION_CALL_ARGUMENTS_DONE = "response.function_call_arguments.done";
    private static final String EVENT_OUTPUT_ITEM_DONE = "response.output_item.done";
    private static final String EVENT_REASONING_TEXT_DELTA = "response.reasoning_text.delta";
    private static final String EVENT_REASONING_SUMMARY_TEXT_DELTA = "response.reasoning_summary_text.delta";
    private static final String EVENT_RESPONSE_COMPLETED = "response.completed";
    private static final String EVENT_RESPONSE_INCOMPLETE = "response.incomplete";
    private static final String EVENT_RESPONSE_FAILED = "response.failed";
    private static final String EVENT_RESPONSE_ERROR = "response.error";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_ROLE = "role";
    private static final String FIELD_CONTENT = "content";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_PARAMETERS = "parameters";
    private static final String FIELD_PROPERTIES = "properties";
    private static final String FIELD_ARGUMENTS = "arguments";
    private static final String FIELD_DELTA = "delta";
    private static final String FIELD_TEXT = "text";
    private static final String FIELD_IMAGE_URL = "image_url";
    private static final String FIELD_FILE_URL = "file_url";
    private static final String FIELD_FILE_DATA = "file_data";
    private static final String FIELD_FILENAME = "filename";
    private static final String FIELD_DETAIL = "detail";
    private static final String FIELD_ITEM = "item";
    private static final String FIELD_ID = "id";
    private static final String FIELD_CALL_ID = "call_id";
    private static final String FIELD_ITEM_ID = "item_id";
    private static final String FIELD_OUTPUT_INDEX = "output_index";
    private static final String FIELD_RESPONSE = "response";
    private static final String FIELD_ERROR = "error";
    private static final String FIELD_MESSAGE = "message";
    private static final String FIELD_OUTPUT = "output";
    private static final String FIELD_USAGE = "usage";
    private static final String FIELD_INPUT_TOKENS = "input_tokens";
    private static final String FIELD_OUTPUT_TOKENS = "output_tokens";
    private static final String FIELD_TOTAL_TOKENS = "total_tokens";
    private static final String FIELD_INPUT_TOKENS_DETAILS = "input_tokens_details";
    private static final String FIELD_CACHED_TOKENS = "cached_tokens";
    private static final String FIELD_OUTPUT_TOKENS_DETAILS = "output_tokens_details";
    private static final String FIELD_REASONING_TOKENS = "reasoning_tokens";
    private static final String FIELD_MODEL = "model";
    private static final String FIELD_INPUT = "input";
    private static final String FIELD_STREAM = "stream";
    private static final String FIELD_STORE = "store";
    private static final String FIELD_TEMPERATURE = "temperature";
    private static final String FIELD_TOP_P = "top_p";
    private static final String FIELD_MAX_OUTPUT_TOKENS = "max_output_tokens";
    private static final String FIELD_MAX_TOOL_CALLS = "max_tool_calls";
    private static final String FIELD_PARALLEL_TOOL_CALLS = "parallel_tool_calls";
    private static final String FIELD_PREVIOUS_RESPONSE_ID = "previous_response_id";
    private static final String FIELD_TOP_LOGPROBS = "top_logprobs";
    private static final String FIELD_TOOLS = "tools";
    private static final String FIELD_TOOL_CHOICE = "tool_choice";
    private static final String FIELD_TRUNCATION = "truncation";
    private static final String FIELD_INCLUDE = "include";
    private static final String FIELD_SERVICE_TIER = "service_tier";
    private static final String FIELD_SAFETY_IDENTIFIER = "safety_identifier";
    private static final String FIELD_PROMPT_CACHE_KEY = "prompt_cache_key";
    private static final String FIELD_PROMPT_CACHE_RETENTION = "prompt_cache_retention";
    private static final String FIELD_REASONING = "reasoning";
    private static final String FIELD_EFFORT = "effort";
    private static final String FIELD_SUMMARY = "summary";
    private static final String FIELD_SUMMARY_TEXT = "summary_text";
    private static final String FIELD_ENCRYPTED_CONTENT = "encrypted_content";
    private static final String FIELD_STRICT = "strict";
    private static final String FIELD_STREAM_OPTIONS = "stream_options";
    private static final String FIELD_INCLUDE_OBFUSCATION = "include_obfuscation";
    private static final String FIELD_TEXT_VERBOSITY = "verbosity";
    private static final String FIELD_FORMAT = "format";
    private static final String FIELD_SCHEMA = "schema";
    private static final String FIELD_ADDITIONAL_PROPERTIES = "additionalProperties";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_CREATED_AT = "created_at";
    private static final String FIELD_COMPLETED_AT = "completed_at";
    private static final String DEFAULT_IMAGE_MIME_TYPE = "image/jpeg";
    private static final String DEFAULT_PDF_FILENAME = "pdf_file";
    private static final String ROLE_SYSTEM = "system";
    private static final String ROLE_USER = "user";
    private static final String ROLE_ASSISTANT = "assistant";
    static final String ENCRYPTED_REASONING_KEY = "encrypted_reasoning";
    private static final String TYPE_FUNCTION = "function";
    private static final String TYPE_FUNCTION_CALL = "function_call";
    private static final String TYPE_MESSAGE = "message";
    private static final String TYPE_REASONING = "reasoning";
    private static final String TYPE_OUTPUT_TEXT = "output_text";
    private static final String TYPE_OBJECT = "object";
    private static final String TYPE_INPUT_TEXT = "input_text";
    private static final String TYPE_INPUT_IMAGE = "input_image";
    private static final String TYPE_INPUT_FILE = "input_file";
    private static final String TYPE_FUNCTION_CALL_OUTPUT = "function_call_output";
    private static final String TYPE_JSON_OBJECT = "json_object";
    private static final String TYPE_JSON_SCHEMA = "json_schema";
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String apiKey;
    private final String organizationId;

    OpenAiResponsesClient(Builder builder) {
        HttpClientBuilder httpClientBuilder = (HttpClientBuilder)Utils.getOrDefault((Object)builder.httpClientBuilder, HttpClientBuilderLoader::loadHttpClientBuilder);
        HttpClient httpClient = httpClientBuilder.build();
        this.httpClient = builder.logRequests || builder.logResponses ? new LoggingHttpClient(httpClient, Boolean.valueOf(builder.logRequests), Boolean.valueOf(builder.logResponses)) : httpClient;
        this.baseUrl = (String)Utils.getOrDefault((Object)builder.baseUrl, (Object)DEFAULT_BASE_URL);
        this.apiKey = builder.apiKey;
        this.organizationId = builder.organizationId;
    }

    static Builder builder() {
        return new Builder();
    }

    ChatResponse chat(ChatRequest chatRequest, OpenAiResponsesChatRequestParameters parameters) {
        try {
            Map<String, Object> payload = this.buildRequestPayload(chatRequest, parameters, false);
            HttpRequest request = this.buildHttpRequest(payload, false);
            SuccessfulHttpResponse rawHttpResponse = this.httpClient.execute(request);
            return this.parseChatResponse(rawHttpResponse);
        }
        catch (Exception e) {
            throw ExceptionMapper.DEFAULT.mapException((Throwable)e);
        }
    }

    void streamingChat(ChatRequest chatRequest, OpenAiResponsesChatRequestParameters parameters, StreamingChatResponseHandler handler) {
        try {
            Map<String, Object> payload = this.buildRequestPayload(chatRequest, parameters, true);
            HttpRequest request = this.buildHttpRequest(payload, true);
            this.httpClient.execute(request, (ServerSentEventParser)new DefaultServerSentEventParser(), (ServerSentEventListener)new ResponsesApiEventListener(handler));
        }
        catch (Exception e) {
            InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> handler.onError((Throwable)ExceptionMapper.DEFAULT.mapException((Throwable)e)));
        }
    }
    private Map<String, Object> buildRequestPayload(
            ChatRequest chatRequest, OpenAiResponsesChatRequestParameters parameters, boolean stream) {
        List<Map<String, Object>> input = new ArrayList<>();
        for (ChatMessage message : chatRequest.messages()) {
            input.addAll(toResponsesMessages(message));
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put(FIELD_MODEL, parameters.modelName());
        payload.put(FIELD_INPUT, input);
        payload.put(FIELD_STREAM, stream);
        payload.put(FIELD_STORE, parameters.store());

        if (parameters.temperature() != null) {
            payload.put(FIELD_TEMPERATURE, parameters.temperature());
        }

        if (parameters.topP() != null) {
            payload.put(FIELD_TOP_P, parameters.topP());
        }

        if (parameters.maxOutputTokens() != null) {
            payload.put(FIELD_MAX_OUTPUT_TOKENS, parameters.maxOutputTokens());
        }

        if (parameters.maxToolCalls() != null) {
            payload.put(FIELD_MAX_TOOL_CALLS, parameters.maxToolCalls());
        }

        if (parameters.parallelToolCalls() != null) {
            payload.put(FIELD_PARALLEL_TOOL_CALLS, parameters.parallelToolCalls());
        }

        if (parameters.previousResponseId() != null) {
            payload.put(FIELD_PREVIOUS_RESPONSE_ID, parameters.previousResponseId());
        }

        if (parameters.topLogprobs() != null) {
            payload.put(FIELD_TOP_LOGPROBS, parameters.topLogprobs());
        }

        if (parameters.truncation() != null && !parameters.truncation().isEmpty()) {
            payload.put(FIELD_TRUNCATION, parameters.truncation());
        }

        if (parameters.include() != null && !parameters.include().isEmpty()) {
            payload.put(FIELD_INCLUDE, parameters.include());
        }

        if (parameters.serviceTier() != null) {
            payload.put(FIELD_SERVICE_TIER, parameters.serviceTier());
        }

        if (parameters.safetyIdentifier() != null) {
            payload.put(FIELD_SAFETY_IDENTIFIER, parameters.safetyIdentifier());
        }

        if (parameters.promptCacheKey() != null) {
            payload.put(FIELD_PROMPT_CACHE_KEY, parameters.promptCacheKey());
        }

        if (parameters.promptCacheRetention() != null) {
            payload.put(FIELD_PROMPT_CACHE_RETENTION, parameters.promptCacheRetention());
        }

        if (parameters.reasoningEffort() != null || parameters.reasoningSummary() != null) {
            Map<String, Object> reasoning = new LinkedHashMap<>();
            if (parameters.reasoningEffort() != null) {
                reasoning.put(FIELD_EFFORT, parameters.reasoningEffort());
            }
            if (parameters.reasoningSummary() != null) {
                reasoning.put(FIELD_SUMMARY, parameters.reasoningSummary());
            }
            payload.put(FIELD_REASONING, reasoning);
        }

        if (stream && parameters.streamIncludeObfuscation() != null) {
            Map<String, Object> streamOptions = new LinkedHashMap<>();
            streamOptions.put(FIELD_INCLUDE_OBFUSCATION, parameters.streamIncludeObfuscation());
            payload.put(FIELD_STREAM_OPTIONS, streamOptions);
        }

        boolean strictTools = Boolean.TRUE.equals(parameters.strictTools());
        List<Map<String, Object>> tools = new ArrayList<>();
        List<ToolSpecification> toolSpecifications = parameters.toolSpecifications();
        if (toolSpecifications != null && !toolSpecifications.isEmpty()) {
            for (ToolSpecification toolSpec : toolSpecifications) {
                boolean effectiveStrict = isEffectivelyStrict(toolSpec, strictTools);

                Map<String, Object> tool = new LinkedHashMap<>();
                tool.put(FIELD_TYPE, TYPE_FUNCTION);
                tool.put(FIELD_NAME, toolSpec.name());
                if (toolSpec.description() != null) {
                    tool.put(FIELD_DESCRIPTION, toolSpec.description());
                }

                Map<String, Object> functionParameters = null;
                if (toolSpec.parameters() != null) {
                    functionParameters = toMap(toolSpec.parameters(), effectiveStrict);
                } else if (effectiveStrict) {
                    functionParameters = new LinkedHashMap<>();
                    functionParameters.put(FIELD_TYPE, TYPE_OBJECT);
                    functionParameters.put(FIELD_PROPERTIES, Collections.emptyMap());
                    functionParameters.put(FIELD_ADDITIONAL_PROPERTIES, false);
                }

                if (functionParameters != null) {
                    tool.put(FIELD_PARAMETERS, functionParameters);
                }

                if (effectiveStrict) {
                    tool.put(FIELD_STRICT, true);
                }

                tools.add(tool);
            }
        }
        if (parameters.serverTools() != null) {
            tools.addAll(parameters.serverTools());
        }
        if (!tools.isEmpty()) {
            payload.put(FIELD_TOOLS, tools);

            if (parameters.toolChoice() != null) {
                payload.put(FIELD_TOOL_CHOICE, toToolChoiceString(parameters.toolChoice()));
            }
        }

        boolean strictJsonSchema = Boolean.TRUE.equals(parameters.strictJsonSchema());
        Map<String, Object> textConfig = toResponseTextConfig(parameters.responseFormat(), strictJsonSchema);
        if (parameters.textVerbosity() != null) {
            if (textConfig == null) {
                textConfig = new LinkedHashMap<>();
            }
            textConfig.put(FIELD_TEXT_VERBOSITY, parameters.textVerbosity());
        }
        if (textConfig != null) {
            payload.put(FIELD_TEXT, textConfig);
        }

        return payload;
    }



    private HttpRequest buildHttpRequest(Map<String, Object> payload, boolean stream) throws Exception {
        String requestBody = OBJECT_MAPPER.writeValueAsString(payload);
        HttpRequest.Builder requestBuilder = HttpRequest.builder().url(this.baseUrl + "/responses").method(HttpMethod.POST).addHeader("Content-Type", new String[]{"application/json"}).addHeader("Accept", new String[]{stream ? "text/event-stream" : "application/json"});
        if (this.apiKey != null && !this.apiKey.trim().isEmpty()) {
            requestBuilder.addHeader("Authorization", new String[]{"Bearer " + this.apiKey});
        }
        if (this.organizationId != null) {
            requestBuilder.addHeader(OPENAI_ORGANIZATION_HEADER, new String[]{this.organizationId});
        }
        return requestBuilder.body(requestBody).build();
    }

    private static String extractText(JsonNode output) {
        if (!output.isArray()) {
            return null;
        }
        StringBuilder textBuilder = new StringBuilder();
        for (JsonNode item : output) {
            JsonNode content;
            if (!"message".equals(item.path(FIELD_TYPE).asText()) || !(content = item.path(FIELD_CONTENT)).isArray()) continue;
            for (JsonNode c : content) {
                if (!TYPE_OUTPUT_TEXT.equals(c.path(FIELD_TYPE).asText())) continue;
                textBuilder.append(c.path(FIELD_TEXT).asText());
            }
        }
        return textBuilder.length() == 0 ? null : textBuilder.toString();
    }

    private static String extractReasoningSummary(JsonNode output) {
        if (!output.isArray()) {
            return null;
        }
        StringBuilder summaryBuilder = new StringBuilder();
        for (JsonNode item : output) {
            JsonNode summaryArray;
            if (!"reasoning".equals(item.path(FIELD_TYPE).asText()) || !(summaryArray = item.path(FIELD_SUMMARY)).isArray()) continue;
            for (JsonNode summaryItem : summaryArray) {
                if (!FIELD_SUMMARY_TEXT.equals(summaryItem.path(FIELD_TYPE).asText())) continue;
                summaryBuilder.append(summaryItem.path(FIELD_TEXT).asText());
            }
        }
        return summaryBuilder.length() == 0 ? null : summaryBuilder.toString();
    }

    private static String extractReasoningEncryptedContent(JsonNode output) {
        if (!output.isArray()) {
            return null;
        }
        for (JsonNode item : output) {
            JsonNode encryptedContent;
            if (!"reasoning".equals(item.path(FIELD_TYPE).asText()) || (encryptedContent = item.path(FIELD_ENCRYPTED_CONTENT)).isMissingNode() || encryptedContent.isNull()) continue;
            return encryptedContent.asText();
        }
        return null;
    }

    private static List<ToolExecutionRequest> extractToolExecutionRequests(JsonNode output) {
        if (!output.isArray()) {
            return Collections.emptyList();
        }
        ArrayList<ToolExecutionRequest> toolExecutionRequests = new ArrayList<ToolExecutionRequest>();
        for (JsonNode item : output) {
            if (!TYPE_FUNCTION_CALL.equals(item.path(FIELD_TYPE).asText())) continue;
            String id = item.path(FIELD_CALL_ID).asText(null);
            if (id == null || id.trim().isEmpty()) {
                id = item.path(FIELD_ID).asText(null);
            }
            ToolExecutionRequest toolExecutionRequest = ToolExecutionRequest.builder().id(id).name(item.path(FIELD_NAME).asText()).arguments(item.path(FIELD_ARGUMENTS).asText("{}")).build();
            toolExecutionRequests.add(toolExecutionRequest);
        }
        return toolExecutionRequests;
    }

    private static OpenAiTokenUsage parseTokenUsage(JsonNode usageNode) {
        JsonNode outputDetailsNode;
        if (usageNode == null || usageNode.isMissingNode() || usageNode.isNull()) {
            return null;
        }
        OpenAiTokenUsage.Builder usageBuilder = OpenAiTokenUsage.builder().inputTokenCount(usageNode.path(FIELD_INPUT_TOKENS).asInt()).outputTokenCount(usageNode.path(FIELD_OUTPUT_TOKENS).asInt()).totalTokenCount(usageNode.path(FIELD_TOTAL_TOKENS).asInt());
        JsonNode inputDetailsNode = usageNode.path(FIELD_INPUT_TOKENS_DETAILS);
        if (!inputDetailsNode.isMissingNode()) {
            usageBuilder.inputTokensDetails(OpenAiTokenUsage.InputTokensDetails.builder().cachedTokens(inputDetailsNode.path(FIELD_CACHED_TOKENS).asInt()).build());
        }
        if (!(outputDetailsNode = usageNode.path(FIELD_OUTPUT_TOKENS_DETAILS)).isMissingNode()) {
            usageBuilder.outputTokensDetails(OpenAiTokenUsage.OutputTokensDetails.builder().reasoningTokens(outputDetailsNode.path(FIELD_REASONING_TOKENS).asInt()).build());
        }
        return usageBuilder.build();
    }

    private static FinishReason finishReasonFromStatus(String status, boolean hasToolCalls) {
        if (status == null || status.trim().isEmpty()) {
            return null;
        }
        switch (status) {
            case "completed": {
                return hasToolCalls ? FinishReason.TOOL_EXECUTION : FinishReason.STOP;
            }
            case "incomplete": {
                return FinishReason.LENGTH;
            }
        }
        return FinishReason.OTHER;
    }

    private ChatResponse parseChatResponse(SuccessfulHttpResponse rawHttpResponse) throws Exception {
        FinishReason finishReason;
        JsonNode responseNode = OBJECT_MAPPER.readTree(rawHttpResponse.body());
        JsonNode outputNode = responseNode.path(FIELD_OUTPUT);
        String text = OpenAiResponsesClient.extractText(outputNode);
        String thinking = OpenAiResponsesClient.extractReasoningSummary(outputNode);
        String encryptedContent = OpenAiResponsesClient.extractReasoningEncryptedContent(outputNode);
        List<ToolExecutionRequest> toolExecutionRequests = OpenAiResponsesClient.extractToolExecutionRequests(outputNode);
        AiMessage.Builder aiMessageBuilder = AiMessage.builder().text(text).thinking(thinking).toolExecutionRequests(toolExecutionRequests);
        if (encryptedContent != null) {
            aiMessageBuilder.attributes(Collections.singletonMap(ENCRYPTED_REASONING_KEY, encryptedContent));
        }
        AiMessage aiMessage = aiMessageBuilder.build();
        OpenAiResponsesChatResponseMetadata.Builder metadataBuilder = (OpenAiResponsesChatResponseMetadata.Builder)((OpenAiResponsesChatResponseMetadata.Builder)OpenAiResponsesChatResponseMetadata.builder().id(responseNode.path(FIELD_ID).asText(null))).modelName(responseNode.path(FIELD_MODEL).asText(null));
        OpenAiTokenUsage tokenUsage = OpenAiResponsesClient.parseTokenUsage(responseNode.path(FIELD_USAGE));
        if (tokenUsage != null) {
            metadataBuilder.tokenUsage(tokenUsage);
        }
        if ((finishReason = OpenAiResponsesClient.finishReasonFromStatus(responseNode.path(FIELD_STATUS).asText(null), !toolExecutionRequests.isEmpty())) != null) {
            metadataBuilder.finishReason(finishReason);
        }
        if (responseNode.hasNonNull(FIELD_CREATED_AT)) {
            metadataBuilder.createdAt(responseNode.path(FIELD_CREATED_AT).asLong());
        }
        if (responseNode.hasNonNull(FIELD_COMPLETED_AT)) {
            metadataBuilder.completedAt(responseNode.path(FIELD_COMPLETED_AT).asLong());
        }
        if (responseNode.hasNonNull(FIELD_SERVICE_TIER)) {
            metadataBuilder.serviceTier(responseNode.path(FIELD_SERVICE_TIER).asText());
        }
        metadataBuilder.rawHttpResponse(rawHttpResponse);
        return ChatResponse.builder().aiMessage(aiMessage).metadata((ChatResponseMetadata)metadataBuilder.build()).build();
    }

    private static List<Map<String, Object>> toResponsesMessages(ChatMessage msg) {
        if (msg instanceof SystemMessage) {
            SystemMessage systemMessage = (SystemMessage)msg;
            return Collections.singletonList(OpenAiResponsesClient.createMessageEntry(ROLE_SYSTEM, Collections.singletonList(OpenAiResponsesClient.createInputTextContent(systemMessage.text()))));
        }
        if (msg instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)msg;
            ArrayList<Map<String, Object>> contentEntries = new ArrayList<Map<String, Object>>();
            for (Content content : userMessage.contents()) {
                if (content instanceof TextContent) {
                    TextContent textContent = (TextContent)content;
                    contentEntries.add(OpenAiResponsesClient.createInputTextContent(textContent.text()));
                    continue;
                }
                if (content instanceof ImageContent) {
                    ImageContent imageContent = (ImageContent)content;
                    contentEntries.add(OpenAiResponsesClient.createInputImageContent(imageContent.image(), imageContent.detailLevel()));
                    continue;
                }
                if (content instanceof PdfFileContent) {
                    PdfFileContent pdfFileContent = (PdfFileContent)content;
                    contentEntries.add(OpenAiResponsesClient.createInputPdfContent(pdfFileContent));
                    continue;
                }
                throw new UnsupportedFeatureException("Unsupported content type: " + content.getClass().getName() + ". Only TextContent, ImageContent, and PdfFileContent are supported.");
            }
            return Collections.singletonList(OpenAiResponsesClient.createMessageEntry(ROLE_USER, contentEntries));
        }
        if (msg instanceof AiMessage) {
            String text;
            AiMessage aiMessage = (AiMessage)msg;
            ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
            String encryptedContent = (String)aiMessage.attribute(ENCRYPTED_REASONING_KEY, String.class);
            if (encryptedContent != null) {
                LinkedHashMap<String, Object> reasoningItem = new LinkedHashMap<String, Object>();
                reasoningItem.put(FIELD_TYPE, "reasoning");
                reasoningItem.put(FIELD_ENCRYPTED_CONTENT, encryptedContent);
                ArrayList summaryItems = new ArrayList();
                if (aiMessage.thinking() != null && !aiMessage.thinking().isEmpty()) {
                    LinkedHashMap<String, String> summaryTextItem = new LinkedHashMap<String, String>();
                    summaryTextItem.put(FIELD_TYPE, FIELD_SUMMARY_TEXT);
                    summaryTextItem.put(FIELD_TEXT, aiMessage.thinking());
                    summaryItems.add(summaryTextItem);
                }
                reasoningItem.put(FIELD_SUMMARY, summaryItems);
                items.add(reasoningItem);
            }
            if ((text = aiMessage.text()) != null && !text.isEmpty()) {
                items.add(OpenAiResponsesClient.createMessageEntry(ROLE_ASSISTANT, Collections.singletonList(OpenAiResponsesClient.createOutputTextContent(text))));
            }

            if (aiMessage.hasToolExecutionRequests()) {
                for (ToolExecutionRequest toolRequest : aiMessage.toolExecutionRequests()) {
                    String callId = requireNonBlank(toolRequest.id(), "ToolExecutionRequest.id");
                    String name = requireNonBlank(toolRequest.name(), "ToolExecutionRequest.name");
                    String arguments = requireNonBlank(toolRequest.arguments(), "ToolExecutionRequest.arguments");
                    LinkedHashMap<String, Object> functionCall = new LinkedHashMap<String, Object>();
                    functionCall.put(FIELD_TYPE, TYPE_FUNCTION_CALL);
                    functionCall.put(FIELD_CALL_ID, callId);
                    functionCall.put(FIELD_NAME, name);
                    functionCall.put(FIELD_ARGUMENTS, arguments);
                    items.add(functionCall);
                }
            }

            return items;
        }
        if (msg instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage toolExecutionResultMessage = (ToolExecutionResultMessage)msg;
            LinkedHashMap<String, Object> outputEntry = new LinkedHashMap<String, Object>();
            outputEntry.put(FIELD_TYPE, TYPE_FUNCTION_CALL_OUTPUT);
            outputEntry.put(FIELD_CALL_ID, toolExecutionResultMessage.id());
            if (toolExecutionResultMessage.hasSingleText()) {
                outputEntry.put(FIELD_OUTPUT, toolExecutionResultMessage.text());
            } else {
                ArrayList<Map<String, Object>> outputContents = new ArrayList<Map<String, Object>>();
                for (Content content : toolExecutionResultMessage.contents()) {
                    if (content instanceof TextContent) {
                        TextContent textContent = (TextContent)content;
                        outputContents.add(OpenAiResponsesClient.createInputTextContent(textContent.text()));
                        continue;
                    }
                    if (content instanceof ImageContent) {
                        ImageContent imageContent = (ImageContent)content;
                        outputContents.add(OpenAiResponsesClient.createInputImageContent(imageContent.image(), imageContent.detailLevel()));
                        continue;
                    }
                    throw new UnsupportedFeatureException("Unsupported content type in tool result: " + content.getClass().getName() + ". Only TextContent and ImageContent are supported.");
                }
                outputEntry.put(FIELD_OUTPUT, outputContents);
            }
            return Collections.singletonList(outputEntry);
        }
        throw new UnsupportedFeatureException("Unsupported message type: " + msg.getClass().getName() + ". Only SystemMessage, UserMessage, AiMessage, and ToolExecutionResultMessage are supported.");
    }

    private static Map<String, Object> createMessageEntry(String role, List<Map<String, Object>> contentEntries) {
        LinkedHashMap<String, Object> entry = new LinkedHashMap<String, Object>();
        entry.put(FIELD_TYPE, "message");
        entry.put(FIELD_ROLE, role);
        entry.put(FIELD_CONTENT, contentEntries);
        return entry;
    }

    private static Map<String, Object> createInputTextContent(String text) {
        LinkedHashMap<String, Object> content = new LinkedHashMap<String, Object>();
        content.put(FIELD_TYPE, TYPE_INPUT_TEXT);
        content.put(FIELD_TEXT, text);
        return content;
    }

    private static Map<String, Object> createOutputTextContent(String text) {
        LinkedHashMap<String, Object> content = new LinkedHashMap<String, Object>();
        content.put(FIELD_TYPE, TYPE_OUTPUT_TEXT);
        content.put(FIELD_TEXT, text);
        return content;
    }

    private static Map<String, Object> createInputImageContent(Image image, ImageContent.DetailLevel detailLevel) {
        LinkedHashMap<String, Object> content = new LinkedHashMap<String, Object>();
        content.put(FIELD_TYPE, TYPE_INPUT_IMAGE);
        content.put(FIELD_IMAGE_URL, OpenAiResponsesClient.buildImageUrl(image));
        content.put(FIELD_DETAIL, OpenAiResponsesClient.toDetailString(detailLevel));
        return content;
    }

    private static Map<String, Object> createInputPdfContent(PdfFileContent pdfFileContent) {
        LinkedHashMap<String, Object> content = new LinkedHashMap<String, Object>();
        content.put(FIELD_TYPE, TYPE_INPUT_FILE);
        if (pdfFileContent.pdfFile().url() != null) {
            content.put(FIELD_FILE_URL, pdfFileContent.pdfFile().url().toString());
        } else if (pdfFileContent.pdfFile().base64Data() != null) {
            content.put(FIELD_FILE_DATA, OpenAiResponsesClient.buildPdfFileData(pdfFileContent));
            content.put(FIELD_FILENAME, DEFAULT_PDF_FILENAME);
        } else {
            throw new IllegalArgumentException("PDF must have either url or base64Data");
        }
        return content;
    }

    private static String toDetailString(ImageContent.DetailLevel detailLevel) {
        switch (detailLevel) {
            case LOW: {
                return "low";
            }
            case HIGH: {
                return "high";
            }
            case AUTO: {
                return "auto";
            }
        }
        throw new UnsupportedFeatureException("DetailLevel " + detailLevel + " is not supported by OpenAI Responses API. Supported values: LOW, HIGH, AUTO");
    }

    private static String buildImageUrl(Image image) {
        if (image.url() != null) {
            return image.url().toString();
        }
        if (image.base64Data() != null) {
            String mimeType = image.mimeType() != null ? image.mimeType() : DEFAULT_IMAGE_MIME_TYPE;
            return "data:" + mimeType + ";base64," + image.base64Data();
        }
        throw new IllegalArgumentException("Image must have either url or base64Data");
    }

    private static String buildPdfFileData(PdfFileContent pdfFileContent) {
        if (pdfFileContent.pdfFile().base64Data() != null) {
            return "data:" + pdfFileContent.pdfFile().mimeType() + ";base64," + pdfFileContent.pdfFile().base64Data();
        }
        throw new IllegalArgumentException("PDF must have base64Data");
    }

    private static String toToolChoiceString(ToolChoice toolChoice) {
        if (toolChoice == null) {
            return null;
        }
        switch (toolChoice) {
            case AUTO: {
                return "auto";
            }
            case REQUIRED: {
                return "required";
            }
            case NONE: {
                return "none";
            }
        }
        return null;
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must be provided");
        }
        return value;
    }

    private static Map<String, Object> toResponseTextConfig(ResponseFormat responseFormat, boolean strict) {
        if (responseFormat == null || responseFormat.type() == ResponseFormatType.TEXT) {
            return null;
        }
        LinkedHashMap<String, Object> textConfig = new LinkedHashMap<String, Object>();
        JsonSchema jsonSchema = responseFormat.jsonSchema();
        if (jsonSchema == null) {
            LinkedHashMap<String, String> format = new LinkedHashMap<String, String>();
            format.put(FIELD_TYPE, TYPE_JSON_OBJECT);
            textConfig.put(FIELD_FORMAT, format);
        } else {
            if (!(jsonSchema.rootElement() instanceof JsonObjectSchema) && !(jsonSchema.rootElement() instanceof JsonRawSchema)) {
                throw new IllegalArgumentException("For OpenAI, the root element of the JSON Schema must be either a JsonObjectSchema or a JsonRawSchema, but it was: " + jsonSchema.rootElement().getClass());
            }
            LinkedHashMap<String, Object> format = new LinkedHashMap<String, Object>();
            format.put(FIELD_TYPE, TYPE_JSON_SCHEMA);
            format.put(FIELD_STRICT, strict);
            if (jsonSchema.name() != null) {
                format.put(FIELD_NAME, jsonSchema.name());
            }
            format.put(FIELD_SCHEMA, JsonSchemaElementUtils.toMap((JsonSchemaElement)jsonSchema.rootElement(), (boolean)strict));
            textConfig.put(FIELD_FORMAT, format);
        }
        return textConfig;
    }

    private static class ResponsesApiEventListener
    implements ServerSentEventListener {
        private final MappingTrackingStreamingChatResponseHandler handler;
        private volatile StreamingHandle streamingHandle;
        private final Map<String, ToolExecutionRequest.Builder> toolCallBuilders = new LinkedHashMap<String, ToolExecutionRequest.Builder>();
        private final Map<String, Integer> toolCallIndices = new LinkedHashMap<String, Integer>();
        private final List<ToolExecutionRequest> completedToolCalls = new ArrayList<ToolExecutionRequest>();
        private final Set<String> completedToolCallItemIds = new HashSet<String>();
        private final List<ServerSentEvent> rawServerSentEvents = new ArrayList<ServerSentEvent>();
        private SuccessfulHttpResponse rawHttpResponse;

        ResponsesApiEventListener(StreamingChatResponseHandler handler) {
            this.handler = new MappingTrackingStreamingChatResponseHandler(handler);
        }

        private boolean isCancelled() {
            return this.streamingHandle != null && this.streamingHandle.isCancelled();
        }

        private void assignIndexIfAbsent(String itemId, int index) {
            this.toolCallIndices.putIfAbsent(itemId, index);
        }

        public void onOpen(SuccessfulHttpResponse response) {
            this.rawHttpResponse = response;
        }

        public void onEvent(ServerSentEvent event) {
            this.onEvent(event, new ServerSentEventContext((ServerSentEventParsingHandle)new CancellationUnsupportedHandle()));
        }

        public void onEvent(ServerSentEvent event, ServerSentEventContext context) {
            if (this.streamingHandle == null) {
                this.streamingHandle = ServerSentEventParsingHandleUtils.toStreamingHandle((ServerSentEventParsingHandle)context.parsingHandle());
            }
            if (this.isCancelled()) {
                return;
            }
            this.rawServerSentEvents.add(event);
            String data = event.data();
            if (data == null || data.isEmpty()) {
                return;
            }
            if (OpenAiResponsesClient.STREAM_DONE_MARKER.equals(data)) {
                return;
            }
            this.handler.resetMappingTracking();
            this.handleDelta(data);
            if (!this.handler.wasMapped()) {
                InternalStreamingChatResponseHandlerUtils.onUnmappedRawEvent((StreamingChatResponseHandler)this.handler, (Object)event);
            }
        }

        public void onError(Throwable error) {
            InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)ExceptionMapper.DEFAULT.mapException(error)));
        }

        private void handleDelta(String data) {
            if (!data.trim().startsWith("{") && !data.trim().startsWith("[")) {
                return;
            }
            try {
                String type;
                JsonNode node = OBJECT_MAPPER.readTree(data);
                String string = type = node.has(OpenAiResponsesClient.FIELD_TYPE) ? node.get(OpenAiResponsesClient.FIELD_TYPE).asText() : "";
                if (OpenAiResponsesClient.EVENT_OUTPUT_TEXT_DELTA.equals(type)) {
                    String text = node.path(OpenAiResponsesClient.FIELD_DELTA).asText();
                    if (!text.isEmpty()) {
                        InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)this.handler, (String)text, (StreamingHandle)this.streamingHandle);
                    }
                } else if (OpenAiResponsesClient.EVENT_REASONING_TEXT_DELTA.equals(type) || OpenAiResponsesClient.EVENT_REASONING_SUMMARY_TEXT_DELTA.equals(type)) {
                    String thinking = node.path(OpenAiResponsesClient.FIELD_DELTA).asText();
                    if (!thinking.isEmpty()) {
                        InternalStreamingChatResponseHandlerUtils.onPartialThinking((StreamingChatResponseHandler)this.handler, (String)thinking, (StreamingHandle)this.streamingHandle);
                    }
                } else if (OpenAiResponsesClient.EVENT_OUTPUT_ITEM_ADDED.equals(type)) {
                    JsonNode item = node.path(OpenAiResponsesClient.FIELD_ITEM);
                    if (OpenAiResponsesClient.TYPE_FUNCTION_CALL.equals(item.path(OpenAiResponsesClient.FIELD_TYPE).asText())) {
                        String itemId = item.path(OpenAiResponsesClient.FIELD_ID).asText();
                        int outputIndex = node.path(OpenAiResponsesClient.FIELD_OUTPUT_INDEX).asInt(0);
                        this.toolCallBuilders.put(itemId, ToolExecutionRequest.builder().id(item.path(OpenAiResponsesClient.FIELD_CALL_ID).asText()).name(item.path(OpenAiResponsesClient.FIELD_NAME).asText()).arguments(""));
                        this.assignIndexIfAbsent(itemId, outputIndex);
                    }
                } else if (OpenAiResponsesClient.EVENT_FUNCTION_CALL_ARGUMENTS_DELTA.equals(type)) {
                    String itemId = node.path(OpenAiResponsesClient.FIELD_ITEM_ID).asText();
                    ToolExecutionRequest.Builder builder = this.toolCallBuilders.get(itemId);
                    if (builder != null) {
                        String currentArgs = builder.build().arguments();
                        String delta = node.path(OpenAiResponsesClient.FIELD_DELTA).asText();
                        builder.arguments(currentArgs + delta);
                        Integer index = this.toolCallIndices.get(itemId);
                        if (index != null && !delta.isEmpty()) {
                            PartialToolCall partialToolCall = PartialToolCall.builder().index(index.intValue()).id(builder.build().id()).name(builder.build().name()).partialArguments(delta).build();
                            InternalStreamingChatResponseHandlerUtils.onPartialToolCall((StreamingChatResponseHandler)this.handler, (PartialToolCall)partialToolCall, (StreamingHandle)this.streamingHandle);
                        }
                    }
                } else if (OpenAiResponsesClient.EVENT_FUNCTION_CALL_ARGUMENTS_DONE.equals(type)) {
                    String itemId = node.path(OpenAiResponsesClient.FIELD_ITEM_ID).asText();
                    ToolExecutionRequest.Builder builder = this.toolCallBuilders.get(itemId);
                    if (builder != null) {
                        builder.arguments(node.path(OpenAiResponsesClient.FIELD_ARGUMENTS).asText());
                        this.completeToolCall(itemId, builder);
                    }
                } else if (OpenAiResponsesClient.EVENT_OUTPUT_ITEM_DONE.equals(type)) {
                    this.handleOutputItemDone(node);
                } else if (OpenAiResponsesClient.EVENT_RESPONSE_COMPLETED.equals(type) || OpenAiResponsesClient.EVENT_RESPONSE_INCOMPLETE.equals(type)) {
                    this.handleResponseCompleted(node);
                } else if (OpenAiResponsesClient.EVENT_RESPONSE_FAILED.equals(type) || OpenAiResponsesClient.EVENT_RESPONSE_ERROR.equals(type)) {
                    this.handleResponseFailure(node);
                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void handleOutputItemDone(JsonNode node) {
            JsonNode item = node.path(OpenAiResponsesClient.FIELD_ITEM);
            if (OpenAiResponsesClient.TYPE_FUNCTION_CALL.equals(item.path(OpenAiResponsesClient.FIELD_TYPE).asText())) {
                JsonNode argumentsNode;
                JsonNode nameNode;
                String itemId = item.path(OpenAiResponsesClient.FIELD_ID).asText();
                int outputIndex = node.path(OpenAiResponsesClient.FIELD_OUTPUT_INDEX).asInt(0);
                ToolExecutionRequest.Builder builder = this.toolCallBuilders.computeIfAbsent(itemId, ignored -> ToolExecutionRequest.builder());
                this.assignIndexIfAbsent(itemId, outputIndex);
                JsonNode callIdNode = item.get(OpenAiResponsesClient.FIELD_CALL_ID);
                if (callIdNode != null && !callIdNode.isNull()) {
                    builder.id(callIdNode.asText());
                }
                if ((nameNode = item.get(OpenAiResponsesClient.FIELD_NAME)) != null && !nameNode.isNull()) {
                    builder.name(nameNode.asText());
                }
                if ((argumentsNode = item.get(OpenAiResponsesClient.FIELD_ARGUMENTS)) != null && !argumentsNode.isNull()) {
                    builder.arguments(argumentsNode.asText());
                }
                this.completeToolCall(itemId, builder);
            }
        }

        private void handleResponseCompleted(JsonNode node) {
            FinishReason finishReason;
            JsonNode responseNode = node.path(OpenAiResponsesClient.FIELD_RESPONSE);
            JsonNode outputNode = responseNode.path(OpenAiResponsesClient.FIELD_OUTPUT);
            String text = OpenAiResponsesClient.extractText(outputNode);
            String thinking = OpenAiResponsesClient.extractReasoningSummary(outputNode);
            String encryptedContent = OpenAiResponsesClient.extractReasoningEncryptedContent(outputNode);
            AiMessage.Builder aiMessageBuilder = AiMessage.builder().text(text).thinking(thinking).toolExecutionRequests(this.completedToolCalls);
            if (encryptedContent != null) {
                aiMessageBuilder.attributes(Collections.singletonMap(OpenAiResponsesClient.ENCRYPTED_REASONING_KEY, encryptedContent));
            }
            AiMessage aiMessage = aiMessageBuilder.build();
            OpenAiResponsesChatResponseMetadata.Builder metadataBuilder = (OpenAiResponsesChatResponseMetadata.Builder)((OpenAiResponsesChatResponseMetadata.Builder)OpenAiResponsesChatResponseMetadata.builder().id(responseNode.path(OpenAiResponsesClient.FIELD_ID).asText(null))).modelName(responseNode.path(OpenAiResponsesClient.FIELD_MODEL).asText(null));
            OpenAiTokenUsage tokenUsage = OpenAiResponsesClient.parseTokenUsage(responseNode.path(OpenAiResponsesClient.FIELD_USAGE));
            if (tokenUsage != null) {
                metadataBuilder.tokenUsage(tokenUsage);
            }
            if ((finishReason = OpenAiResponsesClient.finishReasonFromStatus(responseNode.path(OpenAiResponsesClient.FIELD_STATUS).asText(null), !this.completedToolCalls.isEmpty())) != null) {
                metadataBuilder.finishReason(finishReason);
            }
            if (responseNode.hasNonNull(OpenAiResponsesClient.FIELD_CREATED_AT)) {
                metadataBuilder.createdAt(responseNode.path(OpenAiResponsesClient.FIELD_CREATED_AT).asLong());
            }
            if (responseNode.hasNonNull(OpenAiResponsesClient.FIELD_COMPLETED_AT)) {
                metadataBuilder.completedAt(responseNode.path(OpenAiResponsesClient.FIELD_COMPLETED_AT).asLong());
            }
            if (responseNode.hasNonNull(OpenAiResponsesClient.FIELD_SERVICE_TIER)) {
                metadataBuilder.serviceTier(responseNode.path(OpenAiResponsesClient.FIELD_SERVICE_TIER).asText());
            }
            if (this.rawHttpResponse != null) {
                metadataBuilder.rawHttpResponse(this.rawHttpResponse);
            }
            if (!this.rawServerSentEvents.isEmpty()) {
                metadataBuilder.rawServerSentEvents(new ArrayList<ServerSentEvent>(this.rawServerSentEvents));
            }
            ChatResponse.Builder responseBuilder = ChatResponse.builder().aiMessage(aiMessage).metadata((ChatResponseMetadata)metadataBuilder.build());
            if (!this.isCancelled()) {
                try {
                    this.handler.onCompleteResponse(responseBuilder.build());
                }
                catch (Exception e) {
                    InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)e));
                }
            }
        }

        private void handleResponseFailure(JsonNode node) {
            JsonNode errorNode = node.path(OpenAiResponsesClient.FIELD_ERROR);
            if (errorNode.isMissingNode()) {
                errorNode = node.path(OpenAiResponsesClient.FIELD_RESPONSE).path(OpenAiResponsesClient.FIELD_ERROR);
            }
            String message = this.extractErrorMessage(errorNode);
            InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)new RuntimeException(message)));
        }

        private String extractErrorMessage(JsonNode errorNode) {
            if (errorNode == null || errorNode.isMissingNode() || errorNode.isNull()) {
                return "Response failed";
            }
            String message = errorNode.path("message").asText(null);
            if (message == null || message.trim().isEmpty()) {
                message = errorNode.toString();
            }
            return "Response failed: " + message;
        }

        private void completeToolCall(String itemId, ToolExecutionRequest.Builder builder) {
            int safeIndex;
            if (builder == null || this.completedToolCallItemIds.contains(itemId)) {
                return;
            }
            ToolExecutionRequest toolExecutionRequest = builder.build();
            this.completedToolCalls.add(toolExecutionRequest);
            this.completedToolCallItemIds.add(itemId);
            this.toolCallBuilders.remove(itemId);
            Integer index = this.toolCallIndices.remove(itemId);
            int n = safeIndex = index != null ? index : this.completedToolCalls.size() - 1;
            if (!this.isCancelled()) {
                InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)this.handler, (CompleteToolCall)new CompleteToolCall(safeIndex, toolExecutionRequest));
            }
        }
    }

    static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String organizationId;
        private boolean logRequests;
        private boolean logResponses;

        Builder() {
        }

        Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        Builder logRequests(Boolean logRequests) {
            if (logRequests != null) {
                this.logRequests = logRequests;
            }
            return this;
        }

        Builder logResponses(Boolean logResponses) {
            if (logResponses != null) {
                this.logResponses = logResponses;
            }
            return this;
        }

        OpenAiResponsesClient build() {
            return new OpenAiResponsesClient(this);
        }
    }
}

