package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIgnoreProperties(ignoreUnknown = true) public class UrlContextMetadata {
    private final Object @JsonProperty("urlMetadata";

    public UrlContextMetadata(Object @JsonProperty("urlMetadata") {
        this.@JsonProperty("urlMetadata" = @JsonProperty("urlMetadata";
    }

    public Object get@JsonProperty("urlMetadata"() {
        return @JsonProperty("urlMetadata";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlContextMetadata that = (UrlContextMetadata) o;
        return java.util.Objects.equals(this.@JsonProperty("urlMetadata", that.@JsonProperty("urlMetadata");
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(@JsonProperty("urlMetadata");
    }

    @Override
    public String toString() {
        return "UrlContextMetadata{"@JsonProperty("urlMetadata"=" + @JsonProperty("urlMetadata" + "}"";
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UrlMetadata(
            @JsonProperty("retrievedUrl") String retrievedUrl,
            @JsonProperty("urlRetrievalStatus") String urlRetrievalStatus) {}
}
