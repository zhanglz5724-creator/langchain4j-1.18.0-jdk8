import java.util.Arrays;
import java.util.HashSet;

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.watsonx.ai.chat.model.ExtractionTags
 *  com.ibm.watsonx.ai.chat.model.Thinking
 *  com.ibm.watsonx.ai.chat.model.ThinkingEffort
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters$Builder
 */
package dev.langchain4j.model.watsonx;

import com.ibm.watsonx.ai.chat.model.ExtractionTags;
import com.ibm.watsonx.ai.chat.model.Thinking;
import com.ibm.watsonx.ai.chat.model.ThinkingEffort;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class WatsonxChatRequestParameters
extends DefaultChatRequestParameters {
    public static final WatsonxChatRequestParameters EMPTY = WatsonxChatRequestParameters.builder().build();
    private final String projectId;
    private final String spaceId;
    private final Thinking thinking;
    private final Map<String, Integer> logitBias;
    private final Boolean logprobs;
    private final Integer topLogprobs;
    private final Integer seed;
    private final String toolChoiceName;
    private final Set<String> guidedChoice;
    private final String guidedRegex;
    private final String guidedGrammar;
    private final Double repetitionPenalty;
    private final Double lengthPenalty;
    private final String deploymentId;
    private final Duration timeout;

    private WatsonxChatRequestParameters(Builder builder) {
        super((DefaultChatRequestParameters.Builder)builder);
        this.projectId = builder.projectId;
        this.spaceId = builder.spaceId;
        this.logitBias = builder.logitBias;
        this.logprobs = builder.logprobs;
        this.topLogprobs = builder.topLogprobs;
        this.seed = builder.seed;
        this.toolChoiceName = builder.toolChoiceName;
        this.timeout = builder.timeout;
        this.thinking = builder.thinking;
        this.guidedChoice = builder.guidedChoice;
        this.guidedRegex = builder.guidedRegex;
        this.guidedGrammar = builder.guidedGrammar;
        this.repetitionPenalty = builder.repetitionPenalty;
        this.lengthPenalty = builder.lengthPenalty;
        this.deploymentId = builder.deploymentId;
    }

    public String projectId() {
        return this.projectId;
    }

    public String spaceId() {
        return this.spaceId;
    }

    public Map<String, Integer> logitBias() {
        return this.logitBias;
    }

    public Boolean logprobs() {
        return this.logprobs;
    }

    public Integer topLogprobs() {
        return this.topLogprobs;
    }

    public Integer seed() {
        return this.seed;
    }

    public String toolChoiceName() {
        return this.toolChoiceName;
    }

    public Duration timeout() {
        return this.timeout;
    }

    public Thinking thinking() {
        return this.thinking;
    }

    public Set<String> guidedChoice() {
        return this.guidedChoice;
    }

    public String guidedRegex() {
        return this.guidedRegex;
    }

    public String guidedGrammar() {
        return this.guidedGrammar;
    }

    public Double repetitionPenalty() {
        return this.repetitionPenalty;
    }

    public Double lengthPenalty() {
        return this.lengthPenalty;
    }

    public String deploymentId() {
        return this.deploymentId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ChatRequestParameters overrideWith(ChatRequestParameters that) {
        return WatsonxChatRequestParameters.builder().overrideWith((ChatRequestParameters)this).overrideWith(that).build();
    }

    public WatsonxChatRequestParameters defaultedBy(ChatRequestParameters that) {
        return WatsonxChatRequestParameters.builder().overrideWith(that).overrideWith((ChatRequestParameters)this).build();
    }

    public boolean equals(Object o) {
        if (o == null || ((Object)((Object)this)).getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        WatsonxChatRequestParameters that = (WatsonxChatRequestParameters)((Object)o);
        return Objects.equals(this.projectId, that.projectId) && Objects.equals(this.spaceId, that.spaceId) && Objects.equals(this.thinking, that.thinking) && Objects.equals(this.logitBias, that.logitBias) && Objects.equals(this.logprobs, that.logprobs) && Objects.equals(this.topLogprobs, that.topLogprobs) && Objects.equals(this.seed, that.seed) && Objects.equals(this.toolChoiceName, that.toolChoiceName) && Objects.equals(this.guidedChoice, that.guidedChoice) && Objects.equals(this.guidedRegex, that.guidedRegex) && Objects.equals(this.guidedGrammar, that.guidedGrammar) && Objects.equals(this.repetitionPenalty, that.repetitionPenalty) && Objects.equals(this.lengthPenalty, that.lengthPenalty) && Objects.equals(this.deploymentId, that.deploymentId) && Objects.equals(this.timeout, that.timeout);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.projectId, this.spaceId, this.thinking, this.logitBias, this.logprobs, this.topLogprobs, this.seed, this.toolChoiceName, this.guidedChoice, this.guidedRegex, this.guidedGrammar, this.repetitionPenalty, this.lengthPenalty, this.deploymentId, this.timeout);
    }

    public String toString() {
        return "WatsonxChatRequestParameters{modelName=" + this.modelName() + ", temperature=" + this.temperature() + ", topP=" + this.topP() + ", topK=" + this.topK() + ", frequencyPenalty=" + this.frequencyPenalty() + ", presencePenalty=" + this.presencePenalty() + ", maxOutputTokens=" + this.maxOutputTokens() + ", stopSequences=" + this.stopSequences() + ", toolSpecifications=" + this.toolSpecifications() + ", toolChoice=" + this.toolChoice() + ", responseFormat=" + this.responseFormat() + ", projectId=" + this.projectId + ", spaceId=" + this.spaceId + ", thinking=" + this.thinking + ", logitBias=" + this.logitBias + ", logprobs=" + this.logprobs + ", topLogprobs=" + this.topLogprobs + ", seed=" + this.seed + ", toolChoiceName=" + this.toolChoiceName + ", guidedChoice=" + this.guidedChoice + ", guidedRegex=" + this.guidedRegex + ", guidedGrammar=" + this.guidedGrammar + ", repetitionPenalty=" + this.repetitionPenalty + ", lengthPenalty=" + this.lengthPenalty + ", deploymentId=" + this.deploymentId + ", timeout=" + this.timeout + "}";
    }

    public static class Builder
    extends DefaultChatRequestParameters.Builder<Builder> {
        private String projectId;
        private String spaceId;
        private Map<String, Integer> logitBias;
        private Boolean logprobs;
        private Integer topLogprobs;
        private Integer seed;
        private String toolChoiceName;
        private Duration timeout;
        private Set<String> guidedChoice;
        private String guidedRegex;
        private String guidedGrammar;
        private Double repetitionPenalty;
        private Double lengthPenalty;
        private String deploymentId;
        private Thinking thinking;

        public Builder overrideWith(ChatRequestParameters parameters) {
            super.overrideWith(parameters);
            if (parameters instanceof WatsonxChatRequestParameters) {
                WatsonxChatRequestParameters watsonxParameters = (WatsonxChatRequestParameters)parameters;
                this.projectId((String)Utils.getOrDefault((Object)watsonxParameters.projectId(), (Object)this.projectId));
                this.spaceId((String)Utils.getOrDefault((Object)watsonxParameters.spaceId(), (Object)this.spaceId));
                this.logitBias(Utils.getOrDefault(watsonxParameters.logitBias(), this.logitBias));
                this.logprobs((Boolean)Utils.getOrDefault((Object)watsonxParameters.logprobs(), (Object)this.logprobs));
                this.topLogprobs((Integer)Utils.getOrDefault((Object)watsonxParameters.topLogprobs(), (Object)this.topLogprobs));
                this.seed((Integer)Utils.getOrDefault((Object)watsonxParameters.seed(), (Object)this.seed));
                this.toolChoiceName((String)Utils.getOrDefault((Object)watsonxParameters.toolChoiceName(), (Object)this.toolChoiceName));
                this.timeout((Duration)Utils.getOrDefault((Object)watsonxParameters.timeout(), (Object)this.timeout));
                this.thinking((Thinking)Utils.getOrDefault((Object)watsonxParameters.thinking(), (Object)this.thinking));
                this.guidedChoice((Set)Utils.getOrDefault(watsonxParameters.guidedChoice(), this.guidedChoice));
                this.guidedRegex((String)Utils.getOrDefault((Object)watsonxParameters.guidedRegex(), (Object)this.guidedRegex));
                this.guidedGrammar((String)Utils.getOrDefault((Object)watsonxParameters.guidedGrammar(), (Object)this.guidedGrammar));
                this.repetitionPenalty((Double)Utils.getOrDefault((Object)watsonxParameters.repetitionPenalty(), (Object)this.repetitionPenalty));
                this.lengthPenalty((Double)Utils.getOrDefault((Object)watsonxParameters.lengthPenalty(), (Object)this.lengthPenalty));
                this.deploymentId((String)Utils.getOrDefault((Object)watsonxParameters.deploymentId(), (Object)this.deploymentId));
            }
            return this;
        }

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder spaceId(String spaceId) {
            this.spaceId = spaceId;
            return this;
        }

        public Builder logitBias(Map<String, Integer> logitBias) {
            this.logitBias = logitBias;
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

        public Builder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public Builder toolChoiceName(String toolChoiceName) {
            this.toolChoiceName = toolChoiceName;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder deploymentId(String deploymentId) {
            this.deploymentId = deploymentId;
            return this;
        }

        public Builder thinking(boolean enabled) {
            return this.thinking(Thinking.builder().enabled(Boolean.valueOf(enabled)).build());
        }

        public Builder thinking(ExtractionTags tags) {
            if (Objects.nonNull(tags)) {
                return this.thinking(Thinking.of((ExtractionTags)tags));
            }
            this.thinking = null;
            return this;
        }

        public Builder thinking(ThinkingEffort thinkingEffort) {
            if (Objects.nonNull(thinkingEffort)) {
                return this.thinking(Thinking.of((ThinkingEffort)thinkingEffort));
            }
            this.thinking = null;
            return this;
        }

        public Builder thinking(Thinking thinking) {
            this.thinking = thinking;
            return this;
        }

        public Builder guidedChoice(String ... guidedChoice) {
            return this.guidedChoice(new HashSet<>(Arrays.asList((Object[])guidedChoice)));
        }

        public Builder guidedChoice(Set<String> guidedChoices) {
            this.guidedChoice = guidedChoices;
            return this;
        }

        public Builder guidedRegex(String guidedRegex) {
            this.guidedRegex = guidedRegex;
            return this;
        }

        public Builder guidedGrammar(String guidedGrammar) {
            this.guidedGrammar = guidedGrammar;
            return this;
        }

        public Builder repetitionPenalty(Double repetitionPenalty) {
            this.repetitionPenalty = repetitionPenalty;
            return this;
        }

        public Builder lengthPenalty(Double lengthPenalty) {
            this.lengthPenalty = lengthPenalty;
            return this;
        }

        public WatsonxChatRequestParameters build() {
            return new WatsonxChatRequestParameters(this);
        }
    }
}

