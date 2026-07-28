/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.store.embedding.filter.Filter
 *  dev.langchain4j.store.embedding.filter.comparison.ContainsString
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
package dev.langchain4j.store.embedding.milvus;

import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.ContainsString;
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

class MilvusMetadataFilterMapper {
    MilvusMetadataFilterMapper() {
    }

    static String map(Filter filter, String metadataFieldName) {
        if (filter instanceof ContainsString) {
            return MilvusMetadataFilterMapper.mapContains((ContainsString)filter, metadataFieldName);
        }
        if (filter instanceof IsEqualTo) {
            return MilvusMetadataFilterMapper.mapEqual((IsEqualTo)filter, metadataFieldName);
        }
        if (filter instanceof IsNotEqualTo) {
            return MilvusMetadataFilterMapper.mapNotEqual((IsNotEqualTo)filter, metadataFieldName);
        }
        if (filter instanceof IsGreaterThan) {
            return MilvusMetadataFilterMapper.mapGreaterThan((IsGreaterThan)filter, metadataFieldName);
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            return MilvusMetadataFilterMapper.mapGreaterThanOrEqual((IsGreaterThanOrEqualTo)filter, metadataFieldName);
        }
        if (filter instanceof IsLessThan) {
            return MilvusMetadataFilterMapper.mapLessThan((IsLessThan)filter, metadataFieldName);
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            return MilvusMetadataFilterMapper.mapLessThanOrEqual((IsLessThanOrEqualTo)filter, metadataFieldName);
        }
        if (filter instanceof IsIn) {
            return MilvusMetadataFilterMapper.mapIn((IsIn)filter, metadataFieldName);
        }
        if (filter instanceof IsNotIn) {
            return MilvusMetadataFilterMapper.mapNotIn((IsNotIn)filter, metadataFieldName);
        }
        if (filter instanceof And) {
            return MilvusMetadataFilterMapper.mapAnd((And)filter, metadataFieldName);
        }
        if (filter instanceof Not) {
            return MilvusMetadataFilterMapper.mapNot((Not)filter, metadataFieldName);
        }
        if (filter instanceof Or) {
            return MilvusMetadataFilterMapper.mapOr((Or)filter, metadataFieldName);
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private static String mapContains(ContainsString containsString, String metadataFieldName) {
        return String.format("%s LIKE %s", MilvusMetadataFilterMapper.formatKey(containsString.key(), metadataFieldName), MilvusMetadataFilterMapper.formatLikePattern(containsString.comparisonValue()));
    }

    private static String formatLikePattern(String value) {
        String escaped = value.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_").replace("\"", "\\\"");
        return "\"%" + escaped + "%\"";
    }

    private static String mapEqual(IsEqualTo isEqualTo, String metadataFieldName) {
        return String.format("%s == %s", MilvusMetadataFilterMapper.formatKey(isEqualTo.key(), metadataFieldName), MilvusMetadataFilterMapper.formatValue(isEqualTo.comparisonValue()));
    }

    private static String mapNotEqual(IsNotEqualTo isNotEqualTo, String metadataFieldName) {
        return String.format("%s != %s", MilvusMetadataFilterMapper.formatKey(isNotEqualTo.key(), metadataFieldName), MilvusMetadataFilterMapper.formatValue(isNotEqualTo.comparisonValue()));
    }

    private static String mapGreaterThan(IsGreaterThan isGreaterThan, String metadataFieldName) {
        return String.format("%s > %s", MilvusMetadataFilterMapper.formatKey(isGreaterThan.key(), metadataFieldName), MilvusMetadataFilterMapper.formatValue(isGreaterThan.comparisonValue()));
    }

    private static String mapGreaterThanOrEqual(IsGreaterThanOrEqualTo isGreaterThanOrEqualTo, String metadataFieldName) {
        return String.format("%s >= %s", MilvusMetadataFilterMapper.formatKey(isGreaterThanOrEqualTo.key(), metadataFieldName), MilvusMetadataFilterMapper.formatValue(isGreaterThanOrEqualTo.comparisonValue()));
    }

    private static String mapLessThan(IsLessThan isLessThan, String metadataFieldName) {
        return String.format("%s < %s", MilvusMetadataFilterMapper.formatKey(isLessThan.key(), metadataFieldName), MilvusMetadataFilterMapper.formatValue(isLessThan.comparisonValue()));
    }

    private static String mapLessThanOrEqual(IsLessThanOrEqualTo isLessThanOrEqualTo, String metadataFieldName) {
        return String.format("%s <= %s", MilvusMetadataFilterMapper.formatKey(isLessThanOrEqualTo.key(), metadataFieldName), MilvusMetadataFilterMapper.formatValue(isLessThanOrEqualTo.comparisonValue()));
    }

    private static String mapIn(IsIn isIn, String metadataFieldName) {
        return String.format("%s in %s", MilvusMetadataFilterMapper.formatKey(isIn.key(), metadataFieldName), MilvusMetadataFilterMapper.formatValues(isIn.comparisonValues()));
    }

    private static String mapNotIn(IsNotIn isNotIn, String metadataFieldName) {
        return String.format("%s not in %s", MilvusMetadataFilterMapper.formatKey(isNotIn.key(), metadataFieldName), MilvusMetadataFilterMapper.formatValues(isNotIn.comparisonValues()));
    }

    private static String mapAnd(And and, String metadataFieldName) {
        return String.format("%s and %s", MilvusMetadataFilterMapper.map(and.left(), metadataFieldName), MilvusMetadataFilterMapper.map(and.right(), metadataFieldName));
    }

    private static String mapNot(Not not, String metadataFieldName) {
        return String.format("not(%s)", MilvusMetadataFilterMapper.map(not.expression(), metadataFieldName));
    }

    private static String mapOr(Or or, String metadataFieldName) {
        return String.format("(%s or %s)", MilvusMetadataFilterMapper.map(or.left(), metadataFieldName), MilvusMetadataFilterMapper.map(or.right(), metadataFieldName));
    }

    private static String formatKey(String key, String metadataFieldName) {
        return metadataFieldName + "[\"" + key + "\"]";
    }

    private static String formatValue(Object value) {
        if (value instanceof String) {
            String stringValue = (String)value;
            String escapedValue = stringValue.replace("\\", "\\\\").replace("\"", "\\\"");
            return "\"" + escapedValue + "\"";
        }
        if (value instanceof UUID) {
            return "\"" + value + "\"";
        }
        return value.toString();
    }

    protected static List<String> formatValues(Collection<?> values) {
        return values.stream().map(MilvusMetadataFilterMapper::formatValue).collect(Collectors.toList());
    }
}

