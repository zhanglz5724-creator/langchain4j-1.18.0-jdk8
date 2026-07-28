/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.store.embedding.oracle;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.oracle.CreateOption;
import dev.langchain4j.store.embedding.oracle.EmbeddingTable;
import dev.langchain4j.store.embedding.oracle.Index;

abstract class IndexBuilder<T extends IndexBuilder> {
    static final int INDEX_NAME_MAX_LENGTH = 128;
    protected String indexName;
    CreateOption createOption = CreateOption.CREATE_NONE;

    IndexBuilder() {
    }

    public T createOption(CreateOption createOption) {
        ValidationUtils.ensureNotNull((Object)((Object)createOption), (String)"createOption");
        this.createOption = createOption;
        return (T)this;
    }

    public T name(String indexName) {
        this.indexName = indexName;
        return (T)this;
    }

    String buildIndexName(String tableName, String suffix) {
        boolean isQuoted;
        String unquotedTableName = tableName;
        boolean bl = isQuoted = unquotedTableName.startsWith("\"") && unquotedTableName.endsWith("\"");
        if (isQuoted) {
            unquotedTableName = this.unquoteTableName(unquotedTableName);
        }
        this.indexName = this.truncateIndexName(unquotedTableName + suffix, isQuoted);
        if (isQuoted) {
            this.indexName = "\"" + this.indexName + "\"";
        }
        return this.indexName;
    }

    private String truncateIndexName(String indexName, boolean isQuoted) {
        int maxLength;
        String truncatedIndexName = indexName;
        int n = maxLength = isQuoted ? 126 : 128;
        if (truncatedIndexName.length() > maxLength) {
            truncatedIndexName = truncatedIndexName.substring(0, maxLength);
        }
        return truncatedIndexName;
    }

    private String unquoteTableName(String tableName) {
        String unquotedTableName = tableName;
        return unquotedTableName.substring(1, unquotedTableName.length() - 1);
    }

    public abstract Index build();

    abstract String getCreateIndexStatement(EmbeddingTable var1);

    String getDropIndexStatement(EmbeddingTable embeddingTable) {
        return "DROP INDEX IF EXISTS " + this.getIndexName(embeddingTable);
    }

    abstract String getIndexName(EmbeddingTable var1);
}

