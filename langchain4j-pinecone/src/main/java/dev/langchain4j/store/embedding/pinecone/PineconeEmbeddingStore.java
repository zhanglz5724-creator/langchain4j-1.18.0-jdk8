/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.protobuf.Struct
 *  com.google.protobuf.Value
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
 *  io.pinecone.clients.Index
 *  io.pinecone.clients.Pinecone
 *  io.pinecone.clients.Pinecone$Builder
 *  io.pinecone.commons.IndexInterface
 *  io.pinecone.unsigned_indices_model.QueryResponseWithUnsignedIndices
 *  io.pinecone.unsigned_indices_model.ScoredVectorWithUnsignedIndices
 *  io.pinecone.unsigned_indices_model.VectorWithUnsignedIndices
 *  org.openapitools.db_control.client.model.IndexList
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.pinecone;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
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
import dev.langchain4j.store.embedding.pinecone.PineconeHelper;
import dev.langchain4j.store.embedding.pinecone.PineconeIndexConfig;
import dev.langchain4j.store.embedding.pinecone.PineconeMetadataFilterMapper;
import io.pinecone.clients.Index;
import io.pinecone.clients.Pinecone;
import io.pinecone.commons.IndexInterface;
import io.pinecone.unsigned_indices_model.QueryResponseWithUnsignedIndices;
import io.pinecone.unsigned_indices_model.ScoredVectorWithUnsignedIndices;
import io.pinecone.unsigned_indices_model.VectorWithUnsignedIndices;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.openapitools.db_control.client.model.IndexList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PineconeEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final Logger log = LoggerFactory.getLogger(PineconeEmbeddingStore.class);
    private static final String DEFAULT_NAMESPACE = "default";
    private static final String DEFAULT_METADATA_TEXT_KEY = "text_segment";
    private final Index index;
    private final String nameSpace;
    private final String metadataTextKey;

    public PineconeEmbeddingStore(String apiKey, String index, String nameSpace, String metadataTextKey, PineconeIndexConfig createIndex, String environment, String projectId) {
        Pinecone client = new Pinecone.Builder(apiKey).build();
        this.nameSpace = nameSpace == null ? DEFAULT_NAMESPACE : nameSpace;
        String string = this.metadataTextKey = metadataTextKey == null ? DEFAULT_METADATA_TEXT_KEY : metadataTextKey;
        if (createIndex != null && !this.isIndexExist(client, index)) {
            createIndex.createIndex(client, index);
        }
        this.index = client.getIndexConnection(index);
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

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        this.index.deleteByIds(new ArrayList<String>(ids), this.nameSpace);
    }

    public void removeAll() {
        this.index.deleteAll(this.nameSpace);
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        QueryResponseWithUnsignedIndices response;
        Embedding embedding = request.queryEmbedding();
        if (Objects.isNull(request.filter())) {
            response = this.index.queryByVector(request.maxResults(), embedding.vectorAsList(), this.nameSpace, true, true);
        } else {
            Struct metadataFilter = PineconeMetadataFilterMapper.map(request.filter());
            response = this.index.queryByVector(request.maxResults(), embedding.vectorAsList(), this.nameSpace, metadataFilter, true, true);
        }
        List matchesList = response.getMatchesList();
        List matches = matchesList.stream().map(indices -> this.toEmbeddingMatch((ScoredVectorWithUnsignedIndices)indices, embedding)).filter(match -> match.score() >= request.minScore()).sorted(Comparator.comparingDouble(EmbeddingMatch::score)).collect(Collectors.toList());
        Collections.reverse(matches);
        return new EmbeddingSearchResult(matches);
    }

    private void addInternal(String id, Embedding embedding, TextSegment textSegment) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), textSegment == null ? null : Collections.singletonList(textSegment));
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> textSegments) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            log.info("Empty embeddings - no ops");
            return;
        }
        ValidationUtils.ensureTrue((ids.size() == embeddings.size() ? 1 : 0) != 0, (String)"ids size is not equal to embeddings size");
        ValidationUtils.ensureTrue((textSegments == null || embeddings.size() == textSegments.size() ? 1 : 0) != 0, (String)"embeddings size is not equal to textSegments size");
        ArrayList<VectorWithUnsignedIndices> vectors = new ArrayList<VectorWithUnsignedIndices>(embeddings.size());
        for (int i = 0; i < embeddings.size(); ++i) {
            String id = ids.get(i);
            Embedding embedding = embeddings.get(i);
            Struct struct = null;
            if (textSegments != null) {
                TextSegment textSegment = textSegments.get(i);
                struct = PineconeHelper.metadataToStruct(textSegment, this.metadataTextKey);
            }
            vectors.add(IndexInterface.buildUpsertVectorWithUnsignedIndices((String)id, (List)embedding.vectorAsList(), null, null, struct));
        }
        this.index.upsert(vectors, this.nameSpace);
    }

    private boolean isIndexExist(Pinecone client, String index) {
        IndexList indexList = client.listIndexes();
        List indexModels = indexList.getIndexes();
        return !Utils.isNullOrEmpty((Collection)indexModels) && indexModels.stream().anyMatch(indexModel -> indexModel.getName().equals(index));
    }

    private EmbeddingMatch<TextSegment> toEmbeddingMatch(ScoredVectorWithUnsignedIndices indices, Embedding referenceEmbedding) {
        Map filedsMap = indices.getMetadata().getFieldsMap();
        Value textSegmentValue = (Value)filedsMap.get(this.metadataTextKey);
        Metadata metadata = Metadata.from(PineconeHelper.structToMetadata(filedsMap, this.metadataTextKey));
        Embedding embedding = Embedding.from((List)indices.getValuesList());
        double cosineSimilarity = CosineSimilarity.between((Embedding)embedding, (Embedding)referenceEmbedding);
        return new EmbeddingMatch(Double.valueOf(RelevanceScore.fromCosineSimilarity((double)cosineSimilarity)), indices.getId(), embedding, textSegmentValue == null ? null : TextSegment.from((String)textSegmentValue.getStringValue(), (Metadata)metadata));
    }

    public static class Builder {
        private String apiKey;
        private String index;
        private String nameSpace;
        private String metadataTextKey;
        private PineconeIndexConfig createIndex;
        @Deprecated
        private String environment;
        @Deprecated
        private String projectId;

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder index(String index) {
            this.index = index;
            return this;
        }

        public Builder nameSpace(String nameSpace) {
            this.nameSpace = nameSpace;
            return this;
        }

        public Builder metadataTextKey(String metadataTextKey) {
            this.metadataTextKey = metadataTextKey;
            return this;
        }

        public Builder createIndex(PineconeIndexConfig createIndex) {
            this.createIndex = createIndex;
            return this;
        }

        @Deprecated
        public Builder environment(String environment) {
            this.environment = environment;
            return this;
        }

        @Deprecated
        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public PineconeEmbeddingStore build() {
            return new PineconeEmbeddingStore(this.apiKey, this.index, this.nameSpace, this.metadataTextKey, this.createIndex, this.environment, this.projectId);
        }
    }
}

