/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.mistralai;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModelCard;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModelResponse;
import dev.langchain4j.model.mistralai.internal.client.MistralAiClient;
import dev.langchain4j.model.mistralai.spi.MistralAiModelsBuilderFactory;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;

public class MistralAiModels {
    private final MistralAiClient client;
    private final Integer maxRetries;

    public MistralAiModels(MistralAiModelsBuilder builder) {
        this.client = ((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)MistralAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.mistral.ai/v1"))).apiKey(builder.apiKey)).timeout(builder.timeout)).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).customHeaders(builder.customHeadersSupplier)).build();
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    public static MistralAiModels withApiKey(String apiKey) {
        return MistralAiModels.builder().apiKey(apiKey).build();
    }

    public Response<List<MistralAiModelCard>> availableModels() {
        MistralAiModelResponse response = (MistralAiModelResponse)RetryUtils.withRetryMappingExceptions(this.client::listModels, (int)this.maxRetries);
        return Response.from(response.getData());
    }

    public static MistralAiModelsBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(MistralAiModelsBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            MistralAiModelsBuilderFactory factory = (MistralAiModelsBuilderFactory)iterator.next();
            return (MistralAiModelsBuilder)factory.get();
        }
        return new MistralAiModelsBuilder();
    }

    public static class MistralAiModelsBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Integer maxRetries;
        private Supplier<Map<String, String>> customHeadersSupplier;

        public MistralAiModelsBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public MistralAiModelsBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public MistralAiModelsBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public MistralAiModelsBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public MistralAiModelsBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public MistralAiModelsBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public MistralAiModelsBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public MistralAiModelsBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public MistralAiModelsBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public MistralAiModels build() {
            return new MistralAiModels(this);
        }
    }
}

