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
 *  oracle.jdbc.OracleType
 */
package dev.langchain4j.store.embedding.oracle;

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
import dev.langchain4j.store.embedding.oracle.SQLFilter;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import oracle.jdbc.OracleType;

final class SQLFilters {
    static final SQLFilter EMPTY = new SQLEmptyFilter();
    private static final int MAX_VARCHAR_LENGTH = 4000;
    private static final Map<Class<? extends Filter>, FilterConstructor> CONSTRUCTORS;

    private SQLFilters() {
    }

    static SQLFilter create(Filter filter, BiFunction<String, OracleType, String> keyMapper) {
        if (filter == null) {
            return EMPTY;
        }
        Class<?> filterClass = filter.getClass();
        FilterConstructor constructor = CONSTRUCTORS.get(filterClass);
        if (constructor == null) {
            throw new UnsupportedOperationException("Unsupported filter type: " + filterClass.getName());
        }
        return constructor.construct(filter, keyMapper);
    }

    private static void setObject(PreparedStatement preparedStatement, int parameterIndex, Object object, OracleType sqlType) throws SQLException {
        Object jdbcObject = SQLFilters.toJdbcObject(object);
        if (jdbcObject instanceof String && sqlType == OracleType.CLOB) {
            String string = (String)jdbcObject;
            int length = string.length();
            if (length < 4000) {
                preparedStatement.setString(parameterIndex, (String)jdbcObject);
            } else {
                preparedStatement.setCharacterStream(parameterIndex, new StringReader(string));
            }
        } else {
            preparedStatement.setObject(parameterIndex, jdbcObject, (SQLType)sqlType);
        }
    }

    private static Object toJdbcObject(Object object) {
        if (object instanceof UUID) {
            return object.toString();
        }
        return object;
    }

    static OracleType toOracleType(Object object) {
        if (object instanceof Number) {
            if (object instanceof Float) {
                return OracleType.BINARY_FLOAT;
            }
            if (object instanceof Double) {
                return OracleType.BINARY_DOUBLE;
            }
            if (object instanceof Integer || object instanceof Long) {
                return OracleType.NUMBER;
            }
            throw new IllegalArgumentException("Unexpected object class: " + object.getClass());
        }
        if (object instanceof String) {
            return OracleType.CLOB;
        }
        return OracleType.VARCHAR2;
    }

    static {
        HashMap<Class<Not>, FilterConstructor> map = new HashMap<Class<Not>, FilterConstructor>();
        map.put(IsEqualTo.class, (filter, keyMapper) -> new SQLComparisonFilter((IsEqualTo)filter, (BiFunction<String, OracleType, String>)keyMapper));
        map.put(IsNotEqualTo.class, (filter, keyMapper) -> new SQLComparisonFilter((IsNotEqualTo)filter, (BiFunction<String, OracleType, String>)keyMapper));
        map.put(IsGreaterThan.class, (filter, keyMapper) -> new SQLComparisonFilter((IsGreaterThan)filter, (BiFunction<String, OracleType, String>)keyMapper));
        map.put(IsGreaterThanOrEqualTo.class, (filter, keyMapper) -> new SQLComparisonFilter((IsGreaterThanOrEqualTo)filter, (BiFunction<String, OracleType, String>)keyMapper));
        map.put(IsLessThan.class, (filter, keyMapper) -> new SQLComparisonFilter((IsLessThan)filter, (BiFunction<String, OracleType, String>)keyMapper));
        map.put(IsLessThanOrEqualTo.class, (filter, keyMapper) -> new SQLComparisonFilter((IsLessThanOrEqualTo)filter, (BiFunction<String, OracleType, String>)keyMapper));
        map.put(IsIn.class, (filter, keyMapper) -> SQLInFilter.create((IsIn)filter, (BiFunction<String, OracleType, String>)keyMapper));
        map.put(IsNotIn.class, (filter, keyMapper) -> SQLInFilter.create((IsNotIn)filter, (BiFunction<String, OracleType, String>)keyMapper));
        map.put(And.class, (filter, keyMapper) -> new SQLLogicalFilter((And)filter, (BiFunction<String, OracleType, String>)keyMapper));
        map.put(Or.class, (filter, keyMapper) -> new SQLLogicalFilter((Or)filter, (BiFunction<String, OracleType, String>)keyMapper));
        map.put(Not.class, (filter, keyMapper) -> new SQLNot((Not)filter, keyMapper));
        CONSTRUCTORS = Collections.unmodifiableMap(map);
    }

    static interface FilterConstructor {
        public SQLFilter construct(Filter var1, BiFunction<String, OracleType, String> var2);
    }

    private static class SQLNot
    implements SQLFilter {
        private final SQLFilter expression;
        private final String sql;

        SQLNot(Not not, BiFunction<String, OracleType, String> keyMapper) {
            this(SQLFilters.create(not.expression(), keyMapper));
        }

        SQLNot(SQLFilter expression) {
            this.expression = expression;
            this.sql = "NOT(" + expression.toSQL() + ")";
        }

        @Override
        public String toSQL() {
            return this.sql;
        }

        @Override
        public int setParameters(PreparedStatement preparedStatement, int parameterIndex) throws SQLException {
            return this.expression.setParameters(preparedStatement, parameterIndex);
        }
    }

    private static class SQLLogicalFilter
    implements SQLFilter {
        private final SQLFilter left;
        private final SQLFilter right;
        private final String sql;

        SQLLogicalFilter(And and, BiFunction<String, OracleType, String> keyMapper) {
            this(and.left(), "AND", and.right(), keyMapper);
        }

        SQLLogicalFilter(Or or, BiFunction<String, OracleType, String> keyMapper) {
            this(or.left(), "OR", or.right(), keyMapper);
        }

        private SQLLogicalFilter(Filter left, String operator, Filter right, BiFunction<String, OracleType, String> keyMapper) {
            this(SQLFilters.create(left, keyMapper), operator, SQLFilters.create(right, keyMapper));
        }

        private SQLLogicalFilter(SQLFilter left, String operator, SQLFilter right) {
            this.left = left;
            this.right = right;
            this.sql = "(" + left.toSQL() + " " + operator + " " + right.toSQL() + ")";
        }

        @Override
        public String toSQL() {
            return this.sql;
        }

        @Override
        public int setParameters(PreparedStatement preparedStatement, int parameterIndex) throws SQLException {
            int leftCount = this.left.setParameters(preparedStatement, parameterIndex);
            int rightCount = this.right.setParameters(preparedStatement, parameterIndex + leftCount);
            return leftCount + rightCount;
        }
    }

    private static class SQLInFilter
    implements SQLFilter {
        private final String sql;
        private final OracleType sqlType;
        private final Collection<?> comparisonValues;

        static SQLFilter create(IsIn isIn, BiFunction<String, OracleType, String> keyMapper) {
            return SQLInFilter.create(isIn.key(), keyMapper, true, isIn.comparisonValues());
        }

        static SQLFilter create(IsNotIn isNotIn, BiFunction<String, OracleType, String> keyMapper) {
            return SQLInFilter.create(isNotIn.key(), keyMapper, false, isNotIn.comparisonValues());
        }

        static SQLFilter create(String key, BiFunction<String, OracleType, String> keyMapper, boolean isIn, Collection<?> comparisonValues) {
            OracleType sqlType;
            Set sqlTypes = comparisonValues.stream().map(SQLFilters::toOracleType).collect(Collectors.toSet());
            if (sqlTypes.size() == 1 && (sqlType = (OracleType)sqlTypes.iterator().next()) != OracleType.CLOB) {
                return new SQLInFilter(key, keyMapper, isIn, comparisonValues, sqlType);
            }
            SQLFilter orFilter = comparisonValues.stream().map(object -> new SQLComparisonFilter(key, keyMapper, "=", object, false)).reduce((left, right) -> new SQLLogicalFilter((SQLFilter)left, "OR", (SQLFilter)right)).orElse(EMPTY);
            return isIn ? orFilter : new SQLNot(orFilter);
        }

        private SQLInFilter(String key, BiFunction<String, OracleType, String> keyMapper, boolean isIn, Collection<?> comparisonValues, OracleType sqlType) {
            this.sqlType = sqlType;
            this.sql = "NVL(" + keyMapper.apply(key, sqlType) + (isIn ? " IN " : " NOT IN ") + "(" + Stream.generate(() -> "?").limit(comparisonValues.size()).collect(Collectors.joining(", ")) + "), " + !isIn + ")";
            this.comparisonValues = comparisonValues;
        }

        @Override
        public String toSQL() {
            return this.sql;
        }

        @Override
        public int setParameters(PreparedStatement preparedStatement, int parameterIndex) throws SQLException {
            int currentParameterIndex = parameterIndex;
            for (Object object : this.comparisonValues) {
                SQLFilters.setObject(preparedStatement, currentParameterIndex++, object, this.sqlType);
            }
            return this.comparisonValues.size();
        }
    }

    private static class SQLComparisonFilter
    implements SQLFilter {
        private final String sql;
        private final OracleType sqlType;
        private final Object comparisonValue;

        SQLComparisonFilter(IsEqualTo isEqualTo, BiFunction<String, OracleType, String> keyMapper) {
            this(isEqualTo.key(), keyMapper, "=", isEqualTo.comparisonValue(), false);
        }

        SQLComparisonFilter(IsNotEqualTo isNotEqualTo, BiFunction<String, OracleType, String> keyMapper) {
            this(isNotEqualTo.key(), keyMapper, "<>", isNotEqualTo.comparisonValue(), true);
        }

        SQLComparisonFilter(IsGreaterThan isGreaterThan, BiFunction<String, OracleType, String> keyMapper) {
            this(isGreaterThan.key(), keyMapper, ">", isGreaterThan.comparisonValue(), false);
        }

        SQLComparisonFilter(IsGreaterThanOrEqualTo isGreaterThanOrEqualTo, BiFunction<String, OracleType, String> keyMapper) {
            this(isGreaterThanOrEqualTo.key(), keyMapper, ">=", isGreaterThanOrEqualTo.comparisonValue(), false);
        }

        SQLComparisonFilter(IsLessThan isLessThan, BiFunction<String, OracleType, String> keyMapper) {
            this(isLessThan.key(), keyMapper, "<", isLessThan.comparisonValue(), false);
        }

        SQLComparisonFilter(IsLessThanOrEqualTo isLessThanOrEqualTo, BiFunction<String, OracleType, String> keyMapper) {
            this(isLessThanOrEqualTo.key(), keyMapper, "<=", isLessThanOrEqualTo.comparisonValue(), false);
        }

        private <T> SQLComparisonFilter(String key, BiFunction<String, OracleType, String> keyMapper, String operator, T comparisonValue, boolean isNullTrue) {
            this.sqlType = SQLFilters.toOracleType(comparisonValue);
            this.sql = this.sqlType == OracleType.CLOB ? "NVL(DBMS_LOB.COMPARE(" + keyMapper.apply(key, this.sqlType) + ", ?) " + operator + " 0, " + isNullTrue + ")" : "NVL(" + keyMapper.apply(key, this.sqlType) + " " + operator + " ?, " + isNullTrue + ")";
            this.comparisonValue = comparisonValue;
        }

        @Override
        public String toSQL() {
            return this.sql;
        }

        @Override
        public int setParameters(PreparedStatement preparedStatement, int parameterIndex) throws SQLException {
            SQLFilters.setObject(preparedStatement, parameterIndex, this.comparisonValue, this.sqlType);
            return 1;
        }
    }

    private static final class SQLEmptyFilter
    implements SQLFilter {
        private SQLEmptyFilter() {
        }

        @Override
        public String toSQL() {
            return "";
        }

        @Override
        public int setParameters(PreparedStatement preparedStatement, int parameterIndex) throws SQLException {
            return 0;
        }

        @Override
        public String asWhereClause() {
            return "";
        }
    }
}

