/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ContentType
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.embedding.listener.EmbeddingModelListener
 *  dev.langchain4j.model.embedding.request.EmbeddingInput
 *  dev.langchain4j.model.embedding.request.EmbeddingRequest
 *  dev.langchain4j.model.embedding.response.EmbeddingResponse
 *  dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata
 *  dev.langchain4j.model.output.TokenUsage
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.jina;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.embedding.listener.EmbeddingModelListener;
import dev.langchain4j.model.embedding.request.EmbeddingInput;
import dev.langchain4j.model.embedding.request.EmbeddingRequest;
import dev.langchain4j.model.embedding.response.EmbeddingResponse;
import dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata;
import dev.langchain4j.model.jina.internal.api.JinaEmbeddingRequest;
import dev.langchain4j.model.jina.internal.api.JinaEmbeddingResponse;
import dev.langchain4j.model.jina.internal.api.JinaMultimodalEmbeddingRequest;
import dev.langchain4j.model.jina.internal.client.JinaClient;
import dev.langchain4j.model.output.TokenUsage;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class JinaEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private static final String DEFAULT_BASE_URL = "https://api.jina.ai/";
    private final JinaClient client;
    private final String modelName;
    private final Integer maxRetries;
    private final Boolean lateChunking;
    private final List<EmbeddingModelListener> listeners;

    @Deprecated
    public JinaEmbeddingModel(String baseUrl, String apiKey, String modelName, Duration timeout, Integer maxRetries, Boolean lateChunking, Boolean logRequests, Boolean logResponses) {
        this.client = JinaClient.builder().baseUrl((String)Utils.getOrDefault((Object)baseUrl, (Object)DEFAULT_BASE_URL)).apiKey(apiKey).timeout((Duration)Utils.getOrDefault((Object)timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)logResponses, (Object)false)).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)maxRetries, (Object)2);
        this.lateChunking = (Boolean)Utils.getOrDefault((Object)lateChunking, (Object)false);
        this.listeners = Collections.emptyList();
    }

    public JinaEmbeddingModel(JinaEmbeddingModelBuilder builder) {
        this.client = JinaClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)DEFAULT_BASE_URL)).apiKey(builder.apiKey).timeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false)).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false)).logger(builder.logger).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.lateChunking = (Boolean)Utils.getOrDefault((Object)builder.lateChunking, (Object)false);
        this.listeners = Utils.copy((List)builder.listeners);
    }

    public List<EmbeddingModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.JINA;
    }

    public static JinaEmbeddingModelBuilder builder() {
        return new JinaEmbeddingModelBuilder();
    }

    public String modelName() {
        return this.modelName;
    }

    public Set<ContentType> supportedContentTypes() {
        return JinaEmbeddingModel.isMultimodalModel(this.modelName) ? new HashSet<ContentType>(Arrays.asList(ContentType.TEXT, ContentType.IMAGE)) : Collections.singleton(ContentType.TEXT);
    }

    public EmbeddingResponse doEmbed(EmbeddingRequest request) {
        JinaEmbeddingResponse response;
        Object wireRequest;
        if (JinaEmbeddingModel.isMultimodalModel(this.modelName)) {
            wireRequest = this.buildMultimodalRequest(request);
            response = (JinaEmbeddingResponse)RetryUtils.withRetryMappingExceptions(() -> this.lambda$doEmbed$0((JinaMultimodalEmbeddingRequest)wireRequest), (int)this.maxRetries);
        } else {
            wireRequest = JinaEmbeddingRequest.builder().model(this.modelName).lateChunking(this.lateChunking).input(request.inputs().stream().map(EmbeddingInput::text).collect(Collectors.toList())).build();
            response = (JinaEmbeddingResponse)RetryUtils.withRetryMappingExceptions(() -> this.lambda$doEmbed$1((JinaEmbeddingRequest)wireRequest), (int)this.maxRetries);
        }
        List embeddings = response.data == null ? Collections.emptyList() : response.data.stream().map(jinaEmbedding -> Embedding.from((float[])jinaEmbedding.embedding)).collect(Collectors.toList());
        TokenUsage tokenUsage = response.usage == null ? null : new TokenUsage(response.usage.promptTokens, Integer.valueOf(0), response.usage.totalTokens);
        return EmbeddingResponse.builder().embeddings(embeddings).metadata(EmbeddingResponseMetadata.builder().modelName((String)Utils.getOrDefault((Object)response.model, (Object)this.modelName)).tokenUsage(tokenUsage).build()).build();
    }

    JinaMultimodalEmbeddingRequest buildMultimodalRequest(EmbeddingRequest request) {
        return new JinaMultimodalEmbeddingRequest(this.modelName, request.inputs().stream().map(this::toMultimodalInput).collect(Collectors.toList()));
    }

    private JinaMultimodalEmbeddingRequest.JinaMultimodalInput toMultimodalInput(EmbeddingInput input) {
        String imageValue = null;
        boolean hasText = false;
        for (Content content : input.contents()) {
            if (content instanceof ImageContent) {
                ImageContent imageContent = (ImageContent)content;
                if (imageValue != null) {
                    throw new UnsupportedFeatureException("Jina embeds one image per input");
                }
                Image image = imageContent.image();
                if (image.url() != null) {
                    imageValue = image.url().toString();
                    continue;
                }
                if (image.base64Data() != null) {
                    imageValue = "data:" + (String)Utils.getOrDefault((Object)image.mimeType(), (Object)"image/png") + ";base64," + image.base64Data();
                    continue;
                }
                throw new UnsupportedFeatureException("ImageContent must have either a URL or base64 data");
            }
            hasText = true;
        }
        if (imageValue != null) {
            if (hasText) {
                throw new UnsupportedFeatureException("Jina embeds a single text or image per input; interleaved text+image is not supported");
            }
            return JinaMultimodalEmbeddingRequest.JinaMultimodalInput.image(imageValue);
        }
        return JinaMultimodalEmbeddingRequest.JinaMultimodalInput.text(input.text());
    }

    private static boolean isMultimodalModel(String modelName) {
        return modelName != null && (modelName.contains("clip") || modelName.contains("embeddings-v4"));
    }

    private /* synthetic */ JinaEmbeddingResponse lambda$doEmbed$1(JinaEmbeddingRequest wireRequest) throws Exception {
        return this.client.embed(wireRequest);
    }

    private /* synthetic */ JinaEmbeddingResponse lambda$doEmbed$0(JinaMultimodalEmbeddingRequest wireRequest) throws Exception {
        return this.client.embedMultimodal(wireRequest);
    }

    public static class JinaEmbeddingModelBuilder {
        private String baseUrl;
        private String apiKey;
        private String modelName;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean lateChunking;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private HttpClientBuilder httpClientBuilder;
        private List<EmbeddingModelListener> listeners;

        JinaEmbeddingModelBuilder() {
        }

        public JinaEmbeddingModelBuilder listeners(List<EmbeddingModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public JinaEmbeddingModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public JinaEmbeddingModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public JinaEmbeddingModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public JinaEmbeddingModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public JinaEmbeddingModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public JinaEmbeddingModelBuilder lateChunking(Boolean lateChunking) {
            this.lateChunking = lateChunking;
            return this;
        }

        public JinaEmbeddingModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public JinaEmbeddingModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public JinaEmbeddingModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public JinaEmbeddingModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public JinaEmbeddingModel build() {
            return new JinaEmbeddingModel(this);
        }

        public String toString() {
            return "JinaEmbeddingModel.JinaEmbeddingModelBuilder(baseUrl=" + this.baseUrl + ", apiKey=" + (this.apiKey == null ? null : "********") + ", modelName=" + this.modelName + ", timeout=" + this.timeout + ", maxRetries=" + this.maxRetries + ", lateChunking=" + this.lateChunking + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

