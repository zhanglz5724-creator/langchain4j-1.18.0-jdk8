/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.api.gax.core.CredentialsProvider
 *  com.google.api.gax.core.FixedCredentialsProvider
 *  com.google.auth.Credentials
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.cloud.aiplatform.v1.EndpointName
 *  com.google.cloud.aiplatform.v1.PredictResponse
 *  com.google.cloud.aiplatform.v1.PredictionServiceClient
 *  com.google.cloud.aiplatform.v1.PredictionServiceSettings
 *  com.google.cloud.aiplatform.v1.PredictionServiceSettings$Builder
 *  com.google.protobuf.Message$Builder
 *  com.google.protobuf.Value
 *  com.google.protobuf.Value$Builder
 *  com.google.protobuf.util.JsonFormat
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ChatMessageType
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.internal.ChatRequestValidationUtils
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.vertexai;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.aiplatform.v1.EndpointName;
import com.google.cloud.aiplatform.v1.PredictResponse;
import com.google.cloud.aiplatform.v1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1.PredictionServiceSettings;
import com.google.protobuf.Message;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.ChatRequestValidationUtils;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.vertexai.Json;
import dev.langchain4j.model.vertexai.VertexAiChatInstance;
import dev.langchain4j.model.vertexai.VertexAiParameters;
import dev.langchain4j.model.vertexai.spi.VertexAiChatModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class VertexAiChatModel
implements ChatModel {
    private final PredictionServiceSettings settings;
    private final EndpointName endpointName;
    private final VertexAiParameters vertexAiParameters;
    private final Integer maxRetries;

    public VertexAiChatModel(Builder builder) {
        try {
            PredictionServiceSettings.Builder settingsBuilder = (PredictionServiceSettings.Builder)PredictionServiceSettings.newBuilder().setEndpoint(ValidationUtils.ensureNotBlank((String)builder.endpoint, (String)"endpoint"));
            if (builder.credentials != null) {
                GoogleCredentials scopedCredentials = builder.credentials.createScoped(new String[]{"https://www.googleapis.com/auth/cloud-platform"});
                settingsBuilder.setCredentialsProvider((CredentialsProvider)FixedCredentialsProvider.create((Credentials)scopedCredentials));
            }
            this.settings = settingsBuilder.build();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.endpointName = EndpointName.ofProjectLocationPublisherModelName((String)ValidationUtils.ensureNotBlank((String)builder.project, (String)"project"), (String)ValidationUtils.ensureNotBlank((String)builder.location, (String)"location"), (String)ValidationUtils.ensureNotBlank((String)builder.publisher, (String)"publisher"), (String)ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName"));
        this.vertexAiParameters = new VertexAiParameters(builder.temperature, builder.maxOutputTokens, builder.topK, builder.topP);
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    @Deprecated
    public VertexAiChatModel(String endpoint, String project, String location, String publisher, String modelName, Double temperature, Integer maxOutputTokens, Integer topK, Double topP, Integer maxRetries) {
        this(VertexAiChatModel.builder().endpoint(endpoint).project(project).location(location).publisher(publisher).modelName(modelName).temperature(temperature).maxOutputTokens(maxOutputTokens).topK(topK).topP(topP).maxRetries(maxRetries));
    }

    public ChatResponse chat(ChatRequest chatRequest) {
        ChatRequestValidationUtils.validateMessages((List)chatRequest.messages());
        ChatRequestParameters parameters = chatRequest.parameters();
        ChatRequestValidationUtils.validateParameters((ChatRequestParameters)parameters);
        ChatRequestValidationUtils.validate((ToolChoice)parameters.toolChoice());
        ChatRequestValidationUtils.validate((List)parameters.toolSpecifications());
        ChatRequestValidationUtils.validate((ResponseFormat)parameters.responseFormat());
        Response<AiMessage> response = this.generate(chatRequest.messages());
        return ChatResponse.builder().aiMessage((AiMessage)response.content()).metadata(ChatResponseMetadata.builder().tokenUsage(response.tokenUsage()).finishReason(response.finishReason()).build()).build();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Response<AiMessage> generate(List<ChatMessage> messages) {
        try (PredictionServiceClient client = PredictionServiceClient.create((PredictionServiceSettings)this.settings);){
            VertexAiChatInstance vertexAiChatInstance = new VertexAiChatInstance(VertexAiChatModel.toContext(messages), VertexAiChatModel.toVertexMessages(messages));
            Value.Builder instanceBuilder = Value.newBuilder();
            JsonFormat.parser().merge(Json.toJson(vertexAiChatInstance), (Message.Builder)instanceBuilder);
            List<Value> instances = Collections.singletonList(instanceBuilder.build());
            Value.Builder parametersBuilder = Value.newBuilder();
            JsonFormat.parser().merge(Json.toJson(this.vertexAiParameters), (Message.Builder)parametersBuilder);
            Value parameters = parametersBuilder.build();
            PredictResponse response = (PredictResponse)RetryUtils.withRetryMappingExceptions(() -> client.predict(this.endpointName, instances, parameters), (int)this.maxRetries);
            Response response2 = Response.from((Object)AiMessage.from((String)VertexAiChatModel.extractContent(response)), (TokenUsage)new TokenUsage(Integer.valueOf(VertexAiChatModel.extractTokenCount(response, "inputTokenCount")), Integer.valueOf(VertexAiChatModel.extractTokenCount(response, "outputTokenCount"))));
            return response2;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String extractContent(PredictResponse predictResponse) {
        return ((Value)((Value)predictResponse.getPredictions(0).getStructValue().getFieldsMap().get("candidates")).getListValue().getValues(0).getStructValue().getFieldsMap().get("content")).getStringValue();
    }

    static int extractTokenCount(PredictResponse predictResponse, String fieldName) {
        return (int)((Value)((Value)((Value)predictResponse.getMetadata().getStructValue().getFieldsMap().get("tokenMetadata")).getStructValue().getFieldsMap().get(fieldName)).getStructValue().getFieldsMap().get("totalTokens")).getNumberValue();
    }

    private static List<VertexAiChatInstance.Message> toVertexMessages(List<ChatMessage> messages) {
        return messages.stream().filter(chatMessage -> chatMessage.type() == ChatMessageType.USER || chatMessage.type() == ChatMessageType.AI).map(chatMessage -> new VertexAiChatInstance.Message(chatMessage.type().name(), VertexAiChatModel.toText(chatMessage))).collect(Collectors.toList());
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

    private static String toContext(List<ChatMessage> messages) {
        return messages.stream().filter(chatMessage -> chatMessage.type() == ChatMessageType.SYSTEM).map(VertexAiChatModel::toText).collect(Collectors.joining("\n"));
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(VertexAiChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            VertexAiChatModelBuilderFactory factory = (VertexAiChatModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public static class Builder {
        private String endpoint;
        private String project;
        private String location;
        private String publisher;
        private String modelName;
        private Double temperature;
        private Integer maxOutputTokens = 200;
        private Integer topK;
        private Double topP;
        private Integer maxRetries;
        private GoogleCredentials credentials;

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder project(String project) {
            this.project = project;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder publisher(String publisher) {
            this.publisher = publisher;
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

        public Builder maxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
            return this;
        }

        public Builder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder credentials(GoogleCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public VertexAiChatModel build() {
            return new VertexAiChatModel(this);
        }
    }
}

