/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.audio;

import dev.langchain4j.internal.Utils;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

public class Audio {
    private final URI url;
    private final byte[] binaryData;
    private final String base64Data;
    private final String mimeType;

    private Audio(Builder builder) {
        this.url = builder.url;
        this.binaryData = builder.binaryData;
        this.base64Data = builder.base64Data;
        this.mimeType = builder.mimeType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public URI url() {
        return this.url;
    }

    public byte[] binaryData() {
        return this.binaryData;
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
        Audio that = (Audio)o;
        return Objects.equals(this.url, that.url) && Arrays.equals(this.binaryData, that.binaryData) && Objects.equals(this.base64Data, that.base64Data) && Objects.equals(this.mimeType, that.mimeType);
    }

    public int hashCode() {
        int result = Objects.hash(this.url, this.base64Data, this.mimeType);
        result = 31 * result + Arrays.hashCode(this.binaryData);
        return result;
    }

    public String toString() {
        return "Audio { url = " + Utils.quoted(this.url) + ", base64Data = " + Utils.quoted(this.base64Data) + ", mimeType = " + Utils.quoted(this.mimeType) + " }";
    }

    public static class Builder {
        private URI url;
        private byte[] binaryData;
        private String base64Data;
        private String mimeType;

        public Builder url(URI url) {
            this.url = url;
            return this;
        }

        public Builder url(String url) {
            return this.url(URI.create(url));
        }

        public Builder binaryData(byte[] binaryData) {
            this.binaryData = binaryData;
            return this;
        }

        public Builder base64Data(String base64Data) {
            this.base64Data = base64Data;
            return this;
        }

        public Builder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Audio build() {
            return new Audio(this);
        }
    }
}

