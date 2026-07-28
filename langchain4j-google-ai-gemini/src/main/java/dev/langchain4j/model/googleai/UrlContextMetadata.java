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
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
public final class UrlContextMetadata {
    private final List<UrlMetadata> urlMetadata;

    @JsonCreator
    public UrlContextMetadata(@JsonProperty(value="urlMetadata") List<UrlMetadata> urlMetadata) {
        this.urlMetadata = urlMetadata;
    }

    public List<UrlMetadata> urlMetadata() {
        return this.urlMetadata;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UrlContextMetadata)) {
            return false;
        }
        UrlContextMetadata that = (UrlContextMetadata)o;
        return Objects.equals(this.urlMetadata, that.urlMetadata);
    }

    public int hashCode() {
        return Objects.hash(this.urlMetadata);
    }

    public String toString() {
        return "UrlContextMetadata[urlMetadata=" + this.urlMetadata + "]";
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class UrlMetadata {
        private final String retrievedUrl;
        private final String urlRetrievalStatus;

        @JsonCreator
        public UrlMetadata(@JsonProperty(value="retrievedUrl") String retrievedUrl, @JsonProperty(value="urlRetrievalStatus") String urlRetrievalStatus) {
            this.retrievedUrl = retrievedUrl;
            this.urlRetrievalStatus = urlRetrievalStatus;
        }

        public String retrievedUrl() {
            return this.retrievedUrl;
        }

        public String urlRetrievalStatus() {
            return this.urlRetrievalStatus;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof UrlMetadata)) {
                return false;
            }
            UrlMetadata that = (UrlMetadata)o;
            return Objects.equals(this.retrievedUrl, that.retrievedUrl) && Objects.equals(this.urlRetrievalStatus, that.urlRetrievalStatus);
        }

        public int hashCode() {
            return Objects.hash(this.retrievedUrl, this.urlRetrievalStatus);
        }

        public String toString() {
            return "UrlMetadata[retrievedUrl=" + this.retrievedUrl + ", urlRetrievalStatus=" + this.urlRetrievalStatus + "]";
        }
    }
}

