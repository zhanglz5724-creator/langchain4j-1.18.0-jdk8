/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.batch.BatchError
 *  dev.langchain4j.model.batch.BatchItemResult
 *  dev.langchain4j.model.batch.BatchPage
 *  dev.langchain4j.model.batch.BatchPagination
 *  dev.langchain4j.model.batch.BatchRequest
 *  dev.langchain4j.model.batch.BatchResponse
 *  dev.langchain4j.model.image.BatchImageModel
 *  dev.langchain4j.model.output.Response
 *  org.jspecify.annotations.NonNull
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.langchain4j.Experimental;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.batch.BatchError;
import dev.langchain4j.model.batch.BatchItemResult;
import dev.langchain4j.model.batch.BatchPage;
import dev.langchain4j.model.batch.BatchPagination;
import dev.langchain4j.model.batch.BatchRequest;
import dev.langchain4j.model.batch.BatchResponse;
import dev.langchain4j.model.googleai.BatchRequestResponse;
import dev.langchain4j.model.googleai.GeminiBatchProcessor;
import dev.langchain4j.model.googleai.GeminiBatchRequest;
import dev.langchain4j.model.googleai.GeminiContent;
import dev.langchain4j.model.googleai.GeminiFiles;
import dev.langchain4j.model.googleai.GeminiGenerateContentRequest;
import dev.langchain4j.model.googleai.GeminiGenerateContentResponse;
import dev.langchain4j.model.googleai.GeminiGenerationConfig;
import dev.langchain4j.model.googleai.GeminiResponseModality;
import dev.langchain4j.model.googleai.GeminiRole;
import dev.langchain4j.model.googleai.GeminiSafetySetting;
import dev.langchain4j.model.googleai.GeminiService;
import dev.langchain4j.model.googleai.GoogleAiGeminiImageModel;
import dev.langchain4j.model.googleai.Json;
import dev.langchain4j.model.googleai.jsonl.JsonLinesWriter;
import dev.langchain4j.model.image.BatchImageModel;
import dev.langchain4j.model.output.Response;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Experimental
public final class GoogleAiGeminiBatchImageModel
implements BatchImageModel {
    private final GeminiBatchProcessor<String, Response<@NonNull Image>, GeminiGenerateContentRequest, GeminiGenerateContentResponse> batchProcessor;
    private final String modelName;
    private final GeminiGenerationConfig.GeminiImageConfig imageConfig;
    private final List<GeminiResponseModality> responseModalities;
    private final List<GeminiSafetySetting> safetySettings;
    private final ImageRequestPreparer preparer;

    GoogleAiGeminiBatchImageModel(GoogleAiGeminiBatchImageModelBuilder builder) {
        this(builder, GoogleAiGeminiBatchImageModel.buildGeminiService(builder));
    }

    GoogleAiGeminiBatchImageModel(GoogleAiGeminiBatchImageModelBuilder builder, GeminiService geminiService) {
        this.modelName = (String)Utils.getOrDefault((Object)builder.modelName, (Object)"gemini-2.5-flash-preview-image-generation");
        this.responseModalities = Collections.singletonList(GeminiResponseModality.IMAGE);
        this.safetySettings = builder.safetySettings;
        this.imageConfig = builder.aspectRatio != null || builder.imageSize != null ? GeminiGenerationConfig.GeminiImageConfig.builder().aspectRatio(builder.aspectRatio).imageSize(builder.imageSize).build() : null;
        this.preparer = new ImageRequestPreparer();
        this.batchProcessor = new GeminiBatchProcessor<String, Response<Image>, GeminiGenerateContentRequest, GeminiGenerateContentResponse>(geminiService, this.preparer);
    }

    private static GeminiService buildGeminiService(GoogleAiGeminiBatchImageModelBuilder builder) {
        return new GeminiService(builder.httpClientBuilder, builder.apiKey, builder.baseUrl, (Boolean)Utils.getOrDefault((Object)builder.logRequestsAndResponses, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false), builder.logger, builder.timeout, null);
    }

    public BatchResponse<Response<Image>> submit(BatchRequest<String> request) {
        return this.batchProcessor.createBatch(null, null, request.requests(), this.modelName, GeminiService.BatchOperationType.BATCH_GENERATE_CONTENT);
    }

    public BatchResponse<Response<@NonNull Image>> submit(GeminiBatchRequest<String> request) {
        return this.batchProcessor.createBatch(request.displayName(), request.priority(), request.requests(), this.modelName, GeminiService.BatchOperationType.BATCH_GENERATE_CONTENT);
    }

    public BatchResponse<Response<@NonNull Image>> submit(String displayName, GeminiFiles.GeminiFile file) {
        return this.batchProcessor.createBatchFromFile(displayName, file, this.modelName, GeminiService.BatchOperationType.BATCH_GENERATE_CONTENT);
    }

    public void writeBatchToFile(JsonLinesWriter writer, Iterable<BatchRequestResponse.BatchFileRequest<String>> requests) throws IOException {
        this.batchProcessor.writeBatch(writer, requests);
    }

    public BatchResponse<Response<@NonNull Image>> retrieve(String batchId) {
        return this.batchProcessor.retrieveBatchResults(batchId);
    }

    public void cancel(String batchId) {
        this.batchProcessor.cancelBatchJob(batchId);
    }

    public void deleteBatchJob(String batchId) {
        this.batchProcessor.deleteBatchJob(batchId);
    }

    public BatchPage<Response<@NonNull Image>> list(@Nullable BatchPagination batchPagination) {
        return this.batchProcessor.listBatchJobs(batchPagination);
    }

    public static GoogleAiGeminiBatchImageModelBuilder builder() {
        return new GoogleAiGeminiBatchImageModelBuilder();
    }

    private class ImageRequestPreparer
    implements GeminiBatchProcessor.RequestPreparer<String, GeminiGenerateContentRequest, GeminiGenerateContentResponse, Response<Image>> {
        private final TypeReference<GeminiGenerateContentResponse> responseWrapperType = new TypeReference<GeminiGenerateContentResponse>(){};
        private final TypeReference<BatchRequestResponse.BatchCreateResponse.InlinedResponseWrapper<GeminiGenerateContentResponse>> inlinedResponseWrapperType = new TypeReference<BatchRequestResponse.BatchCreateResponse.InlinedResponseWrapper<GeminiGenerateContentResponse>>(){};

        private ImageRequestPreparer() {
        }

        @Override
        public String prepareRequest(String prompt) {
            return prompt;
        }

        @Override
        public GeminiGenerateContentRequest createInlinedRequest(String prompt) {
            GeminiContent content = new GeminiContent(Collections.singletonList(GeminiContent.GeminiPart.ofText(prompt)), GeminiRole.USER.toString());
            return GeminiGenerateContentRequest.builder().contents(Collections.singletonList(content)).generationConfig(GeminiGenerationConfig.builder().responseModalities(GoogleAiGeminiBatchImageModel.this.responseModalities).imageConfig(GoogleAiGeminiBatchImageModel.this.imageConfig).build()).safetySettings(GoogleAiGeminiBatchImageModel.this.safetySettings).build();
        }

        @Override
        public List<BatchItemResult<Response<Image>>> extractResults(BatchRequestResponse.BatchCreateResponse<GeminiGenerateContentResponse> response) {
            if (response == null || response.inlinedResponses() == null) {
                return Collections.emptyList();
            }
            ArrayList<BatchItemResult<Response<Image>>> results = new ArrayList<BatchItemResult<Response<Image>>>();
            for (BatchRequestResponse.BatchCreateResponse.InlinedResponseWrapper<GeminiGenerateContentResponse> wrapper : response.inlinedResponses().inlinedResponses()) {
                BatchRequestResponse.Operation.Status error;
                BatchRequestResponse.BatchCreateResponse.InlinedResponseWrapper<GeminiGenerateContentResponse> typed = Json.convertValue(wrapper, this.inlinedResponseWrapperType);
                if (typed.response() != null) {
                    GeminiGenerateContentResponse geminiResponse = Json.convertValue(typed.response(), this.responseWrapperType);
                    results.add(BatchItemResult.success(this.extractImage(geminiResponse)));
                }
                if ((error = typed.error()) == null) continue;
                results.add(BatchItemResult.<Response<Image>>failure((BatchError) error.toGenericStatus()));
            }
            return results;
        }

        private Response<Image> extractImage(GeminiGenerateContentResponse geminiResponse) {
            if (geminiResponse.candidates() == null || geminiResponse.candidates().isEmpty()) {
                throw new GoogleAiGeminiImageModel.GeminiImageGenerationException("No image generated in responses");
            }
            GeminiGenerateContentResponse.GeminiCandidate candidate = geminiResponse.candidates().get(0);
            if (candidate.content() == null || candidate.content().parts() == null) {
                throw new GoogleAiGeminiImageModel.GeminiImageGenerationException("No content in responses candidate");
            }
            for (GeminiContent.GeminiPart part : candidate.content().parts()) {
                if (part.inlineData() == null) continue;
                Image image = Image.builder().base64Data(part.inlineData().data()).mimeType(part.inlineData().mimeType()).build();
                return Response.from(image);
            }
            throw new GoogleAiGeminiImageModel.GeminiImageGenerationException("No image data found in responses");
        }
    }

    public static class GoogleAiGeminiBatchImageModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String apiKey;
        private String baseUrl;
        private String modelName;
        private String aspectRatio;
        private String imageSize;
        private Duration timeout;
        private Boolean logRequestsAndResponses;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private List<GeminiSafetySetting> safetySettings;

        private GoogleAiGeminiBatchImageModelBuilder() {
        }

        public GoogleAiGeminiBatchImageModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public GoogleAiGeminiBatchImageModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public GoogleAiGeminiBatchImageModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public GoogleAiGeminiBatchImageModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public GoogleAiGeminiBatchImageModelBuilder aspectRatio(String aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        public GoogleAiGeminiBatchImageModelBuilder imageSize(String imageSize) {
            this.imageSize = imageSize;
            return this;
        }

        public GoogleAiGeminiBatchImageModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public GoogleAiGeminiBatchImageModelBuilder logRequestsAndResponses(Boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
            return this;
        }

        public GoogleAiGeminiBatchImageModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public GoogleAiGeminiBatchImageModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public GoogleAiGeminiBatchImageModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public GoogleAiGeminiBatchImageModelBuilder safetySettings(List<GeminiSafetySetting> safetySettings) {
            this.safetySettings = safetySettings;
            return this;
        }

        public GoogleAiGeminiBatchImageModel build() {
            return new GoogleAiGeminiBatchImageModel(this);
        }
    }
}

