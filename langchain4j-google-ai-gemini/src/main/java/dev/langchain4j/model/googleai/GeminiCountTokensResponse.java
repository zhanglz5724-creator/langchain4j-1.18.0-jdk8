package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIgnoreProperties(ignoreUnknown = true) class GeminiCountTokensResponse {
    private final Object @JsonProperty("totalTokens";

    public GeminiCountTokensResponse(Object @JsonProperty("totalTokens") {
        this.@JsonProperty("totalTokens" = @JsonProperty("totalTokens";
    }

    public Object get@JsonProperty("totalTokens"() {
        return @JsonProperty("totalTokens";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeminiCountTokensResponse that = (GeminiCountTokensResponse) o;
        return java.util.Objects.equals(this.@JsonProperty("totalTokens", that.@JsonProperty("totalTokens");
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(@JsonProperty("totalTokens");
    }

    @Override
    public String toString() {
        return "GeminiCountTokensResponse{"@JsonProperty("totalTokens"=" + @JsonProperty("totalTokens" + "}"";
    }

}
