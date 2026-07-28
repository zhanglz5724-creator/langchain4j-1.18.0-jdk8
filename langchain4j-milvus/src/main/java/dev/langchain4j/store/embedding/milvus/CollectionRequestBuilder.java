/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.store.embedding.filter.Filter
 *  io.milvus.common.clientenum.ConsistencyLevelEnum
 *  io.milvus.param.MetricType
 *  io.milvus.param.collection.DropCollectionParam
 *  io.milvus.param.collection.FlushParam
 *  io.milvus.param.collection.HasCollectionParam
 *  io.milvus.param.collection.LoadCollectionParam
 *  io.milvus.param.dml.DeleteParam
 *  io.milvus.param.dml.InsertParam
 *  io.milvus.param.dml.InsertParam$Field
 *  io.milvus.param.dml.QueryParam
 *  io.milvus.param.dml.SearchParam
 *  io.milvus.param.dml.SearchParam$Builder
 */
package dev.langchain4j.store.embedding.milvus;

import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.milvus.FieldDefinition;
import dev.langchain4j.store.embedding.milvus.MilvusMetadataFilterMapper;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.param.MetricType;
import io.milvus.param.collection.DropCollectionParam;
import io.milvus.param.collection.FlushParam;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.dml.DeleteParam;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.QueryParam;
import io.milvus.param.dml.SearchParam;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class CollectionRequestBuilder {
    CollectionRequestBuilder() {
    }

    static FlushParam buildFlushRequest(String collectionName) {
        return FlushParam.newBuilder().withCollectionNames(Collections.singletonList(collectionName)).build();
    }

    static HasCollectionParam buildHasCollectionRequest(String collectionName) {
        return HasCollectionParam.newBuilder().withCollectionName(collectionName).build();
    }

    static DropCollectionParam buildDropCollectionRequest(String collectionName) {
        return DropCollectionParam.newBuilder().withCollectionName(collectionName).build();
    }

    static InsertParam buildInsertRequest(String collectionName, List<InsertParam.Field> fields) {
        return InsertParam.newBuilder().withCollectionName(collectionName).withFields(fields).build();
    }

    static LoadCollectionParam buildLoadCollectionInMemoryRequest(String collectionName) {
        return LoadCollectionParam.newBuilder().withCollectionName(collectionName).build();
    }

    static SearchParam buildSearchRequest(String collectionName, FieldDefinition fieldDefinition, List<Float> vector, Filter filter, int maxResults, MetricType metricType, ConsistencyLevelEnum consistencyLevel) {
        SearchParam.Builder builder = SearchParam.newBuilder().withCollectionName(collectionName).withFloatVectors(Collections.singletonList(vector)).withVectorFieldName(fieldDefinition.getVectorFieldName()).withTopK(Integer.valueOf(maxResults)).withMetricType(metricType).withConsistencyLevel(consistencyLevel).withOutFields(Arrays.asList(fieldDefinition.getIdFieldName(), fieldDefinition.getTextFieldName(), fieldDefinition.getMetadataFieldName()));
        if (filter != null) {
            builder.withExpr(MilvusMetadataFilterMapper.map(filter, fieldDefinition.getMetadataFieldName()));
        }
        return builder.build();
    }

    static QueryParam buildQueryRequest(String collectionName, FieldDefinition fieldDefinition, List<String> rowIds, ConsistencyLevelEnum consistencyLevel) {
        return QueryParam.newBuilder().withCollectionName(collectionName).withExpr(CollectionRequestBuilder.buildQueryExpression(rowIds, fieldDefinition.getIdFieldName())).withConsistencyLevel(consistencyLevel).withOutFields(Collections.singletonList(fieldDefinition.getVectorFieldName())).withLimit(Long.valueOf(rowIds.size())).build();
    }

    static DeleteParam buildDeleteRequest(String collectionName, String expr) {
        return DeleteParam.newBuilder().withCollectionName(collectionName).withExpr(expr).build();
    }

    private static String buildQueryExpression(List<String> rowIds, String idFieldName) {
        return rowIds.stream().map(id -> String.format("%s == '%s'", idFieldName, id)).collect(Collectors.joining(" || "));
    }
}

