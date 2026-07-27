/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.image;

import dev.langchain4j.internal.Utils;
import java.net.URI;
import java.util.Objects;

public final class Image {
    private final URI url;
    private final String base64Data;
    private final String mimeType;
    private final String revisedPrompt;

    private Image(Builder builder) {
        this.url = builder.url;
        this.base64Data = builder.base64Data;
        this.mimeType = builder.mimeType;
        this.revisedPrompt = builder.revisedPrompt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public URI url() {
        return this.url;
    }

    public String base64Data() {
        return this.base64Data;
    }

    public String mimeType() {
        return this.mimeType;
    }

    public String revisedPrompt() {
        return this.revisedPrompt;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Image that = (Image)o;
        return Objects.equals(this.url, that.url) && Objects.equals(this.base64Data, that.base64Data) && Objects.equals(this.mimeType, that.mimeType) && Objects.equals(this.revisedPrompt, that.revisedPrompt);
    }

    public int hashCode() {
        return Objects.hash(this.url, this.base64Data, this.mimeType, this.revisedPrompt);
    }

    public String toString() {
        return "Image { url = " + Utils.quoted(this.url) + ", base64Data = " + Utils.quoted(this.base64Data) + ", mimeType = " + Utils.quoted(this.mimeType) + ", revisedPrompt = " + Utils.quoted(this.revisedPrompt) + " }";
    }

    public static class Builder {
        private URI url;
        private String base64Data;
        private String mimeType;
        private String revisedPrompt;

        public Builder url(URI url) {
            this.url = url;
            return this;
        }

        public Builder url(String url) {
            return this.url(URI.create(url));
        }

        public Builder base64Data(String base64Data) {
            this.base64Data = base64Data;
            return this;
        }

        public Builder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder revisedPrompt(String revisedPrompt) {
            this.revisedPrompt = revisedPrompt;
            return this;
        }

        public Image build() {
            return new Image(this);
        }
    }
}

