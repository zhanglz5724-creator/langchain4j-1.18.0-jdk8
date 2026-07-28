/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.couchbase.client.java.Bucket
 *  com.couchbase.client.java.Cluster
 *  com.couchbase.client.java.Collection
 *  com.couchbase.client.java.manager.search.SearchIndex
 *  com.couchbase.client.java.search.SearchRequest
 *  com.couchbase.client.java.search.result.SearchResult
 *  com.couchbase.client.java.search.vector.VectorQuery
 *  com.couchbase.client.java.search.vector.VectorSearch
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.CosineSimilarity
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.RelevanceScore
 *  reactor.util.annotation.NonNull
 *  reactor.util.annotation.Nullable
 */
package dev.langchain4j.store.embedding.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.search.SearchIndex;
import com.couchbase.client.java.search.SearchRequest;
import com.couchbase.client.java.search.result.SearchResult;
import com.couchbase.client.java.search.vector.VectorQuery;
import com.couchbase.client.java.search.vector.VectorSearch;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.CosineSimilarity;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.RelevanceScore;
import dev.langchain4j.store.embedding.couchbase.Document;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

public class CouchbaseEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final String REMOVE_QUERY_PATTERN = "DELETE FROM `%s`.`%s`.`%s` WHERE %s";
    private final Cluster cluster;
    private final com.couchbase.client.java.Collection collection;
    private final String searchIndex;

    public CouchbaseEmbeddingStore(String clusterUrl, String username, String password, String bucketName, String scopeName, String collectionName, String searchIndexName, Integer dimensions) {
        this(clusterUrl, username, password, bucketName, scopeName, collectionName, searchIndexName, dimensions, 10);
    }

    public CouchbaseEmbeddingStore(String clusterUrl, String username, String password, String bucketName, String scopeName, String collectionName, String searchIndexName, Integer dimensions, Integer bucketTimeout) {
        this.cluster = Cluster.connect((String)clusterUrl, (String)username, (String)password);
        Bucket bucket = this.cluster.bucket(bucketName);
        bucket.waitUntilReady(Duration.ofSeconds(bucketTimeout.intValue()));
        this.collection = bucket.scope(scopeName).collection(collectionName);
        this.searchIndex = searchIndexName;
        if (this.cluster.searchIndexes().getAllIndexes().stream().noneMatch(i -> i.name().equals(searchIndexName))) {
            HashMap sourceParams = new HashMap();
            HashMap<String, Map<String, Object>> params = new HashMap<String, Map<String, Object>>();
            params.put("doc_config", this.docConfig());
            params.put("mapping", this.mapping(dimensions));
            HashMap planParams = new HashMap();
            SearchIndex index = new SearchIndex(null, searchIndexName, "fulltext-index", params, null, bucketName, sourceParams, "gocbcore", planParams);
            this.cluster.searchIndexes().upsertIndex(index);
        }
    }

    private Map<String, Object> mapping(Integer dimensions) {
        HashMap<String, Object> mapping = new HashMap<String, Object>();
        mapping.put("default_analyzer", "standard");
        mapping.put("default_datetime_parser", "dateTimeOptional");
        mapping.put("default_field", "_all");
        HashMap<String, Boolean> defaultMapping = new HashMap<String, Boolean>();
        defaultMapping.put("dynamic", true);
        defaultMapping.put("enabled", false);
        mapping.put("default_mapping", defaultMapping);
        mapping.put("default_type", "_default");
        mapping.put("docvalues_dynamic", false);
        mapping.put("index_dynamic", false);
        mapping.put("store_dynamic", false);
        mapping.put("type_field", "_type");
        mapping.put("types", this.types(dimensions));
        return mapping;
    }

    private Map<String, Object> types(Integer dimensions) {
        HashMap<String, Object> types = new HashMap<String, Object>();
        HashMap<String, Object> type = new HashMap<String, Object>();
        type.put("dynamic", false);
        type.put("enabled", true);
        type.put("properties", this.properties(dimensions));
        types.put(String.format("%s.%s", this.collection.scopeName(), this.collection.name()), type);
        return types;
    }

    private Map<String, Object> properties(int dimensions) {
        HashMap<String, Object> props = new HashMap<String, Object>();
        props.put("vector", this.embedding(dimensions));
        props.put("metadata", this.metadata());
        props.put("text", this.text());
        return props;
    }

    private Map<String, Object> text() {
        HashMap<String, Object> text = new HashMap<String, Object>();
        text.put("enabled", true);
        ArrayList fields = new ArrayList();
        text.put("fields", fields);
        HashMap<String, String> field = new HashMap<String, String>();
        fields.add(field);
        field.put("name", "text");
        field.put("type", "text");
        return text;
    }

    private Map<String, Object> metadata() {
        HashMap<String, Object> metadata = new HashMap<String, Object>();
        metadata.put("enabled", true);
        metadata.put("dynamic", true);
        metadata.put("fields", new ArrayList());
        return metadata;
    }

    private Map<String, Object> embedding(Integer dimensions) {
        HashMap<String, Object> embedding = new HashMap<String, Object>();
        embedding.put("enabled", true);
        ArrayList fields = new ArrayList();
        embedding.put("fields", fields);
        HashMap<String, Object> field = new HashMap<String, Object>();
        fields.add(field);
        field.put("dims", dimensions);
        field.put("index", true);
        field.put("name", "vector");
        field.put("similarity", "l2_norm");
        field.put("type", "vector");
        field.put("vector_index_optimized_for", "recall");
        return embedding;
    }

    private Map<String, Object> docConfig() {
        HashMap<String, Object> docConfig = new HashMap<String, Object>();
        docConfig.put("mode", "scope.collection.type_field");
        docConfig.put("type_field", "type");
        return docConfig;
    }

    public String add(Embedding embedding) {
        String id = UUID.randomUUID().toString();
        this.add(id, embedding);
        return id;
    }

    public void add(@NonNull String id, @NonNull Embedding embedding) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), null);
    }

    public String add(@NonNull Embedding embedding, @Nullable TextSegment textSegment) {
        return (String)this.addAll(Collections.singletonList(embedding), textSegment == null ? null : Collections.singletonList(textSegment)).get(0);
    }

    public List<String> addAll(@NonNull List<Embedding> embeddings) {
        return this.addAll(embeddings, null);
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            return;
        }
        int size = ids.size();
        if (embedded != null && embedded.size() != size) {
            throw new IllegalArgumentException("embedded and ids have different sizes");
        }
        for (int i = 0; i < size; ++i) {
            Document document = new Document();
            document.setId(ids.get(i));
            Embedding embedding = embeddings.get(i);
            document.setVector(embedding.vector());
            if (embedded != null) {
                TextSegment segment = embedded.get(i);
                document.setText(segment.text());
                document.setMetadata(segment.metadata().toMap());
            }
            this.collection.upsert(ids.get(i), (Object)document);
        }
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        ids.forEach(arg_0 -> ((com.couchbase.client.java.Collection)this.collection).remove(arg_0));
    }

    public void removeAll() {
        this.cluster.query(String.format(REMOVE_QUERY_PATTERN, this.collection.bucketName(), this.collection.scopeName(), this.collection.name(), "true"));
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        VectorQuery vectorQuery = VectorQuery.create((String)"vector", (float[])request.queryEmbedding().vector()).numCandidates(request.maxResults());
        SearchResult searchResult = this.cluster.search(this.searchIndex, SearchRequest.create((VectorSearch)VectorSearch.create((VectorQuery)vectorQuery.numCandidates(request.maxResults()))));
        List matches = searchResult.rows().stream().filter(Objects::nonNull).filter(row -> this.collection.exists(row.id()).exists()).map(row -> {
            Document data = (Document)this.collection.get(row.id()).contentAs(Document.class);
            if (data == null) {
                throw new IllegalStateException(String.format("document with id '%s' not found", row.id()));
            }
            Embedding embedding = new Embedding(data.getVector());
            return new EmbeddingMatch(Double.valueOf(RelevanceScore.fromCosineSimilarity((double)CosineSimilarity.between((Embedding)embedding, (Embedding)request.queryEmbedding()))), row.id(), embedding, (Object)(data.getText() == null ? null : new TextSegment(data.getText(), new Metadata(data.getMetadata()))));
        }).filter(r -> r.score() >= request.minScore()).collect(Collectors.toList());
        return new EmbeddingSearchResult(matches);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String clusterUrl;
        String username;
        String password;
        String bucketName;
        String scopeName;
        String collectionName;
        String searchIndexName;
        Integer dimensions;
        Integer bucketTimeout = 10;

        public Builder clusterUrl(String clusterUrl) {
            this.clusterUrl = clusterUrl;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder bucketName(String bucketName) {
            this.bucketName = bucketName;
            return this;
        }

        public Builder scopeName(String scopeName) {
            this.scopeName = scopeName;
            return this;
        }

        public Builder collectionName(String collectionName) {
            this.collectionName = collectionName;
            return this;
        }

        public Builder searchIndexName(String searchIndexName) {
            this.searchIndexName = searchIndexName;
            return this;
        }

        public Builder dimensions(Integer dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder bucketTimeout(Integer bucketTimeout) {
            this.bucketTimeout = bucketTimeout;
            return this;
        }

        public CouchbaseEmbeddingStore build() {
            return new CouchbaseEmbeddingStore(this.clusterUrl, this.username, this.password, this.bucketName, this.scopeName, this.collectionName, this.searchIndexName, this.dimensions, this.bucketTimeout);
        }
    }
}

