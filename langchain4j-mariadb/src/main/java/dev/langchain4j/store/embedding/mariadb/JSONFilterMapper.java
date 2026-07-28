/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.mariadb;

import dev.langchain4j.store.embedding.mariadb.MariaDbFilterMapper;

class JSONFilterMapper
extends MariaDbFilterMapper {
    final String metadataColumn;

    public JSONFilterMapper(String metadataColumn) {
        this.metadataColumn = metadataColumn;
    }

    @Override
    String formatKey(String key) {
        String escapedKey = key.replace("\\", "\\\\").replace("'", "''");
        return "JSON_VALUE(" + this.metadataColumn + ", '$." + escapedKey + "')";
    }
}

