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
package dev.langchain4j.store.embedding.mariadb;

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
import java.util.UUID;
import java.util.stream.Collectors;

abstract class MariaDbFilterMapper {
    MariaDbFilterMapper() {
    }

    public String map(Filter filter) {
        if (filter instanceof IsEqualTo) {
            return this.mapEqual((IsEqualTo)filter);
        }
        if (filter instanceof IsNotEqualTo) {
            return this.mapNotEqual((IsNotEqualTo)filter);
        }
        if (filter instanceof IsGreaterThan) {
            return this.mapGreaterThan((IsGreaterThan)filter);
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            return this.mapGreaterThanOrEqual((IsGreaterThanOrEqualTo)filter);
        }
        if (filter instanceof IsLessThan) {
            return this.mapLessThan((IsLessThan)filter);
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            return this.mapLessThanOrEqual((IsLessThanOrEqualTo)filter);
        }
        if (filter instanceof IsIn) {
            return this.mapIn((IsIn)filter);
        }
        if (filter instanceof IsNotIn) {
            return this.mapNotIn((IsNotIn)filter);
        }
        if (filter instanceof And) {
            return this.mapAnd((And)filter);
        }
        if (filter instanceof Not) {
            return this.mapNot((Not)filter);
        }
        if (filter instanceof Or) {
            return this.mapOr((Or)filter);
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private String mapEqual(IsEqualTo isEqualTo) {
        String key = this.formatKey(isEqualTo.key());
        return String.format("%s is not null and %s = %s", key, key, this.formatValue(isEqualTo.comparisonValue()));
    }

    private String mapNotEqual(IsNotEqualTo isNotEqualTo) {
        String key = this.formatKey(isNotEqualTo.key());
        return String.format("(%s is null or %s != %s)", key, key, this.formatValue(isNotEqualTo.comparisonValue()));
    }

    private String mapGreaterThan(IsGreaterThan isGreaterThan) {
        return String.format("%s > %s", this.formatKey(isGreaterThan.key()), this.formatValue(isGreaterThan.comparisonValue()));
    }

    private String mapGreaterThanOrEqual(IsGreaterThanOrEqualTo isGreaterThanOrEqualTo) {
        return String.format("%s >= %s", this.formatKey(isGreaterThanOrEqualTo.key()), this.formatValue(isGreaterThanOrEqualTo.comparisonValue()));
    }

    private String mapLessThan(IsLessThan isLessThan) {
        return String.format("%s < %s", this.formatKey(isLessThan.key()), this.formatValue(isLessThan.comparisonValue()));
    }

    private String mapLessThanOrEqual(IsLessThanOrEqualTo isLessThanOrEqualTo) {
        return String.format("%s <= %s", this.formatKey(isLessThanOrEqualTo.key()), this.formatValue(isLessThanOrEqualTo.comparisonValue()));
    }

    private String mapIn(IsIn isIn) {
        return String.format("%s in %s", this.formatKey(isIn.key()), this.formatValue(isIn.comparisonValues()));
    }

    private String mapNotIn(IsNotIn isNotIn) {
        String key = this.formatKey(isNotIn.key());
        return String.format("(%s is null or %s not in %s)", key, key, this.formatValue(isNotIn.comparisonValues()));
    }

    private String mapAnd(And and) {
        return String.format("%s and %s", this.map(and.left()), this.map(and.right()));
    }

    private String mapNot(Not not) {
        return String.format("not (%s)", this.map(not.expression()));
    }

    private String mapOr(Or or) {
        return String.format("(%s or %s)", this.map(or.left()), this.map(or.right()));
    }

    abstract String formatKey(String var1);

    String formatValue(Object value) {
        if (value instanceof Collection) {
            Collection vals = (Collection)value;
            return "(" + vals.stream().map(this::formatValue).collect(Collectors.joining(",")) + ")";
        }
        if (value instanceof String) {
            String stringValue = (String)value;
            String escapedValue = stringValue.replace("\\", "\\\\").replace("'", "''");
            return "'" + escapedValue + "'";
        }
        if (value instanceof UUID) {
            return "'" + value + "'";
        }
        return value.toString();
    }
}

