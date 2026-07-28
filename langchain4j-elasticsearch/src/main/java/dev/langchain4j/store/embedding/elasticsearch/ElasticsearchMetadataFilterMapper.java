/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  co.elastic.clients.elasticsearch._types.FieldValue
 *  co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery
 *  co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery$Builder
 *  co.elastic.clients.elasticsearch._types.query_dsl.Query
 *  co.elastic.clients.elasticsearch._types.query_dsl.Query$Builder
 *  co.elastic.clients.json.JsonData
 *  co.elastic.clients.util.ObjectBuilder
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
 */
package dev.langchain4j.store.embedding.elasticsearch;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
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

class ElasticsearchMetadataFilterMapper {
    ElasticsearchMetadataFilterMapper() {
    }

    static Query map(Filter filter) {
        if (filter instanceof IsEqualTo) {
            return ElasticsearchMetadataFilterMapper.mapEqual((IsEqualTo)filter);
        }
        if (filter instanceof IsNotEqualTo) {
            return ElasticsearchMetadataFilterMapper.mapNotEqual((IsNotEqualTo)filter);
        }
        if (filter instanceof IsGreaterThan) {
            return ElasticsearchMetadataFilterMapper.mapGreaterThan((IsGreaterThan)filter);
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            return ElasticsearchMetadataFilterMapper.mapGreaterThanOrEqual((IsGreaterThanOrEqualTo)filter);
        }
        if (filter instanceof IsLessThan) {
            return ElasticsearchMetadataFilterMapper.mapLessThan((IsLessThan)filter);
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            return ElasticsearchMetadataFilterMapper.mapLessThanOrEqual((IsLessThanOrEqualTo)filter);
        }
        if (filter instanceof IsIn) {
            return ElasticsearchMetadataFilterMapper.mapIn((IsIn)filter);
        }
        if (filter instanceof IsNotIn) {
            return ElasticsearchMetadataFilterMapper.mapNotIn((IsNotIn)filter);
        }
        if (filter instanceof And) {
            return ElasticsearchMetadataFilterMapper.mapAnd((And)filter);
        }
        if (filter instanceof Not) {
            return ElasticsearchMetadataFilterMapper.mapNot((Not)filter);
        }
        if (filter instanceof Or) {
            return ElasticsearchMetadataFilterMapper.mapOr((Or)filter);
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private static Query mapEqual(IsEqualTo isEqualTo) {
        return (Query)new Query.Builder().bool(b -> b.filter(f -> f.term(t -> t.field(ElasticsearchMetadataFilterMapper.formatKey(isEqualTo.key(), isEqualTo.comparisonValue())).value(v -> v.anyValue(JsonData.of((Object)isEqualTo.comparisonValue())))))).build();
    }

    private static Query mapNotEqual(IsNotEqualTo isNotEqualTo) {
        return (Query)new Query.Builder().bool(b -> b.mustNot(mn -> mn.term(t -> t.field(ElasticsearchMetadataFilterMapper.formatKey(isNotEqualTo.key(), isNotEqualTo.comparisonValue())).value(v -> v.anyValue(JsonData.of((Object)isNotEqualTo.comparisonValue())))))).build();
    }

    private static Query mapGreaterThan(IsGreaterThan isGreaterThan) {
        return (Query)new Query.Builder().bool(b -> b.filter(f -> f.range(r -> r.untyped(nf -> (ObjectBuilder)nf.field("metadata." + isGreaterThan.key()).gt((Object)JsonData.of((Object)isGreaterThan.comparisonValue())))))).build();
    }

    private static Query mapGreaterThanOrEqual(IsGreaterThanOrEqualTo isGreaterThanOrEqualTo) {
        return (Query)new Query.Builder().bool(b -> b.filter(f -> f.range(r -> r.untyped(nf -> (ObjectBuilder)nf.field("metadata." + isGreaterThanOrEqualTo.key()).gte((Object)JsonData.of((Object)isGreaterThanOrEqualTo.comparisonValue())))))).build();
    }

    private static Query mapLessThan(IsLessThan isLessThan) {
        return (Query)new Query.Builder().bool(b -> b.filter(f -> f.range(r -> r.untyped(nf -> (ObjectBuilder)nf.field("metadata." + isLessThan.key()).lt((Object)JsonData.of((Object)isLessThan.comparisonValue())))))).build();
    }

    private static Query mapLessThanOrEqual(IsLessThanOrEqualTo isLessThanOrEqualTo) {
        return (Query)new Query.Builder().bool(b -> b.filter(f -> f.range(r -> r.untyped(nf -> (ObjectBuilder)nf.field("metadata." + isLessThanOrEqualTo.key()).lte((Object)JsonData.of((Object)isLessThanOrEqualTo.comparisonValue())))))).build();
    }

    public static Query mapIn(IsIn isIn) {
        return (Query)new Query.Builder().bool(b -> b.filter(f -> f.terms(t -> t.field(ElasticsearchMetadataFilterMapper.formatKey(isIn.key(), isIn.comparisonValues())).terms(terms -> {
            List values = isIn.comparisonValues().stream().map(it -> FieldValue.of((JsonData)JsonData.of((Object)it))).collect(Collectors.toList());
            return terms.value(values);
        })))).build();
    }

    public static Query mapNotIn(IsNotIn isNotIn) {
        return (Query)new Query.Builder().bool(b -> b.mustNot(mn -> mn.terms(t -> t.field(ElasticsearchMetadataFilterMapper.formatKey(isNotIn.key(), isNotIn.comparisonValues())).terms(terms -> {
            List values = isNotIn.comparisonValues().stream().map(it -> FieldValue.of((JsonData)JsonData.of((Object)it))).collect(Collectors.toList());
            return terms.value(values);
        })))).build();
    }

    private static Query mapAnd(And and) {
        BoolQuery boolQuery = new BoolQuery.Builder().must(ElasticsearchMetadataFilterMapper.map(and.left()), new Query[0]).must(ElasticsearchMetadataFilterMapper.map(and.right()), new Query[0]).build();
        return (Query)new Query.Builder().bool(boolQuery).build();
    }

    private static Query mapNot(Not not) {
        BoolQuery boolQuery = new BoolQuery.Builder().mustNot(ElasticsearchMetadataFilterMapper.map(not.expression()), new Query[0]).build();
        return (Query)new Query.Builder().bool(boolQuery).build();
    }

    private static Query mapOr(Or or) {
        BoolQuery boolQuery = new BoolQuery.Builder().should(ElasticsearchMetadataFilterMapper.map(or.left()), new Query[0]).should(ElasticsearchMetadataFilterMapper.map(or.right()), new Query[0]).build();
        return (Query)new Query.Builder().bool(boolQuery).build();
    }

    private static String formatKey(String key, Object comparisonValue) {
        if (comparisonValue instanceof String || comparisonValue instanceof UUID) {
            return "metadata." + key + ".keyword";
        }
        return "metadata." + key;
    }

    private static String formatKey(String key, Collection<?> comparisonValues) {
        return ElasticsearchMetadataFilterMapper.formatKey(key, comparisonValues.iterator().next());
    }
}

