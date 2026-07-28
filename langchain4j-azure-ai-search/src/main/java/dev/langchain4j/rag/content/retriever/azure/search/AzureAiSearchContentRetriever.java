/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.core.credential.AzureKeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.search.documents.indexes.models.SearchIndex
 *  com.azure.search.documents.models.IndexingResult
 *  com.azure.search.documents.models.QueryType
 *  com.azure.search.documents.models.SearchOptions
 *  com.azure.search.documents.models.SearchPagedIterable
 *  com.azure.search.documents.models.VectorQuery
 *  com.azure.search.documents.models.VectorizedQuery
 *  dev.langchain4j.data.document.Document
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.EmbeddingModel
 *  dev.langchain4j.rag.content.Content
 *  dev.langchain4j.rag.content.ContentMetadata
 *  dev.langchain4j.rag.content.retriever.ContentRetriever
 *  dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
 *  dev.langchain4j.rag.query.Query
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.filter.Filter
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.rag.content.retriever.azure.search;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.search.documents.indexes.models.SearchIndex;
import com.azure.search.documents.models.IndexingResult;
import com.azure.search.documents.models.QueryType;
import com.azure.search.documents.models.SearchOptions;
import com.azure.search.documents.models.SearchPagedIterable;
import com.azure.search.documents.models.VectorQuery;
import com.azure.search.documents.models.VectorizedQuery;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.ContentMetadata;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.content.retriever.azure.search.AzureAiSearchFilterMapper;
import dev.langchain4j.rag.content.retriever.azure.search.AzureAiSearchQueryType;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.azure.search.AbstractAzureAiSearchEmbeddingStore;
import dev.langchain4j.store.embedding.azure.search.AzureAiSearchRuntimeException;
import dev.langchain4j.store.embedding.azure.search.Document;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AzureAiSearchContentRetriever
extends AbstractAzureAiSearchEmbeddingStore
implements ContentRetriever {
    private static final Logger log = LoggerFactory.getLogger(AzureAiSearchContentRetriever.class);
    private final EmbeddingModel embeddingModel;
    private final AzureAiSearchQueryType azureAiSearchQueryType;
    private final int maxResults;
    private final double minScore;
    private final Filter filter;
    private final String searchFilter;

    public AzureAiSearchContentRetriever(String endpoint, AzureKeyCredential keyCredential, TokenCredential tokenCredential, boolean createOrUpdateIndex, int dimensions, SearchIndex index, String indexName, EmbeddingModel embeddingModel, int maxResults, double minScore, AzureAiSearchQueryType azureAiSearchQueryType, AzureAiSearchFilterMapper filterMapper, Filter filter) {
        ValidationUtils.ensureNotNull((Object)endpoint, (String)"endpoint");
        ValidationUtils.ensureTrue((keyCredential != null && tokenCredential == null || keyCredential == null && tokenCredential != null ? 1 : 0) != 0, (String)"either keyCredential or tokenCredential must be set");
        if (AzureAiSearchQueryType.FULL_TEXT.equals((Object)azureAiSearchQueryType)) {
            ValidationUtils.ensureTrue((dimensions == 0 ? 1 : 0) != 0, (String)"for full-text search, dimensions must be 0");
        } else {
            ValidationUtils.ensureNotNull((Object)embeddingModel, (String)"embeddingModel");
            if (index == null) {
                ValidationUtils.ensureTrue((dimensions >= 2 && dimensions <= 3072 ? 1 : 0) != 0, (String)"dimensions must be set to a positive, non-zero integer between 2 and 3072");
            } else {
                ValidationUtils.ensureTrue((dimensions == 0 ? 1 : 0) != 0, (String)"for custom index, dimensions must be 0");
            }
        }
        if (keyCredential == null) {
            if (index == null) {
                this.initialize(endpoint, null, tokenCredential, createOrUpdateIndex, dimensions, null, indexName, filterMapper);
            } else {
                this.initialize(endpoint, null, tokenCredential, createOrUpdateIndex, 0, index, indexName, filterMapper);
            }
        } else if (index == null) {
            this.initialize(endpoint, keyCredential, null, createOrUpdateIndex, dimensions, null, indexName, filterMapper);
        } else {
            this.initialize(endpoint, keyCredential, null, createOrUpdateIndex, 0, index, indexName, filterMapper);
        }
        this.embeddingModel = embeddingModel;
        this.azureAiSearchQueryType = azureAiSearchQueryType;
        this.maxResults = maxResults;
        this.minScore = minScore;
        this.filter = filter;
        this.searchFilter = this.filterMapper.map(filter);
    }

    public void add(String content) {
        this.add(Collections.singletonList(TextSegment.from((String)content)));
    }

    public void add(Document document) {
        this.add(Collections.singletonList(document.toTextSegment()));
    }

    public void add(TextSegment segment) {
        this.add(Collections.singletonList(segment));
    }

    public void add(List<TextSegment> segments) {
        if (Utils.isNullOrEmpty(segments)) {
            log.info("Empty embeddings - no ops");
            return;
        }
        ArrayList<dev.langchain4j.store.embedding.azure.search.Document> documents = new ArrayList<dev.langchain4j.store.embedding.azure.search.Document>();
        for (TextSegment segment : segments) {
            dev.langchain4j.store.embedding.azure.search.Document document = new dev.langchain4j.store.embedding.azure.search.Document();
            document.setId(Utils.randomUUID());
            document.setContent(segment.text());
            Document.Metadata metadata = new Document.Metadata();
            metadata.setAttributes(segment.metadata());
            document.setMetadata(metadata);
            documents.add(document);
        }
        List indexingResults = this.searchClient.indexDocuments(AzureAiSearchContentRetriever.toUploadBatch(documents)).getResults();
        for (IndexingResult indexingResult : indexingResults) {
            if (!indexingResult.isSucceeded()) {
                throw new AzureAiSearchRuntimeException("Failed to add content: " + indexingResult.getErrorMessage());
            }
            log.debug("Added content: {}", (Object)indexingResult.getKey());
        }
    }

    public List<Content> retrieve(Query query) {
        if (this.azureAiSearchQueryType == AzureAiSearchQueryType.VECTOR) {
            Embedding referenceEmbedding = (Embedding)this.embeddingModel.embed(query.text()).content();
            EmbeddingSearchRequest request = EmbeddingSearchRequest.builder().queryEmbedding(referenceEmbedding).maxResults(Integer.valueOf(this.maxResults)).minScore(Double.valueOf(this.minScore)).filter(this.filter).build();
            List searchResult = super.search(request).matches();
            return searchResult.stream().map(embeddingMatch -> {
                HashMap<ContentMetadata, Object> metadata = new HashMap<ContentMetadata, Object>();
                metadata.put(ContentMetadata.SCORE, embeddingMatch.score());
                metadata.put(ContentMetadata.EMBEDDING_ID, embeddingMatch.embeddingId());
                return Content.from((TextSegment)((TextSegment)embeddingMatch.embedded()), metadata);
            }).collect(Collectors.toList());
        }
        if (this.azureAiSearchQueryType == AzureAiSearchQueryType.FULL_TEXT) {
            String content = query.text();
            return this.findRelevantWithFullText(content, this.maxResults, this.minScore);
        }
        if (this.azureAiSearchQueryType == AzureAiSearchQueryType.HYBRID) {
            Embedding referenceEmbedding = (Embedding)this.embeddingModel.embed(query.text()).content();
            String content = query.text();
            return this.findRelevantWithHybrid(referenceEmbedding, content, this.maxResults, this.minScore);
        }
        if (this.azureAiSearchQueryType == AzureAiSearchQueryType.HYBRID_WITH_RERANKING) {
            Embedding referenceEmbedding = (Embedding)this.embeddingModel.embed(query.text()).content();
            String content = query.text();
            return this.findRelevantWithHybridAndReranking(referenceEmbedding, content, this.maxResults, this.minScore);
        }
        throw new AzureAiSearchRuntimeException("Unknown Azure AI Search Query Type: " + (Object)((Object)this.azureAiSearchQueryType));
    }

    private List<Content> findRelevantWithFullText(String content, int maxResults, double minScore) {
        SearchPagedIterable searchResults = this.searchClient.search(new SearchOptions().setSearchText(content).setTop(Integer.valueOf(maxResults)).setFilter(this.searchFilter));
        return this.mapResultsToContentList(searchResults, AzureAiSearchQueryType.FULL_TEXT, minScore);
    }

    private List<Content> findRelevantWithHybrid(Embedding referenceEmbedding, String content, int maxResults, double minScore) {
        List vector = referenceEmbedding.vectorAsList();
        VectorizedQuery vectorizedQuery = new VectorizedQuery(vector).setFields("content_vector").setKNearestNeighbors(Integer.valueOf(maxResults));
        SearchPagedIterable searchResults = this.searchClient.search(new SearchOptions().setSearchText(content).setVectorQueries(new VectorQuery[]{vectorizedQuery}).setTop(Integer.valueOf(maxResults)).setFilter(this.searchFilter));
        return this.mapResultsToContentList(searchResults, AzureAiSearchQueryType.HYBRID, minScore);
    }

    private List<Content> findRelevantWithHybridAndReranking(Embedding referenceEmbedding, String content, int maxResults, double minScore) {
        List vector = referenceEmbedding.vectorAsList();
        VectorizedQuery vectorizedQuery = new VectorizedQuery(vector).setFields("content_vector").setKNearestNeighbors(Integer.valueOf(maxResults));
        SearchPagedIterable searchResults = this.searchClient.search(new SearchOptions().setSearchText(content).setVectorQueries(new VectorQuery[]{vectorizedQuery}).setSemanticConfigurationName("semantic-search-config").setQueryType(QueryType.SEMANTIC).setTop(Integer.valueOf(maxResults)).setFilter(this.searchFilter));
        return this.mapResultsToContentList(searchResults, AzureAiSearchQueryType.HYBRID_WITH_RERANKING, minScore);
    }

    private List<Content> mapResultsToContentList(SearchPagedIterable searchResults, AzureAiSearchQueryType azureAiSearchQueryType, double minScore) {
        ArrayList<Content> result = new ArrayList<Content>();
        this.getEmbeddingMatches(searchResults, minScore, azureAiSearchQueryType).forEach(embeddingMatch -> {
            HashMap<ContentMetadata, Object> metadata = new HashMap<ContentMetadata, Object>();
            metadata.put(ContentMetadata.SCORE, embeddingMatch.score());
            metadata.put(ContentMetadata.EMBEDDING_ID, embeddingMatch.embeddingId());
            Content content = Content.from((TextSegment)((TextSegment)embeddingMatch.embedded()), metadata);
            result.add(content);
        });
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String endpoint;
        private AzureKeyCredential keyCredential;
        private TokenCredential tokenCredential;
        private boolean createOrUpdateIndex = true;
        private int dimensions;
        private SearchIndex index;
        private String indexName;
        private EmbeddingModel embeddingModel;
        private int maxResults = (Integer)EmbeddingStoreContentRetriever.DEFAULT_MAX_RESULTS.apply(null);
        private double minScore = (Double)EmbeddingStoreContentRetriever.DEFAULT_MIN_SCORE.apply(null);
        private AzureAiSearchQueryType azureAiSearchQueryType;
        private Filter filter;
        private AzureAiSearchFilterMapper filterMapper;

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.keyCredential = new AzureKeyCredential(apiKey);
            return this;
        }

        public Builder tokenCredential(TokenCredential tokenCredential) {
            this.tokenCredential = tokenCredential;
            return this;
        }

        public Builder createOrUpdateIndex(boolean createOrUpdateIndex) {
            this.createOrUpdateIndex = createOrUpdateIndex;
            return this;
        }

        public Builder dimensions(int dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder index(SearchIndex index) {
            this.index = index;
            return this;
        }

        public Builder indexName(String indexName) {
            this.indexName = indexName;
            return this;
        }

        public Builder embeddingModel(EmbeddingModel embeddingModel) {
            this.embeddingModel = embeddingModel;
            return this;
        }

        public Builder maxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public Builder minScore(double minScore) {
            this.minScore = minScore;
            return this;
        }

        public Builder queryType(AzureAiSearchQueryType azureAiSearchQueryType) {
            this.azureAiSearchQueryType = azureAiSearchQueryType;
            return this;
        }

        public Builder filter(Filter filter) {
            this.filter = filter;
            return this;
        }

        public Builder filterMapper(AzureAiSearchFilterMapper filterMapper) {
            this.filterMapper = filterMapper;
            return this;
        }

        public AzureAiSearchContentRetriever build() {
            return new AzureAiSearchContentRetriever(this.endpoint, this.keyCredential, this.tokenCredential, this.createOrUpdateIndex, this.dimensions, this.index, this.indexName, this.embeddingModel, this.maxResults, this.minScore, this.azureAiSearchQueryType, this.filterMapper, this.filter);
        }
    }
}

