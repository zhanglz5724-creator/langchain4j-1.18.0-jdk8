/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alicloud.openservices.tablestore.model.search.query.Query
 *  com.alicloud.openservices.tablestore.model.search.query.QueryBuilder
 *  com.alicloud.openservices.tablestore.model.search.query.QueryBuilders
 *  com.alicloud.openservices.tablestore.model.search.query.TermsQuery$Builder
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
package dev.langchain4j.store.embedding.tablestore;

import com.alicloud.openservices.tablestore.model.search.query.Query;
import com.alicloud.openservices.tablestore.model.search.query.QueryBuilder;
import com.alicloud.openservices.tablestore.model.search.query.QueryBuilders;
import com.alicloud.openservices.tablestore.model.search.query.TermsQuery;
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
import dev.langchain4j.store.embedding.tablestore.IsTextMatch;
import dev.langchain4j.store.embedding.tablestore.IsTextMatchPhrase;
import java.util.UUID;

class TablestoreMetadataFilterMapper {
    TablestoreMetadataFilterMapper() {
    }

    static Query map(Filter filter) {
        if (filter == null) {
            return QueryBuilders.matchAll().build();
        }
        if (filter instanceof IsEqualTo) {
            return TablestoreMetadataFilterMapper.mapEqual((IsEqualTo)filter);
        }
        if (filter instanceof IsNotEqualTo) {
            return TablestoreMetadataFilterMapper.mapNotEqual((IsNotEqualTo)filter);
        }
        if (filter instanceof IsTextMatch) {
            return TablestoreMetadataFilterMapper.mapMatch((IsTextMatch)filter);
        }
        if (filter instanceof IsTextMatchPhrase) {
            return TablestoreMetadataFilterMapper.mapMatchPhrase((IsTextMatchPhrase)filter);
        }
        if (filter instanceof IsGreaterThan) {
            return TablestoreMetadataFilterMapper.mapGreaterThan((IsGreaterThan)filter);
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            return TablestoreMetadataFilterMapper.mapGreaterThanOrEqual((IsGreaterThanOrEqualTo)filter);
        }
        if (filter instanceof IsLessThan) {
            return TablestoreMetadataFilterMapper.mapLessThan((IsLessThan)filter);
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            return TablestoreMetadataFilterMapper.mapLessThanOrEqual((IsLessThanOrEqualTo)filter);
        }
        if (filter instanceof IsIn) {
            return TablestoreMetadataFilterMapper.mapIn((IsIn)filter);
        }
        if (filter instanceof IsNotIn) {
            return TablestoreMetadataFilterMapper.mapNotIn((IsNotIn)filter);
        }
        if (filter instanceof And) {
            return TablestoreMetadataFilterMapper.mapAnd((And)filter);
        }
        if (filter instanceof Not) {
            return TablestoreMetadataFilterMapper.mapNot((Not)filter);
        }
        if (filter instanceof Or) {
            return TablestoreMetadataFilterMapper.mapOr((Or)filter);
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private static Object transformType(Object object) {
        if (object instanceof Float) {
            object = ((Float)object).doubleValue();
        }
        if (object instanceof UUID) {
            object = ((UUID)object).toString();
        }
        return object;
    }

    private static Query mapEqual(IsEqualTo isEqualTo) {
        return QueryBuilders.term((String)isEqualTo.key(), (Object)TablestoreMetadataFilterMapper.transformType(isEqualTo.comparisonValue())).build();
    }

    private static Query mapMatch(IsTextMatch isTextMatch) {
        return QueryBuilders.match((String)isTextMatch.key(), (String)isTextMatch.comparisonValue()).build();
    }

    private static Query mapMatchPhrase(IsTextMatchPhrase isTextMatchPhrase) {
        return QueryBuilders.matchPhrase((String)isTextMatchPhrase.key(), (String)isTextMatchPhrase.comparisonValue()).build();
    }

    private static Query mapNotEqual(IsNotEqualTo isNotEqualTo) {
        return QueryBuilders.bool().mustNot((QueryBuilder)QueryBuilders.term((String)isNotEqualTo.key(), (Object)TablestoreMetadataFilterMapper.transformType(isNotEqualTo.comparisonValue()))).build();
    }

    private static Query mapGreaterThan(IsGreaterThan isGreaterThan) {
        return QueryBuilders.range((String)isGreaterThan.key()).greaterThan(TablestoreMetadataFilterMapper.transformType(isGreaterThan.comparisonValue())).build();
    }

    private static Query mapGreaterThanOrEqual(IsGreaterThanOrEqualTo isGreaterThanOrEqualTo) {
        return QueryBuilders.range((String)isGreaterThanOrEqualTo.key()).greaterThanOrEqual(TablestoreMetadataFilterMapper.transformType(isGreaterThanOrEqualTo.comparisonValue())).build();
    }

    private static Query mapLessThan(IsLessThan isLessThan) {
        return QueryBuilders.range((String)isLessThan.key()).lessThan(TablestoreMetadataFilterMapper.transformType(isLessThan.comparisonValue())).build();
    }

    private static Query mapLessThanOrEqual(IsLessThanOrEqualTo isLessThanOrEqualTo) {
        return QueryBuilders.range((String)isLessThanOrEqualTo.key()).lessThanOrEqual(TablestoreMetadataFilterMapper.transformType(isLessThanOrEqualTo.comparisonValue())).build();
    }

    private static Query mapIn(IsIn isIn) {
        TermsQuery.Builder builder = QueryBuilders.terms((String)isIn.key());
        for (Object object : isIn.comparisonValues()) {
            builder.addTerm(TablestoreMetadataFilterMapper.transformType(object));
        }
        return builder.build();
    }

    private static Query mapNotIn(IsNotIn isNotIn) {
        TermsQuery.Builder builder = QueryBuilders.terms((String)isNotIn.key());
        for (Object object : isNotIn.comparisonValues()) {
            builder.addTerm(TablestoreMetadataFilterMapper.transformType(object));
        }
        return QueryBuilders.bool().mustNot((Query)builder.build()).build();
    }

    private static Query mapAnd(And and) {
        return QueryBuilders.bool().must(TablestoreMetadataFilterMapper.map(and.left())).must(TablestoreMetadataFilterMapper.map(and.right())).build();
    }

    private static Query mapNot(Not not) {
        return QueryBuilders.bool().mustNot(TablestoreMetadataFilterMapper.map(not.expression())).build();
    }

    private static Query mapOr(Or or) {
        return QueryBuilders.bool().should(TablestoreMetadataFilterMapper.map(or.left())).should(TablestoreMetadataFilterMapper.map(or.right())).build();
    }
}

