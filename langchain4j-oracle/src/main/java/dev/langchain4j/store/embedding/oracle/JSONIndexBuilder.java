/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 *  oracle.jdbc.OracleType
 */
package dev.langchain4j.store.embedding.oracle;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.oracle.CreateOption;
import dev.langchain4j.store.embedding.oracle.EmbeddingTable;
import dev.langchain4j.store.embedding.oracle.Index;
import dev.langchain4j.store.embedding.oracle.IndexBuilder;
import dev.langchain4j.store.embedding.oracle.SQLFilters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import oracle.jdbc.OracleType;

public class JSONIndexBuilder
extends IndexBuilder<JSONIndexBuilder> {
    private boolean isUnique;
    private boolean isBitmap;
    private CreateOption createOption = CreateOption.CREATE_IF_NOT_EXISTS;
    private final List<MetadataKey> indexExpressions = new ArrayList<MetadataKey>();

    JSONIndexBuilder() {
    }

    public JSONIndexBuilder isUnique(boolean isUnique) {
        this.isUnique = isUnique;
        return this;
    }

    public JSONIndexBuilder isBitmap(boolean isBitmap) {
        this.isBitmap = isBitmap;
        return this;
    }

    public JSONIndexBuilder key(String key, Class<?> keyType, Order order) {
        ValidationUtils.ensureNotBlank((String)key, (String)"key");
        ValidationUtils.ensureNotNull(keyType, (String)"sqlType");
        ValidationUtils.ensureNotNull((Object)((Object)order), (String)"order");
        this.indexExpressions.add(new MetadataKey(key, keyType, order));
        return this;
    }

    @Override
    public Index build() {
        return new Index(this);
    }

    @Override
    String getCreateIndexStatement(EmbeddingTable embeddingTable) {
        return "CREATE " + (this.isUnique ? " UNIQUE " : "") + (this.isBitmap ? " BITMAP " : "") + " INDEX " + (this.createOption == CreateOption.CREATE_IF_NOT_EXISTS ? " IF NOT EXISTS " : "") + this.getIndexName(embeddingTable) + " ON " + embeddingTable.name() + "(" + this.getIndexExpression(embeddingTable) + ")";
    }

    @Override
    String getIndexName(EmbeddingTable embeddingTable) {
        if (this.indexName == null) {
            this.indexName = this.buildIndexName(embeddingTable.name(), "_METADATA_" + this.indexExpressions.stream().map(metadataKey -> metadataKey.getKey().toUpperCase()).collect(Collectors.joining("_")));
        }
        return this.indexName;
    }

    private String getIndexExpression(EmbeddingTable embeddingTable) {
        return this.indexExpressions.stream().map(metadataKey -> {
            OracleType oracleType = SQLFilters.toOracleType(metadataKey.keyType);
            return embeddingTable.mapMetadataKey(metadataKey.key, oracleType) + " " + metadataKey.order;
        }).collect(Collectors.joining(","));
    }

    private class MetadataKey {
        private String key;
        private Class<?> keyType;
        private Order order;

        public MetadataKey(String key, Class<?> keyType, Order order) {
            this.key = key;
            this.keyType = keyType;
            this.order = order;
        }

        public String getKey() {
            return this.key;
        }

        public Order getOrder() {
            return this.order;
        }

        public Class<?> getKeyType() {
            return this.keyType;
        }
    }

    public static enum Order {
        ASC,
        DESC;

    }
}

