/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 */
package dev.langchain4j.model.openai.internal.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.shared.CompletionTokensDetails;
import dev.langchain4j.model.openai.internal.shared.PromptTokensDetails;
import java.util.Objects;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class Usage {
    @JsonProperty
    private final Integer totalTokens;
    @JsonProperty
    private final Integer promptTokens;
    @JsonProperty
    private final PromptTokensDetails promptTokensDetails;
    @JsonProperty
    private final Integer completionTokens;
    @JsonProperty
    private final CompletionTokensDetails completionTokensDetails;

    public Usage(Builder builder) {
        this.totalTokens = builder.totalTokens;
        this.promptTokens = builder.promptTokens;
        this.promptTokensDetails = builder.promptTokensDetails;
        this.completionTokens = builder.completionTokens;
        this.completionTokensDetails = builder.completionTokensDetails;
    }

    public Integer totalTokens() {
        return this.totalTokens;
    }

    public Integer promptTokens() {
        return this.promptTokens;
    }

    public PromptTokensDetails promptTokensDetails() {
        return this.promptTokensDetails;
    }

    public Integer completionTokens() {
        return this.completionTokens;
    }

    public CompletionTokensDetails completionTokensDetails() {
        return this.completionTokensDetails;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof Usage && this.equalTo((Usage)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(Usage another) {
        return Objects.equals(this.totalTokens, another.totalTokens) && Objects.equals(this.promptTokens, another.promptTokens) && Objects.equals(this.promptTokensDetails, another.promptTokensDetails) && Objects.equals(this.completionTokens, another.completionTokens) && Objects.equals(this.completionTokensDetails, another.completionTokensDetails);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.totalTokens);
        h += (h << 5) + Objects.hashCode(this.promptTokens);
        h += (h << 5) + Objects.hashCode(this.promptTokensDetails);
        h += (h << 5) + Objects.hashCode(this.completionTokens);
        h += (h << 5) + Objects.hashCode(this.completionTokensDetails);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "Usage{totalTokens=" + this.totalTokens + ", promptTokens=" + this.promptTokens + ", promptTokensDetails=" + this.promptTokensDetails + ", completionTokens=" + this.completionTokens + ", completionTokensDetails=" + this.completionTokensDetails + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private Integer totalTokens;
        private Integer promptTokens;
        private PromptTokensDetails promptTokensDetails;
        private Integer completionTokens;
        private CompletionTokensDetails completionTokensDetails;

        public Builder totalTokens(Integer totalTokens) {
            this.totalTokens = totalTokens;
            return this;
        }

        public Builder promptTokens(Integer promptTokens) {
            this.promptTokens = promptTokens;
            return this;
        }

        public Builder promptTokensDetails(PromptTokensDetails promptTokensDetails) {
            this.promptTokensDetails = promptTokensDetails;
            return this;
        }

        public Builder completionTokens(Integer completionTokens) {
            this.completionTokens = completionTokens;
            return this;
        }

        public Builder completionTokensDetails(CompletionTokensDetails completionTokensDetails) {
            this.completionTokensDetails = completionTokensDetails;
            return this;
        }

        public Usage build() {
            return new Usage(this);
        }
    }
}

