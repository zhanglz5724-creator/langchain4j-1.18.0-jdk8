/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.store.embedding.chroma;

import dev.langchain4j.Internal;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.store.embedding.chroma.AddEmbeddingsRequest;
import dev.langchain4j.store.embedding.chroma.ChromaApiImpl;
import dev.langchain4j.store.embedding.chroma.ChromaClient;
import dev.langchain4j.store.embedding.chroma.ChromaHttpClient;
import dev.langchain4j.store.embedding.chroma.Collection;
import dev.langchain4j.store.embedding.chroma.CreateCollectionRequest;
import dev.langchain4j.store.embedding.chroma.DeleteEmbeddingsRequest;
import dev.langchain4j.store.embedding.chroma.QueryRequest;
import dev.langchain4j.store.embedding.chroma.QueryResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.function.Supplier;

@Internal
class ChromaClientV1
implements ChromaClient {
    private final ChromaApiImpl chromaApi;

    private ChromaClientV1(Builder builder) {
        ChromaHttpClient httpClient = new ChromaHttpClient(Utils.ensureTrailingForwardSlash((String)builder.baseUrl), builder.timeout, builder.logRequests, builder.logResponses, builder.httpClientBuilder, builder.customHeadersSupplier);
        this.chromaApi = new ChromaApiImpl(httpClient);
    }

    @Override
    public Collection createCollection(CreateCollectionRequest createCollectionRequest) {
        try {
            return this.chromaApi.createCollection(createCollectionRequest);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection collection(String collectionName) {
        try {
            return this.chromaApi.collection(collectionName);
        }
        catch (RuntimeException e) {
            return null;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addEmbeddings(String collectionId, AddEmbeddingsRequest addEmbeddingsRequest) {
        try {
            return this.chromaApi.addEmbeddings(collectionId, addEmbeddingsRequest);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QueryResponse queryCollection(String collectionId, QueryRequest queryRequest) {
        try {
            return this.chromaApi.queryCollection(collectionId, queryRequest);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEmbeddings(String collectionId, DeleteEmbeddingsRequest deleteEmbeddingsRequest) {
        try {
            this.chromaApi.deleteEmbeddings(collectionId, deleteEmbeddingsRequest);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCollection(String collectionName) {
        try {
            this.chromaApi.deleteCollection(collectionName);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Builder {
        private String baseUrl;
        private Duration timeout;
        private HttpClientBuilder httpClientBuilder;
        private Supplier<Map<String, String>> customHeadersSupplier;
        private boolean logRequests;
        private boolean logResponses;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public Builder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public Builder logRequests(boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public ChromaClient build() {
            return new ChromaClientV1(this);
        }
    }
}

