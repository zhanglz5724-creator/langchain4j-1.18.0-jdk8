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
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
public final class GroundingMetadata {
    private final List<GroundingChunk> groundingChunks;
    private final List<GroundingSupport> groundingSupports;
    private final List<String> webSearchQueries;
    private final SearchEntryPoint searchEntryPoint;
    private final RetrievalMetadata retrievalMetadata;
    private final String googleMapsWidgetContextToken;

    @JsonCreator
    public GroundingMetadata(@JsonProperty(value="groundingChunks") List<GroundingChunk> groundingChunks, @JsonProperty(value="groundingSupports") List<GroundingSupport> groundingSupports, @JsonProperty(value="webSearchQueries") List<String> webSearchQueries, @JsonProperty(value="searchEntryPoint") SearchEntryPoint searchEntryPoint, @JsonProperty(value="retrievalMetadata") RetrievalMetadata retrievalMetadata, @JsonProperty(value="googleMapsWidgetContextToken") String googleMapsWidgetContextToken) {
        this.groundingChunks = groundingChunks;
        this.groundingSupports = groundingSupports;
        this.webSearchQueries = webSearchQueries;
        this.searchEntryPoint = searchEntryPoint;
        this.retrievalMetadata = retrievalMetadata;
        this.googleMapsWidgetContextToken = googleMapsWidgetContextToken;
    }

    public List<GroundingChunk> groundingChunks() {
        return this.groundingChunks;
    }

    public List<GroundingSupport> groundingSupports() {
        return this.groundingSupports;
    }

    public List<String> webSearchQueries() {
        return this.webSearchQueries;
    }

    public SearchEntryPoint searchEntryPoint() {
        return this.searchEntryPoint;
    }

    public RetrievalMetadata retrievalMetadata() {
        return this.retrievalMetadata;
    }

    public String googleMapsWidgetContextToken() {
        return this.googleMapsWidgetContextToken;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroundingMetadata)) {
            return false;
        }
        GroundingMetadata that = (GroundingMetadata)o;
        return Objects.equals(this.groundingChunks, that.groundingChunks) && Objects.equals(this.groundingSupports, that.groundingSupports) && Objects.equals(this.webSearchQueries, that.webSearchQueries) && Objects.equals(this.searchEntryPoint, that.searchEntryPoint) && Objects.equals(this.retrievalMetadata, that.retrievalMetadata) && Objects.equals(this.googleMapsWidgetContextToken, that.googleMapsWidgetContextToken);
    }

    public int hashCode() {
        return Objects.hash(this.groundingChunks, this.groundingSupports, this.webSearchQueries, this.searchEntryPoint, this.retrievalMetadata, this.googleMapsWidgetContextToken);
    }

    public String toString() {
        return "GroundingMetadata[groundingChunks=" + this.groundingChunks + ", groundingSupports=" + this.groundingSupports + ", webSearchQueries=" + this.webSearchQueries + ", searchEntryPoint=" + this.searchEntryPoint + ", retrievalMetadata=" + this.retrievalMetadata + ", googleMapsWidgetContextToken=" + this.googleMapsWidgetContextToken + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class RetrievalMetadata {
        private final Double googleSearchDynamicRetrievalScore;

        @JsonCreator
        public RetrievalMetadata(@JsonProperty(value="googleSearchDynamicRetrievalScore") Double googleSearchDynamicRetrievalScore) {
            this.googleSearchDynamicRetrievalScore = googleSearchDynamicRetrievalScore;
        }

        public Double googleSearchDynamicRetrievalScore() {
            return this.googleSearchDynamicRetrievalScore;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof RetrievalMetadata)) {
                return false;
            }
            RetrievalMetadata that = (RetrievalMetadata)o;
            return Objects.equals(this.googleSearchDynamicRetrievalScore, that.googleSearchDynamicRetrievalScore);
        }

        public int hashCode() {
            return Objects.hash(this.googleSearchDynamicRetrievalScore);
        }

        public String toString() {
            return "RetrievalMetadata[googleSearchDynamicRetrievalScore=" + this.googleSearchDynamicRetrievalScore + "]";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class SearchEntryPoint {
        private final String renderedContent;
        private final String sdkBlob;

        @JsonCreator
        public SearchEntryPoint(@JsonProperty(value="renderedContent") String renderedContent, @JsonProperty(value="sdkBlob") String sdkBlob) {
            this.renderedContent = renderedContent;
            this.sdkBlob = sdkBlob;
        }

        public String renderedContent() {
            return this.renderedContent;
        }

        public String sdkBlob() {
            return this.sdkBlob;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SearchEntryPoint)) {
                return false;
            }
            SearchEntryPoint that = (SearchEntryPoint)o;
            return Objects.equals(this.renderedContent, that.renderedContent) && Objects.equals(this.sdkBlob, that.sdkBlob);
        }

        public int hashCode() {
            return Objects.hash(this.renderedContent, this.sdkBlob);
        }

        public String toString() {
            return "SearchEntryPoint[renderedContent=" + this.renderedContent + ", sdkBlob=" + this.sdkBlob + "]";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class Segment {
        private final Integer partIndex;
        private final Integer startIndex;
        private final Integer endIndex;
        private final String text;

        @JsonCreator
        public Segment(@JsonProperty(value="partIndex") Integer partIndex, @JsonProperty(value="startIndex") Integer startIndex, @JsonProperty(value="endIndex") Integer endIndex, @JsonProperty(value="text") String text) {
            this.partIndex = partIndex;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.text = text;
        }

        public Integer partIndex() {
            return this.partIndex;
        }

        public Integer startIndex() {
            return this.startIndex;
        }

        public Integer endIndex() {
            return this.endIndex;
        }

        public String text() {
            return this.text;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Segment)) {
                return false;
            }
            Segment that = (Segment)o;
            return Objects.equals(this.partIndex, that.partIndex) && Objects.equals(this.startIndex, that.startIndex) && Objects.equals(this.endIndex, that.endIndex) && Objects.equals(this.text, that.text);
        }

        public int hashCode() {
            return Objects.hash(this.partIndex, this.startIndex, this.endIndex, this.text);
        }

        public String toString() {
            return "Segment[partIndex=" + this.partIndex + ", startIndex=" + this.startIndex + ", endIndex=" + this.endIndex + ", text=" + this.text + "]";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class GroundingSupport {
        private final List<Integer> groundingChunkIndices;
        private final List<Double> confidenceScores;
        private final Segment segment;

        @JsonCreator
        public GroundingSupport(@JsonProperty(value="groundingChunkIndices") List<Integer> groundingChunkIndices, @JsonProperty(value="confidenceScores") List<Double> confidenceScores, @JsonProperty(value="segment") Segment segment) {
            this.groundingChunkIndices = groundingChunkIndices;
            this.confidenceScores = confidenceScores;
            this.segment = segment;
        }

        public List<Integer> groundingChunkIndices() {
            return this.groundingChunkIndices;
        }

        public List<Double> confidenceScores() {
            return this.confidenceScores;
        }

        public Segment segment() {
            return this.segment;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GroundingSupport)) {
                return false;
            }
            GroundingSupport that = (GroundingSupport)o;
            return Objects.equals(this.groundingChunkIndices, that.groundingChunkIndices) && Objects.equals(this.confidenceScores, that.confidenceScores) && Objects.equals(this.segment, that.segment);
        }

        public int hashCode() {
            return Objects.hash(this.groundingChunkIndices, this.confidenceScores, this.segment);
        }

        public String toString() {
            return "GroundingSupport[groundingChunkIndices=" + this.groundingChunkIndices + ", confidenceScores=" + this.confidenceScores + ", segment=" + this.segment + "]";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class GroundingChunk {
        private final Web web;
        private final RetrievedContext retrievedContext;
        private final Maps maps;

        @JsonCreator
        public GroundingChunk(@JsonProperty(value="web") Web web, @JsonProperty(value="retrievedContext") RetrievedContext retrievedContext, @JsonProperty(value="maps") Maps maps) {
            this.web = web;
            this.retrievedContext = retrievedContext;
            this.maps = maps;
        }

        public Web web() {
            return this.web;
        }

        public RetrievedContext retrievedContext() {
            return this.retrievedContext;
        }

        public Maps maps() {
            return this.maps;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GroundingChunk)) {
                return false;
            }
            GroundingChunk that = (GroundingChunk)o;
            return Objects.equals(this.web, that.web) && Objects.equals(this.retrievedContext, that.retrievedContext) && Objects.equals(this.maps, that.maps);
        }

        public int hashCode() {
            return Objects.hash(this.web, this.retrievedContext, this.maps);
        }

        public String toString() {
            return "GroundingChunk[web=" + this.web + ", retrievedContext=" + this.retrievedContext + ", maps=" + this.maps + "]";
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        public static final class Maps {
            private final String uri;
            private final String title;
            private final String text;
            private final String placeId;
            private final PlaceAnswerSources placeAnswerSources;

            @JsonCreator
            public Maps(@JsonProperty(value="uri") String uri, @JsonProperty(value="title") String title, @JsonProperty(value="text") String text, @JsonProperty(value="placeId") String placeId, @JsonProperty(value="placeAnswerSources") PlaceAnswerSources placeAnswerSources) {
                this.uri = uri;
                this.title = title;
                this.text = text;
                this.placeId = placeId;
                this.placeAnswerSources = placeAnswerSources;
            }

            public String uri() {
                return this.uri;
            }

            public String title() {
                return this.title;
            }

            public String text() {
                return this.text;
            }

            public String placeId() {
                return this.placeId;
            }

            public PlaceAnswerSources placeAnswerSources() {
                return this.placeAnswerSources;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof Maps)) {
                    return false;
                }
                Maps that = (Maps)o;
                return Objects.equals(this.uri, that.uri) && Objects.equals(this.title, that.title) && Objects.equals(this.text, that.text) && Objects.equals(this.placeId, that.placeId) && Objects.equals(this.placeAnswerSources, that.placeAnswerSources);
            }

            public int hashCode() {
                return Objects.hash(this.uri, this.title, this.text, this.placeId, this.placeAnswerSources);
            }

            public String toString() {
                return "Maps[uri=" + this.uri + ", title=" + this.title + ", text=" + this.text + ", placeId=" + this.placeId + ", placeAnswerSources=" + this.placeAnswerSources + "]";
            }

            @JsonIgnoreProperties(ignoreUnknown=true)
            public static final class ReviewSnippet {
                private final String reviewId;
                private final String googleMapsUri;
                private final String title;

                @JsonCreator
                public ReviewSnippet(@JsonProperty(value="reviewId") String reviewId, @JsonProperty(value="googleMapsUri") String googleMapsUri, @JsonProperty(value="title") String title) {
                    this.reviewId = reviewId;
                    this.googleMapsUri = googleMapsUri;
                    this.title = title;
                }

                public String reviewId() {
                    return this.reviewId;
                }

                public String googleMapsUri() {
                    return this.googleMapsUri;
                }

                public String title() {
                    return this.title;
                }

                public boolean equals(Object o) {
                    if (this == o) {
                        return true;
                    }
                    if (!(o instanceof ReviewSnippet)) {
                        return false;
                    }
                    ReviewSnippet that = (ReviewSnippet)o;
                    return Objects.equals(this.reviewId, that.reviewId) && Objects.equals(this.googleMapsUri, that.googleMapsUri) && Objects.equals(this.title, that.title);
                }

                public int hashCode() {
                    return Objects.hash(this.reviewId, this.googleMapsUri, this.title);
                }

                public String toString() {
                    return "ReviewSnippet[reviewId=" + this.reviewId + ", googleMapsUri=" + this.googleMapsUri + ", title=" + this.title + "]";
                }
            }

            @JsonIgnoreProperties(ignoreUnknown=true)
            public static final class PlaceAnswerSources {
                private final List<ReviewSnippet> reviewSnippets;

                @JsonCreator
                public PlaceAnswerSources(@JsonProperty(value="reviewSnippets") List<ReviewSnippet> reviewSnippets) {
                    this.reviewSnippets = reviewSnippets;
                }

                public List<ReviewSnippet> reviewSnippets() {
                    return this.reviewSnippets;
                }

                public boolean equals(Object o) {
                    if (this == o) {
                        return true;
                    }
                    if (!(o instanceof PlaceAnswerSources)) {
                        return false;
                    }
                    PlaceAnswerSources that = (PlaceAnswerSources)o;
                    return Objects.equals(this.reviewSnippets, that.reviewSnippets);
                }

                public int hashCode() {
                    return Objects.hash(this.reviewSnippets);
                }

                public String toString() {
                    return "PlaceAnswerSources[reviewSnippets=" + this.reviewSnippets + "]";
                }
            }
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        public static final class RetrievedContext {
            private final String uri;
            private final String title;
            private final String text;

            @JsonCreator
            public RetrievedContext(@JsonProperty(value="uri") String uri, @JsonProperty(value="title") String title, @JsonProperty(value="text") String text) {
                this.uri = uri;
                this.title = title;
                this.text = text;
            }

            public String uri() {
                return this.uri;
            }

            public String title() {
                return this.title;
            }

            public String text() {
                return this.text;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof RetrievedContext)) {
                    return false;
                }
                RetrievedContext that = (RetrievedContext)o;
                return Objects.equals(this.uri, that.uri) && Objects.equals(this.title, that.title) && Objects.equals(this.text, that.text);
            }

            public int hashCode() {
                return Objects.hash(this.uri, this.title, this.text);
            }

            public String toString() {
                return "RetrievedContext[uri=" + this.uri + ", title=" + this.title + ", text=" + this.text + "]";
            }
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        public static final class Web {
            private final String uri;
            private final String title;

            @JsonCreator
            public Web(@JsonProperty(value="uri") String uri, @JsonProperty(value="title") String title) {
                this.uri = uri;
                this.title = title;
            }

            public String uri() {
                return this.uri;
            }

            public String title() {
                return this.title;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof Web)) {
                    return false;
                }
                Web that = (Web)o;
                return Objects.equals(this.uri, that.uri) && Objects.equals(this.title, that.title);
            }

            public int hashCode() {
                return Objects.hash(this.uri, this.title);
            }

            public String toString() {
                return "Web[uri=" + this.uri + ", title=" + this.title + "]";
            }
        }
    }

    public static class Builder {
        private List<GroundingChunk> groundingChunks;
        private List<GroundingSupport> groundingSupports;
        private List<String> webSearchQueries;
        private SearchEntryPoint searchEntryPoint;
        private RetrievalMetadata retrievalMetadata;
        private String googleMapsWidgetContextToken;

        public Builder groundingChunks(List<GroundingChunk> groundingChunks) {
            this.groundingChunks = groundingChunks;
            return this;
        }

        public Builder groundingSupports(List<GroundingSupport> groundingSupports) {
            this.groundingSupports = groundingSupports;
            return this;
        }

        public Builder webSearchQueries(List<String> webSearchQueries) {
            this.webSearchQueries = webSearchQueries;
            return this;
        }

        public Builder searchEntryPoint(SearchEntryPoint searchEntryPoint) {
            this.searchEntryPoint = searchEntryPoint;
            return this;
        }

        public Builder retrievalMetadata(RetrievalMetadata retrievalMetadata) {
            this.retrievalMetadata = retrievalMetadata;
            return this;
        }

        public Builder googleMapsWidgetContextToken(String googleMapsWidgetContextToken) {
            this.googleMapsWidgetContextToken = googleMapsWidgetContextToken;
            return this;
        }

        public GroundingMetadata build() {
            return new GroundingMetadata(this.groundingChunks, this.groundingSupports, this.webSearchQueries, this.searchEntryPoint, this.retrievalMetadata, this.googleMapsWidgetContextToken);
        }
    }
}

