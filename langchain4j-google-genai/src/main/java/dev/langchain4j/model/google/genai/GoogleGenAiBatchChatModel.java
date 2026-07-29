/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.genai.Client
 *  com.google.genai.types.BatchJob
 *  com.google.genai.types.BatchJobDestination
 *  com.google.genai.types.BatchJobSource
 *  com.google.genai.types.CancelBatchJobConfig
 *  com.google.genai.types.Content
 *  com.google.genai.types.CreateBatchJobConfig
 *  com.google.genai.types.DeleteBatchJobConfig
 *  com.google.genai.types.File
 *  com.google.genai.types.GenerateContentConfig
 *  com.google.genai.types.GenerateContentResponse
 *  com.google.genai.types.GetBatchJobConfig
 *  com.google.genai.types.InlinedRequest
 *  com.google.genai.types.InlinedResponse
 *  com.google.genai.types.JobError
 *  com.google.genai.types.JobState
 *  com.google.genai.types.JobState$Known
 *  com.google.genai.types.SafetySetting
 *  dev.langchain4j.Experimental
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
 *  dev.langchain4j.model.chat.BatchChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.response.ChatResponse
 */
package dev.langchain4j.model.google.genai;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.genai.Client;
import com.google.genai.types.BatchJob;
import com.google.genai.types.BatchJobDestination;
import com.google.genai.types.BatchJobSource;
import com.google.genai.types.CancelBatchJobConfig;
import com.google.genai.types.Content;
import com.google.genai.types.CreateBatchJobConfig;
import com.google.genai.types.DeleteBatchJobConfig;
import com.google.genai.types.File;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GetBatchJobConfig;
import com.google.genai.types.InlinedRequest;
import com.google.genai.types.InlinedResponse;
import com.google.genai.types.JobError;
import com.google.genai.types.JobState;
import com.google.genai.types.SafetySetting;
import dev.langchain4j.Experimental;
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
import dev.langchain4j.model.chat.BatchChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.google.genai.GoogleGenAiBatchUtils;
import dev.langchain4j.model.google.genai.GoogleGenAiClientFactory;
import dev.langchain4j.model.google.genai.GoogleGenAiConfigBuilder;
import dev.langchain4j.model.google.genai.GoogleGenAiContentMapper;
import dev.langchain4j.model.google.genai.GoogleGenAiExceptionMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Experimental
public final class GoogleGenAiBatchChatModel
implements BatchChatModel {
    private final Client client;
    private final String modelName;
    private final Integer maxRetries;
    private final List<SafetySetting> safetySettings;
    private final Integer thinkingBudget;
    private final String thinkingLevel;
    private final Integer seed;
    private final boolean googleSearchEnabled;
    private final boolean googleMapsEnabled;
    private final boolean urlContextEnabled;
    private final List<String> allowedFunctionNames;
    private final String vertexSearchDatastore;
    private final Map<String, String> labels;
    private final String cachedContent;
    private final ChatRequestParameters defaultRequestParameters;

    private GoogleGenAiBatchChatModel(Builder builder) {
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)3);
        this.safetySettings = Utils.copy((List)builder.safetySettings);
        this.thinkingBudget = builder.thinkingBudget;
        this.thinkingLevel = builder.thinkingLevel;
        this.seed = builder.seed;
        this.googleSearchEnabled = (Boolean)Utils.getOrDefault((Object)builder.googleSearch, (Object)false);
        this.googleMapsEnabled = (Boolean)Utils.getOrDefault((Object)builder.googleMaps, (Object)false);
        this.urlContextEnabled = (Boolean)Utils.getOrDefault((Object)builder.urlContext, (Object)false);
        this.allowedFunctionNames = Utils.copy((List)builder.allowedFunctionNames);
        this.vertexSearchDatastore = builder.vertexSearchDatastore;
        this.labels = builder.labels != null ? new HashMap(builder.labels) : null;
        this.cachedContent = builder.cachedContent;
        this.defaultRequestParameters = builder.defaultRequestParameters;
        this.client = builder.client != null ? builder.client : GoogleGenAiClientFactory.createClient(builder.apiKey, builder.googleCredentials, builder.projectId, builder.location, builder.timeout, builder.customHeaders, builder.apiEndpoint);
    }

    public static Builder builder() {
        return new Builder();
    }

    public BatchResponse<ChatResponse> submit(BatchRequest<ChatRequest> request) {
        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withZone(ZoneId.systemDefault()).format(Instant.now());
        return this.submit("batch-chat-job-" + timestamp, request.requests());
    }

    public BatchResponse<ChatResponse> retrieve(String batchId) {
        BatchJob batchJob = this.client.batches.get(batchId, GetBatchJobConfig.builder().build());
        return this.processResponse(batchJob);
    }

    public void cancel(String batchId) {
        this.client.batches.cancel(batchId, CancelBatchJobConfig.builder().build());
    }

    public BatchPage<ChatResponse> list(BatchPagination pagination) {
        Integer pageSize = pagination != null ? pagination.pageSize() : null;
        String pageToken = pagination != null ? pagination.pageToken() : null;
        return GoogleGenAiBatchUtils.listBatchJobs(this.client, pageSize, pageToken, this::processResponse);
    }

    public BatchResponse<ChatResponse> submit(String displayName, List<ChatRequest> requests) {
        GoogleGenAiBatchChatModel.validateModelInChatRequests(this.modelName, requests);
        List inlinedRequests = requests.stream().map(this::createInlinedRequest).collect(Collectors.toList());
        BatchJobSource src = BatchJobSource.builder().inlinedRequests(inlinedRequests).build();
        CreateBatchJobConfig config = CreateBatchJobConfig.builder().displayName(displayName).build();
        BatchJob batchJob = (BatchJob)RetryUtils.withRetryMappingExceptions(() -> this.client.batches.create(this.modelName, src, config), (int)this.maxRetries, (ExceptionMapper)GoogleGenAiExceptionMapper.INSTANCE);
        return this.processResponse(batchJob);
    }

    public BatchResponse<ChatResponse> submit(String displayName, File file) {
        BatchJobSource src = BatchJobSource.builder().fileName(file.name().isPresent() ? (String)file.name().get() : null).build();
        CreateBatchJobConfig config = CreateBatchJobConfig.builder().displayName(displayName).build();
        BatchJob batchJob = (BatchJob)RetryUtils.withRetryMappingExceptions(() -> this.client.batches.create(this.modelName, src, config), (int)this.maxRetries, (ExceptionMapper)GoogleGenAiExceptionMapper.INSTANCE);
        return this.processResponse(batchJob);
    }

    public void deleteBatchJob(String batchId) {
        this.client.batches.delete(batchId, DeleteBatchJobConfig.builder().build());
    }

    private InlinedRequest createInlinedRequest(ChatRequest request) {
        Content systemInstruction = GoogleGenAiContentMapper.toSystemInstruction(request.messages());
        List<Content> contents = GoogleGenAiContentMapper.toContents(request.messages());
        ChatRequestParameters params = this.defaultRequestParameters != null ? this.defaultRequestParameters.overrideWith(request.parameters()) : request.parameters();
        GenerateContentConfig config = GoogleGenAiConfigBuilder.buildConfig(params, systemInstruction, this.safetySettings, this.thinkingBudget, this.thinkingLevel, this.seed, this.googleSearchEnabled, this.googleMapsEnabled, this.urlContextEnabled, this.allowedFunctionNames, this.vertexSearchDatastore, this.labels, this.cachedContent);
        return InlinedRequest.builder().contents(contents).config(config).build();
    }

    private BatchResponse<ChatResponse> processResponse(BatchJob batchJob) {
        String jobName = batchJob.name().orElse("unknown");
        JobState.Known state = batchJob.state().map(JobState::knownEnum).orElse(JobState.Known.JOB_STATE_UNSPECIFIED);
        BatchState translatedState = GoogleGenAiBatchUtils.toBatchState(state);
        BatchResponse.Builder builder = BatchResponse.builder().batchId(jobName).state(translatedState);
        if (state == JobState.Known.JOB_STATE_SUCCEEDED) {
            ArrayList<BatchItemResult<ChatResponse>> results = new ArrayList<>();
            if (batchJob.dest().isPresent() && ((BatchJobDestination)batchJob.dest().get()).inlinedResponses().isPresent()) {
                List<InlinedResponse> inlinedResponses = ((BatchJobDestination)batchJob.dest().get()).inlinedResponses().get();
                for (InlinedResponse inlined : inlinedResponses) {
                    if (inlined.response().isPresent()) {
                        results.add(BatchItemResult.success(GoogleGenAiContentMapper.toChatResponse((GenerateContentResponse) inlined.response().get(), batchJob.model().orElse(this.modelName))));
                        continue;
                    }
                    if (!inlined.error().isPresent()) continue;
                    results.add(BatchItemResult.<ChatResponse>failure(GoogleGenAiBatchUtils.toBatchError(inlined.error().get())));
                }
            }
            builder.results(results);
        } else if (state == JobState.Known.JOB_STATE_FAILED) {
            builder.results(Collections.singletonList(BatchItemResult.<ChatResponse>failure(GoogleGenAiBatchUtils.toBatchError(batchJob.error().orElse(null)))));
        }
        return builder.build();
    }

    private static void validateModelInChatRequests(String modelName, List<ChatRequest> requests) {
        Set modelNames = Stream.concat(requests.stream().map(ChatRequest::modelName), Stream.of(modelName)).filter(Objects::nonNull).collect(Collectors.toSet());
        if (modelNames.size() != 1) {
            throw new IllegalArgumentException("Batch requests cannot contain ChatRequest objects with different models; all requests must use the same model: " + modelNames);
        }
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
        private Integer thinkingBudget;
        private String thinkingLevel;
        private Integer seed;
        private Boolean googleSearch;
        private Boolean googleMaps;
        private Boolean urlContext;
        private List<SafetySetting> safetySettings;
        private List<String> allowedFunctionNames;
        private ChatRequestParameters defaultRequestParameters;
        private String vertexSearchDatastore;
        private Map<String, String> labels;
        private String apiEndpoint;
        private Map<String, String> customHeaders;
        private String cachedContent;

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

        public Builder thinkingBudget(Integer thinkingBudget) {
            this.thinkingBudget = thinkingBudget;
            return this;
        }

        public Builder thinkingLevel(String thinkingLevel) {
            this.thinkingLevel = thinkingLevel;
            return this;
        }

        public Builder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public Builder googleSearch(Boolean googleSearch) {
            this.googleSearch = googleSearch;
            return this;
        }

        public Builder googleMaps(Boolean googleMaps) {
            this.googleMaps = googleMaps;
            return this;
        }

        public Builder urlContext(Boolean urlContext) {
            this.urlContext = urlContext;
            return this;
        }

        public Builder safetySettings(List<SafetySetting> safetySettings) {
            this.safetySettings = safetySettings;
            return this;
        }

        public Builder allowedFunctionNames(List<String> allowedFunctionNames) {
            this.allowedFunctionNames = allowedFunctionNames;
            return this;
        }

        public Builder defaultRequestParameters(ChatRequestParameters defaultRequestParameters) {
            this.defaultRequestParameters = defaultRequestParameters;
            return this;
        }

        public Builder vertexSearchDatastore(String vertexSearchDatastore) {
            this.vertexSearchDatastore = vertexSearchDatastore;
            return this;
        }

        public Builder labels(Map<String, String> labels) {
            this.labels = labels;
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

        public Builder cachedContent(String cachedContent) {
            this.cachedContent = cachedContent;
            return this;
        }

        public GoogleGenAiBatchChatModel build() {
            return new GoogleGenAiBatchChatModel(this);
        }
    }
}

