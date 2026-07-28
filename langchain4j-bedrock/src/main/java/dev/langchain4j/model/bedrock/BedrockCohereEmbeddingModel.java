/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
 *  software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
 *  software.amazon.awssdk.core.SdkBytes
 *  software.amazon.awssdk.regions.Region
 *  software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient
 *  software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClientBuilder
 *  software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest
 *  software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.bedrock.BedrockCohereEmbeddingResponse;
import dev.langchain4j.model.bedrock.BedrockExceptionMapper;
import dev.langchain4j.model.bedrock.Json;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClientBuilder;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

public class BedrockCohereEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private static final int DEFAULT_MAX_SEGMENTS_PER_BATCH = 96;
    private static final String INPUT_TOKEN_COUNT_HEADER = "x-amzn-bedrock-input-token-count";
    private final BedrockRuntimeClient client;
    private final String model;
    private final String inputType;
    private final String truncate;
    private final int maxRetries;
    private final int maxSegmentsPerBatch;

    public BedrockCohereEmbeddingModel(Builder builder) {
        this.client = (BedrockRuntimeClient)Utils.getOrDefault((Object)builder.client, () -> this.initClient(builder));
        this.model = ValidationUtils.ensureNotBlank((String)builder.model, (String)"model");
        this.inputType = ValidationUtils.ensureNotBlank((String)builder.inputType, (String)"inputType");
        this.truncate = builder.truncate;
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.maxSegmentsPerBatch = (Integer)Utils.getOrDefault((Object)builder.maxSegmentsPerBatch, (Object)96);
    }

    private BedrockRuntimeClient initClient(Builder builder) {
        return (BedrockRuntimeClient)((BedrockRuntimeClientBuilder)((BedrockRuntimeClientBuilder)BedrockRuntimeClient.builder().region((Region)Utils.getOrDefault((Object)builder.region, (Object)Region.US_EAST_1))).credentialsProvider((AwsCredentialsProvider)Utils.getOrDefault((Object)builder.credentialsProvider, () -> DefaultCredentialsProvider.builder().build()))).build();
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        ArrayList embeddings = new ArrayList();
        Integer inputTokenCount = null;
        for (int i = 0; i < textSegments.size(); i += this.maxSegmentsPerBatch) {
            List<TextSegment> batch = textSegments.subList(i, Math.min(textSegments.size(), i + this.maxSegmentsPerBatch));
            Map<String, Object> requestParameters = this.toRequestParameters(batch);
            String requestJson = Json.toJson(requestParameters);
            InvokeModelResponse invokeModelResponse = (InvokeModelResponse)RetryUtils.withRetryMappingExceptions(() -> this.invoke(requestJson), (int)this.maxRetries, (ExceptionMapper)BedrockExceptionMapper.INSTANCE);
            String responseJson = invokeModelResponse.body().asUtf8String();
            BedrockCohereEmbeddingResponse embeddingResponse = Json.fromJson(responseJson, BedrockCohereEmbeddingResponse.class);
            embeddings.addAll(Arrays.stream(embeddingResponse.getEmbeddings().getFloatEmbeddings()).map(Embedding::from).collect(Collectors.toList()));
            inputTokenCount = this.sum(inputTokenCount, this.inputTokenCountFrom(invokeModelResponse).orElse(embeddingResponse.getInputTextTokenCount()));
        }
        return Response.from(embeddings, (TokenUsage)this.tokenUsageFrom(inputTokenCount));
    }

    private Optional<Integer> inputTokenCountFrom(InvokeModelResponse response) {
        return response.sdkHttpResponse().firstMatchingHeader(INPUT_TOKEN_COUNT_HEADER).map(Integer::parseInt);
    }

    private TokenUsage tokenUsageFrom(Integer inputTokenCount) {
        return inputTokenCount == null ? null : new TokenUsage(inputTokenCount);
    }

    private Integer sum(Integer first, Integer second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        return first + second;
    }

    private Map<String, Object> toRequestParameters(List<TextSegment> textSegments) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("texts", textSegments.stream().map(TextSegment::text).collect(Collectors.toList()));
        parameters.put("input_type", this.inputType);
        parameters.put("truncate", this.truncate);
        parameters.put("embedding_types", Collections.singletonList("float"));
        return parameters;
    }

    private InvokeModelResponse invoke(String body) {
        InvokeModelRequest invokeModelRequest = (InvokeModelRequest)InvokeModelRequest.builder().modelId(this.model).body(SdkBytes.fromString((String)body, (Charset)Charset.defaultCharset())).build();
        return this.client.invokeModel(invokeModelRequest);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static enum Truncate {
        NONE("NONE"),
        START("START"),
        END("END");

        private final String value;

        private Truncate(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static enum InputType {
        SEARCH_DOCUMENT("search_document"),
        SEARCH_QUERY("search_query"),
        CLASSIFICATION("classification"),
        CLUSTERING("clustering");

        private final String value;

        private InputType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static enum Model {
        COHERE_EMBED_ENGLISH_V3("cohere.embed-english-v3"),
        COHERE_EMBED_MULTILINGUAL_V3("cohere.embed-multilingual-v3");

        private final String value;

        private Model(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static class Builder {
        private String model;
        private String inputType;
        private String truncate;
        private BedrockRuntimeClient client;
        private Region region;
        private AwsCredentialsProvider credentialsProvider;
        private Integer maxRetries;
        private Integer maxSegmentsPerBatch;

        public Builder model(Model model) {
            return this.model(model.getValue());
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder inputType(InputType inputType) {
            return this.inputType(inputType.getValue());
        }

        public Builder inputType(String inputType) {
            this.inputType = inputType;
            return this;
        }

        public Builder truncate(Truncate truncate) {
            return this.truncate(truncate.getValue());
        }

        public Builder truncate(String truncate) {
            this.truncate = truncate;
            return this;
        }

        public Builder client(BedrockRuntimeClient client) {
            this.client = client;
            return this;
        }

        public Builder region(Region region) {
            this.region = region;
            return this;
        }

        public Builder credentialsProvider(AwsCredentialsProvider credentialsProvider) {
            this.credentialsProvider = credentialsProvider;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder maxSegmentsPerBatch(Integer maxSegmentsPerBatch) {
            this.maxSegmentsPerBatch = maxSegmentsPerBatch;
            return this;
        }

        public BedrockCohereEmbeddingModel build() {
            return new BedrockCohereEmbeddingModel(this);
        }
    }
}

