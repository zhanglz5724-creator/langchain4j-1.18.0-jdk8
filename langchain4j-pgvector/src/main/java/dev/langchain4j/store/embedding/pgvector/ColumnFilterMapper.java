/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.store.embedding.pgvector;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.store.embedding.pgvector.PgVectorFilterMapper;
import java.util.regex.Pattern;

class ColumnFilterMapper
extends PgVectorFilterMapper {
    private static final Pattern SAFE_IDENTIFIER = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");

    ColumnFilterMapper() {
    }

    @Override
    String formatKey(String key, Class<?> valueType) {
        return String.format("%s::%s", ColumnFilterMapper.validateIdentifier(key), SQL_TYPE_MAP.get(valueType));
    }

    @Override
    String formatKeyAsString(String key) {
        return ColumnFilterMapper.validateIdentifier(key);
    }

    private static String validateIdentifier(String key) {
        if (Utils.isNullOrBlank((String)key) || !SAFE_IDENTIFIER.matcher(key).matches()) {
            throw new IllegalArgumentException("Invalid metadata key: '" + key + "'");
        }
        return key;
    }
}

