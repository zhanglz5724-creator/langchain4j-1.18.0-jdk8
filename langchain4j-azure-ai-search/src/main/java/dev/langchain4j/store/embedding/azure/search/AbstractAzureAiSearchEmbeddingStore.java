/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.core.credential.AzureKeyCredential
 *  com.azure.core.credential.KeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.search.documents.SearchClient
 *  com.azure.search.documents.SearchClientBuilder
 *  com.azure.search.documents.indexes.SearchIndexClient
 *  com.azure.search.documents.indexes.SearchIndexClientBuilder
 *  com.azure.search.documents.indexes.models.HnswAlgorithmConfiguration
 *  com.azure.search.documents.indexes.models.HnswParameters
 *  com.azure.search.documents.indexes.models.SearchField
 *  com.azure.search.documents.indexes.models.SearchFieldDataType
 *  com.azure.search.documents.indexes.models.SearchIndex
 *  com.azure.search.documents.indexes.models.SemanticConfiguration
 *  com.azure.search.documents.indexes.models.SemanticField
 *  com.azure.search.documents.indexes.models.SemanticPrioritizedFields
 *  com.azure.search.documents.indexes.models.SemanticSearch
 *  com.azure.search.documents.indexes.models.VectorSearch
 *  com.azure.search.documents.indexes.models.VectorSearchAlgorithmMetric
 *  com.azure.search.documents.indexes.models.VectorSearchProfile
 *  com.azure.search.documents.models.IndexAction
 *  com.azure.search.documents.models.IndexActionType
 *  com.azure.search.documents.models.IndexDocumentsBatch
 *  com.azure.search.documents.models.IndexingResult
 *  com.azure.search.documents.models.SearchOptions
 *  com.azure.search.documents.models.SearchPagedIterable
 *  com.azure.search.documents.models.SearchResult
 *  com.azure.search.documents.models.VectorQuery
 *  com.azure.search.documents.models.VectorizedQuery
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.RelevanceScore
 *  dev.langchain4j.store.embedding.filter.Filter
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.azure.search;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.KeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.search.documents.SearchClient;
import com.azure.search.documents.SearchClientBuilder;
import com.azure.search.documents.indexes.SearchIndexClient;
import com.azure.search.documents.indexes.SearchIndexClientBuilder;
import com.azure.search.documents.indexes.models.HnswAlgorithmConfiguration;
import com.azure.search.documents.indexes.models.HnswParameters;
import com.azure.search.documents.indexes.models.SearchField;
import com.azure.search.documents.indexes.models.SearchFieldDataType;
import com.azure.search.documents.indexes.models.SearchIndex;
import com.azure.search.documents.indexes.models.SemanticConfiguration;
import com.azure.search.documents.indexes.models.SemanticField;
import com.azure.search.documents.indexes.models.SemanticPrioritizedFields;
import com.azure.search.documents.indexes.models.SemanticSearch;
import com.azure.search.documents.indexes.models.VectorSearch;
import com.azure.search.documents.indexes.models.VectorSearchAlgorithmMetric;
import com.azure.search.documents.indexes.models.VectorSearchProfile;
import com.azure.search.documents.models.IndexAction;
import com.azure.search.documents.models.IndexActionType;
import com.azure.search.documents.models.IndexDocumentsBatch;
import com.azure.search.documents.models.IndexingResult;
import com.azure.search.documents.models.SearchOptions;
import com.azure.search.documents.models.SearchPagedIterable;
import com.azure.search.documents.models.SearchResult;
import com.azure.search.documents.models.VectorQuery;
import com.azure.search.documents.models.VectorizedQuery;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.retriever.azure.search.AzureAiSearchFilterMapper;
import dev.langchain4j.rag.content.retriever.azure.search.AzureAiSearchQueryType;
import dev.langchain4j.rag.content.retriever.azure.search.DefaultAzureAiSearchFilterMapper;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.RelevanceScore;
import dev.langchain4j.store.embedding.azure.search.AzureAiSearchRuntimeException;
import dev.langchain4j.store.embedding.azure.search.Document;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAzureAiSearchEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final Logger log = LoggerFactory.getLogger(AbstractAzureAiSearchEmbeddingStore.class);
    public static final String DEFAULT_INDEX_NAME = "vectorsearch";
    static final String DEFAULT_FIELD_ID = "id";
    protected static final String DEFAULT_FIELD_CONTENT = "content";
    protected final String DEFAULT_FIELD_CONTENT_VECTOR = "content_vector";
    protected static final String DEFAULT_FIELD_METADATA = "metadata";
    protected static final String DEFAULT_FIELD_METADATA_SOURCE = "source";
    protected static final String DEFAULT_FIELD_METADATA_ATTRS = "attributes";
    protected static final String SEMANTIC_SEARCH_CONFIG_NAME = "semantic-search-config";
    protected static final String VECTOR_ALGORITHM_NAME = "vector-search-algorithm";
    protected static final String VECTOR_SEARCH_PROFILE_NAME = "vector-search-profile";
    private boolean createOrUpdateIndex;
    private SearchIndexClient searchIndexClient;
    protected SearchClient searchClient;
    private String indexName;
    protected AzureAiSearchFilterMapper filterMapper;

    protected void initialize(String endpoint, AzureKeyCredential keyCredential, TokenCredential tokenCredential, boolean createOrUpdateIndex, int dimensions, SearchIndex index, String indexName, AzureAiSearchFilterMapper filterMapper) {
        ValidationUtils.ensureNotNull((Object)endpoint, (String)"endpoint");
        this.filterMapper = filterMapper == null ? new DefaultAzureAiSearchFilterMapper() : filterMapper;
        if (index != null && Utils.isNotNullOrBlank((String)indexName)) {
            throw new IllegalArgumentException("index and indexName cannot be both defined");
        }
        this.indexName = createOrUpdateIndex && index != null ? index.getName() : (String)Utils.getOrDefault((Object)indexName, (Object)DEFAULT_INDEX_NAME);
        this.createOrUpdateIndex = createOrUpdateIndex;
        if (keyCredential != null) {
            if (createOrUpdateIndex) {
                this.searchIndexClient = new SearchIndexClientBuilder().endpoint(endpoint).credential((KeyCredential)keyCredential).buildClient();
            }
            this.searchClient = new SearchClientBuilder().endpoint(endpoint).credential((KeyCredential)keyCredential).indexName(this.indexName).buildClient();
        } else {
            if (createOrUpdateIndex) {
                this.searchIndexClient = new SearchIndexClientBuilder().endpoint(endpoint).credential(tokenCredential).buildClient();
            }
            this.searchClient = new SearchClientBuilder().endpoint(endpoint).credential(tokenCredential).indexName(this.indexName).buildClient();
        }
        if (createOrUpdateIndex) {
            if (index == null) {
                this.createOrUpdateIndex(dimensions);
            } else {
                this.createOrUpdateIndex(index);
            }
        }
    }

    public void createOrUpdateIndex(int dimensions) {
        if (!this.createOrUpdateIndex) {
            throw new IllegalArgumentException("createOrUpdateIndex is false, so the index cannot be created or updated");
        }
        if (dimensions == 0) {
            log.info("Dimensions is 0, so the index will only be created for full text search");
        }
        ArrayList<SearchField> fields = new ArrayList<SearchField>();
        fields.add(new SearchField(DEFAULT_FIELD_ID, SearchFieldDataType.STRING).setKey(Boolean.valueOf(true)).setFilterable(Boolean.valueOf(true)));
        fields.add(new SearchField(DEFAULT_FIELD_CONTENT, SearchFieldDataType.STRING).setSearchable(Boolean.valueOf(true)).setFilterable(Boolean.valueOf(true)));
        if (dimensions > 0) {
            fields.add(new SearchField("content_vector", SearchFieldDataType.collection((SearchFieldDataType)SearchFieldDataType.SINGLE)).setSearchable(Boolean.valueOf(true)).setVectorSearchDimensions(Integer.valueOf(dimensions)).setVectorSearchProfileName(VECTOR_SEARCH_PROFILE_NAME));
        }
        fields.add(new SearchField(DEFAULT_FIELD_METADATA, SearchFieldDataType.COMPLEX).setFields(Arrays.asList(new SearchField(DEFAULT_FIELD_METADATA_SOURCE, SearchFieldDataType.STRING).setFilterable(Boolean.valueOf(true)), new SearchField(DEFAULT_FIELD_METADATA_ATTRS, SearchFieldDataType.collection((SearchFieldDataType)SearchFieldDataType.COMPLEX)).setFields(Arrays.asList(new SearchField("key", SearchFieldDataType.STRING).setFilterable(Boolean.valueOf(true)), new SearchField("value", SearchFieldDataType.STRING).setFilterable(Boolean.valueOf(true)))))));
        SearchIndex index = null;
        if (dimensions > 0) {
            VectorSearch vectorSearch = new VectorSearch().setAlgorithms(Collections.singletonList(new HnswAlgorithmConfiguration(VECTOR_ALGORITHM_NAME).setParameters(new HnswParameters().setMetric(VectorSearchAlgorithmMetric.COSINE).setM(Integer.valueOf(4)).setEfSearch(Integer.valueOf(500)).setEfConstruction(Integer.valueOf(400))))).setProfiles(Collections.singletonList(new VectorSearchProfile(VECTOR_SEARCH_PROFILE_NAME, VECTOR_ALGORITHM_NAME)));
            SemanticSearch semanticSearch = new SemanticSearch().setDefaultConfigurationName(SEMANTIC_SEARCH_CONFIG_NAME).setConfigurations(Collections.singletonList(new SemanticConfiguration(SEMANTIC_SEARCH_CONFIG_NAME, new SemanticPrioritizedFields().setContentFields(new SemanticField[]{new SemanticField(DEFAULT_FIELD_CONTENT)}).setKeywordsFields(new SemanticField[]{new SemanticField(DEFAULT_FIELD_CONTENT)}))));
            index = new SearchIndex(this.indexName, fields).setVectorSearch(vectorSearch).setSemanticSearch(semanticSearch);
        } else {
            index = new SearchIndex(this.indexName, fields);
        }
        this.searchIndexClient.createOrUpdateIndex(index);
    }

    void createOrUpdateIndex(SearchIndex index) {
        if (!this.createOrUpdateIndex) {
            throw new IllegalArgumentException("createOrUpdateIndex is false, so the index cannot be created or updated");
        }
        this.searchIndexClient.createOrUpdateIndex(index);
    }

    public void deleteIndex() {
        if (!this.createOrUpdateIndex) {
            throw new IllegalArgumentException("createOrUpdateIndex is false, so the index cannot be deleted");
        }
        this.searchIndexClient.deleteIndex(this.indexName);
    }

    public String add(Embedding embedding) {
        String id = Utils.randomUUID();
        this.addInternal(id, embedding, null);
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

    public void remove(String id) {
        ValidationUtils.ensureNotBlank((String)id, (String)DEFAULT_FIELD_ID);
        this.removeAll(Collections.singletonList(id));
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        ArrayList<IndexAction> actions = new ArrayList<IndexAction>();
        for (String id : ids) {
            ValidationUtils.ensureNotBlank((String)id, (String)DEFAULT_FIELD_ID);
            HashMap<String, String> document = new HashMap<String, String>();
            document.put(DEFAULT_FIELD_ID, id);
            actions.add(new IndexAction().setActionType(IndexActionType.DELETE).setAdditionalProperties(document));
        }
        this.searchClient.indexDocuments(new IndexDocumentsBatch(actions));
    }

    public void removeAll() {
        SearchOptions searchOptions = new SearchOptions().setSearchText("*").setSelect(new String[]{DEFAULT_FIELD_ID});
        SearchPagedIterable searchResults = this.searchClient.search(searchOptions);
        List<String> ids = searchResults.stream().map(SearchResult::getAdditionalProperties).map(doc -> (String)doc.get(DEFAULT_FIELD_ID)).collect(Collectors.toList());
        this.removeAll(ids);
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        SearchOptions searchOptions = new SearchOptions().setSearchText("*").setSelect(new String[]{DEFAULT_FIELD_ID}).setFilter(this.filterMapper.map(filter));
        SearchPagedIterable searchResults = this.searchClient.search(searchOptions);
        List<String> ids = searchResults.stream().map(SearchResult::getAdditionalProperties).map(doc -> (String)doc.get(DEFAULT_FIELD_ID)).collect(Collectors.toList());
        this.removeAll(ids);
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        List vector = request.queryEmbedding().vectorAsList();
        VectorizedQuery vectorizedQuery = new VectorizedQuery(vector).setFields("content_vector").setKNearestNeighbors(Integer.valueOf(request.maxResults()));
        SearchPagedIterable searchResults = this.searchClient.search(new SearchOptions().setFilter(this.filterMapper.map(request.filter())).setVectorQueries(new VectorQuery[]{vectorizedQuery}));
        return new EmbeddingSearchResult(this.getEmbeddingMatches(searchResults, request.minScore(), AzureAiSearchQueryType.VECTOR));
    }

    protected List<EmbeddingMatch<TextSegment>> getEmbeddingMatches(SearchPagedIterable searchResults, Double minScore, AzureAiSearchQueryType azureAiSearchQueryType) {
        ArrayList<EmbeddingMatch<TextSegment>> result = new ArrayList<EmbeddingMatch<TextSegment>>();
        for (SearchResult searchResult : searchResults) {
            EmbeddingMatch embeddingMatch;
            String embeddedContent;
            Double score = AbstractAzureAiSearchEmbeddingStore.fromAzureScoreToRelevanceScore(searchResult, azureAiSearchQueryType);
            if (score < minScore) continue;
            Map searchDocument = searchResult.getAdditionalProperties();
            String embeddingId = (String)searchDocument.get(DEFAULT_FIELD_ID);
            List embeddingList = (List)searchDocument.get("content_vector");
            Embedding embedding = null;
            if (embeddingList != null) {
                float[] embeddingArray = this.doublesListToFloatArray(embeddingList);
                embedding = Embedding.from((float[])embeddingArray);
            }
            if (Utils.isNotNullOrBlank((String)(embeddedContent = (String)searchDocument.get(DEFAULT_FIELD_CONTENT)))) {
                Metadata langChainMetadata;
                LinkedHashMap metadata = (LinkedHashMap)searchDocument.get(DEFAULT_FIELD_METADATA);
                if (metadata == null) {
                    langChainMetadata = Metadata.from(Collections.emptyMap());
                } else {
                    List attributes = (List)metadata.get(DEFAULT_FIELD_METADATA_ATTRS);
                    HashMap<String, String> attributesMap = new HashMap<String, String>();
                    for (Object attribute : attributes) {
                        LinkedHashMap innerAttribute = (LinkedHashMap)attribute;
                        String key = (String)innerAttribute.get("key");
                        String value = (String)innerAttribute.get("value");
                        attributesMap.put(key, value);
                    }
                    langChainMetadata = Metadata.from(attributesMap);
                }
                TextSegment embedded = TextSegment.textSegment((String)embeddedContent, (Metadata)langChainMetadata);
                embeddingMatch = new EmbeddingMatch(score, embeddingId, embedding, (Object)embedded);
            } else {
                embeddingMatch = new EmbeddingMatch(score, embeddingId, embedding, null);
            }
            result.add((EmbeddingMatch<TextSegment>)embeddingMatch);
        }
        return result;
    }

    private void addInternal(String id, Embedding embedding, TextSegment embedded) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), embedded == null ? null : Collections.singletonList(embedded));
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            log.info("Empty embeddings - no ops");
            return;
        }
        ValidationUtils.ensureTrue((ids.size() == embeddings.size() ? 1 : 0) != 0, (String)"ids size is not equal to embeddings size");
        ValidationUtils.ensureTrue((embedded == null || embeddings.size() == embedded.size() ? 1 : 0) != 0, (String)"embeddings size is not equal to embedded size");
        ArrayList<Document> documents = new ArrayList<Document>();
        for (int i = 0; i < ids.size(); ++i) {
            Document document = new Document();
            document.setId(ids.get(i));
            document.setContentVector(embeddings.get(i).vectorAsList());
            if (embedded != null) {
                document.setContent(embedded.get(i).text());
                Document.Metadata metadata = new Document.Metadata();
                ArrayList<Document.Metadata.Attribute> attributes = new ArrayList<Document.Metadata.Attribute>();
                for (Map.Entry entry : embedded.get(i).metadata().toMap().entrySet()) {
                    Document.Metadata.Attribute attribute = new Document.Metadata.Attribute();
                    attribute.setKey((String)entry.getKey());
                    attribute.setValue(String.valueOf(entry.getValue()));
                    attributes.add(attribute);
                }
                metadata.setAttributes(attributes);
                document.setMetadata(metadata);
            }
            documents.add(document);
        }
        List indexingResults = this.searchClient.indexDocuments(AbstractAzureAiSearchEmbeddingStore.toUploadBatch(documents)).getResults();
        for (IndexingResult indexingResult : indexingResults) {
            if (!indexingResult.isSucceeded()) {
                throw new AzureAiSearchRuntimeException("Failed to add embedding: " + indexingResult.getErrorMessage());
            }
            log.debug("Added embedding: {}", (Object)indexingResult.getKey());
        }
    }

    protected static IndexDocumentsBatch toUploadBatch(List<Document> documents) {
        ArrayList<IndexAction> actions = new ArrayList<IndexAction>();
        for (Document document : documents) {
            actions.add(new IndexAction().setActionType(IndexActionType.UPLOAD).setAdditionalProperties(AbstractAzureAiSearchEmbeddingStore.toSearchDocument(document)));
        }
        return new IndexDocumentsBatch(actions);
    }

    private static Map<String, Object> toSearchDocument(Document document) {
        HashMap<String, Object> searchDocument = new HashMap<String, Object>();
        searchDocument.put(DEFAULT_FIELD_ID, document.getId());
        if (document.getContent() != null) {
            searchDocument.put(DEFAULT_FIELD_CONTENT, document.getContent());
        }
        if (document.getContentVector() != null) {
            searchDocument.put("content_vector", document.getContentVector());
        }
        if (document.getMetadata() != null) {
            HashMap<String, Object> metadata = new HashMap<String, Object>();
            if (document.getMetadata().getSource() != null) {
                metadata.put(DEFAULT_FIELD_METADATA_SOURCE, document.getMetadata().getSource());
            }
            if (document.getMetadata().getAttributes() != null) {
                ArrayList attributes = new ArrayList();
                for (Document.Metadata.Attribute attribute : document.getMetadata().getAttributes()) {
                    HashMap<String, String> attributeMap = new HashMap<String, String>();
                    attributeMap.put("key", attribute.getKey());
                    attributeMap.put("value", attribute.getValue());
                    attributes.add(attributeMap);
                }
                metadata.put(DEFAULT_FIELD_METADATA_ATTRS, attributes);
            }
            searchDocument.put(DEFAULT_FIELD_METADATA, metadata);
        }
        return searchDocument;
    }

    float[] doublesListToFloatArray(List<Double> doubles) {
        float[] array = new float[doubles.size()];
        for (int i = 0; i < doubles.size(); ++i) {
            array[i] = doubles.get(i).floatValue();
        }
        return array;
    }

    protected static double fromAzureScoreToRelevanceScore(double score) {
        double cosineDistance = (1.0 - score) / score;
        double cosineSimilarity = -cosineDistance + 1.0;
        return RelevanceScore.fromCosineSimilarity((double)cosineSimilarity);
    }

    public static double fromAzureScoreToRelevanceScore(SearchResult searchResult, AzureAiSearchQueryType azureAiSearchQueryType) {
        if (azureAiSearchQueryType == AzureAiSearchQueryType.VECTOR) {
            double score = searchResult.getScore();
            return AbstractAzureAiSearchEmbeddingStore.fromAzureScoreToRelevanceScore(score);
        }
        if (azureAiSearchQueryType == AzureAiSearchQueryType.FULL_TEXT) {
            return searchResult.getScore();
        }
        if (azureAiSearchQueryType == AzureAiSearchQueryType.HYBRID) {
            return searchResult.getScore();
        }
        if (azureAiSearchQueryType == AzureAiSearchQueryType.HYBRID_WITH_RERANKING) {
            return searchResult.getRerankerScore() / 4.0;
        }
        throw new AzureAiSearchRuntimeException("Unknown Azure AI Search Query Type: " + (Object)((Object)azureAiSearchQueryType));
    }
}

