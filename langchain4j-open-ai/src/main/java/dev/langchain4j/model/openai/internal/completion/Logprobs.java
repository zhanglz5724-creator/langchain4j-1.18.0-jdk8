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
package dev.langchain4j.model.openai.internal.completion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class Logprobs {
    @JsonProperty
    private final List<String> tokens;
    @JsonProperty
    private final List<Double> tokenLogprobs;
    @JsonProperty
    private final List<Map<String, Double>> topLogprobs;
    @JsonProperty
    private final List<Integer> textOffset;

    public Logprobs(Builder builder) {
        this.tokens = builder.tokens;
        this.tokenLogprobs = builder.tokenLogprobs;
        this.topLogprobs = builder.topLogprobs;
        this.textOffset = builder.textOffset;
    }

    public List<String> tokens() {
        return this.tokens;
    }

    public List<Double> tokenLogprobs() {
        return this.tokenLogprobs;
    }

    public List<Map<String, Double>> topLogprobs() {
        return this.topLogprobs;
    }

    public List<Integer> textOffset() {
        return this.textOffset;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof Logprobs && this.equalTo((Logprobs)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(Logprobs another) {
        return Objects.equals(this.tokens, another.tokens) && Objects.equals(this.tokenLogprobs, another.tokenLogprobs) && Objects.equals(this.topLogprobs, another.topLogprobs) && Objects.equals(this.textOffset, another.textOffset);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.tokens);
        h += (h << 5) + Objects.hashCode(this.tokenLogprobs);
        h += (h << 5) + Objects.hashCode(this.topLogprobs);
        h += (h << 5) + Objects.hashCode(this.textOffset);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "Logprobs{tokens=" + this.tokens + ", tokenLogprobs=" + this.tokenLogprobs + ", topLogprobs=" + this.topLogprobs + ", textOffset=" + this.textOffset + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private List<String> tokens;
        private List<Double> tokenLogprobs;
        private List<Map<String, Double>> topLogprobs;
        private List<Integer> textOffset;

        public Builder tokens(List<String> tokens) {
            if (tokens != null) {
                this.tokens = Collections.unmodifiableList(tokens);
            }
            return this;
        }

        public Builder tokenLogprobs(List<Double> tokenLogprobs) {
            if (tokenLogprobs != null) {
                this.tokenLogprobs = Collections.unmodifiableList(tokenLogprobs);
            }
            return this;
        }

        public Builder topLogprobs(List<Map<String, Double>> topLogprobs) {
            if (topLogprobs != null) {
                ArrayList<Map<String, Double>> topLogprobsCopy = new ArrayList<Map<String, Double>>();
                for (Map<String, Double> map : topLogprobs) {
                    topLogprobsCopy.add(Collections.unmodifiableMap(map));
                }
                this.topLogprobs = Collections.unmodifiableList(topLogprobsCopy);
            }
            return this;
        }

        public Builder textOffset(List<Integer> textOffset) {
            if (textOffset != null) {
                this.textOffset = Collections.unmodifiableList(textOffset);
            }
            return this;
        }

        public Logprobs build() {
            return new Logprobs(this);
        }
    }
}

