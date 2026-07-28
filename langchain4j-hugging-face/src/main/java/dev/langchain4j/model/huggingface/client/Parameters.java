/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.huggingface.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Objects;

@Deprecated
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Parameters {
    private final Integer topK;
    private final Double topP;
    private final Double temperature;
    private final Double repetitionPenalty;
    private final Integer maxNewTokens;
    private final Double maxTime;
    private final Boolean returnFullText;
    private final Integer numReturnSequences;
    private final Boolean doSample;

    public Parameters(Builder builder) {
        this.topK = builder.topK;
        this.topP = builder.topP;
        this.temperature = builder.temperature;
        this.repetitionPenalty = builder.repetitionPenalty;
        this.maxNewTokens = builder.maxNewTokens;
        this.maxTime = builder.maxTime;
        this.returnFullText = builder.returnFullText;
        this.numReturnSequences = builder.numReturnSequences;
        this.doSample = builder.doSample;
    }

    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof Parameters && this.equalTo((Parameters)another);
    }

    private boolean equalTo(Parameters another) {
        return Objects.equals(this.topK, another.topK) && Objects.equals(this.topP, another.topP) && Objects.equals(this.temperature, another.temperature) && Objects.equals(this.repetitionPenalty, another.repetitionPenalty) && Objects.equals(this.maxNewTokens, another.maxNewTokens) && Objects.equals(this.maxTime, another.maxTime) && Objects.equals(this.returnFullText, another.returnFullText) && Objects.equals(this.numReturnSequences, another.numReturnSequences) && Objects.equals(this.doSample, another.doSample);
    }

    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.topK);
        h += (h << 5) + Objects.hashCode(this.topP);
        h += (h << 5) + Objects.hashCode(this.temperature);
        h += (h << 5) + Objects.hashCode(this.repetitionPenalty);
        h += (h << 5) + Objects.hashCode(this.maxNewTokens);
        h += (h << 5) + Objects.hashCode(this.maxTime);
        h += (h << 5) + Objects.hashCode(this.returnFullText);
        h += (h << 5) + Objects.hashCode(this.numReturnSequences);
        h += (h << 5) + Objects.hashCode(this.doSample);
        return h;
    }

    public String toString() {
        return "TextGenerationRequest { topK = " + this.topK + ", topP = " + this.topP + ", temperature = " + this.temperature + ", repetitionPenalty = " + this.repetitionPenalty + ", maxNewTokens = " + this.maxNewTokens + ", maxTime = " + this.maxTime + ", returnFullText = " + this.returnFullText + ", numReturnSequences = " + this.numReturnSequences + ", doSample = " + this.doSample + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Integer topK;
        private Double topP;
        private Double temperature;
        private Double repetitionPenalty;
        private Integer maxNewTokens;
        private Double maxTime;
        private Boolean returnFullText;
        private Integer numReturnSequences;
        private Boolean doSample;

        public Builder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder repetitionPenalty(Double repetitionPenalty) {
            this.repetitionPenalty = repetitionPenalty;
            return this;
        }

        public Builder maxNewTokens(Integer maxNewTokens) {
            this.maxNewTokens = maxNewTokens;
            return this;
        }

        public Builder maxTime(Double maxTime) {
            this.maxTime = maxTime;
            return this;
        }

        public Builder returnFullText(Boolean returnFullText) {
            this.returnFullText = returnFullText;
            return this;
        }

        public Builder numReturnSequences(Integer numReturnSequences) {
            this.numReturnSequences = numReturnSequences;
            return this;
        }

        public Builder doSample(Boolean doSample) {
            this.doSample = doSample;
            return this;
        }

        public Parameters build() {
            return new Parameters(this);
        }
    }
}

