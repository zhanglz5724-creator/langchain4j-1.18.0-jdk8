/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.datastax.oss.driver.api.core.CqlIdentifier
 *  com.datastax.oss.driver.api.core.CqlSession
 *  com.datastax.oss.driver.api.core.CqlSessionBuilder
 *  com.datastax.oss.driver.api.core.uuid.Uuids
 *  com.dtsx.astra.sdk.cassio.CassIO
 *  com.dtsx.astra.sdk.cassio.ClusteredRecord
 *  com.dtsx.astra.sdk.cassio.ClusteredTable
 *  com.dtsx.astra.sdk.utils.AstraEnvironment
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ChatMessageDeserializer
 *  dev.langchain4j.data.message.ChatMessageSerializer
 *  dev.langchain4j.store.memory.chat.ChatMemoryStore
 *  org.jspecify.annotations.NonNull
 */
package dev.langchain4j.store.memory.chat.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.dtsx.astra.sdk.cassio.CassIO;
import com.dtsx.astra.sdk.cassio.ClusteredRecord;
import com.dtsx.astra.sdk.cassio.ClusteredTable;
import com.dtsx.astra.sdk.utils.AstraEnvironment;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;

public class CassandraChatMemoryStore
implements ChatMemoryStore {
    public static final String DEFAULT_TABLE_NAME = "message_store";
    private final ClusteredTable messageTable;

    public CassandraChatMemoryStore(CqlSession session) {
        this(session, DEFAULT_TABLE_NAME);
    }

    public CassandraChatMemoryStore(CqlSession session, String tableName) {
        this.messageTable = new ClusteredTable(session, ((CqlIdentifier)session.getKeyspace().get()).asInternal(), tableName);
    }

    public void create() {
        this.messageTable.create();
    }

    public void delete() {
        this.messageTable.delete();
    }

    public void clear() {
        this.messageTable.clear();
    }

    public CqlSession getCassandraSession() {
        return this.messageTable.getCqlSession();
    }

    public List<ChatMessage> getMessages(@NonNull Object memoryId) {
        Objects.requireNonNull(memoryId, "'memoryId' must not be null");
        List<ChatMessage> latestFirstList = this.messageTable.findPartition(this.getMemoryId(memoryId)).stream().map(this::toChatMessage).collect(Collectors.toList());
        Collections.reverse(latestFirstList);
        return latestFirstList;
    }

    public void updateMessages(@NonNull Object memoryId, @NonNull List<ChatMessage> messages) {
        Objects.requireNonNull(memoryId, "'memoryId' must not be null");
        Objects.requireNonNull(messages, "'messages' must not be null");
        this.deleteMessages(memoryId);
        this.messageTable.upsertPartition(messages.stream().map(record -> this.fromChatMessage(this.getMemoryId(memoryId), (ChatMessage)record)).collect(Collectors.toList()));
    }

    public void deleteMessages(@NonNull Object memoryId) {
        Objects.requireNonNull(memoryId, "'memoryId' must not be null");
        this.messageTable.deletePartition(this.getMemoryId(memoryId));
    }

    private ChatMessage toChatMessage(@NonNull ClusteredRecord record) {
        Objects.requireNonNull(record, "'record' must not be null");
        try {
            return ChatMessageDeserializer.messageFromJson((String)record.getBody());
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse message body", e);
        }
    }

    private ClusteredRecord fromChatMessage(@NonNull String memoryId, @NonNull ChatMessage chatMessage) {
        Objects.requireNonNull(memoryId, "'memoryId' must not be null");
        Objects.requireNonNull(chatMessage, "'chatMessage' must not be null");
        try {
            ClusteredRecord record = new ClusteredRecord();
            record.setRowId(Uuids.timeBased());
            record.setPartitionId(memoryId);
            record.setBody(ChatMessageSerializer.messageToJson((ChatMessage)chatMessage));
            return record;
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse message body", e);
        }
    }

    private String getMemoryId(Object memoryId) {
        if (!(memoryId instanceof String)) {
            throw new IllegalArgumentException("memoryId must be a String");
        }
        return (String)memoryId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static BuilderAstra builderAstra() {
        return new BuilderAstra();
    }

    public static class Builder {
        public static Integer DEFAULT_PORT = 9042;
        private List<String> contactPoints;
        private String localDataCenter;
        private Integer port = DEFAULT_PORT;
        private String userName;
        private String password;
        protected String keyspace;
        protected String table = "message_store";

        public Builder contactPoints(List<String> contactPoints) {
            this.contactPoints = contactPoints;
            return this;
        }

        public Builder localDataCenter(String localDataCenter) {
            this.localDataCenter = localDataCenter;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder keyspace(String keyspace) {
            this.keyspace = keyspace;
            return this;
        }

        public Builder table(String table) {
            this.table = table;
            return this;
        }

        public CassandraChatMemoryStore build() {
            CqlSessionBuilder builder = (CqlSessionBuilder)((CqlSessionBuilder)CqlSession.builder().withKeyspace(this.keyspace)).withLocalDatacenter(this.localDataCenter);
            if (this.userName != null && this.password != null) {
                builder.withAuthCredentials(this.userName, this.password);
            }
            this.contactPoints.forEach(cp -> builder.addContactPoint(new InetSocketAddress((String)cp, (int)this.port)));
            return new CassandraChatMemoryStore((CqlSession)builder.build(), this.table);
        }
    }

    public static class BuilderAstra {
        private String token;
        private UUID dbId;
        private String tableName = "message_store";
        private String keyspaceName = "default_keyspace";
        private String dbRegion = "us-east1";
        private AstraEnvironment env = AstraEnvironment.PROD;

        public BuilderAstra token(String token) {
            this.token = token;
            return this;
        }

        public BuilderAstra databaseId(UUID dbId) {
            this.dbId = dbId;
            return this;
        }

        public BuilderAstra env(AstraEnvironment env) {
            this.env = env;
            return this;
        }

        public BuilderAstra databaseRegion(String dbRegion) {
            this.dbRegion = dbRegion;
            return this;
        }

        public BuilderAstra keyspace(String keyspaceName) {
            this.keyspaceName = keyspaceName;
            return this;
        }

        public BuilderAstra table(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public CassandraChatMemoryStore build() {
            CqlSession cqlSession = CassIO.init((String)this.token, (UUID)this.dbId, (String)this.dbRegion, (String)this.keyspaceName, (AstraEnvironment)this.env);
            return new CassandraChatMemoryStore(cqlSession, this.tableName);
        }
    }
}

