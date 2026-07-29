/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.genai.Client
 *  com.google.genai.types.BatchJob
 *  com.google.genai.types.BatchJobDestination
 *  com.google.genai.types.BatchJobSource
 *  com.google.genai.types.Blob
 *  com.google.genai.types.CancelBatchJobConfig
 *  com.google.genai.types.Content
 *  com.google.genai.types.CreateBatchJobConfig
 *  com.google.genai.types.DeleteBatchJobConfig
 *  com.google.genai.types.File
 *  com.google.genai.types.GenerateContentConfig
 *  com.google.genai.types.GenerateContentConfig$Builder
 *  com.google.genai.types.GenerateContentResponse
 *  com.google.genai.types.GetBatchJobConfig
 *  com.google.genai.types.ImageConfig
 *  com.google.genai.types.ImageConfig$Builder
 *  com.google.genai.types.InlinedRequest
 *  com.google.genai.types.InlinedResponse
 *  com.google.genai.types.JobError
 *  com.google.genai.types.JobState
 *  com.google.genai.types.JobState$Known
 *  com.google.genai.types.Part
 *  com.google.genai.types.SafetySetting
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.image.Image
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
 *  dev.langchain4j.model.image.BatchImageModel
 *  dev.langchain4j.model.output.Response
 */
package dev.langchain4j.model.google.genai;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.genai.Client;
import com.google.genai.types.BatchJob;
import com.google.genai.types.BatchJobDestination;
import com.google.genai.types.BatchJobSource;
import com.google.genai.types.Blob;
import com.google.genai.types.CancelBatchJobConfig;
import com.google.genai.types.Content;
import com.google.genai.types.CreateBatchJobConfig;
import com.google.genai.types.DeleteBatchJobConfig;
import com.google.genai.types.File;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GetBatchJobConfig;
import com.google.genai.types.ImageConfig;
import com.google.genai.types.InlinedRequest;
import com.google.genai.types.InlinedResponse;
import com.google.genai.types.JobError;
import com.google.genai.types.JobState;
import com.google.genai.types.Part;
import com.google.genai.types.SafetySetting;
import dev.langchain4j.Experimental;
import dev.langchain4j.data.image.Image;
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
import dev.langchain4j.model.google.genai.GoogleGenAiBatchUtils;
import dev.langchain4j.model.google.genai.GoogleGenAiClientFactory;
import dev.langchain4j.model.google.genai.GoogleGenAiExceptionMapper;
import dev.langchain4j.model.image.BatchImageModel;
import dev.langchain4j.model.output.Response;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Experimental
public final class GoogleGenAiBatchImageModel
implements BatchImageModel {
    private final Client client;
    private final String modelName;
    private final Integer maxRetries;
    private final List<SafetySetting> safetySettings;
    private final String aspectRatio;
    private final String imageSize;
    private final String personGeneration;
    private final Map<String, String> labels;

    private GoogleGenAiBatchImageModel(Builder builder) {
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)3);
        this.safetySettings = builder.safetySettings != null ? new ArrayList(builder.safetySettings) : null;
        this.aspectRatio = builder.aspectRatio;
        this.imageSize = builder.imageSize;
        this.personGeneration = builder.personGeneration;
        this.labels = builder.labels != null ? new HashMap(builder.labels) : null;
        this.client = builder.client != null ? builder.client : GoogleGenAiClientFactory.createClient(builder.apiKey, builder.googleCredentials, builder.projectId, builder.location, builder.timeout, builder.customHeaders, builder.apiEndpoint);
    }

    public static Builder builder() {
        return new Builder();
    }

    public BatchResponse<Response<Image>> submit(BatchRequest<String> request) {
        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withZone(ZoneId.systemDefault()).format(Instant.now());
        return this.submit("batch-image-job-" + timestamp, request.requests());
    }

    public BatchResponse<Response<Image>> retrieve(String batchId) {
        BatchJob batchJob = this.client.batches.get(batchId, GetBatchJobConfig.builder().build());
        return this.processResponse(batchJob);
    }

    public void cancel(String batchId) {
        this.client.batches.cancel(batchId, CancelBatchJobConfig.builder().build());
    }

    public BatchPage<Response<Image>> list(BatchPagination pagination) {
        Integer pageSize = pagination != null ? pagination.pageSize() : null;
        String pageToken = pagination != null ? pagination.pageToken() : null;
        return GoogleGenAiBatchUtils.listBatchJobs(this.client, pageSize, pageToken, this::processResponse);
    }

    public BatchResponse<Response<Image>> submit(String displayName, List<String> prompts) {
        List inlinedRequests = prompts.stream().map(prompt -> this.createInlinedRequest(new ImageGenerationRequest((String)prompt))).collect(Collectors.toList());
        BatchJobSource src = BatchJobSource.builder().inlinedRequests(inlinedRequests).build();
        CreateBatchJobConfig config = CreateBatchJobConfig.builder().displayName(displayName).build();
        BatchJob batchJob = (BatchJob)RetryUtils.withRetryMappingExceptions(() -> this.client.batches.create(this.modelName, src, config), (int)this.maxRetries, (ExceptionMapper)GoogleGenAiExceptionMapper.INSTANCE);
        return this.processResponse(batchJob);
    }

    public BatchResponse<Response<Image>> submit(String displayName, File file) {
        BatchJobSource src = BatchJobSource.builder().fileName(file.name().isPresent() ? (String)file.name().get() : null).build();
        CreateBatchJobConfig config = CreateBatchJobConfig.builder().displayName(displayName).build();
        BatchJob batchJob = (BatchJob)RetryUtils.withRetryMappingExceptions(() -> this.client.batches.create(this.modelName, src, config), (int)this.maxRetries, (ExceptionMapper)GoogleGenAiExceptionMapper.INSTANCE);
        return this.processResponse(batchJob);
    }

    public void deleteBatchJob(String batchId) {
        this.client.batches.delete(batchId, DeleteBatchJobConfig.builder().build());
    }

    private InlinedRequest createInlinedRequest(ImageGenerationRequest request) {
        Content content = Content.builder().parts(Collections.singletonList(Part.fromText((String)request.prompt()))).build();
        GenerateContentConfig.Builder configBuilder = GenerateContentConfig.builder().responseModalities(Collections.singletonList("IMAGE"));
        if (this.safetySettings != null && !this.safetySettings.isEmpty()) {
            configBuilder.safetySettings(this.safetySettings);
        }
        if (this.aspectRatio != null || this.imageSize != null || this.personGeneration != null) {
            ImageConfig.Builder imageConfigBuilder = ImageConfig.builder();
            if (this.aspectRatio != null) {
                imageConfigBuilder.aspectRatio(this.aspectRatio);
            }
            if (this.imageSize != null) {
                imageConfigBuilder.imageSize(this.imageSize);
            }
            if (this.personGeneration != null) {
                imageConfigBuilder.personGeneration(this.personGeneration);
            }
            configBuilder.imageConfig(imageConfigBuilder.build());
        }
        if (this.labels != null && !this.labels.isEmpty()) {
            configBuilder.labels(this.labels);
        }
        return InlinedRequest.builder().contents(Collections.singletonList(content)).config(configBuilder.build()).build();
    }

    private BatchResponse<Response<Image>> processResponse(BatchJob batchJob) {
        String jobName = batchJob.name().orElse("unknown");
        JobState.Known state = batchJob.state().map(JobState::knownEnum).orElse(JobState.Known.JOB_STATE_UNSPECIFIED);
        BatchState translatedState = GoogleGenAiBatchUtils.toBatchState(state);
        BatchResponse.Builder builder = BatchResponse.builder().batchId(jobName).state(translatedState);
        if (state == JobState.Known.JOB_STATE_SUCCEEDED) {
            ArrayList<BatchItemResult> results = new ArrayList<BatchItemResult>();
            if (batchJob.dest().isPresent() && ((BatchJobDestination)batchJob.dest().get()).inlinedResponses().isPresent()) {
                List inlinedResponses = (List)((BatchJobDestination)batchJob.dest().get()).inlinedResponses().get();
                for (InlinedResponse inlined : inlinedResponses) {
                    if (inlined.response().isPresent()) {
                        GenerateContentResponse response = (GenerateContentResponse)inlined.response().get();
                        boolean imageAdded = false;
                        if (response.parts() != null && !response.parts().isEmpty()) {
                            for (Part part : response.parts()) {
                                Blob blob;
                                if (!part.inlineData().isPresent() || !(blob = (Blob)part.inlineData().get()).data().isPresent()) continue;
                                byte[] bytes = (byte[])blob.data().get();
                                String base64Data = Base64.getEncoder().encodeToString(bytes);
                                String mimeType = blob.mimeType().orElse("image/png");
                                Image image = Image.builder().base64Data(base64Data).mimeType(mimeType).build();
                                results.add(BatchItemResult.success((Object)Response.from(image)));
                                imageAdded = true;
                                break;
                            }
                        }
                        if (imageAdded) continue;
                        results.add(BatchItemResult.failure((BatchError)new BatchError(0, "No image data found in response", new ArrayList())));
                        continue;
                    }
                    if (!inlined.error().isPresent()) continue;
                    results.add(BatchItemResult.failure((BatchError)GoogleGenAiBatchUtils.toBatchError((JobError)inlined.error().get())));
                }
            }
            builder.results(results);
        } else if (state == JobState.Known.JOB_STATE_FAILED) {
            builder.results(Collections.singletonList(BatchItemResult.failure((BatchError)GoogleGenAiBatchUtils.toBatchError(batchJob.error().orElse(null)))));
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
        private String aspectRatio;
        private String imageSize;
        private String personGeneration;
        private List<SafetySetting> safetySettings;
        private String apiEndpoint;
        private Map<String, String> customHeaders;
        private Map<String, String> labels;

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
            this.modelName = modelName;
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

        public Builder aspectRatio(String aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        public Builder imageSize(String imageSize) {
            this.imageSize = imageSize;
            return this;
        }

        public Builder personGeneration(String personGeneration) {
            this.personGeneration = personGeneration;
            return this;
        }

        public Builder safetySettings(List<SafetySetting> safetySettings) {
            this.safetySettings = safetySettings;
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

        public Builder labels(Map<String, String> labels) {
            this.labels = labels;
            return this;
        }

        public GoogleGenAiBatchImageModel build() {
            return new GoogleGenAiBatchImageModel(this);
        }
    }

    public static final class ImageGenerationRequest {
        private final String prompt;

        public ImageGenerationRequest(String prompt) {
            ValidationUtils.ensureNotBlank((String)prompt, (String)"prompt");
            this.prompt = prompt;
        }

        public String prompt() {
            return this.prompt;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ImageGenerationRequest)) {
                return false;
            }
            ImageGenerationRequest that = (ImageGenerationRequest)o;
            return Objects.equals(this.prompt, that.prompt);
        }

        public int hashCode() {
            return Objects.hash(this.prompt);
        }

        public String toString() {
            return "ImageGenerationRequest[prompt=" + this.prompt + "]";
        }
    }
}

