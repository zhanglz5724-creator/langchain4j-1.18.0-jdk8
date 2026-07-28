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
package dev.langchain4j.model.openai.internal.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.chat.ImageDetail;
import java.util.Objects;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImageUrl {
    @JsonProperty
    private final String url;
    @JsonProperty
    private final ImageDetail detail;

    public ImageUrl(Builder builder) {
        this.url = builder.url;
        this.detail = builder.detail;
    }

    public String getUrl() {
        return this.url;
    }

    public ImageDetail getDetail() {
        return this.detail;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof ImageUrl && this.equalTo((ImageUrl)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(ImageUrl another) {
        return Objects.equals(this.url, another.url) && Objects.equals((Object)this.detail, (Object)another.detail);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.url);
        h += (h << 5) + Objects.hashCode((Object)this.detail);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ImageUrl{url=" + this.url + ", detail=" + (Object)((Object)this.detail) + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String url;
        private ImageDetail detail;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder detail(ImageDetail detail) {
            this.detail = detail;
            return this;
        }

        public ImageUrl build() {
            return new ImageUrl(this);
        }
    }
}

