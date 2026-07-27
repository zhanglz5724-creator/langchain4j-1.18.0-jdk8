/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.filter.comparison;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.Objects;

public class ContainsString
implements Filter {
    private final String key;
    private final String comparisonValue;

    public ContainsString(String key, String comparisonValue) {
        this.key = ValidationUtils.ensureNotBlank(key, "key");
        this.comparisonValue = ValidationUtils.ensureNotNull(comparisonValue, "comparisonValue with key '" + key + "'");
    }

    public String key() {
        return this.key;
    }

    public String comparisonValue() {
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
        if (actualValue instanceof String) {
            String str = (String)actualValue;
            return str.contains(this.comparisonValue);
        }
        throw Exceptions.illegalArgument("Type mismatch: actual value of metadata key \"%s\" (%s) has type %s, while it is expected to be a string", this.key, actualValue, actualValue.getClass().getName());
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ContainsString)) {
            return false;
        }
        ContainsString other = (ContainsString)o;
        return Objects.equals(this.key, other.key) && Objects.equals(this.comparisonValue, other.comparisonValue);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.comparisonValue);
    }

    public String toString() {
        return "ContainsString(key=" + this.key + ", comparisonValue=" + this.comparisonValue + ")";
    }
}

