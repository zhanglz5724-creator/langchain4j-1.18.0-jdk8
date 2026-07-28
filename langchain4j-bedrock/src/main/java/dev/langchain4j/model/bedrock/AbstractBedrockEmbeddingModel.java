/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.embedding.listener.EmbeddingModelListener
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
 *  software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
 *  software.amazon.awssdk.core.SdkBytes
 *  software.amazon.awssdk.regions.Region
 *  software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient
 *  software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClientBuilder
 *  software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest
 *  software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.Internal;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.bedrock.BedrockEmbeddingResponse;
import dev.langchain4j.model.bedrock.BedrockExceptionMapper;
import dev.langchain4j.model.bedrock.Json;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.embedding.listener.EmbeddingModelListener;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClientBuilder;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

@Internal
abstract class AbstractBedrockEmbeddingModel<T extends BedrockEmbeddingResponse>
extends DimensionAwareEmbeddingModel {
    private static final Region DEFAULT_REGION = Region.US_EAST_1;
    private static final AwsCredentialsProvider DEFAULT_CREDENTIALS_PROVIDER = DefaultCredentialsProvider.builder().build();
    private static final Integer DEFAULT_MAX_RETRIES = 2;
    private volatile BedrockRuntimeClient client;
    private final Region region;
    private final AwsCredentialsProvider credentialsProvider;
    private final Integer maxRetries;
    private final List<EmbeddingModelListener> listeners;

    protected AbstractBedrockEmbeddingModel(AbstractBedrockEmbeddingModelBuilder<T, ?, ?> builder) {
        this.client = ((AbstractBedrockEmbeddingModelBuilder)builder).client;
        this.listeners = Utils.copy((List)((AbstractBedrockEmbeddingModelBuilder)builder).listeners);
        this.region = ((AbstractBedrockEmbeddingModelBuilder)builder).isRegionSet ? ((AbstractBedrockEmbeddingModelBuilder)builder).region : DEFAULT_REGION;
        this.credentialsProvider = ((AbstractBedrockEmbeddingModelBuilder)builder).isCredentialsProviderSet ? ((AbstractBedrockEmbeddingModelBuilder)builder).credentialsProvider : DEFAULT_CREDENTIALS_PROVIDER;
        this.maxRetries = ((AbstractBedrockEmbeddingModelBuilder)builder).isMaxRetriesSet ? ((AbstractBedrockEmbeddingModelBuilder)builder).maxRetries : DEFAULT_MAX_RETRIES;
    }

    protected Response<List<Embedding>> doEmbedAll(List<TextSegment> textSegments) {
        List<Map<String, Object>> requestParameters = this.getRequestParameters(textSegments);
        List responses = requestParameters.stream().map(Json::toJson).map(body -> (InvokeModelResponse)RetryUtils.withRetryMappingExceptions(() -> this.invoke((String)body), (int)this.maxRetries, (ExceptionMapper)BedrockExceptionMapper.INSTANCE)).map(invokeModelResponse -> invokeModelResponse.body().asUtf8String()).map(response -> (BedrockEmbeddingResponse)Json.fromJson(response, this.getResponseClassType())).collect(Collectors.toList());
        int totalInputToken = 0;
        ArrayList<Embedding> embeddings = new ArrayList<Embedding>();
        for (BedrockEmbeddingResponse response2 : responses) {
            embeddings.add(response2.toEmbedding());
            totalInputToken += response2.getInputTextTokenCount();
        }
        return Response.from(embeddings, (TokenUsage)new TokenUsage(Integer.valueOf(totalInputToken)));
    }

    protected abstract List<Map<String, Object>> getRequestParameters(List<TextSegment> var1);

    public List<EmbeddingModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.AMAZON_BEDROCK;
    }

    public String modelName() {
        return this.getModelId();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public BedrockRuntimeClient getClient() {
        if (this.client == null) {
            AbstractBedrockEmbeddingModel abstractBedrockEmbeddingModel = this;
            synchronized (abstractBedrockEmbeddingModel) {
                if (this.client == null) {
                    this.client = this.initClient();
                }
            }
        }
        return this.client;
    }

    protected abstract String getModelId();

    protected abstract Class<T> getResponseClassType();

    protected InvokeModelResponse invoke(String body) {
        InvokeModelRequest invokeModelRequest = (InvokeModelRequest)InvokeModelRequest.builder().modelId(this.getModelId()).body(SdkBytes.fromString((String)body, (Charset)Charset.defaultCharset())).build();
        return this.getClient().invokeModel(invokeModelRequest);
    }

    protected static Map<String, Object> of(String key, Object value) {
        HashMap<String, Object> map = new HashMap<String, Object>(1);
        map.put(key, value);
        return map;
    }

    private BedrockRuntimeClient initClient() {
        return (BedrockRuntimeClient)((BedrockRuntimeClientBuilder)((BedrockRuntimeClientBuilder)BedrockRuntimeClient.builder().region(this.region)).credentialsProvider(this.credentialsProvider)).build();
    }

    public Region getRegion() {
        return this.region;
    }

    public AwsCredentialsProvider getCredentialsProvider() {
        return this.credentialsProvider;
    }

    public Integer getMaxRetries() {
        return this.maxRetries;
    }

    public static abstract class AbstractBedrockEmbeddingModelBuilder<T extends BedrockEmbeddingResponse, C extends AbstractBedrockEmbeddingModel<T>, B extends AbstractBedrockEmbeddingModelBuilder<T, C, B>> {
        private BedrockRuntimeClient client;
        private Region region;
        private boolean isRegionSet;
        private AwsCredentialsProvider credentialsProvider;
        private boolean isCredentialsProviderSet;
        private Integer maxRetries;
        private boolean isMaxRetriesSet;
        private List<EmbeddingModelListener> listeners;

        public B client(BedrockRuntimeClient client) {
            this.client = client;
            return this.self();
        }

        public B listeners(List<EmbeddingModelListener> listeners) {
            this.listeners = listeners;
            return this.self();
        }

        public B region(Region region) {
            this.region = region;
            this.isRegionSet = true;
            return this.self();
        }

        public B credentialsProvider(AwsCredentialsProvider credentialsProvider) {
            this.credentialsProvider = credentialsProvider;
            this.isCredentialsProviderSet = true;
            return this.self();
        }

        public B maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            this.isMaxRetriesSet = true;
            return this.self();
        }

        protected abstract B self();

        public abstract C build();
    }
}

