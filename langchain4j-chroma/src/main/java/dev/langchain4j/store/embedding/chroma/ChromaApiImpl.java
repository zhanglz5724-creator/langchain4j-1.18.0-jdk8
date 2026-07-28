/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.store.embedding.chroma;

import dev.langchain4j.Internal;
import dev.langchain4j.store.embedding.chroma.AddEmbeddingsRequest;
import dev.langchain4j.store.embedding.chroma.ChromaHttpClient;
import dev.langchain4j.store.embedding.chroma.Collection;
import dev.langchain4j.store.embedding.chroma.CreateCollectionRequest;
import dev.langchain4j.store.embedding.chroma.DeleteEmbeddingsRequest;
import dev.langchain4j.store.embedding.chroma.QueryRequest;
import dev.langchain4j.store.embedding.chroma.QueryResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Internal
class ChromaApiImpl {
    private final ChromaHttpClient httpClient;

    public ChromaApiImpl(ChromaHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Collection collection(String collectionName) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("collection_name", collectionName);
        return this.httpClient.get("api/v1/collections/{collection_name}", Collection.class, pathParams);
    }

    public Collection createCollection(CreateCollectionRequest createCollectionRequest) throws IOException {
        return this.httpClient.post("api/v1/collections", createCollectionRequest, Collection.class);
    }

    public boolean addEmbeddings(String collectionId, AddEmbeddingsRequest embedding) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("collection_id", collectionId);
        Boolean result = this.httpClient.post("api/v1/collections/{collection_id}/add", embedding, Boolean.class, pathParams);
        return Boolean.TRUE.equals(result);
    }

    public QueryResponse queryCollection(String collectionId, QueryRequest queryRequest) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("collection_id", collectionId);
        return this.httpClient.post("api/v1/collections/{collection_id}/query", queryRequest, QueryResponse.class, pathParams);
    }

    public List<String> deleteEmbeddings(String collectionId, DeleteEmbeddingsRequest embedding) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("collection_id", collectionId);
        return this.httpClient.post("api/v1/collections/{collection_id}/delete", embedding, List.class, pathParams);
    }

    public Collection deleteCollection(String collectionName) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("collection_name", collectionName);
        this.httpClient.delete("api/v1/collections/{collection_name}", pathParams);
        return null;
    }
}

