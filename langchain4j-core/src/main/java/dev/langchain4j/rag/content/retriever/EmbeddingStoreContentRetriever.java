/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content.retriever;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.request.EmbeddingInputType;
import dev.langchain4j.model.embedding.request.EmbeddingRequest;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.ContentMetadata;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.spi.ServiceHelper;
import dev.langchain4j.spi.model.embedding.EmbeddingModelFactory;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmbeddingStoreContentRetriever
implements ContentRetriever {
    public static final Function<Query, Integer> DEFAULT_MAX_RESULTS = query -> 3;
    public static final Function<Query, Double> DEFAULT_MIN_SCORE = query -> 0.0;
    public static final Function<Query, Filter> DEFAULT_FILTER = query -> null;
    public static final String DEFAULT_DISPLAY_NAME = "Default";
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;
    private final Function<Query, Integer> maxResultsProvider;
    private final Function<Query, Double> minScoreProvider;
    private final Function<Query, Filter> filterProvider;
    private final EmbeddingInputType embeddingInputType;
    private final String displayName;

    public EmbeddingStoreContentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        this(DEFAULT_DISPLAY_NAME, embeddingStore, embeddingModel, DEFAULT_MAX_RESULTS, DEFAULT_MIN_SCORE, DEFAULT_FILTER, null);
    }

    public EmbeddingStoreContentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel, int maxResults) {
        this(DEFAULT_DISPLAY_NAME, embeddingStore, embeddingModel, query -> maxResults, DEFAULT_MIN_SCORE, DEFAULT_FILTER, null);
    }

    public EmbeddingStoreContentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel, Integer maxResults, Double minScore) {
        this(DEFAULT_DISPLAY_NAME, embeddingStore, embeddingModel, query -> maxResults, query -> minScore, DEFAULT_FILTER, null);
    }

    private EmbeddingStoreContentRetriever(String displayName, EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel, Function<Query, Integer> dynamicMaxResults, Function<Query, Double> dynamicMinScore, Function<Query, Filter> dynamicFilter, EmbeddingInputType embeddingInputType) {
        this.displayName = Utils.getOrDefault(displayName, DEFAULT_DISPLAY_NAME);
        this.embeddingStore = ValidationUtils.ensureNotNull(embeddingStore, "embeddingStore");
        this.embeddingModel = ValidationUtils.ensureNotNull(Utils.getOrDefault(embeddingModel, EmbeddingStoreContentRetriever::loadEmbeddingModel), "embeddingModel");
        this.maxResultsProvider = Utils.getOrDefault(dynamicMaxResults, DEFAULT_MAX_RESULTS);
        this.minScoreProvider = Utils.getOrDefault(dynamicMinScore, DEFAULT_MIN_SCORE);
        this.filterProvider = Utils.getOrDefault(dynamicFilter, DEFAULT_FILTER);
        this.embeddingInputType = embeddingInputType;
    }

    private static EmbeddingModel loadEmbeddingModel() {
        Collection<EmbeddingModelFactory> factories = ServiceHelper.loadFactories(EmbeddingModelFactory.class);
        if (factories.size() > 1) {
            throw new RuntimeException("Conflict: multiple embedding models have been found in the classpath. Please explicitly specify the one you wish to use.");
        }
        Iterator<EmbeddingModelFactory> iterator = factories.iterator();
        if (iterator.hasNext()) {
            EmbeddingModelFactory factory = iterator.next();
            return factory.create();
        }
        return null;
    }

    public static EmbeddingStoreContentRetrieverBuilder builder() {
        return new EmbeddingStoreContentRetrieverBuilder();
    }

    public static EmbeddingStoreContentRetriever from(EmbeddingStore<TextSegment> embeddingStore) {
        return EmbeddingStoreContentRetriever.builder().embeddingStore(embeddingStore).build();
    }

    @Override
    public List<Content> retrieve(Query query) {
        Embedding embeddedQuery = this.embedQuery(query.text());
        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder().query(query.text()).queryEmbedding(embeddedQuery).maxResults(this.maxResultsProvider.apply(query)).minScore(this.minScoreProvider.apply(query)).filter(this.filterProvider.apply(query)).build();
        EmbeddingSearchResult<TextSegment> searchResult = this.embeddingStore.search(searchRequest);
        return searchResult.matches().stream().map(embeddingMatch -> {
            HashMap<ContentMetadata, Object> metadata = new HashMap<ContentMetadata, Object>();
            metadata.put(ContentMetadata.SCORE, embeddingMatch.score());
            metadata.put(ContentMetadata.EMBEDDING_ID, embeddingMatch.embeddingId());
            return Content.from((TextSegment)embeddingMatch.embedded(), metadata);
        }).collect(Collectors.toList());
    }

    private Embedding embedQuery(String text) {
        if (this.embeddingInputType == null) {
            return this.embeddingModel.embed(text).content();
        }
        return this.embeddingModel.embed(EmbeddingRequest.builder().input(text).inputType(this.embeddingInputType).build()).embeddings().get(0);
    }

    public String toString() {
        return "EmbeddingStoreContentRetriever{displayName='" + this.displayName + '\'' + '}';
    }

    public static class EmbeddingStoreContentRetrieverBuilder {
        private String displayName;
        private EmbeddingStore<TextSegment> embeddingStore;
        private EmbeddingModel embeddingModel;
        private Function<Query, Integer> dynamicMaxResults;
        private Function<Query, Double> dynamicMinScore;
        private Function<Query, Filter> dynamicFilter;
        private EmbeddingInputType embeddingInputType;

        EmbeddingStoreContentRetrieverBuilder() {
        }

        public EmbeddingStoreContentRetrieverBuilder embeddingInputType(EmbeddingInputType embeddingInputType) {
            this.embeddingInputType = embeddingInputType;
            return this;
        }

        public EmbeddingStoreContentRetrieverBuilder maxResults(Integer maxResults) {
            if (maxResults != null) {
                this.dynamicMaxResults = query -> ValidationUtils.ensureGreaterThanZero(maxResults, "maxResults");
            }
            return this;
        }

        public EmbeddingStoreContentRetrieverBuilder minScore(Double minScore) {
            if (minScore != null) {
                this.dynamicMinScore = query -> ValidationUtils.ensureBetween(minScore, 0.0, 1.0, "minScore");
            }
            return this;
        }

        public EmbeddingStoreContentRetrieverBuilder filter(Filter filter) {
            if (filter != null) {
                this.dynamicFilter = query -> filter;
            }
            return this;
        }

        public EmbeddingStoreContentRetrieverBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public EmbeddingStoreContentRetrieverBuilder embeddingStore(EmbeddingStore<TextSegment> embeddingStore) {
            this.embeddingStore = embeddingStore;
            return this;
        }

        public EmbeddingStoreContentRetrieverBuilder embeddingModel(EmbeddingModel embeddingModel) {
            this.embeddingModel = embeddingModel;
            return this;
        }

        public EmbeddingStoreContentRetrieverBuilder dynamicMaxResults(Function<Query, Integer> dynamicMaxResults) {
            this.dynamicMaxResults = dynamicMaxResults;
            return this;
        }

        public EmbeddingStoreContentRetrieverBuilder dynamicMinScore(Function<Query, Double> dynamicMinScore) {
            this.dynamicMinScore = dynamicMinScore;
            return this;
        }

        public EmbeddingStoreContentRetrieverBuilder dynamicFilter(Function<Query, Filter> dynamicFilter) {
            this.dynamicFilter = dynamicFilter;
            return this;
        }

        public EmbeddingStoreContentRetriever build() {
            return new EmbeddingStoreContentRetriever(this.displayName, this.embeddingStore, this.embeddingModel, this.dynamicMaxResults, this.dynamicMinScore, this.dynamicFilter, this.embeddingInputType);
        }
    }
}

