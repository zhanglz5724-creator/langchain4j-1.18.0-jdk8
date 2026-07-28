/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.mariadb;

import dev.langchain4j.store.embedding.mariadb.MariaDbFilterMapper;
import dev.langchain4j.store.embedding.mariadb.MariaDbValidator;

class ColumnFilterMapper
extends MariaDbFilterMapper {
    ColumnFilterMapper() {
    }

    @Override
    String formatKey(String key) {
        return MariaDbValidator.validateAndEnquoteIdentifier(key, true);
    }
}

