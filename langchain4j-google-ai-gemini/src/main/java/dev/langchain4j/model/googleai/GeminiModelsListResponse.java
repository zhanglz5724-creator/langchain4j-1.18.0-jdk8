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
import dev.langchain4j.model.googleai.GeminiModelInfo;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
final class GeminiModelsListResponse {
    private final List<GeminiModelInfo> models;
    private final String nextPageToken;

    @JsonCreator
    GeminiModelsListResponse(@JsonProperty(value="models") List<GeminiModelInfo> models, @JsonProperty(value="nextPageToken") String nextPageToken) {
        this.models = models;
        this.nextPageToken = nextPageToken;
    }

    List<GeminiModelInfo> models() {
        return this.models;
    }

    String nextPageToken() {
        return this.nextPageToken;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeminiModelsListResponse)) {
            return false;
        }
        GeminiModelsListResponse that = (GeminiModelsListResponse)o;
        return Objects.equals(this.models, that.models) && Objects.equals(this.nextPageToken, that.nextPageToken);
    }

    public int hashCode() {
        return Objects.hash(this.models, this.nextPageToken);
    }

    public String toString() {
        return "GeminiModelsListResponse[models=" + this.models + ", nextPageToken=" + this.nextPageToken + "]";
    }
}

