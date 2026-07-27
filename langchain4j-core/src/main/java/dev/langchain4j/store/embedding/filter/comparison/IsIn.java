/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.filter.comparison;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.NumberComparator;
import dev.langchain4j.store.embedding.filter.comparison.TypeChecker;
import dev.langchain4j.store.embedding.filter.comparison.UUIDComparator;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class IsIn
implements Filter {
    private final String key;
    private final Collection<?> comparisonValues;

    public IsIn(String key, Collection<?> comparisonValues) {
        this.key = ValidationUtils.ensureNotBlank(key, "key");
        HashSet copy = new HashSet(ValidationUtils.ensureNotEmpty(comparisonValues, "comparisonValues with key '" + key + "'"));
        comparisonValues.forEach(value -> ValidationUtils.ensureNotNull(value, "comparisonValue with key '" + key + "'"));
        this.comparisonValues = Collections.unmodifiableSet(copy);
    }

    public String key() {
        return this.key;
    }

    public Collection<?> comparisonValues() {
        return this.comparisonValues;
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
        TypeChecker.ensureTypesAreCompatible(actualValue, this.comparisonValues.iterator().next(), this.key);
        if (this.comparisonValues.iterator().next() instanceof Number) {
            return NumberComparator.containsAsBigDecimals(actualValue, this.comparisonValues);
        }
        if (this.comparisonValues.iterator().next() instanceof UUID) {
            return UUIDComparator.containsAsUUID(actualValue, this.comparisonValues);
        }
        return this.comparisonValues.contains(actualValue);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IsIn)) {
            return false;
        }
        IsIn other = (IsIn)o;
        return Objects.equals(this.key, other.key) && Objects.equals(this.comparisonValues, other.comparisonValues);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.comparisonValues);
    }

    public String toString() {
        return "IsIn(key=" + this.key + ", comparisonValues=" + this.comparisonValues + ")";
    }
}

