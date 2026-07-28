/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.protobuf.ListValue
 *  com.google.protobuf.Struct
 *  com.google.protobuf.Value
 *  com.google.protobuf.Value$Builder
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
package dev.langchain4j.store.embedding.pinecone;

import com.google.protobuf.ListValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
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
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PineconeMetadataFilterMapper {
    private static final Map<Class<? extends Filter>, String> ATOMIC_PREDICT_MAP = Stream.of(new AbstractMap.SimpleEntry<Class<IsEqualTo>, String>(IsEqualTo.class, "$eq"), new AbstractMap.SimpleEntry<Class<IsNotEqualTo>, String>(IsNotEqualTo.class, "$ne"), new AbstractMap.SimpleEntry<Class<IsGreaterThan>, String>(IsGreaterThan.class, "$gt"), new AbstractMap.SimpleEntry<Class<IsGreaterThanOrEqualTo>, String>(IsGreaterThanOrEqualTo.class, "$gte"), new AbstractMap.SimpleEntry<Class<IsLessThan>, String>(IsLessThan.class, "$lt"), new AbstractMap.SimpleEntry<Class<IsLessThanOrEqualTo>, String>(IsLessThanOrEqualTo.class, "$lte"), new AbstractMap.SimpleEntry<Class<IsIn>, String>(IsIn.class, "$in"), new AbstractMap.SimpleEntry<Class<IsNotIn>, String>(IsNotIn.class, "$nin")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    private static final Map<Class<? extends Filter>, String> COMBINE_PREDICT_MAP = Stream.of(new AbstractMap.SimpleEntry<Class<And>, String>(And.class, "$and"), new AbstractMap.SimpleEntry<Class<Or>, String>(Or.class, "$or")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    PineconeMetadataFilterMapper() {
    }

    static Struct map(Filter filter) {
        if (filter instanceof IsEqualTo) {
            return PineconeMetadataFilterMapper.mapEqual((IsEqualTo)filter);
        }
        if (filter instanceof IsNotEqualTo) {
            return PineconeMetadataFilterMapper.mapNotEqual((IsNotEqualTo)filter);
        }
        if (filter instanceof IsGreaterThan) {
            return PineconeMetadataFilterMapper.mapGreaterThan((IsGreaterThan)filter);
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            return PineconeMetadataFilterMapper.mapGreaterThanOrEqual((IsGreaterThanOrEqualTo)filter);
        }
        if (filter instanceof IsLessThan) {
            return PineconeMetadataFilterMapper.mapLessThan((IsLessThan)filter);
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            return PineconeMetadataFilterMapper.mapLessThanOrEqual((IsLessThanOrEqualTo)filter);
        }
        if (filter instanceof IsIn) {
            return PineconeMetadataFilterMapper.mapIn((IsIn)filter);
        }
        if (filter instanceof IsNotIn) {
            return PineconeMetadataFilterMapper.mapNotIn((IsNotIn)filter);
        }
        if (filter instanceof And) {
            return PineconeMetadataFilterMapper.mapAnd((And)filter);
        }
        if (filter instanceof Not) {
            return PineconeMetadataFilterMapper.mapNot((Not)filter);
        }
        if (filter instanceof Or) {
            return PineconeMetadataFilterMapper.mapOr((Or)filter);
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private static Struct mapEqual(IsEqualTo isEqualTo) {
        return PineconeMetadataFilterMapper.mapAtomicPredict(IsEqualTo.class, isEqualTo.key(), isEqualTo.comparisonValue());
    }

    private static Struct mapNotEqual(IsNotEqualTo isNotEqualTo) {
        return PineconeMetadataFilterMapper.mapAtomicPredict(IsNotEqualTo.class, isNotEqualTo.key(), isNotEqualTo.comparisonValue());
    }

    private static Struct mapGreaterThan(IsGreaterThan isGreaterThan) {
        return PineconeMetadataFilterMapper.mapAtomicPredict(IsGreaterThan.class, isGreaterThan.key(), isGreaterThan.comparisonValue());
    }

    private static Struct mapGreaterThanOrEqual(IsGreaterThanOrEqualTo isGreaterThanOrEqualTo) {
        return PineconeMetadataFilterMapper.mapAtomicPredict(IsGreaterThanOrEqualTo.class, isGreaterThanOrEqualTo.key(), isGreaterThanOrEqualTo.comparisonValue());
    }

    private static Struct mapLessThan(IsLessThan isLessThan) {
        return PineconeMetadataFilterMapper.mapAtomicPredict(IsLessThan.class, isLessThan.key(), isLessThan.comparisonValue());
    }

    private static Struct mapLessThanOrEqual(IsLessThanOrEqualTo isLessThanOrEqualTo) {
        return PineconeMetadataFilterMapper.mapAtomicPredict(IsLessThanOrEqualTo.class, isLessThanOrEqualTo.key(), isLessThanOrEqualTo.comparisonValue());
    }

    public static Struct mapIn(IsIn isIn) {
        return PineconeMetadataFilterMapper.mapAtomicPredict(IsIn.class, isIn.key(), isIn.comparisonValues());
    }

    public static Struct mapNotIn(IsNotIn isNotIn) {
        return PineconeMetadataFilterMapper.mapAtomicPredict(IsNotIn.class, isNotIn.key(), isNotIn.comparisonValues());
    }

    private static Struct mapAnd(And and) {
        return PineconeMetadataFilterMapper.mapCombinePredict(And.class, and.left(), and.right());
    }

    private static Struct mapOr(Or or) {
        return PineconeMetadataFilterMapper.mapCombinePredict(Or.class, or.left(), or.right());
    }

    private static Struct mapAtomicPredict(Class<? extends Filter> clazz, String key, Object comparisonValue) {
        return Struct.newBuilder().putFields(key, Value.newBuilder().setStructValue(Struct.newBuilder().putFields(ATOMIC_PREDICT_MAP.get(clazz), PineconeMetadataFilterMapper.getValueBuilder(comparisonValue).build()).build()).build()).build();
    }

    private static Struct mapCombinePredict(Class<? extends Filter> clazz, Filter left, Filter right) {
        return Struct.newBuilder().putFields(COMBINE_PREDICT_MAP.get(clazz), Value.newBuilder().setListValue(ListValue.newBuilder().addValues(Value.newBuilder().setStructValue(PineconeMetadataFilterMapper.map(left)).build()).addValues(Value.newBuilder().setStructValue(PineconeMetadataFilterMapper.map(right)).build()).build()).build()).build();
    }

    private static Struct mapNot(Not not) {
        Filter expression = not.expression();
        if (expression instanceof IsEqualTo) {
            expression = new IsNotEqualTo(((IsEqualTo)expression).key(), ((IsEqualTo)expression).comparisonValue());
        } else if (expression instanceof IsNotEqualTo) {
            expression = new IsEqualTo(((IsNotEqualTo)expression).key(), ((IsNotEqualTo)expression).comparisonValue());
        } else if (expression instanceof IsGreaterThan) {
            expression = new IsLessThanOrEqualTo(((IsGreaterThan)expression).key(), ((IsGreaterThan)expression).comparisonValue());
        } else if (expression instanceof IsGreaterThanOrEqualTo) {
            expression = new IsLessThan(((IsGreaterThanOrEqualTo)expression).key(), ((IsGreaterThanOrEqualTo)expression).comparisonValue());
        } else if (expression instanceof IsLessThan) {
            expression = new IsGreaterThanOrEqualTo(((IsLessThan)expression).key(), ((IsLessThan)expression).comparisonValue());
        } else if (expression instanceof IsLessThanOrEqualTo) {
            expression = new IsGreaterThan(((IsLessThanOrEqualTo)expression).key(), ((IsLessThanOrEqualTo)expression).comparisonValue());
        } else if (expression instanceof IsIn) {
            expression = new IsNotIn(((IsIn)expression).key(), ((IsIn)expression).comparisonValues());
        } else if (expression instanceof IsNotIn) {
            expression = new IsIn(((IsNotIn)expression).key(), ((IsNotIn)expression).comparisonValues());
        } else if (expression instanceof And) {
            expression = new Or(Filter.not((Filter)((And)expression).left()), Filter.not((Filter)((And)expression).right()));
        } else if (expression instanceof Or) {
            expression = new And(Filter.not((Filter)((Or)expression).left()), Filter.not((Filter)((Or)expression).right()));
        } else {
            throw new UnsupportedOperationException("Unsupported filter type: " + expression.getClass().getName());
        }
        return PineconeMetadataFilterMapper.map(expression);
    }

    private static Value.Builder getValueBuilder(Object value) {
        if (value instanceof Number) {
            return Value.newBuilder().setNumberValue(((Number)value).doubleValue());
        }
        if (value instanceof String || value instanceof UUID) {
            return Value.newBuilder().setStringValue(value.toString());
        }
        if (value instanceof Boolean) {
            return Value.newBuilder().setBoolValue(((Boolean)value).booleanValue());
        }
        if (value instanceof Collection) {
            return Value.newBuilder().setListValue(ListValue.newBuilder().addAllValues((Iterable)((Collection)value).stream().map(PineconeMetadataFilterMapper::getValueBuilder).map(Value.Builder::build).collect(Collectors.toList())).build());
        }
        throw new UnsupportedOperationException("Unsupported value type: " + value.getClass().getName());
    }
}

