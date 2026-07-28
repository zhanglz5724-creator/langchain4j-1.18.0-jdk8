/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.model.googleai.GeminiContent;
import dev.langchain4j.model.googleai.GroundingMetadata;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
final class GeminiGenerateContentResponse {
    private final String responseId;
    private final String modelVersion;
    private final List<GeminiCandidate> candidates;
    private final GeminiUsageMetadata usageMetadata;
    private final GroundingMetadata groundingMetadata;

    @JsonCreator
    GeminiGenerateContentResponse(@JsonProperty(value="responseId") String responseId, @JsonProperty(value="modelVersion") String modelVersion, @JsonProperty(value="candidates") List<GeminiCandidate> candidates, @JsonProperty(value="usageMetadata") GeminiUsageMetadata usageMetadata, @JsonProperty(value="groundingMetadata") GroundingMetadata groundingMetadata) {
        this.responseId = responseId;
        this.modelVersion = modelVersion;
        this.candidates = candidates;
        this.usageMetadata = usageMetadata;
        this.groundingMetadata = groundingMetadata;
    }

    String responseId() {
        return this.responseId;
    }

    String modelVersion() {
        return this.modelVersion;
    }

    List<GeminiCandidate> candidates() {
        return this.candidates;
    }

    GeminiUsageMetadata usageMetadata() {
        return this.usageMetadata;
    }

    GroundingMetadata groundingMetadata() {
        return this.groundingMetadata;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeminiGenerateContentResponse)) {
            return false;
        }
        GeminiGenerateContentResponse that = (GeminiGenerateContentResponse)o;
        return Objects.equals(this.responseId, that.responseId) && Objects.equals(this.modelVersion, that.modelVersion) && Objects.equals(this.candidates, that.candidates) && Objects.equals(this.usageMetadata, that.usageMetadata) && Objects.equals(this.groundingMetadata, that.groundingMetadata);
    }

    public int hashCode() {
        return Objects.hash(this.responseId, this.modelVersion, this.candidates, this.usageMetadata, this.groundingMetadata);
    }

    public String toString() {
        return "GeminiGenerateContentResponse[responseId=" + this.responseId + ", modelVersion=" + this.modelVersion + ", candidates=" + this.candidates + ", usageMetadata=" + this.usageMetadata + ", groundingMetadata=" + this.groundingMetadata + "]";
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    static final class GeminiUsageMetadata {
        private final Integer promptTokenCount;
        private final Integer candidatesTokenCount;
        private final Integer totalTokenCount;
        private final Integer cachedContentTokenCount;
        private final Integer thoughtsTokenCount;

        @JsonCreator
        GeminiUsageMetadata(@JsonProperty(value="promptTokenCount") Integer promptTokenCount, @JsonProperty(value="candidatesTokenCount") Integer candidatesTokenCount, @JsonProperty(value="totalTokenCount") Integer totalTokenCount, @JsonProperty(value="cachedContentTokenCount") Integer cachedContentTokenCount, @JsonProperty(value="thoughtsTokenCount") Integer thoughtsTokenCount) {
            this.promptTokenCount = promptTokenCount;
            this.candidatesTokenCount = candidatesTokenCount;
            this.totalTokenCount = totalTokenCount;
            this.cachedContentTokenCount = cachedContentTokenCount;
            this.thoughtsTokenCount = thoughtsTokenCount;
        }

        Integer promptTokenCount() {
            return this.promptTokenCount;
        }

        Integer candidatesTokenCount() {
            return this.candidatesTokenCount;
        }

        Integer totalTokenCount() {
            return this.totalTokenCount;
        }

        Integer cachedContentTokenCount() {
            return this.cachedContentTokenCount;
        }

        Integer thoughtsTokenCount() {
            return this.thoughtsTokenCount;
        }

        public static Builder builder() {
            return new Builder();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiUsageMetadata)) {
                return false;
            }
            GeminiUsageMetadata that = (GeminiUsageMetadata)o;
            return Objects.equals(this.promptTokenCount, that.promptTokenCount) && Objects.equals(this.candidatesTokenCount, that.candidatesTokenCount) && Objects.equals(this.totalTokenCount, that.totalTokenCount) && Objects.equals(this.cachedContentTokenCount, that.cachedContentTokenCount) && Objects.equals(this.thoughtsTokenCount, that.thoughtsTokenCount);
        }

        public int hashCode() {
            return Objects.hash(this.promptTokenCount, this.candidatesTokenCount, this.totalTokenCount, this.cachedContentTokenCount, this.thoughtsTokenCount);
        }

        public String toString() {
            return "GeminiUsageMetadata[promptTokenCount=" + this.promptTokenCount + ", candidatesTokenCount=" + this.candidatesTokenCount + ", totalTokenCount=" + this.totalTokenCount + ", cachedContentTokenCount=" + this.cachedContentTokenCount + ", thoughtsTokenCount=" + this.thoughtsTokenCount + "]";
        }

        public static class Builder {
            private Integer promptTokenCount;
            private Integer candidatesTokenCount;
            private Integer totalTokenCount;
            private Integer cachedContentTokenCount;
            private Integer thoughtsTokenCount;

            private Builder() {
            }

            Builder promptTokenCount(Integer promptTokenCount) {
                this.promptTokenCount = promptTokenCount;
                return this;
            }

            Builder candidatesTokenCount(Integer candidatesTokenCount) {
                this.candidatesTokenCount = candidatesTokenCount;
                return this;
            }

            Builder totalTokenCount(Integer totalTokenCount) {
                this.totalTokenCount = totalTokenCount;
                return this;
            }

            Builder cachedContentTokenCount(Integer cachedContentTokenCount) {
                this.cachedContentTokenCount = cachedContentTokenCount;
                return this;
            }

            Builder thoughtsTokenCount(Integer thoughtsTokenCount) {
                this.thoughtsTokenCount = thoughtsTokenCount;
                return this;
            }

            GeminiUsageMetadata build() {
                return new GeminiUsageMetadata(this.promptTokenCount, this.candidatesTokenCount, this.totalTokenCount, this.cachedContentTokenCount, this.thoughtsTokenCount);
            }
        }
    }

    static enum GeminiUrlRetrievalStatus {
        URL_RETRIEVAL_STATUS_UNSPECIFIED,
        URL_RETRIEVAL_STATUS_SUCCESS,
        URL_RETRIEVAL_STATUS_ERROR,
        URL_RETRIEVAL_STATUS_PAYWALL,
        URL_RETRIEVAL_STATUS_UNSAFE;

    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    static final class GeminiUrlMetadata {
        private final String retrievedUrl;
        private final GeminiUrlRetrievalStatus urlRetrievalStatus;

        @JsonCreator
        GeminiUrlMetadata(@JsonProperty(value="retrievedUrl") String retrievedUrl, @JsonProperty(value="urlRetrievalStatus") GeminiUrlRetrievalStatus urlRetrievalStatus) {
            this.retrievedUrl = retrievedUrl;
            this.urlRetrievalStatus = urlRetrievalStatus;
        }

        String retrievedUrl() {
            return this.retrievedUrl;
        }

        GeminiUrlRetrievalStatus urlRetrievalStatus() {
            return this.urlRetrievalStatus;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiUrlMetadata)) {
                return false;
            }
            GeminiUrlMetadata that = (GeminiUrlMetadata)o;
            return Objects.equals(this.retrievedUrl, that.retrievedUrl) && this.urlRetrievalStatus == that.urlRetrievalStatus;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.retrievedUrl, this.urlRetrievalStatus});
        }

        public String toString() {
            return "GeminiUrlMetadata[retrievedUrl=" + this.retrievedUrl + ", urlRetrievalStatus=" + (Object)((Object)this.urlRetrievalStatus) + "]";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    static final class GeminiUrlContextMetadata {
        private final List<GeminiUrlMetadata> urlMetadata;

        @JsonCreator
        GeminiUrlContextMetadata(@JsonProperty(value="urlMetadata") List<GeminiUrlMetadata> urlMetadata) {
            this.urlMetadata = urlMetadata;
        }

        List<GeminiUrlMetadata> urlMetadata() {
            return this.urlMetadata;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiUrlContextMetadata)) {
                return false;
            }
            GeminiUrlContextMetadata that = (GeminiUrlContextMetadata)o;
            return Objects.equals(this.urlMetadata, that.urlMetadata);
        }

        public int hashCode() {
            return Objects.hash(this.urlMetadata);
        }

        public String toString() {
            return "GeminiUrlContextMetadata[urlMetadata=" + this.urlMetadata + "]";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    static final class GeminiCandidate {
        private final GeminiContent content;
        private final GeminiFinishReason finishReason;
        private final GeminiUrlContextMetadata urlContextMetadata;
        private final GroundingMetadata groundingMetadata;

        @JsonCreator
        GeminiCandidate(@JsonProperty(value="content") GeminiContent content, @JsonProperty(value="finishReason") GeminiFinishReason finishReason, @JsonProperty(value="urlContextMetadata") GeminiUrlContextMetadata urlContextMetadata, @JsonProperty(value="groundingMetadata") GroundingMetadata groundingMetadata) {
            this.content = content;
            this.finishReason = finishReason;
            this.urlContextMetadata = urlContextMetadata;
            this.groundingMetadata = groundingMetadata;
        }

        GeminiContent content() {
            return this.content;
        }

        GeminiFinishReason finishReason() {
            return this.finishReason;
        }

        GeminiUrlContextMetadata urlContextMetadata() {
            return this.urlContextMetadata;
        }

        GroundingMetadata groundingMetadata() {
            return this.groundingMetadata;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiCandidate)) {
                return false;
            }
            GeminiCandidate that = (GeminiCandidate)o;
            return Objects.equals(this.content, that.content) && this.finishReason == that.finishReason && Objects.equals(this.urlContextMetadata, that.urlContextMetadata) && Objects.equals(this.groundingMetadata, that.groundingMetadata);
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.content, this.finishReason, this.urlContextMetadata, this.groundingMetadata});
        }

        public String toString() {
            return "GeminiCandidate[content=" + this.content + ", finishReason=" + (Object)((Object)this.finishReason) + ", urlContextMetadata=" + this.urlContextMetadata + ", groundingMetadata=" + this.groundingMetadata + "]";
        }

        static enum GeminiFinishReason {
            FINISH_REASON_UNSPECIFIED,
            STOP,
            MAX_TOKENS,
            SAFETY,
            RECITATION,
            LANGUAGE,
            OTHER,
            BLOCKLIST,
            PROHIBITED_CONTENT,
            SPII,
            MALFORMED_FUNCTION_CALL,
            IMAGE_RECITATION;

        }
    }
}

