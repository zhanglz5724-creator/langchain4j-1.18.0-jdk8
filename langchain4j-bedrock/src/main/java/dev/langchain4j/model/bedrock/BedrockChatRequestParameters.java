/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters$Builder
 *  software.amazon.awssdk.services.bedrockruntime.model.CacheTTL
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.bedrock.BedrockCachePointPlacement;
import dev.langchain4j.model.bedrock.BedrockGuardrailConfiguration;
import dev.langchain4j.model.bedrock.BedrockServiceTier;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import java.util.HashMap;
import java.util.Map;
import software.amazon.awssdk.services.bedrockruntime.model.CacheTTL;

public class BedrockChatRequestParameters
extends DefaultChatRequestParameters {
    public static final BedrockChatRequestParameters EMPTY = BedrockChatRequestParameters.builder().build();
    private final Map<String, Object> additionalModelRequestFields;
    private final BedrockCachePointPlacement cachePointPlacement;
    private final CacheTTL cacheTtl;
    private final BedrockGuardrailConfiguration bedrockGuardrailConfiguration;
    private final BedrockServiceTier serviceTier;

    private BedrockChatRequestParameters(Builder builder) {
        super((DefaultChatRequestParameters.Builder)builder);
        this.additionalModelRequestFields = Utils.copy((Map)builder.additionalModelRequestFields);
        this.cachePointPlacement = builder.cachePointPlacement;
        this.cacheTtl = builder.cacheTtl;
        this.bedrockGuardrailConfiguration = builder.bedrockGuardrailConfiguration;
        this.serviceTier = builder.serviceTier;
    }

    public BedrockChatRequestParameters overrideWith(ChatRequestParameters that) {
        return BedrockChatRequestParameters.builder().overrideWith((ChatRequestParameters)this).overrideWith(that).build();
    }

    public BedrockChatRequestParameters defaultedBy(ChatRequestParameters that) {
        return BedrockChatRequestParameters.builder().overrideWith(that).overrideWith((ChatRequestParameters)this).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, Object> additionalModelRequestFields() {
        return this.additionalModelRequestFields;
    }

    public BedrockCachePointPlacement cachePointPlacement() {
        return this.cachePointPlacement;
    }

    public CacheTTL cacheTtl() {
        return this.cacheTtl;
    }

    public BedrockGuardrailConfiguration bedrockGuardrailConfiguration() {
        return this.bedrockGuardrailConfiguration;
    }

    public BedrockServiceTier serviceTier() {
        return this.serviceTier;
    }

    public static class Builder
    extends DefaultChatRequestParameters.Builder<Builder> {
        private Map<String, Object> additionalModelRequestFields;
        private BedrockCachePointPlacement cachePointPlacement;
        private CacheTTL cacheTtl;
        private BedrockGuardrailConfiguration bedrockGuardrailConfiguration;
        private BedrockServiceTier serviceTier;

        public Builder overrideWith(ChatRequestParameters parameters) {
            super.overrideWith(parameters);
            if (parameters instanceof BedrockChatRequestParameters) {
                BedrockChatRequestParameters bedrockRequestParameters = (BedrockChatRequestParameters)parameters;
                if (bedrockRequestParameters.additionalModelRequestFields != null && !bedrockRequestParameters.additionalModelRequestFields.isEmpty()) {
                    if (this.additionalModelRequestFields == null) {
                        this.additionalModelRequestFields = new HashMap<String, Object>();
                    }
                    this.additionalModelRequestFields.putAll(bedrockRequestParameters.additionalModelRequestFields);
                }
                this.cachePointPlacement = (BedrockCachePointPlacement)((Object)Utils.getOrDefault((Object)((Object)bedrockRequestParameters.cachePointPlacement), (Object)((Object)this.cachePointPlacement)));
                this.cacheTtl = (CacheTTL)Utils.getOrDefault((Object)bedrockRequestParameters.cacheTtl, (Object)this.cacheTtl);
                this.bedrockGuardrailConfiguration = (BedrockGuardrailConfiguration)Utils.getOrDefault((Object)bedrockRequestParameters.bedrockGuardrailConfiguration, (Object)this.bedrockGuardrailConfiguration);
                this.serviceTier = (BedrockServiceTier)((Object)Utils.getOrDefault((Object)((Object)bedrockRequestParameters.serviceTier), (Object)((Object)this.serviceTier)));
            }
            return this;
        }

        public Builder additionalModelRequestFields(Map<String, Object> additionalModelRequestFields) {
            this.additionalModelRequestFields = additionalModelRequestFields;
            return this;
        }

        public Builder additionalModelRequestField(String key, Object value) {
            if (this.additionalModelRequestFields == null) {
                this.additionalModelRequestFields = new HashMap<String, Object>();
            }
            this.additionalModelRequestFields.put(key, value);
            return this;
        }

        public Builder enableReasoning(final Integer tokenBudget) {
            if (tokenBudget != null) {
                if (this.additionalModelRequestFields == null) {
                    this.additionalModelRequestFields = new HashMap<String, Object>();
                }
                HashMap<String, Object> reasoningConfig = new HashMap<String, Object>(){
                    {
                        this.put("type", "enabled");
                        this.put("budget_tokens", tokenBudget);
                    }
                };
                this.additionalModelRequestFields.put("reasoning_config", reasoningConfig);
            }
            return this;
        }

        public Builder enableAdaptiveReasoning(final String effort) {
            if (this.additionalModelRequestFields == null) {
                this.additionalModelRequestFields = new HashMap<String, Object>();
            }
            HashMap<String, Object> reasoningConfig = new HashMap<String, Object>(){
                {
                    this.put("type", "adaptive");
                }
            };
            this.additionalModelRequestFields.put("reasoning_config", reasoningConfig);
            if (effort != null) {
                HashMap<String, Object> outputConfig = new HashMap<String, Object>(){
                    {
                        this.put("effort", effort);
                    }
                };
                this.additionalModelRequestFields.put("output_config", outputConfig);
            }
            return this;
        }

        public Builder promptCaching(BedrockCachePointPlacement placement) {
            this.cachePointPlacement = placement;
            return this;
        }

        public Builder promptCaching(BedrockCachePointPlacement placement, CacheTTL ttl) {
            this.cachePointPlacement = placement;
            this.cacheTtl = ttl;
            return this;
        }

        public Builder guardrailConfiguration(BedrockGuardrailConfiguration bedrockGuardrailConfiguration) {
            this.bedrockGuardrailConfiguration = bedrockGuardrailConfiguration;
            return this;
        }

        public Builder serviceTier(BedrockServiceTier serviceTier) {
            this.serviceTier = serviceTier;
            return this;
        }

        public BedrockChatRequestParameters build() {
            return new BedrockChatRequestParameters(this);
        }
    }
}

