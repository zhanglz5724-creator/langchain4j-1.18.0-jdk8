/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bson.codecs.pojo.annotations.BsonId
 */
package dev.langchain4j.store.embedding.azure.cosmos.mongo.vcore;

import java.util.List;
import java.util.Map;
import org.bson.codecs.pojo.annotations.BsonId;

public class AzureCosmosDbMongoVCoreDocument {
    @BsonId
    private String id;
    private List<Float> embedding;
    private String text;
    private Map<String, String> metadata;

    public AzureCosmosDbMongoVCoreDocument(String id, List<Float> embedding, String text, Map<String, String> metadata) {
        this.id = id;
        this.embedding = embedding;
        this.text = text;
        this.metadata = metadata;
    }

    public AzureCosmosDbMongoVCoreDocument() {
    }

    public static AzureCosmosDbMongoVCoreDocumentBuilder builder() {
        return new AzureCosmosDbMongoVCoreDocumentBuilder();
    }

    public String getId() {
        return this.id;
    }

    public List<Float> getEmbedding() {
        return this.embedding;
    }

    public String getText() {
        return this.text;
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmbedding(List<Float> embedding) {
        this.embedding = embedding;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AzureCosmosDbMongoVCoreDocument)) {
            return false;
        }
        AzureCosmosDbMongoVCoreDocument other = (AzureCosmosDbMongoVCoreDocument)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        List<Float> this$embedding = this.getEmbedding();
        List<Float> other$embedding = other.getEmbedding();
        if (this$embedding == null ? other$embedding != null : !((Object)this$embedding).equals(other$embedding)) {
            return false;
        }
        String this$text = this.getText();
        String other$text = other.getText();
        if (this$text == null ? other$text != null : !this$text.equals(other$text)) {
            return false;
        }
        Map<String, String> this$metadata = this.getMetadata();
        Map<String, String> other$metadata = other.getMetadata();
        return !(this$metadata == null ? other$metadata != null : !((Object)this$metadata).equals(other$metadata));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AzureCosmosDbMongoVCoreDocument;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        List<Float> $embedding = this.getEmbedding();
        result = result * 59 + ($embedding == null ? 43 : ((Object)$embedding).hashCode());
        String $text = this.getText();
        result = result * 59 + ($text == null ? 43 : $text.hashCode());
        Map<String, String> $metadata = this.getMetadata();
        result = result * 59 + ($metadata == null ? 43 : ((Object)$metadata).hashCode());
        return result;
    }

    public String toString() {
        return "AzureCosmosDbMongoVCoreDocument(id=" + this.getId() + ", embedding=" + this.getEmbedding() + ", text=" + this.getText() + ", metadata=" + this.getMetadata() + ")";
    }

    public static class AzureCosmosDbMongoVCoreDocumentBuilder {
        private String id;
        private List<Float> embedding;
        private String text;
        private Map<String, String> metadata;

        AzureCosmosDbMongoVCoreDocumentBuilder() {
        }

        public AzureCosmosDbMongoVCoreDocumentBuilder id(String id) {
            this.id = id;
            return this;
        }

        public AzureCosmosDbMongoVCoreDocumentBuilder embedding(List<Float> embedding) {
            this.embedding = embedding;
            return this;
        }

        public AzureCosmosDbMongoVCoreDocumentBuilder text(String text) {
            this.text = text;
            return this;
        }

        public AzureCosmosDbMongoVCoreDocumentBuilder metadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        public AzureCosmosDbMongoVCoreDocument build() {
            return new AzureCosmosDbMongoVCoreDocument(this.id, this.embedding, this.text, this.metadata);
        }

        public String toString() {
            return "AzureCosmosDbMongoVCoreDocument.AzureCosmosDbMongoVCoreDocumentBuilder(id=" + this.id + ", embedding=" + this.embedding + ", text=" + this.text + ", metadata=" + this.metadata + ")";
        }
    }
}

