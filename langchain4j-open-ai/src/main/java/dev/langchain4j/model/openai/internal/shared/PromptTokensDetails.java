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
import java.util.Objects;

@JsonDeserialize(builder=PromptTokensDetails.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class PromptTokensDetails {
    @JsonProperty
    private final Integer cachedTokens;

    public PromptTokensDetails(Builder builder) {
        this.cachedTokens = builder.cachedTokens;
    }

    public Integer cachedTokens() {
        return this.cachedTokens;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof PromptTokensDetails && this.equalTo((PromptTokensDetails)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(PromptTokensDetails another) {
        return Objects.equals(this.cachedTokens, another.cachedTokens);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.cachedTokens);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "PromptTokensDetails{cachedTokens=" + this.cachedTokens + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private Integer cachedTokens;

        public Builder cachedTokens(Integer cachedTokens) {
            this.cachedTokens = cachedTokens;
            return this;
        }

        public PromptTokensDetails build() {
            return new PromptTokensDetails(this);
        }
    }
}

