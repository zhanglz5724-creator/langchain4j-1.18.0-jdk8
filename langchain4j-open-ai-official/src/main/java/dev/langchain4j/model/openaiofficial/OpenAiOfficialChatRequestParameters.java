/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.openai.models.ChatModel
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters$Builder
 */
package dev.langchain4j.model.openaiofficial;

import com.openai.models.ChatModel;
import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import java.util.Map;
import java.util.Objects;

@Experimental
public class OpenAiOfficialChatRequestParameters
extends DefaultChatRequestParameters {
    public static final OpenAiOfficialChatRequestParameters EMPTY = OpenAiOfficialChatRequestParameters.builder().build();
    private final Integer maxCompletionTokens;
    private final Map<String, Integer> logitBias;
    private final Boolean parallelToolCalls;
    private final Integer seed;
    private final String user;
    private final Boolean store;
    private final Map<String, String> metadata;
    private final String serviceTier;
    private final String reasoningEffort;

    private OpenAiOfficialChatRequestParameters(Builder builder) {
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

    public OpenAiOfficialChatRequestParameters overrideWith(ChatRequestParameters that) {
        return OpenAiOfficialChatRequestParameters.builder().overrideWith((ChatRequestParameters)this).overrideWith(that).build();
    }

    public OpenAiOfficialChatRequestParameters defaultedBy(ChatRequestParameters that) {
        return OpenAiOfficialChatRequestParameters.builder().overrideWith(that).overrideWith((ChatRequestParameters)this).build();
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
        OpenAiOfficialChatRequestParameters that = (OpenAiOfficialChatRequestParameters)((Object)o);
        return Objects.equals(this.maxCompletionTokens, that.maxCompletionTokens) && Objects.equals(this.logitBias, that.logitBias) && Objects.equals(this.parallelToolCalls, that.parallelToolCalls) && Objects.equals(this.seed, that.seed) && Objects.equals(this.user, that.user) && Objects.equals(this.store, that.store) && Objects.equals(this.metadata, that.metadata) && Objects.equals(this.serviceTier, that.serviceTier) && Objects.equals(this.reasoningEffort, that.reasoningEffort);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.maxCompletionTokens, this.logitBias, this.parallelToolCalls, this.seed, this.user, this.store, this.metadata, this.serviceTier, this.reasoningEffort);
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

        public Builder overrideWith(ChatRequestParameters parameters) {
            super.overrideWith(parameters);
            if (parameters instanceof OpenAiOfficialChatRequestParameters) {
                OpenAiOfficialChatRequestParameters openAiParameters = (OpenAiOfficialChatRequestParameters)parameters;
                this.maxCompletionTokens((Integer)Utils.getOrDefault((Object)openAiParameters.maxCompletionTokens(), (Object)this.maxCompletionTokens));
                this.logitBias(Utils.getOrDefault(openAiParameters.logitBias(), this.logitBias));
                this.parallelToolCalls((Boolean)Utils.getOrDefault((Object)openAiParameters.parallelToolCalls(), (Object)this.parallelToolCalls));
                this.seed((Integer)Utils.getOrDefault((Object)openAiParameters.seed(), (Object)this.seed));
                this.user((String)Utils.getOrDefault((Object)openAiParameters.user(), (Object)this.user));
                this.store((Boolean)Utils.getOrDefault((Object)openAiParameters.store(), (Object)this.store));
                this.metadata(Utils.getOrDefault(openAiParameters.metadata(), this.metadata));
                this.serviceTier((String)Utils.getOrDefault((Object)openAiParameters.serviceTier(), (Object)this.serviceTier));
                this.reasoningEffort((String)Utils.getOrDefault((Object)openAiParameters.reasoningEffort(), (Object)this.reasoningEffort));
            }
            return this;
        }

        public Builder modelName(ChatModel modelName) {
            return (Builder)super.modelName(modelName.toString());
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

        public OpenAiOfficialChatRequestParameters build() {
            return new OpenAiOfficialChatRequestParameters(this);
        }
    }
}

