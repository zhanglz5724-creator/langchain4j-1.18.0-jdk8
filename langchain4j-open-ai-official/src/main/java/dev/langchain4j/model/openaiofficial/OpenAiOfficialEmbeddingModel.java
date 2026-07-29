/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.openai.azure.AzureOpenAIServiceVersion
 *  com.openai.client.OpenAIClient
 *  com.openai.credential.Credential
 *  com.openai.models.embeddings.CreateEmbeddingResponse
 *  com.openai.models.embeddings.EmbeddingCreateParams
 *  com.openai.models.embeddings.EmbeddingCreateParams$Builder
 *  com.openai.models.embeddings.EmbeddingCreateParams$Input
 *  com.openai.models.embeddings.EmbeddingModel
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.openaiofficial;

import com.openai.azure.AzureOpenAIServiceVersion;
import com.openai.client.OpenAIClient;
import com.openai.credential.Credential;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.EmbeddingCreateParams;
import com.openai.models.embeddings.EmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.openaiofficial.InternalOpenAiOfficialHelper;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialEmbeddingModelName;
import dev.langchain4j.model.openaiofficial.setup.OpenAiOfficialSetup;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.net.Proxy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OpenAiOfficialEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private final OpenAIClient client;
    private final String modelName;
    private final Integer dimensions;
    private final String user;
    private final Integer maxSegmentsPerBatch;

    public OpenAiOfficialEmbeddingModel(Builder builder) {
        this.client = builder.openAIClient != null ? builder.openAIClient : OpenAiOfficialSetup.setupSyncClient(builder.baseUrl, builder.apiKey, builder.credential, builder.microsoftFoundryDeploymentName, builder.azureOpenAIServiceVersion, builder.organizationId, builder.isMicrosoftFoundry, builder.isGitHubModels, builder.modelName, builder.timeout, builder.maxRetries, builder.proxy, builder.customHeaders);
        this.modelName = builder.modelName;
        this.dimensions = (Integer)Utils.getOrDefault((Object)builder.dimensions, (Object)this.knownDimension());
        this.user = builder.user;
        this.maxSegmentsPerBatch = (Integer)Utils.getOrDefault((Object)builder.maxSegmentsPerBatch, (Object)2048);
        ValidationUtils.ensureGreaterThanZero((Integer)this.maxSegmentsPerBatch, (String)"maxSegmentsPerBatch");
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        List<String> texts = textSegments.stream().map(TextSegment::text).collect(Collectors.toList());
        List<List<String>> textBatches = this.partition(texts, this.maxSegmentsPerBatch);
        return this.embedBatchedTexts(textBatches);
    }

    public String modelName() {
        return this.modelName;
    }

    private List<List<String>> partition(List<String> inputList, int size) {
        ArrayList<List<String>> result = new ArrayList<List<String>>();
        for (int i = 0; i < inputList.size(); i += size) {
            int toIndex = Math.min(i + size, inputList.size());
            result.add(inputList.subList(i, toIndex));
        }
        return result;
    }

    private Response<List<Embedding>> embedBatchedTexts(List<List<String>> textBatches) {
        ArrayList<Response<List<Embedding>>> responses = new ArrayList<Response<List<Embedding>>>();
        for (List<String> batch : textBatches) {
            Response<List<Embedding>> response2 = this.embedTexts(batch);
            responses.add(response2);
        }
        return Response.from(responses.stream().flatMap(response -> response.content().stream()).collect(Collectors.toList()), responses.stream().map(Response::tokenUsage).filter(Objects::nonNull).reduce(TokenUsage::add).orElse(null));
    }

    private Response<List<Embedding>> embedTexts(List<String> texts) {
        EmbeddingCreateParams.Input input = EmbeddingCreateParams.Input.ofArrayOfStrings(texts);
        EmbeddingCreateParams.Builder embeddingCreateParamsBuilder = EmbeddingCreateParams.builder();
        embeddingCreateParamsBuilder.input(input);
        embeddingCreateParamsBuilder.model(this.modelName);
        if (this.user != null) {
            embeddingCreateParamsBuilder.user(this.user);
        }
        if (this.dimensions != null) {
            embeddingCreateParamsBuilder.dimensions((long)this.dimensions.intValue());
        }
        CreateEmbeddingResponse createEmbeddingResponse = this.client.embeddings().create(embeddingCreateParamsBuilder.build());
        List<Embedding> embeddings = createEmbeddingResponse.data().stream().map(embeddingItem -> Embedding.from(embeddingItem.embedding())).collect(Collectors.toList());
        return Response.from(embeddings, (TokenUsage)InternalOpenAiOfficialHelper.tokenUsageFrom(createEmbeddingResponse.usage()));
    }

    public static Builder builder() {
        return new Builder();
    }

    protected Integer knownDimension() {
        if (this.dimensions != null) {
            return this.dimensions;
        }
        return OpenAiOfficialEmbeddingModelName.knownDimension(this.modelName);
    }

    public static class Builder {
        private String baseUrl;
        private String apiKey;
        private Credential credential;
        private String microsoftFoundryDeploymentName;
        private AzureOpenAIServiceVersion azureOpenAIServiceVersion;
        private String organizationId;
        private boolean isMicrosoftFoundry;
        private boolean isGitHubModels;
        private OpenAIClient openAIClient;
        private String modelName;
        private Integer dimensions;
        private String user;
        private Integer maxSegmentsPerBatch;
        private Duration timeout;
        private Integer maxRetries;
        private Proxy proxy;
        private Map<String, String> customHeaders;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder credential(Credential credential) {
            this.credential = credential;
            return this;
        }

        @Deprecated
        public Builder azureDeploymentName(String azureDeploymentName) {
            this.microsoftFoundryDeploymentName = azureDeploymentName;
            return this;
        }

        public Builder microsoftFoundryDeploymentName(String microsoftFoundryDeploymentName) {
            this.microsoftFoundryDeploymentName = microsoftFoundryDeploymentName;
            return this;
        }

        public Builder azureOpenAIServiceVersion(AzureOpenAIServiceVersion azureOpenAIServiceVersion) {
            this.azureOpenAIServiceVersion = azureOpenAIServiceVersion;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        @Deprecated
        public Builder isAzure(boolean isAzure) {
            this.isMicrosoftFoundry = isAzure;
            return this;
        }

        public Builder isMicrosoftFoundry(boolean isMicrosoftFoundry) {
            this.isMicrosoftFoundry = isMicrosoftFoundry;
            return this;
        }

        public Builder isGitHubModels(boolean isGitHubModels) {
            this.isGitHubModels = isGitHubModels;
            return this;
        }

        public Builder openAIClient(OpenAIClient openAIClient) {
            this.openAIClient = openAIClient;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder modelName(EmbeddingModel modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public Builder dimensions(Integer dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder maxSegmentsPerBatch(Integer maxSegmentsPerBatch) {
            this.maxSegmentsPerBatch = maxSegmentsPerBatch;
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

        public Builder proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public OpenAiOfficialEmbeddingModel build() {
            return new OpenAiOfficialEmbeddingModel(this);
        }
    }
}

