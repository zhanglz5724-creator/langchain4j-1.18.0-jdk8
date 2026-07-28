/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.store.embedding.filter.Filter
 */
package dev.langchain4j.store.embedding.pgvector;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.store.embedding.filter.Filter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

interface MetadataHandler {
    public String columnDefinitionsString();

    public void createMetadataIndexes(Statement var1, String var2);

    public List<String> columnsNames();

    public String whereClause(Filter var1);

    public Metadata fromResultSet(ResultSet var1);

    public String insertClause();

    public void setMetadata(PreparedStatement var1, Integer var2, Metadata var3);
}

