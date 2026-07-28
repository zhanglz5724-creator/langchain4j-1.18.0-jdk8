/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.cloud.aiplatform.v1.EndpointName
 *  com.google.cloud.aiplatform.v1.PredictResponse
 *  com.google.cloud.aiplatform.v1.PredictionServiceClient
 *  com.google.cloud.aiplatform.v1.PredictionServiceSettings
 *  com.google.cloud.aiplatform.v1.PredictionServiceSettings$Builder
 *  com.google.protobuf.Message$Builder
 *  com.google.protobuf.Value
 *  com.google.protobuf.Value$Builder
 *  com.google.protobuf.util.JsonFormat
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.language.LanguageModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.vertexai;

import com.google.cloud.aiplatform.v1.EndpointName;
import com.google.cloud.aiplatform.v1.PredictResponse;
import com.google.cloud.aiplatform.v1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1.PredictionServiceSettings;
import com.google.protobuf.Message;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.language.LanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.vertexai.Json;
import dev.langchain4j.model.vertexai.VertexAiChatModel;
import dev.langchain4j.model.vertexai.VertexAiParameters;
import dev.langchain4j.model.vertexai.VertexAiTextInstance;
import dev.langchain4j.model.vertexai.spi.VertexAiLanguageModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class VertexAiLanguageModel
implements LanguageModel {
    private final PredictionServiceSettings settings;
    private final EndpointName endpointName;
    private final VertexAiParameters vertexAiParameters;
    private final Integer maxRetries;

    public VertexAiLanguageModel(String endpoint, String project, String location, String publisher, String modelName, Double temperature, Integer maxOutputTokens, Integer topK, Double topP, Integer maxRetries) {
        try {
            this.settings = ((PredictionServiceSettings.Builder)PredictionServiceSettings.newBuilder().setEndpoint(ValidationUtils.ensureNotBlank((String)endpoint, (String)"endpoint"))).build();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.endpointName = EndpointName.ofProjectLocationPublisherModelName((String)ValidationUtils.ensureNotBlank((String)project, (String)"project"), (String)ValidationUtils.ensureNotBlank((String)location, (String)"location"), (String)ValidationUtils.ensureNotBlank((String)publisher, (String)"publisher"), (String)ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName"));
        this.vertexAiParameters = new VertexAiParameters(temperature, maxOutputTokens, topK, topP);
        this.maxRetries = maxRetries == null ? 3 : maxRetries;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Response<String> generate(String prompt) {
        try (PredictionServiceClient client = PredictionServiceClient.create((PredictionServiceSettings)this.settings);){
            Value.Builder instanceBuilder = Value.newBuilder();
            JsonFormat.parser().merge(Json.toJson(new VertexAiTextInstance(prompt)), (Message.Builder)instanceBuilder);
            List<Value> instances = Collections.singletonList(instanceBuilder.build());
            Value.Builder parametersBuilder = Value.newBuilder();
            JsonFormat.parser().merge(Json.toJson(this.vertexAiParameters), (Message.Builder)parametersBuilder);
            Value parameters = parametersBuilder.build();
            PredictResponse response = (PredictResponse)RetryUtils.withRetryMappingExceptions(() -> client.predict(this.endpointName, instances, parameters), (int)this.maxRetries);
            Response response2 = Response.from((Object)VertexAiLanguageModel.extractContent(response), (TokenUsage)new TokenUsage(Integer.valueOf(VertexAiChatModel.extractTokenCount(response, "inputTokenCount")), Integer.valueOf(VertexAiChatModel.extractTokenCount(response, "outputTokenCount"))));
            return response2;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String extractContent(PredictResponse predictResponse) {
        return ((Value)predictResponse.getPredictions(0).getStructValue().getFieldsMap().get("content")).getStringValue();
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(VertexAiLanguageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            VertexAiLanguageModelBuilderFactory factory = (VertexAiLanguageModelBuilderFactory)iterator.next();
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

        public VertexAiLanguageModel build() {
            return new VertexAiLanguageModel(this.endpoint, this.project, this.location, this.publisher, this.modelName, this.temperature, this.maxOutputTokens, this.topK, this.topP, this.maxRetries);
        }
    }
}

