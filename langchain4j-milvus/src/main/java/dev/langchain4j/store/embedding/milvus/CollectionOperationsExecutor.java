/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.milvus.client.MilvusServiceClient
 *  io.milvus.common.clientenum.ConsistencyLevelEnum
 *  io.milvus.grpc.DataType
 *  io.milvus.grpc.QueryResults
 *  io.milvus.grpc.SearchResults
 *  io.milvus.param.IndexType
 *  io.milvus.param.MetricType
 *  io.milvus.param.R
 *  io.milvus.param.R$Status
 *  io.milvus.param.collection.CollectionSchemaParam
 *  io.milvus.param.collection.CreateCollectionParam
 *  io.milvus.param.collection.DropCollectionParam
 *  io.milvus.param.collection.FieldType
 *  io.milvus.param.collection.FlushParam
 *  io.milvus.param.collection.HasCollectionParam
 *  io.milvus.param.collection.LoadCollectionParam
 *  io.milvus.param.dml.InsertParam
 *  io.milvus.param.dml.InsertParam$Field
 *  io.milvus.param.dml.QueryParam
 *  io.milvus.param.dml.SearchParam
 *  io.milvus.param.index.CreateIndexParam
 *  io.milvus.response.QueryResultsWrapper
 *  io.milvus.response.SearchResultsWrapper
 */
package dev.langchain4j.store.embedding.milvus;

import dev.langchain4j.store.embedding.milvus.CollectionRequestBuilder;
import dev.langchain4j.store.embedding.milvus.FieldDefinition;
import dev.langchain4j.store.embedding.milvus.RequestToMilvusFailedException;
import io.milvus.client.MilvusServiceClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.grpc.DataType;
import io.milvus.grpc.QueryResults;
import io.milvus.grpc.SearchResults;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.collection.CollectionSchemaParam;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.DropCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.collection.FlushParam;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.QueryParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.param.index.CreateIndexParam;
import io.milvus.response.QueryResultsWrapper;
import io.milvus.response.SearchResultsWrapper;
import java.util.List;

class CollectionOperationsExecutor {
    CollectionOperationsExecutor() {
    }

    static void flush(MilvusServiceClient milvusClient, String collectionName) {
        FlushParam request = CollectionRequestBuilder.buildFlushRequest(collectionName);
        R response = milvusClient.flush(request);
        CollectionOperationsExecutor.checkResponseNotFailed(response);
    }

    static boolean hasCollection(MilvusServiceClient milvusClient, String collectionName) {
        HasCollectionParam request = CollectionRequestBuilder.buildHasCollectionRequest(collectionName);
        R response = milvusClient.hasCollection(request);
        CollectionOperationsExecutor.checkResponseNotFailed(response);
        return (Boolean)response.getData();
    }

    static void createCollection(MilvusServiceClient milvusClient, String collectionName, FieldDefinition fieldDefinition, int dimension) {
        CreateCollectionParam request = CreateCollectionParam.newBuilder().withCollectionName(collectionName).withSchema(CollectionSchemaParam.newBuilder().addFieldType(FieldType.newBuilder().withName(fieldDefinition.getIdFieldName()).withDataType(DataType.VarChar).withMaxLength(Integer.valueOf(36)).withPrimaryKey(true).withAutoID(false).build()).addFieldType(FieldType.newBuilder().withName(fieldDefinition.getTextFieldName()).withDataType(DataType.VarChar).withMaxLength(Integer.valueOf(65535)).build()).addFieldType(FieldType.newBuilder().withName(fieldDefinition.getMetadataFieldName()).withDataType(DataType.JSON).build()).addFieldType(FieldType.newBuilder().withName(fieldDefinition.getVectorFieldName()).withDataType(DataType.FloatVector).withDimension(Integer.valueOf(dimension)).build()).build()).build();
        R response = milvusClient.createCollection(request);
        CollectionOperationsExecutor.checkResponseNotFailed(response);
    }

    static void dropCollection(MilvusServiceClient milvusClient, String collectionName) {
        DropCollectionParam request = CollectionRequestBuilder.buildDropCollectionRequest(collectionName);
        R response = milvusClient.dropCollection(request);
        CollectionOperationsExecutor.checkResponseNotFailed(response);
    }

    static void createIndex(MilvusServiceClient milvusClient, String collectionName, String vectorFieldName, IndexType indexType, MetricType metricType, String extraParameters) {
        CreateIndexParam request = CreateIndexParam.newBuilder().withCollectionName(collectionName).withFieldName(vectorFieldName).withIndexType(indexType).withMetricType(metricType).withExtraParam(extraParameters).build();
        R response = milvusClient.createIndex(request);
        CollectionOperationsExecutor.checkResponseNotFailed(response);
    }

    static void createIndex(MilvusServiceClient milvusClient, String collectionName, String vectorFieldName, IndexType indexType, MetricType metricType) {
        CreateIndexParam request = CreateIndexParam.newBuilder().withCollectionName(collectionName).withFieldName(vectorFieldName).withIndexType(indexType).withMetricType(metricType).build();
        R response = milvusClient.createIndex(request);
        CollectionOperationsExecutor.checkResponseNotFailed(response);
    }

    static void insert(MilvusServiceClient milvusClient, String collectionName, List<InsertParam.Field> fields) {
        InsertParam request = CollectionRequestBuilder.buildInsertRequest(collectionName, fields);
        R response = milvusClient.insert(request);
        CollectionOperationsExecutor.checkResponseNotFailed(response);
    }

    static void loadCollectionInMemory(MilvusServiceClient milvusClient, String collectionName) {
        LoadCollectionParam request = CollectionRequestBuilder.buildLoadCollectionInMemoryRequest(collectionName);
        R response = milvusClient.loadCollection(request);
        CollectionOperationsExecutor.checkResponseNotFailed(response);
    }

    static SearchResultsWrapper search(MilvusServiceClient milvusClient, SearchParam searchRequest) {
        R response = milvusClient.search(searchRequest);
        CollectionOperationsExecutor.checkResponseNotFailed(response);
        return new SearchResultsWrapper(((SearchResults)response.getData()).getResults());
    }

    static QueryResultsWrapper queryForVectors(MilvusServiceClient milvusClient, String collectionName, FieldDefinition fieldDefinition, List<String> rowIds, ConsistencyLevelEnum consistencyLevel) {
        QueryParam request = CollectionRequestBuilder.buildQueryRequest(collectionName, fieldDefinition, rowIds, consistencyLevel);
        R response = milvusClient.query(request);
        CollectionOperationsExecutor.checkResponseNotFailed(response);
        return new QueryResultsWrapper((QueryResults)response.getData());
    }

    static void removeForVector(MilvusServiceClient milvusClient, String collectionName, String expr) {
        R response = milvusClient.delete(CollectionRequestBuilder.buildDeleteRequest(collectionName, expr));
        CollectionOperationsExecutor.checkResponseNotFailed(response);
    }

    private static <T> void checkResponseNotFailed(R<T> response) {
        if (response == null) {
            throw new RequestToMilvusFailedException("Request to Milvus DB failed. Response is null");
        }
        if (response.getStatus().intValue() != R.Status.Success.getCode()) {
            String message = String.format("Request to Milvus DB failed. Response status:'%d'.%n", response.getStatus());
            throw new RequestToMilvusFailedException(message, response.getException());
        }
    }
}

