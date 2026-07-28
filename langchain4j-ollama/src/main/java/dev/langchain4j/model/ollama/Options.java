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
package dev.langchain4j.model.ollama;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
class Options {
    private Integer mirostat;
    private Double mirostatEta;
    private Double mirostatTau;
    private Integer repeatLastN;
    private Double temperature;
    private Integer topK;
    private Double topP;
    private Double repeatPenalty;
    private Integer seed;
    private Integer numPredict;
    private Integer numCtx;
    private Integer numThread;
    private List<String> stop;
    private Double minP;
    private Integer numKeep;
    private Double typicalP;
    private Integer numBatch;
    private Integer numGPU;
    private Integer mainGPU;
    private Boolean useMmap;

    Options() {
    }

    Options(Builder builder) {
        this.mirostat = builder.mirostat;
        this.mirostatEta = builder.mirostatEta;
        this.mirostatTau = builder.mirostatTau;
        this.repeatLastN = builder.repeatLastN;
        this.temperature = builder.temperature;
        this.topK = builder.topK;
        this.topP = builder.topP;
        this.repeatPenalty = builder.repeatPenalty;
        this.seed = builder.seed;
        this.numPredict = builder.numPredict;
        this.numCtx = builder.numCtx;
        this.numThread = builder.numThread;
        this.stop = builder.stop;
        this.minP = builder.minP;
        this.numKeep = builder.numKeep;
        this.typicalP = builder.typicalP;
        this.numBatch = builder.numBatch;
        this.numGPU = builder.numGPU;
        this.mainGPU = builder.mainGPU;
        this.useMmap = builder.useMmap;
    }

    public Integer getMirostat() {
        return this.mirostat;
    }

    public void setMirostat(Integer mirostat) {
        this.mirostat = mirostat;
    }

    public Double getMirostatEta() {
        return this.mirostatEta;
    }

    public void setMirostatEta(Double mirostatEta) {
        this.mirostatEta = mirostatEta;
    }

    public Double getMirostatTau() {
        return this.mirostatTau;
    }

    public void setMirostatTau(Double mirostatTau) {
        this.mirostatTau = mirostatTau;
    }

    public Integer getRepeatLastN() {
        return this.repeatLastN;
    }

    public void setRepeatLastN(Integer repeatLastN) {
        this.repeatLastN = repeatLastN;
    }

    public Double getTemperature() {
        return this.temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getTopK() {
        return this.topK;
    }

    public void setTopK(Integer topK) {
        this.topK = topK;
    }

    public Double getTopP() {
        return this.topP;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public Double getRepeatPenalty() {
        return this.repeatPenalty;
    }

    public void setRepeatPenalty(Double repeatPenalty) {
        this.repeatPenalty = repeatPenalty;
    }

    public Integer getSeed() {
        return this.seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public Integer getNumPredict() {
        return this.numPredict;
    }

    public void setNumPredict(Integer numPredict) {
        this.numPredict = numPredict;
    }

    public Integer getNumCtx() {
        return this.numCtx;
    }

    public void setNumCtx(Integer numCtx) {
        this.numCtx = numCtx;
    }

    public Integer getNumThread() {
        return this.numThread;
    }

    public void setNumThread(Integer numThread) {
        this.numThread = numThread;
    }

    public List<String> getStop() {
        return this.stop;
    }

    public void setStop(List<String> stop) {
        this.stop = stop;
    }

    public Double getMinP() {
        return this.minP;
    }

    public void setMinP(Double minP) {
        this.minP = minP;
    }

    public Boolean getUseMmap() {
        return this.useMmap;
    }

    public void setUseMmap(Boolean useMmap) {
        this.useMmap = useMmap;
    }

    public Integer getMainGPU() {
        return this.mainGPU;
    }

    public void setMainGPU(Integer mainGPU) {
        this.mainGPU = mainGPU;
    }

    public Integer getNumGPU() {
        return this.numGPU;
    }

    public void setNumGPU(Integer numGPU) {
        this.numGPU = numGPU;
    }

    public Integer getNumBatch() {
        return this.numBatch;
    }

    public void setNumBatch(Integer numBatch) {
        this.numBatch = numBatch;
    }

    public Double getTypicalP() {
        return this.typicalP;
    }

    public void setTypicalP(Double typicalP) {
        this.typicalP = typicalP;
    }

    public Integer getNumKeep() {
        return this.numKeep;
    }

    public void setNumKeep(Integer numKeep) {
        this.numKeep = numKeep;
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private Integer mirostat;
        private Double mirostatEta;
        private Double mirostatTau;
        private Integer repeatLastN;
        private Double temperature;
        private Integer topK;
        private Double topP;
        private Double repeatPenalty;
        private Integer seed;
        private Integer numPredict;
        private Integer numCtx;
        private Integer numThread;
        private List<String> stop;
        private Double minP;
        private Integer numKeep;
        private Double typicalP;
        private Integer numBatch;
        private Integer numGPU;
        private Integer mainGPU;
        private Boolean useMmap;

        Builder() {
        }

        Builder mirostat(Integer mirostat) {
            this.mirostat = mirostat;
            return this;
        }

        Builder mirostatEta(Double mirostatEta) {
            this.mirostatEta = mirostatEta;
            return this;
        }

        Builder mirostatTau(Double mirostatTau) {
            this.mirostatTau = mirostatTau;
            return this;
        }

        Builder repeatLastN(Integer repeatLastN) {
            this.repeatLastN = repeatLastN;
            return this;
        }

        Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        Builder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        Builder repeatPenalty(Double repeatPenalty) {
            this.repeatPenalty = repeatPenalty;
            return this;
        }

        Builder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        Builder numPredict(Integer numPredict) {
            this.numPredict = numPredict;
            return this;
        }

        Builder numCtx(Integer numCtx) {
            this.numCtx = numCtx;
            return this;
        }

        Builder numThread(Integer numThread) {
            this.numThread = numThread;
            return this;
        }

        Builder stop(List<String> stop) {
            this.stop = stop;
            return this;
        }

        Builder minP(Double minP) {
            this.minP = minP;
            return this;
        }

        Builder numKeep(Integer numKeep) {
            this.numKeep = numKeep;
            return this;
        }

        Builder typicalP(Double typicalP) {
            this.typicalP = typicalP;
            return this;
        }

        Builder mainGPU(Integer mainGPU) {
            this.mainGPU = mainGPU;
            return this;
        }

        Builder useMmap(Boolean useMmap) {
            this.useMmap = useMmap;
            return this;
        }

        Builder numGPU(Integer numGPU) {
            this.numGPU = numGPU;
            return this;
        }

        Builder numBatch(Integer numBatch) {
            this.numBatch = numBatch;
            return this;
        }

        Options build() {
            return new Options(this);
        }
    }
}

