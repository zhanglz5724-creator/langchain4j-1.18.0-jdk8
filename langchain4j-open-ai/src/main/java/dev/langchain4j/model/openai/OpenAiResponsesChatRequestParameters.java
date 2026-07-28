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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OpenAiResponsesChatRequestParameters
extends DefaultChatRequestParameters {
    public static final OpenAiResponsesChatRequestParameters EMPTY = OpenAiResponsesChatRequestParameters.builder().build();
    private final String previousResponseId;
    private final Integer maxToolCalls;
    private final Boolean parallelToolCalls;
    private final Integer topLogprobs;
    private final String truncation;
    private final List<String> include;
    private final String serviceTier;
    private final String safetyIdentifier;
    private final String promptCacheKey;
    private final String promptCacheRetention;
    private final String reasoningEffort;
    private final String reasoningSummary;
    private final String textVerbosity;
    private final Boolean streamIncludeObfuscation;
    private final Boolean store;
    private final Boolean strictTools;
    private final Boolean strictJsonSchema;
    private final List<Map<String, Object>> serverTools;

    private OpenAiResponsesChatRequestParameters(Builder builder) {
        super((DefaultChatRequestParameters.Builder)builder);
        this.previousResponseId = builder.previousResponseId;
        this.maxToolCalls = builder.maxToolCalls;
        this.parallelToolCalls = builder.parallelToolCalls;
        this.topLogprobs = builder.topLogprobs;
        this.truncation = builder.truncation;
        this.include = Utils.copy((List)builder.include);
        this.serviceTier = builder.serviceTier;
        this.safetyIdentifier = builder.safetyIdentifier;
        this.promptCacheKey = builder.promptCacheKey;
        this.promptCacheRetention = builder.promptCacheRetention;
        this.reasoningEffort = builder.reasoningEffort;
        this.reasoningSummary = builder.reasoningSummary;
        this.textVerbosity = builder.textVerbosity;
        this.streamIncludeObfuscation = builder.streamIncludeObfuscation;
        this.store = builder.store;
        this.strictTools = builder.strictTools;
        this.strictJsonSchema = builder.strictJsonSchema;
        this.serverTools = Utils.copy((List)builder.serverTools);
    }

    public String previousResponseId() {
        return this.previousResponseId;
    }

    public Integer maxToolCalls() {
        return this.maxToolCalls;
    }

    public Boolean parallelToolCalls() {
        return this.parallelToolCalls;
    }

    public Integer topLogprobs() {
        return this.topLogprobs;
    }

    public String truncation() {
        return this.truncation;
    }

    public List<String> include() {
        return this.include;
    }

    public String serviceTier() {
        return this.serviceTier;
    }

    public String safetyIdentifier() {
        return this.safetyIdentifier;
    }

    public String promptCacheKey() {
        return this.promptCacheKey;
    }

    public String promptCacheRetention() {
        return this.promptCacheRetention;
    }

    public String reasoningEffort() {
        return this.reasoningEffort;
    }

    public String reasoningSummary() {
        return this.reasoningSummary;
    }

    public String textVerbosity() {
        return this.textVerbosity;
    }

    public Boolean streamIncludeObfuscation() {
        return this.streamIncludeObfuscation;
    }

    public Boolean store() {
        return this.store;
    }

    public Boolean strictTools() {
        return this.strictTools;
    }

    public Boolean strictJsonSchema() {
        return this.strictJsonSchema;
    }

    public List<Map<String, Object>> serverTools() {
        return this.serverTools;
    }

    public OpenAiResponsesChatRequestParameters overrideWith(ChatRequestParameters that) {
        return OpenAiResponsesChatRequestParameters.builder().overrideWith((ChatRequestParameters)this).overrideWith(that).build();
    }

    public OpenAiResponsesChatRequestParameters defaultedBy(ChatRequestParameters that) {
        return OpenAiResponsesChatRequestParameters.builder().overrideWith(that).overrideWith((ChatRequestParameters)this).build();
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
        OpenAiResponsesChatRequestParameters that = (OpenAiResponsesChatRequestParameters)((Object)o);
        return Objects.equals(this.previousResponseId, that.previousResponseId) && Objects.equals(this.maxToolCalls, that.maxToolCalls) && Objects.equals(this.parallelToolCalls, that.parallelToolCalls) && Objects.equals(this.topLogprobs, that.topLogprobs) && Objects.equals(this.truncation, that.truncation) && Objects.equals(this.include, that.include) && Objects.equals(this.serviceTier, that.serviceTier) && Objects.equals(this.safetyIdentifier, that.safetyIdentifier) && Objects.equals(this.promptCacheKey, that.promptCacheKey) && Objects.equals(this.promptCacheRetention, that.promptCacheRetention) && Objects.equals(this.reasoningEffort, that.reasoningEffort) && Objects.equals(this.reasoningSummary, that.reasoningSummary) && Objects.equals(this.textVerbosity, that.textVerbosity) && Objects.equals(this.streamIncludeObfuscation, that.streamIncludeObfuscation) && Objects.equals(this.store, that.store) && Objects.equals(this.strictTools, that.strictTools) && Objects.equals(this.strictJsonSchema, that.strictJsonSchema) && Objects.equals(this.serverTools, that.serverTools);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.previousResponseId, this.maxToolCalls, this.parallelToolCalls, this.topLogprobs, this.truncation, this.include, this.serviceTier, this.safetyIdentifier, this.promptCacheKey, this.promptCacheRetention, this.reasoningEffort, this.reasoningSummary, this.textVerbosity, this.streamIncludeObfuscation, this.store, this.strictTools, this.strictJsonSchema, this.serverTools);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends DefaultChatRequestParameters.Builder<Builder> {
        private String previousResponseId;
        private Integer maxToolCalls;
        private Boolean parallelToolCalls;
        private Integer topLogprobs;
        private String truncation;
        private List<String> include;
        private String serviceTier;
        private String safetyIdentifier;
        private String promptCacheKey;
        private String promptCacheRetention;
        private String reasoningEffort;
        private String reasoningSummary;
        private String textVerbosity;
        private Boolean streamIncludeObfuscation;
        private Boolean store;
        private Boolean strictTools;
        private Boolean strictJsonSchema;
        private List<Map<String, Object>> serverTools;

        public Builder overrideWith(ChatRequestParameters parameters) {
            super.overrideWith(parameters);
            if (parameters instanceof OpenAiResponsesChatRequestParameters) {
                OpenAiResponsesChatRequestParameters p = (OpenAiResponsesChatRequestParameters)parameters;
                this.previousResponseId((String)Utils.getOrDefault((Object)p.previousResponseId(), (Object)this.previousResponseId));
                this.maxToolCalls((Integer)Utils.getOrDefault((Object)p.maxToolCalls(), (Object)this.maxToolCalls));
                this.parallelToolCalls((Boolean)Utils.getOrDefault((Object)p.parallelToolCalls(), (Object)this.parallelToolCalls));
                this.topLogprobs((Integer)Utils.getOrDefault((Object)p.topLogprobs(), (Object)this.topLogprobs));
                this.truncation((String)Utils.getOrDefault((Object)p.truncation(), (Object)this.truncation));
                this.include(Utils.getOrDefault(p.include(), this.include));
                this.serviceTier((String)Utils.getOrDefault((Object)p.serviceTier(), (Object)this.serviceTier));
                this.safetyIdentifier((String)Utils.getOrDefault((Object)p.safetyIdentifier(), (Object)this.safetyIdentifier));
                this.promptCacheKey((String)Utils.getOrDefault((Object)p.promptCacheKey(), (Object)this.promptCacheKey));
                this.promptCacheRetention((String)Utils.getOrDefault((Object)p.promptCacheRetention(), (Object)this.promptCacheRetention));
                this.reasoningEffort((String)Utils.getOrDefault((Object)p.reasoningEffort(), (Object)this.reasoningEffort));
                this.reasoningSummary((String)Utils.getOrDefault((Object)p.reasoningSummary(), (Object)this.reasoningSummary));
                this.textVerbosity((String)Utils.getOrDefault((Object)p.textVerbosity(), (Object)this.textVerbosity));
                this.streamIncludeObfuscation((Boolean)Utils.getOrDefault((Object)p.streamIncludeObfuscation(), (Object)this.streamIncludeObfuscation));
                this.store((Boolean)Utils.getOrDefault((Object)p.store(), (Object)this.store));
                this.strictTools((Boolean)Utils.getOrDefault((Object)p.strictTools(), (Object)this.strictTools));
                this.strictJsonSchema((Boolean)Utils.getOrDefault((Object)p.strictJsonSchema(), (Object)this.strictJsonSchema));
                this.serverTools(Utils.getOrDefault(p.serverTools(), this.serverTools));
            }
            return this;
        }

        public Builder previousResponseId(String previousResponseId) {
            this.previousResponseId = previousResponseId;
            return this;
        }

        public Builder maxToolCalls(Integer maxToolCalls) {
            this.maxToolCalls = maxToolCalls;
            return this;
        }

        public Builder parallelToolCalls(Boolean parallelToolCalls) {
            this.parallelToolCalls = parallelToolCalls;
            return this;
        }

        public Builder topLogprobs(Integer topLogprobs) {
            this.topLogprobs = topLogprobs;
            return this;
        }

        public Builder truncation(String truncation) {
            this.truncation = truncation;
            return this;
        }

        public Builder include(List<String> include) {
            this.include = include;
            return this;
        }

        public Builder serviceTier(String serviceTier) {
            this.serviceTier = serviceTier;
            return this;
        }

        public Builder safetyIdentifier(String safetyIdentifier) {
            this.safetyIdentifier = safetyIdentifier;
            return this;
        }

        public Builder promptCacheKey(String promptCacheKey) {
            this.promptCacheKey = promptCacheKey;
            return this;
        }

        public Builder promptCacheRetention(String promptCacheRetention) {
            this.promptCacheRetention = promptCacheRetention;
            return this;
        }

        public Builder reasoningEffort(String reasoningEffort) {
            this.reasoningEffort = reasoningEffort;
            return this;
        }

        public Builder reasoningSummary(String reasoningSummary) {
            this.reasoningSummary = reasoningSummary;
            return this;
        }

        public Builder textVerbosity(String textVerbosity) {
            this.textVerbosity = textVerbosity;
            return this;
        }

        public Builder streamIncludeObfuscation(Boolean streamIncludeObfuscation) {
            this.streamIncludeObfuscation = streamIncludeObfuscation;
            return this;
        }

        public Builder store(Boolean store) {
            this.store = store;
            return this;
        }

        public Builder strictTools(Boolean strictTools) {
            this.strictTools = strictTools;
            return this;
        }

        public Builder strictJsonSchema(Boolean strictJsonSchema) {
            this.strictJsonSchema = strictJsonSchema;
            return this;
        }

        public Builder serverTools(List<Map<String, Object>> serverTools) {
            this.serverTools = serverTools;
            return this;
        }

        public OpenAiResponsesChatRequestParameters build() {
            return new OpenAiResponsesChatRequestParameters(this);
        }
    }
}

