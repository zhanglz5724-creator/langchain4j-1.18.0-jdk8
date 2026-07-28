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
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
final class GeminiCountTokensResponse {
    private final Integer totalTokens;

    @JsonCreator
    GeminiCountTokensResponse(@JsonProperty(value="totalTokens") Integer totalTokens) {
        this.totalTokens = totalTokens;
    }

    Integer totalTokens() {
        return this.totalTokens;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeminiCountTokensResponse)) {
            return false;
        }
        GeminiCountTokensResponse that = (GeminiCountTokensResponse)o;
        return Objects.equals(this.totalTokens, that.totalTokens);
    }

    public int hashCode() {
        return Objects.hash(this.totalTokens);
    }

    public String toString() {
        return "GeminiCountTokensResponse[totalTokens=" + this.totalTokens + "]";
    }
}

