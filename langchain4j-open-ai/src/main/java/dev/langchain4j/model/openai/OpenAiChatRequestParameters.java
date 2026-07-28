/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters$Builder
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import java.util.Map;
import java.util.Objects;

public class OpenAiChatRequestParameters
extends DefaultChatRequestParameters {
    public static final OpenAiChatRequestParameters EMPTY = OpenAiChatRequestParameters.builder().build();
    private final Integer maxCompletionTokens;
    private final Map<String, Integer> logitBias;
    private final Boolean parallelToolCalls;
    private final Integer seed;
    private final String user;
    private final Boolean store;
    private final Map<String, String> metadata;
    private final String serviceTier;
    private final String reasoningEffort;
    private final Boolean logprobs;
    private final Integer topLogprobs;
    private final Map<String, Object> customParameters;

    private OpenAiChatRequestParameters(Builder builder) {
        super((DefaultChatRequestParameters.Builder)builder);
        this.maxCompletionTokens = builder.maxCompletionTokens;
        this.logitBias = Utils.copy((Map)builder.logitBias);
        this.parallelToolCalls = builder.parallelToolCalls;
        this.seed = builder.seed;
        this.user = builder.user;
        this.store = builder.store;
        this.metadata = Utils.copy((Map)builder.metadata);
        this.serviceTier = builder.serviceTier;
        this.reasoningEffort = builder.reasoningEffort;
        this.logprobs = builder.logprobs;
        this.topLogprobs = builder.topLogprobs;
        this.customParameters = Utils.copy((Map)builder.customParameters);
    }

    public Integer maxCompletionTokens() {
        return this.maxCompletionTokens;
    }

    public Map<String, Integer> logitBias() {
        return this.logitBias;
    }

    public Boolean parallelToolCalls() {
        return this.parallelToolCalls;
    }

    public Integer seed() {
        return this.seed;
    }

    public String user() {
        return this.user;
    }

    public Boolean store() {
        return this.store;
    }

    public Map<String, String> metadata() {
        return this.metadata;
    }

    public String serviceTier() {
        return this.serviceTier;
    }

    public String reasoningEffort() {
        return this.reasoningEffort;
    }

    public Boolean logprobs() {
        return this.logprobs;
    }

    public Integer topLogprobs() {
        return this.topLogprobs;
    }

    public Map<String, Object> customParameters() {
        return this.customParameters;
    }

    public OpenAiChatRequestParameters overrideWith(ChatRequestParameters that) {
        return OpenAiChatRequestParameters.builder().overrideWith((ChatRequestParameters)this).overrideWith(that).build();
    }

    public OpenAiChatRequestParameters defaultedBy(ChatRequestParameters that) {
        return OpenAiChatRequestParameters.builder().overrideWith(that).overrideWith((ChatRequestParameters)this).build();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || ((Object)((Object)this)).getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        OpenAiChatRequestParameters that = (OpenAiChatRequestParameters)((Object)o);
        return Objects.equals(this.maxCompletionTokens, that.maxCompletionTokens) && Objects.equals(this.logitBias, that.logitBias) && Objects.equals(this.parallelToolCalls, that.parallelToolCalls) && Objects.equals(this.seed, that.seed) && Objects.equals(this.user, that.user) && Objects.equals(this.store, that.store) && Objects.equals(this.metadata, that.metadata) && Objects.equals(this.serviceTier, that.serviceTier) && Objects.equals(this.reasoningEffort, that.reasoningEffort) && Objects.equals(this.logprobs, that.logprobs) && Objects.equals(this.topLogprobs, that.topLogprobs) && Objects.equals(this.customParameters, that.customParameters);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.maxCompletionTokens, this.logitBias, this.parallelToolCalls, this.seed, this.user, this.store, this.metadata, this.serviceTier, this.reasoningEffort, this.logprobs, this.topLogprobs, this.customParameters);
    }

    public String toString() {
        return "OpenAiChatRequestParameters{modelName=" + Utils.quoted((Object)this.modelName()) + ", temperature=" + this.temperature() + ", topP=" + this.topP() + ", topK=" + this.topK() + ", frequencyPenalty=" + this.frequencyPenalty() + ", presencePenalty=" + this.presencePenalty() + ", maxOutputTokens=" + this.maxOutputTokens() + ", stopSequences=" + this.stopSequences() + ", toolSpecifications=" + this.toolSpecifications() + ", toolChoice=" + this.toolChoice() + ", responseFormat=" + this.responseFormat() + ", maxCompletionTokens=" + this.maxCompletionTokens + ", logitBias=" + this.logitBias + ", parallelToolCalls=" + this.parallelToolCalls + ", seed=" + this.seed + ", user=" + Utils.quoted((Object)this.user) + ", store=" + this.store + ", metadata=" + this.metadata + ", serviceTier=" + Utils.quoted((Object)this.serviceTier) + ", reasoningEffort=" + Utils.quoted((Object)this.reasoningEffort) + ", logprobs=" + this.logprobs + ", topLogprobs=" + this.topLogprobs + ", customParameters=" + this.customParameters + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends DefaultChatRequestParameters.Builder<Builder> {
        private Integer maxCompletionTokens;
        private Map<String, Integer> logitBias;
        private Boolean parallelToolCalls;
        private Integer seed;
        private String user;
        private Boolean store;
        private Map<String, String> metadata;
        private String serviceTier;
        private String reasoningEffort;
        private Boolean logprobs;
        private Integer topLogprobs;
        private Map<String, Object> customParameters;

        public Builder overrideWith(ChatRequestParameters parameters) {
            super.overrideWith(parameters);
            if (parameters instanceof OpenAiChatRequestParameters) {
                OpenAiChatRequestParameters openAiParameters = (OpenAiChatRequestParameters)parameters;
                this.maxCompletionTokens((Integer)Utils.getOrDefault((Object)openAiParameters.maxCompletionTokens(), (Object)this.maxCompletionTokens));
                this.logitBias(Utils.getOrDefault(openAiParameters.logitBias(), this.logitBias));
                this.parallelToolCalls((Boolean)Utils.getOrDefault((Object)openAiParameters.parallelToolCalls(), (Object)this.parallelToolCalls));
                this.seed((Integer)Utils.getOrDefault((Object)openAiParameters.seed(), (Object)this.seed));
                this.user((String)Utils.getOrDefault((Object)openAiParameters.user(), (Object)this.user));
                this.store((Boolean)Utils.getOrDefault((Object)openAiParameters.store(), (Object)this.store));
                this.metadata(Utils.getOrDefault(openAiParameters.metadata(), this.metadata));
                this.serviceTier((String)Utils.getOrDefault((Object)openAiParameters.serviceTier(), (Object)this.serviceTier));
                this.reasoningEffort((String)Utils.getOrDefault((Object)openAiParameters.reasoningEffort(), (Object)this.reasoningEffort));
                this.logprobs((Boolean)Utils.getOrDefault((Object)openAiParameters.logprobs(), (Object)this.logprobs));
                this.topLogprobs((Integer)Utils.getOrDefault((Object)openAiParameters.topLogprobs(), (Object)this.topLogprobs));
                this.customParameters(Utils.getOrDefault(openAiParameters.customParameters(), this.customParameters));
            }
            return this;
        }

        public Builder modelName(OpenAiChatModelName modelName) {
            return (Builder)super.modelName(modelName == null ? null : modelName.toString());
        }

        public Builder maxCompletionTokens(Integer maxCompletionTokens) {
            this.maxCompletionTokens = maxCompletionTokens;
            return this;
        }

        public Builder logitBias(Map<String, Integer> logitBias) {
            this.logitBias = logitBias;
            return this;
        }

        public Builder parallelToolCalls(Boolean parallelToolCalls) {
            this.parallelToolCalls = parallelToolCalls;
            return this;
        }

        public Builder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder store(Boolean store) {
            this.store = store;
            return this;
        }

        public Builder metadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder serviceTier(String serviceTier) {
            this.serviceTier = serviceTier;
            return this;
        }

        public Builder reasoningEffort(String reasoningEffort) {
            this.reasoningEffort = reasoningEffort;
            return this;
        }

        public Builder logprobs(Boolean logprobs) {
            this.logprobs = logprobs;
            return this;
        }

        public Builder topLogprobs(Integer topLogprobs) {
            this.topLogprobs = topLogprobs;
            return this;
        }

        public Builder customParameters(Map<String, Object> customParameters) {
            this.customParameters = customParameters;
            return this;
        }

        public OpenAiChatRequestParameters build() {
            return new OpenAiChatRequestParameters(this);
        }
    }
}

