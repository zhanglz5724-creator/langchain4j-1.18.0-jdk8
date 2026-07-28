/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  org.hibernate.dialect.CockroachDialect
 *  org.hibernate.dialect.DB2Dialect
 *  org.hibernate.dialect.Dialect
 *  org.hibernate.dialect.HANADialect
 *  org.hibernate.dialect.MariaDBDialect
 *  org.hibernate.dialect.MySQLDialect
 *  org.hibernate.dialect.OracleDialect
 *  org.hibernate.dialect.PostgreSQLDialect
 *  org.hibernate.dialect.SQLServerDialect
 */
package dev.langchain4j.store.embedding.hibernate;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.store.embedding.hibernate.DatabaseKindImpl;
import dev.langchain4j.store.embedding.hibernate.DistanceFunction;
import org.hibernate.dialect.CockroachDialect;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.HANADialect;
import org.hibernate.dialect.MariaDBDialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.OracleDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SQLServerDialect;

public interface DatabaseKind {
    public static final DatabaseKind DB2 = new DatabaseKindImpl("jdbc:db2://{host}:{port}/{database}", (distanceFunction, indexType, table, embeddingColumn, indexOptions) -> {
        String distanceMetric = switch (distanceFunction) {
            default -> throw new IncompatibleClassChangeError();
            case DistanceFunction.COSINE -> "cosine";
            case DistanceFunction.EUCLIDEAN -> "euclidean";
            case DistanceFunction.EUCLIDEAN_SQUARED -> "euclidean_squared";
            case DistanceFunction.MANHATTAN -> "manhattan";
            case DistanceFunction.HAMMING -> "hamming";
            case DistanceFunction.JACCARD -> "jaccard";
            case DistanceFunction.INNER_PRODUCT, DistanceFunction.NEGATIVE_INNER_PRODUCT -> "dot";
        };
        return "create vector index " + table + "_index on " + table + "(" + embeddingColumn + ") with distance " + distanceMetric + " " + (String)Utils.getOrDefault((Object)indexOptions, (Object)"") + ";";
    });
    public static final DatabaseKind MARIADB = new DatabaseKindImpl("jdbc:mariadb://{host}:{port}/{database}", (distanceFunction, indexType, table, embeddingColumn, indexOptions) -> {
        String distanceMethod = switch (distanceFunction) {
            case DistanceFunction.COSINE -> "cosine";
            case DistanceFunction.EUCLIDEAN, DistanceFunction.EUCLIDEAN_SQUARED -> "euclidean";
            default -> throw new IllegalArgumentException("MariaDB does not support the distance function: " + distanceFunction);
        };
        return "create vector index if not exists " + table + "_index on " + table + "(" + embeddingColumn + ") distance=" + distanceMethod + " " + (String)Utils.getOrDefault((Object)indexOptions, (Object)"") + ";";
    });
    public static final DatabaseKind MSSQL = new DatabaseKindImpl("jdbc:sqlserver://{host}:{port};databaseName={database};sendTimeAsDatetime=false;trustServerCertificate=true", (distanceFunction, indexType, table, embeddingColumn, indexOptions) -> {
        String distanceMetric = switch (distanceFunction) {
            case DistanceFunction.COSINE -> "'cosine'";
            case DistanceFunction.EUCLIDEAN, DistanceFunction.EUCLIDEAN_SQUARED -> "'euclidean'";
            case DistanceFunction.INNER_PRODUCT, DistanceFunction.NEGATIVE_INNER_PRODUCT -> "'dot'";
            default -> throw new IllegalArgumentException("SQL Server does not support the distance function: " + distanceFunction);
        };
        return "create vector index " + table + "_index on " + table + "(" + embeddingColumn + ") with (metric=" + distanceMetric + (String)(Utils.isNullOrBlank((String)indexOptions) ? "" : "," + indexOptions) + ");";
    });
    public static final DatabaseKind MYSQL = new DatabaseKindImpl("jdbc:mysql://{host}:{port}/{database}?allowPublicKeyRetrieval=true", (distanceFunction, indexType, table, embeddingColumn, indexOptions) -> null);
    public static final DatabaseKind POSTGRESQL = new DatabaseKindImpl("jdbc:postgresql://{host}:{port}/{database}", (distanceFunction, indexType, table, embeddingColumn, indexOptions) -> {
        String vectorOps = switch (distanceFunction) {
            default -> throw new IncompatibleClassChangeError();
            case DistanceFunction.COSINE -> "vector_cosine_ops";
            case DistanceFunction.EUCLIDEAN, DistanceFunction.EUCLIDEAN_SQUARED -> "vector_l2_ops";
            case DistanceFunction.MANHATTAN -> "vector_l1_ops";
            case DistanceFunction.HAMMING -> "vector_hamming_ops";
            case DistanceFunction.JACCARD -> "vector_jaccard_ops";
            case DistanceFunction.INNER_PRODUCT, DistanceFunction.NEGATIVE_INNER_PRODUCT -> "vector_ip_ops";
        };
        String indexMethod = indexType == null ? "ivfflat" : indexType;
        return "create index if not exists " + table + "_" + indexMethod + "_index on " + table + " using " + indexMethod + "(" + embeddingColumn + " " + vectorOps + ") with (" + (String)Utils.getOrDefault((Object)indexOptions, (Object)"") + ");";
    }, "create extension if not exists vector;");
    public static final DatabaseKind COCKROACHDB = new DatabaseKindImpl("jdbc:postgresql://{host}:{port}/{database}", (distanceFunction, indexType, table, embeddingColumn, indexOptions) -> {
        String vectorOps = switch (distanceFunction) {
            case DistanceFunction.COSINE -> "vector_cosine_ops";
            case DistanceFunction.EUCLIDEAN, DistanceFunction.EUCLIDEAN_SQUARED -> "vector_l2_ops";
            case DistanceFunction.INNER_PRODUCT, DistanceFunction.NEGATIVE_INNER_PRODUCT -> "vector_ip_ops";
            default -> throw new IllegalArgumentException("CockroachDB does not support the distance function: " + distanceFunction);
        };
        String indexMethod = indexType == null ? "ivfflat" : indexType;
        return "create vector index if not exists " + table + "_" + indexMethod + "_index on " + table + "(" + embeddingColumn + " " + vectorOps + ") with (" + (String)Utils.getOrDefault((Object)indexOptions, (Object)"") + ");";
    }, "set cluster setting feature.vector_index.enabled = true;");
    public static final DatabaseKind ORACLE = new DatabaseKindImpl("jdbc:oracle:thin:@{host}:{port}/{database}", (distanceFunction, indexType, table, embeddingColumn, indexOptions) -> {
        String distanceMetric = switch (distanceFunction) {
            default -> throw new IncompatibleClassChangeError();
            case DistanceFunction.COSINE -> "cosine";
            case DistanceFunction.EUCLIDEAN -> "euclidean";
            case DistanceFunction.EUCLIDEAN_SQUARED -> "euclidean_squared";
            case DistanceFunction.MANHATTAN -> "manhattan";
            case DistanceFunction.HAMMING -> "hamming";
            case DistanceFunction.JACCARD -> "jaccard";
            case DistanceFunction.INNER_PRODUCT, DistanceFunction.NEGATIVE_INNER_PRODUCT -> "dot";
        };
        return "create vector index if not exists " + table + "_index on " + table + "(" + embeddingColumn + ") organization neighbor partitions with distance " + distanceMetric + " " + (String)Utils.getOrDefault((Object)indexOptions, (Object)"") + ";";
    });
    public static final DatabaseKind HANA = new DatabaseKindImpl("jdbc:sap://{host}:{port}", (distanceFunction, indexType, table, embeddingColumn, indexOptions) -> {
        String distanceMetric = switch (distanceFunction) {
            case DistanceFunction.COSINE -> "cosine_similarity";
            case DistanceFunction.EUCLIDEAN -> "l2distance";
            default -> throw new IllegalArgumentException("SAP HANA does not support the distance function: " + distanceFunction);
        };
        return "create hnsw vector index " + table + "_index on " + table + "(" + embeddingColumn + ") similarity function " + distanceMetric + " " + (String)Utils.getOrDefault((Object)indexOptions, (Object)"") + ";";
    });

    public String createJdbcUrl(String var1, int var2, String var3);

    public boolean isJdbcUrl(String var1);

    public String createIndexDDL(DistanceFunction var1, String var2, String var3, String var4, String var5);

    public String getSetupSql();

    public static DatabaseKind determineDatabaseKind(String jdbcUrl) {
        if (DB2.isJdbcUrl(jdbcUrl)) {
            return DB2;
        }
        if (MARIADB.isJdbcUrl(jdbcUrl)) {
            return MARIADB;
        }
        if (MSSQL.isJdbcUrl(jdbcUrl)) {
            return MSSQL;
        }
        if (MYSQL.isJdbcUrl(jdbcUrl)) {
            return MYSQL;
        }
        if (POSTGRESQL.isJdbcUrl(jdbcUrl)) {
            return POSTGRESQL;
        }
        if (COCKROACHDB.isJdbcUrl(jdbcUrl)) {
            return COCKROACHDB;
        }
        if (ORACLE.isJdbcUrl(jdbcUrl)) {
            return ORACLE;
        }
        if (HANA.isJdbcUrl(jdbcUrl)) {
            return HANA;
        }
        return null;
    }

    public static DatabaseKind determineDatabaseKind(Dialect dialect) {
        if (dialect instanceof DB2Dialect) {
            return DB2;
        }
        if (dialect instanceof MariaDBDialect) {
            return MARIADB;
        }
        if (dialect instanceof SQLServerDialect) {
            return MSSQL;
        }
        if (dialect instanceof MySQLDialect) {
            return MYSQL;
        }
        if (dialect instanceof PostgreSQLDialect) {
            return POSTGRESQL;
        }
        if (dialect instanceof CockroachDialect) {
            return COCKROACHDB;
        }
        if (dialect instanceof OracleDialect) {
            return ORACLE;
        }
        if (dialect instanceof HANADialect) {
            return HANA;
        }
        return null;
    }
}

