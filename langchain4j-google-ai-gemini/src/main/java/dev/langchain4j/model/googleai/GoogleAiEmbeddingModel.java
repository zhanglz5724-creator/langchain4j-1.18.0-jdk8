/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ContentType
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.embedding.EmbeddingModel
 *  dev.langchain4j.model.embedding.EmbeddingModelListenerUtils
 *  dev.langchain4j.model.embedding.listener.EmbeddingModelListener
 *  dev.langchain4j.model.embedding.request.EmbeddingInput
 *  dev.langchain4j.model.embedding.request.EmbeddingInputType
 *  dev.langchain4j.model.embedding.request.EmbeddingParameter
 *  dev.langchain4j.model.embedding.request.EmbeddingRequest
 *  dev.langchain4j.model.embedding.request.EmbeddingRequestParameters
 *  dev.langchain4j.model.embedding.response.EmbeddingResponse
 *  dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata
 *  dev.langchain4j.model.output.Response
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModelListenerUtils;
import dev.langchain4j.model.embedding.listener.EmbeddingModelListener;
import dev.langchain4j.model.embedding.request.EmbeddingInput;
import dev.langchain4j.model.embedding.request.EmbeddingInputType;
import dev.langchain4j.model.embedding.request.EmbeddingParameter;
import dev.langchain4j.model.embedding.request.EmbeddingRequest;
import dev.langchain4j.model.embedding.request.EmbeddingRequestParameters;
import dev.langchain4j.model.embedding.response.EmbeddingResponse;
import dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata;
import dev.langchain4j.model.googleai.GeminiContent;
import dev.langchain4j.model.googleai.GeminiEmbeddingRequestResponse;
import dev.langchain4j.model.googleai.GeminiService;
import dev.langchain4j.model.output.Response;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class GoogleAiEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private static final int MAX_NUMBER_OF_SEGMENTS_PER_BATCH = 100;
    private final GeminiService geminiService;
    private final String modelName;
    private final Integer maxRetries;
    private final TaskType taskType;
    private final String titleMetadataKey;
    private final Integer outputDimensionality;
    private final List<EmbeddingModelListener> listeners;

    public GoogleAiEmbeddingModel(GoogleAiEmbeddingModelBuilder builder) {
        this.geminiService = new GeminiService(builder.httpClientBuilder, builder.apiKey, builder.baseUrl, (Boolean)Utils.getOrDefault((Object)builder.logRequestsAndResponses, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false), builder.logger, builder.timeout, null);
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.taskType = builder.taskType;
        this.titleMetadataKey = (String)Utils.getOrDefault((Object)builder.titleMetadataKey, (Object)"title");
        this.outputDimensionality = builder.outputDimensionality;
        this.listeners = Utils.copy((List)builder.listeners);
    }

    public List<EmbeddingModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.GOOGLE_AI_GEMINI;
    }

    public static GoogleAiEmbeddingModelBuilder builder() {
        return new GoogleAiEmbeddingModelBuilder();
    }

    public Response<Embedding> embed(TextSegment textSegment) {
        return EmbeddingModelListenerUtils.withListeners((EmbeddingModel)this, (TextSegment)textSegment, () -> {
            GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest embeddingRequest = this.getGoogleAiEmbeddingRequest(textSegment);
            GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse geminiResponse = (GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse)RetryUtils.withRetryMappingExceptions(() -> this.geminiService.embed(this.modelName, embeddingRequest), (int)this.maxRetries);
            return Response.from((Object)Embedding.from(geminiResponse.embedding().values()));
        });
    }

    public Response<Embedding> embed(String text) {
        return this.embed(TextSegment.from((String)text));
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        return EmbeddingModelListenerUtils.withListeners((EmbeddingModel)this, textSegments, () -> {
            List<GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest> embeddingRequests = textSegments.stream().map(this::getGoogleAiEmbeddingRequest).collect(Collectors.toList());
            return Response.from(this.batchEmbed(embeddingRequests));
        });
    }

    public Set<EmbeddingParameter<?>> supportedParameters() {
        return Collections.singleton(EmbeddingRequestParameters.INPUT_TYPE);
    }

    public EmbeddingResponse doEmbed(EmbeddingRequest request) {
        EmbeddingInputType inputType = request.inputType();
        boolean embedding2 = GoogleAiEmbeddingModel.isMultimodalModel(this.modelName);
        TaskType taskType = embedding2 ? null : this.toTaskType(inputType);
        List<GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest> embeddingRequests = request.inputs().stream().map(input -> this.buildEmbeddingRequest(embedding2 ? GoogleAiEmbeddingModel.withTaskInstruction(input, inputType) : input, taskType)).collect(Collectors.toList());
        return EmbeddingResponse.builder().embeddings(this.batchEmbed(embeddingRequests)).metadata(EmbeddingResponseMetadata.builder().modelName(this.modelName).build()).build();
    }

    public Set<ContentType> supportedContentTypes() {
        return GoogleAiEmbeddingModel.isMultimodalModel(this.modelName) ? Collections.unmodifiableSet(new HashSet<ContentType>(Arrays.asList(ContentType.TEXT, ContentType.IMAGE))) : Collections.singleton(ContentType.TEXT);
    }

    private static boolean isMultimodalModel(String modelName) {
        return modelName != null && modelName.contains("embedding-2");
    }

    public String modelName() {
        return this.modelName;
    }

    private TaskType toTaskType(EmbeddingInputType inputType) {
        if (inputType == null) {
            return this.taskType;
        }
        switch (inputType) {
            case QUERY: {
                return TaskType.RETRIEVAL_QUERY;
            }
            case DOCUMENT: {
                return TaskType.RETRIEVAL_DOCUMENT;
            }
        }
        throw new IllegalArgumentException("Unknown input type: " + inputType);
    }

    private static EmbeddingInput withTaskInstruction(EmbeddingInput input, EmbeddingInputType inputType) {
        if (inputType == null) {
            return input;
        }
        boolean textOnly = input.contents().stream().allMatch(content -> content instanceof TextContent);
        if (!textOnly) {
            return input;
        }
        return EmbeddingInput.from((String)GoogleAiEmbeddingModel.applyTaskInstruction(input.text(), inputType));
    }

    private static String applyTaskInstruction(String text, EmbeddingInputType inputType) {
        switch (inputType) {
            case QUERY: {
                return "task: search result | query: " + text;
            }
            case DOCUMENT: {
                return "title: none | text: " + text;
            }
        }
        throw new IllegalArgumentException("Unknown input type: " + inputType);
    }

    private List<Embedding> batchEmbed(List<GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest> embeddingRequests) {
        ArrayList<Embedding> allEmbeddings = new ArrayList<Embedding>();
        int numberOfEmbeddings = embeddingRequests.size();
        int numberOfBatches = 1 + numberOfEmbeddings / 100;
        for (int i = 0; i < numberOfBatches; ++i) {
            int startIndex = 100 * i;
            int lastIndex = Math.min(startIndex + 100, numberOfEmbeddings);
            if (startIndex >= numberOfEmbeddings) break;
            GeminiEmbeddingRequestResponse.GeminiBatchEmbeddingRequest batchEmbeddingRequest = new GeminiEmbeddingRequestResponse.GeminiBatchEmbeddingRequest(embeddingRequests.subList(startIndex, lastIndex));
            GeminiEmbeddingRequestResponse.GeminiBatchEmbeddingResponse geminiResponse = (GeminiEmbeddingRequestResponse.GeminiBatchEmbeddingResponse)RetryUtils.withRetryMappingExceptions(() -> this.geminiService.batchEmbed(this.modelName, batchEmbeddingRequest));
            allEmbeddings.addAll(geminiResponse.embeddings().stream().map(values -> Embedding.from(values.values())).collect(Collectors.toList()));
        }
        return allEmbeddings;
    }

    private GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest getGoogleAiEmbeddingRequest(TextSegment textSegment) {
        String title = null;
        if (TaskType.RETRIEVAL_DOCUMENT.equals((Object)this.taskType) && textSegment.metadata() != null && textSegment.metadata().getString(this.titleMetadataKey) != null) {
            title = textSegment.metadata().getString(this.titleMetadataKey);
        }
        return this.buildEmbeddingRequest(textSegment.text(), this.taskType, title);
    }

    private GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest buildEmbeddingRequest(String text, TaskType taskType, String title) {
        GeminiContent.GeminiPart geminiPart = GeminiContent.GeminiPart.builder().text(text).build();
        GeminiContent content = new GeminiContent(Collections.singletonList(geminiPart), null);
        return new GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest("models/" + this.modelName, content, taskType, title, this.outputDimensionality);
    }

    private GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest buildEmbeddingRequest(EmbeddingInput input, TaskType taskType) {
        ArrayList<GeminiContent.GeminiPart> parts = new ArrayList<GeminiContent.GeminiPart>();
        for (Content content : input.contents()) {
            if (content instanceof TextContent) {
                TextContent textContent = (TextContent)content;
                parts.add(GeminiContent.GeminiPart.builder().text(textContent.text()).build());
                continue;
            }
            if (content instanceof ImageContent) {
                ImageContent imageContent = (ImageContent)content;
                Image image = imageContent.image();
                if (image.base64Data() == null) {
                    throw new UnsupportedFeatureException("Gemini requires base64 image data (a plain URL is not supported)");
                }
                parts.add(GeminiContent.GeminiPart.builder().inlineData(new GeminiContent.GeminiPart.GeminiBlob((String)Utils.getOrDefault((Object)image.mimeType(), (Object)"image/png"), image.base64Data())).build());
                continue;
            }
            throw new UnsupportedFeatureException("Unsupported content type: " + content.type());
        }
        GeminiContent content = new GeminiContent(parts, null);
        return new GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest("models/" + this.modelName, content, taskType, null, this.outputDimensionality);
    }

    public Integer knownDimension() {
        return this.outputDimensionality;
    }

    static abstract class BaseGoogleAiEmbeddingModelBuilder<B extends BaseGoogleAiEmbeddingModelBuilder<B>> {
        HttpClientBuilder httpClientBuilder;
        String modelName;
        String apiKey;
        String baseUrl;
        Integer maxRetries;
        TaskType taskType;
        String titleMetadataKey;
        Integer outputDimensionality;
        List<EmbeddingModelListener> listeners;
        Duration timeout;
        Boolean logRequestsAndResponses;
        Boolean logRequests;
        Boolean logResponses;
        Logger logger;

        BaseGoogleAiEmbeddingModelBuilder() {
        }

        public B httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this.builder();
        }

        protected B builder() {
            return (B)this;
        }

        public B modelName(String modelName) {
            this.modelName = modelName;
            return this.builder();
        }

        public B apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this.builder();
        }

        public B baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this.builder();
        }

        public B maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this.builder();
        }

        public B taskType(TaskType taskType) {
            this.taskType = taskType;
            return this.builder();
        }

        public B titleMetadataKey(String titleMetadataKey) {
            this.titleMetadataKey = titleMetadataKey;
            return this.builder();
        }

        public B outputDimensionality(Integer outputDimensionality) {
            this.outputDimensionality = outputDimensionality;
            return this.builder();
        }

        public B listeners(List<EmbeddingModelListener> listeners) {
            this.listeners = listeners;
            return this.builder();
        }

        public B timeout(Duration timeout) {
            this.timeout = timeout;
            return this.builder();
        }

        public B logRequestsAndResponses(Boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
            return this.builder();
        }

        public B logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this.builder();
        }

        public B logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this.builder();
        }

        public B logger(Logger logger) {
            this.logger = logger;
            return this.builder();
        }
    }

    public static class GoogleAiEmbeddingModelBuilder
    extends BaseGoogleAiEmbeddingModelBuilder<GoogleAiEmbeddingModelBuilder> {
        public GoogleAiEmbeddingModel build() {
            return new GoogleAiEmbeddingModel(this);
        }
    }

    public static enum TaskType {
        RETRIEVAL_QUERY,
        RETRIEVAL_DOCUMENT,
        SEMANTIC_SIMILARITY,
        CLASSIFICATION,
        CLUSTERING,
        QUESTION_ANSWERING,
        FACT_VERIFICATION;

    }
}

