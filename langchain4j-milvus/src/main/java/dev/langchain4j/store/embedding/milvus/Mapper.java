/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.ToNumberPolicy
 *  com.google.gson.ToNumberStrategy
 *  com.google.gson.reflect.TypeToken
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.RelevanceScore
 *  io.milvus.client.MilvusServiceClient
 *  io.milvus.common.clientenum.ConsistencyLevelEnum
 *  io.milvus.exception.ParamException
 *  io.milvus.param.MetricType
 *  io.milvus.response.QueryResultsWrapper
 *  io.milvus.response.QueryResultsWrapper$RowRecord
 *  io.milvus.response.SearchResultsWrapper
 *  io.milvus.response.SearchResultsWrapper$IDScore
 */
package dev.langchain4j.store.embedding.milvus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.reflect.TypeToken;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.RelevanceScore;
import dev.langchain4j.store.embedding.milvus.CollectionOperationsExecutor;
import dev.langchain4j.store.embedding.milvus.FieldDefinition;
import dev.langchain4j.store.embedding.milvus.Generator;
import io.milvus.client.MilvusServiceClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.exception.ParamException;
import io.milvus.param.MetricType;
import io.milvus.response.QueryResultsWrapper;
import io.milvus.response.SearchResultsWrapper;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Mapper {
    private static final Gson GSON = new GsonBuilder().setObjectToNumberStrategy((ToNumberStrategy)ToNumberPolicy.LONG_OR_DOUBLE).create();
    private static final Type MAP_TYPE = new TypeToken<Map<String, Object>>(){}.getType();

    Mapper() {
    }

    static List<List<Float>> toVectors(List<Embedding> embeddings) {
        return embeddings.stream().map(Embedding::vectorAsList).collect(Collectors.toList());
    }

    static List<String> toScalars(List<TextSegment> textSegments, int size) {
        return Utils.isNullOrEmpty(textSegments) ? Generator.generateEmptyScalars(size) : Mapper.textSegmentsToScalars(textSegments);
    }

    static List<JsonObject> toMetadataJsons(List<TextSegment> textSegments, int size) {
        return Utils.isNullOrEmpty(textSegments) ? Generator.generateEmptyJsons(size) : textSegments.stream().map(segment -> GSON.toJsonTree((Object)segment.metadata().toMap()).getAsJsonObject()).collect(Collectors.toList());
    }

    static List<String> textSegmentsToScalars(List<TextSegment> textSegments) {
        return textSegments.stream().map(TextSegment::text).collect(Collectors.toList());
    }

    static List<EmbeddingMatch<TextSegment>> toEmbeddingMatches(MilvusServiceClient milvusClient, SearchResultsWrapper resultsWrapper, String collectionName, FieldDefinition fieldDefinition, ConsistencyLevelEnum consistencyLevel, boolean queryForVectorOnSearch, MetricType metricType) {
        ArrayList<EmbeddingMatch<TextSegment>> matches = new ArrayList<EmbeddingMatch<TextSegment>>();
        HashMap<String, Embedding> idToEmbedding = new HashMap<String, Embedding>();
        if (queryForVectorOnSearch && !resultsWrapper.getRowRecords().isEmpty()) {
            try {
                List rowIds = resultsWrapper.getFieldWrapper(fieldDefinition.getIdFieldName()).getFieldData();
                idToEmbedding.putAll(Mapper.queryEmbeddings(milvusClient, collectionName, fieldDefinition, rowIds, consistencyLevel));
            }
            catch (ParamException rowIds) {
                // empty catch block
            }
        }
        for (int i = 0; i < resultsWrapper.getRowRecords().size(); ++i) {
            double score = ((SearchResultsWrapper.IDScore)resultsWrapper.getIDScore(0).get(i)).getScore();
            String rowId = ((SearchResultsWrapper.IDScore)resultsWrapper.getIDScore(0).get(i)).getStrID();
            Embedding embedding = (Embedding)idToEmbedding.get(rowId);
            TextSegment textSegment = Mapper.toTextSegment((QueryResultsWrapper.RowRecord)resultsWrapper.getRowRecords().get(i), fieldDefinition);
            EmbeddingMatch embeddingMatch = new EmbeddingMatch(Double.valueOf(Mapper.toRelevanceScore(score, metricType)), rowId, embedding, (Object)textSegment);
            matches.add((EmbeddingMatch<TextSegment>)embeddingMatch);
        }
        return matches;
    }

    static double toRelevanceScore(double score, MetricType metricType) {
        if (metricType == MetricType.L2) {
            return 1.0 / (1.0 + score);
        }
        return RelevanceScore.fromCosineSimilarity((double)score);
    }

    private static TextSegment toTextSegment(QueryResultsWrapper.RowRecord rowRecord, FieldDefinition fieldDefinition) {
        String text;
        Object textField = rowRecord.get(fieldDefinition.getTextFieldName());
        String string = text = textField == null ? null : textField.toString();
        if (Utils.isNullOrBlank((String)text)) {
            return null;
        }
        if (!rowRecord.getFieldValues().containsKey(fieldDefinition.getMetadataFieldName())) {
            return TextSegment.from((String)text);
        }
        JsonObject metadata = (JsonObject)rowRecord.get(fieldDefinition.getMetadataFieldName());
        return TextSegment.from((String)text, (Metadata)Mapper.toMetadata(metadata));
    }

    private static Metadata toMetadata(JsonObject metadata) {
        Map metadataMap = (Map)GSON.fromJson((JsonElement)metadata, MAP_TYPE);
        metadataMap.forEach((key, value) -> {
            if (value instanceof BigDecimal) {
                metadataMap.put(key, ((BigDecimal)value).doubleValue());
            }
        });
        return Metadata.from((Map)metadataMap);
    }

    private static Map<String, Embedding> queryEmbeddings(MilvusServiceClient milvusClient, String collectionName, FieldDefinition fieldDefinition, List<String> rowIds, ConsistencyLevelEnum consistencyLevel) {
        QueryResultsWrapper queryResultsWrapper = CollectionOperationsExecutor.queryForVectors(milvusClient, collectionName, fieldDefinition, rowIds, consistencyLevel);
        HashMap<String, Embedding> idToEmbedding = new HashMap<String, Embedding>();
        for (QueryResultsWrapper.RowRecord row : queryResultsWrapper.getRowRecords()) {
            String id = row.get(fieldDefinition.getIdFieldName()).toString();
            List vector = (List)row.get(fieldDefinition.getVectorFieldName());
            idToEmbedding.put(id, Embedding.from((List)vector));
        }
        return idToEmbedding;
    }
}

