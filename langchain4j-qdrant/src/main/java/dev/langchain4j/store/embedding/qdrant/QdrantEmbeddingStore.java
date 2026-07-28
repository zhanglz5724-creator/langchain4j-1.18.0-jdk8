/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
 *  dev.langchain4j.store.embedding.filter.Filter
 *  io.qdrant.client.PointIdFactory
 *  io.qdrant.client.QdrantClient
 *  io.qdrant.client.QdrantGrpcClient
 *  io.qdrant.client.QdrantGrpcClient$Builder
 *  io.qdrant.client.QueryFactory
 *  io.qdrant.client.ValueFactory
 *  io.qdrant.client.VectorsFactory
 *  io.qdrant.client.WithPayloadSelectorFactory
 *  io.qdrant.client.WithVectorsSelectorFactory
 *  io.qdrant.client.grpc.Common$Filter
 *  io.qdrant.client.grpc.Common$PointId
 *  io.qdrant.client.grpc.JsonWithInt$Value
 *  io.qdrant.client.grpc.Points$DeletePoints
 *  io.qdrant.client.grpc.Points$PointStruct
 *  io.qdrant.client.grpc.Points$PointStruct$Builder
 *  io.qdrant.client.grpc.Points$PointsIdsList
 *  io.qdrant.client.grpc.Points$PointsSelector
 *  io.qdrant.client.grpc.Points$QueryPoints
 *  io.qdrant.client.grpc.Points$QueryPoints$Builder
 *  io.qdrant.client.grpc.Points$ScoredPoint
 *  io.qdrant.client.grpc.Points$VectorOutput
 *  javax.annotation.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.qdrant;

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
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.qdrant.ObjectFactory;
import dev.langchain4j.store.embedding.qdrant.QdrantFilterConverter;
import dev.langchain4j.store.embedding.qdrant.ValueMapFactory;
import io.qdrant.client.PointIdFactory;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.QueryFactory;
import io.qdrant.client.ValueFactory;
import io.qdrant.client.VectorsFactory;
import io.qdrant.client.WithPayloadSelectorFactory;
import io.qdrant.client.WithVectorsSelectorFactory;
import io.qdrant.client.grpc.Common;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QdrantEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final Logger log = LoggerFactory.getLogger(QdrantEmbeddingStore.class);
    private final QdrantClient client;
    private final String payloadTextKey;
    private final String collectionName;

    public QdrantEmbeddingStore(String collectionName, String host, int port, boolean useTls, String payloadTextKey, @Nullable String apiKey) {
        QdrantGrpcClient.Builder grpcClientBuilder = QdrantGrpcClient.newBuilder((String)host, (int)port, (boolean)useTls);
        if (apiKey != null) {
            grpcClientBuilder.withApiKey(apiKey);
        }
        this.client = new QdrantClient(grpcClientBuilder.build());
        this.collectionName = collectionName;
        this.payloadTextKey = payloadTextKey;
    }

    public QdrantEmbeddingStore(QdrantClient client, String collectionName, String payloadTextKey) {
        this.client = client;
        this.collectionName = collectionName;
        this.payloadTextKey = payloadTextKey;
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

    private void addInternal(String id, Embedding embedding, TextSegment textSegment) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), textSegment == null ? null : Collections.singletonList(textSegment));
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> textSegments) throws RuntimeException {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            log.info("Empty embeddings - no ops");
            return;
        }
        try {
            ArrayList<Points.PointStruct> points = new ArrayList<Points.PointStruct>(embeddings.size());
            for (int i = 0; i < embeddings.size(); ++i) {
                String id = ids.get(i);
                Common.PointId qdrantId = QdrantEmbeddingStore.toPointId(id);
                Embedding embedding = embeddings.get(i);
                Points.PointStruct.Builder pointBuilder = Points.PointStruct.newBuilder().setId(qdrantId).setVectors(VectorsFactory.vectors((float[])embedding.vector()));
                if (textSegments != null) {
                    Map metadata = textSegments.get(i).metadata().toMap();
                    Map<String, JsonWithInt.Value> payload = ValueMapFactory.valueMap(metadata);
                    payload.put(this.payloadTextKey, ValueFactory.value((String)textSegments.get(i).text()));
                    pointBuilder.putAllPayload(payload);
                }
                points.add(pointBuilder.build());
            }
            this.client.upsertAsync(this.collectionName, points).get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
        this.removeAll(Collections.singleton(id));
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        try {
            Points.PointsIdsList pointsIdsList = Points.PointsIdsList.newBuilder().addAllIds((Iterable)ids.stream().map(QdrantEmbeddingStore::toPointId).collect(Collectors.toList())).build();
            Points.PointsSelector pointsSelector = Points.PointsSelector.newBuilder().setPoints(pointsIdsList).build();
            this.client.deleteAsync(Points.DeletePoints.newBuilder().setCollectionName(this.collectionName).setPoints(pointsSelector).build()).get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        try {
            Common.Filter qdrantFilter = QdrantFilterConverter.convertExpression(filter);
            Points.PointsSelector pointsSelector = Points.PointsSelector.newBuilder().setFilter(qdrantFilter).build();
            this.client.deleteAsync(Points.DeletePoints.newBuilder().setCollectionName(this.collectionName).setPoints(pointsSelector).build()).get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAll() {
        this.clearStore();
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        List results;
        Points.QueryPoints.Builder queryBuilder = Points.QueryPoints.newBuilder().setCollectionName(this.collectionName).setQuery(QueryFactory.nearest((List)request.queryEmbedding().vectorAsList())).setWithVectors(WithVectorsSelectorFactory.enable((boolean)true)).setWithPayload(WithPayloadSelectorFactory.enable((boolean)true)).setLimit((long)request.maxResults());
        if (request.filter() != null) {
            Common.Filter filter = QdrantFilterConverter.convertExpression(request.filter());
            queryBuilder.setFilter(filter);
        }
        try {
            results = (List)this.client.queryAsync(queryBuilder.build()).get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        if (results.isEmpty()) {
            return new EmbeddingSearchResult(Collections.emptyList());
        }
        List matches = results.stream().map(vector -> this.toEmbeddingMatch((Points.ScoredPoint)vector, request.queryEmbedding())).filter(match -> match.score() >= request.minScore()).sorted(Comparator.comparingDouble(EmbeddingMatch::score)).collect(Collectors.toList());
        Collections.reverse(matches);
        return new EmbeddingSearchResult(matches);
    }

    public void clearStore() {
        try {
            Common.Filter emptyFilter = Common.Filter.newBuilder().build();
            Points.PointsSelector allPointsSelector = Points.PointsSelector.newBuilder().setFilter(emptyFilter).build();
            this.client.deleteAsync(Points.DeletePoints.newBuilder().setCollectionName(this.collectionName).setPoints(allPointsSelector).build()).get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        this.client.close();
    }

    private EmbeddingMatch<TextSegment> toEmbeddingMatch(Points.ScoredPoint scoredPoint, Embedding referenceEmbedding) {
        Map payload = scoredPoint.getPayloadMap();
        JsonWithInt.Value textSegmentValue = payload.getOrDefault(this.payloadTextKey, null);
        Map<String, Object> metadata = payload.entrySet().stream().filter(entry -> !((String)entry.getKey()).equals(this.payloadTextKey)).collect(Collectors.toMap(Map.Entry::getKey, entry -> ObjectFactory.object((JsonWithInt.Value)entry.getValue())));
        Embedding embedding = QdrantEmbeddingStore.toEmbedding(scoredPoint.getVectors().getVector());
        double cosineSimilarity = CosineSimilarity.between((Embedding)embedding, (Embedding)referenceEmbedding);
        return new EmbeddingMatch(Double.valueOf(RelevanceScore.fromCosineSimilarity((double)cosineSimilarity)), QdrantEmbeddingStore.pointIdToString(scoredPoint.getId()), embedding, textSegmentValue == null ? null : TextSegment.from((String)textSegmentValue.getStringValue(), (Metadata)new Metadata(metadata)));
    }

    private static Embedding toEmbedding(Points.VectorOutput vectorOutput) {
        return Embedding.from((List)Utils.getOrDefault((List)vectorOutput.getDense().getDataList(), (List)vectorOutput.getDataList()));
    }

    public static Builder builder() {
        return new Builder();
    }

    private static Common.PointId toPointId(String id) {
        try {
            long num = Long.parseUnsignedLong(id);
            return PointIdFactory.id((long)num);
        }
        catch (NumberFormatException e) {
            return PointIdFactory.id((UUID)UUID.fromString(id));
        }
    }

    private static String pointIdToString(Common.PointId pointId) {
        switch (pointId.getPointIdOptionsCase()) {
            case NUM: {
                return Long.toUnsignedString(pointId.getNum());
            }
            case UUID: {
                return pointId.getUuid();
            }
        }
        throw new IllegalStateException("Unknown point ID type: " + pointId.getPointIdOptionsCase());
    }

    public static class Builder {
        private String collectionName;
        private String host = "localhost";
        private int port = 6334;
        private boolean useTls = false;
        private String payloadTextKey = "text_segment";
        private String apiKey = null;
        private QdrantClient client = null;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder collectionName(String collectionName) {
            this.collectionName = collectionName;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder useTls(boolean useTls) {
            this.useTls = useTls;
            return this;
        }

        public Builder payloadTextKey(String payloadTextKey) {
            this.payloadTextKey = payloadTextKey;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder client(QdrantClient client) {
            this.client = client;
            return this;
        }

        public QdrantEmbeddingStore build() {
            Objects.requireNonNull(this.collectionName, "collectionName cannot be null");
            if (this.client != null) {
                return new QdrantEmbeddingStore(this.client, this.collectionName, this.payloadTextKey);
            }
            return new QdrantEmbeddingStore(this.collectionName, this.host, this.port, this.useTls, this.payloadTextKey, this.apiKey);
        }
    }
}

