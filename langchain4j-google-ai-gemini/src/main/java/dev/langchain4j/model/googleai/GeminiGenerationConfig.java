/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.model.googleai.GeminiMediaResolutionLevel;
import dev.langchain4j.model.googleai.GeminiResponseModality;
import dev.langchain4j.model.googleai.GeminiSchema;
import dev.langchain4j.model.googleai.GeminiThinkingConfig;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
final class GeminiGenerationConfig {
    private final List<String> stopSequences;
    private final String responseMimeType;
    private final GeminiSchema responseSchema;
    private final Map<String, Object> responseJsonSchema;
    private final Integer candidateCount;
    private final Integer maxOutputTokens;
    private final Double temperature;
    private final Integer topK;
    private final Integer seed;
    private final Double topP;
    private final Double presencePenalty;
    private final Double frequencyPenalty;
    private final GeminiThinkingConfig thinkingConfig;
    private final Boolean responseLogprobs;
    private final Boolean enableEnhancedCivicAnswers;
    private final List<GeminiResponseModality> responseModalities;
    private final GeminiImageConfig imageConfig;
    private final Integer logprobs;
    private final GeminiMediaResolutionLevel mediaResolution;

    @JsonCreator
    GeminiGenerationConfig(@JsonProperty(value="stopSequences") List<String> stopSequences, @JsonProperty(value="responseMimeType") String responseMimeType, @JsonProperty(value="responseSchema") GeminiSchema responseSchema, @JsonProperty(value="responseJsonSchema") Map<String, Object> responseJsonSchema, @JsonProperty(value="candidateCount") Integer candidateCount, @JsonProperty(value="maxOutputTokens") Integer maxOutputTokens, @JsonProperty(value="temperature") Double temperature, @JsonProperty(value="topK") Integer topK, @JsonProperty(value="seed") Integer seed, @JsonProperty(value="topP") Double topP, @JsonProperty(value="presencePenalty") Double presencePenalty, @JsonProperty(value="frequencyPenalty") Double frequencyPenalty, @JsonProperty(value="thinkingConfig") GeminiThinkingConfig thinkingConfig, @JsonProperty(value="responseLogprobs") Boolean responseLogprobs, @JsonProperty(value="enableEnhancedCivicAnswers") Boolean enableEnhancedCivicAnswers, @JsonProperty(value="responseModalities") List<GeminiResponseModality> responseModalities, @JsonProperty(value="imageConfig") GeminiImageConfig imageConfig, @JsonProperty(value="logprobs") Integer logprobs, @JsonProperty(value="mediaResolution") GeminiMediaResolutionLevel mediaResolution) {
        this.stopSequences = stopSequences;
        this.responseMimeType = responseMimeType;
        this.responseSchema = responseSchema;
        this.responseJsonSchema = responseJsonSchema;
        this.candidateCount = candidateCount;
        this.maxOutputTokens = maxOutputTokens;
        this.temperature = temperature;
        this.topK = topK;
        this.seed = seed;
        this.topP = topP;
        this.presencePenalty = presencePenalty;
        this.frequencyPenalty = frequencyPenalty;
        this.thinkingConfig = thinkingConfig;
        this.responseLogprobs = responseLogprobs;
        this.enableEnhancedCivicAnswers = enableEnhancedCivicAnswers;
        this.responseModalities = responseModalities;
        this.imageConfig = imageConfig;
        this.logprobs = logprobs;
        this.mediaResolution = mediaResolution;
    }

    List<String> stopSequences() {
        return this.stopSequences;
    }

    String responseMimeType() {
        return this.responseMimeType;
    }

    GeminiSchema responseSchema() {
        return this.responseSchema;
    }

    Map<String, Object> responseJsonSchema() {
        return this.responseJsonSchema;
    }

    Integer candidateCount() {
        return this.candidateCount;
    }

    Integer maxOutputTokens() {
        return this.maxOutputTokens;
    }

    Double temperature() {
        return this.temperature;
    }

    Integer topK() {
        return this.topK;
    }

    Integer seed() {
        return this.seed;
    }

    Double topP() {
        return this.topP;
    }

    Double presencePenalty() {
        return this.presencePenalty;
    }

    Double frequencyPenalty() {
        return this.frequencyPenalty;
    }

    GeminiThinkingConfig thinkingConfig() {
        return this.thinkingConfig;
    }

    Boolean responseLogprobs() {
        return this.responseLogprobs;
    }

    Boolean enableEnhancedCivicAnswers() {
        return this.enableEnhancedCivicAnswers;
    }

    List<GeminiResponseModality> responseModalities() {
        return this.responseModalities;
    }

    GeminiImageConfig imageConfig() {
        return this.imageConfig;
    }

    Integer logprobs() {
        return this.logprobs;
    }

    GeminiMediaResolutionLevel mediaResolution() {
        return this.mediaResolution;
    }

    static GeminiGenerationConfigBuilder builder() {
        return new GeminiGenerationConfigBuilder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeminiGenerationConfig)) {
            return false;
        }
        GeminiGenerationConfig that = (GeminiGenerationConfig)o;
        return Objects.equals(this.stopSequences, that.stopSequences) && Objects.equals(this.responseMimeType, that.responseMimeType) && Objects.equals(this.responseSchema, that.responseSchema) && Objects.equals(this.responseJsonSchema, that.responseJsonSchema) && Objects.equals(this.candidateCount, that.candidateCount) && Objects.equals(this.maxOutputTokens, that.maxOutputTokens) && Objects.equals(this.temperature, that.temperature) && Objects.equals(this.topK, that.topK) && Objects.equals(this.seed, that.seed) && Objects.equals(this.topP, that.topP) && Objects.equals(this.presencePenalty, that.presencePenalty) && Objects.equals(this.frequencyPenalty, that.frequencyPenalty) && Objects.equals(this.thinkingConfig, that.thinkingConfig) && Objects.equals(this.responseLogprobs, that.responseLogprobs) && Objects.equals(this.enableEnhancedCivicAnswers, that.enableEnhancedCivicAnswers) && Objects.equals(this.responseModalities, that.responseModalities) && Objects.equals(this.imageConfig, that.imageConfig) && Objects.equals(this.logprobs, that.logprobs) && this.mediaResolution == that.mediaResolution;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.stopSequences, this.responseMimeType, this.responseSchema, this.responseJsonSchema, this.candidateCount, this.maxOutputTokens, this.temperature, this.topK, this.seed, this.topP, this.presencePenalty, this.frequencyPenalty, this.thinkingConfig, this.responseLogprobs, this.enableEnhancedCivicAnswers, this.responseModalities, this.imageConfig, this.logprobs, this.mediaResolution});
    }

    public String toString() {
        return "GeminiGenerationConfig[stopSequences=" + this.stopSequences + ", responseMimeType=" + this.responseMimeType + ", responseSchema=" + this.responseSchema + ", responseJsonSchema=" + this.responseJsonSchema + ", candidateCount=" + this.candidateCount + ", maxOutputTokens=" + this.maxOutputTokens + ", temperature=" + this.temperature + ", topK=" + this.topK + ", seed=" + this.seed + ", topP=" + this.topP + ", presencePenalty=" + this.presencePenalty + ", frequencyPenalty=" + this.frequencyPenalty + ", thinkingConfig=" + this.thinkingConfig + ", responseLogprobs=" + this.responseLogprobs + ", enableEnhancedCivicAnswers=" + this.enableEnhancedCivicAnswers + ", responseModalities=" + this.responseModalities + ", imageConfig=" + this.imageConfig + ", logprobs=" + this.logprobs + ", mediaResolution=" + (Object)((Object)this.mediaResolution) + "]";
    }

    static class GeminiGenerationConfigBuilder {
        private List<String> stopSequences;
        private String responseMimeType;
        private GeminiSchema responseSchema;
        private Map<String, Object> responseJsonSchema;
        private Integer candidateCount;
        private Integer maxOutputTokens;
        private Double temperature;
        private Integer topK;
        private Integer seed;
        private Double topP;
        private Double presencePenalty;
        private Double frequencyPenalty;
        private Boolean responseLogprobs;
        private Boolean enableEnhancedCivicAnswers;
        private GeminiThinkingConfig thinkingConfig;
        private Integer logprobs;
        private GeminiMediaResolutionLevel mediaResolution;
        private List<GeminiResponseModality> responseModalities;
        private GeminiImageConfig imageConfig;

        GeminiGenerationConfigBuilder() {
        }

        GeminiGenerationConfigBuilder stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return this;
        }

        GeminiGenerationConfigBuilder responseMimeType(String responseMimeType) {
            this.responseMimeType = responseMimeType;
            return this;
        }

        GeminiGenerationConfigBuilder responseSchema(GeminiSchema responseSchema) {
            this.responseSchema = responseSchema;
            return this;
        }

        GeminiGenerationConfigBuilder responseJsonSchema(Map<String, Object> responseJsonSchema) {
            this.responseJsonSchema = responseJsonSchema;
            return this;
        }

        GeminiGenerationConfigBuilder candidateCount(Integer candidateCount) {
            this.candidateCount = candidateCount;
            return this;
        }

        GeminiGenerationConfigBuilder maxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
            return this;
        }

        GeminiGenerationConfigBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        GeminiGenerationConfigBuilder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        GeminiGenerationConfigBuilder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        GeminiGenerationConfigBuilder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        GeminiGenerationConfigBuilder presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        GeminiGenerationConfigBuilder frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        GeminiGenerationConfigBuilder thinkingConfig(GeminiThinkingConfig thinkingConfig) {
            this.thinkingConfig = thinkingConfig;
            return this;
        }

        GeminiGenerationConfigBuilder responseLogprobs(Boolean responseLogprobs) {
            this.responseLogprobs = responseLogprobs;
            return this;
        }

        GeminiGenerationConfigBuilder enableEnhancedCivicAnswers(Boolean enableEnhancedCivicAnswers) {
            this.enableEnhancedCivicAnswers = enableEnhancedCivicAnswers;
            return this;
        }

        GeminiGenerationConfigBuilder logprobs(Integer logprobs) {
            this.logprobs = logprobs;
            return this;
        }

        GeminiGenerationConfigBuilder mediaResolution(GeminiMediaResolutionLevel mediaResolution) {
            this.mediaResolution = mediaResolution;
            return this;
        }

        GeminiGenerationConfigBuilder responseModalities(List<GeminiResponseModality> responseModalities) {
            this.responseModalities = responseModalities;
            return this;
        }

        GeminiGenerationConfigBuilder imageConfig(GeminiImageConfig imageConfig) {
            this.imageConfig = imageConfig;
            return this;
        }

        GeminiGenerationConfig build() {
            return new GeminiGenerationConfig(this.stopSequences, this.responseMimeType, this.responseSchema, this.responseJsonSchema, this.candidateCount, this.maxOutputTokens, this.temperature, this.topK, this.seed, this.topP, this.presencePenalty, this.frequencyPenalty, this.thinkingConfig, this.responseLogprobs, this.enableEnhancedCivicAnswers, this.responseModalities, this.imageConfig, this.logprobs, this.mediaResolution);
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    static final class GeminiImageConfig {
        private final String aspectRatio;
        private final String imageSize;

        @JsonCreator
        GeminiImageConfig(@JsonProperty(value="aspectRatio") String aspectRatio, @JsonProperty(value="imageSize") String imageSize) {
            this.aspectRatio = aspectRatio;
            this.imageSize = imageSize;
        }

        String aspectRatio() {
            return this.aspectRatio;
        }

        String imageSize() {
            return this.imageSize;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiImageConfig)) {
                return false;
            }
            GeminiImageConfig that = (GeminiImageConfig)o;
            return Objects.equals(this.aspectRatio, that.aspectRatio) && Objects.equals(this.imageSize, that.imageSize);
        }

        public int hashCode() {
            return Objects.hash(this.aspectRatio, this.imageSize);
        }

        public String toString() {
            return "GeminiImageConfig[aspectRatio=" + this.aspectRatio + ", imageSize=" + this.imageSize + "]";
        }

        static GeminiImageConfigBuilder builder() {
            return new GeminiImageConfigBuilder();
        }

        static class GeminiImageConfigBuilder {
            private String aspectRatio;
            private String imageSize;

            GeminiImageConfigBuilder() {
            }

            GeminiImageConfigBuilder aspectRatio(String aspectRatio) {
                this.aspectRatio = aspectRatio;
                return this;
            }

            GeminiImageConfigBuilder imageSize(String imageSize) {
                this.imageSize = imageSize;
                return this;
            }

            GeminiImageConfig build() {
                return new GeminiImageConfig(this.aspectRatio, this.imageSize);
            }
        }
    }
}

