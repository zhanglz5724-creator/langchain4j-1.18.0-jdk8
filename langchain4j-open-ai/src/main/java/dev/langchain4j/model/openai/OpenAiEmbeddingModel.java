/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.embedding.listener.EmbeddingModelListener
 *  dev.langchain4j.model.embedding.request.EmbeddingInput
 *  dev.langchain4j.model.embedding.request.EmbeddingParameter
 *  dev.langchain4j.model.embedding.request.EmbeddingRequest
 *  dev.langchain4j.model.embedding.request.EmbeddingRequestParameters
 *  dev.langchain4j.model.embedding.response.EmbeddingResponse
 *  dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.embedding.listener.EmbeddingModelListener;
import dev.langchain4j.model.embedding.request.EmbeddingInput;
import dev.langchain4j.model.embedding.request.EmbeddingParameter;
import dev.langchain4j.model.embedding.request.EmbeddingRequestParameters;
import dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;
import dev.langchain4j.model.openai.OpenAiEmbeddingRequestParameters;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.OpenAiUtils;
import dev.langchain4j.model.openai.internal.embedding.EmbeddingRequest;
import dev.langchain4j.model.openai.internal.embedding.EmbeddingResponse;
import dev.langchain4j.model.openai.spi.OpenAiEmbeddingModelBuilderFactory;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class OpenAiEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private final OpenAiClient client;
    private final String modelName;
    private final Integer dimensions;
    private final String user;
    private final Integer maxRetries;
    private final Integer maxSegmentsPerBatch;
    private final String encodingFormat;
    private final Map<String, Object> customParameters;
    private final List<EmbeddingModelListener> listeners;

    public OpenAiEmbeddingModel(OpenAiEmbeddingModelBuilder builder) {
        this.client = ((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)OpenAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.openai.com/v1"))).apiKey(builder.apiKey)).organizationId(builder.organizationId)).projectId(builder.projectId)).connectTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(15L)))).readTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L)))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).userAgent("langchain4j-openai")).customHeaders(builder.customHeadersSupplier)).customQueryParams(builder.customQueryParams)).build();
        this.modelName = builder.modelName;
        this.dimensions = builder.dimensions;
        this.user = builder.user;
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.maxSegmentsPerBatch = (Integer)Utils.getOrDefault((Object)builder.maxSegmentsPerBatch, (Object)2048);
        this.encodingFormat = builder.encodingFormat;
        this.customParameters = builder.customParameters == null ? null : Collections.unmodifiableMap(new LinkedHashMap(builder.customParameters));
        this.listeners = Utils.copy((List)builder.listeners);
        ValidationUtils.ensureGreaterThanZero((Integer)this.maxSegmentsPerBatch, (String)"maxSegmentsPerBatch");
    }

    public List<EmbeddingModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.OPEN_AI;
    }

    protected Integer knownDimension() {
        if (this.dimensions != null) {
            return this.dimensions;
        }
        return OpenAiEmbeddingModelName.knownDimension(this.modelName());
    }

    public String modelName() {
        return this.modelName;
    }

    public Set<EmbeddingParameter<?>> supportedParameters() {
        return new HashSet(Arrays.asList(EmbeddingRequestParameters.MODEL_NAME, EmbeddingRequestParameters.DIMENSIONS, OpenAiEmbeddingRequestParameters.USER, OpenAiEmbeddingRequestParameters.ENCODING_FORMAT, OpenAiEmbeddingRequestParameters.CUSTOM_PARAMETERS));
    }

    public EmbeddingRequestParameters defaultRequestParameters() {
        return ((OpenAiEmbeddingRequestParameters.Builder)((OpenAiEmbeddingRequestParameters.Builder)OpenAiEmbeddingRequestParameters.builder().modelName(this.modelName)).dimensions(this.dimensions)).user(this.user).encodingFormat(this.encodingFormat).customParameters(this.customParameters).build();
    }

    public dev.langchain4j.model.embedding.response.EmbeddingResponse doEmbed(dev.langchain4j.model.embedding.request.EmbeddingRequest request) {
        EmbeddingRequestParameters parameters = request.parameters();
        List<String> texts = request.inputs().stream().map(EmbeddingInput::text).collect(Collectors.toList());
        List<List<String>> textBatches = this.partition(texts, this.maxSegmentsPerBatch);
        ArrayList<EmbeddedBatch> responses = new ArrayList<EmbeddedBatch>();
        for (List<String> batch2 : textBatches) {
            responses.add(this.embedTexts(batch2, parameters));
        }
        List embeddings = responses.stream().flatMap(batch -> batch.embeddings().stream()).collect(Collectors.toList());
        TokenUsage tokenUsage = responses.stream().map(EmbeddedBatch::tokenUsage).filter(Objects::nonNull).reduce(TokenUsage::add).orElse(null);
        String responseModelName = responses.stream().map(EmbeddedBatch::modelName).filter(Objects::nonNull).findFirst().orElse(null);
        return dev.langchain4j.model.embedding.response.EmbeddingResponse.builder().embeddings(embeddings).metadata(EmbeddingResponseMetadata.builder().modelName((String)Utils.getOrDefault((Object)responseModelName, (Object)Utils.getOrDefault((Object)parameters.modelName(), (Object)this.modelName))).tokenUsage(tokenUsage).build()).build();
    }

    private List<List<String>> partition(List<String> inputList, int size) {
        ArrayList<List<String>> result = new ArrayList<List<String>>();
        for (int i = 0; i < inputList.size(); i += size) {
            int fromIndex = i;
            int toIndex = Math.min(i + size, inputList.size());
            result.add(inputList.subList(fromIndex, toIndex));
        }
        return result;
    }

    private EmbeddedBatch embedTexts(List<String> texts, EmbeddingRequestParameters parameters) {
        EmbeddingRequest request = EmbeddingRequest.builder().input(texts).model(parameters.modelName()).dimensions(parameters.dimensions()).user((String)parameters.parameter(OpenAiEmbeddingRequestParameters.USER)).encodingFormat((String)parameters.parameter(OpenAiEmbeddingRequestParameters.ENCODING_FORMAT)).customParameters((Map)parameters.parameter(OpenAiEmbeddingRequestParameters.CUSTOM_PARAMETERS)).build();
        EmbeddingResponse response = (EmbeddingResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.embedding(request).execute(), (int)this.maxRetries);
        List<Embedding> embeddings = response.data().stream().map(openAiEmbedding -> Embedding.from(openAiEmbedding.embedding())).collect(Collectors.toList());
        return new EmbeddedBatch(embeddings, OpenAiUtils.tokenUsageFrom(response.usage()), response.model());
    }

    public static OpenAiEmbeddingModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OpenAiEmbeddingModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OpenAiEmbeddingModelBuilderFactory factory = (OpenAiEmbeddingModelBuilderFactory)iterator.next();
            return (OpenAiEmbeddingModelBuilder)factory.get();
        }
        return new OpenAiEmbeddingModelBuilder();
    }

    public static class OpenAiEmbeddingModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String organizationId;
        private String projectId;
        private String modelName;
        private Integer dimensions;
        private String user;
        private Duration timeout;
        private Integer maxRetries;
        private Integer maxSegmentsPerBatch;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Supplier<Map<String, String>> customHeadersSupplier;
        private Map<String, String> customQueryParams;
        private String encodingFormat;
        private Map<String, Object> customParameters;
        private List<EmbeddingModelListener> listeners;

        public OpenAiEmbeddingModelBuilder listeners(List<EmbeddingModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public OpenAiEmbeddingModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public OpenAiEmbeddingModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public OpenAiEmbeddingModelBuilder modelName(OpenAiEmbeddingModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public OpenAiEmbeddingModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public OpenAiEmbeddingModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public OpenAiEmbeddingModelBuilder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public OpenAiEmbeddingModelBuilder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public OpenAiEmbeddingModelBuilder dimensions(Integer dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public OpenAiEmbeddingModelBuilder user(String user) {
            this.user = user;
            return this;
        }

        public OpenAiEmbeddingModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public OpenAiEmbeddingModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public OpenAiEmbeddingModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public OpenAiEmbeddingModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OpenAiEmbeddingModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public OpenAiEmbeddingModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public OpenAiEmbeddingModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public OpenAiEmbeddingModelBuilder customQueryParams(Map<String, String> customQueryParams) {
            this.customQueryParams = customQueryParams;
            return this;
        }

        public OpenAiEmbeddingModelBuilder maxSegmentsPerBatch(Integer maxSegmentsPerBatch) {
            this.maxSegmentsPerBatch = maxSegmentsPerBatch;
            return this;
        }

        public OpenAiEmbeddingModelBuilder encodingFormat(String encodingFormat) {
            this.encodingFormat = encodingFormat;
            return this;
        }

        public OpenAiEmbeddingModelBuilder customParameters(Map<String, Object> customParameters) {
            this.customParameters = customParameters;
            return this;
        }

        public OpenAiEmbeddingModel build() {
            return new OpenAiEmbeddingModel(this);
        }
    }

    private static class EmbeddedBatch {
        private final List<Embedding> embeddings;
        private final TokenUsage tokenUsage;
        private final String modelName;

        EmbeddedBatch(List<Embedding> embeddings, TokenUsage tokenUsage, String modelName) {
            this.embeddings = embeddings;
            this.tokenUsage = tokenUsage;
            this.modelName = modelName;
        }

        List<Embedding> embeddings() {
            return this.embeddings;
        }

        TokenUsage tokenUsage() {
            return this.tokenUsage;
        }

        String modelName() {
            return this.modelName;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof EmbeddedBatch)) {
                return false;
            }
            EmbeddedBatch that = (EmbeddedBatch)o;
            return Objects.equals(this.embeddings, that.embeddings) && Objects.equals(this.tokenUsage, that.tokenUsage) && Objects.equals(this.modelName, that.modelName);
        }

        public int hashCode() {
            return Objects.hash(this.embeddings, this.tokenUsage, this.modelName);
        }

        public String toString() {
            return "EmbeddedBatch{embeddings=" + this.embeddings + ", tokenUsage=" + this.tokenUsage + ", modelName='" + this.modelName + '\'' + '}';
        }
    }
}

