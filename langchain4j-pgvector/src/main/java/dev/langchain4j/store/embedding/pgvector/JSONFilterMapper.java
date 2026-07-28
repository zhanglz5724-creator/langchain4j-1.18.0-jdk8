/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.pgvector;

import dev.langchain4j.store.embedding.pgvector.PgVectorFilterMapper;

class JSONFilterMapper
extends PgVectorFilterMapper {
    final String metadataColumn;

    public JSONFilterMapper(String metadataColumn) {
        this.metadataColumn = metadataColumn;
    }

    @Override
    String formatKey(String key, Class<?> valueType) {
        return String.format("(%s->>'%s')::%s", this.metadataColumn, JSONFilterMapper.escapeKey(key), SQL_TYPE_MAP.get(valueType));
    }

    @Override
    String formatKeyAsString(String key) {
        return this.metadataColumn + "->>'" + JSONFilterMapper.escapeKey(key) + "'";
    }

    private static String escapeKey(String key) {
        return key.replace("'", "''");
    }
}

