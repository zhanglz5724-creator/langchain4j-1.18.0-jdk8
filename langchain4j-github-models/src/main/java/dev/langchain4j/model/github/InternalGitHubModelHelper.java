/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.inference.ChatCompletionsClientBuilder
 *  com.azure.ai.inference.EmbeddingsClientBuilder
 *  com.azure.ai.inference.ModelServiceVersion
 *  com.azure.ai.inference.models.ChatCompletionsFunctionToolCall
 *  com.azure.ai.inference.models.ChatCompletionsFunctionToolDefinition
 *  com.azure.ai.inference.models.ChatCompletionsOptions
 *  com.azure.ai.inference.models.ChatCompletionsResponseFormat
 *  com.azure.ai.inference.models.ChatCompletionsResponseFormatJsonObject
 *  com.azure.ai.inference.models.ChatCompletionsResponseFormatJsonSchema
 *  com.azure.ai.inference.models.ChatCompletionsResponseFormatJsonSchemaDefinition
 *  com.azure.ai.inference.models.ChatCompletionsToolCall
 *  com.azure.ai.inference.models.ChatCompletionsToolDefinition
 *  com.azure.ai.inference.models.ChatMessageImageContentItem
 *  com.azure.ai.inference.models.ChatMessageImageDetailLevel
 *  com.azure.ai.inference.models.ChatMessageImageUrl
 *  com.azure.ai.inference.models.ChatMessageTextContentItem
 *  com.azure.ai.inference.models.ChatRequestAssistantMessage
 *  com.azure.ai.inference.models.ChatRequestMessage
 *  com.azure.ai.inference.models.ChatRequestSystemMessage
 *  com.azure.ai.inference.models.ChatRequestToolMessage
 *  com.azure.ai.inference.models.ChatRequestUserMessage
 *  com.azure.ai.inference.models.ChatResponseMessage
 *  com.azure.ai.inference.models.CompletionsFinishReason
 *  com.azure.ai.inference.models.CompletionsUsage
 *  com.azure.ai.inference.models.FunctionCall
 *  com.azure.ai.inference.models.FunctionDefinition
 *  com.azure.core.credential.AzureKeyCredential
 *  com.azure.core.credential.KeyCredential
 *  com.azure.core.exception.HttpResponseException
 *  com.azure.core.http.HttpClient
 *  com.azure.core.http.ProxyOptions
 *  com.azure.core.http.netty.NettyAsyncHttpClientProvider
 *  com.azure.core.http.policy.ExponentialBackoffOptions
 *  com.azure.core.http.policy.HttpLogDetailLevel
 *  com.azure.core.http.policy.HttpLogOptions
 *  com.azure.core.http.policy.RetryOptions
 *  com.azure.core.util.BinaryData
 *  com.azure.core.util.ClientOptions
 *  com.azure.core.util.Header
 *  com.azure.core.util.HttpClientOptions
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.github;

import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.EmbeddingsClientBuilder;
import com.azure.ai.inference.ModelServiceVersion;
import com.azure.ai.inference.models.ChatCompletionsFunctionToolCall;
import com.azure.ai.inference.models.ChatCompletionsFunctionToolDefinition;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.ChatCompletionsResponseFormat;
import com.azure.ai.inference.models.ChatCompletionsResponseFormatJsonObject;
import com.azure.ai.inference.models.ChatCompletionsResponseFormatJsonSchema;
import com.azure.ai.inference.models.ChatCompletionsResponseFormatJsonSchemaDefinition;
import com.azure.ai.inference.models.ChatCompletionsToolCall;
import com.azure.ai.inference.models.ChatCompletionsToolDefinition;
import com.azure.ai.inference.models.ChatMessageImageContentItem;
import com.azure.ai.inference.models.ChatMessageImageDetailLevel;
import com.azure.ai.inference.models.ChatMessageImageUrl;
import com.azure.ai.inference.models.ChatMessageTextContentItem;
import com.azure.ai.inference.models.ChatRequestAssistantMessage;
import com.azure.ai.inference.models.ChatRequestMessage;
import com.azure.ai.inference.models.ChatRequestSystemMessage;
import com.azure.ai.inference.models.ChatRequestToolMessage;
import com.azure.ai.inference.models.ChatRequestUserMessage;
import com.azure.ai.inference.models.ChatResponseMessage;
import com.azure.ai.inference.models.CompletionsFinishReason;
import com.azure.ai.inference.models.CompletionsUsage;
import com.azure.ai.inference.models.FunctionCall;
import com.azure.ai.inference.models.FunctionDefinition;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.KeyCredential;
import com.azure.core.exception.HttpResponseException;
import com.azure.core.http.HttpClient;
import com.azure.core.http.ProxyOptions;
import com.azure.core.http.netty.NettyAsyncHttpClientProvider;
import com.azure.core.http.policy.ExponentialBackoffOptions;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.core.http.policy.RetryOptions;
import com.azure.core.util.BinaryData;
import com.azure.core.util.ClientOptions;
import com.azure.core.util.Header;
import com.azure.core.util.HttpClientOptions;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InternalGitHubModelHelper {
    private static final Logger logger = LoggerFactory.getLogger(InternalGitHubModelHelper.class);
    public static final String DEFAULT_GITHUB_MODELS_ENDPOINT = "https://models.inference.ai.azure.com";
    public static final String DEFAULT_USER_AGENT = "langchain4j-github-models";
    private static final Map<String, Object> NO_PARAMETER_DATA = new HashMap<String, Object>();

    InternalGitHubModelHelper() {
    }

    public static ChatCompletionsClientBuilder setupChatCompletionsBuilder(String endpoint, ModelServiceVersion serviceVersion, String gitHubToken, Duration timeout, Integer maxRetries, ProxyOptions proxyOptions, boolean logRequestsAndResponses, String userAgentSuffix, Map<String, String> customHeaders) {
        HttpClientOptions clientOptions = InternalGitHubModelHelper.getClientOptions(timeout, proxyOptions, userAgentSuffix, customHeaders);
        ChatCompletionsClientBuilder chatCompletionsClientBuilder = new ChatCompletionsClientBuilder().endpoint(InternalGitHubModelHelper.getEndpoint(endpoint)).serviceVersion(InternalGitHubModelHelper.getModelServiceVersion(serviceVersion)).httpClient(InternalGitHubModelHelper.getHttpClient(clientOptions)).clientOptions((ClientOptions)clientOptions).httpLogOptions(InternalGitHubModelHelper.getHttpLogOptions(logRequestsAndResponses)).retryOptions(InternalGitHubModelHelper.getRetryOptions(maxRetries)).credential(InternalGitHubModelHelper.getCredential(gitHubToken));
        return chatCompletionsClientBuilder;
    }

    public static EmbeddingsClientBuilder setupEmbeddingsBuilder(String endpoint, ModelServiceVersion serviceVersion, String gitHubToken, Duration timeout, Integer maxRetries, ProxyOptions proxyOptions, boolean logRequestsAndResponses, String userAgentSuffix, Map<String, String> customHeaders) {
        HttpClientOptions clientOptions = InternalGitHubModelHelper.getClientOptions(timeout, proxyOptions, userAgentSuffix, customHeaders);
        EmbeddingsClientBuilder embeddingsClientBuilder = new EmbeddingsClientBuilder().endpoint(InternalGitHubModelHelper.getEndpoint(endpoint)).serviceVersion(InternalGitHubModelHelper.getModelServiceVersion(serviceVersion)).httpClient(InternalGitHubModelHelper.getHttpClient(clientOptions)).clientOptions((ClientOptions)clientOptions).httpLogOptions(InternalGitHubModelHelper.getHttpLogOptions(logRequestsAndResponses)).retryOptions(InternalGitHubModelHelper.getRetryOptions(maxRetries)).credential(InternalGitHubModelHelper.getCredential(gitHubToken));
        return embeddingsClientBuilder;
    }

    private static String getEndpoint(String endpoint) {
        return Utils.isNullOrBlank((String)endpoint) ? DEFAULT_GITHUB_MODELS_ENDPOINT : endpoint;
    }

    public static ModelServiceVersion getModelServiceVersion(ModelServiceVersion serviceVersion) {
        return (ModelServiceVersion)Utils.getOrDefault((Object)serviceVersion, (Object)ModelServiceVersion.getLatest());
    }

    private static HttpClient getHttpClient(HttpClientOptions clientOptions) {
        return new NettyAsyncHttpClientProvider().createInstance(clientOptions);
    }

    private static HttpClientOptions getClientOptions(Duration timeout, ProxyOptions proxyOptions, String userAgentSuffix, Map<String, String> customHeaders) {
        timeout = (Duration)Utils.getOrDefault((Object)timeout, (Object)Duration.ofSeconds(60L));
        HttpClientOptions clientOptions = new HttpClientOptions();
        clientOptions.setConnectTimeout(timeout);
        clientOptions.setResponseTimeout(timeout);
        clientOptions.setReadTimeout(timeout);
        clientOptions.setWriteTimeout(timeout);
        clientOptions.setProxyOptions(proxyOptions);
        String userAgent = DEFAULT_USER_AGENT;
        if (userAgentSuffix != null && !userAgentSuffix.isEmpty()) {
            userAgent = "langchain4j-github-models-" + userAgentSuffix;
        }
        ArrayList<Header> headers = new ArrayList<Header>();
        headers.add(new Header("User-Agent", userAgent));
        if (customHeaders != null) {
            customHeaders.forEach((name, value) -> headers.add(new Header(name, value)));
        }
        clientOptions.setHeaders(headers);
        return clientOptions;
    }

    private static HttpLogOptions getHttpLogOptions(boolean logRequestsAndResponses) {
        HttpLogOptions httpLogOptions = new HttpLogOptions();
        if (logRequestsAndResponses) {
            httpLogOptions.setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS);
        }
        return httpLogOptions;
    }

    private static RetryOptions getRetryOptions(Integer maxRetries) {
        maxRetries = (Integer)Utils.getOrDefault((Object)maxRetries, (Object)2);
        ExponentialBackoffOptions exponentialBackoffOptions = new ExponentialBackoffOptions();
        exponentialBackoffOptions.setMaxRetries(maxRetries);
        return new RetryOptions(exponentialBackoffOptions);
    }

    private static KeyCredential getCredential(String gitHubToken) {
        ValidationUtils.ensureNotNull((Object)gitHubToken, (String)"%s", (Object[])new Object[]{"GitHub token is a mandatory parameter for connecting to GitHub models."});
        return new AzureKeyCredential(gitHubToken);
    }

    public static List<ChatRequestMessage> toAzureAiMessages(List<ChatMessage> messages) {
        return messages.stream().map(InternalGitHubModelHelper::toAzureAiMessage).collect(Collectors.toList());
    }

    public static ChatRequestMessage toAzureAiMessage(ChatMessage message) {
        if (message instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage)message;
            ChatRequestAssistantMessage chatRequestAssistantMessage = new ChatRequestAssistantMessage((String)Utils.getOrDefault((Object)aiMessage.text(), (Object)""));
            chatRequestAssistantMessage.setToolCalls(InternalGitHubModelHelper.toolExecutionRequestsFrom(message));
            return chatRequestAssistantMessage;
        }
        if (message instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage toolExecutionResultMessage = (ToolExecutionResultMessage)message;
            if (!toolExecutionResultMessage.hasSingleText()) {
                throw new UnsupportedFeatureException("GitHub Models does not support non-text content in tool results. Only text content is supported.");
            }
            ChatRequestToolMessage chatRequestToolMessage = new ChatRequestToolMessage(toolExecutionResultMessage.id());
            chatRequestToolMessage.setContent(toolExecutionResultMessage.text());
            return chatRequestToolMessage;
        }
        if (message instanceof SystemMessage) {
            SystemMessage systemMessage = (SystemMessage)message;
            return new ChatRequestSystemMessage(systemMessage.text());
        }
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)message;
            ChatRequestUserMessage chatRequestUserMessage = userMessage.hasSingleText() ? new ChatRequestUserMessage(userMessage.singleText()) : ChatRequestUserMessage.fromContentItems(userMessage.contents().stream().map(content -> {
                if (content instanceof TextContent) {
                    String text = ((TextContent)content).text();
                    return new ChatMessageTextContentItem(text);
                }
                if (content instanceof ImageContent) {
                    ImageContent imageContent = (ImageContent)content;
                    ValidationUtils.ensureNotNull((Object)imageContent.image().url(), (String)"%s", (Object[])new Object[]{"Image URL is not present. Base64 encoded images are not supported at the moment."});
                    ChatMessageImageUrl imageUrl = new ChatMessageImageUrl(imageContent.image().url().toString());
                    imageUrl.setDetail(ChatMessageImageDetailLevel.fromString((String)imageContent.detailLevel().name()));
                    return new ChatMessageImageContentItem(imageUrl);
                }
                throw new IllegalArgumentException("Unsupported content type: " + content.type());
            }).collect(Collectors.toList()));
            return chatRequestUserMessage;
        }
        throw new IllegalArgumentException("Unsupported message type: " + message.type());
    }

    private static List<ChatCompletionsToolCall> toolExecutionRequestsFrom(ChatMessage message) {
        AiMessage aiMessage;
        if (message instanceof AiMessage && (aiMessage = (AiMessage)message).hasToolExecutionRequests()) {
            return aiMessage.toolExecutionRequests().stream().map(toolExecutionRequest -> new ChatCompletionsFunctionToolCall(toolExecutionRequest.id(), new FunctionCall(toolExecutionRequest.name(), toolExecutionRequest.arguments()))).collect(Collectors.toList());
        }
        return null;
    }

    public static List<ChatCompletionsToolDefinition> toToolDefinitions(Collection<ToolSpecification> toolSpecifications) {
        return toolSpecifications.stream().map(InternalGitHubModelHelper::toToolDefinition).collect(Collectors.toList());
    }

    private static ChatCompletionsToolDefinition toToolDefinition(ToolSpecification toolSpecification) {
        FunctionDefinition functionDefinition = new FunctionDefinition(toolSpecification.name());
        functionDefinition.setDescription(toolSpecification.description());
        functionDefinition.setParameters(InternalGitHubModelHelper.getParameters(toolSpecification));
        return new ChatCompletionsFunctionToolDefinition(functionDefinition);
    }

    public static BinaryData toToolChoice(ToolSpecification toolThatMustBeExecuted) {
        FunctionCall functionCall = new FunctionCall(toolThatMustBeExecuted.name(), InternalGitHubModelHelper.getParameters(toolThatMustBeExecuted).toString());
        ChatCompletionsFunctionToolCall toolToCall = new ChatCompletionsFunctionToolCall(toolThatMustBeExecuted.name(), functionCall);
        return BinaryData.fromObject((Object)toolToCall);
    }

    private static BinaryData getParameters(ToolSpecification toolSpecification) {
        return InternalGitHubModelHelper.toAzureAiParameters(toolSpecification.parameters());
    }

    private static BinaryData toAzureAiParameters(JsonObjectSchema toolParameters) {
        Parameters parameters = new Parameters();
        if (toolParameters == null) {
            return BinaryData.fromObject(NO_PARAMETER_DATA);
        }
        parameters.setProperties(JsonSchemaElementUtils.toMap((Map)toolParameters.properties()));
        parameters.setRequired(toolParameters.required());
        return BinaryData.fromObject((Object)parameters);
    }

    public static AiMessage aiMessageFrom(ChatResponseMessage chatResponseMessage) {
        String text = chatResponseMessage.getContent();
        if (Utils.isNullOrEmpty((Collection)chatResponseMessage.getToolCalls())) {
            return AiMessage.aiMessage((String)text);
        }
        List toolExecutionRequests = chatResponseMessage.getToolCalls().stream().map(chatCompletionsFunctionToolCall -> ToolExecutionRequest.builder().id(chatCompletionsFunctionToolCall.getId()).name(chatCompletionsFunctionToolCall.getFunction().getName()).arguments(chatCompletionsFunctionToolCall.getFunction().getArguments()).build()).collect(Collectors.toList());
        return Utils.isNullOrBlank((String)text) ? AiMessage.aiMessage(toolExecutionRequests) : AiMessage.aiMessage((String)text, toolExecutionRequests);
    }

    public static TokenUsage tokenUsageFrom(CompletionsUsage azureAiUsage) {
        if (azureAiUsage == null) {
            return null;
        }
        return new TokenUsage(Integer.valueOf(azureAiUsage.getPromptTokens()), Integer.valueOf(azureAiUsage.getCompletionTokens()), Integer.valueOf(azureAiUsage.getTotalTokens()));
    }

    public static FinishReason finishReasonFrom(CompletionsFinishReason azureAiFinishReason) {
        if (azureAiFinishReason == null) {
            return null;
        }
        if (azureAiFinishReason == CompletionsFinishReason.STOPPED) {
            return FinishReason.STOP;
        }
        if (azureAiFinishReason == CompletionsFinishReason.TOKEN_LIMIT_REACHED) {
            return FinishReason.LENGTH;
        }
        if (azureAiFinishReason == CompletionsFinishReason.CONTENT_FILTERED) {
            return FinishReason.CONTENT_FILTER;
        }
        if (azureAiFinishReason == CompletionsFinishReason.TOOL_CALLS) {
            return FinishReason.TOOL_EXECUTION;
        }
        return null;
    }

    public static FinishReason contentFilterManagement(HttpResponseException httpResponseException, String contentFilterCode) {
        FinishReason exceptionFinishReason = FinishReason.OTHER;
        if (httpResponseException.getValue() instanceof Map) {
            try {
                String code;
                Map errorDetails;
                Object errorCode;
                Map error = (Map)httpResponseException.getValue();
                Object errorMap = error.get("error");
                if (errorMap instanceof Map && (errorCode = (errorDetails = (Map)errorMap).get("code")) instanceof String && contentFilterCode.equals(code = (String)errorCode)) {
                    exceptionFinishReason = FinishReason.CONTENT_FILTER;
                }
            }
            catch (ClassCastException classCastException) {
                logger.error("Error parsing error response from Azure OpenAI", (Throwable)classCastException);
            }
        }
        return exceptionFinishReason;
    }

    static ChatRequest createListenerRequest(ChatCompletionsOptions options, List<ChatMessage> messages, List<ToolSpecification> toolSpecifications) {
        return ChatRequest.builder().messages(messages).parameters(ChatRequestParameters.builder().modelName(options.getModel()).temperature(options.getTemperature()).topP(options.getTopP()).maxOutputTokens(options.getMaxTokens()).toolSpecifications(toolSpecifications).build()).build();
    }

    static ChatResponse createListenerResponse(String responseId, String responseModel, Response<AiMessage> response) {
        if (response == null) {
            return null;
        }
        return ChatResponse.builder().aiMessage((AiMessage)response.content()).metadata(ChatResponseMetadata.builder().id(responseId).modelName(responseModel).tokenUsage(response.tokenUsage()).finishReason(response.finishReason()).build()).build();
    }

    static ChatCompletionsResponseFormat toChatCompletionsResponseFormat(ResponseFormat responseFormat, Boolean strict) {
        if (responseFormat == null || responseFormat.type() == ResponseFormatType.TEXT) {
            return null;
        }
        JsonSchema jsonSchema = responseFormat.jsonSchema();
        if (jsonSchema == null) {
            return new ChatCompletionsResponseFormatJsonObject();
        }
        if (!(jsonSchema.rootElement() instanceof JsonObjectSchema)) {
            throw new IllegalArgumentException("For OpenAI, the root element of the JSON Schema must be a JsonObjectSchema, but it was: " + jsonSchema.rootElement().getClass());
        }
        return new ChatCompletionsResponseFormatJsonSchema(new ChatCompletionsResponseFormatJsonSchemaDefinition(jsonSchema.name(), InternalGitHubModelHelper.toJsonSchemaDefinition(jsonSchema.rootElement(), strict)).setStrict(strict));
    }

    static Map<String, BinaryData> toJsonSchemaDefinition(JsonSchemaElement jsonSchemaElement, boolean strict) {
        Map map = JsonSchemaElementUtils.toMap((JsonSchemaElement)jsonSchemaElement, (boolean)strict);
        HashMap<String, BinaryData> result = new HashMap<String, BinaryData>();
        map.forEach((key, value) -> result.put((String)key, BinaryData.fromObject((Object)value)));
        return result;
    }

    static {
        NO_PARAMETER_DATA.put("type", "object");
        NO_PARAMETER_DATA.put("properties", new HashMap());
    }

    private static class Parameters {
        private final String type = "object";
        private Map<String, Map<String, Object>> properties = new HashMap<String, Map<String, Object>>();
        private List<String> required = new ArrayList<String>();

        private Parameters() {
        }

        public String getType() {
            return this.type;
        }

        public Map<String, Map<String, Object>> getProperties() {
            return this.properties;
        }

        public void setProperties(Map<String, Map<String, Object>> properties) {
            this.properties = properties;
        }

        public List<String> getRequired() {
            return this.required;
        }

        public void setRequired(List<String> required) {
            this.required = required;
        }
    }
}

