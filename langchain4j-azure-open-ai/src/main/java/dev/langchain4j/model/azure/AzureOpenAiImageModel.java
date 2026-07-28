/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.ai.openai.OpenAIClient
 *  com.azure.ai.openai.models.ImageGenerationData
 *  com.azure.ai.openai.models.ImageGenerationOptions
 *  com.azure.ai.openai.models.ImageGenerationQuality
 *  com.azure.ai.openai.models.ImageGenerationResponseFormat
 *  com.azure.ai.openai.models.ImageGenerationStyle
 *  com.azure.ai.openai.models.ImageGenerations
 *  com.azure.ai.openai.models.ImageSize
 *  com.azure.core.credential.KeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.core.http.HttpClientProvider
 *  com.azure.core.http.ProxyOptions
 *  com.azure.core.http.policy.RetryOptions
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.image.ImageModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.azure;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.ImageGenerationData;
import com.azure.ai.openai.models.ImageGenerationOptions;
import com.azure.ai.openai.models.ImageGenerationQuality;
import com.azure.ai.openai.models.ImageGenerationResponseFormat;
import com.azure.ai.openai.models.ImageGenerationStyle;
import com.azure.ai.openai.models.ImageGenerations;
import com.azure.ai.openai.models.ImageSize;
import com.azure.core.credential.KeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.core.http.HttpClientProvider;
import com.azure.core.http.ProxyOptions;
import com.azure.core.http.policy.RetryOptions;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.azure.AzureOpenAiExceptionMapper;
import dev.langchain4j.model.azure.InternalAzureOpenAiHelper;
import dev.langchain4j.model.azure.spi.AzureOpenAiImageModelBuilderFactory;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;

public class AzureOpenAiImageModel
implements ImageModel {
    private final OpenAIClient client;
    private final String deploymentName;
    private final ImageGenerationQuality quality;
    private final ImageSize size;
    private final String user;
    private final ImageGenerationStyle style;
    private final ImageGenerationResponseFormat responseFormat;

    public AzureOpenAiImageModel(Builder builder) {
        this.client = builder.openAIClient == null ? (builder.tokenCredential != null ? InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.tokenCredential, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders) : (builder.keyCredential != null ? InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.keyCredential, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders) : InternalAzureOpenAiHelper.setupSyncClient(builder.endpoint, builder.serviceVersion, builder.apiKey, builder.timeout, builder.maxRetries, builder.retryOptions, builder.httpClientProvider, builder.proxyOptions, builder.logRequestsAndResponses, builder.userAgentSuffix, builder.customHeaders))) : (OpenAIClient)ValidationUtils.ensureNotNull((Object)builder.openAIClient, (String)"openAIClient");
        this.deploymentName = ValidationUtils.ensureNotBlank((String)builder.deploymentName, (String)"deploymentName");
        this.quality = builder.quality != null ? ImageGenerationQuality.fromString((String)builder.quality) : null;
        this.size = builder.size != null ? ImageSize.fromString((String)builder.size) : null;
        this.user = builder.user;
        this.style = builder.style != null ? ImageGenerationStyle.fromString((String)builder.style) : null;
        this.responseFormat = builder.responseFormat != null ? ImageGenerationResponseFormat.fromString((String)builder.responseFormat) : null;
    }

    public Response<Image> generate(String prompt) {
        ImageGenerationOptions options = new ImageGenerationOptions(prompt).setModel(this.deploymentName).setQuality(this.quality).setSize(this.size).setUser(this.user).setStyle(this.style).setResponseFormat(this.responseFormat);
        ImageGenerations imageGenerations = (ImageGenerations)AzureOpenAiExceptionMapper.INSTANCE.withExceptionMapper(() -> this.client.getImageGenerations(this.deploymentName, options));
        Image image = InternalAzureOpenAiHelper.imageFrom((ImageGenerationData)imageGenerations.getData().get(0));
        return Response.from((Object)image);
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(AzureOpenAiImageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            AzureOpenAiImageModelBuilderFactory factory = (AzureOpenAiImageModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public static class Builder {
        private String endpoint;
        private String serviceVersion;
        private String apiKey;
        private KeyCredential keyCredential;
        private TokenCredential tokenCredential;
        private HttpClientProvider httpClientProvider;
        private String deploymentName;
        private String quality;
        private String size;
        private String user;
        private String style;
        private String responseFormat;
        private Duration timeout;
        private Integer maxRetries;
        private RetryOptions retryOptions;
        private ProxyOptions proxyOptions;
        private boolean logRequestsAndResponses;
        private OpenAIClient openAIClient;
        private String userAgentSuffix;
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

        public Builder quality(String quality) {
            this.quality = quality;
            return this;
        }

        public Builder quality(ImageGenerationQuality imageGenerationQuality) {
            this.quality = imageGenerationQuality.toString();
            return this;
        }

        public Builder size(String size) {
            this.size = size;
            return this;
        }

        public Builder size(ImageSize imageSize) {
            this.size = imageSize.toString();
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder style(String style) {
            this.style = style;
            return this;
        }

        public Builder style(ImageGenerationStyle imageGenerationStyle) {
            this.style = imageGenerationStyle.toString();
            return this;
        }

        public Builder responseFormat(String responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public Builder responseFormat(ImageGenerationResponseFormat imageGenerationResponseFormat) {
            this.responseFormat = imageGenerationResponseFormat.toString();
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

        public Builder logRequestsAndResponses(Boolean logRequestsAndResponses) {
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

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public AzureOpenAiImageModel build() {
            return new AzureOpenAiImageModel(this);
        }
    }
}

