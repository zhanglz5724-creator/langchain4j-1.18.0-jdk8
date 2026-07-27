/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.filter;

import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.ValidationUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@JacocoIgnoreCoverageGenerated
public class MetadataFilterBuilder {
    private final String key;

    public MetadataFilterBuilder(String key) {
        this.key = ValidationUtils.ensureNotBlank(key, "key");
    }

    public static MetadataFilterBuilder metadataKey(String key) {
        return new MetadataFilterBuilder(key);
    }

    public Filter containsString(String value) {
        return new ContainsString(this.key, value);
    }

    public Filter isEqualTo(String value) {
        return new IsEqualTo(this.key, value);
    }

    public Filter isEqualTo(UUID value) {
        return new IsEqualTo(this.key, value);
    }

    public Filter isEqualTo(int value) {
        return new IsEqualTo(this.key, value);
    }

    public Filter isEqualTo(long value) {
        return new IsEqualTo(this.key, value);
    }

    public Filter isEqualTo(float value) {
        return new IsEqualTo(this.key, Float.valueOf(value));
    }

    public Filter isEqualTo(double value) {
        return new IsEqualTo(this.key, value);
    }

    public Filter isNotEqualTo(String value) {
        return new IsNotEqualTo(this.key, value);
    }

    public Filter isNotEqualTo(UUID value) {
        return new IsNotEqualTo(this.key, value);
    }

    public Filter isNotEqualTo(int value) {
        return new IsNotEqualTo(this.key, value);
    }

    public Filter isNotEqualTo(long value) {
        return new IsNotEqualTo(this.key, value);
    }

    public Filter isNotEqualTo(float value) {
        return new IsNotEqualTo(this.key, Float.valueOf(value));
    }

    public Filter isNotEqualTo(double value) {
        return new IsNotEqualTo(this.key, value);
    }

    public Filter isGreaterThan(String value) {
        return new IsGreaterThan(this.key, (Comparable<?>)((Object)value));
    }

    public Filter isGreaterThan(int value) {
        return new IsGreaterThan(this.key, Integer.valueOf(value));
    }

    public Filter isGreaterThan(long value) {
        return new IsGreaterThan(this.key, Long.valueOf(value));
    }

    public Filter isGreaterThan(float value) {
        return new IsGreaterThan(this.key, Float.valueOf(value));
    }

    public Filter isGreaterThan(double value) {
        return new IsGreaterThan(this.key, Double.valueOf(value));
    }

    public Filter isGreaterThanOrEqualTo(String value) {
        return new IsGreaterThanOrEqualTo(this.key, (Comparable<?>)((Object)value));
    }

    public Filter isGreaterThanOrEqualTo(int value) {
        return new IsGreaterThanOrEqualTo(this.key, Integer.valueOf(value));
    }

    public Filter isGreaterThanOrEqualTo(long value) {
        return new IsGreaterThanOrEqualTo(this.key, Long.valueOf(value));
    }

    public Filter isGreaterThanOrEqualTo(float value) {
        return new IsGreaterThanOrEqualTo(this.key, Float.valueOf(value));
    }

    public Filter isGreaterThanOrEqualTo(double value) {
        return new IsGreaterThanOrEqualTo(this.key, Double.valueOf(value));
    }

    public Filter isLessThan(String value) {
        return new IsLessThan(this.key, (Comparable<?>)((Object)value));
    }

    public Filter isLessThan(int value) {
        return new IsLessThan(this.key, Integer.valueOf(value));
    }

    public Filter isLessThan(long value) {
        return new IsLessThan(this.key, Long.valueOf(value));
    }

    public Filter isLessThan(float value) {
        return new IsLessThan(this.key, Float.valueOf(value));
    }

    public Filter isLessThan(double value) {
        return new IsLessThan(this.key, Double.valueOf(value));
    }

    public Filter isLessThanOrEqualTo(String value) {
        return new IsLessThanOrEqualTo(this.key, (Comparable<?>)((Object)value));
    }

    public Filter isLessThanOrEqualTo(int value) {
        return new IsLessThanOrEqualTo(this.key, Integer.valueOf(value));
    }

    public Filter isLessThanOrEqualTo(long value) {
        return new IsLessThanOrEqualTo(this.key, Long.valueOf(value));
    }

    public Filter isLessThanOrEqualTo(float value) {
        return new IsLessThanOrEqualTo(this.key, Float.valueOf(value));
    }

    public Filter isLessThanOrEqualTo(double value) {
        return new IsLessThanOrEqualTo(this.key, Double.valueOf(value));
    }

    public Filter isBetween(String fromValue, String toValue) {
        return this.isGreaterThanOrEqualTo(fromValue).and(this.isLessThanOrEqualTo(toValue));
    }

    public Filter isBetween(int fromValue, int toValue) {
        return this.isGreaterThanOrEqualTo(fromValue).and(this.isLessThanOrEqualTo(toValue));
    }

    public Filter isBetween(long fromValue, long toValue) {
        return this.isGreaterThanOrEqualTo(fromValue).and(this.isLessThanOrEqualTo(toValue));
    }

    public Filter isBetween(float fromValue, float toValue) {
        return this.isGreaterThanOrEqualTo(fromValue).and(this.isLessThanOrEqualTo(toValue));
    }

    public Filter isBetween(double fromValue, double toValue) {
        return this.isGreaterThanOrEqualTo(fromValue).and(this.isLessThanOrEqualTo(toValue));
    }

    public Filter isIn(String ... values) {
        return new IsIn(this.key, Arrays.asList(values));
    }

    public Filter isIn(UUID ... values) {
        return new IsIn(this.key, Arrays.asList(values));
    }

    public Filter isIn(int ... values) {
        return new IsIn(this.key, Arrays.stream(values).boxed().collect(Collectors.toList()));
    }

    public Filter isIn(long ... values) {
        return new IsIn(this.key, Arrays.stream(values).boxed().collect(Collectors.toList()));
    }

    public Filter isIn(float ... values) {
        ArrayList<Float> valuesList = new ArrayList<Float>();
        for (float value : values) {
            valuesList.add(Float.valueOf(value));
        }
        return new IsIn(this.key, valuesList);
    }

    public Filter isIn(double ... values) {
        return new IsIn(this.key, Arrays.stream(values).boxed().collect(Collectors.toList()));
    }

    public Filter isIn(Collection<?> values) {
        return new IsIn(this.key, values);
    }

    public Filter isNotIn(String ... values) {
        return new IsNotIn(this.key, Arrays.asList(values));
    }

    public Filter isNotIn(UUID ... values) {
        return new IsNotIn(this.key, Arrays.asList(values));
    }

    public Filter isNotIn(int ... values) {
        return new IsNotIn(this.key, Arrays.stream(values).boxed().collect(Collectors.toList()));
    }

    public Filter isNotIn(long ... values) {
        return new IsNotIn(this.key, Arrays.stream(values).boxed().collect(Collectors.toList()));
    }

    public Filter isNotIn(float ... values) {
        ArrayList<Float> valuesList = new ArrayList<Float>();
        for (float value : values) {
            valuesList.add(Float.valueOf(value));
        }
        return new IsNotIn(this.key, valuesList);
    }

    public Filter isNotIn(double ... values) {
        return new IsNotIn(this.key, Arrays.stream(values).boxed().collect(Collectors.toList()));
    }

    public Filter isNotIn(Collection<?> values) {
        return new IsNotIn(this.key, values);
    }
}

