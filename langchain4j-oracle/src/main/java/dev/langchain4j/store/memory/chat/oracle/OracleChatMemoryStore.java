/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ChatMessageSerializer
 *  dev.langchain4j.store.memory.chat.ChatMemoryStore
 */
package dev.langchain4j.store.memory.chat.oracle;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;
import javax.sql.DataSource;

public final class OracleChatMemoryStore
implements ChatMemoryStore {
    private static final String DEFAULT_TABLE_NAME = "CHAT_MEMORY";
    private static final String DEFAULT_MEMORY_ID_COLUMN_NAME = "MEMORY_ID";
    private static final String DEFAULT_CONTENT_COLUMN_NAME = "CONTENT";
    private static final Pattern SIMPLE_IDENTIFIER = Pattern.compile("^[A-Za-z_][A-Za-z0-9_]*$");
    private static final Pattern QUOTED_IDENTIFIER = Pattern.compile("^\"([^\"]|\"\")+\"$");
    private final DataSource dataSource;
    private final String selectSql;
    private final String mergeSql;
    private final String deleteSql;

    private OracleChatMemoryStore(Builder builder) {
        if (builder.dataSource == null) {
            throw new IllegalArgumentException("dataSource cannot be null");
        }
        this.dataSource = builder.dataSource;
        String tableName = builder.tableName == null ? DEFAULT_TABLE_NAME : OracleChatMemoryStore.validateIdentifier(builder.tableName, "tableName");
        String memoryIdColumnName = builder.memoryIdColumnName == null ? DEFAULT_MEMORY_ID_COLUMN_NAME : OracleChatMemoryStore.validateIdentifier(builder.memoryIdColumnName, "memoryIdColumnName");
        String contentColumnName = builder.contentColumnName == null ? DEFAULT_CONTENT_COLUMN_NAME : OracleChatMemoryStore.validateIdentifier(builder.contentColumnName, "contentColumnName");
        this.selectSql = "SELECT " + contentColumnName + " FROM " + tableName + " WHERE " + memoryIdColumnName + " = ?";
        this.mergeSql = "MERGE INTO " + tableName + " t USING (SELECT ? AS " + memoryIdColumnName + ", ? AS " + contentColumnName + " FROM dual) s ON (t." + memoryIdColumnName + " = s." + memoryIdColumnName + ") WHEN MATCHED THEN UPDATE SET t." + contentColumnName + " = s." + contentColumnName + " WHEN NOT MATCHED THEN INSERT (" + memoryIdColumnName + ", " + contentColumnName + ") VALUES (s." + memoryIdColumnName + ", s." + contentColumnName + ")";
        this.deleteSql = "DELETE FROM " + tableName + " WHERE " + memoryIdColumnName + " = ?";
    }

    private static String validateIdentifier(String identifier, String fieldName) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        if (SIMPLE_IDENTIFIER.matcher(identifier).matches()) {
            return identifier;
        }
        if (QUOTED_IDENTIFIER.matcher(identifier).matches()) {
            return identifier;
        }
        throw new IllegalArgumentException(fieldName + " contains unsupported characters: " + identifier);
    }

    public static Builder builder() {
        return new Builder();
    }

    /*
     * Exception decompiling
     */
    public List<ChatMessage> getMessages(Object memoryId) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 8 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        if (memoryId == null) {
            throw new IllegalArgumentException("memoryId cannot be null");
        }
        if (messages == null) {
            throw new IllegalArgumentException("messages cannot be null");
        }
        String json = ChatMessageSerializer.messagesToJson(messages);
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(this.mergeSql);){
            statement.setObject(1, memoryId);
            statement.setString(2, json);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to update messages for memoryId=" + memoryId, e);
        }
    }

    public void deleteMessages(Object memoryId) {
        if (memoryId == null) {
            throw new IllegalArgumentException("memoryId cannot be null");
        }
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(this.deleteSql);){
            statement.setObject(1, memoryId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to delete messages for memoryId=" + memoryId, e);
        }
    }

    public static final class Builder {
        private DataSource dataSource;
        private String tableName;
        private String memoryIdColumnName;
        private String contentColumnName;

        public Builder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder memoryIdColumnName(String memoryIdColumnName) {
            this.memoryIdColumnName = memoryIdColumnName;
            return this;
        }

        public Builder contentColumnName(String contentColumnName) {
            this.contentColumnName = contentColumnName;
            return this;
        }

        public OracleChatMemoryStore build() {
            return new OracleChatMemoryStore(this);
        }
    }
}

