/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.moderation.Moderation
 *  dev.langchain4j.model.moderation.ModerationModel
 *  dev.langchain4j.model.moderation.ModerationRequest
 *  dev.langchain4j.model.moderation.ModerationResponse
 *  dev.langchain4j.model.moderation.listener.ModerationModelListener
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.mistralai;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.mistralai.internal.api.MistralAiCategories;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModerationRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModerationResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModerationResult;
import dev.langchain4j.model.mistralai.internal.client.MistralAiClient;
import dev.langchain4j.model.moderation.Moderation;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.moderation.ModerationRequest;
import dev.langchain4j.model.moderation.ModerationResponse;
import dev.langchain4j.model.moderation.listener.ModerationModelListener;
import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;

public class MistralAiModerationModel
implements ModerationModel {
    private final MistralAiClient client;
    private final String modelName;
    private final Integer maxRetries;
    private final List<ModerationModelListener> listeners;

    public MistralAiModerationModel(Builder builder) {
        this.client = ((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)((MistralAiClient.Builder)MistralAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.mistral.ai/v1"))).apiKey(builder.apiKey)).timeout(builder.timeout)).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.listeners = Utils.copy((List)builder.listeners);
    }

    public List<ModerationModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.MISTRAL_AI;
    }

    public String modelName() {
        return this.modelName;
    }

    public ModerationResponse doModerate(ModerationRequest moderationRequest) {
        MistralAiModerationRequest request = MistralAiModerationRequest.builder().model(moderationRequest.modelName()).input(moderationRequest.texts()).build();
        MistralAiModerationResponse response = (MistralAiModerationResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.moderation(request), (int)this.maxRetries);
        List texts = moderationRequest.texts();
        List<MistralAiModerationResult> results = response.results();
        int flaggedIndex = this.findFirstFlaggedIndex(results);
        Moderation moderation = flaggedIndex >= 0 ? Moderation.flagged((String)((String)texts.get(flaggedIndex))) : Moderation.notFlagged();
        return ModerationResponse.builder().moderation(moderation).build();
    }

    private int findFirstFlaggedIndex(List<MistralAiModerationResult> results) {
        for (int i = 0; i < results.size(); ++i) {
            MistralAiCategories categories = results.get(i).getCategories();
            if (categories == null || !this.isAnyCategoryFlagged(categories)) continue;
            return i;
        }
        return -1;
    }

    private boolean isAnyCategoryFlagged(MistralAiCategories categories) {
        return Boolean.TRUE.equals(categories.getSexual()) || Boolean.TRUE.equals(categories.getHateAndDiscrimination()) || Boolean.TRUE.equals(categories.getViolenceAndThreats()) || Boolean.TRUE.equals(categories.getDangerousAndCriminalContent()) || Boolean.TRUE.equals(categories.getSelfHarm()) || Boolean.TRUE.equals(categories.getHealth()) || Boolean.TRUE.equals(categories.getLaw()) || Boolean.TRUE.equals(categories.getPii());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private Duration timeout;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private String modelName;
        private Integer maxRetries;
        private List<ModerationModelListener> listeners;

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder listeners(List<ModerationModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public MistralAiModerationModel build() {
            return new MistralAiModerationModel(this);
        }
    }
}

