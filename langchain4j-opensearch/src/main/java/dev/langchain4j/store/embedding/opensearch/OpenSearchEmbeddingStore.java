/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.filter.Filter
 *  org.apache.hc.client5.http.auth.AuthScope
 *  org.apache.hc.client5.http.auth.Credentials
 *  org.apache.hc.client5.http.auth.CredentialsProvider
 *  org.apache.hc.client5.http.auth.UsernamePasswordCredentials
 *  org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider
 *  org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder
 *  org.apache.hc.client5.http.nio.AsyncClientConnectionManager
 *  org.apache.hc.core5.http.HttpHost
 *  org.apache.hc.core5.http.message.BasicHeader
 *  org.opensearch.client.json.JsonData
 *  org.opensearch.client.json.JsonpMapper
 *  org.opensearch.client.json.jackson.JacksonJsonpMapper
 *  org.opensearch.client.opensearch.OpenSearchClient
 *  org.opensearch.client.opensearch._types.BulkIndexByScrollFailure
 *  org.opensearch.client.opensearch._types.ErrorCause
 *  org.opensearch.client.opensearch._types.InlineScript
 *  org.opensearch.client.opensearch._types.InlineScript$Builder
 *  org.opensearch.client.opensearch._types.OpenSearchException
 *  org.opensearch.client.opensearch._types.mapping.DynamicMapping
 *  org.opensearch.client.opensearch._types.mapping.ObjectProperty$Builder
 *  org.opensearch.client.opensearch._types.mapping.Property
 *  org.opensearch.client.opensearch._types.mapping.TextProperty
 *  org.opensearch.client.opensearch._types.mapping.TypeMapping
 *  org.opensearch.client.opensearch._types.query_dsl.Query
 *  org.opensearch.client.opensearch._types.query_dsl.ScriptScoreQuery
 *  org.opensearch.client.opensearch._types.query_dsl.ScriptScoreQuery$Builder
 *  org.opensearch.client.opensearch.core.BulkRequest$Builder
 *  org.opensearch.client.opensearch.core.BulkResponse
 *  org.opensearch.client.opensearch.core.DeleteByQueryResponse
 *  org.opensearch.client.opensearch.core.SearchRequest
 *  org.opensearch.client.opensearch.core.SearchResponse
 *  org.opensearch.client.opensearch.core.bulk.BulkResponseItem
 *  org.opensearch.client.opensearch.core.bulk.DeleteOperation$Builder
 *  org.opensearch.client.opensearch.core.bulk.IndexOperation$Builder
 *  org.opensearch.client.transport.OpenSearchTransport
 *  org.opensearch.client.transport.aws.AwsSdk2Transport
 *  org.opensearch.client.transport.aws.AwsSdk2TransportOptions
 *  org.opensearch.client.transport.endpoints.BooleanResponse
 *  org.opensearch.client.transport.httpclient5.ApacheHttpClient5Transport
 *  org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  software.amazon.awssdk.http.SdkHttpClient
 *  software.amazon.awssdk.http.apache.ApacheHttpClient
 *  software.amazon.awssdk.regions.Region
 */
package dev.langchain4j.store.embedding.opensearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.opensearch.Document;
import dev.langchain4j.store.embedding.opensearch.OpenSearchMetadataFilterMapper;
import dev.langchain4j.store.embedding.opensearch.OpenSearchRequestFailedException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.nio.AsyncClientConnectionManager;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.BasicHeader;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.json.JsonpMapper;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.BulkIndexByScrollFailure;
import org.opensearch.client.opensearch._types.ErrorCause;
import org.opensearch.client.opensearch._types.InlineScript;
import org.opensearch.client.opensearch._types.OpenSearchException;
import org.opensearch.client.opensearch._types.mapping.DynamicMapping;
import org.opensearch.client.opensearch._types.mapping.ObjectProperty;
import org.opensearch.client.opensearch._types.mapping.Property;
import org.opensearch.client.opensearch._types.mapping.TextProperty;
import org.opensearch.client.opensearch._types.mapping.TypeMapping;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch._types.query_dsl.ScriptScoreQuery;
import org.opensearch.client.opensearch.core.BulkRequest;
import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.core.DeleteByQueryResponse;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.bulk.BulkResponseItem;
import org.opensearch.client.opensearch.core.bulk.DeleteOperation;
import org.opensearch.client.opensearch.core.bulk.IndexOperation;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.aws.AwsSdk2Transport;
import org.opensearch.client.transport.aws.AwsSdk2TransportOptions;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5Transport;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;

public class OpenSearchEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final Logger log = LoggerFactory.getLogger(OpenSearchEmbeddingStore.class);
    private final String indexName;
    private final OpenSearchClient client;

    public OpenSearchEmbeddingStore(String serverUrl, String apiKey, String userName, String password, String indexName) {
        HttpHost openSearchHost;
        try {
            openSearchHost = HttpHost.create((String)serverUrl);
        }
        catch (URISyntaxException se) {
            throw new OpenSearchRequestFailedException("Failed to create HttpHost from server URL", se);
        }
        ApacheHttpClient5Transport transport = ApacheHttpClient5TransportBuilder.builder((HttpHost[])new HttpHost[]{openSearchHost}).setMapper((JsonpMapper)new JacksonJsonpMapper()).setHttpClientConfigCallback(httpClientBuilder -> {
            if (!Utils.isNullOrBlank((String)apiKey)) {
                httpClientBuilder.setDefaultHeaders(Collections.singletonList(new BasicHeader("Authorization", (Object)("ApiKey " + apiKey))));
            }
            if (!Utils.isNullOrBlank((String)userName) && !Utils.isNullOrBlank((String)password)) {
                BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(new AuthScope(openSearchHost), (Credentials)new UsernamePasswordCredentials(userName, password.toCharArray()));
                httpClientBuilder.setDefaultCredentialsProvider((CredentialsProvider)credentialsProvider);
            }
            httpClientBuilder.setConnectionManager((AsyncClientConnectionManager)PoolingAsyncClientConnectionManagerBuilder.create().build());
            return httpClientBuilder;
        }).build();
        this.client = new OpenSearchClient((OpenSearchTransport)transport);
        this.indexName = (String)ValidationUtils.ensureNotNull((Object)indexName, (String)"indexName");
    }

    public OpenSearchEmbeddingStore(String serverUrl, String serviceName, String region, AwsSdk2TransportOptions options, String indexName) {
        Region selectedRegion = Region.of((String)region);
        SdkHttpClient httpClient = ApacheHttpClient.builder().build();
        AwsSdk2Transport transport = new AwsSdk2Transport(httpClient, serverUrl, serviceName, selectedRegion, options);
        this.client = new OpenSearchClient((OpenSearchTransport)transport);
        this.indexName = (String)ValidationUtils.ensureNotNull((Object)indexName, (String)"indexName");
    }

    public OpenSearchEmbeddingStore(OpenSearchClient openSearchClient, String indexName) {
        this.client = (OpenSearchClient)ValidationUtils.ensureNotNull((Object)openSearchClient, (String)"openSearchClient");
        this.indexName = (String)ValidationUtils.ensureNotNull((Object)indexName, (String)"indexName");
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
        List<String> ids = embeddings.stream().map(ignored -> Utils.randomUUID()).collect(Collectors.toList());
        this.addAll(ids, embeddings, null);
        return ids;
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        List<EmbeddingMatch<TextSegment>> matches;
        try {
            ScriptScoreQuery scriptScoreQuery = this.buildDefaultScriptScoreQuery(request.queryEmbedding().vector(), (float)request.minScore(), request.filter());
            SearchResponse response = this.client.search(SearchRequest.of(s -> s.index(this.indexName, new String[0]).query(n -> n.scriptScore(scriptScoreQuery)).size(Integer.valueOf(request.maxResults()))), Document.class);
            matches = this.toEmbeddingMatch((SearchResponse<Document>)response);
        }
        catch (IOException ex) {
            throw new OpenSearchRequestFailedException("Failed to search embeddings", ex);
        }
        return new EmbeddingSearchResult(matches);
    }

    private ScriptScoreQuery buildDefaultScriptScoreQuery(float[] vector, float minScore, Filter filter) throws JsonProcessingException {
        Query query = filter == null ? Query.of(qu -> qu.matchAll(m -> m)) : OpenSearchMetadataFilterMapper.map(filter);
        return ScriptScoreQuery.of(q -> (ScriptScoreQuery.Builder)q.minScore(Float.valueOf(minScore)).query(query).script(s -> s.inline(InlineScript.of(i -> (InlineScript.Builder)((InlineScript.Builder)((InlineScript.Builder)i.source("knn_score").lang("knn").params("field", JsonData.of((Object)"vector"))).params("query_value", JsonData.of((Object)vector))).params("space_type", JsonData.of((Object)"cosinesimil"))))).boost(Float.valueOf(0.5f)));
    }

    private void addInternal(String id, Embedding embedding, TextSegment embedded) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), embedded == null ? null : Collections.singletonList(embedded));
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            log.info("[do not add empty embeddings to opensearch]");
            return;
        }
        ValidationUtils.ensureTrue((ids.size() == embeddings.size() ? 1 : 0) != 0, (String)"ids size is not equal to embeddings size");
        ValidationUtils.ensureTrue((embedded == null || embeddings.size() == embedded.size() ? 1 : 0) != 0, (String)"embeddings size is not equal to embedded size");
        try {
            this.createIndexIfNotExist(embeddings.get(0).dimension());
            this.bulk(ids, embeddings, embedded);
        }
        catch (IOException ex) {
            throw new OpenSearchRequestFailedException("Failed to add embeddings", ex);
        }
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        try {
            this.bulkRemove(ids);
        }
        catch (IOException ex) {
            throw new OpenSearchRequestFailedException("Failed to remove embeddings", ex);
        }
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        Query query = OpenSearchMetadataFilterMapper.map(filter);
        try {
            this.removeByQuery(query);
        }
        catch (IOException ex) {
            throw new OpenSearchRequestFailedException("Failed to remove embeddings by filter", ex);
        }
    }

    public void removeAll() {
        try {
            this.client.indices().delete(dir -> dir.index(this.indexName, new String[0]));
        }
        catch (OpenSearchException e) {
            if (e.status() == 404) {
                log.debug("The index [{}] does not exist.", (Object)this.indexName);
            }
            throw new OpenSearchRequestFailedException("Failed to delete index", e);
        }
        catch (IOException ex) {
            throw new OpenSearchRequestFailedException("Failed to delete index", ex);
        }
    }

    private void bulkRemove(Collection<String> ids) throws IOException {
        BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();
        for (String id : ids) {
            bulkBuilder.operations(op -> op.delete(dlt -> (DeleteOperation.Builder)((DeleteOperation.Builder)dlt.index(this.indexName)).id(id)));
        }
        BulkResponse bulkResponse = this.client.bulk(bulkBuilder.build());
        if (bulkResponse.errors()) {
            for (BulkResponseItem item : bulkResponse.items()) {
                if (item.error() == null) continue;
                ErrorCause errorCause = item.error();
                throw new OpenSearchRequestFailedException("type: " + errorCause.type() + ",reason: " + errorCause.reason());
            }
        }
    }

    private void removeByQuery(Query query) throws IOException {
        Iterator iterator;
        DeleteByQueryResponse response = this.client.deleteByQuery(delete -> delete.index(this.indexName, new String[0]).query(query));
        if (!response.failures().isEmpty() && (iterator = response.failures().iterator()).hasNext()) {
            BulkIndexByScrollFailure failure = (BulkIndexByScrollFailure)iterator.next();
            ErrorCause errorCause = failure.cause();
            throw new OpenSearchRequestFailedException("type: " + errorCause.type() + ",reason: " + errorCause.reason());
        }
    }

    private void createIndexIfNotExist(int dimension) throws IOException {
        BooleanResponse response = this.client.indices().exists(c -> c.index(this.indexName, new String[0]));
        if (!response.value()) {
            this.client.indices().create(c -> c.index(this.indexName).settings(s -> s.knn(Boolean.valueOf(true))).mappings(this.getDefaultMappings(dimension)));
        }
    }

    private TypeMapping getDefaultMappings(int dimension) {
        HashMap<String, Property> properties = new HashMap<String, Property>(4);
        properties.put("text", Property.of(p -> p.text(TextProperty.of(t -> t))));
        properties.put("vector", Property.of(p -> p.knnVector(k -> k.dimension(dimension))));
        properties.put("metadata", Property.of(p -> p.object(o -> (ObjectProperty.Builder)o.dynamic(DynamicMapping.True))));
        return TypeMapping.of(c -> c.properties(properties));
    }

    private void bulk(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) throws IOException {
        int size = ids.size();
        BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();
        for (int i = 0; i < size; ++i) {
            int finalI = i;
            Document document = Document.builder().vector(embeddings.get(i).vector()).text(embedded == null ? null : embedded.get(i).text()).metadata(embedded == null ? null : (Map)Optional.ofNullable(embedded.get(i).metadata()).map(Metadata::toMap).orElse(null)).build();
            bulkBuilder.operations(op -> op.index(idx -> ((IndexOperation.Builder)((IndexOperation.Builder)idx.index(this.indexName)).id((String)ids.get(finalI))).document((Object)document)));
        }
        BulkResponse bulkResponse = this.client.bulk(bulkBuilder.build());
        if (bulkResponse.errors()) {
            for (BulkResponseItem item : bulkResponse.items()) {
                ErrorCause errorCause;
                if (item.error() == null || (errorCause = item.error()) == null) continue;
                throw new OpenSearchRequestFailedException("type: " + errorCause.type() + ",reason: " + errorCause.reason());
            }
        }
    }

    private List<EmbeddingMatch<TextSegment>> toEmbeddingMatch(SearchResponse<Document> response) {
        return response.hits().hits().stream().map(hit -> Optional.ofNullable(hit.source()).map(document -> new EmbeddingMatch(hit.score(), hit.id(), new Embedding(document.getVector()), document.getText() == null ? null : TextSegment.from((String)document.getText(), (Metadata)new Metadata(document.getMetadata())))).orElse(null)).collect(Collectors.toList());
    }

    public static class Builder {
        private String serverUrl;
        private String apiKey;
        private String userName;
        private String password;
        private String serviceName;
        private String region;
        private AwsSdk2TransportOptions options;
        private String indexName = "default";
        private OpenSearchClient openSearchClient;

        public Builder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder options(AwsSdk2TransportOptions options) {
            this.options = options;
            return this;
        }

        public Builder indexName(String indexName) {
            this.indexName = indexName;
            return this;
        }

        public Builder openSearchClient(OpenSearchClient openSearchClient) {
            this.openSearchClient = openSearchClient;
            return this;
        }

        public OpenSearchEmbeddingStore build() {
            if (this.openSearchClient != null) {
                return new OpenSearchEmbeddingStore(this.openSearchClient, this.indexName);
            }
            if (!Utils.isNullOrBlank((String)this.serviceName) && !Utils.isNullOrBlank((String)this.region) && this.options != null) {
                return new OpenSearchEmbeddingStore(this.serverUrl, this.serviceName, this.region, this.options, this.indexName);
            }
            return new OpenSearchEmbeddingStore(this.serverUrl, this.apiKey, this.userName, this.password, this.indexName);
        }
    }
}

