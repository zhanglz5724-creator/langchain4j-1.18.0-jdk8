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
package dev.langchain4j.model.openai.internal.image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import java.util.Objects;

@JsonDeserialize(builder=ImageUsage.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImageUsage {
    @JsonProperty
    private final Integer inputTokens;
    @JsonProperty
    private final Integer outputTokens;
    @JsonProperty
    private final Integer totalTokens;
    @JsonProperty
    private final TokensDetails inputTokensDetails;
    @JsonProperty
    private final TokensDetails outputTokensDetails;

    public ImageUsage(Builder builder) {
        this.inputTokens = builder.inputTokens;
        this.outputTokens = builder.outputTokens;
        this.totalTokens = builder.totalTokens;
        this.inputTokensDetails = builder.inputTokensDetails;
        this.outputTokensDetails = builder.outputTokensDetails;
    }

    public Integer inputTokens() {
        return this.inputTokens;
    }

    public Integer outputTokens() {
        return this.outputTokens;
    }

    public Integer totalTokens() {
        return this.totalTokens;
    }

    public TokensDetails inputTokensDetails() {
        return this.inputTokensDetails;
    }

    public TokensDetails outputTokensDetails() {
        return this.outputTokensDetails;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        if (another == null || this.getClass() != another.getClass()) {
            return false;
        }
        ImageUsage that = (ImageUsage)another;
        return Objects.equals(this.inputTokens, that.inputTokens) && Objects.equals(this.outputTokens, that.outputTokens) && Objects.equals(this.totalTokens, that.totalTokens) && Objects.equals(this.inputTokensDetails, that.inputTokensDetails) && Objects.equals(this.outputTokensDetails, that.outputTokensDetails);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        return Objects.hash(this.inputTokens, this.outputTokens, this.totalTokens, this.inputTokensDetails, this.outputTokensDetails);
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ImageUsage{inputTokens=" + this.inputTokens + ", outputTokens=" + this.outputTokens + ", totalTokens=" + this.totalTokens + ", inputTokensDetails=" + this.inputTokensDetails + ", outputTokensDetails=" + this.outputTokensDetails + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonDeserialize(builder=TokensDetails.TokensDetailsBuilder.class)
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TokensDetails {
        @JsonProperty
        private final Integer imageTokens;
        @JsonProperty
        private final Integer textTokens;

        public TokensDetails(TokensDetailsBuilder builder) {
            this.imageTokens = builder.imageTokens;
            this.textTokens = builder.textTokens;
        }

        public Integer imageTokens() {
            return this.imageTokens;
        }

        public Integer textTokens() {
            return this.textTokens;
        }

        @JacocoIgnoreCoverageGenerated
        public boolean equals(Object another) {
            if (this == another) {
                return true;
            }
            if (another == null || this.getClass() != another.getClass()) {
                return false;
            }
            TokensDetails that = (TokensDetails)another;
            return Objects.equals(this.imageTokens, that.imageTokens) && Objects.equals(this.textTokens, that.textTokens);
        }

        @JacocoIgnoreCoverageGenerated
        public int hashCode() {
            return Objects.hash(this.imageTokens, this.textTokens);
        }

        @JacocoIgnoreCoverageGenerated
        public String toString() {
            return "TokensDetails{imageTokens=" + this.imageTokens + ", textTokens=" + this.textTokens + '}';
        }

        public static TokensDetailsBuilder builder() {
            return new TokensDetailsBuilder();
        }

        @JsonPOJOBuilder(withPrefix="")
        @JsonIgnoreProperties(ignoreUnknown=true)
        @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class TokensDetailsBuilder {
            private Integer imageTokens;
            private Integer textTokens;

            public TokensDetailsBuilder imageTokens(Integer imageTokens) {
                this.imageTokens = imageTokens;
                return this;
            }

            public TokensDetailsBuilder textTokens(Integer textTokens) {
                this.textTokens = textTokens;
                return this;
            }

            public TokensDetails build() {
                return new TokensDetails(this);
            }
        }
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {
        private Integer inputTokens;
        private Integer outputTokens;
        private Integer totalTokens;
        private TokensDetails inputTokensDetails;
        private TokensDetails outputTokensDetails;

        public Builder inputTokens(Integer inputTokens) {
            this.inputTokens = inputTokens;
            return this;
        }

        public Builder outputTokens(Integer outputTokens) {
            this.outputTokens = outputTokens;
            return this;
        }

        public Builder totalTokens(Integer totalTokens) {
            this.totalTokens = totalTokens;
            return this;
        }

        public Builder inputTokensDetails(TokensDetails inputTokensDetails) {
            this.inputTokensDetails = inputTokensDetails;
            return this;
        }

        public Builder outputTokensDetails(TokensDetails outputTokensDetails) {
            this.outputTokensDetails = outputTokensDetails;
            return this;
        }

        public ImageUsage build() {
            return new ImageUsage(this);
        }
    }
}

