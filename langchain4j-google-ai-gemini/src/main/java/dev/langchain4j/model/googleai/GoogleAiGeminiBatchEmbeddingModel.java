/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.batch.BatchError
 *  dev.langchain4j.model.batch.BatchItemResult
 *  dev.langchain4j.model.batch.BatchPage
 *  dev.langchain4j.model.batch.BatchPagination
 *  dev.langchain4j.model.batch.BatchRequest
 *  dev.langchain4j.model.batch.BatchResponse
 *  dev.langchain4j.model.embedding.BatchEmbeddingModel
 *  dev.langchain4j.model.output.Response
 *  org.jspecify.annotations.NonNull
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.langchain4j.Experimental;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.batch.BatchError;
import dev.langchain4j.model.batch.BatchItemResult;
import dev.langchain4j.model.batch.BatchPage;
import dev.langchain4j.model.batch.BatchPagination;
import dev.langchain4j.model.batch.BatchRequest;
import dev.langchain4j.model.batch.BatchResponse;
import dev.langchain4j.model.embedding.BatchEmbeddingModel;
import dev.langchain4j.model.googleai.BatchRequestResponse;
import dev.langchain4j.model.googleai.GeminiBatchProcessor;
import dev.langchain4j.model.googleai.GeminiBatchRequest;
import dev.langchain4j.model.googleai.GeminiContent;
import dev.langchain4j.model.googleai.GeminiEmbeddingRequestResponse;
import dev.langchain4j.model.googleai.GeminiFiles;
import dev.langchain4j.model.googleai.GeminiService;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.model.googleai.Json;
import dev.langchain4j.model.googleai.jsonl.JsonLinesWriter;
import dev.langchain4j.model.output.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Experimental
public final class GoogleAiGeminiBatchEmbeddingModel
implements BatchEmbeddingModel {
    private final GeminiBatchProcessor<TextSegment, Response<@NonNull Embedding>, GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest, GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse> batchProcessor;
    private final String modelName;
    private final GoogleAiEmbeddingModel.TaskType taskType;
    private final String titleMetadataKey;
    private final Integer outputDimensionality;
    private final EmbeddingRequestPreparer preparer = new EmbeddingRequestPreparer();

    GoogleAiGeminiBatchEmbeddingModel(Builder builder) {
        this(builder, new GeminiService(builder.httpClientBuilder, builder.apiKey, builder.baseUrl, (Boolean)Utils.getOrDefault((Object)builder.logRequestsAndResponses, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false), builder.logger, builder.timeout, null));
    }

    GoogleAiGeminiBatchEmbeddingModel(Builder builder, GeminiService geminiService) {
        this.batchProcessor = new GeminiBatchProcessor<TextSegment, Response<Embedding>, GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest, GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse>(geminiService, this.preparer);
        this.modelName = builder.modelName;
        this.taskType = builder.taskType;
        this.titleMetadataKey = (String)Utils.getOrDefault((Object)builder.titleMetadataKey, (Object)"title");
        this.outputDimensionality = builder.outputDimensionality;
    }

    public BatchResponse<Response<Embedding>> submit(BatchRequest<TextSegment> request) {
        return this.batchProcessor.createBatch(null, null, request.requests(), this.modelName, GeminiService.BatchOperationType.ASYNC_BATCH_EMBED_CONTENT);
    }

    public BatchResponse<Response<Embedding>> submit(GeminiBatchRequest<TextSegment> request) {
        return this.batchProcessor.createBatch(request.displayName(), request.priority(), request.requests(), this.modelName, GeminiService.BatchOperationType.ASYNC_BATCH_EMBED_CONTENT);
    }

    public BatchResponse<Response<Embedding>> submit(String displayName, GeminiFiles.GeminiFile file) {
        return this.batchProcessor.createBatchFromFile(displayName, file, this.modelName, GeminiService.BatchOperationType.ASYNC_BATCH_EMBED_CONTENT);
    }

    public void writeBatchToFile(JsonLinesWriter writer, Iterable<BatchRequestResponse.BatchFileRequest<TextSegment>> requests) throws IOException {
        this.batchProcessor.writeBatch(writer, requests);
    }

    public BatchResponse<Response<Embedding>> retrieve(String batchId) {
        return this.batchProcessor.retrieveBatchResults(batchId);
    }

    public void cancel(String batchId) {
        this.batchProcessor.cancelBatchJob(batchId);
    }

    public void deleteBatchJob(String batchId) {
        this.batchProcessor.deleteBatchJob(batchId);
    }

    public BatchPage<Response<Embedding>> list(@Nullable BatchPagination batchPagination) {
        return this.batchProcessor.listBatchJobs(batchPagination);
    }

    public static Builder builder() {
        return new Builder();
    }

    private class EmbeddingRequestPreparer
    implements GeminiBatchProcessor.RequestPreparer<TextSegment, GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest, GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse, Response<Embedding>> {
        private final TypeReference<BatchRequestResponse.BatchCreateResponse.InlinedResponseWrapper<GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse>> responseWrapperType = new TypeReference<BatchRequestResponse.BatchCreateResponse.InlinedResponseWrapper<GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse>>(){};

        private EmbeddingRequestPreparer() {
        }

        @Override
        public TextSegment prepareRequest(TextSegment textSegment) {
            return textSegment;
        }

        @Override
        public GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest createInlinedRequest(TextSegment textSegment) {
            GeminiContent.GeminiPart geminiPart = GeminiContent.GeminiPart.builder().text(textSegment.text()).build();
            GeminiContent content = new GeminiContent(Collections.singletonList(geminiPart), null);
            String title = null;
            if (GoogleAiEmbeddingModel.TaskType.RETRIEVAL_DOCUMENT.equals((Object)GoogleAiGeminiBatchEmbeddingModel.this.taskType) && textSegment.metadata() != null && textSegment.metadata().getString(GoogleAiGeminiBatchEmbeddingModel.this.titleMetadataKey) != null) {
                title = textSegment.metadata().getString(GoogleAiGeminiBatchEmbeddingModel.this.titleMetadataKey);
            }
            return new GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest("models/" + GoogleAiGeminiBatchEmbeddingModel.this.modelName, content, GoogleAiGeminiBatchEmbeddingModel.this.taskType, title, GoogleAiGeminiBatchEmbeddingModel.this.outputDimensionality);
        }

        @Override
        public List<BatchItemResult<Response<Embedding>>> extractResults(BatchRequestResponse.BatchCreateResponse<GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse> response) {
            if (response == null || response.inlinedResponses() == null) {
                return Collections.emptyList();
            }
            ArrayList<BatchItemResult<Response<Embedding>>> results = new ArrayList<BatchItemResult<Response<Embedding>>>();
            for (BatchRequestResponse.BatchCreateResponse.InlinedResponseWrapper<GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse> wrapper : response.inlinedResponses().inlinedResponses()) {
                BatchRequestResponse.Operation.Status error;
                BatchRequestResponse.BatchCreateResponse.InlinedResponseWrapper<GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse> typed = Json.convertValue(wrapper, this.responseWrapperType);
                GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse typedResponse = typed.response();
                if (typedResponse != null) {
                    Embedding embedding = Embedding.from(typedResponse.embedding().values());
                    results.add(BatchItemResult.success(Response.from(embedding)));
                }
                if ((error = typed.error()) == null) continue;
                results.add(BatchItemResult.<Response<Embedding>>failure((BatchError) error.toGenericStatus()));
            }
            return results;
        }
    }

    public static class Builder
    extends GoogleAiEmbeddingModel.BaseGoogleAiEmbeddingModelBuilder<Builder> {
        public GoogleAiGeminiBatchEmbeddingModel build() {
            return new GoogleAiGeminiBatchEmbeddingModel(this);
        }
    }
}

