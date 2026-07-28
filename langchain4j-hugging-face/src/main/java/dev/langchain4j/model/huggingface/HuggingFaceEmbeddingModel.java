/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.huggingface;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.huggingface.FactoryCreator;
import dev.langchain4j.model.huggingface.client.EmbeddingRequest;
import dev.langchain4j.model.huggingface.client.HuggingFaceClient;
import dev.langchain4j.model.huggingface.spi.HuggingFaceClientFactory;
import dev.langchain4j.model.huggingface.spi.HuggingFaceEmbeddingModelBuilderFactory;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class HuggingFaceEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15L);
    private HuggingFaceClient client;
    private final boolean waitForModel;
    private final String modelId;
    private final String baseUrl;

    public HuggingFaceEmbeddingModel(String baseUrl, String accessToken, String modelId, Boolean waitForModel, Duration timeout) {
        this(new HuggingFaceEmbeddingModelBuilder().baseUrl(baseUrl).accessToken(accessToken).modelId(modelId).waitForModel(waitForModel).timeout(timeout));
    }

    public HuggingFaceEmbeddingModel(String accessToken, String modelId, Boolean waitForModel, Duration timeout) {
        this(new HuggingFaceEmbeddingModelBuilder().accessToken(accessToken).modelId(modelId).waitForModel(waitForModel).timeout(timeout));
    }

    public HuggingFaceEmbeddingModel(HuggingFaceEmbeddingModelBuilder builder) {
        ValidationUtils.ensureNotBlank((String)builder.accessToken, (String)"%s", (Object[])new Object[]{"HuggingFace access token must be defined. It can be generated here: https://huggingface.co/settings/tokens"});
        this.waitForModel = builder.waitForModel == null || builder.waitForModel != false;
        this.baseUrl = builder.baseUrl;
        this.modelId = builder.modelId;
        this.client = this.createClient(builder.httpClientBuilder, builder.accessToken, builder.modelId, builder.timeout);
    }

    private HuggingFaceClient createClient(final HttpClientBuilder httpClientBuilder, final String accessToken, final String modelId, final Duration timeout) {
        return FactoryCreator.FACTORY.create(new HuggingFaceClientFactory.Input(){

            @Override
            public String baseUrl() {
                return HuggingFaceEmbeddingModel.this.baseUrl;
            }

            @Override
            public String apiKey() {
                return accessToken;
            }

            @Override
            public String modelId() {
                return modelId;
            }

            @Override
            public Duration timeout() {
                return timeout == null ? DEFAULT_TIMEOUT : timeout;
            }

            @Override
            public HttpClientBuilder httpClientBuilder() {
                return httpClientBuilder;
            }
        });
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        List<String> texts = textSegments.stream().map(TextSegment::text).collect(Collectors.toList());
        return this.embedTexts(texts);
    }

    private Response<List<Embedding>> embedTexts(List<String> texts) {
        EmbeddingRequest request = new EmbeddingRequest(texts, this.waitForModel);
        List<float[]> response = this.client.embed(request);
        List embeddings = response.stream().map(Embedding::from).collect(Collectors.toList());
        return Response.from(embeddings);
    }

    public static HuggingFaceEmbeddingModel withAccessToken(String accessToken) {
        return HuggingFaceEmbeddingModel.builder().accessToken(accessToken).build();
    }

    public static HuggingFaceEmbeddingModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(HuggingFaceEmbeddingModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            HuggingFaceEmbeddingModelBuilderFactory factory = (HuggingFaceEmbeddingModelBuilderFactory)iterator.next();
            return (HuggingFaceEmbeddingModelBuilder)factory.get();
        }
        return new HuggingFaceEmbeddingModelBuilder();
    }

    public static class HuggingFaceEmbeddingModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String accessToken;
        private String modelId;
        private Boolean waitForModel;
        private Duration timeout;

        public HuggingFaceEmbeddingModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public HuggingFaceEmbeddingModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public HuggingFaceEmbeddingModelBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public HuggingFaceEmbeddingModelBuilder modelId(String modelId) {
            this.modelId = modelId;
            return this;
        }

        public HuggingFaceEmbeddingModelBuilder waitForModel(Boolean waitForModel) {
            this.waitForModel = waitForModel;
            return this;
        }

        public HuggingFaceEmbeddingModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public HuggingFaceEmbeddingModel build() {
            return new HuggingFaceEmbeddingModel(this);
        }

        public String toString() {
            return "HuggingFaceEmbeddingModel.HuggingFaceEmbeddingModelBuilder(baseUrl=" + this.baseUrl + ", accessToken=" + (this.accessToken == null ? null : "********") + ", modelId=" + this.modelId + ", waitForModel=" + this.waitForModel + ", timeout=" + this.timeout + ")";
        }
    }
}

