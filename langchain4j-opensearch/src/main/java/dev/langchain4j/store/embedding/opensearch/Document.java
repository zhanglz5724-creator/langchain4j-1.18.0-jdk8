/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.opensearch;

import java.util.Arrays;
import java.util.Map;

class Document {
    private float[] vector;
    private String text;
    private Map<String, Object> metadata;

    public Document(float[] vector, String text, Map<String, Object> metadata) {
        this.vector = vector;
        this.text = text;
        this.metadata = metadata;
    }

    public Document() {
    }

    public static DocumentBuilder builder() {
        return new DocumentBuilder();
    }

    public float[] getVector() {
        return this.vector;
    }

    public String getText() {
        return this.text;
    }

    public Map<String, Object> getMetadata() {
        return this.metadata;
    }

    public void setVector(float[] vector) {
        this.vector = vector;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        Document other = (Document)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!Arrays.equals(this.getVector(), other.getVector())) {
            return false;
        }
        String this$text = this.getText();
        String other$text = other.getText();
        if (this$text == null ? other$text != null : !this$text.equals(other$text)) {
            return false;
        }
        Map<String, Object> this$metadata = this.getMetadata();
        Map<String, Object> other$metadata = other.getMetadata();
        return !(this$metadata == null ? other$metadata != null : !((Object)this$metadata).equals(other$metadata));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Document;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + Arrays.hashCode(this.getVector());
        String $text = this.getText();
        result = result * 59 + ($text == null ? 43 : $text.hashCode());
        Map<String, Object> $metadata = this.getMetadata();
        result = result * 59 + ($metadata == null ? 43 : ((Object)$metadata).hashCode());
        return result;
    }

    public String toString() {
        return "Document(vector=" + Arrays.toString(this.getVector()) + ", text=" + this.getText() + ", metadata=" + this.getMetadata() + ")";
    }

    public static class DocumentBuilder {
        private float[] vector;
        private String text;
        private Map<String, Object> metadata;

        DocumentBuilder() {
        }

        public DocumentBuilder vector(float[] vector) {
            this.vector = vector;
            return this;
        }

        public DocumentBuilder text(String text) {
            this.text = text;
            return this;
        }

        public DocumentBuilder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Document build() {
            return new Document(this.vector, this.text, this.metadata);
        }

        public String toString() {
            return "Document.DocumentBuilder(vector=" + Arrays.toString(this.vector) + ", text=" + this.text + ", metadata=" + this.metadata + ")";
        }
    }
}

