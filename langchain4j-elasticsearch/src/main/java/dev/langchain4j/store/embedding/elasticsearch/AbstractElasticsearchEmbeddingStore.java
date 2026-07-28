import java.util.Arrays;

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  co.elastic.clients.elasticsearch.ElasticsearchClient
 *  co.elastic.clients.elasticsearch._types.BulkIndexByScrollFailure
 *  co.elastic.clients.elasticsearch._types.ElasticsearchException
 *  co.elastic.clients.elasticsearch._types.ErrorCause
 *  co.elastic.clients.elasticsearch._types.query_dsl.Query
 *  co.elastic.clients.elasticsearch.core.BulkRequest$Builder
 *  co.elastic.clients.elasticsearch.core.BulkResponse
 *  co.elastic.clients.elasticsearch.core.DeleteByQueryResponse
 *  co.elastic.clients.elasticsearch.core.SearchResponse
 *  co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem
 *  co.elastic.clients.elasticsearch.core.bulk.DeleteOperation$Builder
 *  co.elastic.clients.elasticsearch.core.bulk.IndexOperation$Builder
 *  co.elastic.clients.json.JsonpMapper
 *  co.elastic.clients.json.jackson.JacksonJsonpMapper
 *  co.elastic.clients.transport.ElasticsearchTransport
 *  co.elastic.clients.transport.Version
 *  co.elastic.clients.transport.rest_client.RestClientTransport
 *  co.elastic.clients.util.ObjectBuilder
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.rag.content.ContentMetadata
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.filter.Filter
 *  org.elasticsearch.client.RestClient
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.BulkIndexByScrollFailure;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.ErrorCause;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.bulk.DeleteOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.Version;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.ObjectBuilder;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.ContentMetadata;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.elasticsearch.Document;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchConfiguration;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchMetadataFilterMapper;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchRequestFailedException;
import dev.langchain4j.store.embedding.filter.Filter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractElasticsearchEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final Logger log = LoggerFactory.getLogger(AbstractElasticsearchEmbeddingStore.class);
    protected ElasticsearchConfiguration configuration;
    protected ElasticsearchClient client;
    protected String indexName;

    @Deprecated(forRemoval=true)
    protected void initialize(ElasticsearchConfiguration configuration, RestClient restClient, String indexName) {
        JacksonJsonpMapper mapper = new JacksonJsonpMapper();
        RestClientTransport transport = new RestClientTransport(restClient, (JsonpMapper)mapper);
        this.configuration = configuration;
        String version = Version.VERSION == null ? "Unknown" : Version.VERSION.toString();
        this.client = (ElasticsearchClient)new ElasticsearchClient((ElasticsearchTransport)transport).withTransportOptions(t -> t.addHeader("user-agent", "langchain4j elastic-java/" + version));
        this.indexName = (String)ValidationUtils.ensureNotNull((Object)indexName, (String)"indexName");
    }

    protected void initialize(ElasticsearchConfiguration configuration, ElasticsearchClient client, String indexName) {
        this.configuration = configuration;
        String version = Version.VERSION == null ? "Unknown" : Version.VERSION.toString();
        this.client = (ElasticsearchClient)client.withTransportOptions(t -> t.addHeader("user-agent", "langchain4j elastic-java/" + version));
        this.indexName = (String)ValidationUtils.ensureNotNull((Object)indexName, (String)"indexName");
    }

    public String add(Embedding embedding) {
        String id = Utils.randomUUID();
        this.add(id, embedding);
        return id;
    }

    public void add(String id, Embedding embedding) {
        this.addInternal(id, embedding, null);
    }

    public String add(String text) {
        String id = Utils.randomUUID();
        this.add(id, text);
        return id;
    }

    public void add(String id, String text) {
        try {
            this.bulkIndexText(Arrays.asList((Object)id), Arrays.asList((Object)TextSegment.from((String)text)));
        }
        catch (IOException e) {
            throw new ElasticsearchRequestFailedException(e);
        }
    }

    public List<String> addAllText(List<String> texts) {
        List<String> ids = texts.stream().map(ignored -> Utils.randomUUID()).collect(Collectors.toList());
        try {
            this.bulkIndexText(ids, texts.stream().map(TextSegment::from).toList());
        }
        catch (IOException e) {
            throw new ElasticsearchRequestFailedException(e);
        }
        return ids;
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

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest embeddingSearchRequest) {
        log.debug("search([...{}...], {}, {})", new Object[]{embeddingSearchRequest.queryEmbedding().vector().length, embeddingSearchRequest.maxResults(), embeddingSearchRequest.minScore()});
        try {
            SearchResponse<Document> response = this.configuration.vectorSearch(this.client, this.indexName, embeddingSearchRequest);
            log.trace("found [{}] results", response);
            List<EmbeddingMatch<TextSegment>> results = this.toMatches(response);
            results.forEach(em -> log.debug("doc [{}] scores [{}]", (Object)em.embeddingId(), (Object)em.score()));
            return new EmbeddingSearchResult(results);
        }
        catch (ElasticsearchException e) {
            if (e.getLocalizedMessage().contains("Unknown key for a VALUE_BOOLEAN in [exclude_vectors]") && this.configuration.isIncludeVectorResponse()) {
                log.warn("Property [includeVectorResponse] is not needed for elasticsearch server versions previous to 9.2, remove it to fix the exception.");
            }
            throw new ElasticsearchRequestFailedException(e);
        }
        catch (IOException e) {
            throw new ElasticsearchRequestFailedException(e);
        }
    }

    public EmbeddingSearchResult<TextSegment> hybridSearch(EmbeddingSearchRequest embeddingSearchRequest, String textQuery) {
        log.debug("hybrid search([...{}...], {}, {})", new Object[]{embeddingSearchRequest.queryEmbedding().vector().length, embeddingSearchRequest.maxResults(), embeddingSearchRequest.minScore()});
        try {
            SearchResponse<Document> response = this.configuration.hybridSearch(this.client, this.indexName, embeddingSearchRequest, textQuery);
            log.trace("found [{}] results", response);
            List<EmbeddingMatch<TextSegment>> results = this.toMatches(response);
            results.forEach(em -> log.debug("doc [{}] scores [{}]", (Object)em.embeddingId(), (Object)em.score()));
            return new EmbeddingSearchResult(results);
        }
        catch (ElasticsearchException e) {
            if (e.getLocalizedMessage().contains("Unknown key for a VALUE_BOOLEAN in [exclude_vectors]") && this.configuration.isIncludeVectorResponse()) {
                log.warn("Property [includeVectorResponse] is not needed for elasticsearch server versions previous to 9.2, remove it to fix the exception.");
            }
            throw new ElasticsearchRequestFailedException(e);
        }
        catch (IOException e) {
            throw new ElasticsearchRequestFailedException(e);
        }
    }

    public List<TextSegment> fullTextSearch(String textQuery) {
        log.debug("full text search([...{}...])", (Object)textQuery.length());
        try {
            SearchResponse<Document> response = this.configuration.fullTextSearch(this.client, this.indexName, textQuery);
            log.trace("found [{}] results", response);
            return this.toTextList(response);
        }
        catch (ElasticsearchException | IOException e) {
            throw new ElasticsearchRequestFailedException(e);
        }
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        this.removeByIds(ids);
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        Query query = ElasticsearchMetadataFilterMapper.map(filter);
        this.removeByQuery(query);
    }

    public void removeAll() {
        try {
            this.client.indices().delete(dir -> dir.index(this.indexName, new String[0]));
        }
        catch (ElasticsearchException e) {
            if (e.status() == 404) {
                log.debug("The index [{}] does not exist.", (Object)this.indexName);
            }
            throw new ElasticsearchRequestFailedException(e);
        }
        catch (IOException e) {
            throw new ElasticsearchRequestFailedException(e);
        }
    }

    private void addInternal(String id, Embedding embedding, TextSegment embedded) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), embedded == null ? null : Collections.singletonList(embedded));
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            log.info("[do not add empty embeddings to elasticsearch]");
            return;
        }
        ValidationUtils.ensureTrue((ids.size() == embeddings.size() ? 1 : 0) != 0, (String)"ids size is not equal to embeddings size");
        ValidationUtils.ensureTrue((embedded == null || embeddings.size() == embedded.size() ? 1 : 0) != 0, (String)"embeddings size is not equal to embedded size");
        try {
            this.bulkIndex(ids, embeddings, embedded);
        }
        catch (IOException e) {
            throw new ElasticsearchRequestFailedException(e);
        }
    }

    private void bulkIndex(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) throws IOException {
        int size = ids.size();
        log.debug("calling bulkIndex with [{}] elements", (Object)size);
        BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();
        for (int i = 0; i < size; ++i) {
            int finalI = i;
            Document document = Document.builder().vector(embeddings.get(i).vector()).text(embedded == null ? null : embedded.get(i).text()).metadata(embedded == null ? null : embedded.get(i).metadata().toMap()).build();
            bulkBuilder.operations(op -> op.index(idx -> ((IndexOperation.Builder)((IndexOperation.Builder)idx.index(this.indexName)).id((String)ids.get(finalI))).document((Object)document)));
        }
        BulkResponse response = this.client.bulk(bulkBuilder.build());
        this.handleBulkResponseErrors(response);
    }

    private void bulkIndexText(List<String> ids, List<TextSegment> embedded) throws IOException {
        int size = ids.size();
        log.debug("calling bulkIndex with [{}] elements", (Object)size);
        BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();
        for (int i = 0; i < size; ++i) {
            int finalI = i;
            Document document = Document.builder().text(embedded == null ? null : embedded.get(i).text()).metadata(embedded == null ? null : embedded.get(i).metadata().toMap()).build();
            bulkBuilder.operations(op -> op.index(idx -> ((IndexOperation.Builder)((IndexOperation.Builder)idx.index(this.indexName)).id((String)ids.get(finalI))).document((Object)document)));
        }
        BulkResponse response = this.client.bulk(bulkBuilder.build());
        this.handleBulkResponseErrors(response);
    }

    private void handleBulkResponseErrors(BulkResponse response) {
        if (response.errors()) {
            for (BulkResponseItem item : response.items()) {
                this.throwIfError(item.error());
            }
        }
    }

    private void throwIfError(ErrorCause errorCause) {
        if (errorCause != null) {
            throw new ElasticsearchRequestFailedException("type: " + errorCause.type() + ", reason: " + errorCause.reason());
        }
    }

    private void removeByQuery(Query query) {
        try {
            DeleteByQueryResponse response = this.client.deleteByQuery(delete -> delete.index(this.indexName, new String[0]).query(query));
            if (!response.failures().isEmpty()) {
                for (BulkIndexByScrollFailure item : response.failures()) {
                    this.throwIfError(item.cause());
                }
            }
        }
        catch (IOException e) {
            throw new ElasticsearchRequestFailedException(e);
        }
    }

    private void removeByIds(Collection<String> ids) {
        try {
            this.bulkRemove(ids);
        }
        catch (IOException e) {
            throw new ElasticsearchRequestFailedException(e);
        }
    }

    private void bulkRemove(Collection<String> ids) throws IOException {
        BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();
        for (String id : ids) {
            bulkBuilder.operations(op -> op.delete(dlt -> (ObjectBuilder)((DeleteOperation.Builder)dlt.index(this.indexName)).id(id)));
        }
        BulkResponse response = this.client.bulk(bulkBuilder.build());
        this.handleBulkResponseErrors(response);
    }

    private List<EmbeddingMatch<TextSegment>> toMatches(SearchResponse<Document> response) {
        return response.hits().hits().stream().map(hit -> Optional.ofNullable((Document)hit.source()).map(document -> new EmbeddingMatch(hit.score(), hit.id(), new Embedding(Optional.ofNullable(document.getVector()).orElse(new float[0])), document.getText() == null ? null : TextSegment.from((String)document.getText(), (Metadata)new Metadata(document.getMetadata())))).orElse(null)).collect(Collectors.toList());
    }

    private List<TextSegment> toTextList(SearchResponse<Document> response) {
        return response.hits().hits().stream().map(hit -> Optional.ofNullable((Document)hit.source()).filter(document -> document.getText() != null).map(document -> TextSegment.from((String)document.getText(), (Metadata)new Metadata(document.getMetadata()).put(ContentMetadata.SCORE.name(), hit.score().doubleValue()).put(ContentMetadata.EMBEDDING_ID.name(), hit.id()))).orElse(null)).collect(Collectors.toList());
    }
}

