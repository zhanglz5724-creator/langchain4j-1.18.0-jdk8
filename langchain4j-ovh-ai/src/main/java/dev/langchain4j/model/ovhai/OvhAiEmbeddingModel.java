/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.embedding.EmbeddingModel
 *  dev.langchain4j.model.output.Response
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.ovhai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.ovhai.internal.api.EmbeddingRequest;
import dev.langchain4j.model.ovhai.internal.api.EmbeddingResponse;
import dev.langchain4j.model.ovhai.internal.client.DefaultOvhAiClient;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;

@Deprecated
public class OvhAiEmbeddingModel
implements EmbeddingModel {
    private final DefaultOvhAiClient client;
    private final int maxRetries;

    private OvhAiEmbeddingModel(OvhAiEmbeddingModelBuilder builder) {
        this.client = ((DefaultOvhAiClient.Builder)((DefaultOvhAiClient.Builder)((DefaultOvhAiClient.Builder)((DefaultOvhAiClient.Builder)((DefaultOvhAiClient.Builder)((DefaultOvhAiClient.Builder)DefaultOvhAiClient.builder().baseUrl(builder.baseUrl)).apiKey(builder.apiKey)).timeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L)))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).build();
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    @Deprecated
    public static OvhAiEmbeddingModel withApiKey(String apiKey) {
        return OvhAiEmbeddingModel.builder().apiKey(apiKey).build();
    }

    public static OvhAiEmbeddingModelBuilder builder() {
        return new OvhAiEmbeddingModelBuilder();
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        EmbeddingRequest request = EmbeddingRequest.builder().input(textSegments.stream().map(TextSegment::text).collect(Collectors.toList())).build();
        EmbeddingResponse response = (EmbeddingResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.embed(request), (int)this.maxRetries);
        List embeddings = response.getEmbeddings().stream().map(Embedding::from).collect(Collectors.toList());
        return Response.from(embeddings);
    }

    public static class OvhAiEmbeddingModelBuilder {
        private String baseUrl;
        private String apiKey;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;

        OvhAiEmbeddingModelBuilder() {
        }

        public OvhAiEmbeddingModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public OvhAiEmbeddingModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public OvhAiEmbeddingModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public OvhAiEmbeddingModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public OvhAiEmbeddingModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public OvhAiEmbeddingModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OvhAiEmbeddingModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public OvhAiEmbeddingModel build() {
            return new OvhAiEmbeddingModel(this);
        }

        public String toString() {
            return "OvhAiEmbeddingModel.OvhAiEmbeddingModelBuilder(baseUrl=" + this.baseUrl + ", apiKey=" + (this.apiKey == null ? null : "********") + ", timeout=" + this.timeout + ", maxRetries=" + this.maxRetries + ", logRequests=" + this.logRequests + ", logResponses=" + this.logResponses + ")";
        }
    }
}

