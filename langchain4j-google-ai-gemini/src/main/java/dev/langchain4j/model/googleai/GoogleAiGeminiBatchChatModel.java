/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.model.batch.BatchError
 *  dev.langchain4j.model.batch.BatchItemResult
 *  dev.langchain4j.model.batch.BatchPage
 *  dev.langchain4j.model.batch.BatchPagination
 *  dev.langchain4j.model.batch.BatchRequest
 *  dev.langchain4j.model.batch.BatchResponse
 *  dev.langchain4j.model.chat.BatchChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.langchain4j.Experimental;
import dev.langchain4j.model.batch.BatchError;
import dev.langchain4j.model.batch.BatchItemResult;
import dev.langchain4j.model.batch.BatchPage;
import dev.langchain4j.model.batch.BatchPagination;
import dev.langchain4j.model.batch.BatchRequest;
import dev.langchain4j.model.batch.BatchResponse;
import dev.langchain4j.model.chat.BatchChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.BaseGeminiChatModel;
import dev.langchain4j.model.googleai.BatchRequestResponse;
import dev.langchain4j.model.googleai.GeminiBatchProcessor;
import dev.langchain4j.model.googleai.GeminiBatchRequest;
import dev.langchain4j.model.googleai.GeminiFiles;
import dev.langchain4j.model.googleai.GeminiGenerateContentRequest;
import dev.langchain4j.model.googleai.GeminiGenerateContentResponse;
import dev.langchain4j.model.googleai.GeminiService;
import dev.langchain4j.model.googleai.Json;
import dev.langchain4j.model.googleai.jsonl.JsonLinesWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.Nullable;

@Experimental
public final class GoogleAiGeminiBatchChatModel
implements BatchChatModel {
    private final GeminiBatchProcessor<ChatRequest, ChatResponse, GeminiGenerateContentRequest, GeminiGenerateContentResponse> batchProcessor;
    private final BaseGeminiChatModel chatModel;
    private final String modelName;
    private final ChatRequestPreparer preparer = new ChatRequestPreparer();

    GoogleAiGeminiBatchChatModel(Builder builder) {
        this(builder, BaseGeminiChatModel.buildGeminiService(builder));
    }

    GoogleAiGeminiBatchChatModel(Builder builder, GeminiService geminiService) {
        this.batchProcessor = new GeminiBatchProcessor<ChatRequest, ChatResponse, GeminiGenerateContentRequest, GeminiGenerateContentResponse>(geminiService, this.preparer);
        this.chatModel = new BaseGeminiChatModel(builder, geminiService);
        this.modelName = builder.modelName;
    }

    public BatchResponse<ChatResponse> submit(BatchRequest<ChatRequest> request) {
        return this.batchProcessor.createBatch(null, null, request.requests(), this.modelName, GeminiService.BatchOperationType.BATCH_GENERATE_CONTENT);
    }

    public BatchResponse<ChatResponse> submit(GeminiBatchRequest<ChatRequest> request) {
        return this.batchProcessor.createBatch(request.displayName(), request.priority(), request.requests(), this.modelName, GeminiService.BatchOperationType.BATCH_GENERATE_CONTENT);
    }

    public BatchResponse<ChatResponse> submit(String displayName, GeminiFiles.GeminiFile file) {
        return this.batchProcessor.createBatchFromFile(displayName, file, this.modelName, GeminiService.BatchOperationType.BATCH_GENERATE_CONTENT);
    }

    public void writeBatchToFile(JsonLinesWriter writer, Iterable<BatchRequestResponse.BatchFileRequest<ChatRequest>> requests) throws IOException {
        this.batchProcessor.writeBatch(writer, requests);
    }

    public BatchResponse<ChatResponse> retrieve(String batchId) {
        return this.batchProcessor.retrieveBatchResults(batchId);
    }

    public void cancel(String batchId) {
        this.batchProcessor.cancelBatchJob(batchId);
    }

    public void deleteBatchJob(String batchId) {
        this.batchProcessor.deleteBatchJob(batchId);
    }

    public BatchPage<ChatResponse> list(@Nullable BatchPagination batchPagination) {
        return this.batchProcessor.listBatchJobs(batchPagination);
    }

    public static Builder builder() {
        return new Builder();
    }

    private class ChatRequestPreparer
    implements GeminiBatchProcessor.RequestPreparer<ChatRequest, GeminiGenerateContentRequest, GeminiGenerateContentResponse, ChatResponse> {
        private final TypeReference<BatchRequestResponse.BatchCreateResponse.InlinedResponseWrapper<GeminiGenerateContentResponse>> responseWrapperType = new TypeReference<BatchRequestResponse.BatchCreateResponse.InlinedResponseWrapper<GeminiGenerateContentResponse>>(){};

        private ChatRequestPreparer() {
        }

        @Override
        public ChatRequest prepareRequest(ChatRequest request) {
            return ChatRequest.builder().messages(request.messages()).parameters((ChatRequestParameters)((GoogleAiGeminiBatchChatModel)GoogleAiGeminiBatchChatModel.this).chatModel.defaultRequestParameters.overrideWith(request.parameters())).build();
        }

        @Override
        public GeminiGenerateContentRequest createInlinedRequest(ChatRequest request) {
            return GoogleAiGeminiBatchChatModel.this.chatModel.createGenerateContentRequest(request);
        }

        @Override
        public List<BatchItemResult<ChatResponse>> extractResults(BatchRequestResponse.BatchCreateResponse<GeminiGenerateContentResponse> response) {
            if (response == null || response.inlinedResponses() == null) {
                return Collections.emptyList();
            }
            ArrayList<BatchItemResult<ChatResponse>> results = new ArrayList<BatchItemResult<ChatResponse>>();
            for (BatchRequestResponse.BatchCreateResponse.InlinedResponseWrapper<GeminiGenerateContentResponse> wrapper : response.inlinedResponses().inlinedResponses()) {
                BatchRequestResponse.Operation.Status error;
                BatchRequestResponse.BatchCreateResponse.InlinedResponseWrapper<GeminiGenerateContentResponse> typed = Json.convertValue(wrapper, this.responseWrapperType);
                GeminiGenerateContentResponse typedResponse = typed.response();
                if (typedResponse != null) {
                    results.add(BatchItemResult.success(GoogleAiGeminiBatchChatModel.this.chatModel.processResponse(typedResponse)));
                }
                if ((error = typed.error()) == null) continue;
                results.add(BatchItemResult.<ChatResponse>failure((BatchError) error.toGenericStatus()));
            }
            return results;
        }
    }

    public static final class Builder
    extends BaseGeminiChatModel.GoogleAiGeminiChatModelBaseBuilder<Builder> {
        private Builder() {
        }

        public GoogleAiGeminiBatchChatModel build() {
            return new GoogleAiGeminiBatchChatModel(this);
        }
    }
}

