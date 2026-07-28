/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.openai.OpenAIAsyncClient
 *  com.azure.ai.openai.OpenAIClient
 *  com.azure.ai.openai.OpenAIClientBuilder
 *  com.azure.ai.openai.OpenAIServiceVersion
 *  com.azure.ai.openai.models.ChatCompletionsFunctionToolCall
 *  com.azure.ai.openai.models.ChatCompletionsFunctionToolDefinition
 *  com.azure.ai.openai.models.ChatCompletionsFunctionToolDefinitionFunction
 *  com.azure.ai.openai.models.ChatCompletionsJsonResponseFormat
 *  com.azure.ai.openai.models.ChatCompletionsJsonSchemaResponseFormat
 *  com.azure.ai.openai.models.ChatCompletionsJsonSchemaResponseFormatJsonSchema
 *  com.azure.ai.openai.models.ChatCompletionsResponseFormat
 *  com.azure.ai.openai.models.ChatCompletionsTextResponseFormat
 *  com.azure.ai.openai.models.ChatCompletionsToolCall
 *  com.azure.ai.openai.models.ChatCompletionsToolDefinition
 *  com.azure.ai.openai.models.ChatCompletionsToolSelection
 *  com.azure.ai.openai.models.ChatCompletionsToolSelectionPreset
 *  com.azure.ai.openai.models.ChatMessageImageContentItem
 *  com.azure.ai.openai.models.ChatMessageImageUrl
 *  com.azure.ai.openai.models.ChatMessageTextContentItem
 *  com.azure.ai.openai.models.ChatRequestAssistantMessage
 *  com.azure.ai.openai.models.ChatRequestMessage
 *  com.azure.ai.openai.models.ChatRequestSystemMessage
 *  com.azure.ai.openai.models.ChatRequestToolMessage
 *  com.azure.ai.openai.models.ChatRequestUserMessage
 *  com.azure.ai.openai.models.ChatResponseMessage
 *  com.azure.ai.openai.models.CompletionsFinishReason
 *  com.azure.ai.openai.models.CompletionsUsage
 *  com.azure.ai.openai.models.FunctionCall
 *  com.azure.ai.openai.models.ImageGenerationData
 *  com.azure.core.credential.AzureKeyCredential
 *  com.azure.core.credential.KeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.core.http.HttpClient
 *  com.azure.core.http.HttpClientProvider
 *  com.azure.core.http.ProxyOptions
 *  com.azure.core.http.policy.ExponentialBackoffOptions
 *  com.azure.core.http.policy.HttpLogDetailLevel
 *  com.azure.core.http.policy.HttpLogOptions
 *  com.azure.core.http.policy.RetryOptions
 *  com.azure.core.util.BinaryData
 *  com.azure.core.util.ClientOptions
 *  com.azure.core.util.Header
 *  com.azure.core.util.HttpClientOptions
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.image.Image$Builder
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
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonRawSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.azure;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.OpenAIServiceVersion;
import com.azure.ai.openai.models.ChatCompletionsFunctionToolCall;
import com.azure.ai.openai.models.ChatCompletionsFunctionToolDefinition;
import com.azure.ai.openai.models.ChatCompletionsFunctionToolDefinitionFunction;
import com.azure.ai.openai.models.ChatCompletionsJsonResponseFormat;
import com.azure.ai.openai.models.ChatCompletionsJsonSchemaResponseFormat;
import com.azure.ai.openai.models.ChatCompletionsJsonSchemaResponseFormatJsonSchema;
import com.azure.ai.openai.models.ChatCompletionsResponseFormat;
import com.azure.ai.openai.models.ChatCompletionsTextResponseFormat;
import com.azure.ai.openai.models.ChatCompletionsToolCall;
import com.azure.ai.openai.models.ChatCompletionsToolDefinition;
import com.azure.ai.openai.models.ChatCompletionsToolSelection;
import com.azure.ai.openai.models.ChatCompletionsToolSelectionPreset;
import com.azure.ai.openai.models.ChatMessageImageContentItem;
import com.azure.ai.openai.models.ChatMessageImageUrl;
import com.azure.ai.openai.models.ChatMessageTextContentItem;
import com.azure.ai.openai.models.ChatRequestAssistantMessage;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestToolMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.azure.ai.openai.models.ChatResponseMessage;
import com.azure.ai.openai.models.CompletionsFinishReason;
import com.azure.ai.openai.models.CompletionsUsage;
import com.azure.ai.openai.models.FunctionCall;
import com.azure.ai.openai.models.ImageGenerationData;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.KeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.core.http.HttpClient;
import com.azure.core.http.HttpClientProvider;
import com.azure.core.http.ProxyOptions;
import com.azure.core.http.policy.ExponentialBackoffOptions;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.core.http.policy.RetryOptions;
import com.azure.core.util.BinaryData;
import com.azure.core.util.ClientOptions;
import com.azure.core.util.Header;
import com.azure.core.util.HttpClientOptions;
import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.image.Image;
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
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonRawSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Internal
class InternalAzureOpenAiHelper {
    static final String DEFAULT_USER_AGENT = "langchain4j-azure-openai";
    private static final Map<String, Object> NO_PARAMETER_DATA = new HashMap<String, Object>();

    InternalAzureOpenAiHelper() {
    }

    static OpenAIClient setupSyncClient(String endpoint, String serviceVersion, Object credential, Duration timeout, Integer maxRetries, RetryOptions retryOptions, HttpClientProvider httpClientProvider, ProxyOptions proxyOptions, boolean logRequestsAndResponses, String userAgentSuffix, Map<String, String> customHeaders) {
        OpenAIClientBuilder openAIClientBuilder = InternalAzureOpenAiHelper.setupOpenAIClientBuilder(endpoint, serviceVersion, credential, timeout, maxRetries, retryOptions, httpClientProvider, proxyOptions, logRequestsAndResponses, userAgentSuffix, customHeaders);
        return openAIClientBuilder.buildClient();
    }

    static OpenAIAsyncClient setupAsyncClient(String endpoint, String serviceVersion, Object credential, Duration timeout, Integer maxRetries, RetryOptions retryOptions, HttpClientProvider httpClientProvider, ProxyOptions proxyOptions, boolean logRequestsAndResponses, String userAgentSuffix, Map<String, String> customHeaders) {
        OpenAIClientBuilder openAIClientBuilder = InternalAzureOpenAiHelper.setupOpenAIClientBuilder(endpoint, serviceVersion, credential, timeout, maxRetries, retryOptions, httpClientProvider, proxyOptions, logRequestsAndResponses, userAgentSuffix, customHeaders);
        return openAIClientBuilder.buildAsyncClient();
    }

    private static OpenAIClientBuilder setupOpenAIClientBuilder(String endpoint, String serviceVersion, Object credential, Duration timeout, Integer maxRetries, RetryOptions retryOptions, HttpClientProvider httpClientProvider, ProxyOptions proxyOptions, boolean logRequestsAndResponses, String userAgentSuffix, Map<String, String> customHeaders) {
        timeout = (Duration)Utils.getOrDefault((Object)timeout, (Object)Duration.ofSeconds(60L));
        HttpClientOptions clientOptions = new HttpClientOptions();
        clientOptions.setConnectTimeout(timeout);
        clientOptions.setResponseTimeout(timeout);
        clientOptions.setReadTimeout(timeout);
        clientOptions.setWriteTimeout(timeout);
        clientOptions.setProxyOptions(proxyOptions);
        String userAgent = DEFAULT_USER_AGENT;
        if (userAgentSuffix != null && !userAgentSuffix.isEmpty()) {
            userAgent = "langchain4j-azure-openai-" + userAgentSuffix;
        }
        ArrayList<Header> headers = new ArrayList<Header>();
        headers.add(new Header("User-Agent", userAgent));
        if (customHeaders != null) {
            customHeaders.forEach((name, value) -> headers.add(new Header(name, value)));
        }
        clientOptions.setHeaders(headers);
        HttpClient httpClient = InternalAzureOpenAiHelper.createHttpClient(httpClientProvider, clientOptions);
        HttpLogOptions httpLogOptions = new HttpLogOptions();
        if (logRequestsAndResponses) {
            httpLogOptions.setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS);
        }
        retryOptions = InternalAzureOpenAiHelper.resolveRetryOptions(maxRetries, retryOptions);
        OpenAIClientBuilder openAIClientBuilder = new OpenAIClientBuilder().endpoint(ValidationUtils.ensureNotBlank((String)endpoint, (String)"endpoint")).serviceVersion(InternalAzureOpenAiHelper.getOpenAIServiceVersion(serviceVersion)).httpClient(httpClient).clientOptions((ClientOptions)clientOptions).httpLogOptions(httpLogOptions).retryOptions(retryOptions);
        if (credential instanceof String) {
            openAIClientBuilder.credential((KeyCredential)new AzureKeyCredential((String)credential));
        } else if (credential instanceof KeyCredential) {
            openAIClientBuilder.credential((KeyCredential)credential);
        } else if (credential instanceof TokenCredential) {
            openAIClientBuilder.credential((TokenCredential)credential);
        } else {
            throw new IllegalArgumentException("Unsupported credential type: " + credential.getClass());
        }
        return openAIClientBuilder;
    }

    static HttpClient createHttpClient(HttpClientProvider httpClientProvider, HttpClientOptions clientOptions) {
        if (httpClientProvider != null) {
            return httpClientProvider.createInstance(clientOptions);
        }
        try {
            return HttpClient.createDefault((HttpClientOptions)clientOptions);
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("No HttpClientProvider implementation found on the classpath. Add 'com.azure:azure-core-http-netty' as a dependency, or provide a custom HttpClientProvider via .httpClientProvider() on the builder.", e);
        }
    }

    static RetryOptions resolveRetryOptions(Integer maxRetries, RetryOptions retryOptions) {
        if (retryOptions == null) {
            maxRetries = (Integer)Utils.getOrDefault((Object)maxRetries, (Object)2);
            ExponentialBackoffOptions exponentialBackoffOptions = new ExponentialBackoffOptions();
            exponentialBackoffOptions.setMaxRetries(maxRetries);
            return new RetryOptions(exponentialBackoffOptions);
        }
        return retryOptions;
    }

    private static OpenAIClientBuilder authenticate(TokenCredential tokenCredential) {
        return new OpenAIClientBuilder().credential(tokenCredential);
    }

    static OpenAIServiceVersion getOpenAIServiceVersion(String serviceVersion) {
        if (serviceVersion == null || serviceVersion.trim().isEmpty()) {
            return OpenAIServiceVersion.getLatest();
        }
        for (OpenAIServiceVersion version : OpenAIServiceVersion.values()) {
            if (!version.getVersion().equals(serviceVersion)) continue;
            return version;
        }
        throw new IllegalArgumentException("Unsupported Azure OpenAI service version: '" + serviceVersion + "'. Leave serviceVersion null or empty to use the latest supported version.");
    }

    static List<ChatRequestMessage> toOpenAiMessages(List<ChatMessage> messages) {
        return messages.stream().map(InternalAzureOpenAiHelper::toOpenAiMessage).collect(Collectors.toList());
    }

    static ChatRequestMessage toOpenAiMessage(ChatMessage message) {
        if (message instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage)message;
            ChatRequestAssistantMessage chatRequestAssistantMessage = new ChatRequestAssistantMessage((String)Utils.getOrDefault((Object)aiMessage.text(), (Object)""));
            chatRequestAssistantMessage.setToolCalls(InternalAzureOpenAiHelper.toolExecutionRequestsFrom(message));
            return chatRequestAssistantMessage;
        }
        if (message instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage toolExecutionResultMessage = (ToolExecutionResultMessage)message;
            if (!toolExecutionResultMessage.hasSingleText()) {
                throw new UnsupportedFeatureException("Azure OpenAI does not support non-text content in tool results. Only text content is supported.");
            }
            return new ChatRequestToolMessage(toolExecutionResultMessage.text(), toolExecutionResultMessage.id());
        }
        if (message instanceof SystemMessage) {
            SystemMessage systemMessage = (SystemMessage)message;
            return new ChatRequestSystemMessage(systemMessage.text());
        }
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)message;
            ChatRequestUserMessage chatRequestUserMessage = userMessage.hasSingleText() ? new ChatRequestUserMessage(((TextContent)userMessage.contents().get(0)).text()) : new ChatRequestUserMessage(userMessage.contents().stream().map(content -> {
                if (content instanceof TextContent) {
                    String text = ((TextContent)content).text();
                    return new ChatMessageTextContentItem(text);
                }
                if (content instanceof ImageContent) {
                    ImageContent imageContent = (ImageContent)content;
                    String imageUrlString = InternalAzureOpenAiHelper.toImageUrl(imageContent.image());
                    ChatMessageImageUrl imageUrl = new ChatMessageImageUrl(imageUrlString);
                    return new ChatMessageImageContentItem(imageUrl);
                }
                throw new IllegalArgumentException("Unsupported content type: " + content.type());
            }).collect(Collectors.toList()));
            chatRequestUserMessage.setName(InternalAzureOpenAiHelper.nameFrom(message));
            return chatRequestUserMessage;
        }
        throw new IllegalArgumentException("Unsupported message type: " + message.type());
    }

    private static String nameFrom(ChatMessage message) {
        if (message instanceof UserMessage) {
            return ((UserMessage)message).name();
        }
        if (message instanceof ToolExecutionResultMessage) {
            return ((ToolExecutionResultMessage)message).toolName();
        }
        return null;
    }

    private static String toImageUrl(Image image) {
        if (image.url() != null) {
            return image.url().toString();
        }
        return String.format("data:%s;base64,%s", image.mimeType(), image.base64Data());
    }

    private static List<ChatCompletionsToolCall> toolExecutionRequestsFrom(ChatMessage message) {
        AiMessage aiMessage;
        if (message instanceof AiMessage && (aiMessage = (AiMessage)message).hasToolExecutionRequests()) {
            return aiMessage.toolExecutionRequests().stream().map(toolExecutionRequest -> new ChatCompletionsFunctionToolCall(toolExecutionRequest.id(), new FunctionCall(toolExecutionRequest.name(), toolExecutionRequest.arguments()))).collect(Collectors.toList());
        }
        return null;
    }

    static List<ChatCompletionsToolDefinition> toToolDefinitions(Collection<ToolSpecification> toolSpecifications) {
        return toolSpecifications.stream().map(InternalAzureOpenAiHelper::toToolDefinition).collect(Collectors.toList());
    }

    private static ChatCompletionsToolDefinition toToolDefinition(ToolSpecification toolSpecification) {
        ChatCompletionsFunctionToolDefinitionFunction functionDefinition = new ChatCompletionsFunctionToolDefinitionFunction(toolSpecification.name());
        functionDefinition.setDescription(toolSpecification.description());
        functionDefinition.setParameters(InternalAzureOpenAiHelper.getParameters(toolSpecification));
        return new ChatCompletionsFunctionToolDefinition(functionDefinition);
    }

    static ChatCompletionsToolSelection toToolChoice(ToolChoice toolChoice) {
        ChatCompletionsToolSelectionPreset preset;
        switch (toolChoice) {
            case AUTO: {
                preset = ChatCompletionsToolSelectionPreset.AUTO;
                break;
            }
            case REQUIRED: {
                preset = ChatCompletionsToolSelectionPreset.REQUIRED;
                break;
            }
            case NONE: {
                preset = ChatCompletionsToolSelectionPreset.NONE;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unexpected tool choice: " + toolChoice);
            }
        }
        return new ChatCompletionsToolSelection(preset);
    }

    private static BinaryData getParameters(ToolSpecification toolSpecification) {
        return InternalAzureOpenAiHelper.toOpenAiParameters(toolSpecification.parameters());
    }

    private static BinaryData toOpenAiParameters(JsonObjectSchema toolParameters) {
        Parameters parameters = new Parameters();
        if (toolParameters == null) {
            return BinaryData.fromObject(NO_PARAMETER_DATA);
        }
        parameters.setProperties(JsonSchemaElementUtils.toMap((Map)toolParameters.properties()));
        parameters.setRequired(toolParameters.required());
        return BinaryData.fromObject((Object)parameters);
    }

    static AiMessage aiMessageFrom(ChatResponseMessage chatResponseMessage) {
        String text = chatResponseMessage.getContent();
        if (Utils.isNullOrEmpty((Collection)chatResponseMessage.getToolCalls())) {
            return AiMessage.aiMessage((String)text);
        }
        List toolExecutionRequests = chatResponseMessage.getToolCalls().stream().filter(toolCall -> toolCall instanceof ChatCompletionsFunctionToolCall).map(toolCall -> (ChatCompletionsFunctionToolCall)toolCall).map(chatCompletionsFunctionToolCall -> ToolExecutionRequest.builder().id(chatCompletionsFunctionToolCall.getId()).name(chatCompletionsFunctionToolCall.getFunction().getName()).arguments(chatCompletionsFunctionToolCall.getFunction().getArguments()).build()).collect(Collectors.toList());
        return Utils.isNullOrBlank((String)text) ? AiMessage.aiMessage(toolExecutionRequests) : AiMessage.aiMessage((String)text, toolExecutionRequests);
    }

    static Image imageFrom(ImageGenerationData imageGenerationData) {
        Image.Builder imageBuilder = Image.builder().revisedPrompt(imageGenerationData.getRevisedPrompt());
        String urlString = imageGenerationData.getUrl();
        String imageData = imageGenerationData.getBase64Data();
        if (urlString != null) {
            try {
                URI uri = new URI(urlString);
                imageBuilder.url(uri);
            }
            catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else if (imageData != null) {
            imageBuilder.base64Data(imageData);
        }
        return imageBuilder.build();
    }

    static TokenUsage tokenUsageFrom(CompletionsUsage openAiUsage) {
        if (openAiUsage == null) {
            return null;
        }
        return new TokenUsage(Integer.valueOf(openAiUsage.getPromptTokens()), Integer.valueOf(openAiUsage.getCompletionTokens()), Integer.valueOf(openAiUsage.getTotalTokens()));
    }

    static FinishReason finishReasonFrom(CompletionsFinishReason openAiFinishReason) {
        if (openAiFinishReason == null) {
            return null;
        }
        if (openAiFinishReason == CompletionsFinishReason.STOPPED) {
            return FinishReason.STOP;
        }
        if (openAiFinishReason == CompletionsFinishReason.TOKEN_LIMIT_REACHED) {
            return FinishReason.LENGTH;
        }
        if (openAiFinishReason == CompletionsFinishReason.CONTENT_FILTERED) {
            return FinishReason.CONTENT_FILTER;
        }
        if (openAiFinishReason == CompletionsFinishReason.FUNCTION_CALL) {
            return FinishReason.TOOL_EXECUTION;
        }
        if (openAiFinishReason == CompletionsFinishReason.TOOL_CALLS) {
            return FinishReason.TOOL_EXECUTION;
        }
        return null;
    }

    static ChatCompletionsResponseFormat toAzureOpenAiResponseFormat(ResponseFormat responseFormat, boolean strict) {
        if (responseFormat == null || responseFormat.type() == ResponseFormatType.TEXT) {
            return new ChatCompletionsTextResponseFormat();
        }
        if (responseFormat.type() != ResponseFormatType.JSON) {
            throw new IllegalArgumentException("Unsupported response format: " + responseFormat);
        }
        JsonSchema jsonSchema = responseFormat.jsonSchema();
        if (jsonSchema == null) {
            return new ChatCompletionsJsonResponseFormat();
        }
        if (!(jsonSchema.rootElement() instanceof JsonObjectSchema) && !(jsonSchema.rootElement() instanceof JsonRawSchema)) {
            throw new IllegalArgumentException("For Azure OpenAI, the root element of the JSON Schema must be either a JsonObjectSchema or a JsonRawSchema, but it was: " + jsonSchema.rootElement().getClass());
        }
        ChatCompletionsJsonSchemaResponseFormatJsonSchema schema = new ChatCompletionsJsonSchemaResponseFormatJsonSchema(jsonSchema.name());
        schema.setStrict(Boolean.valueOf(strict));
        Map schemaMap = JsonSchemaElementUtils.toMap((JsonSchemaElement)jsonSchema.rootElement(), (boolean)strict);
        schema.setSchema(BinaryData.fromObject((Object)schemaMap));
        return new ChatCompletionsJsonSchemaResponseFormat(schema);
    }

    static void validate(ChatRequestParameters parameters) {
        if (parameters.topK() != null) {
            throw new UnsupportedFeatureException("'topK' parameter is not supported by OpenAI");
        }
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

