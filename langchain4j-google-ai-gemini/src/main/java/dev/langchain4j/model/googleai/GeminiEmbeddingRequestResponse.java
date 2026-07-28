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
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import java.util.List;
import java.util.Objects;

public final class GeminiEmbeddingRequestResponse {
    private GeminiEmbeddingRequestResponse() {
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    static final class GeminiBatchEmbeddingResponse {
        private final List<GeminiEmbeddingResponse.GeminiEmbeddingResponseValues> embeddings;

        @JsonCreator
        GeminiBatchEmbeddingResponse(@JsonProperty(value="embeddings") List<GeminiEmbeddingResponse.GeminiEmbeddingResponseValues> embeddings) {
            this.embeddings = embeddings;
        }

        List<GeminiEmbeddingResponse.GeminiEmbeddingResponseValues> embeddings() {
            return this.embeddings;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiBatchEmbeddingResponse)) {
                return false;
            }
            GeminiBatchEmbeddingResponse that = (GeminiBatchEmbeddingResponse)o;
            return Objects.equals(this.embeddings, that.embeddings);
        }

        public int hashCode() {
            return Objects.hash(this.embeddings);
        }

        public String toString() {
            return "GeminiBatchEmbeddingResponse[embeddings=" + this.embeddings + "]";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    static final class GeminiBatchEmbeddingRequest {
        private final List<GeminiEmbeddingRequest> requests;

        @JsonCreator
        GeminiBatchEmbeddingRequest(@JsonProperty(value="requests") List<GeminiEmbeddingRequest> requests) {
            this.requests = requests;
        }

        List<GeminiEmbeddingRequest> requests() {
            return this.requests;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiBatchEmbeddingRequest)) {
                return false;
            }
            GeminiBatchEmbeddingRequest that = (GeminiBatchEmbeddingRequest)o;
            return Objects.equals(this.requests, that.requests);
        }

        public int hashCode() {
            return Objects.hash(this.requests);
        }

        public String toString() {
            return "GeminiBatchEmbeddingRequest[requests=" + this.requests + "]";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class GeminiEmbeddingResponse {
        private final GeminiEmbeddingResponseValues embedding;

        @JsonCreator
        public GeminiEmbeddingResponse(@JsonProperty(value="embedding") GeminiEmbeddingResponseValues embedding) {
            this.embedding = embedding;
        }

        public GeminiEmbeddingResponseValues embedding() {
            return this.embedding;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiEmbeddingResponse)) {
                return false;
            }
            GeminiEmbeddingResponse that = (GeminiEmbeddingResponse)o;
            return Objects.equals(this.embedding, that.embedding);
        }

        public int hashCode() {
            return Objects.hash(this.embedding);
        }

        public String toString() {
            return "GeminiEmbeddingResponse[embedding=" + this.embedding + "]";
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        public static final class GeminiEmbeddingResponseValues {
            private final List<Float> values;

            @JsonCreator
            public GeminiEmbeddingResponseValues(@JsonProperty(value="values") List<Float> values) {
                this.values = values;
            }

            public List<Float> values() {
                return this.values;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof GeminiEmbeddingResponseValues)) {
                    return false;
                }
                GeminiEmbeddingResponseValues that = (GeminiEmbeddingResponseValues)o;
                return Objects.equals(this.values, that.values);
            }

            public int hashCode() {
                return Objects.hash(this.values);
            }

            public String toString() {
                return "GeminiEmbeddingResponseValues[values=" + this.values + "]";
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    static final class GeminiEmbeddingRequest {
        private final String model;
        private final GeminiContent content;
        private final GoogleAiEmbeddingModel.TaskType taskType;
        private final String title;
        private final Integer outputDimensionality;

        @JsonCreator
        GeminiEmbeddingRequest(@JsonProperty(value="model") String model, @JsonProperty(value="content") GeminiContent content, @JsonProperty(value="taskType") GoogleAiEmbeddingModel.TaskType taskType, @JsonProperty(value="title") String title, @JsonProperty(value="outputDimensionality") Integer outputDimensionality) {
            this.model = model;
            this.content = content;
            this.taskType = taskType;
            this.title = title;
            this.outputDimensionality = outputDimensionality;
        }

        String model() {
            return this.model;
        }

        GeminiContent content() {
            return this.content;
        }

        GoogleAiEmbeddingModel.TaskType taskType() {
            return this.taskType;
        }

        String title() {
            return this.title;
        }

        Integer outputDimensionality() {
            return this.outputDimensionality;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiEmbeddingRequest)) {
                return false;
            }
            GeminiEmbeddingRequest that = (GeminiEmbeddingRequest)o;
            return Objects.equals(this.model, that.model) && Objects.equals(this.content, that.content) && this.taskType == that.taskType && Objects.equals(this.title, that.title) && Objects.equals(this.outputDimensionality, that.outputDimensionality);
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.model, this.content, this.taskType, this.title, this.outputDimensionality});
        }

        public String toString() {
            return "GeminiEmbeddingRequest[model=" + this.model + ", content=" + this.content + ", taskType=" + (Object)((Object)this.taskType) + ", title=" + this.title + ", outputDimensionality=" + this.outputDimensionality + "]";
        }
    }
}

