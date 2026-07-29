/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.batch.BatchError
 *  dev.langchain4j.model.batch.BatchItemResult
 *  dev.langchain4j.model.batch.BatchPage
 *  dev.langchain4j.model.batch.BatchPagination
 *  dev.langchain4j.model.batch.BatchResponse
 *  dev.langchain4j.model.batch.BatchState
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.batch.BatchError;
import dev.langchain4j.model.batch.BatchItemResult;
import dev.langchain4j.model.batch.BatchPage;
import dev.langchain4j.model.batch.BatchPagination;
import dev.langchain4j.model.batch.BatchResponse;
import dev.langchain4j.model.batch.BatchState;
import dev.langchain4j.model.googleai.BatchRequestResponse;
import dev.langchain4j.model.googleai.GeminiFiles;
import dev.langchain4j.model.googleai.GeminiService;
import dev.langchain4j.model.googleai.jsonl.JsonLinesWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;

@Experimental
final class GeminiBatchProcessor<REQUEST, RESPONSE, API_REQUEST, API_RESPONSE> {
    private final GeminiService geminiService;
    private final RequestPreparer<REQUEST, API_REQUEST, API_RESPONSE, RESPONSE> preparer;

    GeminiBatchProcessor(GeminiService geminiService, RequestPreparer<REQUEST, API_REQUEST, API_RESPONSE, RESPONSE> preparer) {
        this.geminiService = geminiService;
        this.preparer = preparer;
    }

    BatchResponse<RESPONSE> createBatch(String displayName, Long priority, List<REQUEST> requests, String modelName, GeminiService.BatchOperationType operationType) {
        List<BatchRequestResponse.BatchCreateRequest.InlinedRequest<Object>> inlineRequests = requests.stream().map(this.preparer::prepareRequest).map(this.preparer::createInlinedRequest).map(request -> new BatchRequestResponse.BatchCreateRequest.InlinedRequest<Object>(request, Collections.emptyMap())).collect(Collectors.toList());
        BatchRequestResponse.BatchCreateRequest request2 = new BatchRequestResponse.BatchCreateRequest(new BatchRequestResponse.BatchCreateRequest.Batch(displayName, new BatchRequestResponse.BatchCreateRequest.InputConfig(new BatchRequestResponse.BatchCreateRequest.Requests(inlineRequests)), (Long)Utils.getOrDefault((Object)priority, (Object)0L)));
        return this.processResponse(this.geminiService.batchCreate(modelName, request2, operationType));
    }

    BatchResponse<RESPONSE> createBatchFromFile(String displayName, GeminiFiles.GeminiFile file, String modelName, GeminiService.BatchOperationType operationType) {
        return this.processResponse(this.geminiService.batchCreate(modelName, new BatchRequestResponse.BatchCreateFileRequest(new BatchRequestResponse.BatchCreateFileRequest.FileBatch(displayName, new BatchRequestResponse.BatchCreateFileRequest.FileInputConfig(file.name()))), operationType));
    }

    void writeBatch(JsonLinesWriter writer, Iterable<BatchRequestResponse.BatchFileRequest<REQUEST>> requests) throws IOException {
        for (BatchRequestResponse.BatchFileRequest<REQUEST> request : requests) {
            REQUEST preparedRequest = this.preparer.prepareRequest(request.request());
            API_REQUEST inlinedRequest = this.preparer.createInlinedRequest(preparedRequest);
            writer.write(new BatchRequestResponse.BatchFileRequest<API_REQUEST>(request.key(), inlinedRequest));
        }
    }

    BatchResponse<RESPONSE> retrieveBatchResults(String batchId) {
        BatchRequestResponse.Operation operation = this.geminiService.batchRetrieveBatch(batchId);
        return this.processResponse(operation);
    }

    void cancelBatchJob(String batchId) {
        this.geminiService.batchCancelBatch(batchId);
    }

    void deleteBatchJob(String batchId) {
        this.geminiService.batchDeleteBatch(batchId);
    }

    BatchPage<RESPONSE> listBatchJobs(BatchPagination batchPagination) {
        Integer pageSize = batchPagination != null ? batchPagination.pageSize() : null;
        String pageToken = batchPagination != null ? batchPagination.pageToken() : null;
        BatchRequestResponse.ListOperationsResponse response = this.geminiService.batchListBatches(pageSize, pageToken);
        List<BatchRequestResponse.Operation<API_RESPONSE>> operations = Utils.getOrDefault(response.operations(), Collections.emptyList());
        return new BatchPage(operations.stream().map(this::processResponse).collect(Collectors.toList()), response.nextPageToken());
    }

    private BatchResponse<RESPONSE> processResponse(BatchRequestResponse.Operation<API_RESPONSE> operation) {
        BatchState state = this.extractBatchState(operation.metadata());
        String batchId = operation.name();
        if (operation.done()) {
            BatchRequestResponse.Operation.Status error = operation.error();
            if (error != null) {
                BatchResponse.Builder<RESPONSE> builder = BatchResponse.builder();
                builder.batchId(batchId);
                builder.state(BatchState.FAILED);
                builder.results(Collections.singletonList(BatchItemResult.<RESPONSE>failure((BatchError) error.toGenericStatus())));
                return builder.build();
            }
            List<BatchItemResult<RESPONSE>> results = this.preparer.extractResults(operation.response());
            BatchState finalState = state.isTerminal() ? state : BatchState.SUCCEEDED;
            BatchResponse.Builder<RESPONSE> builder = BatchResponse.builder();
            builder.batchId(batchId);
            builder.state(finalState);
            builder.results(results);
            return builder.build();
        }
        BatchResponse.Builder<RESPONSE> builder = BatchResponse.builder();
        builder.batchId(batchId);
        builder.state(state);
        return builder.build();
    }

    private BatchState extractBatchState(@Nullable Map<String, Object> metadata) {
        if (metadata == null) {
            return BatchState.UNSPECIFIED;
        }
        Object stateObj = metadata.get("state");
        if (stateObj == null) {
            return BatchState.UNSPECIFIED;
        }
        try {
            String stateStr = stateObj.toString();
            if (stateStr.startsWith("BATCH_STATE_")) {
                stateStr = stateStr.substring("BATCH_STATE_".length());
            }
            return BatchState.valueOf((String)stateStr);
        }
        catch (IllegalArgumentException e) {
            return BatchState.UNSPECIFIED;
        }
    }

    static interface RequestPreparer<REQUEST, API_REQUEST, API_RESPONSE, RESPONSE> {
        public REQUEST prepareRequest(REQUEST var1);

        public API_REQUEST createInlinedRequest(REQUEST var1);

        public List<BatchItemResult<RESPONSE>> extractResults(BatchRequestResponse.BatchCreateResponse<API_RESPONSE> var1);
    }
}

