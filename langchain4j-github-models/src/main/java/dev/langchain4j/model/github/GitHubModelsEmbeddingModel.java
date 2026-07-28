/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.inference.EmbeddingsClient
 *  com.azure.ai.inference.ModelServiceVersion
 *  com.azure.ai.inference.models.EmbeddingItem
 *  com.azure.ai.inference.models.EmbeddingsResult
 *  com.azure.core.http.ProxyOptions
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.github;

import com.azure.ai.inference.EmbeddingsClient;
import com.azure.ai.inference.ModelServiceVersion;
import com.azure.ai.inference.models.EmbeddingItem;
import com.azure.ai.inference.models.EmbeddingsResult;
import com.azure.core.http.ProxyOptions;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.github.GitHubModelsEmbeddingModelName;
import dev.langchain4j.model.github.InternalGitHubModelHelper;
import dev.langchain4j.model.github.spi.GitHubModelsEmbeddingModelBuilderFactory;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class GitHubModelsEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private static final Logger logger = LoggerFactory.getLogger(GitHubModelsEmbeddingModel.class);
    private static final int BATCH_SIZE = 16;
    private EmbeddingsClient client;
    private final String modelName;
    private final Integer dimensions;

    private GitHubModelsEmbeddingModel(EmbeddingsClient client, String modelName, Integer dimensions) {
        this(modelName, dimensions);
        this.client = client;
    }

    private GitHubModelsEmbeddingModel(String endpoint, ModelServiceVersion serviceVersion, String gitHubToken, String modelName, Duration timeout, Integer maxRetries, ProxyOptions proxyOptions, boolean logRequestsAndResponses, String userAgentSuffix, Integer dimensions, Map<String, String> customHeaders) {
        this(modelName, dimensions);
        this.client = InternalGitHubModelHelper.setupEmbeddingsBuilder(endpoint, serviceVersion, gitHubToken, timeout, maxRetries, proxyOptions, logRequestsAndResponses, userAgentSuffix, customHeaders).buildClient();
    }

    private GitHubModelsEmbeddingModel(String modelName, Integer dimensions) {
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.dimensions = dimensions;
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
            EmbeddingsResult result = this.client.embed(batch, this.dimensions, null, null, this.modelName, null);
            for (EmbeddingItem embeddingItem : result.getData()) {
                Embedding embedding = Embedding.from((List)embeddingItem.getEmbeddingList());
                embeddings.add(embedding);
            }
            inputTokenCount += result.getUsage().getPromptTokens();
        }
        return Response.from(embeddings, (TokenUsage)new TokenUsage(Integer.valueOf(inputTokenCount)));
    }

    protected Integer knownDimension() {
        if (this.dimensions != null) {
            return this.dimensions;
        }
        return GitHubModelsEmbeddingModelName.knownDimension(this.modelName);
    }

    public String modelName() {
        return this.modelName;
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(GitHubModelsEmbeddingModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            GitHubModelsEmbeddingModelBuilderFactory factory = (GitHubModelsEmbeddingModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public static class Builder {
        private String endpoint;
        private ModelServiceVersion serviceVersion;
        private String gitHubToken;
        private String modelName;
        private Duration timeout;
        private Integer maxRetries;
        private ProxyOptions proxyOptions;
        private boolean logRequestsAndResponses;
        private EmbeddingsClient embeddingsClient;
        private String userAgentSuffix;
        private Integer dimensions;
        private Map<String, String> customHeaders;

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder serviceVersion(ModelServiceVersion serviceVersion) {
            this.serviceVersion = serviceVersion;
            return this;
        }

        public Builder gitHubToken(String gitHubToken) {
            this.gitHubToken = gitHubToken;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder modelName(GitHubModelsEmbeddingModelName modelName) {
            this.modelName = modelName.toString();
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

        public Builder proxyOptions(ProxyOptions proxyOptions) {
            this.proxyOptions = proxyOptions;
            return this;
        }

        public Builder logRequestsAndResponses(boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
            return this;
        }

        public Builder embeddingsClient(EmbeddingsClient embeddingsClient) {
            this.embeddingsClient = embeddingsClient;
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

        public GitHubModelsEmbeddingModel build() {
            if (this.embeddingsClient == null) {
                return new GitHubModelsEmbeddingModel(this.endpoint, this.serviceVersion, this.gitHubToken, this.modelName, this.timeout, this.maxRetries, this.proxyOptions, this.logRequestsAndResponses, this.userAgentSuffix, this.dimensions, this.customHeaders);
            }
            return new GitHubModelsEmbeddingModel(this.embeddingsClient, this.modelName, this.dimensions);
        }
    }
}

