/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.JsonNode
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.chat.ContentType;
import dev.langchain4j.model.openai.internal.chat.ImageDetail;
import dev.langchain4j.model.openai.internal.chat.ImageUrl;
import dev.langchain4j.model.openai.internal.chat.InputAudio;
import dev.langchain4j.model.openai.internal.chat.PdfFile;
import dev.langchain4j.model.openai.internal.chat.VideoUrl;
import java.util.Locale;
import java.util.Objects;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class Content {
    @JsonProperty
    private final ContentType type;
    @JsonProperty
    private final String text;
    private final ImageUrl imageUrl;
    private final String inputImageUrl;
    @JsonProperty
    private final VideoUrl videoUrl;
    @JsonProperty
    private final InputAudio inputAudio;
    @JsonProperty
    private final PdfFile file;

    public Content(Builder builder) {
        this.type = builder.type;
        this.text = builder.text;
        this.imageUrl = builder.imageUrl;
        this.inputImageUrl = builder.inputImageUrl;
        this.videoUrl = builder.videoUrl;
        this.inputAudio = builder.inputAudio;
        this.file = builder.file;
    }

    public ContentType type() {
        return this.type;
    }

    public String text() {
        return this.text;
    }

    public ImageUrl imageUrl() {
        return this.imageUrl;
    }

    public String inputImageUrl() {
        return this.inputImageUrl;
    }

    public VideoUrl videoUrl() {
        return this.videoUrl;
    }

    public InputAudio inputAudio() {
        return this.inputAudio;
    }

    public PdfFile file() {
        return this.file;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof Content && this.equalTo((Content)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(Content another) {
        return Objects.equals((Object)this.type, (Object)another.type) && Objects.equals(this.text, another.text) && Objects.equals(this.imageUrl, another.imageUrl) && Objects.equals(this.inputImageUrl, another.inputImageUrl) && Objects.equals(this.videoUrl, another.videoUrl) && Objects.equals(this.inputAudio, another.inputAudio) && Objects.equals(this.file, another.file);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode((Object)this.type);
        h += (h << 5) + Objects.hashCode(this.text);
        h += (h << 5) + Objects.hashCode(this.imageUrl);
        h += (h << 5) + Objects.hashCode(this.inputImageUrl);
        h += (h << 5) + Objects.hashCode(this.videoUrl);
        h += (h << 5) + Objects.hashCode(this.inputAudio);
        h += (h << 5) + Objects.hashCode(this.file);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "Content{type=" + (Object)((Object)this.type) + ", text=" + this.text + ", imageUrl=" + this.imageUrl + ", inputImageUrl=" + this.inputImageUrl + ", videoUrl=" + this.videoUrl + ", inputAudio=" + this.inputAudio + ", file=" + this.file + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonProperty(value="image_url")
    private Object imageUrlForSerialization() {
        return this.imageUrl != null ? this.imageUrl : this.inputImageUrl;
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private ContentType type;
        private String text;
        private ImageUrl imageUrl;
        private String inputImageUrl;
        private VideoUrl videoUrl;
        private InputAudio inputAudio;
        private PdfFile file;

        public Builder type(ContentType type) {
            this.type = type;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder imageUrl(ImageUrl imageUrl) {
            this.imageUrl = imageUrl;
            this.inputImageUrl = null;
            return this;
        }

        @JsonProperty(value="image_url")
        Builder imageUrl(JsonNode imageUrl) {
            if (imageUrl == null || imageUrl.isNull()) {
                return this;
            }
            if (imageUrl.isTextual()) {
                return this.inputImageUrl(imageUrl.asText());
            }
            return this.imageUrl(ImageUrl.builder().url(Builder.textValue(imageUrl.get("url"))).detail(Builder.imageDetail(imageUrl.get("detail"))).build());
        }

        public Builder inputImageUrl(String inputImageUrl) {
            this.inputImageUrl = inputImageUrl;
            this.imageUrl = null;
            return this;
        }

        public Builder videoUrl(VideoUrl videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public Builder inputAudio(InputAudio inputAudio) {
            this.inputAudio = inputAudio;
            return this;
        }

        public Builder file(PdfFile file) {
            this.file = file;
            return this;
        }

        public Content build() {
            return new Content(this);
        }

        private static String textValue(JsonNode node) {
            return node == null || node.isNull() ? null : node.asText();
        }

        private static ImageDetail imageDetail(JsonNode node) {
            if (node == null || node.isNull()) {
                return null;
            }
            return ImageDetail.valueOf(node.asText().toUpperCase(Locale.ROOT));
        }
    }
}

