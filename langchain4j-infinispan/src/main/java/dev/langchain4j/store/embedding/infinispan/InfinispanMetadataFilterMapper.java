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
 */
package dev.langchain4j.store.embedding.infinispan;

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
import java.util.Optional;
import java.util.stream.Collectors;

class InfinispanMetadataFilterMapper {
    private int i = -1;

    InfinispanMetadataFilterMapper() {
    }

    FilterResult map(Filter filter) {
        String filterQuery = "";
        if (filter == null) {
            return null;
        }
        if (filter instanceof IsEqualTo) {
            ++this.i;
            filterQuery = this.mapEqual((IsEqualTo)filter);
        } else if (filter instanceof IsNotEqualTo) {
            ++this.i;
            filterQuery = this.mapNotEqual((IsNotEqualTo)filter);
        } else if (filter instanceof IsGreaterThan) {
            ++this.i;
            filterQuery = this.mapGreaterThan((IsGreaterThan)filter);
        } else if (filter instanceof IsGreaterThanOrEqualTo) {
            ++this.i;
            filterQuery = this.mapGreaterThanOrEqual((IsGreaterThanOrEqualTo)filter);
        } else if (filter instanceof IsLessThan) {
            ++this.i;
            filterQuery = this.mapLessThan((IsLessThan)filter);
        } else if (filter instanceof IsLessThanOrEqualTo) {
            ++this.i;
            filterQuery = this.mapLessThanOrEqual((IsLessThanOrEqualTo)filter);
        } else if (filter instanceof IsIn) {
            ++this.i;
            filterQuery = this.mapIn((IsIn)filter);
        } else if (filter instanceof IsNotIn) {
            ++this.i;
            filterQuery = this.mapNotIn((IsNotIn)filter);
        } else if (filter instanceof And) {
            filterQuery = this.mapAnd((And)filter);
        } else if (filter instanceof Not) {
            filterQuery = this.mapNot((Not)filter);
        } else if (filter instanceof Or) {
            filterQuery = this.mapOr((Or)filter);
        } else {
            throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
        }
        return new FilterResult(filterQuery);
    }

    private String mapEqual(IsEqualTo filter) {
        return this.metadataKey(filter.key()) + this.computeFilter("=", filter.comparisonValue());
    }

    private String mapNotEqual(IsNotEqualTo filter) {
        return this.computeFilter("!=", filter.comparisonValue()) + this.metadataKeyLast(filter.key()) + this.addMetadataNullCheck();
    }

    private String mapGreaterThan(IsGreaterThan filter) {
        return this.metadataKey(filter.key()) + this.computeFilter(">", filter.comparisonValue());
    }

    private String mapGreaterThanOrEqual(IsGreaterThanOrEqualTo filter) {
        return this.metadataKey(filter.key()) + this.computeFilter(">=", filter.comparisonValue());
    }

    private String mapLessThan(IsLessThan filter) {
        return this.metadataKey(filter.key()) + this.computeFilter("<", filter.comparisonValue());
    }

    private String mapLessThanOrEqual(IsLessThanOrEqualTo filter) {
        return this.metadataKey(filter.key()) + this.computeFilter("<=", filter.comparisonValue());
    }

    private String mapIn(IsIn filter) {
        Optional first = filter.comparisonValues().stream().findFirst();
        if (first.isEmpty()) {
            throw new UnsupportedOperationException("Infinispan metadata filter IN must contain values");
        }
        Object o = first.get();
        String inStatement = this.formattedComparisonValues(filter.comparisonValues(), o instanceof Number);
        String m = "m" + this.i + ".";
        String filterQuery = m + "value IN (" + inStatement + ")";
        if (o instanceof Integer || o instanceof Long) {
            filterQuery = m + "value_int IN (" + inStatement + ")";
        } else if (o instanceof Float || o instanceof Double) {
            filterQuery = m + "value_float IN (" + inStatement + ")";
        }
        return this.metadataKey(filter.key()) + filterQuery;
    }

    private String mapNotIn(IsNotIn filter) {
        Optional first = filter.comparisonValues().stream().findFirst();
        if (first.isEmpty()) {
            throw new UnsupportedOperationException("Infinispan metadata filter IN must contain values");
        }
        Object o = first.get();
        String inStatement = this.formattedComparisonValues(filter.comparisonValues(), o instanceof Number);
        String m = "m" + this.i + ".";
        String filterQuery = m + "value NOT IN (" + inStatement + ")";
        if (o instanceof Integer || o instanceof Long) {
            filterQuery = m + "value_int NOT IN (" + inStatement + ")";
        } else if (o instanceof Float || o instanceof Double) {
            filterQuery = m + "value_float NOT IN (" + inStatement + ")";
        }
        String inFilterQuery = m + "value IN (" + inStatement + ")";
        if (o instanceof Integer || o instanceof Long) {
            inFilterQuery = m + "value_int IN (" + inStatement + ")";
        } else if (o instanceof Float || o instanceof Double) {
            inFilterQuery = m + "value_float IN (" + inStatement + ")";
        }
        return "(" + filterQuery + this.metadataKeyLast(filter.key()) + ") OR (" + inFilterQuery + " and " + m + "name!='" + InfinispanMetadataFilterMapper.escape(filter.key()) + "')" + this.addMetadataNullCheck();
    }

    private String computeFilter(String operator, Object value) {
        String m = "m" + this.i + ".";
        String filterQuery = m + "value " + operator + " '" + InfinispanMetadataFilterMapper.escape(String.valueOf(value)) + "'";
        if (value instanceof Integer || value instanceof Long) {
            Long longValue = this.getLongValue(value);
            filterQuery = m + "value_int " + operator + " " + longValue;
        } else if (value instanceof Float || value instanceof Double) {
            Double doubleValue = this.getDoubleValue(value);
            filterQuery = m + "value_float " + operator + " " + doubleValue;
        }
        return filterQuery;
    }

    private String addMetadataNullCheck() {
        return " OR (i.metadata is null) ";
    }

    private Double getDoubleValue(Object value) {
        Double doubleValue = value instanceof Float ? ((Float)value).doubleValue() : ((Double)value).doubleValue();
        return doubleValue;
    }

    private Long getLongValue(Object value) {
        Long longValue = value instanceof Integer ? ((Integer)value).longValue() : ((Long)value).longValue();
        return longValue;
    }

    private String mapAnd(And filter) {
        return "((" + this.map((Filter)filter.left()).query + ") AND (" + this.map((Filter)filter.right()).query + "))";
    }

    private String mapNot(Not filter) {
        return "(NOT (" + this.map((Filter)filter.expression()).query + "))";
    }

    private String mapOr(Or filter) {
        return "((" + this.map((Filter)filter.left()).query + ") OR (" + this.map((Filter)filter.right()).query + "))";
    }

    private String metadataKeyLast(String key) {
        return " and m" + this.i + ".name='" + InfinispanMetadataFilterMapper.escape(key) + "'";
    }

    private String metadataKey(String key) {
        return "m" + this.i + ".name='" + InfinispanMetadataFilterMapper.escape(key) + "' and ";
    }

    private String formattedComparisonValues(Collection<?> comparisonValues, boolean isNumeric) {
        String inStatement = comparisonValues.stream().map((? super T s) -> isNumeric ? s.toString() : "'" + InfinispanMetadataFilterMapper.escape(String.valueOf(s)) + "'").collect(Collectors.joining(", "));
        return inStatement;
    }

    private static String escape(String s) {
        return s.replace("'", "''");
    }

    class FilterResult {
        String join;
        String query;

        public FilterResult(String query) {
            this.query = query;
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j <= InfinispanMetadataFilterMapper.this.i; ++j) {
                sb.append(" join i.metadata m").append(j);
            }
            this.join = sb.toString();
        }
    }
}

