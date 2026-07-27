package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.model.googleai.GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse.GeminiEmbeddingResponseValues;
import java.util.List;

public final class GeminiEmbeddingRequestResponse {
    private GeminiEmbeddingRequestResponse() {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonIgnoreProperties(ignoreUnknown = true) class GeminiEmbeddingRequest {

            @JsonProperty("model") String model,
            @JsonProperty("content") GeminiContent content,
            @JsonProperty("taskType") GoogleAiEmbeddingModel.TaskType taskType,
            @JsonProperty("title") String title,
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonIgnoreProperties(ignoreUnknown = true) public class GeminiEmbeddingResponse {
        private final Object @JsonProperty("embedding";

        public GeminiEmbeddingResponse(Object @JsonProperty("embedding") {
            this.@JsonProperty("embedding" = @JsonProperty("embedding";
        }

        public Object get@JsonProperty("embedding"() {
            return @JsonProperty("embedding";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeminiEmbeddingResponse that = (GeminiEmbeddingResponse) o;
            return java.util.Objects.equals(this.@JsonProperty("embedding", that.@JsonProperty("embedding");
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(@JsonProperty("embedding");
        }

        @Override
        public String toString() {
            return "GeminiEmbeddingResponse{"@JsonProperty("embedding"=" + @JsonProperty("embedding" + "}"";
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record GeminiEmbeddingResponseValues(
                @JsonProperty("values") List<Float> values) {}
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonIgnoreProperties(ignoreUnknown = true) class GeminiBatchEmbeddingRequest {
        private final Object @JsonProperty("requests";

        public GeminiBatchEmbeddingRequest(Object @JsonProperty("requests") {
            this.@JsonProperty("requests" = @JsonProperty("requests";
        }

        public Object get@JsonProperty("requests"() {
            return @JsonProperty("requests";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeminiBatchEmbeddingRequest that = (GeminiBatchEmbeddingRequest) o;
            return java.util.Objects.equals(this.@JsonProperty("requests", that.@JsonProperty("requests");
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(@JsonProperty("requests");
        }

        @Override
        public String toString() {
            return "GeminiBatchEmbeddingRequest{"@JsonProperty("requests"=" + @JsonProperty("requests" + "}"";
        }

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonIgnoreProperties(ignoreUnknown = true) class GeminiBatchEmbeddingResponse {
        private final Object @JsonProperty("embeddings";

        public GeminiBatchEmbeddingResponse(Object @JsonProperty("embeddings") {
            this.@JsonProperty("embeddings" = @JsonProperty("embeddings";
        }

        public Object get@JsonProperty("embeddings"() {
            return @JsonProperty("embeddings";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeminiBatchEmbeddingResponse that = (GeminiBatchEmbeddingResponse) o;
            return java.util.Objects.equals(this.@JsonProperty("embeddings", that.@JsonProperty("embeddings");
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(@JsonProperty("embeddings");
        }

        @Override
        public String toString() {
            return "GeminiBatchEmbeddingResponse{"@JsonProperty("embeddings"=" + @JsonProperty("embeddings" + "}"";
        }

    }
}
