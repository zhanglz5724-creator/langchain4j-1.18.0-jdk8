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
import dev.langchain4j.store.embedding.chroma.Database;
import dev.langchain4j.store.embedding.chroma.DeleteEmbeddingsRequest;
import dev.langchain4j.store.embedding.chroma.QueryRequest;
import dev.langchain4j.store.embedding.chroma.QueryResponse;
import dev.langchain4j.store.embedding.chroma.Tenant;
import java.io.IOException;
import java.util.HashMap;

@Internal
class ChromaApiV2Impl {
    private final ChromaHttpClient httpClient;

    public ChromaApiV2Impl(ChromaHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void createTenant(Tenant tenant) throws IOException {
        this.httpClient.post("api/v2/tenants", tenant, Void.class);
    }

    public Tenant tenant(String tenantName) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("tenant_name", tenantName);
        return this.httpClient.get("api/v2/tenants/{tenant_name}", Tenant.class, pathParams);
    }

    public void createDatabase(String tenantName, Database createDatabaseRequest) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("tenant_name", tenantName);
        this.httpClient.post("api/v2/tenants/{tenant_name}/databases", createDatabaseRequest, Void.class, pathParams);
    }

    public Database database(String tenantName, String databaseName) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("tenant_name", tenantName);
        pathParams.put("database_name", databaseName);
        return this.httpClient.get("api/v2/tenants/{tenant_name}/databases/{database_name}", Database.class, pathParams);
    }

    public Collection collection(String tenantName, String databaseName, String collectionName) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("tenant_name", tenantName);
        pathParams.put("database_name", databaseName);
        pathParams.put("collection_name", collectionName);
        return this.httpClient.get("api/v2/tenants/{tenant_name}/databases/{database_name}/collections/{collection_name}", Collection.class, pathParams);
    }

    public Collection createCollection(String tenantName, String databaseName, CreateCollectionRequest createCollectionRequest) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("tenant_name", tenantName);
        pathParams.put("database_name", databaseName);
        return this.httpClient.post("api/v2/tenants/{tenant_name}/databases/{database_name}/collections", createCollectionRequest, Collection.class, pathParams);
    }

    public void addEmbeddings(String tenantName, String databaseName, String collectionId, AddEmbeddingsRequest embedding) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("tenant_name", tenantName);
        pathParams.put("database_name", databaseName);
        pathParams.put("collection_id", collectionId);
        this.httpClient.post("api/v2/tenants/{tenant_name}/databases/{database_name}/collections/{collection_id}/add", embedding, Void.class, pathParams);
    }

    public QueryResponse queryCollection(String tenantName, String databaseName, String collectionId, QueryRequest queryRequest) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("tenant_name", tenantName);
        pathParams.put("database_name", databaseName);
        pathParams.put("collection_id", collectionId);
        return this.httpClient.post("api/v2/tenants/{tenant_name}/databases/{database_name}/collections/{collection_id}/query", queryRequest, QueryResponse.class, pathParams);
    }

    public void deleteEmbeddings(String tenantName, String databaseName, String collectionId, DeleteEmbeddingsRequest embedding) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("tenant_name", tenantName);
        pathParams.put("database_name", databaseName);
        pathParams.put("collection_id", collectionId);
        this.httpClient.post("api/v2/tenants/{tenant_name}/databases/{database_name}/collections/{collection_id}/delete", embedding, Void.class, pathParams);
    }

    public Collection deleteCollection(String tenantName, String databaseName, String collectionName) throws IOException {
        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("tenant_name", tenantName);
        pathParams.put("database_name", databaseName);
        pathParams.put("collection_name", collectionName);
        this.httpClient.delete("api/v2/tenants/{tenant_name}/databases/{database_name}/collections/{collection_name}", pathParams);
        return null;
    }
}

