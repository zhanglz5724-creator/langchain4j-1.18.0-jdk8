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
import java.util.UUID;

public class IsNotEqualTo
implements Filter {
    private final String key;
    private final Object comparisonValue;

    public IsNotEqualTo(String key, Object comparisonValue) {
        this.key = ValidationUtils.ensureNotBlank(key, "key");
        this.comparisonValue = ValidationUtils.ensureNotNull(comparisonValue, "comparisonValue with key '" + key + "'");
    }

    public String key() {
        return this.key;
    }

    public Object comparisonValue() {
        return this.comparisonValue;
    }

    @Override
    public boolean test(Object object) {
        if (!(object instanceof Metadata)) {
            return false;
        }
        Metadata metadata = (Metadata)object;
        if (!metadata.containsKey(this.key)) {
            return true;
        }
        Object actualValue = metadata.toMap().get(this.key);
        TypeChecker.ensureTypesAreCompatible(actualValue, this.comparisonValue, this.key);
        if (actualValue instanceof Number) {
            return NumberComparator.compareAsBigDecimals(actualValue, this.comparisonValue) != 0;
        }
        if (this.comparisonValue instanceof UUID && actualValue instanceof String) {
            return !actualValue.equals(this.comparisonValue.toString());
        }
        return !actualValue.equals(this.comparisonValue);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IsNotEqualTo)) {
            return false;
        }
        IsNotEqualTo other = (IsNotEqualTo)o;
        return Objects.equals(this.key, other.key) && Objects.equals(this.comparisonValue, other.comparisonValue);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.comparisonValue);
    }

    public String toString() {
        return "IsNotEqualTo(key=" + this.key + ", comparisonValue=" + this.comparisonValue + ")";
    }
}

