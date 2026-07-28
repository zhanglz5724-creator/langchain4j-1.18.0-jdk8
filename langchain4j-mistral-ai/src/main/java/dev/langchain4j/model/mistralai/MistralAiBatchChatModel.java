/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.batch.BatchError
 *  dev.langchain4j.model.batch.BatchItemResult
 *  dev.langchain4j.model.batch.BatchPage
 *  dev.langchain4j.model.batch.BatchPagination
 *  dev.langchain4j.model.batch.BatchRequest
 *  dev.langchain4j.model.batch.BatchResponse
 *  dev.langchain4j.model.batch.BatchState
 *  dev.langchain4j.model.chat.BatchChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.mistralai;

import dev.langchain4j.Experimental;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
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
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.mistralai.InternalMistralAIHelper;
import dev.langchain4j.model.mistralai.MistralAiChatResponseMetadata;
import dev.langchain4j.model.mistralai.internal.api.MistralAiBatchJob;
import dev.langchain4j.model.mistralai.internal.api.MistralAiBatchJobRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiBatchJobsResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiBatchResultEntry;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionResponse;
import dev.langchain4j.model.mistralai.internal.client.MistralAiClient;
import dev.langchain4j.model.mistralai.internal.mapper.MistralAiMapper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Experimental
public final class MistralAiBatchChatModel
implements BatchChatModel {
    private static final String CHAT_COMPLETIONS_ENDPOINT = "/v1/chat/completions";
    private static final String CUSTOM_ID_PREFIX = "request-";
    private static final String STATUS_QUEUED = "QUEUED";
    private static final String STATUS_RUNNING = "RUNNING";
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_FAILED = "FAILED";
    private static final String STATUS_TIMEOUT_EXCEEDED = "TIMEOUT_EXCEEDED";
    private static final String STATUS_CANCELLATION_REQUESTED = "CANCELLATION_REQUESTED";
    private static final String STATUS_CANCELLED = "CANCELLED";
    private final MistralAiClient client;
    private final ChatRequestParameters defaultRequestParameters;
    private final Boolean safePrompt;
    private final Integer randomSeed;
    private final boolean sendThinking;
    private final boolean returnThinking;
    private final boolean strictJsonSchema;
    private final int maxRetries;
    private final Integer timeoutHours;

    public MistralAiBatchChatModel(Builder builder) {
        this.client = ((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)MistralAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.mistral.ai/v1"))).apiKey(builder.apiKey)).timeout(builder.timeout)).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).customHeaders(builder.customHeadersSupplier)).build();
        this.safePrompt = builder.safePrompt;
        this.randomSeed = builder.randomSeed;
        this.sendThinking = (Boolean)Utils.getOrDefault((Object)builder.sendThinking, (Object)false);
        this.returnThinking = (Boolean)Utils.getOrDefault((Object)builder.returnThinking, (Object)false);
        this.strictJsonSchema = (Boolean)Utils.getOrDefault((Object)builder.strictJsonSchema, (Object)false);
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.timeoutHours = builder.timeoutHours;
        this.defaultRequestParameters = MistralAiBatchChatModel.initDefaultRequestParameters(builder);
    }

    private static ChatRequestParameters initDefaultRequestParameters(Builder builder) {
        ChatRequestParameters commonParameters;
        if (builder.defaultRequestParameters != null) {
            InternalMistralAIHelper.validate(builder.defaultRequestParameters);
            commonParameters = builder.defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        return DefaultChatRequestParameters.builder().modelName((String)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName())).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature())).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP())).frequencyPenalty((Double)Utils.getOrDefault((Object)builder.frequencyPenalty, (Object)commonParameters.frequencyPenalty())).presencePenalty((Double)Utils.getOrDefault((Object)builder.presencePenalty, (Object)commonParameters.presencePenalty())).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxTokens, (Object)commonParameters.maxOutputTokens())).stopSequences(Utils.getOrDefault((List)builder.stopSequences, (List)commonParameters.stopSequences())).toolSpecifications(commonParameters.toolSpecifications()).toolChoice(commonParameters.toolChoice()).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat())).build();
    }

    public BatchResponse<ChatResponse> submit(BatchRequest<ChatRequest> request) {
        List requests = request.requests();
        ArrayList<MistralAiBatchJobRequest.Request> items = new ArrayList<MistralAiBatchJobRequest.Request>();
        for (int i = 0; i < requests.size(); ++i) {
            items.add(new MistralAiBatchJobRequest.Request(CUSTOM_ID_PREFIX + i, this.toMistralAiRequest((ChatRequest)requests.get(i))));
        }
        MistralAiBatchJobRequest jobRequest = MistralAiBatchJobRequest.builder().requests(items).endpoint(CHAT_COMPLETIONS_ENDPOINT).model(this.defaultRequestParameters.modelName()).timeoutHours(this.timeoutHours).build();
        MistralAiBatchJob job = (MistralAiBatchJob)RetryUtils.withRetryMappingExceptions(() -> this.client.createBatchJob(jobRequest), (int)this.maxRetries);
        return this.toBatchResponse(job, Collections.emptyList());
    }

    public BatchResponse<ChatResponse> retrieve(String batchId) {
        MistralAiBatchJob job = (MistralAiBatchJob)RetryUtils.withRetryMappingExceptions(() -> this.client.retrieveBatchJob(batchId), (int)this.maxRetries);
        List<Object> results = Collections.emptyList();
        if (job.outputFile != null || job.errorFile != null) {
            ArrayList<MistralAiBatchResultEntry> entries = new ArrayList<MistralAiBatchResultEntry>();
            if (job.outputFile != null) {
                entries.addAll((Collection)RetryUtils.withRetryMappingExceptions(() -> this.client.downloadBatchResults(job.outputFile), (int)this.maxRetries));
            }
            if (job.errorFile != null) {
                entries.addAll((Collection)RetryUtils.withRetryMappingExceptions(() -> this.client.downloadBatchResults(job.errorFile), (int)this.maxRetries));
            }
            entries.sort(Comparator.comparingInt(this::customIdIndex));
            results = entries.stream().map(this::toBatchItemResult).collect(Collectors.toList());
        }
        return this.toBatchResponse(job, results);
    }

    public void cancel(String batchId) {
        RetryUtils.withRetryMappingExceptions(() -> this.client.cancelBatchJob(batchId), (int)this.maxRetries);
    }

    public BatchPage<ChatResponse> list(@Nullable BatchPagination pagination) {
        Integer pageSize = pagination != null ? pagination.pageSize() : null;
        Integer page = pagination != null && pagination.pageToken() != null ? Integer.valueOf(Integer.parseInt(pagination.pageToken())) : null;
        MistralAiBatchJobsResponse response = (MistralAiBatchJobsResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.listBatchJobs(page, pageSize), (int)this.maxRetries);
        ArrayList<BatchResponse<ChatResponse>> batches = new ArrayList<BatchResponse<ChatResponse>>();
        if (response.data != null) {
            for (MistralAiBatchJob job : response.data) {
                batches.add(this.toBatchResponse(job, Collections.emptyList()));
            }
        }
        int currentPage = page != null ? page : 0;
        String nextPageToken = null;
        if (pageSize != null && pageSize > 0 && response.total != null && (long)(currentPage + 1) * (long)pageSize.intValue() < (long)response.total.intValue()) {
            nextPageToken = String.valueOf(currentPage + 1);
        }
        return new BatchPage(batches, nextPageToken);
    }

    public static Builder builder() {
        return new Builder();
    }

    private MistralAiChatCompletionRequest toMistralAiRequest(ChatRequest chatRequest) {
        ChatRequestParameters merged = this.defaultRequestParameters.overrideWith(chatRequest.parameters());
        ChatRequest effectiveRequest = ChatRequest.builder().messages(chatRequest.messages()).parameters(merged).build();
        return InternalMistralAIHelper.createMistralAiRequest(effectiveRequest, this.safePrompt, this.randomSeed, false, this.sendThinking, this.strictJsonSchema);
    }

    private ChatResponse toChatResponse(MistralAiChatCompletionResponse response) {
        return ChatResponse.builder().aiMessage(MistralAiMapper.aiMessageFrom(response, this.returnThinking)).metadata((ChatResponseMetadata)((MistralAiChatResponseMetadata.Builder)((MistralAiChatResponseMetadata.Builder)((MistralAiChatResponseMetadata.Builder)((MistralAiChatResponseMetadata.Builder)MistralAiChatResponseMetadata.builder().id(response.getId())).modelName(response.getModel())).tokenUsage(MistralAiMapper.tokenUsageFrom(response.getUsage()))).finishReason(MistralAiMapper.finishReasonFrom(response.getChoices().get(0).getFinishReason()))).build()).build();
    }

    private BatchResponse<ChatResponse> toBatchResponse(MistralAiBatchJob job, List<BatchItemResult<ChatResponse>> results) {
        return BatchResponse.builder().batchId(job.id).state(MistralAiBatchChatModel.toBatchState(job.status)).results(results).build();
    }

    private static BatchState toBatchState(@Nullable String status) {
        if (status == null) {
            return BatchState.UNSPECIFIED;
        }
        switch (status) {
            case "QUEUED": {
                return BatchState.PENDING;
            }
            case "RUNNING": 
            case "CANCELLATION_REQUESTED": {
                return BatchState.RUNNING;
            }
            case "SUCCESS": {
                return BatchState.SUCCEEDED;
            }
            case "FAILED": {
                return BatchState.FAILED;
            }
            case "TIMEOUT_EXCEEDED": {
                return BatchState.EXPIRED;
            }
            case "CANCELLED": {
                return BatchState.CANCELLED;
            }
        }
        return BatchState.UNSPECIFIED;
    }

    private BatchItemResult<ChatResponse> toBatchItemResult(MistralAiBatchResultEntry entry) {
        boolean succeeded;
        MistralAiBatchResultEntry.Response response = entry.response;
        boolean bl = succeeded = entry.error == null && response != null && response.body != null && (response.statusCode == null || response.statusCode < 400) && MistralAiBatchChatModel.hasChoices(response.body);
        if (succeeded) {
            return BatchItemResult.success((Object)this.toChatResponse(response.body));
        }
        int code = response != null && response.statusCode != null ? response.statusCode : 0;
        List<Map<String, Object>> details = entry.error != null ? Collections.singletonList(entry.error) : null;
        return BatchItemResult.failure((BatchError)new BatchError(code, MistralAiBatchChatModel.errorMessage(entry), details));
    }

    private static boolean hasChoices(MistralAiChatCompletionResponse body) {
        return body.getChoices() != null && !body.getChoices().isEmpty();
    }

    private static String errorMessage(MistralAiBatchResultEntry entry) {
        if (entry.error != null) {
            Object message = entry.error.get("message");
            return message != null ? message.toString() : entry.error.toString();
        }
        if (entry.response != null && entry.response.statusCode != null && entry.response.statusCode >= 400) {
            return "Request failed with status code " + entry.response.statusCode;
        }
        return "Malformed batch result: no response body or choices";
    }

    private int customIdIndex(MistralAiBatchResultEntry entry) {
        String customId = entry.customId;
        if (customId != null && customId.startsWith(CUSTOM_ID_PREFIX)) {
            try {
                return Integer.parseInt(customId.substring(CUSTOM_ID_PREFIX.length()));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return Integer.MAX_VALUE;
    }

    public static final class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Integer maxTokens;
        private Double frequencyPenalty;
        private Double presencePenalty;
        private List<String> stopSequences;
        private ResponseFormat responseFormat;
        private Boolean safePrompt;
        private Integer randomSeed;
        private Boolean sendThinking;
        private Boolean returnThinking;
        private Boolean strictJsonSchema;
        private Integer timeoutHours;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Supplier<Map<String, String>> customHeadersSupplier;
        private ChatRequestParameters defaultRequestParameters;

        private Builder() {
        }

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
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

        public Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public Builder presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public Builder stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return this;
        }

        public Builder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public Builder safePrompt(Boolean safePrompt) {
            this.safePrompt = safePrompt;
            return this;
        }

        public Builder randomSeed(Integer randomSeed) {
            this.randomSeed = randomSeed;
            return this;
        }

        public Builder sendThinking(Boolean sendThinking) {
            this.sendThinking = sendThinking;
            return this;
        }

        public Builder returnThinking(Boolean returnThinking) {
            this.returnThinking = returnThinking;
            return this;
        }

        public Builder strictJsonSchema(Boolean strictJsonSchema) {
            this.strictJsonSchema = strictJsonSchema;
            return this;
        }

        public Builder timeoutHours(Integer timeoutHours) {
            this.timeoutHours = timeoutHours;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public Builder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public Builder defaultRequestParameters(ChatRequestParameters defaultRequestParameters) {
            this.defaultRequestParameters = defaultRequestParameters;
            return this;
        }

        public MistralAiBatchChatModel build() {
            return new MistralAiBatchChatModel(this);
        }
    }
}

