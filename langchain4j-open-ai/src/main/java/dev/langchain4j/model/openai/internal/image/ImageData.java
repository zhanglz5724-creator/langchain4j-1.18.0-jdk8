/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 */
package dev.langchain4j.model.openai.internal.image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import java.net.URI;
import java.util.Objects;

@JsonDeserialize(builder=ImageData.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImageData {
    @JsonProperty
    private URI url;
    @JsonProperty
    private final String b64Json;
    @JsonProperty
    private final String revisedPrompt;

    public ImageData(Builder builder) {
        this.url = builder.url;
        this.b64Json = builder.b64Json;
        this.revisedPrompt = builder.revisedPrompt;
    }

    public URI url() {
        return this.url;
    }

    public String b64Json() {
        return this.b64Json;
    }

    public String revisedPrompt() {
        return this.revisedPrompt;
    }

    public void url(URI url) {
        this.url = url;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        if (another == null || this.getClass() != another.getClass()) {
            return false;
        }
        ImageData anotherImageData = (ImageData)another;
        return Objects.equals(this.url, anotherImageData.url) && Objects.equals(this.b64Json, anotherImageData.b64Json) && Objects.equals(this.revisedPrompt, anotherImageData.revisedPrompt);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        return Objects.hash(this.url, this.b64Json, this.revisedPrompt);
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ImageData{url='" + this.url + '\'' + ", b64Json='" + this.b64Json + '\'' + ", revisedPrompt='" + this.revisedPrompt + '\'' + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {
        private URI url;
        private String b64Json;
        private String revisedPrompt;

        public Builder url(URI url) {
            this.url = url;
            return this;
        }

        public Builder b64Json(String b64Json) {
            this.b64Json = b64Json;
            return this;
        }

        public Builder revisedPrompt(String revisedPrompt) {
            this.revisedPrompt = revisedPrompt;
            return this;
        }

        public ImageData build() {
            return new ImageData(this);
        }
    }
}

