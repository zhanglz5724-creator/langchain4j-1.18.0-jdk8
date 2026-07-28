/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.store.embedding.filter.Filter
 *  dev.langchain4j.store.embedding.filter.comparison.IsEqualTo
 *  dev.langchain4j.store.embedding.filter.comparison.IsGreaterThan
 *  dev.langchain4j.store.embedding.filter.comparison.IsGreaterThanOrEqualTo
 *  dev.langchain4j.store.embedding.filter.comparison.IsIn
 *  dev.langchain4j.store.embedding.filter.comparison.IsLessThan
 *  dev.langchain4j.store.embedding.filter.comparison.IsLessThanOrEqualTo
 *  dev.langchain4j.store.embedding.filter.comparison.IsNotEqualTo
 *  dev.langchain4j.store.embedding.filter.comparison.IsNotIn
 *  dev.langchain4j.store.embedding.filter.logical.And
 *  dev.langchain4j.store.embedding.filter.logical.Not
 *  dev.langchain4j.store.embedding.filter.logical.Or
 *  org.opensearch.client.json.JsonData
 *  org.opensearch.client.opensearch._types.FieldValue
 *  org.opensearch.client.opensearch._types.query_dsl.BoolQuery
 *  org.opensearch.client.opensearch._types.query_dsl.BoolQuery$Builder
 *  org.opensearch.client.opensearch._types.query_dsl.Query
 *  org.opensearch.client.opensearch._types.query_dsl.Query$Builder
 */
package dev.langchain4j.store.embedding.opensearch;

import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import dev.langchain4j.store.embedding.filter.comparison.IsGreaterThan;
import dev.langchain4j.store.embedding.filter.comparison.IsGreaterThanOrEqualTo;
import dev.langchain4j.store.embedding.filter.comparison.IsIn;
import dev.langchain4j.store.embedding.filter.comparison.IsLessThan;
import dev.langchain4j.store.embedding.filter.comparison.IsLessThanOrEqualTo;
import dev.langchain4j.store.embedding.filter.comparison.IsNotEqualTo;
import dev.langchain4j.store.embedding.filter.comparison.IsNotIn;
import dev.langchain4j.store.embedding.filter.logical.And;
import dev.langchain4j.store.embedding.filter.logical.Not;
import dev.langchain4j.store.embedding.filter.logical.Or;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch._types.query_dsl.Query;

class OpenSearchMetadataFilterMapper {
    private static final String METADATA_PREFIX = "metadata.";
    private static final String KEYWORD_SUFFIX = ".keyword";

    private OpenSearchMetadataFilterMapper() {
    }

    static Query map(Filter filter) {
        if (filter instanceof IsEqualTo) {
            return OpenSearchMetadataFilterMapper.mapEqual((IsEqualTo)filter);
        }
        if (filter instanceof IsNotEqualTo) {
            return OpenSearchMetadataFilterMapper.mapNotEqual((IsNotEqualTo)filter);
        }
        if (filter instanceof IsGreaterThan) {
            return OpenSearchMetadataFilterMapper.mapGreaterThan((IsGreaterThan)filter);
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            return OpenSearchMetadataFilterMapper.mapGreaterThanOrEqual((IsGreaterThanOrEqualTo)filter);
        }
        if (filter instanceof IsLessThan) {
            return OpenSearchMetadataFilterMapper.mapLessThan((IsLessThan)filter);
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            return OpenSearchMetadataFilterMapper.mapLessThanOrEqual((IsLessThanOrEqualTo)filter);
        }
        if (filter instanceof IsIn) {
            return OpenSearchMetadataFilterMapper.mapIn((IsIn)filter);
        }
        if (filter instanceof IsNotIn) {
            return OpenSearchMetadataFilterMapper.mapNotIn((IsNotIn)filter);
        }
        if (filter instanceof And) {
            return OpenSearchMetadataFilterMapper.mapAnd((And)filter);
        }
        if (filter instanceof Not) {
            return OpenSearchMetadataFilterMapper.mapNot((Not)filter);
        }
        if (filter instanceof Or) {
            return OpenSearchMetadataFilterMapper.mapOr((Or)filter);
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private static Query mapEqual(IsEqualTo isEqualTo) {
        return (Query)new Query.Builder().bool(b -> b.filter(f -> f.term(t -> t.field(OpenSearchMetadataFilterMapper.formatKey(isEqualTo.key(), isEqualTo.comparisonValue())).value(OpenSearchMetadataFilterMapper.toFieldValue(isEqualTo.comparisonValue()))))).build();
    }

    private static Query mapNotEqual(IsNotEqualTo isNotEqualTo) {
        return (Query)new Query.Builder().bool(b -> b.mustNot(mn -> mn.term(t -> t.field(OpenSearchMetadataFilterMapper.formatKey(isNotEqualTo.key(), isNotEqualTo.comparisonValue())).value(OpenSearchMetadataFilterMapper.toFieldValue(isNotEqualTo.comparisonValue()))))).build();
    }

    private static Query mapGreaterThan(IsGreaterThan isGreaterThan) {
        return (Query)new Query.Builder().bool(b -> b.filter(f -> f.range(r -> r.field(METADATA_PREFIX + isGreaterThan.key()).gt(JsonData.of((Object)isGreaterThan.comparisonValue()))))).build();
    }

    private static Query mapGreaterThanOrEqual(IsGreaterThanOrEqualTo isGreaterThanOrEqualTo) {
        return (Query)new Query.Builder().bool(b -> b.filter(f -> f.range(r -> r.field(METADATA_PREFIX + isGreaterThanOrEqualTo.key()).gte(JsonData.of((Object)isGreaterThanOrEqualTo.comparisonValue()))))).build();
    }

    private static Query mapLessThan(IsLessThan isLessThan) {
        return (Query)new Query.Builder().bool(b -> b.filter(f -> f.range(r -> r.field(METADATA_PREFIX + isLessThan.key()).lt(JsonData.of((Object)isLessThan.comparisonValue()))))).build();
    }

    private static Query mapLessThanOrEqual(IsLessThanOrEqualTo isLessThanOrEqualTo) {
        return (Query)new Query.Builder().bool(b -> b.filter(f -> f.range(r -> r.field(METADATA_PREFIX + isLessThanOrEqualTo.key()).lte(JsonData.of((Object)isLessThanOrEqualTo.comparisonValue()))))).build();
    }

    public static Query mapIn(IsIn isIn) {
        return (Query)new Query.Builder().bool(b -> b.filter(f -> f.terms(t -> t.field(OpenSearchMetadataFilterMapper.formatKey(isIn.key(), isIn.comparisonValues())).terms(terms -> {
            List values = isIn.comparisonValues().stream().map(OpenSearchMetadataFilterMapper::toFieldValue).collect(Collectors.toList());
            return terms.value(values);
        })))).build();
    }

    public static Query mapNotIn(IsNotIn isNotIn) {
        return (Query)new Query.Builder().bool(b -> b.mustNot(mn -> mn.terms(t -> t.field(OpenSearchMetadataFilterMapper.formatKey(isNotIn.key(), isNotIn.comparisonValues())).terms(terms -> {
            List values = isNotIn.comparisonValues().stream().map(OpenSearchMetadataFilterMapper::toFieldValue).collect(Collectors.toList());
            return terms.value(values);
        })))).build();
    }

    private static Query mapAnd(And and) {
        BoolQuery boolQuery = new BoolQuery.Builder().must(OpenSearchMetadataFilterMapper.map(and.left()), new Query[0]).must(OpenSearchMetadataFilterMapper.map(and.right()), new Query[0]).build();
        return (Query)new Query.Builder().bool(boolQuery).build();
    }

    private static Query mapNot(Not not) {
        BoolQuery boolQuery = new BoolQuery.Builder().mustNot(OpenSearchMetadataFilterMapper.map(not.expression()), new Query[0]).build();
        return (Query)new Query.Builder().bool(boolQuery).build();
    }

    private static Query mapOr(Or or) {
        BoolQuery boolQuery = new BoolQuery.Builder().should(OpenSearchMetadataFilterMapper.map(or.left()), new Query[0]).should(OpenSearchMetadataFilterMapper.map(or.right()), new Query[0]).build();
        return (Query)new Query.Builder().bool(boolQuery).build();
    }

    private static FieldValue toFieldValue(Object value) {
        if (value instanceof String) {
            return FieldValue.of((String)((String)value));
        }
        if (value instanceof Long) {
            return FieldValue.of((long)((Long)value));
        }
        if (value instanceof Integer) {
            return FieldValue.of((long)((Integer)value).intValue());
        }
        if (value instanceof Double) {
            return FieldValue.of((double)((Double)value));
        }
        if (value instanceof Float) {
            return FieldValue.of((double)((Float)value).doubleValue());
        }
        if (value instanceof Boolean) {
            return FieldValue.of((boolean)((Boolean)value));
        }
        return FieldValue.of((String)value.toString());
    }

    private static String formatKey(String key, Object comparisonValue) {
        if (comparisonValue instanceof String || comparisonValue instanceof UUID) {
            return METADATA_PREFIX + key + KEYWORD_SUFFIX;
        }
        return METADATA_PREFIX + key;
    }

    private static String formatKey(String key, Collection<?> comparisonValues) {
        return OpenSearchMetadataFilterMapper.formatKey(key, comparisonValues.iterator().next());
    }
}

