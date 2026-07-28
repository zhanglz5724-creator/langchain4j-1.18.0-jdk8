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
import dev.langchain4j.store.embedding.chroma.ChromaApiV2Impl;
import dev.langchain4j.store.embedding.chroma.ChromaClient;
import dev.langchain4j.store.embedding.chroma.ChromaHttpClient;
import dev.langchain4j.store.embedding.chroma.Collection;
import dev.langchain4j.store.embedding.chroma.CreateCollectionRequest;
import dev.langchain4j.store.embedding.chroma.Database;
import dev.langchain4j.store.embedding.chroma.DeleteEmbeddingsRequest;
import dev.langchain4j.store.embedding.chroma.QueryRequest;
import dev.langchain4j.store.embedding.chroma.QueryResponse;
import dev.langchain4j.store.embedding.chroma.Tenant;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.function.Supplier;

@Internal
class ChromaClientV2
implements ChromaClient {
    private final ChromaApiV2Impl chromaApi;
    private final String tenantName;
    private final String databaseName;

    private ChromaClientV2(Builder builder) {
        this.tenantName = (String)Utils.getOrDefault((Object)builder.tenantName, (Object)"default");
        this.databaseName = (String)Utils.getOrDefault((Object)builder.databaseName, (Object)"default");
        ChromaHttpClient httpClient = new ChromaHttpClient(Utils.ensureTrailingForwardSlash((String)builder.baseUrl), builder.timeout, builder.logRequests, builder.logResponses, builder.httpClientBuilder, builder.customHeadersSupplier);
        this.chromaApi = new ChromaApiV2Impl(httpClient);
    }

    public void createTenant() {
        try {
            this.chromaApi.createTenant(new Tenant(this.tenantName));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Tenant tenant() {
        try {
            return this.chromaApi.tenant(this.tenantName);
        }
        catch (RuntimeException e) {
            return null;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createDatabase() {
        try {
            this.chromaApi.createDatabase(this.tenantName, new Database(this.databaseName));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Database database() {
        try {
            return this.chromaApi.database(this.tenantName, this.databaseName);
        }
        catch (RuntimeException e) {
            return null;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection createCollection(CreateCollectionRequest createCollectionRequest) {
        try {
            return this.chromaApi.createCollection(this.tenantName, this.databaseName, createCollectionRequest);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection collection(String collectionName) {
        try {
            return this.chromaApi.collection(this.tenantName, this.databaseName, collectionName);
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
            this.chromaApi.addEmbeddings(this.tenantName, this.databaseName, collectionId, addEmbeddingsRequest);
            return true;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QueryResponse queryCollection(String collectionId, QueryRequest queryRequest) {
        try {
            return this.chromaApi.queryCollection(this.tenantName, this.databaseName, collectionId, queryRequest);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEmbeddings(String collectionId, DeleteEmbeddingsRequest deleteEmbeddingsRequest) {
        try {
            this.chromaApi.deleteEmbeddings(this.tenantName, this.databaseName, collectionId, deleteEmbeddingsRequest);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCollection(String collectionName) {
        try {
            this.chromaApi.deleteCollection(this.tenantName, this.databaseName, collectionName);
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
        private String tenantName;
        private String databaseName;

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

        public Builder tenantName(String tenantName) {
            this.tenantName = tenantName;
            return this;
        }

        public Builder databaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public ChromaClientV2 build() {
            return new ChromaClientV2(this);
        }
    }
}

