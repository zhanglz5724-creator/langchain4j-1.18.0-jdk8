/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.azure.cosmos.nosql;

import java.util.List;
import java.util.Map;

public class AzureCosmosDbNoSqlMatchedDocument {
    private String id;
    private List<Float> embedding;
    private String text;
    private Map<String, String> metadata;
    private Double score;

    public AzureCosmosDbNoSqlMatchedDocument(String id, List<Float> embedding, String text, Map<String, String> metadata, Double score) {
        this.id = id;
        this.embedding = embedding;
        this.text = text;
        this.metadata = metadata;
        this.score = score;
    }

    public AzureCosmosDbNoSqlMatchedDocument() {
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

    public Double getScore() {
        return this.score;
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

    public void setScore(Double score) {
        this.score = score;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AzureCosmosDbNoSqlMatchedDocument)) {
            return false;
        }
        AzureCosmosDbNoSqlMatchedDocument other = (AzureCosmosDbNoSqlMatchedDocument)o;
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
        if (this$metadata == null ? other$metadata != null : !((Object)this$metadata).equals(other$metadata)) {
            return false;
        }
        Double this$score = this.getScore();
        Double other$score = other.getScore();
        return !(this$score == null ? other$score != null : !((Object)this$score).equals(other$score));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AzureCosmosDbNoSqlMatchedDocument;
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
        Double $score = this.getScore();
        result = result * 59 + ($score == null ? 43 : ((Object)$score).hashCode());
        return result;
    }

    public String toString() {
        return "AzureCosmosDbNoSqlMatchedDocument(id=" + this.getId() + ", embedding=" + this.getEmbedding() + ", text=" + this.getText() + ", metadata=" + this.getMetadata() + ", score=" + this.getScore() + ")";
    }
}

