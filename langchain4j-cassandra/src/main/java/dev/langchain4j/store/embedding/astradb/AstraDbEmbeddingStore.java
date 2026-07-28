/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.dtsx.astra.sdk.AstraDBCollection
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  io.stargate.sdk.data.domain.JsonDocument
 *  io.stargate.sdk.data.domain.JsonDocumentMutationResult
 *  io.stargate.sdk.data.domain.JsonDocumentResult
 *  io.stargate.sdk.data.domain.odm.Document
 *  io.stargate.sdk.data.domain.query.Filter
 *  org.jspecify.annotations.NonNull
 */
package dev.langchain4j.store.embedding.astradb;

import com.dtsx.astra.sdk.AstraDBCollection;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.stargate.sdk.data.domain.JsonDocument;
import io.stargate.sdk.data.domain.JsonDocumentMutationResult;
import io.stargate.sdk.data.domain.JsonDocumentResult;
import io.stargate.sdk.data.domain.odm.Document;
import io.stargate.sdk.data.domain.query.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;

public class AstraDbEmbeddingStore
implements EmbeddingStore<TextSegment> {
    public static final String KEY_ATTRIBUTES_BLOB = "body_blob";
    public static final String KEY_SIMILARITY = "$similarity";
    private final AstraDBCollection astraDBCollection;
    private final int itemsPerChunk;
    private final int concurrentThreads;

    public AstraDbEmbeddingStore(@NonNull AstraDBCollection client) {
        this(client, 20, 8);
    }

    public AstraDbEmbeddingStore(@NonNull AstraDBCollection client, int itemsPerChunk, int concurrentThreads) {
        Objects.requireNonNull(client, "'client' must not be null");
        if (itemsPerChunk > 20 || itemsPerChunk < 1) {
            throw new IllegalArgumentException("'itemsPerChunk' should be in between 1 and 20");
        }
        if (concurrentThreads < 1) {
            throw new IllegalArgumentException("'concurrentThreads' should be at least 1");
        }
        this.astraDBCollection = client;
        this.itemsPerChunk = itemsPerChunk;
        this.concurrentThreads = concurrentThreads;
    }

    public void clear() {
        this.astraDBCollection.deleteAll();
    }

    public String add(Embedding embedding) {
        return this.add(embedding, null);
    }

    public String add(Embedding embedding, TextSegment textSegment) {
        return this.astraDBCollection.insertOne(this.mapRecord(Utils.randomUUID(), embedding, textSegment)).getDocument().getId();
    }

    public void add(String id, Embedding embedding) {
        this.astraDBCollection.upsertOne(new JsonDocument().id(id).vector(embedding.vector()));
    }

    public List<String> addAll(List<Embedding> embeddings) {
        if (embeddings == null) {
            return null;
        }
        List recordList = embeddings.stream().map(e -> this.mapRecord(Utils.randomUUID(), (Embedding)e, null)).collect(Collectors.toList());
        return this.astraDBCollection.insertManyChunkedJsonDocuments(recordList, this.itemsPerChunk, this.concurrentThreads).stream().map(JsonDocumentMutationResult::getDocument).map(Document::getId).collect(Collectors.toList());
    }

    public void addAll(List<String> ids, List<Embedding> embeddingList, List<TextSegment> textSegmentList) {
        if (embeddingList == null || textSegmentList == null || embeddingList.size() != textSegmentList.size()) {
            throw new IllegalArgumentException("embeddingList and textSegmentList must not be null and have the same size");
        }
        ArrayList<JsonDocument> recordList = new ArrayList<JsonDocument>();
        for (int i = 0; i < embeddingList.size(); ++i) {
            recordList.add(this.mapRecord(ids.get(i), embeddingList.get(i), textSegmentList.get(i)));
        }
        this.astraDBCollection.insertManyChunkedJsonDocuments(recordList, this.itemsPerChunk, this.concurrentThreads);
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        if (request.filter() != null) {
            throw new UnsupportedOperationException("EmbeddingSearchRequest.Filter is not supported yet.");
        }
        List<EmbeddingMatch<TextSegment>> matches = this.findRelevant(request.queryEmbedding(), request.maxResults(), request.minScore());
        return new EmbeddingSearchResult(matches);
    }

    public List<EmbeddingMatch<TextSegment>> findRelevant(Embedding referenceEmbedding, int maxResults, double minScore) {
        return this.findRelevant(referenceEmbedding, null, maxResults, minScore);
    }

    public List<EmbeddingMatch<TextSegment>> findRelevant(Embedding referenceEmbedding, Filter metaDatafilter, int maxResults, double minScore) {
        return this.astraDBCollection.findVector(referenceEmbedding.vector(), metaDatafilter, maxResults).filter(r -> (double)r.getSimilarity().floatValue() >= minScore).map(this::mapJsonResult).collect(Collectors.toList());
    }

    private EmbeddingMatch<TextSegment> mapJsonResult(JsonDocumentResult jsonRes) {
        Object body;
        Double score = jsonRes.getSimilarity().floatValue();
        String embeddingId = jsonRes.getId();
        Embedding embedding = Embedding.from((float[])jsonRes.getVector());
        TextSegment embedded = null;
        Map properties = jsonRes.getData();
        if (properties != null && (body = properties.get(KEY_ATTRIBUTES_BLOB)) != null) {
            Metadata metadata = new Metadata(properties.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() == null ? "" : entry.getValue().toString())));
            metadata.remove(KEY_ATTRIBUTES_BLOB);
            metadata.remove(KEY_SIMILARITY);
            embedded = new TextSegment(body.toString(), metadata);
        }
        return new EmbeddingMatch(score, embeddingId, embedding, embedded);
    }

    private JsonDocument mapRecord(String id, Embedding embedding, TextSegment textSegment) {
        JsonDocument record = new JsonDocument().id(id).vector(embedding.vector());
        if (textSegment != null) {
            record.put(KEY_ATTRIBUTES_BLOB, (Object)textSegment.text());
            Utils.toStringValueMap((Map)textSegment.metadata().toMap()).forEach((arg_0, arg_1) -> ((JsonDocument)record).put(arg_0, arg_1));
        }
        return record;
    }

    public AstraDBCollection astraDBCollection() {
        return this.astraDBCollection;
    }

    public int itemsPerChunk() {
        return this.itemsPerChunk;
    }

    public int concurrentThreads() {
        return this.concurrentThreads;
    }
}

