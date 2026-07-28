/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.openai.OpenAIClient
 *  com.azure.ai.openai.models.EmbeddingItem
 *  com.azure.ai.openai.models.Embeddings
 *  com.azure.ai.openai.models.EmbeddingsOptions
 *  com.azure.core.credential.KeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.core.http.HttpClientProvider
 *  com.azure.core.http.ProxyOptions
 *  com.azure.core.http.policy.RetryOptions
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.azure;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.azure.core.credential.KeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.core.http.HttpClientProvider;
import com.azure.core.http.ProxyOptions;
import com.azure.core.http.policy.RetryOptions;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.azure.AzureOpenAiEmbeddingModelName;
import dev.langchain4j.model.azure.AzureOpenAiExceptionMapper;
import dev.langchain4j.model.azure.InternalAzureOpenAiHelper;
import dev.langchain4j.model.azure.spi.AzureOpenAiEmbeddingModelBuilderFactory;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AzureOpenAiEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private static final int BATCH_SIZE = 16;
    private final OpenAIClient client;
    private final String deploymentName;
    private final Integer dimensions;

    public AzureOpenAiEmbeddingModel(Builder builder) {
        this.client = builder.openAIClient == null ? (builder.tokenCredential != null ? InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.tokenCredential, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders) : (builder.keyCredential != null ? InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.keyCredential, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders) : InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.apiKey, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders))) : (OpenAIClient)ValidationUtils.ensureNotNull((Object)builder.openAIClient, (String)"openAIClient");
        this.deploymentName = ValidationUtils.ensureNotBlank((String)builder.deploymentName, (String)"deploymentName");
        this.dimensions = builder.dimensions;
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        List<String> texts = textSegments.stream().map(TextSegment::text).collect(Collectors.toList());
        return this.embedTexts(texts);
    }

    private Response<List<Embedding>> embedTexts(List<String> texts) {
        ArrayList<Embedding> embeddings = new ArrayList<Embedding>();
        int inputTokenCount = 0;
        for (int i = 0; i < texts.size(); i += 16) {
            List<String> batch = texts.subList(i, Math.min(i + 16, texts.size()));
            EmbeddingsOptions options = new EmbeddingsOptions(batch).setDimensions(this.dimensions);
            Embeddings response = (Embeddings)AzureOpenAiExceptionMapper.INSTANCE.withExceptionMapper(() -> this.client.getEmbeddings(this.deploymentName, options));
            for (EmbeddingItem embeddingItem : response.getData()) {
                Embedding embedding = Embedding.from((List)embeddingItem.getEmbedding());
                embeddings.add(embedding);
            }
            inputTokenCount += response.getUsage().getPromptTokens();
        }
        return Response.from(embeddings, (TokenUsage)new TokenUsage(Integer.valueOf(inputTokenCount)));
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(AzureOpenAiEmbeddingModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            AzureOpenAiEmbeddingModelBuilderFactory factory = (AzureOpenAiEmbeddingModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    protected Integer knownDimension() {
        if (this.dimensions != null) {
            return this.dimensions;
        }
        return AzureOpenAiEmbeddingModelName.knownDimension(this.deploymentName);
    }

    public static class Builder {
        private String endpoint;
        private String serviceVersion;
        private String apiKey;
        private KeyCredential keyCredential;
        private TokenCredential tokenCredential;
        private HttpClientProvider httpClientProvider;
        private String deploymentName;
        private Duration timeout;
        private Integer maxRetries;
        private RetryOptions retryOptions;
        private ProxyOptions proxyOptions;
        private boolean logRequestsAndResponses;
        private OpenAIClient openAIClient;
        private String userAgentSuffix;
        private Integer dimensions;
        private Map<String, String> customHeaders;

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder serviceVersion(String serviceVersion) {
            this.serviceVersion = serviceVersion;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder nonAzureApiKey(String nonAzureApiKey) {
            this.keyCredential = new KeyCredential(nonAzureApiKey);
            this.endpoint = "https://api.openai.com/v1";
            return this;
        }

        public Builder tokenCredential(TokenCredential tokenCredential) {
            this.tokenCredential = tokenCredential;
            return this;
        }

        public Builder httpClientProvider(HttpClientProvider httpClientProvider) {
            this.httpClientProvider = httpClientProvider;
            return this;
        }

        public Builder deploymentName(String deploymentName) {
            this.deploymentName = deploymentName;
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

        public Builder retryOptions(RetryOptions retryOptions) {
            this.retryOptions = retryOptions;
            return this;
        }

        public Builder proxyOptions(ProxyOptions proxyOptions) {
            this.proxyOptions = proxyOptions;
            return this;
        }

        public Builder logRequestsAndResponses(boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
            return this;
        }

        public Builder openAIClient(OpenAIClient openAIClient) {
            this.openAIClient = openAIClient;
            return this;
        }

        public Builder userAgentSuffix(String userAgentSuffix) {
            this.userAgentSuffix = userAgentSuffix;
            return this;
        }

        public Builder dimensions(Integer dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public AzureOpenAiEmbeddingModel build() {
            return new AzureOpenAiEmbeddingModel(this);
        }
    }
}

