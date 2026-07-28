/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.mongodb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoDbMatchedDocument {
    private String id;
    private List<Float> embedding;
    private String text;
    private Map<String, Object> metadata;
    private Double score;

    public MongoDbMatchedDocument() {
    }

    public MongoDbMatchedDocument(String id, List<Float> embedding, String text, Map<String, String> metadata, Double score) {
        this.id = id;
        this.embedding = embedding;
        this.text = text;
        this.metadata = new HashMap<String, String>(metadata);
        this.score = score;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Float> getEmbedding() {
        return this.embedding;
    }

    public void setEmbedding(List<Float> embedding) {
        this.embedding = embedding;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> getMetadata() {
        return this.metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Double getScore() {
        return this.score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}

