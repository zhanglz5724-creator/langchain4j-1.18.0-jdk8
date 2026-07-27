package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.internal.Utils;
import java.util.List;
import org.jspecify.annotations.Nullable;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIgnoreProperties(ignoreUnknown = true) class GeminiCountTokensRequest {
    private final Object @JsonProperty("contents";

    public GeminiCountTokensRequest(Object @JsonProperty("contents") {
        this.@JsonProperty("contents" = @JsonProperty("contents";
    }

    public Object get@JsonProperty("contents"() {
        return @JsonProperty("contents";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeminiCountTokensRequest that = (GeminiCountTokensRequest) o;
        return java.util.Objects.equals(this.@JsonProperty("contents", that.@JsonProperty("contents");
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(@JsonProperty("contents");
    }

    @Override
    public String toString() {
        return "GeminiCountTokensRequest{"@JsonProperty("contents"=" + @JsonProperty("contents" + "}"";
    }

    GeminiCountTokensRequest {
        if (Utils.isNullOrEmpty(contents) && generateContentRequest == null) {
            throw new IllegalArgumentException("Either contents or generateContentRequest should be set");
        }
    }
}
