/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.filter.comparison;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.NumberComparator;
import dev.langchain4j.store.embedding.filter.comparison.TypeChecker;
import java.util.Objects;

public class IsGreaterThan
implements Filter {
    private final String key;
    private final Comparable<?> comparisonValue;

    public IsGreaterThan(String key, Comparable<?> comparisonValue) {
        this.key = ValidationUtils.ensureNotBlank(key, "key");
        this.comparisonValue = ValidationUtils.ensureNotNull(comparisonValue, "comparisonValue with key '" + key + "'");
    }

    public String key() {
        return this.key;
    }

    public Comparable<?> comparisonValue() {
        return this.comparisonValue;
    }

    @Override
    public boolean test(Object object) {
        if (!(object instanceof Metadata)) {
            return false;
        }
        Metadata metadata = (Metadata)object;
        if (!metadata.containsKey(this.key)) {
            return false;
        }
        Object actualValue = metadata.toMap().get(this.key);
        TypeChecker.ensureTypesAreCompatible(actualValue, this.comparisonValue, this.key);
        if (actualValue instanceof Number) {
            return NumberComparator.compareAsBigDecimals(actualValue, this.comparisonValue) > 0;
        }
        return ((Comparable)actualValue).compareTo(this.comparisonValue) > 0;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IsGreaterThan)) {
            return false;
        }
        IsGreaterThan other = (IsGreaterThan)o;
        return Objects.equals(this.key, other.key) && Objects.equals(this.comparisonValue, other.comparisonValue);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.comparisonValue);
    }

    public String toString() {
        return "IsGreaterThan(key=" + this.key + ", comparisonValue=" + this.comparisonValue + ")";
    }
}

