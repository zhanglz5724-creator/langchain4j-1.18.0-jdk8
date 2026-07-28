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
package dev.langchain4j.model.openai.internal.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(builder=LogProb.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class LogProb {
    @JsonProperty
    private final String token;
    @JsonProperty
    private final Double logprob;
    @JsonProperty
    private final List<Integer> bytes;
    @JsonProperty
    private final List<LogProb> topLogprobs;

    public LogProb(Builder builder) {
        this.token = builder.token;
        this.logprob = builder.logprob;
        this.bytes = builder.bytes;
        this.topLogprobs = builder.topLogprobs;
    }

    public String token() {
        return this.token;
    }

    public Double logprob() {
        return this.logprob;
    }

    public List<Integer> bytes() {
        return this.bytes;
    }

    public List<LogProb> topLogprobs() {
        return this.topLogprobs;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof LogProb && this.equalTo((LogProb)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(LogProb another) {
        return Objects.equals(this.token, another.token) && Objects.equals(this.logprob, another.logprob) && Objects.equals(this.bytes, another.bytes) && Objects.equals(this.topLogprobs, another.topLogprobs);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.token);
        h += (h << 5) + Objects.hashCode(this.logprob);
        h += (h << 5) + Objects.hashCode(this.bytes);
        h += (h << 5) + Objects.hashCode(this.topLogprobs);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "LogProb{token=" + this.token + ", logprob=" + this.logprob + ", bytes=" + this.bytes + ", topLogprobs=" + this.topLogprobs + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String token;
        private Double logprob;
        private List<Integer> bytes;
        private List<LogProb> topLogprobs;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder logprob(Double logprob) {
            this.logprob = logprob;
            return this;
        }

        public Builder bytes(List<Integer> bytes) {
            if (bytes != null) {
                this.bytes = Collections.unmodifiableList(bytes);
            }
            return this;
        }

        public Builder topLogprobs(List<LogProb> topLogprobs) {
            if (topLogprobs != null) {
                this.topLogprobs = Collections.unmodifiableList(topLogprobs);
            }
            return this;
        }

        public LogProb build() {
            return new LogProb(this);
        }
    }
}

