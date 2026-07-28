/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alicloud.openservices.tablestore.SyncClient
 *  com.alicloud.openservices.tablestore.core.utils.ValueUtil
 *  com.alicloud.openservices.tablestore.model.CapacityUnit
 *  com.alicloud.openservices.tablestore.model.Column
 *  com.alicloud.openservices.tablestore.model.ColumnType
 *  com.alicloud.openservices.tablestore.model.ColumnValue
 *  com.alicloud.openservices.tablestore.model.CreateTableRequest
 *  com.alicloud.openservices.tablestore.model.DeleteRowRequest
 *  com.alicloud.openservices.tablestore.model.DeleteTableRequest
 *  com.alicloud.openservices.tablestore.model.Direction
 *  com.alicloud.openservices.tablestore.model.GetRangeRequest
 *  com.alicloud.openservices.tablestore.model.GetRangeResponse
 *  com.alicloud.openservices.tablestore.model.ListTableResponse
 *  com.alicloud.openservices.tablestore.model.PrimaryKey
 *  com.alicloud.openservices.tablestore.model.PrimaryKeyBuilder
 *  com.alicloud.openservices.tablestore.model.PrimaryKeySchema
 *  com.alicloud.openservices.tablestore.model.PrimaryKeyType
 *  com.alicloud.openservices.tablestore.model.PrimaryKeyValue
 *  com.alicloud.openservices.tablestore.model.PutRowRequest
 *  com.alicloud.openservices.tablestore.model.RangeRowQueryCriteria
 *  com.alicloud.openservices.tablestore.model.ReservedThroughput
 *  com.alicloud.openservices.tablestore.model.Row
 *  com.alicloud.openservices.tablestore.model.RowDeleteChange
 *  com.alicloud.openservices.tablestore.model.RowPutChange
 *  com.alicloud.openservices.tablestore.model.TableMeta
 *  com.alicloud.openservices.tablestore.model.TableOptions
 *  com.alicloud.openservices.tablestore.model.search.CreateSearchIndexRequest
 *  com.alicloud.openservices.tablestore.model.search.DeleteSearchIndexRequest
 *  com.alicloud.openservices.tablestore.model.search.FieldSchema
 *  com.alicloud.openservices.tablestore.model.search.FieldSchema$Analyzer
 *  com.alicloud.openservices.tablestore.model.search.FieldType
 *  com.alicloud.openservices.tablestore.model.search.IndexSchema
 *  com.alicloud.openservices.tablestore.model.search.ListSearchIndexRequest
 *  com.alicloud.openservices.tablestore.model.search.ListSearchIndexResponse
 *  com.alicloud.openservices.tablestore.model.search.SearchHit
 *  com.alicloud.openservices.tablestore.model.search.SearchIndexInfo
 *  com.alicloud.openservices.tablestore.model.search.SearchQuery
 *  com.alicloud.openservices.tablestore.model.search.SearchRequest
 *  com.alicloud.openservices.tablestore.model.search.SearchResponse
 *  com.alicloud.openservices.tablestore.model.search.query.KnnVectorQuery
 *  com.alicloud.openservices.tablestore.model.search.query.Query
 *  com.alicloud.openservices.tablestore.model.search.query.QueryBuilders
 *  com.alicloud.openservices.tablestore.model.search.sort.ScoreSort
 *  com.alicloud.openservices.tablestore.model.search.sort.Sort
 *  com.alicloud.openservices.tablestore.model.search.vector.VectorDataType
 *  com.alicloud.openservices.tablestore.model.search.vector.VectorMetricType
 *  com.alicloud.openservices.tablestore.model.search.vector.VectorOptions
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.filter.Filter
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.tablestore;

import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.core.utils.ValueUtil;
import com.alicloud.openservices.tablestore.model.CapacityUnit;
import com.alicloud.openservices.tablestore.model.Column;
import com.alicloud.openservices.tablestore.model.ColumnType;
import com.alicloud.openservices.tablestore.model.ColumnValue;
import com.alicloud.openservices.tablestore.model.CreateTableRequest;
import com.alicloud.openservices.tablestore.model.DeleteRowRequest;
import com.alicloud.openservices.tablestore.model.DeleteTableRequest;
import com.alicloud.openservices.tablestore.model.Direction;
import com.alicloud.openservices.tablestore.model.GetRangeRequest;
import com.alicloud.openservices.tablestore.model.GetRangeResponse;
import com.alicloud.openservices.tablestore.model.ListTableResponse;
import com.alicloud.openservices.tablestore.model.PrimaryKey;
import com.alicloud.openservices.tablestore.model.PrimaryKeyBuilder;
import com.alicloud.openservices.tablestore.model.PrimaryKeySchema;
import com.alicloud.openservices.tablestore.model.PrimaryKeyType;
import com.alicloud.openservices.tablestore.model.PrimaryKeyValue;
import com.alicloud.openservices.tablestore.model.PutRowRequest;
import com.alicloud.openservices.tablestore.model.RangeRowQueryCriteria;
import com.alicloud.openservices.tablestore.model.ReservedThroughput;
import com.alicloud.openservices.tablestore.model.Row;
import com.alicloud.openservices.tablestore.model.RowDeleteChange;
import com.alicloud.openservices.tablestore.model.RowPutChange;
import com.alicloud.openservices.tablestore.model.TableMeta;
import com.alicloud.openservices.tablestore.model.TableOptions;
import com.alicloud.openservices.tablestore.model.search.CreateSearchIndexRequest;
import com.alicloud.openservices.tablestore.model.search.DeleteSearchIndexRequest;
import com.alicloud.openservices.tablestore.model.search.FieldSchema;
import com.alicloud.openservices.tablestore.model.search.FieldType;
import com.alicloud.openservices.tablestore.model.search.IndexSchema;
import com.alicloud.openservices.tablestore.model.search.ListSearchIndexRequest;
import com.alicloud.openservices.tablestore.model.search.ListSearchIndexResponse;
import com.alicloud.openservices.tablestore.model.search.SearchHit;
import com.alicloud.openservices.tablestore.model.search.SearchIndexInfo;
import com.alicloud.openservices.tablestore.model.search.SearchQuery;
import com.alicloud.openservices.tablestore.model.search.SearchRequest;
import com.alicloud.openservices.tablestore.model.search.SearchResponse;
import com.alicloud.openservices.tablestore.model.search.query.KnnVectorQuery;
import com.alicloud.openservices.tablestore.model.search.query.Query;
import com.alicloud.openservices.tablestore.model.search.query.QueryBuilders;
import com.alicloud.openservices.tablestore.model.search.sort.ScoreSort;
import com.alicloud.openservices.tablestore.model.search.sort.Sort;
import com.alicloud.openservices.tablestore.model.search.vector.VectorDataType;
import com.alicloud.openservices.tablestore.model.search.vector.VectorMetricType;
import com.alicloud.openservices.tablestore.model.search.vector.VectorOptions;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.tablestore.TablestoreMetadataFilterMapper;
import dev.langchain4j.store.embedding.tablestore.TablestoreUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TablestoreEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SyncClient client;
    private final String tableName;
    private final String searchIndexName;
    private final String pkName;
    private final String textField;
    private final String embeddingField;
    private final int vectorDimension;
    private final VectorMetricType vectorMetricType;
    private final List<FieldSchema> metadataSchemaList;
    private static final String DEFAULT_TABLE_NAME = "langchain4j_embedding_store_ots_v1";
    private static final String DEFAULT_INDEX_NAME = "langchain4j_embedding_ots_index_v1";
    private static final String DEFAULT_TABLE_PK_NAME = "id";
    private static final String DEFAULT_TEXT_FIELD_NAME = "default_content";
    private static final String DEFAULT_VECTOR_FIELD_NAME = "default_embedding";
    private static final VectorMetricType DEFAULT_VECTOR_METRIC_TYPE = VectorMetricType.COSINE;

    public TablestoreEmbeddingStore(SyncClient client, int vectorDimension) {
        this(client, vectorDimension, Collections.emptyList());
    }

    public TablestoreEmbeddingStore(SyncClient client, int vectorDimension, List<FieldSchema> metadataSchemaList) {
        this(client, DEFAULT_TABLE_NAME, DEFAULT_INDEX_NAME, DEFAULT_TABLE_PK_NAME, DEFAULT_TEXT_FIELD_NAME, DEFAULT_VECTOR_FIELD_NAME, vectorDimension, DEFAULT_VECTOR_METRIC_TYPE, metadataSchemaList);
    }

    public TablestoreEmbeddingStore(SyncClient client, String tableName, String searchIndexName, String pkName, String textField, String embeddingField, int vectorDimension, VectorMetricType vectorMetricType, List<FieldSchema> metadataSchemaList) {
        this.client = (SyncClient)ValidationUtils.ensureNotNull((Object)client, (String)"client");
        this.tableName = ValidationUtils.ensureNotBlank((String)tableName, (String)"tableName");
        this.searchIndexName = ValidationUtils.ensureNotBlank((String)searchIndexName, (String)"searchIndexName");
        this.pkName = ValidationUtils.ensureNotBlank((String)pkName, (String)"pkName");
        this.textField = ValidationUtils.ensureNotBlank((String)textField, (String)"textField");
        this.embeddingField = ValidationUtils.ensureNotBlank((String)embeddingField, (String)"embeddingField");
        this.vectorDimension = ValidationUtils.ensureGreaterThanZero((Integer)vectorDimension, (String)"vectorDimension");
        this.vectorMetricType = (VectorMetricType)ValidationUtils.ensureNotNull((Object)vectorMetricType, (String)"vectorMetricType");
        ValidationUtils.ensureNotNull(metadataSchemaList, (String)"metadataSchemaList");
        ArrayList<FieldSchema> tmpMetaList = new ArrayList<FieldSchema>();
        tmpMetaList.add(new FieldSchema(textField, FieldType.TEXT).setIndex(true).setAnalyzer(FieldSchema.Analyzer.MaxWord));
        tmpMetaList.add(new FieldSchema(embeddingField, FieldType.VECTOR).setIndex(true).setVectorOptions(new VectorOptions(VectorDataType.FLOAT_32, vectorDimension, vectorMetricType)));
        for (FieldSchema fieldSchema : metadataSchemaList) {
            if (fieldSchema.getFieldName().equals(textField)) {
                throw Exceptions.illegalArgument((String)"the custom meta data field name matches the system text field:{}", (Object[])new Object[]{textField});
            }
            if (fieldSchema.getFieldName().equals(embeddingField)) {
                throw Exceptions.illegalArgument((String)"the custom meta data field name matches the system embedding field:{}", (Object[])new Object[]{embeddingField});
            }
            tmpMetaList.add(fieldSchema);
        }
        this.metadataSchemaList = Collections.unmodifiableList(tmpMetaList);
    }

    public void init() {
        this.createTableIfNotExist();
        this.createSearchIndexIfNotExist();
    }

    public SyncClient getClient() {
        return this.client;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getSearchIndexName() {
        return this.searchIndexName;
    }

    public String getPkName() {
        return this.pkName;
    }

    public String getTextField() {
        return this.textField;
    }

    public String getEmbeddingField() {
        return this.embeddingField;
    }

    public int getVectorDimension() {
        return this.vectorDimension;
    }

    public VectorMetricType getVectorMetricType() {
        return this.vectorMetricType;
    }

    public List<FieldSchema> getMetadataSchemaList() {
        return this.metadataSchemaList;
    }

    public String add(Embedding embedding) {
        String id = UUID.randomUUID().toString();
        this.innerAdd(id, embedding, null);
        return id;
    }

    public void add(String id, Embedding embedding) {
        this.innerAdd(id, embedding, null);
    }

    public String add(Embedding embedding, TextSegment textSegment) {
        String id = UUID.randomUUID().toString();
        this.innerAdd(id, embedding, textSegment);
        return id;
    }

    public List<String> addAll(List<Embedding> embeddings) {
        return this.addAll(embeddings, null);
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (embedded != null) {
            ValidationUtils.ensureEq((Object)embeddings.size(), (Object)embedded.size(), (String)"the size of embeddings should be the same as the size of embedded", (Object[])new Object[0]);
        }
        ArrayList<Exception> exceptions = new ArrayList<Exception>();
        for (int i = 0; i < embeddings.size(); ++i) {
            Embedding embedding = embeddings.get(i);
            TextSegment textSegment = null;
            if (embedded != null) {
                textSegment = embedded.get(i);
            }
            try {
                this.innerAdd(ids.get(i), embedding, textSegment);
                continue;
            }
            catch (Exception e) {
                exceptions.add(e);
            }
        }
        if (!exceptions.isEmpty()) {
            IllegalStateException exception = new IllegalStateException("Add all embeddings with error, failed:" + exceptions.size());
            for (Exception e : exceptions) {
                exception.addSuppressed(e);
            }
            throw exception;
        }
    }

    public void remove(String id) {
        ValidationUtils.ensureNotBlank((String)id, (String)DEFAULT_TABLE_PK_NAME);
        this.innerDelete(id);
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        this.log.debug("remove all:{}", ids);
        ArrayList<Exception> exceptions = new ArrayList<Exception>();
        for (String id : ids) {
            try {
                this.remove(id);
            }
            catch (Exception e) {
                exceptions.add(e);
            }
        }
        if (!exceptions.isEmpty()) {
            IllegalStateException exception = new IllegalStateException("remove all embeddings with error, failed:" + exceptions.size());
            for (Exception e : exceptions) {
                exception.addSuppressed(e);
            }
            throw exception;
        }
    }

    public void removeAll(Filter filter) {
        if (filter == null) {
            throw Exceptions.illegalArgument((String)"filter cannot be null", (Object[])new Object[0]);
        }
        this.forEachAllData(Collections.emptyList(), row -> {
            Metadata metadata = this.rowToMetadata((Row)row);
            if (filter.test((Object)metadata)) {
                this.remove(row.getPrimaryKey().getPrimaryKeyColumn(this.pkName).getValue().asString());
            }
        });
    }

    public void removeAll() {
        this.log.debug("remove all");
        this.forEachAllData(Collections.emptyList(), row -> this.innerDelete(row.getPrimaryKey().getPrimaryKeyColumn(this.pkName).getValue().asString()));
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        this.log.debug("search ([...{}...], {}, {})", new Object[]{request.queryEmbedding().vector().length, request.maxResults(), request.minScore()});
        KnnVectorQuery knnVectorQuery = QueryBuilders.knnVector((String)this.embeddingField, (int)request.maxResults(), (float[])request.queryEmbedding().vector()).filter(this.mapFilterToQuery(request.filter())).build();
        SearchQuery searchQuery = SearchQuery.newBuilder().query((Query)knnVectorQuery).getTotalCount(false).limit(request.maxResults()).offset(0).sort(new Sort(Collections.singletonList(new ScoreSort()))).build();
        SearchRequest searchRequest = SearchRequest.newBuilder().tableName(this.tableName).indexName(this.searchIndexName).searchQuery(searchQuery).returnAllColumns(true).build();
        SearchResponse response = this.client.search(searchRequest);
        this.log.debug("search requestId:{}", (Object)response.getRequestId());
        return this.searchResponseToEmbeddingSearchResult(request, response);
    }

    protected Query mapFilterToQuery(Filter filter) {
        return TablestoreMetadataFilterMapper.map(filter);
    }

    private EmbeddingSearchResult<TextSegment> searchResponseToEmbeddingSearchResult(EmbeddingSearchRequest request, SearchResponse response) {
        List searchHits = response.getSearchHits();
        ArrayList<EmbeddingMatch> matches = new ArrayList<EmbeddingMatch>(searchHits.size());
        for (SearchHit hit : searchHits) {
            Double score = hit.getScore();
            if (score < request.minScore()) continue;
            Row row = hit.getRow();
            String text = null;
            if (row.getLatestColumn(this.textField) != null) {
                text = row.getLatestColumn(this.textField).getValue().asString();
            }
            float[] embedding = null;
            if (row.getLatestColumn(this.embeddingField) != null) {
                String embeddingString = row.getLatestColumn(this.embeddingField).getValue().asString();
                embedding = TablestoreUtils.parseEmbeddingString(embeddingString);
            }
            Metadata metadata = this.rowToMetadata(row);
            TextSegment textSegment = null;
            if (text != null && embedding != null) {
                textSegment = new TextSegment(text, metadata);
            }
            EmbeddingMatch match = new EmbeddingMatch(score, row.getPrimaryKey().getPrimaryKeyColumn(this.pkName).getValue().asString(), new Embedding(embedding), (Object)textSegment);
            matches.add(match);
        }
        return new EmbeddingSearchResult(matches);
    }

    private void createTableIfNotExist() {
        if (this.tableExists()) {
            this.log.info("table:{} already exists", (Object)this.tableName);
            return;
        }
        TableMeta tableMeta = new TableMeta(this.tableName);
        tableMeta.addPrimaryKeyColumn(new PrimaryKeySchema(this.pkName, PrimaryKeyType.STRING));
        TableOptions tableOptions = new TableOptions(-1, 1);
        CreateTableRequest request = new CreateTableRequest(tableMeta, tableOptions);
        request.setReservedThroughput(new ReservedThroughput(new CapacityUnit(0, 0)));
        this.client.createTable(request);
        this.log.info("create table:{}", (Object)this.tableName);
    }

    private void createSearchIndexIfNotExist() {
        if (this.searchindexExists()) {
            this.log.info("index:{} already exists", (Object)this.searchIndexName);
            return;
        }
        CreateSearchIndexRequest request = new CreateSearchIndexRequest();
        request.setTableName(this.tableName);
        request.setIndexName(this.searchIndexName);
        IndexSchema indexSchema = new IndexSchema();
        indexSchema.setFieldSchemas(this.metadataSchemaList);
        request.setIndexSchema(indexSchema);
        this.client.createSearchIndex(request);
        this.log.info("create index:{}", (Object)this.searchIndexName);
    }

    protected void deleteTableAndIndex() {
        List<SearchIndexInfo> searchIndexInfos = this.listSearchIndex();
        this.deleteIndex(searchIndexInfos);
        this.deleteTable();
    }

    private boolean tableExists() {
        ListTableResponse listTableResponse = this.client.listTable();
        return listTableResponse.getTableNames().contains(this.tableName);
    }

    private boolean searchindexExists() {
        List<SearchIndexInfo> searchIndexInfos = this.listSearchIndex();
        for (SearchIndexInfo indexInfo : searchIndexInfos) {
            if (!indexInfo.getIndexName().equals(this.searchIndexName)) continue;
            return true;
        }
        return false;
    }

    private void deleteIndex(List<SearchIndexInfo> indexNames) {
        indexNames.forEach(info -> {
            DeleteSearchIndexRequest request = new DeleteSearchIndexRequest();
            request.setTableName(info.getTableName());
            request.setIndexName(info.getIndexName());
            this.client.deleteSearchIndex(request);
            this.log.info("delete table:{}, index:{}", (Object)info.getTableName(), (Object)info.getIndexName());
        });
    }

    private void deleteTable() {
        DeleteTableRequest request = new DeleteTableRequest(this.tableName);
        this.client.deleteTable(request);
        this.log.info("delete table:{}", (Object)this.tableName);
    }

    private List<SearchIndexInfo> listSearchIndex() {
        ListSearchIndexRequest request = new ListSearchIndexRequest();
        request.setTableName(this.tableName);
        ListSearchIndexResponse listSearchIndexResponse = this.client.listSearchIndex(request);
        return listSearchIndexResponse.getIndexInfos();
    }

    protected void innerAdd(String id, Embedding embedding, TextSegment textSegment) {
        ValidationUtils.ensureNotNull((Object)embedding, (String)"embedding");
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(this.pkName, PrimaryKeyValue.fromString((String)id));
        PrimaryKey primaryKey = primaryKeyBuilder.build();
        RowPutChange rowPutChange = new RowPutChange(this.tableName, primaryKey);
        String embeddinged = TablestoreUtils.embeddingToString(embedding.vector());
        rowPutChange.addColumn(new Column(this.embeddingField, ColumnValue.fromString((String)embeddinged)));
        if (textSegment != null) {
            Metadata metadata;
            String text = textSegment.text();
            if (text != null) {
                rowPutChange.addColumn(new Column(this.textField, ColumnValue.fromString((String)text)));
            }
            if ((metadata = textSegment.metadata()) != null) {
                Map map = metadata.toMap();
                for (Map.Entry entry : map.entrySet()) {
                    String key = (String)entry.getKey();
                    Object value = entry.getValue();
                    if (this.textField.equals(key)) {
                        throw Exceptions.illegalArgument((String)"there is a metadata(%s,%s) that is consistent with the name of the text field:%s", (Object[])new Object[]{key, value, this.textField});
                    }
                    if (this.embeddingField.equals(key)) {
                        throw Exceptions.illegalArgument((String)"there is a metadata(%s,%s) that is consistent with the name of the vector field:%s", (Object[])new Object[]{key, value, this.embeddingField});
                    }
                    if (value instanceof Float) {
                        rowPutChange.addColumn(new Column(key, ColumnValue.fromDouble((double)((Float)value).floatValue())));
                        continue;
                    }
                    if (value instanceof UUID) {
                        rowPutChange.addColumn(new Column(key, ColumnValue.fromString((String)((UUID)value).toString())));
                        continue;
                    }
                    rowPutChange.addColumn(new Column(key, ValueUtil.toColumnValue(value)));
                }
            }
        }
        try {
            this.client.putRow(new PutRowRequest(rowPutChange));
            if (this.log.isDebugEnabled()) {
                this.log.debug("add id:{}, textSegment:{}, embedding:{}", new Object[]{id, textSegment, TablestoreUtils.maxLogOrNull(embedding.toString())});
            }
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("add embedding data failed, id:%s, textSegment:%s,embedding:%s", id, textSegment, embedding), e);
        }
    }

    protected void innerDelete(String id) {
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(this.pkName, PrimaryKeyValue.fromString((String)id));
        PrimaryKey primaryKey = primaryKeyBuilder.build();
        RowDeleteChange rowDeleteChange = new RowDeleteChange(this.tableName, primaryKey);
        try {
            this.client.deleteRow(new DeleteRowRequest(rowDeleteChange));
            this.log.debug("delete id:{}", (Object)id);
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("delete embedding data failed, id:%s", id), e);
        }
    }

    private void forEachAllData(Collection<String> columnsToGet, Consumer<Row> rowConsumer) {
        RangeRowQueryCriteria rangeRowQueryCriteria = new RangeRowQueryCriteria(this.tableName);
        PrimaryKeyBuilder start = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        start.addPrimaryKeyColumn(this.pkName, PrimaryKeyValue.INF_MIN);
        PrimaryKeyBuilder end = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        end.addPrimaryKeyColumn(this.pkName, PrimaryKeyValue.INF_MAX);
        rangeRowQueryCriteria.setInclusiveStartPrimaryKey(start.build());
        rangeRowQueryCriteria.setExclusiveEndPrimaryKey(end.build());
        rangeRowQueryCriteria.setMaxVersions(1);
        rangeRowQueryCriteria.setLimit(5000);
        rangeRowQueryCriteria.addColumnsToGet(columnsToGet);
        rangeRowQueryCriteria.setDirection(Direction.FORWARD);
        GetRangeRequest getRangeRequest = new GetRangeRequest(rangeRowQueryCriteria);
        while (true) {
            GetRangeResponse getRangeResponse = this.client.getRange(getRangeRequest);
            for (Row row : getRangeResponse.getRows()) {
                rowConsumer.accept(row);
            }
            if (getRangeResponse.getNextStartPrimaryKey() == null) break;
            rangeRowQueryCriteria.setInclusiveStartPrimaryKey(getRangeResponse.getNextStartPrimaryKey());
        }
    }

    private Metadata rowToMetadata(Row row) {
        Metadata metadata = new Metadata();
        block5: for (Column column : row.getColumns()) {
            if (column.getName().equals(this.embeddingField) || column.getName().equals(this.textField)) continue;
            ColumnType columnType = column.getValue().getType();
            switch (columnType) {
                case STRING: {
                    metadata.put(column.getName(), column.getValue().asString());
                    continue block5;
                }
                case INTEGER: {
                    metadata.put(column.getName(), column.getValue().asLong());
                    continue block5;
                }
                case DOUBLE: {
                    metadata.put(column.getName(), column.getValue().asDouble());
                    continue block5;
                }
                default: {
                    this.log.warn("unsupported columnType:{}, key:{}, value:{}", new Object[]{columnType, column.getName(), column.getValue()});
                }
            }
        }
        return metadata;
    }
}

