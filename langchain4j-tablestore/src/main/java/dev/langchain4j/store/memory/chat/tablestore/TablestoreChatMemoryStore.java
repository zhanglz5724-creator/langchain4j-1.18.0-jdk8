/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alicloud.openservices.tablestore.SyncClient
 *  com.alicloud.openservices.tablestore.model.CapacityUnit
 *  com.alicloud.openservices.tablestore.model.Column
 *  com.alicloud.openservices.tablestore.model.ColumnValue
 *  com.alicloud.openservices.tablestore.model.CreateTableRequest
 *  com.alicloud.openservices.tablestore.model.DeleteRowRequest
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
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ChatMessageDeserializer
 *  dev.langchain4j.data.message.ChatMessageSerializer
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.memory.chat.ChatMemoryStore
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.memory.chat.tablestore;

import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.CapacityUnit;
import com.alicloud.openservices.tablestore.model.Column;
import com.alicloud.openservices.tablestore.model.ColumnValue;
import com.alicloud.openservices.tablestore.model.CreateTableRequest;
import com.alicloud.openservices.tablestore.model.DeleteRowRequest;
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
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TablestoreChatMemoryStore
implements ChatMemoryStore {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SyncClient client;
    private final String tableName;
    private final String pkName1;
    private final String pkName2;
    private final String chatMessageFieldName;
    private static final String DEFAULT_TABLE_NAME = "langchain4j_chat_memory_store_ots_v1";
    private static final String DEFAULT_TABLE_PK_1_NAME = "memory_id";
    private static final String DEFAULT_TABLE_PK_2_NAME = "seq_no";
    private static final String DEFAULT_CHAT_MESSAGE_FIELD_NAME = "chat_message";

    public TablestoreChatMemoryStore(SyncClient client) {
        this(client, DEFAULT_TABLE_NAME, DEFAULT_TABLE_PK_1_NAME, DEFAULT_TABLE_PK_2_NAME, DEFAULT_CHAT_MESSAGE_FIELD_NAME);
    }

    public TablestoreChatMemoryStore(SyncClient client, String tableName, String pkName1, String pkName2, String chatMessageFieldName) {
        this.client = client;
        this.tableName = tableName;
        this.pkName1 = pkName1;
        this.pkName2 = pkName2;
        this.chatMessageFieldName = chatMessageFieldName;
    }

    public void init() {
        this.createTableIfNotExist();
    }

    public void clear() {
        this.forEachAllData(PrimaryKeyValue.INF_MIN, PrimaryKeyValue.INF_MAX, row -> {
            String id = row.getPrimaryKey().getPrimaryKeyColumn(this.pkName1).getValue().asString();
            long seqNo = row.getPrimaryKey().getPrimaryKeyColumn(this.pkName2).getValue().asLong();
            this.innerDelete(id, seqNo);
        });
    }

    public List<ChatMessage> getMessages(Object memoryId) {
        String memoryIdStr = this.getMemoryId(memoryId);
        this.log.debug("get messages, memoryIdStr:{}", (Object)memoryIdStr);
        ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
        this.forEachAllData(PrimaryKeyValue.fromString((String)memoryIdStr), row -> {
            Column column = row.getLatestColumn(this.chatMessageFieldName);
            if (column != null) {
                String jsonString = column.getValue().asString();
                try {
                    ChatMessage chatMessage = ChatMessageDeserializer.messageFromJson((String)jsonString);
                    messages.add(chatMessage);
                }
                catch (Exception e) {
                    String id = row.getPrimaryKey().getPrimaryKeyColumn(this.pkName1).getValue().asString();
                    long seqNo = row.getPrimaryKey().getPrimaryKeyColumn(this.pkName2).getValue().asLong();
                    throw new RuntimeException(String.format("unable to parse message body, memoryId:%s, seqNo:%s", id, seqNo), e);
                }
            }
        });
        return messages;
    }

    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String memoryIdStr = this.getMemoryId(memoryId);
        this.log.debug("update messages, memoryIdStr:{}", (Object)memoryIdStr);
        ValidationUtils.ensureNotEmpty(messages, (String)"messages");
        this.deleteMessages(memoryId);
        ArrayList<Exception> exceptions = new ArrayList<Exception>();
        for (int i = 0; i < messages.size(); ++i) {
            ChatMessage message = messages.get(i);
            try {
                this.innerAdd(memoryIdStr, i, ChatMessageSerializer.messageToJson((ChatMessage)message));
                continue;
            }
            catch (Exception e) {
                exceptions.add(e);
            }
        }
        if (!exceptions.isEmpty()) {
            IllegalStateException exception = new IllegalStateException("update messages with error, failed:" + exceptions.size());
            for (Exception e : exceptions) {
                exception.addSuppressed(e);
            }
            throw exception;
        }
    }

    public void deleteMessages(Object memoryId) {
        String memoryIdStr = this.getMemoryId(memoryId);
        this.log.debug("delete messages, memoryIdStr:{}", (Object)memoryIdStr);
        this.forEachAllData(PrimaryKeyValue.fromString((String)memoryIdStr), row -> {
            String id = row.getPrimaryKey().getPrimaryKeyColumn(this.pkName1).getValue().asString();
            long seqNo = row.getPrimaryKey().getPrimaryKeyColumn(this.pkName2).getValue().asLong();
            this.innerDelete(id, seqNo);
        });
    }

    private void innerDelete(String memoryId, long seqNo) {
        ValidationUtils.ensureNotNull((Object)memoryId, (String)"memoryId");
        ValidationUtils.ensureNotNull((Object)seqNo, (String)"seqNo");
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(this.pkName1, PrimaryKeyValue.fromString((String)memoryId));
        primaryKeyBuilder.addPrimaryKeyColumn(this.pkName2, PrimaryKeyValue.fromLong((long)seqNo));
        PrimaryKey primaryKey = primaryKeyBuilder.build();
        RowDeleteChange rowDeleteChange = new RowDeleteChange(this.tableName, primaryKey);
        try {
            this.client.deleteRow(new DeleteRowRequest(rowDeleteChange));
            this.log.debug("delete memoryId:{}, seqNo:{}", (Object)memoryId, (Object)seqNo);
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("delete embedding data failed, memoryId:%s, seqNo:%s", memoryId, seqNo), e);
        }
    }

    private void innerAdd(String memoryId, int seqNo, String chatMessage) {
        ValidationUtils.ensureNotNull((Object)memoryId, (String)"memoryId");
        ValidationUtils.ensureNotNull((Object)seqNo, (String)"seqNo");
        ValidationUtils.ensureNotNull((Object)chatMessage, (String)"chatMessage");
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(this.pkName1, PrimaryKeyValue.fromString((String)memoryId));
        primaryKeyBuilder.addPrimaryKeyColumn(this.pkName2, PrimaryKeyValue.fromLong((long)seqNo));
        PrimaryKey primaryKey = primaryKeyBuilder.build();
        RowPutChange rowPutChange = new RowPutChange(this.tableName, primaryKey);
        rowPutChange.addColumn(new Column(this.chatMessageFieldName, ColumnValue.fromString((String)chatMessage)));
        try {
            this.client.putRow(new PutRowRequest(rowPutChange));
            if (this.log.isDebugEnabled()) {
                this.log.debug("add memoryId:{}, seqNo:{}, chatMessage:{}", new Object[]{memoryId, seqNo, chatMessage});
            }
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("add embedding data failed, memoryId:%s, seqNo:%s, chatMessage:%s", memoryId, seqNo, chatMessage), e);
        }
    }

    private String getMemoryId(Object memoryId) {
        boolean isNullOrEmpty;
        boolean bl = isNullOrEmpty = memoryId == null || memoryId.toString().trim().isEmpty();
        if (isNullOrEmpty) {
            throw new IllegalArgumentException("memoryId cannot be null or empty");
        }
        return memoryId.toString();
    }

    private void createTableIfNotExist() {
        if (this.tableExists()) {
            this.log.info("table:{} already exists", (Object)this.tableName);
            return;
        }
        TableMeta tableMeta = new TableMeta(this.tableName);
        tableMeta.addPrimaryKeyColumn(new PrimaryKeySchema(this.pkName1, PrimaryKeyType.STRING));
        tableMeta.addPrimaryKeyColumn(new PrimaryKeySchema(this.pkName2, PrimaryKeyType.INTEGER));
        TableOptions tableOptions = new TableOptions(-1, 1);
        CreateTableRequest request = new CreateTableRequest(tableMeta, tableOptions);
        request.setReservedThroughput(new ReservedThroughput(new CapacityUnit(0, 0)));
        this.client.createTable(request);
        this.log.info("create table:{}", (Object)this.tableName);
    }

    private boolean tableExists() {
        ListTableResponse listTableResponse = this.client.listTable();
        return listTableResponse.getTableNames().contains(this.tableName);
    }

    private void forEachAllData(PrimaryKeyValue memoryId, Consumer<Row> rowConsumer) {
        this.forEachAllData(memoryId, memoryId, rowConsumer);
    }

    private void forEachAllData(PrimaryKeyValue memoryIdStart, PrimaryKeyValue memoryIdEnd, Consumer<Row> rowConsumer) {
        RangeRowQueryCriteria rangeRowQueryCriteria = new RangeRowQueryCriteria(this.tableName);
        PrimaryKeyBuilder start = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        start.addPrimaryKeyColumn(this.pkName1, memoryIdStart);
        start.addPrimaryKeyColumn(this.pkName2, PrimaryKeyValue.INF_MIN);
        PrimaryKeyBuilder end = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        end.addPrimaryKeyColumn(this.pkName1, memoryIdEnd);
        end.addPrimaryKeyColumn(this.pkName2, PrimaryKeyValue.INF_MAX);
        rangeRowQueryCriteria.setInclusiveStartPrimaryKey(start.build());
        rangeRowQueryCriteria.setExclusiveEndPrimaryKey(end.build());
        rangeRowQueryCriteria.setMaxVersions(1);
        rangeRowQueryCriteria.setLimit(5000);
        rangeRowQueryCriteria.addColumnsToGet(Collections.singletonList(this.chatMessageFieldName));
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
}

