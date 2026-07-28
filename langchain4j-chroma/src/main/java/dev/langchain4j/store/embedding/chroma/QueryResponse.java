/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.store.embedding.chroma;

import dev.langchain4j.Internal;
import java.util.List;
import java.util.Map;

@Internal
class QueryResponse {
    private List<List<String>> ids;
    private List<List<List<Float>>> embeddings;
    private List<List<String>> documents;
    private List<List<Map<String, Object>>> metadatas;
    private List<List<Double>> distances;

    QueryResponse() {
    }

    public List<List<String>> getIds() {
        return this.ids;
    }

    public List<List<List<Float>>> getEmbeddings() {
        return this.embeddings;
    }

    public List<List<String>> getDocuments() {
        return this.documents;
    }

    public List<List<Map<String, Object>>> getMetadatas() {
        return this.metadatas;
    }

    public List<List<Double>> getDistances() {
        return this.distances;
    }
}

