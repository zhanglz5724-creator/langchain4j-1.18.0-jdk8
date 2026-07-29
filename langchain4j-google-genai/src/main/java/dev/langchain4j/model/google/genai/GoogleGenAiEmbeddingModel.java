/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.genai.Client
 *  com.google.genai.types.Content
 *  com.google.genai.types.ContentEmbedding
 *  com.google.genai.types.ContentEmbeddingStatistics
 *  com.google.genai.types.EmbedContentConfig
 *  com.google.genai.types.EmbedContentConfig$Builder
 *  com.google.genai.types.EmbedContentResponse
 *  com.google.genai.types.Part
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ContentType
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.ExceptionMapper
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
 *  dev.langchain4j.model.embedding.response.EmbeddingResponse$Builder
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.google.genai;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.genai.Client;
import com.google.genai.types.ContentEmbedding;
import com.google.genai.types.ContentEmbeddingStatistics;
import com.google.genai.types.EmbedContentConfig;
import com.google.genai.types.EmbedContentResponse;
import com.google.genai.types.Part;
import dev.langchain4j.Experimental;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.ExceptionMapper;
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
import dev.langchain4j.model.google.genai.GoogleGenAiClientFactory;
import dev.langchain4j.model.google.genai.GoogleGenAiExceptionMapper;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Experimental
public class GoogleGenAiEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private static final Logger log = LoggerFactory.getLogger(GoogleGenAiEmbeddingModel.class);
    private final Client client;
    private final String modelName;
    private final Integer outputDimensionality;
    private final TaskTypeEnum taskType;
    private final String titleMetadataKey;
    private final Integer maxSegmentsPerBatch;
    private final Integer maxRetries;
    private final boolean logRequests;
    private final boolean logResponses;
    private final List<EmbeddingModelListener> listeners;

    public GoogleGenAiEmbeddingModel(Builder builder) {
        this.client = builder.client != null ? builder.client : GoogleGenAiClientFactory.createClient(builder.apiKey, builder.googleCredentials, builder.projectId, builder.location, builder.timeout, builder.customHeaders, builder.apiEndpoint);
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.outputDimensionality = builder.outputDimensionality;
        this.taskType = builder.taskType;
        this.titleMetadataKey = (String)Utils.getOrDefault((Object)builder.titleMetadataKey, (Object)"title");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)3);
        this.maxSegmentsPerBatch = (Integer)Utils.getOrDefault((Object)builder.maxSegmentsPerBatch, (Object)100);
        this.logRequests = (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false);
        this.logResponses = (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false);
        this.listeners = Utils.copy((List)builder.listeners);
    }

    public List<EmbeddingModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.GOOGLE_GENAI;
    }

    public Set<EmbeddingParameter<?>> supportedParameters() {
        return new HashSet(Arrays.asList(EmbeddingRequestParameters.INPUT_TYPE, EmbeddingRequestParameters.DIMENSIONS));
    }

    public Set<ContentType> supportedContentTypes() {
        return GoogleGenAiEmbeddingModel.isEmbedding2(this.modelName) ? new HashSet<ContentType>(Arrays.asList(ContentType.TEXT, ContentType.IMAGE)) : Collections.singleton(ContentType.TEXT);
    }

    public EmbeddingResponse doEmbed(EmbeddingRequest request) {
        EmbeddingInputType inputType = request.inputType();
        boolean embedding2 = GoogleGenAiEmbeddingModel.isEmbedding2(this.modelName);
        String effectiveTaskType = embedding2 ? null : this.toSdkTaskType(inputType);
        Integer effectiveDimensions = (Integer)Utils.getOrDefault((Object)request.dimensions(), (Object)this.outputDimensionality);
        EmbedContentConfig.Builder configBuilder = EmbedContentConfig.builder();
        if (effectiveTaskType != null) {
            configBuilder.taskType(effectiveTaskType);
        }
        if (effectiveDimensions != null) {
            configBuilder.outputDimensionality(effectiveDimensions);
        }
        EmbedContentConfig config = configBuilder.build();
        boolean multimodal = request.inputs().stream().flatMap(input -> input.contentTypes().stream()).anyMatch(type -> type != ContentType.TEXT);
        ArrayList<EmbedContentResponse> responses = new ArrayList<>();
        if (multimodal) {
            for (EmbeddingInput input2 : request.inputs()) {
                com.google.genai.types.Content content = this.toContent(input2, inputType);
                responses.add(RetryUtils.withRetryMappingExceptions(() -> this.client.models.embedContent(this.modelName, content, config), (int)this.maxRetries, (ExceptionMapper)GoogleGenAiExceptionMapper.INSTANCE));
            }
        } else {
            List texts = request.inputs().stream().map(input -> embedding2 ? GoogleGenAiEmbeddingModel.applyTaskInstruction(input.text(), inputType) : input.text()).collect(Collectors.toList());
            for (int i = 0; i < texts.size(); i += this.maxSegmentsPerBatch.intValue()) {
                List batch = texts.subList(i, Math.min(i + this.maxSegmentsPerBatch, texts.size()));
                responses.add(RetryUtils.withRetryMappingExceptions(() -> this.client.models.embedContent(this.modelName, batch, config), (int)this.maxRetries, (ExceptionMapper)GoogleGenAiExceptionMapper.INSTANCE));
            }
        }
        ArrayList<Embedding> embeddings = new ArrayList<Embedding>();
        int tokenCount = 0;
        boolean tokenCountReported = false;
        for (EmbedContentResponse embedContentResponse : responses) {
            if (!embedContentResponse.embeddings().isPresent()) continue;
            List<ContentEmbedding> embeddingList = embedContentResponse.embeddings().get();
            for (ContentEmbedding embedding : embeddingList) {
                if (embedding.values().isPresent()) {
                    embeddings.add(Embedding.from(embedding.values().get()));
                }
                if (!embedding.statistics().isPresent() || !((ContentEmbeddingStatistics)embedding.statistics().get()).tokenCount().isPresent()) continue;
                tokenCount += Math.round(((Float)((ContentEmbeddingStatistics)embedding.statistics().get()).tokenCount().get()).floatValue());
                tokenCountReported = true;
            }
        }
        EmbeddingResponse.Builder responseBuilder = EmbeddingResponse.builder().embeddings(embeddings).modelName(this.modelName);
        if (tokenCountReported) {
            responseBuilder.tokenUsage(new TokenUsage(Integer.valueOf(tokenCount)));
        }
        return responseBuilder.build();
    }

    private static boolean isEmbedding2(String modelName) {
        return modelName != null && modelName.contains("embedding-2");
    }

    private com.google.genai.types.Content toContent(EmbeddingInput input, EmbeddingInputType inputType) {
        boolean textOnly = input.contents().stream().allMatch(content -> content instanceof TextContent);
        ArrayList<Part> parts = new ArrayList<Part>();
        for (Content content2 : input.contents()) {
            if (content2 instanceof TextContent) {
                TextContent textContent = (TextContent)content2;
                String text = textOnly ? GoogleGenAiEmbeddingModel.applyTaskInstruction(textContent.text(), inputType) : textContent.text();
                parts.add(Part.fromText((String)text));
                continue;
            }
            if (content2 instanceof ImageContent) {
                ImageContent imageContent = (ImageContent)content2;
                Image image = imageContent.image();
                if (image.base64Data() == null) {
                    throw new UnsupportedFeatureException("Gemini requires base64 image data (a plain URL is not supported)");
                }
                parts.add(Part.fromBytes((byte[])Base64.getDecoder().decode(image.base64Data()), (String)((String)Utils.getOrDefault((Object)image.mimeType(), (Object)"image/png"))));
                continue;
            }
            throw new UnsupportedFeatureException("Unsupported content type: " + content2.type());
        }
        return com.google.genai.types.Content.fromParts((Part[])parts.toArray(new Part[0]));
    }

    private String toSdkTaskType(EmbeddingInputType inputType) {
        if (inputType == null) {
            return this.taskType != null ? this.taskType.getSdkTaskType() : null;
        }
        switch (inputType) {
            case QUERY: {
                return TaskTypeEnum.RETRIEVAL_QUERY.getSdkTaskType();
            }
            case DOCUMENT: {
                return TaskTypeEnum.RETRIEVAL_DOCUMENT.getSdkTaskType();
            }
        }
        throw new IllegalArgumentException("Unknown input type: " + inputType);
    }

    private static String applyTaskInstruction(String text, EmbeddingInputType inputType) {
        if (inputType == null) {
            return text;
        }
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

    public Response<Embedding> embed(TextSegment textSegment) {
        return Response.from(this.embedAll(Collections.singletonList(textSegment)).content().get(0));
    }

    public Response<Embedding> embed(String text) {
        return this.embed(TextSegment.from((String)text));
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        return EmbeddingModelListenerUtils.withListeners((EmbeddingModel)this, textSegments, () -> this.embedAllInternal(textSegments));
    }

    /*
     * WARNING - void declaration
     */
    private Response<List<Embedding>> embedAllInternal(List<TextSegment> textSegments) {
        if (textSegments == null || textSegments.isEmpty()) {
            return Response.from(new ArrayList());
        }
        if (this.logRequests) {
            log.info("Request:\n- model: {}\n- texts: {}", (Object)this.modelName, textSegments.stream().map(TextSegment::text).collect(Collectors.toList()));
        }
        LinkedHashMap<String, List<IndexedSegment>> grouped = new LinkedHashMap<>();
        for (int i = 0; i < textSegments.size(); ++i) {
            String title = null;
            TextSegment segment = textSegments.get(i);
            if (TaskTypeEnum.RETRIEVAL_DOCUMENT.equals(this.taskType) && segment.metadata() != null) {
                title = segment.metadata().getString(this.titleMetadataKey);
            }
            grouped.computeIfAbsent(title, k -> new ArrayList()).add(new IndexedSegment(i, segment));
        }
        Embedding[] embeddingsArray = new Embedding[textSegments.size()];
        for (Map.Entry<String, List<IndexedSegment>> entry : grouped.entrySet()) {
            String title = entry.getKey();
            List<IndexedSegment> indexedSegments = entry.getValue();
            int size = indexedSegments.size();
            for (int i = 0; i < size; i += this.maxSegmentsPerBatch.intValue()) {
                EmbedContentResponse response;
                List<IndexedSegment> batch = indexedSegments.subList(i, Math.min(i + this.maxSegmentsPerBatch, size));
                List<String> texts = batch.stream().map(is -> is.segment.text()).collect(Collectors.toList());
                EmbedContentConfig.Builder configBuilder = EmbedContentConfig.builder();
                if (this.taskType != null) {
                    configBuilder.taskType(this.taskType.getSdkTaskType());
                }
                if (this.outputDimensionality != null) {
                    configBuilder.outputDimensionality(this.outputDimensionality);
                }
                if (title != null) {
                    configBuilder.title(title);
                }
                if (!(response = (EmbedContentResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.models.embedContent(this.modelName, texts, configBuilder.build()), (int)this.maxRetries, (ExceptionMapper)GoogleGenAiExceptionMapper.INSTANCE)).embeddings().isPresent()) continue;
                List<ContentEmbedding> embeddings = response.embeddings().get();
                for (int j = 0; j < batch.size(); ++j) {
                    if (j >= embeddings.size() || !((ContentEmbedding)embeddings.get(j)).values().isPresent()) continue;
                    embeddingsArray[((IndexedSegment)batch.get((int)j)).index] = Embedding.from((List)((List)((ContentEmbedding)embeddings.get(j)).values().get()));
                }
            }
        }
        Response response = Response.from(Arrays.asList(embeddingsArray));
        if (this.logResponses) {
            log.info("Response:\n- model: {}\n- response: {}", (Object)this.modelName, (Object)response);
        }
        return response;
    }

    public String modelName() {
        return this.modelName;
    }

    public Integer knownDimension() {
        return this.outputDimensionality;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Client client;
        private String modelName;
        private String apiKey;
        private GoogleCredentials googleCredentials;
        private String projectId;
        private String location;
        private Boolean logRequests;
        private Boolean logResponses;
        private Duration timeout;
        private Integer outputDimensionality;
        private TaskTypeEnum taskType;
        private String titleMetadataKey;
        private String apiEndpoint;
        private Map<String, String> customHeaders;
        private Integer maxSegmentsPerBatch = 100;
        private Integer maxRetries = 3;
        private List<EmbeddingModelListener> listeners;

        public Builder client(Client client) {
            this.client = client;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder googleCredentials(GoogleCredentials googleCredentials) {
            this.googleCredentials = googleCredentials;
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

        public Builder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public Builder logRequestsAndResponses(Boolean logRequestsAndResponses) {
            this.logRequests = logRequestsAndResponses;
            this.logResponses = logRequestsAndResponses;
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

        public Builder taskType(TaskTypeEnum taskType) {
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

        public Builder maxSegmentsPerBatch(Integer maxSegmentsPerBatch) {
            this.maxSegmentsPerBatch = maxSegmentsPerBatch;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder listeners(List<EmbeddingModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public GoogleGenAiEmbeddingModel build() {
            return new GoogleGenAiEmbeddingModel(this);
        }
    }

    private static class IndexedSegment {
        final int index;
        final TextSegment segment;

        IndexedSegment(int index, TextSegment segment) {
            this.index = index;
            this.segment = segment;
        }
    }

    public static enum TaskTypeEnum {
        TASK_TYPE_UNSPECIFIED("TASK_TYPE_UNSPECIFIED"),
        RETRIEVAL_QUERY("RETRIEVAL_QUERY"),
        RETRIEVAL_DOCUMENT("RETRIEVAL_DOCUMENT"),
        SEMANTIC_SIMILARITY("SEMANTIC_SIMILARITY"),
        CLASSIFICATION("CLASSIFICATION"),
        CLUSTERING("CLUSTERING"),
        QUESTION_ANSWERING("QUESTION_ANSWERING"),
        FACT_VERIFICATION("FACT_VERIFICATION"),
        CODE_RETRIEVAL_QUERY("CODE_RETRIEVAL_QUERY");

        private final String sdkTaskType;

        private TaskTypeEnum(String sdkTaskType) {
            this.sdkTaskType = sdkTaskType;
        }

        public String getSdkTaskType() {
            return this.sdkTaskType;
        }
    }
}

