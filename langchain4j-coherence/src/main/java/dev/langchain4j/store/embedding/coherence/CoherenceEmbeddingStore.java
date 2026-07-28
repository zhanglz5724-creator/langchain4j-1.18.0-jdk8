/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.oracle.coherence.ai.DocumentChunk
 *  com.oracle.coherence.ai.DocumentChunk$Id
 *  com.oracle.coherence.ai.Float32Vector
 *  com.oracle.coherence.ai.QueryResult
 *  com.oracle.coherence.ai.Vector
 *  com.oracle.coherence.ai.VectorIndexExtractor
 *  com.oracle.coherence.ai.search.SimilaritySearch
 *  com.oracle.coherence.common.base.Logger
 *  com.tangosol.internal.util.processor.CacheProcessors
 *  com.tangosol.net.Coherence
 *  com.tangosol.net.NamedMap
 *  com.tangosol.net.NamedMap$Option
 *  com.tangosol.net.Session
 *  com.tangosol.util.Filter
 *  com.tangosol.util.InvocableMap$EntryAggregator
 *  com.tangosol.util.UUID
 *  com.tangosol.util.ValueExtractor
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
 */
package dev.langchain4j.store.embedding.coherence;

import com.oracle.coherence.ai.DocumentChunk;
import com.oracle.coherence.ai.Float32Vector;
import com.oracle.coherence.ai.QueryResult;
import com.oracle.coherence.ai.Vector;
import com.oracle.coherence.ai.VectorIndexExtractor;
import com.oracle.coherence.ai.search.SimilaritySearch;
import com.oracle.coherence.common.base.Logger;
import com.tangosol.internal.util.processor.CacheProcessors;
import com.tangosol.net.Coherence;
import com.tangosol.net.NamedMap;
import com.tangosol.net.Session;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.UUID;
import com.tangosol.util.ValueExtractor;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.RelevanceScore;
import dev.langchain4j.store.embedding.coherence.CoherenceMetadataFilterMapper;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CoherenceEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final ValueExtractor<DocumentChunk, Vector<float[]>> EXTRACTOR = ValueExtractor.of(DocumentChunk::vector);
    public static final String DEFAULT_MAP_NAME = "documentChunks";
    protected final NamedMap<DocumentChunk.Id, DocumentChunk> documentChunks;
    protected final boolean normalizeEmbeddings;

    protected CoherenceEmbeddingStore(NamedMap<DocumentChunk.Id, DocumentChunk> namedMap, boolean normalizeEmbeddings) {
        this.documentChunks = namedMap;
        this.normalizeEmbeddings = normalizeEmbeddings;
    }

    public String add(Embedding embedding) {
        DocumentChunk.Id id = DocumentChunk.id((String)new UUID().toString(), (int)0);
        this.addInternal(id, embedding, null);
        return id.toString();
    }

    public void add(String id, Embedding embedding) {
        this.addInternal(DocumentChunk.Id.parse((String)id), embedding, null);
    }

    public String add(Embedding embedding, TextSegment segment) {
        DocumentChunk.Id id = DocumentChunk.id((String)new UUID().toString(), (int)0);
        this.addInternal(id, embedding, segment);
        return id.toString();
    }

    public List<String> addAll(List<Embedding> embeddings) {
        return this.addAll(embeddings, null);
    }

    public void remove(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
        this.documentChunks.remove((Object)DocumentChunk.Id.parse((String)id));
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        Set chunkIds = ids.stream().map(DocumentChunk.Id::parse).collect(Collectors.toSet());
        this.documentChunks.keySet().removeAll(chunkIds);
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        com.tangosol.util.Filter<DocumentChunk> chunkFilter = CoherenceMetadataFilterMapper.map(filter);
        this.documentChunks.invokeAll(chunkFilter, CacheProcessors.removeBlind());
    }

    public void removeAll() {
        this.documentChunks.truncate();
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        double minScore;
        Embedding queryEmbedding = request.queryEmbedding();
        if (this.normalizeEmbeddings) {
            queryEmbedding.normalize();
        }
        boolean checkMin = (minScore = request.minScore()) != 0.0;
        com.tangosol.util.Filter<DocumentChunk> filter = CoherenceMetadataFilterMapper.map(request.filter());
        Float32Vector vector = new Float32Vector(queryEmbedding.vector());
        SimilaritySearch aggregator = new SimilaritySearch(EXTRACTOR, (Vector)vector, request.maxResults());
        if (filter != null) {
            aggregator = aggregator.filter(filter);
        }
        List results = (List)this.documentChunks.aggregate((InvocableMap.EntryAggregator)aggregator);
        ArrayList<EmbeddingMatch<TextSegment>> matches = new ArrayList<EmbeddingMatch<TextSegment>>();
        for (QueryResult result : results) {
            double score = RelevanceScore.fromCosineSimilarity((double)(1.0 - result.getDistance()));
            if (checkMin && score < minScore) continue;
            matches.add(this.embeddingMatch(score, (DocumentChunk.Id)result.getKey(), (DocumentChunk)result.getValue()));
        }
        return new EmbeddingSearchResult(matches);
    }

    private void addInternal(DocumentChunk.Id id, Embedding embedding, TextSegment segment) {
        this.documentChunks.put((Object)id, (Object)this.createChunk(embedding, segment));
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> segments) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            Logger.info((String)"Skipped adding empty embeddings");
            return;
        }
        boolean hasEmbedded = segments != null && !segments.isEmpty();
        ValidationUtils.ensureTrue((ids.size() == embeddings.size() ? 1 : 0) != 0, (String)"ids size is not equal to embeddings size");
        if (hasEmbedded) {
            ValidationUtils.ensureTrue((embeddings.size() == segments.size() ? 1 : 0) != 0, (String)"embeddings size is not equal to embedded size");
        }
        HashMap<DocumentChunk.Id, DocumentChunk> map = new HashMap<DocumentChunk.Id, DocumentChunk>();
        for (int i = 0; i < embeddings.size(); ++i) {
            Embedding embedding = embeddings.get(i);
            TextSegment segment = hasEmbedded ? segments.get(i) : null;
            String id = ids.get(i);
            map.put(new DocumentChunk.Id(id, 0), this.createChunk(embedding, segment));
        }
        this.documentChunks.putAll(map);
    }

    private EmbeddingMatch<TextSegment> embeddingMatch(double score, DocumentChunk.Id id, DocumentChunk chunk) {
        String key = id.toString();
        String text = chunk.text();
        TextSegment segment = text == null ? null : new TextSegment(chunk.text(), CoherenceEmbeddingStore.mapToMetadata(chunk.metadata()));
        Vector vector = chunk.vector();
        Embedding embedding = vector == null ? null : new Embedding((float[])vector.get());
        return new EmbeddingMatch(Double.valueOf(score), key, embedding, (Object)segment);
    }

    private static Metadata mapToMetadata(Map<String, Object> mapMetadata) {
        mapMetadata.entrySet().removeIf(entry -> entry.getValue() == null);
        return Metadata.from(mapMetadata);
    }

    private DocumentChunk createChunk(Embedding embedding, TextSegment segment) {
        String text = segment == null ? null : segment.text();
        Map metadata = segment == null ? Collections.emptyMap() : segment.metadata().toMap();
        DocumentChunk chunk = new DocumentChunk(text, metadata);
        if (this.normalizeEmbeddings) {
            embedding.normalize();
        }
        Float32Vector vector = new Float32Vector(embedding.vector());
        chunk.setVector((Vector)vector);
        return chunk;
    }

    public static CoherenceEmbeddingStore create() {
        return CoherenceEmbeddingStore.builder().build();
    }

    public static CoherenceEmbeddingStore create(String name) {
        return CoherenceEmbeddingStore.builder().name(name).build();
    }

    public static CoherenceEmbeddingStore create(NamedMap<DocumentChunk.Id, DocumentChunk> map) {
        return new CoherenceEmbeddingStore(map, false);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name = "documentChunks";
        private String sessionName;
        private Session session;
        private VectorIndexExtractor<DocumentChunk, float[]> extractor;
        private boolean normalizeEmbeddings = false;

        protected Builder() {
        }

        public Builder name(String name) {
            this.name = Utils.isNullOrEmpty((String)name) ? CoherenceEmbeddingStore.DEFAULT_MAP_NAME : name;
            return this;
        }

        public Builder session(String sessionName) {
            this.sessionName = sessionName;
            this.session = null;
            return this;
        }

        public Builder session(Session session) {
            this.session = session;
            this.sessionName = null;
            return this;
        }

        public Builder index(VectorIndexExtractor<DocumentChunk, float[]> extractor) {
            this.extractor = extractor;
            return this;
        }

        public Builder normalizeEmbeddings(boolean f) {
            this.normalizeEmbeddings = f;
            return this;
        }

        public CoherenceEmbeddingStore build() {
            Session session = this.session;
            if (session == null) {
                session = this.sessionName != null ? Coherence.getInstance().getSession(this.sessionName) : Coherence.getInstance().getSession();
            }
            NamedMap map = session.getMap(this.name, new NamedMap.Option[0]);
            if (this.extractor != null) {
                map.addIndex(this.extractor);
            }
            return new CoherenceEmbeddingStore((NamedMap<DocumentChunk.Id, DocumentChunk>)map, this.normalizeEmbeddings);
        }
    }
}

