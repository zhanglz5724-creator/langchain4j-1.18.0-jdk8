/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.hibernate;

import dev.langchain4j.store.embedding.hibernate.DatabaseKind;
import dev.langchain4j.store.embedding.hibernate.DistanceFunction;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

class DatabaseKindImpl
implements DatabaseKind {
    private final String[] jdbcUrlParts;
    private final TemplatePart[] templateParts;
    private final VectorIndexExporter indexExporter;
    private final String setupSql;

    public DatabaseKindImpl(String jdbcUrlTemplate, VectorIndexExporter indexExporter) {
        this(jdbcUrlTemplate, indexExporter, null);
    }

    public DatabaseKindImpl(String jdbcUrlTemplate, VectorIndexExporter indexExporter, String setupSql) {
        StringTokenizer tokenizer = new StringTokenizer(jdbcUrlTemplate, "{");
        ArrayList<String> jdbcUrlParts = new ArrayList<String>();
        ArrayList<TemplatePart> templateParts = new ArrayList<TemplatePart>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (templateParts.size() < jdbcUrlParts.size()) {
                int partEnd = token.indexOf(125);
                if (partEnd == -1) {
                    throw new IllegalArgumentException("Invalid JDBC URL template: " + jdbcUrlTemplate);
                }
                templateParts.add(TemplatePart.valueOf(token.substring(0, partEnd).toUpperCase(Locale.ROOT)));
                jdbcUrlParts.add(token.substring(partEnd + 1));
                continue;
            }
            jdbcUrlParts.add(token);
        }
        this.jdbcUrlParts = jdbcUrlParts.toArray(new String[0]);
        this.templateParts = templateParts.toArray(new TemplatePart[0]);
        this.indexExporter = indexExporter;
        this.setupSql = setupSql;
    }

    @Override
    public String createIndexDDL(DistanceFunction distanceFunction, String indexType, String table, String embeddingColumn, String indexOptions) {
        return this.indexExporter.createIndexDDL(distanceFunction, indexType, table, embeddingColumn, indexOptions);
    }

    @Override
    public String getSetupSql() {
        return this.setupSql;
    }

    @Override
    public String createJdbcUrl(String host, int port, String database) {
        StringBuilder builder = new StringBuilder();
        block5: for (int i = 0; i < this.jdbcUrlParts.length - 1; ++i) {
            builder.append(this.jdbcUrlParts[i]);
            switch (this.templateParts[i]) {
                case HOST: {
                    builder.append(host);
                    continue block5;
                }
                case PORT: {
                    builder.append(port);
                    continue block5;
                }
                case DATABASE: {
                    builder.append(database);
                }
            }
        }
        builder.append(this.jdbcUrlParts[this.jdbcUrlParts.length - 1]);
        return builder.toString();
    }

    @Override
    public boolean isJdbcUrl(String jdbcUrl) {
        return jdbcUrl.startsWith(this.jdbcUrlParts[0]);
    }

    static interface VectorIndexExporter {
        public String createIndexDDL(DistanceFunction var1, String var2, String var3, String var4, String var5);
    }

    static enum TemplatePart {
        HOST,
        PORT,
        DATABASE;

    }
}

