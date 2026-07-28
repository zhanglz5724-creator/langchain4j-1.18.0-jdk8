/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.data.document.Metadata
 */
package dev.langchain4j.store.embedding.azure.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.stream.Collectors;

public class Document {
    private String id;
    private String content;
    @JsonProperty(value="content_vector")
    private Collection<Float> contentVector;
    private Metadata metadata;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Collection<Float> getContentVector() {
        return this.contentVector;
    }

    public void setContentVector(Collection<Float> contentVector) {
        this.contentVector = contentVector;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public static class Metadata {
        private String source;
        private Collection<Attribute> attributes;

        public String getSource() {
            return this.source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Collection<Attribute> getAttributes() {
            return this.attributes;
        }

        public void setAttributes(Collection<Attribute> attributes) {
            this.attributes = attributes;
        }

        public void setAttributes(dev.langchain4j.data.document.Metadata metadata) {
            this.attributes = metadata.toMap().entrySet().stream().map(entry -> {
                Attribute attribute = new Attribute();
                attribute.setKey((String)entry.getKey());
                attribute.setValue(entry.getValue().toString());
                return attribute;
            }).collect(Collectors.toList());
        }

        public static class Attribute {
            private String key;
            private String value;

            public String getKey() {
                return this.key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getValue() {
                return this.value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}

