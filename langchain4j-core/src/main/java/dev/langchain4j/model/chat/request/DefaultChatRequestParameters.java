/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.request;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DefaultChatRequestParameters
implements ChatRequestParameters {
    public static final ChatRequestParameters EMPTY = DefaultChatRequestParameters.builder().build();
    private final String modelName;
    private final Double temperature;
    private final Double topP;
    private final Integer topK;
    private final Double frequencyPenalty;
    private final Double presencePenalty;
    private final Integer maxOutputTokens;
    private final List<String> stopSequences;
    private final List<ToolSpecification> toolSpecifications;
    private final ToolChoice toolChoice;
    private final ResponseFormat responseFormat;

    protected DefaultChatRequestParameters(Builder<?> builder) {
        this.modelName = ((Builder)builder).modelName;
        this.temperature = ((Builder)builder).temperature;
        this.topP = ((Builder)builder).topP;
        this.topK = ((Builder)builder).topK;
        this.frequencyPenalty = ((Builder)builder).frequencyPenalty;
        this.presencePenalty = ((Builder)builder).presencePenalty;
        this.maxOutputTokens = ((Builder)builder).maxOutputTokens;
        this.stopSequences = Utils.copy(((Builder)builder).stopSequences);
        this.toolSpecifications = Utils.copy(((Builder)builder).toolSpecifications);
        this.toolChoice = ((Builder)builder).toolChoice;
        this.responseFormat = ((Builder)builder).responseFormat;
    }

    @Override
    public String modelName() {
        return this.modelName;
    }

    @Override
    public Double temperature() {
        return this.temperature;
    }

    @Override
    public Double topP() {
        return this.topP;
    }

    @Override
    public Integer topK() {
        return this.topK;
    }

    @Override
    public Double frequencyPenalty() {
        return this.frequencyPenalty;
    }

    @Override
    public Double presencePenalty() {
        return this.presencePenalty;
    }

    @Override
    public Integer maxOutputTokens() {
        return this.maxOutputTokens;
    }

    @Override
    public List<String> stopSequences() {
        return this.stopSequences;
    }

    @Override
    public List<ToolSpecification> toolSpecifications() {
        return this.toolSpecifications;
    }

    @Override
    public ToolChoice toolChoice() {
        return this.toolChoice;
    }

    @Override
    public ResponseFormat responseFormat() {
        return this.responseFormat;
    }

    @Override
    public ChatRequestParameters overrideWith(ChatRequestParameters that) {
        if (this.isSubtypeOfThis(that)) {
            return that.defaultedBy(this);
        }
        return ((Builder)((Builder)DefaultChatRequestParameters.builder().overrideWith(this)).overrideWith(that)).build();
    }

    @Override
    public ChatRequestParameters defaultedBy(ChatRequestParameters that) {
        if (this.isSubtypeOfThis(that)) {
            return that.overrideWith(this);
        }
        return ((Builder)((Builder)DefaultChatRequestParameters.builder().overrideWith(that)).overrideWith(this)).build();
    }

    private boolean isSubtypeOfThis(ChatRequestParameters that) {
        return that != null && that.getClass() != this.getClass() && this.getClass().isAssignableFrom(that.getClass());
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DefaultChatRequestParameters that = (DefaultChatRequestParameters)o;
        return Objects.equals(this.modelName, that.modelName) && Objects.equals(this.temperature, that.temperature) && Objects.equals(this.topP, that.topP) && Objects.equals(this.topK, that.topK) && Objects.equals(this.frequencyPenalty, that.frequencyPenalty) && Objects.equals(this.presencePenalty, that.presencePenalty) && Objects.equals(this.maxOutputTokens, that.maxOutputTokens) && Objects.equals(this.stopSequences, that.stopSequences) && Objects.equals(this.toolSpecifications, that.toolSpecifications) && Objects.equals((Object)this.toolChoice, (Object)that.toolChoice) && Objects.equals(this.responseFormat, that.responseFormat);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        return Objects.hash(new Object[]{this.modelName, this.temperature, this.topP, this.topK, this.frequencyPenalty, this.presencePenalty, this.maxOutputTokens, this.stopSequences, this.toolSpecifications, this.toolChoice, this.responseFormat});
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "DefaultChatRequestParameters{modelName='" + this.modelName + '\'' + ", temperature=" + this.temperature + ", topP=" + this.topP + ", topK=" + this.topK + ", frequencyPenalty=" + this.frequencyPenalty + ", presencePenalty=" + this.presencePenalty + ", maxOutputTokens=" + this.maxOutputTokens + ", stopSequences=" + this.stopSequences + ", toolSpecifications=" + this.toolSpecifications + ", toolChoice=" + (Object)((Object)this.toolChoice) + ", responseFormat=" + this.responseFormat + '}';
    }

    public static Builder<?> builder() {
        return new Builder();
    }

    public static class Builder<T extends Builder<T>> {
        private String modelName;
        private Double temperature;
        private Double topP;
        private Integer topK;
        private Double frequencyPenalty;
        private Double presencePenalty;
        private Integer maxOutputTokens;
        private List<String> stopSequences;
        private List<ToolSpecification> toolSpecifications;
        private ToolChoice toolChoice;
        private ResponseFormat responseFormat;

        public T overrideWith(ChatRequestParameters parameters) {
            this.modelName(Utils.getOrDefault(parameters.modelName(), this.modelName));
            this.temperature(Utils.getOrDefault(parameters.temperature(), this.temperature));
            this.topP(Utils.getOrDefault(parameters.topP(), this.topP));
            this.topK(Utils.getOrDefault(parameters.topK(), this.topK));
            this.frequencyPenalty(Utils.getOrDefault(parameters.frequencyPenalty(), this.frequencyPenalty));
            this.presencePenalty(Utils.getOrDefault(parameters.presencePenalty(), this.presencePenalty));
            this.maxOutputTokens(Utils.getOrDefault(parameters.maxOutputTokens(), this.maxOutputTokens));
            this.stopSequences(Utils.getOrDefault(parameters.stopSequences(), this.stopSequences));
            this.toolSpecifications(Utils.getOrDefault(parameters.toolSpecifications(), this.toolSpecifications));
            this.toolChoice(Utils.getOrDefault(parameters.toolChoice(), this.toolChoice));
            this.responseFormat(Utils.getOrDefault(parameters.responseFormat(), this.responseFormat));
            return (T)this;
        }

        public T modelName(String modelName) {
            this.modelName = modelName;
            return (T)this;
        }

        public T temperature(Double temperature) {
            this.temperature = temperature;
            return (T)this;
        }

        public T topP(Double topP) {
            this.topP = topP;
            return (T)this;
        }

        public T topK(Integer topK) {
            this.topK = topK;
            return (T)this;
        }

        public T frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return (T)this;
        }

        public T presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return (T)this;
        }

        public T maxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
            return (T)this;
        }

        public T stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return (T)this;
        }

        public T stopSequences(String ... stopSequences) {
            return this.stopSequences(Arrays.asList(stopSequences));
        }

        public T toolSpecifications(List<ToolSpecification> toolSpecifications) {
            this.toolSpecifications = toolSpecifications;
            return (T)this;
        }

        public T toolSpecifications(ToolSpecification ... toolSpecifications) {
            return this.toolSpecifications(Arrays.asList(toolSpecifications));
        }

        public T toolChoice(ToolChoice toolChoice) {
            this.toolChoice = toolChoice;
            return (T)this;
        }

        public T responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return (T)this;
        }

        public T responseFormat(JsonSchema jsonSchema) {
            if (jsonSchema != null) {
                ResponseFormat responseFormat = ResponseFormat.builder().type(ResponseFormatType.JSON).jsonSchema(jsonSchema).build();
                return this.responseFormat(responseFormat);
            }
            return (T)this;
        }

        public ChatRequestParameters build() {
            return new DefaultChatRequestParameters(this);
        }
    }
}

