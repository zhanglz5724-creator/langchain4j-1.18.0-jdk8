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
 *  io.qdrant.client.ConditionFactory
 *  io.qdrant.client.grpc.Common$Condition
 *  io.qdrant.client.grpc.Common$Filter
 *  io.qdrant.client.grpc.Common$Filter$Builder
 *  io.qdrant.client.grpc.Common$Range
 */
package dev.langchain4j.store.embedding.qdrant;

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
import io.qdrant.client.ConditionFactory;
import io.qdrant.client.grpc.Common;
import java.util.ArrayList;
import java.util.UUID;

class QdrantFilterConverter {
    QdrantFilterConverter() {
    }

    public static Common.Filter convertExpression(Filter expression) {
        return QdrantFilterConverter.convertOperand(expression);
    }

    private static Common.Filter convertOperand(Filter operand) {
        Common.Filter.Builder context = Common.Filter.newBuilder();
        ArrayList<Common.Condition> mustClauses = new ArrayList<Common.Condition>();
        ArrayList<Common.Condition> shouldClauses = new ArrayList<Common.Condition>();
        ArrayList<Common.Condition> mustNotClauses = new ArrayList<Common.Condition>();
        if (operand instanceof Not) {
            Not not = (Not)operand;
            mustNotClauses.add(ConditionFactory.filter((Common.Filter)QdrantFilterConverter.convertOperand(not.expression())));
        } else if (operand instanceof And) {
            And and = (And)operand;
            mustClauses.add(ConditionFactory.filter((Common.Filter)QdrantFilterConverter.convertOperand(and.left())));
            mustClauses.add(ConditionFactory.filter((Common.Filter)QdrantFilterConverter.convertOperand(and.right())));
        } else if (operand instanceof Or) {
            Or or = (Or)operand;
            shouldClauses.add(ConditionFactory.filter((Common.Filter)QdrantFilterConverter.convertOperand(or.left())));
            shouldClauses.add(ConditionFactory.filter((Common.Filter)QdrantFilterConverter.convertOperand(or.right())));
        } else {
            mustClauses.add(QdrantFilterConverter.convert(operand));
        }
        return context.addAllMust(mustClauses).addAllShould(shouldClauses).addAllMustNot(mustNotClauses).build();
    }

    private static Common.Condition convert(Filter filter) {
        if (filter instanceof ContainsString) {
            return QdrantFilterConverter.buildContainsCondition((ContainsString)filter);
        }
        if (filter instanceof IsEqualTo) {
            return QdrantFilterConverter.buildEqCondition((IsEqualTo)filter);
        }
        if (filter instanceof IsNotEqualTo) {
            return QdrantFilterConverter.buildNeCondition((IsNotEqualTo)filter);
        }
        if (filter instanceof IsGreaterThan) {
            return QdrantFilterConverter.buildGtCondition((IsGreaterThan)filter);
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            return QdrantFilterConverter.buildGteCondition((IsGreaterThanOrEqualTo)filter);
        }
        if (filter instanceof IsLessThan) {
            return QdrantFilterConverter.buildLtCondition((IsLessThan)filter);
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            return QdrantFilterConverter.buildLteCondition((IsLessThanOrEqualTo)filter);
        }
        if (filter instanceof IsIn) {
            return QdrantFilterConverter.buildInCondition((IsIn)filter);
        }
        if (filter instanceof IsNotIn) {
            return QdrantFilterConverter.buildNInCondition((IsNotIn)filter);
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private static Common.Condition buildContainsCondition(ContainsString containsString) {
        return ConditionFactory.matchText((String)containsString.key(), (String)containsString.comparisonValue());
    }

    private static Common.Condition buildEqCondition(IsEqualTo equalTo) {
        String key = equalTo.key();
        Object value = equalTo.comparisonValue();
        if (value instanceof String || value instanceof UUID) {
            return ConditionFactory.matchKeyword((String)key, (String)value.toString());
        }
        if (value instanceof Boolean) {
            return ConditionFactory.match((String)key, (boolean)((Boolean)value));
        }
        if (value instanceof Integer || value instanceof Long) {
            long lValue = Long.parseLong(value.toString());
            return ConditionFactory.match((String)key, (long)lValue);
        }
        if (value instanceof Float || value instanceof Double) {
            double dValue = ((Number)value).doubleValue();
            return ConditionFactory.range((String)key, (Common.Range)Common.Range.newBuilder().setGte(dValue).setLte(dValue).build());
        }
        throw new IllegalArgumentException("Invalid value type for IsEqualTo. Can either be a String or Boolean or Integer or Long or Float or Double");
    }

    private static Common.Condition buildNeCondition(IsNotEqualTo notEqual) {
        String key = notEqual.key();
        Object value = notEqual.comparisonValue();
        if (value instanceof String || value instanceof UUID) {
            return ConditionFactory.filter((Common.Filter)Common.Filter.newBuilder().addMustNot(ConditionFactory.matchKeyword((String)key, (String)value.toString())).build());
        }
        if (value instanceof Boolean) {
            Common.Condition condition = ConditionFactory.match((String)key, (boolean)((Boolean)value));
            return ConditionFactory.filter((Common.Filter)Common.Filter.newBuilder().addMustNot(condition).build());
        }
        if (value instanceof Integer || value instanceof Long) {
            long lValue = Long.parseLong(value.toString());
            Common.Condition condition = ConditionFactory.match((String)key, (long)lValue);
            return ConditionFactory.filter((Common.Filter)Common.Filter.newBuilder().addMustNot(condition).build());
        }
        if (value instanceof Float || value instanceof Double) {
            double dValue = ((Number)value).doubleValue();
            Common.Condition condition = ConditionFactory.range((String)key, (Common.Range)Common.Range.newBuilder().setGte(dValue).setLte(dValue).build());
            return ConditionFactory.filter((Common.Filter)Common.Filter.newBuilder().addMustNot(condition).build());
        }
        throw new IllegalArgumentException("Invalid value type for IsNotEqualto. Can either be a String or Boolean or Integer or Long or Float or Double");
    }

    private static Common.Condition buildGtCondition(IsGreaterThan greaterThan) {
        String key = greaterThan.key();
        Comparable value = greaterThan.comparisonValue();
        if (value instanceof Number) {
            Double dvalue = Double.parseDouble(value.toString());
            return ConditionFactory.range((String)key, (Common.Range)Common.Range.newBuilder().setGt(dvalue.doubleValue()).build());
        }
        throw new RuntimeException("Unsupported value type for IsGreaterThan condition. Only supports Number");
    }

    private static Common.Condition buildLtCondition(IsLessThan lessThan) {
        String key = lessThan.key();
        Comparable value = lessThan.comparisonValue();
        if (value instanceof Number) {
            Double dvalue = Double.parseDouble(value.toString());
            return ConditionFactory.range((String)key, (Common.Range)Common.Range.newBuilder().setLt(dvalue.doubleValue()).build());
        }
        throw new RuntimeException("Unsupported value type for IsLessThan condition. Only supports Number");
    }

    private static Common.Condition buildGteCondition(IsGreaterThanOrEqualTo greaterThanOrEqualTo) {
        String key = greaterThanOrEqualTo.key();
        Comparable value = greaterThanOrEqualTo.comparisonValue();
        if (value instanceof Number) {
            Double dvalue = Double.parseDouble(value.toString());
            return ConditionFactory.range((String)key, (Common.Range)Common.Range.newBuilder().setGte(dvalue.doubleValue()).build());
        }
        throw new RuntimeException("Unsupported value type for IsGreaterThanOrEqualTo condition. Only supports Number");
    }

    private static Common.Condition buildLteCondition(IsLessThanOrEqualTo lessThanOrEqualTo) {
        String key = lessThanOrEqualTo.key();
        Comparable value = lessThanOrEqualTo.comparisonValue();
        if (value instanceof Number) {
            Double dvalue = Double.parseDouble(value.toString());
            return ConditionFactory.range((String)key, (Common.Range)Common.Range.newBuilder().setLte(dvalue.doubleValue()).build());
        }
        throw new RuntimeException("Unsupported value type for IsLessThanOrEqualTo condition. Only supports Number");
    }

    private static Common.Condition buildInCondition(IsIn in) {
        String key = in.key();
        ArrayList valueList = new ArrayList(in.comparisonValues());
        Object firstValue = valueList.get(0);
        if (firstValue instanceof String || firstValue instanceof UUID) {
            ArrayList<String> stringValues = new ArrayList<String>();
            for (Object valueObj : valueList) {
                stringValues.add(valueObj.toString());
            }
            return ConditionFactory.matchKeywords((String)key, stringValues);
        }
        if (firstValue instanceof Integer || firstValue instanceof Long) {
            ArrayList<Long> longValues = new ArrayList<Long>();
            for (Object valueObj : valueList) {
                Long longValue = Long.parseLong(valueObj.toString());
                longValues.add(longValue);
            }
            return ConditionFactory.matchValues((String)key, longValues);
        }
        throw new RuntimeException("Unsupported value in IsIn value list. Only supports String or Integer or Long");
    }

    private static Common.Condition buildNInCondition(IsNotIn notIn) {
        String key = notIn.key();
        ArrayList valueList = new ArrayList(notIn.comparisonValues());
        Object firstValue = valueList.get(0);
        if (firstValue instanceof String || firstValue instanceof UUID) {
            ArrayList<String> stringValues = new ArrayList<String>();
            for (Object valueObj : valueList) {
                stringValues.add(valueObj.toString());
            }
            return ConditionFactory.matchExceptKeywords((String)key, stringValues);
        }
        if (firstValue instanceof Integer || firstValue instanceof Long) {
            ArrayList<Long> longValues = new ArrayList<Long>();
            for (Object valueObj : valueList) {
                Long longValue = Long.parseLong(valueObj.toString());
                longValues.add(longValue);
            }
            return ConditionFactory.matchExceptValues((String)key, longValues);
        }
        throw new RuntimeException("Unsupported value in IsNotIn value list. Only supports String or Integer or Long");
    }
}

