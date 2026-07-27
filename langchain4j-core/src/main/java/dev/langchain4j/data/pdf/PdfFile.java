/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.pdf;

import dev.langchain4j.internal.Utils;
import java.net.URI;
import java.util.Objects;

public class PdfFile {
    public static String DEFAULT_MIME_TYPE = "application/pdf";
    private final URI url;
    private final String base64Data;
    private final String mimeType;

    private PdfFile(Builder builder) {
        this.url = builder.url;
        this.base64Data = builder.base64Data;
        this.mimeType = Utils.getOrDefault(builder.mimeType, DEFAULT_MIME_TYPE);
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

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        PdfFile that = (PdfFile)o;
        return Objects.equals(this.url, that.url) && Objects.equals(this.base64Data, that.base64Data) && Objects.equals(this.mimeType, that.mimeType);
    }

    public int hashCode() {
        return Objects.hash(this.url, this.base64Data, this.mimeType);
    }

    public String toString() {
        return "PdfFile { url = " + Utils.quoted(this.url) + ", base64Data = " + Utils.quoted(this.base64Data) + ", mimeType = " + Utils.quoted(this.mimeType) + " }";
    }

    public static class Builder {
        private URI url;
        private String base64Data;
        private String mimeType;

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

        public PdfFile build() {
            return new PdfFile(this);
        }
    }
}

