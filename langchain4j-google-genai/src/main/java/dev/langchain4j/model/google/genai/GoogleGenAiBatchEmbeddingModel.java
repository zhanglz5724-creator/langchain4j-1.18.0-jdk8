/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.genai.Client
 *  com.google.genai.types.BatchJob
 *  com.google.genai.types.BatchJobDestination
 *  com.google.genai.types.CancelBatchJobConfig
 *  com.google.genai.types.Content
 *  com.google.genai.types.ContentEmbedding
 *  com.google.genai.types.CreateEmbeddingsBatchJobConfig
 *  com.google.genai.types.DeleteBatchJobConfig
 *  com.google.genai.types.EmbedContentBatch
 *  com.google.genai.types.EmbedContentConfig
 *  com.google.genai.types.EmbedContentConfig$Builder
 *  com.google.genai.types.EmbeddingsBatchJobSource
 *  com.google.genai.types.File
 *  com.google.genai.types.GetBatchJobConfig
 *  com.google.genai.types.InlinedEmbedContentResponse
 *  com.google.genai.types.JobError
 *  com.google.genai.types.JobState
 *  com.google.genai.types.JobState$Known
 *  com.google.genai.types.Part
 *  com.google.genai.types.SingleEmbedContentResponse
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.batch.BatchError
 *  dev.langchain4j.model.batch.BatchItemResult
 *  dev.langchain4j.model.batch.BatchPage
 *  dev.langchain4j.model.batch.BatchPagination
 *  dev.langchain4j.model.batch.BatchRequest
 *  dev.langchain4j.model.batch.BatchResponse
 *  dev.langchain4j.model.batch.BatchResponse$Builder
 *  dev.langchain4j.model.batch.BatchState
 *  dev.langchain4j.model.embedding.BatchEmbeddingModel
 *  dev.langchain4j.model.output.Response
 */
package dev.langchain4j.model.google.genai;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.genai.Client;
import com.google.genai.types.BatchJob;
import com.google.genai.types.BatchJobDestination;
import com.google.genai.types.CancelBatchJobConfig;
import com.google.genai.types.Content;
import com.google.genai.types.ContentEmbedding;
import com.google.genai.types.CreateEmbeddingsBatchJobConfig;
import com.google.genai.types.DeleteBatchJobConfig;
import com.google.genai.types.EmbedContentBatch;
import com.google.genai.types.EmbedContentConfig;
import com.google.genai.types.EmbeddingsBatchJobSource;
import com.google.genai.types.File;
import com.google.genai.types.GetBatchJobConfig;
import com.google.genai.types.InlinedEmbedContentResponse;
import com.google.genai.types.JobError;
import com.google.genai.types.JobState;
import com.google.genai.types.Part;
import com.google.genai.types.SingleEmbedContentResponse;
import dev.langchain4j.Experimental;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.batch.BatchError;
import dev.langchain4j.model.batch.BatchItemResult;
import dev.langchain4j.model.batch.BatchPage;
import dev.langchain4j.model.batch.BatchPagination;
import dev.langchain4j.model.batch.BatchRequest;
import dev.langchain4j.model.batch.BatchResponse;
import dev.langchain4j.model.batch.BatchState;
import dev.langchain4j.model.embedding.BatchEmbeddingModel;
import dev.langchain4j.model.google.genai.GoogleGenAiBatchUtils;
import dev.langchain4j.model.google.genai.GoogleGenAiClientFactory;
import dev.langchain4j.model.google.genai.GoogleGenAiEmbeddingModel;
import dev.langchain4j.model.google.genai.GoogleGenAiExceptionMapper;
import dev.langchain4j.model.output.Response;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Experimental
public final class GoogleGenAiBatchEmbeddingModel
implements BatchEmbeddingModel {
    private final Client client;
    private final String modelName;
    private final Integer maxRetries;
    private final Integer outputDimensionality;
    private final GoogleGenAiEmbeddingModel.TaskTypeEnum taskType;
    private final String titleMetadataKey;

    private GoogleGenAiBatchEmbeddingModel(Builder builder) {
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)3);
        this.outputDimensionality = builder.outputDimensionality;
        this.taskType = builder.taskType;
        this.titleMetadataKey = builder.titleMetadataKey;
        this.client = builder.client != null ? builder.client : GoogleGenAiClientFactory.createClient(builder.apiKey, builder.googleCredentials, builder.projectId, builder.location, builder.timeout, builder.customHeaders, builder.apiEndpoint);
    }

    public static Builder builder() {
        return new Builder();
    }

    public BatchResponse<Response<Embedding>> submit(BatchRequest<TextSegment> request) {
        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withZone(ZoneId.systemDefault()).format(Instant.now());
        return this.submit("batch-embedding-job-" + timestamp, request.requests());
    }

    public BatchResponse<Response<Embedding>> retrieve(String batchId) {
        BatchJob batchJob = this.client.batches.get(batchId, GetBatchJobConfig.builder().build());
        return this.processResponse(batchJob);
    }

    public void cancel(String batchId) {
        this.client.batches.cancel(batchId, CancelBatchJobConfig.builder().build());
    }

    public BatchPage<Response<Embedding>> list(BatchPagination pagination) {
        Integer pageSize = pagination != null ? pagination.pageSize() : null;
        String pageToken = pagination != null ? pagination.pageToken() : null;
        return GoogleGenAiBatchUtils.listBatchJobs(this.client, pageSize, pageToken, this::processResponse);
    }

    public BatchResponse<Response<Embedding>> submit(String displayName, List<TextSegment> requests) {
        List contents = requests.stream().map(segment -> Content.builder().parts(Collections.singletonList(Part.builder().text(segment.text()).build())).build()).collect(Collectors.toList());
        EmbedContentConfig.Builder configBuilder = EmbedContentConfig.builder();
        if (this.outputDimensionality != null) {
            configBuilder.outputDimensionality(this.outputDimensionality);
        }
        if (this.taskType != null) {
            configBuilder.taskType(this.taskType.getSdkTaskType());
        }
        EmbedContentBatch inlinedRequests = EmbedContentBatch.builder().contents(contents).config(configBuilder.build()).build();
        EmbeddingsBatchJobSource src = EmbeddingsBatchJobSource.builder().inlinedRequests(inlinedRequests).build();
        CreateEmbeddingsBatchJobConfig config = CreateEmbeddingsBatchJobConfig.builder().displayName(displayName).build();
        BatchJob batchJob = (BatchJob)RetryUtils.withRetryMappingExceptions(() -> this.client.batches.createEmbeddings(this.modelName, src, config), (int)this.maxRetries, (ExceptionMapper)GoogleGenAiExceptionMapper.INSTANCE);
        return this.processResponse(batchJob);
    }

    public BatchResponse<Response<Embedding>> submit(String displayName, File file) {
        EmbeddingsBatchJobSource src = EmbeddingsBatchJobSource.builder().fileName(file.name().isPresent() ? (String)file.name().get() : null).build();
        CreateEmbeddingsBatchJobConfig config = CreateEmbeddingsBatchJobConfig.builder().displayName(displayName).build();
        BatchJob batchJob = (BatchJob)RetryUtils.withRetryMappingExceptions(() -> this.client.batches.createEmbeddings(this.modelName, src, config), (int)this.maxRetries, (ExceptionMapper)GoogleGenAiExceptionMapper.INSTANCE);
        return this.processResponse(batchJob);
    }

    public void deleteBatchJob(String batchId) {
        this.client.batches.delete(batchId, DeleteBatchJobConfig.builder().build());
    }

    private BatchResponse<Response<Embedding>> processResponse(BatchJob batchJob) {
        String jobName = batchJob.name().orElse("unknown");
        JobState.Known state = batchJob.state().map(JobState::knownEnum).orElse(JobState.Known.JOB_STATE_UNSPECIFIED);
        BatchState translatedState = GoogleGenAiBatchUtils.toBatchState(state);
        BatchResponse.Builder builder = BatchResponse.builder().batchId(jobName).state(translatedState);
        if (state == JobState.Known.JOB_STATE_SUCCEEDED) {
            ArrayList<BatchItemResult<Response<Embedding>>> results = new ArrayList<>();
            if (batchJob.dest().isPresent() && ((BatchJobDestination)batchJob.dest().get()).inlinedEmbedContentResponses().isPresent()) {
                List<InlinedEmbedContentResponse> inlinedResponses = ((BatchJobDestination)batchJob.dest().get()).inlinedEmbedContentResponses().get();
                for (InlinedEmbedContentResponse inlined : inlinedResponses) {
                    if (inlined.response().isPresent()) {
                        Optional<ContentEmbedding> embeddingOpt = ((SingleEmbedContentResponse)inlined.response().get()).embedding();
                        if (!embeddingOpt.isPresent() || !embeddingOpt.get().values().isPresent()) continue;
                        List<Float> values = (List<Float>) embeddingOpt.get().values().get();
                        float[] floatArray = new float[values.size()];
                        for (int i = 0; i < values.size(); ++i) {
                            floatArray[i] = values.get(i).floatValue();
                        }
                        results.add(BatchItemResult.success(Response.from(Embedding.from(floatArray))));
                        continue;
                    }
                    if (!inlined.error().isPresent()) continue;
                    results.add(BatchItemResult.<Response<Embedding>>failure(GoogleGenAiBatchUtils.toBatchError(inlined.error().get())));
                }
            }
            builder.results(results);
        } else if (state == JobState.Known.JOB_STATE_FAILED) {
            builder.results(Collections.singletonList(BatchItemResult.<Response<Embedding>>failure(GoogleGenAiBatchUtils.toBatchError(batchJob.error().orElse(null)))));
        }
        return builder.build();
    }

    public static class Builder {
        private Client client;
        private GoogleCredentials googleCredentials;
        private String apiKey;
        private String projectId;
        private String location;
        private String modelName;
        private Integer maxRetries;
        private Duration timeout;
        private Integer outputDimensionality;
        private GoogleGenAiEmbeddingModel.TaskTypeEnum taskType;
        private String titleMetadataKey;
        private String apiEndpoint;
        private Map<String, String> customHeaders;

        public Builder client(Client client) {
            this.client = client;
            return this;
        }

        public Builder googleCredentials(GoogleCredentials credentials) {
            this.googleCredentials = credentials;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder outputDimensionality(Integer outputDimensionality) {
            this.outputDimensionality = outputDimensionality;
            return this;
        }

        public Builder taskType(GoogleGenAiEmbeddingModel.TaskTypeEnum taskType) {
            this.taskType = taskType;
            return this;
        }

        public Builder titleMetadataKey(String titleMetadataKey) {
            this.titleMetadataKey = titleMetadataKey;
            return this;
        }

        public Builder apiEndpoint(String apiEndpoint) {
            this.apiEndpoint = apiEndpoint;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public GoogleGenAiBatchEmbeddingModel build() {
            return new GoogleGenAiBatchEmbeddingModel(this);
        }
    }
}

