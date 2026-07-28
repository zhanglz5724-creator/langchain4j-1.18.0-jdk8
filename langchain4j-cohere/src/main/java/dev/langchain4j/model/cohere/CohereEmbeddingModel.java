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
 *  dev.langchain4j.internal.ExceptionMapper
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
 *  dev.langchain4j.model.output.TokenUsage
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.cohere;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.cohere.CohereClient;
import dev.langchain4j.model.cohere.EmbedRequest;
import dev.langchain4j.model.cohere.EmbedResponse;
import dev.langchain4j.model.cohere.EmbedV2Request;
import dev.langchain4j.model.cohere.EmbedV2Response;
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
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class CohereEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private static final String DEFAULT_BASE_URL = "https://api.cohere.ai/v1/";
    private static final int DEFAULT_MAX_SEGMENTS_PER_BATCH = 96;
    private final CohereClient client;
    private final CohereClient v2Client;
    private final String modelName;
    private final String inputType;
    private final int maxSegmentsPerBatch;
    private final List<EmbeddingModelListener> listeners;

    @Deprecated
    public CohereEmbeddingModel(String baseUrl, String apiKey, String modelName, String inputType, Duration timeout, Boolean logRequests, Boolean logResponses, Integer maxSegmentsPerBatch) {
        String resolvedBaseUrl = (String)Utils.getOrDefault((Object)baseUrl, (Object)DEFAULT_BASE_URL);
        this.client = CohereClient.builder().baseUrl(resolvedBaseUrl).apiKey(ValidationUtils.ensureNotBlank((String)apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)logResponses, (Object)false)).build();
        this.v2Client = CohereClient.builder().baseUrl(CohereEmbeddingModel.toV2BaseUrl(resolvedBaseUrl)).apiKey(ValidationUtils.ensureNotBlank((String)apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)logResponses, (Object)false)).build();
        this.modelName = modelName;
        this.inputType = inputType;
        this.maxSegmentsPerBatch = (Integer)Utils.getOrDefault((Object)maxSegmentsPerBatch, (Object)96);
        this.listeners = Collections.emptyList();
    }

    public CohereEmbeddingModel(CohereEmbeddingModelBuilder builder) {
        String baseUrl = (String)Utils.getOrDefault((Object)builder.baseUrl, (Object)DEFAULT_BASE_URL);
        this.client = CohereClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl(baseUrl).apiKey(ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false)).logger(builder.logger).build();
        this.v2Client = CohereClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl((String)Utils.getOrDefault((Object)builder.v2BaseUrl, (Object)CohereEmbeddingModel.toV2BaseUrl(baseUrl))).apiKey(ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false)).logger(builder.logger).build();
        this.modelName = builder.modelName;
        this.inputType = builder.inputType;
        this.maxSegmentsPerBatch = (Integer)Utils.getOrDefault((Object)builder.maxSegmentsPerBatch, (Object)96);
        this.listeners = Utils.copy((List)builder.listeners);
    }

    public List<EmbeddingModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.COHERE;
    }

    @Deprecated
    public static CohereEmbeddingModel withApiKey(String apiKey) {
        return CohereEmbeddingModel.builder().apiKey(apiKey).build();
    }

    public static CohereEmbeddingModelBuilder builder() {
        return new CohereEmbeddingModelBuilder();
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        return EmbeddingModelListenerUtils.withListeners((EmbeddingModel)this, textSegments, () -> {
            List<String> texts = textSegments.stream().map(TextSegment::text).collect(Collectors.toList());
            return this.embedTexts(texts);
        });
    }

    public String modelName() {
        return this.modelName;
    }

    public Set<ContentType> supportedContentTypes() {
        return new HashSet<ContentType>(Arrays.asList(ContentType.TEXT, ContentType.IMAGE));
    }

    public Set<EmbeddingParameter<?>> supportedParameters() {
        return Collections.singleton(EmbeddingRequestParameters.INPUT_TYPE);
    }

    public EmbeddingResponse doEmbed(EmbeddingRequest request) {
        String effectiveInputType = (String)Utils.getOrDefault((Object)CohereEmbeddingModel.toCohereInputType(request.inputType()), (Object)this.inputType);
        ArrayList embeddings = new ArrayList();
        int inputTokens = 0;
        List inputs = request.inputs();
        for (int i = 0; i < inputs.size(); i += this.maxSegmentsPerBatch) {
            List batch = inputs.subList(i, Math.min(i + this.maxSegmentsPerBatch, inputs.size()));
            EmbedV2Response response = (EmbedV2Response)ExceptionMapper.mappingException(() -> this.v2Client.embedV2(this.buildV2Request(batch, effectiveInputType)));
            if (response.getEmbeddings() != null && response.getEmbeddings().getFloatEmbeddings() != null) {
                embeddings.addAll(response.getEmbeddings().getFloatEmbeddings().stream().map(Embedding::from).collect(Collectors.toList()));
            }
            inputTokens += CohereEmbeddingModel.v2TokenUsage(response);
        }
        return EmbeddingResponse.builder().embeddings(embeddings).metadata(EmbeddingResponseMetadata.builder().modelName(this.modelName).tokenUsage(new TokenUsage(Integer.valueOf(inputTokens), Integer.valueOf(0))).build()).build();
    }

    EmbedV2Request buildV2Request(List<EmbeddingInput> inputs, String resolvedInputType) {
        return EmbedV2Request.builder().model(this.modelName).inputType(resolvedInputType).embeddingTypes(Arrays.asList("float")).inputs(inputs.stream().map(this::toV2Input).collect(Collectors.toList())).build();
    }

    private EmbedV2Request.V2Input toV2Input(EmbeddingInput input) {
        return new EmbedV2Request.V2Input(input.contents().stream().map(this::toV2Content).collect(Collectors.toList()));
    }

    private EmbedV2Request.V2Content toV2Content(Content content) {
        if (content instanceof TextContent) {
            TextContent textContent = (TextContent)content;
            return EmbedV2Request.V2Content.text(textContent.text());
        }
        if (content instanceof ImageContent) {
            ImageContent imageContent = (ImageContent)content;
            Image image = imageContent.image();
            if (image.url() != null) {
                return EmbedV2Request.V2Content.imageUrl(image.url().toString());
            }
            if (image.base64Data() != null) {
                String dataUrl = "data:" + (String)Utils.getOrDefault((Object)image.mimeType(), (Object)"image/png") + ";base64," + image.base64Data();
                return EmbedV2Request.V2Content.imageUrl(dataUrl);
            }
            throw new UnsupportedFeatureException("ImageContent must have either a URL or base64 data");
        }
        throw new UnsupportedFeatureException("Unsupported content type: " + content.type());
    }

    static String toCohereInputType(EmbeddingInputType inputType) {
        if (inputType == null) {
            return null;
        }
        switch (inputType) {
            case QUERY: {
                return "search_query";
            }
            case DOCUMENT: {
                return "search_document";
            }
        }
        throw new IllegalArgumentException("Unknown input type: " + inputType);
    }

    private static String toV2BaseUrl(String v1BaseUrl) {
        return v1BaseUrl.contains("/v1") ? v1BaseUrl.replace("/v1", "/v2") : v1BaseUrl;
    }

    private static int v2TokenUsage(EmbedV2Response response) {
        if (response.getMeta() != null && response.getMeta().getBilledUnits() != null && response.getMeta().getBilledUnits().getInputTokens() != null) {
            return response.getMeta().getBilledUnits().getInputTokens();
        }
        return 0;
    }

    private Response<List<Embedding>> embedTexts(List<String> texts) {
        ArrayList<Embedding> embeddings = new ArrayList<Embedding>();
        Integer totalTokenUsage = 0;
        for (int i = 0; i < texts.size(); i += this.maxSegmentsPerBatch) {
            List<String> batch = texts.subList(i, Math.min(i + this.maxSegmentsPerBatch, texts.size()));
            EmbedRequest request = EmbedRequest.builder().texts(batch).inputType(this.inputType).model(this.modelName).build();
            EmbedResponse response = (EmbedResponse)ExceptionMapper.mappingException(() -> this.client.embed(request));
            embeddings.addAll(CohereEmbeddingModel.getEmbeddings(response));
            totalTokenUsage = totalTokenUsage + CohereEmbeddingModel.getTokenUsage(response);
        }
        return Response.from(embeddings, (TokenUsage)new TokenUsage(totalTokenUsage, Integer.valueOf(0)));
    }

    private static List<Embedding> getEmbeddings(EmbedResponse response) {
        return Arrays.stream(response.getEmbeddings()).map(Embedding::from).collect(Collectors.toList());
    }

    private static Integer getTokenUsage(EmbedResponse response) {
        if (response.getMeta() != null && response.getMeta().getBilledUnits() != null && response.getMeta().getBilledUnits().getInputTokens() != null) {
            return response.getMeta().getBilledUnits().getInputTokens();
        }
        return 0;
    }

    public static class CohereEmbeddingModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String v2BaseUrl;
        private String apiKey;
        private String modelName;
        private String inputType;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Integer maxSegmentsPerBatch;
        private List<EmbeddingModelListener> listeners;

        CohereEmbeddingModelBuilder() {
        }

        public CohereEmbeddingModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public CohereEmbeddingModelBuilder listeners(List<EmbeddingModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public CohereEmbeddingModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public CohereEmbeddingModelBuilder v2BaseUrl(String v2BaseUrl) {
            this.v2BaseUrl = v2BaseUrl;
            return this;
        }

        public CohereEmbeddingModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public CohereEmbeddingModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public CohereEmbeddingModelBuilder inputType(String inputType) {
            this.inputType = inputType;
            return this;
        }

        public CohereEmbeddingModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public CohereEmbeddingModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public CohereEmbeddingModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public CohereEmbeddingModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public CohereEmbeddingModelBuilder maxSegmentsPerBatch(Integer maxSegmentsPerBatch) {
            this.maxSegmentsPerBatch = maxSegmentsPerBatch;
            return this;
        }

        public CohereEmbeddingModel build() {
            return new CohereEmbeddingModel(this);
        }

        public String toString() {
            return "CohereEmbeddingModel.CohereEmbeddingModelBuilder(baseUrl=" + this.baseUrl + ", apiKey=" + (this.apiKey == null ? null : "********") + ", modelName=" + this.modelName + ", inputType=" + this.inputType + ", timeout=" + this.timeout + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ", maxSegmentsPerBatch=" + this.maxSegmentsPerBatch + ")";
        }
    }
}

