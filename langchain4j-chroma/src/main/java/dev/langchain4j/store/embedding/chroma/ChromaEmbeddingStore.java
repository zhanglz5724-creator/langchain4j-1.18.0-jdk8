/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.filter.Filter
 */
package dev.langchain4j.store.embedding.chroma;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.chroma.AddEmbeddingsRequest;
import dev.langchain4j.store.embedding.chroma.ChromaApiVersion;
import dev.langchain4j.store.embedding.chroma.ChromaClient;
import dev.langchain4j.store.embedding.chroma.ChromaClientV1;
import dev.langchain4j.store.embedding.chroma.ChromaClientV2;
import dev.langchain4j.store.embedding.chroma.ChromaMetadataFilterMapper;
import dev.langchain4j.store.embedding.chroma.Collection;
import dev.langchain4j.store.embedding.chroma.CreateCollectionRequest;
import dev.langchain4j.store.embedding.chroma.Database;
import dev.langchain4j.store.embedding.chroma.DeleteEmbeddingsRequest;
import dev.langchain4j.store.embedding.chroma.QueryRequest;
import dev.langchain4j.store.embedding.chroma.QueryResponse;
import dev.langchain4j.store.embedding.chroma.Tenant;
import dev.langchain4j.store.embedding.filter.Filter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ChromaEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private final ChromaClient chromaClient;
    private String collectionId;
    private final String collectionName;

    public ChromaEmbeddingStore(Builder builder) {
        this.collectionName = (String)Utils.getOrDefault((Object)builder.collectionName, (Object)"default");
        ChromaApiVersion apiVersion = (ChromaApiVersion)((Object)Utils.getOrDefault((Object)((Object)builder.apiVersion), (Object)((Object)ChromaApiVersion.V1)));
        if (apiVersion == ChromaApiVersion.V1) {
            this.chromaClient = new ChromaClientV1.Builder().baseUrl(builder.baseUrl).timeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(5L))).httpClientBuilder(builder.httpClientBuilder).customHeaders(builder.customHeadersSupplier).logRequests(builder.logRequests).logResponses(builder.logResponses).build();
        } else {
            ChromaClientV2 chromaClientV2 = new ChromaClientV2.Builder().baseUrl(builder.baseUrl).tenantName(builder.tenantName).databaseName(builder.databaseName).timeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(5L))).httpClientBuilder(builder.httpClientBuilder).customHeaders(builder.customHeadersSupplier).logRequests(builder.logRequests).logResponses(builder.logResponses).build();
            this.createTenantAndDbIfNotExists(chromaClientV2);
            this.chromaClient = chromaClientV2;
        }
        Collection collection = this.chromaClient.collection(this.collectionName);
        if (collection == null) {
            this.createCollection();
        } else {
            this.collectionId = collection.getId();
        }
    }

    @Deprecated
    public ChromaEmbeddingStore(String baseUrl, String collectionName, Duration timeout, boolean logRequests, boolean logResponses) {
        this(ChromaEmbeddingStore.builder().apiVersion(ChromaApiVersion.V1).baseUrl(baseUrl).collectionName(collectionName).logRequests(logRequests).logResponses(logResponses).timeout(timeout));
    }

    public static Builder builder() {
        return new Builder();
    }

    public String add(Embedding embedding) {
        String id = Utils.randomUUID();
        this.add(id, embedding);
        return id;
    }

    public void add(String id, Embedding embedding) {
        this.addInternal(id, embedding, null);
    }

    public String add(Embedding embedding, TextSegment textSegment) {
        String id = Utils.randomUUID();
        this.addInternal(id, embedding, textSegment);
        return id;
    }

    public List<String> addAll(List<Embedding> embeddings) {
        List<String> ids = embeddings.stream().map(embedding -> Utils.randomUUID()).collect(Collectors.toList());
        this.addAll(ids, embeddings, null);
        return ids;
    }

    private void addInternal(String id, Embedding embedding, TextSegment textSegment) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), textSegment == null ? null : Collections.singletonList(textSegment));
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> textSegments) {
        List<Object> documents;
        List<Object> metadatas;
        int size = embeddings.size();
        if (textSegments == null) {
            metadatas = Collections.nCopies(size, null);
            documents = Collections.nCopies(size, null);
        } else {
            metadatas = textSegments.stream().map(TextSegment::metadata).map(Metadata::toMap).collect(Collectors.toList());
            documents = textSegments.stream().map(TextSegment::text).collect(Collectors.toList());
        }
        AddEmbeddingsRequest addEmbeddingsRequest = AddEmbeddingsRequest.builder().embeddings(embeddings.stream().map(Embedding::vector).collect(Collectors.toList())).ids(ids).metadatas(metadatas).documents(documents).build();
        this.chromaClient.addEmbeddings(this.collectionId, addEmbeddingsRequest);
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        QueryRequest queryRequest = new QueryRequest.Builder().queryEmbeddings(request.queryEmbedding().vectorAsList()).nResults(request.maxResults()).where(ChromaMetadataFilterMapper.map(request.filter())).build();
        return new EmbeddingSearchResult(this.queryAndFilter(queryRequest, request.minScore()));
    }

    public void removeAll(java.util.Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        this.chromaClient.deleteEmbeddings(this.collectionId, DeleteEmbeddingsRequest.builder().ids(new ArrayList<String>(ids)).build());
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        this.chromaClient.deleteEmbeddings(this.collectionId, DeleteEmbeddingsRequest.builder().where(ChromaMetadataFilterMapper.map(filter)).build());
    }

    public void removeAll() {
        this.chromaClient.deleteCollection(this.collectionName);
        this.createCollection();
    }

    private List<EmbeddingMatch<TextSegment>> queryAndFilter(QueryRequest queryRequest, double minScore) {
        QueryResponse queryResponse = this.chromaClient.queryCollection(this.collectionId, queryRequest);
        List<EmbeddingMatch<TextSegment>> matches = ChromaEmbeddingStore.toEmbeddingMatches(queryResponse);
        return matches.stream().filter(match -> match.score() >= minScore).collect(Collectors.toList());
    }

    private static List<EmbeddingMatch<TextSegment>> toEmbeddingMatches(QueryResponse queryResponse) {
        ArrayList<EmbeddingMatch<TextSegment>> embeddingMatches = new ArrayList<EmbeddingMatch<TextSegment>>();
        for (int i = 0; i < queryResponse.getIds().get(0).size(); ++i) {
            double score = ChromaEmbeddingStore.distanceToScore(queryResponse.getDistances().get(0).get(i));
            String embeddingId = queryResponse.getIds().get(0).get(i);
            Embedding embedding = Embedding.from(queryResponse.getEmbeddings().get(0).get(i));
            TextSegment textSegment = ChromaEmbeddingStore.toTextSegment(queryResponse, i);
            embeddingMatches.add((EmbeddingMatch<TextSegment>)new EmbeddingMatch(Double.valueOf(score), embeddingId, embedding, (Object)textSegment));
        }
        return embeddingMatches;
    }

    private static double distanceToScore(double distance) {
        return 1.0 - distance / 2.0;
    }

    private static TextSegment toTextSegment(QueryResponse queryResponse, int i) {
        String text = queryResponse.getDocuments().get(0).get(i);
        Map<String, Object> metadata = queryResponse.getMetadatas().get(0).get(i);
        return text == null ? null : TextSegment.from((String)text, (Metadata)(metadata == null ? new Metadata() : new Metadata(metadata)));
    }

    private void createCollection() {
        this.collectionId = this.chromaClient.createCollection(new CreateCollectionRequest(this.collectionName)).getId();
    }

    private void createTenantAndDbIfNotExists(ChromaClientV2 chromaClient) {
        Database database;
        Tenant tenant = chromaClient.tenant();
        if (tenant == null) {
            chromaClient.createTenant();
        }
        if ((database = chromaClient.database()) == null) {
            chromaClient.createDatabase();
        }
    }

    public static class Builder {
        private ChromaApiVersion apiVersion;
        private String baseUrl;
        private String tenantName;
        private String databaseName;
        private String collectionName;
        private Duration timeout;
        private HttpClientBuilder httpClientBuilder;
        private Supplier<Map<String, String>> customHeadersSupplier;
        private boolean logRequests;
        private boolean logResponses;

        public Builder apiVersion(ChromaApiVersion apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
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

        public Builder collectionName(String collectionName) {
            this.collectionName = collectionName;
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

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
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

        public ChromaEmbeddingStore build() {
            return new ChromaEmbeddingStore(this);
        }
    }
}

