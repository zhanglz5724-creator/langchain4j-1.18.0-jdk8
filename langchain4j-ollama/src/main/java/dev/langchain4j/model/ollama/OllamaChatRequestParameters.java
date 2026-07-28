/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters$Builder
 */
package dev.langchain4j.model.ollama;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import java.util.Objects;

public class OllamaChatRequestParameters
extends DefaultChatRequestParameters {
    public static final OllamaChatRequestParameters EMPTY = OllamaChatRequestParameters.builder().build();
    private final Integer mirostat;
    private final Double mirostatEta;
    private final Double mirostatTau;
    private final Integer numCtx;
    private final Integer numThread;
    private final Integer repeatLastN;
    private final Double repeatPenalty;
    private final Integer seed;
    private final Double minP;
    private final Integer keepAlive;
    private final Boolean think;
    private final Integer numKeep;
    private final Double typicalP;
    private final Integer numBatch;
    private final Integer numGPU;
    private final Integer mainGPU;
    private final Boolean useMmap;

    private OllamaChatRequestParameters(Builder builder) {
        super((DefaultChatRequestParameters.Builder)builder);
        this.mirostat = builder.mirostat;
        this.mirostatEta = builder.mirostatEta;
        this.mirostatTau = builder.mirostatTau;
        this.numCtx = builder.numCtx;
        this.numThread = builder.numThread;
        this.repeatLastN = builder.repeatLastN;
        this.repeatPenalty = builder.repeatPenalty;
        this.seed = builder.seed;
        this.minP = builder.minP;
        this.keepAlive = builder.keepAlive;
        this.think = builder.think;
        this.numKeep = builder.numKeep;
        this.typicalP = builder.typicalP;
        this.numBatch = builder.numBatch;
        this.numGPU = builder.numGPU;
        this.mainGPU = builder.mainGPU;
        this.useMmap = builder.useMmap;
    }

    public Integer mirostat() {
        return this.mirostat;
    }

    public Double mirostatEta() {
        return this.mirostatEta;
    }

    public Double mirostatTau() {
        return this.mirostatTau;
    }

    public Integer numCtx() {
        return this.numCtx;
    }

    public Integer numThread() {
        return this.numThread;
    }

    public Boolean useMmap() {
        return this.useMmap;
    }

    public Integer mainGPU() {
        return this.mainGPU;
    }

    public Integer numGPU() {
        return this.numGPU;
    }

    public Integer numBatch() {
        return this.numBatch;
    }

    public Double typicalP() {
        return this.typicalP;
    }

    public Integer numKeep() {
        return this.numKeep;
    }

    public Integer repeatLastN() {
        return this.repeatLastN;
    }

    public Double repeatPenalty() {
        return this.repeatPenalty;
    }

    public Integer seed() {
        return this.seed;
    }

    public Double minP() {
        return this.minP;
    }

    public Integer keepAlive() {
        return this.keepAlive;
    }

    public Boolean think() {
        return this.think;
    }

    public boolean equals(Object o) {
        if (o == null || ((Object)((Object)this)).getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        OllamaChatRequestParameters that = (OllamaChatRequestParameters)((Object)o);
        return Objects.equals(this.mirostat, that.mirostat) && Objects.equals(this.mirostatEta, that.mirostatEta) && Objects.equals(this.mirostatTau, that.mirostatTau) && Objects.equals(this.numCtx, that.numCtx) && Objects.equals(this.numThread, that.numThread) && Objects.equals(this.numKeep, that.numKeep) && Objects.equals(this.typicalP, that.typicalP) && Objects.equals(this.numBatch, that.numBatch) && Objects.equals(this.numGPU, that.numGPU) && Objects.equals(this.mainGPU, that.mainGPU) && Objects.equals(this.useMmap, that.useMmap) && Objects.equals(this.repeatLastN, that.repeatLastN) && Objects.equals(this.repeatPenalty, that.repeatPenalty) && Objects.equals(this.seed, that.seed) && Objects.equals(this.minP, that.minP) && Objects.equals(this.keepAlive, that.keepAlive) && Objects.equals(this.think, that.think);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.mirostat, this.mirostatEta, this.mirostatTau, this.numCtx, this.numThread, this.numKeep, this.typicalP, this.numBatch, this.numGPU, this.mainGPU, this.useMmap, this.repeatLastN, this.repeatPenalty, this.seed, this.minP, this.keepAlive, this.think);
    }

    public String toString() {
        return "OllamaChatRequestParameters{modelName=" + Utils.quoted((Object)this.modelName()) + ", temperature=" + this.temperature() + ", topP=" + this.topP() + ", topK=" + this.topK() + ", frequencyPenalty=" + this.frequencyPenalty() + ", presencePenalty=" + this.presencePenalty() + ", maxOutputTokens=" + this.maxOutputTokens() + ", stopSequences=" + this.stopSequences() + ", toolSpecifications=" + this.toolSpecifications() + ", toolChoice=" + this.toolChoice() + ", responseFormat=" + this.responseFormat() + ", mirostat=" + this.mirostat + ", mirostatEta=" + this.mirostatEta + ", mirostatTau=" + this.mirostatTau + ", numCtx=" + this.numCtx + ", numThread=" + this.numThread + ", numKeep=" + this.numKeep + ", typicalP=" + this.typicalP + ", numBatch=" + this.numBatch + ", numGPU=" + this.numGPU + ", mainGPU=" + this.mainGPU + ", useMmap=" + this.useMmap + ", repeatLastN=" + this.repeatLastN + ", repeatPenalty=" + this.repeatPenalty + ", seed=" + this.seed + ", minP=" + this.minP + ", keepAlive=" + this.keepAlive + ", think=" + this.think + '}';
    }

    public OllamaChatRequestParameters overrideWith(ChatRequestParameters that) {
        return OllamaChatRequestParameters.builder().overrideWith((ChatRequestParameters)this).overrideWith(that).build();
    }

    public OllamaChatRequestParameters defaultedBy(ChatRequestParameters that) {
        return OllamaChatRequestParameters.builder().overrideWith(that).overrideWith((ChatRequestParameters)this).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends DefaultChatRequestParameters.Builder<Builder> {
        private Integer mirostat;
        private Double mirostatEta;
        private Double mirostatTau;
        private Integer numCtx;
        private Integer numThread;
        private Integer repeatLastN;
        private Double repeatPenalty;
        private Integer seed;
        private Double minP;
        private Integer keepAlive;
        private Boolean think;
        private Integer numKeep;
        private Double typicalP;
        private Integer numBatch;
        private Integer numGPU;
        private Integer mainGPU;
        private Boolean useMmap;

        public Builder overrideWith(ChatRequestParameters parameters) {
            super.overrideWith(parameters);
            if (parameters instanceof OllamaChatRequestParameters) {
                OllamaChatRequestParameters ollamaChatRequestParameters = (OllamaChatRequestParameters)parameters;
                this.mirostat((Integer)Utils.getOrDefault((Object)ollamaChatRequestParameters.mirostat, (Object)this.mirostat));
                this.mirostatEta((Double)Utils.getOrDefault((Object)ollamaChatRequestParameters.mirostatEta, (Object)this.mirostatEta));
                this.mirostatTau((Double)Utils.getOrDefault((Object)ollamaChatRequestParameters.mirostatTau, (Object)this.mirostatTau));
                this.numCtx((Integer)Utils.getOrDefault((Object)ollamaChatRequestParameters.numCtx, (Object)this.numCtx));
                this.numThread((Integer)Utils.getOrDefault((Object)ollamaChatRequestParameters.numThread, (Object)this.numThread));
                this.numKeep((Integer)Utils.getOrDefault((Object)ollamaChatRequestParameters.numKeep, (Object)this.numKeep));
                this.typicalP((Double)Utils.getOrDefault((Object)ollamaChatRequestParameters.typicalP, (Object)this.typicalP));
                this.numBatch((Integer)Utils.getOrDefault((Object)ollamaChatRequestParameters.numBatch, (Object)this.numBatch));
                this.numGPU((Integer)Utils.getOrDefault((Object)ollamaChatRequestParameters.numGPU, (Object)this.numGPU));
                this.mainGPU((Integer)Utils.getOrDefault((Object)ollamaChatRequestParameters.mainGPU, (Object)this.mainGPU));
                this.useMmap((Boolean)Utils.getOrDefault((Object)ollamaChatRequestParameters.useMmap, (Object)this.useMmap));
                this.repeatLastN((Integer)Utils.getOrDefault((Object)ollamaChatRequestParameters.repeatLastN, (Object)this.repeatLastN));
                this.repeatPenalty((Double)Utils.getOrDefault((Object)ollamaChatRequestParameters.repeatPenalty, (Object)this.repeatPenalty));
                this.seed((Integer)Utils.getOrDefault((Object)ollamaChatRequestParameters.seed, (Object)this.seed));
                this.minP((Double)Utils.getOrDefault((Object)ollamaChatRequestParameters.minP, (Object)this.minP));
                this.keepAlive((Integer)Utils.getOrDefault((Object)ollamaChatRequestParameters.keepAlive, (Object)this.keepAlive));
                this.think((Boolean)Utils.getOrDefault((Object)ollamaChatRequestParameters.think, (Object)this.think));
            }
            return this;
        }

        public Builder mirostat(Integer mirostat) {
            this.mirostat = mirostat;
            return this;
        }

        public Builder mirostatEta(Double mirostatEta) {
            this.mirostatEta = mirostatEta;
            return this;
        }

        public Builder mirostatTau(Double mirostatTau) {
            this.mirostatTau = mirostatTau;
            return this;
        }

        public Builder numCtx(Integer numCtx) {
            this.numCtx = numCtx;
            return this;
        }

        public Builder numThread(Integer numThread) {
            this.numThread = numThread;
            return this;
        }

        public Builder numKeep(Integer numKeep) {
            this.numKeep = numKeep;
            return this;
        }

        public Builder typicalP(Double typicalP) {
            this.typicalP = typicalP;
            return this;
        }

        public Builder numBatch(Integer numBatch) {
            this.numBatch = numBatch;
            return this;
        }

        public Builder numGPU(Integer numGPU) {
            this.numGPU = numGPU;
            return this;
        }

        public Builder mainGPU(Integer mainGPU) {
            this.mainGPU = mainGPU;
            return this;
        }

        public Builder useMmap(Boolean useMmap) {
            this.useMmap = useMmap;
            return this;
        }

        public Builder repeatLastN(Integer repeatLastN) {
            this.repeatLastN = repeatLastN;
            return this;
        }

        public Builder repeatPenalty(Double repeatPenalty) {
            this.repeatPenalty = repeatPenalty;
            return this;
        }

        public Builder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public Builder minP(Double minP) {
            this.minP = minP;
            return this;
        }

        public Builder keepAlive(Integer keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        public Builder think(Boolean think) {
            this.think = think;
            return this;
        }

        public OllamaChatRequestParameters build() {
            return new OllamaChatRequestParameters(this);
        }
    }
}

