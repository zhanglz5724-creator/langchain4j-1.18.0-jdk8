/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.ChatRequestValidationUtils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.huggingface;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.ChatRequestValidationUtils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.huggingface.FactoryCreator;
import dev.langchain4j.model.huggingface.client.HuggingFaceClient;
import dev.langchain4j.model.huggingface.client.Options;
import dev.langchain4j.model.huggingface.client.Parameters;
import dev.langchain4j.model.huggingface.client.TextGenerationRequest;
import dev.langchain4j.model.huggingface.client.TextGenerationResponse;
import dev.langchain4j.model.huggingface.spi.HuggingFaceChatModelBuilderFactory;
import dev.langchain4j.model.huggingface.spi.HuggingFaceClientFactory;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class HuggingFaceChatModel
implements ChatModel {
    private final HuggingFaceClient client;
    private final Double temperature;
    private final Integer maxNewTokens;
    private final Boolean returnFullText;
    private final Boolean waitForModel;

    public HuggingFaceChatModel(String accessToken, String modelId, Duration timeout, Double temperature, Integer maxNewTokens, Boolean returnFullText, Boolean waitForModel) {
        this(HuggingFaceChatModel.builder().accessToken(accessToken).modelId(modelId).timeout(timeout).temperature(temperature).maxNewTokens(maxNewTokens).returnFullText(returnFullText).waitForModel(waitForModel));
    }

    public HuggingFaceChatModel(String baseUrl, String accessToken, String modelId, Duration timeout, Double temperature, Integer maxNewTokens, Boolean returnFullText, Boolean waitForModel) {
        this(HuggingFaceChatModel.builder().baseUrl(baseUrl).accessToken(accessToken).modelId(modelId).timeout(timeout).temperature(temperature).maxNewTokens(maxNewTokens).returnFullText(returnFullText).waitForModel(waitForModel));
    }

    public HuggingFaceChatModel(final Builder builder) {
        this.client = FactoryCreator.FACTORY.create(new HuggingFaceClientFactory.Input(){

            @Override
            public String baseUrl() {
                return builder.baseUrl;
            }

            @Override
            public String apiKey() {
                return builder.accessToken;
            }

            @Override
            public String modelId() {
                return builder.modelId;
            }

            @Override
            public Duration timeout() {
                return builder.timeout;
            }

            @Override
            public HttpClientBuilder httpClientBuilder() {
                return builder.httpClientBuilder;
            }
        });
        this.temperature = builder.temperature;
        this.maxNewTokens = builder.maxNewTokens;
        this.returnFullText = builder.returnFullText;
        this.waitForModel = builder.waitForModel;
    }

    public ChatResponse chat(ChatRequest chatRequest) {
        ChatRequestValidationUtils.validateMessages((List)chatRequest.messages());
        ChatRequestParameters parameters = chatRequest.parameters();
        ChatRequestValidationUtils.validateParameters((ChatRequestParameters)parameters);
        ChatRequestValidationUtils.validate((List)parameters.toolSpecifications());
        ChatRequestValidationUtils.validate((ToolChoice)parameters.toolChoice());
        ChatRequestValidationUtils.validate((ResponseFormat)parameters.responseFormat());
        Response<AiMessage> response = this.generate(chatRequest.messages());
        return ChatResponse.builder().aiMessage((AiMessage)response.content()).metadata(ChatResponseMetadata.builder().tokenUsage(response.tokenUsage()).finishReason(response.finishReason()).build()).build();
    }

    private Response<AiMessage> generate(List<ChatMessage> messages) {
        TextGenerationRequest request = TextGenerationRequest.builder().inputs(messages.stream().map(HuggingFaceChatModel::toText).collect(Collectors.joining("\n"))).parameters(Parameters.builder().temperature(this.temperature).maxNewTokens(this.maxNewTokens).returnFullText(this.returnFullText).build()).options(Options.builder().waitForModel(this.waitForModel).build()).build();
        TextGenerationResponse textGenerationResponse = this.client.chat(request);
        return Response.from((Object)AiMessage.from((String)textGenerationResponse.getGeneratedText()));
    }

    private static String toText(ChatMessage chatMessage) {
        if (chatMessage instanceof SystemMessage) {
            SystemMessage systemMessage = (SystemMessage)chatMessage;
            return systemMessage.text();
        }
        if (chatMessage instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)chatMessage;
            return userMessage.singleText();
        }
        if (chatMessage instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage)chatMessage;
            return aiMessage.text();
        }
        throw new IllegalArgumentException("Unsupported message type: " + chatMessage.type());
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(HuggingFaceChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            HuggingFaceChatModelBuilderFactory factory = (HuggingFaceChatModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public static HuggingFaceChatModel withAccessToken(String accessToken) {
        return HuggingFaceChatModel.builder().accessToken(accessToken).build();
    }

    public static final class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String accessToken;
        private String modelId;
        private Duration timeout = Duration.ofSeconds(15L);
        private Double temperature;
        private Integer maxNewTokens;
        private Boolean returnFullText = false;
        private Boolean waitForModel = true;

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder modelId(String modelId) {
            if (modelId != null) {
                this.modelId = modelId;
            }
            return this;
        }

        public Builder timeout(Duration timeout) {
            if (timeout != null) {
                this.timeout = timeout;
            }
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder maxNewTokens(Integer maxNewTokens) {
            this.maxNewTokens = maxNewTokens;
            return this;
        }

        public Builder returnFullText(Boolean returnFullText) {
            if (returnFullText != null) {
                this.returnFullText = returnFullText;
            }
            return this;
        }

        public Builder waitForModel(Boolean waitForModel) {
            if (waitForModel != null) {
                this.waitForModel = waitForModel;
            }
            return this;
        }

        public HuggingFaceChatModel build() {
            ValidationUtils.ensureNotBlank((String)this.accessToken, (String)"%s", (Object[])new Object[]{"HuggingFace access token must be defined. It can be generated here: https://huggingface.co/settings/tokens"});
            return new HuggingFaceChatModel(this);
        }
    }
}

