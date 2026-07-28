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
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.embedding.listener.EmbeddingModelListener
 *  dev.langchain4j.model.embedding.request.EmbeddingInput
 *  dev.langchain4j.model.embedding.request.EmbeddingInputType
 *  dev.langchain4j.model.embedding.request.EmbeddingParameter
 *  dev.langchain4j.model.embedding.request.EmbeddingRequest
 *  dev.langchain4j.model.embedding.request.EmbeddingRequestParameters
 *  dev.langchain4j.model.embedding.response.EmbeddingResponse
 *  dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata
 *  dev.langchain4j.model.output.TokenUsage
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.voyageai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.embedding.listener.EmbeddingModelListener;
import dev.langchain4j.model.embedding.request.EmbeddingInput;
import dev.langchain4j.model.embedding.request.EmbeddingInputType;
import dev.langchain4j.model.embedding.request.EmbeddingParameter;
import dev.langchain4j.model.embedding.request.EmbeddingRequestParameters;
import dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.voyageai.EmbeddingRequest;
import dev.langchain4j.model.voyageai.EmbeddingResponse;
import dev.langchain4j.model.voyageai.MultimodalEmbeddingRequest;
import dev.langchain4j.model.voyageai.VoyageAiClient;
import dev.langchain4j.model.voyageai.VoyageAiEmbeddingModelName;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class VoyageAiEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private final VoyageAiClient client;
    private final Integer maxRetries;
    private final String modelName;
    private final String inputType;
    private final Boolean truncation;
    private final String encodingFormat;
    private final Integer maxSegmentsPerBatch;
    private final boolean multimodal;
    private final List<EmbeddingModelListener> listeners;

    @Deprecated
    public VoyageAiEmbeddingModel(HttpClientBuilder httpClientBuilder, Map<String, String> customHeaders, String baseUrl, Duration timeout, Integer maxRetries, String apiKey, String modelName, String inputType, Boolean truncation, String encodingFormat, Boolean logRequests, Boolean logResponses, Integer maxSegmentsPerBatch) {
        this.maxRetries = (Integer)Utils.getOrDefault((Object)maxRetries, (Object)2);
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.maxSegmentsPerBatch = (Integer)Utils.getOrDefault((Object)maxSegmentsPerBatch, (Object)128);
        this.truncation = truncation;
        this.inputType = inputType;
        this.encodingFormat = encodingFormat;
        this.multimodal = VoyageAiEmbeddingModel.isMultimodalModel(this.modelName);
        this.listeners = Collections.emptyList();
        this.client = VoyageAiClient.builder().httpClientBuilder(httpClientBuilder).baseUrl((String)Utils.getOrDefault((Object)baseUrl, (Object)"https://api.voyageai.com/v1/")).apiKey(ValidationUtils.ensureNotBlank((String)apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)logResponses, (Object)false)).customHeaders(() -> customHeaders).build();
    }

    public VoyageAiEmbeddingModel(Builder builder) {
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxSegmentsPerBatch = (Integer)Utils.getOrDefault((Object)builder.maxSegmentsPerBatch, (Object)128);
        this.truncation = builder.truncation;
        this.inputType = builder.inputType;
        this.encodingFormat = builder.encodingFormat;
        this.multimodal = (Boolean)Utils.getOrDefault((Object)builder.multimodal, (Object)VoyageAiEmbeddingModel.isMultimodalModel(this.modelName));
        this.listeners = Utils.copy((List)builder.listeners);
        this.client = VoyageAiClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.voyageai.com/v1/")).apiKey(ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey")).timeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false)).logger(builder.logger).customHeaders(builder.customHeadersSupplier).build();
    }

    public Set<ContentType> supportedContentTypes() {
        return this.multimodal ? new HashSet<ContentType>(Arrays.asList(ContentType.TEXT, ContentType.IMAGE)) : Collections.singleton(ContentType.TEXT);
    }

    public Set<EmbeddingParameter<?>> supportedParameters() {
        return Collections.singleton(EmbeddingRequestParameters.INPUT_TYPE);
    }

    public dev.langchain4j.model.embedding.response.EmbeddingResponse doEmbed(dev.langchain4j.model.embedding.request.EmbeddingRequest request) {
        String effectiveInputType = (String)Utils.getOrDefault((Object)VoyageAiEmbeddingModel.toVoyageInputType(request.inputType()), (Object)this.inputType);
        ArrayList<Embedding> embeddings = new ArrayList<Embedding>();
        int totalTokens = 0;
        List inputs = request.inputs();
        String responseModelName = null;
        for (int i = 0; i < inputs.size(); i += this.maxSegmentsPerBatch.intValue()) {
            EmbeddingResponse wireResponse;
            Object wireRequest;
            List batch = inputs.subList(i, Math.min(i + this.maxSegmentsPerBatch, inputs.size()));
            if (this.multimodal) {
                wireRequest = MultimodalEmbeddingRequest.builder().inputs(batch.stream().map(this::toMultimodalInput).collect(Collectors.toList())).model(this.modelName).inputType(effectiveInputType).truncation(this.truncation).build();
                wireResponse = (EmbeddingResponse)RetryUtils.withRetryMappingExceptions(() -> this.lambda$doEmbed$1((MultimodalEmbeddingRequest)wireRequest), (int)this.maxRetries);
            } else {
                wireRequest = EmbeddingRequest.builder().input(batch.stream().map(EmbeddingInput::text).collect(Collectors.toList())).model(this.modelName).inputType(effectiveInputType).truncation(this.truncation).encodingFormat(this.encodingFormat).build();
                wireResponse = (EmbeddingResponse)RetryUtils.withRetryMappingExceptions(() -> this.lambda$doEmbed$2((EmbeddingRequest)wireRequest), (int)this.maxRetries);
            }
            embeddings.addAll(this.getEmbeddings(wireResponse));
            totalTokens += this.getTokenUsage(wireResponse).intValue();
            if (responseModelName != null) continue;
            responseModelName = wireResponse.getModel();
        }
        return dev.langchain4j.model.embedding.response.EmbeddingResponse.builder().embeddings(embeddings).metadata(EmbeddingResponseMetadata.builder().modelName((String)Utils.getOrDefault(responseModelName, (Object)this.modelName)).tokenUsage(new TokenUsage(Integer.valueOf(totalTokens))).build()).build();
    }

    private MultimodalEmbeddingRequest.MultimodalInput toMultimodalInput(EmbeddingInput input) {
        return new MultimodalEmbeddingRequest.MultimodalInput(input.contents().stream().map(this::toContentBlock).collect(Collectors.toList()));
    }

    private MultimodalEmbeddingRequest.ContentBlock toContentBlock(Content content) {
        if (content instanceof TextContent) {
            TextContent textContent = (TextContent)content;
            return MultimodalEmbeddingRequest.ContentBlock.text(textContent.text());
        }
        if (content instanceof ImageContent) {
            ImageContent imageContent = (ImageContent)content;
            Image image = imageContent.image();
            if (image.url() != null) {
                return MultimodalEmbeddingRequest.ContentBlock.imageUrl(image.url().toString());
            }
            if (image.base64Data() != null) {
                String dataUrl = "data:" + (String)Utils.getOrDefault((Object)image.mimeType(), (Object)"image/png") + ";base64," + image.base64Data();
                return MultimodalEmbeddingRequest.ContentBlock.imageBase64(dataUrl);
            }
            throw new UnsupportedFeatureException("ImageContent must have either a URL or base64 data");
        }
        throw new UnsupportedFeatureException("Unsupported content type: " + content.type());
    }

    private static String toVoyageInputType(EmbeddingInputType inputType) {
        if (inputType == null) {
            return null;
        }
        switch (inputType) {
            case QUERY: {
                return "query";
            }
            case DOCUMENT: {
                return "document";
            }
        }
        throw new IllegalArgumentException("Unknown input type: " + inputType);
    }

    private static boolean isMultimodalModel(String modelName) {
        return modelName != null && modelName.contains("multimodal");
    }

    public String modelName() {
        return this.modelName;
    }

    public List<EmbeddingModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.VOYAGE_AI;
    }

    protected Integer knownDimension() {
        return VoyageAiEmbeddingModelName.knownDimension(this.modelName);
    }

    private List<Embedding> getEmbeddings(EmbeddingResponse response) {
        return response.getData().stream().sorted(Comparator.comparingInt(EmbeddingResponse.EmbeddingData::getIndex)).map(EmbeddingResponse.EmbeddingData::getEmbedding).map(Embedding::from).collect(Collectors.toList());
    }

    private Integer getTokenUsage(EmbeddingResponse response) {
        if (response.getUsage() != null) {
            return response.getUsage().getTotalTokens();
        }
        return 0;
    }

    public static Builder builder() {
        return new Builder();
    }

    private /* synthetic */ EmbeddingResponse lambda$doEmbed$2(EmbeddingRequest wireRequest) throws Exception {
        return this.client.embed(wireRequest);
    }

    private /* synthetic */ EmbeddingResponse lambda$doEmbed$1(MultimodalEmbeddingRequest wireRequest) throws Exception {
        return this.client.multimodalEmbed(wireRequest);
    }

    public static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private Supplier<Map<String, String>> customHeadersSupplier;
        private String baseUrl;
        private Duration timeout;
        private Integer maxRetries;
        private String apiKey;
        private String modelName;
        private String inputType;
        private Boolean truncation;
        private String encodingFormat;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Integer maxSegmentsPerBatch;
        private Boolean multimodal;
        private List<EmbeddingModelListener> listeners;

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
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

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
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

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder modelName(VoyageAiEmbeddingModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder inputType(String inputType) {
            this.inputType = inputType;
            return this;
        }

        public Builder truncation(Boolean truncation) {
            this.truncation = truncation;
            return this;
        }

        public Builder encodingFormat(String encodingFormat) {
            this.encodingFormat = encodingFormat;
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

        public Builder maxSegmentsPerBatch(Integer maxSegmentsPerBatch) {
            this.maxSegmentsPerBatch = maxSegmentsPerBatch;
            return this;
        }

        public Builder multimodal(Boolean multimodal) {
            this.multimodal = multimodal;
            return this;
        }

        public Builder listeners(List<EmbeddingModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public VoyageAiEmbeddingModel build() {
            return new VoyageAiEmbeddingModel(this);
        }
    }
}

