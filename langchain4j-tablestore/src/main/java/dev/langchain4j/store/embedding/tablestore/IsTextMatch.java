/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.filter.Filter
 */
package dev.langchain4j.store.embedding.tablestore;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.Objects;
import java.util.StringJoiner;

public class IsTextMatch
implements Filter {
    private final String key;
    private final String comparisonValue;

    public IsTextMatch(String key, String comparisonValue) {
        this.key = ValidationUtils.ensureNotBlank((String)key, (String)"key");
        this.comparisonValue = (String)ValidationUtils.ensureNotNull((Object)comparisonValue, (String)("comparisonValue with key '" + key + "'"));
    }

    public String key() {
        return this.key;
    }

    public String comparisonValue() {
        return this.comparisonValue;
    }

    public boolean test(Object object) {
        throw new UnsupportedOperationException("only used in search filters");
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IsTextMatch)) {
            return false;
        }
        IsTextMatch that = (IsTextMatch)o;
        return Objects.equals(this.key, that.key) && Objects.equals(this.comparisonValue, that.comparisonValue);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.comparisonValue);
    }

    public String toString() {
        return new StringJoiner(", ", IsTextMatch.class.getSimpleName() + "[", "]").add("key='" + this.key + "'").add("comparisonValue='" + this.comparisonValue + "'").toString();
    }
}

