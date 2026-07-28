/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.api.gax.core.FixedCredentialsProvider
 *  com.google.auth.Credentials
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.cloud.aiplatform.v1beta1.ComputeTokensRequest
 *  com.google.cloud.aiplatform.v1beta1.ComputeTokensResponse
 *  com.google.cloud.aiplatform.v1beta1.EndpointName
 *  com.google.cloud.aiplatform.v1beta1.LlmUtilityServiceClient
 *  com.google.cloud.aiplatform.v1beta1.LlmUtilityServiceSettings
 *  com.google.cloud.aiplatform.v1beta1.LlmUtilityServiceSettings$Builder
 *  com.google.cloud.aiplatform.v1beta1.PredictResponse
 *  com.google.cloud.aiplatform.v1beta1.PredictionServiceClient
 *  com.google.cloud.aiplatform.v1beta1.PredictionServiceSettings
 *  com.google.cloud.aiplatform.v1beta1.PredictionServiceSettings$Builder
 *  com.google.cloud.aiplatform.v1beta1.TokensInfo
 *  com.google.protobuf.Message$Builder
 *  com.google.protobuf.Value
 *  com.google.protobuf.Value$Builder
 *  com.google.protobuf.util.JsonFormat
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.vertexai;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.aiplatform.v1beta1.ComputeTokensRequest;
import com.google.cloud.aiplatform.v1beta1.ComputeTokensResponse;
import com.google.cloud.aiplatform.v1beta1.EndpointName;
import com.google.cloud.aiplatform.v1beta1.LlmUtilityServiceClient;
import com.google.cloud.aiplatform.v1beta1.LlmUtilityServiceSettings;
import com.google.cloud.aiplatform.v1beta1.PredictResponse;
import com.google.cloud.aiplatform.v1beta1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1beta1.PredictionServiceSettings;
import com.google.cloud.aiplatform.v1beta1.TokensInfo;
import com.google.protobuf.Message;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.vertexai.Json;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingInstance;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingModelName;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingParameters;
import dev.langchain4j.model.vertexai.spi.VertexAiEmbeddingModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VertexAiEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private static final String DEFAULT_GOOGLEAPIS_ENDPOINT_SUFFIX = "-aiplatform.googleapis.com:443";
    private static final int COMPUTE_TOKENS_MAX_INPUTS_PER_REQUEST = 2048;
    private static final int DEFAULT_MAX_SEGMENTS_PER_BATCH = 250;
    private static final int DEFAULT_MAX_TOKENS_PER_BATCH = 20000;
    private final PredictionServiceSettings settings;
    private final LlmUtilityServiceSettings llmUtilitySettings;
    private final EndpointName endpointName;
    private final Integer maxRetries;
    private final Integer maxSegmentsPerBatch;
    private final Integer maxTokensPerBatch;
    private final TaskType taskType;
    private final String titleMetadataKey;
    private final Integer outputDimensionality;
    private final Boolean autoTruncate;
    private final String modelName;

    public VertexAiEmbeddingModel(Builder builder) {
        String regionWithBaseAPI = builder.endpoint != null ? builder.endpoint : ValidationUtils.ensureNotBlank((String)builder.location, (String)"location") + DEFAULT_GOOGLEAPIS_ENDPOINT_SUFFIX;
        this.endpointName = EndpointName.ofProjectLocationPublisherModelName((String)ValidationUtils.ensureNotBlank((String)builder.project, (String)"project"), (String)builder.location, (String)ValidationUtils.ensureNotBlank((String)builder.publisher, (String)"publisher"), (String)ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName"));
        try {
            Optional<Object> credentialsProvider = Optional.empty();
            if (builder.credentials != null) {
                credentialsProvider = Optional.of(FixedCredentialsProvider.create((Credentials)builder.credentials.createScoped(new String[]{"https://www.googleapis.com/auth/cloud-platform"})));
            }
            PredictionServiceSettings.Builder settingsBuilder = (PredictionServiceSettings.Builder)PredictionServiceSettings.newBuilder().setEndpoint(regionWithBaseAPI);
            credentialsProvider.ifPresent(arg_0 -> ((PredictionServiceSettings.Builder)settingsBuilder).setCredentialsProvider(arg_0));
            this.settings = settingsBuilder.build();
            LlmUtilityServiceSettings.Builder utilBuilder = (LlmUtilityServiceSettings.Builder)LlmUtilityServiceSettings.newBuilder().setEndpoint(this.settings.getEndpoint());
            credentialsProvider.ifPresent(arg_0 -> ((LlmUtilityServiceSettings.Builder)utilBuilder).setCredentialsProvider(arg_0));
            this.llmUtilitySettings = utilBuilder.build();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.maxSegmentsPerBatch = ValidationUtils.ensureGreaterThanZero((Integer)((Integer)Utils.getOrDefault((Object)builder.maxSegmentsPerBatch, (Object)250)), (String)"maxSegmentsPerBatch");
        this.maxTokensPerBatch = ValidationUtils.ensureGreaterThanZero((Integer)((Integer)Utils.getOrDefault((Object)builder.maxTokensPerBatch, (Object)20000)), (String)"maxTokensPerBatch");
        this.taskType = builder.taskType;
        this.titleMetadataKey = (String)Utils.getOrDefault((Object)builder.titleMetadataKey, (Object)"title");
        this.outputDimensionality = builder.outputDimensionality;
        this.autoTruncate = (Boolean)Utils.getOrDefault((Object)builder.autoTruncate, (Object)false);
        this.modelName = builder.modelName;
    }

    @Deprecated
    public VertexAiEmbeddingModel(String endpoint, String project, String location, String publisher, String modelName, Integer maxRetries, Integer maxSegmentsPerBatch, Integer maxTokensPerBatch, TaskType taskType, String titleMetadataKey, Integer outputDimensionality, Boolean autoTruncate) {
        this(VertexAiEmbeddingModel.builder().endpoint(endpoint).project(project).location(location).publisher(publisher).modelName(modelName).maxRetries(maxRetries).maxSegmentsPerBatch(maxSegmentsPerBatch).maxTokensPerBatch(maxTokensPerBatch).taskType(taskType).titleMetadataKey(titleMetadataKey).outputDimensionality(outputDimensionality).autoTruncate(autoTruncate));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Response<List<Embedding>> embedAll(List<TextSegment> segments) {
        try (PredictionServiceClient client = PredictionServiceClient.create((PredictionServiceSettings)this.settings);){
            ArrayList embeddings = new ArrayList();
            int inputTokenCount = 0;
            List<Integer> tokensCounts = this.calculateTokensCounts(segments);
            List<Integer> batchSizes = this.groupByBatches(tokensCounts);
            int i = 0;
            for (int j = 0; i < segments.size() && j < batchSizes.size(); i += batchSizes.get(j).intValue(), ++j) {
                List<TextSegment> batch = segments.subList(i, i + batchSizes.get(j));
                ArrayList<Value> instances = new ArrayList<Value>();
                for (TextSegment segment : batch) {
                    VertexAiEmbeddingInstance embeddingInstance = new VertexAiEmbeddingInstance(segment.text());
                    if (this.taskType != null) {
                        embeddingInstance.setTaskType(this.taskType);
                        if (this.taskType.equals((Object)TaskType.RETRIEVAL_DOCUMENT)) {
                            embeddingInstance.setTitle(segment.metadata().getString(this.titleMetadataKey));
                        }
                    }
                    Value.Builder instanceBuilder = Value.newBuilder();
                    JsonFormat.parser().merge(Json.toJson(embeddingInstance), (Message.Builder)instanceBuilder);
                    instances.add(instanceBuilder.build());
                }
                VertexAiEmbeddingParameters parameters = new VertexAiEmbeddingParameters(this.outputDimensionality, (Boolean)Utils.getOrDefault((Object)this.autoTruncate, (Object)false));
                Value.Builder parameterBuilder = Value.newBuilder();
                JsonFormat.parser().merge(Json.toJson(parameters), (Message.Builder)parameterBuilder);
                PredictResponse response = (PredictResponse)RetryUtils.withRetryMappingExceptions(() -> client.predict(this.endpointName, instances, parameterBuilder.build()), (int)this.maxRetries);
                embeddings.addAll(response.getPredictionsList().stream().map(VertexAiEmbeddingModel::toEmbedding).collect(Collectors.toList()));
                for (Value prediction : response.getPredictionsList()) {
                    inputTokenCount += VertexAiEmbeddingModel.extractTokenCount(prediction);
                }
            }
            Response response = Response.from(embeddings, (TokenUsage)new TokenUsage(Integer.valueOf(inputTokenCount)));
            return response;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String modelName() {
        return this.modelName;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public List<Integer> calculateTokensCounts(List<TextSegment> segments) {
        try (LlmUtilityServiceClient utilClient = LlmUtilityServiceClient.create((LlmUtilityServiceSettings)this.llmUtilitySettings);){
            ArrayList<Integer> tokensCounts = new ArrayList<Integer>();
            for (int i = 0; i < segments.size(); i += 2048) {
                List<TextSegment> batch = segments.subList(i, Math.min(i + 2048, segments.size()));
                ArrayList<Value> instances = new ArrayList<Value>();
                for (TextSegment segment : batch) {
                    Value.Builder instanceBuilder = Value.newBuilder();
                    JsonFormat.parser().merge(Json.toJson(new VertexAiEmbeddingInstance(segment.text())), (Message.Builder)instanceBuilder);
                    instances.add(instanceBuilder.build());
                }
                ComputeTokensRequest computeTokensRequest = ComputeTokensRequest.newBuilder().setEndpoint(this.endpointName.toString()).addAllInstances(instances).build();
                ComputeTokensResponse computeTokensResponse = utilClient.computeTokens(computeTokensRequest);
                tokensCounts.addAll(computeTokensResponse.getTokensInfoList().stream().map(TokensInfo::getTokensCount).collect(Collectors.toList()));
            }
            ArrayList<Integer> arrayList = tokensCounts;
            return arrayList;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Integer knownDimension() {
        return VertexAiEmbeddingModelName.knownDimension(this.endpointName.getModel());
    }

    private List<Integer> groupByBatches(List<Integer> tokensCounts) {
        ArrayList batches = new ArrayList();
        ArrayList<Integer> currentBatch = new ArrayList<Integer>();
        int currentBatchSum = 0;
        for (Integer tokensCount : tokensCounts) {
            if (currentBatchSum + tokensCount <= this.maxTokensPerBatch && currentBatch.size() < this.maxSegmentsPerBatch) {
                currentBatch.add(tokensCount);
                currentBatchSum += tokensCount.intValue();
                continue;
            }
            batches.add(currentBatch);
            currentBatch = new ArrayList();
            currentBatch.add(tokensCount);
            currentBatchSum = tokensCount;
        }
        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }
        return batches.stream().mapToInt(List::size).boxed().collect(Collectors.toList());
    }

    private static Embedding toEmbedding(Value prediction) {
        List vector = ((Value)prediction.getStructValue().getFieldsMap().get("embeddings")).getStructValue().getFieldsOrThrow("values").getListValue().getValuesList().stream().map(v -> Float.valueOf((float)v.getNumberValue())).collect(Collectors.toList());
        return Embedding.from(vector);
    }

    private static int extractTokenCount(Value prediction) {
        return (int)((Value)((Value)((Value)prediction.getStructValue().getFieldsMap().get("embeddings")).getStructValue().getFieldsMap().get("statistics")).getStructValue().getFieldsMap().get("token_count")).getNumberValue();
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(VertexAiEmbeddingModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            VertexAiEmbeddingModelBuilderFactory factory = (VertexAiEmbeddingModelBuilderFactory)iterator.next();
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
        private Integer maxRetries;
        private Integer maxSegmentsPerBatch;
        private Integer maxTokensPerBatch;
        private TaskType taskType;
        private String titleMetadataKey;
        private Integer outputDimensionality;
        private Boolean autoTruncate;
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

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder maxSegmentsPerBatch(Integer maxBatchSize) {
            this.maxSegmentsPerBatch = maxBatchSize;
            return this;
        }

        public Builder maxTokensPerBatch(Integer maxTokensPerBatch) {
            this.maxTokensPerBatch = maxTokensPerBatch;
            return this;
        }

        public Builder taskType(TaskType taskType) {
            this.taskType = taskType;
            return this;
        }

        public Builder titleMetadataKey(String titleMetadataKey) {
            this.titleMetadataKey = titleMetadataKey;
            return this;
        }

        public Builder autoTruncate(Boolean autoTruncate) {
            this.autoTruncate = autoTruncate;
            return this;
        }

        public Builder outputDimensionality(Integer outputDimensionality) {
            this.outputDimensionality = outputDimensionality;
            return this;
        }

        public Builder credentials(GoogleCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public VertexAiEmbeddingModel build() {
            return new VertexAiEmbeddingModel(this);
        }
    }

    public static enum TaskType {
        RETRIEVAL_QUERY,
        RETRIEVAL_DOCUMENT,
        SEMANTIC_SIMILARITY,
        CLASSIFICATION,
        CLUSTERING,
        QUESTION_ANSWERING,
        FACT_VERIFICATION,
        CODE_RETRIEVAL_QUERY;

    }
}

