/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.oracle;

import java.sql.PreparedStatement;
import java.sql.SQLException;

interface SQLFilter {
    public String toSQL();

    default public String asWhereClause() {
        return " WHERE " + this.toSQL();
    }

    public int setParameters(PreparedStatement var1, int var2) throws SQLException;
}

